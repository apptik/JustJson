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

package io.apptik.json.generator.matcher;


import io.apptik.json.schema.Schema;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static io.apptik.json.JsonElement.*;

public class SchemaDefMatchers {

    private SchemaDefMatchers() {}

    public static Matcher<Schema> isObjectType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_OBJECT)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Object type");
            }
        };
    }

    public static Matcher<Schema> isArrayType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_ARRAY)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Array type");
            }
        };
    }

    public static Matcher<Schema> isStringType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_STRING)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is String type");
            }
        };
    }

    public static Matcher<Schema> isNumberType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_NUMBER) && !isIntegerType().matches(item)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Number type");
            }
        };
    }

    public static Matcher<Schema> isIntegerType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_INTEGER)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Integer type");
            }
        };
    }

    public static Matcher<Schema> isLimitedNumber() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!isNumberType().matches(item)) return false;
                if(item.getMinimum()==null && item.getMaximum()==null) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Limited Number");
            }
        };
    }

    public static Matcher<Schema> isBooleanType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getType() == null) return false;
                if(!item.getType().contains(TYPE_BOOLEAN)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Boolean type");
            }
        };
    }

    public static Matcher<Schema> isEnum() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(item.getEnum() == null) return false;
                if(item.getEnum().length() == 0) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is enum");
            }
        };
    }

    public static Matcher<Schema> isRangeObject() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!isObjectType().matches(item)) return false;
                if(item.getProperties() == null) return false;
                if(item.getProperties().length() != 2) return false;
                if(item.getProperties().optValue("min") == null) return false;
                if(item.getProperties().optValue("max") == null) return false;
                if(!isNumberType().matches(item.getProperties().optValue("min"))) return false;
                if(!isNumberType().matches(item.getProperties().optValue("max"))) return false;


                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Range Object");
            }
        };
    }

    public static Matcher<Schema> isLimitedRangeObject() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {

                if(!isRangeObject().matches(item)) return false;
                if(Double.compare(item.getProperties().optValue("min").getMinimum(), Double.NaN)==0) return false;
                if(Double.compare(item.getProperties().optValue("max").getMaximum(), Double.NaN)==0) return false;


                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Limited Range Object");
            }
        };
    }





}
