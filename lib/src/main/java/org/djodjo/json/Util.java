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

package org.djodjo.json;

class Util {
    /**
     * Returns the input if it is a Util-permissible value; throws otherwise.
     */
    private static final Double NEGATIVE_ZERO = -0d;

   static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else  if (value instanceof JsonElement) {
            return ((JsonElement)value).toString();
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }



    public static JsonException typeMismatch(Object indexOrName, Object actual,
                                             String requiredType, boolean mode) throws JsonException {
        if (actual == null) {
            throw new JsonException("Value at " + indexOrName + " is null.");
        } else {
            throw new JsonException("Value " + actual + " at " + indexOrName
                    + " of type " + actual.getClass().getName()
                    + " cannot be converted to " + requiredType + ". Strict mode is: " + mode);
        }
    }

    public static JsonException typeMismatch(Object indexOrName, Object actual,
                                             String requiredType) throws JsonException {
        if (actual == null) {
            throw new JsonException("Value at " + indexOrName + " is null.");
        } else {
            throw new JsonException("Value " + actual + " at " + indexOrName
                    + " of type " + actual.getClass().getName()
                    + " cannot be converted to " + requiredType);
        }
    }

    public static JsonException typeMismatch(Object actual, String requiredType)
            throws JsonException {
        if (actual == null) {
            throw new JsonException("Value is null.");
        } else {
            throw new JsonException("Value " + actual
                    + " of type " + actual.getClass().getName()
                    + " cannot be converted to " + requiredType);
        }
    }

    /**
     * Encodes the number as a Json string.
     *
     * @param number a finite value. May not be {@link Double#isNaN() NaNs} or
     *     {@link Double#isInfinite() infinities}.
     */
    public static String numberToString(Number number) throws JsonException {
        if (number == null) {
            throw new JsonException("Number must be non-null");
        }

        double doubleValue = number.doubleValue();

        // the original returns "-0" instead of "-0.0" for negative zero
        if (number.equals(NEGATIVE_ZERO)) {
            return "-0";
        }

        long longValue = number.longValue();
        if (doubleValue == (double) longValue) {
            return Long.toString(longValue);
        }

        return number.toString();
    }
}
