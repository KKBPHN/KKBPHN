package com.miui.internal.hybrid;

import java.util.HashMap;
import java.util.Map;
import miui.hybrid.HybridFeature;

public class FeatureManager {
    private Config mConfig;
    private Map mFeatures = new HashMap();
    private ClassLoader mLoader;

    public FeatureManager(Config config, ClassLoader classLoader) {
        this.mConfig = config;
        this.mLoader = classLoader;
    }

    private HybridFeature initFeature(String str) {
        try {
            return (HybridFeature) this.mLoader.loadClass(str).newInstance();
        } catch (ClassNotFoundException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("feature not found: ");
            sb.append(str);
            throw new HybridException(204, sb.toString());
        } catch (InstantiationException unused2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("feature cannot be instantiated: ");
            sb2.append(str);
            throw new HybridException(204, sb2.toString());
        } catch (IllegalAccessException unused3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("feature cannot be accessed: ");
            sb3.append(str);
            throw new HybridException(204, sb3.toString());
        }
    }

    public HybridFeature lookupFeature(String str) {
        HybridFeature hybridFeature = (HybridFeature) this.mFeatures.get(str);
        if (hybridFeature != null) {
            return hybridFeature;
        }
        Feature feature = this.mConfig.getFeature(str);
        if (feature != null) {
            HybridFeature initFeature = initFeature(str);
            initFeature.setParams(feature.getParams());
            this.mFeatures.put(str, initFeature);
            return initFeature;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("feature not declared: ");
        sb.append(str);
        throw new HybridException(204, sb.toString());
    }
}
