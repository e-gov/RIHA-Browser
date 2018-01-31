package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.InfoSystemDataModel;
import ee.ria.riha.service.notification.model.IssueDataModel;
import ee.ria.riha.service.notification.model.IssueStatusUpdateNotification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class IssueStatusUpdateNotificationHandlerTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private Template template;

    @InjectMocks
    private IssueStatusUpdateNotificationHandler notificationHandler;

    @Before
    public void setUp() throws IOException {
        when(messageSource.getMessage(Matchers.any(), Matchers.anyObject(), Matchers.any(Locale.class)))
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
        verify(template).process(objectArgumentCaptor.capture(), Matchers.anyObject());

        assertThat(objectArgumentCaptor.getValue(), is(instanceOf(Map.class)));

        Map<String, Object> dataModel = ((Map<String, Object>) objectArgumentCaptor.getValue());
        assertThat(dataModel, hasEntry("baseUrl", "https://riha.ee"));
        assertThat(dataModel, hasEntry("commented", true));
        assertThat(dataModel, hasEntry("infoSystem", infoSystem));
        assertThat(dataModel, hasEntry("issue", issue));
    }

}