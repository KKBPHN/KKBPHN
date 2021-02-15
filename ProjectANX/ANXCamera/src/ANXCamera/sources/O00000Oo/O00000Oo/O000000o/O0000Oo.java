package O00000Oo.O00000oO.O000000o;

import android.content.Context;
import dalvik.system.DexFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class O0000Oo implements O0000O0o {
    private static final O0000Oo sInstance = new O0000Oo();
    private O0000O0o O0ooO;

    public O0000Oo() {
        StringBuilder sb = new StringBuilder();
        sb.append("com.mi.device.Device");
        sb.append(C0124O00000oO.O0Ooo0o.toUpperCase(Locale.ENGLISH));
        this.O0ooO = O000Ooo(sb.toString());
        if (this.O0ooO == null) {
            this.O0ooO = new C0125O00000oo();
        }
    }

    private static List O0000O0o(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            Enumeration entries = new DexFile(context.getPackageCodePath()).entries();
            while (entries.hasMoreElements()) {
                String str2 = (String) entries.nextElement();
                if (str2.contains(str)) {
                    arrayList.add(str2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static O0000O0o O000Ooo(String str) {
        try {
            return (O0000O0o) Class.forName(str).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InstantiationException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    private String O000OooO(String str) {
        char[] charArray = str.toCharArray();
        if (charArray[0] < 'a' || charArray[0] > 'z') {
            return str;
        }
        charArray[0] = (char) (charArray[0] - ' ');
        return String.valueOf(charArray);
    }

    public static O0000Oo getDevice() {
        return sInstance;
    }

    public boolean O00000Oo() {
        return this.O0ooO.O00000Oo();
    }

    public boolean O00000oo() {
        return this.O0ooO.O00000oo();
    }

    public boolean O0000O0o() {
        return this.O0ooO.O0000O0o();
    }

    public int O0000OOo() {
        return this.O0ooO.O0000OOo();
    }
}
