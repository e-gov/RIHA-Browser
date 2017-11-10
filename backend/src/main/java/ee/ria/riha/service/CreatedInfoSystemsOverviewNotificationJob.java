package ee.ria.riha.service;

import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.notifications.model.InfoSystemDataModel;
import ee.ria.riha.service.notifications.model.NewInfoSystemsNotification;
import ee.ria.riha.storage.util.FilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    private InfoSystemRepository infoSystemRepository;
    private NotificationService notificationService;

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

            notificationService.sendNewInfoSystemsNotification(new NewInfoSystemsNotification(infoSystemDataModels));
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
}
