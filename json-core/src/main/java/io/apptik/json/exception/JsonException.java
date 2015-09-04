/*
 * Copyright (C) 2014 Kalin Maldzhanski
 * Copyright (C) 2010 The Android Open Source Project
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

package io.apptik.json.exception;

// Note: this class was written without inspecting the non-free org.json sourcecode.

/**
 * Thrown to indicate a problems such as:
 * <ul>
 *   <li>Use of null as a name
 *   <li>Use of numeric types not available, such as {@link
 *       Double#isNaN() NaNs} or {@link Double#isInfinite() infinities}.
 *   <li>Lookups using an out of range index or nonexistent name
 *   <li>Type mismatches on lookups
 * </ul>
 *
 * <p>This is an unchecked exception because it is rarely recoverable.
 */
public class JsonException extends RuntimeException {

    public JsonException(String s) {
        super(s);
    }

    public JsonException(String s, Exception ex) {
        super(s, ex);
    }
}
