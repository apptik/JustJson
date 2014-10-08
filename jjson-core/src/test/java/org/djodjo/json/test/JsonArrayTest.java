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

package org.djodjo.json.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.djodjo.json.JsonArray;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.JsonNull;
import org.djodjo.json.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This black box test was written without inspecting the non-free org.json sourcecode.
 */
@RunWith(JUnit4.class)
public class JsonArrayTest extends TestCase {

    @Test
    public void testEmptyArray() throws JsonException {
        JsonArray array = new JsonArray();
        assertEquals(0, array.length());
        try {
            array.get(0);
            fail();
        } catch (JsonException e) {
        }
        try {
            array.getBoolean(0);
            fail();
        } catch (JsonException e) {
        }

        assertEquals("[]", array.toString());

        // out of bounds is co-opted with defaulting
        assertTrue(array.isNull(0));
        assertNull(array.opt(0));
        assertNull(array.optBoolean(0));
        assertTrue(array.optBoolean(0, Boolean.TRUE));

        // bogus (but documented) behaviour: returns null rather than an empty object!
        assertNull(array.toJsonObject(new JsonArray()));
    }

    @Test
    public void testEqualsAndHashCode() throws JsonException {
        JsonArray a = new JsonArray();
        JsonArray b = new JsonArray();
        assertTrue(a.equals(b));
        assertEquals("equals() not consistent with hashCode()", a.hashCode(), b.hashCode());

        a.put(Boolean.TRUE);
        a.put(Boolean.FALSE);
        b.put(Boolean.TRUE);
        b.put(Boolean.FALSE);
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        b.put(Boolean.TRUE);
        assertFalse(a.equals(b));
        assertTrue(a.hashCode() != b.hashCode());
    }

    @Test
    public void testBooleans() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(Boolean.TRUE);
        array.put(Boolean.FALSE);
        array.put(2, Boolean.FALSE);
        array.put(3, Boolean.FALSE);
        array.put(2, Boolean.TRUE);
        assertEquals("[true,false,true,false]", array.toString());
        assertEquals(4, array.length());
        assertTrue(array.getBoolean(0));
        assertFalse(array.getBoolean(1));
        assertTrue(array.getBoolean(2));
        assertFalse(array.getBoolean(3));
        assertFalse(array.isNull(0));
        assertFalse(array.isNull(1));
        assertFalse(array.isNull(2));
        assertFalse(array.isNull(3));
        assertEquals(Boolean.TRUE, array.optBoolean(0));
        assertEquals(Boolean.FALSE, array.optBoolean(1, Boolean.TRUE));
        assertEquals(Boolean.TRUE, array.optBoolean(2, Boolean.FALSE));
        assertEquals(Boolean.FALSE, array.optBoolean(3));
        assertEquals("true", array.getString(0));
        assertEquals("false", array.getString(1));
        assertEquals("true", array.optString(2));
        assertEquals("false", array.optString(3, "x"));

        JsonArray other = new JsonArray();
        other.put(Boolean.TRUE);
        other.put(Boolean.FALSE);
        other.put(Boolean.TRUE);
        other.put(Boolean.FALSE);
        assertTrue(array.equals(other));
        other.put(Boolean.TRUE);
        assertFalse(array.equals(other));

