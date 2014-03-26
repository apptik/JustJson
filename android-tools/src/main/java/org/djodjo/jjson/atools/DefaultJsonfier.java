package org.djodjo.jjson.atools;


import android.view.View;
import android.view.ViewGroup;

import org.djodjo.json.JsonElementWrapper;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.schema.Schema;

public class DefaultJsonfier implements Jsonfier {


    private String prefix4All = "";
    private String postfix4All = "";



    @Override
    public JsonObjectWrapper jsonfy(View form, JsonObjectWrapper container, boolean validate) {
        return null;
    }

    @Override
    public ViewGroup inflateJson(Schema schema, ViewGroup container) {
        return container;
    }

    @Override
    public ViewGroup fillInJson(JsonElementWrapper src, ViewGroup container, boolean addMissingViews) {

        return container;
    }
}
