/*
 * Copyright (C) 2015 Kalin Maldzhanski
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

package io.apptik.json.examples.jsongenerator;


import io.apptik.json.JsonElement;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;

import java.io.IOException;
import java.util.ArrayList;

public class JsonGeneratorExample {
    Schema schema1;

    public static void main(String[] args) {
        JsonGeneratorExample generator = new JsonGeneratorExample();
                Schema arraySchema = null;
        try {
            arraySchema =  new SchemaV4().wrap(JsonElement.readFrom("{\"type\" : \"array\",\"items\" : {\"type\": \"object\",\"required\" : [\"name\"],\"properties\": {\"name\": {\"type\": \"string\"},\"birthday\": {\"type\": \"integer\",\"format\": \"int32\"}}}}").asJsonObject());
        } catch (Exception e) {}
        System.out.println(generator.generateNoSettings(arraySchema).toString());
        System.out.println("generation without settings");
        System.out.println(generator.generateNoSettings(generator.schema1).toString());
        System.out.println("generation with settings");
        System.out.println(generator.generateWithSettings(generator.schema1).toString());

    }

    public JsonGeneratorExample() {
        try {
            schema1 =  new SchemaV4().wrap(JsonElement.readFrom(
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
                            "\"five1\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," +
                            "\"five2\" : {\"type\" : \"integer\"}," +
                            "\"six\" : {\"enum\" : [\"one\", 2, 3.5, true, [\"almost empty aray\"], {\"one-item\":\"object\"}, null]  }, " +
                            "\"seven\" : {\"type\" : \"string\", \"format\": \"uri\" }" +
                            "}" +
                            "}]" +
                            "}]" +
                            "}").asJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement generateNoSettings(Schema schema) {
        return new JsonGenerator(schema, new JsonGeneratorConfig()).generate();
    }

    private JsonElement generateWithSettings(Schema schema) {
        JsonGeneratorConfig gConf = new JsonGeneratorConfig();
        ArrayList<String> images =  new ArrayList<String>();
        images.add("/photos/image.jpg");
        images.add("/photos/image.jpg");

        gConf.uriPaths.put("seven", images);

        gConf.globalArrayItemsMax = 7;

        gConf.globalIntegerMin = 0;
        gConf.globalIntegerMax = 201;
        //can still limit numbers as long as its still valid according to the schema
        gConf.integerMin.put("five1", 300);
        gConf.integerMax.put("five1", 400);
        gConf.skipObjectProperties.add("two");

        return new JsonGenerator(schema, gConf).generate();
    }
}
