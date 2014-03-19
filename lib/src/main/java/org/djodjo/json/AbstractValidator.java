package org.djodjo.json;


public abstract class AbstractValidator implements Validator {

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
