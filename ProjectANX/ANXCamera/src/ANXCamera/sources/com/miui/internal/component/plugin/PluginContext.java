package com.miui.internal.component.plugin;

import android.content.Context;

public class PluginContext {
    private static volatile PluginContext instance;
    private Context applicationContext;
    private PluginClassLoader pluginClassLoader;
    private PluginLoader pluginLoader;
    private PluginResourceLoader pluginResourceLoader;

    private PluginContext() {
    }

    public static PluginContext getInstance() {
        if (instance == null) {
            synchronized (PluginContext.class) {
                if (instance == null) {
                    instance = new PluginContext();
                }
            }
        }
        return instance;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public PluginClassLoader getPluginClassLoader() {
        return this.pluginClassLoader;
    }

    public PluginLoader getPluginLoader() {
        return this.pluginLoader;
    }

    public PluginResourceLoader getPluginResourceLoader() {
        return this.pluginResourceLoader;
    }

    public void setApplicationContext(Context context) {
        this.applicationContext = context;
    }

    public void setPluginClassLoader(PluginClassLoader pluginClassLoader2) {
        this.pluginClassLoader = pluginClassLoader2;
    }

    public void setPluginLoader(PluginLoader pluginLoader2) {
        this.pluginLoader = pluginLoader2;
    }

    public void setPluginResourceLoader(PluginResourceLoader pluginResourceLoader2) {
        this.pluginResourceLoader = pluginResourceLoader2;
    }
}
