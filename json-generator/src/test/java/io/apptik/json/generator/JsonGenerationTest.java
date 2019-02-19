/*
 * Copyright (C) 2014 Kalin Maldzhanski
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

package io.apptik.json.generator;


import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.schema.SchemaV4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class JsonGenerationTest {
    SchemaV4 schema;

    @Before
    public void setUp() throws Exception {
        schema = new SchemaV4().wrap(JsonElement.readFrom(
                "{\n" +


                        "\"type\" : \"object\"," +
                        "\"properties\" : {" +
                        "\"one\" : {\"type\" : \"number\"  } ," +
                        "\"two\" : {\"type\" : \"string\"  }," +
                        "\"three\" : " + "{" +
                        "\"type\" : \"object\"," +
                        "\"properties\" : {" +
                        "\"one\" : {\"type\" : \"number\"  } ," +
                        "\"two\" : {\"type\" : \"string\" }" +
                        "}" +
                        "}," +
                        "\"four\" : {\"type\" : \"boolean\"  }," +
                        "\"five\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," +
                        "\"six\" : {\"enum\" : [\"one\", 2, 3.5, true, [\"almost empty aray\"], {\"one-item\":\"object\"}, null]  }, " +
                        "\"seven\" : {\"type\" : \"string\", \"format\": \"uri\" }," +
                        "\"eight\" : {\"type\" : \"string\", \"format\": \"email\" }" +
                        "}" +
                        "}").asJsonObject());
    }

    @Test
    public void testGenerate() throws Exception {
        JsonGeneratorConfig gConf = new JsonGeneratorConfig();
        ArrayList<String> images = new ArrayList<String>();
        images.add("/photos/image.jpg");
        images.add("/photos/image.jpg");

        gConf.uriPaths.put("seven", images);
        // gConf.globalUriPaths = images;
        JsonObject job = new JsonGenerator(schema, gConf).generate().asJsonObject();

        System.out.println(job.toString());

        assertEquals(8, job.length());
    }

    @Test
    public void testDefaults() throws Exception {
        SchemaV4 defaultsSchema = new SchemaV4().wrap(JsonElement.readFrom(
                "{\n" +
                        "\"type\" : \"object\"," +
                        "\"properties\" : {" +
                            "\"integerDefault\" : {\"type\" : \"integer\", \"default\": 1  } ," +
                            "\"integerConst\" : {\"type\" : \"integer\", \"const\": 2  }," +
                            "\"integerEnum\" : {\"type\" : \"integer\", \"enum\": [ 3, 4]  }," +
                            "\"integerExamples\" : {\"type\" : \"integer\", \"examples\": [ 5, 6]  }," +
                            "\"numberDefault\" : {\"type\" : \"number\", \"default\": 25.1  } ," +
                            "\"numberConst\" : {\"type\" : \"number\", \"const\": 25.2  }," +
                            "\"numberEnum\" : {\"type\" : \"number\", \"enum\": [ 25.3, 25.4 ]  }," +
                            "\"numberExamples\" : {\"type\" : \"number\", \"examples\": [ 25.5, 25.6]  }," +
                            "\"stringDefault\" : {\"type\" : \"string\", \"default\": \"someDefault\"  }," +
                            "\"stringConst\" : {\"type\" : \"string\", \"const\": \"someConst\"  }," +
                            "\"stringEnum\" : {\"type\" : \"string\", \"enum\": [\"someEnum\", \"someEnum2\"]  }," +
                            "\"stringExamples\" : {\"type\" : \"string\", \"examples\": [\"someExample\", \"someExample2\"]  }," +
                            "\"booleanDefault\" : {\"type\" : \"boolean\", \"default\": true  }," +
                            "\"booleanConst\" : {\"type\" : \"boolean\", \"const\": false  }," +
                            "\"booleanEnum\" : {\"type\" : \"boolean\", \"enum\": [ true, true]  }," +
                            "\"booleanExamples\" : {\"type\" : \"boolean\", \"examples\": [ false, false]  }," +
                            "\"arrayWithDefaults\" : {\"type\" : \"array\"," +
                                " \"items\" : { \"type\" : \"object\", " +
                                    "\"properties\" : {" +
                                        "\"integerDefault\" : {\"type\" : \"integer\", \"default\": 1  } ," +
                                        "\"integerConst\" : {\"type\" : \"integer\", \"const\": 2  }," +
                                        "\"integerEnum\" : {\"type\" : \"integer\", \"enum\": [ 3, 4]  }," +
                                        "\"integerExamples\" : {\"type\" : \"integer\", \"examples\": [ 5, 6]  }," +
                                        "\"numberDefault\" : {\"type\" : \"number\", \"default\": 25.1  } ," +
                                        "\"numberConst\" : {\"type\" : \"number\", \"const\": 25.2  }," +
                                        "\"numberEnum\" : {\"type\" : \"number\", \"enum\": [ 25.3, 25.4 ]  }," +
                                        "\"numberExamples\" : {\"type\" : \"number\", \"examples\": [ 25.5, 25.6]  }," +
                                        "\"stringDefault\" : {\"type\" : \"string\", \"default\": \"someDefault\"  }," +
                                        "\"stringConst\" : {\"type\" : \"string\", \"const\": \"someConst\"  }," +
                                        "\"stringEnum\" : {\"type\" : \"string\", \"enum\": [\"someEnum\", \"someEnum2\"]  }," +
                                        "\"stringExamples\" : {\"type\" : \"string\", \"examples\": [\"someExample\", \"someExample2\"]  }," +
                                        "\"booleanDefault\" : {\"type\" : \"boolean\", \"default\": true  }," +
                                        "\"booleanConst\" : {\"type\" : \"boolean\", \"const\": false  }," +
                                        "\"booleanEnum\" : {\"type\" : \"boolean\", \"enum\": [ true, true]  }," +
                                        "\"booleanExamples\" : {\"type\" : \"boolean\", \"examples\": [ false, false]  }" +
                                    "}" +
                                "}" +
                            "}" +
                        "}" +
                    "}").asJsonObject());

        JsonObject job = new JsonGenerator(defaultsSchema, new JsonGeneratorConfig()).generate().asJsonObject();
        System.out.println("Output is: " + job.toString());
        assertEquals("someDefault", job.getString("stringDefault"));
        assertEquals("someConst", job.getString("stringConst"));
        assertTrue(job.getString("stringEnum").contains("someEnum") || job.getString("stringEnum").contains("someEnum2"));
        assertTrue(job.getString("stringExamples").contains("someExample") || job.getString("stringExamples").contains("someExample2"));

        assertTrue(1 == job.getInt("integerDefault"));
        assertTrue(2 == job.getInt("integerConst"));
        assertTrue(job.getInt("integerEnum") == 3|| job.getInt("integerEnum") == 4);
        assertTrue(job.getInt("integerExamples") == 5 || job.getInt("integerExamples") == 6);

        assertEquals(25.1, job.getDouble("numberDefault"));
        assertEquals(25.2, job.getDouble("numberConst"));
        assertTrue(job.getDouble("numberEnum") == 25.3|| job.getDouble("numberEnum") == 25.4);
        assertTrue(job.getDouble("numberExamples") == 25.5 || job.getDouble("numberExamples") == 25.6);

        assertTrue(true == job.getBoolean("booleanDefault"));
        assertTrue(false == job.getBoolean("booleanConst"));
        assertTrue(job.getBoolean("booleanEnum") == true);
        assertTrue(job.getBoolean("booleanExamples") == false);

        assertEquals("someDefault", job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringDefault"));
        assertEquals("someConst", job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringConst"));
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringEnum").contains("someEnum")
                || job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringEnum").contains("someEnum2"));
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringExamples").contains("someExample")
                || job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getString("stringExamples").contains("someExample2"));

        assertTrue(1 == job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerDefault"));
        assertTrue(2 == job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerConst"));
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerEnum") == 3
                || job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerEnum") == 4);
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerExamples") == 5
                || job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getInt("integerExamples") == 6);

        assertEquals(25.1, job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberDefault"));
        assertEquals(25.2, job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberConst"));
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberEnum") == 25.3||
                job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberEnum") == 25.4);
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberExamples") == 25.5 ||
                job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getDouble("numberExamples") == 25.6);

        assertTrue(true == job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getBoolean("booleanDefault"));
        assertTrue(false == job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getBoolean("booleanConst"));
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getBoolean("booleanEnum") == true);
        assertTrue(job.getJsonArray("arrayWithDefaults").get(0).asJsonObject().getBoolean("booleanExamples") == false);
    }

    @Test
    public void testLimitedNumber() throws Exception {
        JsonGeneratorConfig gConf = new JsonGeneratorConfig();
        gConf.globalIntegerMin = 300;
        gConf.globalIntegerMax = 400;

        JsonGenerator g = new JsonGenerator(schema, gConf);
        JsonElement el = g.generate();
        JsonObject job = el.asJsonObject();
        System.out.println(job.toString());
        assertTrue(job.getInt("five") >= 300);
        assertTrue(job.getInt("five") <= 400);
    }

    @Test
    public void testEmailTypeString() {
        JsonObject job = new JsonGenerator(schema, null).generate().asJsonObject();
        System.out.println(job.toString());
        String emailString = job.get("eight").toString();
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{1,10}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(emailString);
        assertTrue(matcher.find());
    }

    @Test
    public void testEmailTypeStringLimitedLocal() {
        JsonGeneratorConfig gConf = new JsonGeneratorConfig();
        gConf.globalEmailLocalPartLengthMin = 5;
        gConf.globalEmailLocalPartLengthMax = 5;
        JsonObject job = new JsonGenerator(schema, gConf).generate().asJsonObject();
        System.out.println(job.toString());
        String emailString = job.get("eight").toString();
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{1,10}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(emailString);
        assertTrue(matcher.find());
        assertTrue(emailString.split("@")[0].length() >= 5);
        assertTrue(emailString.split("@")[0].length() <= 5);

    }
}
