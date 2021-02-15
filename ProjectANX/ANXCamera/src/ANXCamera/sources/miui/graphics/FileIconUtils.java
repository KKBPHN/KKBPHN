package miui.graphics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.android.camera.statistic.MistatsConstants.Manual;
import java.util.HashMap;
import miui.R;

public class FileIconUtils {
    private static final String LOG_TAG = "FileIconHelper";
    private static final String TYPE_APK = "apk";
    private static HashMap sFileExtToIcons = new HashMap();

    static {
        addItem(new String[]{"mp3"}, R.drawable.file_icon_mp3);
        addItem(new String[]{"wma"}, R.drawable.file_icon_wma);
        addItem(new String[]{"wav"}, R.drawable.file_icon_wav);
        addItem(new String[]{"mid"}, R.drawable.file_icon_mid);
        addItem(new String[]{"mp4", "wmv", "mpeg", "m4v", "3gp", "3g2", "3gpp2", "asf", "flv", "mkv", "vob", "ts", "f4v", "rm", "mov", "rmvb"}, R.drawable.file_icon_video);
        addItem(new String[]{"jpg", "jpeg", "gif", "png", "bmp", "wbmp"}, R.drawable.file_icon_picture);
        addItem(new String[]{"txt", Manual.LOG, "ini", "lrc"}, R.drawable.file_icon_txt);
        addItem(new String[]{"doc", "docx"}, R.drawable.file_icon_doc);
        addItem(new String[]{"ppt", "pptx"}, R.drawable.file_icon_ppt);
        addItem(new String[]{"xls", "xlsx"}, R.drawable.file_icon_xls);
        addItem(new String[]{"wps"}, R.drawable.file_icon_wps);
        addItem(new String[]{"pps"}, R.drawable.file_icon_pps);
        addItem(new String[]{"et"}, R.drawable.file_icon_et);
        addItem(new String[]{"wpt"}, R.drawable.file_icon_wpt);
        addItem(new String[]{"ett"}, R.drawable.file_icon_ett);
        addItem(new String[]{"dps"}, R.drawable.file_icon_dps);
        addItem(new String[]{"dpt"}, R.drawable.file_icon_dpt);
        addItem(new String[]{"pdf"}, R.drawable.file_icon_pdf);
        addItem(new String[]{"zip"}, R.drawable.file_icon_zip);
        addItem(new String[]{"mtz"}, R.drawable.file_icon_theme);
        addItem(new String[]{"rar"}, R.drawable.file_icon_rar);
        addItem(new String[]{"apk"}, R.drawable.file_icon_apk);
        addItem(new String[]{"amr"}, R.drawable.file_icon_amr);
        addItem(new String[]{"vcf"}, R.drawable.file_icon_vcf);
        addItem(new String[]{"flac"}, R.drawable.file_icon_flac);
        addItem(new String[]{"aac"}, R.drawable.file_icon_aac);
        addItem(new String[]{"ape"}, R.drawable.file_icon_ape);
        addItem(new String[]{"m4a"}, R.drawable.file_icon_m4a);
        addItem(new String[]{"ogg"}, R.drawable.file_icon_ogg);
        addItem(new String[]{"audio"}, R.drawable.file_icon_audio);
        addItem(new String[]{"html"}, R.drawable.file_icon_html);
        addItem(new String[]{"xml"}, R.drawable.file_icon_xml);
        addItem(new String[]{"3gpp"}, R.drawable.file_icon_3gpp);
    }

    protected FileIconUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    private static void addItem(String[] strArr, int i) {
        if (strArr != null) {
            for (String lowerCase : strArr) {
                sFileExtToIcons.put(lowerCase.toLowerCase(), Integer.valueOf(i));
            }
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context] */
    /* JADX WARNING: type inference failed for: r2v1, types: [android.content.Context] */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r2v5, types: [android.graphics.drawable.Drawable] */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=android.content.Context, code=null, for r2v0, types: [android.content.Context] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v4
  assigns: []
  uses: []
  mth insns count: 20
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
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Drawable getApkIcon(Context r2, String str) {
        PackageManager packageManager = r2.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(str, 1);
        if (packageArchiveInfo != null) {
            ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
            if (applicationInfo != null) {
                try {
                    r2 = r2;
                    applicationInfo.publicSourceDir = str;
                    r2 = packageManager.getApplicationIcon(applicationInfo);
                    r2 = r2;
                    return r2;
                } catch (OutOfMemoryError e) {
                    Log.e(LOG_TAG, e.toString());
                    r2 = r2;
                }
            }
        }
        return r2.getResources().getDrawable(R.drawable.file_icon_default);
    }

    private static String getExtFromFilename(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf != -1 ? str.substring(lastIndexOf + 1, str.length()) : "";
    }

    public static Drawable getFileIcon(Context context, String str) {
        String extFromFilename = getExtFromFilename(str);
        return extFromFilename.equals("apk") ? getApkIcon(context, str) : context.getResources().getDrawable(getFileIconId(extFromFilename));
    }

    public static int getFileIconId(String str) {
        Integer num = (Integer) sFileExtToIcons.get(str.toLowerCase());
        return num == null ? R.drawable.file_icon_default : num.intValue();
    }
}
