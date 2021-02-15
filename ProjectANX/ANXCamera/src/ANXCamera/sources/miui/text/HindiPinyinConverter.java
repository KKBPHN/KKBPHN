package miui.text;

import android.util.Log;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.xiaomi.stat.d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import miui.util.Pools;
import miui.util.Pools.Manager;
import miui.util.Pools.SimplePool;
import miui.util.SoftReferenceSingleton;

public class HindiPinyinConverter {
    private static final boolean DEBUG = false;
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public HindiPinyinConverter createInstance() {
            return new HindiPinyinConverter();
        }
    };
    private static final int NUM_ADDITIONAL_CONSONANTS = 8;
    private static final int NUM_CONSONANTS = 35;
    private static final int NUM_DEPENDENT_VOWELS = 15;
    private static final int NUM_INDEPENDENT_VOWELS = 12;
    private static final int NUM_VARIOUS_SIGN = 3;
    private static final String TAG = "HindiPinyinConverter";
    private static SimplePool sArrayList = Pools.createSimplePool(new Manager() {
        public ArrayList createInstance() {
            return new ArrayList();
        }

        public void onRelease(ArrayList arrayList) {
            arrayList.clear();
        }
    }, 4);
    private static final String sConsonantSyllableEnding = "्";
    private static SimplePool sStringBuilder = Pools.createSimplePool(new Manager() {
        public StringBuilder createInstance() {
            return new StringBuilder();
        }

        public void onRelease(StringBuilder sb) {
            sb.setLength(0);
        }
    }, 4);
    private String[] mAdditionalConsonantPinyins;
    private String[] mAdditionalConsonantUnicodes;
    private String[] mConsonantPinyins;
    private String[] mConsonantUnicodes;
    private String[] mDependentVowelSignPinyins;
    private String[] mDependentVowelSignUnicodes;
    private HashMap mDoubleCharacters;
    private String[] mIndependentVowelPinyins;
    private String[] mIndependentVowelUnicodes;
    private HashMap mSingleCharacters;
    private HashMap mTripleCharacters;
    private String[] mVariousSignPinyins;
    private String[] mVariousSignUnicodes;

    private HindiPinyinConverter() {
        initUnicodeAndPinyin();
        mapUnicodeToPinyin();
    }

    private static String concat(String... strArr) {
        StringBuilder sb = (StringBuilder) sStringBuilder.acquire();
        for (String append : strArr) {
            sb.append(append);
        }
        String sb2 = sb.toString();
        sStringBuilder.release(sb);
        return sb2;
    }

    public static HindiPinyinConverter getInstance() {
        return (HindiPinyinConverter) INSTANCE.get();
    }

    private void initUnicodeAndPinyin() {
        this.mIndependentVowelUnicodes = new String[12];
        this.mIndependentVowelPinyins = new String[12];
        this.mDependentVowelSignUnicodes = new String[15];
        this.mDependentVowelSignPinyins = new String[15];
        this.mConsonantUnicodes = new String[35];
        this.mConsonantPinyins = new String[35];
        this.mAdditionalConsonantUnicodes = new String[8];
        this.mAdditionalConsonantPinyins = new String[8];
        this.mVariousSignUnicodes = new String[3];
        this.mVariousSignPinyins = new String[3];
        this.mSingleCharacters = new HashMap();
        this.mDoubleCharacters = new HashMap();
        this.mTripleCharacters = new HashMap();
        String[] strArr = this.mIndependentVowelUnicodes;
        strArr[0] = "अ";
        strArr[1] = "आ";
        strArr[2] = "इ";
        strArr[3] = "ई";
        strArr[4] = "उ";
        strArr[5] = "ऊ";
        strArr[6] = "ऋ";
        strArr[7] = "ए";
        strArr[8] = "ऐ";
        strArr[9] = "ऑ";
        strArr[10] = "ओ";
        strArr[11] = "औ";
        String[] strArr2 = this.mIndependentVowelPinyins;
        strArr2[0] = SupportedConfigFactory.CLOSE_BY_HHT;
        strArr2[1] = d.O;
        strArr2[2] = SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE;
        strArr2[3] = "ee";
        strArr2[4] = "u";
        strArr2[5] = "oo";
        strArr2[6] = "r";
        String str = "e";
        strArr2[7] = str;
        strArr2[8] = "ai";
        strArr2[9] = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT;
        strArr2[10] = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT;
        strArr2[11] = "au";
        String[] strArr3 = this.mDependentVowelSignUnicodes;
        strArr3[0] = "ा";
        strArr3[1] = "ि";
        strArr3[2] = "ी";
        strArr3[3] = "ु";
        strArr3[4] = "ू";
        strArr3[5] = "ृ";
        strArr3[6] = "ॄ";
        strArr3[7] = "ॅ";
        strArr3[8] = "े";
        strArr3[9] = "ै";
        strArr3[10] = "ॉ";
        strArr3[11] = "ो";
        strArr3[12] = "ौ";
        strArr3[13] = "ॎ";
        strArr3[14] = "ॏ";
        String[] strArr4 = this.mDependentVowelSignPinyins;
        strArr4[0] = d.O;
        strArr4[1] = SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE;
        strArr4[2] = "ee";
        strArr4[3] = "u";
        strArr4[4] = "oo";
        strArr4[5] = "r";
        strArr4[6] = "R";
        strArr4[7] = str;
        strArr4[8] = str;
        strArr4[9] = "ai";
        strArr4[10] = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT;
        strArr4[11] = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT;
        strArr4[12] = "au";
        strArr4[13] = str;
        strArr4[14] = "aw";
        String[] strArr5 = this.mConsonantUnicodes;
        strArr5[0] = "क";
        strArr5[1] = "ख";
        strArr5[2] = "ग";
        strArr5[3] = "घ";
        strArr5[4] = "ङ";
        strArr5[5] = "च";
        strArr5[6] = "छ";
        strArr5[7] = "ज";
        strArr5[8] = "झ";
        strArr5[9] = "ञ";
        strArr5[10] = "ट";
        strArr5[11] = "ठ";
        strArr5[12] = "ड";
        strArr5[13] = "ढ";
        strArr5[14] = "ण";
        strArr5[15] = "त";
        strArr5[16] = "थ";
        strArr5[17] = "द";
        strArr5[18] = "ध";
        strArr5[19] = "न";
        strArr5[20] = "ऩ";
        strArr5[21] = "प";
        strArr5[22] = "फ";
        strArr5[23] = "ब";
        strArr5[24] = "भ";
        strArr5[25] = "म";
        strArr5[26] = "य";
        strArr5[27] = "र";
        strArr5[28] = "ऱ";
        strArr5[29] = "ल";
        strArr5[30] = "व";
        strArr5[31] = "श";
        strArr5[32] = "ष";
        strArr5[33] = "स";
        strArr5[34] = "ह";
        String[] strArr6 = this.mConsonantPinyins;
        strArr6[0] = SupportedConfigFactory.CLOSE_BY_FILTER;
        strArr6[1] = "kh";
        strArr6[2] = SupportedConfigFactory.CLOSE_BY_HDR;
        strArr6[3] = "gh";
        strArr6[4] = "ng";
        strArr6[5] = SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION;
        strArr6[6] = "ch";
        strArr6[7] = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL;
        strArr6[8] = "jh";
        strArr6[9] = "ny";
        strArr6[10] = "T";
        strArr6[11] = "Th";
        strArr6[12] = "D";
        strArr6[13] = "Dh";
        strArr6[14] = "N";
        strArr6[15] = "t";
        strArr6[16] = "th";
        strArr6[17] = SupportedConfigFactory.CLOSE_BY_BURST_SHOOT;
        strArr6[18] = "dh";
        strArr6[19] = "n";
        strArr6[20] = "Nn";
        strArr6[21] = "p";
        strArr6[22] = "ph";
        strArr6[23] = SupportedConfigFactory.CLOSE_BY_GROUP;
        strArr6[24] = "bh";
        strArr6[25] = "m";
        strArr6[26] = "y";
        strArr6[27] = "r";
        strArr6[28] = "R";
        strArr6[29] = SupportedConfigFactory.CLOSE_BY_RATIO;
        strArr6[30] = "v";
        strArr6[31] = "sh";
        strArr6[32] = "S";
        strArr6[33] = "s";
        strArr6[34] = SupportedConfigFactory.CLOSE_BY_VIDEO;
        String[] strArr7 = this.mAdditionalConsonantUnicodes;
        strArr7[0] = "क़";
        strArr7[1] = "ख़";
        strArr7[2] = "ग़";
        strArr7[3] = "ज़";
        strArr7[4] = "ड़";
        strArr7[5] = "ढ़";
        strArr7[6] = "फ़";
        strArr7[7] = "य़";
        String[] strArr8 = this.mAdditionalConsonantPinyins;
        strArr8[0] = "q";
        strArr8[1] = "khh";
        strArr8[2] = "ghh";
        strArr8[3] = "z";
        strArr8[4] = "Ddh";
        strArr8[5] = "rh";
        strArr8[6] = SupportedConfigFactory.CLOSE_BY_BOKEH;
        strArr8[7] = "Y";
        String[] strArr9 = this.mVariousSignUnicodes;
        strArr9[0] = "ँ";
        strArr9[1] = "ं";
        strArr9[2] = "ः";
        String[] strArr10 = this.mVariousSignPinyins;
        strArr10[0] = "an";
        strArr10[1] = "an";
        strArr10[2] = "ah";
    }

    private void mapUnicodeToPinyin() {
        for (int i = 0; i < 35; i++) {
            String str = this.mConsonantUnicodes[i];
            StringBuilder sb = new StringBuilder();
            sb.append(this.mConsonantPinyins[i]);
            sb.append(SupportedConfigFactory.CLOSE_BY_HHT);
            this.mSingleCharacters.put(str, sb.toString());
        }
        for (int i2 = 0; i2 < 12; i2++) {
            this.mSingleCharacters.put(this.mIndependentVowelUnicodes[i2], this.mIndependentVowelPinyins[i2]);
        }
        for (int i3 = 0; i3 < 8; i3++) {
            this.mSingleCharacters.put(this.mAdditionalConsonantUnicodes[i3], this.mAdditionalConsonantPinyins[i3]);
        }
        for (int i4 = 0; i4 < 35; i4++) {
            for (int i5 = 0; i5 < 15; i5++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mConsonantUnicodes[i4]);
                sb2.append(this.mDependentVowelSignUnicodes[i5]);
                String sb3 = sb2.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.mConsonantPinyins[i4]);
                sb4.append(this.mDependentVowelSignPinyins[i5]);
                this.mDoubleCharacters.put(sb3, sb4.toString());
            }
            for (int i6 = 0; i6 < 3; i6++) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.mConsonantUnicodes[i4]);
                sb5.append(this.mVariousSignUnicodes[i6]);
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append(this.mConsonantPinyins[i4]);
                sb7.append(this.mVariousSignPinyins[i6]);
                this.mDoubleCharacters.put(sb6, sb7.toString());
            }
        }
        for (int i7 = 0; i7 < 8; i7++) {
            for (int i8 = 0; i8 < 15; i8++) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(this.mAdditionalConsonantUnicodes[i7]);
                sb8.append(this.mDependentVowelSignUnicodes[i8]);
                String sb9 = sb8.toString();
                StringBuilder sb10 = new StringBuilder();
                sb10.append(this.mAdditionalConsonantPinyins[i7]);
                sb10.append(this.mDependentVowelSignPinyins[i8]);
                this.mDoubleCharacters.put(sb9, sb10.toString());
            }
            for (int i9 = 0; i9 < 3; i9++) {
                StringBuilder sb11 = new StringBuilder();
                sb11.append(this.mConsonantUnicodes[i7]);
                sb11.append(this.mVariousSignUnicodes[i9]);
                String sb12 = sb11.toString();
                StringBuilder sb13 = new StringBuilder();
                sb13.append(this.mConsonantPinyins[i7]);
                sb13.append(this.mVariousSignPinyins[i9]);
                this.mDoubleCharacters.put(sb12, sb13.toString());
            }
        }
        for (int i10 = 0; i10 < 35; i10++) {
            StringBuilder sb14 = new StringBuilder();
            sb14.append(this.mConsonantUnicodes[i10]);
            sb14.append(sConsonantSyllableEnding);
            this.mDoubleCharacters.put(sb14.toString(), this.mConsonantPinyins[i10]);
        }
        for (int i11 = 0; i11 < 12; i11++) {
        }
        for (int i12 = 0; i12 < 35; i12++) {
            for (int i13 = 0; i13 < 15; i13++) {
                for (int i14 = 0; i14 < 3; i14++) {
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append(this.mConsonantUnicodes[i12]);
                    sb15.append(this.mDependentVowelSignUnicodes[i13]);
                    sb15.append(this.mVariousSignUnicodes[i14]);
                    String sb16 = sb15.toString();
                    StringBuilder sb17 = new StringBuilder();
                    sb17.append(this.mConsonantPinyins[i12]);
                    sb17.append(this.mDependentVowelSignPinyins[i13]);
                    sb17.append(this.mVariousSignPinyins[i14].substring(1));
                    this.mTripleCharacters.put(sb16, sb17.toString());
                }
            }
        }
        this.mDoubleCharacters.put("अं", "am");
    }

    private static String stringToUnicode(String str) {
        StringBuilder sb = (StringBuilder) sStringBuilder.acquire();
        int length = str.length();
        int i = 0;
        while (i < length) {
            int codePointAt = Character.codePointAt(str, i);
            int charCount = Character.charCount(codePointAt);
            if (charCount > 1) {
                i += charCount - 1;
            }
            if (codePointAt < 128) {
                sb.appendCodePoint(codePointAt);
            } else {
                sb.append(String.format("\\u%04x", new Object[]{Integer.valueOf(codePointAt)}));
            }
            i++;
        }
        String sb2 = sb.toString();
        sStringBuilder.release(sb);
        return sb2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0048, code lost:
        if (r0.mSingleCharacters.containsKey(r7) != false) goto L_0x004a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String hindiToPinyin(String str) {
        String str2;
        int i;
        String str3;
        String str4 = str;
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder sb = (StringBuilder) sStringBuilder.acquire();
        int length = str.length();
        int i2 = 0;
        while (true) {
            String str5 = TAG;
            if (i2 < length) {
                int charCount = Character.charCount(Character.codePointAt(str4, i2));
                int i3 = i2 + charCount;
                String substring = str4.substring(i2, i3);
                String str6 = "";
                if (i3 < length) {
                    charCount = Character.charCount(Character.codePointAt(str4, i3));
                    str2 = str4.substring(i3, i3 + charCount);
                } else {
                    str2 = str6;
                }
                if (!str2.isEmpty()) {
                    int i4 = i3 + charCount;
                    if (i4 < length) {
                        charCount = Character.charCount(Character.codePointAt(str4, i4));
                        str3 = str4.substring(i4, i4 + charCount);
                    } else {
                        str3 = str6;
                    }
                    if (str3.isEmpty()) {
                        String concat = concat(substring, str2);
                        if (this.mDoubleCharacters.containsKey(concat)) {
                            str6 = (String) this.mDoubleCharacters.get(concat);
                            i3 += charCount;
                        } else if (!this.mSingleCharacters.containsKey(substring)) {
                            Log.w(str5, String.format("Ignore unknown hindi: %s%s%s %s", new Object[]{substring, str2, str3, stringToUnicode(concat(substring, str2, str3))}));
                        }
                        i = length;
                        i2 = i3;
                        sb.append(str6);
                        str4 = str;
                        length = i;
                    } else {
                        String concat2 = concat(substring, str2);
                        i = length;
                        String concat3 = concat(concat2, str3);
                        if (this.mTripleCharacters.containsKey(concat3)) {
                            str6 = (String) this.mTripleCharacters.get(concat3);
                            i2 = i4 + charCount;
                            sb.append(str6);
                            str4 = str;
                            length = i;
                        } else {
                            if (this.mDoubleCharacters.containsKey(concat2)) {
                                str6 = (String) this.mDoubleCharacters.get(concat2);
                                i3 += charCount;
                            } else if (this.mSingleCharacters.containsKey(substring)) {
                                str6 = (String) this.mSingleCharacters.get(substring);
                            } else {
                                Log.w(str5, String.format("Ignore unknown hindi: '%s%s%s' '%s'", new Object[]{substring, str2, str3, stringToUnicode(concat(substring, str2, str3))}));
                            }
                            i2 = i3;
                            sb.append(str6);
                            str4 = str;
                            length = i;
                        }
                    }
                }
                str6 = (String) this.mSingleCharacters.get(substring);
                i = length;
                i2 = i3;
                sb.append(str6);
                str4 = str;
                length = i;
            } else {
                String sb2 = sb.toString();
                sStringBuilder.release(sb);
                Log.d(str5, String.format("hindiToPinyin(): using time %d ms", new Object[]{Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
                return sb2;
            }
        }
    }

    public String[] hindiToPinyins(String str) {
        ArrayList arrayList = (ArrayList) sArrayList.acquire();
        String hindiToPinyin = hindiToPinyin(str);
        arrayList.add(hindiToPinyin);
        String str2 = "ee";
        if (hindiToPinyin.contains(str2)) {
            arrayList.add(hindiToPinyin.replaceAll(str2, "ii"));
        }
        String str3 = "oo";
        if (hindiToPinyin.contains(str3)) {
            arrayList.add(hindiToPinyin.replaceAll(str3, "uu"));
        }
        String str4 = "v";
        if (hindiToPinyin.contains(str4)) {
            arrayList.add(hindiToPinyin.replaceAll(str4, "w"));
        }
        ArrayList arrayList2 = (ArrayList) sArrayList.acquire();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str5 = (String) it.next();
            if (!str5.endsWith(d.O) && str5.endsWith(SupportedConfigFactory.CLOSE_BY_HHT)) {
                arrayList2.add(str5.substring(0, str5.length() - 1));
            }
        }
        arrayList.addAll(arrayList2);
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        sArrayList.release(arrayList);
        sArrayList.release(arrayList2);
        return strArr;
    }
}
