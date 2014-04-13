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

package org.djodjo.json.infalter;

import java.util.HashMap;

public class InflaterSettings {

    static InflaterSettings defInstance = null;
    static HashMap<String, InflaterSettings> instances;



    private InflaterSettings() {
    }

    /**
     * Get the default InflaterSettings
     * @return default InflaterSettings
     */
    public static InflaterSettings get() {
        if(defInstance ==null)
            defInstance =new InflaterSettings();
        return defInstance;
    }

    /**
     * Get specific InflaterSettings.
     * Useful when working with nested json elements
     * @param name the name of the InflaterSettings normally the parent fragment name.
     * @return InflaterSettings corresponding to this name
     */
    public static InflaterSettings get(String name) {
        if(instances.get(name) == null)
            instances.put(name,new InflaterSettings());
        return instances.get(name) ;
    }


}
