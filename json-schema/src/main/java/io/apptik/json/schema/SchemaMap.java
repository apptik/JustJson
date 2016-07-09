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

package io.apptik.json.schema;


import io.apptik.json.JsonElement;
import io.apptik.json.wrapper.TypedJsonObject;

public class SchemaMap extends TypedJsonObject<Schema> {

    //blank schema used to obtain sub-schemas
    public Schema schema;

    public SchemaMap(Schema schema) {
        this.schema = schema;
    }

    @Override
    protected Schema get(JsonElement jsonElement, String key) {
        return (Schema) schema.getEmptySchema(key).wrap(jsonElement.asJsonObject());
    }


    @Override
    protected JsonElement to(Schema value) {
        return value.getJson();
    }
}
