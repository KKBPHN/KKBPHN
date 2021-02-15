package io.reactivex.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.concurrent.atomic.AtomicInteger;

public final class VolatileSizeArrayList extends AtomicInteger implements List, RandomAccess {
    private static final long serialVersionUID = 3972397474470203923L;
    final ArrayList list;

    public VolatileSizeArrayList() {
        this.list = new ArrayList();
    }

    public VolatileSizeArrayList(int i) {
        this.list = new ArrayList(i);
    }

    public void add(int i, Object obj) {
        this.list.add(i, obj);
        lazySet(this.list.size());
    }

    public boolean add(Object obj) {
        boolean add = this.list.add(obj);
        lazySet(this.list.size());
        return add;
    }

    public boolean addAll(int i, Collection collection) {
        boolean addAll = this.list.addAll(i, collection);
        lazySet(this.list.size());
        return addAll;
    }

    public boolean addAll(Collection collection) {
        boolean addAll = this.list.addAll(collection);
        lazySet(this.list.size());
        return addAll;
    }

    public void clear() {
        this.list.clear();
        lazySet(0);
    }

    public boolean contains(Object obj) {
        return this.list.contains(obj);
    }

    public boolean containsAll(Collection collection) {
        return this.list.containsAll(collection);
    }

    public boolean equals(Object obj) {
        boolean z = obj instanceof VolatileSizeArrayList;
        ArrayList arrayList = this.list;
        return z ? arrayList.equals(((VolatileSizeArrayList) obj).list) : arrayList.equals(obj);
    }

    public Object get(int i) {
        return this.list.get(i);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    public int indexOf(Object obj) {
        return this.list.indexOf(obj);
    }

    public boolean isEmpty() {
        return get() == 0;
    }

    public Iterator iterator() {
        return this.list.iterator();
    }

    public int lastIndexOf(Object obj) {
        return this.list.lastIndexOf(obj);
    }

    public ListIterator listIterator() {
        return this.list.listIterator();
    }

    public ListIterator listIterator(int i) {
        return this.list.listIterator(i);
    }

    public Object remove(int i) {
        Object remove = this.list.remove(i);
        lazySet(this.list.size());
        return remove;
    }

    public boolean remove(Object obj) {
        boolean remove = this.list.remove(obj);
        lazySet(this.list.size());
        return remove;
    }

    public boolean removeAll(Collection collection) {
        boolean removeAll = this.list.removeAll(collection);
        lazySet(this.list.size());
        return removeAll;
    }

    public boolean retainAll(Collection collection) {
        boolean retainAll = this.list.retainAll(collection);
        lazySet(this.list.size());
        return retainAll;
    }

    public Object set(int i, Object obj) {
        return this.list.set(i, obj);
    }

    public int size() {
        return get();
    }

    public List subList(int i, int i2) {
        return this.list.subList(i, i2);
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public Object[] toArray(Object[] objArr) {
        return this.list.toArray(objArr);
    }

    public String toString() {
        return this.list.toString();
    }
}
