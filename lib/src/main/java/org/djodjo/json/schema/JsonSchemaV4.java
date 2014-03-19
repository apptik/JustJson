package org.djodjo.json.schema;


import java.net.URI;
import java.net.URISyntaxException;

public class JsonSchemaV4 extends JsonSchema {

    public JsonSchemaV4() {
        super();
        try {
            this.setJsonSchema(new URI("http://json-schema.org/draft-04/schema#"));
        } catch (URISyntaxException e) {
        }
    }
}
