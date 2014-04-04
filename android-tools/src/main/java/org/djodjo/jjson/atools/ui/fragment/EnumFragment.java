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

package org.djodjo.jjson.atools.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.djodjo.jjson.atools.FragmentBuilder;
import org.djodjo.jjson.atools.R;

import java.util.ArrayList;


public class EnumFragment extends BasePropertyFragment {

    public final static int LAYOUT_ENUM_RADIO = R.layout.fragment_enum_radio;
    public final static int LAYOUT_ENUM_SPINNER = R.layout.fragment_enum_spinner;
    public final static int LAYOUT_ENUM_LISTVIEW = R.layout.fragment_enum_listview;

    public static final String ARG_OPTIONS = "options";

    private ArrayList<String> options;

    public EnumFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_SPINNER: return LAYOUT_ENUM_SPINNER;
            case FragmentBuilder.DISPLAY_TYPE_RADIO: return LAYOUT_ENUM_RADIO;
        }

        if(options.size()>6)
            return LAYOUT_ENUM_SPINNER;
            //return LAYOUT_ENUM_LISTVIEW;
        else
            return LAYOUT_ENUM_RADIO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            options = getArguments().getStringArrayList(ARG_OPTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(layoutId == LAYOUT_ENUM_RADIO) {
            RadioGroup enumRadioGroup = (RadioGroup) v.findViewById(R.id.enumRadioGroup);
            for(String option:options) {
                RadioButton button = (RadioButton)inflater.inflate(R.layout.radio_button, enumRadioGroup, false);
                button.setText(option);
                button.setTextAppearance(getActivity(), styleValue);


                enumRadioGroup.addView(button);
            }
        }
        else if(layoutId == LAYOUT_ENUM_SPINNER) {
            Spinner enumSpinner = (Spinner) v.findViewById(R.id.enumSpinner);
            SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, styleValue, options);
            enumSpinner.setAdapter(adapter);

        }
        else if(layoutId == LAYOUT_ENUM_LISTVIEW) {
            ListView enumListView = (ListView) v.findViewById(R.id.enumListView);
            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, styleValue, options);
            enumListView.setAdapter(adapter);
            enumListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            //if in scroll view we need to measure height ourselves
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, enumListView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = enumListView.getLayoutParams();
            params.height = totalHeight + (enumListView.getDividerHeight() * (adapter.getCount() - 1));
            enumListView.setLayoutParams(params);
            enumListView.requestLayout();

        }

        return v;
    }



}
