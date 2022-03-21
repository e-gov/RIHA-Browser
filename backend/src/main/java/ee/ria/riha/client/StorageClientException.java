package ee.ria.riha.client;

/**
 * Indicates that RIHA-Storage produced error REST response
 *
 * @author Valentin Suhnjov
 */
public class StorageClientException extends RuntimeException {

    private final StorageError storageError;

    public StorageClientException(StorageError rihaRestError) {
        super();
        this.storageError = rihaRestError;
    }

    public StorageError getStorageError() {
        return storageError;
    }
}
