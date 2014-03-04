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

package org.djodjo.json;


import java.io.IOException;
import java.util.ArrayList;

public class JsonStringArrayWrapper extends JsonElementWrapper {

    public ArrayList<String> getStringList() {
        return jsonWrappersList;
    }

    private final ArrayList<String> jsonWrappersList = new ArrayList<String>() ;

    public JsonStringArrayWrapper() {
        super();

    }

    public JsonStringArrayWrapper wrap(JsonArray jsonArray) {
        super.wrap(jsonArray);
        jsonWrappersList.clear();
        for(JsonElement e : getJson()) {
            jsonWrappersList.add(e.toString());
        }
        return this;
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


}
