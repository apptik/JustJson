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

package org.djodjo.json.aws;


import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonString;
import org.djodjo.json.exception.JsonException;
import org.djodjo.json.wrapper.TypedJsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DynamoDb2Item extends TypedJsonObject<AttributeValue> {


    public static List<DynamoDb2Item> createFromList(List<Map<String, AttributeValue>> vals) {
       List<DynamoDb2Item> res = new ArrayList<DynamoDb2Item>();
       for(Map<String, AttributeValue> item:vals) {
           res.add(DynamoDb2Item.createFrom(item));
       }
       return res;
    }


    public static DynamoDb2Item createFrom(Map<String, AttributeValue> val) {
        return new DynamoDb2Item().putAll(val);
    }

    @Override
    protected AttributeValue get(JsonElement el, String key) {
        AttributeValue atVal = new AttributeValue();
        if(el.isNumber()) {
            atVal.withN(el.toString());
        } else if(el.isJsonArray()) {
            boolean isNumberArr = true;
            ArrayList<String> attrList =  new ArrayList<String>();
            for (JsonElement arrEl : el.asJsonArray()) {
                if (isNumberArr && !arrEl.isNumber()) isNumberArr = false;
                attrList.add(arrEl.toString());
            }
            if(isNumberArr) {
                atVal.withNS(attrList);
            } else {
                atVal.withSS(attrList);
            }
        } else {
            atVal.withS(el.toString());
        }

        return atVal;
    }

    @Override
    protected JsonElement to(AttributeValue value) {
        //the output JASON care only for one type of attribute.
        JsonElement el = new JsonString("");
        try {
            if(value.getS()!=null) {
                el = JsonElement.wrap(value.getS());
            } else if(value.getN()!=null) {
                el = JsonElement.wrap(value.getN());
            }
            else if(value.getSS()!=null) {
                el = JsonElement.wrap(value.getSS());
            }
            else if(value.getNS()!=null) {
                el = JsonElement.wrap(value.getNS());
            }
            else if(value.getB()!=null) {
                el = JsonElement.wrap(value.getB());
            }
            else if(value.getN()!=null) {
                el = JsonElement.wrap(value.getBS());
            }

        } catch (JsonException e) {
            e.printStackTrace();
        }
        return el;
    }
}
