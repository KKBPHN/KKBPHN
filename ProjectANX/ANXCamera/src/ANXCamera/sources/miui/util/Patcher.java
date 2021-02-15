package miui.util;

public class Patcher {
    static {
        System.loadLibrary("miuidiffpatcher");
    }

    public native int applyPatch(String str, String str2, String str3);
}
