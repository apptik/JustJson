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

package org.djodjo.json.android.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import org.djodjo.json.android.R;

import java.util.ArrayList;


public class EnumFragment extends BasePropertyFragment {

    public final static int LAYOUT_ENUM_RADIO = R.layout.fragment_enum_radio;
    public final static int LAYOUT_ENUM_SPINNER = R.layout.fragment_enum_spinner;
    public final static int LAYOUT_ENUM_LISTVIEW = R.layout.fragment_enum_listview;

    public static final String ARG_OPTIONS = "options";
    public static final String ARG_IS_CONTROLLER = "isController";

    protected ArrayList<String> options;

    protected boolean isController = false;

    protected ControllerCallback controllerCallback = null;

    public EnumFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case DisplayType.DISPLAY_TYPE_SPINNER: return LAYOUT_ENUM_SPINNER;
            case DisplayType.DISPLAY_TYPE_RADIO: return LAYOUT_ENUM_RADIO;
        }

        if(options.size()>3)
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
            isController = getArguments().getBoolean(ARG_IS_CONTROLLER, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(isController) {
            try {
                controllerCallback = (ControllerCallback) getFragmentManager().findFragmentByTag("oneOf");
            } catch(Exception ex) {
            }
        }

        if(layoutId == LAYOUT_ENUM_RADIO) {
            RadioGroup enumRadioGroup = (RadioGroup) v.findViewById(R.id.enumRadioGroup);
            for(final String option:options) {
                RadioButton button = (RadioButton)inflater.inflate(R.layout.radio_button, enumRadioGroup, false);
                button.setText(option);
                button.setTextAppearance(getActivity(), styleValue);
                if(buttonSelector!=0) {
                    button.setBackgroundResource(buttonSelector);
                } else if (customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_RADIOBUTTON_SELECTOR) != 0)
                {
                    button.setBackgroundResource(customButtonSelectors.get(ARG_GLOBAL_RADIOBUTTON_SELECTOR));
                }
                if(controllerCallback !=null) {
                    button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                controllerCallback.onValueChanged(label, options.indexOf(option));
                            }
                        }
                    });
                }
                enumRadioGroup.addView(button);

            }
        }
        else if(layoutId == LAYOUT_ENUM_SPINNER) {
            Spinner enumSpinner = (Spinner) v.findViewById(R.id.enumSpinner);
            SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, options);
            enumSpinner.setAdapter(adapter);
            if(controllerCallback !=null) {
                enumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        controllerCallback.onValueChanged(label, position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        }
        else if(layoutId == LAYOUT_ENUM_LISTVIEW) {
            ListView enumListView = (ListView) v.findViewById(R.id.enumListView);
            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, options);
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
