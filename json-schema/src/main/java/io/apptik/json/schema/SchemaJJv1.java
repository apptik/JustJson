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

package io.apptik.json.schema;

import io.apptik.json.JsonArray;
import io.apptik.json.JsonObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ABSOLUTELY EXPERIMENTAL
 *
 * New keyword is "extends" which is used as "merge" (v5 proposal) but with easier syntax. es:
 * {
 "merge": {
 "source": {
 "type": "object",
 "properties": { "p": { "type": "string" } },
 "additionalProperties": false
 },
 "with": {
 "properties": { "q": { "enum": [ null ] } }
 }
 }
 }
 *
 * equals to
 * {
 *     "extends": {
 *          "type": "object",
 *          "properties": { "p": { "type": "string" } },
 *          "additionalProperties": false
 *          },
 *     "properties": { "q": { "enum": [ null ] } }
 * }
 *
 * This is really powerful when used with $ref
 *
 *
 * "equivalent keyword is used here to provide list of links to equivalent schemas/rdf classes"
 */
public class SchemaJJv1 extends SchemaV4 {

    public  SchemaJJv1() {
        super();
        setSchema("http://openthings.cc/schema/schema-jj-01");
    }

    @Override
    public Schema getEmptySchema(String path) {
        return new SchemaJJv1().setSchemaFetcher(schemaFetcher).setOrigSrc(this.origSrc.resolve(path));
    }

    public Schema setSuperSchema(URI superSchema) {
        getJson().put("extends", getSchemaFetcher().fetch(superSchema, null,null));
        return this;
    }

    public  <O extends SchemaJJv1> O setSuperSchemaRef(String superSchemaUri) {
        getJson().put("extends", new JsonObject().put("$ref", superSchemaUri));
        return (O)this;
    }

    public <O extends SchemaJJv1> O setExtends(Schema superSchema) {
        getJson().put("extends", superSchema);
        return (O)this;
    }

    public Schema getExtends() {
        return (Schema)getEmptySchema("extends").wrap(getJson().optJsonObject("extends"));
    }


    public <O extends SchemaJJv1> O addEquivalent(String equivalent) {
        JsonArray equivArray = getJson().optJsonArray("equivalent");
        if(equivArray==null) getJson().put("equivalent", new JsonArray());
        getJson().getJsonArray("equivalent").put(equivalent);
        return (O)this;
    }

    public <O extends SchemaJJv1> O addEquivalentList(Collection<String> equivalents) {
        JsonArray equivArray = getJson().optJsonArray("equivalent");
        if(equivArray==null) getJson().put("equivalent", equivalents);
        return (O)this;
    }

    public ArrayList<String> getEquivalents() {
        JsonArray equivArray = getJson().optJsonArray("equivalent");
        if(equivArray==null) return null;
        return equivArray.toArrayList();
    }
}
