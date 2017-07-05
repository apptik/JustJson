package io.apptik.json.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.JsonString;

import java.util.ArrayList;

import org.junit.Test;

public class FreezingTest {

	@Test
	public void arrayChildrenFreezing() {
		JsonArray array = new JsonArray();
		JsonArray childArray = new JsonArray();
		JsonObject childObject = new JsonObject();
		array.put(childArray);
		array.put(childObject);
		array.freeze();
		try {
			childArray.put(true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		try {
			childObject.put("foochild", true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

	}

	@Test
	public void arrayFreezing() {
		JsonArray array = new JsonArray();
		array.put(true);
		assertTrue(array.getBoolean(0));
		array.freeze();
		// Adding
		try {
			array.add(new JsonString("xx"));
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.add(0, new JsonString("xx"));
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		ArrayList<JsonElement> els = new ArrayList<JsonElement>();
		els.add(new JsonString("xx"));
		els.add(new JsonString("xx"));
		try {
			array.addAll(els);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.addAll(0, els);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		// Removing
		try {
			array.remove(0);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.removeAll(els);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.retainAll(els);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.clear();
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		// putting

		try {
			array.put(0, true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		try {
			array.put(0, 5);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(5);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(0, 5.5);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(5.5);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(0, null);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(null);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(0, "xx");
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put("xx");
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(0, new JsonObject());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(new JsonObject());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		try {
			array.put(0, new JsonArray());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
		try {
			array.put(new JsonArray());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}
	}

	@Test
	public void objectChildrenFreezing() {
		JsonObject object = new JsonObject();
		JsonArray childArray = new JsonArray();
		JsonObject childObject = new JsonObject();
		object.put("foo", childArray);
		object.put("bar", childObject);
		object.freeze();
		try {
			childArray.put(true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonArray instance.",
					ex.getMessage());
		}

		try {
			childObject.put("foochild", true);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

	}

	@Test
	public void objectFreezing() {
		JsonObject object = new JsonObject();
		object.put("foo", true);
		assertTrue(object.getBoolean("foo"));
		object.freeze();
		try {
			object.put("bar", 20);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.put("bar", 5.5);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.put("bar", null);
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.put("bar", "xx");
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.put("bar", new JsonObject());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.put("bar", new JsonArray());
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.clear();
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}

		try {
			object.remove("bar");
			fail();
		} catch (IllegalStateException ex) {
			assertEquals("Attempt to modify a frozen JsonObject instance.",
					ex.getMessage());
		}
	}

}
