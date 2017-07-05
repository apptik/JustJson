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

import static io.apptik.json.generator.matcher.FormatMatchers.isDateFormat;
import static io.apptik.json.generator.matcher.FormatMatchers.isDateTimeFormat;
import static io.apptik.json.generator.matcher.FormatMatchers.isEmailFormat;
import static io.apptik.json.generator.matcher.FormatMatchers.isTimeFormat;
import static io.apptik.json.generator.matcher.FormatMatchers.isUriFormat;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonString;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.generator.generators.formats.DateGenerator;
import io.apptik.json.generator.generators.formats.DateTimeGenerator;
import io.apptik.json.generator.generators.formats.EmailGenerator;
import io.apptik.json.generator.generators.formats.TimeGenerator;
import io.apptik.json.generator.generators.formats.UriGenerator;
import io.apptik.json.schema.Schema;
import io.apptik.json.util.LinkedTreeMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.hamcrest.Matcher;

import com.mifmif.common.regex.Generex;

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

	public StringGenerator(final Schema schema,
			final JsonGeneratorConfig configuration) {
		super(schema, configuration);
	}

	public StringGenerator(final Schema schema,
			final JsonGeneratorConfig configuration, final String propertyName) {
		super(schema, configuration, propertyName);
	}

	@Override
	public JsonElement generate() {
		int minChars = 0;
		int maxChars = 15;
		if (schema.getFormat() != null) {
			for (Map.Entry<Matcher<Schema>, Class> entry : stringFormatMatchers
					.entrySet()) {
				if (entry.getKey().matches(schema)) {
					JsonGenerator gen = null;

					try {
						gen = (JsonGenerator) entry
								.getValue()
								.getDeclaredConstructor(Schema.class,
										JsonGeneratorConfig.class, String.class)
								.newInstance(schema, configuration,
										propertyName);
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
		// check min and max length defined in the schema before else take the
		// configuration
		int maxLength = this.schema.getMaxLength();
		int minLength = this.schema.getMinLength();
		if (configuration != null) {
			// want to restrict the min authorized by the schema
			if (configuration.globalStringLengthMin != null) {
				minChars = minLength < configuration.globalStringLengthMin ? configuration.globalStringLengthMin
						: minLength;
			}
			// want to restrict the max authorized by the schema
			if (configuration.globalStringLengthMax != null) {
				maxChars = maxLength > configuration.globalStringLengthMax ? configuration.globalStringLengthMax
						: maxLength;
			}
			if (propertyName != null) {
				// want to restrict the min authorized by the schema for this
				// property
				if (configuration.stringLengthMin.get(propertyName) != null) {
					minChars = minLength < configuration.stringLengthMin
							.get(propertyName) ? configuration.stringLengthMin
							.get(propertyName) : minLength;
				}
				// want to restrict the max authorized by the schema for this
				// property
				if (configuration.stringLengthMax.get(propertyName) != null) {
					minChars = maxLength < configuration.stringLengthMax
							.get(propertyName) ? configuration.stringLengthMax
							.get(propertyName) : maxLength;
				}

				if (configuration.stringPredefinedValues.get(propertyName) != null) {
					return new JsonString(
							configuration.stringPredefinedValues
									.get(propertyName)
									.get(rnd.nextInt(configuration.stringPredefinedValues
											.get(propertyName).size())));
				}
			}

		}

		// manage only pattern and TODO manage pattern properties
		String regex = this.schema.getPattern();
		if (regex == null) {
			if (maxLength < minLength) {
				maxLength = minLength + 1;
			}
			regex = "\\w{" + minLength + "," + maxLength + "}";
		}
		Generex wGenerex = new Generex(regex);

		return new JsonString(wGenerex.random());

		/*
		 * else { String res = ""; int cnt = minChars + rnd.nextInt(maxChars -
		 * minChars); for (int i = 0; i < cnt; i++) { res += (rnd.nextBoolean())
		 * ? (char) (65 + rnd.nextInt(25)) : (char) (97 + rnd.nextInt(25)); }
		 * return new JsonString(res); }
		 */
	}
}
