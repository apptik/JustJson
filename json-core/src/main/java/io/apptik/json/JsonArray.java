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

import static io.apptik.json.JsonNull.JSON_NULL;
import io.apptik.json.exception.JsonException;
import io.apptik.json.util.Freezable;
import io.apptik.json.util.Util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Note: this class was written without inspecting the non-free org.json sourcecode.

/**
 * A dense indexed sequence of values. Values may be any mix of
 * {@link JsonObject JsonObjects}, other {@link JsonArray JsonArrays}, Strings,
 * Booleans, Integers, Longs, Doubles, {@code null} or {@link JsonNull}. Values
 * may not be {@link Double#isNaN() NaNs}, {@link Double#isInfinite()
 * infinities}, or of any type not listed here.
 * <p>
 * <p>
 * {@code JsonArray} has the same type coercion behavior and optional/mandatory
 * accessors as {@link JsonObject}. See that class' documentation for details.
 * <p>
 * <p>
 * <strong>Warning:</strong> this class represents null in two incompatible
 * ways: the standard Java {@code null} reference, and the sentinel value
 * {@link JsonNull}. In particular, {@code get} fails if the requested index
 * holds the null reference, but succeeds if it holds {@code JsonNull}.
 * <p>
 * <p>
 * Instances of this class are not thread safe.
 */
