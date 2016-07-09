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


import io.apptik.json.JsonElement;
import io.apptik.json.exception.JsonException;
import io.apptik.json.JsonObject;

import java.io.IOException;
import java.net.URI;

public class JsonObjectWrapper extends JsonElementWrapper<JsonObject> {

    public JsonObjectWrapper() {
    }

    public JsonObjectWrapper(JsonObject jsonElement) {
        super(jsonElement);
    }

    public JsonObjectWrapper(JsonObject jsonElement, String contentType) {
        super(jsonElement, contentType);
    }

    public JsonObjectWrapper(JsonObject jsonElement, String contentType, URI metaInfo) {
        super(jsonElement, contentType, metaInfo);
    }

    @Override
    public JsonObject getJson() {
        if(super.getJson() == null) try {
            this.json  = JsonElement.readFrom("{ }").asJsonObject();
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getJson();
    }

    /**
     * Merges JsonObjectWrapper with another.
     * If values are already present they are not changed.
     * @param anotherJsonObjectWrapper
     * @return
     */
    public JsonObjectWrapper merge(JsonObjectWrapper anotherJsonObjectWrapper) {
        this.getJson().merge(anotherJsonObjectWrapper.getJson());
        return this;
    }

}
