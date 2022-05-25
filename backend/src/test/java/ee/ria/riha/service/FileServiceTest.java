package ee.ria.riha.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ria.riha.TestUtils;
import ee.ria.riha.domain.FileRepository;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.InfoSystemDocumentMetadata;
import ee.ria.riha.domain.model.InfoSystemFileMetadata;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.service.auth.InfoSystemAuthorizationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    @Rule
    public CleanAuthentication cleanAuthentication;
    @Mock
    private InfoSystemRepository infoSystemRepository;
    @Mock
    private InfoSystemAuthorizationService infoSystemAuthorizationService;
    @Mock
    private FileRepository fileRepository;
    @InjectMocks
    private FileService fileService;

    private final UUID infoSystemUuid = UUID.fromString("005b04a6-c144-44af-86ed-b526afed7b3e");
    private final InfoSystem infoSystem = new InfoSystem();

    private final UUID documentUuid = UUID.fromString("d25b672a-0659-4970-9bb3-31743454528a");
    private final InfoSystemDocumentMetadata documentMetadata = new InfoSystemDocumentMetadata();

    private final UUID dataFileUuid = UUID.fromString("e98bd769-681d-45cf-9fa7-a9fdaab7ca7a");
    private final InfoSystemFileMetadata dataFileMetadata = new InfoSystemFileMetadata();

    private final Authentication authenticationToken = TestUtils.getOAuth2LoginToken(null, null);

    @Before
    public void setUp() throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        TestUtils.setActiveOrganisation(authenticationToken, JaneAuthenticationTokenBuilder.ORGANIZATION_CODE);

        infoSystem.setUuid(infoSystemUuid);
        infoSystem.setShortName("test-system");
        infoSystem.setOwnerCode(JaneAuthenticationTokenBuilder.ORGANIZATION_CODE);

        documentMetadata.setName("document");
        documentMetadata.setUrl("file://" + documentUuid);
        documentMetadata.setAccessRestricted(false);

        dataFileMetadata.setName("dataFile");
        dataFileMetadata.setUrl("file://" + dataFileUuid);

        setDocument(documentMetadata);
        setDataFile(dataFileMetadata);

        when(infoSystemAuthorizationService.isOwner(any(InfoSystem.class))).thenCallRealMethod();
        when(fileRepository.download(any(), any())).thenReturn(ResponseEntity.ok().build());
    }

    private void setDataFile(InfoSystemFileMetadata document) {
        setDocumentResource(document, "data_files");

    }

    @Test
    public void downloadsFileWhenSpecifiedInDocuments() throws IOException {
        fileService.download(infoSystem, documentUuid);

        verify(fileRepository).download(documentUuid, infoSystemUuid);
    }

    @Test
    public void downloadsFileWhenSpecifiedInDataFiles() throws IOException {
        fileService.download(infoSystem, dataFileUuid);

        verify(fileRepository).download(dataFileUuid, infoSystemUuid);
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void doesNotDownloadFilesNotInTheInfoSystemDescription() throws IOException {
        fileService.download(infoSystem, UUID.fromString("8fa1fa68-266c-4cf3-8c51-446a3feb5b56"));
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void doesNotAllowUnauthorizedUsersToDownloadRestrictedFiles() throws IOException {
        SecurityContextHolder.clearContext();

        documentMetadata.setAccessRestricted(true);
        setDocument(documentMetadata);

        fileService.download(infoSystem, documentUuid);
    }

    private void setDocument(InfoSystemFileMetadata document) {
        setDocumentResource(document, "documents");
    }

    private void setDocumentResource(InfoSystemFileMetadata document, String documents) {
        ((ArrayNode) infoSystem.getJsonContent().withArray(documents))
                .removeAll()
                .add(createDocument(document));
    }

    private ObjectNode createDocument(InfoSystemFileMetadata fileMetadata) {
        ObjectNode documentNode = JsonNodeFactory.instance.objectNode()
                .put("name", fileMetadata.getName())
                .put("url", fileMetadata.getUrl());

        if (fileMetadata instanceof InfoSystemDocumentMetadata) {
            if (((InfoSystemDocumentMetadata) fileMetadata).isAccessRestricted()) {
                documentNode.putObject("accessRestriction").put("reasonCode", 38);
            }
        }

        return documentNode;
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void doesNotAllowSimpleAuthorizedUsersToDownloadRestrictedFiles() throws IOException {
        TestUtils.setActiveOrganisation(authenticationToken, null);

        documentMetadata.setAccessRestricted(true);
        setDocument(documentMetadata);

        fileService.download(infoSystem, documentUuid);
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void doesNotAllowNonOwnersToDownloadRestrictedFiles() throws IOException {
        infoSystem.setOwnerCode("some-other-organization-code");

        documentMetadata.setAccessRestricted(true);
        setDocument(documentMetadata);

        fileService.download(infoSystem, documentUuid);
    }

}
