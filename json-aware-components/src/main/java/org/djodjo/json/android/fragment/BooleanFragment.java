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
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import org.djodjo.json.JsonBoolean;
import org.djodjo.json.JsonElement;
import org.djodjo.json.android.R;


public class BooleanFragment extends BasePropertyFragment {

    public final static int LAYOUT_BOOL_CHECKED_TEXTVIEW = R.layout.fragment_bool_checkedtextview;
    public final static int LAYOUT_BOOL_CHECKBOX = R.layout.fragment_bool_check;
    public final static int LAYOUT_BOOL_SWITCH = R.layout.fragment_bool_switch;
    public final static int LAYOUT_BOOL_TOGGLE = R.layout.fragment_bool_toggle;


    public BooleanFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        int currLayoutId =  globalLayouts.get(ARG_GLOBAL_BOOLEAN_LAYOUT);
        if(currLayoutId==0) {
            currLayoutId = LAYOUT_BOOL_CHECKED_TEXTVIEW;
        }
        return currLayoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        final Checkable checkable = (Checkable) (v != null ? v.findViewById(R.id.prop_value) : null);

        if(checkable instanceof CompoundButton && !(checkable instanceof ToggleButton)) {
            ((CompoundButton)checkable).setText(title);
        } else if(checkable instanceof CheckedTextView) {
            ((CheckedTextView)checkable).setText(title);
            ((CheckedTextView)checkable).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CheckedTextView)checkable).toggle();
                }
            });
        }

        if(buttonSelector!=0 && checkable instanceof CompoundButton) {
            ((CompoundButton)checkable).setButtonDrawable(buttonSelector);
        } else if(buttonSelector!=0 && checkable instanceof CheckedTextView) {
            ((CheckedTextView)checkable).setCheckMarkDrawable(buttonSelector);
        } else if (checkable instanceof CheckBox && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR) != 0)
        {
            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR));
        } else if (checkable instanceof CheckedTextView && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR) != 0)
        {
           ((CheckedTextView)checkable).setCheckMarkDrawable(customButtonSelectors.get(ARG_GLOBAL_CHECKBOX_SELECTOR));
        }
        else if (checkable instanceof ToggleButton && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR) != 0)
        {
            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_TOGGLEBUTTON_SELECTOR));
        }
        else if (checkable instanceof Switch && customButtonSelectors!= null && customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR) != 0)
        {
            ((CompoundButton)checkable).setButtonDrawable(customButtonSelectors.get(ARG_GLOBAL_SWITCHBUTTON_SELECTOR));
        }

        return v;
    }

    @Override
    public JsonElement getJsonElement() {
        final Checkable checkable = (Checkable) (getView() != null ? getView().findViewById(R.id.prop_value) : null);
        JsonBoolean res = null;
        if(checkable!=null) {
            res = new JsonBoolean(checkable.isChecked());
        }
        return res;
    }

}
