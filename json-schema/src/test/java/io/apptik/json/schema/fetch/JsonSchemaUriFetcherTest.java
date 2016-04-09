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

package io.apptik.json.schema.fetch;


import io.apptik.json.JsonElement;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;
import io.apptik.json.wrapper.JsonObjectWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class JsonSchemaUriFetcherTest {
    Schema schema;
    private URI schemaUri;
    Schema schemaGeo;

    @Before
    public void setUp() throws Exception {
        schema =  new SchemaV4();
        schemaGeo = new SchemaV4().wrap(JsonElement.readFrom("{\"description\":\"A geographical coordinate\",\"type\":\"object\",\"properties\":{\"latitude\":{\"type\":\"number\"},\"longitude\":{\"type\":\"number\"}}}"));
        schemaUri = URI.create("http://json-schema.org/geo");
    }

   //@Test
    public void testFetch() throws Exception {

        SchemaUriFetcher suf =  new SchemaUriFetcher();

        System.out.println(schemaUri.getPath());

        Schema schema= suf.fetch(schemaUri, null, null);

        System.out.println(schema.toString());

        assertEquals(schemaGeo.getJson(), schema.getJson());

    }


    //@Test
    public void testFetchFromObjectWrapper() throws Exception {
        JsonObjectWrapper job;


        Schema schemaGeo = new SchemaV4().wrap(JsonElement.readFrom("{\"description\":\"A geographical coordinate\",\"type\":\"object\",\"properties\":{\"latitude\":{\"type\":\"number\"},\"longitude\":{\"type\":\"number\"}}}"));
        job = (JsonObjectWrapper) new JsonObjectWrapper().setJsonSchemaUri(schemaUri);


        assertEquals(schemaGeo.getJson(), job.getJsonSchema().getJson());

    }

    //@Test
    public void testFetchWithConfig() throws Exception {

        final URI schemaUri2 = URI.create("ftp://some.addr/test123/geo");

        SchemaUriFetcher suf = (SchemaUriFetcher) new SchemaUriFetcher().withConfig(
                new SchemaFetcherConfig()
                        .withUriSchemeReplacement("ftp",schemaUri.getScheme())
                        .withUriAuthorityReplacement("some.addr", schemaUri.getAuthority())
                        .withUriPathReplacement("/test123/geo", schemaUri.getPath())
        );

        System.out.println("Original URI:" + schemaUri2);
        System.out.println("Resolved URI:" + suf.convertUri(schemaUri2));

        assertEquals(schemaUri, suf.convertUri(schemaUri2));

        Schema schema= suf.fetch(schemaUri2, null, null);

        System.out.println(schema.toString());

        assertEquals(schemaGeo.getJson(), schema.getJson());

    }


    public void testFetchFromObjectWrapperWithConfig() throws Exception {
        JsonObjectWrapper job;
        final URI schemaUri2 = URI.create("ftp://some.addr/test123/geo");

        Schema schemaGeo = new SchemaV4().wrap(JsonElement.readFrom("{\"description\":\"A geographical coordinate\",\"type\":\"object\",\"properties\":{\"latitude\":{\"type\":\"number\"},\"longitude\":{\"type\":\"number\"}}}"));
        job = new JsonObjectWrapper();
        job.getDefaultSchemaFetcher().withConfig(
                new SchemaFetcherConfig()
                        .withUriSchemeReplacement("ftp", schemaUri.getScheme())
                        .withUriAuthorityReplacement("some.addr", schemaUri.getAuthority())
                        .withUriPathReplacement("/test123/geo", schemaUri.getPath())
        );
        job.setJsonSchemaUri(schemaUri2);

        assertEquals(schemaGeo.getJson(), job.getJsonSchema().getJson());

    }

    @Test
    public void testFetchWithResolving() throws Exception {
        JsonObjectWrapper job;
        final URI schemaUri2 = URI.create("http://openthings.cc/rest/schema/xyz");
        //job = (JsonObjectWrapper) new JsonObjectWrapper().setJsonSchemaUri(schemaUri2);

        SchemaUriFetcher suf =  new SchemaUriFetcher();
        Schema schema= suf.fetch(schemaUri2, null, null);
        //TODO
        //System.out.println(schema.mergeAllRefs().toString());

    }


}
