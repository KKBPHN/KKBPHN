package O00000o0;

import com.mi.device.Common;
import java.util.Locale;

public class O00000Oo {
    private O00000Oo() {
    }

    public static Common O000Oo0(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("com.mi.device.");
            sb.append(capitalize(str));
            return (Common) Class.forName(sb.toString()).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException unused) {
            return new Common();
        }
    }

    private static String capitalize(String str) {
        char[] charArray = str.toLowerCase(Locale.ENGLISH).toCharArray();
        if (charArray[0] >= 'a' && charArray[0] <= 'z') {
            charArray[0] = (char) (charArray[0] - ' ');
        }
        return new String(charArray);
    }
}
