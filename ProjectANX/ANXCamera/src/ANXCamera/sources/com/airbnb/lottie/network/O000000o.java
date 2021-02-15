package com.airbnb.lottie.network;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.util.Pair;
import com.airbnb.lottie.O00000o.O00000o;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

class O000000o {
    private final Context appContext;
    private final String url;

    O000000o(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
    }

    private static String O000000o(String str, FileExtension fileExtension, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("lottie_cache_");
        sb.append(str.replaceAll("\\W+", ""));
        sb.append(z ? fileExtension.Oo0o() : fileExtension.extension);
        return sb.toString();
    }

    @Nullable
    private File O000OoO(String str) {
        File file = new File(this.appContext.getCacheDir(), O000000o(str, FileExtension.JSON, false));
        if (file.exists()) {
            return file;
        }
        File file2 = new File(this.appContext.getCacheDir(), O000000o(str, FileExtension.ZIP, false));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public File O000000o(InputStream inputStream, FileExtension fileExtension) {
        FileOutputStream fileOutputStream;
        File file = new File(this.appContext.getCacheDir(), O000000o(this.url, fileExtension, true));
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    return file;
                }
            }
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    public void O000000o(FileExtension fileExtension) {
        File file = new File(this.appContext.getCacheDir(), O000000o(this.url, fileExtension, true));
        File file2 = new File(file.getAbsolutePath().replace(".temp", ""));
        boolean renameTo = file.renameTo(file2);
        StringBuilder sb = new StringBuilder();
        sb.append("Copying temp file to real file (");
        sb.append(file2);
        sb.append(")");
        O00000o.debug(sb.toString());
        if (!renameTo) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to rename cache file ");
            sb2.append(file.getAbsolutePath());
            sb2.append(" to ");
            sb2.append(file2.getAbsolutePath());
            sb2.append(".");
            O00000o.warning(sb2.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @Nullable
    public Pair fetch() {
        try {
            File O000OoO = O000OoO(this.url);
            if (O000OoO == null) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(O000OoO);
            FileExtension fileExtension = O000OoO.getAbsolutePath().endsWith(".zip") ? FileExtension.ZIP : FileExtension.JSON;
            StringBuilder sb = new StringBuilder();
            sb.append("Cache hit for ");
            sb.append(this.url);
            sb.append(" at ");
            sb.append(O000OoO.getAbsolutePath());
            O00000o.debug(sb.toString());
            return new Pair(fileExtension, fileInputStream);
        } catch (FileNotFoundException unused) {
            return null;
        }
    }
}
