package com.miui.internal.component.module;

import android.content.Context;
import com.xiaomi.stat.d;
import java.io.File;
import java.lang.reflect.Field;
import miui.module.Repository;

public abstract class LocalRepository extends Repository {
    private Context mContext;

    public LocalRepository(Context context) {
        this.mContext = context;
    }

    private boolean createSymlink(File file, File file2) {
        try {
            Field declaredField = Class.forName("libcore.io.Libcore").getDeclaredField(d.l);
            declaredField.setAccessible(true);
            Object obj = declaredField.get(null);
            obj.getClass().getMethod("symlink", new Class[]{String.class, String.class}).invoke(obj, new Object[]{file.getAbsolutePath(), file2.getAbsolutePath()});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(String str) {
        return getModuleFile(getRootFolder(this.mContext), str).exists();
    }

    public String fetch(File file, String str) {
        File moduleFile = getModuleFile(getRootFolder(this.mContext), str);
        if (moduleFile.exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(".apk");
            File file2 = new File(file, sb.toString());
            file2.delete();
            if (createSymlink(moduleFile, file2)) {
                return file2.getName();
            }
        }
        return null;
    }

    public abstract File getModuleFile(File file, String str);

    public abstract File getRootFolder(Context context);
}
