package io.apptik.json.modelgen;


import io.apptik.json.JsonElement;
import io.apptik.json.modelgen.util.WordUtils;
import io.apptik.json.schema.Schema;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.apptik.json.JsonElement.*;

public class Util {
    final static String JOB_FTL_TEMPLATE = "JsonObjectWrapper.ftl";

    static Map<String, String> javaTypeMap = new HashMap<String, String>();
    static Map<String, String> optMethodMap = new HashMap<String, String>();

    static {
        javaTypeMap.put(TYPE_ARRAY, "JsonArray");
        javaTypeMap.put(TYPE_OBJECT, "JsonObject");
        javaTypeMap.put(TYPE_BOOLEAN, "Boolean");
        javaTypeMap.put(TYPE_INTEGER, "Integer");
        javaTypeMap.put(TYPE_NUMBER, "Double");
        javaTypeMap.put(TYPE_STRING, "String");
        javaTypeMap.put(TYPE_NULL, "JsonElement");

        optMethodMap.put(TYPE_ARRAY, "JsonArray");
        optMethodMap.put(TYPE_OBJECT, "JsonObject");
        optMethodMap.put(TYPE_BOOLEAN, "Boolean");
        optMethodMap.put(TYPE_INTEGER, "Int");
        optMethodMap.put(TYPE_NUMBER, "Double");
        optMethodMap.put(TYPE_STRING, "String");
        optMethodMap.put(TYPE_NULL, "");

    }

    private Util() {
    }

    public static String getJavaType(String jsonType) {
        String res = javaTypeMap.get(jsonType);

        if (res == null) {
            throw new RuntimeException("Unknown type");
        } else {
            return res;
        }
    }

    public static String getOptMethod(String jsonType) {
        String res = optMethodMap.get(jsonType);

        if (res == null) {
            throw new RuntimeException("Unknown type");
        } else {
            return res;
        }
    }

    public static Generator.Prop getPropFromJsonEntry(String key, JsonElement el) {
        Generator.Prop p = new Generator.Prop();
        p.origKey = key;
        p.methodName = WordUtils.capitalize(p.origKey);
        p.returnType = Util.getJavaType(el.getJsonType());
        p.optMethod = Util.getOptMethod(el.getJsonType());
        return p;
    }

    public static Generator.Prop getPropFromJsonSchema(String key, Schema schema) {
        Generator.Prop p = new Generator.Prop();
        p.origKey = key;
        //todo add conf to choose key or schema title if exists
        // p.origKey = schema.getTitle();
        p.methodName = WordUtils.capitalize(p.origKey);
        String type;
        if(schema.getType().size() == 1) {
            type = schema.getType().get(0);
        } else {
            //hack to return JsonElement as this property may be of many types
            type = JsonElement.TYPE_NULL;
        }
        p.returnType = Util.getJavaType(type);
        p.optMethod = Util.getOptMethod(type);
        return p;
    }



    public static InputStream getResource(String name) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(name);
        return is;
    }

    public static InputStream getJOBftl() {
        return getResource(JOB_FTL_TEMPLATE);
    }

}
