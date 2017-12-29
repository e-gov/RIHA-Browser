package ee.ria.riha.domain.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InfoSystemTest {

    private InfoSystem validInfoSystem = new InfoSystem();

    @Before
    public void setUp() {
        validInfoSystem.setId(123L);
        validInfoSystem.setUuid(UUID.fromString("53524f32-b732-4ce6-99a8-448d931d870d"));
        validInfoSystem.setFullName("Rebaste register");
        validInfoSystem.setShortName("fox");
        validInfoSystem.setOwnerCode("12345");
        validInfoSystem.setOwnerName("Rebane");
        validInfoSystem.setCreationTimestamp("2017-12-19T12:13:14.137+02:00");
        validInfoSystem.setUpdateTimestamp("2017-12-19T15:16:17.137+02:00");
        validInfoSystem.addContact("contact1", "contact1@example.com");
        validInfoSystem.addContact("contact2", "contact2@example.com");
        validInfoSystem.setLastPositiveApprovalRequestType(IssueType.ESTABLISHMENT_REQUEST);
        validInfoSystem.setLastPositiveApprovalRequestDate(new Date());
        validInfoSystem.setLastPositiveEstablishmentRequestDate(new Date());
        validInfoSystem.setLastPositiveTakeIntoUseRequestDate(new Date());
        validInfoSystem.setLastPositiveFinalizationRequestDate(new Date());
    }

    @Test
    public void retrievedUuid() {
        assertThat(validInfoSystem.getUuid(), equalTo(UUID.fromString("53524f32-b732-4ce6-99a8-448d931d870d")));
    }

    @Test
    public void returnsNullWhenUuidNotSpecified() {
        assertThat(new InfoSystem().getUuid(), nullValue());
    }

    @Test
    public void retrievesOwnerName() {
        assertThat(validInfoSystem.getOwnerName(), equalTo("Rebane"));
    }

    @Test
    public void returnsNullWhenOwnerNameNotSpecified() {
        assertThat(new InfoSystem().getOwnerName(), nullValue());
    }

    @Test
    public void retrievesOwnerCode() {
        assertThat(validInfoSystem.getOwnerCode(), equalTo("12345"));
    }

    @Test
    public void returnsNullWhenOwnerCodeNotSpecified() {
        assertThat(new InfoSystem().getOwnerCode(), nullValue());
    }

    @Test
    public void retrievesFullName() {
        assertThat(validInfoSystem.getFullName(), equalTo("Rebaste register"));
    }

    @Test
    public void returnsNullWhenFullnameNotSpecified() {
        assertThat(new InfoSystem().getFullName(), nullValue());
    }

    @Test
    public void retrievesShortName() {
        assertThat(validInfoSystem.getShortName(), equalTo("fox"));
    }

    @Test
    public void returnsNullWhenShortNameNotSpecified() {
        assertThat(new InfoSystem().getShortName(), nullValue());
    }

    @Test
    public void retrievesCreationTimestamp() {
        assertThat(validInfoSystem.getCreationTimestamp(), equalTo("2017-12-19T12:13:14.137+02:00"));
    }

    @Test
    public void returnsNullWhenCreationTimeNotSpecified() {
        assertThat(new InfoSystem().getCreationTimestamp(), nullValue());
    }

    @Test
    public void retrievesUpdateTimestamp() {
        assertThat(validInfoSystem.getUpdateTimestamp(), equalTo("2017-12-19T15:16:17.137+02:00"));
    }

    @Test
    public void returnsNullWhenUpdateTimeNotSpecified() {
        assertThat(new InfoSystem().getUpdateTimestamp(), nullValue());
    }

    @Test
    public void retrievesContactEmails() {
        assertThat(validInfoSystem.getContactsEmails(),
                containsInAnyOrder("contact1@example.com", "contact2@example.com"));
    }

    @Test
    public void returnsEmptyListWhenContactsNotSpecified() {
        assertThat(new InfoSystem().getContactsEmails(), empty());
    }

    @Test
    public void copiesPropertiesAndMakesJsonDeepCopy() {
        InfoSystem copy = validInfoSystem.copy();

        assertThat(copy.getId(), equalTo(validInfoSystem.getId()));
        assertThat(copy.getLastPositiveApprovalRequestDate(),
                equalTo(validInfoSystem.getLastPositiveApprovalRequestDate()));
        assertThat(copy.getLastPositiveApprovalRequestType(),
                equalTo(validInfoSystem.getLastPositiveApprovalRequestType()));
        assertThat(copy.getLastPositiveEstablishmentRequestDate(),
                equalTo(validInfoSystem.getLastPositiveEstablishmentRequestDate()));
        assertThat(copy.getLastPositiveTakeIntoUseRequestDate(),
                equalTo(validInfoSystem.getLastPositiveTakeIntoUseRequestDate()));
        assertThat(copy.getLastPositiveFinalizationRequestDate(),
                equalTo(validInfoSystem.getLastPositiveFinalizationRequestDate()));
        assertThat(copy.getJsonContent(), not(sameInstance(validInfoSystem.getJsonContent())));
        assertThat(copy.getJsonContent(), equalTo(validInfoSystem.getJsonContent()));
    }

}