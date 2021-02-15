package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.OEMCompat;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class SplitInstallerImpl extends SplitInstaller {
    private static final boolean IS_VM_MULTIDEX_CAPABLE = isVMMultiDexCapable(System.getProperty("java.vm.version"));
    private static final String TAG = "SplitInstallerImpl";
    private final Context appContext;
    private final boolean verifySignature;

    SplitInstallerImpl(Context context, boolean z) {
        this.appContext = context;
        this.verifySignature = z;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.io.File>, for r1v0, types: [java.util.List, java.util.List<java.io.File>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void deleteCorruptedFiles(List<File> list) {
        for (File deleteFileSafely : list) {
            FileUtil.deleteFileSafely(deleteFileSafely);
        }
    }

    private boolean isLibExtractNeeded(SplitInfo splitInfo) {
        return splitInfo.hasLibs();
    }

    private boolean isMultiDexExtractNeeded(SplitInfo splitInfo) {
        return splitInfo.isMultiDex();
    }

    private boolean isVMMultiDexCapable() {
        return IS_VM_MULTIDEX_CAPABLE;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0029, code lost:
        if (r1 < 1) goto L_0x002c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isVMMultiDexCapable(String str) {
        boolean z;
        if (str != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(str);
            if (matcher.matches()) {
                z = true;
                try {
                    int parseInt = Integer.parseInt(matcher.group(1));
                    int parseInt2 = Integer.parseInt(matcher.group(2));
                    if (parseInt <= 2) {
                        if (parseInt == 2) {
                        }
                    }
                } catch (NumberFormatException unused) {
                }
                StringBuilder sb = new StringBuilder();
                sb.append("VM with version ");
                sb.append(str);
                sb.append(!z ? " has multidex support" : " does not have multidex support");
                SplitLog.i("Split:MultiDex", sb.toString(), new Object[0]);
                return z;
            }
        }
        z = false;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("VM with version ");
        sb2.append(str);
        sb2.append(!z ? " has multidex support" : " does not have multidex support");
        SplitLog.i("Split:MultiDex", sb2.toString(), new Object[0]);
        return z;
    }

    /* access modifiers changed from: protected */
    public void checkSplitMD5(File file, String str) {
        String md5 = FileUtil.getMD5(file);
        if (!str.equals(md5)) {
            deleteCorruptedFiles(Collections.singletonList(file));
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check split apk md5, expect ");
            sb.append(str);
            sb.append(" but ");
            sb.append(md5);
            throw new InstallException(-13, new IOException(sb.toString()));
        }
    }

    /* access modifiers changed from: protected */
    public boolean createInstalledMark(File file) {
        if (file.exists()) {
            return false;
        }
        try {
            FileUtil.createFileSafely(file);
            return true;
        } catch (IOException e) {
            throw new InstallException(-16, e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean createInstalledMarkLock(File file, File file2) {
        if (file.exists()) {
            return false;
        }
        try {
            FileUtil.createFileSafelyLock(file, file2);
            return true;
        } catch (IOException e) {
            throw new InstallException(-16, e);
        }
    }

    /* access modifiers changed from: protected */
    public void extractLib(SplitInfo splitInfo, File file) {
        SplitLibExtractor splitLibExtractor;
        String str = TAG;
        try {
            splitLibExtractor = new SplitLibExtractor(file, SplitPathManager.require().getSplitLibDir(splitInfo));
            SplitLog.i(str, "Succeed to extract libs:  %s", splitLibExtractor.load(splitInfo, false).toString());
            FileUtil.closeQuietly(splitLibExtractor);
        } catch (IOException e) {
            SplitLog.w(str, "Failed to load or extract lib files", (Throwable) e);
            throw new InstallException(-15, e);
        } catch (IOException e2) {
            throw new InstallException(-15, e2);
        } catch (Throwable th) {
            FileUtil.closeQuietly(splitLibExtractor);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public List extractMultiDex(SplitInfo splitInfo, File file) {
        SplitMultiDexExtractor splitMultiDexExtractor;
        Object[] objArr = {file.getName()};
        String str = TAG;
        SplitLog.w(str, "VM do not support multi-dex, but split %s has multi dex files, so we need install other dex files manually", objArr);
        File splitCodeCacheDir = SplitPathManager.require().getSplitCodeCacheDir(splitInfo);
        StringBuilder sb = new StringBuilder();
        sb.append(splitInfo.getSplitName());
        String str2 = "@";
        sb.append(str2);
        sb.append(SplitBaseInfoProvider.getVersionName());
        sb.append(str2);
        sb.append(splitInfo.getSplitVersion());
        String sb2 = sb.toString();
        try {
            splitMultiDexExtractor = new SplitMultiDexExtractor(file, splitCodeCacheDir);
            List<File> load = splitMultiDexExtractor.load(this.appContext, sb2, false);
            ArrayList arrayList = new ArrayList(load.size());
            for (File absolutePath : load) {
                arrayList.add(absolutePath.getAbsolutePath());
            }
            SplitLog.w(str, "Succeed to load or extract dex files", load.toString());
            FileUtil.closeQuietly(splitMultiDexExtractor);
            return arrayList;
        } catch (IOException e) {
            SplitLog.w(str, "Failed to load or extract dex files", (Throwable) e);
            throw new InstallException(-14, e);
        } catch (IOException e2) {
            throw new InstallException(-14, e2);
        } catch (Throwable th) {
            FileUtil.closeQuietly(splitMultiDexExtractor);
            throw th;
        }
    }

    public InstallResult install(boolean z, SplitInfo splitInfo) {
        File file;
        File file2;
        List list;
        File splitDir = SplitPathManager.require().getSplitDir(splitInfo);
        if (!splitInfo.isBuiltIn() || !splitInfo.getUrl().startsWith(SplitConstants.URL_NATIVE)) {
            StringBuilder sb = new StringBuilder();
            sb.append(splitInfo.getSplitName());
            sb.append(".apk");
            file = new File(splitDir, sb.toString());
        } else {
            String str = this.appContext.getApplicationInfo().nativeLibraryDir;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(SplitConstants.SPLIT_PREFIX);
            sb2.append(splitInfo.getSplitName());
            file = new File(str, System.mapLibraryName(sb2.toString()));
        }
        if (FileUtil.isLegalFile(file)) {
            boolean z2 = this.verifySignature;
            String str2 = TAG;
            if (z2) {
                SplitLog.d(str2, "Need to verify split %s signature!", file.getAbsolutePath());
                verifySignature(file);
            }
            checkSplitMD5(file, splitInfo.getMd5());
            String str3 = null;
            if (isLibExtractNeeded(splitInfo)) {
                extractLib(splitInfo, file);
                file2 = SplitPathManager.require().getSplitLibDir(splitInfo);
            } else {
                file2 = null;
            }
            if (splitInfo.hasDex()) {
                list = new ArrayList();
                list.add(file.getAbsolutePath());
                if (!isVMMultiDexCapable() && isMultiDexExtractNeeded(splitInfo)) {
                    list.addAll(extractMultiDex(splitInfo, file));
                }
            } else {
                list = null;
            }
            File splitMarkFile = SplitPathManager.require().getSplitMarkFile(splitInfo);
            if (list != null) {
                String join = TextUtils.join(File.pathSeparator, list);
                File splitOptDir = SplitPathManager.require().getSplitOptDir(splitInfo);
                if (file2 != null) {
                    str3 = file2.getAbsolutePath();
                }
                if (!splitMarkFile.exists()) {
                    try {
                        new DexClassLoader(join, splitOptDir.getAbsolutePath(), str3, SplitInstallerImpl.class.getClassLoader());
                    } catch (Throwable th) {
                        throw new InstallException(-17, th);
                    }
                }
                if (OEMCompat.shouldCheckOatFileInCurrentSys()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Start to check oat file, current api level is ");
                    sb3.append(VERSION.SDK_INT);
                    SplitLog.v(str2, sb3.toString(), new Object[0]);
                    boolean isSpecialManufacturer = OEMCompat.isSpecialManufacturer();
                    File oatFilePath = OEMCompat.getOatFilePath(file, splitOptDir);
                    if (FileUtil.isLegalFile(oatFilePath)) {
                        boolean checkOatFile = OEMCompat.checkOatFile(oatFilePath);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Result of oat file %s is ");
                        sb4.append(checkOatFile);
                        SplitLog.v(str2, sb4.toString(), oatFilePath.getAbsoluteFile());
                        if (!checkOatFile) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("Failed to check oat file ");
                            sb5.append(oatFilePath.getAbsolutePath());
                            SplitLog.w(str2, sb5.toString(), new Object[0]);
                            if (isSpecialManufacturer) {
                                try {
                                    FileUtil.deleteFileSafelyLock(oatFilePath, SplitPathManager.require().getSplitSpecialLockFile(splitInfo));
                                } catch (IOException unused) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("Failed to delete corrupted oat file ");
                                    sb6.append(oatFilePath.exists());
                                    SplitLog.w(str2, sb6.toString(), new Object[0]);
                                }
                            } else {
                                FileUtil.deleteFileSafely(oatFilePath);
                            }
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("System generate split ");
                            sb7.append(splitInfo.getSplitName());
                            sb7.append(" oat file failed!");
                            throw new InstallException(-18, new FileNotFoundException(sb7.toString()));
                        }
                    } else if (isSpecialManufacturer) {
                        SplitLog.v(str2, "Oat file %s is not exist in vivo & oppo, system would use interpreter mode.", oatFilePath.getAbsoluteFile());
                        File splitSpecialMarkFile = SplitPathManager.require().getSplitSpecialMarkFile(splitInfo);
                        if (!splitMarkFile.exists() && !splitSpecialMarkFile.exists()) {
                            return new InstallResult(splitInfo.getSplitName(), file, list, createInstalledMarkLock(splitSpecialMarkFile, SplitPathManager.require().getSplitSpecialLockFile(splitInfo)));
                        }
                    }
                }
            }
            return new InstallResult(splitInfo.getSplitName(), file, list, createInstalledMark(splitMarkFile));
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append("Split apk ");
        sb8.append(file.getAbsolutePath());
        sb8.append(" is illegal!");
        throw new InstallException(-11, new FileNotFoundException(sb8.toString()));
    }

    /* access modifiers changed from: protected */
    public void verifySignature(File file) {
        if (!SignatureValidator.validateSplit(this.appContext, file)) {
            deleteCorruptedFiles(Collections.singletonList(file));
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check split apk ");
            sb.append(file.getAbsolutePath());
            sb.append(" signature!");
            throw new InstallException(-12, new SignatureException(sb.toString()));
        }
    }
}
