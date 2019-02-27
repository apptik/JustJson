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

import io.apptik.json.JsonBoolean;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonNumber;
import io.apptik.json.JsonString;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.schema.Schema;

import java.util.Random;

public class BooleanGenerator extends JsonGenerator {

    public BooleanGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public BooleanGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        String defaultValue = schema.getDefault();

        //Check for generic default types
        if (schema.getDefaultBoolean() != null) {
            return new JsonBoolean(schema.getDefaultBoolean());
        }
        if (schema.getConstBoolean() != null) {
            return new JsonBoolean(schema.getConstBoolean());
        }
        if (schema.getExamples() != null && schema.getExamples().length() > 0) {
            return checkIfDefaultIsABoolean(schema.getExamples().get(new Random().nextInt(schema.getExamples().length())).toString());
        }
        if (schema.getEnum() != null && schema.getEnum().length() > 0) {
            return checkIfDefaultIsABoolean(schema.getEnum().get(new Random().nextInt(schema.getEnum().length() -1)).toString());
        }
        
        if (defaultValue != null && !defaultValue.isEmpty()) {
            if(!defaultValue.equalsIgnoreCase("true") && !defaultValue.equalsIgnoreCase("false")) {
                throw new RuntimeException("Default value: " + defaultValue + " of key: " + propertyName + ", is not a boolean type");
            } else {
                return (new JsonBoolean(Boolean.parseBoolean(defaultValue)));
            }
        } else {
            return new JsonBoolean(rnd.nextBoolean());
        }
    }

    private JsonBoolean checkIfDefaultIsABoolean(String defaultValue) {
        if(!defaultValue.equalsIgnoreCase("true") && !defaultValue.equalsIgnoreCase("false")) {
            throw new RuntimeException("Default value: " + defaultValue + " of key: " + propertyName + ", is not a boolean type");
        } else {
            return (new JsonBoolean(Boolean.parseBoolean(defaultValue)));
        }
    }
}
