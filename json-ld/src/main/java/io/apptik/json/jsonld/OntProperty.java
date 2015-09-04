package io.apptik.json.jsonld;

import org.hamcrest.Matcher;

public abstract class OntProperty {

    abstract String getType();
    abstract Matcher getMatcher();

}
