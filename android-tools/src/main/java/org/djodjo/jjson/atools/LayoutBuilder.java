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


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import org.djodjo.jjson.atools.ui.fragment.BasePropertyFragment;
import org.djodjo.jjson.atools.util.OneOfFragment;
import org.djodjo.json.LinkedTreeMap;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LayoutBuilder<T extends Schema> {


    //setings bundle args
    private static final String ARG_DISPLAY_TYPES = "displayTypes";

    private static final String ARG_BUTTON_COLORS = "customButtonSelectors";
    private static final String ARG_CUSTOM_TITLE_STYLE = "customTitleTextAppearances";
    private static final String ARG_CUSTOM_DESC_STYLE = "customDescTextAppearances";
    private static final String ARG_CUSTOM_VALUE_TEXT_STYLE = "customValueTextAppearances";
    private static final String ARG_NO_TITLE = "noTitle";
    private static final String ARG_NO_DESC = "noDescription";

    private static final String ARG_GLOBAL_DISPLAY_TYPE = "globalDisplayType";


    //used for the hashmap containing all posible global selectors
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
    private int typeChooser4AnyOf = FragmentBuilder.DISPLAY_TYPE_SPINNER;
    private int typeChooser4AllOf = FragmentBuilder.DISPLAY_TYPE_SPINNER;
    private int typeChooser4OneOf = FragmentBuilder.DISPLAY_TYPE_SPINNER;


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
    private int globalDisplayType = -1;

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
    OneOfFragment oneOfFragment =  null;

    public LayoutBuilder(T schema, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.schema = schema;
        this.globalButtonSelectors =  new HashMap<String, Integer>();
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_CHECKBOX_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_RADIOBUTTON_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_THUMB_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_TOGGLEBUTTON_SELECTOR,0);
        globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SWITCHBUTTON_SELECTOR,0);
    }

    public LayoutBuilder<T> setSettingsBundle(Bundle args) {
        //populate hashmaps and values
        if(args == null) return this;

        displayTypes = (HashMap<String, Integer>)args.getSerializable(ARG_DISPLAY_TYPES);
        customButtonSelectors =  (HashMap<String, Integer>)args.getSerializable(ARG_BUTTON_COLORS);
        customTitleTextAppearances = (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_TITLE_STYLE);
        customDescTextAppearances =  (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_DESC_STYLE);
        customValueTextAppearances = (HashMap<String, Integer>)args.getSerializable(ARG_CUSTOM_VALUE_TEXT_STYLE);
        noTitle =   (HashMap<String, Boolean>)args.getSerializable(ARG_NO_TITLE);
        noDescription = ( HashMap<String, Boolean>)args.getSerializable(ARG_NO_DESC);

        globalDisplayType = args.getInt(ARG_GLOBAL_DISPLAY_TYPE, -1);
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
        bundle.putSerializable(ARG_BUTTON_COLORS, customButtonSelectors);
        bundle.putSerializable(ARG_CUSTOM_TITLE_STYLE, customTitleTextAppearances);
        bundle.putSerializable(ARG_CUSTOM_DESC_STYLE, customDescTextAppearances);
        bundle.putSerializable(ARG_CUSTOM_VALUE_TEXT_STYLE, customValueTextAppearances);
        bundle.putSerializable(ARG_NO_TITLE, noTitle);
        bundle.putSerializable(ARG_NO_DESC, noDescription);

        bundle.putInt(ARG_GLOBAL_DISPLAY_TYPE, globalDisplayType);
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

    public LayoutBuilder<T> setGlobalDisplayType(int globalDisplayType) {
        this.globalDisplayType = globalDisplayType;
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
        if(fragmentManager==null) return;
        if(fragBuilders == null || fragBuilders.size()<1)
        {

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

                                    .withButtonSelector(chooseButtonSelectors(property.getKey()))
                                    .withTitleTextAppearance(chooseTitleTextAppearance(property.getKey()))
                                    .withDescTextAppearance(chooseDescTextAppearance(property.getKey()))
                                    .withValueTextAppearance(chooseValueTextAppearance(property.getKey()))
                                    .withNoTitle(isNoTile(property.getKey()))
                                    .withNoDescription(isNoDescription(property.getKey()))
                                    .withGlobalButtonSelectors(globalButtonSelectors)
                    );
                }
            }

            // --> check for oneOf
            if (schema.getOneOf() != null && schema.getOneOf().getJson().length() > 0) {
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
                oneOfFragment = OneOfFragment.newInstance(stringSchemas, oneOfControllers, bundleSettings());
            }

        }


        // --> The TRANSACTION
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // --> clean fragments if not appending
        if(!append) {
            //FragmentTransaction.replace does not replace all the fragments in the container but only one thus we need to remove them all one by one
            Fragment currFrag =  fragmentManager.findFragmentById(containerId);
            Log.d(this.getClass().toString(), "start fragment removal");
            while(currFrag!=null) {
                try {
                    fragmentManager.beginTransaction().remove(currFrag).commit();
                    // fragment will not be removed instantly so we need to wait for the next one, otherwise too many commits buildup in the heap causing OutOfMemory
                    Fragment nextFrag =  fragmentManager.findFragmentById(containerId);
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

        // --> build and add fragments
        for (Map.Entry<String, FragmentBuilder> builder:fragBuilders.entrySet()) {
            Fragment fragment = builder.getValue().build();
            if(fragment!= null) {
                transaction.add(containerId, fragment, builder.getKey());
            }

        }

        //add oneOf fragment if exists
        if(oneOfFragment != null) {
            transaction.add(containerId, oneOfFragment, "oneOf");
        }

        transaction.commit();

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
            res = globalDisplayType;
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
