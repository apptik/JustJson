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

package org.djodjo.json;


public interface Validator {

    /**
     * Fast check is JsonElement is valid
     * @param el the JsonElemnt being validated
     * @return valid or not valid
     */
    public boolean isValid(JsonElement el);

    /**
     * Runs a full validation and returns a result
     * @param el the JsonElemnt being validated
     * @return huyman readable result of the validation
     */
    public String validate(JsonElement el);

    boolean validate(JsonElement el, StringBuilder sb);

    /**
     *
     * @return Title to identify this validator
     */
    String getTitle();
}
