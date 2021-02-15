package com.ss.android.medialib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import androidx.collection.ArraySet;
import com.ss.android.vesdk.VELogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.regex.Pattern;

public class DeviceInfoUtils {
    private static final String CPU_FILE = "/proc/cpuinfo";
    private static final String HARDWARE_PATTERN = "Hardware";
    private static final String TAG = "DeviceInfoUtils";
    private static String sCpuHardware;

    private static void closeQuietly(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getExternalStorage(Context context) {
        String str;
        try {
            str = Environment.getExternalStorageDirectory().getAbsolutePath();
        } catch (Exception unused) {
            str = "/sdcard";
        }
        try {
            StatFs statFs = new StatFs(str);
            return VERSION.SDK_INT >= 18 ? (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED : (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
        } catch (Exception unused2) {
            return -1;
        }
    }

    public static long getInternalStorage() {
        long j;
        long j2;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        if (VERSION.SDK_INT >= 18) {
            j = statFs.getBlockCountLong();
            j2 = statFs.getBlockSizeLong();
        } else {
            j = (long) statFs.getBlockCount();
            j2 = (long) statFs.getBlockSize();
        }
        return (j * j2) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    }

    public static String getMaxCpuFreq() {
        String str;
        InputStream inputStream = null;
        try {
            inputStream = new ProcessBuilder(new String[]{"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"}).start().getInputStream();
            byte[] bArr = new byte[24];
            str = "";
            while (inputStream.read(bArr) != -1) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(new String(bArr));
                str = sb.toString();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            str = "N/A";
            if (inputStream != null) {
                inputStream.close();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return str.trim();
    }

    public static int getNumOfCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]", file.getName());
                }
            }).length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int getRealScreenHeight(Context context) {
        int i = 0;
        if (context == null) {
            return 0;
        }
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (VERSION.SDK_INT >= 17) {
            Point point = new Point();
            defaultDisplay.getRealSize(point);
            i = point.y;
        } else {
            try {
                i = ((Integer) Display.class.getMethod("getRawHeight", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
            }
        }
        return i;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Set getSensorUsability(Context context) {
        ArraySet arraySet = new ArraySet();
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        if (sensorManager == null) {
            return arraySet;
        }
        for (int defaultSensor : new int[]{1, 13, 9, 4, 5, 10, 2, 3, 6, 8, 12, 11, 7, 15}) {
            Sensor defaultSensor2 = sensorManager.getDefaultSensor(defaultSensor);
            if (defaultSensor2 != null) {
                arraySet.add(defaultSensor2);
            }
        }
        return arraySet;
    }

    public static String getSimOperator(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        } catch (Exception unused) {
            VELogUtil.w(TAG, "No permission");
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x006d A[SYNTHETIC, Splitter:B:31:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0077 A[SYNTHETIC, Splitter:B:36:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0088 A[SYNTHETIC, Splitter:B:45:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0092 A[SYNTHETIC, Splitter:B:50:0x0092] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long getTotalMemory() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        long j;
        IOException e;
        try {
            fileReader = new FileReader("/proc/meminfo");
            try {
                bufferedReader = new BufferedReader(fileReader, 8192);
                try {
                    String readLine = bufferedReader.readLine();
                    String[] split = readLine.split("\\s+");
                    for (String str : split) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append("/t");
                        Log.i(readLine, sb.toString());
                    }
                    j = (long) Integer.valueOf(split[1]).intValue();
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        fileReader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } catch (IOException e4) {
                    e = e4;
                    try {
                        e.printStackTrace();
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        }
                        j = 0;
                        return j / 1024;
                    } catch (Throwable th) {
                        th = th;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (IOException e8) {
                                e8.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } catch (IOException e9) {
                e = e9;
                bufferedReader = null;
                e.printStackTrace();
                if (bufferedReader != null) {
                }
                if (fileReader != null) {
                }
                j = 0;
                return j / 1024;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
                if (bufferedReader != null) {
                }
                if (fileReader != null) {
                }
                throw th;
            }
        } catch (IOException e10) {
            fileReader = null;
            e = e10;
            bufferedReader = null;
            e.printStackTrace();
            if (bufferedReader != null) {
            }
            if (fileReader != null) {
            }
            j = 0;
            return j / 1024;
        } catch (Throwable th3) {
            fileReader = null;
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
            }
            if (fileReader != null) {
            }
            throw th;
        }
        return j / 1024;
    }

    @SuppressLint({"infer"})
    public static String readCpuHardware() {
        BufferedReader bufferedReader;
        Reader reader;
        String str = ":";
        if (!TextUtils.isEmpty(sCpuHardware)) {
            return sCpuHardware;
        }
        try {
            reader = new FileReader(CPU_FILE);
            try {
                bufferedReader = new BufferedReader(reader);
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else if (readLine.startsWith(HARDWARE_PATTERN) && readLine.contains(str)) {
                            String substring = readLine.substring(readLine.indexOf(str) + 1);
                            if (!TextUtils.isEmpty(substring)) {
                                sCpuHardware = substring.trim();
                                break;
                            }
                        }
                    } catch (IOException unused) {
                        closeQuietly(bufferedReader);
                        closeQuietly(reader);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(bufferedReader);
                        closeQuietly(reader);
                        throw th;
                    }
                }
                String str2 = sCpuHardware;
                closeQuietly(bufferedReader);
                closeQuietly(reader);
                return str2;
            } catch (IOException unused2) {
                bufferedReader = null;
                closeQuietly(bufferedReader);
                closeQuietly(reader);
                return null;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
                closeQuietly(bufferedReader);
                closeQuietly(reader);
                throw th;
            }
        } catch (IOException unused3) {
            reader = null;
            bufferedReader = null;
            closeQuietly(bufferedReader);
            closeQuietly(reader);
            return null;
        } catch (Throwable th3) {
            th = th3;
            reader = null;
            bufferedReader = null;
            closeQuietly(bufferedReader);
            closeQuietly(reader);
            throw th;
        }
    }
}
