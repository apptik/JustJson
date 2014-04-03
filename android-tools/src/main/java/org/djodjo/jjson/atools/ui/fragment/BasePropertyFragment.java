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


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.djodjo.jjson.atools.R;


public abstract class BasePropertyFragment extends Fragment {

    public static final String ARG_LABEL = "label";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESC = "description";
    public static final String ARG_DEFAULT_VAL = "default_val";
    public static final String ARG_LAYOUT = "layout_id";

    public static final String ARG_DISPLAY_TYPE = "display_type";


    public String getLabel() {
        return label;
    }

    protected String label;
    protected String title;
    protected String description;

    protected int layoutId;

    protected int displayType;

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
            displayType = getArguments().getInt(ARG_DISPLAY_TYPE, -1);

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
        ((TextView)v.findViewById(R.id.txtPropTitle)).setText(title);
        if(description != null && !description.trim().isEmpty()) {
                  final TextView txtPropDesc =   (TextView)v.findViewById(R.id.txtPropDescription);
                    txtPropDesc.setText(description);
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
