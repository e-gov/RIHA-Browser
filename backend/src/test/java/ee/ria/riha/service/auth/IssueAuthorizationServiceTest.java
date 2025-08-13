package ee.ria.riha.service.auth;

import ee.ria.riha.domain.model.*;
import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class IssueAuthorizationServiceTest {

    private static final Long ISSUE_ID = 123L;
    private static final UUID INFO_SYSTEM_UUID = UUID.fromString("09b3c134-0d13-41eb-a4ab-ac74f158583a");

    @Mock
    private IssueService issueService;

    @Mock
    private InfoSystemService infoSystemService;

    @Mock
    private InfoSystemAuthorizationService infoSystemAuthorizationService;

    @InjectMocks
    private IssueAuthorizationService issueAuthorizationService = new IssueAuthorizationService();

    private Issue issue = Issue.builder().id(ISSUE_ID).infoSystemUuid(INFO_SYSTEM_UUID).build();

    private InfoSystem infoSystem = new InfoSystem();

    @BeforeEach
    public void setUp() {
        infoSystem.setUuid(INFO_SYSTEM_UUID);

        when(issueService.getIssueById(ISSUE_ID)).thenReturn(issue);
        when(infoSystemService.get(INFO_SYSTEM_UUID)).thenReturn(infoSystem);
    }

    @Test
    public void isIssueOwnerResolvesIssueById() {
        issueAuthorizationService.isIssueOwner(ISSUE_ID);
        verify(issueService).getIssueById(ISSUE_ID);
    }

    @Test
    public void isIssueOwnerResolvesInfoSystemByIssueInfoSystemUuid() {
        issueAuthorizationService.isIssueOwner(ISSUE_ID);
        verify(infoSystemService).get(INFO_SYSTEM_UUID);
    }

    @Test
    public void isIssueOwnerDelegatesInfoSystemOwnerCheck() {
        issueAuthorizationService.isIssueOwner(ISSUE_ID);
        verify(infoSystemAuthorizationService).isOwner(infoSystem);
    }

}
