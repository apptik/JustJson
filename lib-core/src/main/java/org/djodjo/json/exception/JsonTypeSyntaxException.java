package org.djodjo.json.exception;


public class JsonTypeSyntaxException extends MalformedJsonException {

    private static final long serialVersionUID = 11L;

    public JsonTypeSyntaxException(String message) {
        super(message);
    }

    public JsonTypeSyntaxException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JsonTypeSyntaxException(Throwable cause) {
        super(cause);
    }
}
