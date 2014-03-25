package org.djodjo.jjson.atools;


import android.app.Fragment;

import java.util.ArrayList;

/**
 * View Builder that is used to generate all vies from JsonSchema describing a JsonObject
 *
 * View builders are created in the order the properties are found in the definition
 *
 * if enums are defined inside "oneOf, anyOf" there is the possibility that the other fields may depends on them
 */
public class FragmentBuilder {

    private ArrayList<String> type;
    private String label;
    private String title;
    private String description;

    private FragmentBuilder dependsOn;

    private Object value;

    public FragmentBuilder(String label) {
        this.label = label;
    }

    public Fragment build() {
        Fragment fragment = JsonFragment.newInstance("", "");


        return fragment;
    }

}
