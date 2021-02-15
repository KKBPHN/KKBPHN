package com.iqiyi.android.qigsaw.core.splitload;

public class SplitDelegateClassLoaderFactory {
    public static ClassLoader instantiateClassLoader(ClassLoader classLoader) {
        return new SplitDelegateClassloader(classLoader);
    }
}
