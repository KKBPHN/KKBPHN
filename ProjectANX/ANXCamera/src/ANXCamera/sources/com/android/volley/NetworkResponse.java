package com.android.volley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class NetworkResponse {
    public final List allHeaders;
    public final byte[] data;
    public final Map headers;
    public final long networkTimeMs;
    public final boolean notModified;
    public final int statusCode;

    private NetworkResponse(int i, byte[] bArr, Map map, List list, boolean z, long j) {
        this.statusCode = i;
        this.data = bArr;
        this.headers = map;
        this.allHeaders = list == null ? null : Collections.unmodifiableList(list);
        this.notModified = z;
        this.networkTimeMs = j;
    }

    @Deprecated
    public NetworkResponse(int i, byte[] bArr, Map map, boolean z) {
        this(i, bArr, map, z, 0);
    }

    @Deprecated
    public NetworkResponse(int i, byte[] bArr, Map map, boolean z, long j) {
        this(i, bArr, map, toAllHeaderList(map), z, j);
    }

    public NetworkResponse(int i, byte[] bArr, boolean z, long j, List list) {
        this(i, bArr, toHeaderMap(list), list, z, j);
    }

    public NetworkResponse(byte[] bArr) {
        this(200, bArr, false, 0, Collections.emptyList());
    }

    @Deprecated
    public NetworkResponse(byte[] bArr, Map map) {
        this(200, bArr, map, false, 0);
    }

    private static List toAllHeaderList(Map map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            arrayList.add(new Header((String) entry.getKey(), (String) entry.getValue()));
        }
        return arrayList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.volley.Header>, for r3v0, types: [java.util.List<com.android.volley.Header>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Map toHeaderMap(List<Header> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Header header : list) {
            treeMap.put(header.getName(), header.getValue());
        }
        return treeMap;
    }
}
