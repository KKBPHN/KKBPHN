package com.miui.internal.component;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import com.miui.internal.component.plugin.PluginClassLoader;
import com.miui.internal.component.plugin.PluginContext;
import com.miui.internal.component.plugin.PluginLoader;
import com.miui.internal.component.plugin.PluginResourceLoader;
import com.miui.internal.util.PackageConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import miui.core.Manifest;
import miui.module.ModuleManager;
import miui.os.FileUtils;
import miui.util.IOUtils;

public class ComponentManager {
    private static final String PLUGIN_BUILT_IN_FOLDER = "plugins";
    private static final String PLUGIN_TARGET_FOLDER = "plugins";
    private Application mApplication;
    private Manifest mManifest;

    public ComponentManager(Application application, Manifest manifest) {
        this.mApplication = application;
        this.mManifest = manifest;
    }

    private void extractFiles(Context context, String str, File file) {
        String[] list;
        try {
            file.mkdirs();
            AssetManager assets = context.getAssets();
            for (String str2 : assets.list(str)) {
                File file2 = new File(file, str2);
                file2.getParentFile().mkdirs();
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("/");
                sb.append(str2);
                InputStream open = assets.open(sb.toString());
                FileUtils.copyToFile(open, file2);
                IOUtils.closeQuietly(open);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getPluginsFolder(Context context) {
        return new File(PackageConstants.getSdkBaseFolder(context), "plugins");
    }

    private void loadModules() {
        ModuleManager.createInstance(this.mApplication).loadModules((String[]) this.mManifest.getDependencies().keySet().toArray(new String[0]));
    }

    private void loadPlugins() {
        PluginContext.getInstance().setApplicationContext(this.mApplication);
        File pluginsFolder = getPluginsFolder(this.mApplication);
        if (!pluginsFolder.exists()) {
            extractFiles(this.mApplication, "plugins", pluginsFolder);
        }
        PluginLoader pluginLoader = new PluginLoader(pluginsFolder);
        PluginContext.getInstance().setPluginLoader(pluginLoader);
        PluginContext.getInstance().setPluginClassLoader(new PluginClassLoader(pluginLoader));
        PluginContext.getInstance().setPluginResourceLoader(new PluginResourceLoader(pluginLoader));
    }

    public void load() {
        loadModules();
        loadPlugins();
    }
}
