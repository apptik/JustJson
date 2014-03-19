package org.djodjo.json;


public interface Validator {

    /**
     * Fast check is JsonElement is valid
     * @param el the JsonElemnt being validated
     * @return valid or not valid
     */
    public boolean isValid(JsonElement el);

    /**
     * Runs a full validation and returns a result
     * @param el the JsonElemnt being validated
     * @return huyman readable result of the validation
     */
    public String validate(JsonElement el);

    boolean validate(JsonElement el, StringBuilder sb);
}
