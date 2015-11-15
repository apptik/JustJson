/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package io.apptik.json.perftest;

import io.apptik.json.perftest.caliper.CaliperRunner;
import io.apptik.json.perftest.jsonrunners.JsonRunner;
import io.apptik.json.perftest.jsonrunners.JsonRunnerFactory;
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;

import java.io.*;

import static io.apptik.json.perftest.resources.Resources.readResource;


/*
 * The JsonWriter writes tokens directly to the underlying writer. Hence, wrapping the writer in a
 * BufferedWriter can drastically improve the writing performance.
 */
public class BufferedWriterBenchmark extends SimpleBenchmark {

  private JsonRunner runner;
  private String json;
  private Object model;

  @Param String input;
  @Param String parser;

  @Override
  protected void setUp() throws Exception {
    json = readResource("input/" + input + ".json");
    runner = JsonRunnerFactory.findByName(parser);
    model = runner.readFromString(json);
  }

  public void timeWriter(int reps) throws Exception {
    for (int i = 0; i < reps; i++) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      Writer writer = new OutputStreamWriter(output);
      runner.writeToWriter(model, writer);
      writer.close();
      if (output.size() == 0) {
        throw new RuntimeException();
      }
    }
  }

  public void timeBufferedWriter(int reps) throws Exception {
    for (int i = 0; i < reps; i++) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      Writer writer = new BufferedWriter(new OutputStreamWriter(output));
      runner.writeToWriter(model, writer);
      writer.close();
      if (output.size() == 0) {
        throw new RuntimeException();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    CaliperRunner runner = new CaliperRunner(BufferedWriterBenchmark.class);
    runner.addParameterDefault("parser", "null", "gson", "jackson", "minimal-json", "jjson");
    runner.addParameterDefault("input", "rap", "caliper", "long-string", "numbers-array");
    runner.exec(args);
  }

}
