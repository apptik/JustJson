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
import org.djodjo.json.generator.generators.ArrayGenerator;
import org.djodjo.json.generator.generators.BooleanGenerator;
import org.djodjo.json.generator.generators.EnumGenerator;
import org.djodjo.json.generator.generators.IntegerGenerator;
import org.djodjo.json.generator.generators.LimitedNumberGenerator;
import org.djodjo.json.generator.generators.NumberGenerator;
import org.djodjo.json.generator.generators.ObjectGenerator;
import org.djodjo.json.generator.generators.RangeGenerator;
import org.djodjo.json.generator.generators.StringGenerator;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.util.Random;

import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isArrayType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isBooleanType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isEnum;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isIntegerType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isLimitedNumber;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isNumberType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isObjectType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isRangeObject;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isStringType;

public class Generator {

    protected SchemaV4 schema;
    protected static Random rnd = new Random();
    protected static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;

    //TODO implement custom matchers
    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(isEnum(), EnumGenerator.class);
        commonPropertyMatchers.put(isStringType(), StringGenerator.class);
        commonPropertyMatchers.put(isBooleanType(), BooleanGenerator.class);
        commonPropertyMatchers.put(isLimitedNumber(), LimitedNumberGenerator.class);
        commonPropertyMatchers.put(isIntegerType(), IntegerGenerator.class);
        commonPropertyMatchers.put(isNumberType(), NumberGenerator.class);
        commonPropertyMatchers.put(isRangeObject(), RangeGenerator.class);
        commonPropertyMatchers.put(isObjectType(), ObjectGenerator.class);
        commonPropertyMatchers.put(isArrayType(), ArrayGenerator.class);
    }

    public Generator(SchemaV4 schema) {
        this.schema = schema;
    }

    public JsonElement generate() {
        if(schema.getType().equals(SchemaV4.TYPE_ARRAY)) throw new UnsupportedOperationException();

        return new ObjectGenerator(schema).generate();
    }
}
