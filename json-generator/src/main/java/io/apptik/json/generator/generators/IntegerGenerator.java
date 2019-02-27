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
import io.apptik.json.JsonNumber;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.schema.Schema;

import java.util.Random;

public class IntegerGenerator extends JsonGenerator {
    public IntegerGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public IntegerGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        int minValue = 0;
        int maxValue = Integer.MAX_VALUE;

        //Check for generic default types
        if (schema.getDefaultInt() != null) {
            return new JsonNumber(schema.getDefaultInt());
        }
        if (schema.getConstInt() != null) {
            return new JsonNumber(schema.getConstInt());
        }
        if (schema.getExamples() != null && schema.getExamples().length() > 0) {
            return checkIfDefaultIsAInteger(schema.getExamples().get(new Random().nextInt(schema.getExamples().length())).toString());
        }
        if (schema.getEnum() != null && schema.getEnum().length() > 0) {
            return checkIfDefaultIsAInteger(schema.getEnum().get(new Random().nextInt(schema.getEnum().length() -1)).toString());
        }
        
        if(configuration!=null) {
            if (configuration.globalIntegerMin!=null) minValue = configuration.globalIntegerMin;
            if (configuration.globalIntegerMax!=null) maxValue = configuration.globalIntegerMax;
            if (propertyName != null ) {
                if (configuration.integerMin.get(propertyName)!=null) minValue = configuration.integerMin.get(propertyName);
                if (configuration.integerMax.get(propertyName)!=null) maxValue = configuration.integerMax.get(propertyName);
            }

        }

        return new JsonNumber(minValue + rnd.nextInt(maxValue-minValue));
    }

    private JsonNumber checkIfDefaultIsAInteger(String defaultValue) {
        try {
            return new JsonNumber(Integer.parseInt(defaultValue));
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Default value: " + defaultValue + " of key: " + propertyName + ", is not a integer type");
        }
    }
}
