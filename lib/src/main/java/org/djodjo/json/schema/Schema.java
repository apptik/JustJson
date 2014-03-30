package org.djodjo.json.schema;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElementWrapper;
import org.djodjo.json.JsonException;
import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonObjectArrayWrapper;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.JsonStringArrayWrapper;
import org.djodjo.json.Validator;

import java.net.URI;
import java.util.ArrayList;

public abstract class Schema extends JsonObjectWrapper {

    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_ENUM = "enum";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_BOOLEAN = "boolean";

    public Schema() {
        super();
        this.setContentType("application/schema+json");
    }

    /**
     *
     * @return empty schema from the same version as the current one
     */
    public abstract Schema getEmptySchema();

    @Override
    public JsonElementWrapper setJsonSchema(URI uri) {
        throw new RuntimeException("Cannot set Schema on a Schema like this. Use setSchema method.");
    }

    @Override
    public URI getJsonSchemaUri() {
        return URI.create(getSchema());
    }

    public Validator getDefaultValidator() {
        return null;
    }

    public String getId() {
        return getJson().optString("id","");
    }

    protected Schema setSchema(String schemaUri) {
        try {
            getJson().put("$schema", schemaUri);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getSchema() {
        return getJson().optString("$schema","");
    }

    public String getTitle() {
        return getJson().optString("title", "");
    }

    public String getDescription() {
        return getJson().optString("description","");
    }

    public String getDefault() {
        return getJson().optString("default","");
    }

    public double getMultipleOf() {
        return getJson().optDouble("multipleOf", Double.MIN_VALUE);
    }

    public double getMaximum() {
        return getJson().optDouble("maximum", Double.MAX_VALUE);
    }

    public boolean getExclusiveMaximum() {
        return getJson().optBoolean("exclusiveMaximum", false);
    }

    public double getMinimum() {
        return getJson().optDouble("minimum", Double.MIN_VALUE);
    }

    public boolean getExclusiveMinimum() {
        return getJson().optBoolean("exclusiveMinimum", false);
    }

    public int getMaxLength() {
        return getJson().optInt("maxLength", Integer.MAX_VALUE);
    }

    public int getMinLength() {
        return getJson().optInt("minLength", 0);
    }

    public String getPattern() {
        return getJson().optString("pattern","");
    }

    //TODO can return also object
    public boolean getAdditionalItems() {
        return getJson().optBoolean("additionalItems", true);
    }

    public ArrayList<Schema> getItems() {
        ArrayList<Schema> res;
        if(getJson().opt("items") == null) {
            return null;
        }
        else if(getJson().opt("items").isJsonArray()) {
            return ((JsonObjectArrayWrapper<Schema>)new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("type"))).getJsonWrappersList();
        }
        else {
            res = new ArrayList<Schema>();
            res.add((Schema)getEmptySchema().wrap(getJson().optJsonObject("items")));
        }
        return res;
    }

    public int getMaxItems() {
        return getJson().optInt("maxItems", Integer.MAX_VALUE);
    }

    public int getMinItems() {
        return getJson().optInt("minItems", 0);
    }

    public boolean getUniqueItems() {
        return getJson().optBoolean("uniqueItems",false);
    }

    public int getMaxProperties() {
        return getJson().optInt("maxProperties", Integer.MAX_VALUE);
    }

    public int getMinProperties() {
        return getJson().optInt("minProperties", 0);
    }

    public ArrayList<String> getRequired() {
        return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("required")).getStringList();
    }

    //TODO can return also object
    public boolean getAdditionalProperties() {
        return getJson().optBoolean("additionalProperties", true);
    }

    public JsonObject getDefinitions() {
        return getJson().optJsonObject("definitions");
    }

    public SchemaMap getProperties() {
        if(!getJson().has("properties")) return null;
        return (SchemaMap) new SchemaMap(this.getEmptySchema()).wrap(getJson().optJsonObject("properties"));
    }

    public JsonObject getPatternProperties() {
        return getJson().optJsonObject("patternProperties");
    }

    public JsonObject getDependencies() {
        return getJson().optJsonObject("dependencies");
    }

    public JsonArray getEnum() {
        return getJson().optJsonArray("enum");
    }

    public ArrayList<String> getType() {
        ArrayList<String> res;
        if(getJson().opt("type")==null) return null;
        if(getJson().opt("type").isJsonArray()) {
            return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("type")).getStringList();
        }
        else {
            res = new ArrayList<String>();
            res.add(getJson().optString("type"));
        }
        return res;
    }

    public JsonObjectArrayWrapper<Schema> getAllOf() {
        return (JsonObjectArrayWrapper<Schema>)new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("allOf"));
    }

    public JsonObjectArrayWrapper<Schema> getAnyOf() {
        return (JsonObjectArrayWrapper<Schema>)new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("anyOf"));
    }

    public JsonObjectArrayWrapper<Schema> getOneOf() {
        return (JsonObjectArrayWrapper<Schema>)new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("oneOf"));
    }

    public Schema getNot() {
        return (Schema)getEmptySchema().wrap(getJson().optJsonObject("not"));
    }


    //TODO
    public Schema merge(Schema schema) {
        return this;
    }

}
