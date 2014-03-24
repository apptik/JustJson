package org.djodjo.jjson.atools;


import android.view.View;

import org.djodjo.json.JsonElementWrapper;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.schema.Schema;

public abstract class AbstractJsonfier implements Jsonfier {


    protected abstract String getPrefix4All();
    protected abstract String getPostfix4All();

    @Override
    public JsonObjectWrapper jsonfy(View form, JsonObjectWrapper container, boolean validate) {
        return null;
    }

    @Override
    public View inflateJson(Schema schema, View container) {
        return null;
    }

    @Override
    public View fillInJson(JsonElementWrapper src, View container, boolean addMissingViews) {
        return null;
    }
}
