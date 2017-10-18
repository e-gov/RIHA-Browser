package ee.ria.riha.domain.model;

import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.util.Assert;

import java.util.UUID;

import static org.json.JSONPointer.builder;
import static org.springframework.util.StringUtils.hasText;

/**
 * Holds Information System data as a {@link JSONObject} and provides accessors for JSON manipulation.
 *
 * @author Valentin Suhnjov
 */
public class InfoSystem {

    private static final String ID_KEY = "main_resource_id";
    private static final String UUID_KEY = "uuid";
    private static final String OWNER_KEY = "owner";
    private static final String OWNER_NAME_KEY = "name";
    private static final String OWNER_CODE_KEY = "code";
    private static final String SHORT_NAME_KEY = "short_name";
    private static final String FULL_NAME_KEY = "name";
    private static final String META_KEY = "meta";
    private static final String META_CREATION_TIMESTAMP_KEY = "creation_timestamp";
    private static final String META_UPDATE_TIMESTAMP_KEY = "update_timestamp";

    private JSONObject jsonObject = new JSONObject();

    private Integer id;
    private UUID uuid;
    private String ownerName;
    private String ownerCode;
    private String shortName;
    private String fullName;
    private String creationTimestamp;
    private String updateTimestamp;

    public InfoSystem() {
        this("{}");
    }

    public InfoSystem(JSONObject jsonObject) {
        Assert.notNull(jsonObject);
        this.jsonObject = jsonObject;

        this.id = ((Integer) getPath(ID_KEY).queryFrom(jsonObject));

        String uuidString = ((String) getPath(UUID_KEY).queryFrom(jsonObject));
        this.uuid = hasText(uuidString) ? UUID.fromString(uuidString) : null;

        this.shortName = ((String) getPath(SHORT_NAME_KEY).queryFrom(jsonObject));
        this.fullName = ((String) getPath(FULL_NAME_KEY).queryFrom(jsonObject));

        JSONObject owner = getOwner();
        this.ownerName = ((String) getPath(OWNER_NAME_KEY).queryFrom(owner));
        this.ownerCode = ((String) getPath(OWNER_CODE_KEY).queryFrom(owner));

        this.creationTimestamp = (String) getPath(META_CREATION_TIMESTAMP_KEY).queryFrom(getMeta());
        this.updateTimestamp = (String) getPath(META_UPDATE_TIMESTAMP_KEY).queryFrom(getMeta());
    }

    public InfoSystem(String json) {
        this(new JSONObject(json));
    }

    private JSONPointer getPath(String... parts) {
        JSONPointer.Builder builder = builder();
        for (String part : parts) {
            builder.append(part);
        }

        return builder.build();
    }

    public String asJson() {
        return jsonObject.toString();
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        jsonObject.putOpt(ID_KEY, id);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        jsonObject.putOpt(UUID_KEY, uuid != null ? uuid.toString() : null);
    }

    private JSONObject getOwner() {
        JSONObject owner = ((JSONObject) getPath(OWNER_KEY).queryFrom(jsonObject));

        if (owner == null) {
            owner = new JSONObject();
            jsonObject.put(OWNER_KEY, owner);
        }

        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
        getOwner().putOpt(OWNER_NAME_KEY, name);
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String code) {
        this.ownerCode = code;
        getOwner().putOpt(OWNER_CODE_KEY, code);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
        jsonObject.putOpt(SHORT_NAME_KEY, shortName);
    }

    public String getFullName() {
    	return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        jsonObject.putOpt(FULL_NAME_KEY, fullName);
    }

    private JSONObject getMeta() {
        JSONObject meta = ((JSONObject) getPath(META_KEY).queryFrom(jsonObject));

        if (meta == null) {
            meta = new JSONObject();
            jsonObject.put(META_KEY, meta);
        }

        return meta;
    }

    public String getCreationTimestamp() {
        return this.creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        getMeta().putOpt(META_CREATION_TIMESTAMP_KEY, creationTimestamp);
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
        getMeta().putOpt(META_UPDATE_TIMESTAMP_KEY, updateTimestamp);
    }
}
