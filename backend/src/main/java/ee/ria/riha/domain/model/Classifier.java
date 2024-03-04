package ee.ria.riha.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class Classifier {

	private int id;
	private String type;
	private String code;
	private String value;
	private String jsonValue;
	private ClassifierDiscriminator discriminator;
	private String description;
	private Date creationDate;
	private Date modifiedDate;
}
