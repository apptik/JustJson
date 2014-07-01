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

package org.djodjo.jjson.exampleapp;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.djodjo.json.JsonElement;
import org.djodjo.json.android.FragmentLayouts;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.schemainfalter.FragmentInflaterSettings;
import org.djodjo.json.schemainfalter.LayoutFragmentBuilder;
import org.djodjo.json.schema.SchemaV4;

import java.io.IOException;
import java.io.InputStreamReader;

public class SchemaFormRendererFragment extends BlankFragment {

    TextView txtInput;

    public SchemaFormRendererFragment() {
        layout = R.layout.fragment_formrender;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        final SchemaV4 schema = new SchemaV4();
        try {
            schema.wrap(JsonElement.readFrom(new InputStreamReader(getActivity().getAssets().open("schema5.json"))));
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                LayoutFragmentBuilder<SchemaV4> lb  = new LayoutFragmentBuilder<SchemaV4>(schema, getFragmentManager());
                lb
                        .setFragmentInflaterSettings(new FragmentInflaterSettings()
                                        .addOneOfController("controller0")
                                        .addOneOfController("controller1")
                                        .setGlobalNoDescription(true)
                                        .setGlobalBooleanLayout(FragmentLayouts.NUMBER_PICKER)
                                        .addCustomLayout("additionalOption3", FragmentLayouts.BOOLEAN_TOGGLE)
                                //.setGlobalRadioButtonSelector(R.drawable.red_radio_selector)
                                //.setGlobalCheckBoxSelector(R.drawable.apptheme_btn_check_holo_light)
                                //.setGlobalSliderThumbSelector(R.drawable.apptheme_scrubber_control_selector_holo_light)
                                //.setGlobalSliderProgressDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light)
                                //.setGlobalThemeColor(getResources().getColor(R.color.apptheme_color))
                        )
                        .build(R.id.form_container);
            }
        }).start();
    }
}
