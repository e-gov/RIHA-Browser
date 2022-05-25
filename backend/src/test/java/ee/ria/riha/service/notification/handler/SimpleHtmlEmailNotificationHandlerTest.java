package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.SimpleHtmlEmailNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleHtmlEmailNotificationHandlerTest {

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private TestableSimpleHtmlEmailNotificationHandler handler;

    private SimpleHtmlEmailNotification notification = new SimpleHtmlEmailNotification();

    @Before
    public void setUp() {
        notification.setFrom("sender@example.com");
        notification.setTo("recipient@example.com");
        notification.setCc(new String[]{"cc@example.com"});
        notification.setBcc(new String[]{"bcc@example.com"});
    }

    @Test
    public void setsBasicMessageParameters() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setFrom(new InternetAddress("sender@example.com"));
        verify(mimeMessage).setRecipient(Message.RecipientType.TO,new InternetAddress("recipient@example.com"));
        verify(mimeMessage).setRecipients(Message.RecipientType.CC,
                InternetAddress.parse("cc@example.com"));
        verify(mimeMessage).setRecipients(Message.RecipientType.BCC,
                InternetAddress.parse("bcc@example.com"));
    }

    @Test
    public void setsSubject() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setSubject(ArgumentMatchers.eq("testable subject"));
    }

    @Test
    public void setsText() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setContent(ArgumentMatchers.eq("testable text"), ArgumentMatchers.eq("text/html"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenFromAddressIsNull() throws Exception {
        notification.setFrom(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenToAddressIsNull() throws Exception {
        notification.setTo(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenToAddressIsEmpty() throws Exception {
        notification.setTo(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);
    }

    @Test
    public void doesNotSetCcWhenCcIsNotDefined() throws Exception {
        notification.setCc(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage, never()).setRecipients(ArgumentMatchers.eq(Message.RecipientType.CC),
                ArgumentMatchers.any(Address[].class));
    }

    @Test
    public void doesNotSetBccWhenBccIsNotDefined() throws Exception {
        notification.setBcc(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage, never()).setRecipients(ArgumentMatchers.eq(Message.RecipientType.BCC),
                ArgumentMatchers.any(Address[].class));
    }

    private static class TestableSimpleHtmlEmailNotificationHandler extends SimpleHtmlEmailNotificationHandler {

        @Override
        protected String getSubject(SimpleHtmlEmailNotification model) {
            return "testable subject";
        }

        @Override
        protected String getText(SimpleHtmlEmailNotification model) {
            return "testable text";
        }

        @Override
        public boolean supports(EmailNotificationDataModel model) {
            return true;
        }
    }
}
