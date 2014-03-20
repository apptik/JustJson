package org.djodjo.jjson.atools;


import android.view.View;

import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.schema.Schema;

public interface Jsonfier {

    JsonObjectWrapper jsonfy(View form, JsonObjectWrapper schema);

    JsonObject jsonfy(View form, Schema schema);

    View inflateJson(JsonObjectWrapper src, View container);
}
