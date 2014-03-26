package org.djodjo.jjson.atools;


import android.app.Activity;
import android.app.Fragment;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.djodjo.json.LinkedTreeMap;
import org.djodjo.json.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LayoutBuilder<T extends Schema> {


    private final Activity activity;
    private final T schema;
    private boolean mergeAnyOf = false;
    private boolean mergeAllOf = false;
    private boolean mergeOneOf = false;

    //othe options are radio or other selector buttons alike
    private Class typeChooser4AnyOf = Spinner.class;
    private Class typeChooser4AllOf = Spinner.class;
    private Class typeChooser4OneOf = Spinner.class;


    //the following controllers are used instead of a general selector
    //these are actual Json Object properties with Enum values,
    //where the name of the property is added to the list
    //the generation of the other Views will depend on the value of these
    //the they are picked in the order defined and control the following ones
    //i.e the first controller will get all possible values displayed,
    //the next one just the ones under the specific schema and sub-schemas, etc.

    private LinkedHashSet<String> anyOfControllers = new LinkedHashSet<String>();
    private LinkedHashSet<String> allOfControllers = new LinkedHashSet<String>();
    private LinkedHashSet<String> oneOfControllers = new LinkedHashSet<String>();

    //map for custom layouts for specific properties for this object
    private HashMap<String, Integer> customLayouts =  new HashMap<String, Integer>();

    private LinkedTreeMap<String, Fragment> propFragments =  new LinkedTreeMap<String, Fragment>();

    public LayoutBuilder(T schema, Activity activity) {
        this.activity = activity;
        this.schema = schema;
    }

    public LayoutBuilder<T> addAnyOfController(String propertyName) {
        anyOfControllers.add(propertyName);
        return this;
    }

    public LayoutBuilder<T> addAllOfController(String propertyName) {
        allOfControllers.add(propertyName);
        return this;
    }

    public LayoutBuilder<T> addOneOfController(String propertyName) {
        oneOfControllers.add(propertyName);
        return this;
    }

    public LayoutBuilder<T> addCustomLayout (String propertyName, int layoutId) {
        customLayouts.put(propertyName, layoutId);
        return this;
    }


    public ViewGroup build(ViewGroup vg) {
        for(Map.Entry<String, Schema> property:schema.getProperties()) {
            FragmentBuilder fragBuilder  = new FragmentBuilder(property.getKey());

            Schema propSchema = property.getValue();

            propFragments.put(property.getKey(),
            fragBuilder.addType(propSchema.getType())
                    .withTitle(propSchema.getTitle())
                    .withDescription(propSchema.getDescription())
                    .withDisplayType(chooseDisplayType(propSchema.getType()))
                    .withLayoutId(getCustomLayoutId(property.getKey()))
                    .build());
        }

        return vg;
    }


    private int getCustomLayoutId(String propertyName) {
        int res = 0;
        if(customLayouts.containsKey(propertyName)) {
            res = customLayouts.get(propertyName);
        }
        return res;
    }

    private int chooseDisplayType(ArrayList<String> types) {
        int res = 0;

        return res;
    }

}
