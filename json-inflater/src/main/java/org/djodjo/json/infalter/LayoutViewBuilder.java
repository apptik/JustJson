package org.djodjo.json.infalter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import org.djodjo.json.android.view.BasePropertyView;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LayoutViewBuilder<T extends Schema> {

    private Set<String> knownFragments = Collections.synchronizedSet(new TreeSet<String>());
    private ArrayList<BasePropertyView> views =  new ArrayList<BasePropertyView>();


    private ViewInflaterSettings inflaterSettings =  new ViewInflaterSettings();

    private final T schema;
    private final Context context;

    public LayoutViewBuilder(T schema, Context context) {
        this.context = context;
        this.schema = schema;

    }


    private void prepareViews() {
        Log.d("JustJsonLayoutViewBulder", "start prep");
        if(views!=null && views.size()>0) return;
        SchemaMap schemaTopProperties = schema.getProperties();

        // --> First find basic properties
        if (schemaTopProperties != null) {
            for (Map.Entry<String, Schema> property : schemaTopProperties) {
                if (inflaterSettings.ignoredProperties.contains(property.getKey())) continue;
                Schema propSchema = property.getValue();
                String label = property.getKey();
                views.add(new ViewBuilder(context, label, propSchema)
                        .inflate());
//                                .withLayoutId(inflaterSettings.getCustomLayoutId(property.getKey()))
//                                .withDisplayType(inflaterSettings.chooseDisplayType(property.getKey()))
//                                .withThemeColor(inflaterSettings.globalThemeColor)
//                                .withButtonSelector(inflaterSettings.chooseButtonSelectors(property.getKey()))
//                                .withTitleTextAppearance(inflaterSettings.chooseTitleTextAppearance(property.getKey()))
//                                .withDescTextAppearance(inflaterSettings.chooseDescTextAppearance(property.getKey()))
//                                .withValueTextAppearance(inflaterSettings.chooseValueTextAppearance(property.getKey()))
//                                .withNoTitle(inflaterSettings.isNoTile(property.getKey()))
//                                .withNoDescription(inflaterSettings.isNoDescription(property.getKey()))
//                                .withGlobalButtonSelectors(inflaterSettings.globalButtonSelectors)
//                                .withGlobalLayouts(inflaterSettings.globalLayouts)
//                                .withCustomFragment(inflaterSettings.customFragments.get(property.getKey()))
//                                .withCustomPropertyMatchers(inflaterSettings.customPropertyMatchers)

            }
        }
        Log.d("JustJsonLayoutViewBulder", "complete prep");
    }
    public void build(final ViewGroup vg) {
        prepareViews();
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vg.removeAllViews();
                for(BasePropertyView view:views) {
                    if(view!=null)
                        vg.addView(view);
                }
            }
        });

    }

    public ViewInflaterSettings getInflaterSettings() {
        return inflaterSettings;
    }

    public LayoutViewBuilder<T> setInflaterSettings(ViewInflaterSettings inflaterSettings) {
        this.inflaterSettings = inflaterSettings;

        return this;
    }



}
