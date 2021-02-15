package com.android.camera.backup;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.android.camera.CameraAppImpl;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import miui.app.backup.BackupMeta;
import miui.app.backup.FullBackupAgent;
import miui.cloud.backup.SettingsBackupHelper;

public class CameraBackupAgent extends FullBackupAgent {
    private static final long RESOTRE_LOW_STORAGE_THRESHOLD = 10485760;
    private static final String TAG = "CameraBackupAgent";

    private void backupFile(File file) {
        if (file.exists()) {
            if (!file.isFile() || !file.canRead()) {
                for (File backupFile : file.listFiles()) {
                    backupFile(backupFile);
                }
            } else {
                addAttachedFile(file.getPath());
            }
        }
    }

    private void backupFile(String str) {
        if (str != null && str.length() != 0) {
            File file = new File(str);
            if (file.exists()) {
                backupFile(file);
            }
        }
    }

    private String correctFileName(String str) {
        int lastIndexOf = str.lastIndexOf(File.separator);
        Storage.switchStoragePathIfNeeded();
        if (lastIndexOf >= 0) {
            String substring = str.substring(0, lastIndexOf);
            if (!File.separator.equals(Character.valueOf(substring.charAt(0)))) {
                StringBuilder sb = new StringBuilder();
                sb.append(File.separator);
                sb.append(substring);
                substring = sb.toString();
            }
            String primaryStoragePath = Storage.getPrimaryStoragePath();
            String secondaryStoragePath = Storage.getSecondaryStoragePath();
            if (substring.startsWith(Storage.DIRECTORY) || (((primaryStoragePath != null && substring.startsWith(primaryStoragePath)) || (secondaryStoragePath != null && substring.startsWith(secondaryStoragePath))) && Storage.getAvailableSpace(substring) > RESOTRE_LOW_STORAGE_THRESHOLD)) {
                return str;
            }
        }
        return Storage.generateFilepath(str, "");
    }

    /* access modifiers changed from: protected */
    public int getVersion(int i) {
        if (!C0124O00000oO.O0o) {
            return CameraBackupAgent.super.getVersion(i);
        }
        return 1;
    }

    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r0v4, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r0v6, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r2v8, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r5v7, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0057, code lost:
        if (r5 == 0) goto L_0x0073;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x003a */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v4
  assigns: []
  uses: []
  mth insns count: 56
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0054 A[SYNTHETIC, Splitter:B:35:0x0054] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x005f A[SYNTHETIC, Splitter:B:43:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0064 A[SYNTHETIC, Splitter:B:47:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x006b A[SYNTHETIC, Splitter:B:55:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0070 A[SYNTHETIC, Splitter:B:59:0x0070] */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onAttachRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str) {
        ? r2;
        ? r0;
        ? r22;
        ? r5;
        ? r23;
        ? r02;
        ? r52;
        Log.d(TAG, "onAttachRestore");
        if (!C0124O00000oO.O0o) {
            return CameraBackupAgent.super.onAttachRestore(backupMeta, parcelFileDescriptor, str);
        }
        ? r03 = 0;
        try {
            r22 = new FileOutputStream(new File(correctFileName(str)));
            try {
                ? r53 = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = r53.read(bArr);
                        if (read > 0) {
                            r22.write(bArr, 0, read);
                        } else {
                            r53 = r53;
                            r22.close();
                            try {
                                r53.close();
                            } catch (IOException unused) {
                            }
                            return 0;
                        }
                    }
                } catch (IllegalArgumentException unused2) {
                    r0 = r53;
                    if (r2 != 0) {
                    }
                    if (r0 != 0) {
                    }
                    return 0;
                } catch (IOException e) {
                    e = e;
                    r02 = r22;
                    r52 = r5;
                    try {
                        e.printStackTrace();
                        if (r02 != 0) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        r23 = r02;
                        r03 = r5;
                        r22 = r23;
                        if (r22 != 0) {
                        }
                        if (r03 != 0) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r23 = r22;
                    r03 = r5;
                    r22 = r23;
                    if (r22 != 0) {
                    }
                    if (r03 != 0) {
                    }
                    throw th;
                }
            } catch (IllegalArgumentException unused3) {
                if (r2 != 0) {
                    try {
                        r2.close();
                    } catch (IOException unused4) {
                    }
                }
                if (r0 != 0) {
                    r0.close();
                }
                return 0;
            } catch (IOException e2) {
                e = e2;
                ? r54 = 0;
                r02 = r22;
                r52 = r54;
                e.printStackTrace();
                if (r02 != 0) {
                    try {
                        r02.close();
                    } catch (IOException unused5) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (r22 != 0) {
                    try {
                        r22.close();
                    } catch (IOException unused6) {
                    }
                }
                if (r03 != 0) {
                    try {
                        r03.close();
                    } catch (IOException unused7) {
                    }
                }
                throw th;
            }
        } catch (IllegalArgumentException unused8) {
            r2 = 0;
            if (r2 != 0) {
            }
            if (r0 != 0) {
            }
            return 0;
        } catch (IOException e3) {
            e = e3;
            r52 = 0;
            e.printStackTrace();
            if (r02 != 0) {
            }
        } catch (Throwable th4) {
            th = th4;
            r22 = 0;
            r03 = r03;
            if (r22 != 0) {
            }
            if (r03 != 0) {
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public int onDataRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor) {
        Log.d(TAG, "onDataRestore");
        if (!C0124O00000oO.O0o) {
            return CameraBackupAgent.super.onDataRestore(backupMeta, parcelFileDescriptor);
        }
        SettingsBackupHelper.restoreSettings(getApplicationContext(), parcelFileDescriptor, new CameraSettingsBackupImpl());
        return 0;
    }

    /* access modifiers changed from: protected */
    public int onFullBackup(ParcelFileDescriptor parcelFileDescriptor, int i) {
        Log.d(TAG, "onFullBackup");
        if (!C0124O00000oO.O0o) {
            return CameraBackupAgent.super.onFullBackup(parcelFileDescriptor, i);
        }
        SettingsBackupHelper.backupSettings(getApplicationContext(), parcelFileDescriptor, new CameraSettingsBackupImpl());
        backupFile(Storage.getPrimaryStoragePath());
        backupFile(Storage.getSecondaryStoragePath());
        return 0;
    }

    /* access modifiers changed from: protected */
    public int onRestoreEnd(BackupMeta backupMeta) {
        Log.d(TAG, "onRestoreEnd: update watermark for vendor");
        Util.generateMainWatermark2File();
        if (!C0124O00000oO.O0o) {
            return CameraBackupAgent.super.onRestoreEnd(backupMeta);
        }
        String primaryStoragePath = Storage.getPrimaryStoragePath();
        String secondaryStoragePath = Storage.getSecondaryStoragePath();
        Intent intent = new Intent("miui.intent.action.MEDIA_SCANNER_SCAN_FOLDER");
        intent.addFlags(16777216);
        if (primaryStoragePath != null) {
            intent.setData(Uri.fromFile(new File(primaryStoragePath)));
            CameraAppImpl.getAndroidContext().sendBroadcast(intent);
        }
        if (secondaryStoragePath != null) {
            intent.setData(Uri.fromFile(new File(secondaryStoragePath)));
            CameraAppImpl.getAndroidContext().sendBroadcast(intent);
        }
        return 0;
    }
}
