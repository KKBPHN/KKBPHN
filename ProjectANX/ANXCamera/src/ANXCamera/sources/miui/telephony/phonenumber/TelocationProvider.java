package miui.telephony.phonenumber;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.R;
import com.miui.internal.util.DirectIndexedFileExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import miui.util.AppConstants;
import miui.util.DirectIndexedFile;
import miui.util.DirectIndexedFile.Reader;

public class TelocationProvider extends ContentProvider {
    public static final int AREACODE_INDEX = 1;
    public static final String AUTHORITY = "com.miui.core.telocation";
    private static final int COMMON_OPERATOR_INDEX_LENGTH = 3;
    private static final String DATA_ASSET_NAME = "telocation.idf";
    public static final String EMPTY = "";
    private static final int IOT_NUMBER_LENGTH = 13;
    private static final int IOT_OPERATOR_INDEX_LENGTH = 5;
    public static final int LOCATION_INDEX = 0;
    public static final int LOCATION_KIND = 0;
    private static final int LONG_UNIQID_LENGTH = 8;
    private static final int NORMAL_UNIQID_LENGTH = 7;
    private static final String OPERATORS_DATA_ASSET_NAME = "operators.dat";
    private static final String TAG = "TelocationProvider";
    private static final String UNIQID_LEN8_NUMBER_ASSET_NAME = "tel_uniqid_len8.dat";
    public static final int UNIQUE_ID_NONE = 0;
    private static final int VIRTUAL_OPERATOR_INDEX_LENGTH = 4;
    private static final HashSet VIRTUAL_OPERATOR_PREFIXES = new HashSet();
    private static final String XIAOMI_DATA_ASSET_NAME = "xiaomi_mobile.dat";
    private static final int XIAOMI_OPERATOR_INDEX_LENGTH = 7;
    private static UriMatcher sUriMatcher = new UriMatcher(-1);
    private final ConcurrentHashMap mAreaCodeTelocations = new ConcurrentHashMap();
    private Reader mDensityIndexFileReader;
    /* access modifiers changed from: private */
    public final HashMap mOperatorsMap = new HashMap();
    /* access modifiers changed from: private */
    public final HashSet mUniqIdLen8Numbers = new HashSet();
    /* access modifiers changed from: private */
    public final HashSet mXiaomiMobileSet = new HashSet();

    public class Contract {
        public static final String[] COLUMNS = {"_id", "location", "query_area_code", "parse_area_code", Column.OPERATOR, "version", Column.UNIQ_ID, "query_area_code_by_address"};
        public static final String PARSE_AREA_CODE = "parse_area_code";
        public static final String QUERY_AREA_CODE = "query_area_code";
        public static final String QUERY_AREA_CODE_BY_ADDRESS = "query_area_code_by_address";
        public static final String QUERY_LOCATION = "query_location";
        public static final String QUERY_OPERATOR = "query_operator";
        public static final String QUERY_UNIQ_ID = "query_uniq_id";
        public static final String QUERY_VERSION = "query_version";
        public static final String RELOAD = "reload";
        public static final int URI_PARSE_AREA_CODE = 4;
        public static final int URI_QUERY_AREA_CODE = 3;
        public static final int URI_QUERY_AREA_CODE_BY_ADDRESS = 8;
        public static final int URI_QUERY_LOCATION = 2;
        public static final int URI_QUERY_OPERATOR = 5;
        public static final int URI_QUERY_UNIQ_ID = 7;
        public static final int URI_QUERY_VERSION = 6;
        public static final int URI_RELOAD = 1;

        public class Column {
            public static final int INDEX_ID = 0;
            public static final int INDEX_LOCATION = 1;
            public static final int INDEX_OPERATOR = 4;
            public static final int INDEX_PARSE_AREA_CODE = 3;
            public static final int INDEX_QUERY_AREA_CODE = 2;
            public static final int INDEX_QUERY_AREA_CODE_BY_ADDRESS = 7;
            public static final int INDEX_UNIQ_ID = 6;
            public static final int INDEX_VERSION = 5;
            public static final String LOCATION = "location";
            public static final String OPERATOR = "operator";
            public static final String PARSE_AREA_CODE = "parse_area_code";
            public static final String QUERY_AREA_CODE = "query_area_code";
            public static final String QUERY_AREA_CODE_BY_ADDRESS = "query_area_code_by_address";
            public static final String UNIQ_ID = "uniq_id";
            public static final String VERSION = "version";
            public static final String _ID = "_id";
        }

