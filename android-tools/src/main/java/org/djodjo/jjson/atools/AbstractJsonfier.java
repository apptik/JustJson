package org.djodjo.jjson.atools;


import android.view.View;

import org.djodjo.json.JsonObject;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.schema.Schema;

public abstract class AbstractJsonfier implements Jsonfier {


    protected abstract String getPrefix4All();
    protected abstract String getPostfix4All();

    @Override
    public JsonObjectWrapper jsonfy(View form, JsonObjectWrapper jsonObjectWrapper) {
        return jsonObjectWrapper.wrap(jsonfy(form, jsonObjectWrapper.fetchJsonSchema()));
    }

    @Override
    public JsonObject jsonfy(View form, Schema schema) {
        return null;
    }

    @Override
    public View inflateJson(JsonObjectWrapper src, View container) {
        return null;
    }
}
