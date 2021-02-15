package com.iqiyi.android.qigsaw.core.common;

import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.io.File;
import java.io.IOException;

@RestrictTo({Scope.LIBRARY_GROUP})
public class OEMCompat {
    private static final String TAG = "Split.OEMCompat";

    public static boolean checkOatFile(@NonNull File file) {
        try {
            if (SplitElfFile.getFileTypeByMagic(file) == 1) {
                SplitElfFile splitElfFile = null;
                try {
                    splitElfFile = new SplitElfFile(file);
                    return true;
                } catch (Throwable unused) {
                    SplitLog.e(TAG, "final parallel dex optimizer file %s is not elf format, return false", file.getName());
                } finally {
                    FileUtil.closeQuietly(splitElfFile);
                }
            }
            return false;
        } catch (IOException unused2) {
            return true;
        }
    }

    public static File getOatFilePath(@NonNull File file, @NonNull File file2) {
        String name = file.getName();
        String str = SplitConstants.DOT_DEX;
        if (!name.endsWith(str)) {
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(name);
                sb.append(str);
                name = sb.toString();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(name.substring(0, lastIndexOf));
                sb2.append(str);
                name = sb2.toString();
            }
        }
        return new File(file2, name);
    }

    public static boolean isSpecialManufacturer() {
        return false;
    }

    public static boolean shouldCheckOatFileInCurrentSys() {
        int i = VERSION.SDK_INT;
        return i > 20 && i < 26;
    }
}
