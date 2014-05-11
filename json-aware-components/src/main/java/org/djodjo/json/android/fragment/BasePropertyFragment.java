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


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.djodjo.json.android.R;

import java.util.HashMap;


public abstract class BasePropertyFragment extends Fragment {

    public static final String ARG_LABEL = "label";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESC = "description";
    public static final String ARG_DEFAULT_VAL = "default_val";
    public static final String ARG_LAYOUT = "layout_id";

    public static final String ARG_DISPLAY_TYPE = "display_type";
    //if ARG_DISPLAY_TYPE is not set or < 0 we check global types
    public static final String ARG_GLOBAL_LAYOUTS = "display_types";
    public static final String ARG_THEME_COLOR = "theme_color";
    public static final String ARG_BUTTON_SELECTOR = "button_selector";
    public static final String ARG_CUSTOM_BUTTON_SELECTORS = "custom_button_selectors";
    public static final String ARG_TITLE_TEXT_APPEARANCE = "title_text_style";
    public static final String ARG_DESC_TEXT_APPEARANCE = "desc_text_style";
    public static final String ARG_VALUE_TEXT_APPEARANCE = "value_text_style";
    public static final String ARG_NO_TITLE = "no_title";
    public static final String ARG_NO_DESC = "no_desc";



    public static final String ARG_GLOBAL_STRING_LAYOUT = "globalStringLayout";
    public static final String ARG_GLOBAL_NUMBER_LAYOUT = "globalNumberLayout";
    public static final String ARG_GLOBAL_LIMITED_NUMBER_LAYOUT = "globalLimitedNumberLayout";
    public static final String ARG_GLOBAL_BOOLEAN_LAYOUT = "globalBooleanLayout";
    //array with any items
    public static final String ARG_GLOBAL_ARRAY_LAYOUT = "globalArrayLayout";
    //array with enum items
    public static final String ARG_GLOBAL_ARRAY_ENUM_LAYOUT = "globalArrayEnumLayout";
    public static final String ARG_GLOBAL_ENUM_LAYOUT = "globalEnumLayout";
    public static final String ARG_GLOBAL_RANGE_LAYOUT = "globalRangeLayout";

    public static final String ARG_GLOBAL_CHECKBOX_SELECTOR = "globalCheckBoxSelector";
    public static final String ARG_GLOBAL_RADIOBUTTON_SELECTOR = "globalRadioButtonSelector";
    public static final String ARG_GLOBAL_SLIDER_THUMB_SELECTOR = "globalSliderThumbSelector";
    public static final String ARG_GLOBAL_SLIDER_PROGRESS_DRAWABLE = "globalSliderProgressDrawable";
    public static final String ARG_GLOBAL_TOGGLEBUTTON_SELECTOR = "globalToggleButtonSelector";
    public static final String ARG_GLOBAL_SWITCHBUTTON_SELECTOR = "globalSwitchButtonSelector";


    public String getLabel() {
        return label;
    }

    protected String label;
    protected String title;
    protected String description;

    protected int layoutId;

    protected HashMap<String,Integer> globalLayouts;
    protected int themeColor;
    protected int buttonSelector;
    protected HashMap<String,Integer> customButtonSelectors;
    protected int styleTitle;
    protected int styleDesc;
    protected int styleValue;
    protected boolean noTitle;
    protected boolean noDesc;

    public BasePropertyFragment() {
    }

    protected abstract int getLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            label = getArguments().getString(ARG_LABEL);
            title = getArguments().getString(ARG_TITLE);
            description = getArguments().getString(ARG_DESC);
            layoutId = getArguments().getInt(ARG_LAYOUT, 0);


            globalLayouts = (HashMap<String,Integer>)getArguments().getSerializable(ARG_GLOBAL_LAYOUTS);
            themeColor = getArguments().getInt(ARG_THEME_COLOR, -1);
            buttonSelector = getArguments().getInt(ARG_BUTTON_SELECTOR);
            customButtonSelectors = (HashMap<String,Integer>)getArguments().getSerializable(ARG_CUSTOM_BUTTON_SELECTORS);

            styleTitle =  getArguments().getInt(ARG_TITLE_TEXT_APPEARANCE);
            styleDesc =  getArguments().getInt(ARG_DESC_TEXT_APPEARANCE);
            styleValue = getArguments().getInt(ARG_VALUE_TEXT_APPEARANCE);
            noTitle = getArguments().getBoolean(ARG_NO_TITLE);
            noDesc = getArguments().getBoolean(ARG_NO_DESC);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        //check if custom layout is required
        if(layoutId==0) layoutId = getLayoutId();
        v = inflater.inflate(layoutId, container, false);
        //set some view params: text, color, etc
        TextView txtTitle =  (TextView) v.findViewById(R.id.txtPropTitle);
        final TextView txtPropDesc =   (TextView)v.findViewById(R.id.txtPropDescription);

        if(txtTitle!=null) {
            if (noTitle || title==null) {
                txtTitle.setVisibility(View.GONE);
            } else {
                txtTitle.setText(title);
                txtTitle.setTextAppearance(getActivity(), styleTitle);
            }
        }

        if(description != null && !description.trim().isEmpty() && !noDesc && txtPropDesc!=null) {
            txtPropDesc.setText(description);
            txtPropDesc.setTextAppearance(getActivity(), styleDesc);
            v.findViewById(R.id.btnPropHelp).setVisibility(View.VISIBLE);
            v.findViewById(R.id.btnPropHelp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtPropDesc.isShown()) {
                        txtPropDesc.setVisibility(View.GONE);
                    } else {
                        txtPropDesc.setVisibility(View.VISIBLE);
                    }
                }
            });
        }


        return v;
    }


}
