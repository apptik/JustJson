package org.djodjo.jjson.atools;


import android.view.ViewGroup;
import android.widget.Spinner;

import org.djodjo.json.schema.Schema;

import java.util.LinkedHashSet;
import java.util.Map;

public class LayoutBuilder<T extends Schema> {


    private T schema;
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

    public LayoutBuilder(T schema) {
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

    public ViewGroup build(ViewGroup vg) {
        for(Map.Entry<String, Schema> propSchema:schema.getProperties()) {

        }

        return vg;
    }



}
