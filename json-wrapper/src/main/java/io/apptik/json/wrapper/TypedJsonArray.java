package io.apptik.json.wrapper;


import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;

import java.io.IOException;
import java.util.*;

/**
 * Helper class that can be used for Json Array containing always the same type of object;
 * This is useful in the general case when elements are read from or written to Json once.
 * however not efficient on many reads nor writes on same object.
 * In the case of many reads and/or writes back use
 * {@link CachedTypedJsonArray}
 *
 *
 * @param <T> The type
 */
public abstract class TypedJsonArray<T> extends JsonElementWrapper implements List<T> {

    public TypedJsonArray() {
    }

    @Override
    public <T extends JsonElementWrapper> T wrap(JsonElement jsonElement) {
        return super.wrap(jsonElement);
    }

    @Override
    public JsonArray getJson() {
        if (super.getJson() == null) try {
            this.json = JsonElement.readFrom("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getJson().asJsonArray();
    }

    protected abstract T get(JsonElement jsonElement, int pos);

    protected abstract JsonElement to(T value);

    @Override
    public int size() {
        return getJson().length();
    }

    @Override
    public boolean isEmpty() {
        return getJson().length() < 1;
    }

    @Override
    public boolean contains(Object o) {
        return getJson().contains(JsonElement.wrap(o));
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<JsonElement> iterator = getJson().iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                JsonElement el = iterator.next();
                return get(el, indexOf(el));
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[getJson().size()];
        for(int i=0;i<arr.length;i++) {
            arr[i] = get(getJson().get(i), i);
        }
        return arr;
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        Object[] elementArr = toArray();
        int size = getJson().size();
        if (t1s.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T1[]) Arrays.copyOf(elementArr, size, t1s.getClass());
        System.arraycopy(elementArr, 0, t1s, 0, size);
        if (t1s.length > size)
            t1s[size] = null;
        return t1s;
    }

    @Override
    public boolean add(T t) {
        getJson().put(to(t));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o))
            return false;
        getJson().remove(indexOf(o));
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return false;
    }

    @Override
    public void clear() {
        JsonElement.wrap(null);
    }

    @Override
    public T get(int i) {
        return get(getJson().asJsonArray().get(i), i);
    }

    @Override
    public T set(int i, T t) {
        getJson().put(i, to(t));
        return t;
    }

    @Override
    public void add(int i, T t) {
        getJson().put(i, to(t));
    }

    @Override
    public T remove(int i) {
        return get(getJson().remove(i), i);
    }

    @Override
    public int indexOf(Object o) {
        return getJson().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getJson().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        final ListIterator<JsonElement> iterator = getJson().listIterator();
        return new ListIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                JsonElement el = iterator.next();
                return get(el, indexOf(el));
            }

            @Override
            public boolean hasPrevious() {
                return iterator.hasPrevious();
            }

            @Override
            public T previous() {
                int pos = iterator.previousIndex();
                return get(iterator.previous(), pos);
            }

            @Override
            public int nextIndex() {
                return iterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iterator.previousIndex();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

            @Override
            public void set(T t) {
                iterator.set(to(t));
            }

            @Override
            public void add(T t) {
                iterator.add(to(t));
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        final ListIterator<JsonElement> iterator = getJson().listIterator(i);
        return new ListIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                JsonElement el = iterator.next();
                return get(el, indexOf(el));
            }

            @Override
            public boolean hasPrevious() {
                return iterator.hasPrevious();
            }

            @Override
            public T previous() {
                int pos = iterator.previousIndex();
                return get(iterator.previous(), pos);
            }

            @Override
            public int nextIndex() {
                return iterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iterator.previousIndex();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

            @Override
            public void set(T t) {
                iterator.set(to(t));
            }

            @Override
            public void add(T t) {
                iterator.add(to(t));
            }
        };
    }

    @Override
    public List<T> subList(int i, int i2) {
        List<JsonElement> subList = getJson().subList(i, i2);
        ArrayList<T> resList = new ArrayList<T>();
        for (JsonElement el : subList) {
            resList.add(get(el, indexOf(el)));
        }
        return resList;
    }
}
