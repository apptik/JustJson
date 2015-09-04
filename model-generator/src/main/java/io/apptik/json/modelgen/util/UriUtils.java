package io.apptik.json.modelgen.util;


import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtils {

    private final static Pattern lastBitPattern = Pattern.compile(".*/([^/?]+).*");
    private UriUtils() {

    }

    public static String getLastBit(URI uri) {
        Matcher m = lastBitPattern.matcher(uri.toString());
        if(m.matches()) {
            return m.group(1);
        }
        //should not happen as uri must be a valid URI
        throw new RuntimeException("Cannot get last bit from uri: " + uri);
    }


    public static String getSchemaId(URI uri) {
        return getLastBit(uri) + ((uri.getFragment()!=null)?"_"+uri.getFragment():"");
    }




}
