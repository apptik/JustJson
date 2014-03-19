package org.djodjo.json.schema;


import java.net.URI;


//TODO
public class JsonSchemaLocalFetcher implements JsonSchemaFetcher{

    @Override
    public JsonSchema fetch(URI schemaUri) {
        return new JsonSchemaV4();
    }
}
