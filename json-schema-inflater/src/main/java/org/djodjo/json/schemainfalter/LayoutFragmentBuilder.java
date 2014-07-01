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


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import org.djodjo.json.schemainfalter.util.OneOfFragment;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;
import org.djodjo.json.util.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LayoutFragmentBuilder<T extends Schema> {

    private Set<String> knownFragments = Collections.synchronizedSet(new TreeSet<String>());


    private FragmentInflaterSettings fragmentInflaterSettings =  new FragmentInflaterSettings();

    // private final Activity activity;
    private final FragmentManager fragmentManager;
    private final T schema;

    //othe options are radio or other selector buttons alike
//    private int typeChooser4AnyOf = FragmentLayouts.DISPLAY_TYPE_SPINNER;
//    private int typeChooser4AllOf = FragmentLayouts.DISPLAY_TYPE_SPINNER;
//    private int typeChooser4OneOf = FragmentLayouts.DISPLAY_TYPE_SPINNER;






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


    public LayoutFragmentBuilder(T schema, FragmentManager fragmentManager, LinkedTreeMap<String, FragmentBuilder> addedFragmentsBuilders) {
        this(schema, fragmentManager);
        extraFragBuilders = addedFragmentsBuilders;
    }

    public LayoutFragmentBuilder(T schema, FragmentManager fragmentManager) {
        //knownFragments =  Collections.synchronizedList(new ArrayList<String>());
        this.fragmentManager = fragmentManager;
        this.schema = schema;

    }








    public LayoutFragmentBuilder reset() {
        Log.d("JustJsonLayoutBulder", "start reset");
        if (knownFragments == null || knownFragments.isEmpty()) return this;

        Log.d("JustJsonLayoutBulder", "resetting...");
        synchronized (knownFragments) {
            Iterator<String> iterator = knownFragments.iterator();
            while (iterator.hasNext()) {
                String fragTag = iterator.next();
                Fragment currFrag = fragmentManager.findFragmentByTag(fragTag);
                if (currFrag != null) {
                    fragmentManager.beginTransaction().remove(currFrag).commitAllowingStateLoss();
                }
            }
        }
        fragBuilders = new LinkedTreeMap<String, FragmentBuilder>();
        knownFragments = Collections.synchronizedSet(new TreeSet<String>());
        Log.d("JustJsonLayoutBulder", "end reset");
        return this;
    }

    public LayoutFragmentBuilder hideAllFragments() {
        Log.d("JustJsonLayoutBulder", "start hideAllFragments");
        if (knownFragments == null || knownFragments.isEmpty()) return this;

        Log.d("JustJsonLayoutBulder", "hiding...");
        synchronized (knownFragments) {
            Iterator<String> iterator = knownFragments.iterator();
            while (iterator.hasNext()) {
                String fragTag = iterator.next();
                Fragment currFrag = fragmentManager.findFragmentByTag(fragTag);
                if (currFrag != null && !currFrag.isHidden()) {
                    fragmentManager.beginTransaction().hide(currFrag).commitAllowingStateLoss();
                }
            }
        }
        Log.d("JustJsonLayoutBulder", "end hideAllFragments");
        return this;
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

    public synchronized LayoutFragmentBuilder prepFragments() {
        Log.d("JustJsonLayoutBulder", "start prep");
        if (fragmentManager == null) return this;

        if (fragBuilders == null || fragBuilders.size() < 1) {

            Log.d("JustJsonLayoutBulder", "start generate main props");
            SchemaMap schemaTopProperties = schema.getProperties();

            // --> First find basic properties
            if (schemaTopProperties != null) {
                for (Map.Entry<String, Schema> property : schemaTopProperties) {
                    if (fragmentInflaterSettings.ignoredProperties.contains(property.getKey())) continue;
                    Schema propSchema = property.getValue();
                    FragmentBuilder fragBuilder;
                    if(extraFragBuilders!=null && extraFragBuilders.containsKey(genFragTag(property.getKey(), propSchema))) {
                        fragBuilder = extraFragBuilders.get(genFragTag(property.getKey(), propSchema));
                    } else {
                        fragBuilder = new FragmentBuilder(property.getKey(), propSchema);
                    }
                    fragBuilders.put(genFragTag(property.getKey(), propSchema),
                            fragBuilder
                                    .withLayoutId(fragmentInflaterSettings.getCustomLayoutId(property.getKey()))
                                    .withThemeColor(fragmentInflaterSettings.globalThemeColor)
                                    .withButtonSelector(fragmentInflaterSettings.chooseButtonSelectors(property.getKey()))
                                    .withTitleTextAppearance(fragmentInflaterSettings.chooseTitleTextAppearance(property.getKey()))
                                    .withDescTextAppearance(fragmentInflaterSettings.chooseDescTextAppearance(property.getKey()))
                                    .withValueTextAppearance(fragmentInflaterSettings.chooseValueTextAppearance(property.getKey()))
                                    .setTranslateOptions(fragmentInflaterSettings.hasEnumTranslation(property.getKey()))
                                    .setTranslateOptionsPrefix(fragmentInflaterSettings.getEnumTranslationPrefix(property.getKey()))
                                    .withNoTitle(fragmentInflaterSettings.isNoTile(property.getKey()))
                                    .withNoDescription(fragmentInflaterSettings.isNoDescription(property.getKey()))
                                    .withGlobalButtonSelectors(fragmentInflaterSettings.globalButtonSelectors)
                                    .withGlobalLayouts(fragmentInflaterSettings.globalLayouts)
                                    .withCustomFragment(fragmentInflaterSettings.customFragments.get(property.getKey()))
                                    .withCustomPropertyMatchers(fragmentInflaterSettings.customPropertyMatchers)
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
                oneOfOneOfFragment = OneOfFragment.newInstance(stringSchemas, fragmentInflaterSettings.oneOfControllers, fragmentInflaterSettings.bundleSettings());
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
                        fragmentManager.beginTransaction().hide(currFrag).commitAllowingStateLoss();
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
                    fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(oldFrag).commitAllowingStateLoss();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
                if (oldFrag.isDetached())
                {
                    fragmentManager.beginTransaction().attach(oldFrag).commitAllowingStateLoss();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
                if(!oldFrag.isAdded())
                {
                    fragmentManager.beginTransaction().add(containerId, oldFrag).commitAllowingStateLoss();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }

            } else {

                Fragment fragment = builder.getValue().build();
                if (fragment != null) {
                    Log.d("JustJsonLayoutBulder", "adding fragment: " + builder.getKey());
                    fragmentManager.beginTransaction().add(containerId, fragment, builder.getKey()).commitAllowingStateLoss();
                    if (!knownFragments.contains(builder.getKey())) {
                        knownFragments.add(builder.getKey());
                    }
                }
            }
        }


        //add oneOf fragment if exists
        if(oneOfOneOfFragment != null) {
            fragmentManager.beginTransaction().add(containerId, oneOfOneOfFragment, "oneOf").commitAllowingStateLoss();

        }

        Log.d("JustJsonLayoutBulder", "All commited - end build");

    }



    public FragmentInflaterSettings getFragmentInflaterSettings() {
        return fragmentInflaterSettings;
    }

    public LayoutFragmentBuilder<T> setFragmentInflaterSettings(FragmentInflaterSettings fragmentInflaterSettings) {
        this.fragmentInflaterSettings = fragmentInflaterSettings;

        return this;
    }



}
