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
import io.apptik.json.JsonObject;
import io.apptik.json.exception.JsonException;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class SchemaUriFetcher implements SchemaFetcher {

    private SchemaFetcherConfig cfg;


    //TODO use config to set to use id instead of orig source
    public URI resolveUri(URI targetUri, URI srcOrigUri, URI srcId) {
        if(targetUri.isAbsolute()) {
            return targetUri;
        } else if(srcOrigUri==null || !srcOrigUri.isAbsolute()) {
            return targetUri;
        } else {
            return srcOrigUri.resolve(targetUri);
        }
    }

    public URI convertUri(URI schemaUri) {
        if(cfg==null) return  schemaUri;
        URI res = null;

        String scheme = schemaUri.getScheme();
        String authority = schemaUri.getAuthority();
        String path = schemaUri.getPath();
        String query = schemaUri.getQuery();

        if(cfg.uriSchemeReplacements.containsKey(scheme)) {
            scheme = cfg.uriSchemeReplacements.get(scheme);
        }

        if(cfg.uriAuthorityReplacements.containsKey(authority)) {
            authority = cfg.uriAuthorityReplacements.get(authority);
        }

        if(cfg.uriPathReplacements.containsKey(path)) {
            path = cfg.uriPathReplacements.get(path);
        }

        if(cfg.uriQueryReplacements.containsKey(query)) {
            query = cfg.uriQueryReplacements.get(query);
        }

        try {
            res =  new URI(scheme, authority, path, query, schemaUri.getFragment());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return res;
    }


    @Override
    public Schema fetch(URI targetUri) {
        return fetch(targetUri, null, null);
    }

    //accepts only absolute URI or converted absolute URI
    @Override
    public Schema fetch(URI targetUri, URI srcOrigUri, URI srcId) {
        Schema res = null;

        URI schemaUri = convertUri(resolveUri(targetUri, srcOrigUri, srcId));
        if(!schemaUri.isAbsolute()) throw new RuntimeException("Json Schema Fetcher works only with absolute URIs");
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
            if(version.equals(Schema.VER_4)) {
                res = new SchemaV4().setSchemaFetcher(this).setOrigSrc(schemaUri).wrap(schemaJson);
            } else {
                res = new SchemaV4().setSchemaFetcher(this).setOrigSrc(schemaUri).wrap(schemaJson);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public SchemaFetcher withConfig(SchemaFetcherConfig cfg) {
        this.cfg = cfg;
        return this;
    }
}
