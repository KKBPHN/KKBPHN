package io.reactivex.internal.util;

import io.reactivex.functions.BiFunction;
import java.util.List;

public enum ListAddBiConsumer implements BiFunction {
    INSTANCE;

    public static BiFunction instance() {
        return INSTANCE;
    }

    public List apply(List list, Object obj) {
        list.add(obj);
        return list;
    }
}
