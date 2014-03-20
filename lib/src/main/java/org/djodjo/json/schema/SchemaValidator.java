package org.djodjo.json.schema;


import org.djodjo.json.AbstractValidator;

public abstract class SchemaValidator<T extends Schema> extends AbstractValidator {

    T schema;

    public SchemaValidator(T schema) {
        this.schema = schema;
    }

}
