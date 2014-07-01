package org.djodjo.json.schema;


import org.djodjo.json.Validator;
import org.djodjo.json.schema.validation.SchemaV5Validator;

public class SchemaV5 extends Schema {

    public SchemaV5() {
        super();
        setSchema("http://json-schema.org/draft-05/schema#");
    }

    @Override
    public Schema getEmptySchema() {
        return new SchemaV5().setSchemaFetcher(schemaFetcher);
    }

    @Override
    public Validator getDefaultValidator() {
        return new SchemaV5Validator(this);
    }
}
