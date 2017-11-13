package ee.ria.riha.domain.model;

/**
 * Type of info system relationship.
 *
 * @author Valentin Suhnjov
 */
public enum RelationType {

    SUB_SYSTEM,
    SUPER_SYSTEM;

    static {
        SUB_SYSTEM.opposite = SUPER_SYSTEM;
        SUPER_SYSTEM.opposite = SUB_SYSTEM;
    }

    private RelationType opposite;

    public RelationType getOpposite() {
        return opposite;
    }

}
