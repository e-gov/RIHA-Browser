package ee.ria.riha.domain.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Classifier implements Serializable {

	private static final long serialVersionUID = 1L;

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
