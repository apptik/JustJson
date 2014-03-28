package org.djodjo.jjson.atools;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.djodjo.json.LinkedTreeMap;
import org.djodjo.json.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LayoutBuilder<T extends Schema> {


    // private final Activity activity;
    private final FragmentManager fragmentManager;
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


    private LinkedTreeMap<String, FragmentBuilder> fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();

    public LayoutBuilder(T schema, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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
        for(Map.Entry<String, Schema> property:schema.getProperties()) {
            FragmentBuilder fragBuilder  = new FragmentBuilder(property.getKey());

            Schema propSchema = property.getValue();

            fragBuilders.put(property.getKey(),
                    fragBuilder.addType(propSchema.getType())
                            .withTitle(propSchema.getTitle())
                            .withDescription(propSchema.getDescription())
                            .withDisplayType(chooseDisplayType(propSchema.getType()))
                            .withLayoutId(getCustomLayoutId(property.getKey()))
            );
        }


        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(!append) {
            //FragmentTransaction.replace does not replace all the fragments in the container but only one  thus we need to remove them all one by one
            Fragment currFrag =  fragmentManager.findFragmentById(containerId);
            while(currFrag!=null) {
                fragmentManager.beginTransaction().remove(currFrag).commit();
                currFrag =  fragmentManager.findFragmentById(containerId);
            }
        }
        for (Map.Entry<String, FragmentBuilder> builder:fragBuilders.entrySet()) {
            Fragment fragment = builder.getValue().build();
            transaction.add(containerId, fragment, builder.getKey());

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

    private int chooseDisplayType(ArrayList<String> types) {
        int res = -1;

        return res;
    }

}
