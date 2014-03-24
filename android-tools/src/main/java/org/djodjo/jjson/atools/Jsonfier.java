package org.djodjo.jjson.atools;


import android.view.View;

import org.djodjo.json.JsonElementWrapper;
import org.djodjo.json.JsonObjectWrapper;
import org.djodjo.json.schema.Schema;

public interface Jsonfier {

    /**
     * Wraps Json data into the container object from UI data
     * @param form the View container where the data should be filled in and extracted
     * @param container empty wrapper that will hold the data. If schema is set in the wrapper it can be used for validation of the data. Note that if Wrapper is not full previous data will be discarded
     * @param validate if true and schema is set validation will be performed.
     * @return the container JsonWrapper this time full
     */
    JsonObjectWrapper jsonfy(View form, JsonObjectWrapper container, boolean validate);

    /**
     * Inflate UI from schema. Data will be filled in if "default" values are available
     * @param schema the
     * @param container
     * @return the container View
     */
    View inflateJson(Schema schema, View container);


    /**
     * Fills in existing data into existing UI
     * @param src
     * @param container
     * @param addMissingViews add views for definitions in the Wrapper/Schema where not present in the UI
     * @return the container View
     */
    View fillInJson(JsonElementWrapper src, View container, boolean addMissingViews);
}
