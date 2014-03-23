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

package org.djodjo.json.schema.validation;


import org.djodjo.json.JsonElement;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaMap;
import org.djodjo.json.schema.SchemaV4;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.Map;

import static org.djodjo.json.schema.validation.CommonMatchers.isLessOrEqualThan;
import static org.djodjo.json.schema.validation.CommonMatchers.isLessThan;
import static org.djodjo.json.schema.validation.CommonMatchers.isMoreOrEqualThan;
import static org.djodjo.json.schema.validation.CommonMatchers.isMoreThan;
import static org.djodjo.json.schema.validation.CommonMatchers.isMultipleOf;
import static org.djodjo.json.schema.validation.CommonMatchers.isOfType;
import static org.djodjo.json.schema.validation.CommonMatchers.isPresent;
import static org.djodjo.json.schema.validation.CommonMatchers.isSubPropertyValid;
import static org.hamcrest.Matchers.allOf;


public class SchemaV4Validator extends SchemaValidator<SchemaV4> {

    ArrayList<Matcher<? super JsonElement>> allMatchers =  new ArrayList<Matcher<? super JsonElement>>();
    public SchemaV4Validator(SchemaV4 schema) {
        super(schema);
        //allMatchers
        putMatchers4Numeric();
        putMatchers4String();
        putMatchers4Array();
        putMatcher4Object();
        putMatchers4Common();


    }


    private void putMatchers4Numeric() {
        double multipleOf = schema.getMultipleOf();
        if(multipleOf != Double.MIN_VALUE && multipleOf>0) {
            allMatchers.add(isMultipleOf(multipleOf));
        }

        double maximum = schema.getMaximum();
        if(maximum != Double.MAX_VALUE) {
            if(schema.getExclusiveMaximum()) {
                allMatchers.add(isLessThan(maximum));
            } else {
                allMatchers.add(isLessOrEqualThan(maximum));
            }
        }

        double minimum = schema.getMinimum();
        if(minimum != Double.MIN_VALUE) {
            if(schema.getExclusiveMinimum()) {
                allMatchers.add(isMoreThan(minimum));
            } else {
                allMatchers.add(isMoreOrEqualThan(minimum));
            }
        }

    }
    private void putMatchers4String() {

    }
    private void putMatchers4Array() {

    }

    private void putMatcher4Object() {
        ArrayList<String> required = schema.getRequired();
        if(required != null && !required.isEmpty()) {
            for(String param : required) {
                allMatchers.add(isPresent(param));
            }
        }


        SchemaMap schemaMap = schema.getProperties();
        if(schemaMap != null && schemaMap.length() > 0) {
            //TODO applies only for objects
            for(Map.Entry<String, Schema> entry : schemaMap) {
                allMatchers.add(isSubPropertyValid(entry.getValue().getDefaultValidator(), entry.getKey()));
            }
        }

    }

    private void putMatchers4Common() {
        ArrayList<String> schemaType = schema.getType();
        if(schemaType != null && !schemaType.isEmpty()) {
            allMatchers.add(isOfType(schemaType));
        }
    }

    @Override
    protected boolean doValidate(JsonElement el, StringBuilder sb) {
        //check if empty schema
        if(allMatchers == null || allMatchers.isEmpty())
            return true;

        Matcher<JsonElement> matcher = allOf(allMatchers);

        return matcher.matches(el);
    }
}
