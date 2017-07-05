package io.apptik.json;

import static io.apptik.json.JsonNull.JSON_NULL;
import io.apptik.json.exception.JsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

public abstract class JsonElement {

	private static class Adapter {
		static public JsonElement fromJson(final JsonReader in)
				throws IOException, JsonException {
			return read(in);
		}

		static public JsonElement fromJson(final Reader in) throws IOException,
				JsonException {
			JsonReader reader = new JsonReader(in);
			return read(reader);
		}

		static public JsonElement fromJson(final String json)
				throws IOException, JsonException {
			return fromJson(new StringReader(json));
		}

		static public JsonElement read(final JsonReader in) throws IOException,
				JsonException {
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

		static public String toJson(final JsonElement value) throws IOException {
			StringWriter stringWriter = new StringWriter();
			toJson(stringWriter, value);
			return stringWriter.toString();
		}

		static public void toJson(final Writer out, final JsonElement value)
				throws IOException {
			JsonWriter writer = new JsonWriter(out);
			write(writer, value);
		}

		static public void write(final JsonWriter out, final JsonElement value)
				throws IOException {
			// TODO should this actually happen??
			if (value == null) {
				out.nullValue();
			} else {
				value.write(out);
			}
		}
	}

	public static final String TYPE_ARRAY = "array";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_INTEGER = "integer";
	public static final String TYPE_NULL = "null";
	public static final String TYPE_NUMBER = "number";
	public static final String TYPE_OBJECT = "object";

	public static final String TYPE_STRING = "string";

	public static JsonElement readFrom(final JsonReader reader)
			throws JsonException, IOException {
		return Adapter.fromJson(reader);
	}

	public static JsonElement readFrom(final Reader reader)
			throws JsonException, IOException {
		return Adapter.fromJson(reader);
	}

	public static JsonElement readFrom(final String text) throws JsonException,
			IOException {
		return JsonElement.readFrom(new StringReader(text));

	}

	/**
	 * Wraps the given object if to JsonXXX object.
	 */
	public static JsonElement wrap(final Object o) throws JsonException {
		if (o == null) {
			// null value means not specified i.e.-> no valued will be mapped
			// Json.null is specific value
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

	public boolean asBoolean() {
		throw new UnsupportedOperationException(toString()
				+ " is not a boolean");
	}

	public byte asByte() {
		throw new UnsupportedOperationException(toString()
				+ " is not a byte number");
	}

	public double asDouble() {
		throw new UnsupportedOperationException(toString()
				+ " is not a double number");
	}

	public float asFloat() {
		throw new UnsupportedOperationException(toString()
				+ " is not a float number");
	}

	public int asInt() {
		throw new UnsupportedOperationException(toString()
				+ " is not an integer number");
	}

	public JsonArray asJsonArray() {
		throw new UnsupportedOperationException(toString()
				+ " is not an json array");
	}

	public JsonObject asJsonObject() {
		throw new UnsupportedOperationException(toString()
				+ " is not a json object");
	}

	public long asLong() {
		throw new UnsupportedOperationException(toString()
				+ " is not a long integer number");
	}

	public String asString() {
		throw new UnsupportedOperationException(toString() + " is not a string");
	}

	@Override
	public boolean equals(final Object object) {
		return super.equals(object);
	}

	public abstract String getJsonType();

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public boolean isBoolean() {
		return false;
	}

	public boolean isJsonArray() {
		return false;
	}

	public boolean isJsonObject() {
		return false;
	}

	public boolean isNull() {
		return false;
	}

	public boolean isNumber() {
		return false;
	}

	public boolean isString() {
		return false;
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

	public abstract void write(JsonWriter writer) throws IOException;

	public void writeTo(final Writer writer) throws IOException {
		write(new JsonWriter(writer));
	}

}
