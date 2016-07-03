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

package io.apptik.json.generator;


import io.apptik.json.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class JsonGeneratorConfig {

    //for object type
    public HashMap<String, Integer> objectPropertiesMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> objectPropertiesMax =  new HashMap<String, Integer>();
    public Integer globalObjectPropertiesMin =  null;
    public Integer globalObjectPropertiesMax = null;

    //for array type
    //first check for predefined items, those items can be filled in a random order
    public HashMap<String, ArrayList<JsonElement>> arrayPredefinedItems =  new HashMap<String, ArrayList<JsonElement>>();
    public HashMap<String, Integer> arrayItemsMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> arrayItemsMax =  new HashMap<String, Integer>();
    public Integer globalArrayItemsMin = null;
    public Integer globalArrayItemsMax = null;


    //for integer type
    public HashMap<String, Integer> integerMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> integerMax =  new HashMap<String, Integer>();
    public Integer globalIntegerMin = null;
    public Integer globalIntegerMax = null;

    //for number type
    public HashMap<String, Integer> numberMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> numberMax =  new HashMap<String, Integer>();
    public Integer globalNumberMin = null;
    public Integer globalNumberMax = null;

    //for string type
    //first check predefined values, acts as if an enum is set in the schema
    public HashMap<String, ArrayList<String>> stringPredefinedValues =  new HashMap<String, ArrayList<String>>();
    public HashMap<String, Integer> stringLengthMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> stringLengthMax =  new HashMap<String, Integer>();
    public Integer globalStringLengthMin = null;
    public Integer globalStringLengthMax = null;

    //for date and datetime formats, string type
    public Date globalDateMin = null;
    public Date globalDateMax = null;
    public HashMap<String, Date> dateMin =  new HashMap<String, Date>();
    public HashMap<String, Date> dateMax =  new HashMap<String, Date>();

    //for uri format, string type
    public ArrayList<String> globalUriSchemes = new ArrayList<String>();
    public ArrayList<String> globalUriHosts = new ArrayList<String>();
    public ArrayList<String> globalUriPaths = new ArrayList<String>();

    public HashMap<String, ArrayList<String>> uriSchemes =  new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> uriHosts =  new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> uriPaths =  new HashMap<String, ArrayList<String>>();

    public HashMap<String, Integer> uriPathLengthMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> uriPathLengthMax =  new HashMap<String, Integer>();
    public Integer globalUriPathLengthMin = null;
    public Integer globalUriPathLengthMax = null;


    public ArrayList<String> globalEmailHosts = new ArrayList<String>();
    public ArrayList<String> globalEmailLocalParts = new ArrayList<String>();

    public HashMap<String, ArrayList<String>> emailHosts =  new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> emailLocalParts =  new HashMap<String, ArrayList<String>>();

    public HashMap<String, Integer> emailLocalPartLengthMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> emailLocalPartLengthMax =  new HashMap<String, Integer>();
    public Integer globalEmailLocalPartLengthMin = null;
    public Integer globalEmailLocalPartLengthMax = null;


    public HashMap<String, Integer> emailHostLengthMin =  new HashMap<String, Integer>();
    public HashMap<String, Integer> emailHostLengthMax =  new HashMap<String, Integer>();
    public Integer globalEmailHostLengthMin = null;
    public Integer globalEmailHostLengthMax = null;

    public ArrayList<String> skipObjectProperties = new ArrayList();



}
