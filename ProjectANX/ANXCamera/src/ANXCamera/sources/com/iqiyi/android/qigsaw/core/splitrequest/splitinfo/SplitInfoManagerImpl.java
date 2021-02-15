package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.common.AbiUtil;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo.LibInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo.LibInfo.Lib;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

final class SplitInfoManagerImpl implements SplitInfoManager {
    private static final String TAG = "SplitInfoManagerImpl";
    private SplitDetails mSplitDetails;
    private SplitInfoVersionManager mVersionManager;

    SplitInfoManagerImpl() {
    }

    private static InputStream createInputStreamFromAssets(Context context, String str) {
        Resources resources = context.getResources();
        if (resources != null) {
            try {
                return resources.getAssets().open(str);
            } catch (IOException unused) {
            }
        }
        return null;
    }

    private SplitDetails createSplitDetailsForDefaultVersion(Context context, String str) {
        String str2 = TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SplitConstants.QIGSAW_PREFIX);
            sb.append(str);
            sb.append(SplitConstants.DOT_JSON);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Default split file name: ");
            sb3.append(sb2);
            SplitLog.i(str2, sb3.toString(), new Object[0]);
            long currentTimeMillis = System.currentTimeMillis();
            SplitDetails parseSplitContentsForDefaultVersion = parseSplitContentsForDefaultVersion(context, sb2);
            SplitLog.i(str2, "Cost %d mil-second to parse default split info", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return parseSplitContentsForDefaultVersion;
        } catch (Throwable th) {
            SplitLog.printErrStackTrace(str2, th, "Failed to create default split info!", new Object[0]);
            return null;
        }
    }

    private SplitDetails createSplitDetailsForNewVersion(File file) {
        String str = TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Updated split file path: ");
            sb.append(file.getAbsolutePath());
            SplitLog.i(str, sb.toString(), new Object[0]);
            long currentTimeMillis = System.currentTimeMillis();
            SplitDetails parseSplitContentsForNewVersion = parseSplitContentsForNewVersion(file);
            SplitLog.i(str, "Cost %d mil-second to parse updated split info", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return parseSplitContentsForNewVersion;
        } catch (Throwable th) {
            SplitLog.printErrStackTrace(str, th, "Failed to create updated split info!", new Object[0]);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00be, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized SplitDetails getOrCreateSplitDetails(Context context) {
        SplitDetails createSplitDetailsForNewVersion;
        SplitInfoVersionManager splitInfoVersionManager = getSplitInfoVersionManager();
        SplitDetails splitDetails = getSplitDetails();
        if (splitDetails == null) {
            String currentVersion = splitInfoVersionManager.getCurrentVersion();
            String defaultVersion = splitInfoVersionManager.getDefaultVersion();
            if (currentVersion != null && currentVersion.length() > 0 && currentVersion.indexOf(95) > -1) {
                StringBuilder sb = new StringBuilder();
                sb.append("5.0.0.0");
                sb.append(currentVersion.substring(currentVersion.indexOf(95)));
                currentVersion = sb.toString();
            }
            if (defaultVersion != null && defaultVersion.length() > 0 && defaultVersion.indexOf(95) > -1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("5.0.0.0");
                sb2.append(defaultVersion.substring(defaultVersion.indexOf(95)));
                defaultVersion = sb2.toString();
            }
            SplitLog.i(TAG, "currentVersion : %s defaultVersion : %s", currentVersion, defaultVersion);
            if (defaultVersion.equals(currentVersion)) {
                createSplitDetailsForNewVersion = createSplitDetailsForDefaultVersion(context, defaultVersion);
            } else {
                File rootDir = splitInfoVersionManager.getRootDir();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(SplitConstants.QIGSAW_PREFIX);
                sb3.append(currentVersion);
                sb3.append(SplitConstants.DOT_JSON);
                createSplitDetailsForNewVersion = createSplitDetailsForNewVersion(new File(rootDir, sb3.toString()));
            }
            splitDetails = createSplitDetailsForNewVersion;
            if (splitDetails != null) {
                if (TextUtils.isEmpty(splitDetails.getQigsawId())) {
                    return null;
                }
                if (!splitDetails.verifySplitInfoListing()) {
                    return null;
                }
            }
            this.mSplitDetails = splitDetails;
        }
    }

    private SplitDetails getSplitDetails() {
        return this.mSplitDetails;
    }

    private SplitInfoVersionManager getSplitInfoVersionManager() {
        return this.mVersionManager;
    }

    private static SplitDetails parseSplitContentsForDefaultVersion(Context context, String str) {
        return parseSplitsContent(readInputStreamContent(createInputStreamFromAssets(context, str)));
    }

    private SplitDetails parseSplitContentsForNewVersion(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        return parseSplitsContent(readInputStreamContent(new FileInputStream(file)));
    }

    private static SplitDetails parseSplitsContent(String str) {
        List list;
        List list2;
        List list3;
        String str2;
        long j;
        LinkedHashMap linkedHashMap;
        int i;
        String str3;
        boolean z;
        int i2;
        ArrayList arrayList;
        List list4;
        List list5;
        List list6;
        LibInfo libInfo;
        String str4;
        String str5;
        long j2;
        boolean z2;
        String str6 = str;
        if (str6 == null) {
            return null;
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        JSONObject jSONObject = new JSONObject(str6);
        String optString = jSONObject.optString("qigsawId");
        String optString2 = jSONObject.optString("appVersionName");
        JSONArray optJSONArray = jSONObject.optJSONArray("abiFilters");
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            list = null;
        } else {
            ArrayList arrayList2 = new ArrayList(optJSONArray.length());
            for (int i3 = 0; i3 < optJSONArray.length(); i3++) {
                arrayList2.add(optJSONArray.getString(i3));
            }
            list = arrayList2;
        }
        JSONArray optJSONArray2 = jSONObject.optJSONArray("updateSplits");
        if (optJSONArray2 == null || optJSONArray2.length() <= 0) {
            list2 = null;
        } else {
            ArrayList arrayList3 = new ArrayList(optJSONArray2.length());
            for (int i4 = 0; i4 < optJSONArray2.length(); i4++) {
                arrayList3.add(optJSONArray2.getString(i4));
            }
            list2 = arrayList3;
        }
        JSONArray optJSONArray3 = jSONObject.optJSONArray("splitEntryFragments");
        if (optJSONArray3 == null || optJSONArray3.length() <= 0) {
            list3 = null;
        } else {
            ArrayList arrayList4 = new ArrayList(optJSONArray3.length());
            for (int i5 = 0; i5 < optJSONArray3.length(); i5++) {
                arrayList4.add(optJSONArray3.getString(i5));
            }
            list3 = arrayList4;
        }
        JSONArray optJSONArray4 = jSONObject.optJSONArray("splits");
        int i6 = 0;
        while (i6 < optJSONArray4.length()) {
            JSONObject jSONObject2 = optJSONArray4.getJSONObject(i6);
            boolean optBoolean = jSONObject2.optBoolean("builtIn");
            String optString3 = jSONObject2.optString(SplitConstants.KET_NAME);
            String optString4 = jSONObject2.optString("version");
            String optString5 = jSONObject2.optString("url");
            String str7 = "md5";
            String optString6 = jSONObject2.optString(str7);
            String str8 = "size";
            long optLong = jSONObject2.optLong(str8);
            int optInt = jSONObject2.optInt("minSdkVersion");
            JSONArray jSONArray = optJSONArray4;
            JSONArray optJSONArray5 = jSONObject2.optJSONArray("nativeLibraries");
            if (optJSONArray5 == null || optJSONArray5.length() <= 0) {
                i2 = optInt;
                linkedHashMap = linkedHashMap2;
                i = i6;
                str3 = optString;
                str2 = optString6;
                j = optLong;
                z = optBoolean;
                list4 = null;
                arrayList = null;
            } else {
                str3 = optString;
                i = i6;
                arrayList = new ArrayList(optJSONArray5.length());
                linkedHashMap = linkedHashMap2;
                List arrayList5 = new ArrayList(optJSONArray5.length());
                i2 = optInt;
                int i7 = 0;
                while (i7 < optJSONArray5.length()) {
                    JSONObject optJSONObject = optJSONArray5.optJSONObject(i7);
                    JSONArray jSONArray2 = optJSONArray5;
                    String optString7 = optJSONObject.optString("abi");
                    arrayList5.add(optString7);
                    List list7 = arrayList5;
                    JSONArray optJSONArray6 = optJSONObject.optJSONArray("jniLibs");
                    ArrayList arrayList6 = new ArrayList();
                    if (optJSONArray6 == null || optJSONArray6.length() <= 0) {
                        str4 = str7;
                        str5 = optString6;
                        j2 = optLong;
                        z2 = optBoolean;
                    } else {
                        j2 = optLong;
                        z2 = optBoolean;
                        int i8 = 0;
                        while (i8 < optJSONArray6.length()) {
                            JSONObject optJSONObject2 = optJSONArray6.optJSONObject(i8);
                            JSONArray jSONArray3 = optJSONArray6;
                            String str9 = optString6;
                            String str10 = str7;
                            arrayList6.add(new Lib(optJSONObject2.optString("name"), optJSONObject2.optString(str7), optJSONObject2.optLong(str8)));
                            i8++;
                            optJSONArray6 = jSONArray3;
                            optString6 = str9;
                            str7 = str10;
                        }
                        str4 = str7;
                        str5 = optString6;
                    }
                    arrayList.add(new LibInfo(optString7, arrayList6));
                    i7++;
                    optJSONArray5 = jSONArray2;
                    arrayList5 = list7;
                    optBoolean = z2;
                    optLong = j2;
                    optString6 = str5;
                    str7 = str4;
                }
                str2 = optString6;
                j = optLong;
                z = optBoolean;
                list4 = arrayList5;
            }
            int optInt2 = jSONObject2.optInt("dexNumber");
            JSONArray optJSONArray7 = jSONObject2.optJSONArray("workProcesses");
            if (optJSONArray7 == null || optJSONArray7.length() <= 0) {
                list5 = null;
            } else {
                ArrayList arrayList7 = new ArrayList(optJSONArray7.length());
                for (int i9 = 0; i9 < optJSONArray7.length(); i9++) {
                    arrayList7.add(optJSONArray7.optString(i9));
                }
                list5 = arrayList7;
            }
            JSONArray optJSONArray8 = jSONObject2.optJSONArray("dependencies");
            if (optJSONArray8 == null || optJSONArray8.length() <= 0) {
                list6 = null;
            } else {
                ArrayList arrayList8 = new ArrayList(optJSONArray8.length());
                for (int i10 = 0; i10 < optJSONArray8.length(); i10++) {
                    arrayList8.add(optJSONArray8.optString(i10));
                }
                list6 = arrayList8;
            }
            String findBasePrimaryAbi = AbiUtil.findBasePrimaryAbi(list);
            if (list4 != null) {
                String findSplitPrimaryAbi = AbiUtil.findSplitPrimaryAbi(findBasePrimaryAbi, list4);
                if (findSplitPrimaryAbi != null) {
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        LibInfo libInfo2 = (LibInfo) it.next();
                        if (findSplitPrimaryAbi.contains(libInfo2.getAbi())) {
                            libInfo = libInfo2;
                            break;
                        }
                    }
                }
            }
            libInfo = null;
            String str11 = optString3;
            SplitInfo splitInfo = new SplitInfo(optString3, optString2, optString4, optString5, str2, j, z, i2, optInt2, list5, list6, list4 != null, libInfo);
            LinkedHashMap linkedHashMap3 = linkedHashMap;
            linkedHashMap3.put(str11, splitInfo);
            i6 = i + 1;
            optJSONArray4 = jSONArray;
            linkedHashMap2 = linkedHashMap3;
            optString = str3;
        }
        String str12 = optString;
        SplitDetails splitDetails = new SplitDetails(optString, optString2, list, list2, list3, new SplitInfoListing(linkedHashMap2));
        return splitDetails;
    }

    private static String readInputStreamContent(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                sb.append(readLine);
            } else {
                FileUtil.closeQuietly(inputStream);
                FileUtil.closeQuietly(bufferedReader);
                return sb.toString();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void attach(SplitInfoVersionManager splitInfoVersionManager) {
        this.mVersionManager = splitInfoVersionManager;
    }

    @Nullable
    public SplitDetails createSplitDetailsForJsonFile(@NonNull String str) {
        File file = new File(str);
        if (file.exists()) {
            return createSplitDetailsForNewVersion(file);
        }
        return null;
    }

    public Collection getAllSplitInfo(Context context) {
        if (getOrCreateSplitDetails(context) != null) {
            return this.mSplitDetails.getSplitInfoListing().getSplitInfoMap().values();
        }
        return null;
    }

    @Nullable
    public String getBaseAppVersionName(Context context) {
        if (getOrCreateSplitDetails(context) != null) {
            return this.mSplitDetails.getAppVersionName();
        }
        return null;
    }

    public String getCurrentSplitInfoVersion() {
        return getSplitInfoVersionManager().getCurrentVersion();
    }

    @Nullable
    public String getQigsawId(Context context) {
        SplitDetails orCreateSplitDetails = getOrCreateSplitDetails(context);
        if (orCreateSplitDetails != null) {
            return orCreateSplitDetails.getQigsawId();
        }
        return null;
    }

    public List getSplitEntryFragments(Context context) {
        SplitDetails orCreateSplitDetails = getOrCreateSplitDetails(context);
        if (orCreateSplitDetails != null) {
            return orCreateSplitDetails.getSplitEntryFragments();
        }
        return null;
    }

    public SplitInfo getSplitInfo(Context context, String str) {
        SplitDetails orCreateSplitDetails = getOrCreateSplitDetails(context);
        if (orCreateSplitDetails != null) {
            for (SplitInfo splitInfo : orCreateSplitDetails.getSplitInfoListing().getSplitInfoMap().values()) {
                if (splitInfo.getSplitName().equals(str)) {
                    return splitInfo;
                }
            }
        }
        return null;
    }

    public List getSplitInfos(Context context, Collection collection) {
        SplitDetails orCreateSplitDetails = getOrCreateSplitDetails(context);
        if (orCreateSplitDetails == null) {
            return null;
        }
        Collection<SplitInfo> values = orCreateSplitDetails.getSplitInfoListing().getSplitInfoMap().values();
        ArrayList arrayList = new ArrayList(collection.size());
        for (SplitInfo splitInfo : values) {
            if (collection.contains(splitInfo.getSplitName())) {
                arrayList.add(splitInfo);
            }
        }
        return arrayList;
    }

    @Nullable
    public List getUpdateSplits(Context context) {
        SplitDetails orCreateSplitDetails = getOrCreateSplitDetails(context);
        if (orCreateSplitDetails != null) {
            return orCreateSplitDetails.getUpdateSplits();
        }
        return null;
    }

    public boolean updateSplitInfoVersion(Context context, String str, File file) {
        SplitInfoVersionManager splitInfoVersionManager = getSplitInfoVersionManager();
        if (str != null && str.length() > 0 && str.indexOf(95) > -1) {
            StringBuilder sb = new StringBuilder();
            sb.append("5.0.0.0");
            sb.append(str.substring(str.indexOf(95)));
            str = sb.toString();
        }
        return splitInfoVersionManager.updateVersion(context, str, file);
    }
}
