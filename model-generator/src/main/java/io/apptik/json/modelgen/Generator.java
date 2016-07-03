package io.apptik.json.modelgen;


import freemarker.template.TemplateException;
import io.apptik.json.JsonObject;
import io.apptik.json.schema.Schema;

import java.io.File;
import java.io.IOException;

public interface Generator {

    /**
     *
     * @param job Json Object to create wrapper for
     * @param wrapperName the wrapper class name. May not be null
     * @param outDir the output directory
     * @throws IOException
     */
    void fromJson(JsonObject job, String wrapperName, File outDir) throws IOException;

    /**
     *
     * @param schema the schema describing the Json Object to create wrapper for
     * @param wrapperName the wrapper class name. If null then schema title is used instead.
     * @param outDir the output directory
     * @throws IOException
     */
    void fromSchema(Schema schema, String wrapperName, File outDir) throws IOException;;

    class Prop {
        public String origKey;
        public String methodName;
        public String returnType;
        public String optMethod;

        @Override
        public String toString() {
            return "Prop{" +
                    "origKey='" + origKey + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", returnType='" + returnType + '\'' +
                    ", optMethod='" + optMethod + '\'' +
                    '}';
        }
    }
}
