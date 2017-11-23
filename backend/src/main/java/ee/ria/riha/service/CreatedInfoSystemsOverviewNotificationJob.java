package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.notifications.model.InfoSystemDataModel;
import ee.ria.riha.service.notifications.model.NewInfoSystemsEmailNotification;
import ee.ria.riha.storage.util.FilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CreatedInfoSystemsOverviewNotificationJob {

    private static final Function<InfoSystem, InfoSystemDataModel> INFO_SYSTEM_TO_DATA_MODEL = infoSystem -> {
        if (infoSystem == null) {
            return null;
        }

        return InfoSystemDataModel.builder()
                .fullName(infoSystem.getFullName())
                .shortName(infoSystem.getShortName())
                .build();
    };
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String SUBJECT_KEY = "notifications.createdInfoSystemsOverview.subject";

    private final String baseUrl;
    private final String from;
    private final String[] to;
    private final String[] cc;
    private final String[] bcc;

    private InfoSystemRepository infoSystemRepository;
    private NotificationService notificationService;
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

            if (infoSystems.isEmpty()) {
                log.info("Info system list is empty, nothing to send");
                return;
            }

            List<InfoSystemDataModel> infoSystemDataModels = infoSystems.stream()
                    .map(INFO_SYSTEM_TO_DATA_MODEL)
                    .collect(Collectors.toList());

            NewInfoSystemsEmailNotification notificationModel = NewInfoSystemsEmailNotification.builder()
                    .from(from)
                    .to(to)
                    .subject(messageSource.getMessage(SUBJECT_KEY, null, Locale.getDefault()))
                    .cc(cc)
                    .bcc(bcc)
                    .infoSystems(infoSystemDataModels)
                    .baseUrl(baseUrl)
                    .build();

            notificationService.sendNewInfoSystemsNotification(notificationModel);
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

    @Autowired
    public void setInfoSystemRepository(InfoSystemRepository infoSystemRepository) {
        this.infoSystemRepository = infoSystemRepository;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
