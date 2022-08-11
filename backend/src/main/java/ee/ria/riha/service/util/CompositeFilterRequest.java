package ee.ria.riha.service.util;

import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
public class CompositeFilterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> filterParameters = new ArrayList<>();
    private final List<String> sortParameters = new ArrayList<>();

    public CompositeFilterRequest() {
    }

    public CompositeFilterRequest(List<String> filterParameters, List<String> sortParameters) {
        if (filterParameters != null) {
            this.filterParameters.addAll(filterParameters);
        }

        if (sortParameters != null) {
            this.sortParameters.addAll(sortParameters);
        }
    }

    public void addFilterParameter(String filterParameter) {
        filterParameters.add(filterParameter);
    }

    public CompositeFilterRequest addSortParameter(String sortParameter) {
        sortParameters.add(sortParameter);
        return this;
    }

    public CompositeFilterRequest addFilterParameters(List<String> filterParameters) {
        this.filterParameters.addAll(filterParameters);
        return this;
    }

    public CompositeFilterRequest addSortParameters(List<String> sortParameters) {
        this.sortParameters.addAll(sortParameters);
        return this;
    }

    public List<String> getFilterParameters() {
        return new ArrayList<>(filterParameters);
    }

    public List<String> getSortParameters() {
        return new ArrayList<>(sortParameters);
    }

}
