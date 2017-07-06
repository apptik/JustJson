/*
 *  Copyright (C) 2014 Kalin Maldzhanski
 *  Copyright (C) 2010 The Android Open Source Project
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
import io.apptik.json.util.LinkedTreeMap;
import io.apptik.json.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Note: this class was written without inspecting the non-free org.json sourcecode.

/**
 * A modifiable set of name/value mappings. Names are unique, non-null strings.
 * Values may be any mix of {@link JsonObject JsonObjects}, {@link JsonArray
 * JsonArrays}, Strings, Booleans, Integers, Longs, Doubles or {@link JsonNull}.
 * Values may not be {@code null}, {@link Double#isNaN() NaNs},
 * {@link Double#isInfinite() infinities}, or of any type not listed here.
 * <p>
 * <p>
 * This class can coerce values to another type when requested.
 * <ul>
 * <li>When the requested type is a boolean, strings will be coerced using a
 * case-insensitive comparison to "true" and "false".
 * <li>When the requested type is a double, other {@link Number} types will be
 * coerced using {@link Number#doubleValue() doubleValue}. Strings that can be
 * coerced using {@link Double#valueOf(String)} will be.
 * <li>When the requested type is an int, other {@link Number} types will be
 * coerced using {@link Number#intValue() intValue}. Strings that can be coerced
 * using {@link Double#valueOf(String)} will be, and then cast to int.
 * <li><a name="lossy">When the requested type is a long, other {@link Number}
 * types will be coerced using {@link Number#longValue() longValue}. Strings
 * that can be coerced using {@link Double#valueOf(String)} will be, and then
 * cast to long. This two-step conversion is lossy for very large values. For
 * example, the string "9223372036854775806" yields the long
 * 9223372036854775807.</a>
 * <li>When the requested type is a String, other non-null values will be
 * coerced using {@link String#valueOf(Object)}. Although null cannot be
 * coerced, the sentinel value {@link JsonNull} is coerced to the string "null".
 * </ul>
 * <p>
 * <p>
 * This class can look up both mandatory and optional values:
 * <ul>
 * <li>Use <code>get<i>Type</i>()</code> to retrieve a mandatory value. This
 * fails with a {@code JsonException} if the requested name has no value or if
 * the value cannot be coerced to the requested type.
 * <li>Use <code>opt<i>Type</i>()</code> to retrieve an optional value. This
 * returns a system- or user-supplied default if the requested name has no value
 * or if the value cannot be coerced to the requested type.
 * </ul>
 * <p>
 * <p>
 * <strong>Warning:</strong> this class represents null in two incompatible
 * ways: the standard Java {@code null} reference, and the sentinel value
 * {@link JsonNull}. In particular, calling {@code put(name, null)} removes the
 * named entry from the object but {@code put(name, JsonObject.NULL)} stores an
 * entry whose value is {@code JsonObject.NULL}.
 * <p>
 * <p>
 * Instances of this class are not thread safe. This class is not designed for
 * inheritance and should not be subclassed. In particular, self-use by
 * overrideable methods is not specified. See <i>Effective Java</i> Item 17,
 * "Design and Document or inheritance or else prohibit it" for further
 * information.
 */
