package ee.ria.riha.domain.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class InfoSystemDocumentMetadataTest {
    @Test
    public void detectsChangesIfAccessRestrictionWasChanged() {
        InfoSystemDocumentMetadata prev = new InfoSystemDocumentMetadata();
        prev.setName("document1");
        prev.setUrl("https://example.com/document1");
        prev.setAccessRestricted(true);
        ObjectNode accessRestrictionNode = JsonNodeFactory.instance.objectNode()
                .put("startDate", "2023-12-18")
                .put("endDate", "2018-12-18")
                .put("reasonCode", "19");
        accessRestrictionNode.putObject("organization").put("code", "12345").put("name", "test");
        prev.setAccessRestrictionJson(accessRestrictionNode);

        InfoSystemDocumentMetadata current = new InfoSystemDocumentMetadata();
        current.setName("document1");
        current.setUrl("https://example.com/document1");
        current.setAccessRestricted(true);
        ObjectNode accessRestrictionNode2 = JsonNodeFactory.instance.objectNode()
                .put("startDate", "2024-12-18")
                .put("endDate", "2018-12-18")
                .put("reasonCode", "19");
        accessRestrictionNode2.putObject("organization").put("code", "12345").put("name", "test");
        current.setAccessRestrictionJson(accessRestrictionNode2);

        assertThat(current.wasChanged(prev), equalTo(true));
    }

    @Test
    public void detectsChangesIfNameWasChanged() {
        InfoSystemDocumentMetadata prev = new InfoSystemDocumentMetadata();
        prev.setName("document1");
        prev.setUrl("https://example.com/document1");

        InfoSystemDocumentMetadata current = new InfoSystemDocumentMetadata();
        current.setName("document2");
        current.setUrl("https://example.com/document1");

        assertThat(current.wasChanged(prev), equalTo(true));
    }
}