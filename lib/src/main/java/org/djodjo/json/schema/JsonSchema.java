package org.djodjo.json.schema;


import org.djodjo.json.JsonObjectWrapper;

public abstract class JsonSchema extends JsonObjectWrapper {

    public JsonSchema() {
        this.setContentType("application/schema+json");
    }

}
