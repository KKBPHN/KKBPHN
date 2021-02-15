package miui.text;

import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.Iterator;
import miui.text.ChinesePinyinConverter.Token;
import miui.util.SoftReferenceSingleton;

public class SortKeyGenerator {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public SortKeyGenerator createInstance() {
            return new SortKeyGenerator();
        }
    };
    private BaseGenerator[] mUtils;

    class BaseGenerator {
        private BaseGenerator() {
        }

        public String getSortKey(String str) {
            return str;
        }
    }

    class ChineseLocaleUtils extends BaseGenerator {
        private ChineseLocaleUtils() {
            super();
        }

        public String getSortKey(String str) {
            ArrayList arrayList = ChinesePinyinConverter.getInstance().get(str);
            if (arrayList == null || arrayList.size() <= 0) {
                super.getSortKey(str);
                return str;
            }
            StringBuilder sb = new StringBuilder();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Token token = (Token) it.next();
                if (2 == token.type) {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(token.target.charAt(0));
                    sb.append("   ");
                    if (token.target.length() > 1) {
                        sb.append(token.target.substring(1));
                    }
                } else if (sb.length() <= 0) {
                    sb.append(token.source);
                }
                sb.append(' ');
                sb.append(token.source);
            }
            return sb.toString();
        }
    }

    enum NameStyle {
        UNDEFINED,
        WESTERN,
        CJK,
        CHINESE,
        JAPANESE,
        KOREAN;

        private static NameStyle guessCJKNameStyle(String str, int i) {
            int length = str.length();
            while (i < length) {
                int codePointAt = Character.codePointAt(str, i);
                if (Character.isLetter(codePointAt)) {
                    UnicodeBlock of = UnicodeBlock.of(codePointAt);
                    if (isJapanesePhoneticUnicodeBlock(of)) {
                        return JAPANESE;
                    }
                    if (isKoreanUnicodeBlock(of)) {
                        return KOREAN;
                    }
                    if (isChineseUnicode(Character.toChars(codePointAt))) {
                        return CHINESE;
                    }
                }
                i += Character.charCount(codePointAt);
            }
            return CJK;
        }

        public static NameStyle guessFullNameStyle(String str) {
            if (str == null) {
                return UNDEFINED;
            }
            NameStyle nameStyle = UNDEFINED;
            int length = str.length();
            int i = 0;
            while (i < length) {
                int codePointAt = Character.codePointAt(str, i);
                if (Character.isLetter(codePointAt)) {
                    UnicodeBlock of = UnicodeBlock.of(codePointAt);
                    if (!isLatinUnicodeBlock(of)) {
                        if (isCJKUnicodeBlock(of)) {
                            return guessCJKNameStyle(str, i);
                        }
                        if (isJapanesePhoneticUnicodeBlock(of)) {
                            return JAPANESE;
                        }
                        if (isKoreanUnicodeBlock(of)) {
                            return KOREAN;
                        }
                    }
                    nameStyle = WESTERN;
                }
                i += Character.charCount(codePointAt);
            }
            return nameStyle;
        }

        private static boolean isCJKUnicodeBlock(UnicodeBlock unicodeBlock) {
            return unicodeBlock == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || unicodeBlock == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || unicodeBlock == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || unicodeBlock == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || unicodeBlock == UnicodeBlock.CJK_RADICALS_SUPPLEMENT || unicodeBlock == UnicodeBlock.CJK_COMPATIBILITY || unicodeBlock == UnicodeBlock.CJK_COMPATIBILITY_FORMS || unicodeBlock == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || unicodeBlock == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
        }

        private static boolean isChineseUnicode(char[] cArr) {
            for (char isChinesePinyinKnown : cArr) {
                if (ChinesePinyinConverter.getInstance().isChinesePinyinKnown(isChinesePinyinKnown)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isJapanesePhoneticUnicodeBlock(UnicodeBlock unicodeBlock) {
            return unicodeBlock == UnicodeBlock.KATAKANA || unicodeBlock == UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS || unicodeBlock == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || unicodeBlock == UnicodeBlock.HIRAGANA;
        }

        private static boolean isKoreanUnicodeBlock(UnicodeBlock unicodeBlock) {
            return unicodeBlock == UnicodeBlock.HANGUL_SYLLABLES || unicodeBlock == UnicodeBlock.HANGUL_JAMO || unicodeBlock == UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
        }

        private static boolean isLatinUnicodeBlock(UnicodeBlock unicodeBlock) {
            return unicodeBlock == UnicodeBlock.BASIC_LATIN || unicodeBlock == UnicodeBlock.LATIN_1_SUPPLEMENT || unicodeBlock == UnicodeBlock.LATIN_EXTENDED_A || unicodeBlock == UnicodeBlock.LATIN_EXTENDED_B || unicodeBlock == UnicodeBlock.LATIN_EXTENDED_ADDITIONAL;
        }
    }

    private SortKeyGenerator() {
        this.mUtils = new BaseGenerator[NameStyle.values().length];
        this.mUtils[NameStyle.UNDEFINED.ordinal()] = new BaseGenerator();
    }

    private synchronized BaseGenerator getForSort(NameStyle nameStyle) {
        BaseGenerator baseGenerator;
        baseGenerator = this.mUtils[nameStyle.ordinal()];
        if (baseGenerator == null && nameStyle == NameStyle.CHINESE) {
            baseGenerator = new ChineseLocaleUtils();
            this.mUtils[nameStyle.ordinal()] = baseGenerator;
        }
        if (baseGenerator == null) {
            baseGenerator = this.mUtils[NameStyle.UNDEFINED.ordinal()];
        }
        return baseGenerator;
    }

    public static SortKeyGenerator getInstance() {
        return (SortKeyGenerator) INSTANCE.get();
    }

    public String getSortKey(String str) {
        return getForSort(NameStyle.guessFullNameStyle(str)).getSortKey(str);
    }
}
