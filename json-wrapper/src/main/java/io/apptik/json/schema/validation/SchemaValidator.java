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


import io.apptik.json.AbstractValidator;
import io.apptik.json.schema.Schema;


/**
 *  General JsonSchema validator as specified in http://tools.ietf.org/html/draft-fge-json-schema-validation-00
 * @param <T>
 */
public abstract class SchemaValidator<T extends Schema> extends AbstractValidator {

    T schema;

    public SchemaValidator(T schema) {
        this.schema = schema;
    }

    public T getSchema() {
        return schema;
    }

    @Override
    public String getTitle() {
        return schema.getTitle();
    }
}
