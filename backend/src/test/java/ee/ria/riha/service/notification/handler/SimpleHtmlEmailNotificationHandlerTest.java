package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.SimpleHtmlEmailNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        notification.setTo(new String[]{"recipient@example.com"});
        notification.setCc(new String[]{"cc@example.com"});
        notification.setBcc(new String[]{"bcc@example.com"});
    }

    @Test
    public void setsBasicMessageParameters() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setFrom(Matchers.eq(new InternetAddress("sender@example.com")));
        verify(mimeMessage).setRecipients(Matchers.eq(Message.RecipientType.TO),
                Matchers.eq(InternetAddress.parse("recipient@example.com")));
        verify(mimeMessage).setRecipients(Matchers.eq(Message.RecipientType.CC),
                Matchers.eq(InternetAddress.parse("cc@example.com")));
        verify(mimeMessage).setRecipients(Matchers.eq(Message.RecipientType.BCC),
                Matchers.eq(InternetAddress.parse("bcc@example.com")));
    }

    @Test
    public void setsSubject() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setSubject(Matchers.eq("testable subject"));
    }

    @Test
    public void setsText() throws Exception {
        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage).setContent(Matchers.eq("testable text"), Matchers.eq("text/html"));
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
        notification.setTo(new String[0]);

        handler.createMessagePreparator(notification).prepare(mimeMessage);
    }

    @Test
    public void doesNotSetCcWhenCcIsNotDefined() throws Exception {
        notification.setCc(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage, never()).setRecipients(Matchers.eq(Message.RecipientType.CC),
                Matchers.any(Address[].class));
    }

    @Test
    public void doesNotSetBccWhenBccIsNotDefined() throws Exception {
        notification.setBcc(null);

        handler.createMessagePreparator(notification).prepare(mimeMessage);

        verify(mimeMessage, never()).setRecipients(Matchers.eq(Message.RecipientType.BCC),
                Matchers.any(Address[].class));
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