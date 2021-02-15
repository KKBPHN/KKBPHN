package com.bumptech.glide.load.resource.transcode;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class TranscoderRegistry {
    private final List transcoders = new ArrayList();

    final class Entry {
        private final Class fromClass;
        private final Class toClass;
        final ResourceTranscoder transcoder;

        Entry(@NonNull Class cls, @NonNull Class cls2, @NonNull ResourceTranscoder resourceTranscoder) {
            this.fromClass = cls;
            this.toClass = cls2;
            this.transcoder = resourceTranscoder;
        }

        public boolean handles(@NonNull Class cls, @NonNull Class cls2) {
            return this.fromClass.isAssignableFrom(cls) && cls2.isAssignableFrom(this.toClass);
        }
    }

    @NonNull
    public synchronized ResourceTranscoder get(@NonNull Class cls, @NonNull Class cls2) {
        if (cls2.isAssignableFrom(cls)) {
            return UnitTranscoder.get();
        }
        for (Entry entry : this.transcoders) {
            if (entry.handles(cls, cls2)) {
                return entry.transcoder;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No transcoder registered to transcode from ");
        sb.append(cls);
        sb.append(" to ");
        sb.append(cls2);
        throw new IllegalArgumentException(sb.toString());
    }

    @NonNull
    public synchronized List getTranscodeClasses(@NonNull Class cls, @NonNull Class cls2) {
        ArrayList arrayList = new ArrayList();
        if (cls2.isAssignableFrom(cls)) {
            arrayList.add(cls2);
            return arrayList;
        }
        for (Entry handles : this.transcoders) {
            if (handles.handles(cls, cls2)) {
                arrayList.add(cls2);
            }
        }
        return arrayList;
    }

    public synchronized void register(@NonNull Class cls, @NonNull Class cls2, @NonNull ResourceTranscoder resourceTranscoder) {
        this.transcoders.add(new Entry(cls, cls2, resourceTranscoder));
    }
}
