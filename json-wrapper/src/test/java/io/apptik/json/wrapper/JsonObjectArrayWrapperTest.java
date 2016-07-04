package io.apptik.json.wrapper;

import io.apptik.json.JsonArray;
import io.apptik.json.JsonObject;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class JsonObjectArrayWrapperTest {

    JsonArray jar;
    JsonObjectArrayWrapper<TestWrapper> arrayWrapper = new JsonObjectArrayWrapper<TestWrapper>();

    static class TestWrapper extends JsonObjectWrapper {

        public String getTheName() {
            return getJson().optString("theName");
        }

        public TestWrapper withName(String name) {
            getJson().put("theName", name);
            return this;
        }
    }

    @Before
    public void setUp() throws Exception {
        jar = JsonArray.readFrom(
                "[{\"theName\":\"value1\"}, " +
                        "{\"theName\":\"value2\"}]").asJsonArray();
    }

    @Test
    public void testWrapNoType() throws Exception {
        try {
            arrayWrapper.wrap(jar);
            fail("Should not wrap not typed Json");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("cannot wrap Typed Element with empty type");
        }
    }

    @Test
    public void testGet() throws Exception {
        arrayWrapper.wrap(jar, TestWrapper.class);
        assertThat(arrayWrapper.get(0).getTheName()).isEqualTo("value1");
        assertThat(arrayWrapper.get(1).getTheName()).isEqualTo("value2");
    }

    @Test
    public void testTo() throws Exception {
        arrayWrapper.withWrapperType(TestWrapper.class);
        arrayWrapper.add(new TestWrapper().withName("value1"));
        arrayWrapper.add(new TestWrapper().withName("value2"));
        assertThat(arrayWrapper.get(0).getTheName()).isEqualTo("value1");
        assertThat(arrayWrapper.get(1).getTheName()).isEqualTo("value2");
    }

    @Test
    public void testContains() throws Exception {
        arrayWrapper.wrap(jar, TestWrapper.class);
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("theName", "value2");
        Map<String, String> testMap2 = new HashMap<String, String>();
        testMap2.put("theName", "valueNA");
        assertThat(arrayWrapper.contains(testMap)).isTrue();
        assertThat(arrayWrapper.contains(new JsonObject(testMap))).isTrue();
        assertThat(arrayWrapper.contains(
                new JsonObjectWrapper().wrap(new JsonObject(testMap)))).isTrue();

        assertThat(arrayWrapper.contains(testMap2)).isFalse();
        assertThat(arrayWrapper.contains(new JsonObject(testMap2))).isFalse();
        assertThat(arrayWrapper.contains(
                new JsonObjectWrapper().wrap(new JsonObject(testMap2)))).isFalse();

    }
}