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

package org.djodjo.json.generator.generators;

import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.generator.Generator;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;
import org.djodjo.json.schema.SchemaV4;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isBooleanType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isEnum;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isLimitedNumber;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isNumberType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isObjectType;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isRangeObject;
import static org.djodjo.json.generator.matcher.SchemaDefMatchers.isStringType;

public class ObjectGenerator extends Generator {

    private static LinkedTreeMap<Matcher<org.djodjo.json.schema.Schema>, Class> commonPropertyMatchers;

    //TODO implement custom matchers
    static {
        commonPropertyMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        commonPropertyMatchers.put(isEnum(), EnumGenerator.class);
        commonPropertyMatchers.put(isStringType(), StringGenerator.class);
        commonPropertyMatchers.put(isBooleanType(), BooleanGenerator.class);
        commonPropertyMatchers.put(isLimitedNumber(), LimitedNumberGenerator.class);
        commonPropertyMatchers.put(isNumberType(), NumberGenerator.class);
        commonPropertyMatchers.put(isRangeObject(), RangeGenerator.class);
        commonPropertyMatchers.put(isObjectType(), ObjectGenerator.class);
    }

    public ObjectGenerator(SchemaV4 schema) {
        super(schema);
    }

    public JsonObject generate() {
        JsonObject res = new JsonObject();
        SchemaMap props = schema.getProperties();
        JsonElement  newEl;
        if(props!=null) {
            for(Map.Entry<String,Schema> propItem : props) {
                Schema propertySchema = propItem.getValue();
                for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                    if (entry.getKey().matches(propertySchema)) {
                        try {
                            Generator gen = (Generator)entry.getValue().getDeclaredConstructor(SchemaV4.class).newInstance(propertySchema);
                            newEl = gen.generate();
                            if(newEl != null)
                                res.put(propItem.getKey(),newEl);
                            break;
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (JsonException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return res;
    }
}
