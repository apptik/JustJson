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
import org.djodjo.json.JsonObject;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.djodjo.json.schema.SchemaV5;
import org.djodjo.json.util.LinkedTreeMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


//TODO
public class SchemaUriFetcher implements SchemaFetcher {

    private LinkedTreeMap<String, String> replacements = new LinkedTreeMap<String, String>();

    public URI resolveUri(URI schemaUri) {
        URI res = schemaUri;

        return res;
    }

    @Override
    public Schema fetch(URI schemaUri) {
        Schema res = null;
        try {
            String fragment = schemaUri.getFragment();
            JsonObject schemaJson = JsonElement.readFrom(new InputStreamReader(schemaUri.toURL().openStream())).asJsonObject();
            if(fragment!=null && !fragment.trim().isEmpty()) {
                String[] pointers = fragment.split("/");
                for (String pointer : pointers) {
                    if (pointer != null && !pointer.trim().isEmpty()) {
                        schemaJson = schemaJson.getJsonObject(pointer);
                    }
                }
            }

            String version = schemaJson.optString("$schema","");
            if(version.equals(Schema.VER_5)) {
                res = new SchemaV5().setSchemaFetcher(this).wrap(schemaJson);
            } else if(version.equals(Schema.VER_4)) {
                res = new SchemaV4().setSchemaFetcher(this).wrap(schemaJson);
            } else {
                res = new SchemaV4().setSchemaFetcher(this).wrap(schemaJson);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return res;
    }
}
