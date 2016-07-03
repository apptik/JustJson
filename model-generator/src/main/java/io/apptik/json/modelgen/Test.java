package io.apptik.json.modelgen;

import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.*;

public class Test extends JsonObjectWrapper {

    private String getTestString() {
        return getJson().optString("test string");
    }


    private Integer getTestInt() {
        return getJson().optInt("test int");
    }


    private Double getTestDouble() {
        return getJson().optDouble("test double");
    }


    private Boolean getTestBool() {
        return getJson().optBoolean("test bool");
    }


    private JsonElement getTestNull() {
        return getJson().opt("testNull");
    }


    private JsonArray getTestArray() {
        return getJson().optJsonArray("test array");
    }


    private JsonObject getTestObject() {
        return getJson().optJsonObject("test Object");
    }

}