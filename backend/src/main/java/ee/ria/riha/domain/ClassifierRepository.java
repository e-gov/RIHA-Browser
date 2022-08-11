package ee.ria.riha.domain;

import java.util.List;

import ee.ria.riha.client.StorageClient;
import ee.ria.riha.service.util.Filterable;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ee.ria.riha.domain.model.Classifier;

@Service
public class ClassifierRepository {

	private static final String CLASSIFIER_PATH = "db/classifier";

	@Autowired
	private StorageClient storageClient;

	@Cacheable("allClassifiers")
	public List<Classifier> findAll() {
		return storageClient.find(CLASSIFIER_PATH, Classifier.class);
	}

	@Cacheable("listClassifiers")
	public PagedResponse<Classifier> list(Pageable pageable, Filterable filterable) {
		return storageClient.list(CLASSIFIER_PATH, pageable, filterable, Classifier.class);
	}
}
