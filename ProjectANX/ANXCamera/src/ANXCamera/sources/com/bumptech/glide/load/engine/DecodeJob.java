package com.bumptech.glide.load.engine;

import android.os.Build.VERSION;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry.NoResultEncoderAvailableException;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob implements FetcherReadyCallback, Runnable, Comparable, Poolable {
    private static final String TAG = "DecodeJob";
    private Callback callback;
    private Key currentAttemptingKey;
    private Object currentData;
    private DataSource currentDataSource;
    private DataFetcher currentFetcher;
    private volatile DataFetcherGenerator currentGenerator;
    private Key currentSourceKey;
    private Thread currentThread;
    private final DecodeHelper decodeHelper = new DecodeHelper();
    private final DeferredEncodeManager deferredEncodeManager = new DeferredEncodeManager();
    private final DiskCacheProvider diskCacheProvider;
    private DiskCacheStrategy diskCacheStrategy;
    private GlideContext glideContext;
    private int height;
    private volatile boolean isCallbackNotified;
    private volatile boolean isCancelled;
    private EngineKey loadKey;
    private Object model;
    private boolean onlyRetrieveFromCache;
    private Options options;
    private int order;
    private final Pool pool;
    private Priority priority;
    private final ReleaseManager releaseManager = new ReleaseManager();
    private RunReason runReason;
    private Key signature;
    private Stage stage;
    private long startFetchTime;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private final List throwables = new ArrayList();
    private int width;

    /* renamed from: com.bumptech.glide.load.engine.DecodeJob$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$load$EncodeStrategy = new int[EncodeStrategy.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason = new int[RunReason.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage = new int[Stage.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(25:0|1|2|3|(2:5|6)|7|9|10|11|12|13|15|16|17|18|19|20|21|23|24|25|26|27|28|30) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|18|19|20|21|23|24|25|26|27|28|30) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0070 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x007a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$com$bumptech$glide$load$EncodeStrategy[EncodeStrategy.SOURCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$load$EncodeStrategy[EncodeStrategy.TRANSFORMED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.RESOURCE_CACHE.ordinal()] = 1;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.DATA_CACHE.ordinal()] = 2;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.SOURCE.ordinal()] = 3;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.FINISHED.ordinal()] = 4;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[Stage.INITIALIZE.ordinal()] = 5;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[RunReason.INITIALIZE.ordinal()] = 1;
            $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[RunReason.SWITCH_TO_SOURCE_SERVICE.ordinal()] = 2;
            try {
                $SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[RunReason.DECODE_DATA.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    interface Callback {
        void onLoadFailed(GlideException glideException);

        void onResourceReady(Resource resource, DataSource dataSource);

        void reschedule(DecodeJob decodeJob);
    }

    final class DecodeCallback implements DecodeCallback {
        private final DataSource dataSource;

        DecodeCallback(DataSource dataSource2) {
            this.dataSource = dataSource2;
        }

        @NonNull
        public Resource onResourceDecoded(@NonNull Resource resource) {
            return DecodeJob.this.onResourceDecoded(this.dataSource, resource);
        }
    }

    class DeferredEncodeManager {
        private ResourceEncoder encoder;
        private Key key;
        private LockedResource toEncode;

        DeferredEncodeManager() {
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            this.key = null;
            this.encoder = null;
            this.toEncode = null;
        }

        /* access modifiers changed from: 0000 */
        public void encode(DiskCacheProvider diskCacheProvider, Options options) {
            try {
                diskCacheProvider.getDiskCache().put(this.key, new DataCacheWriter(this.encoder, this.toEncode, options));
            } finally {
                this.toEncode.unlock();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean hasResourceToEncode() {
            return this.toEncode != null;
        }

        /* access modifiers changed from: 0000 */
        public void init(Key key2, ResourceEncoder resourceEncoder, LockedResource lockedResource) {
            this.key = key2;
            this.encoder = resourceEncoder;
            this.toEncode = lockedResource;
        }
    }

    interface DiskCacheProvider {
        DiskCache getDiskCache();
    }

    class ReleaseManager {
        private boolean isEncodeComplete;
        private boolean isFailed;
        private boolean isReleased;

        ReleaseManager() {
        }

        private boolean isComplete(boolean z) {
            return (this.isFailed || z || this.isEncodeComplete) && this.isReleased;
        }

        /* access modifiers changed from: 0000 */
        public synchronized boolean onEncodeComplete() {
            this.isEncodeComplete = true;
            return isComplete(false);
        }

        /* access modifiers changed from: 0000 */
        public synchronized boolean onFailed() {
            this.isFailed = true;
            return isComplete(false);
        }

        /* access modifiers changed from: 0000 */
        public synchronized boolean release(boolean z) {
            this.isReleased = true;
            return isComplete(z);
        }

        /* access modifiers changed from: 0000 */
        public synchronized void reset() {
            this.isEncodeComplete = false;
            this.isReleased = false;
            this.isFailed = false;
        }
    }

    enum RunReason {
        INITIALIZE,
        SWITCH_TO_SOURCE_SERVICE,
        DECODE_DATA
    }

    enum Stage {
        INITIALIZE,
        RESOURCE_CACHE,
        DATA_CACHE,
        SOURCE,
        ENCODE,
        FINISHED
    }

    DecodeJob(DiskCacheProvider diskCacheProvider2, Pool pool2) {
        this.diskCacheProvider = diskCacheProvider2;
        this.pool = pool2;
    }

    private Resource decodeFromData(DataFetcher dataFetcher, Object obj, DataSource dataSource) {
        if (obj == null) {
            dataFetcher.cleanup();
            return null;
        }
        try {
            long logTime = LogTime.getLogTime();
            Resource decodeFromFetcher = decodeFromFetcher(obj, dataSource);
            if (Log.isLoggable(TAG, 2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Decoded result ");
                sb.append(decodeFromFetcher);
                logWithTimeAndKey(sb.toString(), logTime);
            }
            return decodeFromFetcher;
        } finally {
            dataFetcher.cleanup();
        }
    }

    private Resource decodeFromFetcher(Object obj, DataSource dataSource) {
        return runLoadPath(obj, dataSource, this.decodeHelper.getLoadPath(obj.getClass()));
    }

    private void decodeFromRetrievedData() {
        if (Log.isLoggable(TAG, 2)) {
            long j = this.startFetchTime;
            StringBuilder sb = new StringBuilder();
            sb.append("data: ");
            sb.append(this.currentData);
            sb.append(", cache key: ");
            sb.append(this.currentSourceKey);
            sb.append(", fetcher: ");
            sb.append(this.currentFetcher);
            logWithTimeAndKey("Retrieved data", j, sb.toString());
        }
        Resource resource = null;
        try {
            resource = decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
        } catch (GlideException e) {
            e.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.throwables.add(e);
        }
        if (resource != null) {
            notifyEncodeAndRelease(resource, this.currentDataSource);
        } else {
            runGenerators();
        }
    }

    private DataFetcherGenerator getNextGenerator() {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()];
        if (i == 1) {
            return new ResourceCacheGenerator(this.decodeHelper, this);
        }
        if (i == 2) {
            return new DataCacheGenerator(this.decodeHelper, this);
        }
        if (i == 3) {
            return new SourceGenerator(this.decodeHelper, this);
        }
        if (i == 4) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized stage: ");
        sb.append(this.stage);
        throw new IllegalStateException(sb.toString());
    }

    private Stage getNextStage(Stage stage2) {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[stage2.ordinal()];
        if (i == 1) {
            return this.diskCacheStrategy.decodeCachedData() ? Stage.DATA_CACHE : getNextStage(Stage.DATA_CACHE);
        } else if (i == 2) {
            return this.onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
        } else if (i == 3 || i == 4) {
            return Stage.FINISHED;
        } else {
            if (i == 5) {
                return this.diskCacheStrategy.decodeCachedResource() ? Stage.RESOURCE_CACHE : getNextStage(Stage.RESOURCE_CACHE);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized stage: ");
            sb.append(stage2);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @NonNull
    private Options getOptionsWithHardwareConfig(DataSource dataSource) {
        Options options2 = this.options;
        if (VERSION.SDK_INT < 26) {
            return options2;
        }
        boolean z = dataSource == DataSource.RESOURCE_DISK_CACHE || this.decodeHelper.isScaleOnlyOrNoTransform();
        Boolean bool = (Boolean) options2.get(Downsampler.ALLOW_HARDWARE_CONFIG);
        if (bool != null && (!bool.booleanValue() || z)) {
            return options2;
        }
        Options options3 = new Options();
        options3.putAll(this.options);
        options3.set(Downsampler.ALLOW_HARDWARE_CONFIG, Boolean.valueOf(z));
        return options3;
    }

    private int getPriority() {
        return this.priority.ordinal();
    }

    private void logWithTimeAndKey(String str, long j) {
        logWithTimeAndKey(str, j, null);
    }

    private void logWithTimeAndKey(String str, long j, String str2) {
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(j));
        sb.append(", load key: ");
        sb.append(this.loadKey);
        if (str2 != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(", ");
            sb2.append(str2);
            str3 = sb2.toString();
        } else {
            str3 = "";
        }
        sb.append(str3);
        sb.append(", thread: ");
        sb.append(Thread.currentThread().getName());
        Log.v(TAG, sb.toString());
    }

    private void notifyComplete(Resource resource, DataSource dataSource) {
        setNotifiedOrThrow();
        this.callback.onResourceReady(resource, dataSource);
    }

    /* JADX INFO: finally extract failed */
    private void notifyEncodeAndRelease(Resource resource, DataSource dataSource) {
        if (resource instanceof Initializable) {
            ((Initializable) resource).initialize();
        }
        LockedResource lockedResource = 0;
        if (this.deferredEncodeManager.hasResourceToEncode()) {
            r3 = LockedResource.obtain(resource);
            lockedResource = r3;
            resource = r3;
        }
        notifyComplete(resource, dataSource);
        this.stage = Stage.ENCODE;
        try {
            if (this.deferredEncodeManager.hasResourceToEncode()) {
                this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
            }
            if (lockedResource != 0) {
                lockedResource.unlock();
            }
            onEncodeComplete();
        } catch (Throwable th) {
            if (lockedResource != 0) {
                lockedResource.unlock();
            }
            throw th;
        }
    }

    private void notifyFailed() {
        setNotifiedOrThrow();
        this.callback.onLoadFailed(new GlideException("Failed to load resource", (List) new ArrayList(this.throwables)));
        onLoadFailed();
    }

    private void onEncodeComplete() {
        if (this.releaseManager.onEncodeComplete()) {
            releaseInternal();
        }
    }

    private void onLoadFailed() {
        if (this.releaseManager.onFailed()) {
            releaseInternal();
        }
    }

    private void releaseInternal() {
        this.releaseManager.reset();
        this.deferredEncodeManager.clear();
        this.decodeHelper.clear();
        this.isCallbackNotified = false;
        this.glideContext = null;
        this.signature = null;
        this.options = null;
        this.priority = null;
        this.loadKey = null;
        this.callback = null;
        this.stage = null;
        this.currentGenerator = null;
        this.currentThread = null;
        this.currentSourceKey = null;
        this.currentData = null;
        this.currentDataSource = null;
        this.currentFetcher = null;
        this.startFetchTime = 0;
        this.isCancelled = false;
        this.model = null;
        this.throwables.clear();
        this.pool.release(this);
    }

    private void runGenerators() {
        this.currentThread = Thread.currentThread();
        this.startFetchTime = LogTime.getLogTime();
        boolean z = false;
        while (!this.isCancelled && this.currentGenerator != null) {
            z = this.currentGenerator.startNext();
            if (z) {
                break;
            }
            this.stage = getNextStage(this.stage);
            this.currentGenerator = getNextGenerator();
            if (this.stage == Stage.SOURCE) {
                reschedule();
                return;
            }
        }
        if ((this.stage == Stage.FINISHED || this.isCancelled) && !z) {
            notifyFailed();
        }
    }

    private Resource runLoadPath(Object obj, DataSource dataSource, LoadPath loadPath) {
        Options optionsWithHardwareConfig = getOptionsWithHardwareConfig(dataSource);
        DataRewinder rewinder = this.glideContext.getRegistry().getRewinder(obj);
        try {
            return loadPath.load(rewinder, optionsWithHardwareConfig, this.width, this.height, new DecodeCallback(dataSource));
        } finally {
            rewinder.cleanup();
        }
    }

    private void runWrapped() {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()];
        if (i == 1) {
            this.stage = getNextStage(Stage.INITIALIZE);
            this.currentGenerator = getNextGenerator();
        } else if (i != 2) {
            if (i == 3) {
                decodeFromRetrievedData();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized run reason: ");
            sb.append(this.runReason);
            throw new IllegalStateException(sb.toString());
        }
        runGenerators();
    }

    private void setNotifiedOrThrow() {
        Throwable th;
        this.stateVerifier.throwIfRecycled();
        if (this.isCallbackNotified) {
            if (this.throwables.isEmpty()) {
                th = null;
            } else {
                List list = this.throwables;
                th = (Throwable) list.get(list.size() - 1);
            }
            throw new IllegalStateException("Already notified", th);
        }
        this.isCallbackNotified = true;
    }

    public void cancel() {
        this.isCancelled = true;
        DataFetcherGenerator dataFetcherGenerator = this.currentGenerator;
        if (dataFetcherGenerator != null) {
            dataFetcherGenerator.cancel();
        }
    }

    public int compareTo(@NonNull DecodeJob decodeJob) {
        int priority2 = getPriority() - decodeJob.getPriority();
        return priority2 == 0 ? this.order - decodeJob.order : priority2;
    }

    @NonNull
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    /* access modifiers changed from: 0000 */
    public DecodeJob init(GlideContext glideContext2, Object obj, EngineKey engineKey, Key key, int i, int i2, Class cls, Class cls2, Priority priority2, DiskCacheStrategy diskCacheStrategy2, Map map, boolean z, boolean z2, boolean z3, Options options2, Callback callback2, int i3) {
        this.decodeHelper.init(glideContext2, obj, key, i, i2, diskCacheStrategy2, cls, cls2, priority2, options2, map, z, z2, this.diskCacheProvider);
        this.glideContext = glideContext2;
        this.signature = key;
        this.priority = priority2;
        this.loadKey = engineKey;
        this.width = i;
        this.height = i2;
        this.diskCacheStrategy = diskCacheStrategy2;
        this.onlyRetrieveFromCache = z3;
        this.options = options2;
        this.callback = callback2;
        this.order = i3;
        this.runReason = RunReason.INITIALIZE;
        this.model = obj;
        return this;
    }

    public void onDataFetcherFailed(Key key, Exception exc, DataFetcher dataFetcher, DataSource dataSource) {
        dataFetcher.cleanup();
        GlideException glideException = new GlideException("Fetching data failed", (Throwable) exc);
        glideException.setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.throwables.add(glideException);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
            this.callback.reschedule(this);
            return;
        }
        runGenerators();
    }

    public void onDataFetcherReady(Key key, Object obj, DataFetcher dataFetcher, DataSource dataSource, Key key2) {
        this.currentSourceKey = key;
        this.currentData = obj;
        this.currentFetcher = dataFetcher;
        this.currentDataSource = dataSource;
        this.currentAttemptingKey = key2;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.DECODE_DATA;
            this.callback.reschedule(this);
            return;
        }
        decodeFromRetrievedData();
    }

    /* JADX WARNING: type inference failed for: r12v7, types: [com.bumptech.glide.load.engine.DataCacheKey] */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @NonNull
    public Resource onResourceDecoded(DataSource dataSource, @NonNull Resource resource) {
        Transformation transformation;
        Resource resource2;
        EncodeStrategy encodeStrategy;
        ResourceCacheKey resourceCacheKey;
        Class cls = resource.get().getClass();
        ResourceEncoder resourceEncoder = null;
        if (dataSource != DataSource.RESOURCE_DISK_CACHE) {
            Transformation transformation2 = this.decodeHelper.getTransformation(cls);
            transformation = transformation2;
            resource2 = transformation2.transform(this.glideContext, resource, this.width, this.height);
        } else {
            resource2 = resource;
            transformation = null;
        }
        if (!resource.equals(resource2)) {
            resource.recycle();
        }
        if (this.decodeHelper.isResourceEncoderAvailable(resource2)) {
            resourceEncoder = this.decodeHelper.getResultEncoder(resource2);
            encodeStrategy = resourceEncoder.getEncodeStrategy(this.options);
        } else {
            encodeStrategy = EncodeStrategy.NONE;
        }
        ResourceEncoder resourceEncoder2 = resourceEncoder;
        if (!this.diskCacheStrategy.isResourceCacheable(!this.decodeHelper.isSourceKey(this.currentSourceKey), dataSource, encodeStrategy)) {
            return resource2;
        }
        if (resourceEncoder2 != null) {
            int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$load$EncodeStrategy[encodeStrategy.ordinal()];
            if (i == 1) {
                resourceCacheKey = new DataCacheKey(this.currentSourceKey, this.signature);
            } else if (i == 2) {
                ResourceCacheKey resourceCacheKey2 = new ResourceCacheKey(this.decodeHelper.getArrayPool(), this.currentSourceKey, this.signature, this.width, this.height, transformation, cls, this.options);
                resourceCacheKey = resourceCacheKey2;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown strategy: ");
                sb.append(encodeStrategy);
                throw new IllegalArgumentException(sb.toString());
            }
            LockedResource obtain = LockedResource.obtain(resource2);
            this.deferredEncodeManager.init(resourceCacheKey, resourceEncoder2, obtain);
            return obtain;
        }
        throw new NoResultEncoderAvailableException(resource2.get().getClass());
    }

    /* access modifiers changed from: 0000 */
    public void release(boolean z) {
        if (this.releaseManager.release(z)) {
            releaseInternal();
        }
    }

    public void reschedule() {
        this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        this.callback.reschedule(this);
    }

    public void run() {
        String str = TAG;
        DataFetcher dataFetcher = this.currentFetcher;
        try {
            if (this.isCancelled) {
                notifyFailed();
                if (dataFetcher != null) {
                    dataFetcher.cleanup();
                }
                return;
            }
            runWrapped();
            if (dataFetcher != null) {
                dataFetcher.cleanup();
            }
        } catch (CallbackException e) {
            throw e;
        } catch (Throwable th) {
            if (dataFetcher != null) {
                dataFetcher.cleanup();
            }
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean willDecodeFromCache() {
        Stage nextStage = getNextStage(Stage.INITIALIZE);
        return nextStage == Stage.RESOURCE_CACHE || nextStage == Stage.DATA_CACHE;
    }
}
