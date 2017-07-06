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

import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaList;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.hamcrest.Matcher;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ArrayGenerator extends JsonGenerator {

	public ArrayGenerator(final Schema schema,
			final JsonGeneratorConfig configuration) {
		super(schema, configuration);
	}

	public ArrayGenerator(final Schema schema,
			final JsonGeneratorConfig configuration, final String propertyName) {
		super(schema, configuration, propertyName);
	}

	@Override
	public JsonArray generate() {
		JsonArray res = new JsonArray();
		SchemaList items = schema.getItems();
		JsonElement newEl;
		int minItems = schema.getMinItems();
		int maxItems = schema.getMaxItems();
		if (configuration != null) {
			if (configuration.globalArrayItemsMin != null) {
				minItems = configuration.globalArrayItemsMin;
			}
			if (configuration.globalArrayItemsMax != null) {
				maxItems = configuration.globalArrayItemsMax;
			}
			if (propertyName != null) {
				if (configuration.arrayItemsMin.get(propertyName) != null) {
					minItems = configuration.arrayItemsMin.get(propertyName);
				}
				if (configuration.arrayItemsMax.get(propertyName) != null) {
					maxItems = configuration.arrayItemsMax.get(propertyName);
				}

			}
			if (configuration.emptyJson) {// for to create only one tem in the
											// array
				minItems = 0;
				maxItems = 1;
			}
		}
		maxItems = minItems + rnd.nextInt(maxItems - minItems);

		// meant for JSON generator after all, not OutOfMemory generator :)
		if (minItems > 500) {
			minItems = 500;
		}
		if (maxItems > 500) {
			maxItems = 500;
		}

		int cnt = 0;

		if (configuration.arrayPredefinedItems != null && propertyName != null
				&& configuration.arrayPredefinedItems.get(propertyName) != null) {
			for (JsonElement je : configuration.arrayPredefinedItems
					.get(propertyName)) {
				res.put(je);
				if (++cnt > maxItems) {
					break;
				}
			}
		} else if (items != null && items.size() > 0) {
			// if we have array with
			if (items.size() == 1) {
				for (int i = 0; i < maxItems; i++) {
					for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers
							.entrySet()) {
						if (entry.getKey().matches(items.get(0))) {
							try {
								JsonGenerator gen = (JsonGenerator) entry
										.getValue()
										.getDeclaredConstructor(Schema.class,
												JsonGeneratorConfig.class,
												String.class)
										.newInstance(items.get(0),
												configuration, propertyName);
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
					for (Map.Entry<Matcher<Schema>, Class> entry : commonPropertyMatchers
							.entrySet()) {
						if (entry.getKey().matches(itemSchema)) {
							try {
								JsonGenerator gen = (JsonGenerator) entry
										.getValue()
										.getDeclaredConstructor(Schema.class,
												JsonGeneratorConfig.class)
										.newInstance(itemSchema, configuration);
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
					if (++cnt > maxItems) {
						break;
					}
				}
			}
		} else {
			// then items can be any
			throw new NotImplementedException();
		}

		return res;
	}
}
