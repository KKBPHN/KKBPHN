package com.xiaomi.idm.api.conn;

import android.text.TextUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IPCLParam.WifiConfig.Builder;

public class WifiConfig {
    private static final String TAG = "WifiConfig";
    int channel;
    String localIp;
    String macAddr;
    String pwd;
    String remoteIp;
    String ssid;
    boolean use5GBand;

    public static WifiConfig buildFromProto(com.xiaomi.mi_connect_service.IPCLParam.WifiConfig wifiConfig) {
        if (wifiConfig == null) {
            return null;
        }
        WifiConfig wifiConfig2 = new WifiConfig();
        wifiConfig2.ssid = wifiConfig.getSsid();
        wifiConfig2.pwd = wifiConfig.getPwd();
        wifiConfig2.use5GBand = wifiConfig.getUse5GBand();
        wifiConfig2.channel = wifiConfig.getChannel();
        wifiConfig2.macAddr = wifiConfig.getMacAddr();
        wifiConfig2.remoteIp = wifiConfig.getRemoteIp();
        wifiConfig2.localIp = wifiConfig.getLocalIp();
        return wifiConfig2;
    }

    public static WifiConfig buildFromProto(byte[] bArr) {
        com.xiaomi.mi_connect_service.IPCLParam.WifiConfig wifiConfig = null;
        if (bArr == null) {
            return null;
        }
        try {
            wifiConfig = com.xiaomi.mi_connect_service.IPCLParam.WifiConfig.parseFrom(bArr);
        } catch (InvalidProtocolBufferException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return buildFromProto(wifiConfig);
    }

    public static WifiConfig buildFromQRCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        if (split == null || split.length < 4) {
            return null;
        }
        WifiConfig wifiConfig = new WifiConfig();
        wifiConfig.ssid = split[0];
        wifiConfig.pwd = split[1];
        wifiConfig.macAddr = split[2];
        try {
            wifiConfig.channel = Integer.parseInt(split[3]);
        } catch (NumberFormatException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return wifiConfig;
    }

    public int getChannel() {
        return this.channel;
    }

    public String getLocalIp() {
        return this.localIp;
    }

    public String getMacAddr() {
        return this.macAddr;
    }

    public String getPwd() {
        return this.pwd;
    }

    public String getRemoteIp() {
        return this.remoteIp;
    }

    public String getSsid() {
        return this.ssid;
    }

    public boolean isUse5GBand() {
        return this.use5GBand;
    }

    public void setChannel(int i) {
        this.channel = i;
    }

    public void setLocalIp(String str) {
        this.localIp = str;
    }

    public void setMacAddr(String str) {
        this.macAddr = str;
    }

    public void setPwd(String str) {
        this.pwd = str;
    }

    public void setRemoteIp(String str) {
        this.remoteIp = str;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }

    public void setUse5GBand(boolean z) {
        this.use5GBand = z;
    }

    public com.xiaomi.mi_connect_service.IPCLParam.WifiConfig toProto() {
        Builder newBuilder = com.xiaomi.mi_connect_service.IPCLParam.WifiConfig.newBuilder();
        String str = this.ssid;
        if (str != null) {
            newBuilder.setSsid(str);
        }
        String str2 = this.pwd;
        if (str2 != null) {
            newBuilder.setPwd(str2);
        }
        newBuilder.setUse5GBand(isUse5GBand());
        newBuilder.setChannel(this.channel);
        String str3 = this.macAddr;
        if (str3 != null) {
            newBuilder.setMacAddr(str3);
        }
        String str4 = this.remoteIp;
        if (str4 != null) {
            newBuilder.setRemoteIp(str4);
        }
        String str5 = this.localIp;
        if (str5 != null) {
            newBuilder.setLocalIp(str5);
        }
        return newBuilder.build();
    }

    public String toQCodeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.ssid);
        String str = ",";
        sb.append(str);
        sb.append(this.pwd);
        sb.append(str);
        sb.append(this.macAddr);
        sb.append(str);
        sb.append(this.channel);
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WifiConfig{use5GBand=");
        sb.append(this.use5GBand);
        sb.append(", ssid='");
        sb.append(this.ssid);
        sb.append('\'');
        sb.append(", pwd='");
        sb.append(this.pwd);
        sb.append('\'');
        sb.append(", channel=");
        sb.append(this.channel);
        sb.append(", macAddr='");
        sb.append(this.macAddr);
        sb.append('\'');
        sb.append(", localIp='");
        sb.append(this.localIp);
        sb.append('\'');
        sb.append(", remoteIp='");
        sb.append(this.remoteIp);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
