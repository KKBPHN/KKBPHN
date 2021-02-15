package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecodePath {
    private static final String TAG = "DecodePath";
    private final Class dataClass;
    private final List decoders;
    private final String failureMessage;
    private final Pool listPool;
    private final ResourceTranscoder transcoder;

    interface DecodeCallback {
        @NonNull
        Resource onResourceDecoded(@NonNull Resource resource);
    }

    public DecodePath(Class cls, Class cls2, Class cls3, List list, ResourceTranscoder resourceTranscoder, Pool pool) {
        this.dataClass = cls;
        this.decoders = list;
        this.transcoder = resourceTranscoder;
        this.listPool = pool;
        StringBuilder sb = new StringBuilder();
        sb.append("Failed DecodePath{");
        sb.append(cls.getSimpleName());
        String str = "->";
        sb.append(str);
        sb.append(cls2.getSimpleName());
        sb.append(str);
        sb.append(cls3.getSimpleName());
        sb.append("}");
        this.failureMessage = sb.toString();
    }

    @NonNull
    private Resource decodeResource(DataRewinder dataRewinder, int i, int i2, @NonNull Options options) {
        Object acquire = this.listPool.acquire();
        Preconditions.checkNotNull(acquire);
        List list = (List) acquire;
        try {
            Resource decodeResourceWithList = decodeResourceWithList(dataRewinder, i, i2, options, list);
            return decodeResourceWithList;
        } finally {
            this.listPool.release(list);
        }
    }

    @NonNull
    private Resource decodeResourceWithList(DataRewinder dataRewinder, int i, int i2, @NonNull Options options, List list) {
        Resource resource;
        int size = this.decoders.size();
        Resource resource2 = null;
        for (int i3 = 0; i3 < size; i3++) {
            ResourceDecoder resourceDecoder = (ResourceDecoder) this.decoders.get(i3);
            try {
                if (resourceDecoder.handles(dataRewinder.rewindAndGet(), options)) {
                    resource2 = resourceDecoder.decode(dataRewinder.rewindAndGet(), i, i2, options);
                }
            } catch (IOException | OutOfMemoryError | RuntimeException e) {
                String str = TAG;
                if (Log.isLoggable(str, 2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to decode data for ");
                    sb.append(resourceDecoder);
                    Log.v(str, sb.toString(), e);
                }
                list.add(e);
            }
            if (resource2 != null) {
                break;
            }
        }
        if (resource != null) {
            return resource;
        }
        throw new GlideException(this.failureMessage, (List) new ArrayList(list));
    }

    public Resource decode(DataRewinder dataRewinder, int i, int i2, @NonNull Options options, DecodeCallback decodeCallback) {
        return this.transcoder.transcode(decodeCallback.onResourceDecoded(decodeResource(dataRewinder, i, i2, options)), options);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecodePath{ dataClass=");
        sb.append(this.dataClass);
        sb.append(", decoders=");
        sb.append(this.decoders);
        sb.append(", transcoder=");
        sb.append(this.transcoder);
        sb.append('}');
        return sb.toString();
    }
}
