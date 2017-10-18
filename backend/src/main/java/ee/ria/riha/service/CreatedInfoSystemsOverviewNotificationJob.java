package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.FilterRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Slf4j
public class CreatedInfoSystemsOverviewNotificationJob {

    private static final String MESSAGE_TEMPLATE = "new-IS-broadcast-template.ftl";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String baseUrl;
    private final String from;
    private final String[] to;
    private final String[] cc;
    private final String[] bcc;

    private JavaMailSenderImpl mailSender;
    private Configuration freeMarkerConfiguration;
    private InfoSystemRepository infoSystemRepository;
    private MessageSource messageSource;

    @Autowired
    public CreatedInfoSystemsOverviewNotificationJob(ApplicationProperties applicationProperties) {
        baseUrl = applicationProperties.getBaseUrl();
        Assert.hasText(baseUrl, "Base URL must be defined");

        from = applicationProperties.getNotification().getFrom();
        Assert.hasText(from, "Notification email sender must be defined");

        to = applicationProperties.getNotification().getCreatedInfoSystemsOverview().getTo();
        Assert.notEmpty(to, "At least one recipient must be defined in the list of recipients");

        cc = applicationProperties.getNotification().getCreatedInfoSystemsOverview().getCc();
        bcc = applicationProperties.getNotification().getCreatedInfoSystemsOverview().getBcc();
    }

    @Scheduled(cron = "${browser.notification.createdInfoSystemsOverview.cron}")
    public void sendCreatedInfoSystemsOverviewNotification() {
        try {
            List<InfoSystem> infoSystems = getListOfCreatedInfoSystems();
            sendNewInfoSystemsBroadcastMessage(infoSystems);
        } catch (Exception e) {
            log.warn("Job execution failed", e);
        }
    }

    private List<InfoSystem> getListOfCreatedInfoSystems() {
        FilterRequest filter = new FilterRequest();

        LocalDateTime endDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        filter.addFilter("j_creation_timestamp,<," + DATE_TIME_FORMATTER.format(endDateTime));

        LocalDateTime startDateTime = endDateTime.minusDays(1);
        filter.addFilter("j_creation_timestamp,>=," + DATE_TIME_FORMATTER.format(startDateTime));

        if (log.isDebugEnabled()) {
            log.debug("Searching for info systems created between {} and {}", startDateTime, endDateTime);
        }
        List<InfoSystem> infoSystems = infoSystemRepository.find(filter);
        if (log.isDebugEnabled()) {
            log.debug("{} info system(s) found", infoSystems.size());
        }

        return infoSystems;
    }

    private void sendNewInfoSystemsBroadcastMessage(List<InfoSystem> infoSystems) {
        if (infoSystems.isEmpty()) {
            log.info("Info system list is empty, nothing to send");
            return;
        }

        MimeMessage message;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);

            if (cc != null) {
                helper.setCc(cc);
            }

            if (bcc != null) {
                helper.setBcc(bcc);
            }

            helper.setSubject(getMessageSubject());
            helper.setSentDate(new Date());
            helper.setText(getMessageText(infoSystems), true);
            if (log.isDebugEnabled()) {
                log.debug("Sending notification message from '{}' to '{}' with subject '{}'", from, to, message.getSubject());
            }
        } catch (MessagingException e) {
            throw new MailPreparationException("Error preparing notification message", e);
        }

        mailSender.send(message);
        log.info("Created info systems overview notification message has been successfully sent");
    }

    private String getMessageSubject() {
        return messageSource.getMessage("notifications.createdInfoSystemsOverview.subject", null, Locale.getDefault());
    }

    private String getMessageText(List<InfoSystem> infoSystems) {
        try {
            Template template = freeMarkerConfiguration.getTemplate(MESSAGE_TEMPLATE);
            Map<String, Object> model = new HashMap<>();
            model.put("infosystems", infoSystems);
            model.put("baseUrl", baseUrl);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            throw new MailPreparationException("Error generating notification message text template " + MESSAGE_TEMPLATE, e);
        }

    }

    @Autowired
    public void setInfoSystemRepository(InfoSystemRepository infoSystemRepository) {
        this.infoSystemRepository = infoSystemRepository;
    }

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