        public class Params {
            public static final String ADMIN_AREA = "adminArea";
            public static final String ENABLE_MOBILE = "enable_mobile";
            public static final String LENGTH = "length";
            public static final String LOCALITY = "locality";
            public static final String NUMBER = "number";
            public static final String START = "start";
            public static final String WITH_AREA_CODE = "with_area_code";
        }
    }

    interface DatFileRawReadListener {
        void onReadRaw(String[] strArr);
    }

    static {
        VIRTUAL_OPERATOR_PREFIXES.add("170");
        VIRTUAL_OPERATOR_PREFIXES.add("171");
        VIRTUAL_OPERATOR_PREFIXES.add("167");
        VIRTUAL_OPERATOR_PREFIXES.add("162");
        UriMatcher uriMatcher = sUriMatcher;
        String str = AUTHORITY;
        uriMatcher.addURI(str, Contract.RELOAD, 1);
        uriMatcher.addURI(str, Contract.QUERY_LOCATION, 2);
        uriMatcher.addURI(str, "query_area_code", 3);
        uriMatcher.addURI(str, "parse_area_code", 4);
        uriMatcher.addURI(str, Contract.QUERY_OPERATOR, 5);
        uriMatcher.addURI(str, Contract.QUERY_VERSION, 6);
        uriMatcher.addURI(str, Contract.QUERY_UNIQ_ID, 7);
        uriMatcher.addURI(str, "query_area_code_by_address", 8);
    }

    private String getAreaCode(CharSequence charSequence, int i, int i2) {
        if (this.mDensityIndexFileReader == null) {
            return "";
        }
        return (String) this.mDensityIndexFileReader.get(0, getUniqId(charSequence, i, i2, true), 1);
    }

