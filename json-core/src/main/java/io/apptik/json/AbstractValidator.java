package io.apptik.json;

public abstract class AbstractValidator implements Validator {

	protected abstract boolean doValidate(JsonElement el, StringBuilder sb);

	public String getTitle() {
		return getClass().getCanonicalName();
	}

	public boolean isValid(final JsonElement el) {
		// System.out.println("Started simple JsonSchema Validation using: " +
		// this.getTitle());
		return doValidate(el, null);
	}

	public String validate(final JsonElement el) {
		// System.out.println("Started JsonSchema Validation using: " +
		// this.getTitle());
		StringBuilder sb = new StringBuilder();
		doValidate(el, sb);
		return sb.toString();
	}

	public boolean validate(final JsonElement el, final StringBuilder sb) {
		// System.out.println("Started full JsonSchema Validation using: " +
		// this.getTitle());
		return doValidate(el, sb);
	}
}
