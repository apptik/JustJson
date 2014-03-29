package org.djodjo.jjson.atools.matcher;


import org.djodjo.json.schema.Schema;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PropertyTypeMatchers {

    private PropertyTypeMatchers() {}

    public static Matcher<Schema> isStringProperty() {
        return new TypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!item.getType().equals(Schema.TYPE_STRING)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("");
            }
        };
    }

    public static Matcher<Schema> isNumberProperty() {
        return new TypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!item.getType().equals(Schema.TYPE_NUMBER)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("");
            }
        };
    }

    public static Matcher<Schema> isBooleanProperty() {
        return new TypeSafeMatcher<Schema>() {
            @Override
            protected boolean matchesSafely(Schema item) {
                if(!item.getType().equals(Schema.TYPE_BOOLEAN)) return false;
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("");
            }
        };
    }

    public static Matcher<Schema> isRangeObject() {
       return new TypeSafeMatcher<Schema>() {
           @Override
           protected boolean matchesSafely(Schema item) {
               if(!item.getType().equals(Schema.TYPE_OBJECT)) return false;
               if(item.getProperties() == null) return false;
               if(item.getProperties().optValue("min") == null) return false;
               if(item.getProperties().optValue("max") == null) return false;
               if(!isNumberProperty().matches(item.getProperties().optValue("min"))) return false;
               if(!isNumberProperty().matches(item.getProperties().optValue("max"))) return false;


               return true;
           }

           @Override
           public void describeTo(Description description) {
               description.appendText("Range Object Matcher");
           }
       };
    }




}
