package miui.text;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.miui.internal.util.DirectIndexedFileExtractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import miui.util.AppConstants;
import miui.util.DirectIndexedFile;
import miui.util.DirectIndexedFile.Reader;
import miui.util.SoftReferenceSingleton;

public class ChinesePinyinConverter {
    private static final char FIRST_BASIC_UNIHAN = '一';
    private static final char FIRST_HINDI_UNICODE = 'ऀ';
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public ChinesePinyinConverter createInstance() {
            return new ChinesePinyinConverter();
        }
    };
    private static final char LAST_BASIC_UNIHAN = '龥';
    private static final char LAST_HINDI_UNICODE = 'ॿ';
    private static final char SPECIAL_LING = '〇';
    private static final String TAG = "ChinesePinyinConverter";
    private static HashMap sHyphenatedNamePolyPhoneMap = new HashMap();
    private static HashMap sLastNamePolyPhoneMap = new HashMap();
    private ChinesePinyinDictionary mDictionary;

    class BopomofoConvertor {
        private static final String[] sPinyinToZhuyin = {SupportedConfigFactory.CLOSE_BY_GROUP, "ㄅ", "p", "ㄆ", "m", "ㄇ", SupportedConfigFactory.CLOSE_BY_BOKEH, "ㄈ", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT, "ㄉ", "t", "ㄊ", "n", "ㄋ", SupportedConfigFactory.CLOSE_BY_RATIO, "ㄌ", SupportedConfigFactory.CLOSE_BY_HDR, "ㄍ", SupportedConfigFactory.CLOSE_BY_FILTER, "ㄎ", SupportedConfigFactory.CLOSE_BY_VIDEO, "ㄏ", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL, "ㄐ", "q", "ㄑ", "x", "ㄒ", "zh", "ㄓ", "ch", "ㄔ", "sh", "ㄕ", "r", "ㄖ", "z", "ㄗ", SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, "ㄘ", "s", "ㄙ", "y", "ㄧ", "w", "ㄨ", SupportedConfigFactory.CLOSE_BY_HHT, "ㄚ", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "ㄛ", "e", "ㄜ", SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE, "ㄧ", "u", "ㄨ", "v", "ㄩ", "ao", "ㄠ", "ai", "ㄞ", "an", "ㄢ", "ang", "ㄤ", "ou", "ㄡ", "ong", "ㄨㄥ", "ei", "ㄟ", "en", "ㄣ", "eng", "ㄥ", "er", "ㄦ", "ie", "ㄧㄝ", "iu", "ㄧㄡ", "in", "ㄧㄣ", "ing", "ㄧㄥ", "iong", "ㄩㄥ", "ui", "ㄨㄟ", "un", "ㄨㄣ", "ue", "ㄩㄝ", "ve", "ㄩㄝ", "van", "ㄩㄢ", "vn", "ㄩㄣ", "", "", "zhi", "ㄓ", "chi", "ㄔ", "shi", "ㄕ", "ri", "ㄖ", "zi", "ㄗ", "ci", "ㄘ", "si", "ㄙ", "yi", "ㄧ", "ye", "ㄧㄝ", "you", "ㄧㄡ", "yin", "ㄧㄣ", "ying", "ㄧㄥ", "wu", "ㄨ", "wei", "ㄨㄟ", "wen", "ㄨㄣ", "yu", "ㄩ", "yue", "ㄩㄝ", "yuan", "ㄩㄢ", "yun", "ㄩㄣ", "yong", "ㄩㄥ", "ju", "ㄐㄩ", "jue", "ㄐㄩㄝ", "juan", "ㄐㄩㄢ", "jun", "ㄐㄩㄣ", "qu", "ㄑㄩ", "que", "ㄑㄩㄝ", "quan", "ㄑㄩㄢ", "qun", "ㄑㄩㄣ", "xu", "ㄒㄩ", "xue", "ㄒㄩㄝ", "xuan", "ㄒㄩㄢ", "xun", "ㄒㄩㄣ"};
        private static final Node sRoot = build();

        class Node {
            public char ch;
            public Node children;
            public Node next;
            public String output;
            public boolean specials;

            private Node() {
            }
        }

        private BopomofoConvertor() {
        }

        private static Node build() {
            Node node = new Node();
            node.output = "";
            int i = 0;
            boolean z = false;
            while (true) {
                String[] strArr = sPinyinToZhuyin;
                if (i >= strArr.length) {
                    return node;
                }
                String str = strArr[i];
                if (str.length() == 0) {
                    z = true;
                } else {
                    int length = str.length();
                    Node node2 = node;
                    for (int i2 = 0; i2 < length; i2++) {
                        char lowerCase = Character.toLowerCase(str.charAt(i2));
                        Node node3 = node2.children;
                        while (node3 != null && node3.ch != lowerCase) {
                            node3 = node3.next;
                        }
                        if (node3 == null) {
                            node3 = new Node();
                            node3.ch = lowerCase;
                            node3.next = node2.children;
                            node2.children = node3;
                        }
                        node2 = node3;
                    }
                    node2.specials = z;
                    node2.output = sPinyinToZhuyin[i + 1];
                }
                i += 2;
            }
        }

        private static int convert(String str, int i, StringBuilder sb) {
            int length = str.length();
            int i2 = i;
            Node node = null;
            Node node2 = sRoot;
            int i3 = i2;
            while (i3 < length && node2 != null) {
                char lowerCase = Character.toLowerCase(str.charAt(i3));
                node2 = node2.children;
                while (node2 != null && node2.ch != lowerCase) {
                    node2 = node2.next;
                }
                if (!(node2 == null || node2.output == null || (node2.specials && i3 != length - 1))) {
                    i2 = i3 + 1;
                    node = node2;
                }
                i3++;
            }
            if (node != null) {
                sb.append(node.output);
            }
            return i2 - i;
        }

        public static CharSequence convert(String str, StringBuilder sb) {
            if (str == null || str.length() == 0) {
                return str;
            }
            int i = 0;
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.setLength(0);
            }
            int length = str.length();
            while (i != length) {
                int convert = convert(str, i, sb);
                if (convert <= 0) {
                    return "";
                }
                i += convert;
            }
            return sb;
        }
    }

    class ChinesePinyinDictionary {
        private static final String UNICODE_2_PINYIN_ASSET_NAME = "pinyinindex.idf";
        private Reader mReader;

        private ChinesePinyinDictionary() {
            Application currentApplication = AppConstants.getCurrentApplication();
            String str = UNICODE_2_PINYIN_ASSET_NAME;
            String directIndexedFilePath = DirectIndexedFileExtractor.getDirectIndexedFilePath(currentApplication, str);
            if (directIndexedFilePath != null && new File(directIndexedFilePath).exists()) {
                try {
                    this.mReader = DirectIndexedFile.open(directIndexedFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.mReader == null) {
                try {
                    this.mReader = DirectIndexedFile.open(AppConstants.getCurrentApplication().getResources().getAssets().open(str, 1));
                } catch (Exception unused) {
                    Log.e(ChinesePinyinConverter.TAG, "Init resource IOException");
                }
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            Reader reader = this.mReader;
            if (reader != null) {
                reader.close();
            }
            super.finalize();
        }

        public String[] getPinyinString(char c) {
            Reader reader = this.mReader;
            if (reader == null) {
                return null;
            }
            String str = (String) reader.get(0, c - 19968, 0);
            if (!TextUtils.isEmpty(str)) {
                return str.split(",");
            }
            Log.e(ChinesePinyinConverter.TAG, "The ChinesePinyinConverter dictionary is not correct, need rebuild or reset the ROM.");
            return null;
        }
    }

    public class Token {
        public static final int HINDI = 4;
        public static final int LATIN = 1;
        public static final int PINYIN = 2;
        private static final char SEPARATOR = ' ';
        public static final int UNKNOWN = 3;
        public String[] polyPhones;
        public String source;
        public String target;
        public int type;

        public Token() {
        }

        public Token(int i, String str, String str2) {
            this.type = i;
            this.source = str;
            this.target = str2;
        }
    }

    static {
        String str = "CHAN";
        String str2 = "YU";
        sHyphenatedNamePolyPhoneMap.put("单于", new String[]{str, str2});
        sHyphenatedNamePolyPhoneMap.put("长孙", new String[]{"ZHANG", "SUN"});
        String str3 = "JU";
        sHyphenatedNamePolyPhoneMap.put("子车", new String[]{"ZI", str3});
        sHyphenatedNamePolyPhoneMap.put("万俟", new String[]{"MO", "QI"});
        sHyphenatedNamePolyPhoneMap.put("澹台", new String[]{"TAN", "TAI"});
        String str4 = "CHI";
        sHyphenatedNamePolyPhoneMap.put("尉迟", new String[]{str2, str4});
        sLastNamePolyPhoneMap.put(Character.valueOf(20040), "YAO");
        sLastNamePolyPhoneMap.put(Character.valueOf(19969), "DING");
        sLastNamePolyPhoneMap.put(Character.valueOf(20446), str2);
        sLastNamePolyPhoneMap.put(Character.valueOf(36158), "JIA");
        sLastNamePolyPhoneMap.put(Character.valueOf(27784), "SHEN");
        sLastNamePolyPhoneMap.put(Character.valueOf(21340), "BU");
        String str5 = "BO";
        sLastNamePolyPhoneMap.put(Character.valueOf(34180), str5);
        sLastNamePolyPhoneMap.put(Character.valueOf(23387), str5);
        sLastNamePolyPhoneMap.put(Character.valueOf(36146), "BEN");
        sLastNamePolyPhoneMap.put(Character.valueOf(36153), "FEI");
        sLastNamePolyPhoneMap.put(Character.valueOf(27850), "BAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(33536), "BI");
        sLastNamePolyPhoneMap.put(Character.valueOf(35098), "CHU");
        HashMap hashMap = sLastNamePolyPhoneMap;
        Character valueOf = Character.valueOf(20256);
        String str6 = "CHUAN";
        hashMap.put(valueOf, str6);
        sLastNamePolyPhoneMap.put(Character.valueOf(21442), "CAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(21333), "SHAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(37079), str4);
        sLastNamePolyPhoneMap.put(Character.valueOf(38241), str);
        sLastNamePolyPhoneMap.put(Character.valueOf(26397), "CHAO");
        sLastNamePolyPhoneMap.put(Character.valueOf(21852), "CHUAI");
        sLastNamePolyPhoneMap.put(Character.valueOf(34928), "CUI");
        String str7 = "CHANG";
        sLastNamePolyPhoneMap.put(Character.valueOf(26216), str7);
        String str8 = "CHOU";
        sLastNamePolyPhoneMap.put(Character.valueOf(19985), str8);
        sLastNamePolyPhoneMap.put(Character.valueOf(30259), str8);
        sLastNamePolyPhoneMap.put(Character.valueOf(38271), str7);
        sLastNamePolyPhoneMap.put(Character.valueOf(36710), "CHE");
        sLastNamePolyPhoneMap.put(Character.valueOf(32735), "ZHAI");
        sLastNamePolyPhoneMap.put(Character.valueOf(20291), "DIAN");
        String str9 = "DIAO";
        sLastNamePolyPhoneMap.put(Character.valueOf(20992), str9);
        sLastNamePolyPhoneMap.put(Character.valueOf(35843), str9);
        sLastNamePolyPhoneMap.put(Character.valueOf(36934), "DI");
        sLastNamePolyPhoneMap.put(Character.valueOf(26123), "GUI");
        sLastNamePolyPhoneMap.put(Character.valueOf(33445), "GAI");
        sLastNamePolyPhoneMap.put(Character.valueOf(33554), "KUANG");
        sLastNamePolyPhoneMap.put(Character.valueOf(37063), "HUAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(24055), "XIANG");
        sLastNamePolyPhoneMap.put(Character.valueOf(25750), "HAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(35265), "JIAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(38477), "JIANG");
        String str10 = "JIAO";
        sLastNamePolyPhoneMap.put(Character.valueOf(35282), str10);
        sLastNamePolyPhoneMap.put(Character.valueOf(32564), str10);
        String str11 = "JI";
        sLastNamePolyPhoneMap.put(Character.valueOf(35760), str11);
        sLastNamePolyPhoneMap.put(Character.valueOf(29722), str3);
        sLastNamePolyPhoneMap.put(Character.valueOf(21095), str11);
        sLastNamePolyPhoneMap.put(Character.valueOf(38589), "JUAN");
        sLastNamePolyPhoneMap.put(Character.valueOf(38551), "KUI");
        sLastNamePolyPhoneMap.put(Character.valueOf(38752), "KU");
        sLastNamePolyPhoneMap.put(Character.valueOf(20048), "YUE");
        sLastNamePolyPhoneMap.put(Character.valueOf(21895), "LA");
        sLastNamePolyPhoneMap.put(Character.valueOf(38610), "LUO");
        sLastNamePolyPhoneMap.put(Character.valueOf(20102), "LIAO");
        String str12 = "MIAO";
        sLastNamePolyPhoneMap.put(Character.valueOf(32554), str12);
        sLastNamePolyPhoneMap.put(Character.valueOf(20340), "MI");
        sLastNamePolyPhoneMap.put(Character.valueOf(35884), str12);
        sLastNamePolyPhoneMap.put(Character.valueOf(20060), "NIE");
        sLastNamePolyPhoneMap.put(Character.valueOf(36898), "PANG");
        sLastNamePolyPhoneMap.put(Character.valueOf(34028), "PENG");
        sLastNamePolyPhoneMap.put(Character.valueOf(26420), "PIAO");
        sLastNamePolyPhoneMap.put(Character.valueOf(20167), "QIU");
        sLastNamePolyPhoneMap.put(Character.valueOf(35203), "QIN");
        sLastNamePolyPhoneMap.put(Character.valueOf(30655), "QU");
        sLastNamePolyPhoneMap.put(Character.valueOf(20160), "SHI");
        sLastNamePolyPhoneMap.put(Character.valueOf(25240), "SHE");
        sLastNamePolyPhoneMap.put(Character.valueOf(30509), "SUI");
        sLastNamePolyPhoneMap.put(Character.valueOf(35299), "XIE");
        sLastNamePolyPhoneMap.put(Character.valueOf(31995), "XI");
        sLastNamePolyPhoneMap.put(Character.valueOf(38500), "XU");
        String str13 = "YUAN";
        sLastNamePolyPhoneMap.put(Character.valueOf(21592), str13);
        sLastNamePolyPhoneMap.put(Character.valueOf(36128), str13);
        sLastNamePolyPhoneMap.put(Character.valueOf(26366), "ZENG");
        sLastNamePolyPhoneMap.put(Character.valueOf(26597), "ZHA");
        sLastNamePolyPhoneMap.put(valueOf, str6);
        sLastNamePolyPhoneMap.put(Character.valueOf(21484), "SHAO");
        sLastNamePolyPhoneMap.put(Character.valueOf(37325), "chong");
        sLastNamePolyPhoneMap.put(Character.valueOf(21306), "OU");
        sLastNamePolyPhoneMap.put(Character.valueOf(26044), str2);
        sLastNamePolyPhoneMap.put(Character.valueOf(31181), "CHONG");
    }

    private ChinesePinyinConverter() {
        this.mDictionary = new ChinesePinyinDictionary();
    }

    private void addToken(StringBuilder sb, ArrayList arrayList, int i) {
        String sb2 = sb.toString();
        Token token = new Token(i, sb2, sb2);
        if (4 == i) {
            String[] hindiToPinyins = HindiPinyinConverter.getInstance().hindiToPinyins(token.source);
            if (hindiToPinyins.length > 0) {
                if (hindiToPinyins.length == 1) {
                    token.target = hindiToPinyins[0];
                    hindiToPinyins = new String[]{token.target};
                } else {
                    token.target = hindiToPinyins[0];
                }
                token.polyPhones = hindiToPinyins;
            }
        }
        arrayList.add(token);
        sb.setLength(0);
    }

    public static ChinesePinyinConverter getInstance() {
        return (ChinesePinyinConverter) INSTANCE.get();
    }

    private ArrayList getPolyPhoneLastNameTokens(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (str.length() >= 2) {
            String substring = str.substring(0, 2);
            String[] strArr = (String[]) sHyphenatedNamePolyPhoneMap.get(substring);
            if (strArr != null) {
                for (int i = 0; i < strArr.length; i++) {
                    Token token = new Token();
                    token.type = 2;
                    token.source = String.valueOf(substring.charAt(i));
                    token.target = strArr[i];
                    arrayList.add(token);
                }
                return arrayList;
            }
        }
        Character valueOf = Character.valueOf(str.charAt(0));
        String str2 = (String) sLastNamePolyPhoneMap.get(valueOf);
        if (str2 == null) {
            return null;
        }
        Token token2 = new Token();
        token2.type = 2;
        token2.source = valueOf.toString();
        token2.target = str2;
        arrayList.add(token2);
        return arrayList;
    }

    public CharSequence convertPinyin2Bopomofo(String str, StringBuilder sb) {
        return BopomofoConvertor.convert(str, sb);
    }

    public ArrayList get(String str) {
        return get(str, true, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0132  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ArrayList get(String str, boolean z, boolean z2) {
        int i;
        int length;
        StringBuilder sb;
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(str)) {
            return arrayList;
        }
        if (!z2) {
            ArrayList polyPhoneLastNameTokens = getPolyPhoneLastNameTokens(str);
            if (polyPhoneLastNameTokens != null && polyPhoneLastNameTokens.size() > 0) {
                arrayList.addAll(polyPhoneLastNameTokens);
                i = polyPhoneLastNameTokens.size();
                length = str.length();
                sb = new StringBuilder();
                int i2 = 1;
                while (i < length) {
                    char charAt = str.charAt(i);
                    int i3 = 2;
                    if (charAt == ' ') {
                        if (sb.length() > 0) {
                            addToken(sb, arrayList, i2);
                        }
                        if (!z) {
                            String valueOf = String.valueOf(' ');
                            arrayList.add(new Token(3, valueOf, valueOf));
                        }
                    } else {
                        if (charAt < 256) {
                            if (i > 0) {
                                char charAt2 = str.charAt(i - 1);
                                boolean z3 = charAt2 >= '0' && charAt2 <= '9';
                                boolean z4 = charAt >= '0' && charAt <= '9';
                                if (z3 != z4 && sb.length() > 0) {
                                    addToken(sb, arrayList, i2);
                                }
                            }
                            if (i2 != 1 && sb.length() > 0) {
                                addToken(sb, arrayList, i2);
                            }
                            sb.append(charAt);
                            i2 = 1;
                        } else {
                            if (charAt == 12295) {
                                Token token = new Token();
                                token.type = 2;
                                token.target = "ling";
                                if (sb.length() > 0) {
                                    addToken(sb, arrayList, i2);
                                }
                                arrayList.add(token);
                            } else if (charAt >= 19968 && charAt <= 40869) {
                                String[] pinyinString = this.mDictionary.getPinyinString(charAt);
                                Token token2 = new Token();
                                token2.source = Character.toString(charAt);
                                if (pinyinString == null) {
                                    token2.type = 3;
                                    token2.target = Character.toString(charAt);
                                } else {
                                    token2.type = 2;
                                    token2.target = pinyinString[0];
                                    if (pinyinString.length > 1) {
                                        token2.polyPhones = pinyinString;
                                    }
                                }
                                int i4 = token2.type;
                                if (i4 == 2) {
                                    if (sb.length() > 0) {
                                        addToken(sb, arrayList, i2);
                                    }
                                    arrayList.add(token2);
                                } else {
                                    if (i2 != i4 && sb.length() > 0) {
                                        addToken(sb, arrayList, i2);
                                    }
                                    i3 = token2.type;
                                    sb.append(charAt);
                                }
                            } else if (charAt < 2304 || charAt > 2431) {
                                if (i2 != 3 && sb.length() > 0) {
                                    addToken(sb, arrayList, i2);
                                }
                                sb.append(charAt);
                            } else {
                                if (i2 != 4 && sb.length() > 0) {
                                    addToken(sb, arrayList, i2);
                                }
                                sb.append(charAt);
                                i2 = 4;
                            }
                            i2 = i3;
                        }
                        i++;
                    }
                    i2 = 3;
                    i++;
                }
                if (sb.length() > 0) {
                    addToken(sb, arrayList, i2);
                }
                return arrayList;
            }
        }
        i = 0;
        length = str.length();
        sb = new StringBuilder();
        int i22 = 1;
        while (i < length) {
        }
        if (sb.length() > 0) {
        }
        return arrayList;
    }

    public boolean isChinesePinyinKnown(char c) {
        return c >= 19968 && c <= 40869;
    }
}
