package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.Validator;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.schema.validation.SchemaV5Validator;

import java.util.Map;

public class SchemaV5 extends Schema {

    public SchemaV5() {
        super();
        setSchema("http://json-schema.org/draft-05/schema#");
    }

    @Override
    public Schema getEmptySchema(String path) {
        return new SchemaV5().setSchemaFetcher(schemaFetcher).setOrigSrc(this.origSrc.resolve(path));
    }

    @Override
    public Validator getDefaultValidator() {
        return new SchemaV5Validator(this);
    }


    public String getTitle(String locale) {
        String res = "";

        if (getJson().opt("title").isString()) {
            res = getJson().optString("title") ;
        } else {
            JsonObject titles = getJson().optJsonObject("title");
            if(titles!=null && titles.length()>0) {
                res = titles.optString(locale);
            } else {
                res = null;
            }
        }

        return res;
    }

    @Override
    public String getTitle() {
        String res = null;

        if(getJson().opt("title").isString()) {
            res = getJson().optString("title") ;
        } else {
            JsonObject titles = getJson().optJsonObject("title");
            if(titles!=null && titles.length()>0) {
                res = titles.valuesSet().toArray(new String[0])[0];
            } else {
                res = null;
            }
        }


        return res;
    }

    public JsonObject getTitles() {
        JsonObject res = new JsonObject();

        if(getJson().opt("title").isString()) {
            try {
                res.put("default", getJson().optString("title"));
            } catch (JsonException e) {
                e.printStackTrace();
            }
        } else {
            res = getJson().optJsonObject("title");
        }

        return res;
    }

    //replaces title if not object
    public <O extends SchemaV5> O addTitle(String title, String locale) {
        JsonElement tit = getJson().opt("title");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("title");
            getJson().put("title", new JsonObject());

        }  else if(tit==null) {
            getJson().put("title", new JsonObject());
        }

        getJson().getJsonObject("title").put(locale, title);

        return (O)this;
    }

    public <O extends SchemaV5> O addTitles(Map<String, String>  titles) {
        JsonElement tit = getJson().opt("title");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("title");
            getJson().put("title", titles);

        }  else if(tit==null) {
            getJson().put("title", titles);
        }

        return (O)this;
    }

    //replaces description if not object
    public <O extends SchemaV5> O addDescription(String title, String locale) {
        JsonElement tit = getJson().opt("description");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("description");
            getJson().put("description", new JsonObject());
        }  else if(tit==null) {
            getJson().put("description", new JsonObject());
        }
        getJson().getJsonObject("description").put(locale, title);
        return (O)this;
    }

    public <O extends SchemaV5> O addDescriptions(Map<String, String> descriptions) {
        JsonElement tit = getJson().opt("description");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("description");
            getJson().put("description", descriptions);

        }  else if(tit==null) {

            getJson().put("description", descriptions);

        }
        return (O)this;
    }



    public String getDescription(String locale) {
        String res = "";

        if (getJson().opt("description").isString()) {
            res = getJson().optString("description") ;
        } else {
            JsonObject titles = getJson().optJsonObject("description");
            if(titles!=null && titles.length()>0) {
                res = titles.optString(locale);
            } else {
                res = null;
            }
        }

        return res;
    }

    @Override
    public String getDescription() {
        String res = null;

        if(getJson().opt("description").isString()) {
            res = getJson().optString("description") ;
        } else {
            JsonObject titles = getJson().optJsonObject("description");
            if(titles!=null && titles.length()>0) {
                res = titles.valuesSet().toArray(new String[0])[0];
            } else {
                res = null;
            }
        }


        return res;
    }

    public JsonObject getDescriptions() {
        JsonObject res = new JsonObject();

        if(getJson().opt("description").isString()) {
            try {
                res.put("default", getJson().optString("description"));
            } catch (JsonException e) {
                e.printStackTrace();
            }
        } else {
            res = getJson().optJsonObject("description");
        }

        return res;
    }





}
