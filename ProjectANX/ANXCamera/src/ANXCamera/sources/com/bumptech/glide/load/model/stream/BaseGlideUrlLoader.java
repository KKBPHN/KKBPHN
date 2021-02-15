package com.bumptech.glide.load.model.stream;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseGlideUrlLoader implements ModelLoader {
    private final ModelLoader concreteLoader;
    @Nullable
    private final ModelCache modelCache;

    protected BaseGlideUrlLoader(ModelLoader modelLoader) {
        this(modelLoader, null);
    }

    protected BaseGlideUrlLoader(ModelLoader modelLoader, @Nullable ModelCache modelCache2) {
        this.concreteLoader = modelLoader;
        this.modelCache = modelCache2;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.String>, for r3v0, types: [java.util.Collection, java.util.Collection<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static List getAlternateKeys(Collection<String> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (String glideUrl : collection) {
            arrayList.add(new GlideUrl(glideUrl));
        }
        return arrayList;
    }

    @Nullable
    public LoadData buildLoadData(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        ModelCache modelCache2 = this.modelCache;
        Object obj2 = modelCache2 != null ? (GlideUrl) modelCache2.get(obj, i, i2) : null;
        if (obj2 == null) {
            String url = getUrl(obj, i, i2, options);
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            GlideUrl glideUrl = new GlideUrl(url, getHeaders(obj, i, i2, options));
            ModelCache modelCache3 = this.modelCache;
            if (modelCache3 != null) {
                modelCache3.put(obj, i, i2, glideUrl);
            }
            obj2 = glideUrl;
        }
        List alternateUrls = getAlternateUrls(obj, i, i2, options);
        LoadData buildLoadData = this.concreteLoader.buildLoadData(obj2, i, i2, options);
        return (buildLoadData == null || alternateUrls.isEmpty()) ? buildLoadData : new LoadData(buildLoadData.sourceKey, getAlternateKeys(alternateUrls), buildLoadData.fetcher);
    }

    /* access modifiers changed from: protected */
    public List getAlternateUrls(Object obj, int i, int i2, Options options) {
        return Collections.emptyList();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Headers getHeaders(Object obj, int i, int i2, Options options) {
        return Headers.DEFAULT;
    }

    public abstract String getUrl(Object obj, int i, int i2, Options options);
}
