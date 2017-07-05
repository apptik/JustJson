package io.apptik.json.wrapper;

import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Helper class that can be used for Json Array containing always the same type
 * of object; This is useful in the general case when elements are read from or
 * written to Json once. however not efficient on many reads nor writes on same
 * object. In the case of many reads and/or writes back use
 * {@link CachedTypedJsonArray}
 *
 * @param <T>
 *            The type
 */
public abstract class TypedJsonArray<T> extends JsonElementWrapper<JsonArray>
		implements List<T> {

	public TypedJsonArray() {
		super();
	}

	public void add(final int i, final T t) {
		getJson().put(i, to(t));
	}

	public boolean add(final T t) {
		getJson().put(to(t));
		return true;
	}

	public boolean addAll(final Collection<? extends T> ts) {
		return false;
	}

	public boolean addAll(final int i, final Collection<? extends T> ts) {
		return false;
	}

	public void clear() {
		JsonElement.wrap(null);
	}

	public boolean contains(final Object o) {
		return getJson().contains(JsonElement.wrap(o));
	}

	public boolean containsAll(final Collection<?> objects) {
		return false;
	}

	public T get(final int i) {
		return getInternal(getJson().asJsonArray().get(i), i);
	}

	protected abstract T get(JsonElement jsonElement, int pos);

	private T getInternal(final JsonElement jsonElement, final int pos) {
		if (jsonElement == null) {
			return null;
		}
		return get(jsonElement, pos);
	}

	@Override
	public JsonArray getJson() {
		if (super.getJson() == null) {
			try {
				this.json = JsonElement.readFrom("[]").asJsonArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.getJson();
	}

	public int indexOf(final Object o) {
		return getJson().indexOf(o);
	}

	public boolean isEmpty() {
		return getJson().isEmpty();
	}

	public Iterator<T> iterator() {
		final Iterator<JsonElement> iterator = getJson().iterator();
		return new Iterator<T>() {

			public boolean hasNext() {
				return iterator.hasNext();
			}

			public T next() {
				JsonElement el = iterator.next();
				return getInternal(el, indexOf(el));
			}

			public void remove() {
				iterator.remove();
			}
		};
	}

	public int lastIndexOf(final Object o) {
		return getJson().lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		final ListIterator<JsonElement> iterator = getJson().listIterator();
		return new ListIterator<T>() {

			public void add(final T t) {
				iterator.add(to(t));
			}

			public boolean hasNext() {
				return iterator.hasNext();
			}

			public boolean hasPrevious() {
				return iterator.hasPrevious();
			}

			public T next() {
				JsonElement el = iterator.next();
				return getInternal(el, indexOf(el));
			}

			public int nextIndex() {
				return iterator.nextIndex();
			}

			public T previous() {
				int pos = iterator.previousIndex();
				return getInternal(iterator.previous(), pos);
			}

			public int previousIndex() {
				return iterator.previousIndex();
			}

			public void remove() {
				iterator.remove();
			}

			public void set(final T t) {
				iterator.set(to(t));
			}
		};
	}

	public ListIterator<T> listIterator(final int i) {
		final ListIterator<JsonElement> iterator = getJson().listIterator(i);
		return new ListIterator<T>() {

			public void add(final T t) {
				iterator.add(to(t));
			}

			public boolean hasNext() {
				return iterator.hasNext();
			}

			public boolean hasPrevious() {
				return iterator.hasPrevious();
			}

			public T next() {
				JsonElement el = iterator.next();
				return getInternal(el, indexOf(el));
			}

			public int nextIndex() {
				return iterator.nextIndex();
			}

			public T previous() {
				int pos = iterator.previousIndex();
				return getInternal(iterator.previous(), pos);
			}

			public int previousIndex() {
				return iterator.previousIndex();
			}

			public void remove() {
				iterator.remove();
			}

			public void set(final T t) {
				iterator.set(to(t));
			}
		};
	}

	public T remove(final int i) {
		return getInternal(getJson().remove(i), i);
	}

	public boolean remove(final Object o) {
		if (!contains(o)) {
			return false;
		}
		getJson().remove(indexOf(o));
		return true;
	}

	public boolean removeAll(final Collection<?> objects) {
		return false;
	}

	public boolean retainAll(final Collection<?> objects) {
		return false;
	}

	public T set(final int i, final T t) {
		getJson().put(i, to(t));
		return t;
	}

	public int size() {
		return getJson().length();
	}

	public List<T> subList(final int i, final int i2) {
		List<JsonElement> subList = getJson().subList(i, i2);
		ArrayList<T> resList = new ArrayList<T>();
		for (JsonElement el : subList) {
			resList.add(getInternal(el, indexOf(el)));
		}
		return resList;
	}

	protected abstract JsonElement to(T value);

	public Object[] toArray() {
		Object[] arr = new Object[getJson().size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = getInternal(getJson().get(i), i);
		}
		return arr;
	}

	public <T1> T1[] toArray(final T1[] t1s) {
		Object[] elementArr = toArray();
		int size = getJson().size();
		if (t1s.length < size) {
			// Make a new array of a's runtime type, but my contents:
			return (T1[]) Arrays.copyOf(elementArr, size, t1s.getClass());
		}
		System.arraycopy(elementArr, 0, t1s, 0, size);
		if (t1s.length > size) {
			t1s[size] = null;
		}
		return t1s;
	}

	@Override
	public <T extends JsonElementWrapper> T wrap(final JsonArray jsonElement) {
		return super.wrap(jsonElement);
	}
}
