package io.apptik.json.examples.modelgenerator;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.modelgen.FtlGenerator;
import io.apptik.json.modelgen.Generator;
import io.apptik.json.modelgen.util.UriUtils;
import io.apptik.json.modelgen.util.WordUtils;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaJJv1;
import io.apptik.json.schema.SchemaV4;
import io.apptik.json.schema.fetch.SchemaUriFetcher;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ModelGeneratorExample {

    public static void main(String[] args) throws Exception {

        fromJson(new JsonObject()
                        .put("test string", "testValue")
                        .put("test int", 123)
                        .put("test double", 123.456)
                        .put("test bool", true)
                        .put("testNull", null)
                        .put("test array", new String[]{"1", "3", "5"})
                        .put("test Object", new HashMap<String, String>())

                , "testFromJson");

        Schema schema = new SchemaV4();
        //schema = new SchemaUriFetcher().fetch(URI.create("http://www.openthings.cc/schema/Person"), null, null);
        schema.wrap(JsonElement.readFrom("{\n" +
                "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                "    \"title\": \"Product\",\n" +
                "    \"description\": \"A product from Acme's catalog\",\n" +
                "    \"type\": \"object\",\n" +
                "    \"properties\": {\n" +
                "        \"id\": {\n" +
                "            \"description\": \"The unique identifier for a product\",\n" +
                "            \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"name\": {\n" +
                "            \"description\": \"Name of the product\",\n" +
                "            \"type\": \"string\"\n" +
                "        },\n" +
                "        \"price\": {\n" +
                "            \"type\": \"number\",\n" +
                "            \"minimum\": 0,\n" +
                "            \"exclusiveMinimum\": true\n" +
                "        },\n" +
                "        \"tags\": {\n" +
                "            \"type\": \"array\",\n" +
                "            \"items\": {\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            \"minItems\": 1,\n" +
                "            \"uniqueItems\": true\n" +
                "        }\n" +
                "    },\n" +
                "    \"required\": [\"id\", \"name\", \"price\"]\n" +
                "}").asJsonObject());
        fromSchema(schema);
    }

    private static void fromJson(JsonObject job, String wrapperName) throws Exception {
        Generator generator = new FtlGenerator();
        generator.fromJson(job, wrapperName, new File("out"));
    }

    private static void fromSchema(Schema schema) throws Exception {
        Generator generator = new FtlGenerator();
        generator.fromSchema(schema, null, new File("out"));
    }

}
