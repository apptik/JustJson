package org.djodjo.json;



import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

public abstract class JsonElement implements Serializable {


    public static JsonElement readFrom( Reader reader ) throws JsonException, IOException {
        return TypeAdapters.JSON_ELEMENT.fromJson(reader);
    }

    public static JsonElement readFrom( String text ) throws JsonException, IOException {
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

    public Number asNumber() throws ParseException {
        throw new UnsupportedOperationException(toString() + " is not a number");
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

    public void writeTo( Writer writer ) throws IOException {
        write( new JsonWriter( writer ) );
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter( stringWriter );
        try {
            write( jsonWriter );
        } catch( IOException exception ) {
            // StringWriter does not throw IOExceptions
            throw new RuntimeException( exception );
        }
        return stringWriter.toString();
    }

    @Override
    public boolean equals( Object object ) {
        return super.equals( object );
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Wraps the given object if to JsonXXX object.
     */
    public static JsonElement wrap(Object o) {
        if (o == null) {
            return new JsonNull();
        }
        if (o instanceof JsonElement) {
            return (JsonElement)o;
        }
        try {
            if (o instanceof Collection) {
                return new JsonArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return new JsonArray(o);
            }
            if (o instanceof Map) {
                return new JsonObject((Map) o);
            }
            if (o instanceof Boolean) {
                return new JsonBoolean((Boolean)o);
            }
            if (o instanceof Number) {
                return new JsonNumber((Number) o);
            }
            if (o instanceof String) {
                return new JsonString((String)o);
            }

            if (o instanceof Character) {
                return new JsonString(Character.toString((Character)o));
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return new JsonString(o.toString());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    protected abstract void write( JsonWriter writer ) throws IOException;



}
