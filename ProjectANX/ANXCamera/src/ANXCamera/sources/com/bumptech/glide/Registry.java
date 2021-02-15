package com.bumptech.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinder.Factory;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Registry {
    private static final String BUCKET_APPEND_ALL = "legacy_append";
    public static final String BUCKET_BITMAP = "Bitmap";
    public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
    public static final String BUCKET_GIF = "Gif";
    private static final String BUCKET_PREPEND_ALL = "legacy_prepend_all";
    private final DataRewinderRegistry dataRewinderRegistry = new DataRewinderRegistry();
    private final ResourceDecoderRegistry decoderRegistry = new ResourceDecoderRegistry();
    private final EncoderRegistry encoderRegistry = new EncoderRegistry();
    private final ImageHeaderParserRegistry imageHeaderParserRegistry = new ImageHeaderParserRegistry();
    private final LoadPathCache loadPathCache = new LoadPathCache();
    private final ModelLoaderRegistry modelLoaderRegistry = new ModelLoaderRegistry(this.throwableListPool);
    private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
    private final ResourceEncoderRegistry resourceEncoderRegistry = new ResourceEncoderRegistry();
    private final Pool throwableListPool = FactoryPools.threadSafeList();
    private final TranscoderRegistry transcoderRegistry = new TranscoderRegistry();

    public class MissingComponentException extends RuntimeException {
        public MissingComponentException(@NonNull String str) {
            super(str);
        }
    }

    public final class NoImageHeaderParserException extends MissingComponentException {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }

    public class NoModelLoaderAvailableException extends MissingComponentException {
        public NoModelLoaderAvailableException(@NonNull Class cls, @NonNull Class cls2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to find any ModelLoaders for model: ");
            sb.append(cls);
            sb.append(" and data: ");
            sb.append(cls2);
            super(sb.toString());
        }

        public NoModelLoaderAvailableException(@NonNull Object obj) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to find any ModelLoaders registered for model class: ");
            sb.append(obj.getClass());
            super(sb.toString());
        }

        public NoModelLoaderAvailableException(@NonNull Object obj, @NonNull List list) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ModelLoaders for model class: ");
            sb.append(list);
            sb.append(", but none that handle this specific model instance: ");
            sb.append(obj);
            super(sb.toString());
        }
    }

    public class NoResultEncoderAvailableException extends MissingComponentException {
        public NoResultEncoderAvailableException(@NonNull Class cls) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to find result encoder for resource class: ");
            sb.append(cls);
            sb.append(", you may need to consider registering a new Encoder for the requested type or DiskCacheStrategy.DATA/DiskCacheStrategy.NONE if caching your transformed resource is unnecessary.");
            super(sb.toString());
        }
    }

    public class NoSourceEncoderAvailableException extends MissingComponentException {
        public NoSourceEncoderAvailableException(@NonNull Class cls) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to find source encoder for data class: ");
            sb.append(cls);
            super(sb.toString());
        }
    }

    public Registry() {
        setResourceDecoderBucketPriorityList(Arrays.asList(new String[]{BUCKET_GIF, BUCKET_BITMAP, BUCKET_BITMAP_DRAWABLE}));
    }

    @NonNull
    private List getDecodePaths(@NonNull Class cls, @NonNull Class cls2, @NonNull Class cls3) {
        ArrayList arrayList = new ArrayList();
        for (Class cls4 : this.decoderRegistry.getResourceClasses(cls, cls2)) {
            for (Class cls5 : this.transcoderRegistry.getTranscodeClasses(cls4, cls3)) {
                DecodePath decodePath = new DecodePath(cls, cls4, cls5, this.decoderRegistry.getDecoders(cls, cls4), this.transcoderRegistry.get(cls4, cls5), this.throwableListPool);
                arrayList.add(decodePath);
            }
        }
        return arrayList;
    }

    @NonNull
    public Registry append(@NonNull Class cls, @NonNull Encoder encoder) {
        this.encoderRegistry.append(cls, encoder);
        return this;
    }

    @NonNull
    public Registry append(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
        this.resourceEncoderRegistry.append(cls, resourceEncoder);
        return this;
    }

    @NonNull
    public Registry append(@NonNull Class cls, @NonNull Class cls2, @NonNull ResourceDecoder resourceDecoder) {
        append(BUCKET_APPEND_ALL, cls, cls2, resourceDecoder);
        return this;
    }

    @NonNull
    public Registry append(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        this.modelLoaderRegistry.append(cls, cls2, modelLoaderFactory);
        return this;
    }

    @NonNull
    public Registry append(@NonNull String str, @NonNull Class cls, @NonNull Class cls2, @NonNull ResourceDecoder resourceDecoder) {
        this.decoderRegistry.append(str, resourceDecoder, cls, cls2);
        return this;
    }

    @NonNull
    public List getImageHeaderParsers() {
        List parsers = this.imageHeaderParserRegistry.getParsers();
        if (!parsers.isEmpty()) {
            return parsers;
        }
        throw new NoImageHeaderParserException();
    }

    @Nullable
    public LoadPath getLoadPath(@NonNull Class cls, @NonNull Class cls2, @NonNull Class cls3) {
        LoadPath loadPath = this.loadPathCache.get(cls, cls2, cls3);
        if (this.loadPathCache.isEmptyLoadPath(loadPath)) {
            return null;
        }
        if (loadPath == null) {
            List decodePaths = getDecodePaths(cls, cls2, cls3);
            if (decodePaths.isEmpty()) {
                loadPath = null;
            } else {
                loadPath = new LoadPath(cls, cls2, cls3, decodePaths, this.throwableListPool);
            }
            this.loadPathCache.put(cls, cls2, cls3, loadPath);
        }
        return loadPath;
    }

    @NonNull
    public List getModelLoaders(@NonNull Object obj) {
        return this.modelLoaderRegistry.getModelLoaders(obj);
    }

    @NonNull
    public List getRegisteredResourceClasses(@NonNull Class cls, @NonNull Class cls2, @NonNull Class cls3) {
        List list = this.modelToResourceClassCache.get(cls, cls2, cls3);
        if (list == null) {
            list = new ArrayList();
            for (Class resourceClasses : this.modelLoaderRegistry.getDataClasses(cls)) {
                for (Class cls4 : this.decoderRegistry.getResourceClasses(resourceClasses, cls2)) {
                    if (!this.transcoderRegistry.getTranscodeClasses(cls4, cls3).isEmpty() && !list.contains(cls4)) {
                        list.add(cls4);
                    }
                }
            }
            this.modelToResourceClassCache.put(cls, cls2, cls3, Collections.unmodifiableList(list));
        }
        return list;
    }

    @NonNull
    public ResourceEncoder getResultEncoder(@NonNull Resource resource) {
        ResourceEncoder resourceEncoder = this.resourceEncoderRegistry.get(resource.getResourceClass());
        if (resourceEncoder != null) {
            return resourceEncoder;
        }
        throw new NoResultEncoderAvailableException(resource.getResourceClass());
    }

    @NonNull
    public DataRewinder getRewinder(@NonNull Object obj) {
        return this.dataRewinderRegistry.build(obj);
    }

    @NonNull
    public Encoder getSourceEncoder(@NonNull Object obj) {
        Encoder encoder = this.encoderRegistry.getEncoder(obj.getClass());
        if (encoder != null) {
            return encoder;
        }
        throw new NoSourceEncoderAvailableException(obj.getClass());
    }

    public boolean isResourceEncoderAvailable(@NonNull Resource resource) {
        return this.resourceEncoderRegistry.get(resource.getResourceClass()) != null;
    }

    @NonNull
    public Registry prepend(@NonNull Class cls, @NonNull Encoder encoder) {
        this.encoderRegistry.prepend(cls, encoder);
        return this;
    }

    @NonNull
    public Registry prepend(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
        this.resourceEncoderRegistry.prepend(cls, resourceEncoder);
        return this;
    }

    @NonNull
    public Registry prepend(@NonNull Class cls, @NonNull Class cls2, @NonNull ResourceDecoder resourceDecoder) {
        prepend(BUCKET_PREPEND_ALL, cls, cls2, resourceDecoder);
        return this;
    }

    @NonNull
    public Registry prepend(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        this.modelLoaderRegistry.prepend(cls, cls2, modelLoaderFactory);
        return this;
    }

    @NonNull
    public Registry prepend(@NonNull String str, @NonNull Class cls, @NonNull Class cls2, @NonNull ResourceDecoder resourceDecoder) {
        this.decoderRegistry.prepend(str, resourceDecoder, cls, cls2);
        return this;
    }

    @NonNull
    public Registry register(@NonNull ImageHeaderParser imageHeaderParser) {
        this.imageHeaderParserRegistry.add(imageHeaderParser);
        return this;
    }

    @NonNull
    public Registry register(@NonNull Factory factory) {
        this.dataRewinderRegistry.register(factory);
        return this;
    }

    @NonNull
    @Deprecated
    public Registry register(@NonNull Class cls, @NonNull Encoder encoder) {
        return append(cls, encoder);
    }

    @NonNull
    @Deprecated
    public Registry register(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
        return append(cls, resourceEncoder);
    }

    @NonNull
    public Registry register(@NonNull Class cls, @NonNull Class cls2, @NonNull ResourceTranscoder resourceTranscoder) {
        this.transcoderRegistry.register(cls, cls2, resourceTranscoder);
        return this;
    }

    @NonNull
    public Registry replace(@NonNull Class cls, @NonNull Class cls2, @NonNull ModelLoaderFactory modelLoaderFactory) {
        this.modelLoaderRegistry.replace(cls, cls2, modelLoaderFactory);
        return this;
    }

    @NonNull
    public final Registry setResourceDecoderBucketPriorityList(@NonNull List list) {
        ArrayList arrayList = new ArrayList(list.size());
        arrayList.addAll(list);
        arrayList.add(0, BUCKET_PREPEND_ALL);
        arrayList.add(BUCKET_APPEND_ALL);
        this.decoderRegistry.setBucketPriorityList(arrayList);
        return this;
    }
}
