package com.miui.internal.component.plugin;

import java.util.List;

public class PluginClassLoader {
    private PluginLoader pluginLoader;

    public PluginClassLoader(PluginLoader pluginLoader2) {
        this.pluginLoader = pluginLoader2;
    }

    public Object lookup(Class cls) {
        return lookup(cls.getName());
    }

    public Object lookup(String str) {
        String str2 = " failed in plugin ";
        List plugins = this.pluginLoader.getPlugins(str);
        if (plugins == null || plugins.size() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("no plugin found for extension ");
            sb.append(str);
            throw new PluginException(sb.toString());
        } else if (plugins.size() <= 1) {
            Plugin plugin = (Plugin) plugins.get(0);
            try {
                return this.pluginLoader.getClassLoader(plugin).loadClass(plugin.getExtension(str).getLocation()).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("no impl of extension ");
                sb2.append(str);
                sb2.append(" found in plugin ");
                sb2.append(plugin.getName());
                throw new PluginException(sb2.toString());
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("init impl of extension ");
                sb3.append(str);
                sb3.append(str2);
                sb3.append(plugin.getName());
                throw new PluginException(sb3.toString());
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
                StringBuilder sb4 = new StringBuilder();
                sb4.append("access impl of extension ");
                sb4.append(str);
                sb4.append(str2);
                sb4.append(plugin.getName());
                throw new PluginException(sb4.toString());
            }
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("more than one plugin found for extension ");
            sb5.append(str);
            throw new PluginException(sb5.toString());
        }
    }
}
