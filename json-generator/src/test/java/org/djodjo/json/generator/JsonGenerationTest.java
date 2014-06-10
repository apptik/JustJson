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

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class JsonGenerationTest {
    SchemaV4 schema;

    @Before
    public void setUp() throws Exception {
        schema =  new SchemaV4().wrap(JsonElement.readFrom("{" +
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
                    "}"+
                "}" +
                "}"));
    }

    @Test
    public void testGenerate() throws Exception {

        JsonObject job = new Generator(schema).generate().asJsonObject();

        System.out.println(job.toString());
        assertEquals(3,job.length());

    }


}
