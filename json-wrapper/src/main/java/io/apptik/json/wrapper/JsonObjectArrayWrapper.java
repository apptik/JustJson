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

import java.io.IOException;
import java.util.*;

/**
 * Json Array that contains Json Objects of a specific type
 * @param <T>
 */
public class JsonObjectArrayWrapper<T extends JsonObjectWrapper> extends TypedJsonArray<T> {

    Class<T> cls;

    public JsonObjectArrayWrapper() {
        super();
    }

    @Override
    public <O extends JsonElementWrapper> O wrap(JsonArray jsonElement) {
        throw new IllegalStateException("cannot wrap Typed Element with empty type");
    }

    public <O extends JsonObjectArrayWrapper> O wrap(JsonArray jsonArray, Class<T> cls) {
        super.wrap(jsonArray);
        this.cls = cls;
        return (O) this;
    }

    public JsonObjectArrayWrapper<T> withWrapperType(Class<T> cls) {
        this.cls = cls;
        return this;
    }

    @Override
    protected T get(JsonElement jsonElement, int pos) {
        try {
            return cls.newInstance().wrap(jsonElement.asJsonObject());
        } catch (InstantiationException e) {
            throw new RuntimeException("Error wrapping json", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error wrapping json", e);
        }
    }

    @Override
    protected JsonElement to(T value) {
        return value.getJson();
    }

    @Override
    public boolean contains(Object o) {
        if(o == null) return false;
        if(JsonObjectWrapper.class.isAssignableFrom(o.getClass())) {
            return getJson().contains(((JsonObjectWrapper)o).getJson());
        }
        return super.contains(o);
    }

}
