package ee.ria.riha.domain.model;

/**
 * Types of resolution for issue that implicitly request such resolution.
 *
 * @author Valentin Suhnjov
 */
public enum IssueResolutionType {

    /**
     * Indicates that issue was positively resolved
     */
    POSITIVE,

    /**
     * Indicates that issue resolution was negative
     */
    NEGATIVE,

    /**
     * Indicates that resolution will not be given
     */
    DISMISSED

}
