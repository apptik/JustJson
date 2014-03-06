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

package org.djodjo.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

// Note: this class was written without inspecting the non-free org.json sourcecode.

/**
 * A dense indexed sequence of values. Values may be any mix of
 * {@link JsonObject JsonObjects}, other {@link JsonArray JsonArrays}, Strings,
 * Booleans, Integers, Longs, Doubles, {@code null} or {@link JsonNull}.
 * Values may not be {@link Double#isNaN() NaNs}, {@link Double#isInfinite()
 * infinities}, or of any type not listed here.
 *
 * <p>{@code JsonArray} has the same type coercion behavior and
 * optional/mandatory accessors as {@link JsonObject}. See that class'
 * documentation for details.
 *
 * <p><strong>Warning:</strong> this class represents null in two incompatible
 * ways: the standard Java {@code null} reference, and the sentinel value {@link
 * JsonNull}. In particular, {@code get} fails if the requested index
 * holds the null reference, but succeeds if it holds {@code JsonNull}.
 *
 * <p>Instances of this class are not thread safe. Although this class is
 * nonfinal, it was not designed for inheritance and should not be subclassed.
 * In particular, self-use by overridable methods is not specified. See
 * <i>Effective Java</i> Item 17, "Design and Document or inheritance or else
 * prohibit it" for further information.
 */
public class JsonArray extends JsonElement implements Iterable<JsonElement> {

    private final List<JsonElement> values;

    /**
     * Creates a {@code JsonArray} with no values.
     */
    public JsonArray() {
        values = new ArrayList<JsonElement>();
    }

    /**
     * Creates a new {@code JsonArray} by copying all values from the given
     * collection.
     *
     * @param copyFrom a collection whose values are of supported types.
     *     Unsupported values are not permitted and will yield an array in an
     *     inconsistent state.
     */
    /* Accept a raw type for API compatibility */
    public JsonArray(Collection copyFrom) {
        this();
        if (copyFrom != null) {
            for (Iterator it = copyFrom.iterator(); it.hasNext();) {
                put(JsonElement.wrap(it.next()));
            }
        }
    }


