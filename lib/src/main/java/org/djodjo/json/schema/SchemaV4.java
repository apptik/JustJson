package org.djodjo.json.schema;


import org.djodjo.json.Validator;

public class SchemaV4 extends Schema {

    public SchemaV4() {
        super();
        setSchema("http://json-schema.org/draft-04/schema#");
    }

    @Override
    public Validator getDefaultValidator() {
        return new SchemaV4Validator(this);
    }
}
