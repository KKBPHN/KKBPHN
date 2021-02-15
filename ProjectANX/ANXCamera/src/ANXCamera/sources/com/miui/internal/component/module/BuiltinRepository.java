package com.miui.internal.component.module;

import android.content.Context;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import java.io.File;

public class BuiltinRepository extends LocalRepository {
    public BuiltinRepository(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public File getModuleFile(File file, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("libmodule_");
        sb.append(str);
        sb.append(SplitConstants.DOT_SO);
        return new File(file, sb.toString());
    }

    /* access modifiers changed from: protected */
    public File getRootFolder(Context context) {
        return new File(context.getApplicationInfo().nativeLibraryDir);
    }
}
