package com.android.camera.sticker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.android.camera.log.Log;
import com.sensetime.stmobile.STMobileAuthentificationNative;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class STLicenseUtils {
    private static final String LICENSE_NAME = "SenseME.lic";
    private static final String PREF_ACTIVATE_CODE = "activate_code";
    private static final String PREF_ACTIVATE_CODE_FILE = "activate_code_file";
    private static final String TAG = "STLicenseUtils";

    /* JADX WARNING: Removed duplicated region for block: B:27:0x004d A[SYNTHETIC, Splitter:B:27:0x004d] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0057 A[SYNTHETIC, Splitter:B:32:0x0057] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00f7 A[SYNTHETIC, Splitter:B:61:0x00f7] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0101 A[SYNTHETIC, Splitter:B:66:0x0101] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean checkLicense(Context context) {
        InputStreamReader inputStreamReader;
        int length;
        String sb;
        BufferedReader bufferedReader;
        StringBuilder sb2 = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            inputStreamReader = new InputStreamReader(context.getResources().getAssets().open(LICENSE_NAME));
            try {
                bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            sb2.append(readLine);
                            sb2.append("\n");
                        } else {
                            try {
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e2) {
                        e = e2;
                        try {
                            e.printStackTrace();
                            if (inputStreamReader != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            length = sb2.toString().length();
                            String str = TAG;
                            if (length != 0) {
                            }
                            Log.e(str, sb);
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader2 = bufferedReader;
                            if (inputStreamReader != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                }
                inputStreamReader.close();
            } catch (IOException e6) {
                e = e6;
                bufferedReader = null;
                e.printStackTrace();
                if (inputStreamReader != null) {
                }
                if (bufferedReader != null) {
                }
                length = sb2.toString().length();
                String str2 = TAG;
                if (length != 0) {
                }
                Log.e(str2, sb);
                return false;
            } catch (Throwable th2) {
                th = th2;
                if (inputStreamReader != null) {
                }
                if (bufferedReader2 != null) {
                }
                throw th;
            }
            try {
                bufferedReader.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        } catch (IOException e8) {
            e = e8;
            inputStreamReader = null;
            bufferedReader = null;
            e.printStackTrace();
            if (inputStreamReader != null) {
            }
            if (bufferedReader != null) {
            }
            length = sb2.toString().length();
            String str22 = TAG;
            if (length != 0) {
            }
            Log.e(str22, sb);
            return false;
        } catch (Throwable th3) {
            th = th3;
            inputStreamReader = null;
            if (inputStreamReader != null) {
            }
            if (bufferedReader2 != null) {
            }
            throw th;
        }
        length = sb2.toString().length();
        String str222 = TAG;
        if (length != 0) {
            sb = "read license data error";
        } else {
            String sb3 = sb2.toString();
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_ACTIVATE_CODE_FILE, 0);
            String str3 = PREF_ACTIVATE_CODE;
            String string = sharedPreferences.getString(str3, null);
            Integer valueOf = Integer.valueOf(-1);
            String str4 = "activeCode: ";
            if (string == null || STMobileAuthentificationNative.checkActiveCodeFromBuffer(sb3, sb3.length(), string) != 0) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str4);
                sb4.append(string == null);
                Log.e(str222, sb4.toString());
                String generateActiveCodeFromBuffer = STMobileAuthentificationNative.generateActiveCodeFromBuffer(sb3, sb3.length());
                if (generateActiveCodeFromBuffer == null || generateActiveCodeFromBuffer.length() == 0) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("generate license error: ");
                    sb5.append(valueOf);
                    sb = sb5.toString();
                } else {
                    Editor edit = sharedPreferences.edit();
                    edit.putString(str3, generateActiveCodeFromBuffer);
                    edit.commit();
                    return true;
                }
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str4);
                sb6.append(string);
                Log.e(str222, sb6.toString());
                return true;
            }
        }
        Log.e(str222, sb);
        return false;
    }
}
