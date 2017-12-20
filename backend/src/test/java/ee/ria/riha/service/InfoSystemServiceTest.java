package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.storage.util.FilterRequest;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InfoSystemServiceTest {

    private static final String EXISTING_INFO_SYSTEM_SHORT_NAME = "sys1";

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    @Mock
    private InfoSystemRepository infoSystemRepository;

    @Mock
    private JsonValidationService infoSystemValidationService;

    @InjectMocks
    private InfoSystemService infoSystemService;

    private InfoSystem existingInfoSystem = new InfoSystem();
    private List<InfoSystem> foundInfoSystems = Lists.newArrayList();

    @Before
    public void setUp() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = JaneAuthenticationTokenBuilder.builder().build();
        authenticationToken.setActiveOrganization("555010203");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        existingInfoSystem.setId(2357L);
        existingInfoSystem.setShortName(EXISTING_INFO_SYSTEM_SHORT_NAME);
        existingInfoSystem.setOwnerCode("555000");
        existingInfoSystem.setCreationTimestamp("2017-10-04T14:44:45.404+03:00");
        existingInfoSystem.setUpdateTimestamp("2017-10-04T14:44:45.404+03:00");

        when(infoSystemRepository.find(any(FilterRequest.class))).thenReturn(foundInfoSystems);
        when(infoSystemRepository.load(EXISTING_INFO_SYSTEM_SHORT_NAME)).thenReturn(existingInfoSystem);
        when(infoSystemRepository.add(any(InfoSystem.class)))
                .thenAnswer((Answer<InfoSystem>) invocation -> invocation.getArgumentAt(0, InfoSystem.class));
    }

    @Test(expected = ValidationException.class)
    public void creatingInfoSystemMustFailIfSuggestedShortNameIsAlreadyInUse() {
        InfoSystem createdInfoSystem = existingInfoSystem.copy();

        // When at least one info system with updated short name is found, test should fail
        foundInfoSystems.add(createdInfoSystem);

        infoSystemService.create(createdInfoSystem);
    }

    @Test
    public void creatingInfoSystemMustSucceedIfSuggestedShortNameIsNotInUseYet() {
        InfoSystem createdInfoSystem = existingInfoSystem.copy();
        infoSystemService.create(createdInfoSystem);

        verify(infoSystemRepository).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfUpdatedShortNameIsNotInUseYet() {
        InfoSystem updatedInfoSystem = existingInfoSystem.copy();
        updatedInfoSystem.setShortName("new-short-name");

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);

        verify(infoSystemRepository).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfShortNameStaysUnchanged() {
        InfoSystem updatedInfoSystem = existingInfoSystem.copy();

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);

        verify(infoSystemRepository, never()).find(any(FilterRequest.class));
        verify(infoSystemRepository).add(any(InfoSystem.class));
    }

    @Test(expected = ValidationException.class)
    public void updatingInfoSystemMustFailIfUpdatedShortNameIsAlreadyInUse() {
        InfoSystem updatedInfoSystem = existingInfoSystem.copy();
        updatedInfoSystem.setShortName("new-short-name");

        // When at least one info system with updated short name is found, test should fail
        foundInfoSystems.add(updatedInfoSystem);

        infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, updatedInfoSystem);
    }

    @Test
    public void setsCreationAndUpdateTimestampDuringInfoSystemCreation() {
        InfoSystem model = new InfoSystem();

        InfoSystem infoSystem = infoSystemService.create(model);

        assertThat(infoSystem.getCreationTimestamp(), is(notNullValue()));
        assertThat(infoSystem.getUpdateTimestamp(), is(notNullValue()));
    }

    @Test
    public void setsNewUpdateTimestampDuringInfoSystemUpdate() {
        InfoSystem model = existingInfoSystem.copy();

        InfoSystem infoSystem = infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, model);

        assertThat(infoSystem.getUpdateTimestamp(), is(not(equalTo(existingInfoSystem.getUpdateTimestamp()))));
    }

    @Test
    public void doesNotChangeCreationTimestampDuringUpdate() {
        InfoSystem model = existingInfoSystem.copy();

        InfoSystem infoSystem = infoSystemService.update(EXISTING_INFO_SYSTEM_SHORT_NAME, model);

        assertThat(infoSystem.getCreationTimestamp(), is(equalTo(existingInfoSystem.getCreationTimestamp())));
    }
}
