package org.djodjo.jjson.atools.matcher;


import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

public abstract class ComparableTypeSafeMatcher<T> extends TypeSafeMatcher<T> implements Comparable<Matcher<T>> {
    @Override
    public int compareTo(Matcher<T> another) {
        return StringDescription.toString(this).compareTo(StringDescription.toString(another));
    }
}
