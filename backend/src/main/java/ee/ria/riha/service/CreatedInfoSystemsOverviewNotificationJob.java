package ee.ria.riha.service;

import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.FilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Slf4j
public class CreatedInfoSystemsOverviewNotificationJob {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private InfoSystemRepository infoSystemRepository;

    @Scheduled(cron = "${browser.notification.createdInfoSystemsOverview.cron}")
    public void sendCreatedInfoSystemsOverviewNotification() {
        try {
            List<InfoSystem> infoSystems = getListOfCreatedInfoSystems();
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
}
