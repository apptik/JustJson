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
import org.djodjo.json.schema.SchemaArray;
import org.djodjo.json.schema.SchemaV4;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Map;

import static org.djodjo.json.schema.validation.CommonMatchers.isOfType;
import static org.djodjo.json.schema.validation.CommonMatchers.isSubPropertyValid;
import static org.hamcrest.Matchers.allOf;


public class SchemaV4Validator extends SchemaValidator<SchemaV4> {

    ArrayList<Matcher<? super JsonElement>> allMatchers =  new ArrayList<Matcher<? super JsonElement>>();
    public SchemaV4Validator(SchemaV4 schema) {
        super(schema);
        //allMatchers
        ArrayList<String> schemaType = schema.getType();
        if(schemaType != null && !schemaType.isEmpty()) {
            allMatchers.add(isOfType(schemaType));
        }

        SchemaArray schemaArray = schema.getProperties();
        if(schemaArray != null && schemaArray.length() > 0) {
            //TODO applies only for objects
            for(Map.Entry<String, Schema> entry : schemaArray) {
                allMatchers.add(isSubPropertyValid(entry.getValue().getDefaultValidator(), entry.getKey()));
            }
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
