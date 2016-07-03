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

package io.apptik.json.generator.generators;

import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.exception.JsonException;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaMap;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectGenerator extends JsonGenerator {

    public ObjectGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public ObjectGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    public JsonObject generate() {
        JsonObject res = new JsonObject();
        SchemaMap props = schema.getProperties();
        JsonElement  newEl;
        if(props!=null) {
            for(Map.Entry<String,Schema> propItem : props) {
                if (!this.configuration.skipObjectProperties.contains(propItem.getKey())) {
                    Schema propertySchema = propItem.getValue();
                    for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers.entrySet()) {
                        if (entry.getKey().matches(propertySchema)) {
                            try {
                                JsonGenerator gen = (JsonGenerator) entry.getValue().getDeclaredConstructor(Schema.class, JsonGeneratorConfig.class, String.class).newInstance(propertySchema, configuration, propItem.getKey());
                                newEl = gen.generate();
                                if (newEl != null) {
                                    res.put(propItem.getKey(), newEl);
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
                            } catch (JsonException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
}
