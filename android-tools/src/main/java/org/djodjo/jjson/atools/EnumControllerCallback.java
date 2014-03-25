package org.djodjo.jjson.atools;


/**
 * Called from the anyOf, allOf, oneOf enum controller fragments
 * by default this is implemented in the JsonSchemaFragment
 */
public interface EnumControllerCallback {

    void onValueChanged(int position, String value);
}
