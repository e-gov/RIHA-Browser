package ee.ria.riha.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Holds Information System data and provides accessors for JSON manipulation.
 *
 * @author Valentin Suhnjov
 */
public class InfoSystem {

    private static final String UUID_KEY = "uuid";
    private static final String OWNER_KEY = "owner";
    private static final String OWNER_NAME_KEY = "name";
    private static final String OWNER_CODE_KEY = "code";
    private static final String SHORT_NAME_KEY = "short_name";
    private static final String FULL_NAME_KEY = "name";
    private static final String CONTACTS_KEY = "contacts";
    private static final String CONTACTS_EMAIL_KEY = "email";
    private static final String META_KEY = "meta";
    private static final String META_CREATION_TIMESTAMP_KEY = "creation_timestamp";
    private static final String META_UPDATE_TIMESTAMP_KEY = "update_timestamp";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode jsonContent = JsonNodeFactory.instance.objectNode();

    private Long id;

    public InfoSystem() {
    }

    public InfoSystem(JsonNode jsonContent) {
        Assert.notNull(jsonContent);
        this.jsonContent = jsonContent;
    }

    public InfoSystem(String jsonContent) {
        try {
            this.jsonContent = objectMapper.readTree(jsonContent);
        } catch (IOException e) {
            throw new IllegalStateException("Could not parse InfoSystem JSON", e);
        }
    }

    public String asJson() {
        try {
            return objectMapper.writeValueAsString(jsonContent);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not construct InfoSystem JSON representation", e);
        }
    }

    public JsonNode getJsonContent() {
        return jsonContent;
    }

    public InfoSystem copy() {
        InfoSystem copy = new InfoSystem(this.jsonContent.deepCopy());
        copy.setId(this.getId());

        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        String uuidStr = jsonContent.path(UUID_KEY).asText(null);
        return StringUtils.hasText(uuidStr) ? UUID.fromString(uuidStr) : null;
    }

    public void setUuid(UUID uuid) {
        ((ObjectNode) jsonContent).put(UUID_KEY, uuid != null ? uuid.toString() : null);
    }

    public String getOwnerName() {
        return jsonContent.path(OWNER_KEY).path(OWNER_NAME_KEY).asText(null);
    }

    public void setOwnerName(String name) {
        ((ObjectNode) jsonContent).with(OWNER_KEY).put(OWNER_NAME_KEY, name);
    }

    public String getOwnerCode() {
        return jsonContent.path(OWNER_KEY).path(OWNER_CODE_KEY).asText(null);
    }

    public void setOwnerCode(String code) {
        ((ObjectNode) jsonContent).with(OWNER_KEY).put(OWNER_CODE_KEY, code);
    }

    public String getShortName() {
        return jsonContent.path(SHORT_NAME_KEY).asText(null);
    }

    public void setShortName(String shortName) {
        ((ObjectNode) jsonContent).put(SHORT_NAME_KEY, shortName);
    }

    public String getFullName() {
        return jsonContent.path(FULL_NAME_KEY).asText(null);
    }

    public void setFullName(String fullName) {
        ((ObjectNode) jsonContent).put(FULL_NAME_KEY, fullName);
    }

    public String getCreationTimestamp() {
        return jsonContent.path(META_KEY).get(META_CREATION_TIMESTAMP_KEY).asText(null);
    }

    public void setCreationTimestamp(String creationTimestamp) {
        ((ObjectNode) jsonContent).with(META_KEY).put(META_CREATION_TIMESTAMP_KEY, creationTimestamp);
    }

    public String getUpdateTimestamp() {
        return jsonContent.path(META_KEY).path(META_UPDATE_TIMESTAMP_KEY).asText(null);
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        ((ObjectNode) jsonContent).with(META_KEY).put(META_UPDATE_TIMESTAMP_KEY, updateTimestamp);
    }

    public List<String> getContactEmails() {
        return jsonContent.withArray(CONTACTS_KEY).findValuesAsText(CONTACTS_EMAIL_KEY);
    }
}
