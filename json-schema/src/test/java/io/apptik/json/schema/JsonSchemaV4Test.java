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

package io.apptik.json.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JsonSchemaV4Test {
	Schema schema;

	@Before
	public void setUp() throws Exception {
		schema = new SchemaV4();
	}

	@Test
	public void testSchemaUri() throws Exception {
		assertEquals(schema.getSchema(),
				"http://json-schema.org/draft-04/schema#");
		assertEquals(schema.getJsonSchemaUri(),
				URI.create("http://json-schema.org/draft-04/schema#"));
	}

	@Test
	public void testValidator() throws Exception {
		assertNotNull(schema.getDefaultValidator());

	}
}
