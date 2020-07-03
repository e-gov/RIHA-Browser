package ee.ria.riha.service;

import com.fasterxml.jackson.databind.*;
import ee.ria.riha.domain.model.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.client.*;

@Service
@Slf4j
public class SystemFeedbackService {

    @Value("${browser.feedbackUrl}")
    private String feedbackEndpointUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendFeedback(SystemFeedbackModel systemFeedbackModel) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(systemFeedbackModel), headers);

            log.debug("prepared request for sending {}", requestEntity);

            ResponseEntity<String> exchange = restTemplate.exchange(
                    feedbackEndpointUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

        } catch (Exception e) {
            log.error("error while submitting feedback", e);
            throw new IllegalStateException("cannot send feedback", e);
        }
    }

}


