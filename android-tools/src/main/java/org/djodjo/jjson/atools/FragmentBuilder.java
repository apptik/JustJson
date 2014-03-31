package org.djodjo.jjson.atools;


import android.os.Bundle;

import org.djodjo.jjson.atools.ui.fragment.BasePropertyFragment;
import org.djodjo.jjson.atools.ui.fragment.BooleanFragment;
import org.djodjo.jjson.atools.ui.fragment.EnumFragment;
import org.djodjo.jjson.atools.ui.fragment.NumberFragment;
import org.djodjo.jjson.atools.ui.fragment.RangeFragment;
import org.djodjo.jjson.atools.ui.fragment.StringFragment;
import org.djodjo.json.LinkedTreeMap;
import org.djodjo.json.schema.Schema;
import org.hamcrest.Matcher;

import java.util.Map;

import static org.djodjo.jjson.atools.matcher.SchemaDefMatchers.isBooleanType;
import static org.djodjo.jjson.atools.matcher.SchemaDefMatchers.isEnum;
import static org.djodjo.jjson.atools.matcher.SchemaDefMatchers.isNumberType;
import static org.djodjo.jjson.atools.matcher.SchemaDefMatchers.isRangeObject;
import static org.djodjo.jjson.atools.matcher.SchemaDefMatchers.isStringType;

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
    public static final int DISPLAY_TYPE_CHECKBOX = 6;
    public static final int DISPLAY_TYPE_SLIDER = 7;
    public static final int DISPLAY_TYPE_NUMBER_PICKER = 8;




    private Schema propertySchema;


    //arguments to be loaded to the created fragment
    protected Bundle args = new Bundle();
    private String title;
    private String description;

    private FragmentBuilder dependsOn;

    private Object value;


    private static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;

    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(isStringType(), StringFragment.class);
        commonPropertyMatchers.put(isBooleanType(),BooleanFragment.class);
        commonPropertyMatchers.put(isEnum(),EnumFragment.class);
        commonPropertyMatchers.put(isNumberType(), NumberFragment.class);
        commonPropertyMatchers.put(isRangeObject(), RangeFragment.class);

    }

    public FragmentBuilder(String label, Schema schema) {
        //label cannot change
        args.putString(BasePropertyFragment.ARG_LABEL, label);
        propertySchema = schema;
    }

    public FragmentBuilder withLayoutId(int layoutId) {
        args.putInt(BasePropertyFragment.ARG_LAYOUT, layoutId);
        return this;
    }

    public FragmentBuilder withDisplayType(int displayType) {
        args.putInt(BasePropertyFragment.ARG_DISPLAY_TYPE, displayType);
        return this;
    }

    public BasePropertyFragment build() {
        BasePropertyFragment fragment = null;


        if(propertySchema.getType().isEmpty()) {
            throw new RuntimeException("No type defined. Fragment builder needs at least one defined type for a property");
        }

        //generate all available arguments, depending on the property type some may not be used
        genFragmentArgs(args, propertySchema);

        //TODO use MIXED fragment

        for(Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
            if(entry.getKey().matches(propertySchema)) {
                try {
                    fragment = (BasePropertyFragment) entry.getValue().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        //fragment can be null in some cases
        if(fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    private static Bundle genFragmentArgs(Bundle args, Schema schema) {
        if(schema == null) return args;

        //args.putStringArrayList(EnumFragment.ARG_OPTIONS, schema.getEnum());
        args.putString(BasePropertyFragment.ARG_TITLE, schema.getTitle());
        args.putString(BasePropertyFragment.ARG_DESC, schema.getDescription());
        args.putString(BasePropertyFragment.ARG_DEFAULT_VAL, schema.getDefault());
        args.putDouble(NumberFragment.ARG_MINIMUM, schema.getMinimum());
        args.putDouble(NumberFragment.ARG_MAXIMUM, schema.getMaximum());

        // --> loop inner(property schemas), used for complex(Object) properties
        // to deliver all sub-arguments to the created fragment

        if(schema.getProperties() != null) {
            for (Map.Entry<String, Schema> property : schema.getProperties()) {
                args.putBundle(property.getKey(), genFragmentArgs(new Bundle(), property.getValue()));
            }
        }

        return args;
    }



}
