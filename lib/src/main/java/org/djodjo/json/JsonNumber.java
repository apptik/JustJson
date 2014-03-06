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


import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

public class JsonNumber extends JsonElement {

    private final String value;

    private Number numValue = null;

    JsonNumber( String string ) {
        if( string == null ) {
            throw new NullPointerException( "value is null" );
        }
        this.value = string;
    }

    JsonNumber( Number value ) {
        if( value == null ) {
            throw new NullPointerException( "value is null" );
        }
        checkDouble(value.doubleValue());
        this.numValue = value;
        this.value = numValue.toString();
    }

    private double checkDouble(double d) throws IllegalArgumentException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalArgumentException("Forbidden numeric value: " + d);
        }
        return d;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public Number asNumber(){
        if(numValue==null) {
            try {
                numValue = NumberFormat.getNumberInstance().parse(value);
            } catch (ParseException ex) {
                numValue = new BigDecimal(value);
            }
        }
        return numValue;
    }

    @Override
    public byte asByte() {
        return asNumber().byteValue();
    }

    @Override
    public int asInt() {
        return asNumber().intValue();
    }

    @Override
    public long asLong() {
        return asNumber().longValue();
    }

    @Override
    public float asFloat() {
        return asNumber().floatValue();
    }

    @Override
    public double asDouble() {
        return asNumber().doubleValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        return
                (o instanceof JsonNumber && ((JsonNumber) o).value.equals(value))
                        ||
                        (  o !=null && asNumber().equals(o))
                ;

    }

    @Override
    protected void write( JsonWriter writer ) throws IOException {
        writer.value( asNumber() );
    }

}
