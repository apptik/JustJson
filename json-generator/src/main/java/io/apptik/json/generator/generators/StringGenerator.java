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
import io.apptik.json.JsonString;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.generator.generators.formats.*;
import io.apptik.json.schema.Schema;
import io.apptik.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static io.apptik.json.generator.matcher.FormatMatchers.*;

public class StringGenerator extends JsonGenerator {

    protected static LinkedTreeMap<Matcher<Schema>, Class> stringFormatMatchers;

    static {
        stringFormatMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        stringFormatMatchers.put(isDateTimeFormat(), DateTimeGenerator.class);
        stringFormatMatchers.put(isDateFormat(), DateGenerator.class);
        stringFormatMatchers.put(isTimeFormat(), TimeGenerator.class);
        stringFormatMatchers.put(isUriFormat(), UriGenerator.class);
        stringFormatMatchers.put(isEmailFormat(), EmailGenerator.class);
    }

    public StringGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public StringGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        int minChars = 0;
        int maxChars = 15;
        if(schema.getFormat()!=null) {
            for (Map.Entry<Matcher<Schema>, Class> entry : stringFormatMatchers.entrySet()) {
                if (entry.getKey().matches(schema)) {
                    JsonGenerator gen = null;

                    try {
                        gen = (JsonGenerator)entry.getValue().getDeclaredConstructor(Schema.class, JsonGeneratorConfig.class, String.class).newInstance(schema, configuration, propertyName);
                        return gen.generate();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        }
        if(configuration!=null) {
            if (configuration.globalStringLengthMin!=null) minChars = configuration.globalStringLengthMin;
            if (configuration.globalStringLengthMax!=null) maxChars = configuration.globalStringLengthMax;
            if (propertyName != null ) {
                if (configuration.stringLengthMin.get(propertyName)!=null) minChars = configuration.stringLengthMin.get(propertyName);
                if (configuration.stringLengthMax.get(propertyName)!=null) maxChars = configuration.stringLengthMax.get(propertyName);

                if (configuration.stringPredefinedValues.get(propertyName) != null) {
                    return new JsonString(configuration.stringPredefinedValues.get(propertyName).get(rnd.nextInt(configuration.stringPredefinedValues.get(propertyName).size())));
                }
            }

        }

        String res = "";
        int cnt = minChars + rnd.nextInt(maxChars-minChars);
        for(int i=0;i<cnt;i++) res += (rnd.nextBoolean())? (char)(65 + rnd.nextInt(25)):(char)(97 + rnd.nextInt(25));
        return new JsonString(res);
    }
}
