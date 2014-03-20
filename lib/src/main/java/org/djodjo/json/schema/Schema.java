package org.djodjo.json.schema;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonElementWrapper;
import org.djodjo.json.JsonException;
import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonObjectArrayWrapper;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.JsonStringArrayWrapper;
import org.djodjo.json.Validator;

import java.net.URI;
import java.util.ArrayList;

public class Schema extends JsonObjectWrapper {

    public Schema() {
        this.setContentType("application/schema+json");
    }
    public Schema(JsonObject jsonObject) {
        throw new RuntimeException("Cannot instantiate Schema like this. Use wrap method.");
    }
    @Override
    public JsonObjectWrapper wrap(JsonElement jsonElement) {
        super.wrap(jsonElement);
        return getRightVersion();
    }
    private Schema getRightVersion() {
        if(getSchema().equals("http://json-schema.org/draft-04/schema#"))
            return (Schema)new SchemaV4().wrap(getJson());
        return this;
    }

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
        return getJson().optDouble("multipleOf", 0);
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

    //TODO
    public boolean getAdditionalItems() {
        return getJson().optBoolean("additionalItems", true);
    }

    public ArrayList<Schema> getItems() {
        ArrayList<Schema> res;
        if(getJson().opt("items").isJsonArray()) {
            return ((JsonObjectArrayWrapper<Schema>)new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("type"))).getJsonWrappersList();
        }
        else {
            res = new ArrayList<Schema>();
            res.add((Schema)new Schema().wrap(getJson().optJsonObject("items")));
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

    //TODO
    public boolean getAdditionalProperties() {
        return getJson().optBoolean("additionalProperties", true);
    }

    public JsonObject getDefinitions() {
        return getJson().optJsonObject("definitions");
    }

    public JsonObject getProperties() {
        return getJson().optJsonObject("properties");
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
        return (Schema)new Schema().wrap(getJson().optJsonObject("not"));
    }


}
