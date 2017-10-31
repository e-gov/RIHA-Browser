package ee.ria.riha.web;

/**
 * Interface for classes that map entity to model.
 *
 * @param <E> - source entity type
 * @param <R> - resulting model type
 */
public interface ModelMapper<E, R> {

    /**
     * Maps entity to model.
     *
     * @param value entity
     * @return mapped model
     */
    R map(E value);

}
