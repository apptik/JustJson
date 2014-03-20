package org.djodjo.json.schema;


import java.net.URI;


//TODO
public class SchemaLocalFetcher implements SchemaFetcher {

    @Override
    public Schema fetch(URI schemaUri) {
        return new SchemaV4();
    }
}
