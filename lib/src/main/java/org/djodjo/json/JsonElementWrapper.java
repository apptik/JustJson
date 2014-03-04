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


import java.net.URI;


/**
 * Json element Wrapper to be used to wrap JSON data with its schema content type used to describe the data.
 *
 * It can be extended to define a data model implementing getters for required members.
 *
 * Compared to full POJO mapping it performs much faster.
 *
 */
public abstract class JsonElementWrapper {

    protected JsonElement json;
    private String contentType;
    private URI jsonSchemaUri;

    public JsonElement getJson() {
        return json;
    }

    public String getContentType() {
        return contentType;
    }

    public URI getJsonSchemaUri() {
        return jsonSchemaUri;
    }


    public JsonElementWrapper() {
    }

    public JsonElementWrapper(JsonElement jsonElement) {
        this.json = jsonElement;
    }

    public JsonElementWrapper(JsonElement jsonElement, String contentType) {
        this(jsonElement);
        this.contentType = contentType;

    }

    public JsonElementWrapper(JsonElement jsonElement, String contentType, URI jsonSchema) {
        this(jsonElement, contentType);
        this.jsonSchemaUri = jsonSchema;
    }

    public JsonElementWrapper wrap(JsonElement jsonElement) {
        this.json = jsonElement;
        return this;
    }

    public JsonElementWrapper setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public JsonElementWrapper setJsonSchema(URI uri) {
        this.jsonSchemaUri = uri;
        return this;
    }

}
