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

package org.djodjo.json.infalter;


import android.app.Fragment;
import android.os.Bundle;

import org.djodjo.json.android.fragment.BasePropertyFragment;
import org.djodjo.json.android.fragment.BooleanFragment;
import org.djodjo.json.android.fragment.EnumFragment;
import org.djodjo.json.android.fragment.LimitedNumberFragment;
import org.djodjo.json.android.fragment.NumberFragment;
import org.djodjo.json.android.fragment.RangeFragment;
import org.djodjo.json.android.fragment.StringFragment;
import org.djodjo.json.infalter.util.FragmentTools;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.djodjo.json.infalter.matcher.SchemaDefMatchers.*;


/**
 * View Builder that is used to generate all vies from JsonSchema describing a JsonObject
 *
 * View builders are created in the order the properties are found in the definition
 *
 * if enums are defined inside "oneOf, anyOf" there is the possibility that the other fields may depends on them
 */
public class FragmentBuilder {


    private Schema propertySchema;


    //arguments to be loaded to the created fragment
    protected Bundle args = new Bundle();

    private static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;
    private LinkedTreeMap<Matcher<Schema>, FragmentPack> customPropertyMatchers;
    private FragmentPack customFragment;



    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(isEnum(), EnumFragment.class);
        commonPropertyMatchers.put(isStringType(), StringFragment.class);
        commonPropertyMatchers.put(isBooleanType(), BooleanFragment.class);
        commonPropertyMatchers.put(isLimitedNumber(), LimitedNumberFragment.class);
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

    public FragmentBuilder withCustomFragment(FragmentPack customFragment) {
        this.customFragment = customFragment;
        return this;
    }
    public FragmentBuilder withCustomPropertyMatchers(LinkedTreeMap<Matcher<Schema>, FragmentPack> customPropertyMatchers) {
        this.customPropertyMatchers = customPropertyMatchers;
        return this;
    }
    public FragmentBuilder withTitle(String title) {
        args.putString(BasePropertyFragment.ARG_TITLE, title);
        return this;
    }

    public FragmentBuilder setController(Boolean value) {
        args.putBoolean(EnumFragment.ARG_IS_CONTROLLER, value);
        return this;
    }

    //options for enums mostly
    public FragmentBuilder withOptions(ArrayList<String> options) {
        args.putStringArrayList(EnumFragment.ARG_OPTIONS, options);
        return this;
    }

    public FragmentBuilder withDescription(String description) {
        args.putString(BasePropertyFragment.ARG_DESC, description);
        return this;
    }

    public FragmentBuilder withDefaultValue(String defaultValue) {
        args.putString(BasePropertyFragment.ARG_DEFAULT_VAL, defaultValue);
        return this;
    }

    public FragmentBuilder withInflaterSettings(InflaterSettings inflaterSettings) {
        String label = args.getString(BasePropertyFragment.ARG_LABEL);
        this
                .withLayoutId(inflaterSettings.getCustomLayoutId(label))
                .withDisplayType(inflaterSettings.chooseDisplayType(label))
                .withThemeColor(inflaterSettings.globalThemeColor)
                .withButtonSelector(inflaterSettings.chooseButtonSelectors(label))
                .withTitleTextAppearance(inflaterSettings.chooseTitleTextAppearance(label))
                .withDescTextAppearance(inflaterSettings.chooseDescTextAppearance(label))
                .withValueTextAppearance(inflaterSettings.chooseValueTextAppearance(label))
                .withNoTitle(inflaterSettings.isNoTile(label))
                .withNoDescription(inflaterSettings.isNoDescription(label))
                .withGlobalButtonSelectors(inflaterSettings.globalButtonSelectors)
                .withGlobalDisplayTypes(inflaterSettings.globalDisplayTypes)
                .withCustomFragment(inflaterSettings.customFragments.get(label))
                .withCustomPropertyMatchers(inflaterSettings.customPropertyMatchers);
        return this;
    }

    public Fragment build() {
        Fragment fragment = null;


//        if((propertySchema.getType()==null || propertySchema.getType().isEmpty()) && propertySchema.getEnum() == null) {
//            //("No type defined. OneOfFragment builder needs at least one defined type for a property");
//            return null;
//        }

        //generate all available arguments, depending on the property type some may not be used
        if(propertySchema!=null) {
            FragmentTools.genFragmentArgs(args, propertySchema);
        }

        //TODO use MIXED fragment

        if(customFragment!=null) {
            try {
                fragment = (Fragment) customFragment.getFragment().newInstance();
                if(customFragment.getArgs()!=null) {
                    args.putAll(customFragment.getArgs());
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(fragment==null) {
            for (Map.Entry<Matcher<Schema>, FragmentPack> entry : customPropertyMatchers.entrySet()) {
                if (entry.getKey().matches(propertySchema)) {
                    try {
                        fragment = (Fragment) entry.getValue().getFragment().newInstance();
                        if(entry.getValue().getArgs()!=null) {
                            args.putAll(entry.getValue().getArgs());
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        if(fragment==null && propertySchema!=null) {
            for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                if (entry.getKey().matches(propertySchema)) {
                    try {
                        fragment = (Fragment) entry.getValue().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        //fragment can be null in some cases
        if(fragment != null) {
            fragment.setArguments(args);
            //fragment.setRetainInstance(true);
        }
        return fragment;
    }


    public FragmentBuilder withGlobalButtonSelectors(HashMap<String, Integer> globalButtonSelectors) {
        args.putSerializable(BasePropertyFragment.ARG_CUSTOM_BUTTON_SELECTORS, globalButtonSelectors);
        return this;
    }

    public FragmentBuilder withGlobalDisplayTypes(HashMap<String, Integer> globalDisplayTypes) {
        args.putSerializable(BasePropertyFragment.ARG_DISPLAY_TYPES, globalDisplayTypes);
        return this;
    }


   public static class FragmentPack {
        Class fragment;
        Bundle args;

        public FragmentPack(Class fragment) {
            args = new Bundle();
            this.fragment = fragment;
        }

        public Class getFragment() {
            return fragment;
        }

        public Bundle getArgs() {
            return args;
        }

        public FragmentPack setArgs(Bundle args) {
            this.args = args;
            return this;
        }

    }
}
