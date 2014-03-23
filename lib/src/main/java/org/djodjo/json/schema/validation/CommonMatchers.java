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

package org.djodjo.json.schema.validation;


import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.Validator;
import org.djodjo.json.schema.Schema;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;


public class CommonMatchers {

    private CommonMatchers(){}


    public static Matcher<JsonElement> isPresent(final String property) {
        return new TypeSafeDiagnosingMatcher<JsonElement>() {
            @Override
            protected boolean matchesSafely(JsonElement item, Description mismatchDescription) {
                //we do not care for the properties if parent item is not JsonObject
                if(!item.isJsonObject()) return true;

                if(!item.asJsonObject().has(property)) {
                    mismatchDescription.appendText("property: '" + property + "' is missing");
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("property is present");
            }
        };
    }

    public static Matcher<JsonElement> isOfType(final String type) {
        return new TypeSafeDiagnosingMatcher<JsonElement>() {
            @Override
            protected boolean matchesSafely(JsonElement item, Description mismatchDescription) {
                if(type.equals(item.getJsonType()))
                    return true;
                else {
                    mismatchDescription.appendText("expected type was '" + type + "' but found '" + item.getJsonType() +"'");
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is of Type: " + type);
            }
        };
    }

    public static Matcher<JsonElement> isOfType(final ArrayList<String> types) {
        return new TypeSafeDiagnosingMatcher<JsonElement>() {
            @Override
            protected boolean matchesSafely(JsonElement item, Description mismatchDescription) {
                if(types.contains(item.getJsonType()))
                    return true;
                else {
                    mismatchDescription.appendText("expected type was any of '" + types.toString() + "' but found '" + item.getJsonType() +"'");
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is of Type: " + types.toString());
            }
        };
    }

    public static Matcher<JsonElement> isSubPropertyValid(final Validator validator, final String property) {
        return new TypeSafeDiagnosingMatcher<JsonElement>() {
            @Override
            protected boolean matchesSafely(JsonElement item, Description mismatchDescription) {
                //we do not care for the properties if parent item is not JsonObject
                if(!item.isJsonObject()) return true;

                //we also dont care if the property is not actually there
                //if it is needed it will be handled by the "required" constraint on another matcher
                if(!item.asJsonObject().has(property)) return true;

                return validator.isValid(item.asJsonObject().opt(property));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is property valid");
            }
        };
    }
}
