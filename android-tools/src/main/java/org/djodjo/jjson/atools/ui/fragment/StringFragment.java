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

import org.djodjo.jjson.atools.R;

import java.util.ArrayList;


public class StringFragment extends BasePropertyFragment {

    private ArrayList<String> options;

    public StringFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_string;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

}
