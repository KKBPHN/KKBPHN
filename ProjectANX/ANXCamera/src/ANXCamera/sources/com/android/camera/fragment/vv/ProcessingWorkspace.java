package com.android.camera.fragment.vv;

import android.content.Context;
import com.android.camera.module.impl.component.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ProcessingWorkspace {
    protected List mItemList = new ArrayList();

    public ProcessingWorkspace() {
        validWorkspaceDir();
    }

    public abstract void add(Object obj);

    public final List getList() {
        return this.mItemList;
    }

    public abstract String getWorkspaceDir();

    public boolean isEmpty() {
        return this.mItemList.isEmpty();
    }

    public abstract void remove(Object obj);

    public abstract boolean restoreWorkspace();

    public abstract boolean saveWorkspace();

    /* access modifiers changed from: protected */
    public final void validWorkspaceDir() {
        File file = new File(getWorkspaceDir());
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        if (!file.exists()) {
            FileUtils.makeNoMediaDir(getWorkspaceDir());
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0034 A[SYNTHETIC, Splitter:B:21:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x003e A[SYNTHETIC, Splitter:B:27:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0049 A[SYNTHETIC, Splitter:B:32:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:24:0x0039=Splitter:B:24:0x0039, B:18:0x002f=Splitter:B:18:0x002f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void writeToCache(String str, Context context, String str2) {
        File file = new File(context.getCacheDir(), str);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            try {
                fileOutputStream2.write(str2.getBytes());
                try {
                    fileOutputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                e.printStackTrace();
                if (fileOutputStream == null) {
                    fileOutputStream.close();
                }
            } catch (IOException e3) {
                e = e3;
                fileOutputStream = fileOutputStream2;
                try {
                    e.printStackTrace();
                    if (fileOutputStream == null) {
                        fileOutputStream.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (fileOutputStream == null) {
            }
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (fileOutputStream == null) {
            }
        }
    }
}
