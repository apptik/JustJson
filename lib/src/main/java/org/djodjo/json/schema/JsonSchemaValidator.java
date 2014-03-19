package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;
import org.djodjo.json.Validator;

public abstract class JsonSchemaValidator<T extends JsonSchema> implements Validator {

    T schema;

    public  JsonSchemaValidator(T schema) {
        this.schema = schema;
    }

    @Override
    public boolean isValid(JsonElement el) {
        return doValidate(el, null);
    }


    @Override
    public String validate(JsonElement el) {
        StringBuilder sb = new StringBuilder();
        doValidate(el, sb);
        return sb.toString();
    }

    @Override
    public boolean validate(JsonElement el, StringBuilder sb) {
        return doValidate(el, sb);
    }

    protected abstract boolean doValidate(JsonElement el, StringBuilder sb);
}
