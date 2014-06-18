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
import org.djodjo.json.JsonString;
import org.djodjo.json.generator.Generator;
import org.djodjo.json.generator.GeneratorConfig;
import org.djodjo.json.generator.generators.formats.DateGenerator;
import org.djodjo.json.generator.generators.formats.DateTimeGenerator;
import org.djodjo.json.generator.generators.formats.TimeGenerator;
import org.djodjo.json.generator.generators.formats.UriGenerator;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaV4;
import org.djodjo.json.util.LinkedTreeMap;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.djodjo.json.generator.matcher.FormatMatchers.isDateFormat;
import static org.djodjo.json.generator.matcher.FormatMatchers.isDateTimeFormat;
import static org.djodjo.json.generator.matcher.FormatMatchers.isTimeFormat;
import static org.djodjo.json.generator.matcher.FormatMatchers.isUriFormat;

public class StringGenerator extends Generator {

    protected static LinkedTreeMap<Matcher<Schema>, Class> stringFormatMatchers;

    static {
        stringFormatMatchers = new LinkedTreeMap<Matcher<Schema>, Class>();
        stringFormatMatchers.put(isDateTimeFormat(), DateTimeGenerator.class);
        stringFormatMatchers.put(isDateFormat(), DateGenerator.class);
        stringFormatMatchers.put(isTimeFormat(), TimeGenerator.class);
        stringFormatMatchers.put(isUriFormat(), UriGenerator.class);
    }

    public StringGenerator(SchemaV4 schema, GeneratorConfig configuration) {
        super(schema, configuration);
    }

    public StringGenerator(SchemaV4 schema, GeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        int minChars = 0;
        int maxChars = 15;
        if(schema.getFormat()!=null) {
            for (Map.Entry<Matcher<Schema>, Class> entry : stringFormatMatchers.entrySet()) {
                if (entry.getKey().matches(schema)) {
                    Generator gen = null;
                    try {
                        gen = (Generator)entry.getValue().getDeclaredConstructor(SchemaV4.class, GeneratorConfig.class, String.class).newInstance(schema, configuration, propertyName);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    return gen.generate();
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
