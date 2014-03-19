package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;

public class JsonSchemaV4Validator extends JsonSchemaValidator<JsonSchemaV4> {

    public JsonSchemaV4Validator(JsonSchemaV4 schema) {
        super(schema);
    }

    //TODO
    @Override
    protected boolean doValidate(JsonElement el, StringBuilder sb) {
        return true;
    }
}
