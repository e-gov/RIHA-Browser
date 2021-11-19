package ee.ria.riha.domain;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.ria.riha.storage.domain.MainResourceRepository;
import ee.ria.riha.storage.domain.model.MainResource;
import ee.ria.riha.storage.util.FilterRequest;
import ee.ria.riha.storage.util.Filterable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class RihaStorageInfoSystemRepositoryTest {

    @Mock
    private MainResourceRepository mainResourceRepository;

    @InjectMocks
    private RihaStorageInfoSystemRepository rihaStorageInfoSystemRepository;

    @Before
    public void setUp() {
        MainResource mainResource = new MainResource();
        mainResource.setJson_content(JsonNodeFactory.instance.objectNode());
        when(mainResourceRepository.find(any(Filterable.class)))
                .thenReturn(Collections.singletonList(mainResource));
    }

    @Test
    public void loadsInfoSystemByShortNameWhenReferenceIsNotUuid() {
        rihaStorageInfoSystemRepository.load("not-uuid");

        ArgumentCaptor<FilterRequest> filterableCaptor = ArgumentCaptor.forClass(FilterRequest.class);
        verify(mainResourceRepository).find(filterableCaptor.capture());

        assertThat(filterableCaptor.getValue().getFilter(), startsWith("short_name"));
    }

    @Test
    public void loadsInfoSystemByUuidWhenReferenceIsUuid() {
        rihaStorageInfoSystemRepository.load("6749398b-f703-4489-81df-130a5115d446");

        ArgumentCaptor<FilterRequest> filterableCaptor = ArgumentCaptor.forClass(FilterRequest.class);
        verify(mainResourceRepository).find(filterableCaptor.capture());

        assertThat(filterableCaptor.getValue().getFilter(), startsWith("uuid"));
    }

    @Test
    public void loadsInfoSystemByShortNameWhenReferenceIsShortHandOfUuid() {
        rihaStorageInfoSystemRepository.load("1-1-1-1-1");

        ArgumentCaptor<FilterRequest> filterableCaptor = ArgumentCaptor.forClass(FilterRequest.class);
        verify(mainResourceRepository).find(filterableCaptor.capture());

        assertThat(filterableCaptor.getValue().getFilter(), startsWith("short_name"));
    }

}
