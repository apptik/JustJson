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
package io.apptik.json.aws.test;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import junit.framework.TestCase;

import io.apptik.json.JsonElement;
import io.apptik.json.aws.DynamoDb2Item;
import io.apptik.json.exception.JsonException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class DynamoDb2ItemTest extends TestCase {

    @Test
    public void testDynamoItem2Json() throws JsonException {
        Map<String, AttributeValue> attrMap = new HashMap<String, AttributeValue>();

        DynamoDb2Item dynamoDb2Item = new DynamoDb2Item();
        System.out.println(JsonElement.wrap(dynamoDb2Item).toString());
    }

    @Test
    public void testJson2DynamoItem() throws JsonException, IOException {
        DynamoDb2Item dynamoDb2Item = new DynamoDb2Item().wrap(JsonElement.readFrom("{\n" +
                "\"DeviceId\" : \"dev1\",\n" +
                "\"num\" : 123,\n" +
                "\"str\" : \"somth cool\",\n" +
                "\"arrNum\" : [1,2,3,4,5,6,7],\n" +
                "\"arrMix\": [1,2,3,4,\"sdf\", \"sdgfg\", \"fdgdfg\"] ,\n" +
                "\"bool\": true,\n" +
                "\"nishto\": null,\n" +
                "\"someObj\" : { \"el1\" : 1, \"el2\": 2, \"el3\": \"krai\"}\n" +
                "}"));
        ArrayList<DynamoDb2Item> list = new ArrayList<DynamoDb2Item>();
        list.add(dynamoDb2Item);

        Map<String, AttributeValue> attrMap = dynamoDb2Item.getEntries();
        assertTrue(attrMap.get("DeviceId").getS().equals("dev1"));
        assertTrue(attrMap.get("num").getN().equals("123"));

        assertEquals("1", attrMap.get("arrNum").getNS().get(0));
        assertEquals("7", attrMap.get("arrNum").getNS().get(6));
        assertEquals("1", attrMap.get("arrMix").getSS().get(0));
        assertEquals("fdgdfg", attrMap.get("arrMix").getSS().get(6));

    }




}
