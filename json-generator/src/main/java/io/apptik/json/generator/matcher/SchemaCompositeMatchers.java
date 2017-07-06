package io.apptik.json.generator.matcher;

import io.apptik.json.schema.Schema;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SchemaCompositeMatchers {

	public static Matcher<Schema> hasAllOf() {
		return new ComparableTypeSafeMatcher<Schema>() {
			public void describeTo(final Description description) {
				description.appendText("Has allOf section");
			}

			@Override
			protected boolean matchesSafely(final Schema item) {
				if (item.getAllOf() == null || item.getAllOf().isEmpty()) {
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<Schema> hasAnyOf() {
		return new ComparableTypeSafeMatcher<Schema>() {
			public void describeTo(final Description description) {
				description.appendText("Has anyOf section");
			}

			@Override
			protected boolean matchesSafely(final Schema item) {
				if (item.getAnyOf() == null || !item.getAnyOf().isEmpty()) {
					return false;
				}
				return true;
			}
		};
	}

	public static Matcher<Schema> hasOneOf() {
		return new ComparableTypeSafeMatcher<Schema>() {
			public void describeTo(final Description description) {
				description.appendText("Has oneOf section");
			}

			@Override
			protected boolean matchesSafely(final Schema item) {
				if (item.getOneOf() == null || item.getOneOf().isEmpty()) {
					return false;
				}
				return true;
			}
		};
	}

	private SchemaCompositeMatchers() {
	}

}
