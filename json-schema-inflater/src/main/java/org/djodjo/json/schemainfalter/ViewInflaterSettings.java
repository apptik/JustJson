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

package org.djodjo.json.schemainfalter;

import android.view.View;

import org.djodjo.json.android.view.BasePropertyView;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewInflaterSettings {

    //setings bundle args
//    private static final String ARG_GLOBAL_LAYOUTS = "globalLayouts";
//
//    private static final String ARG_BUTTON_SELECTORS = "customButtonSelectors";
//    private static final String ARG_CUSTOM_TITLE_STYLE = "customTitleTextAppearances";
//    private static final String ARG_CUSTOM_DESC_STYLE = "customDescTextAppearances";
//    private static final String ARG_CUSTOM_VALUE_TEXT_STYLE = "customValueTextAppearances";
//    private static final String ARG_NO_TITLE = "noTitle";
//    private static final String ARG_NO_DESC = "noDescription";
//
//    private static final String ARG_GLOBAL_DISPLAY_TYPES = "globalLayouts";
//
//    private static final String ARG_GLOBAL_THEME_COLOR = "globalThemeColor";
//
//
//    //used for the hashmap containing all possible global selectors
//    public static final String ARG_GLOBAL_BOTTON_SELECTORS = "globalButtonSelectors";
//    private static final String ARG_GLOBAL_TITLE_STYLE = "globalTitleTextAppearance";
//    private static final String ARG_GLOBAL_DESC_STYLE = "globalDescTextAppearance";
//    private static final String ARG_GLOBAL_VALUE_TEXT_STYLE = "globalValuesTextAppearance";
//    private static final String ARG_GLOBAL_NO_DESC = "globalNoDescription";
//    private static final String ARG_GLOBAL_NO_TITLE = "globalNoTitle";

    /**
     * custom matchers to be used to match custom Fragment to a specific property schema type.
     * if any they all need to be passed to the fragment builders
     */


    //map a matcher to specific Property View to be used
    private LinkedTreeMap<Matcher<Schema>, Class> customPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
    //map exact property labels to specific Property View to be used
    private HashMap<String, Class> customViews = new HashMap<String, Class>();

    //map exact property labels to specific Layout to be inflated
    private HashMap<String, Integer> customLayoutsByLabel =  new HashMap<String, Integer>();
    //map exact property view class to specific Layout to be inflated
    private HashMap<Class<BasePropertyView>, Integer> customLayoutsByPropertyView =  new HashMap<Class<BasePropertyView>, Integer>();

    private HashMap<String, Integer> displayTypesByLabel = new HashMap<String, Integer>();
    private HashMap<Class<BasePropertyView>, Integer> displayTypesByPropertyView = new HashMap<Class<BasePropertyView>, Integer>();

    //style ref
    private int globalButtonSelector = 0;
    private HashMap<String, Integer> buttonSelectorsByLabel =  new HashMap<String, Integer>();
    //specify selectors for specific property view ex: StringPropertyView, BooleanPropertyView, etc.
    private HashMap<Class<BasePropertyView>, Integer> buttonSelectorsByPropertyView =  new HashMap<Class<BasePropertyView>, Integer>();
    //specify selectors for specific view ex: CheckBox, Switch, etc.
    private HashMap<Class<View>, Integer> buttonSelectorsByViewControl =  new HashMap<Class<View>, Integer>();


    //style ref
    private int globalTitleTextAppearance = R.style.textTitle;
    private HashMap<String, Integer> titleTextAppearanceByLabel = new HashMap<String, Integer>();
    private HashMap<Class<BasePropertyView>, Integer> titleTextAppearanceByPropertyView = new HashMap<Class<BasePropertyView>, Integer>();

    //style ref
    private int globalDescTextAppearance = R.style.textDesc;
    private HashMap<String, Integer> descTextAppearanceByLabel =  new HashMap<String, Integer>();
    private HashMap<Class<BasePropertyView>, Integer> descTextAppearanceByPropertyView =  new HashMap<Class<BasePropertyView>, Integer>();

    //style ref
    private int globalValueTextAppearance = R.style.textValue;
    private HashMap<String, Integer> valueTextAppearanceByLabel = new HashMap<String, Integer>();
    private HashMap<Class<BasePropertyView>, Integer> valueTextAppearanceByPropertyView = new HashMap<Class<BasePropertyView>, Integer>();

    private boolean globalNoTitle = false;
    private HashMap<String, Boolean> noTitleByLabel =  new HashMap<String, Boolean>();
    private HashMap<Class<BasePropertyView>, Boolean> noTitleByPropertyView =  new HashMap<Class<BasePropertyView>, Boolean>();

    private boolean globalNoDescription = false;
    private HashMap<String, Boolean> noDescriptionByLabel = new HashMap<String, Boolean>();
    private HashMap<Class<BasePropertyView>, Boolean> noDescriptionByPropertyView = new HashMap<Class<BasePropertyView>, Boolean>();


    int globalThemeColor = -1;




    //the following controllers are used instead of a general selector
    //these are actual Json Object properties with Enum values,
    //where the name of the property is added to the list
    //the generation of the other Views will depend on the value of these
    //the they are picked in the order defined and control the following ones
    //i.e the first controller will get all possible values displayed,
    //the next one just the ones under the specific schema and sub-schemas, etc.

    ArrayList<String> oneOfControllers = new ArrayList<String>();
    ArrayList<String> ignoredProperties = new ArrayList<String>();


    public ViewInflaterSettings() {

    }

//
//
//    /**
//     * Add cusotm selector for a specific property.
//     * @param propertyName the property to have this selector
//     * @param customButtonSelector the style id of the selector
//     * @return
//     */
//    public ViewInflaterSettings addCustomButtonSelector(String propertyName, Integer customButtonSelector) {
//        this.customButtonSelectors.put(propertyName, customButtonSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomButtonSelectors(HashMap<String, Integer> customButtonColors) {
//        this.customButtonSelectors.putAll(customButtonColors);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomTitleTextAppearance(String propertyName, Integer customTitleTextAppearance) {
//        this.customTitleTextAppearances.put(propertyName, customTitleTextAppearance);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomTitleTextAppearances(HashMap<String, Integer> customTitleTextAppearances) {
//        this.customTitleTextAppearances.putAll(customTitleTextAppearances);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomDescTextAppearance(String propertyName, Integer customDescTextAppearance) {
//        this.customDescTextAppearances.put(propertyName, customDescTextAppearance);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomDescTextAppearances(HashMap<String, Integer> customDescTextAppearances) {
//        this.customDescTextAppearances.putAll(customDescTextAppearances);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomValueTextAppearance(String propertyName, Integer customValueTextAppearance) {
//        this.customValueTextAppearances.put(propertyName, customValueTextAppearance);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomValueTextAppearances(HashMap<String, Integer> customValueTextAppearances) {
//        this.customValueTextAppearances.putAll(customValueTextAppearances);
//        return this;
//    }
//
//    public ViewInflaterSettings addNoTitle(String propertyName) {
//        this.noTitle.put(propertyName, true);
//        return this;
//    }
//
//    public ViewInflaterSettings addNoTitles(HashMap<String, Boolean> noTitles) {
//        this.noTitle.putAll(noTitles);
//        return this;
//    }
//
//    public ViewInflaterSettings addNoDescription(String propertyName) {
//        this.noDescription.put(propertyName, true);
//        return this;
//    }
//
//    public ViewInflaterSettings addNoDescriptions(HashMap<String, Boolean> noDescriptions) {
//        this.noDescription.putAll(noDescriptions);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalStringLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_STRING_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalNumberLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_NUMBER_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    /**
//     * Set global display type of a limited number property. Limited means the property has "minimum" and "maximum" values defined.
//     * @param globalDisplayType
//     * @return
//     */
//    public ViewInflaterSettings setGlobalLimitedNumberLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_LIMITED_NUMBER_DISPLAY_TYPE, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalBooleanLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_BOOLEAN_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalArrayLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_ARRAY_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalArrayEnumLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_ARRAY_ENUM_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalEnumLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_ENUM_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalRangeLayout(int globalDisplayType) {
//        this.globalLayouts.put(BasePropertyFragment.ARG_GLOBAL_RANGE_LAYOUT, globalDisplayType);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalThemeColor(int globalThemeColor) {
//        this.globalThemeColor = globalThemeColor;
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalCheckBoxSelector(int globalCheckBoxSelector) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_CHECKBOX_SELECTOR, globalCheckBoxSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalRadioButtonSelector(int globalRadioButtonSelector) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_RADIOBUTTON_SELECTOR, globalRadioButtonSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalSliderThumbSelector(int globalSliderThumbSelector) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_THUMB_SELECTOR, globalSliderThumbSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalSliderProgressDrawable(int globalSliderProgressDrawable) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE, globalSliderProgressDrawable);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalToggleButtonSelector(int globalToggleButtonSelector) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_TOGGLEBUTTON_SELECTOR, globalToggleButtonSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalSwitchButtonSelector(int globalSwitchButtonSelector) {
//        this.globalButtonSelectors.put(BasePropertyFragment.ARG_GLOBAL_SWITCHBUTTON_SELECTOR, globalSwitchButtonSelector);
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalTitleTextAppearance(int globalTitleTextAppearance) {
//        this.globalTitleTextAppearance = globalTitleTextAppearance;
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalDescTextAppearance(int globalDescTextAppearance) {
//        this.globalDescTextAppearance = globalDescTextAppearance;
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalValuesTextAppearance(int globalValuesTextAppearance) {
//        this.globalValuesTextAppearance = globalValuesTextAppearance;
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalNoDescription(boolean globalNoDescription) {
//        this.globalNoDescription = globalNoDescription;
//        return this;
//    }
//
//    public ViewInflaterSettings setGlobalNoTitle(boolean globalNoTitle) {
//        this.globalNoTitle = globalNoTitle;
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomPropertyMatcher (Matcher<Schema> propertyMatcher,  FragmentBuilder.FragmentPack fragmentClass) {
//        customPropertyMatchers.put(propertyMatcher, fragmentClass);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomPropertyMatchers (Map<Matcher<Schema>, FragmentBuilder.FragmentPack> propertyMatchers) {
//        customPropertyMatchers.putAll(propertyMatchers);
//        return this;
//    }
//
//    public ViewInflaterSettings addDisplayType (String propertyName, int customLayout) {
//        globalLayouts.put(propertyName, customLayout);
//        return this;
//    }
//
//    public ViewInflaterSettings addDisplayTypes (Map<String, Integer> propertyDisplayTypes) {
//        globalLayouts.putAll(propertyDisplayTypes);
//        return this;
//    }
//
//    public ViewInflaterSettings addOneOfController(String propertyName) {
//        oneOfControllers.add(propertyName);
//        return this;
//    }
//
//    public ViewInflaterSettings addOneOfControllers(ArrayList<String> propertyNames) {
//        oneOfControllers.addAll(propertyNames);
//        return this;
//    }
//
//    public ViewInflaterSettings ignoreProperty(String propertyName) {
//        ignoredProperties.add(propertyName);
//        return this;
//    }
//
//    public ViewInflaterSettings ignoreProperties(ArrayList<String> propertyNames) {
//        ignoredProperties.addAll(propertyNames);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomLayout (String propertyName, int layoutId) {
//        customLayouts.put(propertyName, layoutId);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomLayouts (Map<String, Integer> propertyLayouts) {
//        customLayouts.putAll(propertyLayouts);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomFragment(String propertyName, FragmentBuilder.FragmentPack fragmentClass) {
//        customFragments.put(propertyName, fragmentClass);
//        return this;
//    }
//
//    public ViewInflaterSettings addCustomFragments(Map<String, FragmentBuilder.FragmentPack> customFragments) {
//        this.customFragments.putAll(customFragments);
//        return this;
//    }
//
//
//
//    int chooseDisplayType(String property) {
//        int res = -1;
//
//        if(globalLayouts.containsKey(property)) {
//            res = globalLayouts.get(property);
//        }
//        else {
//            res = -1;
//        }
//
//        return res;
//    }
//
//    int chooseButtonSelectors(String property) {
//        int res = 0;
//        if(customButtonSelectors.containsKey(property)) {
//            res = customButtonSelectors.get(property);
//        }
//        return res;
//    }
//
//
//    int chooseTitleTextAppearance(String property) {
//        int res = 0;
//
//        if(customTitleTextAppearances.containsKey(property)) {
//            res = customTitleTextAppearances.get(property);
//        } else {
//            res = globalTitleTextAppearance;
//        }
//
//        return res;
//    }
//
//    int chooseDescTextAppearance(String property) {
//        int res = 0;
//
//        if(customDescTextAppearances.containsKey(property)) {
//            res = customDescTextAppearances.get(property);
//        } else {
//            res = globalDescTextAppearance;
//        }
//
//        return res;
//    }
//
//    int chooseValueTextAppearance(String property) {
//        int res = 0;
//
//        if(customValueTextAppearances.containsKey(property)) {
//            res = customValueTextAppearances.get(property);
//        } else {
//            res = globalValuesTextAppearance;
//        }
//
//        return res;
//    }
//
//    boolean isNoTile(String property) {
//        boolean res = false;
//        if(noTitle.containsKey(property)) {
//            res = noTitle.get(property);
//        } else {
//            res = globalNoTitle;
//        }
//        return res;
//    }
//
//    boolean isNoDescription(String property) {
//        boolean res = false;
//        if(noDescription.containsKey(property)) {
//            res = noDescription.get(property);
//        } else {
//            res = globalNoDescription;
//        }
//        return res;
//    }
//
//    int getCustomLayoutId(String propertyName) {
//        int res = 0;
//        if(customLayouts.containsKey(propertyName)) {
//            res = customLayouts.get(propertyName);
//        }
//        return res;
//    }

}
