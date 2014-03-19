package org.djodjo.json.schema;


import org.djodjo.json.AbstractValidator;

public abstract class JsonSchemaValidator<T extends JsonSchema> extends AbstractValidator {

    T schema;

    public  JsonSchemaValidator(T schema) {
        this.schema = schema;
    }

}
