package ee.ria.riha.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
    private static final String CONTACTS_NAME_KEY = "name";
    private static final String CONTACTS_EMAIL_KEY = "email";
    private static final String META_KEY = "meta";
    private static final String META_CREATION_TIMESTAMP_KEY = "creation_timestamp";
    private static final String META_UPDATE_TIMESTAMP_KEY = "update_timestamp";

    private Long id;
    private JsonNode jsonContent;
    private IssueType lastPositiveApprovalRequestType;
    private Date lastPositiveApprovalRequestDate;
    private Date lastPositiveEstablishmentRequestDate;
    private Date lastPositiveTakeIntoUseRequestDate;
    private Date lastPositiveFinalizationRequestDate;

    /**
     * Creates {@link InfoSystem} instance with empty {@link JsonNode} as a source
     */
    public InfoSystem() {
        this(JsonNodeFactory.instance.objectNode());
    }

    /**
     * Creates {@link InfoSystem} instance with provided {@link JsonNode} as a source
     *
     * @param jsonContent source {@link JsonNode}
     */
    public InfoSystem(JsonNode jsonContent) {
        Assert.notNull(jsonContent);
        this.jsonContent = jsonContent;
    }

    /**
     * Makes copy of info system properties and deep copy of source {@link JsonNode}
     *
     * @return copy of info system
     */
    public InfoSystem copy() {
        InfoSystem copy = new InfoSystem(this.jsonContent.deepCopy());
        copy.setId(this.getId());
        copy.setLastPositiveApprovalRequestDate(this.getLastPositiveApprovalRequestDate());
        copy.setLastPositiveApprovalRequestType(this.getLastPositiveApprovalRequestType());
        copy.setLastPositiveEstablishmentRequestDate(this.getLastPositiveEstablishmentRequestDate());
        copy.setLastPositiveTakeIntoUseRequestDate(this.getLastPositiveTakeIntoUseRequestDate());
        copy.setLastPositiveFinalizationRequestDate(this.getLastPositiveFinalizationRequestDate());

        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonNode getJsonContent() {
        return jsonContent;
    }

    /**
     * Retrieves and parses uuid property from JSON. Returns null when property is not set or is empty.
     *
     * @return parsed UUID from JSON or null if not exists
     * @throws IllegalArgumentException in case of incorrect UUID representation
     */
    public UUID getUuid() {
        String uuidStr = jsonContent.path(UUID_KEY).asText(null);

        if (!StringUtils.hasText(uuidStr)) {
            return null;
        }

        return UUID.fromString(uuidStr);
    }

    /**
     * Sets info system uuid JSON property
     *
     * @param uuid info system UUID
     */
    public void setUuid(UUID uuid) {
        ((ObjectNode) jsonContent).put(UUID_KEY, uuid != null ? uuid.toString() : null);
    }

    /**
     * Retrieves info system owner name from JSON. Returns null when property is not set.
     *
     * @return owner name or null if not specified
     */
    public String getOwnerName() {
        return jsonContent.path(OWNER_KEY).path(OWNER_NAME_KEY).asText(null);
    }

    /**
     * Sets info system owner name JSON property
     *
     * @param name info system owner name
     */
    public void setOwnerName(String name) {
        ((ObjectNode) jsonContent).with(OWNER_KEY).put(OWNER_NAME_KEY, name);
    }

    /**
     * Retrieves info system owner code from JSON. Returns null when property is not set.
     *
     * @return owner code or null if not specified
     */
    public String getOwnerCode() {
        return jsonContent.path(OWNER_KEY).path(OWNER_CODE_KEY).asText(null);
    }

    /**
     * Sets info system owner code JSON property
     *
     * @param code info system owner code
     */
    public void setOwnerCode(String code) {
        ((ObjectNode) jsonContent).with(OWNER_KEY).put(OWNER_CODE_KEY, code);
    }

    /**
     * Retrieves info system short name from JSON. Returns null when property is not set.
     *
     * @return info system short name or null if not specified
     */
    public String getShortName() {
        return jsonContent.path(SHORT_NAME_KEY).asText(null);
    }

    /**
     * Sets info system short name JSON property
     *
     * @param shortName info system short name
     */
    public void setShortName(String shortName) {
        ((ObjectNode) jsonContent).put(SHORT_NAME_KEY, shortName);
    }

    /**
     * Retrieves info system full name from JSON. Returns null when property is not set.
     *
     * @return info system full name or null if not specified
     */
    public String getFullName() {
        return jsonContent.path(FULL_NAME_KEY).asText(null);
    }

    /**
     * Sets info system full name JSON property
     *
     * @param fullName info system full name
     */
    public void setFullName(String fullName) {
        ((ObjectNode) jsonContent).put(FULL_NAME_KEY, fullName);
    }

    /**
     * Retrieves info system creation timestamp from JSON. Returns null when property is not set.
     *
     * @return info system creation timestamp or null if not specified
     */
    public String getCreationTimestamp() {
        return jsonContent.path(META_KEY).path(META_CREATION_TIMESTAMP_KEY).asText(null);
    }

    /**
     * Sets info system creation timestamp JSON property
     *
     * @param creationTimestamp creation timestamp
     */
    public void setCreationTimestamp(String creationTimestamp) {
        ((ObjectNode) jsonContent).with(META_KEY).put(META_CREATION_TIMESTAMP_KEY, creationTimestamp);
    }

    /**
     * Retrieves info system update timestamp from JSON. Returns null when property is not set.
     *
     * @return info system update timestamp or null if not specified
     */
    public String getUpdateTimestamp() {
        return jsonContent.path(META_KEY).path(META_UPDATE_TIMESTAMP_KEY).asText(null);
    }

    /**
     * Sets info system owner name JSON property
     *
     * @param updateTimestamp update timestamp
     */
    public void setUpdateTimestamp(String updateTimestamp) {
        ((ObjectNode) jsonContent).with(META_KEY).put(META_UPDATE_TIMESTAMP_KEY, updateTimestamp);
    }

    public IssueType getLastPositiveApprovalRequestType() {
        return lastPositiveApprovalRequestType;
    }

    public void setLastPositiveApprovalRequestType(IssueType lastPositiveApprovalRequestType) {
        this.lastPositiveApprovalRequestType = lastPositiveApprovalRequestType;
    }

    public Date getLastPositiveApprovalRequestDate() {
        return lastPositiveApprovalRequestDate;
    }

    public void setLastPositiveApprovalRequestDate(Date lastPositiveApprovalRequestDate) {
        this.lastPositiveApprovalRequestDate = lastPositiveApprovalRequestDate;
    }

    public Date getLastPositiveEstablishmentRequestDate() {
        return lastPositiveEstablishmentRequestDate;
    }

    public void setLastPositiveEstablishmentRequestDate(Date lastPositiveEstablishmentRequestDate) {
        this.lastPositiveEstablishmentRequestDate = lastPositiveEstablishmentRequestDate;
    }

    public Date getLastPositiveTakeIntoUseRequestDate() {
        return lastPositiveTakeIntoUseRequestDate;
    }

    public void setLastPositiveTakeIntoUseRequestDate(Date lastPositiveTakeIntoUseRequestDate) {
        this.lastPositiveTakeIntoUseRequestDate = lastPositiveTakeIntoUseRequestDate;
    }

    public Date getLastPositiveFinalizationRequestDate() {
        return lastPositiveFinalizationRequestDate;
    }

    public void setLastPositiveFinalizationRequestDate(Date lastPositiveFinalizationRequestDate) {
        this.lastPositiveFinalizationRequestDate = lastPositiveFinalizationRequestDate;
    }

    /**
     * Utility method for adding contacts. Will create contacts {@link ArrayNode} if is does not exists.
     *
     * @param name  name of a contact
     * @param email contact email
     */
    public void addContact(String name, String email) {
        ((ArrayNode) jsonContent.withArray(CONTACTS_KEY)).addObject()
                .put(CONTACTS_NAME_KEY, name)
                .put(CONTACTS_EMAIL_KEY, email);
    }

    /**
     * Utility method for retrieving emails of all contacts. Does not modify source JsonNode.
     *
     * @return list of contact emails or empty list
     */
    public List<String> getContactsEmails() {
        JsonNode contactsNode = jsonContent.get(CONTACTS_KEY);
        if (contactsNode == null || !contactsNode.isArray()) {
            return new ArrayList<>();
        }

        return contactsNode.findValuesAsText(CONTACTS_EMAIL_KEY);
    }

}
