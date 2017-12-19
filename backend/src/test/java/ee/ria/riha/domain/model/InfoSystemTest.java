package ee.ria.riha.domain.model;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class InfoSystemTest {

    private String validJson = "{\n" +
            "  \"name\": \"Rebaste register\",\n" +
            "  \"shortname\": \"fox\",\n" +
            "  \"owner\": {\n" +
            "    \"code\": \"12345\",\n" +
            "    \"name\": \"Rebane\"\n" +
            "  },\n" +
            "  \"documentation\": \"http://riha.eesti.ee\",\n" +
            "  \"meta\": {\n" +
            "    \"system_status\": {\n" +
            "      \"timestamp\": \"2016-12-13T17:10:20.785\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"contacts\": [\n" +
            "    {\n" +
            "      \"name\": \"contact1\",\n" +
            "      \"email\": \"contact1@example.com\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"contact2\",\n" +
            "      \"email\": \"contact2@example.com\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"uri\": \"http://base.url/fox\",\n" +
            "  \"uuid\": \"53524f32-b732-4ce6-99a8-448d931d870d\"\n" +
            "}";

    @Test
    public void allInfoSystemPropertiesAreNotRequired() {
        InfoSystem infoSystem = new InfoSystem("{}");

        assertNull(infoSystem.getId());
        assertNull(infoSystem.getUuid());
        assertNull(infoSystem.getOwnerCode());
        assertNull(infoSystem.getOwnerName());
    }

    @Test
    public void deserializeFromJson() {
        InfoSystem infoSystem = new InfoSystem(validJson);

        assertEquals(UUID.fromString("53524f32-b732-4ce6-99a8-448d931d870d"), infoSystem.getUuid());
        assertEquals("12345", infoSystem.getOwnerCode());
        assertEquals("Rebane", infoSystem.getOwnerName());
    }

    @Test
    public void setsProperties() {
        //language=JSON
        String expectedJson = "{\n" +
                "  \"owner\":\n" +
                "  {\n" +
                "    \"code\": \"123\",\n" +
                "    \"name\": \"Rebane\"\n" +
                "  },\n" +
                "  \"uuid\": \"53524f32-b732-4ce6-99a8-448d931d870d\"\n" +
                "}";

        InfoSystem infoSystem = new InfoSystem("{}");

        infoSystem.setUuid(UUID.fromString("53524f32-b732-4ce6-99a8-448d931d870d"));
        infoSystem.setOwnerCode("123");
        infoSystem.setOwnerName("Rebane");

        JSONAssert.assertEquals(expectedJson, infoSystem.getJsonContent().toString(), false);
    }

    @Test
    public void doesNotFailWhenOwnerIsNull() {
        String json = "{\n" +
                "  \"owner\": null\n" +
                "}";
        InfoSystem infoSystem = new InfoSystem(json);

        assertThat(infoSystem.getOwnerCode(), is(nullValue()));
        assertThat(infoSystem.getOwnerName(), is(nullValue()));
    }

    @Test
    public void doesNotFailWhenOwnerCodeOrNameIsNull() {
        String json = "{\n" +
                "  \"owner\": {\n" +
                "    \"code\": null,\n" +
                "    \"name\": null\n" +
                "  }\n" +
                "}";
        InfoSystem infoSystem = new InfoSystem(json);

        assertThat(infoSystem.getOwnerCode(), is(nullValue()));
        assertThat(infoSystem.getOwnerName(), is(nullValue()));
    }
}