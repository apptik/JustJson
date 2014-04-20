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

package org.djodjo.json.wrapper;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.exception.JsonException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Json Array that contains Json Objects of a specific type
 * @param <T>
 */
public class JsonObjectArrayWrapper<T extends JsonObjectWrapper> extends JsonElementWrapper {

    public ArrayList<T> getJsonWrappersList() {
        return jsonWrappersList;
    }

    private final ArrayList<T> jsonWrappersList = new ArrayList<T>() ;

    public JsonObjectArrayWrapper() {
        super();

    }

    public <O extends JsonObjectArrayWrapper> O wrap(JsonArray jsonArray, Class<T> cls) {
        super.wrap(jsonArray);
        jsonWrappersList.clear();

        for(JsonElement e : getJson()) {
            if(e.isJsonObject()) {
                try {
                    T el = cls.newInstance();
                    el.wrap(e);
                    jsonWrappersList.add(el);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }


        return (O) this;
    }

    @Override
    public JsonArray getJson() {
        if(super.getJson() == null) try {
            this.json  = JsonElement.readFrom("[ ]");
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getJson().asJsonArray();
    }

    public <O extends JsonObjectArrayWrapper> O addJsonObjectWrapperItem(T jsonObjectWrapperItem) {
        jsonWrappersList.add(jsonObjectWrapperItem);
        getJson().asJsonArray().put(jsonObjectWrapperItem.getJson());
        return (O)this;
    }

}
