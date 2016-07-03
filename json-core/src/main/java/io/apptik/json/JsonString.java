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


package io.apptik.json;


import java.io.IOException;

public final class JsonString extends JsonElement {

    private final String value;

    public JsonString( String value) {
        if( value == null ) {
            throw new NullPointerException( "value is null" );
        }
        this.value = value;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String toString() {
        //we dont want to return the same object
        return new String(value);
    }

    @Override
    public String asString() {
        //we dont want to return the same object
        return new String(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        return (o instanceof JsonString && ((JsonString) o).value.equals(value))
                || (o != null && o.equals(value))
                ;
    }

    @Override
    public void write( JsonWriter writer ) throws IOException {
        writer.value(value);
    }

    @Override
    public String getJsonType() {
        return TYPE_STRING;
    }


}
