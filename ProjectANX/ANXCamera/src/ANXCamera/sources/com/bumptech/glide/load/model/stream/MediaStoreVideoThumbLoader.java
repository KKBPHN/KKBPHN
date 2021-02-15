package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.signature.ObjectKey;

public class MediaStoreVideoThumbLoader implements ModelLoader {
    private final Context context;

    public class Factory implements ModelLoaderFactory {
        private final Context context;

        public Factory(Context context2) {
            this.context = context2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreVideoThumbLoader(this.context);
        }

        public void teardown() {
        }
    }

    public MediaStoreVideoThumbLoader(Context context2) {
        this.context = context2.getApplicationContext();
    }

    private boolean isRequestingDefaultFrame(Options options) {
        Long l = (Long) options.get(VideoDecoder.TARGET_FRAME);
        return l != null && l.longValue() == -1;
    }

    @Nullable
    public LoadData buildLoadData(@NonNull Uri uri, int i, int i2, @NonNull Options options) {
        if (!MediaStoreUtil.isThumbnailSize(i, i2) || !isRequestingDefaultFrame(options)) {
            return null;
        }
        return new LoadData(new ObjectKey(uri), ThumbFetcher.buildVideoFetcher(this.context, uri));
    }

    public boolean handles(@NonNull Uri uri) {
        return MediaStoreUtil.isMediaStoreVideoUri(uri);
    }
}
