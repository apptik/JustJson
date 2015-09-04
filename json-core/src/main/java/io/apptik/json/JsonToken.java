/*
 * Copyright (C) 2014 Kalin Maldzhanski
 * Copyright (C) 2010 The Android Open Source Project
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

/**
 * A structure, name or value type in a Util-encoded string.
 */
public enum JsonToken {

    /**
     * The opening of a Util array. Written using {@link JsonWriter#beginObject}
     * and read using {@link JsonReader#beginObject}.
     */
    BEGIN_ARRAY,

    /**
     * The closing of a Util array. Written using {@link JsonWriter#endArray}
     * and read using {@link JsonReader#endArray}.
     */
    END_ARRAY,

    /**
     * The opening of a Util object. Written using {@link JsonWriter#beginObject}
     * and read using {@link JsonReader#beginObject}.
     */
    BEGIN_OBJECT,

    /**
     * The closing of a Util object. Written using {@link JsonWriter#endObject}
     * and read using {@link JsonReader#endObject}.
     */
    END_OBJECT,

    /**
     * A Util property name. Within objects, tokens alternate between names and
     * their values. Written using {@link JsonWriter#name} and read using {@link
     * JsonReader#nextName}
     */
    NAME,

    /**
     * A Util string.
     */
    STRING,

    /**
     * A Util number represented in this API by a Java {@code double}, {@code
     * long}, or {@code int}.
     */
    NUMBER,

    /**
     * A Util {@code true} or {@code false}.
     */
    BOOLEAN,

    /**
     * A Util {@code null}.
     */
    NULL,

    /**
     * The end of the Util stream. This sentinel value is returned by {@link
     * JsonReader#peek()} to signal that the Util-encoded value has no more
     * tokens.
     */
    END_DOCUMENT
}
