package ee.ria.riha.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

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
    private static final String PURPOSE = "purpose";
    private static final String CONTACTS_KEY = "contacts";
    private static final String CONTACTS_NAME_KEY = "name";
    private static final String CONTACTS_EMAIL_KEY = "email";
    private static final String META_KEY = "meta";
    private static final String META_CREATION_TIMESTAMP_KEY = "creation_timestamp";
    private static final String META_UPDATE_TIMESTAMP_KEY = "update_timestamp";
    private static final String DATA_FILES_KEY = "data_files";
    private static final String DOCUMENTS_KEY = "documents";
    private static final String LEGISLATIONS_KEY = "legislations";
    private static final String TOPICS_KEY = "topics";
    private static final String FILE_METADATA_URL_KEY = "url";
    private static final String FILE_METADATA_NAME_KEY = "name";
    private static final String FILE_METADATA_TYPE_KEY = "type";
    private static final String FILE_METADATA_ACCESS_RESTRICTION_KEY = "accessRestriction";
    private static final String FIELD_DIFFERENCES_KEY = "differences";
    private static final String SECURITY_SECTION_KEY = "security";

    private static final Function<JsonNode, InfoSystemFileMetadata> DATA_FILE_METADATA_EXTRACTOR = jsonNode -> {
        InfoSystemFileMetadata metadata = new InfoSystemFileMetadata();
        metadata.setName(jsonNode.path(FILE_METADATA_NAME_KEY).asText(null));
        metadata.setUrl(jsonNode.path(FILE_METADATA_URL_KEY).asText(null));
        metadata.setType(jsonNode.path(FILE_METADATA_TYPE_KEY).asText(null));
        metadata.setCreationTimestamp(jsonNode.path(META_CREATION_TIMESTAMP_KEY).asText(null));
        metadata.setUpdateTimestamp(jsonNode.path(META_UPDATE_TIMESTAMP_KEY).asText(null));
        return metadata;
    };

    private static final String DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY = "accessRestriction";
    private static final Function<JsonNode, InfoSystemDocumentMetadata> DOCUMENT_METADATA_EXTRACTOR = jsonNode -> {
        InfoSystemDocumentMetadata metadata = new InfoSystemDocumentMetadata();
        metadata.setName(jsonNode.path(FILE_METADATA_NAME_KEY).asText(null));
        metadata.setUrl(jsonNode.path(FILE_METADATA_URL_KEY).asText(null));
        metadata.setType(jsonNode.path(FILE_METADATA_TYPE_KEY).asText(null));
        metadata.setCreationTimestamp(jsonNode.path(META_CREATION_TIMESTAMP_KEY).asText(null));
        metadata.setUpdateTimestamp(jsonNode.path(META_UPDATE_TIMESTAMP_KEY).asText(null));
        metadata.setAccessRestricted(jsonNode.hasNonNull(DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY));
        if (metadata.isAccessRestricted()) {
            metadata.setAccessRestrictionJson(jsonNode.path(DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY));
        }
        return metadata;
    };

    private Long id;
    private JsonNode jsonContent;
    private IssueType lastPositiveApprovalRequestType;
    private Date lastPositiveApprovalRequestDate;
    private Date lastPositiveEstablishmentRequestDate;
    private Date lastPositiveTakeIntoUseRequestDate;
    private Date lastPositiveFinalizationRequestDate;
    private boolean hasUsedSystemTypeRelations;

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

    public Date getLastPositiveApprovalRequestDate() {
        return lastPositiveApprovalRequestDate;
    }

    public IssueType getLastPositiveApprovalRequestType() {
        return lastPositiveApprovalRequestType;
    }

    public void setLastPositiveApprovalRequestType(IssueType lastPositiveApprovalRequestType) {
        this.lastPositiveApprovalRequestType = lastPositiveApprovalRequestType;
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

    public void setLastPositiveApprovalRequestDate(Date lastPositiveApprovalRequestDate) {
        this.lastPositiveApprovalRequestDate = lastPositiveApprovalRequestDate;
    }

    public void setDifferences(String differences) {
        ((ObjectNode) jsonContent).put(FIELD_DIFFERENCES_KEY, differences);
    }

    public String getDifferences() {
        return jsonContent.path(FIELD_DIFFERENCES_KEY).asText(null);
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
     * Retrieves info system purpose from JSON. Returns null when property is not set.
     *
     * @return info system purpose or null if not specified
     */
    public String getPurpose() {
        return jsonContent.path(PURPOSE).asText(null);
    }

    /**
     * Sets info system purpose JSON property
     *
     * @param purpose info system purpose
     */
    public void setPurpose(String purpose) {
        ((ObjectNode) jsonContent).put(PURPOSE, purpose);
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

    /**
     * Utility method for retrieving of document metadata. Does not modify source JsonNode.
     *
     * @return list of document metadata or empty in case documents node does not exists, not an array or empty
     */
    public List<InfoSystemDocumentMetadata> getDocumentMetadata() {
        JsonNode documentsNode = jsonContent.path(DOCUMENTS_KEY);
        if (!documentsNode.isArray()) {
            return new ArrayList<>();
        }

        return extractFileMetadata(documentsNode, DOCUMENT_METADATA_EXTRACTOR);
    }

    public boolean isHasUsedSystemTypeRelations() {
        return hasUsedSystemTypeRelations;
    }

    public void setHasUsedSystemTypeRelations(boolean hasUsedSystemTypeRelations) {
        this.hasUsedSystemTypeRelations = hasUsedSystemTypeRelations;
    }

    private <T extends InfoSystemFileMetadata> List<T> extractFileMetadata(JsonNode documentsNode,
                                                                           Function<JsonNode, T> metadataExtractor) {
        List<T> documents = new ArrayList<>();
        for (JsonNode documentNode : documentsNode) {
            documents.add(metadataExtractor.apply(documentNode));
        }

        return documents;
    }

    /**
     * Utility method for retrieving of data file metadata. Does not modify source JsonNode.
     *
     * @return list of data file metadata or empty list in case data file node does not exists, not an array or empty
     */
    public List<InfoSystemFileMetadata> getDataFileMetadata() {
        JsonNode dataFilesNode = jsonContent.path(DATA_FILES_KEY);
        if (!dataFilesNode.isArray()) {
            return new ArrayList<>();
        }

        return extractFileMetadata(dataFilesNode, DATA_FILE_METADATA_EXTRACTOR);
    }

    /**
     * Utility method for retrieving of legislations metadata. Does not modify source JsonNode.
     *
     * @return list of legislations metadata or empty list in case data file node does not exists, not an array or empty
     */
    public List<InfoSystemFileMetadata> getLegislationsMetadata() {
        JsonNode dataFilesNode = jsonContent.path(LEGISLATIONS_KEY);
        if (!dataFilesNode.isArray()) {
            return new ArrayList<>();
        }

        return extractFileMetadata(dataFilesNode, DATA_FILE_METADATA_EXTRACTOR);
    }

    public List<String> getTopics() {
        JsonNode topicsNode = jsonContent.path(TOPICS_KEY);
        if (!topicsNode.isArray()) {
            return new ArrayList<>();
        }

        ArrayList<String> result = new ArrayList<>();

        for (JsonNode topicNode : topicsNode) {
            if (topicNode != null) {
                result.add(topicNode.toString());
            }
        }

        return result;
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
     * Utility method for deleting contacts. Will remove all content from existing CONTACTS_KEY path
     */
    public void clearContacts() {
        ((ArrayNode) jsonContent.withArray(CONTACTS_KEY)).removeAll();
    }

    /**
     *  Urility method for clearing security section of InfoSystem object.
     */
    public void clearSecuritySection() {
        ((ObjectNode) jsonContent).remove(SECURITY_SECTION_KEY);
    }

    public void removeTopic(String topicToRemove) {
        JsonNode topicsNode = jsonContent.path(TOPICS_KEY);
        if (topicsNode == null || !topicsNode.isArray()) {
            return;
        }

        Iterator<JsonNode> iterator = topicsNode.iterator();
        while (iterator.hasNext()) {
            if (topicToRemove.equalsIgnoreCase(iterator.next().asText())) {
                iterator.remove();
            }
        }
    }


    /**
     * Utility method for retrieving emails of all contacts. Does not modify source JsonNode.
     *
     * @return list of contact emails or empty list
     */
    public List<String> getContactsEmails() {
        JsonNode contactsNode = jsonContent.path(CONTACTS_KEY);
        if (!contactsNode.isArray()) {
            return new ArrayList<>();
        }

        return contactsNode.findValuesAsText(CONTACTS_EMAIL_KEY);
    }

    /**
     * Utility method for setting creation and update timestamp for files and links that were created or updated
     * during infosystem update
     *
     * @param prevSystemMetadata previous infosystem state
     */
    public void setCreationAndUpdateTimestampToFilesMetadata(InfoSystem prevSystemMetadata) {
        setCreationAndUpdateTimestampToFilesMetadata(this.getDocumentMetadata(), prevSystemMetadata.getDocumentMetadata(), DOCUMENTS_KEY);
        setCreationAndUpdateTimestampToFilesMetadata(this.getDataFileMetadata(), prevSystemMetadata.getDataFileMetadata(), DATA_FILES_KEY);
        setCreationAndUpdateTimestampToFilesMetadata(this.getLegislationsMetadata(), prevSystemMetadata.getLegislationsMetadata(), LEGISLATIONS_KEY);
    }

    /**
     * Utility method for setting creation and update timestamp for given list of files or links states
     *
     * @param currentFilesMetadataList list of current files states
     * @param prevFilesMetadataList    list of previous files states
     * @param jsonNodeName             name of json node containing the list
     */
    public void setCreationAndUpdateTimestampToFilesMetadata(List<? extends InfoSystemFileMetadata> currentFilesMetadataList,
                                                             List<? extends InfoSystemFileMetadata> prevFilesMetadataList, String jsonNodeName) {
        ArrayNode filesNode = ((ArrayNode) jsonContent.withArray(jsonNodeName));
        filesNode.removeAll();

        currentFilesMetadataList.forEach(currentFileMetadata -> {
            Optional<? extends InfoSystemFileMetadata> foundPrevFileMetadata = prevFilesMetadataList.stream().filter(prevFileMetadata ->
                    prevFileMetadata.getUrl().equals(currentFileMetadata.getUrl())
                            || prevFileMetadata.getName().equals(currentFileMetadata.getName())
            ).findFirst();

            if (foundPrevFileMetadata.isPresent()) {
                //found prev version of same doc
                currentFileMetadata.setCreationTimestamp(foundPrevFileMetadata.get().getCreationTimestamp());
                if (currentFileMetadata.wasChanged(foundPrevFileMetadata.get())) {
                    currentFileMetadata.setUpdateTimestamp(this.getUpdateTimestamp());
                } else {
                    currentFileMetadata.setUpdateTimestamp(foundPrevFileMetadata.get().getUpdateTimestamp());
                }
            } else {
                //new doc
                currentFileMetadata.setCreationTimestamp(this.getUpdateTimestamp());
            }

            ObjectNode docNode = filesNode.addObject().put(FILE_METADATA_NAME_KEY, currentFileMetadata.getName())
                    .put(FILE_METADATA_URL_KEY, currentFileMetadata.getUrl());

            if (currentFileMetadata.getType() != null) {
                docNode.put(FILE_METADATA_TYPE_KEY, currentFileMetadata.getType());
            }
            if (currentFileMetadata.getCreationTimestamp() != null) {
                docNode.put(META_CREATION_TIMESTAMP_KEY, currentFileMetadata.getCreationTimestamp());
            }
            if (currentFileMetadata.getUpdateTimestamp() != null) {
                docNode.put(META_UPDATE_TIMESTAMP_KEY, currentFileMetadata.getUpdateTimestamp());
            }

            if (currentFileMetadata instanceof InfoSystemDocumentMetadata &&
                    ((InfoSystemDocumentMetadata) currentFileMetadata).getAccessRestrictionJson() != null) {
                docNode.set(FILE_METADATA_ACCESS_RESTRICTION_KEY,
                        ((InfoSystemDocumentMetadata) currentFileMetadata).getAccessRestrictionJson());
            }
        });


    }
}
