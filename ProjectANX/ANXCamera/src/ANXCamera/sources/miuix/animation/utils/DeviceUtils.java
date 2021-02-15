package miuix.animation.utils;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceUtils {
    static final String ARCHITECTURE = "CPU architecture";
    static final int ARM_V8 = 8;
    static final int BIG_CORE_FREQ = 2000000;
    static final int CORE_COUNT = 8;
    static final String CpuMaxInfoFormat = "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq";
    static final int D800 = 73;
    public static final int DEVICE_HIGHEND = 2;
    public static final int DEVICE_MIDDLE = 1;
    public static final int DEVICE_PRIMARY = 0;
    public static final int DEVICE_UNKNOWN = -1;
    static final String HEX = "0x";
    static final int HIGH_FREQ = 2700000;
    static final String IMPLEMENTOR = "CPU implementer";
    static final int MIDDLE_EIGHT_SERIES = 45;
    static final int MIDDLE_FREQ = 2300000;
    static final int MTK_DIMENSITY = 68;
    static final Pattern MT_PATTERN = Pattern.compile("MT([\\d]{2})([\\d]+)");
    static final String PART = "CPU part";
    static final String PROCESSOR = "processor";
    static final String QUALCOMM = "Qualcomm";
    static final String SEPARATOR = ": ";
    static final Pattern SM_PATTERN = Pattern.compile("Inc ([A-Z]+)([\\d]+)");
    static final String SNAPDRAGON = "sm";
    static final String TAG = "DeviceUtils";
    static int mLevel = -1;
    static int mTotalRam = Integer.MAX_VALUE;

    public class CpuInfo {
        int architecture;
        int id;
        int implementor;
        int maxFreq;
        int part;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CpuInfo{id=");
            sb.append(this.id);
            sb.append(", implementor=");
            sb.append(Integer.toHexString(this.implementor));
            sb.append(", architecture=");
            sb.append(this.architecture);
            sb.append(", part=");
            sb.append(Integer.toHexString(this.part));
            sb.append(", maxFreq=");
            sb.append(this.maxFreq);
            sb.append('}');
            return sb.toString();
        }
    }

    public class CpuStats {
        int bigCoreCount;
        int level = -1;
        int maxFreq;
        int smallCoreCount;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CpuStats{level=");
            sb.append(this.level);
            sb.append(", maxFreq=");
            sb.append(this.maxFreq);
            sb.append(", bigCoreCount=");
            sb.append(this.bigCoreCount);
            sb.append(", smallCoreCount=");
            sb.append(this.smallCoreCount);
            sb.append('}');
            return sb.toString();
        }
    }

    private static CpuInfo createCpuInfo(String str) {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.id = Integer.parseInt(str);
        String contentFromFileInfo = getContentFromFileInfo(String.format(Locale.ENGLISH, CpuMaxInfoFormat, new Object[]{Integer.valueOf(cpuInfo.id)}));
        if (contentFromFileInfo != null) {
            cpuInfo.maxFreq = Integer.parseInt(contentFromFileInfo);
        }
        return cpuInfo;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0020, code lost:
        if (r5.maxFreq > MIDDLE_FREQ) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        r5.level = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        if (r0 > MIDDLE_FREQ) goto L_0x0022;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void decideLevel(CpuStats cpuStats) {
        if (cpuStats.level == -1) {
            if (cpuStats.bigCoreCount >= 4) {
                int i = cpuStats.maxFreq;
                if (i > HIGH_FREQ) {
                    cpuStats.level = 2;
                }
            }
            cpuStats.level = 1;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miuix.animation.utils.DeviceUtils$CpuInfo>, for r4v0, types: [java.util.List<miuix.animation.utils.DeviceUtils$CpuInfo>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void doCpuStats(CpuStats cpuStats, List<CpuInfo> list) {
        for (CpuInfo cpuInfo : list) {
            if (cpuInfo.architecture < 8) {
                cpuStats.level = 0;
            }
            int i = cpuInfo.maxFreq;
            if (i > cpuStats.maxFreq) {
                cpuStats.maxFreq = i;
            }
            if (cpuInfo.maxFreq >= BIG_CORE_FREQ) {
                cpuStats.bigCoreCount++;
            } else {
                cpuStats.smallCoreCount++;
            }
        }
        decideLevel(cpuStats);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0021 A[SYNTHETIC, Splitter:B:13:0x0021] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0028 A[SYNTHETIC, Splitter:B:21:0x0028] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String getContentFromFileInfo(String str) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                try {
                    fileInputStream.close();
                } catch (IOException unused) {
                }
                return readLine;
            } catch (IOException unused2) {
                if (fileInputStream != null) {
                }
                return null;
            } catch (Throwable th) {
                th = th;
                if (fileInputStream != null) {
                }
                throw th;
            }
        } catch (IOException unused3) {
            fileInputStream = null;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused4) {
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    private static void getCpuInfo(String str, String str2, CpuInfo cpuInfo) {
        if (str.contains(IMPLEMENTOR)) {
            cpuInfo.implementor = toInt(str2);
        } else if (str.contains(ARCHITECTURE)) {
            cpuInfo.architecture = toInt(str2);
        } else if (str.contains(PART)) {
            cpuInfo.part = toInt(str2);
        }
    }

    public static List getCpuInfoList() {
        ArrayList arrayList = new ArrayList();
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            CpuInfo cpuInfo = null;
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(SEPARATOR);
                if (split.length > 1) {
                    cpuInfo = parseLine(split, arrayList, cpuInfo);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getChipSetFromCpuInfo failed", e);
        }
        return arrayList;
    }

    private static int getCpuLevel() {
        String hardwareInfo = getHardwareInfo();
        int i = hardwareInfo.length() > 0 ? hardwareInfo.contains(QUALCOMM) ? getQualcommCpuLevel(hardwareInfo) : getMtkCpuLevel(hardwareInfo) : -1;
        return i == -1 ? getCpuStats().level : i;
    }

    public static CpuStats getCpuStats() {
        List cpuInfoList = getCpuInfoList();
        CpuStats cpuStats = new CpuStats();
        if (cpuInfoList.size() < 8) {
            cpuStats.level = 0;
        }
        doCpuStats(cpuStats, cpuInfoList);
        return cpuStats;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0020, code lost:
        if (getTotalRam() <= 4) goto L_0x000d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getDeviceLevel() {
        int i = mLevel;
        if (i != -1) {
            return i;
        }
        if (!isMiuiLite()) {
            mLevel = getCpuLevel();
            if (mLevel == 1) {
            }
            return mLevel;
        }
        mLevel = 0;
        return mLevel;
    }

    private static String getHardwareInfo() {
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (!scanner.hasNextLine()) {
                    String[] split = nextLine.split(SEPARATOR);
                    if (split.length > 1) {
                        return split[1];
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getChipSetFromCpuInfo failed", e);
        }
        return "";
    }

    private static int getMtkCpuLevel(String str) {
        Matcher matcher = MT_PATTERN.matcher(str);
        if (matcher.find()) {
            String group = matcher.group(1);
            if (group != null) {
                String group2 = matcher.group(2);
                if (group2 != null) {
                    return (Integer.parseInt(group) != 68 || Integer.parseInt(group2) < 73) ? 0 : 1;
                }
            }
        }
        return -1;
    }

    public static int getQualcommCpuLevel(String str) {
        Matcher matcher = SM_PATTERN.matcher(str);
        if (matcher.find()) {
            String group = matcher.group(1);
            if (group != null) {
                String group2 = matcher.group(2);
                if (group2 != null && group.toLowerCase(Locale.ENGLISH).equals(SNAPDRAGON)) {
                    int parseInt = Integer.parseInt(group2.substring(0, 1));
                    int parseInt2 = Integer.parseInt(group2.substring(1));
                    if (parseInt < 8 || parseInt2 <= 45) {
                        return parseInt >= 7 ? 1 : 0;
                    }
                    return 2;
                }
            }
        }
        return -1;
    }

    public static int getTotalRam() {
        if (mTotalRam == Integer.MAX_VALUE) {
            try {
                mTotalRam = (int) (((((Long) Class.forName("miui.util.HardwareInfo").getMethod("getTotalPhysicalMemory", new Class[0]).invoke(null, new Object[0])).longValue() / 1024) / 1024) / 1024);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                mTotalRam = 0;
            }
        }
        return mTotalRam;
    }

    private static boolean isMiuiLite() {
        try {
            return ((Boolean) Class.forName("miui.os.Build").getDeclaredField("IS_MIUI_LITE_VERSION").get(null)).booleanValue();
        } catch (Exception e) {
            Log.i(CommonUtils.TAG, "getDeviceLevel failed", e);
            return false;
        }
    }

    private static CpuInfo parseLine(String[] strArr, List list, CpuInfo cpuInfo) {
        String trim = strArr[1].trim();
        if (strArr[0].contains(PROCESSOR) && TextUtils.isDigitsOnly(trim)) {
            CpuInfo createCpuInfo = createCpuInfo(trim);
            list.add(createCpuInfo);
            return createCpuInfo;
        } else if (cpuInfo == null) {
            return cpuInfo;
        } else {
            getCpuInfo(strArr[0], trim, cpuInfo);
            return cpuInfo;
        }
    }

    private static int toInt(String str) {
        return str.startsWith(HEX) ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str);
    }
}
