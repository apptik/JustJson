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

package io.apptik.json.wrapper;

import io.apptik.json.ElementWrapper;
import io.apptik.json.JsonElement;
import io.apptik.json.Validator;
import io.apptik.json.exception.JsonException;
import io.apptik.json.util.LinkedTreeMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Json element Wrapper to be used to wrap JSON data with its schema content
 * type used to describe the data.
 * <p/>
 * It can be extended to define a data model implementing getters and setters
 * for required members.
 * <p/>
 * The idea is that there is no need to instantiate different POJO for different
 * purpose on the same JSON data, but just wrap that data.
 * <p/>
 * Compared to pure POJO mapping it is particularly useful when single JSON
 * representation can be mapped to 2 or more POJOs. For example if we get data
 * about https://schema.org/LocalBusiness which is
 * https://schema.org/Organization and https://schema.org/Place at the same time
 * we need to reference Place and Organization POJOs in the LocalBusiness POJO
 * as in java we cannot extend from 2 classes. This means that for any possible
 * combination of DataTypes available for the Resources we get we need to create
 * POJOs to delegate to others.
 * <p/>
 * Using Json wrappers is fairly simple as we can just create wrappers
 * implementing interfaces specific to the type we need to work with, while full
 * json data itself is still available to be wrapped again for other needs.
 */
public abstract class JsonElementWrapper<T extends JsonElement> implements
		ElementWrapper {

	private String contentType;
	private transient LinkedTreeMap<String, MetaInfoFetcher> fetchers = new LinkedTreeMap<String, MetaInfoFetcher>();
	protected transient T json;
	private URI jsonSchemaUri;

	private MetaInfo metaInfo;
	private transient LinkedHashSet<Validator> validators;

	public JsonElementWrapper() {
		// todo do we need default fetcher ?
		// fetchers.put("defaultUriFetcher", new SchemaUriFetcher());
	}

	public JsonElementWrapper(final T jsonElement) {
		this.json = jsonElement;
	}

	public JsonElementWrapper(final T jsonElement, final String contentType) {
		this(jsonElement);
		this.contentType = contentType;

	}

	public JsonElementWrapper(final T jsonElement, final String contentType,
			final URI metaInfo) {
		this(jsonElement, contentType);
		this.jsonSchemaUri = metaInfo;
		tryFetchMetaInfo(this.jsonSchemaUri);
	}

	public JsonElementWrapper addSchemaFetcher(final String name,
			final MetaInfoFetcher fetcher) {
		this.fetchers.put(name, fetcher);
		return this;
	}

	public JsonElementWrapper addValidator(final Validator validator) {
		getValidators().add(validator);
		return this;
	}

	private MetaInfo doFetchMetaInfo(final URI jsonSchemaUri) {
		MetaInfo currSchema = null;

		Iterator<Map.Entry<String, MetaInfoFetcher>> iterator = fetchers
				.entrySet().iterator();
		while (iterator.hasNext() && currSchema == null) {
			Map.Entry<String, MetaInfoFetcher> entry = iterator.next();
			System.out.println("JsonElementWrapper try fetch using: "
					+ entry.getKey());
			try {
				currSchema = entry.getValue().fetch(jsonSchemaUri);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("JsonElementWrapper fetch result: "
					+ ((currSchema == null) ? "FAIL" : "OK"));
		}
		return currSchema;
	}

	public MetaInfo fetchMetaInfo() {
		if (metaInfo == null) {
			tryFetchMetaInfo(this.jsonSchemaUri);
		}
		return metaInfo;
	}

	public String getContentType() {
		return contentType;
	}

	public MetaInfoFetcher getDefaultSchemaFetcher() {
		return this.fetchers.get("defaultUriFetcher");
	}

	public T getJson() {
		return json;
	}

	public URI getJsonSchemaUri() {
		return jsonSchemaUri;
	}

	public MetaInfo getMetaInfo() {
		return metaInfo;
	}

	public LinkedHashSet<Validator> getValidators() {
		if (validators == null) {
			validators = new LinkedHashSet<Validator>();
		}
		return validators;
	}

	public boolean isDataValid() {
		for (Validator validator : getValidators()) {
			if (!validator.isValid(this.getJson())) {
				return false;
			}
		}
		return true;
	}

	private void readObject(final ObjectInputStream ois)
			throws ClassNotFoundException, IOException, JsonException {
		ois.defaultReadObject();
		this.wrap((T) JsonElement.readFrom((String) ois.readObject()));
	}

	public JsonElementWrapper setContentType(final String contentType) {
		this.contentType = contentType;
		return this;
	}

	public JsonElementWrapper setDefaultSchemaFetcher(
			final MetaInfoFetcher fetcher) {
		this.fetchers.put("defaultUriFetcher", fetcher);
		return this;
	}

	public JsonElementWrapper setMetaInfo(final MetaInfo metaInfo) {
		this.metaInfo = metaInfo;
		return this;
	}

	public JsonElementWrapper setMetaInfoUri(final URI uri) {
		this.jsonSchemaUri = uri;
		tryFetchMetaInfo(this.jsonSchemaUri);
		return this;
	}

	public JsonElementWrapper setSchemaFetchers(
			final Map<String, MetaInfoFetcher> newFetchers) {
		this.fetchers.clear();
		this.fetchers.putAll(newFetchers);
		return this;
	}

	@Override
	public String toString() {
		return getJson().toString();
	}

	/**
	 * Tries to fetch a schema and add the default Schema validator for it
	 *
	 * @param jsonSchemaUri
	 * @return
	 */
	private MetaInfo tryFetchMetaInfo(final URI jsonSchemaUri) {
		if (jsonSchemaUri == null) {
			return null;
		}
		try {
			metaInfo = doFetchMetaInfo(jsonSchemaUri);
			Validator validator = metaInfo.getDefaultValidator();
			if (validator != null) {
				getValidators().add(validator);
			}
		} catch (Exception ex) {
			return null;
		}

		return metaInfo;
	}

	public boolean validateData(final StringBuilder sb) {
		boolean res = true;
		Iterator<Validator> iterator = getValidators().iterator();
		System.out.println("JsonElementWrapper start validating ");
		Validator validator;
		while (iterator.hasNext()) {
			validator = iterator.next();
			System.out.println("JsonElementWrapper validating using: "
					+ validator.getTitle());

			if (!validator.validate(this.getJson(), sb)) {
				res = false;
			}
		}
		return res;
	}

	public <J extends JsonElementWrapper> J wrap(final T jsonElement) {
		this.json = jsonElement;
		return (J) this;
	}

	private void writeObject(final ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(json.toString());
	}

}
