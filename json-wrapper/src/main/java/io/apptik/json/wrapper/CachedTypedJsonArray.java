package io.apptik.json.wrapper;


import io.apptik.json.JsonArray;
import io.apptik.json.JsonElement;

import java.util.*;

/**
 * This class is used for wrapping json arrays where elements
 * are of the same type. It is using ArrayList to save all translated elements
 * and performs async mapping when wrapped. Until wrapping is done only get method
 * is available for parsed elements.
 * This is useful when a big array of complex elements needs to be loaded from json
 * and only displayed multiple times.
 */
public abstract class CachedTypedJsonArray<T> extends TypedJsonArray<T> {
    //we need reference-equality in place of object-equality when comparing original json elements
    List<T> elements = Collections.synchronizedList(new ArrayList<T>());
    volatile boolean wrapping;

    @Override
    public <T extends JsonElementWrapper> T wrap(JsonElement jsonElement) {
        super.wrap(jsonElement);
        wrapElements();
        return (T) this;
    }

    private synchronized void wrapElements() {
        wrapping = true;
        elements.clear();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (JsonElement je : getJson()) {
                    elements.add(get(je, elements.size()));
                }
                wrapping = false;
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    @Override
    public T get(int i) {
        if (i >= elements.size()) {
            return get(super.getJson().get(i), i);
        } else {
            return elements.get(i);
        }
    }

    @Override
    public T set(int i, T t) {
        blockUntilWrapped();
        return elements.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        blockUntilWrapped();
        elements.add(i, t);
    }

    @Override
    public T remove(int i) {
        blockUntilWrapped();
        return elements.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        blockUntilWrapped();
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        blockUntilWrapped();
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        blockUntilWrapped();
        return elements.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        blockUntilWrapped();
        return elements.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i2) {
        blockUntilWrapped();
        return elements.subList(i, i2);
    }

    @Override
    public void clear() {
        blockUntilWrapped();
        elements.clear();
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        blockUntilWrapped();
        return elements.retainAll(objects);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        blockUntilWrapped();
        return elements.removeAll(objects);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        blockUntilWrapped();
        return elements.addAll(i, ts);
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        blockUntilWrapped();
        return elements.addAll(ts);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        blockUntilWrapped();
        return elements.containsAll(objects);
    }

    @Override
    public boolean remove(Object o) {
        blockUntilWrapped();
        return elements.remove(o);
    }

    @Override
    public boolean add(T t) {
        blockUntilWrapped();
        return elements.add(t);
    }

    @Override
    public Iterator<T> iterator() {
        blockUntilWrapped();
        return elements.iterator();
    }

    @Override
    public boolean contains(Object o) {
        blockUntilWrapped();
        return elements.contains(o);
    }

    @Override
    public int size() {
        if(wrapping) {
            return json.asJsonArray().size();
        } else {
            return elements.size();
        }
    }

    @Override
    public Object[] toArray() {
        blockUntilWrapped();
        return elements.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        blockUntilWrapped();
        return elements.toArray(t1s);
    }


    @Override
    public JsonArray getJson() {
        this.json.asJsonArray().clear();
        for(T el:elements) {
            this.json.asJsonArray().put(to(el));
        }
        return super.getJson();
    }

    private void blockUntilWrapped() {
        while (wrapping) {
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
