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
import io.apptik.json.exception.JsonException;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;


//TODO
public class SchemaResourceFetcher implements SchemaFetcher {

    @Override
    public Schema fetch(URI targetUri, URI srcOrigUri, URI srcId) {
        Schema res = new SchemaV4();
        final String resource = targetUri.getPath();

        URL url = getClass().getClassLoader().getResource(resource);
        System.out.println("Fetching res (simpple):" + resource);
        System.out.println("Fetching res (full):" + url);
        try {

            res.wrap(JsonElement.readFrom(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resource))).asJsonObject());

        } catch (JsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }

    @Override
    public SchemaFetcher withConfig(SchemaFetcherConfig cfg) {
        return null;
    }

    @Override
    public Schema fetch(URI targetUri) {
        return fetch(targetUri, null, null);
    }
}