        other = new JsonArray();
        other.put("true");
        other.put("false");
        other.put("truE");
        other.put("FALSE");
        assertFalse(array.equals(other));
        assertFalse(other.equals(array));
        assertEquals(Boolean.TRUE, other.getBoolean(0));
        assertEquals(Boolean.FALSE, other.optBoolean(1, Boolean.TRUE));
        assertEquals(Boolean.TRUE, other.optBoolean(2));
        assertEquals(Boolean.FALSE, other.getBoolean(3));
    }

    // http://code.google.com/p/android/issues/detail?id=16411
    @Test
    public void testCoerceStringToBoolean() throws JsonException {
        JsonArray array = new JsonArray();
        array.put("maybe");
        try {
            array.getBoolean(0);
            fail();
        } catch (JsonException expected) {
        }
        assertNull(array.optBoolean(0));
        assertTrue(array.optBoolean(0, Boolean.TRUE));
    }

    @Test
    public void testNulls() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(3, (Collection) null);
        array.put(0, new JsonNull());
        assertEquals(4, array.length());
        assertEquals("[null,null,null,null]", array.toString());


        assertEquals(array.get(0), null);
        assertEquals(array.get(1), null);
        assertEquals(array.get(2), null);
        assertEquals(array.get(3), null);

        assertEquals(array.opt(0), null);
        assertEquals(array.opt(1), null);
        assertEquals(array.opt(2), null);
        assertEquals(array.opt(3), null);
        assertTrue(array.isNull(0));
        assertTrue(array.isNull(1));
        assertTrue(array.isNull(2));
        assertTrue(array.isNull(3));
        assertEquals("null", array.optString(0));
        assertEquals("null", array.optString(1));
        assertEquals("null", array.optString(2));
        assertEquals("null", array.optString(3));
    }


    @Test
    public void testParseNullYieldsJsonObjectNull() throws JsonException, IOException {
        JsonArray array = JsonArray.readFrom( "[\"null\",null]").asJsonArray();
        array.put((Collection) null);
        assertEquals(array.get(0),"null");
        assertEquals(array.get(1), null);
        assertEquals(array.get(2), null);

        assertEquals("null", array.get(0).toString());
        assertEquals("null", array.get(1).toString());
        assertEquals("null", array.get(2).toString());
    }

        @Test
    public void testNumbers() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(Double.MIN_VALUE);
        array.put(9223372036854775806L);
        array.put(Double.MAX_VALUE);
        array.put(-0d);
        assertEquals(4, array.length());

        // toString() and getString(int) return the same values for -0d
        assertEquals("[4.9E-324,9223372036854775806,1.7976931348623157E308,-0.0]", array.toString());

        assertEquals(array.get(0),Double.MIN_VALUE);
        assertEquals(array.get(1),9223372036854775806L);
        assertEquals(array.get(2), Double.MAX_VALUE);
        assertEquals(array.get(3), -0d);
        assertEquals(array.getDouble(0), Double.MIN_VALUE);
        assertEquals(array.getDouble(1), 9.223372036854776E18);
        assertEquals(array.getDouble(2), Double.MAX_VALUE);
        assertEquals(array.getDouble(3), -0d);
        assertEquals(Long.valueOf(0), array.getLong(0));
        assertEquals(Long.valueOf(9223372036854775806l), array.getLong(1));
        assertEquals((Long)Long.MAX_VALUE, array.getLong(2));
        assertEquals(Long.valueOf(0), array.getLong(3));
        assertEquals((Integer)0, array.getInt(0));
        assertEquals(Integer.valueOf(-2), array.getInt(1));
        assertEquals((Integer)Integer.MAX_VALUE, array.getInt(2));
        assertEquals(Integer.valueOf(0), array.getInt(3));
        assertEquals(array.opt(0), Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, array.optDouble(0));
        assertEquals(Long.valueOf(0), array.optLong(0, 1L));
        assertEquals(Integer.valueOf(0), array.optInt(0, 1));
        assertEquals("4.9E-324", array.getString(0));
        assertEquals("9223372036854775806", array.getString(1));
        assertEquals("1.7976931348623157E308", array.getString(2));
        assertEquals("-0.0", array.getString(3));

        JsonArray other = new JsonArray();
        other.put(Double.MIN_VALUE);
        other.put(9223372036854775806L);
        other.put(Double.MAX_VALUE);
        other.put(-0d);
        assertTrue(array.equals(other));
        other.put(0, 0L);
        assertFalse(array.equals(other));
    }

    @Test
    public void testStrings() throws JsonException {
        JsonArray array = new JsonArray();
        array.put("true");
        array.put("5.5");
        array.put("9223372036854775806");
        array.put("null");
        array.put("5\"8' tall");
        assertEquals(5, array.length());
        assertEquals("[\"true\",\"5.5\",\"9223372036854775806\",\"null\",\"5\\\"8' tall\"]",
                array.toString());

        assertEquals(array.get(0), "true");
        assertEquals("null", array.getString(3));
        assertEquals("5\"8' tall", array.getString(4));
        assertEquals(array.opt(0), "true");
        assertEquals("5.5", array.optString(1));
        assertEquals("9223372036854775806", array.optString(2, null));
        assertEquals("null", array.optString(3, "-1"));
        assertFalse(array.isNull(0));
        assertFalse(array.isNull(3));

        assertEquals(Boolean.TRUE, array.getBoolean(0));
        assertEquals(Boolean.TRUE, array.optBoolean(0));
        assertEquals(Boolean.TRUE, array.optBoolean(0, Boolean.FALSE));
        assertNull(array.optInt(0));
        assertEquals(Integer.valueOf(-2), array.optInt(0, -2));

        assertEquals(5.5d, array.getDouble(1));
        assertEquals(Long.valueOf(5L), array.getLong(1));
        assertEquals(Integer.valueOf(5), array.getInt(1));
        assertEquals(Integer.valueOf(5), array.optInt(1, 3));

        // The last digit of the string is a 6 but getLong returns a 7. It's probably parsing as a
        // double and then converting that to a long. This is consistent with JavaScript.
        assertEquals(Long.valueOf(9223372036854775807L), array.getLong(2));
        assertEquals(9.223372036854776E18, array.getDouble(2));
        assertEquals((Integer)Integer.MAX_VALUE, array.getInt(2));

        assertFalse(array.isNull(3));
        try {
            array.getDouble(3);
            fail();
        } catch (JsonException e) {
        }
        assertNull(array.optDouble(3));
        assertEquals(-1.0d, array.optDouble(3, -1.0d));
    }

    @Test
    public void testToJsonObject() throws JsonException {
        JsonArray keys = new JsonArray();
        keys.put("a");
        keys.put("b");

        JsonArray values = new JsonArray();
        values.put(5.5d);
        values.put(Boolean.FALSE);

        JsonObject object = values.toJsonObject(keys);
        assertEquals(object.get("a"), 5.5d);
        assertEquals(object.get("b"), Boolean.FALSE);

        keys.put(0, "a");
        values.put(0, 11.0d);
        assertEquals(object.get("a"), 5.5d);
    }

    @Test
    public void testToJsonObjectWithNulls() throws JsonException {
        JsonArray keys = new JsonArray();
        keys.put("a");
        keys.put("b");

        JsonArray values = new JsonArray();
        values.put(5.5d);
        values.put((Collection) null);

        // null values are stripped!
        JsonObject object = values.toJsonObject(keys);
        assertEquals(1, object.length());
        assertFalse(object.has("b"));
        assertEquals("{\"a\":5.5}", object.toString());
    }

    @Test
    public void testToJsonObjectMoreNamesThanValues() throws JsonException {
        JsonArray keys = new JsonArray();
        keys.put("a");
        keys.put("b");
        JsonArray values = new JsonArray();
        values.put(5.5d);
        JsonObject object = values.toJsonObject(keys);
        assertEquals(1, object.length());
        assertEquals(object.get("a"), 5.5d);
    }

    @Test
    public void testToJsonObjectMoreValuesThanNames() throws JsonException {
        JsonArray keys = new JsonArray();
        keys.put("a");
        JsonArray values = new JsonArray();
        values.put(5.5d);
        values.put(11.0d);
        JsonObject object = values.toJsonObject(keys);
        assertEquals(1, object.length());
        assertEquals(object.get("a"), 5.5d);
    }

    @Test
    public void testToJsonObjectNullKey() throws JsonException {
        JsonArray keys = new JsonArray();
        keys.put(new JsonNull());
        JsonArray values = new JsonArray();
        values.put(5.5d);
        JsonObject object = values.toJsonObject(keys);
        assertEquals(1, object.length());
        assertEquals(5.5d, object.getDouble("null"));
    }

    @Test

    public void testPutUnsupportedNumbers() throws JsonException {
        JsonArray array = new JsonArray();

        try {
            array.put(Double.NaN);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            array.put(0, Double.NEGATIVE_INFINITY);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            array.put(0, Double.POSITIVE_INFINITY);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPutUnsupportedNumbersAsObject() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(Double.valueOf(Double.NaN));
        array.put(Double.valueOf(Double.NEGATIVE_INFINITY));
        array.put(Double.valueOf(Double.POSITIVE_INFINITY));
        assertEquals(null, array.toString());
    }

    /**
     * Although JsonArray constructor fails
     */
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithUnsupportedNumbers() throws JsonException {
        JsonArray array = new JsonArray(Arrays.asList(5.5, Double.NaN));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToStringWithUnsupportedNumbers() throws JsonException {
        // when the array contains an unsupported number, toString fails
        JsonArray array = new JsonArray(Arrays.asList(5.5, Double.NaN));
        array.toString();
    }

    @Test
    public void testListConstructorCopiesContents() throws JsonException {
        List<Object> contents = Arrays.<Object>asList(5);
        JsonArray array = new JsonArray(contents);
        contents.set(0, 10);
        assertEquals(array.get(0), 5);
    }

    @Test
    public void testCreate() throws JsonException {
        JsonArray array = new JsonArray(Arrays.asList(5.5, Boolean.TRUE));
        assertEquals(2, array.length());
        assertEquals(5.5, array.getDouble(0));
        assertEquals(array.get(1), Boolean.TRUE);
        assertEquals("[5.5,true]", array.toString());
    }

    @Test
    public void testAccessOutOfBounds() throws JsonException {
        JsonArray array = new JsonArray();
        array.put("foo");
        assertNull(array.opt(3));
        assertNull(array.opt(-3));
        assertNull(array.optString(3));
        assertNull(array.optString(-3));
        try {
            array.get(3);
            fail();
        } catch (JsonException e) {
        }
        try {
            array.get(-3);
            fail();
        } catch (JsonException e) {
        }
        try {
            array.getString(3);
            fail();
        } catch (JsonException e) {
        }
        try {
            array.getString(-3);
            fail();
        } catch (JsonException e) {
        }
    }

    @Test
    public void test_remove() throws Exception {
        JsonArray a = new JsonArray();
        assertEquals(a.remove(-1), null);
        assertEquals(a.remove(0), null);

        a.put("hello");
        assertEquals(a.remove(-1), null);
        assertEquals(a.remove(1), null);
        assertEquals(a.remove(0), "hello");
        assertEquals(a.remove(0), null);
    }

    enum MyEnum { A, B, C; }

    // https://code.google.com/p/android/issues/detail?id=62539
    @Test
    public void testEnums() throws Exception {
        // This works because it's in java.* and any class in there falls back to toString.
        JsonArray a1 = new JsonArray(java.lang.annotation.RetentionPolicy.values());
        assertEquals("[\"SOURCE\",\"CLASS\",\"RUNTIME\"]", a1.toString());

        // This should also
        JsonArray a2 = new JsonArray(MyEnum.values());
        assertEquals("[\"A\",\"B\",\"C\"]", a2.toString());
    }
}
