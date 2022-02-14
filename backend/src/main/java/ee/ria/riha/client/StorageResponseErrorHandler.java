package ee.ria.riha.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Handles RIHA-Storage 4xx errors. Delegates handling of 5xx errors to default {@link DefaultResponseErrorHandler}
 *
 * @author Valentin Suhnjov
 */
@Slf4j
public class StorageResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();

        if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                StorageError error = objectMapper.readValue(response.getBody(), StorageError.class);
                throw new StorageClientException(error);
            } catch (JsonParseException | JsonMappingException e) {
                log.warn("Error parsing storage error, will use default response error handler", e);
                super.handleError(response);
            }
        } else {
            super.handleError(response);
        }
    }

}
