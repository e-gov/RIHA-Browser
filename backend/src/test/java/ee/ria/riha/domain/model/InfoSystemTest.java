package ee.ria.riha.domain.model;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InfoSystemTest {

    @Test
    public void allInfoSystemPropertiesAreNotRequired() {
        InfoSystem infoSystem = new InfoSystem(new JSONObject("{}"));

        assertNull(infoSystem.getId());
        assertNull(infoSystem.getUuid());
        assertNull(infoSystem.getOwnerCode());
        assertNull(infoSystem.getOwnerName());
    }

    @Test
    public void deserializeFromJson() {
        //language=JSON
        String json =
                "{\n" +
                        "  \"name\": \"Rebaste register\",\n" +
                        "  \"shortname\": \"fox\",\n" +
                        "  \"owner\": {\n" +
                        "    \"code\": \"12345\",\n" +
                        "    \"name\": \"Rebane\"\n" +
                        "  },\n" +
                        "  \"main_resource_id\": 357,\n" +
                        "  \"documentation\": \"http://riha.eesti.ee\",\n" +
                        "  \"meta\": {\n" +
                        "    \"system_status\": {\n" +
                        "      \"timestamp\": \"2016-12-13T17:10:20.785\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"uri\": \"http://base.url/fox\",\n" +
                        "  \"uuid\": \"53524f32-b732-4ce6-99a8-448d931d870d\"\n" +
                        "}";

        InfoSystem infoSystem = new InfoSystem(new JSONObject(json));

        assertEquals((Integer) 357, infoSystem.getId());
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
                "  \"main_resource_id\": 357,\n" +
                "  \"uuid\": \"53524f32-b732-4ce6-99a8-448d931d870d\"\n" +
                "}";

        InfoSystem infoSystem = new InfoSystem("{}");

        infoSystem.setId(357);
        infoSystem.setUuid(UUID.fromString("53524f32-b732-4ce6-99a8-448d931d870d"));
        infoSystem.setOwnerCode("123");
        infoSystem.setOwnerName("Rebane");

        JSONAssert.assertEquals(new JSONObject(expectedJson), infoSystem.getJsonObject(), false);
    }

}