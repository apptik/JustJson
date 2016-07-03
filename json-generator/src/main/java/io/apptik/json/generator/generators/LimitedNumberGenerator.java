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

public class LimitedNumberGenerator extends JsonGenerator {
    public LimitedNumberGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema,configuration);
    }

    public LimitedNumberGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {

        double mustMin;
        double mustMax;
        double minValue = mustMin = (schema.getMinimum()!=null)?schema.getMinimum():0;
        double maxValue = mustMax = (schema.getMaximum()!=null)?schema.getMaximum():Integer.MAX_VALUE;


        if(configuration!=null) {
            if (configuration.globalIntegerMin!=null && mustMin<configuration.globalIntegerMin) {
                minValue = configuration.globalIntegerMin;
            }
            if (configuration.globalIntegerMax!=null && mustMax > configuration.globalIntegerMax){
                maxValue = configuration.globalIntegerMax;
            }
            if (propertyName != null ) {
                if (configuration.integerMin.get(propertyName)!=null && mustMin< configuration.integerMin.get(propertyName)){
                    minValue = configuration.integerMin.get(propertyName);
                }
                if (configuration.integerMax.get(propertyName)!=null && mustMax>configuration.integerMax.get(propertyName)){
                    maxValue = configuration.integerMax.get(propertyName);
                }
            }

        }


        return new JsonNumber((int)minValue + rnd.nextInt((int) (maxValue-minValue)));
    }
}
