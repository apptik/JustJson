/*
 * Copyright (C) 2014 Kalin Maldzhanski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apptik.json.generator.generators.formats;

import io.apptik.json.JsonElement;
import io.apptik.json.JsonString;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class UriGenerator extends JsonGenerator {

    public UriGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public UriGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        URI uri;
        String scheme;
        String host;
        String path;

        ArrayList<String> schemes;
        ArrayList<String> hosts;
        ArrayList<String> paths;

        if (configuration != null && propertyName != null && configuration.uriSchemes.get(propertyName) != null
                && configuration.uriSchemes.get(propertyName).size() > 0) {
            schemes = configuration.uriSchemes.get(propertyName);
        } else if (configuration != null && configuration.globalUriSchemes != null && configuration.globalUriSchemes.size() > 0) {
            schemes = configuration.globalUriSchemes;
        } else {
            schemes = new ArrayList<String>();
            schemes.add("http");
            schemes.add("https");
            schemes.add("ftp");
            schemes.add("file");
            schemes.add("resource");
            schemes.add(null);
        }

        if (configuration != null && propertyName != null && configuration.uriHosts.get(propertyName) != null
                && configuration.uriHosts.get(propertyName).size() > 0) {
            hosts = configuration.uriHosts.get(propertyName);
        } else if (configuration != null && configuration.globalUriHosts != null & configuration.globalUriHosts.size() > 0) {
            hosts = configuration.globalUriHosts;
        } else {
            hosts = new ArrayList<String>();
            hosts.add("google.com");
            hosts.add("yahoo.com");
            hosts.add("bing.com");
            hosts.add("djodjo.org");
            hosts.add(null);
        }


        if (configuration != null && propertyName != null && configuration.uriPaths.get(propertyName) != null
                && configuration.uriPaths.get(propertyName).size() > 0) {
            paths = configuration.uriPaths.get(propertyName);
        } else if (configuration != null && configuration.globalUriPaths != null && configuration.globalUriPaths.size() > 0) {
            paths = configuration.globalUriPaths;
        } else {
            paths = new ArrayList<String>();
            //TODO check for config vars
            int pathMinLen = 3;
            int pathMaxLen = rnd.nextInt(33);
            if (configuration != null) {
                if (configuration.globalUriPathLengthMin != null) {
                    pathMinLen = configuration.globalUriPathLengthMin;
                }
                if (configuration.globalUriPathLengthMax != null) {
                    pathMaxLen = configuration.globalUriPathLengthMax;
                }
                if (propertyName != null) {
                    if (configuration.uriPathLengthMin.get(propertyName) != null) {
                        pathMinLen = configuration.uriPathLengthMin.get(propertyName);
                    }
                    if (configuration.uriPathLengthMax.get(propertyName) != null) {
                        pathMaxLen = configuration.uriPathLengthMax.get(propertyName);
                    }
                }
            }
            String res = "";
            int cnt;
            if (pathMaxLen > 0) {
                res = "/";
                cnt = pathMinLen;
                for (int i = 0; i < cnt; i++)
                    res += (rnd.nextBoolean()) ? (char) (65 + rnd.nextInt(25)) : (char) (97 + rnd.nextInt(25));
            }
            while (res.length() < pathMaxLen) {
                res += "/";
                cnt = rnd.nextInt(7);
                for (int i = 0; i < cnt && res.length() < pathMaxLen; i++)
                    res += (rnd.nextBoolean()) ? (char) (65 + rnd.nextInt(25)) : (char) (97 + rnd.nextInt(25));
            }
            paths.add(res);
        }


//        if(configuration!=null) {
//            if (configuration.globalStringLengthMin!=null) minChars = configuration.globalStringLengthMin;
//            if (configuration.globalStringLengthMax!=null) maxChars = configuration.globalStringLengthMax;
//            if (propertyName != null ) {
//                if (configuration.stringLengthMin.get(propertyName)!=null) minChars = configuration.stringLengthMin.get(propertyName);
//                if (configuration.stringLengthMax.get(propertyName)!=null) maxChars = configuration.stringLengthMax.get(propertyName);
//
//                if (configuration.stringPredefinedValues.get(propertyName) != null) {
//                    return new JsonString(configuration.stringPredefinedValues.get(propertyName).get(rnd.nextInt(configuration.stringPredefinedValues.get(propertyName).size())));
//                }
//            }
//
//        }


        scheme = schemes.get(rnd.nextInt(schemes.size()));
        host = hosts.get(rnd.nextInt(hosts.size()));
        if (paths.size() == 1) path = paths.get(0);
        else
            path = paths.get(rnd.nextInt(paths.size()));

        try {
            uri = new URI(scheme, host, path, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("We must not be here!");
        }

        return new JsonString(uri.toString());
    }
}
