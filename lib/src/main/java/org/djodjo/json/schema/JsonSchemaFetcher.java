package org.djodjo.json.schema;

import java.net.URI;

public interface JsonSchemaFetcher {
    JsonSchema fetch(URI schemaUri);
}
