package com.ss.android.vesdk.runtime;

import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.ss.android.ttve.common.TECloudCtrlInvoker;
import com.ss.android.ttve.common.TESpdLogManager;
import com.ss.android.ttve.common.TESpdLogManager.InfoLevel;
import com.ss.android.vesdk.VELogUtil;
import com.ss.android.vesdk.runtime.persistence.VESP;
import com.xiaomi.stat.MiStat.Param;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

public class VECloudCtrlManager {
    private static String[] COMMANDS = {"vesdk_log_command"};
    private static String TAG = "VECloudCtrlManager";
    private static volatile VECloudCtrlManager mTECloudCtrlManager;
    private TECloudCtrlInvoker mCloudCtrlInvoker;
    private boolean mLogStatus;
    private String mWorkpsace;

    private VECloudCtrlManager() {
        this.mLogStatus = false;
        this.mWorkpsace = Environment.getExternalStorageDirectory().toString();
        this.mLogStatus = false;
        this.mCloudCtrlInvoker = new TECloudCtrlInvoker();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0030 A[Catch:{ Exception -> 0x0170 }] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003b A[Catch:{ Exception -> 0x0170 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d7 A[Catch:{ Exception -> 0x0170 }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00da A[Catch:{ Exception -> 0x0170 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x014c A[Catch:{ Exception -> 0x0170 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int doCommand(@NonNull String str, @NonNull String str2) {
        boolean z;
        boolean z2;
        String str3 = str;
        String str4 = "sign";
        String str5 = Param.LEVEL;
        String str6 = "endtime";
        String str7 = "starttime";
        String str8 = "enable";
        String str9 = "";
        int i = -1;
        try {
            JSONObject jSONObject = new JSONObject(str2);
            if (str.hashCode() == 2043549244) {
                if (str3.equals("vesdk_log_command")) {
                    z = false;
                    if (!z) {
                        VELogUtil.e(TAG, " json contail invalid command ");
                        i = -2;
                    } else {
                        boolean has = jSONObject.has(str8);
                        String str10 = BaseEvent.VALUE_FALSE;
                        String string = has ? jSONObject.getString(str8) : str10;
                        String str11 = "2018-12-08 00:00:00";
                        String string2 = jSONObject.has(str7) ? jSONObject.getString(str7) : str11;
                        if (jSONObject.has(str6)) {
                            str11 = jSONObject.getString(str6);
                        }
                        String string3 = jSONObject.has(str5) ? jSONObject.getString(str5) : str9;
                        String string4 = jSONObject.has(str4) ? jSONObject.getString(str4) : str9;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str3);
                        sb.append(string);
                        sb.append(string2);
                        sb.append(str11);
                        sb.append(string3);
                        if (!this.mCloudCtrlInvoker.verifyJson(sb.toString(), string4)) {
                            storeCommand(str3, str9);
                            VELogUtil.e(TAG, "Cloud Ctrl Command Json is doctored");
                            return -1;
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long time = simpleDateFormat.parse(string2).getTime();
                        long time2 = simpleDateFormat.parse(str11).getTime();
                        long currentTimeMillis = System.currentTimeMillis();
                        InfoLevel infoLevel = InfoLevel.LEVEL3;
                        if (string3.hashCode() == 68) {
                            if (string3.equals("D")) {
                                z2 = false;
                                InfoLevel infoLevel2 = !z2 ? InfoLevel.LEVEL3 : InfoLevel.LEVEL0;
                                if (this.mLogStatus && string.equals(BaseEvent.VALUE_TRUE) && currentTimeMillis >= time && currentTimeMillis < time2) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(this.mWorkpsace);
                                    sb2.append("/vesdklog");
                                    String sb3 = sb2.toString();
                                    VELogUtil.d(TAG, sb3);
                                    int initSpdLog = TESpdLogManager.getInstance().initSpdLog(sb3, infoLevel2.ordinal(), 3);
                                    if (initSpdLog < 0) {
                                        String str12 = TAG;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(" TESpdLog init fail ");
                                        sb4.append(initSpdLog);
                                        VELogUtil.e(str12, sb4.toString());
                                        return -3;
                                    }
                                    TESpdLogManager.getInstance().setLevel(infoLevel2);
                                    this.mLogStatus = true;
                                } else if (string.equals(str10) || (currentTimeMillis < time && currentTimeMillis >= time2)) {
                                    if (this.mLogStatus) {
                                        TESpdLogManager.getInstance().close();
                                        this.mLogStatus = false;
                                    }
                                    String str13 = TAG;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append(str3);
                                    sb5.append(" expired");
                                    VELogUtil.d(str13, sb5.toString());
                                    storeCommand(str3, str9);
                                }
                                i = 0;
                            }
                        }
                        z2 = true;
                        if (!z2) {
                        }
                        if (this.mLogStatus) {
                        }
                        if (this.mLogStatus) {
                        }
                        String str132 = TAG;
                        StringBuilder sb52 = new StringBuilder();
                        sb52.append(str3);
                        sb52.append(" expired");
                        VELogUtil.d(str132, sb52.toString());
                        storeCommand(str3, str9);
                        i = 0;
                    }
                    return i;
                }
            }
            z = true;
            if (!z) {
            }
        } catch (Exception e) {
            String str14 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(" json parse failed ");
            sb6.append(e.toString());
            VELogUtil.e(str14, sb6.toString());
            storeCommand(str3, str9);
        }
        return i;
    }

    public static VECloudCtrlManager getInstance() {
        if (mTECloudCtrlManager == null) {
            synchronized (VECloudCtrlManager.class) {
                if (mTECloudCtrlManager == null) {
                    mTECloudCtrlManager = new VECloudCtrlManager();
                }
            }
        }
        return mTECloudCtrlManager;
    }

    private void storeCommand(@NonNull String str, @NonNull String str2) {
        VESP.getInstance().put(str, str2);
    }

    public void closeCloudControlRes() {
        if (this.mLogStatus) {
            TESpdLogManager.getInstance().close();
            this.mLogStatus = false;
        }
    }

    public void execStoredCommands(@NonNull String str) {
        String[] strArr;
        this.mWorkpsace = str;
        for (String str2 : COMMANDS) {
            String str3 = (String) VESP.getInstance().get(str2, "");
            if (!str3.isEmpty()) {
                doCommand(str2, str3);
            }
        }
    }

    public void storeCloudControlCommand(@NonNull Context context, @NonNull String str) {
        VELogUtil.d(TAG, str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("command");
            VESP.getInstance().init(context);
            storeCommand(string, jSONObject.toString());
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(" json parse failed ");
            sb.append(e.toString());
            VELogUtil.e(str2, sb.toString());
        }
    }
}
