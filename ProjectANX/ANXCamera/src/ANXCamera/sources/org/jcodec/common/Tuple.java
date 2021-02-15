package org.jcodec.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tuple {

    public interface Mapper {
        Object map(Object obj);
    }

    public class _1 {
        public final Object v0;

        public _1(Object obj) {
            this.v0 = obj;
        }
    }

    public class _2 {
        public final Object v0;
        public final Object v1;

        public _2(Object obj, Object obj2) {
            this.v0 = obj;
            this.v1 = obj2;
        }
    }

    public class _3 {
        public final Object v0;
        public final Object v1;
        public final Object v2;

        public _3(Object obj, Object obj2, Object obj3) {
            this.v0 = obj;
            this.v1 = obj2;
            this.v2 = obj3;
        }
    }

    public class _4 {
        public final Object v0;
        public final Object v1;
        public final Object v2;
        public final Object v3;

        public _4(Object obj, Object obj2, Object obj3, Object obj4) {
            this.v0 = obj;
            this.v1 = obj2;
            this.v2 = obj3;
            this.v3 = obj4;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_1>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_1>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _1_project0(List<_1> list) {
        LinkedList linkedList = new LinkedList();
        for (_1 _12 : list) {
            linkedList.add(_12.v0);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_1>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_1>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _1map0(List<_1> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_1 _12 : list) {
            linkedList.add(single(mapper.map(_12.v0)));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_2>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_2>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _2_project0(List<_2> list) {
        LinkedList linkedList = new LinkedList();
        for (_2 _22 : list) {
            linkedList.add(_22.v0);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_2>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_2>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _2_project1(List<_2> list) {
        LinkedList linkedList = new LinkedList();
        for (_2 _22 : list) {
            linkedList.add(_22.v1);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_2>, for r3v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_2>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _2map0(List<_2> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_2 _22 : list) {
            linkedList.add(pair(mapper.map(_22.v0), _22.v1));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_2>, for r3v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_2>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _2map1(List<_2> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_2 _22 : list) {
            linkedList.add(pair(_22.v0, mapper.map(_22.v1)));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3_project0(List<_3> list) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(_32.v0);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3_project1(List<_3> list) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(_32.v1);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3_project2(List<_3> list) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(_32.v2);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r4v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3map0(List<_3> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(triple(mapper.map(_32.v0), _32.v1, _32.v2));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r4v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3map1(List<_3> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(triple(_32.v0, mapper.map(_32.v1), _32.v2));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_3>, for r4v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_3>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _3map3(List<_3> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_3 _32 : list) {
            linkedList.add(triple(_32.v0, _32.v1, mapper.map(_32.v2)));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4_project0(List<_4> list) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(_42.v0);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4_project1(List<_4> list) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(_42.v1);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4_project2(List<_4> list) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(_42.v2);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r2v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4_project3(List<_4> list) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(_42.v3);
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r5v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4map0(List<_4> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(quad(mapper.map(_42.v0), _42.v1, _42.v2, _42.v3));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r5v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4map1(List<_4> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(quad(_42.v0, mapper.map(_42.v1), _42.v2, _42.v3));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r5v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4map3(List<_4> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(quad(_42.v0, _42.v1, mapper.map(_42.v2), _42.v3));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.jcodec.common.Tuple$_4>, for r5v0, types: [java.util.List, java.util.List<org.jcodec.common.Tuple$_4>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List _4map4(List<_4> list, Mapper mapper) {
        LinkedList linkedList = new LinkedList();
        for (_4 _42 : list) {
            linkedList.add(quad(_42.v0, _42.v1, _42.v2, mapper.map(_42.v3)));
        }
        return linkedList;
    }

    public static Map arrayAsMap(_2[] _2Arr) {
        HashMap hashMap = new HashMap();
        for (_2 _22 : _2Arr) {
            hashMap.put(_22.v0, _22.v1);
        }
        return hashMap;
    }

    public static List asList(Map map) {
        LinkedList linkedList = new LinkedList();
        for (Entry entry : map.entrySet()) {
            linkedList.add(pair(entry.getKey(), entry.getValue()));
        }
        return linkedList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Iterable, code=java.lang.Iterable<org.jcodec.common.Tuple$_2>, for r3v0, types: [java.lang.Iterable<org.jcodec.common.Tuple$_2>, java.lang.Iterable] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Map asMap(Iterable<_2> iterable) {
        HashMap hashMap = new HashMap();
        for (_2 _22 : iterable) {
            hashMap.put(_22.v0, _22.v1);
        }
        return hashMap;
    }

    public static _2 pair(Object obj, Object obj2) {
        return new _2(obj, obj2);
    }

    public static _4 quad(Object obj, Object obj2, Object obj3, Object obj4) {
        return new _4(obj, obj2, obj3, obj4);
    }

    public static _1 single(Object obj) {
        return new _1(obj);
    }

    public static _3 triple(Object obj, Object obj2, Object obj3) {
        return new _3(obj, obj2, obj3);
    }
}
