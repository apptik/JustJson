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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Jsonfier {

     private List<WeakReference<JsonfiableFragment>> jsonfiableFragments =
            new ArrayList<WeakReference<JsonfiableFragment>>();


    static Jsonfier defInstance = null;
    static HashMap<String, Jsonfier> instances;

    private Jsonfier() {
    }

    /**
     * Get the default Jsonfier
     * @return default Jsonfier
     */
    public static Jsonfier get() {
        if(defInstance ==null)
            defInstance =new Jsonfier();
        return defInstance;
    }

    /**
     * Get specific Jsonfier.
     * Useful when working with nested json elements;
     * @param name the name of the Jsonfier normally the parent fragment name.
     * @return Jsonfier corresponding to this name
     */
    public static Jsonfier get(String name) {
        if(instances.get(name) == null)
            instances.put(name,new Jsonfier());
        return instances.get(name) ;
    }

    void registerFragment(JsonfiableFragment fragment) {
        jsonfiableFragments.add(new WeakReference<JsonfiableFragment>(fragment));
    }

    void unregisterFragment(JsonfiableFragment fragment) {
        jsonfiableFragments.remove(new WeakReference<JsonfiableFragment>(fragment));
    }

    public List<JsonfiableFragment> getActiveFragments() {
        ArrayList<JsonfiableFragment> ret = new ArrayList<JsonfiableFragment>();
        for(WeakReference<JsonfiableFragment> ref : jsonfiableFragments) {
            JsonfiableFragment f = ref.get();
            if(f != null) {
                if(f.isVisible()) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }
}
