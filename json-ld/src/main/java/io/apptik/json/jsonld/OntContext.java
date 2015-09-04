package io.apptik.json.jsonld;


import java.util.HashMap;
import java.util.HashSet;

public abstract class OntContext {

    protected HashMap<String, OntProperty> properties;
    protected HashSet<String> types;

    //abstract Validator getValidator();

}
