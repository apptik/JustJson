package org.djodjo.json;


public abstract class AbstractValidator implements Validator {

    @Override
    public boolean isValid(JsonElement el) {
        //System.out.println("Started simple JsonSchema Validation using: " + this.getTitle());
        return doValidate(el, null);
    }

    @Override
    public String validate(JsonElement el) {
        //System.out.println("Started JsonSchema Validation using: " + this.getTitle());
        StringBuilder sb = new StringBuilder();
        doValidate(el, sb);
        return sb.toString();
    }

    @Override
    public boolean validate(JsonElement el, StringBuilder sb) {
        //System.out.println("Started full JsonSchema Validation using: " + this.getTitle());
        return doValidate(el, sb);
    }

    protected abstract boolean doValidate(JsonElement el, StringBuilder sb);

    @Override
    public String getTitle() {
        return getClass().getCanonicalName();
    }
}

