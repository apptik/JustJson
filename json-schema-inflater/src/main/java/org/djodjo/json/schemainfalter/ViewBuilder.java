package org.djodjo.json.schemainfalter;


import android.content.Context;

import org.djodjo.json.android.view.BasePropertyView;
import org.djodjo.json.android.view.BooleanPropertyView;
import org.djodjo.json.android.view.StringPropertyView;
import org.djodjo.json.schemainfalter.util.FragmentTools;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import static org.djodjo.json.schemainfalter.matcher.SchemaDefMatchers.isBooleanType;
import static org.djodjo.json.schemainfalter.matcher.SchemaDefMatchers.isStringType;

public class ViewBuilder {

    private Context context;
    private Schema schema;
    private String label;
    private static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;
    private LinkedTreeMap<Matcher<Schema>, Class> customPropertyMatchers;
    private Class customView;
    private Class currentView;

    private ArrayList<String> options; //enums
    private double minimum; //number
    private double maximum; //number
    private String title;
    private String description;
    private String defVal; //default value

    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        //commonPropertyMatchers.put(isEnum(), EnumPropertyView.class);
        commonPropertyMatchers.put(isStringType(), StringPropertyView.class);
        commonPropertyMatchers.put(isBooleanType(), BooleanPropertyView.class);
        //commonPropertyMatchers.put(isLimitedNumber(), LimitedNumberPropertyView.class);
        // commonPropertyMatchers.put(isNumberType(), NumberPropertyView.class);
        //commonPropertyMatchers.put(isRangeObject(), RangePropertyView.class);

    }

    public ViewBuilder(Context context, String label, Schema schema) {
        //label cannot change, label might be null if parent is not an object but an array
        this.context = context;
        this.label = label;
        this.schema = schema;
        parseSchema();
    }

    public <T extends BasePropertyView> T prepare() {
        if(customView!=null) {
            currentView = customView;
        }

        if(currentView==null && schema!=null && customPropertyMatchers!=null) {
            for (Map.Entry<Matcher<Schema>, Class> entry : customPropertyMatchers.entrySet()) {
                if (entry.getKey().matches(schema)) {
                    currentView = entry.getValue();
                    break;
                }
            }
        }

        if(currentView==null && schema!=null && commonPropertyMatchers!=null) {
            for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                if (entry.getKey().matches(schema)) {

                    currentView =  entry.getValue();
                    break;
                }
            }
        }

        if(currentView==null) return null;
        T res =null;
        try {
            res = (T)currentView.getConstructor(Context.class).newInstance(this.context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if(res!=null) {
            res.setTitle(title);
            res.setDescription(description);
        }


        return res;
    }
    public <T extends BasePropertyView> T inflate() {
        T v = this.prepare();
        if(v==null) return null;
        return v.inflate();
    }

    public static <T extends BasePropertyView> T inflate(T view) {
        if(view == null) return null;
        return view.inflate();
    }
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
//    public ViewBuilder withDisplayType(int customLayout) {
//        args.putInt(BasePropertyFragment.ARG_DISPLAY_TYPE, customLayout);
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


    private void parseSchema() {
        if(schema==null) return;
        if(schema.getEnum()!=null) {
            FragmentTools.genEnumStringList(schema);
        }
        title =schema.getTitle();
        description = schema.getDescription();
        defVal = schema.getDefault();
        minimum = schema.getMinimum();
        maximum = schema.getMaximum();
    }


}
