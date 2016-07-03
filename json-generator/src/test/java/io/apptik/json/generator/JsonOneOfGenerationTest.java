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

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class JsonOneOfGenerationTest {
    SchemaV4 schema;

    @Before
    public void setUp() throws Exception {
        schema =  new SchemaV4().wrap(JsonElement.readFrom(
                "{" +
                        "\"type\" : \"object\"," +
                        "\"oneOf\" :  [" +
                        "{" +
                        "\"type\" : \"object\"," +
                        "\"oneOf\" :  [" +
"{\n" +


                "\"type\" : \"object\"," +
                "\"properties\" : {" +
                "\"one\" : {\"type\" : \"number\"  } ," +
                "\"two\" : {\"type\" : \"string\" }," +
                "\"three\" : " + "{" +
                    "\"type\" : \"object\"," +
                    "\"properties\" : {" +
                    "\"one\" : {\"type\" : \"number\"  } ," +
                    "\"two\" : {\"type\" : \"string\" }" +
                    "}" +
                    "},"+
                "\"four\" : {\"type\" : \"boolean\"  }," +
                "\"five\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," +
                "\"six\" : {\"enum\" : [\"one\", 2, 3.5, true, [\"almost empty aray\"], {\"one-item\":\"object\"}, null]  }, " +
                "\"seven\" : {\"type\" : \"string\", \"format\": \"uri\" }" +
        "}" +
                        "}]" +
                        "}]" +
                "}"));
    }

    @Test
    public void testGenerateOneOf() throws Exception {
        JsonGeneratorConfig gConf = new JsonGeneratorConfig();
        ArrayList<String> images =  new ArrayList<String>();
        images.add("/photos/image.jpg");
        images.add("/photos/image.jpg");

        gConf.uriPaths.put("seven", images);
        // gConf.globalUriPaths = images;
        JsonObject job = new JsonGenerator(schema, gConf).generate().asJsonObject();

        System.out.println(job.toString());

        assertEquals(7,job.length());
    }


}
