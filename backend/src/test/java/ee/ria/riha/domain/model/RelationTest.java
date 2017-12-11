package ee.ria.riha.domain.model;

import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Valentin Suhnjov
 */
public class RelationTest {

    @Test
    public void reverseShouldBeSymmetric() {
        Relation relation = Relation.builder()
                .id(123L)
                .infoSystemUuid(UUID.randomUUID())
                .infoSystemName("Info System")
                .infoSystemShortName("is-1")
                .relatedInfoSystemUuid(UUID.randomUUID())
                .relatedInfoSystemName("Related Info System")
                .relatedInfoSystemShortName("ris-2")
                .creationDate(new Date())
                .modifiedDate(new Date())
                .type(RelationType.SUB_SYSTEM)
                .build();

        Relation symmetricRelation = relation.reverse().reverse();

        assertEqual(symmetricRelation, relation);
    }

    private void assertEqual(Relation actual, Relation expected) {
        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getInfoSystemUuid(), is(equalTo(expected.getInfoSystemUuid())));
        assertThat(actual.getInfoSystemName(), is(equalTo(expected.getInfoSystemName())));
        assertThat(actual.getInfoSystemShortName(), is(equalTo(expected.getInfoSystemShortName())));
        assertThat(actual.getRelatedInfoSystemUuid(), is(equalTo(expected.getRelatedInfoSystemUuid())));
        assertThat(actual.getRelatedInfoSystemName(), is(equalTo(expected.getRelatedInfoSystemName())));
        assertThat(actual.getRelatedInfoSystemShortName(), is(equalTo(expected.getRelatedInfoSystemShortName())));
        assertThat(actual.getCreationDate(), is(equalTo(expected.getCreationDate())));
        assertThat(actual.getModifiedDate(), is(equalTo(expected.getModifiedDate())));
        assertThat(actual.getType(), is(equalTo(expected.getType())));
    }

}