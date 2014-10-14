package org.djodjo.json.jsonld;

import org.djodjo.json.JsonElement;
import org.djodjo.json.wrapper.JsonObjectWrapper;

public class JsonLD extends JsonObjectWrapper{


    /**
     * Used to define the short-hand names that are used throughout a JSON-LD document. These short-hand names are called terms and help developers to express specific identifiers in a compact manner.
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



    public JsonLD setContext(JsonElement el) {
        getJson().put("@context",el); return this;
    }

    public JsonLD setId(JsonElement el) {
        getJson().put("@id",el); return this;
    }

    public JsonLD setValue(JsonElement el) {
        getJson().put("@value",el); return this;
    }

    public JsonLD setLanguage(JsonElement el) {
        getJson().put("@language",el); return this;
    }


    public JsonLD setType(JsonElement el) {
        getJson().put("@type",el); return this;
    }


    public JsonLD setContainer(JsonElement el) {
        getJson().put("@container",el); return this;
    }

  
    public JsonLD setList(JsonElement el) {
        getJson().put("@list",el); return this;
    }

 
    public JsonLD setSet(JsonElement el) {
        getJson().put("@set",el); return this;
    }

    public JsonLD setReverse(JsonElement el) {
        getJson().put("@reverse",el); return this;
    }

 
    public JsonLD setIndex(JsonElement el) {
        getJson().put("@index",el); return this;
    }


    public JsonLD setBase(JsonElement el) {
        getJson().put("@base",el); return this;
    }

    public JsonLD setVocab(JsonElement el) {
        getJson().put("@vocab",el); return this;
    }


    public JsonLD setGraph(JsonElement el) {
        getJson().put("@graph",el); return this;
    }

}
