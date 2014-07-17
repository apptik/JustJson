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

import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.generator.Generator;
import org.djodjo.json.generator.GeneratorConfig;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaList;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ArrayGenerator extends Generator {

    public ArrayGenerator(Schema schema, GeneratorConfig configuration) {
        super(schema, configuration);
    }

    public ArrayGenerator(Schema schema, GeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    public JsonArray generate() {
        JsonArray res = new JsonArray();
        SchemaList items = schema.getItems();
        JsonElement  newEl;
        int minItems = schema.getMinItems();
        int maxItems = schema.getMaxItems();

        if(configuration!=null) {
            if (configuration.globalArrayItemsMin!=null) minItems = configuration.globalArrayItemsMin;
            if (configuration.globalArrayItemsMax!=null) maxItems = configuration.globalArrayItemsMax;
            if (propertyName != null ) {
                if (configuration.arrayItemsMin.get(propertyName)!=null) minItems = configuration.arrayItemsMin.get(propertyName);
                if (configuration.arrayItemsMax.get(propertyName)!=null) maxItems = configuration.arrayItemsMax.get(propertyName);

            }
        }
        maxItems = minItems + rnd.nextInt(maxItems-minItems);


        //meant for JSON generator after all, not OutOfMemory generator :)
        if(minItems>500) minItems = 500;
        if(maxItems>500) maxItems = 500;

        int cnt = 0;

        if(configuration.arrayPredefinedItems != null && propertyName!=null && configuration.arrayPredefinedItems.get(propertyName)!=null) {
            for(JsonElement je:configuration.arrayPredefinedItems.get(propertyName)) {
                res.put(je);
                if(++cnt>maxItems) break;
            }
        }
        else if(items!=null && items.size()>0) {
            //if we have array with
            if (items.size() == 1) {
                for(int i =0;i<maxItems;i++) {
                    for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                        if (entry.getKey().matches(items.get(0))) {
                            try {
                                Generator gen = (Generator) entry.getValue().getDeclaredConstructor(Schema.class, GeneratorConfig.class, String.class).newInstance(items.get(0), configuration, propertyName);
                                newEl = gen.generate();
                                if (newEl != null) {
                                    res.put(newEl);
                                    break;
                                }
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {

                for (Schema itemSchema : items) {
                    for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                        if (entry.getKey().matches(itemSchema)) {
                            try {
                                Generator gen = (Generator) entry.getValue().getDeclaredConstructor(Schema.class, GeneratorConfig.class).newInstance(itemSchema, configuration);
                                newEl = gen.generate();
                                if (newEl != null) {
                                    res.put(newEl);
                                    break;
                                }
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(++cnt>maxItems) break;
                }
            }
        } else {
            //then items can be any
            throw new NotImplementedException();
        }

        return res;
    }
}
