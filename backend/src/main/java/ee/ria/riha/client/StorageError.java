package ee.ria.riha.client;

/**
 * RIHA-Storage error message model
 *
 * @author Valentin Suhnjov
 */
public class StorageError {

    private int errcode;
    private String errmsg;
    private String errtrace;

    public StorageError() {
    }

    public StorageError(int errcode, String errmsg, String errtrace) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.errtrace = errtrace;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrtrace() {
        return errtrace;
    }

    public void setErrtrace(String errtrace) {
        this.errtrace = errtrace;
    }

    @Override
    public String toString() {
        return "StorageError{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", errtrace='" + errtrace + '\'' +
                '}';
    }

}
