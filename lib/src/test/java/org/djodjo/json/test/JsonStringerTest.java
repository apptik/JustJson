/*
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

import junit.framework.TestCase;

import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonException;
import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonStringer;

/**
 * This black box test was written without inspecting the non-free org.json sourcecode.
 */
public class JsonStringerTest extends TestCase {

    public void testEmptyStringer() {
        // why isn't this the empty string?
        assertNull(new JsonStringer().toString());
    }

    public void testValueJsonNull() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(JsonObject.NULL);
        stringer.endArray();
        assertEquals("[null]", stringer.toString());
    }

    public void testEmptyObject() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.object();
        stringer.endObject();
        assertEquals("{}", stringer.toString());
    }

    public void testEmptyArray() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.endArray();
        assertEquals("[]", stringer.toString());
    }

    public void testArray() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(false);
        stringer.value(5.0);
        stringer.value(5L);
        stringer.value("five");
        stringer.value(null);
        stringer.endArray();
        assertEquals("[false,5,5,\"five\",null]", stringer.toString());
    }

    public void testValueObjectMethods() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(Boolean.FALSE);
        stringer.value(Double.valueOf(5.0));
        stringer.value(Long.valueOf(5L));
        stringer.endArray();
        assertEquals("[false,5,5]", stringer.toString());
    }

    public void testKeyValue() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.object();
        stringer.key("a").value(false);
        stringer.key("b").value(5.0);
        stringer.key("c").value(5L);
        stringer.key("d").value("five");
        stringer.key("e").value(null);
        stringer.endObject();
        assertEquals("{\"a\":false," +
                "\"b\":5," +
                "\"c\":5," +
                "\"d\":\"five\"," +
                "\"e\":null}", stringer.toString());
    }

    /**
     * Test what happens when extreme values are emitted. Such values are likely
     * to be rounded during parsing.
     */
    public void testNumericRepresentations() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(Long.MAX_VALUE);
        stringer.value(Double.MIN_VALUE);
        stringer.endArray();
        assertEquals("[9223372036854775807,4.9E-324]", stringer.toString());
    }

    public void testWeirdNumbers() throws JsonException {
        try {
            new JsonStringer().array().value(Double.NaN);
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().array().value(Double.NEGATIVE_INFINITY);
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().array().value(Double.POSITIVE_INFINITY);
            fail();
        } catch (JsonException e) {
        }

        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(-0.0d);
        stringer.value(0.0d);
        stringer.endArray();
        assertEquals("[-0,0]", stringer.toString());
    }

    public void testMismatchedScopes() {
        try {
            new JsonStringer().key("a");
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().value("a");
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().endObject();
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().endArray();
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().array().endObject();
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().object().endArray();
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().object().key("a").key("a");
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonStringer().object().value(false);
            fail();
        } catch (JsonException e) {
        }
    }

    public void testNullKey() {
        try {
            new JsonStringer().object().key(null);
            fail();
        } catch (JsonException e) {
        }
    }

    public void testRepeatedKey() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.object();
        stringer.key("a").value(true);
        stringer.key("a").value(false);
        stringer.endObject();
        // JsonStringer doesn't attempt to detect duplicates
        assertEquals("{\"a\":true,\"a\":false}", stringer.toString());
    }

    public void testEmptyKey() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.object();
        stringer.key("").value(false);
        stringer.endObject();
        assertEquals("{\"\":false}", stringer.toString()); // legit behaviour!
    }

    public void testEscaping() throws JsonException {
        assertEscapedAllWays("a", "a");
        assertEscapedAllWays("a\\\"", "a\"");
        assertEscapedAllWays("\\\"", "\"");
        assertEscapedAllWays(":", ":");
        assertEscapedAllWays(",", ",");
        assertEscapedAllWays("\\b", "\b");
        assertEscapedAllWays("\\f", "\f");
        assertEscapedAllWays("\\n", "\n");
        assertEscapedAllWays("\\r", "\r");
        assertEscapedAllWays("\\t", "\t");
        assertEscapedAllWays(" ", " ");
        assertEscapedAllWays("\\\\", "\\");
        assertEscapedAllWays("{", "{");
        assertEscapedAllWays("}", "}");
        assertEscapedAllWays("[", "[");
        assertEscapedAllWays("]", "]");
        assertEscapedAllWays("\\u0000", "\0");
        assertEscapedAllWays("\\u0019", "\u0019");
        assertEscapedAllWays(" ", "\u0020");
    }

    private void assertEscapedAllWays(String escaped, String original) throws JsonException {
        assertEquals("{\"" + escaped + "\":false}",
                new JsonStringer().object().key(original).value(false).endObject().toString());
        assertEquals("{\"a\":\"" + escaped + "\"}",
                new JsonStringer().object().key("a").value(original).endObject().toString());
        assertEquals("[\"" + escaped + "\"]",
                new JsonStringer().array().value(original).endArray().toString());
        assertEquals("\"" + escaped + "\"", JsonObject.quote(original));
    }

    public void testJsonArrayAsValue() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(false);
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.value(array);
        stringer.endArray();
        assertEquals("[[false]]", stringer.toString());
    }

    public void testJsonObjectAsValue() throws JsonException {
        JsonObject object = new JsonObject();
        object.put("a", false);
        JsonStringer stringer = new JsonStringer();
        stringer.object();
        stringer.key("b").value(object);
        stringer.endObject();
        assertEquals("{\"b\":{\"a\":false}}", stringer.toString());
    }

    public void testArrayNestingMaxDepthSupports20() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.array();
        }
        for (int i = 0; i < 20; i++) {
            stringer.endArray();
        }
        assertEquals("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]", stringer.toString());

        stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.array();
        }
    }

    public void testObjectNestingMaxDepthSupports20() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.object();
            stringer.key("a");
        }
        stringer.value(false);
        for (int i = 0; i < 20; i++) {
            stringer.endObject();
        }
        assertEquals("{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":" +
                "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":false" +
                "}}}}}}}}}}}}}}}}}}}}", stringer.toString());

        stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.object();
            stringer.key("a");
        }
    }

    public void testMixedMaxDepthSupports20() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        for (int i = 0; i < 20; i+=2) {
            stringer.array();
            stringer.object();
            stringer.key("a");
        }
        stringer.value(false);
        for (int i = 0; i < 20; i+=2) {
            stringer.endObject();
            stringer.endArray();
        }
        assertEquals("[{\"a\":[{\"a\":[{\"a\":[{\"a\":[{\"a\":" +
                "[{\"a\":[{\"a\":[{\"a\":[{\"a\":[{\"a\":false" +
                "}]}]}]}]}]}]}]}]}]}]", stringer.toString());

        stringer = new JsonStringer();
        for (int i = 0; i < 20; i+=2) {
            stringer.array();
            stringer.object();
            stringer.key("a");
        }
    }

    public void testMaxDepthWithArrayValue() throws JsonException {
        JsonArray array = new JsonArray();
        array.put(false);

        JsonStringer stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.array();
        }
        stringer.value(array);
        for (int i = 0; i < 20; i++) {
            stringer.endArray();
        }
        assertEquals("[[[[[[[[[[[[[[[[[[[[[false]]]]]]]]]]]]]]]]]]]]]", stringer.toString());
    }

    public void testMaxDepthWithObjectValue() throws JsonException {
        JsonObject object = new JsonObject();
        object.put("a", false);
        JsonStringer stringer = new JsonStringer();
        for (int i = 0; i < 20; i++) {
            stringer.object();
            stringer.key("b");
        }
        stringer.value(object);
        for (int i = 0; i < 20; i++) {
            stringer.endObject();
        }
        assertEquals("{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":" +
                "{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":{\"b\":" +
                "{\"a\":false}}}}}}}}}}}}}}}}}}}}}", stringer.toString());
    }

    public void testMultipleRoots() throws JsonException {
        JsonStringer stringer = new JsonStringer();
        stringer.array();
        stringer.endArray();
        try {
            stringer.object();
            fail();
        } catch (JsonException e) {
        }
    }
}
