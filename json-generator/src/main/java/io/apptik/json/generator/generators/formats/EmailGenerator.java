package io.apptik.json.generator.generators.formats;

import io.apptik.json.JsonElement;
import io.apptik.json.JsonString;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;

import java.util.ArrayList;

/**
 * Created by sandy on 6/11/2016.
 */
public class EmailGenerator extends JsonGenerator {
    public EmailGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public EmailGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    // This api is here but preferably should be in some util
    private String generateRandomString(int minChars, int maxChars) {
        int cnt = minChars;
        if(maxChars>minChars) {
            cnt += rnd.nextInt(maxChars - minChars);
        }
        String res = "";
        for (int i = 0; i < cnt; i++)
            res += (rnd.nextBoolean()) ? (char) (65 + rnd.nextInt(25)) : (char) (97 + rnd.nextInt(25));
        return res;
    }

    @Override
    public JsonElement generate() {
        ArrayList<String> hosts;
        ArrayList<String> locals;

        if (configuration != null && propertyName != null && configuration.emailHosts.get(propertyName) != null
                && configuration.emailHosts.get(propertyName).size() > 0) {
            hosts = configuration.emailHosts.get(propertyName);
        } else if (configuration != null && configuration.globalEmailHosts != null & configuration.globalEmailHosts.size() > 0) {
            hosts = configuration.globalEmailHosts;
        } else {
            //random host
            hosts = new ArrayList<String>();
            int minHostLen = 3;
            int maxHostLen = 10;
            if (configuration != null) {
                if (configuration.globalEmailHostLengthMin != null) {
                    minHostLen = configuration.globalEmailHostLengthMin;
                }
                if (configuration.globalEmailHostLengthMax != null) {
                    maxHostLen = configuration.globalEmailHostLengthMax;
                }
                if (propertyName != null) {
                    if (configuration.emailHostLengthMin.get(propertyName) != null) {
                        minHostLen = configuration.emailHostLengthMin.get(propertyName);
                    }
                    if (configuration.emailHostLengthMax.get(propertyName) != null) {
                        maxHostLen = configuration.emailHostLengthMax.get(propertyName);
                    }
                }
            }
            if (maxHostLen < 3) maxHostLen = 3;
            String sld = generateRandomString(Math.max(1, minHostLen - 2), maxHostLen - 2);
            String tld = generateRandomString(1, maxHostLen - sld.length() - 1);
            hosts.add(sld + "." + tld);
        }

        if (configuration != null && propertyName != null && configuration.emailLocalParts.get(propertyName) != null
                && configuration.emailLocalParts.get(propertyName).size() > 0) {
            locals = configuration.emailLocalParts.get(propertyName);
        } else if (configuration != null && configuration.globalEmailLocalParts != null & configuration.globalEmailLocalParts.size() > 0) {
            locals = configuration.globalEmailLocalParts;
        } else {
            //random local
            locals = new ArrayList<String>();
            int minlocalLen = 1;
            int maxlocalLen = 10;
            if (configuration != null) {
                if (configuration.globalEmailLocalPartLengthMin != null) {
                    minlocalLen = configuration.globalEmailLocalPartLengthMin;
                }
                if (configuration.globalEmailLocalPartLengthMax != null) {
                    maxlocalLen = configuration.globalEmailLocalPartLengthMax;
                }
                if (propertyName != null) {
                    if (configuration.emailLocalPartLengthMin.get(propertyName) != null) {
                        minlocalLen = configuration.emailLocalPartLengthMin.get(propertyName);
                    }
                    if (configuration.emailLocalPartLengthMax.get(propertyName) != null) {
                        maxlocalLen = configuration.emailLocalPartLengthMax.get(propertyName);
                    }
                }
            }

            locals.add(generateRandomString(minlocalLen, maxlocalLen));
        }


        // The assumption is the length of email strings
        // Can make it more configurable
        return new JsonString(
                locals.get(rnd.nextInt(locals.size()))
                        + "@"
                        + hosts.get(rnd.nextInt(hosts.size())));
    }
}
