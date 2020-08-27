package ee.ria.riha.service;

import com.fasterxml.jackson.databind.*;
import ee.ria.riha.conf.*;
import ee.ria.riha.domain.model.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.client.*;

import static ee.ria.riha.conf.ApplicationConfiguration.*;

@Service
@Slf4j
public class SystemFeedbackService {

    private final FeedbackServiceConnectionProperties feedbackServiceConnectionProperties;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public SystemFeedbackService(FeedbackServiceConnectionProperties feedbackServiceConnectionProperties,
                                 ObjectMapper objectMapper,
                                 @Qualifier(FEEDBACK_SERVICE) RestTemplate restTemplate) {
        this.feedbackServiceConnectionProperties = feedbackServiceConnectionProperties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public void sendFeedback(SystemFeedbackModel systemFeedbackModel) {

        try {
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(systemFeedbackModel), headers);

            log.debug("prepared request for sending {}", requestEntity);

            ResponseEntity<String> exchange = restTemplate.exchange(
                    feedbackServiceConnectionProperties.getUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

        } catch (Exception e) {
            log.error("error while submitting feedback", e);
            throw new IllegalStateException("cannot send feedback", e);
        }
    }

}


