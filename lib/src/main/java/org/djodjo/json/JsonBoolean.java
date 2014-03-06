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

public class JsonBoolean extends JsonElement {

    private final boolean value;

    JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public int hashCode() {
        return new Boolean(value).hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        return o instanceof JsonBoolean && ((JsonBoolean) o).value==value;
   }

    @Override
    protected void write( JsonWriter writer ) throws IOException {
        writer.value(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}
