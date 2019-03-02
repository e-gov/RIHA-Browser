package ee.ria.riha.service;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ria.riha.domain.ClassifierRepository;
import ee.ria.riha.domain.model.Classifier;
import ee.ria.riha.domain.model.ClassifierDiscriminator;

/**
 * @author Valentin Suhnjov
 */
@Service
public class EnvironmentService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassifierRepository classifierRepository;

    private Map<String, Map<String, CodeValuePair>> classifiers;

    public void loadClassifiers() {
        this.classifiers = classifierRepository.findAll().stream()
                .collect(Collectors.groupingBy(Classifier::getType,
                        Collectors.toMap(Classifier::getCode, classifier -> new CodeValuePair(classifier.getCode(), getClassifierValue(classifier)))));
    }

    private String getClassifierValue(Classifier classifier) {
        return ClassifierDiscriminator.JSON == classifier.getDiscriminator() ?
                classifier.getJsonValue() : classifier.getValue();
    }

    public Map<String, Map<String, CodeValuePair>> getClassifiers() {
        if (classifiers == null) {
            loadClassifiers();
        }
        return classifiers;
    }

    /**
     * @deprecated use user service instead
     */
    @Deprecated
    public void changeActiveOrganization(String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
    }

    @Data
    @AllArgsConstructor
    public class CodeValuePair {
        String code;
        String value;
    }
}
