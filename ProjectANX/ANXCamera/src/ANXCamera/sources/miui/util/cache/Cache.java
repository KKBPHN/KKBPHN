package miui.util.cache;

public interface Cache {
    void clear();

    Object get(Object obj);

    int getCount();

    void put(Object obj, Object obj2, int i);

    void setMaxSize(int i);
}
