package miui.util;

import android.os.DropBoxManager;
import android.os.Process;
import android.util.Log;
import com.android.camera.Util;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DropBoxLog {
    private static final String CLOUD_DROP_BOX_LOG_TAG = "micloud";
    private static final SoftReferenceSingleton DATE_FORMAT = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public SimpleDateFormat createInstance() {
            return new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        }
    };
    private StringBuilder mStringBuilder = new StringBuilder();

    private void appendLog(String str, String str2, String str3, Throwable th) {
        this.mStringBuilder.append(((SimpleDateFormat) DATE_FORMAT.get()).format(new Date()));
        this.mStringBuilder.append(' ');
        this.mStringBuilder.append(str);
        this.mStringBuilder.append('/');
        this.mStringBuilder.append(str2);
        this.mStringBuilder.append('(');
        this.mStringBuilder.append(Process.myPid());
        this.mStringBuilder.append(')');
        this.mStringBuilder.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        this.mStringBuilder.append(str3);
        if (th != null) {
            this.mStringBuilder.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            this.mStringBuilder.append(Log.getStackTraceString(th));
        }
        this.mStringBuilder.append(13);
    }

    public static void writeLog(CharSequence charSequence, String str) {
        DropBoxManager dropBoxManager = (DropBoxManager) AppConstants.getCurrentApplication().getSystemService("dropbox");
        if (charSequence != null && charSequence.length() > 0 && dropBoxManager != null && dropBoxManager.isTagEnabled(str)) {
            dropBoxManager.addText(str, charSequence.toString());
        }
    }

    public void commit(String str) {
        if (str == null) {
            str = AppConstants.getCurrentApplication().getPackageName();
        }
        StringBuilder sb = this.mStringBuilder;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("micloud_");
        sb2.append(str);
        writeLog(sb, sb2.toString());
        this.mStringBuilder.setLength(0);
    }

    public DropBoxLog d(String str, String str2) {
        Log.d(str, str2);
        appendLog("D", str, str2, null);
        return this;
    }

    public DropBoxLog d(String str, String str2, Throwable th) {
        Log.d(str, str2, th);
        appendLog("D", str, str2, th);
        return this;
    }

    public DropBoxLog e(String str, String str2) {
        Log.e(str, str2);
        appendLog("E", str, str2, null);
        return this;
    }

    public DropBoxLog e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
        appendLog("E", str, str2, th);
        return this;
    }

    public String getMessage() {
        return this.mStringBuilder.toString();
    }

    public DropBoxLog i(String str, String str2) {
        Log.i(str, str2);
        appendLog("I", str, str2, null);
        return this;
    }

    public DropBoxLog i(String str, String str2, Throwable th) {
        Log.i(str, str2, th);
        appendLog("I", str, str2, th);
        return this;
    }

    public DropBoxLog v(String str, String str2) {
        Log.v(str, str2);
        appendLog("V", str, str2, null);
        return this;
    }

    public DropBoxLog v(String str, String str2, Throwable th) {
        Log.v(str, str2, th);
        appendLog("V", str, str2, th);
        return this;
    }

    public DropBoxLog w(String str, String str2) {
        Log.w(str, str2);
        appendLog("W", str, str2, null);
        return this;
    }

    public DropBoxLog w(String str, String str2, Throwable th) {
        Log.w(str, str2, th);
        appendLog("W", str, str2, th);
        return this;
    }
}
