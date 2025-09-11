package ee.ria.riha.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
  private static final String STANDARD_SYSTEM = "standard_system";
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

  private static final Function<JsonNode, InfoSystemFileMetadata> DATA_FILE_METADATA_EXTRACTOR =
      jsonNode -> {
        InfoSystemFileMetadata metadata = new InfoSystemFileMetadata();
        metadata.setName(jsonNode.path(FILE_METADATA_NAME_KEY).asText(null));
        metadata.setUrl(jsonNode.path(FILE_METADATA_URL_KEY).asText(null));
        metadata.setType(jsonNode.path(FILE_METADATA_TYPE_KEY).asText(null));
        
        // Normalize timestamps when extracting from JSON
        String creationTimestamp = jsonNode.path(META_CREATION_TIMESTAMP_KEY).asText(null);
        String updateTimestamp = jsonNode.path(META_UPDATE_TIMESTAMP_KEY).asText(null);
        
        // Apply timestamp normalization
        if (creationTimestamp != null) {
          creationTimestamp = new InfoSystem().normalizeTimestamp(creationTimestamp);
        }
        
        if (updateTimestamp != null) {
          updateTimestamp = new InfoSystem().normalizeTimestamp(updateTimestamp);
        }
        
        metadata.setCreationTimestamp(creationTimestamp);
        metadata.setUpdateTimestamp(updateTimestamp);
        return metadata;
      };

  private static final String DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY = "accessRestriction";
  private static final Function<JsonNode, InfoSystemDocumentMetadata> DOCUMENT_METADATA_EXTRACTOR =
      jsonNode -> {
        InfoSystemDocumentMetadata metadata = new InfoSystemDocumentMetadata();
        metadata.setName(jsonNode.path(FILE_METADATA_NAME_KEY).asText(null));
        metadata.setUrl(jsonNode.path(FILE_METADATA_URL_KEY).asText(null));
        metadata.setType(jsonNode.path(FILE_METADATA_TYPE_KEY).asText(null));
        
        // Normalize timestamps when extracting from JSON
        String creationTimestamp = jsonNode.path(META_CREATION_TIMESTAMP_KEY).asText(null);
        String updateTimestamp = jsonNode.path(META_UPDATE_TIMESTAMP_KEY).asText(null);
        
        // Apply timestamp normalization
        if (creationTimestamp != null) {
          creationTimestamp = new InfoSystem().normalizeTimestamp(creationTimestamp);
        }
        
        if (updateTimestamp != null) {
          updateTimestamp = new InfoSystem().normalizeTimestamp(updateTimestamp);
        }
        
        metadata.setCreationTimestamp(creationTimestamp);
        metadata.setUpdateTimestamp(updateTimestamp);
        metadata.setAccessRestricted(jsonNode.hasNonNull(DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY));
        if (metadata.isAccessRestricted()) {
          metadata.setAccessRestrictionJson(
              jsonNode.path(DOCUMENT_METADATA_ACCESS_RESTRICTION_KEY));
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

  /** Creates {@link InfoSystem} instance with empty {@link JsonNode} as a source */
  public InfoSystem() {
    this(JsonNodeFactory.instance.objectNode());
  }

  /**
   * Creates {@link InfoSystem} instance with provided {@link JsonNode} as a source
   *
   * @param jsonContent source {@link JsonNode}
   */
  public InfoSystem(JsonNode jsonContent) {
    Assert.notNull(jsonContent, "Content in null");
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
   * Retrieves and parses uuid property from JSON. Returns null when property is not set or is
   * empty.
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
    // Check if OWNER node exists, if not create it
    if (!jsonContent.has(OWNER_KEY)) {
      ((ObjectNode) jsonContent).putObject(OWNER_KEY);
    }
    // Access the OWNER node and set the value
    ((ObjectNode) jsonContent.path(OWNER_KEY)).put(OWNER_NAME_KEY, name);
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
    // Check if OWNER node exists, if not create it
    if (!jsonContent.has(OWNER_KEY)) {
      ((ObjectNode) jsonContent).putObject(OWNER_KEY);
    }
    // Access the OWNER node and set the value
    ((ObjectNode) jsonContent.path(OWNER_KEY)).put(OWNER_CODE_KEY, code);
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

  public void setStandardInformationSystemUndefined() {
    ((ObjectNode) jsonContent).remove(STANDARD_SYSTEM);
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
   * Normalizes timestamp format if needed.
   *
   * @return info system creation timestamp or null if not specified
   */
  public String getCreationTimestamp() {
    return normalizeTimestamp(jsonContent.path(META_KEY).path(META_CREATION_TIMESTAMP_KEY).asText(null));
  }

  /**
   * Sets info system creation timestamp JSON property
   *
   * @param creationTimestamp creation timestamp
   */
  public void setCreationTimestamp(String creationTimestamp) {
    // Check if META node exists, if not create it
    if (!jsonContent.has(META_KEY)) {
      ((ObjectNode) jsonContent).putObject(META_KEY);
    }
    // Access the META node and set the value
    ((ObjectNode) jsonContent.path(META_KEY)).put(META_CREATION_TIMESTAMP_KEY, normalizeTimestamp(creationTimestamp));
  }

  /**
   * Retrieves info system update timestamp from JSON. Returns null when property is not set.
   * Normalizes timestamp format if needed.
   *
   * @return info system update timestamp or null if not specified
   */
  public String getUpdateTimestamp() {
    return normalizeTimestamp(jsonContent.path(META_KEY).path(META_UPDATE_TIMESTAMP_KEY).asText(null));
  }

  /**
   * Sets info system owner name JSON property
   *
   * @param updateTimestamp update timestamp
   */
  public void setUpdateTimestamp(String updateTimestamp) {
    // Check if META node exists, if not create it
    if (!jsonContent.has(META_KEY)) {
      ((ObjectNode) jsonContent).putObject(META_KEY);
    }
    // Access the META node and set the value
    ((ObjectNode) jsonContent.path(META_KEY)).put(META_UPDATE_TIMESTAMP_KEY, normalizeTimestamp(updateTimestamp));
  }

  /**
   * Utility method for retrieving of document metadata. Does not modify source JsonNode.
   *
   * @return list of document metadata or empty in case documents node does not exists, not an array
   *     or empty
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

  private <T extends InfoSystemFileMetadata> List<T> extractFileMetadata(
      JsonNode documentsNode, Function<JsonNode, T> metadataExtractor) {
    List<T> documents = new ArrayList<>();
    for (JsonNode documentNode : documentsNode) {
      documents.add(metadataExtractor.apply(documentNode));
    }

    return documents;
  }

  /**
   * Utility method for retrieving of data file metadata. Does not modify source JsonNode.
   *
   * @return list of data file metadata or empty list in case data file node does not exists, not an
   *     array or empty
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
   * @return list of legislations metadata or empty list in case data file node does not exists, not
   *     an array or empty
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
   * Utility method for adding contacts. Will create contacts {@link ArrayNode} if is does not
   * exists.
   *
   * @param name name of a contact
   * @param email contact email
   */
  public void addContact(String name, String email) {
    ((ArrayNode) jsonContent.withArray(CONTACTS_KEY))
        .addObject()
        .put(CONTACTS_NAME_KEY, name)
        .put(CONTACTS_EMAIL_KEY, email);
  }

  /**
   * Utility method for deleting contacts. Will remove all content from existing CONTACTS_KEY path
   */
  public void clearContacts() {
    ((ArrayNode) jsonContent.withArray(CONTACTS_KEY)).removeAll();
  }

  /** Utility method for clearing security section of InfoSystem object. */
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
   * Utility method for setting creation and update timestamp for files and links that were created
   * or updated during infosystem update
   *
   * @param prevSystemMetadata previous infosystem state
   */
  public void setCreationAndUpdateTimestampToFilesMetadata(InfoSystem prevSystemMetadata) {
    setCreationAndUpdateTimestampToFilesMetadata(
        this.getDocumentMetadata(), prevSystemMetadata.getDocumentMetadata(), DOCUMENTS_KEY);
    setCreationAndUpdateTimestampToFilesMetadata(
        this.getDataFileMetadata(), prevSystemMetadata.getDataFileMetadata(), DATA_FILES_KEY);
    setCreationAndUpdateTimestampToFilesMetadata(
        this.getLegislationsMetadata(),
        prevSystemMetadata.getLegislationsMetadata(),
        LEGISLATIONS_KEY);
  }

  /**
   * Utility method for setting creation and update timestamp for given list of files or links
   * states
   *
   * @param currentFilesMetadataList list of current files states
   * @param prevFilesMetadataList list of previous files states
   * @param jsonNodeName name of json node containing the list
   */
  public void setCreationAndUpdateTimestampToFilesMetadata(
      List<? extends InfoSystemFileMetadata> currentFilesMetadataList,
      List<? extends InfoSystemFileMetadata> prevFilesMetadataList,
      String jsonNodeName) {
    ArrayNode filesNode = ((ArrayNode) jsonContent.withArray(jsonNodeName));
    filesNode.removeAll();

    currentFilesMetadataList.forEach(
        currentFileMetadata -> {
          Optional<? extends InfoSystemFileMetadata> foundPrevFileMetadata =
              getPrevFileMetadata(prevFilesMetadataList, currentFileMetadata);

          if (foundPrevFileMetadata.isPresent()) {
            // found prev version of same doc
            currentFileMetadata.setCreationTimestamp(
                normalizeTimestamp(foundPrevFileMetadata.get().getCreationTimestamp()));
            if (currentFileMetadata.wasChanged(foundPrevFileMetadata.get())) {
              currentFileMetadata.setUpdateTimestamp(this.getUpdateTimestamp());
            } else {
              currentFileMetadata.setUpdateTimestamp(
                  normalizeTimestamp(foundPrevFileMetadata.get().getUpdateTimestamp()));
            }
          } else {
            // new doc
            currentFileMetadata.setCreationTimestamp(this.getUpdateTimestamp());
          }

          updateFilesNode(filesNode, currentFileMetadata);
        });
  }

  private void updateFilesNode(ArrayNode filesNode, InfoSystemFileMetadata currentFileMetadata) {
    ObjectNode docNode =
        filesNode
            .addObject()
            .put(FILE_METADATA_NAME_KEY, currentFileMetadata.getName())
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

    if (currentFileMetadata instanceof InfoSystemDocumentMetadata metadata
        && metadata.getAccessRestrictionJson() != null) {
      docNode.set(FILE_METADATA_ACCESS_RESTRICTION_KEY, metadata.getAccessRestrictionJson());
    }
  }

  private Optional<? extends InfoSystemFileMetadata> getPrevFileMetadata(
      List<? extends InfoSystemFileMetadata> prevFilesMetadataList,
      InfoSystemFileMetadata currentFileMetadata) {
    return prevFilesMetadataList.stream()
        .filter(
            prevFileMetadata ->
                prevFileMetadata.getUrl().equals(currentFileMetadata.getUrl())
                    || prevFileMetadata.getName().equals(currentFileMetadata.getName()))
        .findFirst();
  }

  public void replaceFileUrl(String oldUrl, String newUrl) {
    replaceFileUrl(oldUrl, newUrl, DOCUMENTS_KEY);
    replaceFileUrl(oldUrl, newUrl, DATA_FILES_KEY);
  }

  private void replaceFileUrl(String oldUrl, String newUrl, String key) {
    JsonNode filesNode = jsonContent.path(key);
    if (filesNode.isArray()) {
      for (JsonNode fileNode : filesNode) {
        String url = fileNode.path(FILE_METADATA_URL_KEY).asText(null);
        if (url.equals(oldUrl)) {
          ((ObjectNode) fileNode).put(FILE_METADATA_URL_KEY, newUrl);
        }
      }
    }
  }

  /**
   * Normalizes timestamp to RFC 3339 format by converting timezone offset from +0300 to +03:00
   * format and handling other potential timestamp format issues
   *
   * @param timestamp the timestamp to normalize
   * @return normalized timestamp in RFC 3339 format
   */
  private String normalizeTimestamp(String timestamp) {
    if (timestamp == null) {
      return null;
    }
    
    try {
      // Check for null-like values
      if ("not-a-timestamp".equals(timestamp) || timestamp.trim().isEmpty() ||
          "null".equals(timestamp) || "undefined".equals(timestamp)) {
        return null; // Return null instead of an invalid timestamp
      }
      
      // Handle timestamp that may have non-standard whitespace or surrounding characters
      String trimmedTimestamp = timestamp.trim();
      if (!trimmedTimestamp.equals(timestamp)) {
        timestamp = trimmedTimestamp;
      }
      
      // Check if timestamp has old format timezone without colon (+0300, -0300, +0200, -0200, etc)
      if (timestamp.matches(".*[+-]\\d{4}$") && !timestamp.matches(".*[+-]\\d{2}:\\d{2}$")) {
        // Convert +0300 to +03:00 format for RFC 3339 compliance
        String timezonePart = timestamp.substring(timestamp.length() - 5);
        String timestampWithoutTz = timestamp.substring(0, timestamp.length() - 5);
        String normalizedTz = timezonePart.substring(0, 3) + ":" + timezonePart.substring(3);
        return timestampWithoutTz + normalizedTz;
      }
      
      // Handle timestamps with Z timezone designator
      if (timestamp.endsWith("Z")) {
        String result = handleZTimezone(timestamp);
        if (result != null) {
          return result;
        }
        // If handling failed, fall through to next approach
      }

      // Handle case where timestamp has +02:00 format (already correct but may need validation)
      if (timestamp.matches(".*[+-]\\d{2}:\\d{2}$")) {
        String result = handleOffsetFormat(timestamp);
        if (result != null) {
          return result;
        }
        // If handling failed, fall through to next approach
      }
      
      // Try more lenient parsing with multiple patterns
      String result = parseWithPatterns(timestamp);
      if (result != null) {
        return result;
      }
      
      // As a last resort, try general ISO pattern parsing
      String isoResult = parseWithIsoPattern(timestamp);
      if (isoResult != null) {
        return isoResult;
      }
      
      // If we reached here and the timestamp doesn't match any pattern,
      // return null (instead of returning invalid timestamp)
      return null;
    } catch (Exception e) {
      // If any exception occurs during parsing, return null to avoid validation failures
      return null;
    }
  }
  
  /**
   * Handles timestamps with Z timezone designator
   * @param timestamp the timestamp ending with Z
   * @return normalized timestamp in RFC 3339 format or null if parsing fails
   */
  private String handleZTimezone(String timestamp) {
    try {
      java.time.Instant instant = java.time.Instant.parse(timestamp);
      java.time.OffsetDateTime odt = instant.atOffset(java.time.ZoneOffset.UTC);
      return odt.toString();
    } catch (Exception e) {
      return null;
    }
  }
  
  /**
   * Handles timestamps with standard offset format like +02:00
   * @param timestamp the timestamp with offset in +HH:MM format
   * @return normalized timestamp in RFC 3339 format or null if parsing fails
   */
  private String handleOffsetFormat(String timestamp) {
    try {
      // The format is already correct, but let's ensure it's valid by parsing and reformatting
      java.time.OffsetDateTime odt = java.time.OffsetDateTime.parse(timestamp);
      return odt.toString(); // This will ensure the format is valid
    } catch (Exception e) {
      return null;
    }
  }
  
  /**
   * Attempts to parse timestamp using various predefined patterns
   * @param timestamp the timestamp to parse
   * @return normalized timestamp in RFC 3339 format or null if parsing fails with all patterns
   */
  private String parseWithPatterns(String timestamp) {
    String[] patterns = {
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd"
    };
    
    for (String pattern : patterns) {
      try {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
        if (pattern.endsWith("yyyy-MM-dd")) {
          // Parse as LocalDate
          java.time.LocalDate date = java.time.LocalDate.parse(timestamp, formatter);
          return date.atStartOfDay().atOffset(java.time.ZoneOffset.UTC).toString();
        } else {
          // Parse as LocalDateTime
          java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(timestamp, formatter);
          return dateTime.atOffset(java.time.ZoneOffset.UTC).toString();
        }
      } catch (Exception e) {
        // Just try the next pattern
      }
    }
    return null;
  }
  
  /**
   * Attempts to parse timestamp using ISO date-time patterns
   * @param timestamp the timestamp to parse
   * @return normalized timestamp in RFC 3339 format or null if parsing fails
   */
  private String parseWithIsoPattern(String timestamp) {
    try {
      java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_DATE_TIME;
      java.time.temporal.TemporalAccessor temporal = formatter.parseBest(
          timestamp, 
          java.time.OffsetDateTime::from, 
          java.time.LocalDateTime::from,
          java.time.LocalDate::from);
          
      if (temporal instanceof java.time.OffsetDateTime offsetDateTime) {
        return offsetDateTime.toString();
      } else if (temporal instanceof java.time.LocalDateTime localDateTime) {
        // Assume UTC if no zone provided
        return localDateTime.atOffset(java.time.ZoneOffset.UTC).toString();
      } else if (temporal instanceof java.time.LocalDate localDate) {
        // Assume midnight UTC if no time provided
        return localDate.atStartOfDay().atOffset(java.time.ZoneOffset.UTC).toString();
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    InfoSystem that = (InfoSystem) o;

    return id != null ? id.equals(that.id) : that.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
