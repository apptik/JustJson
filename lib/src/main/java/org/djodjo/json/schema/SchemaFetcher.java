package org.djodjo.json.schema;

import java.net.URI;

public interface SchemaFetcher {
    Schema fetch(URI schemaUri);
}
