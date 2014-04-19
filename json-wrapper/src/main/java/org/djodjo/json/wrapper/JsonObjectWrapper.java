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


import org.djodjo.json.JsonElement;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.JsonObject;

import java.io.IOException;

public class JsonObjectWrapper extends JsonElementWrapper {

    @Override
    public JsonObject getJson() {
        if(super.getJson() == null) try {
            this.json  = JsonElement.readFrom("{ }");
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getJson().asJsonObject();
    }

    public JsonObjectWrapper() {
        super();
    }

    public JsonObjectWrapper(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public JsonObjectWrapper wrap(JsonElement jsonElement) {
        return super.wrap(jsonElement);
    }
}
