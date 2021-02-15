package miui.os;

import android.util.Log;
import java.io.IOException;
import java.util.Locale;

public class ProcessUtils {
    private static final String TAG = "ProcessUtils";

    protected ProcessUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static String getProcessNameByPid(int i) {
        String format = String.format(Locale.US, "/proc/%d/cmdline", new Object[]{Integer.valueOf(i)});
        try {
            String readFileAsString = FileUtils.readFileAsString(format);
            if (readFileAsString != null) {
                int indexOf = readFileAsString.indexOf(0);
                if (indexOf >= 0) {
                    readFileAsString = readFileAsString.substring(0, indexOf);
                }
                return readFileAsString;
            }
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fail to read cmdline: ");
            sb.append(format);
            Log.e(TAG, sb.toString(), e);
        }
        return null;
    }
}
