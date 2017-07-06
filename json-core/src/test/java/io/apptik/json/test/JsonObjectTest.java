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

package io.apptik.json.test;

import static io.apptik.json.JsonNull.JSON_NULL;
import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonNumber;
import io.apptik.json.JsonObject;
import io.apptik.json.JsonString;
import io.apptik.json.exception.JsonException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This black box test was written without inspecting the non-free org.json
 * sourcecode.
 */
@RunWith(JUnit4.class)
public class JsonObjectTest extends TestCase {

	@Test
	public void test_toString_listAsMapValue() throws Exception {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add("a");
		list.add(new ArrayList<String>());
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("x", "l");
		map.put("y", list);
		assertEquals("{\"x\":\"l\",\"y\":[\"a\",[]]}",
				new JsonObject(map).toString());
	}

	@Test
	public void test_wrap() throws Exception {
		assertEquals(JSON_NULL, JsonElement.wrap(null));

		JsonArray a = new JsonArray();
		assertEquals(a, JsonElement.wrap(a));

		JsonObject o = new JsonObject();
		assertEquals(o, JsonElement.wrap(o));

		assertEquals(JSON_NULL, JsonElement.wrap(JSON_NULL));

		assertTrue(JsonElement.wrap(new byte[0]) instanceof JsonArray);
		assertTrue(JsonElement.wrap(new ArrayList<String>()) instanceof JsonArray);
		assertTrue(JsonElement.wrap(new HashMap<String, String>()) instanceof JsonObject);
		assertTrue(JsonElement.wrap(Double.valueOf(0)) instanceof JsonNumber);
		assertTrue(JsonElement.wrap("hello") instanceof JsonString);
		assertTrue(JsonElement.wrap(new ConnectException()) instanceof JsonString);
	}

	@Test
	public void testAccumulateExistingArray() throws JsonException {
		JsonArray array = new JsonArray();
		JsonObject object = new JsonObject();
		object.put("foo", array);
		object.accumulate("foo", 5);
		assertEquals("[5]", array.toString());
	}

