package org.djodjo.json.infalter;


import org.djodjo.json.android.fragment.EnumFragment;
import org.djodjo.json.android.view.StringPropertyView;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import static org.djodjo.json.infalter.matcher.SchemaDefMatchers.isEnum;
import static org.djodjo.json.infalter.matcher.SchemaDefMatchers.isStringType;

public class ViewBuilder {

    private Schema propertySchema;
    private static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;
    private LinkedTreeMap<Matcher<Schema>, Class> customPropertyMatchers;
    private Class customView;

    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(isEnum(), EnumFragment.class);
        commonPropertyMatchers.put(isStringType(), StringPropertyView.class);
        //commonPropertyMatchers.put(isBooleanType(), BooleanPropertyView.class);
       //commonPropertyMatchers.put(isLimitedNumber(), LimitedNumberPropertyView.class);
       // commonPropertyMatchers.put(isNumberType(), NumberPropertyView.class);
        //commonPropertyMatchers.put(isRangeObject(), RangePropertyView.class);

    }

//    public ViewBuilder(Context context, String label, Schema schema) {
//        //label cannot change
//        args.putString(BasePropertyFragment.ARG_LABEL, label);
//        propertySchema = schema;
//    }
//
//    public ViewBuilder withLayoutId(int layoutId) {
//        args.putInt(BasePropertyFragment.ARG_LAYOUT, layoutId);
//        return this;
//    }
//
//    public ViewBuilder withThemeColor(int themeColor) {
//        args.putInt(BasePropertyFragment.ARG_THEME_COLOR, themeColor);
//        return this;
//    }
//
//    public ViewBuilder withDisplayType(int displayType) {
//        args.putInt(BasePropertyFragment.ARG_DISPLAY_TYPE, displayType);
//        return this;
//    }
//
//    public ViewBuilder withButtonSelector(int buttonColor) {
//        args.putInt(BasePropertyFragment.ARG_BUTTON_SELECTOR, buttonColor);
//        return this;
//    }
//
//    public ViewBuilder withTitleTextAppearance(int style) {
//        args.putInt(BasePropertyFragment.ARG_TITLE_TEXT_APPEARANCE, style);
//        return this;
//    }
//
//    public ViewBuilder withDescTextAppearance(int style) {
//        args.putInt(BasePropertyFragment.ARG_DESC_TEXT_APPEARANCE, style);
//        return this;
//    }
//
//    public ViewBuilder withValueTextAppearance(int style) {
//        args.putInt(BasePropertyFragment.ARG_VALUE_TEXT_APPEARANCE, style);
//        return this;
//    }
//
//
//    public ViewBuilder withNoTitle(boolean noType) {
//        args.putBoolean(BasePropertyFragment.ARG_NO_TITLE, noType);
//        return this;
//    }
//
//    public ViewBuilder withNoDescription(boolean noDescription) {
//        args.putBoolean(BasePropertyFragment.ARG_NO_DESC, noDescription);
//        return this;
//    }
//
//    public ViewBuilder withCustomView(Class customView) {
//        this.customView = customView;
//        return this;
//    }
//    public ViewBuilder withCustomPropertyMatchers(LinkedTreeMap<Matcher<Schema>, Class> customPropertyMatchers) {
//        this.customPropertyMatchers = customPropertyMatchers;
//        return this;
//    }
//    public ViewBuilder withTitle(String title) {
//        args.putString(BasePropertyFragment.ARG_TITLE, title);
//        return this;
//    }
//
//    public ViewBuilder setController(Boolean value) {
//        args.putBoolean(EnumFragment.ARG_IS_CONTROLLER, value);
//        return this;
//    }
//
//    //options for enums mostly
//    public ViewBuilder withOptions(ArrayList<String> options) {
//        args.putStringArrayList(EnumFragment.ARG_OPTIONS, options);
//        return this;
//    }
//
//    public ViewBuilder withDescription(String description) {
//        args.putString(BasePropertyFragment.ARG_DESC, description);
//        return this;
//    }
//
//    public ViewBuilder withDefaultValue(String defaultValue) {
//        args.putString(BasePropertyFragment.ARG_DEFAULT_VAL, defaultValue);
//        return this;
//    }
}
