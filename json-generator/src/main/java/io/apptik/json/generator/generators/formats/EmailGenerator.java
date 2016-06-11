package io.apptik.json.generator.generators.formats;

import io.apptik.json.JsonElement;
import io.apptik.json.JsonString;
import io.apptik.json.generator.Generator;
import io.apptik.json.generator.GeneratorConfig;
import io.apptik.json.schema.Schema;

/**
 * Created by sandy on 6/11/2016.
 */
public class EmailGenerator extends Generator {
    public EmailGenerator(Schema schema, GeneratorConfig configuration) {
        super(schema, configuration);
    }

    public EmailGenerator(Schema schema, GeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    // This api is here but preferably should be in some util
    private String generateRandomString(int minChars, int maxChars){
        int cnt = minChars + rnd.nextInt(maxChars-minChars);
        String res = "";
        for(int i=0;i<cnt;i++) res += (rnd.nextBoolean())? (char)(65 + rnd.nextInt(25)):(char)(97 + rnd.nextInt(25));
        return res;
    }

    @Override
    public JsonElement generate(){
        // The assumption is the length of email strings
        // Can make it more configurable
        return new JsonString(generateRandomString(1,10)
                                + "@"
                                + generateRandomString(1,10)
                                + "."
                                + generateRandomString(2,6));
    }
}
