package org.djodjo.json.jsonld;

import org.djodjo.json.ElementWrapper;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;

/**
 * JsonObject Wrap that knows json-ld.
 * used for convenience when processing JsonLdContext or any json-ld documents
 */
public final class JsonLdWrap implements ElementWrapper {
    
    JsonObject json;
    
    public JsonLdWrap() {
        json = new JsonObject();
    }
    
    public JsonLdWrap(JsonObject jsonObject) {
        wrap(jsonObject);
    }
    
    public JsonLdWrap wrap(JsonObject jsonObject) {
        this.json = jsonObject;
        return this;
    }
    
    public JsonObject getJson() {
        return json;
    }
    
    /**
     * Used to define the short-hand names that are used throughout a JSON-LD document. 
     * These short-hand names are called terms and help developers to express
     * specific identifiers in a compact manner.
     * This can be string(uri), array or object
     */
    public JsonElement getContext() {
        return getJson().opt("@context");
    }

    /**
     * Used to uniquely identify things that are being described in the document with IRIs or blank node identifiers.
     */
    public String getId() {
        return getJson().optString("@id");
    }

    /**
     * Used to specify the data that is associated with a particular property in the graph.
     */
    public String getValue() {
        return getJson().optString("@value");
    }

    /**
     * Used to specify the language for a particular string value or the default language of a JSON-LD document.
     */
    public String getLanguage() {
        return getJson().optString("@language");
    }

    /**
     * Used to set the data type of a node or typed value.
     */
    public String getType() {
        return getJson().optString("@type");
    }

    /**
     * Used to set the default container type for a term.
     */
    public String getContainer() {
        return getJson().optString("@container");
    }

    /**
     * Used to express an ordered set of data.
     */
    public String getList() {
        return getJson().optString("@list");
    }

    /**
     * Used to express an unordered set of data and to ensure that values are always represented as arrays.
     */
    public String getSet() {
        return getJson().optString("@set");
    }

    /**
     * Used to express reverse properties.
     */
    public String getReverse() {
        return getJson().optString("@reverse");
    }

    /**
     * Used to specify that a container is used to index information and that processing should continue deeper into a JSON data structure.
     */
    public String getIndex() {
        return getJson().optString("@index");
    }

    /**
     * Used to set the base IRI against which relative IRIs are resolved.
     */
    public String getBase() {
        return getJson().optString("@base");
    }

    /**
     * Used to expand properties and values in @type with a common prefix IRI.
     */
    public String getVocab() {
        return getJson().optString("@vocab");
    }


    /**
     * Used to express a graph.
     */
    public String getGraph() {
        return getJson().optString("@graph");
    }



    public JsonLdWrap setContext(JsonElement el) {
        getJson().put("@context",el); return this;
    }

    public JsonLdWrap setId(JsonElement el) {
        getJson().put("@id",el); return this;
    }

    public JsonLdWrap setValue(JsonElement el) {
        getJson().put("@value",el); return this;
    }

    public JsonLdWrap setLanguage(JsonElement el) {
        getJson().put("@language",el); return this;
    }


    public JsonLdWrap setType(JsonElement el) {
        getJson().put("@type",el); return this;
    }


    public JsonLdWrap setContainer(JsonElement el) {
        getJson().put("@container",el); return this;
    }

  
    public JsonLdWrap setList(JsonElement el) {
        getJson().put("@list",el); return this;
    }

 
    public JsonLdWrap setSet(JsonElement el) {
        getJson().put("@set",el); return this;
    }

    public JsonLdWrap setReverse(JsonElement el) {
        getJson().put("@reverse",el); return this;
    }

 
    public JsonLdWrap setIndex(JsonElement el) {
        getJson().put("@index",el); return this;
    }


    public JsonLdWrap setBase(JsonElement el) {
        getJson().put("@base",el); return this;
    }

    public JsonLdWrap setVocab(JsonElement el) {
        getJson().put("@vocab",el); return this;
    }


    public JsonLdWrap setGraph(JsonElement el) {
        getJson().put("@graph",el); return this;
    }

}
