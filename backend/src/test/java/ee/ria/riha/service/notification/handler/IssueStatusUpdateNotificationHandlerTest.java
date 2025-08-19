package ee.ria.riha.service.notification.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.ria.riha.service.notification.model.InfoSystemDataModel;
import ee.ria.riha.service.notification.model.IssueDataModel;
import ee.ria.riha.service.notification.model.IssueStatusUpdateNotification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class IssueStatusUpdateNotificationHandlerTest {

  @Mock private MessageSource messageSource;

  @Mock private Configuration freemarkerConfiguration;

  @Mock private Template template;

  @InjectMocks private IssueStatusUpdateNotificationHandler notificationHandler;

  @BeforeEach
  public void setUp() throws IOException {
    when(messageSource.getMessage(
            ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(Locale.class)))
        .thenReturn("mock subject");

    when(freemarkerConfiguration.getTemplate("issue-status-update-notification-template.ftl"))
        .thenReturn(template);
  }

  @Test
  public void supportsIssueStatusUpdateNotifications() {
    assertThat(notificationHandler.supports(new IssueStatusUpdateNotification()), is(true));
  }

  @Test
  public void generatesSubject() {
    IssueStatusUpdateNotification model = new IssueStatusUpdateNotification();
    model.setInfoSystem(new InfoSystemDataModel());
    String subject = notificationHandler.getSubject(model);

    assertThat(subject, is(equalTo("mock subject")));
  }

  @Test
  public void passDataModelToTemplate() throws IOException, TemplateException {
    IssueDataModel issue = new IssueDataModel();
    InfoSystemDataModel infoSystem = new InfoSystemDataModel();

    IssueStatusUpdateNotification model = new IssueStatusUpdateNotification();
    model.setIssue(issue);
    model.setInfoSystem(infoSystem);
    model.setCommented(true);
    model.setBaseUrl("https://riha.ee");

    notificationHandler.getText(model);

    ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);
    verify(template).process(objectArgumentCaptor.capture(), ArgumentMatchers.any());

    assertThat(objectArgumentCaptor.getValue(), is(instanceOf(Map.class)));

    Map<String, Object> dataModel = ((Map<String, Object>) objectArgumentCaptor.getValue());
    assertThat(dataModel, hasEntry("baseUrl", "https://riha.ee"));
    assertThat(dataModel, hasEntry("commented", true));
    assertThat(dataModel, hasEntry("infoSystem", infoSystem));
    assertThat(dataModel, hasEntry("issue", issue));
  }
}
