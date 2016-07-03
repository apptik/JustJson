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
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;

public class NumberGenerator extends JsonGenerator {
    public NumberGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public NumberGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {

        int minValue = 0;
        int maxValue = Integer.MAX_VALUE;
        if(configuration!=null) {
            if (configuration.globalNumberMin!=null) minValue = configuration.globalNumberMin;
            if (configuration.globalNumberMax!=null) maxValue = configuration.globalNumberMax;
            if (propertyName != null ) {
                if (configuration.numberMin.get(propertyName)!=null) minValue = configuration.numberMin.get(propertyName);
                if (configuration.numberMax.get(propertyName)!=null) maxValue = configuration.numberMax.get(propertyName);
            }

        }

        return new JsonNumber(minValue + rnd.nextInt(maxValue-minValue) + Math.abs(rnd.nextDouble()));
    }
}
