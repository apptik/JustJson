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

}
