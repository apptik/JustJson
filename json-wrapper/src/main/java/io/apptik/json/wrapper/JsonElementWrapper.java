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


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import io.apptik.json.ElementWrapper;
import io.apptik.json.JsonElement;
import io.apptik.json.Validator;
import io.apptik.json.exception.JsonException;
import io.apptik.json.util.LinkedTreeMap;


/**
 * Json element Wrapper to be used to wrap JSON data with its schema content type used to describe the data.
 *
 * It can be extended to define a data model implementing getters for required members.
 *
 * Compared to full POJO mapping it performs much faster.
 *
 */
public abstract class JsonElementWrapper implements ElementWrapper {

    protected transient JsonElement json;
    private String contentType;
    private URI jsonSchemaUri;
    private MetaInfo metaInfo;

    private transient LinkedHashSet<Validator> validators = new LinkedHashSet<Validator>();
    private transient LinkedTreeMap<String, MetaInfoFetcher> fetchers = new LinkedTreeMap<String, MetaInfoFetcher>();


    @Override
    public <T extends JsonElement> T getJson() {
        return (T)json;
    }

    public String getContentType() {
        return contentType;
    }

    public URI getJsonSchemaUri() {
        return jsonSchemaUri;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public JsonElementWrapper() {
       //todo do we need default fetcher ?
        // fetchers.put("defaultUriFetcher", new SchemaUriFetcher());
    }

    public JsonElementWrapper(JsonElement jsonElement) {
        this.json = jsonElement;
    }

    public JsonElementWrapper(JsonElement jsonElement, String contentType) {
        this(jsonElement);
        this.contentType = contentType;

    }

    public JsonElementWrapper(JsonElement jsonElement, String contentType, URI metaInfo) {
        this(jsonElement, contentType);
        this.jsonSchemaUri = metaInfo;
        tryFetchMetaInfo(this.jsonSchemaUri);
    }




    /**
     * Tries to fetch a schema and add the default Schema validator for it
     * @param jsonSchemaUri
     * @return
     */
    private MetaInfo tryFetchMetaInfo(URI jsonSchemaUri) {
        if(jsonSchemaUri==null) return null;
        try {
            metaInfo = doFetchMetaInfo(jsonSchemaUri);
            Validator validator = metaInfo.getDefaultValidator();
            if(validator !=null) validators.add(validator);
        } catch (Exception ex) {
            return null;
        }

        return metaInfo;
    }

    private MetaInfo doFetchMetaInfo(URI jsonSchemaUri) {
        MetaInfo currSchema = null;

        Iterator<Map.Entry<String,MetaInfoFetcher>> iterator =  fetchers.entrySet().iterator();
        while (iterator.hasNext() && currSchema==null){
            Map.Entry<String,MetaInfoFetcher> entry = iterator.next();
            System.out.println("JsonElementWrapper try fetch using: " + entry.getKey());
            try {
                currSchema = entry.getValue().fetch(jsonSchemaUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("JsonElementWrapper fetch result: " + ((currSchema==null)?"FAIL":"OK"));
        }
        return currSchema;
    }

    public <T extends JsonElementWrapper> T wrap(JsonElement jsonElement) {
        this.json = jsonElement;
        return (T)this;
    }

    public JsonElementWrapper setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public JsonElementWrapper setMetaInfoUri(URI uri) {
        this.jsonSchemaUri = uri;
        tryFetchMetaInfo(this.jsonSchemaUri);
        return this;
    }

    public JsonElementWrapper setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
    }

    public JsonElementWrapper addSchemaFetcher(String name, MetaInfoFetcher fetcher) {
        this.fetchers.put(name, fetcher);
        return this;
    }

    public MetaInfoFetcher getDefaultSchemaFetcher() {
        return this.fetchers.get("defaultUriFetcher");
    }

    public JsonElementWrapper setDefaultSchemaFetcher(MetaInfoFetcher fetcher) {
        this.fetchers.put("defaultUriFetcher", fetcher);
        return this;
    }

    public JsonElementWrapper setSchemaFetchers(Map<String, MetaInfoFetcher> newFetchers) {
        this.fetchers.clear();
        this.fetchers.putAll(newFetchers);
        return this;
    }

    public JsonElementWrapper addValidator(Validator validator) {
        this.validators.add(validator);
        return this;
    }

    public LinkedHashSet<Validator> getValidators() {
        return validators;
    }

    public boolean isDataValid() {
        for (Validator validator : validators) {
            if (!validator.isValid(this.getJson()))
                return false;
        }
        return true;
    }

    public boolean validateData(StringBuilder sb) {
        boolean res = true;
        Iterator<Validator> iterator = validators.iterator();
        System.out.println("JsonElementWrapper start validating ");
        Validator validator;
        while (iterator.hasNext()){
            validator =  iterator.next();
            System.out.println("JsonElementWrapper validating using: " + validator.getTitle());

            if(!validator.validate(this.getJson(), sb)) {
                res = false;
            }
        }
        return res;
    }

    public MetaInfo fetchMetaInfo() {
        if(metaInfo ==null)
            tryFetchMetaInfo(this.jsonSchemaUri);
        return metaInfo;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(json.toString());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, JsonException {
        ois.defaultReadObject();
        this.wrap(JsonElement.readFrom((String) ois.readObject()));
    }

    public String toString() {
        return getJson().toString();
    }


}
