package ee.ria.riha.service;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.*;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InfoSystemServiceTest {

    private static final String PERSONAL_CODE = "12345";
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "strong";
    private static final SimpleGrantedAuthority CUSTOM_ROLE = new SimpleGrantedAuthority("ROLE_TEST");
    private static final List<SimpleGrantedAuthority> WRAPPED_ROLES = Collections.singletonList(CUSTOM_ROLE);
    private static final User WRAPPED_USER_DETAILS = new User(USERNAME, PASSWORD, WRAPPED_ROLES);

    private static final String EXISTING_SHORT_NAME = "sys1";
    private static final String DISTINGUISH_SHORT_NAME = "sys2";
    private final InfoSystem infoSystemWithExistingShortName = new InfoSystem(new JSONObject("{\n\"short_name\": \"sys1\"\n}"));
    private final InfoSystem infoSystemWithDistinguishShortName = new InfoSystem(new JSONObject("{\n\"short_name\": \"sys2\"\n}"));
    private List<InfoSystem> foundInfoSystems = Lists.newArrayList();

    @Mock
    private InfoSystemRepository infoSystemRepository;

    @Mock
    private JsonValidationService infoSystemValidationService;

    @InjectMocks
    private InfoSystemService infoSystemService;

    @BeforeClass
    public static void setAuthorization() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = new RihaOrganizationAwareAuthenticationToken(
                new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE,
                        ImmutableMultimap.of(new RihaOrganization("123", "test_org"),
                                CUSTOM_ROLE)), null, null);

        authenticationToken.setActiveOrganization("123");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Before
    public void setUp() {
        when(infoSystemRepository.find(any(FilterRequest.class))).thenReturn(foundInfoSystems);
        when(infoSystemRepository.load(EXISTING_SHORT_NAME)).thenReturn(infoSystemWithExistingShortName);
        when(infoSystemRepository.load(DISTINGUISH_SHORT_NAME)).thenReturn(infoSystemWithDistinguishShortName);
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void creatingInfoSystemMustFailIfSuggestedShortNameIsAlreadyInUse() {
        foundInfoSystems.add(infoSystemWithExistingShortName);
        infoSystemService.create(infoSystemWithExistingShortName);
        verify(infoSystemRepository, times(1)).find(any(FilterRequest.class));
        verify(infoSystemRepository, times(0)).add(any(InfoSystem.class));
    }

    @Test
    public void creatingInfoSystemMustSucceedIfSuggestedShortNameIsNotInUseYet() {
        infoSystemService.create(infoSystemWithDistinguishShortName);
        verify(infoSystemRepository, times(1)).find(any(FilterRequest.class));
        verify(infoSystemRepository, times(1)).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfUpdatedShortNameIsNotInUseYet() {
        infoSystemService.update(EXISTING_SHORT_NAME, infoSystemWithDistinguishShortName);
        verify(infoSystemRepository, times(1)).find(any(FilterRequest.class));
        verify(infoSystemRepository, times(1)).add(any(InfoSystem.class));
    }

    @Test
    public void updatingInfoSystemMustSucceedIfShortNameStaysUnchanged() {
        infoSystemService.update(EXISTING_SHORT_NAME, infoSystemWithExistingShortName);
        verify(infoSystemRepository, times(0)).find(any(FilterRequest.class));
        verify(infoSystemRepository, times(1)).add(any(InfoSystem.class));
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void updatingInfoSystemMustFailIfUpdatedShortNameIsAlreadyInUse() {
        foundInfoSystems.add(infoSystemWithExistingShortName);
        infoSystemService.update(infoSystemWithDistinguishShortName.getShortName(), infoSystemWithExistingShortName);
        verify(infoSystemRepository, times(1)).find(any(FilterRequest.class));
        verify(infoSystemRepository, times(0)).add(any(InfoSystem.class));
    }
}
