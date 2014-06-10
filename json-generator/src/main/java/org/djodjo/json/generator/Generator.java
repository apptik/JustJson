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
import org.djodjo.json.generator.generators.ObjectGenerator;
import org.djodjo.json.schema.SchemaV4;

public class Generator {

    protected SchemaV4 schema;

    public Generator(SchemaV4 schema) {
        this.schema = schema;
    }

    public JsonElement generate() {
        if(schema.getType().equals(SchemaV4.TYPE_ARRAY)) throw new UnsupportedOperationException();

        return new ObjectGenerator(schema).generate();
    }
}
