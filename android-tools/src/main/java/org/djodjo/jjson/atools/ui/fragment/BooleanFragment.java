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


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import org.djodjo.jjson.atools.FragmentBuilder;
import org.djodjo.jjson.atools.R;


public class BooleanFragment extends BasePropertyFragment {

    public final static int LAYOUT_BOOL_CHECKBOX = R.layout.fragment_bool_check;
    public final static int LAYOUT_BOOL_SWITCH = R.layout.fragment_bool_switch;
    public final static int LAYOUT_BOOL_TOGGLE = R.layout.fragment_bool_toggle;


    public BooleanFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        switch (displayType) {
            case FragmentBuilder.DISPLAY_TYPE_CHECKBOX: return LAYOUT_BOOL_CHECKBOX;
            case FragmentBuilder.DISPLAY_TYPE_SWITCH: return LAYOUT_BOOL_SWITCH;
            case FragmentBuilder.DISPLAY_TYPE_TOGGLE: return LAYOUT_BOOL_TOGGLE;
        }
        return LAYOUT_BOOL_SWITCH;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        CompoundButton button = (CompoundButton)v.findViewById(R.id.prop_value);
        if(buttonSelector!=0) {
            button.setButtonDrawable(buttonSelector);
        } else if (button instanceof CheckBox && customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR) != 0)
        {
            button.setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR));
        }
        else if (button instanceof ToggleButton && customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR) != 0)
        {
            button.setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR));
        }
        else if (button instanceof Switch && customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR) != 0)
        {
            button.setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR));
        }

        return v;
    }
}
