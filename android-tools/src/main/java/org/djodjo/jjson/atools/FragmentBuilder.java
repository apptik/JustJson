package org.djodjo.jjson.atools;


import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * View Builder that is used to generate all vies from JsonSchema describing a JsonObject
 *
 * View builders are created in the order the properties are found in the definition
 *
 * if enums are defined inside "oneOf, anyOf" there is the possibility that the other fields may depends on them
 */
public class FragmentBuilder {

    public static final int DISPLAY_TYPE_TEXT= 0;
    public static final int DISPLAY_TYPE_RADIO = 1;
    public static final int DISPLAY_TYPE_SPINNER = 2;
    public static final int DISPLAY_TYPE_LISTVIEW = 3;
    public static final int DISPLAY_TYPE_TOGGLE = 4;
    public static final int DISPLAY_TYPE_SWITCH = 5;
    public static final int DISPLAY_TYPE_SLIDER = 6;
    public static final int DISPLAY_TYPE_NUMBER_PICKER = 7;

    public static final String TYPE_STRING = "string";
    public static final String TYPE_ENUM = "enum";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_OBJECT = "object";

    public static final String ARG_LABEL = "label";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESC = "description";
    public static final String ARG_LAYOUT = "layout_id";

    public static final String ARG_DISPLAY_TYPE = "display_type";

    public static final String ARG_OPTIONS = "options";

    private HashSet<String> type = new HashSet<String>();
    private ArrayList<String> options = new ArrayList<String>();


    protected Bundle args = new Bundle();
    private String title;
    private String description;

    private FragmentBuilder dependsOn;

    private Object value;

    public FragmentBuilder(String label) {
        args.putString(ARG_LABEL, label);
    }

    public FragmentBuilder addType(String type) {
        this.type.add(type);
        return this;
    }

    public FragmentBuilder addType(ArrayList<String> types) {
        if(types != null) {
            for (String propType:types) {
                this.type.add(propType);
            }
        }
        return this;
    }

    public FragmentBuilder addOption(String option) {
        this.options.add(option);
        return this;
    }

    public FragmentBuilder withTitle(String title) {
        args.putString(ARG_TITLE, title);
        return this;
    }

    public FragmentBuilder withDescription(String description) {
        args.putString(ARG_DESC, description);
        return this;
    }

    public FragmentBuilder withLayoutId(int layoutId) {
        args.putInt(ARG_LAYOUT, layoutId);
        return this;
    }

    public FragmentBuilder withDisplayType(int displayType) {
        args.putInt(ARG_DISPLAY_TYPE, displayType);
        return this;
    }

    public Fragment build() {
        BasePropertyFragment fragment = null;
        args.putStringArrayList(ARG_OPTIONS, options);

        if(type.isEmpty()) {
            throw new RuntimeException("No type defined. Fragment builder needs at least one defined type for a property");
        }

        if(type.size()==1) {
            if(type.contains(TYPE_STRING)) {

            }
            if(type.contains(TYPE_ENUM)) {
                fragment = new EnumFragment();
            }
        } else {
            //TODO use MIXED fragment
        }

        fragment.setArguments(args);
        return fragment;
    }

}
