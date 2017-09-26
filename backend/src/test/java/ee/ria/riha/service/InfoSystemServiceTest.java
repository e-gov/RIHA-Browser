package ee.ria.riha.service;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.FilterRequest;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InfoSystemServiceTest {

    private static final String EXISTING_INFO_SYSTEM_SHORT_NAME = "sys1";

    @Mock
    private InfoSystemRepository infoSystemRepository;

    @Mock
    private JsonValidationService infoSystemValidationService;

    @InjectMocks
    private InfoSystemService infoSystemService;

    private InfoSystem existingInfoSystem = new InfoSystem(
            "{\n" +
                    "  \"main_resource_id\": 2357,\n" +
                    "  \"short_name\": \"" + EXISTING_INFO_SYSTEM_SHORT_NAME + "\",\n" +
                    "  \"owner\": {\n" +
                    "    \"code\": \"555000\"\n" +
                    "  }\n" +
                    "}");
    private List<InfoSystem> foundInfoSystems = Lists.newArrayList();

    @BeforeClass
    public static void setAuthorization() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = new RihaOrganizationAwareAuthenticationToken(
                new RihaUserDetails(new User("john.doe", "strong", AuthorityUtils.NO_AUTHORITIES),
                                    "EE12345678901",
                                    ImmutableMultimap.of(new RihaOrganization("123", "test_org"),
                                                         new SimpleGrantedAuthority("ROLE_TEST"))),
                null,
                null);

        authenticationToken.setActiveOrganization("123");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Before
    public void setUp() {
        when(infoSystemRepository.find(any(FilterRequest.class))).thenReturn(foundInfoSystems);
        when(infoSystemRepository.load(EXISTING_INFO_SYSTEM_SHORT_NAME)).thenReturn(existingInfoSystem);
    }

    @Test(expected = ValidationException.class)
    public void creatingInfoSystemMustFailIfSuggestedShortNameIsAlreadyInUse() {
        InfoSystem createdInfoSystem = new InfoSystem(existingInfoSystem.getJsonObject());

        // When at least one info system with updated short name is found, test should fail
        foundInfoSystems.add(createdInfoSystem);

        infoSystemService.create(createdInfoSystem);
    }

    @Test
    public void creatingInfoSystemMustSucceedIfSuggestedShortNameIsNotInUseYet() {
        InfoSystem createdInfoSystem = new InfoSystem(existingInfoSystem.getJsonObject());
        infoSystemService.create(createdInfoSystem);

        verify(infoSystemRepository).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfUpdatedShortNameIsNotInUseYet() {
        InfoSystem updatedInfoSystem = new InfoSystem(existingInfoSystem.getJsonObject());
        updatedInfoSystem.setShortName("new-short-name");

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);

        verify(infoSystemRepository).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfShortNameStaysUnchanged() {
        InfoSystem updatedInfoSystem = new InfoSystem(existingInfoSystem.getJsonObject());

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);

        verify(infoSystemRepository, never()).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test(expected = ValidationException.class)
    public void updatingInfoSystemMustFailIfUpdatedShortNameIsAlreadyInUse() {
        InfoSystem updatedInfoSystem = new InfoSystem(existingInfoSystem.getJsonObject());
        updatedInfoSystem.setShortName("new-short-name");

        // When at least one info system with updated short name is found, test should fail
        foundInfoSystems.add(updatedInfoSystem);

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);
    }
}
