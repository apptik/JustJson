/*
 * Copyright (C) 2014 Kalin Maldzhanski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.djodjo.jjson.atools;


import android.os.Bundle;

import org.djodjo.jjson.atools.ui.fragment.BasePropertyFragment;
import org.djodjo.jjson.atools.ui.fragment.BooleanFragment;
import org.djodjo.jjson.atools.ui.fragment.EnumFragment;
import org.djodjo.jjson.atools.ui.fragment.NumberFragment;
import org.djodjo.jjson.atools.ui.fragment.RangeFragment;
import org.djodjo.jjson.atools.ui.fragment.StringFragment;
import org.djodjo.jjson.atools.util.FragmentTools;
import org.djodjo.json.LinkedTreeMap;
import org.djodjo.json.schema.Schema;
import org.hamcrest.Matcher;

import java.lang.ref.WeakReference;
import java.util.HashMap;
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
    private WeakReference<BasePropertyFragment> fragmentWeakReference = null;


    //arguments to be loaded to the created fragment
    protected Bundle args = new Bundle();

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

    public FragmentBuilder withThemeColor(int themeColor) {
        args.putInt(BasePropertyFragment.ARG_THEME_COLOR, themeColor);
        return this;
    }

    public FragmentBuilder withDisplayType(int displayType) {
        args.putInt(BasePropertyFragment.ARG_DISPLAY_TYPE, displayType);
        return this;
    }

    public FragmentBuilder withButtonSelector(int buttonColor) {
        args.putInt(BasePropertyFragment.ARG_BUTTON_SELECTOR, buttonColor);
        return this;
    }

    public FragmentBuilder withTitleTextAppearance(int style) {
        args.putInt(BasePropertyFragment.ARG_TITLE_TEXT_APPEARANCE, style);
        return this;
    }

    public FragmentBuilder withDescTextAppearance(int style) {
        args.putInt(BasePropertyFragment.ARG_DESC_TEXT_APPEARANCE, style);
        return this;
    }

    public FragmentBuilder withValueTextAppearance(int style) {
        args.putInt(BasePropertyFragment.ARG_VALUE_TEXT_APPEARANCE, style);
        return this;
    }


    public FragmentBuilder withNoTitle(boolean noType) {
        args.putBoolean(BasePropertyFragment.ARG_NO_TITLE, noType);
        return this;
    }

    public FragmentBuilder withNoDescription(boolean noDescription) {
        args.putBoolean(BasePropertyFragment.ARG_NO_DESC, noDescription);
        return this;
    }

    public BasePropertyFragment build() {
//        if(fragmentWeakReference != null && fragmentWeakReference.get()!=null) {
//            return fragmentWeakReference.get();
//        }

        BasePropertyFragment fragment = null;


        if((propertySchema.getType()==null || propertySchema.getType().isEmpty()) && propertySchema.getEnum() == null) {
            //("No type defined. Fragment builder needs at least one defined type for a property");
            return null;
        }

        //generate all available arguments, depending on the property type some may not be used
        FragmentTools.genFragmentArgs(args, propertySchema);

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
           // fragmentWeakReference =  new WeakReference<BasePropertyFragment>(fragment);
        }
        return fragment;
    }


    public FragmentBuilder withGlobalButtonSelectors(HashMap<String, Integer> globalButtonSelectors) {
        args.putSerializable(BasePropertyFragment.ARG_CUSTOM_BUTTON_SELECTORS, globalButtonSelectors);
        return this;
    }
}
