package io.apptik.json.test;


import io.apptik.json.JsonObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FreezingTest {

    @Test
    public void objectFreezing() {
        JsonObject object = new JsonObject();
        object.put("foo", true);
        assertTrue(object.getBoolean("foo"));
        object.freeze();
        try {
            object.put("bar", 20);
            fail();
        } catch(IllegalStateException ex) {
            assertEquals("Attempt to modify a frozen JsonObject instance.",
                    ex.getMessage());
        }
    }


}
