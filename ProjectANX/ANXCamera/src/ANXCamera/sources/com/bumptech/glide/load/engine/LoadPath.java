package com.bumptech.glide.load.engine;

import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoadPath {
    private final Class dataClass;
    private final List decodePaths;
    private final String failureMessage;
    private final Pool listPool;

    public LoadPath(Class cls, Class cls2, Class cls3, List list, Pool pool) {
        this.dataClass = cls;
        this.listPool = pool;
        Preconditions.checkNotEmpty((Collection) list);
        this.decodePaths = list;
        StringBuilder sb = new StringBuilder();
        sb.append("Failed LoadPath{");
        sb.append(cls.getSimpleName());
        String str = "->";
        sb.append(str);
        sb.append(cls2.getSimpleName());
        sb.append(str);
        sb.append(cls3.getSimpleName());
        sb.append("}");
        this.failureMessage = sb.toString();
    }

    private Resource loadWithExceptionList(DataRewinder dataRewinder, @NonNull Options options, int i, int i2, DecodeCallback decodeCallback, List list) {
        Resource resource;
        List list2 = list;
        int size = this.decodePaths.size();
        Resource resource2 = null;
        for (int i3 = 0; i3 < size; i3++) {
            try {
                resource2 = ((DecodePath) this.decodePaths.get(i3)).decode(dataRewinder, i, i2, options, decodeCallback);
            } catch (GlideException e) {
                list2.add(e);
            }
            if (resource2 != null) {
                break;
            }
        }
        if (resource != null) {
            return resource;
        }
        throw new GlideException(this.failureMessage, (List) new ArrayList(list2));
    }

    public Class getDataClass() {
        return this.dataClass;
    }

    public Resource load(DataRewinder dataRewinder, @NonNull Options options, int i, int i2, DecodeCallback decodeCallback) {
        Object acquire = this.listPool.acquire();
        Preconditions.checkNotNull(acquire);
        List list = (List) acquire;
        try {
            Resource loadWithExceptionList = loadWithExceptionList(dataRewinder, options, i, i2, decodeCallback, list);
            return loadWithExceptionList;
        } finally {
            this.listPool.release(list);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoadPath{decodePaths=");
        sb.append(Arrays.toString(this.decodePaths.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
