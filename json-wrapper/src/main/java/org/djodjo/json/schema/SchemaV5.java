package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;
import org.djodjo.json.Validator;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.schema.validation.SchemaV5Validator;

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
    public SchemaV5 addTitle(String title, String locale) {
        JsonElement tit = getJson().opt("title");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("title");
            try {
                getJson().put("title", new JsonObject());
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }  else if(tit==null) {
            try {
                getJson().put("title", new JsonObject());
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }


        try {
            getJson().optJsonObject("title").put(locale, title);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
    }

    //replaces title if not object
    public SchemaV5 addDescription(String title, String locale) {
        JsonElement tit = getJson().opt("description");
        if(tit!=null && !tit.isJsonObject()) {
            getJson().remove("description");
            try {
                getJson().put("description", new JsonObject());
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }  else if(tit==null) {
            try {
                getJson().put("description", new JsonObject());
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }


        try {
            getJson().optJsonObject("description").put(locale, title);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
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
