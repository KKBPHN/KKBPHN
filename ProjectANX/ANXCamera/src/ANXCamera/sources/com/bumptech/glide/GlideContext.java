package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.Glide.RequestOptionsFactory;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GlideContext extends ContextWrapper {
    @VisibleForTesting
    static final TransitionOptions DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
    private final ArrayPool arrayPool;
    private final List defaultRequestListeners;
    @GuardedBy("this")
    @Nullable
    private RequestOptions defaultRequestOptions;
    private final RequestOptionsFactory defaultRequestOptionsFactory;
    private final Map defaultTransitionOptions;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final boolean isLoggingRequestOriginsEnabled;
    private final int logLevel;
    private final Registry registry;

    public GlideContext(@NonNull Context context, @NonNull ArrayPool arrayPool2, @NonNull Registry registry2, @NonNull ImageViewTargetFactory imageViewTargetFactory2, @NonNull RequestOptionsFactory requestOptionsFactory, @NonNull Map map, @NonNull List list, @NonNull Engine engine2, boolean z, int i) {
        super(context.getApplicationContext());
        this.arrayPool = arrayPool2;
        this.registry = registry2;
        this.imageViewTargetFactory = imageViewTargetFactory2;
        this.defaultRequestOptionsFactory = requestOptionsFactory;
        this.defaultRequestListeners = list;
        this.defaultTransitionOptions = map;
        this.engine = engine2;
        this.isLoggingRequestOriginsEnabled = z;
        this.logLevel = i;
    }

    @NonNull
    public ViewTarget buildImageViewTarget(@NonNull ImageView imageView, @NonNull Class cls) {
        return this.imageViewTargetFactory.buildTarget(imageView, cls);
    }

    @NonNull
    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public List getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    public synchronized RequestOptions getDefaultRequestOptions() {
        if (this.defaultRequestOptions == null) {
            this.defaultRequestOptions = (RequestOptions) this.defaultRequestOptionsFactory.build().lock();
        }
        return this.defaultRequestOptions;
    }

    @NonNull
    public TransitionOptions getDefaultTransitionOptions(@NonNull Class cls) {
        TransitionOptions transitionOptions;
        TransitionOptions transitionOptions2 = (TransitionOptions) this.defaultTransitionOptions.get(cls);
        if (transitionOptions2 == null) {
            for (Entry entry : this.defaultTransitionOptions.entrySet()) {
                if (((Class) entry.getKey()).isAssignableFrom(cls)) {
                    transitionOptions2 = (TransitionOptions) entry.getValue();
                }
            }
        }
        return transitionOptions == null ? DEFAULT_TRANSITION_OPTIONS : transitionOptions;
    }

    @NonNull
    public Engine getEngine() {
        return this.engine;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    @NonNull
    public Registry getRegistry() {
        return this.registry;
    }

    public boolean isLoggingRequestOriginsEnabled() {
        return this.isLoggingRequestOriginsEnabled;
    }
}
