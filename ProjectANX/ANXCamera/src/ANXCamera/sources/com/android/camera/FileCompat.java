package com.android.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FileCompat {
    static final FileCompatCommonImpl IMPL_COMMON;
    static final FileCompatOperateImpl IMPL_OPERATE;
    public static final int REQUEST_CODE_OPEN_EXTERNAL_DOCUMENT_PERMISSION = 161;
    public static final String TAG = "FileCompat";

    class BaseFileCompatImpl implements FileCompatOperateImpl {
        HashMap mDocumentFileHashMap = new HashMap(1);

        BaseFileCompatImpl() {
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r2v1, types: [java.io.OutputStream] */
        /* JADX WARNING: type inference failed for: r6v2, types: [java.io.OutputStream] */
        /* JADX WARNING: type inference failed for: r2v2, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r4v1 */
        /* JADX WARNING: type inference failed for: r2v3 */
        /* JADX WARNING: type inference failed for: r6v3 */
        /* JADX WARNING: type inference failed for: r4v2 */
        /* JADX WARNING: type inference failed for: r4v3, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r2v4 */
        /* JADX WARNING: type inference failed for: r6v4 */
        /* JADX WARNING: type inference failed for: r2v5 */
        /* JADX WARNING: type inference failed for: r2v6 */
        /* JADX WARNING: type inference failed for: r2v7, types: [java.io.OutputStream] */
        /* JADX WARNING: type inference failed for: r2v8 */
        /* JADX WARNING: type inference failed for: r4v4 */
        /* JADX WARNING: type inference failed for: r2v9 */
        /* JADX WARNING: type inference failed for: r2v10 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v5
  assigns: [?[OBJECT, ARRAY], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.io.OutputStream]
  uses: [?[OBJECT, ARRAY], ?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.FileInputStream, java.io.OutputStream]
  mth insns count: 54
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x004d A[SYNTHETIC, Splitter:B:42:0x004d] */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x0052 A[SYNTHETIC, Splitter:B:46:0x0052] */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x0060 A[SYNTHETIC, Splitter:B:54:0x0060] */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x0065 A[SYNTHETIC, Splitter:B:58:0x0065] */
        /* JADX WARNING: Unknown variable types count: 6 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean copyFile(String str, String str2) {
            ? r4;
            ? r6;
            ? r2;
            ? r22;
            String str3 = "copyFile error";
            String str4 = FileCompat.TAG;
            ? r23 = 0;
            try {
                ? fileInputStream = new FileInputStream(str);
                try {
                    r22 = r23;
                    r22 = getFileOutputStream(str2, false);
                    r22 = r22;
                    if (r22 == 0) {
                        try {
                            fileInputStream.close();
                        } catch (Exception unused) {
                        }
                        if (r22 != 0) {
                            try {
                                r22.close();
                            } catch (Exception e) {
                                Log.w(str4, str3, (Throwable) e);
                            }
                        }
                        return false;
                    }
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read != -1) {
                            r22.write(bArr, 0, read);
                        } else {
                            try {
                                break;
                            } catch (Exception unused2) {
                            }
                        }
                    }
                    fileInputStream.close();
                    if (r22 != 0) {
                        try {
                            r22.close();
                        } catch (Exception e2) {
                            Log.w(str4, str3, (Throwable) e2);
                        }
                    }
                    return true;
                } catch (Exception e3) {
                    e = e3;
                    r6 = r23;
                    r2 = fileInputStream;
                    try {
                        Log.w(str4, str3, (Throwable) e);
                        if (r2 != 0) {
                        }
                        if (r6 != 0) {
                        }
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        r4 = r2;
                        r23 = r6;
                        if (r4 != 0) {
                        }
                        if (r23 != 0) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r4 = fileInputStream;
                    r23 = r22;
                    if (r4 != 0) {
                    }
                    if (r23 != 0) {
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                r6 = 0;
                Log.w(str4, str3, (Throwable) e);
                if (r2 != 0) {
                    try {
                        r2.close();
                    } catch (Exception unused3) {
                    }
                }
                if (r6 != 0) {
                    try {
                        r6.close();
                    } catch (Exception e5) {
                        Log.w(str4, str3, (Throwable) e5);
                    }
                }
                return false;
            } catch (Throwable th3) {
                th = th3;
                r4 = 0;
                if (r4 != 0) {
                    try {
                        r4.close();
                    } catch (Exception unused4) {
                    }
                }
                if (r23 != 0) {
                    try {
                        r23.close();
                    } catch (Exception e6) {
                        Log.w(str4, str3, (Throwable) e6);
                    }
                }
                throw th;
            }
        }

        public boolean createNewFile(String str) {
            try {
                return new File(str).createNewFile();
            } catch (IOException e) {
                Log.w(FileCompat.TAG, "createNewFile error", (Throwable) e);
                return false;
            }
        }

        public String createNewFileFixPath(String str) {
            if (createNewFile(str)) {
                return str;
            }
            return null;
        }

        public FileWrapper createNewFileWrapper(String str) {
            return null;
        }

        public boolean deleteFile(String str) {
            return new File(str).delete();
        }

        public boolean exists(String str) {
            return new File(str).exists();
        }

        public OutputStream getFileOutputStream(String str, boolean z) {
            if (!z && !exists(str)) {
                return null;
            }
            try {
                return new FileOutputStream(new File(str));
            } catch (Exception unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("getFileOutputStream error, path = ");
                sb.append(str);
                Log.w(FileCompat.TAG, sb.toString());
                return null;
            }
        }

        public ParcelFileDescriptor getParcelFileDescriptor(String str, boolean z) {
            return CameraAppImpl.getAndroidContext().getContentResolver().openFileDescriptor(Uri.fromFile(new File(str)), "rw");
        }

        public boolean mkdirs(String str) {
            return new File(str).mkdirs();
        }

        public void removeDocumentFileForPath(String str) {
            this.mDocumentFileHashMap.remove(str);
        }

        public boolean renameFile(String str, String str2) {
            return new File(str).renameTo(new File(str2));
        }
    }

    interface FileCompatCommonImpl {
        String getSDPath(String str);

        boolean getStorageAccessForLOLLIPOP(Activity activity, String str);

        Uri getTreeUri(String str);

        boolean handleActivityResult(Activity activity, int i, int i2, Intent intent);

        boolean hasStoragePermission(String str);

        boolean isExternalSDFile(String str);

        void updateSDPath();
    }

    interface FileCompatOperateImpl {
        boolean copyFile(String str, String str2);

        boolean createNewFile(String str);

        String createNewFileFixPath(String str);

        FileWrapper createNewFileWrapper(String str);

        boolean deleteFile(String str);

        boolean exists(String str);

        OutputStream getFileOutputStream(String str, boolean z);

        ParcelFileDescriptor getParcelFileDescriptor(String str, boolean z);

        boolean mkdirs(String str);

        void removeDocumentFileForPath(String str);

        boolean renameFile(String str, String str2);
    }

    class FileWrapper {
        DocumentFile documentFile;
        File legacyFile;

        FileWrapper() {
        }

        public long getLength() {
            return this.legacyFile.length();
        }
    }

    class KitKatFileCompatCommonImpl implements FileCompatCommonImpl {
        protected String accessSDPath;
        protected String[] sdPaths;

        public KitKatFileCompatCommonImpl() {
            update();
        }

        /* access modifiers changed from: protected */
        @TargetApi(19)
        public String[] getExtSDCardPaths() {
            String[] strArr = new String[0];
            ArrayList arrayList = new ArrayList();
            String sdcardPath = CompatibilityUtils.getSdcardPath(CameraAppImpl.getAndroidContext());
            StringBuilder sb = new StringBuilder();
            sb.append("getExtSDCardPaths: activePath = ");
            sb.append(sdcardPath);
            Log.d(FileCompat.TAG, sb.toString());
            if (!TextUtils.isEmpty(sdcardPath)) {
                arrayList.add(sdcardPath);
            }
            return !arrayList.isEmpty() ? (String[]) arrayList.toArray(new String[arrayList.size()]) : strArr;
        }

        public String getSDPath(String str) {
            String[] strArr = this.sdPaths;
            if (strArr == null) {
                return null;
            }
            for (String str2 : strArr) {
                if (str.startsWith(str2)) {
                    return str2;
                }
            }
            return null;
        }

        public boolean getStorageAccessForLOLLIPOP(Activity activity, String str) {
            return false;
        }

        public Uri getTreeUri(String str) {
            return null;
        }

        public boolean handleActivityResult(Activity activity, int i, int i2, Intent intent) {
            return false;
        }

        public boolean hasStoragePermission(String str) {
            return true;
        }

        public boolean isExternalSDFile(String str) {
            String[] strArr = this.sdPaths;
            if (strArr == null) {
                return false;
            }
            for (String startsWith : strArr) {
                if (str.startsWith(startsWith)) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void update() {
            this.sdPaths = getExtSDCardPaths();
        }

        public void updateSDPath() {
            update();
        }
    }

    class LollipopFileCompatCommonImpl extends KitKatFileCompatCommonImpl {
        private static final String SD_PATH_TREE_URI = "sd_path_tree_uri";

        public LollipopFileCompatCommonImpl() {
            update();
        }

        @TargetApi(21)
        public boolean getStorageAccessForLOLLIPOP(Activity activity, String str) {
            String[] extSDCardPaths = getExtSDCardPaths();
            if (extSDCardPaths == null || extSDCardPaths.length == 0) {
                return false;
            }
            int length = extSDCardPaths.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str2 = extSDCardPaths[i];
                if (str.startsWith(str2)) {
                    this.accessSDPath = str2;
                    break;
                }
                i++;
            }
            List storageVolumes = ((StorageManager) activity.getSystemService("storage")).getStorageVolumes();
            if (storageVolumes.size() > 1) {
                Intent createAccessIntent = ((StorageVolume) storageVolumes.get(1)).createAccessIntent(Environment.DIRECTORY_DCIM);
                String str3 = FileCompat.TAG;
                if (createAccessIntent == null) {
                    Log.w(str3, "getStorageAccessForLOLLIPOP error, intent is null");
                    return false;
                }
                try {
                    activity.startActivityForResult(createAccessIntent, 161);
                } catch (Exception e) {
                    Log.w(str3, "getStorageAccessForLOLLIPOP error", (Throwable) e);
                }
            }
            return true;
        }

        public Uri getTreeUri(String str) {
            String string = CameraAppImpl.getAndroidContext().getSharedPreferences(SD_PATH_TREE_URI, 0).getString(str, null);
            if (string == null) {
                return null;
            }
            return Uri.parse(string);
        }

        @TargetApi(19)
        public boolean handleActivityResult(Activity activity, int i, int i2, Intent intent) {
            String str;
            if (i == 161 && i2 == -1) {
                Uri data = intent.getData();
                String str2 = FileCompat.TAG;
                if (data == null) {
                    str = "handleActivityResult: uri is null, documents permission is Failed!";
                } else if (!DocumentFile.fromTreeUri(activity, data).exists()) {
                    str = "handleActivityResult: documentFile is not exist, documents permission is Failed!";
                } else {
                    try {
                        activity.getContentResolver().takePersistableUriPermission(data, intent.getFlags() & 3);
                        activity.getSharedPreferences(SD_PATH_TREE_URI, 0).edit().putString(this.accessSDPath, data.toString()).apply();
                        return true;
                    } catch (Exception e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("cacheUri failed, uri = ");
                        sb.append(data);
                        Log.w(str2, sb.toString(), (Throwable) e);
                        update();
                    }
                }
                Log.d(str2, str);
                return false;
            }
            return false;
        }

        public boolean hasStoragePermission(String str) {
            if (!isExternalSDFile(str)) {
                return true;
            }
            String sDPath = super.getSDPath(str);
            boolean z = false;
            if (sDPath == null || getTreeUri(sDPath) == null) {
                return false;
            }
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(CameraAppImpl.getAndroidContext(), getTreeUri(sDPath));
            StringBuilder sb = new StringBuilder();
            String str2 = "hasStoragePermission document = ";
            sb.append(str2);
            sb.append(fromTreeUri);
            String sb2 = sb.toString();
            String str3 = FileCompat.TAG;
            Log.d(str3, sb2);
            if (fromTreeUri == null) {
                return false;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(fromTreeUri.exists());
            String str4 = ", ";
            sb3.append(str4);
            sb3.append(fromTreeUri.canRead());
            sb3.append(str4);
            sb3.append(fromTreeUri.canWrite());
            Log.d(str3, sb3.toString());
            if (fromTreeUri.exists() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                z = true;
            }
            return z;
        }
    }

    class LollipopFileCompatImpl extends BaseFileCompatImpl {
        LollipopFileCompatImpl() {
        }

        public boolean copyFile(String str, String str2) {
            return super.copyFile(str, str2);
        }

        public boolean createNewFile(String str) {
            boolean z = true;
            if (super.createNewFile(str)) {
                return true;
            }
            if (!FileCompat.isSDFile(str)) {
                return false;
            }
            if (getDocumentFileByPath(str, true, null, false) == null) {
                z = false;
            }
            return z;
        }

        public String createNewFileFixPath(String str) {
            if (super.createNewFile(str)) {
                return str;
            }
            if (!FileCompat.isSDFile(str)) {
                return null;
            }
            DocumentFile documentFileByPath = getDocumentFileByPath(str, true, null, false);
            if (documentFileByPath == null) {
                return null;
            }
            String substring = str.substring(0, str.lastIndexOf(File.separator));
            StringBuilder sb = new StringBuilder();
            sb.append(substring);
            sb.append(File.separator);
            sb.append(documentFileByPath.getName());
            return sb.toString();
        }

        public boolean deleteFile(String str) {
            boolean z = true;
            if (super.deleteFile(str)) {
                return true;
            }
            if (!FileCompat.isSDFile(str)) {
                return false;
            }
            DocumentFile documentFileByPath = getDocumentFileByPath(str, false, null, false);
            if (documentFileByPath != null) {
                z = documentFileByPath.delete();
            }
            removeDocumentFileForPath(str);
            return z;
        }

        public boolean exists(String str) {
            boolean z = true;
            if (super.exists(str)) {
                return true;
            }
            if (!FileCompat.isSDFile(str)) {
                return false;
            }
            if (getDocumentFileByPath(str, false, null, false) == null) {
                z = false;
            }
            return z;
        }

        public DocumentFile getDocumentFileByPath(String str, boolean z, String str2, boolean z2) {
            String str3;
            String str4;
            DocumentFile createDirectory;
            String str5 = str;
            boolean z3 = z;
            String str6 = FileCompat.TAG;
            Log.d(str6, "getDocumentFileByPath start>>");
            String access$000 = FileCompat.getSDPath(str);
            String str7 = null;
            if (access$000 == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("getDocumentFileByPath: no sd path for ");
                sb.append(str5);
                Log.w(str6, sb.toString());
                return null;
            }
            DocumentFile documentFile = (DocumentFile) this.mDocumentFileHashMap.get(str5);
            if (documentFile != null && documentFile.exists()) {
                return documentFile;
            }
            Uri access$100 = FileCompat.getTreeUri(access$000);
            if (access$100 == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getDocumentFileByPath: no tree uri for ");
                sb2.append(access$000);
                Log.w(str6, sb2.toString());
                return null;
            }
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(CameraAppImpl.getAndroidContext(), access$100);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(access$000);
            sb3.append(File.separator);
            sb3.append(Environment.DIRECTORY_DCIM);
            String sb4 = sb3.toString();
            if (str5.equals(sb4)) {
                return fromTreeUri;
            }
            String substring = str5.substring(sb4.length() + 1);
            if (TextUtils.isEmpty(substring)) {
                Log.w(str6, "getDocumentFileByPath: empty relative path");
                return null;
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("\\");
            sb5.append(File.separator);
            String[] split = substring.split(sb5.toString());
            String[] strArr = new String[(split.length - 1)];
            System.arraycopy(split, 0, strArr, 0, split.length - 1);
            int length = strArr.length;
            DocumentFile documentFile2 = fromTreeUri;
            int i = 0;
            boolean z4 = false;
            while (true) {
                str3 = "getDocumentFileByPath: no document for ";
                if (i >= length) {
                    break;
                }
                String str8 = strArr[i];
                if (documentFile2 == null) {
                    break;
                }
                if (z4) {
                    createDirectory = documentFile2.createDirectory(str8);
                } else {
                    DocumentFile findFile = documentFile2.findFile(str8);
                    if (findFile != null) {
                        documentFile2 = findFile;
                        i++;
                    } else if (z3) {
                        createDirectory = documentFile2.createDirectory(str8);
                        z4 = true;
                    } else {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str3);
                        sb6.append(str8);
                        Log.d(str6, sb6.toString());
                        return null;
                    }
                }
                documentFile2 = createDirectory;
                i++;
            }
            if (documentFile2 == null) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str3);
                sb7.append(substring);
                Log.d(str6, sb7.toString());
                return null;
            }
            String str9 = split[split.length - 1];
            StringBuilder sb8 = new StringBuilder();
            sb8.append("getDocumentFileByPath: createIfNotFound = ");
            sb8.append(z3);
            Log.d(str6, sb8.toString());
            long currentTimeMillis = System.currentTimeMillis();
            if (z3) {
                String str10 = "createFile error";
                if (z2) {
                    try {
                        documentFile = documentFile2.findFile(str9);
                        if (documentFile == null) {
                            documentFile = documentFile2.createDirectory(str9);
                        }
                    } catch (Exception e) {
                        Log.w(str6, str10, (Throwable) e);
                    }
                } else {
                    int indexOf = str9.indexOf(".");
                    if (!TextUtils.isEmpty(str2) || indexOf <= 0) {
                        str4 = str2;
                    } else {
                        str4 = FileCompat.getMimeTypeFromPath(str9);
                        if (!TextUtils.isEmpty(str4)) {
                            str7 = str9.substring(0, indexOf);
                        }
                    }
                    if (str7 != null) {
                        str9 = str7;
                    }
                    try {
                        documentFile = documentFile2.createFile(str4, str9);
                    } catch (Exception e2) {
                        Log.w(str6, str10, (Throwable) e2);
                    }
                }
            } else {
                documentFile = documentFile2.findFile(str9);
            }
            if (documentFile != null) {
                this.mDocumentFileHashMap.put(str5, documentFile);
            }
            StringBuilder sb9 = new StringBuilder();
            sb9.append("getDocumentFileByPath end<< cost time = ");
            sb9.append(System.currentTimeMillis() - currentTimeMillis);
            sb9.append(" ms");
            Log.d(str6, sb9.toString());
            return documentFile;
        }

        public OutputStream getFileOutputStream(String str, boolean z) {
            if (VERSION.SDK_INT < 28 || !FileCompat.isSDFile(str)) {
                OutputStream fileOutputStream = super.getFileOutputStream(str, z);
                if (fileOutputStream != null) {
                    return fileOutputStream;
                }
            }
            OutputStream outputStream = null;
            if (!FileCompat.isSDFile(str)) {
                return null;
            }
            DocumentFile documentFileByPath = getDocumentFileByPath(str, z, null, false);
            if (documentFileByPath != null) {
                try {
                    outputStream = CameraAppImpl.getAndroidContext().getContentResolver().openOutputStream(documentFileByPath.getUri());
                } catch (FileNotFoundException e) {
                    Log.w(FileCompat.TAG, "getFileOutputStream error", (Throwable) e);
                }
            }
            return outputStream;
        }

        public ParcelFileDescriptor getParcelFileDescriptor(String str, boolean z) {
            if (!FileCompat.isSDFile(str)) {
                return super.getParcelFileDescriptor(str, z);
            }
            return CameraAppImpl.getAndroidContext().getContentResolver().openFileDescriptor(getDocumentFileByPath(str, z, FileCompat.getMimeTypeFromPath(str), false).getUri(), "rw");
        }

        public boolean mkdirs(String str) {
            boolean z = true;
            if (super.mkdirs(str)) {
                return true;
            }
            if (!FileCompat.isSDFile(str)) {
                return false;
            }
            if (getDocumentFileByPath(str, true, null, true) == null) {
                z = false;
            }
            return z;
        }

        public boolean renameFile(String str, String str2) {
            String str3;
            if (super.renameFile(str, str2)) {
                return true;
            }
            boolean equalsIgnoreCase = new File(str).getParent().equalsIgnoreCase(new File(str2).getParent());
            String str4 = FileCompat.TAG;
            if (!equalsIgnoreCase) {
                str3 = "only support rename to the same folder";
            } else {
                String name = new File(str2).getName();
                DocumentFile documentFileByPath = getDocumentFileByPath(str, false, null, false);
                if (documentFileByPath == null) {
                    str3 = "renameFile: null document";
                } else {
                    try {
                        return documentFileByPath.renameTo(name);
                    } catch (Exception e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("renameFile error, path = ");
                        sb.append(str);
                        throw new IOException(sb.toString(), e);
                    }
                }
            }
            Log.w(str4, str3);
            return false;
        }
    }

    class MarshmallowFileCompatImpl extends LollipopFileCompatImpl {
        MarshmallowFileCompatImpl() {
        }
    }

    static {
        FileCompatOperateImpl fileCompatOperateImpl;
        if (VERSION.SDK_INT >= 28) {
            IMPL_COMMON = new LollipopFileCompatCommonImpl();
            fileCompatOperateImpl = new MarshmallowFileCompatImpl();
        } else {
            IMPL_COMMON = new KitKatFileCompatCommonImpl();
            fileCompatOperateImpl = new BaseFileCompatImpl();
        }
        IMPL_OPERATE = fileCompatOperateImpl;
    }

    public static boolean copyFile(String str, String str2) {
        return IMPL_OPERATE.copyFile(str, str2);
    }

    public static boolean createNewFile(String str) {
        return IMPL_OPERATE.createNewFile(str);
    }

    public static String createNewFileFixPath(String str) {
        return IMPL_OPERATE.createNewFileFixPath(str);
    }

    public static boolean deleteFile(String str) {
        return IMPL_OPERATE.deleteFile(str);
    }

    public static boolean exists(String str) {
        return IMPL_OPERATE.exists(str);
    }

    public static OutputStream getFileOutputStream(String str, boolean z) {
        return IMPL_OPERATE.getFileOutputStream(str, z);
    }

    /* access modifiers changed from: private */
    public static String getMimeTypeFromPath(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf < 0) {
            return null;
        }
        String lowerCase = str.substring(lastIndexOf + 1).toLowerCase(Locale.ENGLISH);
        if ("jpg".equals(lowerCase) || "jpeg".equals(lowerCase)) {
            return Storage.MIME_JPEG;
        }
        if ("png".equals(lowerCase)) {
            return "image/png";
        }
        if ("mp4".equals(lowerCase)) {
            return "video/mp4";
        }
        return null;
    }

    public static ParcelFileDescriptor getParcelFileDescriptor(String str, boolean z) {
        return IMPL_OPERATE.getParcelFileDescriptor(str, z);
    }

    /* access modifiers changed from: private */
    public static String getSDPath(String str) {
        return IMPL_COMMON.getSDPath(str);
    }

    @TargetApi(21)
    public static boolean getStorageAccessForLOLLIPOP(Activity activity, String str) {
        return IMPL_COMMON.getStorageAccessForLOLLIPOP(activity, str);
    }

    /* access modifiers changed from: private */
    public static Uri getTreeUri(String str) {
        return IMPL_COMMON.getTreeUri(str);
    }

    @TargetApi(21)
    public static boolean handleActivityResult(Activity activity, int i, int i2, Intent intent) {
        return IMPL_COMMON.handleActivityResult(activity, i, i2, intent);
    }

    @TargetApi(21)
    public static boolean hasStoragePermission(String str) {
        return IMPL_COMMON.hasStoragePermission(str);
    }

    @TargetApi(19)
    public static boolean isSDFile(String str) {
        return IMPL_COMMON.isExternalSDFile(str);
    }

    public static boolean mkdirs(String str) {
        return IMPL_OPERATE.mkdirs(str);
    }

    public static void removeDocumentFileForPath(String str) {
        IMPL_OPERATE.removeDocumentFileForPath(str);
    }

    public static boolean renameFile(String str, String str2) {
        return IMPL_OPERATE.renameFile(str, str2);
    }

    @TargetApi(21)
    public static void updateSDPath() {
        IMPL_COMMON.updateSDPath();
    }
}
