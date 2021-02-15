package miuix.core.util.concurrent;

public interface Queue {

    public interface Predicate {
        boolean apply(Object obj);
    }

    int clear();

    Object get();

    int getCapacity();

    boolean isEmpty();

    boolean put(Object obj);

    int remove(Predicate predicate);

    boolean remove(Object obj);
}
