package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;

public class SchemaV4Validator extends SchemaValidator<SchemaV4> {

    public SchemaV4Validator(SchemaV4 schema) {
        super(schema);
    }

    //TODO
    @Override
    protected boolean doValidate(JsonElement el, StringBuilder sb) {
        return true;
    }
}
