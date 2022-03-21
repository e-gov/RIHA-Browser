package ee.ria.riha.client;

/**
 * List of RIHA-Storage error codes
 *
 * @author Valentin Suhnjov
 */
public class StorageErrorCode {

    // techincal errors
    public static final int CLIENT_TIMEOUT = 3;
    public static final int DB_CONNECTION_ERROR = 4;

    // error codes general
    public static final int INPUT_CAN_NOT_FIND_COLUMN = 13;
    public static final int INPUT_UNKNOWN_OBJECT_TYPE_REQUESTED = 14;
    public static final int INPUT_NO_OBJECT_FOUND_WITH_GIVEN_ID = 15;

    // URL parsing related error codes
    public static final int INPUT_FILTER_MUST_HAVE_3_ITEMS_PER_GROUP = 16;
    public static final int INPUT_URL_OP_VALUE_UNKNOWN_OR_NOTSUITABLE = 17;
    public static final int INPUT_URL_PATH_VALUE_NOTVALID = 18;
    public static final int INPUT_URL_REQUIRED_ATTRIBUTES_MISSING = 19;

    // JSON parsing related error codes
    public static final int INPUT_JSON_MISSING = 20;
    public static final int INPUT_JSON_NOT_VALID_JSON = 21;
    public static final int INPUT_JSON_REQUIRED_PROPERTIES_MISSING = 22;
    public static final int INPUT_JSON_LIST_ERRORS = 23;
    public static final int INPUT_JSON_PATH_VALUE_NOTVALID = 24;
    public static final int INPUT_JSON_OP_VALUE_UNKNOWN = 25;
    public static final int INPUT_JSON_ARRAY_RECEIVED_BUT_CAN_ACCEPT_SINGLE_JSON_OBJ_ONLY = 26;
    public static final int INPUT_JSON_GENERAL_SOMETHING_MISSING = 27;
    public static final int JSON_TYPE_ERROR = 28;
    public static final int UPDATE_ID_MISSING = 29;
    public static final int FILTER_OP_VALUE_MUST_BE_ARRAY = 33;

    // authentication problems
    public static final int NO_HTTP_AUTH_TOKEN_PROVIDED = 50;
    public static final int NO_AUTH_TOKEN_PROVIDED = 51;
    public static final int AUTH_TOKEN_INVALID = 52;
    public static final int CANT_CONNECT_TO_AUTH = 53;
    public static final int THIRD_PARTY_AUTH_TOKEN_INVALID = 54;
    public static final int DOCUMENT_FILE_READ_ERROR = 55;
    public static final int DOCUMENT_CREATE_HAS_NO_REF = 56;
    public static final int DOCUMENT_FILE_NOT_FOUND = 57;
    public static final int INPUT_EXPECTED_INTEGER = 60;
    public static final int URL_ENCODING_PERCENT = 61;
    public static final int DATE_FORMAT_ERROR = 62;
    public static final int KIND_NOT_FOUND = 65;
    public static final int VARCHAR_TOO_LONG_ERROR = 68;
    public static final int SQL_TRIGGER_ERROR = 69;
    public static final int SQL_ERROR = 70;
    public static final int FOREIGN_KEY_VIOLATION = 71;
    public static final int CONSTRAINT_VIOLATION = 72;
    public static final int SQL_NO_SUCH_OPERATOR_EXISTS = 73;
    public static final int CAN_UPDATE_VERSION_HERE = 74;
    public static final int NO_ITEM_WITH_URI_FOUND = 75;
    public static final int VERSION_MUST_BE_UPDATED = 76;
    public static final int CANT_UPDATE_ARCHIVED = 77;
    public static final int CANT_CREATE_NEW_VERSION = 78;
    public static final int TABLE_CANT_BE_MODIFIED = 80;
    public static final int WRONG_TABLE_FULL_SERVICE = 81;
    public static final int NOT_AUTHORIZED_FOR_CREATE = 90;
    public static final int NOT_AUTHORIZED_FOR_READ = 91;
    public static final int NOT_AUTHORIZED_FOR_UPDATE = 92;
    public static final int NOT_AUTHORIZED_FOR_DELETE = 93;
    public static final int NOT_AUTHORIZED_NO_REF_MAIN_RESOURECE = 94;

    // special errors
    public static final int UNMAPPED_PROBLEM = 30;
    public static final int THIS_PART_NOT_IMPLEMENTED_YET = 31;

    // HTTP codes
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;

    private StorageErrorCode() {
    }

}