    private String getAreaCode(String str, String str2) {
        if (this.mDensityIndexFileReader == null) {
            return "";
        }
        if (this.mAreaCodeTelocations.isEmpty()) {
            synchronized (this.mAreaCodeTelocations) {
                if (this.mAreaCodeTelocations.isEmpty()) {
                    for (int i = 0; i < 1000; i++) {
                        String str3 = (String) this.mDensityIndexFileReader.get(0, i, 0);
                        if (!TextUtils.isEmpty(str3)) {
                            this.mAreaCodeTelocations.put(String.valueOf(i), str3);
                        }
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("adminArea:");
        sb.append(str);
        sb.append(" locality:");
        sb.append(str2);
        Log.d(TAG, sb.toString());
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            String replace = str.replace("省", "").replace("市", "");
            String replace2 = str2.replace("市", "").replace("区", "").replace("县", "");
            for (Entry entry : this.mAreaCodeTelocations.entrySet()) {
                String str4 = (String) entry.getValue();
                if (str4.startsWith(replace) && str4.contains(replace2)) {
                    return (String) entry.getKey();
                }
            }
        }
        return "";
    }

    private String getLocation(CharSequence charSequence, int i, int i2, boolean z) {
        String str = "";
        if (this.mDensityIndexFileReader == null || !z) {
            return str;
        }
        int uniqId = getUniqId(charSequence, i, i2, true);
        return uniqId <= 0 ? str : (String) this.mDensityIndexFileReader.get(0, uniqId, 0);
    }

    private String getOperator(CharSequence charSequence, int i, int i2) {
        if (TextUtils.isEmpty(charSequence) || charSequence.length() < 3) {
            return "";
        }
        CharSequence subSequence = charSequence.subSequence(i, i2 + i);
        CharSequence subSequence2 = subSequence.subSequence(0, 3);
        if (VIRTUAL_OPERATOR_PREFIXES.contains(subSequence2)) {
            if (subSequence.length() < 7) {
                return "";
            }
            if (this.mXiaomiMobileSet.size() == 0) {
                synchronized (this.mXiaomiMobileSet) {
                    if (this.mXiaomiMobileSet.size() == 0) {
                        initxiaomiMobile();
                    }
                }
            }
            if (this.mXiaomiMobileSet.contains(subSequence.subSequence(0, 7))) {
                return AppConstants.getCurrentApplication().getResources().getString(R.string.xiaomi_mobile);
            }
            subSequence2 = subSequence.subSequence(0, 4);
        } else if (subSequence.length() >= 5 && subSequence.subSequence(0, 4).equals("1064")) {
            subSequence2 = subSequence.subSequence(0, 5);
        }
        if (this.mOperatorsMap.size() == 0) {
            synchronized (this.mOperatorsMap) {
                if (this.mOperatorsMap.size() == 0) {
                    initOperators();
                }
            }
        }
        if (this.mOperatorsMap.containsKey(subSequence2)) {
            return (String) this.mOperatorsMap.get(subSequence2);
        }
        for (int i3 = 4; i3 >= 3; i3--) {
            CharSequence subSequence3 = subSequence.subSequence(0, i3);
            if (this.mOperatorsMap.containsKey(subSequence3)) {
                return (String) this.mOperatorsMap.get(subSequence3);
            }
        }
        return "";
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0088 A[SYNTHETIC, Splitter:B:25:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0093 A[SYNTHETIC, Splitter:B:30:0x0093] */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initDatFile(String str, DatFileRawReadListener datFileRawReadListener) {
        String str2 = "Failed to close reader";
        String str3 = TAG;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(AppConstants.getCurrentApplication().getResources().getAssets().open(str), "UTF-8"));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        try {
                            bufferedReader2.close();
                            return;
                        } catch (Exception e) {
                            Log.e(str3, str2, e);
                            return;
                        }
                    } else if (!readLine.trim().startsWith("#")) {
                        if (readLine.trim().startsWith("version")) {
                            Log.i(str3, String.format("%s version: %s", new Object[]{str, readLine.split(":")[1]}));
                        } else {
                            datFileRawReadListener.onReadRaw(readLine.split(" "));
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    bufferedReader = bufferedReader2;
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to get ");
                        sb.append(str);
                        Log.e(str3, sb.toString(), e);
                        if (bufferedReader == null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader2 = bufferedReader;
                        if (bufferedReader2 != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (Exception e3) {
                            Log.e(str3, str2, e3);
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to get ");
            sb2.append(str);
            Log.e(str3, sb2.toString(), e);
            if (bufferedReader == null) {
                bufferedReader.close();
            }
        }
    }

    private void initOperators() {
        initDatFile(OPERATORS_DATA_ASSET_NAME, new DatFileRawReadListener() {
            public void onReadRaw(String[] strArr) {
                TelocationProvider.this.mOperatorsMap.put(strArr[0], strArr[1]);
            }
        });
    }

    private void initReader() {
        Application currentApplication = AppConstants.getCurrentApplication();
        String str = DATA_ASSET_NAME;
        String directIndexedFilePath = DirectIndexedFileExtractor.getDirectIndexedFilePath(currentApplication, str);
        String str2 = TAG;
        if (directIndexedFilePath != null && new File(directIndexedFilePath).exists()) {
            try {
                this.mDensityIndexFileReader = DirectIndexedFile.open(directIndexedFilePath);
                StringBuilder sb = new StringBuilder();
                sb.append("Read Telocation from ");
                sb.append(directIndexedFilePath);
                Log.v(str2, sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.mDensityIndexFileReader == null) {
            try {
                this.mDensityIndexFileReader = DirectIndexedFile.open(AppConstants.getCurrentApplication().getResources().getAssets().open(str, 1));
                Log.v(str2, "Read Telocation from assets");
            } catch (IOException unused) {
                Log.w(str2, "Can't read telocation.idf, NO mobile telocation supported!");
            }
        }
        Reader reader = this.mDensityIndexFileReader;
        if (reader != null) {
            int dataVersion = reader.getDataVersion();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Telocation version :");
            sb2.append(dataVersion);
            Log.i(str2, sb2.toString());
        }
    }

    private void initUniqIdLen8Numbers() {
        initDatFile(UNIQID_LEN8_NUMBER_ASSET_NAME, new DatFileRawReadListener() {
            public void onReadRaw(String[] strArr) {
                int i = 0;
                try {
                    i = Integer.parseInt(strArr[0]);
                } catch (NumberFormatException unused) {
                }
                if (i != 0) {
                    TelocationProvider.this.mUniqIdLen8Numbers.add(Integer.valueOf(i));
                }
            }
        });
    }

    private void initxiaomiMobile() {
        initDatFile(XIAOMI_DATA_ASSET_NAME, new DatFileRawReadListener() {
            public void onReadRaw(String[] strArr) {
                TelocationProvider.this.mXiaomiMobileSet.add(strArr[0]);
            }
        });
    }

    private String parseAreaCode(CharSequence charSequence, int i, int i2) {
        if (this.mDensityIndexFileReader == null) {
            return "";
        }
        return (String) this.mDensityIndexFileReader.get(0, getUniqId(charSequence, i, i2, false), 1);
    }

    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Reader reader = this.mDensityIndexFileReader;
        if (reader != null) {
            reader.close();
        }
        super.finalize();
    }

    public String getType(Uri uri) {
        return null;
    }

    public int getUniqId(CharSequence charSequence, int i, int i2, boolean z) {
        int charAt;
        int i3;
        if (i2 > 0 && charSequence.charAt(i) == '0') {
            i++;
            i2--;
        }
        if (i2 <= 1) {
            return 0;
        }
        if (this.mUniqIdLen8Numbers.size() == 0) {
            synchronized (this.mUniqIdLen8Numbers) {
                if (this.mUniqIdLen8Numbers.size() == 0) {
                    initUniqIdLen8Numbers();
                }
            }
        }
        switch (charSequence.charAt(i)) {
            case '0':
                break;
            case '1':
                int i4 = i + 1;
                if (charSequence.charAt(i4) == '0' && (!z || i2 < 13 || !charSequence.subSequence(i, i + i2).toString().startsWith("1064"))) {
                    return 10;
                }
                if (z && i2 >= 7) {
                    int charAt2 = ((charSequence.charAt(i4) - '0') * 100000) + 1000000 + ((charSequence.charAt(i + 2) - '0') * 10000) + ((charSequence.charAt(i + 3) - '0') * 1000) + ((charSequence.charAt(i + 4) - '0') * 100) + ((charSequence.charAt(i + 5) - '0') * 10) + (charSequence.charAt(i + 6) - '0');
                    if (charAt2 == 1380013 && i2 > 10 && charSequence.charAt(i + 7) == '8' && charSequence.charAt(i + 8) == '0' && charSequence.charAt(i + 9) == '0' && charSequence.charAt(i + 10) == '0') {
                        return 0;
                    }
                    if (i2 < 8 || !this.mUniqIdLen8Numbers.contains(Integer.valueOf(charAt2))) {
                        if (i2 >= 13 && ((charAt2 >= 1410000 && charAt2 <= 1419999) || (charAt2 >= 1064000 && charAt2 <= 1064999))) {
                            charAt = (charAt2 * 100) + ((charSequence.charAt(i + 7) - '0') * 10);
                            i3 = i + 8;
                        }
                        return charAt2;
                    }
                    charAt = charAt2 * 10;
                    i3 = i + 7;
                    charAt2 = charAt + (charSequence.charAt(i3) - '0');
                    return charAt2;
                }
                break;
            case '2':
                return (charSequence.charAt(i + 1) - '0') + 20;
            default:
                if (i2 > 2) {
                    return ((((charSequence.charAt(i) - '0') * 10) + (charSequence.charAt(i + 1) - '0')) * 10) + (charSequence.charAt(i + 2) - '0');
                }
                break;
        }
        return 0;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        initReader();
        return true;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        MatrixCursor matrixCursor = new MatrixCursor(Contract.COLUMNS);
        Object[] objArr = new Object[Contract.COLUMNS.length];
        objArr[0] = Integer.valueOf(1);
        int match = sUriMatcher.match(uri);
        String str3 = Params.LENGTH;
        String str4 = "start";
        String str5 = Params.NUMBER;
        switch (match) {
            case 1:
                initReader();
                break;
            case 2:
                objArr[1] = getLocation(uri.getQueryParameter(str5), Integer.valueOf(uri.getQueryParameter(str4)).intValue(), Integer.valueOf(uri.getQueryParameter(str3)).intValue(), Boolean.valueOf(uri.getQueryParameter(Params.WITH_AREA_CODE)).booleanValue());
                break;
            case 3:
                objArr[2] = getAreaCode(uri.getQueryParameter(str5), Integer.valueOf(uri.getQueryParameter(str4)).intValue(), Integer.valueOf(uri.getQueryParameter(str3)).intValue());
                break;
            case 4:
                objArr[3] = parseAreaCode(uri.getQueryParameter(str5), Integer.valueOf(uri.getQueryParameter(str4)).intValue(), Integer.valueOf(uri.getQueryParameter(str3)).intValue());
                break;
            case 5:
                objArr[4] = getOperator(uri.getQueryParameter(str5), Integer.valueOf(uri.getQueryParameter(str4)).intValue(), Integer.valueOf(uri.getQueryParameter(str3)).intValue());
                break;
            case 6:
                if (this.mDensityIndexFileReader == null) {
                    initReader();
                }
                objArr[5] = Integer.valueOf(this.mDensityIndexFileReader.getDataVersion());
                break;
            case 7:
                objArr[6] = Integer.valueOf(getUniqId(uri.getQueryParameter(str5), Integer.valueOf(uri.getQueryParameter(str4)).intValue(), Integer.valueOf(uri.getQueryParameter(str3)).intValue(), uri.getBooleanQueryParameter(Params.ENABLE_MOBILE, true)));
                break;
            case 8:
                objArr[7] = getAreaCode(uri.getQueryParameter(Params.ADMIN_AREA), uri.getQueryParameter(Params.LOCALITY));
                break;
        }
        matrixCursor.addRow(objArr);
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
