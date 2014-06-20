package org.djodjo.json.schema;


import org.djodjo.json.Validator;
import org.djodjo.json.schema.validation.SchemaV4Validator;

public class SchemaV4 extends Schema {

    public SchemaV4() {
        super();
        setSchema("http://json-schema.org/draft-04/schema#");
    }

    @Override
    public Schema getEmptySchema() {
        return new SchemaV4().setSchemaFetcher(schemaFetcher);
    }

    @Override
    public Validator getDefaultValidator() {
        return new SchemaV4Validator(this);
    }
}
