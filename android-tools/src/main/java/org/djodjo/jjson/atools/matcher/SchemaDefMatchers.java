package org.djodjo.jjson.atools.matcher;


import org.djodjo.json.schema.Schema;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SchemaDefMatchers {

    private SchemaDefMatchers() {}

    public static Matcher<Schema> isObjectType() {
        return new ComparableTypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!item.getType().contains(Schema.TYPE_OBJECT)) return false;
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
                if(!item.getType().contains(Schema.TYPE_ARRAY)) return false;
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
                if(!item.getType().contains(Schema.TYPE_STRING)) return false;
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
                if(!item.getType().contains(Schema.TYPE_NUMBER) && !isIntegerType().matches(item)) return false;
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
                if(!item.getType().contains(Schema.TYPE_INTEGER)) return false;
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
                if(item.getMinimum() == Double.MIN_VALUE) return false;
                if(item.getMaximum() == Double.MAX_VALUE) return false;
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
                if(!item.getType().contains(Schema.TYPE_BOOLEAN)) return false;
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
                if(item.getProperties().optValue("min").getMinimum() == Double.MIN_VALUE) return false;
                if(item.getProperties().optValue("max").getMaximum() == Double.MAX_VALUE) return false;


                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Limited Range Object");
            }
        };
    }




}
