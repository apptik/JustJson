package io.apptik.json.perf;



public enum JsonInput {


    SmallObject {
        @Override
        String create() {
            return "{\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"view\",\n" +
                    "              \"enum\": [\n" +
                    "                \"view\",\n" +
                    "                \"embed\",\n" +
                    "                \"edit\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Scope under which the request is made; determines fields present in response.\"\n" +
                    "            }";
        }
    },
    SmallArray {
        @Override
        String create() {
            return "[\n" +
                    "        \"GET\",\n" +
                    "        \"POST\",\n" +
                    "        \"PUT\",\n" +
                    "        \"PATCH\",\n" +
                    "        \"DELETE\"\n" +
                    "      ]";
        }
    },
    MediumObject {
        @Override
        String create() {
            return " {\n" +
                    "          \"methods\": [\n" +
                    "            \"GET\"\n" +
                    "          ],\n" +
                    "          \"args\": {\n" +
                    "            \"context\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"view\",\n" +
                    "              \"enum\": [\n" +
                    "                \"view\",\n" +
                    "                \"embed\",\n" +
                    "                \"edit\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Scope under which the request is made; determines fields present in response.\"\n" +
                    "            },\n" +
                    "            \"page\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": 1,\n" +
                    "              \"description\": \"Current page of the collection.\"\n" +
                    "            },\n" +
                    "            \"per_page\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": 10,\n" +
                    "              \"description\": \"Maximum number of items to be returned in result set.\"\n" +
                    "            },\n" +
                    "            \"search\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Limit results to those matching a string.\"\n" +
                    "            },\n" +
                    "            \"after\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Limit response to resources published after a given ISO8601 compliant date.\"\n" +
                    "            },\n" +
                    "            \"author\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Limit result set to posts assigned to specific authors.\"\n" +
                    "            },\n" +
                    "            \"author_exclude\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Ensure result set excludes posts assigned to specific authors.\"\n" +
                    "            },\n" +
                    "            \"before\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Limit response to resources published before a given ISO8601 compliant date.\"\n" +
                    "            },\n" +
                    "            \"exclude\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Ensure result set excludes specific ids.\"\n" +
                    "            },\n" +
                    "            \"include\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Limit result set to specific ids.\"\n" +
                    "            },\n" +
                    "            \"offset\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Offset the result set by a specific number of items.\"\n" +
                    "            },\n" +
                    "            \"order\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"desc\",\n" +
                    "              \"enum\": [\n" +
                    "                \"asc\",\n" +
                    "                \"desc\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Order sort attribute ascending or descending.\"\n" +
                    "            },\n" +
                    "            \"orderby\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"date\",\n" +
                    "              \"enum\": [\n" +
                    "                \"date\",\n" +
                    "                \"id\",\n" +
                    "                \"include\",\n" +
                    "                \"title\",\n" +
                    "                \"slug\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Sort collection by object attribute.\"\n" +
                    "            },\n" +
                    "            \"slug\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Limit result set to posts with a specific slug.\"\n" +
                    "            },\n" +
                    "            \"status\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"publish\",\n" +
                    "              \"description\": \"Limit result set to posts assigned a specific status.\"\n" +
                    "            },\n" +
                    "            \"filter\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Use WP Query arguments to modify the response; private query vars require appropriate authorization.\"\n" +
                    "            },\n" +
                    "            \"categories\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Limit result set to all items that have the specified term assigned in the categories taxonomy.\"\n" +
                    "            },\n" +
                    "            \"tags\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": [],\n" +
                    "              \"description\": \"Limit result set to all items that have the specified term assigned in the tags taxonomy.\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }";
        }
    },
    MediumArray {
        @Override
        String create() {
            return "[\n" +
                    "        {\n" +
                    "          \"methods\": [\n" +
                    "            \"GET\"\n" +
                    "          ],\n" +
                    "          \"args\": {\n" +
                    "            \"context\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": \"view\",\n" +
                    "              \"enum\": [\n" +
                    "                \"view\",\n" +
                    "                \"embed\",\n" +
                    "                \"edit\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Scope under which the request is made; determines fields present in response.\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"methods\": [\n" +
                    "            \"POST\",\n" +
                    "            \"PUT\",\n" +
                    "            \"PATCH\"\n" +
                    "          ],\n" +
                    "          \"args\": {\n" +
                    "            \"date\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The date the object was published, in the site's timezone.\"\n" +
                    "            },\n" +
                    "            \"date_gmt\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The date the object was published, as GMT.\"\n" +
                    "            },\n" +
                    "            \"password\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"A password to protect access to the post.\"\n" +
                    "            },\n" +
                    "            \"slug\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"An alphanumeric identifier for the object unique to its type.\"\n" +
                    "            },\n" +
                    "            \"status\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"enum\": [\n" +
                    "                \"publish\",\n" +
                    "                \"future\",\n" +
                    "                \"draft\",\n" +
                    "                \"pending\",\n" +
                    "                \"private\",\n" +
                    "                \"wc-pending\",\n" +
                    "                \"wc-processing\",\n" +
                    "                \"wc-on-hold\",\n" +
                    "                \"wc-completed\",\n" +
                    "                \"wc-cancelled\",\n" +
                    "                \"wc-refunded\",\n" +
                    "                \"wc-failed\"\n" +
                    "              ],\n" +
                    "              \"description\": \"A named status for the object.\"\n" +
                    "            },\n" +
                    "            \"title\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The title for the object.\"\n" +
                    "            },\n" +
                    "            \"content\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The content for the object.\"\n" +
                    "            },\n" +
                    "            \"author\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The id for the author of the object.\"\n" +
                    "            },\n" +
                    "            \"excerpt\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The excerpt for the object.\"\n" +
                    "            },\n" +
                    "            \"featured_media\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The id of the featured media for the object.\"\n" +
                    "            },\n" +
                    "            \"comment_status\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"enum\": [\n" +
                    "                \"open\",\n" +
                    "                \"closed\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Whether or not comments are open on the object.\"\n" +
                    "            },\n" +
                    "            \"ping_status\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"enum\": [\n" +
                    "                \"open\",\n" +
                    "                \"closed\"\n" +
                    "              ],\n" +
                    "              \"description\": \"Whether or not the object can be pinged.\"\n" +
                    "            },\n" +
                    "            \"format\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"enum\": [\n" +
                    "                \"standard\",\n" +
                    "                \"aside\",\n" +
                    "                \"chat\",\n" +
                    "                \"gallery\",\n" +
                    "                \"link\",\n" +
                    "                \"image\",\n" +
                    "                \"quote\",\n" +
                    "                \"status\",\n" +
                    "                \"video\",\n" +
                    "                \"audio\"\n" +
                    "              ],\n" +
                    "              \"description\": \"The format for the object.\"\n" +
                    "            },\n" +
                    "            \"sticky\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"Whether or not the object should be treated as sticky.\"\n" +
                    "            },\n" +
                    "            \"categories\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The terms assigned to the object in the category taxonomy.\"\n" +
                    "            },\n" +
                    "            \"tags\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"description\": \"The terms assigned to the object in the post_tag taxonomy.\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"methods\": [\n" +
                    "            \"DELETE\"\n" +
                    "          ],\n" +
                    "          \"args\": {\n" +
                    "            \"force\": {\n" +
                    "              \"required\": false,\n" +
                    "              \"default\": false,\n" +
                    "              \"description\": \"Whether to bypass trash and force deletion.\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]";
        }
    }
    ;
    abstract String create();
}
