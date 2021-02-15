package net.majorkernelpanic.streaming.mp4;

import android.util.Base64;
import android.util.Log;
import java.io.IOException;

public class MP4Config {
    public static final String TAG = "MP4Config";
    private String mPPS;
    private String mProfilLevel;
    private String mSPS;
    private MP4Parser mp4Parser;

    public MP4Config(String str) {
        try {
            this.mp4Parser = MP4Parser.parse(str);
        } catch (IOException unused) {
        }
        StsdBox stsdBox = this.mp4Parser.getStsdBox();
        this.mPPS = stsdBox.getB64PPS();
        this.mSPS = stsdBox.getB64SPS();
        this.mProfilLevel = stsdBox.getProfileLevel();
        this.mp4Parser.close();
    }

    public MP4Config(String str, String str2) {
        this.mPPS = str2;
        this.mSPS = str;
        this.mProfilLevel = MP4Parser.toHexString(Base64.decode(str, 2), 1, 3);
    }

    public MP4Config(String str, String str2, String str3) {
        this.mProfilLevel = str;
        this.mPPS = str3;
        this.mSPS = str2;
    }

    public MP4Config(byte[] bArr, byte[] bArr2) {
        this.mPPS = Base64.encodeToString(bArr2, 0, bArr2.length, 2);
        this.mSPS = Base64.encodeToString(bArr, 0, bArr.length, 2);
        this.mProfilLevel = MP4Parser.toHexString(bArr, 1, 3);
    }

    public String getB64PPS() {
        StringBuilder sb = new StringBuilder();
        sb.append("PPS: ");
        sb.append(this.mPPS);
        Log.d(TAG, sb.toString());
        return this.mPPS;
    }

    public String getB64SPS() {
        StringBuilder sb = new StringBuilder();
        sb.append("SPS: ");
        sb.append(this.mSPS);
        Log.d(TAG, sb.toString());
        return this.mSPS;
    }

    public String getProfileLevel() {
        return this.mProfilLevel;
    }
}
