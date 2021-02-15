package com.android.camera.aiwatermark.parser;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.log.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AIWatermarkParser extends AbstractParser {
    private static final String TAG = AbstractParser.class.getSimpleName();

    public ArrayList parseJson() {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArray = new JSONObject(readJson()).getJSONArray("watermarks");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                WatermarkItem watermarkItem = new WatermarkItem(jSONObject.getString(WatermarkConstant.ITEM_KEY), jSONObject.getInt("type"), null, jSONObject.getInt("id"), jSONObject.getInt("location"), jSONObject.getInt(WatermarkConstant.ITEM_COUNTRY));
                arrayList.add(watermarkItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0061, code lost:
        if (r5 != 4) goto L_0x01e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x016e, code lost:
        r5 = 65535;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x016f, code lost:
        switch(r5) {
            case 0: goto L_0x01ca;
            case 1: goto L_0x01c0;
            case 2: goto L_0x01b0;
            case 3: goto L_0x01a0;
            case 4: goto L_0x0190;
            case 5: goto L_0x0180;
            case 6: goto L_0x0174;
            default: goto L_0x0172;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0174, code lost:
        r5 = r4.nextText();
        r1.markItem.setText(r5);
        r16 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0180, code lost:
        r5 = java.lang.Integer.parseInt(r4.nextText());
        r1.markItem.setCountry(r5);
        r20 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0190, code lost:
        r5 = java.lang.Integer.parseInt(r4.nextText());
        r1.markItem.setLocation(r5);
        r19 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x01a0, code lost:
        r5 = java.lang.Integer.parseInt(r4.nextText());
        r1.markItem.setResId(r5);
        r18 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01b0, code lost:
        r5 = java.lang.Integer.parseInt(r4.nextText());
        r1.markItem.setType(r5);
        r17 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x01c0, code lost:
        r15 = r4.nextText();
        r1.markItem.setKey(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x01ca, code lost:
        r8 = new com.android.camera.aiwatermark.data.WatermarkItem(r15, r17, r16, r18, r19, r20);
        r1.markItem = r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ArrayList parseXml() {
        char c;
        String str = "drawable";
        try {
            Context androidContext = CameraAppImpl.getAndroidContext();
            InputStream openRawResource = androidContext.getResources().openRawResource(R.raw.ai_watermark);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new InputStreamReader(openRawResource));
            int OO00oO = C0122O00000o.instance().OO00oO();
            int eventType = newPullParser.getEventType();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("start ... type =");
            sb.append(eventType);
            Log.d(str2, sb.toString());
            String str3 = null;
            String str4 = null;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (eventType != 1) {
                if (eventType != 0) {
                    String str5 = WatermarkConstant.ITEM_TAG;
                    if (eventType != 2) {
                        if (eventType == 3) {
                            if (TextUtils.equals(str5, newPullParser.getName())) {
                                Resources resources = androidContext.getResources();
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("ic_wp_");
                                sb2.append(this.markItem.getKey());
                                int identifier = resources.getIdentifier(sb2.toString(), str, androidContext.getPackageName());
                                if (identifier != -1) {
                                    this.markItem.setResId(identifier);
                                }
                                Resources resources2 = androidContext.getResources();
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("ic_wr_");
                                sb3.append(this.markItem.getKey());
                                int identifier2 = resources2.getIdentifier(sb3.toString(), str, androidContext.getPackageName());
                                if (identifier2 != -1) {
                                    this.markItem.setResRvItem(identifier2);
                                }
                                if (OO00oO == this.markItem.getCountry() || this.markItem.getType() == 11 || this.markItem.getType() == 12) {
                                    this.watermarkItems.add(this.markItem);
                                }
                                this.markItem = null;
                            } else if (TextUtils.equals("watermarks", newPullParser.getName())) {
                                String str6 = TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("end this parser watermarkItems=");
                                sb4.append(this.watermarkItems.size());
                                Log.d(str6, sb4.toString());
                                return this.watermarkItems;
                            }
                        }
                    }
                    if (newPullParser.getName() != null) {
                        String name = newPullParser.getName();
                        switch (name.hashCode()) {
                            case -213424028:
                                if (name.equals(str5)) {
                                    c = 0;
                                    break;
                                }
                            case 3355:
                                if (name.equals("id")) {
                                    c = 3;
                                    break;
                                }
                            case 106079:
                                if (name.equals(WatermarkConstant.ITEM_KEY)) {
                                    c = 1;
                                    break;
                                }
                            case 3556653:
                                if (name.equals("text")) {
                                    c = 6;
                                    break;
                                }
                            case 3575610:
                                if (name.equals("type")) {
                                    c = 2;
                                    break;
                                }
                            case 957831062:
                                if (name.equals(WatermarkConstant.ITEM_COUNTRY)) {
                                    c = 5;
                                    break;
                                }
                            case 1901043637:
                                if (name.equals("location")) {
                                    c = 4;
                                    break;
                                }
                        }
                    }
                } else {
                    this.watermarkItems.clear();
                }
                Log.d(TAG, "before next()");
                eventType = newPullParser.next();
            }
        } catch (IOException e) {
            Log.d(TAG, "ioexception");
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            String str7 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("XmlPullParserException e=");
            sb5.append(e2.getMessage());
            Log.d(str7, sb5.toString());
            e2.printStackTrace();
        }
        return this.watermarkItems;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x005c A[SYNTHETIC, Splitter:B:31:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0064 A[Catch:{ IOException -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0069 A[Catch:{ IOException -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0079 A[SYNTHETIC, Splitter:B:45:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0081 A[Catch:{ IOException -> 0x007d }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0086 A[Catch:{ IOException -> 0x007d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String readJson() {
        InputStreamReader inputStreamReader;
        InputStream inputStream;
        BufferedReader bufferedReader;
        IOException e;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            inputStream = CameraAppImpl.getAndroidContext().getResources().openRawResource(R.raw.ai_watermark);
            try {
                inputStreamReader = new InputStreamReader(inputStream);
                try {
                    bufferedReader = new BufferedReader(inputStreamReader);
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        } catch (IOException e2) {
                            e = e2;
                            try {
                                e.printStackTrace();
                                if (bufferedReader != null) {
                                }
                                if (inputStreamReader != null) {
                                }
                                if (inputStream != null) {
                                }
                                return sb.toString();
                            } catch (Throwable th) {
                                th = th;
                                bufferedReader2 = bufferedReader;
                                if (bufferedReader2 != null) {
                                    try {
                                        bufferedReader2.close();
                                    } catch (IOException e3) {
                                        e3.printStackTrace();
                                        throw th;
                                    }
                                }
                                if (inputStreamReader != null) {
                                    inputStreamReader.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                throw th;
                            }
                        }
                    }
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                } catch (IOException e4) {
                    IOException iOException = e4;
                    bufferedReader = null;
                    e = iOException;
                    e.printStackTrace();
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return sb.toString();
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader2 != null) {
                    }
                    if (inputStreamReader != null) {
                    }
                    if (inputStream != null) {
                    }
                    throw th;
                }
            } catch (IOException e5) {
                bufferedReader = null;
                e = e5;
                inputStreamReader = null;
                e.printStackTrace();
                if (bufferedReader != null) {
                }
                if (inputStreamReader != null) {
                }
                if (inputStream != null) {
                }
                return sb.toString();
            } catch (Throwable th3) {
                th = th3;
                inputStreamReader = null;
                if (bufferedReader2 != null) {
                }
                if (inputStreamReader != null) {
                }
                if (inputStream != null) {
                }
                throw th;
            }
            try {
                bufferedReader.close();
                inputStreamReader.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        } catch (IOException e7) {
            inputStreamReader = null;
            bufferedReader = null;
            e = e7;
            inputStream = null;
            e.printStackTrace();
            if (bufferedReader != null) {
            }
            if (inputStreamReader != null) {
            }
            if (inputStream != null) {
            }
            return sb.toString();
        } catch (Throwable th4) {
            th = th4;
            inputStream = null;
            inputStreamReader = null;
            if (bufferedReader2 != null) {
            }
            if (inputStreamReader != null) {
            }
            if (inputStream != null) {
            }
            throw th;
        }
        return sb.toString();
    }
}
