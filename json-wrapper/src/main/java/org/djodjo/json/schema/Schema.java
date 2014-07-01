package org.djodjo.json.schema;


import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.Validator;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.schema.fetch.SchemaFetcher;
import org.djodjo.json.schema.fetch.SchemaUriFetcher;
import org.djodjo.json.wrapper.JsonElementWrapper;
import org.djodjo.json.wrapper.JsonObjectArrayWrapper;
import org.djodjo.json.wrapper.JsonObjectWrapper;
import org.djodjo.json.wrapper.JsonStringArrayWrapper;

import java.net.URI;
import java.util.ArrayList;
//TODO BIG cleanup
public abstract class Schema extends JsonObjectWrapper {

    public static final String VER_4 = "http://json-schema.org/draft-04/schema#";
    public static final String VER_5 = "http://json-schema.org/draft-05/schema#";


    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";

    //formats as defined in http://tools.ietf.org/html/draft-zyp-json-schema-03#section-5.23
    //note that there is no definition for the formats in draft-v4 except in http://json-schema.org/latest/json-schema-validation.html#anchor104

    /**
     *    date-time  This SHOULD be a date in ISO 8601 format of YYYY-MM-
     DDThh:mm:ssZ in UTC time.  This is the recommended form of date/
     timestamp.
     */
    public static final String FORMAT_DATE_TIME = "date-time";

    /**
     *    date  This SHOULD be a date in the format of YYYY-MM-DD.  It is
     recommended that you use the "date-time" format instead of "date"
     unless you need to transfer only the date part.
     */
    public static final String FORMAT_DATE = "date";

    /**
     *
     time  This SHOULD be a time in the format of hh:mm:ss.  It is
     recommended that you use the "date-time" format instead of "time"
     unless you need to transfer only the time part.
     */
    public static final String FORMAT_TIME = "time";

    /**
     *    utc-millisec  This SHOULD be the difference, measured in
     milliseconds, between the specified time and midnight, 00:00 of
     January 1, 1970 UTC.  The value SHOULD be a number (integer or
     float).
     */
    public static final String FORMAT_UTC_MILISEC = "utc-millisec";

    /**
     *    regex  A regular expression, following the regular expression
     specification from ECMA 262/Perl 5.
     */
    public static final String FORMAT_REGEX = "regex";

    /**
     *    color  This is a CSS color (like "#FF0000" or "red"), based on CSS
     2.1 [W3C.CR-CSS21-20070719].
     */
    public static final String FORMAT_COLOR = "color";

    /**
     *    style  This is a CSS style definition (like "color: red; background-
     color:#FFF"), based on CSS 2.1 [W3C.CR-CSS21-20070719].
     */
    public static final String FORMAT_STYLE = "style";

    /**
     *    phone  This SHOULD be a phone number (format MAY follow E.123).
     */
    public static final String FORMAT_PHONE = "phone";

    /**
     * uri  This value SHOULD be a URI..
     */
    public static final String FORMAT_URI = "uri";

    /**
     * email  This SHOULD be an email address.
     */
    public static final String FORMAT_EMAIL = "email";


    /**
     *    ip-address  This SHOULD be an ip version 4 address.
     */
    public static final String FORMAT_IP_ADDR = "ip-address"; //draft v3
    public static final String FORMAT_IPV4 = "ipv4"; //draft v4

    /**
     *    ipv6  This SHOULD be an ip version 6 address.
     */
    public static final String FORMAT_IPV6 = "ipv6";

    /**
     *
     host-name  This SHOULD be a host-name.
     */
    public static final String FORMAT_HOST_NAME = "host-name"; //draft v3
    public static final String FORMAT_HOSTNAME = "hostname"; //draft v4

    protected URI origSrc = null;

    protected SchemaFetcher schemaFetcher = null;


    public Schema() {
        super();
        this.setContentType("application/schema+json");
    }

    public Schema(URI schemaRef) {
        this();
        origSrc = schemaRef;
        this.wrap(new SchemaUriFetcher().fetch(origSrc).getJson());
    }

    public SchemaFetcher getSchemaFetcher() {
        return schemaFetcher;
    }

    public <O extends Schema> O setSchemaFetcher(SchemaFetcher schemaFetcher) {
        this.schemaFetcher = schemaFetcher;
        return (O)this;
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
    public <T extends JsonElementWrapper> T wrap(JsonElement jsonElement) {
        Schema schema = super.wrap(jsonElement);
        mergeWithRef();
        return (T) schema;
    }

    private void mergeWithRef() {
        if(this.getRef()!=null && !this.getRef().trim().isEmpty()) {
            //populate values
            //if there are title and description already do not change those.
            Schema refSchema;
            if(schemaFetcher==null) schemaFetcher = new SchemaUriFetcher();
            refSchema = schemaFetcher.fetch(URI.create(this.getRef()));

            if (refSchema!=null) {
                getJson().merge(refSchema.getJson());
            }
        }
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

    public String getRef() {
        return getJson().optString("$ref","");
    }

    public String getTitle() {
        return getJson().optString("title", "");
    }

    //TODO validation but optional
    public String getFormat() {
        return getJson().optString("format", "");
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
        return getJson().optDouble("maximum", Double.NaN);
    }

    public boolean getExclusiveMaximum() {
        return getJson().optBoolean("exclusiveMaximum", false);
    }

    public double getMinimum() {
        return getJson().optDouble("minimum", Double.NaN);
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
        return new SchemaMap(this.getEmptySchema()).wrap(getJson().optJsonObject("properties"));
    }

    public SchemaMap getPatternProperties() {
        return new SchemaMap(this.getEmptySchema()).wrap(getJson().optJsonObject("patternProperties"));
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
        return new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("allOf"), (Class)this.getClass());
    }

    public JsonObjectArrayWrapper<Schema> getAnyOf() {
        return new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("anyOf"), (Class)this.getClass());
    }

    public JsonObjectArrayWrapper<Schema> getOneOf() {
        return new JsonObjectArrayWrapper<Schema>().wrap(getJson().optJsonArray("oneOf"), (Class)this.getClass() );
    }

    public Schema getNot() {
        return (Schema)getEmptySchema().wrap(getJson().optJsonObject("not"));
    }

}
