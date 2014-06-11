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

package org.djodjo.json.generator;


import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.schema.SchemaV4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class JsonGenerationTest {
    SchemaV4 schema;

    @Before
    public void setUp() throws Exception {
        schema =  new SchemaV4().wrap(JsonElement.readFrom("{" +
                "    \"type\": \"object\",\n" +
                "    \"properties\": {\n" +
                "       \"pubdate\": {\n" +
                "            \"description\": \"the publication date of the property. This is a date in ISO 8601 format of YYYY-MM-DDThh:mm:ssZ in UTC time. ex: 2014-01-27T00:40:57.165Z\",\n" +
                "            \"type\": \"string\",\n" +
                "            \"format\": \"date-time\"\n" +
                "        },\n" +
                "       \"adType\" : {\n" +
                "            \"enum\": [\"PLUS\", \"PREMIUM\", \"NORMAL\"]\n" +
                "       },\n" +
                "       \"photos\": {\n" +
                "            \"description\": \"list of photos absolute links\",\n" +
                "            \"type\": \"array\",\n" +
                "                \"items\": { \"type\": \"string\", \"format\":\"uri\" },\n" +
                "                \"uniqueItems\": true" +
                "        },\n" +
                "        \"info\": {\n" +
                "                            \"type\": \"object\",\n" +
                "                \"required\": [\"idestate\", \"subtype\", \"price\", \"location\"],\n" +
                "                \"properties\": {\n" +
                "                    \"idestate\":  { \"type\" : \"integer\" },\n" +
                "                    \"buyRent\" : {\"enum\" : [\"RENT\", \"BUY\"]},\n" +
                "             \"mainType\" : {\n" +
                "             \"title\" : \"Type\",\n" +
                "             \"enum\" : [\"HOUSE\", \"APARTMENT\", \"GARAGE\", \"OTHER\"]\n" +
                "              },\n" +
                "                    \"subtype\": {\n" +
                "                        \"description\": \"ex: villa\",\n" +
                "                        \"type\": \"string\"\n" +
                "                    },\n" +
                "                    \"rating\": { \"type\": \"boolean\" },\n" +
                //"                    \"price\": { \"$ref\": \"#/definitions/price\" },\n" +
                //"                    \"location\": { \"$ref\": \"#/definitions/location\" },\n" +
                "                    \"rooms\": { \"type\" : \"integer\" },\n" +
                "                    \"m2\": { \"type\" : \"integer\" },\n" +
                "                    \"availability\": { \"type\": \"string\" ,\n" +
                "                                        \"format\": \"date-time\"\n}" +
                "                }" +
                "        },\n" +
                "        \"flags\": {\n" +
                "            \"description\": \"list of string flags to be displayed\",\n" +
                "             \"properties\" : {\n" +
                "                \"publicSale\": { \"type\" : \"boolean\" },\n" +
                "                \"notaries\": { \"type\" : \"boolean\" },\n" +
                "                \"new\": { \"type\" : \"boolean\" },\n" +
                "                \"newPrice\": { \"type\" : \"boolean\" },\n" +
                "                \"annuity\": { \"type\" : \"boolean\" },\n" +
                "                \"newProject\": { \"type\" : \"integer\" },\n" +
                "                \"passiveHouse\": { \"type\" : \"boolean\" },\n" +
                "                \"lowEnergy\": { \"type\" : \"boolean\" },\n" +
                "                \"propertyId\": { \"type\" : \"boolean\" }\n" +
                "            }" +
                "        }\n" +
                "    }" +

//                "\"type\" : \"object\"," +
//                "\"properties\" : {" +
//                "\"one\" : {\"type\" : \"number\"  } ," +
//                "\"two\" : {\"type\" : \"string\" }," +
//                "\"three\" : " + "{" +
//                    "\"type\" : \"object\"," +
//                    "\"properties\" : {" +
//                    "\"one\" : {\"type\" : \"number\"  } ," +
//                    "\"two\" : {\"type\" : \"string\" }" +
//                    "}" +
//                    "},"+
//                "\"four\" : {\"type\" : \"boolean\"  }," +
//                "\"five\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," +
//                "\"six\" : {\"enum\" : [\"one\", 2, 3.5, true, [\"almost empty aray\"], {\"one-item\":\"object\"}, null]  }" +
//                "}" +
                "}"));
    }

    @Test
    public void testGenerate() throws Exception {

        GeneratorConfig gConf = new GeneratorConfig();
        ArrayList<String> subtypes =  new ArrayList<String>();
        subtypes.add("House");
        subtypes.add("Apartment block");
        subtypes.add("Villa");
        subtypes.add("Castle");
        subtypes.add("Manor house");
        subtypes.add("Garage box");
        subtypes.add("Town house");
        subtypes.add("Mixed-use building");
        subtypes.add("Farmhouse");
        subtypes.add("Bungalow");
        subtypes.add("Exceptional property");
        subtypes.add("Parking");
        subtypes.add("Mansion");
        subtypes.add("Pavillon");
        subtypes.add("Country cottage");
        subtypes.add("Chalet");
        subtypes.add("Other property");
        gConf.stringPredefinedValues.put("subtype", subtypes);
        gConf.arrayItemsMax.put("photos", 5);
        gConf.integerMax.put("rooms", 5);
        gConf.integerMax.put("m2", 300);
        gConf.globalUriHosts.add("djodjo.org");
        gConf.globalUriSchemes.add("http");
        gConf.globalUriPathsLengthMax = 10;
        JsonObject job = new Generator(schema, gConf).generate().asJsonObject();

        System.out.println(job.toString());
        assertEquals(6,job.length());

    }


}
