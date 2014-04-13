package org.djodjo.json.android.fragment;


/**
 * Called from the anyOf, allOf, oneOf enum controller fragments
 * by default this is implemented in the JsonSchemaFragment
 */
public interface ControllerCallback {

    void onValueChanged(String propertyName, int position);
}
