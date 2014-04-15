/*
 * Copyright (C) 2014 Kalin Maldzhanski
 * Copyright (C) 2011 Google Inc.
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

package org.djodjo.json.util;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonBoolean;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonNull;
import org.djodjo.json.JsonNumber;
import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonReader;
import org.djodjo.json.JsonString;
import org.djodjo.json.JsonToken;
import org.djodjo.json.JsonWriter;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.exception.JsonTypeSyntaxException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.BitSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;



/**
 * Type adapters for basic types.
 */
public final class TypeAdapters {
  private TypeAdapters() {}

  @SuppressWarnings("rawtypes")
  public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>() {
    @Override
    public void write(JsonWriter out, Class value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: "
            + value.getName() + ". Forgot to register a type adapter?");
      }
    }
    @Override
    public Class read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      } else {
        throw new UnsupportedOperationException(
            "Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
      }
    }
  };

  public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() {
    public BitSet read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }

      BitSet bitset = new BitSet();
      in.beginArray();
      int i = 0;
      JsonToken tokenType = in.peek();
      while (tokenType != JsonToken.END_ARRAY) {
        boolean set;
        switch (tokenType) {
        case NUMBER:
          set = in.nextInt() != 0;
          break;
        case BOOLEAN:
          set = in.nextBoolean();
          break;
        case STRING:
          String stringValue = in.nextString();
          try {
            set = Integer.parseInt(stringValue) != 0;
          } catch (NumberFormatException e) {
            throw new JsonTypeSyntaxException(
                "Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
          }
          break;
        default:
          throw new JsonTypeSyntaxException("Invalid bitset value type: " + tokenType);
        }
        if (set) {
          bitset.set(i);
        }
        ++i;
        tokenType = in.peek();
      }
      in.endArray();
      return bitset;
    }

    public void write(JsonWriter out, BitSet src) throws IOException {
      if (src == null) {
        out.nullValue();
        return;
      }

      out.beginArray();
      for (int i = 0; i < src.length(); i++) {
        int value = (src.get(i)) ? 1 : 0;
        out.value(value);
      }
      out.endArray();
    }
  };



  public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
    @Override
    public Boolean read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      } else if (in.peek() == JsonToken.STRING) {
        // support strings for compatibility with GSON 1.7
        return Boolean.parseBoolean(in.nextString());
      }
      return in.nextBoolean();
    }
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
      if (value == null) {
        out.nullValue();
        return;
      }
      out.value(value);
    }
  };

  /**
   * Writes a boolean as a string. Useful for map keys, where booleans aren't
   * otherwise permitted.
   */
  public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
    @Override public Boolean read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return Boolean.valueOf(in.nextString());
    }

    @Override public void write(JsonWriter out, Boolean value) throws IOException {
      out.value(value == null ? "null" : value.toString());
    }
  };



  public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        int intValue = in.nextInt();
        return (byte) intValue;
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };


  public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        return (short) in.nextInt();
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };



  public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        return in.nextInt();
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };



  public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        return in.nextLong();
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };

  public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return (float) in.nextDouble();
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };

  public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return in.nextDouble();
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };

  public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>() {
    @Override
    public Number read(JsonReader in) throws IOException {
      JsonToken jsonToken = in.peek();
      switch (jsonToken) {
      case NULL:
        in.nextNull();
        return null;
      case NUMBER:
        return new JsonNumber(in.nextString()).asNumber();
      default:
        throw new JsonTypeSyntaxException("Expecting number, got: " + jsonToken);
      }
    }
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
    }
  };


  public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
    @Override
    public Character read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String str = in.nextString();
      if (str.length() != 1) {
        throw new JsonTypeSyntaxException("Expecting character, got: " + str);
      }
      return str.charAt(0);
    }
    @Override
    public void write(JsonWriter out, Character value) throws IOException {
      out.value(value == null ? null : String.valueOf(value));
    }
  };



  public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
    @Override
    public String read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      /* coerce booleans to strings for backwards compatibility */
      if (peek == JsonToken.BOOLEAN) {
        return Boolean.toString(in.nextBoolean());
      }
      return in.nextString();
    }
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }
  };
  
  public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
    @Override public BigDecimal read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        return new BigDecimal(in.nextString());
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }

    @Override public void write(JsonWriter out, BigDecimal value) throws IOException {
      out.value(value);
    }
  };
  
  public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
    @Override public BigInteger read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        return new BigInteger(in.nextString());
      } catch (NumberFormatException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }

    @Override public void write(JsonWriter out, BigInteger value) throws IOException {
      out.value(value);
    }
  };



  public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>() {
    @Override
    public StringBuilder read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return new StringBuilder(in.nextString());
    }
    @Override
    public void write(JsonWriter out, StringBuilder value) throws IOException {
      out.value(value == null ? null : value.toString());
    }
  };


  public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>() {
    @Override
    public StringBuffer read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return new StringBuffer(in.nextString());
    }
    @Override
    public void write(JsonWriter out, StringBuffer value) throws IOException {
      out.value(value == null ? null : value.toString());
    }
  };



  public static final TypeAdapter<URL> URL = new TypeAdapter<URL>() {
    @Override
    public URL read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String nextString = in.nextString();
      return "null".equals(nextString) ? null : new URL(nextString);
    }
    @Override
    public void write(JsonWriter out, URL value) throws IOException {
      out.value(value == null ? null : value.toExternalForm());
    }
  };



  public static final TypeAdapter<URI> URI = new TypeAdapter<URI>() {
    @Override
    public URI read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      try {
        String nextString = in.nextString();
        return "null".equals(nextString) ? null : new URI(nextString);
      } catch (URISyntaxException e) {
        throw new JsonTypeSyntaxException(e);
      }
    }
    @Override
    public void write(JsonWriter out, URI value) throws IOException {
      out.value(value == null ? null : value.toASCIIString());
    }
  };



  public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>() {
    @Override
    public InetAddress read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      // regrettably, this should have included both the host name and the host address
      return InetAddress.getByName(in.nextString());
    }
    @Override
    public void write(JsonWriter out, InetAddress value) throws IOException {
      out.value(value == null ? null : value.getHostAddress());
    }
  };



  public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>() {
    @Override
    public UUID read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return java.util.UUID.fromString(in.nextString());
    }
    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
      out.value(value == null ? null : value.toString());
    }
  };




