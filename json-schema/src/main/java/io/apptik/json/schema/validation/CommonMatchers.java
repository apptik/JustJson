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

package io.apptik.json.schema.validation;

import static io.apptik.json.JsonElement.TYPE_INTEGER;
import static io.apptik.json.JsonElement.TYPE_NUMBER;
import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.Validator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CommonMatchers {

	public static Matcher<JsonElement> areItemsUnique() {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("unique items");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the items if parent item is not JsonArray
				if (!item.isJsonArray()) {
					return true;
				}

				JsonElement prevEl = null;
				for (JsonElement el : item.asJsonArray()) {
					if (prevEl != null && el.equals(prevEl)) {
						mismatchDescription
								.appendText("items in Json array are not unique");
						return false;
					}
					prevEl = el;
				}

				return true;
			}
		};
	}

	public static Matcher<JsonElement> areItemsValid(final Validator validator) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("are array items valid");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonArray
				if (!item.isJsonArray()) {
					return true;
				}

				for (int i = 0; i < item.asJsonArray().length(); i++) {
					StringBuilder sb = new StringBuilder();
					if (!validator.validate(item.asJsonArray().opt(i), sb)) {
						mismatchDescription
								.appendText(
										"item at pos: "
												+ i
												+ ", does not validate by validator "
												+ validator.getTitle())
								.appendText("\nDetails: ")
								.appendText(sb.toString());
						return false;
					}
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> doesItemCountMatches(final int itemsCount) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("array items max count");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonArray
				if (!item.isJsonArray()) {
					return true;
				}

				if (item.asJsonArray().length() > itemsCount) {
					mismatchDescription
							.appendText("items in Json array more than defined");
					return false;
				}

				return true;
			}
		};
	}

	public static Matcher<CharSequence> hasPattern(final String regex) {
		final Pattern p = Pattern.compile(regex);
		return new TypeSafeDiagnosingMatcher<CharSequence>() {

			public void describeTo(final Description description) {
				description.appendText("string matching pattern: "
						+ p.pattern());
			}

			@Override
			protected boolean matchesSafely(final CharSequence item,
					final Description mismatchDescription) {
				return p.matcher(item).matches();
			}
		};
	}

	// <== STRING <==

	// ==> NUMBER ==>

	/**
	 * General matcher
	 *
	 * @param validator
	 * @param element
	 * @return
	 */
	public static Matcher<JsonElement> isElementValid(
			final Validator validator, final JsonElement element) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("is array item valid");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				if (!validator.isValid(element)) {
					mismatchDescription.appendText("element: "
							+ element.toString()
							+ ", does not validate by validator "
							+ validator.getTitle());
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isInEnums(final JsonArray enums) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nMatch to one of enum values: "
						+ enums.toString());
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {

				if (enums.contains(item)) {
					return true;
				}

				mismatchDescription.appendText(", mismatch value '"
						+ item.toString() + "'");
				return false;
			}
		};
	}

	public static Matcher<JsonElement> isItemValid(final Validator validator,
			final int itemPos) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("is array item valid");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonArray
				if (!item.isJsonArray()) {
					return true;
				}

				// we also dont care if the item at position is not actually
				// there
				// if it is needed it will be handled by another matcher
				if (item.asJsonArray().opt(itemPos) == null) {
					return true;
				}
				StringBuilder sb = new StringBuilder();
				if (!validator.validate(item.asJsonArray().opt(itemPos), sb)) {
					mismatchDescription
							.appendText(
									"item at pos: "
											+ itemPos
											+ ", does not validate by validator "
											+ validator.getTitle())
							.appendText("\nDetails: ")
							.appendText(sb.toString());
					return false;
				}

				return true;
			}
		};
	}

	public static Matcher<JsonElement> isLessOrEqualThan(final double value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("maximum");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// Number
				if (!item.isNumber()) {
					return true;
				}
				if (!(item.asDouble() <= value)) {
					mismatchDescription
							.appendText("value is not less than maximum "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isLessThan(final double value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("exclusive maximum");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// Number
				if (!item.isNumber()) {
					return true;
				}
				if (!(item.asDouble() < value)) {
					mismatchDescription
							.appendText("value is not less than exclusive maximum "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	// <== NUMBER <==

	public static Matcher<JsonElement> isMoreOrEqualThan(final double value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("minimum");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// Number
				if (!item.isNumber()) {
					return true;
				}
				if (!(item.asDouble() >= value)) {
					mismatchDescription
							.appendText("value is not more than minimum "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isMoreThan(final double value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("exclusive minimum");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// Number
				if (!item.isNumber()) {
					return true;
				}
				if (!(item.asDouble() > value)) {
					mismatchDescription
							.appendText("value is not more than exclusive minimum "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isMultipleOf(final double value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("multipleOf");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// Number
				if (!item.isNumber()) {
					return true;
				}

				Number remainder = item.asDouble() % value;
				if (!remainder.equals(0) && !remainder.equals(0.0)) {
					mismatchDescription.appendText("value is not multipleOf "
							+ value);
					return false;
				}

				return true;
			}
		};
	}

	// <== COMMON <==

	// ==> ARRAY ==>

	public static Matcher<JsonElement> isNoAdditionalProperties(
			final Set<String> properties, final Set<String> patternProperties) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description
						.appendText("no additional properties exist except the ones defined in 'properties' and 'patternProperties' ");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				Set<String> objectProps = new HashSet<String>();
				objectProps.addAll(item.asJsonObject().keySet());

				objectProps.removeAll(properties);

				for (String pattern : patternProperties) {
					Pattern p = Pattern.compile(pattern);
					Iterator<String> it = objectProps.iterator();
					while (it.hasNext()) {
						String prop = it.next();
						if (p.matcher(prop).matches()) {
							it.remove();
						}
					}
				}

				if (objectProps.size() > 0) {
					for (String prop : objectProps) {
						mismatchDescription.appendText("\nproperty: '" + prop
								+ "' is not defined in the schema. ");
					}
					return false;
				}
				return true;
			}
		};
	}

	// if(!validator.validate(item.asJsonObject().opt(property), sb)) {
	// mismatchDescription.appendText(", mismatch value: " +
	// item.asJsonObject().opt(property))
	// .appendText("\nDetails: ")
	// .appendText(sb.toString());
	// return false;
	// }

	public static Matcher<JsonElement> isOfType(final List<String> types) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nMatch to one of types: "
						+ types.toString());
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				if (types.contains(item.getJsonType())
						|| (item.getJsonType().equals(TYPE_INTEGER) && types
								.contains(TYPE_NUMBER))) {
					return true;
				} else {
					mismatchDescription.appendText(", mismatch type '"
							+ item.getJsonType() + "'");
					return false;
				}
			}
		};
	}

	// ==> COMMON ==>
	public static Matcher<JsonElement> isOfType(final String type) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nMatch to type: " + type);
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				if (type.equals(item.getJsonType())) {
					return true;
				} else {
					mismatchDescription.appendText(", mismatch type '"
							+ item.getJsonType() + "'");
					return false;
				}
			}
		};
	}

	public static Matcher<JsonElement> isPropertyPatternValid(
			final Validator validator, final String propertyPattern) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nMatch object property pattern '"
						+ propertyPattern + "' with schema: "
						+ ((SchemaValidator) validator).getSchema());
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				// we also dont care if the property is not actually there
				// if it is needed it will be handled by the "required"
				// constraint on another matcher

				Pattern p = Pattern.compile(propertyPattern);
				for (Map.Entry<String, JsonElement> entry : item.asJsonObject()) {
					if (p.matcher(entry.getKey()).matches()) {
						StringBuilder sb = new StringBuilder();
						if (!validator.validate(entry.getValue(), sb)) {
							mismatchDescription
									.appendText(
											", mismatch of property: '"
													+ entry.getKey()
													+ "' with value: "
													+ entry.getValue())
									.appendText("\nDetails: ")
									.appendText(sb.toString());
							return false;
						}
					}
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isPropertyPresent(final String property) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nCheck if property '" + property
						+ "' exists");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				if (!item.asJsonObject().has(property)) {
					mismatchDescription.appendText(", does not exist in : "
							+ item);
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> isPropertyValid(
			final Validator validator, final String property) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("\nMatch object property '" + property
						+ "' with schema: "
						+ ((SchemaValidator) validator).getSchema());
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				// we also dont care if the property is not actually there
				// if it is needed it will be handled by the "required"
				// constraint on another matcher
				if (!item.asJsonObject().has(property)) {
					return true;
				}
				StringBuilder sb = new StringBuilder();
				if (!validator.validate(item.asJsonObject().opt(property), sb)) {
					mismatchDescription
							.appendText(
									", mismatch value: "
											+ item.asJsonObject().opt(property))
							.appendText("\nDetails: ")
							.appendText(sb.toString());
					return false;
				}
				return true;
			}
		};
	}

	// <== ARRAY <==

	// ==> OBJECT ==>

	public static Matcher<JsonElement> matchesPattern(final String value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("Pattern match");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// String
				if (!item.isString()) {
					return true;
				}
				if (!Pattern.matches(value, item.asString())) {
					mismatchDescription.appendText("Pattern '" + value
							+ "' does not match '" + item.asString() + "'");
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<JsonElement> maxItems(final int maxItems) {
		return doesItemCountMatches(maxItems);
	}

	public static Matcher<JsonElement> maxProperties(final int maxProperties) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("object properties max count");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				if (item.asJsonObject().length() > maxProperties) {
					mismatchDescription
							.appendText("properties in Json object more than defined");
					return false;
				}

				return true;
			}
		};
	}

	public static Matcher<JsonElement> minItems(final int minItems) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("array items min count");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the items if parent item is not JsonArray
				if (!item.isJsonArray()) {
					return true;
				}

				if (item.asJsonArray().length() < minItems) {
					mismatchDescription
							.appendText("items in Json array less than defined");
					return false;
				}

				return true;
			}
		};
	}

	public static Matcher<JsonElement> minProperties(final int minProperties) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("object properties min count");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// JsonObject
				if (!item.isJsonObject()) {
					return true;
				}

				if (item.asJsonObject().length() < minProperties) {
					mismatchDescription
							.appendText("properties in Json object less than defined");
					return false;
				}

				return true;
			}
		};
	}

	// ==> STRING ==>
	public static Matcher<JsonElement> withCharsLessOrEqualTo(final int value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("String maximum length");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// String
				if (!item.isString()) {
					return true;
				}
				if (item.asString().length() > value) {
					mismatchDescription
							.appendText("String length more than maximum value: "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	// <== OBJECT <==

	// ==> GENERAL ==>

	public static Matcher<JsonElement> withCharsMoreOrEqualTo(final int value) {
		return new TypeSafeDiagnosingMatcher<JsonElement>() {

			public void describeTo(final Description description) {
				description.appendText("String minimum length");
			}

			@Override
			protected boolean matchesSafely(final JsonElement item,
					final Description mismatchDescription) {
				// we do not care for the properties if parent item is not
				// String
				if (!item.isString()) {
					return true;
				}
				if (item.asString().length() < value) {
					mismatchDescription
							.appendText("String length less than minimum value: "
									+ value);
					return false;
				}
				return true;
			}
		};
	}

	private CommonMatchers() {
	}

	// <== GENERAL <==

}
