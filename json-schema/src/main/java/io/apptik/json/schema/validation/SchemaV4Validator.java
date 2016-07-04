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

package io.apptik.json.schema.validation;


import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaList;
import io.apptik.json.schema.SchemaMap;
import io.apptik.json.schema.SchemaV4;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.*;

import static io.apptik.json.schema.validation.CommonMatchers.*;
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
        Double multipleOf = schema.getMultipleOf();
        if(multipleOf != null && multipleOf>0) {
            allMatchers.add(isMultipleOf(multipleOf));
        }

        Double maximum = schema.getMaximum();
        if(maximum!=null) {
            if(schema.getExclusiveMaximum()) {
                allMatchers.add(isLessThan(maximum));
            } else {
                allMatchers.add(isLessOrEqualThan(maximum));
            }
        }

        Double minimum = schema.getMinimum();
        if(minimum!=null) {
            if(schema.getExclusiveMinimum()) {
                allMatchers.add(isMoreThan(minimum));
            } else {
                allMatchers.add(isMoreOrEqualThan(minimum));
            }
        }

    }

    private void putMatchers4String() {

        Integer maxLength = schema.getMaxLength();
        if(maxLength != null) {
            allMatchers.add(withCharsLessOrEqualTo(maxLength));
        }

        Integer minLength = schema.getMinLength();
        if(minLength!=null && minLength > 0) {
            allMatchers.add(withCharsMoreOrEqualTo(minLength));
        }

        String pattern = schema.getPattern();
        if(pattern != null && !pattern.isEmpty()) {
            allMatchers.add(matchesPattern(pattern));
        }



    }
    private void putMatchers4Array() {

        SchemaList items = schema.getItems();
        if(items != null && !items.isEmpty()) {
            if(items.size() == 1) {
                //single type for array items
                allMatchers.add(areItemsValid(items.get(0).getDefaultValidator()));
            } else {
                //tuple typing
                //first check if schemas needs to be the same number as the instance items
                if(!schema.getAdditionalItems()) {
                    allMatchers.add(doesItemCountMatches(items.size()));
                }

                //then check if available items are according to provided schemas
                for(int i=0;i<items.size();i++) {
                    allMatchers.add(isItemValid(items.get(i).getDefaultValidator(), i));
                }

            }
        }

        Integer maxItems = schema.getMaxItems();
        if(maxItems !=null) {
            allMatchers.add(maxItems(maxItems));
        }

        Integer minItems = schema.getMinItems();
        if(minItems != null) {
            allMatchers.add(minItems(minItems));
        }

        if(schema.getUniqueItems()) {
            allMatchers.add(areItemsUnique());
        }


    }

    private void putMatcher4Object() {

        Integer maxProperties = schema.getMaxProperties();
        if(maxProperties !=null) {
            allMatchers.add(maxProperties(maxProperties));
        }

        Integer minProperties = schema.getMinProperties();
        if(minProperties != null && minProperties > 0) {
            allMatchers.add(minProperties(minProperties));
        }

        List<String> required = schema.getRequired();
        if(required != null && !required.isEmpty()) {
            for(String param : required) {
                allMatchers.add(isPropertyPresent(param));
            }
        }

        //validates only child properties if any found matching the property names
        SchemaMap propertiesSchemaMap = schema.getProperties();
        if(propertiesSchemaMap != null && propertiesSchemaMap.length() > 0) {
            for(Map.Entry<String, Schema> entry : propertiesSchemaMap) {
                allMatchers.add(isPropertyValid(entry.getValue().getDefaultValidator(), entry.getKey()));
            }
        }

        //validates only child properties if any found matching the property patterns
        SchemaMap patternPropertiesSchemaMap = schema.getPatternProperties();
        if(patternPropertiesSchemaMap != null && patternPropertiesSchemaMap.length() > 0) {
            for(Map.Entry<String, Schema> entry : patternPropertiesSchemaMap) {
                allMatchers.add(isPropertyPatternValid(entry.getValue().getDefaultValidator(), entry.getKey()));
            }
        }

        boolean additionalProperties = schema.getAdditionalProperties();
        if(!additionalProperties) {
            Set proppertiesSet;
            Set patternPropertiesSet;
            if(propertiesSchemaMap == null || propertiesSchemaMap.getEntries() == null) {
                proppertiesSet = Collections.EMPTY_SET;
            } else {
                proppertiesSet = propertiesSchemaMap.getEntries().keySet();
            }

            if(patternPropertiesSchemaMap ==null || patternPropertiesSchemaMap.getEntries() ==null) {
                patternPropertiesSet = Collections.EMPTY_SET;
            } else {
                patternPropertiesSet = patternPropertiesSchemaMap.getEntries().keySet();
            }
            allMatchers.add(isNoAdditionalProperties(proppertiesSet, patternPropertiesSet));
        }

        //TODO as per : http://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.4.5

    }

    private void putMatchers4Common() {

        List<String> schemaType = schema.getType();
        if(schemaType != null && !schemaType.isEmpty()) {
            allMatchers.add(isOfType(schemaType));
        }

        JsonArray enums = schema.getEnum();
        if(enums != null) {
            allMatchers.add(isInEnums(enums));
        }

        //TODO anyOf,oneOf,allOf,no + optional format

    }

    @Override
    protected boolean doValidate(JsonElement el, StringBuilder sb) {
        //System.out.println("SchemaV4Validator start: " + this.getTitle());
        //check if empty schema
        if(allMatchers == null || allMatchers.isEmpty()) {
            System.out.println("SchemaV4Validator NO MATCHERS end: " + this.getTitle());
            return true;
        }

        Matcher<JsonElement> matcher = allOf(allMatchers);
        //System.out.println("SchemaV4Validator end: " + this.getTitle());
       if(sb!=null) {
           matcher.describeMismatch(el, new StringDescription(sb));
       }
        return matcher.matches(el);
    }
}