//    public static final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
//
//
//    public static final TypeAdapter<Timestamp> TIME_STAMP =  new TypeAdapter<Timestamp>() {
//        @Override public Timestamp read(JsonReader in) throws IOException {
//          Date date = dateTypeAdapter.read(in);
//          return date != null ? new Timestamp(date.getTime()) : null;
//        }
//
//        @Override public void write(JsonWriter out, Timestamp value) throws IOException {
//          dateTypeAdapter.write(out, value);
//        }
//    };

  public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>() {
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY_OF_MONTH = "dayOfMonth";
    private static final String HOUR_OF_DAY = "hourOfDay";
    private static final String MINUTE = "minute";
    private static final String SECOND = "second";

    @Override
    public Calendar read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return  null;
      }
      in.beginObject();
      int year = 0;
      int month = 0;
      int dayOfMonth = 0;
      int hourOfDay = 0;
      int minute = 0;
      int second = 0;
      while (in.peek() != JsonToken.END_OBJECT) {
        String name = in.nextName();
        int value = in.nextInt();
        if (YEAR.equals(name)) {
          year = value;
        } else if (MONTH.equals(name)) {
          month = value;
        } else if (DAY_OF_MONTH.equals(name)) {
          dayOfMonth = value;
        } else if (HOUR_OF_DAY.equals(name)) {
          hourOfDay = value;
        } else if (MINUTE.equals(name)) {
          minute = value;
        } else if (SECOND.equals(name)) {
          second = value;
        }
      }
      in.endObject();
      return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
    }

    @Override
    public void write(JsonWriter out, Calendar value) throws IOException {
      if (value == null) {
        out.nullValue();
        return;
      }
      out.beginObject();
      out.name(YEAR);
      out.value(value.get(Calendar.YEAR));
      out.name(MONTH);
      out.value(value.get(Calendar.MONTH));
      out.name(DAY_OF_MONTH);
      out.value(value.get(Calendar.DAY_OF_MONTH));
      out.name(HOUR_OF_DAY);
      out.value(value.get(Calendar.HOUR_OF_DAY));
      out.name(MINUTE);
      out.value(value.get(Calendar.MINUTE));
      out.name(SECOND);
      out.value(value.get(Calendar.SECOND));
      out.endObject();
    }
  };



  public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>() {
    @Override
    public Locale read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String locale = in.nextString();
      StringTokenizer tokenizer = new StringTokenizer(locale, "_");
      String language = null;
      String country = null;
      String variant = null;
      if (tokenizer.hasMoreElements()) {
        language = tokenizer.nextToken();
      }
      if (tokenizer.hasMoreElements()) {
        country = tokenizer.nextToken();
      }
      if (tokenizer.hasMoreElements()) {
        variant = tokenizer.nextToken();
      }
      if (country == null && variant == null) {
        return new Locale(language);
      } else if (variant == null) {
        return new Locale(language, country);
      } else {
        return new Locale(language, country, variant);
      }
    }
    @Override
    public void write(JsonWriter out, Locale value) throws IOException {
      out.value(value == null ? null : value.toString());
    }
  };


  public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() {
    @Override public JsonElement read(JsonReader in) throws IOException, JsonException {
      switch (in.peek()) {
      case STRING:
        return new JsonString(in.nextString());
      case NUMBER:
        return new JsonNumber(in.nextString());
      case BOOLEAN:
        return new JsonBoolean(in.nextBoolean());
      case NULL:
        in.nextNull();
        return new JsonNull();
      case BEGIN_ARRAY:
        JsonArray array = new JsonArray();
        in.beginArray();
        while (in.hasNext()) {
          array.put(read(in));
        }
        in.endArray();
        return array;
      case BEGIN_OBJECT:
        JsonObject object = new JsonObject();
        in.beginObject();
        while (in.hasNext()) {
          object.put(in.nextName(), read(in));
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

    @Override public void write(JsonWriter out, JsonElement value) throws IOException {
      //TODO should this actually happen??
        if (value == null) {
        out.nullValue();
      } else {
          value.write(out);
      }
//      else if (value.isJsonPrimitive()) {
//        JsonPrimitive primitive = value.getAsJsonPrimitive();
//        if (primitive.isNumber()) {
//          out.value(primitive.getAsNumber());
//        } else if (primitive.isBoolean()) {
//          out.value(primitive.getAsBoolean());
//        } else {
//          out.value(primitive.getAsString());
//        }
//
//      } else if (value.isJsonArray()) {
//        out.beginArray();
//        for (JsonElement e : value.getAsJsonArray()) {
//          write(out, e);
//        }
//        out.endArray();
//
//      } else if (value.isJsonObject()) {
//        out.beginObject();
//        for (Map.Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
//          out.name(e.getKey());
//          write(out, e.getValue());
//        }
//        out.endObject();
//
//      } else {
//        throw new IllegalArgumentException("Couldn't write " + value.getClass());
//      }
    }
  };


  private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<String, T> nameToConstant = new HashMap<String, T>();
    private final Map<T, String> constantToName = new HashMap<T, String>();

    public EnumTypeAdapter(Class<T> classOfT) {

        for (T constant : classOfT.getEnumConstants()) {
          String name = constant.name();
          nameToConstant.put(name, constant);
          constantToName.put(constant, name);
        }

    }
    public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return nameToConstant.get(in.nextString());
    }

    public void write(JsonWriter out, T value) throws IOException {
      out.value(value == null ? null : constantToName.get(value));
    }
  }




}
