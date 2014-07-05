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

package org.djodjo.json.schema;

import org.djodjo.json.JsonObject;

import java.net.URI;

/**
 * New keyword is "extends" which is used as "merge" but with easier syntax. es:
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
 */
public class SchemaJJv1 extends SchemaV5 {

    public  SchemaJJv1() {
        super();
        setSchema("http://openthings.cc/schema/schema-jj-01");
    }

    @Override
    public Schema getEmptySchema() {
        return new SchemaJJv1().setSchemaFetcher(schemaFetcher);
    }

    protected Schema setSuperSchema(URI superSchema) {
        getJson().put("extends", getSchemaFetcher().fetch(superSchema));
        return this;
    }

    protected Schema setSuperSchemaRef(String superSchemaUri) {
        getJson().put("extends", new JsonObject().put("$ref", superSchemaUri));
        return this;
    }

    protected Schema setExtends(Schema superSchema) {
        getJson().put("extends", superSchema);
        return this;
    }

    public String getExtends() {
        return getJson().optString("extends","");
    }


}