public final class JsonArray extends JsonElement implements List<JsonElement>,
		Freezable<JsonArray> {

	private volatile boolean frozen = false;
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
	 * @param copyFrom
	 *            a collection whose values are of supported types. Unsupported
	 *            values are not permitted and will yield an array in an
	 *            inconsistent state.
	 */
	/* Accept a raw type for API compatibility */
	public JsonArray(final Collection copyFrom) throws JsonException {
		this();
		if (copyFrom != null) {
			for (Object aCopyFrom : copyFrom) {
				put(JsonElement.wrap(aCopyFrom));
			}
		}
	}

	/**
	 * Creates a new {@code JsonArray} with values from the given primitive
	 * array.
	 */
	public JsonArray(final Object array) throws JsonException {
		if (!array.getClass().isArray()) {
			throw new JsonException("Not a primitive array: "
					+ array.getClass());
		}
		final int length = Array.getLength(array);
		values = new ArrayList<JsonElement>(length);
		for (int i = 0; i < length; ++i) {
			put(JsonElement.wrap(Array.get(array, i)));
		}
	}

	public void add(final int i, final JsonElement jsonElement) {
		checkIfFrozen();
		put(i, jsonElement);
	}

	public boolean add(final JsonElement jsonElement) {
		checkIfFrozen();
		return values.add(jsonElement);
	}

	public boolean addAll(final Collection<? extends JsonElement> jsonElements) {
		checkIfFrozen();
		return values.addAll(jsonElements);
	}

	public boolean addAll(final int i,
			final Collection<? extends JsonElement> jsonElements) {
		checkIfFrozen();
		return addAll(jsonElements);
	}

	@Override
	public JsonArray asJsonArray() {
		return this;
	}

	public void checkIfFrozen() {
		if (isFrozen()) {
			throw new IllegalStateException(
					"Attempt to modify a frozen JsonArray instance.");
		}
	}

	public void clear() {
		checkIfFrozen();
		values.clear();
	}

	public JsonArray cloneAsThawed() {
		try {
			return JsonElement.readFrom(this.toString()).asJsonArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new JsonException("Cannot Recreate Json Array", e);
		}
	}

	public boolean contains(final Object o) {
		return values.contains(o);
	}

	public boolean containsAll(final Collection<?> objects) {
		return values.containsAll(objects);
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof JsonArray && ((JsonArray) o).values.equals(values);
	}

	public JsonArray freeze() {
		frozen = true;
		for (JsonElement el : values) {
			if (el.isJsonArray()) {
				el.asJsonArray().freeze();
			}
			if (el.isJsonObject()) {
				el.asJsonObject().freeze();
			}
		}
		return this;
	}

	/**
	 * Returns the value at {@code index}.
	 *
	 * @throws JsonException
	 *             if this array has no value at {@code index}, or if that value
	 *             is the {@code null} reference. This method returns normally
	 *             if the value is {@code JsonObject#NULL}.
	 */
	public JsonElement get(final int index) throws JsonException {
		try {
			JsonElement value = values.get(index);
			if (value == null) {
				throw new JsonException("Value at " + index + " is null.");
			}
			return value;
		} catch (IndexOutOfBoundsException e) {
			throw new JsonException("Index " + index + " out of range [0.."
					+ values.size() + ")");
		}
	}

	public Boolean getBoolean(final int index) throws JsonException {
		return getBoolean(index, false);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a boolean or can
	 * be coerced to a boolean.
	 *
	 * @throws JsonException
	 *             if the value at {@code index} doesn't exist or cannot be
	 *             coerced to a boolean.
	 */
	public Boolean getBoolean(final int index, final boolean strict)
			throws JsonException {
		JsonElement el = get(index);
		Boolean res = null;
		if (strict && !el.isBoolean()) {
			throw Util.typeMismatch(index, el, "boolean", true);
		}
		if (el.isBoolean()) {
			res = el.asBoolean();
		}
		if (el.isString()) {
			res = Util.toBoolean(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(index, el, "boolean", strict);
		}
		return res;
	}

	public Double getDouble(final int index) throws JsonException {
		return getDouble(index, false);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a double or can be
	 * coerced to a double.
	 *
	 * @throws JsonException
	 *             if the value at {@code index} doesn't exist or cannot be
	 *             coerced to a double.
	 */
	public Double getDouble(final int index, final boolean strict)
			throws JsonException {
		JsonElement el = get(index);
		Double res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(index, el, "double", true);
		}
		if (el.isNumber()) {
			res = el.asDouble();
		}
		if (el.isString()) {
			res = Util.toDouble(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(index, el, "double", strict);
		}
		return res;
	}

	/**
	 * Returns the value at {@code index} if it exists and is an int or can be
	 * coerced to an int.
	 *
	 * @throws JsonException
	 *             if the value at {@code index} doesn't exist or cannot be
	 *             coerced to a int.
	 */
	public Integer getInt(final int index) throws JsonException {
		return getInt(index, false);
	}

	/**
	 * Returns the value at {@code index} if it exists and is an int or can be
	 * coerced to an int. Returns 0 otherwise.
	 */
	public Integer getInt(final int index, final boolean strict)
			throws JsonException {
		JsonElement el = get(index);
		Integer res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(index, el, "int", true);
		}
		if (el.isNumber()) {
			res = el.asInt();
		}
		if (el.isString()) {
			res = Util.toInteger(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(index, el, "int", strict);
		}
		return res;
	}

	/**
	 * Returns the value at {@code index} if it exists and is a
	 * {@code JsonArray}.
	 *
	 * @throws JsonException
	 *             if the value doesn't exist or is not a {@code JsonArray}.
	 */
	public JsonArray getJsonArray(final int index) throws JsonException {
		JsonElement el = get(index);
		if (!el.isJsonArray()) {
			throw Util.typeMismatch(index, el, "JsonArray");
		}
		return el.asJsonArray();
	}

	/**
	 * Returns the value at {@code index} if it exists and is a
	 * {@code JsonObject}.
	 *
	 * @throws JsonException
	 *             if the value doesn't exist or is not a {@code JsonObject}.
	 */
	public JsonObject getJsonObject(final int index) throws JsonException {
		JsonElement el = get(index);
		if (!el.isJsonObject()) {
			throw Util.typeMismatch(index, el, "JsonObject");
		}
		return el.asJsonObject();
	}

	@Override
	public String getJsonType() {
		return TYPE_ARRAY;
	}

	public Long getLong(final int index) throws JsonException {
		return getLong(index, false);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a long or can be
	 * coerced to a long.
	 *
	 * @throws JsonException
	 *             if the value at {@code index} doesn't exist or cannot be
	 *             coerced to a long.
	 */

	public Long getLong(final int index, final boolean strict)
			throws JsonException {
		JsonElement el = get(index);
		Long res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(index, el, "long", true);
		}
		if (el.isNumber()) {
			res = el.asLong();
		}
		if (el.isString()) {
			res = Util.toLong(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(index, el, "long", strict);
		}
		return res;
	}

	public String getString(final int index) throws JsonException {
		return getString(index, false);
	}

	/**
	 * Returns the value at {@code index} if it exists, coercing it if
	 * necessary.
	 *
	 * @throws JsonException
	 *             if no such value exists.
	 */
	public String getString(final int index, final boolean strict)
			throws JsonException {
		JsonElement el = get(index);
		String res = null;
		if (strict && !el.isString()) {
			throw Util.typeMismatch(index, el, "string", true);
		}
		res = el.toString();
		if (res == null) {
			throw Util.typeMismatch(index, el, "string", strict);
		}
		return res;
	}

	@Override
	public int hashCode() {
		// diverge from the original, which doesn't implement hashCode
		return values.hashCode();
	}

	public int indexOf(final Object o) {
		return values.indexOf(o);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public boolean isFrozen() {
		return frozen;
	}

	@Override
	public boolean isJsonArray() {
		return true;
	}

	/**
	 * Returns true if this array has no value at {@code index}, or if its value
	 * is the {@code null} reference .
	 */
	public boolean isNull(final int index) {
		Object value = opt(index);
		return value == null || value.equals(JSON_NULL);
	}

	public boolean isOnlyArrays() {
		for (JsonElement el : values) {
			if (!el.isJsonArray()) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnlyBooleans() {
		for (JsonElement el : values) {
			if (!el.isBoolean()) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnlyNumbers() {
		for (JsonElement el : values) {
			if (!el.isNumber()) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnlyObjects() {
		for (JsonElement el : values) {
			if (!el.isJsonObject()) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnlyStrings() {
		for (JsonElement el : values) {
			if (!el.isString()) {
				return false;
			}
		}
		return true;
	}

	public Iterator<JsonElement> iterator() {
		return values.iterator();
	}

	public int lastIndexOf(final Object o) {
		return values.lastIndexOf(o);
	}

	/**
	 * Returns the number of values in this array.
	 */
	public int length() {
		return values.size();
	}

	public ListIterator<JsonElement> listIterator() {
		return values.listIterator();
	}

	public ListIterator<JsonElement> listIterator(final int i) {
		return values.listIterator(i);
	}

	/**
	 * Returns the value at {@code index}, or null if the array has no value at
	 * {@code index}.
	 */
	public JsonElement opt(final int index) {
		if (index < 0 || index >= values.size()) {
			return null;
		}
		return values.get(index);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a boolean or can
	 * be coerced to a boolean. Returns false otherwise.
	 */
	public Boolean optBoolean(final int index) {
		return optBoolean(index, null);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a boolean or can
	 * be coerced to a boolean. Returns {@code fallback} otherwise.
	 */
	public Boolean optBoolean(final int index, final Boolean fallback) {
		return optBoolean(index, fallback, false);
	}

	public Boolean optBoolean(final int index, final Boolean fallback,
			final boolean strict) {
		try {
			return getBoolean(index, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value at {@code index} if it exists and is a double or can be
	 * coerced to a double. Returns {@code NaN} otherwise.
	 */
	public Double optDouble(final int index) {
		return optDouble(index, null);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a double or can be
	 * coerced to a double. Returns {@code fallback} otherwise.
	 */
	public Double optDouble(final int index, final Double fallback) {
		return optDouble(index, fallback, false);
	}

	public Double optDouble(final int index, final Double fallback,
			final boolean strict) {
		try {
			return getDouble(index, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	public Integer optInt(final int index) {
		return optInt(index, null);
	}

	/**
	 * Returns the value at {@code index} if it exists and is an int or can be
	 * coerced to an int. Returns {@code fallback} otherwise.
	 */
	public Integer optInt(final int index, final Integer fallback) {
		return optInt(index, fallback, false);
	}

	public Integer optInt(final int index, final Integer fallback,
			final boolean strict) {
		try {
			return getInt(index, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value at {@code index} if it exists and is a
	 * {@code JsonArray}. Returns null otherwise.
	 */
	public JsonArray optJsonArray(final int index) {
		JsonElement el;
		try {
			el = get(index);
		} catch (JsonException e) {
			return null;
		}
		if (!el.isJsonArray()) {
			return null;
		}
		return el.asJsonArray();
	}

	/**
	 * Returns the value at {@code index} if it exists and is a
	 * {@code JsonObject}. Returns null otherwise.
	 */
	public JsonObject optJsonObject(final int index) {
		JsonElement el;
		try {
			el = get(index);
		} catch (JsonException e) {
			return null;
		}
		if (!el.isJsonObject()) {
			return null;
		}
		return el.asJsonObject();
	}

	/**
	 * Returns the value at {@code index} if it exists and is a long or can be
	 * coerced to a long. Returns 0 otherwise.
	 */
	public Long optLong(final int index) {
		return optLong(index, null);
	}

	/**
	 * Returns the value at {@code index} if it exists and is a long or can be
	 * coerced to a long. Returns {@code fallback} otherwise.
	 */
	public Long optLong(final int index, final Long fallback) {
		return optLong(index, fallback, false);
	}

	public Long optLong(final int index, final Long fallback,
			final boolean strict) {
		try {
			return getLong(index, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value at {@code index} if it exists, coercing it if
	 * necessary. Returns the empty string if no such value exists.
	 */
	public String optString(final int index) {
		return optString(index, null);
	}

	/**
	 * Returns the value at {@code index} if it exists, coercing it if
	 * necessary. Returns {@code fallback} if no such value exists.
	 */
	public String optString(final int index, final String fallback) {
		return optString(index, fallback, false);
	}

	public String optString(final int index, final String fallback,
			final boolean strict) {
		try {
			return getString(index, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Appends {@code value} to the end of this array.
	 *
	 * @return this array.
	 */
	public JsonArray put(final boolean value) {
		return put(new JsonBoolean(value));
	}

	/**
	 * Appends {@code value} to the end of this array.
	 *
	 * @param value
	 *            a finite value. May not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this array.
	 */
	public JsonArray put(final double value) throws JsonException {
		return put(new JsonNumber(value));
	}

	/**
	 * Appends {@code value} to the end of this array.
	 *
	 * @return this array.
	 */
	public JsonArray put(final int value) {
		return put(new JsonNumber(value));
	}

	/**
	 * Sets the value at {@code index} to {@code value}, null padding this array
	 * to the required length if necessary. If a value already exists at
	 * {@code index}, it will be replaced.
	 *
	 * @return this array.
	 */
	public JsonArray put(final int index, final boolean value)
			throws JsonException {
		return put(index, (Boolean) value);
	}

	/**
	 * Sets the value at {@code index} to {@code value}, null padding this array
	 * to the required length if necessary. If a value already exists at
	 * {@code index}, it will be replaced.
	 *
	 * @param value
	 *            a finite value. May not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this array.
	 */
	public JsonArray put(final int index, final double value)
			throws JsonException {
		return put(index, (Double) value);
	}

	/**
	 * Sets the value at {@code index} to {@code value}, null padding this array
	 * to the required length if necessary. If a value already exists at
	 * {@code index}, it will be replaced.
	 *
	 * @return this array.
	 */
	public JsonArray put(final int index, final int value) throws JsonException {
		return put(index, (Integer) value);
	}

	/**
	 * Sets the value at {@code index} to {@code value}, null padding this array
	 * to the required length if necessary. If a value already exists at
	 * {@code index}, it will be replaced.
	 *
	 * @return this array.
	 */
	public JsonArray put(final int index, final long value)
			throws JsonException {
		return put(index, (Long) value);
	}

	/**
	 * Sets the value at {@code index} to {@code value}, null padding this array
	 * to the required length if necessary. If a value already exists at
	 * {@code index}, it will be replaced.
	 *
	 * @param value
	 *            a {@link JsonObject}, {@link JsonArray}, String, Boolean,
	 *            Integer, Long, Double, or {@code null}. May not be
	 *            {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
	 *            infinities}.
	 * @return this array.
	 */
	public JsonArray put(final int index, final Object value)
			throws JsonException {
		checkIfFrozen();
		while (values.size() <= index) {
			values.add(JSON_NULL);
		}
		values.set(index, wrap(value));
		return this;
	}

	public JsonArray put(final JsonElement value) {
		checkIfFrozen();
		putInternal(value);

		return this;
	}

	/**
	 * Appends {@code value} to the end of this array.
	 *
	 * @return this array.
	 */
	public JsonArray put(final long value) {
		return put(new JsonNumber(value));
	}

	/**
	 * Appends {@code value} to the end of this array.
	 *
	 * @param value
	 *            a {@link JsonObject}, {@link JsonArray}, String, Boolean,
	 *            Integer, Long, Double, or {@code null}. May not be
	 *            {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
	 *            infinities}. Unsupported values are not permitted and will
	 *            cause the array to be in an inconsistent state.
	 * @return this array.
	 */
	public JsonArray put(final Object value) throws JsonException {
		return put(wrap(value));
	}

	void putInternal(final JsonElement value) {
		if (value != null) {
			values.add(value);
		}
	}

	/**
	 * Removes and returns the value at {@code index}, or null if the array has
	 * no value at {@code index}.
	 */
	public JsonElement remove(final int index) {
		checkIfFrozen();
		if (index < 0 || index >= values.size()) {
			return null;
		}
		return values.remove(index);
	}

	public boolean remove(final Object o) {
		checkIfFrozen();
		return values.remove(o);
	}

	public boolean removeAll(final Collection<?> objects) {
		checkIfFrozen();
		return values.removeAll(objects);
	}

	public boolean retainAll(final Collection<?> objects) {
		checkIfFrozen();
		return values.retainAll(objects);
	}

	public JsonElement set(final int i, final JsonElement jsonElement) {
		checkIfFrozen();
		return values.set(i, jsonElement);
	}

	public int size() {
		return values.size();
	}

	public List<JsonElement> subList(final int i, final int i2) {
		return values.subList(i, i2);
	}

	public Object[] toArray() {
		return values.toArray();
	}

	public <T> T[] toArray(final T[] ts) {
		return values.toArray(ts);
	}

	public ArrayList<String> toArrayList() {
		ArrayList<String> res = new ArrayList<String>();
		for (JsonElement el : values) {
			res.add(el.toString());
		}
		return res;
	}

	/**
	 * Returns a new object whose values are the values in this array, and whose
	 * names are the values in {@code names}. Names and values are paired up by
	 * index from 0 through to the shorter array's length. Names that are not
	 * strings will be coerced to strings. This method returns null if either
	 * array is empty.
	 */
	public JsonObject toJsonObject(final JsonArray names) throws JsonException {
		JsonObject result = new JsonObject();
		int length = Math.min(names.length(), values.size());
		if (length == 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			String name = names.opt(i).toString();
			if (opt(i) != null) {
				result.put(name, opt(i));
			}
		}
		return result;
	}

	@Override
	public void write(final JsonWriter writer) throws IOException {
		writer.beginArray();
		for (JsonElement e : this) {
			e.write(writer);
		}
		writer.endArray();
	}
}
