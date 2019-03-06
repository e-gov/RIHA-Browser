package ee.ria.riha.domain.model;

/**
 * @author Valentin Suhnjov
 */
public enum IssueType {

    /**
     * Request approval to establish information system
     */
    ESTABLISHMENT_REQUEST,

    /**
     * Request approval to take system into use
     */
    TAKE_INTO_USE_REQUEST,

    /**
     * Request approval to modify information system data
     */
    MODIFICATION_REQUEST,

    /**
     * Request approval to finalize information system
     */
    FINALIZATION_REQUEST,


    /**
     * syntetic issue type
     */
    AUTOMATICALLY_REGISTERED
}
