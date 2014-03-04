/*
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

package org.djodjo.json;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Converts Java objects to and from JSON.
 *
**/

public abstract class TypeAdapter<T> {

  /**
   * Writes one JSON value (an array, object, string, number, boolean or null)
   * for {@code value}.
   *
   * @param value the Java object to write. May be null.
   */
  public abstract void write(JsonWriter out, T value) throws IOException;

  public final void toJson(Writer out, T value) throws IOException {
    JsonWriter writer = new JsonWriter(out);
    write(writer, value);
  }

  public final String toJson(T value) throws IOException {
    StringWriter stringWriter = new StringWriter();
    toJson(stringWriter, value);
    return stringWriter.toString();
  }



  /**
   * Reads one JSON value (an array, object, string, number, boolean or null)
   * and converts it to a Java object. Returns the converted object.
   *
   * @return the converted Java object. May be null.
   */
  public abstract T read(JsonReader in) throws IOException, JsonException;


  public final T fromJson(Reader in) throws IOException, JsonException {
    JsonReader reader = new JsonReader(in);
    return read(reader);
  }

  public final T fromJson(String json) throws IOException, JsonException {
    return fromJson(new StringReader(json));
  }

}
