package org.djodjo.json.adapter.example;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.exception.JsonException;

import java.io.IOException;

public class JsonProvider {

    public static JsonObject getSampleObject() {
        try {
            return JsonElement.readFrom("{ \"a\": 1, \"b\": 2, \"c\": 3 }").asJsonObject();
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonArray getSampleArray() {
        try {
            return JsonElement.readFrom("[ \"a\", \"b\", \"c\", 1, 2, 3 ]").asJsonArray();
        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