	@Test
	public void testAccumulateMutatesInPlace() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		object.accumulate("foo", 6);
		JsonArray array = object.getJsonArray("foo");
		assertEquals("[5,6]", array.toString());
		object.accumulate("foo", 7);
		assertEquals("[5,6,7]", array.toString());
	}

	@Test
	public void testAccumulateNull() {
		JsonObject object = new JsonObject();
		try {
			object.accumulate(null, 5);
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testAccumulatePutArray() throws JsonException {
		JsonObject object = new JsonObject();
		object.accumulate("foo", 5);
		assertEquals("{\"foo\":5}", object.toString());
		object.accumulate("foo", new JsonArray());
		assertEquals("{\"foo\":[5,[]]}", object.toString());
	}

	@Test
	public void testAccumulateValueChecking() throws JsonException {
		JsonObject object = new JsonObject();
		try {
			object.accumulate("foo", Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		object.accumulate("foo", 1);
		try {
			object.accumulate("foo", Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		object.accumulate("foo", 2);
		try {
			object.accumulate("foo", Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testAppendExistingArray() throws JsonException {
		JsonArray array = new JsonArray();
		JsonObject object = new JsonObject();
		object.put("foo", array);
		object.append("foo", 5);
		assertEquals("[5]", array.toString());
	}

	@Test
	public void testAppendExistingInvalidKey() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		try {
			object.append("foo", 6);
			fail();
		} catch (JsonException expected) {
		}
	}

	@Test
	public void testAppendNull() {
		JsonObject object = new JsonObject();
		try {
			object.append(null, 5);
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testAppendPutArray() throws JsonException {
		JsonObject object = new JsonObject();
		object.append("foo", 5);
		assertEquals("{\"foo\":[5]}", object.toString());
		object.append("foo", new JsonArray());
		assertEquals("{\"foo\":[5,[]]}", object.toString());
	}

	@Test
	public void testArrayCoercion() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "[Boolean.TRUE]");
		try {
			object.getJsonArray("foo");
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testBooleans() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", Boolean.TRUE);
		object.put("bar", Boolean.FALSE);
		object.put("baz", "true");
		object.put("quux", "false");
		assertEquals(4, object.length());
		assertEquals(Boolean.TRUE, object.getBoolean("foo"));
		assertEquals(Boolean.FALSE, object.getBoolean("bar"));
		assertEquals(Boolean.TRUE, object.getBoolean("baz", Boolean.FALSE));
		assertEquals(Boolean.FALSE, object.getBoolean("quux", Boolean.FALSE));
		assertFalse(object.isNull("foo"));
		assertFalse(object.isNull("quux"));
		assertTrue(object.has("foo"));
		assertTrue(object.has("quux"));
		assertFalse(object.has("missing"));
		assertEquals(Boolean.TRUE, object.optBoolean("foo"));
		assertEquals(Boolean.FALSE, object.optBoolean("bar"));
		assertEquals(Boolean.TRUE,
				object.optBoolean("baz", Boolean.FALSE, Boolean.FALSE));
		assertFalse(object.optBoolean("quux"));
		assertFalse(object.optBoolean("missing"));
		assertEquals(Boolean.TRUE, object.optBoolean("foo", Boolean.TRUE));
		assertEquals(Boolean.FALSE, object.optBoolean("bar", Boolean.TRUE));
		assertEquals(Boolean.TRUE, object.optBoolean("baz", Boolean.TRUE));
		assertEquals(Boolean.FALSE,
				object.optBoolean("quux", Boolean.TRUE, Boolean.FALSE));
		assertEquals(Boolean.TRUE, object.optBoolean("missing", Boolean.TRUE));

		object.put("foo", "truE");
		object.put("bar", "FALSE");
		assertEquals(Boolean.TRUE, object.getBoolean("foo", Boolean.FALSE));
		assertEquals(Boolean.FALSE, object.getBoolean("bar", Boolean.FALSE));
		assertEquals(Boolean.TRUE,
				object.optBoolean("foo", Boolean.FALSE, Boolean.FALSE));
		assertEquals(Boolean.FALSE,
				object.optBoolean("bar", Boolean.TRUE, Boolean.FALSE));
	}

	@Test
	public void testCoerceStringToBoolean() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "maybe");
		try {
			object.getBoolean("foo");
			fail();
		} catch (JsonException expected) {
		}
		assertFalse(object.optBoolean("foo"));
		assertEquals(Boolean.TRUE, object.optBoolean("foo", Boolean.TRUE));
	}

	/**
	 * JsonObject constructor fails
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithUnsupportedNumbers() throws JsonException {
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("foo", Double.NaN);
		contents.put("bar", Double.NEGATIVE_INFINITY);
		contents.put("baz", Double.POSITIVE_INFINITY);

		JsonObject object = new JsonObject(contents);
	}

	@Test
	public void testEmptyObject() throws JsonException {
		JsonObject object = new JsonObject();
		assertEquals(0, object.length());

		// bogus (but documented) behaviour: returns null rather than the empty
		// object!
		assertNull(object.names());

		// returns null rather than an empty array!
		assertNull(object.toJsonArray(new JsonArray()));
		assertEquals("{}", object.toString());
		try {
			object.get("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getBoolean("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getDouble("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getInt("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getJsonArray("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getJsonObject("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getLong("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getString("foo");
			fail();
		} catch (JsonException e) {
		}
		assertFalse(object.has("foo"));
		assertNull(object.opt("foo"));
		assertFalse(object.optBoolean("foo"));
		assertEquals(Boolean.TRUE, object.optBoolean("foo", Boolean.TRUE));
		assertNull(object.optDouble("foo"));
		assertEquals(5.0, object.optDouble("foo", 5.0));
		assertEquals(0, (int) object.optInt("foo"));
		assertEquals((Integer) 5, object.optInt("foo", 5));
		assertEquals(null, object.optJsonArray("foo"));
		assertEquals(null, object.optJsonObject("foo"));
		assertEquals((long) 0, (long) object.optLong("foo"));
		assertEquals(Long.valueOf(Long.MAX_VALUE - 1),
				object.optLong("foo", Long.MAX_VALUE - 1));
		assertEquals("", object.optString("foo")); // empty string is default!
		assertEquals("bar", object.optString("foo", "bar"));
		assertNull(object.remove("foo"));
	}

	@Test
	public void testEmptyStringKey() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("", 5);
		assertEquals(object.get(""), 5);
		assertEquals("{\"\":5}", object.toString());
	}

	@Test
	public void testEqualsAndHashCode() throws JsonException {
		JsonObject a = new JsonObject();
		JsonObject b = new JsonObject();

		// Json object override equals
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), System.identityHashCode(a));
	}

	@Test
	public void testFloats() throws JsonException {
		JsonObject object = new JsonObject();
		try {
			object.put("foo", (Float) Float.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", (Float) Float.NEGATIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", (Float) Float.POSITIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testForeignObjects() throws JsonException {
		Object foreign = new Object() {
			@Override
			public String toString() {
				return "x";
			}
		};

		// foreign object types are accepted and treated as Strings!
		JsonObject object = new JsonObject();
		object.put("foo", foreign);
		assertEquals("{\"foo\":\"x\"}", object.toString());
	}

	@Test
	public void testGet() throws JsonException {
		JsonObject object = new JsonObject();
		Object value = new Object();
		object.put("foo", value);
		object.put("bar", new Object());
		object.put("baz", new Object());
		assertEquals(object.get("foo"), value.toString());
		try {
			object.get("FOO");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.put(null, value);
			fail();
		} catch (JsonException e) {
		}
		try {
			object.get(null);
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testHas() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		assertTrue(object.has("foo"));
		assertFalse(object.has("bar"));
		assertFalse(object.has(null));
	}

	@Test
	public void testJsonObjects() throws JsonException {
		JsonObject object = new JsonObject();

		JsonArray a = new JsonArray();
		JsonObject b = new JsonObject();
		object.put("foo", a);
		object.put("bar", b);

		assertSame(a, object.getJsonArray("foo"));
		assertSame(b, object.getJsonObject("bar"));
		try {
			object.getJsonObject("foo");
			fail();
		} catch (JsonException e) {
		}
		try {
			object.getJsonArray("bar");
			fail();
		} catch (JsonException e) {
		}
		assertEquals(a, object.optJsonArray("foo"));
		assertEquals(b, object.optJsonObject("bar"));
		assertEquals(null, object.optJsonArray("bar"));
		assertEquals(null, object.optJsonObject("foo"));
	}

	@Test
	public void testKeys() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		object.put("bar", 6);
		object.put("foo", 7);

		@SuppressWarnings("unchecked")
		Iterator<String> keys = object.keys();
		Set<String> result = new HashSet<String>();
		assertTrue(keys.hasNext());
		result.add(keys.next());
		assertTrue(keys.hasNext());
		result.add(keys.next());
		assertFalse(keys.hasNext());
		assertEquals(new HashSet<String>(Arrays.asList("foo", "bar")), result);

		try {
			keys.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testKeysEmptyObject() {
		JsonObject object = new JsonObject();
		assertFalse(object.keys().hasNext());
		try {
			object.keys().next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testMapConstructorCopiesContents() throws JsonException {
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("foo", 5);
		JsonObject object = new JsonObject(contents);
		contents.put("foo", 10);
		assertEquals(object.get("foo"), 5);
	}

	@Test
	public void testMapConstructorWithBogusEntries() {
		Map<Object, Object> contents = new HashMap<Object, Object>();
		contents.put(5, 5);

		try {
			new JsonObject(contents);
			fail("JsonObject constructor doesn't validate its input!");
		} catch (Exception e) {
		}
	}

	@Test
	public void testMutatingKeysMutatesObject() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		Iterator keys = object.keys();
		keys.next();
		keys.remove();
		assertEquals(0, object.length());
	}

	@Test
	public void testNames() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		object.put("bar", 6);
		object.put("baz", 7);
		JsonArray array = object.names();
		assertTrue(array.toString().contains("foo"));
		assertTrue(array.toString().contains("bar"));
		assertTrue(array.toString().contains("baz"));
	}

	@Test
	public void testNullCoercionToString() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", JSON_NULL);
		assertEquals("null", object.getString("foo", Boolean.FALSE));
	}

	@Test
	public void testNullKeys() {
		try {
			new JsonObject().put(null, Boolean.FALSE);
			fail();
		} catch (JsonException e) {
		}
		try {
			new JsonObject().put(null, 0.0d);
			fail();
		} catch (JsonException e) {
		}
		try {
			new JsonObject().put(null, 5);
			fail();
		} catch (JsonException e) {
		}
		try {
			new JsonObject().put(null, 5L);
			fail();
		} catch (JsonException e) {
		}
		try {
			new JsonObject().put(null, "foo");
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testNullValue() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", JSON_NULL);
		object.put("bar", (Collection) null);

		assertTrue(object.has("foo"));
		assertTrue(object.has("bar"));
		assertTrue(object.isNull("foo"));
		assertTrue(object.isNull("bar"));
	}

	@Test
	public void testNumbers() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", Double.MIN_VALUE);
		object.put("bar", 9223372036854775806L);
		object.put("baz", Double.MAX_VALUE);
		object.put("quux", -0d);
		assertEquals(4, object.length());

		String toString = object.toString();
		assertTrue(toString, toString.contains("\"foo\":4.9E-324"));
		assertTrue(toString, toString.contains("\"bar\":9223372036854775806"));
		assertTrue(toString,
				toString.contains("\"baz\":1.7976931348623157E308"));

		assertTrue(toString, toString.contains("\"quux\":-0.0}") // no trailing
																	// decimal
																	// point
				|| toString.contains("\"quux\":-0.0,"));

		assertEquals(object.get("foo"), Double.MIN_VALUE);
		assertEquals(object.get("bar"), 9223372036854775806L);
		assertEquals(object.get("baz"), Double.MAX_VALUE);
		assertEquals(object.get("quux"), -0d);
		assertEquals(Double.MIN_VALUE, object.getDouble("foo"));
		assertEquals(9.223372036854776E18, object.getDouble("bar"));
		assertEquals(Double.MAX_VALUE, object.getDouble("baz"));
		assertEquals(-0d, object.getDouble("quux"));
		assertEquals((Long) 0l, object.getLong("foo"));
		assertEquals((Long) 9223372036854775806L, object.getLong("bar"));
		assertEquals((Long) Long.MAX_VALUE, object.getLong("baz"));
		assertEquals((Long) 0l, object.getLong("quux"));
		assertEquals((Integer) 0, object.getInt("foo"));
		assertEquals((Integer) (-2), object.getInt("bar"));
		assertEquals((Integer) Integer.MAX_VALUE, object.getInt("baz"));
		assertEquals((Integer) 0, object.getInt("quux"));
		assertEquals(object.opt("foo"), Double.MIN_VALUE);
		assertEquals((Long) 9223372036854775806L, object.optLong("bar"));
		assertEquals(Double.MAX_VALUE, object.optDouble("baz"));
		assertEquals((Integer) 0, object.optInt("quux"));
		assertEquals(object.opt("foo"), Double.MIN_VALUE);
		assertEquals((Long) 9223372036854775806L, object.optLong("bar"));
		assertEquals(Double.MAX_VALUE, object.optDouble("baz"));
		assertEquals((Integer) 0, object.optInt("quux"));
		assertEquals(Double.MIN_VALUE, object.optDouble("foo", 5.0d));
		assertEquals((Long) 9223372036854775806L, object.optLong("bar", 1L));
		assertEquals((Long) Long.MAX_VALUE, object.optLong("baz", 1L));
		assertEquals((Integer) 0, object.optInt("quux", -1));
		assertEquals("4.9E-324", object.getString("foo", Boolean.FALSE));
		assertEquals("9223372036854775806",
				object.getString("bar", Boolean.FALSE));
		assertEquals("1.7976931348623157E308",
				object.getString("baz", Boolean.FALSE));
		assertEquals("-0.0", object.getString("quux", Boolean.FALSE));
	}

	@Test
	public void testObjectCoercion() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "{}");
		try {
			object.getJsonObject("foo");
			fail();
		} catch (JsonException e) {
		}
	}

	@Test
	public void testOptNull() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "bar");
		assertEquals(null, object.opt(null));
		assertFalse(object.optBoolean(null));
		assertNull(object.optDouble(null));
		assertEquals(0, (int) object.optInt(null));
		assertEquals((long) 0, (long) object.optLong(null));
		assertEquals(null, object.optJsonArray(null));
		assertEquals(null, object.optJsonObject(null));
		assertEquals("", object.optString(null));
		assertEquals(Boolean.TRUE, object.optBoolean(null, Boolean.TRUE));
		assertEquals(0.0d, object.optDouble(null, 0.0d));
		assertEquals((Integer) 1, object.optInt(null, 1));
		assertEquals((Long) 1L, object.optLong(null, 1L));
		assertEquals("baz", object.optString(null, "baz"));
	}

	@Test
	public void testOtherNumbers() throws JsonException {
		Number nan = new Number() {
			@Override
			public double doubleValue() {
				return Double.NaN;
			}

			@Override
			public float floatValue() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int intValue() {
				throw new UnsupportedOperationException();
			}

			@Override
			public long longValue() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String toString() {
				return "x";
			}
		};

		JsonObject object = new JsonObject();
		try {
			object.put("foo", nan);
			fail("Object.put() accepted a NaN (via a custom Number class)");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPut() throws JsonException {
		JsonObject object = new JsonObject();
		assertSame(object, object.put("foo", Boolean.TRUE));
		object.put("foo", Boolean.FALSE);
		assertEquals(Boolean.FALSE, object.getBoolean("foo"));

		object.put("foo", 5.0d);
		assertEquals(5.0d, object.getDouble("foo"));
		object.put("foo", 0);
		assertEquals((Integer) 0, object.getInt("foo"));
		object.put("bar", Long.MAX_VALUE - 1);
		assertEquals((Long) (Long.MAX_VALUE - 1), object.getLong("bar"));
		object.put("baz", "x");
		assertEquals("x", object.get("baz").toString());
		object.put("bar", null);
		assertEquals(object.get("bar"), null);
	}

	@Test
	public void testPutNullRemoves() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "bar");
		object.put("foo", (Collection) null);
		assertEquals(1, object.length());
		assertTrue(object.has("foo"));
		assertEquals(object.get("foo"), null);
	}

	@Test
	public void testPutOpt() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "bar");
		object.putOpt("foo", null);
		assertEquals(object.get("foo"), "bar");
		object.putOpt(null, null);
		assertEquals(1, object.length());
		object.putOpt(null, "bar");
		assertEquals(1, object.length());
	}

	@Test
	public void testPutOptUnsupportedNumbers() throws JsonException {
		JsonObject object = new JsonObject();
		try {
			object.putOpt("foo", Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.putOpt("foo", Double.NEGATIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.putOpt("foo", Double.POSITIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPutUnsupportedNumbers() throws JsonException {
		JsonObject object = new JsonObject();
		try {
			object.put("foo", Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", Double.NEGATIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", Double.POSITIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPutUnsupportedNumbersAsObjects() throws JsonException {
		JsonObject object = new JsonObject();
		try {
			object.put("foo", (Double) Double.NaN);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", (Double) Double.NEGATIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			object.put("foo", (Double) Double.POSITIVE_INFINITY);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testQuote() {
		// covered by JsonStringerTest.testEscaping
	}

	@Test
	public void testRemove() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "bar");
		assertEquals(null, object.remove(null));
		assertEquals(null, object.remove(""));
		assertEquals(null, object.remove("bar"));
		assertEquals(object.remove("foo"), "bar");
		assertEquals(null, object.remove("foo"));
	}

	@Test
	public void testStrings() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", "true");
		object.put("bar", "5.5");
		object.put("baz", "9223372036854775806");
		object.put("quux", "null");
		object.put("height", "5\"8' tall");

		assertTrue(object.toString().contains("\"foo\":\"true\""));
		assertTrue(object.toString().contains("\"bar\":\"5.5\""));
		assertTrue(object.toString()
				.contains("\"baz\":\"9223372036854775806\""));
		assertTrue(object.toString().contains("\"quux\":\"null\""));
		assertTrue(object.toString().contains("\"height\":\"5\\\"8' tall\""));

		assertEquals("true", object.get("foo").toString());
		assertEquals("null", object.getString("quux"));
		assertEquals("5\"8' tall", object.getString("height"));
		assertEquals("true", object.opt("foo").toString());
		assertEquals("5.5", object.optString("bar"));
		assertEquals("true", object.optString("foo", "x"));
		assertFalse(object.isNull("foo"));

		assertEquals(Boolean.TRUE,
				object.optBoolean("foo", Boolean.TRUE, Boolean.FALSE));
		assertEquals(0, (int) object.optInt("foo"));
		assertEquals((Integer) (-2), object.optInt("foo", -2));

		assertEquals(5.5d, object.getDouble("bar", Boolean.FALSE));
		assertEquals((Long) 5L, object.getLong("bar", Boolean.FALSE));
		assertEquals((Integer) 5, object.getInt("bar", Boolean.FALSE));
		assertEquals((Integer) 5, object.optInt("bar", 3, Boolean.FALSE));

		// The last digit of the string is a 6 but getLong returns a 7. It's
		// probably parsing as a
		// double and then converting that to a long. This is consistent with
		// JavaScript.
		assertEquals((Long) 9223372036854775807L,
				object.getLong("baz", Boolean.FALSE));
		assertEquals(9.223372036854776E18,
				object.getDouble("baz", Boolean.FALSE));
		assertEquals((Integer) Integer.MAX_VALUE,
				object.getInt("baz", Boolean.FALSE));

		assertFalse(object.isNull("quux"));
		try {
			object.getDouble("quux");
			fail();
		} catch (JsonException e) {
		}
		assertNull(object.optDouble("quux"));
		assertEquals(-1.0d, object.optDouble("quux", -1.0d));

		object.put("foo", "TRUE");
		assertEquals(Boolean.TRUE, object.getBoolean("foo", Boolean.FALSE));
	}

	@Test
	public void testToJsonArray() throws JsonException {
		JsonObject object = new JsonObject();
		Object value = new Object();
		object.put("foo", Boolean.TRUE);
		object.put("bar", 5.0d);
		object.put("baz", -0.0d);
		object.put("quux", value);

		JsonArray names = new JsonArray();
		names.put("baz");
		names.put("quux");
		names.put("foo");

		JsonArray array = object.toJsonArray(names);
		assertEquals(array.get(0), -0.0d);
		assertEquals(array.get(1), value.toString());
		assertEquals(array.get(2), Boolean.TRUE);

		object.put("foo", Boolean.FALSE);
		assertEquals(array.get(2), Boolean.TRUE);
	}

	@Test
	public void testToJsonArrayEndsUpEmpty() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		JsonArray array = new JsonArray();
		array.put("bar");
		assertEquals(0, object.toJsonArray(array).length());
	}

	@Test
	public void testToJsonArrayMissingNames() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", Boolean.TRUE);
		object.put("bar", 5.0d);
		object.put("baz", JSON_NULL);

		JsonArray names = new JsonArray();
		names.put("bar");
		names.put("foo");
		names.put("quux");
		names.put("baz");

		JsonArray array = object.toJsonArray(names);
		assertEquals(3, array.length());

		assertEquals(array.get(0), 5.0d);
		assertEquals(array.get(1), Boolean.TRUE);
		assertEquals(array.get(2), null);
	}

	@Test
	public void testToJsonArrayNonString() throws JsonException {
		JsonObject object = new JsonObject();
		object.put("foo", 5);
		object.put("null", 10);
		object.put("false", 15);

		JsonArray names = new JsonArray();
		names.put(JSON_NULL);
		names.put(Boolean.FALSE);
		names.put("foo");

		// array elements are converted to strings to do name lookups on the
		// map!
		JsonArray array = object.toJsonArray(names);
		assertEquals(3, array.length());
		assertEquals(array.get(0), 10);
		assertEquals(array.get(1), 15);
		assertEquals(array.get(2), 5);
	}

	@Test
	public void testToJsonArrayNull() throws JsonException {
		JsonObject object = new JsonObject();
		assertEquals(null, object.toJsonArray(null));
		object.put("foo", 5);
		try {
			object.toJsonArray(null);
		} catch (JsonException e) {
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToStringWithUnsupportedNumbers() throws JsonException {
		// when the object contains an unsupported number JsonObject constructor
		// fails
		JsonObject object = new JsonObject(Collections.singletonMap("foo",
				Double.NaN));
	}
}
