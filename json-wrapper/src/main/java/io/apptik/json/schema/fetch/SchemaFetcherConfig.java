package io.apptik.json.schema.fetch;


import java.util.HashMap;

public class SchemaFetcherConfig {
    //uri anatomy:
    //[scheme:][//authority][path][?query][#fragment]
    //where authority: [user-info@]host[:port]

    public HashMap<String, String> uriSchemeReplacements =  new HashMap<String, String>();
    public HashMap<String, String> uriAuthorityReplacements =  new HashMap<String, String>();
    public HashMap<String, String> uriPathReplacements =  new HashMap<String, String>();
    public HashMap<String, String> uriQueryReplacements =  new HashMap<String, String>();


    public SchemaFetcherConfig withUriSchemeReplacement(String value, String replacement) {
        uriSchemeReplacements.put(value, replacement);
        return this;
    }
    public SchemaFetcherConfig withUriAuthorityReplacement(String value, String replacement) {
        uriAuthorityReplacements.put(value, replacement);
        return this;
    }
    public SchemaFetcherConfig withUriPathReplacement(String value, String replacement) {
        uriPathReplacements.put(value, replacement);
        return this;
    }
    public SchemaFetcherConfig withUriQueryReplacement(String value, String replacement) {
        uriQueryReplacements.put(value, replacement);
        return this;
    }

}
