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


import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import org.djodjo.json.android.fragment.BasePropertyFragment;
import org.djodjo.json.android.fragment.DisplayType;
import org.djodjo.json.infalter.util.OneOfFragment;
import org.djodjo.json.util.LinkedTreeMap;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LayoutBuilder<T extends Schema> {


    //setings bundle args
    private static final String ARG_DISPLAY_TYPES = "displayTypes";

    private static final String ARG_BUTTON_SELECTORS = "customButtonSelectors";
    private static final String ARG_CUSTOM_TITLE_STYLE = "customTitleTextAppearances";
    private static final String ARG_CUSTOM_DESC_STYLE = "customDescTextAppearances";
    private static final String ARG_CUSTOM_VALUE_TEXT_STYLE = "customValueTextAppearances";
    private static final String ARG_NO_TITLE = "noTitle";
    private static final String ARG_NO_DESC = "noDescription";

    private static final String ARG_GLOBAL_DISPLAY_TYPES = "globalDisplayTypes";

    private static final String ARG_GLOBAL_THEME_COLOR = "globalThemeColor";


    //used for the hashmap containing all possible global selectors
    private static final String ARG_GLOBAL_BOTTON_SELECTORS = "globalButtonSelectors";
    private static final String ARG_GLOBAL_TITLE_STYLE = "globalTitleTextAppearance";
    private static final String ARG_GLOBAL_DESC_STYLE = "globalDescTextAppearance";
    private static final String ARG_GLOBAL_VALUE_TEXT_STYLE = "globalValuesTextAppearance";
    private static final String ARG_GLOBAL_NO_DESC = "globalNoDescription";
    private static final String ARG_GLOBAL_NO_TITLE = "globalNoTitle";


    // private final Activity activity;
    private final FragmentManager fragmentManager;
    private final T schema;
    private boolean mergeAnyOf = false;
    private boolean mergeAllOf = false;
    private boolean mergeOneOf = false;

    //othe options are radio or other selector buttons alike
    private int typeChooser4AnyOf = DisplayType.DISPLAY_TYPE_SPINNER;
    private int typeChooser4AllOf = DisplayType.DISPLAY_TYPE_SPINNER;
    private int typeChooser4OneOf = DisplayType.DISPLAY_TYPE_SPINNER;


    //the following controllers are used instead of a general selector
    //these are actual Json Object properties with Enum values,
    //where the name of the property is added to the list
    //the generation of the other Views will depend on the value of these
    //the they are picked in the order defined and control the following ones
    //i.e the first controller will get all possible values displayed,
    //the next one just the ones under the specific schema and sub-schemas, etc.

    private ArrayList<String> oneOfControllers = new ArrayList<String>();
    private ArrayList<String> ignoredProperties = new ArrayList<String>();

    //map for custom layouts for specific properties for this object
    private HashMap<String, Integer> customLayouts =  new HashMap<String, Integer>();

    private HashMap<String, Integer> displayTypes = new HashMap<String, Integer>();
    private HashMap<String, Integer> customButtonSelectors =  new HashMap<String, Integer>();
    private HashMap<String, Integer> customTitleTextAppearances = new HashMap<String, Integer>();
    private HashMap<String, Integer> customDescTextAppearances =  new HashMap<String, Integer>();
    private HashMap<String, Integer> customValueTextAppearances = new HashMap<String, Integer>();
    private HashMap<String, Boolean> noTitle =  new HashMap<String, Boolean>();
    private HashMap<String, Boolean> noDescription = new HashMap<String, Boolean>();

    /**
     * a mask of the possible display types for all elements
     */
    private HashMap<String, Integer> globalDisplayTypes = new HashMap<String, Integer>();


    private int globalThemeColor = -1;

    HashMap<String, Integer> globalButtonSelectors = new HashMap<String, Integer>();
    //style ref
    private int globalTitleTextAppearance = R.style.textTitle;
    //style ref
    private int globalDescTextAppearance = R.style.textDesc;
    //style ref
    private int globalValuesTextAppearance = R.style.textValue;
    private boolean globalNoDescription = false;
    private boolean globalNoTitle = false;


    private LinkedTreeMap<String, FragmentBuilder> fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();
    OneOfFragment oneOfOneOfFragment =  null;



    public LayoutBuilder(T schema, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.schema = schema;
        this.globalButtonSelectors =  new HashMap<String, Integer>();
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_CHECKBOX_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_RADIOBUTTON_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_THUMB_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_TOGGLEBUTTON_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SWITCHBUTTON_SELECTOR,0);

        this.globalDisplayTypes = new HashMap<String, Integer>();
        setGlobalStringDisplayType(DisplayType.DISPLAY_TYPE_TEXT);
        setGlobalNumberDisplayType(DisplayType.DISPLAY_TYPE_TEXT);
        setGlobalLimitedNumberDisplayType(DisplayType.DISPLAY_TYPE_SLIDER);
        setGlobalBooleanDisplayType(DisplayType.DISPLAY_TYPE_SWITCH);
        setGlobalArrayDisplayType(DisplayType.DISPLAY_TYPE_LISTVIEW);
        setGlobalArrayEnumDisplayType(DisplayType.DISPLAY_TYPE_SPINNER); //multi select
        setGlobalEnumDisplayType(DisplayType.DISPLAY_TYPE_SPINNER); //single select
        setGlobalRangeDisplayType(DisplayType.DISPLAY_TYPE_SLIDER);
    }

    public LayoutBuilder<T> setSettingsBundle(Bundle args) {
        //populate hashmaps and values
        if(args == null) return this;

        displayTypes = (HashMap<String, Integer>)args.getSerializable(ARG_DISPLAY_TYPES);
        customButtonSelectors =  (HashMap<String, Integer>)args.getSerializable(ARG_BUTTON_SELECTORS);
        customTitleTextAppearances = (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_TITLE_STYLE);
        customDescTextAppearances =  (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_DESC_STYLE);
        customValueTextAppearances = (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_VALUE_TEXT_STYLE);
        noTitle =   (HashMap<String, Boolean>)args.getSerializable(ARG_NO_TITLE);
        noDescription = ( HashMap<String, Boolean>)args.getSerializable(ARG_NO_DESC);

        globalDisplayTypes = (HashMap<String, Integer>) args.getSerializable(ARG_GLOBAL_DISPLAY_TYPES);
        globalThemeColor = args.getInt(ARG_GLOBAL_THEME_COLOR, -1);
        globalButtonSelectors = (HashMap<String, Integer>) args.getSerializable(ARG_GLOBAL_BOTTON_SELECTORS);
        globalTitleTextAppearance = args.getInt(ARG_GLOBAL_TITLE_STYLE, R.style.textTitle);
        globalDescTextAppearance = args.getInt(ARG_GLOBAL_DESC_STYLE, R.style.textDesc);
        globalValuesTextAppearance = args.getInt(ARG_GLOBAL_VALUE_TEXT_STYLE, R.style.textValue);
        globalNoDescription = args.getBoolean(ARG_GLOBAL_NO_DESC, false);
        globalNoTitle = args.getBoolean(ARG_GLOBAL_NO_TITLE, false);

        return this;
    }

    private Bundle bundleSettings() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DISPLAY_TYPES, displayTypes);
        bundle.putSerializable(ARG_BUTTON_SELECTORS, customButtonSelectors);
        bundle.putSerializable(ARG_CUSTOM_TITLE_STYLE, customTitleTextAppearances);
        bundle.putSerializable(ARG_CUSTOM_DESC_STYLE, customDescTextAppearances);
        bundle.putSerializable(ARG_CUSTOM_VALUE_TEXT_STYLE, customValueTextAppearances);
        bundle.putSerializable(ARG_NO_TITLE, noTitle);
        bundle.putSerializable(ARG_NO_DESC, noDescription);

        bundle.putSerializable(ARG_GLOBAL_DISPLAY_TYPES, globalDisplayTypes);
        bundle.putInt(ARG_GLOBAL_THEME_COLOR, globalThemeColor);
        bundle.putSerializable(ARG_GLOBAL_BOTTON_SELECTORS, globalButtonSelectors);
        bundle.putInt(ARG_GLOBAL_TITLE_STYLE, globalTitleTextAppearance);
        bundle.putInt(ARG_GLOBAL_DESC_STYLE, globalDescTextAppearance);
        bundle.putInt(ARG_GLOBAL_VALUE_TEXT_STYLE, globalValuesTextAppearance);
        bundle.putBoolean(ARG_GLOBAL_NO_DESC, globalNoDescription);
        bundle.putBoolean(ARG_GLOBAL_NO_TITLE, globalNoTitle);

        return bundle;
    }


    /**
     * Add cusotm selector for a specific property.
     * @param propertyName the property to have this selector
     * @param customButtonSelector the style id of the selector
     * @return
     */
    public LayoutBuilder<T> addCustomButtonSelector(String propertyName, Integer customButtonSelector) {
        this.customButtonSelectors.put(propertyName, customButtonSelector);
        return this;
    }

    public LayoutBuilder<T> addCustomButtonSelectors(HashMap<String, Integer> customButtonColors) {
        this.customButtonSelectors.putAll(customButtonColors);
        return this;
    }

    public LayoutBuilder<T> addCustomTitleTextAppearance(String propertyName, Integer customTitleTextAppearance) {
        this.customTitleTextAppearances.put(propertyName, customTitleTextAppearance);
        return this;
    }

    public LayoutBuilder<T> addCustomTitleTextAppearances(HashMap<String, Integer> customTitleTextAppearances) {
        this.customTitleTextAppearances.putAll(customTitleTextAppearances);
        return this;
    }

    public LayoutBuilder<T> addCustomDescTextAppearance(String propertyName, Integer customDescTextAppearance) {
        this.customDescTextAppearances.put(propertyName, customDescTextAppearance);
        return this;
    }

    public LayoutBuilder<T> addCustomDescTextAppearances(HashMap<String, Integer> customDescTextAppearances) {
        this.customDescTextAppearances.putAll(customDescTextAppearances);
        return this;
    }

    public LayoutBuilder<T> addCustomValueTextAppearance(String propertyName, Integer customValueTextAppearance) {
        this.customValueTextAppearances.put(propertyName, customValueTextAppearance);
        return this;
    }

    public LayoutBuilder<T> addCustomValueTextAppearances(HashMap<String, Integer> customValueTextAppearances) {
        this.customValueTextAppearances.putAll(customValueTextAppearances);
        return this;
    }

    public LayoutBuilder<T> addNoTitle(String propertyName) {
        this.noTitle.put(propertyName, true);
        return this;
    }

    public LayoutBuilder<T> addNoTitles(HashMap<String, Boolean> noTitles) {
        this.noTitle.putAll(noTitles);
        return this;
    }

    public LayoutBuilder<T> addNoDescription(String propertyName) {
        this.noDescription.put(propertyName, true);
        return this;
    }

    public LayoutBuilder<T> addNoDescriptions(HashMap<String, Boolean> noDescriptions) {
        this.noDescription.putAll(noDescriptions);
        return this;
    }

    public LayoutBuilder<T> setGlobalStringDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_STRING_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalNumberDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_NUMBER_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    /**
     * Set global display type of a limited number property. Limited means the property has "minimum" and "maximum" values defined.
     * @param globalDisplayType
     * @return
     */
    public LayoutBuilder<T> setGlobalLimitedNumberDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_LIMITED_NUMBER_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalBooleanDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_BOOLEAN_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalArrayDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_ARRAY_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalArrayEnumDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_ARRAY_ENUM_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalEnumDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_ENUM_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalRangeDisplayType(int globalDisplayType) {
        this.globalDisplayTypes.put(BasePropertyFragment.ARG_GLOBAL_RANGE_DISPLAY_TYPE, globalDisplayType);
        return this;
    }

    public LayoutBuilder<T> setGlobalThemeColor(int globalThemeColor) {
        this.globalThemeColor = globalThemeColor;
        return this;
    }

    public LayoutBuilder<T> setGlobalCheckBoxSelector(int globalCheckBoxSelector) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_CHECKBOX_SELECTOR, globalCheckBoxSelector);
        return this;
    }

    public LayoutBuilder<T> setGlobalRadioButtonSelector(int globalRadioButtonSelector) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_RADIOBUTTON_SELECTOR, globalRadioButtonSelector);
        return this;
    }

    public LayoutBuilder<T> setGlobalSliderThumbSelector(int globalSliderThumbSelector) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_THUMB_SELECTOR, globalSliderThumbSelector);
        return this;
    }

    public LayoutBuilder<T> setGlobalSliderProgressDrawable(int globalSliderProgressDrawable) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE, globalSliderProgressDrawable);
        return this;
    }

    public LayoutBuilder<T> setGlobalToggleButtonSelector(int globalToggleButtonSelector) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_TOGGLEBUTTON_SELECTOR, globalToggleButtonSelector);
        return this;
    }

    public LayoutBuilder<T> setGlobalSwitchButtonSelector(int globalSwitchButtonSelector) {
        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SWITCHBUTTON_SELECTOR, globalSwitchButtonSelector);
        return this;
    }

    public LayoutBuilder<T> setGlobalTitleTextAppearance(int globalTitleTextAppearance) {
        this.globalTitleTextAppearance = globalTitleTextAppearance;
        return this;
    }

    public LayoutBuilder<T> setGlobalDescTextAppearance(int globalDescTextAppearance) {
        this.globalDescTextAppearance = globalDescTextAppearance;
        return this;
    }

    public LayoutBuilder<T> setGlobalValuesTextAppearance(int globalValuesTextAppearance) {
        this.globalValuesTextAppearance = globalValuesTextAppearance;
        return this;
    }

    public LayoutBuilder<T> setGlobalNoDescription(boolean globalNoDescription) {
        this.globalNoDescription = globalNoDescription;
        return this;
    }

    public LayoutBuilder<T> setGlobalNoTitle(boolean globalNoTitle) {
        this.globalNoTitle = globalNoTitle;
        return this;
    }



    public LayoutBuilder<T> addOneOfController(String propertyName) {
        oneOfControllers.add(propertyName);
        return this;
    }

    public LayoutBuilder<T> addOneOfControllers(ArrayList<String> propertyNames) {
        oneOfControllers.addAll(propertyNames);
        return this;
    }

    public LayoutBuilder<T> ignoreProperty(String propertyName) {
        ignoredProperties.add(propertyName);
        return this;
    }

    public LayoutBuilder<T> ignoreProperties(ArrayList<String> propertyNames) {
        ignoredProperties.addAll(propertyNames);
        return this;
    }

    public LayoutBuilder<T> addCustomLayout (String propertyName, int layoutId) {
        customLayouts.put(propertyName, layoutId);
        return this;
    }

    public LayoutBuilder<T> addCustomLayouts (Map<String, Integer> propertyLayouts) {
        customLayouts.putAll(propertyLayouts);
        return this;
    }

    public LayoutBuilder<T> addDisplayType (String propertyName, int displayType) {
        displayTypes.put(propertyName, displayType);
        return this;
    }

    public LayoutBuilder<T> addDisplayTypes (Map<String, Integer> propertyDisplayTypes) {
        displayTypes.putAll(propertyDisplayTypes);
        return this;
    }

    public void reset() {
        fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();
    }

    public void build(ViewGroup vg, boolean append) {
        build(vg.getId(), append);
    }
    public void build(ViewGroup vg) {
        build(vg.getId());
    }
    public void build(int containerId) {
        build(containerId, false);
    }
    public void build(int containerId, boolean append) {
        Log.d(this.getClass().toString(), "start build");
        if(fragmentManager==null) return;
        if(fragBuilders == null || fragBuilders.size()<1)
        {

            Log.d(this.getClass().toString(), "start generate main props");
            SchemaMap schemaTopProperties = schema.getProperties();

            // --> First find basic properties
            if(schemaTopProperties!=null) {
                for (Map.Entry<String, Schema> property : schemaTopProperties) {
                    if (ignoredProperties.contains(property.getKey())) continue;
                    Schema propSchema = property.getValue();
                    FragmentBuilder fragBuilder = new FragmentBuilder(property.getKey(), propSchema);
                    fragBuilders.put(property.getKey(),
                            fragBuilder
                                    .withLayoutId(getCustomLayoutId(property.getKey()))
                                    .withDisplayType(chooseDisplayType(property.getKey()))
                                    .withThemeColor(globalThemeColor)
                                    .withButtonSelector(chooseButtonSelectors(property.getKey()))
                                    .withTitleTextAppearance(chooseTitleTextAppearance(property.getKey()))
                                    .withDescTextAppearance(chooseDescTextAppearance(property.getKey()))
                                    .withValueTextAppearance(chooseValueTextAppearance(property.getKey()))
                                    .withNoTitle(isNoTile(property.getKey()))
                                    .withNoDescription(isNoDescription(property.getKey()))
                                    .withGlobalButtonSelectors(globalButtonSelectors)
                                    .withGlobalDisplayTypes(globalDisplayTypes)
                    );
                }
            }
            Log.d(this.getClass().toString(), "end generate main props");


            // --> check for oneOf
            if (schema.getOneOf() != null && schema.getOneOf().getJson().length() > 0) {
                Log.d(this.getClass().toString(), "start generate onOf");
                ArrayList<Schema> oneOfSchemas = schema.getOneOf().getJsonWrappersList();
                ArrayList<String> stringSchemas = new ArrayList<String>();
                for (Schema oneOfSchema : oneOfSchemas) {
                    //before sending schemas to the oneOf fragment check if they are not already defined in here. if so merge and remove from common Layout
                    if(schemaTopProperties!=null) {
                        SchemaMap propSchemas = oneOfSchema.getProperties();
                        for (Map.Entry<String, Schema> property : propSchemas) {
                            Schema topPropertySchema = schemaTopProperties.optValue(property.getKey());
                            if (topPropertySchema != null) {
                                property.getValue().merge(topPropertySchema);
                                fragBuilders.remove(property.getKey());
                            }
                        }
                    }

                    //now add the schema to send
                    stringSchemas.add(oneOfSchema.getJson().toString());
                }
                //
                Log.d(this.getClass().toString(), "end generate onOf");
                oneOfOneOfFragment = OneOfFragment.newInstance(stringSchemas, oneOfControllers, bundleSettings());
            }

        }




        // --> clean fragments if not appending
        if(!append) {

            Log.d(this.getClass().toString(), "start fragment removal");
            //FragmentTransaction.replace does not replace all the fragments in the container but only one thus we need to remove them all one by one
            android.app.Fragment currFrag =  fragmentManager.findFragmentById(containerId);

            while(currFrag!=null) {
                try {
                    fragmentManager.beginTransaction().remove(currFrag).commit();
                    // fragment will not be removed instantly so we need to wait for the next one, otherwise too many commits buildup in the heap causing OutOfMemory
                    android.app.Fragment nextFrag =  fragmentManager.findFragmentById(containerId);
                    while (nextFrag != null && nextFrag==currFrag) {
                        nextFrag =  fragmentManager.findFragmentById(containerId);
                    }
                    currFrag = nextFrag;
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            Log.d(this.getClass().toString(), "complete fragment removal");
        }


        // --> The TRANSACTION
        // --> build and add fragments
        for (Map.Entry<String, FragmentBuilder> builder:fragBuilders.entrySet()) {

            BasePropertyFragment fragment = builder.getValue().build();
            if(fragment!= null) {
                Log.d("JustJsonLayoutBulder", "adding fragment: " + builder.getKey());
                fragmentManager.beginTransaction().add(containerId, fragment, builder.getKey()).commit();
            }

        }

        //add oneOf fragment if exists
        if(oneOfOneOfFragment != null) {
            fragmentManager.beginTransaction().add(containerId, oneOfOneOfFragment, "oneOf").commit();

        }

        Log.d(this.getClass().toString(), "All commited - end build");

    }


    private int getCustomLayoutId(String propertyName) {
        int res = 0;
        if(customLayouts.containsKey(propertyName)) {
            res = customLayouts.get(propertyName);
        }
        return res;
    }

    private int chooseDisplayType(String property) {
        int res = -1;

        if(displayTypes.containsKey(property)) {
            res = displayTypes.get(property);
        }
        else {
            res = -1;
        }

        return res;
    }

    private int chooseButtonSelectors(String property) {
        int res = 0;
        if(customButtonSelectors.containsKey(property)) {
            res = customButtonSelectors.get(property);
        }
        return res;
    }


    private int chooseTitleTextAppearance(String property) {
        int res = 0;

        if(customTitleTextAppearances.containsKey(property)) {
            res = customTitleTextAppearances.get(property);
        } else {
            res = globalTitleTextAppearance;
        }

        return res;
    }

    private int chooseDescTextAppearance(String property) {
        int res = 0;

        if(customDescTextAppearances.containsKey(property)) {
            res = customDescTextAppearances.get(property);
        } else {
            res = globalDescTextAppearance;
        }

        return res;
    }

    private int chooseValueTextAppearance(String property) {
        int res = 0;

        if(customValueTextAppearances.containsKey(property)) {
            res = customValueTextAppearances.get(property);
        } else {
            res = globalValuesTextAppearance;
        }

        return res;
    }

    private boolean isNoTile(String property) {
        boolean res = false;
        if(noTitle.containsKey(property)) {
            res = noTitle.get(property);
        } else {
            res = globalNoTitle;
        }
        return res;
    }

    private boolean isNoDescription(String property) {
        boolean res = false;
        if(noDescription.containsKey(property)) {
            res = noDescription.get(property);
        } else {
            res = globalNoDescription;
        }
        return res;
    }


}
