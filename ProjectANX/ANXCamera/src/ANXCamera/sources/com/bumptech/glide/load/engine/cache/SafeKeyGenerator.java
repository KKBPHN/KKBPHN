package com.bumptech.glide.load.engine.cache;

import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
    private final Pool digestPool = FactoryPools.threadSafe(10, new Factory() {
        public PoolableDigestContainer create() {
            try {
                return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    });
    private final LruCache loadIdToSafeHash = new LruCache(1000);

    final class PoolableDigestContainer implements Poolable {
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest2) {
            this.messageDigest = messageDigest2;
        }

        @NonNull
        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }

    private String calculateHexStringDigest(Key key) {
        Object acquire = this.digestPool.acquire();
        Preconditions.checkNotNull(acquire);
        PoolableDigestContainer poolableDigestContainer = (PoolableDigestContainer) acquire;
        try {
            key.updateDiskCacheKey(poolableDigestContainer.messageDigest);
            return Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
        } finally {
            this.digestPool.release(poolableDigestContainer);
        }
    }

    public String getSafeKey(Key key) {
        String str;
        synchronized (this.loadIdToSafeHash) {
            str = (String) this.loadIdToSafeHash.get(key);
        }
        if (str == null) {
            str = calculateHexStringDigest(key);
        }
        synchronized (this.loadIdToSafeHash) {
            this.loadIdToSafeHash.put(key, str);
        }
        return str;
    }
}
