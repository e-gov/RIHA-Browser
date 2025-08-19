package ee.ria.riha.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.ria.riha.domain.model.MainResource;
import ee.ria.riha.service.util.FilterRequest;
import ee.ria.riha.service.util.Filterable;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class RihaStorageInfoSystemRepositoryTest {

  @Mock private MainResourceRepository mainResourceRepository;

  @InjectMocks private RihaStorageInfoSystemRepository rihaStorageInfoSystemRepository;

  @BeforeEach
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
