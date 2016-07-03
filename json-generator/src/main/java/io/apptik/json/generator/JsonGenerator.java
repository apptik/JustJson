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

package io.apptik.json.generator;

import io.apptik.json.JsonElement;
import io.apptik.json.generator.matcher.SchemaCompositeMatchers;
import io.apptik.json.generator.matcher.SchemaDefMatchers;
import io.apptik.json.generator.generators.ArrayGenerator;
import io.apptik.json.generator.generators.BooleanGenerator;
import io.apptik.json.generator.generators.EnumGenerator;
import io.apptik.json.generator.generators.IntegerGenerator;
import io.apptik.json.generator.generators.LimitedNumberGenerator;
import io.apptik.json.generator.generators.NumberGenerator;
import io.apptik.json.generator.generators.ObjectGenerator;
import io.apptik.json.generator.generators.RangeGenerator;
import io.apptik.json.generator.generators.StringGenerator;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaList;
import io.apptik.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.util.Random;

import static io.apptik.json.JsonElement.*;

public class JsonGenerator {

    protected Schema schema;
    protected JsonGeneratorConfig configuration ;
    //valid for elements which parent is of type Object
    protected String propertyName;
    protected static Random rnd = new Random();
    protected static LinkedTreeMap<Matcher<Schema>, Class> commonPropertyMatchers;

    //TODO implement custom matchers
    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(SchemaDefMatchers.isEnum(), EnumGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isStringType(), StringGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isBooleanType(), BooleanGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isLimitedNumber(), LimitedNumberGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isIntegerType(), IntegerGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isNumberType(), NumberGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isRangeObject(), RangeGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isObjectType(), ObjectGenerator.class);
        commonPropertyMatchers.put(SchemaDefMatchers.isArrayType(), ArrayGenerator.class);

    }

    public JsonGenerator(Schema schema, JsonGeneratorConfig configuration) {
        this.schema = schema;
        this.configuration = configuration;
        if(this.configuration==null) {
            this.configuration=new JsonGeneratorConfig();
        }
        schema.mergeAllRefs();
        mergeComposites();

    }

    public JsonGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        this(schema, configuration);
        this.propertyName = propertyName;
        schema.mergeAllRefs();
        mergeComposites();
    }


    private void mergeComposites() {
        mergeOneOf();
        mergeAllOf();
        mergeAnyOf();
    }


    //TODO dirty and incorrect for overlaps
    private void mergeOneOf() {
        if (SchemaCompositeMatchers.hasOneOf().matches(schema)) {
            SchemaList list = schema.getOneOf();
            Schema choice = list.get(rnd.nextInt(list.size()));
            schema.getJson().remove("oneOf");
            schema.merge(choice);
        }
    }

    private void mergeAnyOf() {
        if(SchemaCompositeMatchers.hasAnyOf().matches(schema)) {
            SchemaList list =  schema.getAnyOf();
            Schema choice = list.get(rnd.nextInt(list.size()));
            schema.getJson().remove("anyOf");
            schema.merge(choice);
        }
    }

    private void mergeAllOf() {
        if (SchemaCompositeMatchers.hasAllOf().matches(schema)) {
            SchemaList list = schema.getAllOf();
            schema.getJson().remove("allOf");
            for (Schema subSchema : list) {
                schema.merge(subSchema);
            }
        }
    }


        //TODO make a choice for multi typed elements
    public JsonElement generate() {
        if(schema.getType().get(0).equals(TYPE_OBJECT)) {
            return new ObjectGenerator(schema, configuration).generate();
        }
        if(schema.getType().get(0).equals(TYPE_ARRAY)) {
            return new ArrayGenerator(schema, configuration).generate();
        }



        throw new UnsupportedOperationException("Use Main generator only for full valid JSON object or array.");
    }
}
