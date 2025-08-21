package ee.ria.riha.service.notification;

import static org.mockito.Mockito.*;

import ee.ria.riha.service.notification.handler.NewInfoSystemsNotificationHandler;
import ee.ria.riha.service.notification.model.NewInfoSystemsEmailNotification;
import ee.ria.riha.service.notification.model.SimpleHtmlEmailNotification;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class EmailNotificationSenderServiceTest {

  @Mock private JavaMailSenderImpl javaMailSender;

  @InjectMocks private EmailNotificationSenderService emailNotificationSenderService;

  @Mock private NewInfoSystemsNotificationHandler handler;

  @Mock private MimeMessage mimeMessage;

  @Mock private MimeMessagePreparator messagePreparator;

  @BeforeEach
  public void setUp() {
    emailNotificationSenderService.getHandlers().add(handler);

    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    when(handler.supports(Mockito.any(NewInfoSystemsEmailNotification.class))).thenCallRealMethod();
    when(handler.createMessagePreparator(Mockito.any(NewInfoSystemsEmailNotification.class)))
        .thenReturn(messagePreparator);
  }

  @Test
  public void sendsNotificationMessage() {
    NewInfoSystemsEmailNotification notification = new NewInfoSystemsEmailNotification();
    notification.setTo("recipient@example.com");
    notification.setFrom("sender@example.com");

    emailNotificationSenderService.sendNotification(notification);

    verify(javaMailSender).send(Mockito.any(MimeMessage.class));
  }

  @Test
  public void doesNotSendNotificationIfHandlerNotFound() {
    emailNotificationSenderService.sendNotification(new SimpleHtmlEmailNotification());

    verify(javaMailSender, never()).send(Mockito.any(MimeMessagePreparator.class));
  }
}
