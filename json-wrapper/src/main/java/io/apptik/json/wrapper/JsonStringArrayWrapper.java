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

package io.apptik.json.wrapper;


import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonString;
import io.apptik.json.exception.JsonException;

import java.io.IOException;
import java.util.ArrayList;

public class JsonStringArrayWrapper extends TypedJsonArray<String> {

    public JsonStringArrayWrapper() {
        super();
    }

    public JsonStringArrayWrapper wrap(JsonArray jsonArray) {
        super.wrap(jsonArray);
        return this;
    }

    @Override
    protected String get(JsonElement jsonElement, int pos) {
        return jsonElement.asString();
    }

    @Override
    protected JsonElement to(String value) {
        return new JsonString(value);
    }


}
