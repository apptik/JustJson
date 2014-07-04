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

package org.djodjo.json.schema.validation;


import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.Validator;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JsonSchemaV4Validation {
    Schema schema;
    JsonObject jsonObject;

    @Before
    public void setUp() throws Exception {
        //create an empty v4 schema http://json-schema.org/draft-04/schema#
        schema =  new SchemaV4();
        //create an object to validate
        jsonObject = JsonObject.readFrom("{ " +
                "\"obj\":{}," +
                "\"arr\":[]," +
                "\"int\":5," +
                "\"num\":6.5," +
                "\"str\":\"123456\"," +
                "\"bool\": true," +
                "\"null\": null" +
                "}").asJsonObject();
    }

    @Test
    public void testValidatesAll() throws Exception {
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }

    @Test
    public void testTypeObject() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"object\"}"));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }
    @Test
    public void testTypeArray() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"array\"}"));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }
    @Test
    public void testTypeString() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"string\"}"));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }
    @Test
    public void testTypeNumber() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"number\"}"));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }
    @Test
    public void testTypeInteger() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"integer\"}"));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }
    @Test
    public void testTypeBool() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"type\" : \"boolean\"}"));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("obj")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("arr")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("int")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("num")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("str")));
        assertTrue(schema.getDefaultValidator().isValid(jsonObject.get("bool")));
        assertFalse(schema.getDefaultValidator().isValid(jsonObject.get("null")));
    }

    @Test
    public void testProperties() throws Exception {
        schema.wrap(JsonElement.readFrom("{" +
                "\"type\" : \"object\"," +
                "\"properties\" : {" +
                "\"one\" : {\"type\" : \"string\"  } ," +
                "\"two\" : {\"type\" : \"object\" }" +
                "}" +
                "}"));
        Validator validator = schema.getDefaultValidator();
        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"," +
                "\"two\":[]" +
                "}")));

        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"two\":\"string is bad\"," +
                "\"one\":{}" +
                "}")));

        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"" +
                "}")));

        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"two\":{}" +
                "}")));

        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"," +
                "\"two\":{}" +
                "}")));
    }

    @Test
    public void testAdditionalProperties() throws Exception {
        schema.wrap(JsonElement.readFrom("{" +
                "\"additionalProperties\" : false," +
                "\"type\" : \"object\"," +
                "\"properties\" : {" +
                "\"one\" : {\"type\" : \"string\"  } ," +
                "\"two\" : {\"type\" : \"object\" }" +
                "}" +
                "}"));
        Validator validator = schema.getDefaultValidator();
        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"" +
                "}")));

        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"two\":{}" +
                "}")));

        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"," +
                "\"two\":{}" +
                "}")));

        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"," +
                "\"three\":{}" +
                "}")));

        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"two\":{}," +
                "\"three\":{}" +
                "}")));

        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":\"string is good\"," +
                "\"two\":{}," +
                "\"three\":{}" +
                "}")));
    }

    @Test
    public void testRequired() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"required\" : [\"other\"]}"));
        Validator validator = schema.getDefaultValidator();
        assertFalse(validator.isValid(jsonObject));
        schema.wrap(JsonElement.readFrom("{\"required\" : [\"obj\"]}"));
        validator = schema.getDefaultValidator();
        assertTrue(validator.isValid(jsonObject));
        schema.wrap(JsonElement.readFrom("{\"required\" : [\"obj\",\"another\"]}"));
        validator = schema.getDefaultValidator();
        assertFalse(validator.isValid(jsonObject));
        schema.wrap(JsonElement.readFrom("{\"required\" : [\"obj\",\"arr\"]}"));
        validator = schema.getDefaultValidator();
        assertTrue(validator.isValid(jsonObject));


    }

    @Test
    public void testEnum() throws Exception {
        schema.wrap(JsonElement.readFrom("{\"enum\" : [\"other\",1,true,null,{ \"one\": 1}, [1,2,3]]}"));
        Validator validator = schema.getDefaultValidator();

        JsonObject jobBad = JsonObject.readFrom("{ " +
                "\"obj\":{\"two\": 2}," +
                "\"arr\":[1,2,3,4]," +
                "\"num\":5," +
                "\"str\":\"123456\"," +
                "\"bool\": false" +
                "}").asJsonObject();

        JsonObject jobGood = JsonObject.readFrom("{ " +
                "\"obj\":{ \"one\": 1}," +
                "\"arr\":[1,2,3]," +
                "\"num\":1," +
                "\"str\":\"other\"," +
                "\"bool\": true," +
                "\"null\": null" +
                "}").asJsonObject();

        assertFalse(validator.isValid(jobBad.get("obj")));
        assertFalse(validator.isValid(jobBad.get("arr")));
        assertFalse(validator.isValid(jobBad.get("num")));
        assertFalse(validator.isValid(jobBad.get("str")));
        assertFalse(validator.isValid(jobBad.get("bool")));

        assertTrue(validator.isValid(jobGood.get("obj")));
        assertTrue(validator.isValid(jobGood.get("arr")));
        assertTrue(validator.isValid(jobGood.get("num")));
        assertTrue(validator.isValid(jobGood.get("str")));
        assertTrue(validator.isValid(jobGood.get("bool")));
        assertTrue(validator.isValid(jobGood.get("null")));
    }

    @Test
    public void testMultipleOf() throws Exception {
        schema.wrap(JsonElement.readFrom("{" +
                "\"type\" : \"object\"," +
                "\"properties\" : {" +
                "\"one\" : {\"type\" : \"number\"   ," +
                "\"multipleOf\" : 5 }" +
                "}" +
                "}"));
        Validator validator = schema.getDefaultValidator();
        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":1" +
                "}")));
        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":10" +
                "}")));
        schema.wrap(JsonElement.readFrom("{" +
                "\"type\" : \"object\"," +
                "\"properties\" : {" +
                "\"one\" : {\"type\" : \"number\"   ," +
                "\"multipleOf\" : 3.4 }" +
                "}" +
                "}"));
        validator = schema.getDefaultValidator();
        assertFalse(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":1" +
                "}")));
        assertTrue(validator.isValid(JsonElement.readFrom("{ " +
                "\"one\":6.8" +
                "}")));
    }

}
