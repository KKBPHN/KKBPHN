package com.miui.internal.component.module;

import android.app.Application;
import android.text.TextUtils;
import com.miui.internal.util.PackageHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import miui.core.ManifestParser;
import miui.module.Dependency;
import miui.module.Module;

public class ModuleLoader {
    private Set loadedModules = new LinkedHashSet();
    private Set loadedResources = new LinkedHashSet();
    private Application mApplication;
    private ModuleFolder[] moduleFolders;

    class ModuleEntry {
        public String apkPath;
        public String libFolderPath;
        public Module module;
        public String optFolderPath;
        public boolean optional;

        public ModuleEntry(Module module2, String str, String str2, String str3, boolean z) {
            this.module = module2;
            this.apkPath = str;
            this.libFolderPath = str2;
            this.optFolderPath = str3;
            this.optional = z;
        }
    }

    public ModuleLoader(Application application, ModuleFolder... moduleFolderArr) {
        this.mApplication = application;
        this.moduleFolders = moduleFolderArr;
    }

    private void load(ModuleEntry moduleEntry, String str) {
        String str2 = null;
        String str3 = (moduleEntry.module.getContent() & 1) != 0 ? moduleEntry.apkPath : null;
        String str4 = (moduleEntry.module.getContent() & 2) != 0 ? moduleEntry.libFolderPath : null;
        if ((moduleEntry.module.getContent() & 4) != 0) {
            str2 = moduleEntry.apkPath;
        }
        boolean z = false;
        if (!(str3 == null && str4 == null)) {
            z = !loadClass(str, str3, str4);
            if (z && !moduleEntry.optional) {
                StringBuilder sb = new StringBuilder();
                sb.append("encounter error when load dex for module: ");
                sb.append(moduleEntry.module.getName());
                throw new ModuleLoadException(sb.toString());
            }
        }
        if (str2 != null && !z) {
            loadResource(str2);
            if (!z || moduleEntry.optional) {
                this.loadedResources.add(moduleEntry.apkPath);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("encounter error when load res for module: ");
                sb2.append(moduleEntry.module.getName());
                throw new ModuleLoadException(sb2.toString());
            }
        }
        if (!z) {
            this.loadedModules.add(moduleEntry.module.getName());
        }
    }

    private boolean loadClass(String str, String str2, String str3) {
        return ModuleClassLoader.load(str2, str, str3, this.mApplication.getClassLoader(), this.mApplication);
    }

    private void loadResource(String str) {
        ModuleResourceLoader.load(Arrays.asList(new String[]{str}), new ArrayList(this.loadedResources));
    }

    public static void loadResources(String... strArr) {
        ModuleResourceLoader.load(Arrays.asList(strArr));
    }

    private Module resolveModule(String str) {
        return ManifestParser.createFromArchive(this.mApplication.getPackageManager(), str).parse(null).getModule();
    }

    public void loadModules(Dependency... dependencyArr) {
        boolean z;
        Dependency[] dependencyArr2 = dependencyArr;
        ArrayList<ModuleEntry> arrayList = new ArrayList<>();
        ArrayList<ModuleEntry> arrayList2 = new ArrayList<>();
        for (Dependency dependency : dependencyArr2) {
            if (!this.loadedModules.contains(dependency.getName())) {
                boolean z2 = (dependency.getType() & 1) != 0;
                ModuleFolder[] moduleFolderArr = this.moduleFolders;
                int length = moduleFolderArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    }
                    ModuleFolder moduleFolder = moduleFolderArr[i];
                    File apkFolder = moduleFolder.getApkFolder();
                    StringBuilder sb = new StringBuilder();
                    sb.append(dependency.getName());
                    sb.append(".apk");
                    File file = new File(apkFolder, sb.toString());
                    if (file.exists()) {
                        String absolutePath = file.getAbsolutePath();
                        ModuleEntry moduleEntry = new ModuleEntry(resolveModule(absolutePath), absolutePath, ModuleUtils.getLibraryFolder(absolutePath).getAbsolutePath(), moduleFolder.getOptFolder().getAbsolutePath(), z2);
                        arrayList.add(moduleEntry);
                        z = true;
                        break;
                    }
                    i++;
                }
                if (!z) {
                    String apkPath = PackageHelper.getApkPath(this.mApplication, dependency.getName(), null);
                    File file2 = TextUtils.isEmpty(apkPath) ? null : new File(apkPath);
                    if (file2 != null && file2.exists()) {
                        String absolutePath2 = file2.getAbsolutePath();
                        ModuleEntry moduleEntry2 = new ModuleEntry(resolveModule(absolutePath2), absolutePath2, ModuleUtils.getLibraryFolder(absolutePath2).getAbsolutePath(), null, z2);
                        arrayList2.add(moduleEntry2);
                    } else if (!z2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("no such module found: ");
                        sb2.append(dependency.getName());
                        throw new ModuleLoadException(sb2.toString());
                    }
                } else {
                    continue;
                }
            }
        }
        for (ModuleEntry moduleEntry3 : arrayList) {
            if (ModuleUtils.isSignatureValid(this.mApplication, moduleEntry3.apkPath)) {
                load(moduleEntry3, moduleEntry3.optFolderPath);
            } else if (!moduleEntry3.optional) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("invalid signature: ");
                sb3.append(moduleEntry3.module.getName());
                throw new ModuleLoadException(sb3.toString());
            }
        }
        for (ModuleEntry load : arrayList2) {
            load(load, null);
        }
    }
}