public final class JsonObject extends JsonElement implements
		Iterable<Map.Entry<String, JsonElement>>, Freezable<JsonObject> {

	private volatile boolean frozen = false;
	private final LinkedTreeMap<String, JsonElement> nameValuePairs = new LinkedTreeMap<String, JsonElement>();

	/**
	 * Creates a {@code JsonObject} with no name/value mappings.
	 */
	public JsonObject() {

	}

	/**
	 * Creates a new {@code JSONObject} by copying all name/value mappings from
	 * the given map.
	 *
	 * @param copyFrom
	 *            a map whose keys are of type {@link String} and whose values
	 *            are of supported types.
	 * @throws NullPointerException
	 *             if any of the map's keys are null.
	 */
	/* (accept a raw type for API compatibility) */
	public JsonObject(final Map copyFrom) throws JsonException {
		this();
		Map<?, ?> contentsTyped = copyFrom;
		for (Map.Entry<?, ?> entry : contentsTyped.entrySet()) {
			/*
			 * Deviate from the original by checking that keys are non-null and
			 * of the proper type. (We still defer validating the values).
			 */
			String key = (String) entry.getKey();
			if (key == null) {
				throw new NullPointerException("key == null");
			}
			nameValuePairs.put(key, wrap(entry.getValue()));
		}
	}

	/**
	 * Appends {@code value} to the array already mapped to {@code name}. If
	 * this object has no mapping for {@code name}, this inserts a new mapping.
	 * If the mapping exists but its value is not an array, the existing and new
	 * values are inserted in order into a new array which is itself mapped to
	 * {@code name}. In aggregate, this allows values to be added to a mapping
	 * one at a time.
	 * <p>
	 * <p>
	 * Note that {@code append(String, Object)} provides better semantics. In
	 * particular, the mapping for {@code name} will <b>always</b> be a
	 * {@link JsonArray}. Using {@code accumulate} will result in either a
	 * {@link JsonArray} or a mapping whose type is the type of {@code value}
	 * depending on the number of calls to it.
	 *
	 * @param value
	 *            a {@link JsonObject}, {@link JsonArray}, String, Boolean,
	 *            Integer, Long, Double or null. May not be
	 *            {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
	 *            infinities}.
	 */
	// TODO: Change {@code append) to {@link #append} when append is
	// unhidden.
	public JsonObject accumulate(final String name, final Object value)
			throws JsonException {
		checkIfFrozen();
		Object current = nameValuePairs.get(checkName(name));
		if (current == null) {
			return put(name, value);
		}

		if (current instanceof JsonArray) {
			JsonArray array = (JsonArray) current;
			array.put(value);
		} else {
			JsonArray array = new JsonArray();
			array.put(current);
			array.put(value);
			nameValuePairs.put(name, array);
		}
		return this;
	}

	/**
	 * Appends values to the array mapped to {@code name}. A new
	 * {@link JsonArray} mapping for {@code name} will be inserted if no mapping
	 * exists. If the existing mapping for {@code name} is not a
	 * {@link JsonArray}, a {@link JsonException} will be thrown.
	 *
	 * @throws JsonException
	 *             if {@code name} is {@code null} or if the mapping for
	 *             {@code name} is non-null and is not a {@link JsonArray}.
	 * @hide
	 */
	public JsonObject append(final String name, final Object value)
			throws JsonException {
		checkIfFrozen();
		Object current = nameValuePairs.get(checkName(name));

		final JsonArray array;
		if (current instanceof JsonArray) {
			array = (JsonArray) current;
		} else if (current == null) {
			JsonArray newArray = new JsonArray();
			nameValuePairs.put(name, newArray);
			array = newArray;
		} else {
			throw new JsonException("Key " + name + " is not a JsonArray");
		}

		array.put(value);

		return this;
	}

	@Override
	public JsonObject asJsonObject() {
		return this;
	}

	public void checkIfFrozen() {
		if (isFrozen()) {
			throw new IllegalStateException(
					"Attempt to modify a frozen JsonObject instance.");
		}
	}

	String checkName(final String name) {
		if (name == null) {
			throw new JsonException("Names must be non-null");
		}
		return name;
	}

	public JsonObject clear() {
		checkIfFrozen();
		nameValuePairs.clear();
		return this;
	}

	public JsonObject cloneAsThawed() {
		try {
			return JsonElement.readFrom(this.toString()).asJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
			throw new JsonException("Cannot Recreate Json Object", e);
		}
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof JsonObject
				&& ((JsonObject) o).nameValuePairs.equals(nameValuePairs);
	}

	public JsonObject freeze() {
		frozen = true;
		for (JsonElement el : nameValuePairs.values()) {
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
	 * Returns the value mapped by {@code name}, or throws if no such mapping
	 * exists.
	 *
	 * @throws JsonException
	 *             if no such mapping exists.
	 */
	public JsonElement get(final String name) throws JsonException {
		JsonElement result = nameValuePairs.get(name);
		if (result == null) {
			throw new JsonException("No value for " + name + ", in: "
					+ this.toString());
		}
		return result;
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * can be coerced to a boolean, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or cannot be coerced to a
	 *             boolean.
	 */
	public Boolean getBoolean(final String name) throws JsonException {
		return getBoolean(name, true);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * can be coerced to a boolean, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or cannot be coerced to a
	 *             boolean.
	 */
	public Boolean getBoolean(final String name, final boolean strict)
			throws JsonException {
		JsonElement el = get(name);
		Boolean res = null;
		if (strict && !el.isBoolean()) {
			throw Util.typeMismatch(name, el, "boolean", true);
		}
		if (el.isBoolean()) {
			res = el.asBoolean();
		}
		if (el.isString()) {
			res = Util.toBoolean(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(name, el, "boolean", strict);
		}
		return res;
	}

	public Double getDouble(final String name) throws JsonException {
		return getDouble(name, true);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or cannot be coerced to a
	 *             double.
	 */
	public Double getDouble(final String name, final boolean strict)
			throws JsonException {
		JsonElement el = get(name);
		Double res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(name, el, "double", true);
		}
		if (el.isNumber()) {
			res = el.asDouble();
		}
		if (el.isString()) {
			res = Util.toDouble(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(name, el, "double", strict);
		}
		return res;
	}

	public Integer getInt(final String name) throws JsonException {
		return getInt(name, true);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or cannot be coerced to an int.
	 */
	public Integer getInt(final String name, final boolean strict)
			throws JsonException {
		JsonElement el = get(name);
		Integer res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(name, el, "int", true);
		}
		if (el.isNumber()) {
			res = el.asInt();
		}
		if (el.isString()) {
			res = Util.toInteger(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(name, el, "int", strict);
		}
		return res;
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a
	 * {@code JsonArray}, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or is not a {@code JsonArray}.
	 */
	public JsonArray getJsonArray(final String name) throws JsonException {
		JsonElement el = get(name);
		if (!el.isJsonArray()) {
			throw Util.typeMismatch(name, el, "JsonArray");
		}
		return el.asJsonArray();
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a
	 * {@code JsonObject}, or throws otherwise.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or is not a {@code JsonObject}.
	 */
	public JsonObject getJsonObject(final String name) throws JsonException {
		JsonElement el = get(name);
		if (!el.isJsonObject()) {
			throw Util.typeMismatch(name, el, "JsonObject");
		}
		return el.asJsonObject();
	}

	@Override
	public String getJsonType() {
		return TYPE_OBJECT;
	}

	public Long getLong(final String name) throws JsonException {
		return getLong(name, true);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or throws otherwise. Note that Util represents
	 * numbers as doubles, so this is <a href="#lossy">lossy</a>; use strings to
	 * transfer numbers via Util.
	 *
	 * @throws JsonException
	 *             if the mapping doesn't exist or cannot be coerced to a long.
	 */
	public Long getLong(final String name, final boolean strict)
			throws JsonException {
		JsonElement el = get(name);
		Long res = null;
		if (strict && !el.isNumber()) {
			throw Util.typeMismatch(name, el, "long", true);
		}
		if (el.isNumber()) {
			res = el.asLong();
		}
		if (el.isString()) {
			res = Util.toLong(el.asString());
		}
		if (res == null) {
			throw Util.typeMismatch(name, el, "long", strict);
		}
		return res;
	}

	public String getString(final String name) throws JsonException {
		return getString(name, true);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or throws if no such mapping exists.
	 *
	 * @throws JsonException
	 *             if no such mapping exists.
	 */
	public String getString(final String name, final boolean strict)
			throws JsonException {
		JsonElement el = get(name);
		String res = null;
		if (strict && !el.isString()) {
			throw Util.typeMismatch(name, el, "string", true);
		}
		res = el.toString();
		if (res == null) {
			throw Util.typeMismatch(name, el, "string", strict);
		}
		return res;
	}

	/**
	 * Returns true if this object has a mapping for {@code name}. The mapping
	 * may be {@link JsonNull}.
	 */
	public boolean has(final String name) {
		return nameValuePairs.containsKey(name);
	}

	public boolean isFrozen() {
		return frozen;
	}

	@Override
	public boolean isJsonObject() {
		return true;
	}

	/**
	 * Returns true if this object has no mapping for {@code name} or if it has
	 * a mapping whose value is {@link JsonNull}.
	 */
	public boolean isNull(final String name) {
		JsonElement value = nameValuePairs.get(name);
		return value.isNull();
	}

	public Iterator<Map.Entry<String, JsonElement>> iterator() {
		return nameValuePairs.entrySet().iterator();
	}

	/**
	 * Returns an iterator of the {@code String} names in this object. The
	 * returned iterator supports {@link Iterator#remove() remove}, which will
	 * remove the corresponding mapping from this object. If this object is
	 * modified after the iterator is returned, the iterator's behavior is
	 * undefined. The order of the keys is undefined.
	 */
	public Iterator<String> keys() {
		return nameValuePairs.keySet().iterator();
	}

	/**
	 * Returns the set of {@code String} names in this object. The returned set
	 * is a view of the keys in this object. {@link Set#remove(Object)} will
	 * remove the corresponding mapping from this object and set iterator
	 * behaviour is undefined if this object is modified after it is returned.
	 * <p>
	 * See {@link #keys()}.
	 *
	 * @hide.
	 */
	public Set<String> keySet() {
		return nameValuePairs.keySet();
	}

	/**
	 * Returns the number of name/value mappings in this object.
	 */
	public int length() {
		return nameValuePairs.size();
	}

	/**
	 * Merge Json Object with another Json Object. It does not change element of
	 * another with the same name exists. However if the element is Json Object
	 * then it will go down and merge that object.
	 *
	 * @param another
	 * @return
	 */
	public JsonObject merge(final JsonObject another) {
		for (Map.Entry<String, JsonElement> anotherEntry : another) {
			JsonElement curr = this.opt(anotherEntry.getKey());
			if (curr == null) {
				try {
					this.put(anotherEntry.getKey(), anotherEntry.getValue());
				} catch (JsonException e) {
					e.printStackTrace();
				}
			} else if (curr.isJsonObject()
					&& anotherEntry.getValue().isJsonObject()) {
				curr.asJsonObject().merge(
						anotherEntry.getValue().asJsonObject());
			}
		}
		return this;
	}

	/**
	 * Returns an array containing the string names in this object. This method
	 * returns null if this object contains no mappings.
	 */
	public JsonArray names() throws JsonException {
		return nameValuePairs.isEmpty() ? null : new JsonArray(
				new ArrayList<String>(nameValuePairs.keySet()));
	}

	/**
	 * Returns the value mapped by {@code name}, or null if no such mapping
	 * exists.
	 */
	public JsonElement opt(final String name) {
		return nameValuePairs.get(name);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * can be coerced to a boolean, or false otherwise.
	 */
	public Boolean optBoolean(final String name) {
		return optBoolean(name, false);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * {@code fallback} otherwise.
	 */
	public Boolean optBoolean(final String name, final Boolean fallback) {
		return optBoolean(name, fallback, true);
	}

	public Boolean optBoolean(final String name, final Boolean fallback,
			final boolean strict) {
		try {
			return getBoolean(name, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or {@code NaN} otherwise.
	 */
	public Double optDouble(final String name) {
		return optDouble(name, null);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or {@code fallback} otherwise.
	 */
	public Double optDouble(final String name, final Double fallback) {
		return optDouble(name, fallback, true);
	}

	public Double optDouble(final String name, final Double fallback,
			final boolean strict) {
		try {
			return getDouble(name, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or 0 otherwise.
	 */
	public Integer optInt(final String name) {
		return optInt(name, 0);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or {@code fallback} otherwise.
	 */
	public Integer optInt(final String name, final Integer fallback) {
		return optInt(name, fallback, true);
	}

	public Integer optInt(final String name, final Integer fallback,
			final boolean strict) {
		try {
			return getInt(name, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a
	 * {@code JsonArray}, or null otherwise.
	 */
	public JsonArray optJsonArray(final String name) {
		JsonElement el = null;
		try {
			el = get(name);
		} catch (JsonException e) {
			return null;
		}
		if (!el.isJsonArray()) {
			return null;
		}
		return el.asJsonArray();
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a
	 * {@code JsonObject}, or null otherwise.
	 */
	public JsonObject optJsonObject(final String name) {
		JsonElement el = null;
		try {
			el = get(name);
		} catch (JsonException e) {
			return null;
		}
		if (!el.isJsonObject()) {
			return null;
		}
		return el.asJsonObject();
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or 0 otherwise. Note that Util represents
	 * numbers as doubles, so this is <a href="#lossy">lossy</a>; use strings to
	 * transfer numbers via Util.
	 */
	public Long optLong(final String name) {
		return optLong(name, (long) 0);
	}

	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or {@code fallback} otherwise. Note that Util
	 * represents numbers as doubles, so this is <a href="#lossy">lossy</a>; use
	 * strings to transfer numbers via Util.
	 */
	public Long optLong(final String name, final Long fallback) {
		return optLong(name, fallback, true);
	}

	public Long optLong(final String name, final Long fallback,
			final boolean strict) {
		try {
			return getLong(name, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or the empty string if no such mapping exists.
	 */
	public String optString(final String name) {
		return optString(name, "");
	}

	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or {@code fallback} if no such mapping exists.
	 */
	public String optString(final String name, final String fallback) {
		return optString(name, fallback, true);
	}

	public String optString(final String name, final String fallback,
			final boolean strict) {
		try {
			return getString(name, strict);
		} catch (JsonException e) {
			return fallback;
		}
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name.
	 *
	 * @return this object.
	 */
	public JsonObject put(final String name, final boolean value)
			throws JsonException {
		return put(name, new JsonBoolean(value));
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name.
	 *
	 * @param value
	 *            a finite value. May not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this object.
	 */
	public JsonObject put(final String name, final double value)
			throws JsonException {
		return put(name, new JsonNumber(value));
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name.
	 *
	 * @return this object.
	 */
	public JsonObject put(final String name, final int value)
			throws JsonException {
		return put(name, new JsonNumber(value));
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name. If the value is {@code null}, any existing
	 * mapping for {@code name} is removed.
	 *
	 * @param value
	 *            a {@link JsonObject}, {@link JsonArray}, {@link JsonString},
	 *            {@link JsonBoolean}, {@link JsonNumber}, {@link JsonNull}. May
	 *            not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this object.
	 */
	public JsonObject put(final String name, final JsonElement value)
			throws JsonException {
		checkIfFrozen();
		putInternal(name, value);
		return this;
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name.
	 *
	 * @return this object.
	 */
	public JsonObject put(final String name, final long value)
			throws JsonException {
		return put(name, new JsonNumber(value));
	}

	/**
	 * Maps {@code name} to {@code value}, clobbering any existing name/value
	 * mapping with the same name. If the value is {@code null}, any existing
	 * mapping for {@code name} is removed.
	 *
	 * @param value
	 *            a {@link JsonObject}, {@link JsonArray}, String, Boolean,
	 *            Integer, Long, Double, {@code null}. May not be
	 *            {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
	 *            infinities}.
	 * @return this object.
	 */
	public JsonObject put(final String name, final Object value)
			throws JsonException {
		return put(name, wrap(value));
	}

	void putInternal(final String name, JsonElement value) {
		if (value == null) {
			value = JSON_NULL;
		}
		nameValuePairs.put(checkName(name), value);
	}

	/**
	 * Equivalent to {@code put(name, value)} when both parameters are non-null;
	 * does nothing otherwise.
	 */
	public JsonObject putOpt(final String name, final Object value)
			throws JsonException {
		if (name == null || value == null) {
			return this;
		}
		return put(name, value);
	}

	/**
	 * Removes the named mapping if it exists; does nothing otherwise.
	 *
	 * @return the value previously mapped by {@code name}, or null if there was
	 *         no such mapping.
	 */
	public Object remove(final String name) {
		checkIfFrozen();
		return nameValuePairs.remove(name);
	}

	/**
	 * Returns an array with the values corresponding to {@code names}. The
	 * array contains null for names that aren't mapped. This method returns
	 * null if {@code names} is either null or empty.
	 */
	public JsonArray toJsonArray(final JsonArray names) throws JsonException {
		JsonArray result = new JsonArray();
		if (names == null) {
			return null;
		}
		int length = names.length();
		if (length == 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			String name = Util.toString(names.opt(i));
			result.put(opt(name));
		}
		return result;
	}

	public Collection<JsonElement> valuesSet() {
		if (isFrozen()) {
			Collections.unmodifiableCollection(nameValuePairs.values());
		}
		return nameValuePairs.values();
	}

	@Override
	public void write(final JsonWriter writer) throws IOException {
		writer.beginObject();
		StringBuilder indent = new StringBuilder();

		for (Map.Entry<String, JsonElement> e : nameValuePairs.entrySet()) {

			writer.name(indent + e.getKey());
			e.getValue().write(writer);
		}
		writer.endObject();
	}

}
