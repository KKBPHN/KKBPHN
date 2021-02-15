package com.bumptech.glide.provider;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
    private final List bucketPriorityList = new ArrayList();
    private final Map decoders = new HashMap();

    class Entry {
        private final Class dataClass;
        final ResourceDecoder decoder;
        final Class resourceClass;

        public Entry(@NonNull Class cls, @NonNull Class cls2, ResourceDecoder resourceDecoder) {
            this.dataClass = cls;
            this.resourceClass = cls2;
            this.decoder = resourceDecoder;
        }

        public boolean handles(@NonNull Class cls, @NonNull Class cls2) {
            return this.dataClass.isAssignableFrom(cls) && cls2.isAssignableFrom(this.resourceClass);
        }
    }

    @NonNull
    private synchronized List getOrAddEntryList(@NonNull String str) {
        List list;
        if (!this.bucketPriorityList.contains(str)) {
            this.bucketPriorityList.add(str);
        }
        list = (List) this.decoders.get(str);
        if (list == null) {
            list = new ArrayList();
            this.decoders.put(str, list);
        }
        return list;
    }

    public synchronized void append(@NonNull String str, @NonNull ResourceDecoder resourceDecoder, @NonNull Class cls, @NonNull Class cls2) {
        getOrAddEntryList(str).add(new Entry(cls, cls2, resourceDecoder));
    }

    @NonNull
    public synchronized List getDecoders(@NonNull Class cls, @NonNull Class cls2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (String str : this.bucketPriorityList) {
            List<Entry> list = (List) this.decoders.get(str);
            if (list != null) {
                for (Entry entry : list) {
                    if (entry.handles(cls, cls2)) {
                        arrayList.add(entry.decoder);
                    }
                }
            }
        }
        return arrayList;
    }

    @NonNull
    public synchronized List getResourceClasses(@NonNull Class cls, @NonNull Class cls2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (String str : this.bucketPriorityList) {
            List<Entry> list = (List) this.decoders.get(str);
            if (list != null) {
                for (Entry entry : list) {
                    if (entry.handles(cls, cls2) && !arrayList.contains(entry.resourceClass)) {
                        arrayList.add(entry.resourceClass);
                    }
                }
            }
        }
        return arrayList;
    }

    public synchronized void prepend(@NonNull String str, @NonNull ResourceDecoder resourceDecoder, @NonNull Class cls, @NonNull Class cls2) {
        getOrAddEntryList(str).add(0, new Entry(cls, cls2, resourceDecoder));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r5v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setBucketPriorityList(@NonNull List<String> list) {
        ArrayList<String> arrayList = new ArrayList<>(this.bucketPriorityList);
        this.bucketPriorityList.clear();
        for (String add : list) {
            this.bucketPriorityList.add(add);
        }
        for (String str : arrayList) {
            if (!list.contains(str)) {
                this.bucketPriorityList.add(str);
            }
        }
    }
}
