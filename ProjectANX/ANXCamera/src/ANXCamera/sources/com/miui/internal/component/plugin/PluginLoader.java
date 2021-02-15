package com.miui.internal.component.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import miui.os.FileUtils;

public class PluginLoader {
    private static final String OPT_FOLDER = "plugins-opt";
    private static final String PLUGIN_FILE_EXTENSION = ".apk";
    private static final String PLUGIN_MANIFEST_BUILT_IN_PATH = "assets/PluginManifest.xml";
    private static final String PLUGIN_MANIFEST_EXTENSION = ".xml";
    private static final String PLUGIN_MANIFEST_FILE_NAME = "PluginManifest.xml";
    private static Map classLoaderMap = new HashMap();
    private static Map extensionMap = new HashMap();
    private static Map pluginMap = new HashMap();
    private static Map resourcesMap = new HashMap();
    private File optFolder = new File(this.pluginFolder.getParentFile(), OPT_FOLDER);
    private File pluginFolder;

    public PluginLoader(File file) {
        this.pluginFolder = file;
        this.pluginFolder.mkdirs();
        this.optFolder.mkdirs();
        loadPlugins();
        buildExtensionMap();
    }

    private void addAssetPath(AssetManager assetManager, String str) {
        String str2 = "invoke asset manager encounter an error: ";
        try {
            AssetManager.class.getMethod("addAssetPath", new Class[]{String.class}).invoke(assetManager, new Object[]{str});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(e.getMessage());
            throw new PluginException(sb.toString());
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(e2.getMessage());
            throw new PluginException(sb2.toString());
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(e3.getMessage());
            throw new PluginException(sb3.toString());
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(e4.getMessage());
            throw new PluginException(sb4.toString());
        }
    }

    private void buildExtensionMap() {
        for (String str : pluginMap.keySet()) {
            Plugin plugin = (Plugin) pluginMap.get(str);
            for (String str2 : plugin.getExtensions().keySet()) {
                List list = (List) extensionMap.get(str2);
                if (list == null) {
                    list = new ArrayList();
                    extensionMap.put(str2, list);
                }
                list.add(plugin);
            }
        }
    }

    private Plugin loadPlugin(File file, File file2) {
        return loadPlugin((InputStream) new FileInputStream(file), file2);
    }

    private Plugin loadPlugin(InputStream inputStream, File file) {
        Plugin parsePlugin = new PluginManifestParser().parsePlugin(inputStream);
        parsePlugin.setFile(file);
        return parsePlugin;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x007e A[SYNTHETIC, Splitter:B:31:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0088 A[SYNTHETIC, Splitter:B:37:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0093 A[SYNTHETIC, Splitter:B:42:0x0093] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x009c A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x009c A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadPlugins() {
        ZipFile zipFile;
        loadSelf();
        File[] listFiles = this.pluginFolder.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().endsWith(".apk")) {
                    File file2 = this.pluginFolder;
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileUtils.getName(file.getPath()));
                    sb.append(PLUGIN_MANIFEST_EXTENSION);
                    File file3 = new File(file2, sb.toString());
                    ZipFile zipFile2 = null;
                    try {
                        if (!file3.exists()) {
                            ZipFile zipFile3 = new ZipFile(file);
                            try {
                                FileUtils.copyToFile(zipFile3.getInputStream(new ZipEntry(PLUGIN_MANIFEST_BUILT_IN_PATH)), file3);
                                zipFile2 = zipFile3;
                            } catch (IOException e) {
                                e = e;
                                zipFile = zipFile3;
                                try {
                                    e.printStackTrace();
                                    if (zipFile == null) {
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    if (zipFile != null) {
                                        try {
                                            zipFile.close();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                    throw th;
                                }
                            } catch (PluginParseException e3) {
                                e = e3;
                                zipFile2 = zipFile3;
                                e.printStackTrace();
                                if (zipFile == null) {
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                zipFile = zipFile3;
                                if (zipFile != null) {
                                }
                                throw th;
                            }
                        }
                        Plugin loadPlugin = loadPlugin(file3, file);
                        pluginMap.put(loadPlugin.getName(), loadPlugin);
                        if (zipFile2 != null) {
                            try {
                                zipFile2.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                    } catch (IOException e5) {
                        e = e5;
                        e.printStackTrace();
                        if (zipFile == null) {
                            zipFile.close();
                        }
                    } catch (PluginParseException e6) {
                        e = e6;
                        e.printStackTrace();
                        if (zipFile == null) {
                            zipFile.close();
                        }
                    }
                }
            }
        }
    }

    private void loadSelf() {
        Context applicationContext = PluginContext.getInstance().getApplicationContext();
        Plugin plugin = null;
        try {
            String packageResourcePath = applicationContext.getPackageResourcePath();
            if (packageResourcePath != null) {
                plugin = loadPlugin(applicationContext.getAssets().open(PLUGIN_MANIFEST_FILE_NAME), new File(packageResourcePath));
            }
        } catch (IOException unused) {
        } catch (PluginParseException e) {
            e.printStackTrace();
        }
        if (plugin == null) {
            plugin = new Plugin();
            plugin.setName(applicationContext.getPackageName());
            plugin.setGroup(applicationContext.getPackageName());
            plugin.setResources(AccessPermission.PRIVATE);
        }
        pluginMap.put(plugin.getName(), plugin);
        classLoaderMap.put(plugin.getName(), PluginLoader.class.getClassLoader());
        resourcesMap.put(plugin.getName(), applicationContext.getResources());
    }

    private ClassLoader newClassLoader(Plugin plugin) {
        return new DexClassLoader(plugin.getFile().getAbsolutePath(), this.optFolder.getAbsolutePath(), null, PluginLoader.class.getClassLoader());
    }

    private Resources newResources(Plugin plugin) {
        String str = "invoke asset manager encounter an error: ";
        try {
            AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
            addAssetPath(assetManager, plugin.getFile().getPath());
            for (Dependency dependency : plugin.getDependencies().values()) {
                if (dependency.isResourcesRequired()) {
                    addAssetPath(assetManager, ((Plugin) pluginMap.get(dependency.getName())).getFile().getPath());
                }
            }
            Resources resources = PluginContext.getInstance().getApplicationContext().getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (InstantiationException e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(e.getMessage());
            throw new PluginException(sb.toString());
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(e2.getMessage());
            throw new PluginException(sb2.toString());
        }
    }

    public ClassLoader getClassLoader(Plugin plugin) {
        if (!classLoaderMap.containsKey(plugin.getName())) {
            synchronized (classLoaderMap) {
                if (!classLoaderMap.containsKey(plugin.getName())) {
                    classLoaderMap.put(plugin.getName(), newClassLoader(plugin));
                }
            }
        }
        return (ClassLoader) classLoaderMap.get(plugin.getName());
    }

    public List getPlugins(String str) {
        return (List) extensionMap.get(str);
    }

    public Resources getResources(Plugin plugin) {
        if (!resourcesMap.containsKey(plugin.getName())) {
            synchronized (resourcesMap) {
                if (!resourcesMap.containsKey(plugin.getName())) {
                    resourcesMap.put(plugin.getName(), newResources(plugin));
                }
            }
        }
        return (Resources) resourcesMap.get(plugin.getName());
    }

    public boolean hasExtension(String str) {
        return extensionMap.containsKey(str);
    }
}
