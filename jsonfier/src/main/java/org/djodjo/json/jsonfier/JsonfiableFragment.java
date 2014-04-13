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

package org.djodjo.json.jsonfier;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import org.djodjo.json.JsonElement;


public class JsonfiableFragment extends Fragment implements Jsonfiable{


    public static final String ARG_PARENT = "parent";

    private String parent;

    public JsonfiableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parent = getArguments().getString(ARG_PARENT);
            Jsonfier.get(parent).registerFragment(this);
        } else {
            Jsonfier.get().registerFragment(this);
        }
    }

    @Override
    public void onDestroy() {
        if(parent!=null && !parent.isEmpty()) {
            Jsonfier.get(parent).unregisterFragment(this);
        } else {
            Jsonfier.get().unregisterFragment(this);
        }
        super.onDestroy();
    }

    @Override
    public String getJsonString() {
        return null;
    }

    @Override
    public JsonElement getJsonElement() {
        return null;
    }

    @Override
    public String getLabel() {
        return this.getTag();
    }
}
