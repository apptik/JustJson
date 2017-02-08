package io.apptik.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import io.apptik.json.exception.JsonException;

import static io.apptik.json.JsonNull.JSON_NULL;

public abstract class JsonElement {

    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_NULL = "null";

    public static JsonElement readFrom(JsonReader reader) throws JsonException, IOException {
        return Adapter.fromJson(reader);
    }

    public static JsonElement readFrom(Reader reader) throws JsonException, IOException {
        return Adapter.fromJson(reader);
    }

    public static JsonElement readFrom(String text) throws JsonException, IOException {
        return JsonElement.readFrom(new StringReader(text));

    }

    public boolean isNull() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isJsonObject() {
        return false;
    }

    public boolean isJsonArray() {
        return false;
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException(toString() + " is not a boolean");
    }

    public double asDouble() {
        throw new UnsupportedOperationException(toString() + " is not a double number");
    }

    public float asFloat() {
        throw new UnsupportedOperationException(toString() + " is not a float number");
    }

    public int asInt() {
        throw new UnsupportedOperationException(toString() + " is not an integer number");
    }

    public long asLong() {
        throw new UnsupportedOperationException(toString() + " is not a long integer number");
    }

    public byte asByte() {
        throw new UnsupportedOperationException(toString() + " is not a byte number");
    }

    public String asString() {
        throw new UnsupportedOperationException(toString() + " is not a string");
    }

    public JsonObject asJsonObject() {
        throw new UnsupportedOperationException(toString() + " is not a json object");
    }

    public JsonArray asJsonArray() {
        throw new UnsupportedOperationException(toString() + " is not an json array");
    }

    public void writeTo(Writer writer) throws IOException {
        write(new JsonWriter(writer));
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            write(jsonWriter);
        } catch (IOException exception) {
            // StringWriter does not throw IOExceptions
            throw new RuntimeException(exception);
        }
        return stringWriter.toString();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Wraps the given object if to JsonXXX object.
     */
    public static JsonElement wrap(Object o) throws JsonException {
        if (o == null) {
            //null value means not specified i.e.-> no valued will be mapped
            //Json.null is specific value
            return null;
        }
        if (o instanceof JsonElement) {
            return (JsonElement) o;
        }
        if (o instanceof ElementWrapper) {
            return ((ElementWrapper) o).getJson();
        }
        if (o instanceof Collection) {
            return new JsonArray((Collection) o);
        } else if (o.getClass().isArray()) {
            return new JsonArray(o);
        }
        if (o instanceof Map) {
            return new JsonObject((Map) o);
        }
        if (o instanceof Boolean) {
            return new JsonBoolean((Boolean) o);
        }
        if (o instanceof Number) {
            return new JsonNumber((Number) o);
        }
        if (o instanceof String) {
            return new JsonString((String) o);
        }
        if (o instanceof Character) {
            return new JsonString(Character.toString((Character) o));
        }
        if (o instanceof ByteBuffer) {
            return new JsonString(((ByteBuffer) o).asCharBuffer().toString());
        }

        return new JsonString(o.toString());
    }

    public abstract void write(JsonWriter writer) throws IOException;

    public abstract String getJsonType();

    private static class Adapter {
        static public void write(JsonWriter out, JsonElement value) throws IOException {
            //TODO should this actually happen??
            if (value == null) {
                out.nullValue();
            } else {
                value.write(out);
            }
        }

        static public void toJson(Writer out, JsonElement value) throws IOException {
            JsonWriter writer = new JsonWriter(out);
            write(writer, value);
        }

        static public String toJson(JsonElement value) throws IOException {
            StringWriter stringWriter = new StringWriter();
            toJson(stringWriter, value);
            return stringWriter.toString();
        }

        static public JsonElement read(JsonReader in) throws IOException, JsonException {
            switch (in.peek()) {
                case STRING:
                    return new JsonString(in.nextString());
                case NUMBER:
                    return new JsonNumber(in.nextString());
                case BOOLEAN:
                    return new JsonBoolean(in.nextBoolean());
                case NULL:
                    in.nextNull();
                    return JSON_NULL;
                case BEGIN_ARRAY:
                    JsonArray array = new JsonArray();
                    in.beginArray();
                    while (in.hasNext()) {
                        array.putInternal(read(in));
                    }
                    in.endArray();
                    return array;
                case BEGIN_OBJECT:
                    JsonObject object = new JsonObject();
                    in.beginObject();
                    while (in.hasNext()) {
                        object.putInternal(in.nextName(), read(in));
                    }
                    in.endObject();
                    return object;
                case END_DOCUMENT:
                case NAME:
                case END_OBJECT:
                case END_ARRAY:
                default:
                    throw new IllegalArgumentException();
            }
        }

        static public JsonElement fromJson(Reader in) throws IOException, JsonException {
           JsonReader reader = new JsonReader(in);
            return read(reader);
        }

        static public JsonElement fromJson(JsonReader in) throws IOException, JsonException {
            return read(in);
        }

        static public JsonElement fromJson(String json) throws IOException, JsonException {
            return fromJson(new StringReader(json));
        }
    }

}
