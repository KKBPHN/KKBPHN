package miui.module;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.miui.internal.component.module.BuiltinRepository;
import com.miui.internal.component.module.CombinedRepository;
import com.miui.internal.component.module.ModuleFolder;
import com.miui.internal.component.module.ModuleLoadException;
import com.miui.internal.component.module.ModuleLoader;
import com.miui.internal.component.module.ModuleUtils;
import com.miui.internal.component.module.PrebuiltRepository;
import com.miui.internal.util.PackageConstants;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import miui.os.FileUtils;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager(PackageConstants.sApplication);
    private static final String MODULE_TARGET_FOLDER = "modules";
    private static final String MODULE_TEMP_FOLDER = "modules-temp";
    private static final WeakHashMap sModuleManagerMap = new WeakHashMap();
    private Application mApplication;
    private ModuleLoadListener mListener;
    private ModuleLoader mModuleLoader;
    private File mModulesFolder = getModulesFolder(this.mApplication);
    private File mTempModulesFolder;

    public interface ModuleLoadListener {
        public static final int FETCH_ERROR = 1;
        public static final int IO_ERROR = 2;
        public static final int LOAD_ERROR = 3;

        void onLoadFail(String str, int i);

        void onLoadFinish();

        void onLoadSuccess(String str, String str2);
    }

    private ModuleManager(Application application) {
        this.mApplication = application;
        this.mModulesFolder.mkdirs();
        this.mTempModulesFolder = getTempModulesFolder(this.mApplication);
        this.mTempModulesFolder.mkdirs();
        Application application2 = this.mApplication;
        File file = this.mModulesFolder;
        this.mModuleLoader = new ModuleLoader(application2, new ModuleFolder(file, file));
    }

    private boolean copyModule(File file, File file2, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(".apk");
        String sb2 = sb.toString();
        File file3 = new File(file, str);
        File file4 = new File(file2, sb2);
        if (!file3.exists()) {
            return false;
        }
        file4.delete();
        return file3.renameTo(file4);
    }

    public static ModuleManager createInstance(Application application) {
        ModuleManager moduleManager;
        if (application == null || application == PackageConstants.sApplication) {
            return INSTANCE;
        }
        synchronized (sModuleManagerMap) {
            moduleManager = (ModuleManager) sModuleManagerMap.get(application);
            if (moduleManager == null) {
                moduleManager = new ModuleManager(application);
                sModuleManagerMap.put(application, moduleManager);
            }
        }
        return moduleManager;
    }

    private boolean extractLibrary(File file, String str) {
        String str2 = "lib/";
        File moduleFile = getModuleFile(str);
        File libraryFolder = ModuleUtils.getLibraryFolder(moduleFile.getAbsolutePath());
        if (libraryFolder.exists()) {
            return true;
        }
        libraryFolder.mkdirs();
        ZipFile zipFile = new ZipFile(moduleFile);
        try {
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    if (!name.contains("..")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(Build.CPU_ABI);
                        if (!name.startsWith(sb.toString())) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(Build.CPU_ABI2);
                            if (!name.startsWith(sb2.toString())) {
                            }
                        }
                        File file2 = new File(libraryFolder, FileUtils.getFileName(name));
                        if (!file2.exists()) {
                            FileUtils.copyToFile(zipFile.getInputStream(zipEntry), file2);
                        }
                    }
                }
            }
            return true;
        } finally {
            zipFile.close();
        }
    }

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    private File getModuleFile(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".apk");
        return new File(this.mModulesFolder, sb.toString());
    }

    private File getModulesFolder(Context context) {
        return new File(PackageConstants.getSdkBaseFolder(context), MODULE_TARGET_FOLDER);
    }

    private File getTempModulesFolder(Context context) {
        return new File(PackageConstants.getSdkBaseFolder(context), MODULE_TEMP_FOLDER);
    }

    private void notifyFail(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("process ");
        sb.append(str);
        sb.append(" failed: ");
        sb.append(i);
        Log.w("miuisdk", sb.toString());
        ModuleLoadListener moduleLoadListener = this.mListener;
        if (moduleLoadListener != null) {
            moduleLoadListener.onLoadFail(str, i);
        }
    }

    private void notifyFinish() {
        Log.d("miuisdk", "process modules finished");
        ModuleLoadListener moduleLoadListener = this.mListener;
        if (moduleLoadListener != null) {
            moduleLoadListener.onLoadFinish();
        }
    }

    private void notifySuccess(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("process ");
        sb.append(str);
        sb.append(" successful");
        Log.d("miuisdk", sb.toString());
        ModuleLoadListener moduleLoadListener = this.mListener;
        if (moduleLoadListener != null) {
            moduleLoadListener.onLoadSuccess(str, str2);
        }
    }

    public void loadModules(Repository repository, String... strArr) {
        if (strArr != null && strArr.length != 0) {
            BuiltinRepository builtinRepository = new BuiltinRepository(this.mApplication);
            PrebuiltRepository prebuiltRepository = new PrebuiltRepository(this.mApplication);
            CombinedRepository combinedRepository = repository == null ? new CombinedRepository(builtinRepository, prebuiltRepository) : new CombinedRepository(builtinRepository, prebuiltRepository, repository);
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (String str : strArr) {
                if (!getModuleFile(str).exists()) {
                    linkedHashSet.add(str);
                }
            }
            Map emptyMap = linkedHashSet.isEmpty() ? Collections.emptyMap() : combinedRepository.fetch(this.mTempModulesFolder, combinedRepository.contains((Set) linkedHashSet));
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                String str2 = strArr[i];
                StringBuilder sb = new StringBuilder();
                sb.append("loading module: ");
                sb.append(str2);
                String str3 = "miuisdk";
                Log.i(str3, sb.toString());
                String str4 = " with ";
                if (!emptyMap.containsKey(str2) || copyModule(this.mTempModulesFolder, this.mModulesFolder, (String) emptyMap.get(str2), str2)) {
                    File moduleFile = getModuleFile(str2);
                    if (moduleFile.exists()) {
                        try {
                            if (!extractLibrary(this.mModulesFolder, str2)) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("fail to extract library to ");
                                sb2.append(this.mModulesFolder);
                                sb2.append(str4);
                                sb2.append(str2);
                                Log.w(str3, sb2.toString());
                                notifyFail(str2, 2);
                            }
                        } catch (IOException e) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("got IOException when extract lib: ");
                            sb3.append(e.getMessage());
                            Log.e(str3, sb3.toString());
                            notifyFail(str2, 2);
                        }
                    }
                    Dependency dependency = new Dependency();
                    dependency.setName(str2);
                    try {
                        this.mModuleLoader.loadModules(dependency);
                        notifySuccess(str2, moduleFile.getAbsolutePath());
                    } catch (ModuleLoadException e2) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("got ModuleLoadException when load modules: ");
                        sb4.append(e2.getMessage());
                        Log.e(str3, sb4.toString());
                        if (linkedHashSet.contains(str2) && !emptyMap.containsKey(str2)) {
                            notifyFail(str2, 1);
                        }
                        notifyFail(str2, 3);
                    } catch (Exception e3) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("got Exception when load modules: ");
                        sb5.append(e3.getMessage());
                        Log.e(str3, sb5.toString());
                        notifyFail(str2, 3);
                    }
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("can't copy module from ");
                    sb6.append(this.mTempModulesFolder);
                    sb6.append(" to ");
                    sb6.append(this.mModulesFolder);
                    sb6.append(str4);
                    sb6.append(str2);
                    Log.w(str3, sb6.toString());
                    notifyFail(str2, 2);
                }
            }
            notifyFinish();
        }
    }

    public void loadModules(String... strArr) {
        loadModules(null, strArr);
    }

    public void setModuleLoadListener(ModuleLoadListener moduleLoadListener) {
        this.mListener = moduleLoadListener;
    }
}
