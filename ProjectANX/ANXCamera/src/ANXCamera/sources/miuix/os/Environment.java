package miuix.os;

import java.io.File;
import java.util.regex.Pattern;

public class Environment extends android.os.Environment {
    private static File EXTERNAL_STORAGE_MIUI_DIRECTORY = null;
    private static final File MIUI_APP_DIRECTORY = new File(getMiuiDataDirectory(), "apps");
    private static final File MIUI_CUSTOMIZED_DIRECTORY = new File(getMiuiDataDirectory(), "current");
    private static final File MIUI_DATA_DIRECTORY = new File("/data/miui/");
    private static final String MIUI_DIRECTORY_NAME = "MIUI";
    private static final File MIUI_PRESET_APP_DIRECTORY = new File(getMiuiDataDirectory(), "preset_apps");
    private static int sCpuCount = 0;

    protected Environment() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static int getCpuCount() {
        if (sCpuCount == 0) {
            Pattern compile = Pattern.compile("cpu[0-9]*");
            int i = 0;
            for (String matcher : new File("/sys/devices/system/cpu/").list()) {
                if (compile.matcher(matcher).matches()) {
                    i++;
                }
            }
            sCpuCount = i;
        }
        return sCpuCount;
    }

    public static File getExternalStorageMiuiDirectory() {
        try {
            if (EXTERNAL_STORAGE_MIUI_DIRECTORY == null) {
                EXTERNAL_STORAGE_MIUI_DIRECTORY = new File(android.os.Environment.getExternalStorageDirectory(), MIUI_DIRECTORY_NAME);
            }
            if (!EXTERNAL_STORAGE_MIUI_DIRECTORY.exists() && android.os.Environment.getExternalStorageDirectory().exists()) {
                EXTERNAL_STORAGE_MIUI_DIRECTORY.mkdir();
            }
            return EXTERNAL_STORAGE_MIUI_DIRECTORY;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getMiuiAppDirectory() {
        return MIUI_APP_DIRECTORY;
    }

    public static File getMiuiCustomizedDirectory() {
        return MIUI_CUSTOMIZED_DIRECTORY;
    }

    public static File getMiuiDataDirectory() {
        return MIUI_DATA_DIRECTORY;
    }

    public static File getMiuiPresetAppDirectory() {
        return MIUI_PRESET_APP_DIRECTORY;
    }

    public static boolean isExternalStorageMounted() {
        return "mounted".equals(android.os.Environment.getExternalStorageState());
    }
}