    /**
     * Creates a new {@code JsonArray} with values from the given primitive array.
     */
    public JsonArray(Object array) throws JsonException {
        if (!array.getClass().isArray()) {
            throw new JsonException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        values = new ArrayList<JsonElement>(length);
        for (int i = 0; i < length; ++i) {
            put(JsonElement.wrap(Array.get(array, i)));
        }
    }

    /**
     * Returns the number of values in this array.
     */
    public int length() {
        return values.size();
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public JsonArray put(boolean value) {
        return put(new JsonBoolean(value));
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
     *     {@link Double#isInfinite() infinities}.
     * @return this array.
     */
    public JsonArray put(double value) throws JsonException {
        return put(new JsonNumber(value));
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public JsonArray put(int value) {
        return put(new JsonNumber(value));
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public JsonArray put(long value) {
        return put(new JsonNumber(value));
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @param value a {@link JsonObject}, {@link JsonArray}, String, Boolean,
     *     Integer, Long, Double, or {@code null}. May
     *     not be {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *     infinities}. Unsupported values are not permitted and will cause the
     *     array to be in an inconsistent state.
     * @return this array.
     */
    public JsonArray put(Object value) {
        return put(wrap(value));
    }

    public JsonArray put(JsonElement value) {
        if (value == null) {
            value = new JsonNull();
        }
        values.add(value);
        return this;
    }

    /**
     * Same as {@link #put}, with added validity checks.
     */
    void checkedPut(Object value) throws JsonException {
        if (value instanceof Number) {
            Util.checkDouble(((Number) value).doubleValue());
        }

        put(value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public JsonArray put(int index, boolean value) throws JsonException {
        return put(index, (Boolean) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
     *     {@link Double#isInfinite() infinities}.
     * @return this array.
     */
    public JsonArray put(int index, double value) throws JsonException {
        return put(index, (Double) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public JsonArray put(int index, int value) throws JsonException {
        return put(index, (Integer) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public JsonArray put(int index, long value) throws JsonException {
        return put(index, (Long) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @param value a {@link JsonObject}, {@link JsonArray}, String, Boolean,
     *     Integer, Long, Double, or {@code null}. May
     *     not be {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *     infinities}.
     * @return this array.
     */
    public JsonArray put(int index, Object value) throws JsonException {

        while (values.size() <= index) {
            values.add(null);
        }
        values.set(index, wrap(value));
        return this;
    }

    /**
     * Returns true if this array has no value at {@code index}, or if its value
     * is the {@code null} reference .
     */
    public boolean isNull(int index) {
        Object value = opt(index);
        return value == null || value == new JsonNull();
    }

    /**
     * Returns the value at {@code index}.
     *
     * @throws JsonException if this array has no value at {@code index}, or if
     *     that value is the {@code null} reference. This method returns
     *     normally if the value is {@code JsonObject#NULL}.
     */
    public JsonElement get(int index) throws JsonException {
        try {
            JsonElement value = values.get(index);
            if (value == null) {
                throw new JsonException("Value at " + index + " is null.");
            }
            return value;
        } catch (IndexOutOfBoundsException e) {
            throw new JsonException("Index " + index + " out of range [0.." + values.size() + ")");
        }
    }

    /**
     * Returns the value at {@code index}, or null if the array has no value
     * at {@code index}.
     */
    public JsonElement opt(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    /**
     * Removes and returns the value at {@code index}, or null if the array has no value
     * at {@code index}.
     */
    public JsonElement remove(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        return values.remove(index);
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean.
     *
     * @throws JsonException if the value at {@code index} doesn't exist or
     *     cannot be coerced to a boolean.
     */
    public boolean getBoolean(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isBoolean()) {
            throw Util.typeMismatch(index, el, "boolean");
        }
        return el.asBoolean();
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean. Returns false otherwise.
     */
    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean. Returns {@code fallback} otherwise.
     */
    public boolean optBoolean(int index, boolean fallback) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return fallback;
        }
        if(!el.isBoolean()) {
            return fallback;
        }
        return el.asBoolean();
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double.
     *
     * @throws JsonException if the value at {@code index} doesn't exist or
     *     cannot be coerced to a double.
     */
    public double getDouble(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isNumber()) {
            throw Util.typeMismatch(index, el, "double");
        }
        return el.asDouble();
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double. Returns {@code NaN} otherwise.
     */
    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double. Returns {@code fallback} otherwise.
     */
    public double optDouble(int index, double fallback) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return fallback;
        }
        if(!el.isNumber()) {
            return fallback;
        }
        return el.asDouble();
    }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int.
     *
     * @throws JsonException if the value at {@code index} doesn't exist or
     *     cannot be coerced to a int.
     */
    public int getInt(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isNumber()) {
            throw Util.typeMismatch(index, el, "int");
        }
        return el.asInt();
   }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int. Returns 0 otherwise.
     */
    public int optInt(int index) {
        return optInt(index, 0);
    }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int. Returns {@code fallback} otherwise.
     */
    public int optInt(int index, int fallback) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return fallback;
        }
        if(!el.isNumber()) {
            return fallback;
        }
        return el.asInt();
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long.
     *
     * @throws JsonException if the value at {@code index} doesn't exist or
     *     cannot be coerced to a long.
     */
    public long getLong(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isNumber()) {
            throw Util.typeMismatch(index, el, "long");
        }
        return el.asLong();
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long. Returns 0 otherwise.
     */
    public long optLong(int index) {
        return optLong(index, 0L);
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long. Returns {@code fallback} otherwise.
     */
    public long optLong(int index, long fallback) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return fallback;
        }
        if(!el.isNumber()) {
            return fallback;
        }
        return el.asInt();
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary.
     *
     * @throws JsonException if no such value exists.
     */
    public String getString(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isString()) {
            throw Util.typeMismatch(index, el, "String");
        }
        return el.asString();
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary. Returns the empty string if no such value exists.
     */
    public String optString(int index) {
        return optString(index, "");
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary. Returns {@code fallback} if no such value exists.
     */
    public String optString(int index, String fallback) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return fallback;
        }
        if(!el.isString()) {
            return fallback;
        }
        return el.asString();
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * JsonArray}.
     *
     * @throws JsonException if the value doesn't exist or is not a {@code
     *     JsonArray}.
     */
    public JsonArray getJsonArray(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isJsonArray()) {
            throw Util.typeMismatch(index, el, "JsonArray");
        }
        return el.asJsonArray();
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * JsonArray}. Returns null otherwise.
     */
    public JsonArray optJsonArray(int index) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return null;
        }
        if(!el.isJsonArray()) {
            return null;
        }
        return el.asJsonArray();
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * JsonObject}.
     *
     * @throws JsonException if the value doesn't exist or is not a {@code
     *     JsonObject}.
     */
    public JsonObject getJsonObject(int index) throws JsonException {
        JsonElement el = get(index);
        if (!el.isJsonObject()) {
            throw Util.typeMismatch(index, el, "JsonObject");
        }
        return el.asJsonObject();
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * JsonObject}. Returns null otherwise.
     */
    public JsonObject optJsonObject(int index) {
        JsonElement el;
        try {
            el = get(index);
        } catch (JsonException e) {
            return null;
        }
        if(!el.isJsonObject()) {
            return null;
        }
        return el.asJsonObject();
    }

    /**
     * Returns a new object whose values are the values in this array, and whose
     * names are the values in {@code names}. Names and values are paired up by
     * index from 0 through to the shorter array's length. Names that are not
     * strings will be coerced to strings. This method returns null if either
     * array is empty.
     */
    public JsonObject toJsonObject(JsonArray names) throws JsonException {
        JsonObject result = new JsonObject();
        int length = Math.min(names.length(), values.size());
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            String name = names.opt(i).toString();
            result.put(name, opt(i));
        }
        return result;
    }

    @Override
    protected void write( JsonWriter writer ) throws IOException {
        writer.beginArray();
        for (JsonElement e : this) {
            e.write(writer);
        }
        writer.endArray();
    }

    @Override
    public boolean isJsonArray() {
        return true;
    }

    @Override
    public JsonArray asJsonArray() {
        return this;
    }

    @Override public boolean equals(Object o) {
        return o instanceof JsonArray && ((JsonArray) o).values.equals(values);
    }

    @Override public int hashCode() {
        // diverge from the original, which doesn't implement hashCode
        return values.hashCode();
    }

    @Override
    public Iterator<JsonElement> iterator() {
        return values.iterator();
    }
}
