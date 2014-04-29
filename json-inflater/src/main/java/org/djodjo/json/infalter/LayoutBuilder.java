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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import org.djodjo.json.android.fragment.DisplayType;
import org.djodjo.json.infalter.util.OneOfFragment;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;
import org.djodjo.json.util.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LayoutBuilder<T extends Schema> {

    private Set<String> knownFragments = Collections.synchronizedSet(new TreeSet<String>());


    private InflaterSettings inflaterSettings =  new InflaterSettings();

    // private final Activity activity;
    private final FragmentManager fragmentManager;
    private final T schema;

    //othe options are radio or other selector buttons alike
    private int typeChooser4AnyOf = DisplayType.DISPLAY_TYPE_SPINNER;
    private int typeChooser4AllOf = DisplayType.DISPLAY_TYPE_SPINNER;
    private int typeChooser4OneOf = DisplayType.DISPLAY_TYPE_SPINNER;






    public Set<String> getKnownFragments() {
        return knownFragments;
    }

    public void setKnownFragments(Set<String> knownFragments) {
        this.knownFragments = knownFragments;
    }

    public LinkedTreeMap<String, FragmentBuilder> getFragBuilders() {
        return fragBuilders;
    }

    private LinkedTreeMap<String, FragmentBuilder> fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();
    private LinkedTreeMap<String, FragmentBuilder> extraFragBuilders;
    OneOfFragment oneOfOneOfFragment =  null;


    public LayoutBuilder(T schema, FragmentManager fragmentManager, LinkedTreeMap<String, FragmentBuilder> addedFragmentsBuilders) {
        this(schema, fragmentManager);
        extraFragBuilders = addedFragmentsBuilders;
    }

    public LayoutBuilder(T schema, FragmentManager fragmentManager) {
        //knownFragments =  Collections.synchronizedList(new ArrayList<String>());
        this.fragmentManager = fragmentManager;
        this.schema = schema;

    }








    public void reset() {
        fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();
    }

    private String genFragTag(String label, Schema propSchema) {
        return label + "_" +propSchema.getJson().toString().hashCode();
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

    public synchronized LayoutBuilder prepFragments() {
        Log.d("JustJsonLayoutBulder", "start prep");
        if (fragmentManager == null) return this;
        if (fragBuilders == null || fragBuilders.size() < 1) {

            Log.d("JustJsonLayoutBulder", "start generate main props");
            SchemaMap schemaTopProperties = schema.getProperties();

            // --> First find basic properties
            if (schemaTopProperties != null) {
                for (Map.Entry<String, Schema> property : schemaTopProperties) {
                    if (inflaterSettings.ignoredProperties.contains(property.getKey())) continue;
                    Schema propSchema = property.getValue();
                    FragmentBuilder fragBuilder;
                    if(extraFragBuilders!=null && extraFragBuilders.containsKey(genFragTag(property.getKey(), propSchema))) {
                        fragBuilder = extraFragBuilders.get(genFragTag(property.getKey(), propSchema));
                    } else {
                        fragBuilder = new FragmentBuilder(property.getKey(), propSchema);
                    }
                    fragBuilders.put(genFragTag(property.getKey(), propSchema),
                            fragBuilder
                                    .withLayoutId(inflaterSettings.getCustomLayoutId(property.getKey()))
                                    .withDisplayType(inflaterSettings.chooseDisplayType(property.getKey()))
                                    .withThemeColor(inflaterSettings.globalThemeColor)
                                    .withButtonSelector(inflaterSettings.chooseButtonSelectors(property.getKey()))
                                    .withTitleTextAppearance(inflaterSettings.chooseTitleTextAppearance(property.getKey()))
                                    .withDescTextAppearance(inflaterSettings.chooseDescTextAppearance(property.getKey()))
                                    .withValueTextAppearance(inflaterSettings.chooseValueTextAppearance(property.getKey()))
                                    .withNoTitle(inflaterSettings.isNoTile(property.getKey()))
                                    .withNoDescription(inflaterSettings.isNoDescription(property.getKey()))
                                    .withGlobalButtonSelectors(inflaterSettings.globalButtonSelectors)
                                    .withGlobalDisplayTypes(inflaterSettings.globalDisplayTypes)
                                    .withCustomFragment(inflaterSettings.customFragments.get(property.getKey()))
                                    .withCustomPropertyMatchers(inflaterSettings.customPropertyMatchers)
                    );
                }
            }
            Log.d("JustJsonLayoutBulder", "end generate main props");


            // --> check for oneOf
            if (schema.getOneOf() != null && schema.getOneOf().getJson().length() > 0) {
                Log.d("JustJsonLayoutBulder", "start generate onOf");
                ArrayList<Schema> oneOfSchemas = schema.getOneOf().getJsonWrappersList();
                ArrayList<String> stringSchemas = new ArrayList<String>();
                for (Schema oneOfSchema : oneOfSchemas) {
                    //before sending schemas to the oneOf fragment check if they are not already defined in here. if so merge and remove from common Layout
                    if (schemaTopProperties != null) {
                        SchemaMap propSchemas = oneOfSchema.getProperties();
                        for (Map.Entry<String, Schema> property : propSchemas) {
                            Schema topPropertySchema = schemaTopProperties.optValue(property.getKey());
                            if (topPropertySchema != null) {
                                property.getValue().merge(topPropertySchema);
                                fragBuilders.remove(genFragTag(property.getKey(), topPropertySchema));
                            }
                        }
                    }

                    //now add the schema to send
                    stringSchemas.add(oneOfSchema.getJson().toString());
                }
                //
                Log.d("JustJsonLayoutBulder", "end generate oneOf");
                oneOfOneOfFragment = OneOfFragment.newInstance(stringSchemas, inflaterSettings.oneOfControllers, inflaterSettings.bundleSettings());
            } // <-- check for oneOf

        }
        Log.d("JustJsonLayoutBulder", "complete prep");
        return this;
    }


    public synchronized void build(int containerId, boolean append) {
        Log.d("JustJsonLayoutBulder", "start build");
        if (fragBuilders == null || fragBuilders.size() < 1) this.prepFragments();

        synchronized (knownFragments) {
            Iterator<String> iterator = knownFragments.iterator();
            while (iterator.hasNext()) {
                String fragTag = iterator.next();
                Fragment currFrag = fragmentManager.findFragmentByTag(fragTag);
                if (currFrag != null) {
                    if (!fragBuilders.containsKey(fragTag)  && !currFrag.isHidden()  && !currFrag.isDetached())
                    {
                        fragmentManager.beginTransaction().hide(currFrag).commit();
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        for (Map.Entry<String, FragmentBuilder> builder : fragBuilders.entrySet()) {

            Fragment oldFrag = fragmentManager.findFragmentByTag(builder.getKey());
            if (oldFrag != null) {

                if (oldFrag.isHidden())
                {
                    fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(oldFrag).commit();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
                if (oldFrag.isDetached())
                {
                    fragmentManager.beginTransaction().attach(oldFrag).commit();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
                if(!oldFrag.isAdded())
                {
                    fragmentManager.beginTransaction().add(containerId, oldFrag).commit();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }

            } else {

                Fragment fragment = builder.getValue().build();
                if (fragment != null) {
                    Log.d("JustJsonLayoutBulder", "adding fragment: " + builder.getKey());
                    fragmentManager.beginTransaction().add(containerId, fragment, builder.getKey()).commit();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
            }
        }


        //add oneOf fragment if exists
        if(oneOfOneOfFragment != null) {
            fragmentManager.beginTransaction().add(containerId, oneOfOneOfFragment, "oneOf").commit();

        }

        Log.d("JustJsonLayoutBulder", "All commited - end build");

    }



    public InflaterSettings getInflaterSettings() {
        return inflaterSettings;
    }

    public LayoutBuilder<T> setInflaterSettings(InflaterSettings inflaterSettings) {
        this.inflaterSettings = inflaterSettings;

        return this;
    }



}
