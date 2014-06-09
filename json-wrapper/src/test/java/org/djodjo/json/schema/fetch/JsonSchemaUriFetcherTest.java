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

package org.djodjo.json.schema.fetch;


import org.djodjo.json.JsonElement;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class JsonSchemaUriFetcherTest {
    Schema schema;

    @Before
    public void setUp() throws Exception {
        schema =  new SchemaV4();
    }

    @Test
    public void testFetch() throws Exception {

        Schema schemaGeo = new SchemaV4().wrap(JsonElement.readFrom("{\"description\":\"A geographical coordinate\",\"type\":\"object\",\"properties\":{\"latitude\":{\"type\":\"number\"},\"longitude\":{\"type\":\"number\"}}}"));
       SchemaUriFetcher suf =  new SchemaUriFetcher();

        final URI schemaUri = URI.create("http://json-schema.org/geo");

        System.out.println(schemaUri.getPath());

        Schema schema= suf.fetch(schemaUri);

        System.out.println(schema.toString());

        assertEquals(schemaGeo.getJson(), schema.getJson());

    }


}
