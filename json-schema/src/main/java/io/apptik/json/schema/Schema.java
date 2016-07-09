package io.apptik.json.schema;


import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.Validator;
import io.apptik.json.schema.fetch.SchemaFetcher;
import io.apptik.json.schema.fetch.SchemaUriFetcher;
import io.apptik.json.wrapper.JsonElementWrapper;
import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.wrapper.JsonStringArrayWrapper;
import io.apptik.json.wrapper.MetaInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO cleanup
public abstract class Schema extends JsonObjectWrapper implements MetaInfo{

    public static final String VER_4 = "http://json-schema.org/draft-04/schema#";
    //not yet ..  probably not gonna happen ..
    public static final String VER_5 = "http://json-schema.org/draft-05/schema#";


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
        this.wrap(new SchemaUriFetcher().fetch(origSrc, null, null).getJson());
    }

    public SchemaFetcher getSchemaFetcher() {
        return schemaFetcher;
    }

    public <O extends Schema> O setSchemaFetcher(SchemaFetcher schemaFetcher) {
        this.schemaFetcher = schemaFetcher;
        return (O)this;
    }

    public <O extends Schema> O setOrigSrc(URI origSrc) {
        this.origSrc = origSrc;
        return (O)this;
    }

    public URI getOrigSrc() {
        return origSrc;
    }


    /**
     *
     * @return empty schema from the same version as the current one
     */
    public abstract Schema getEmptySchema(String path);

    @Override
    public JsonElementWrapper setMetaInfoUri(URI uri) {
        throw new RuntimeException("Cannot set Schema on a Schema like this. Use setSchema method.");
    }

    @Override
    public <T extends JsonElementWrapper> T wrap(JsonObject jsonElement) {
        Schema schema = super.wrap(jsonElement);
        mergeWithRef();
        return (T) schema;
    }

    public Schema mergeAllRefs(){

        if(getItems()!=null) {
            for (Schema vals : getItems()) {
                vals.mergeAllRefs();
            }
        }
        if(getProperties()!=null) {
            for (Map.Entry<String, Schema> vals : getProperties()) {
                vals.getValue().mergeAllRefs();
            }
        }
        if(getPatternProperties()!=null) {
            for (Map.Entry<String, Schema> vals : getPatternProperties()) {
                vals.getValue().mergeAllRefs();
            }
        }
        if(getOneOf()!=null) {
            for (Schema vals : getOneOf()) {
                vals.mergeAllRefs();
            }
        }
        if(getAllOf()!=null) {
            for (Schema vals : getAllOf()) {
                vals.mergeAllRefs();
            }
        }
        if(getAnyOf()!=null) {
            for (Schema vals : getAnyOf()) {
                vals.mergeAllRefs();
            }
        }
        if(getNot()!=null) {
            getNot().mergeAllRefs();
        }

        return this;
    }

    private void mergeWithRef() {
        if(this.getRef()!=null && !this.getRef().trim().isEmpty()) {
            //populate values
            //if there are title and description already do not change those.
            Schema refSchema;
            if(schemaFetcher==null) schemaFetcher = new SchemaUriFetcher();
            refSchema = schemaFetcher.fetch(URI.create(this.getRef()), origSrc, URI.create(getId()));

            //TODO not really according to the specs, however specs not really clear what "$ref should precede all other..." means
            if (refSchema!=null) {
                setOrigSrc(refSchema.origSrc);
                setSchemaFetcher(refSchema.getSchemaFetcher());
                //we want to keep title and description for the top schema (these does not affect validation in any way)
                String oldTitle = getTitle();
                String oldDesc = getDescription();
                getJson().clear();
                merge(refSchema);
                getJson().put("title", oldTitle).put("description", oldDesc);
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

    /**
     * Should be URI
     * @param schemaId
     * @param <O>
     * @return
     */
    public <O extends Schema> O setId(String schemaId) {
        getJson().put("id", schemaId);
        return (O)this;
    }

    protected Schema setSchema(String schemaUri) {
        getJson().put("$schema", schemaUri);
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

    public Double getMultipleOf() {
        return getJson().optDouble("multipleOf");
    }

    public Double getMaximum() {
        return getJson().optDouble("maximum");
    }

    public boolean getExclusiveMaximum() {
        return getJson().optBoolean("exclusiveMaximum", false);
    }

    public Double getMinimum() {
        return getJson().optDouble("minimum");
    }

    public boolean getExclusiveMinimum() {
        return getJson().optBoolean("exclusiveMinimum", false);
    }

    public Integer getMaxLength() {
        return getJson().optInt("maxLength");
    }

    public Integer getMinLength() {
        return getJson().optInt("minLength");
    }

    public String getPattern() {
        return getJson().optString("pattern","");
    }

    //TODO can return also object
    public boolean getAdditionalItems() {
        return getJson().optBoolean("additionalItems", true);
    }

    public SchemaList getItems() {
        SchemaList res;
        if(getJson().opt("items") == null) {
            return null;
        }
        else if(getJson().opt("items").isJsonArray()) {
            return new SchemaList(getEmptySchema("items")).wrap(getJson().optJsonArray("items"));
        }
        else {
            res = new SchemaList(getEmptySchema("items"));
            res.add((Schema)getEmptySchema("items/0").wrap(getJson().optJsonObject("items")));
        }
        return res;
    }

    public Integer getMaxItems() {
        return getJson().optInt("maxItems");
    }

    public Integer getMinItems() {
        return getJson().optInt("minItems");
    }

    public boolean getUniqueItems() {
        return getJson().optBoolean("uniqueItems",false);
    }

    public Integer getMaxProperties() {
        return getJson().optInt("maxProperties");
    }

    public Integer getMinProperties() {
        return getJson().optInt("minProperties");
    }

    public List<String> getRequired() {
        return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("required"));
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
        return new SchemaMap(this.getEmptySchema("properties")).wrap(getJson().optJsonObject("properties"));
    }

    public SchemaMap getPatternProperties() {
        return new SchemaMap(this.getEmptySchema("patternProperties")).wrap(getJson().optJsonObject("patternProperties"));
    }

    public JsonObject getDependencies() {
        return getJson().optJsonObject("dependencies");
    }

    public JsonArray getEnum() {
        return getJson().optJsonArray("enum");
    }

    public List<String> getType() {
        ArrayList<String> res;
        if(getJson().opt("type")==null) return null;
        if(getJson().opt("type").isJsonArray()) {
            return new JsonStringArrayWrapper().wrap(getJson().optJsonArray("type"));
        }
        else {
            res = new ArrayList<String>();
            res.add(getJson().optString("type"));
        }
        return res;
    }


    //TODO will not pass memebers. use smth similar to SchemaMap
    public SchemaList getAllOf() {
        if(!getJson().has("allOf")) return null;
        return new SchemaList(getEmptySchema("allOf")).wrap(getJson().optJsonArray("allOf"));
    }

    public SchemaList getAnyOf() {
        if(!getJson().has("anyOf")) return null;
        return new SchemaList(getEmptySchema("anyOf")).wrap(getJson().optJsonArray("anyOf"));
    }

    public SchemaList getOneOf() {
        if(!getJson().has("oneOf")) return null;
        return new SchemaList(getEmptySchema("oneOf")).wrap(getJson().optJsonArray("oneOf"));
    }

    public Schema getNot() {
        if(!getJson().has("not")) return null;
        return (Schema)getEmptySchema("not").wrap(getJson().optJsonObject("not"));
    }

}
