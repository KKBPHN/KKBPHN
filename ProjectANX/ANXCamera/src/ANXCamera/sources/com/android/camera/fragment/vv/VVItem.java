package com.android.camera.fragment.vv;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.module.impl.component.LiveSubVVImpl;
import com.android.camera.resource.BaseResourceItem;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VVItem extends BaseResourceItem {
    public static final Creator CREATOR = new Creator() {
        public VVItem createFromParcel(Parcel parcel) {
            return new VVItem(parcel);
        }

        public VVItem[] newArray(int i) {
            return new VVItem[i];
        }
    };
    private static final int DURATION_WATERMARK = 2000;
    public static final String ID_PREFIX = "vv";
    public String composeJsonPath;
    public String configJsonPath;
    public String coverPath;
    public List durationList;
    public String filterPath;
    public String iconUrl;
    public transient int index;
    public boolean isValid;
    public String musicPath;
    public String name;
    public String placeholder;
    public String previewVideoPath;
    public String rawInfo;
    public List speedList;
    public long totalDuration;

    public VVItem() {
        this.isValid = true;
    }

    protected VVItem(Parcel parcel) {
        this.name = parcel.readString();
        this.coverPath = parcel.readString();
        this.previewVideoPath = parcel.readString();
        this.filterPath = parcel.readString();
        this.configJsonPath = parcel.readString();
        this.composeJsonPath = parcel.readString();
        this.musicPath = parcel.readString();
        this.durationList = new ArrayList();
        parcel.readList(this.durationList, Long.class.getClassLoader());
        this.totalDuration = parcel.readLong();
        this.id = parcel.readString();
        this.versionCode = parcel.readInt();
        this.uri = parcel.readString();
        this.baseArchivesFolder = parcel.readString();
    }

    public static final VVItem createFromRawInfo(String str) {
        String contentFromFile = Util.getContentFromFile(new File(str));
        if (TextUtils.isEmpty(contentFromFile)) {
            return null;
        }
        VVItem vVItem = new VVItem();
        try {
            vVItem.parseSummaryData(new JSONObject(contentFromFile), 0);
            StringBuilder sb = new StringBuilder();
            sb.append(LiveSubVVImpl.TEMPLATE_PATH);
            sb.append(vVItem.id);
            sb.append(File.separator);
            String sb2 = sb.toString();
            if (!vVItem.simpleVerification(sb2)) {
                return null;
            }
            vVItem.onDecompressFinished(sb2, false);
            return vVItem;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void fillDetailData(JSONObject jSONObject) {
    }

    public long getDuration(int i) {
        return ((Long) this.durationList.get(i)).longValue();
    }

    public int getEssentialFragmentSize() {
        return this.durationList.size();
    }

    public String getIDPrefix() {
        return ID_PREFIX;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    public boolean isCloudItem() {
        return !TextUtils.isEmpty(this.placeholder);
    }

    public void onDecompressFailed(String str, String str2) {
        if (isCloudItem()) {
            this.mZipPath = str;
            this.baseArchivesFolder = str2;
            setState(0);
        }
    }

    public void onDecompressFinished(String str, boolean z) {
        this.baseArchivesFolder = str;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("pv/cover.jpg");
        this.coverPath = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("pv/preview.mov");
        this.previewVideoPath = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append("filter.png");
        this.filterPath = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append(ComposerHelper.CONFIG_FILE_NAME);
        this.configJsonPath = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append("compose.json");
        this.composeJsonPath = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(str);
        sb6.append("bgm.mp3");
        this.musicPath = sb6.toString();
        if (z) {
            onUpToDate(str);
            setState(7);
        }
    }

    public void parseSummaryData(JSONObject jSONObject, int i) {
        this.rawInfo = jSONObject.toString();
        this.index = i;
        String optString = jSONObject.optString("exclude");
        int i2 = 0;
        if (TextUtils.isEmpty(optString) || !C0124O00000oO.O0Ooo0o.equalsIgnoreCase(optString)) {
            String optString2 = jSONObject.optString("include");
            if (TextUtils.isEmpty(optString2) || C0124O00000oO.O0Ooo0o.equalsIgnoreCase(optString2)) {
                this.id = jSONObject.optString("id");
                this.versionCode = jSONObject.optInt("versionCode", 0);
                this.uri = jSONObject.optString("uri");
                this.placeholder = jSONObject.optString("placeholder");
                this.iconUrl = jSONObject.optString("iconUrl");
                JSONArray optJSONArray = jSONObject.optJSONArray("fragments");
                JSONArray optJSONArray2 = jSONObject.optJSONArray("speed");
                this.durationList = new ArrayList();
                for (int i3 = 0; i3 < optJSONArray.length(); i3++) {
                    double d = 1.0d;
                    if (optJSONArray2 != null) {
                        d = optJSONArray2.optDouble(i3);
                        if (this.speedList == null) {
                            this.speedList = new ArrayList(optJSONArray2.length());
                        }
                        this.speedList.add(Float.valueOf((float) d));
                    }
                    this.durationList.add(Long.valueOf((long) (((double) optJSONArray.optLong(i3)) * d)));
                    this.totalDuration += optJSONArray.optLong(i3);
                }
                this.totalDuration += 2000;
                JSONArray optJSONArray3 = jSONObject.optJSONArray("overlapping");
                if (optJSONArray3 != null) {
                    for (int i4 = 0; i4 < optJSONArray3.length(); i4++) {
                        this.totalDuration -= optJSONArray3.optLong(i4);
                    }
                }
                String country = Locale.getDefault().getCountry();
                JSONArray optJSONArray4 = jSONObject.optJSONArray("i18n");
                while (true) {
                    if (i2 >= optJSONArray4.length()) {
                        break;
                    }
                    JSONObject optJSONObject = optJSONArray4.optJSONObject(i2);
                    String optString3 = optJSONObject.optString("lang");
                    String str = "name";
                    if (optString3.equalsIgnoreCase("default")) {
                        this.name = optJSONObject.optString(str);
                    } else if (optString3.equalsIgnoreCase(country)) {
                        this.name = optJSONObject.optString(str);
                        break;
                    }
                    i2++;
                }
                return;
            }
            this.isValid = false;
            return;
        }
        this.isValid = false;
    }

    public boolean simpleVerification(String str) {
        return new File(str, ComposerHelper.CONFIG_FILE_NAME).exists() && new File(str, "compose.json").exists();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.coverPath);
        parcel.writeString(this.previewVideoPath);
        parcel.writeString(this.filterPath);
        parcel.writeString(this.configJsonPath);
        parcel.writeString(this.composeJsonPath);
        parcel.writeString(this.musicPath);
        parcel.writeList(this.durationList);
        parcel.writeLong(this.totalDuration);
        parcel.writeString(this.id);
        parcel.writeInt(this.versionCode);
        parcel.writeString(this.uri);
        parcel.writeString(this.baseArchivesFolder);
    }
}
