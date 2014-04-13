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

package org.djodjo.json.infalter.util;

import android.os.Bundle;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.android.fragment.BasePropertyFragment;
import org.djodjo.json.android.fragment.EnumFragment;
import org.djodjo.json.android.fragment.NumberFragment;
import org.djodjo.json.schema.Schema;

import java.util.ArrayList;
import java.util.Map;


public class FragmentTools {

    private FragmentTools() {}

    public static Bundle genFragmentArgs(Bundle args, Schema schema) {
        if(schema == null) return args;

        //args.putStringArrayList(EnumFragment.ARG_OPTIONS, schema.getEnum());
        if(schema.getEnum()!=null)
            args.putStringArrayList(EnumFragment.ARG_OPTIONS, FragmentTools.genEnumStringList(schema));
        args.putString(BasePropertyFragment.ARG_TITLE, schema.getTitle());
        args.putString(BasePropertyFragment.ARG_DESC, schema.getDescription());
        args.putString(BasePropertyFragment.ARG_DEFAULT_VAL, schema.getDefault());
        args.putInt(NumberFragment.ARG_MINIMUM, (int)schema.getMinimum());
        args.putInt(NumberFragment.ARG_MAXIMUM, (int)schema.getMaximum());

        // --> loop inner(property schemas), used for complex(Object) properties
        // to deliver all sub-arguments to the created fragment

        if(schema.getProperties() != null) {
            for (Map.Entry<String, Schema> property : schema.getProperties()) {
                args.putBundle(property.getKey(), genFragmentArgs(new Bundle(), property.getValue()));
            }
        }

        return args;
    }

    public static ArrayList<String> genEnumStringList(Schema schema) {
        ArrayList<String> res = new ArrayList<String>();
        JsonArray enums = schema.getEnum();
        for(JsonElement el:enums) {
            res.add(el.toString());
        }

        return res;
    }
}
