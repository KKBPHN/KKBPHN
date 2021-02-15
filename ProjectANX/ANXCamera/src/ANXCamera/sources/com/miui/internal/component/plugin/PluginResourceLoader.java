package com.miui.internal.component.plugin;

import android.content.res.Resources;
import java.util.List;

public class PluginResourceLoader {
    private PluginLoader pluginLoader;

    public PluginResourceLoader(PluginLoader pluginLoader2) {
        this.pluginLoader = pluginLoader2;
    }

    public Resources lookup(Class cls) {
        return lookup(cls.getName());
    }

    public Resources lookup(String str) {
        List plugins = this.pluginLoader.getPlugins(str);
        if (plugins == null || plugins.size() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("no plugin found for extension ");
            sb.append(str);
            throw new PluginException(sb.toString());
        } else if (plugins.size() <= 1) {
            return this.pluginLoader.getResources((Plugin) plugins.get(0));
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("more than one plugin found for extension ");
            sb2.append(str);
            throw new PluginException(sb2.toString());
        }
    }
}
