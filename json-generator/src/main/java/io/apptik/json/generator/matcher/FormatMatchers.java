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

public class FormatMatchers {

    private FormatMatchers() {}

    public static Matcher<Schema> isDateFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                return Schema.FORMAT_DATE.equals(item.getFormat());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is date format");
            }
        };
    }

    public static Matcher<Schema> isDateTimeFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                return Schema.FORMAT_DATE_TIME.equals(item.getFormat());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is date-time format");
            }
        };
    }

    public static Matcher<Schema> isColorFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!Schema.FORMAT_COLOR.equals(item.getFormat())) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is color format");
            }
        };
    }

    public static Matcher<Schema> isEmailFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_EMAIL)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is email format");
            }
        };
    }

    public static Matcher<Schema> isHostnameFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_HOST_NAME) && !item.getFormat().equals(Schema.FORMAT_HOSTNAME)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is hostname format");
            }
        };
    }

    public static Matcher<Schema> isIPv4Format() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_IP_ADDR) && !item.getFormat().equals(Schema.FORMAT_IPV4)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is IP v4 address format");
            }
        };
    }

    public static Matcher<Schema> isIPv6Format() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_IPV6)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is IP v6 address format");
            }
        };
    }

    public static Matcher<Schema> isPhoneFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_PHONE)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is phone format");
            }
        };
    }

    public static Matcher<Schema> isRegexFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_REGEX)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is regex format");
            }
        };
    }

    public static Matcher<Schema> isStyleFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_STYLE)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is style format");
            }
        };
    }

    public static Matcher<Schema> isTimeFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_TIME)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is time format");
            }
        };
    }

    public static Matcher<Schema> isUriFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_URI)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is uri format");
            }
        };
    }

    public static Matcher<Schema> isUTCmilisecFormat() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!SchemaDefMatchers.isStringType().matches(item)) return false;
                if(item.getFormat() == null) return false;
                if(!item.getFormat().equals(Schema.FORMAT_UTC_MILISEC)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is utc-millisec format");
            }
        };
    }




}
