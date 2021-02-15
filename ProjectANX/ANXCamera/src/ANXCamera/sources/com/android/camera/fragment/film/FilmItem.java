package com.android.camera.fragment.film;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.extra.DataItemLive;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.resource.BaseResourceItem;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.io.File;
import org.json.JSONObject;

public class FilmItem extends BaseResourceItem {
    public static final Creator CREATOR = new Creator() {
        public FilmItem createFromParcel(Parcel parcel) {
            return new FilmItem(parcel);
        }

        public FilmItem[] newArray(int i) {
            return new FilmItem[i];
        }
    };
    public String configJsonPath;
    public String coverPath;
    private String name;
    public String previewVideoPath;
    public long totalDuration;
    public String version;

    public FilmItem() {
    }

    protected FilmItem(Parcel parcel) {
        this.name = parcel.readString();
        this.coverPath = parcel.readString();
        this.previewVideoPath = parcel.readString();
        this.totalDuration = parcel.readLong();
        this.id = parcel.readString();
        this.versionCode = parcel.readInt();
        this.uri = parcel.readString();
        this.baseArchivesFolder = parcel.readString();
        this.configJsonPath = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void fillDetailData(JSONObject jSONObject) {
    }

    public String getIDPrefix() {
        return null;
    }

    public String getName() {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        StringBuilder sb = new StringBuilder();
        sb.append("film_");
        sb.append(this.id);
        int identifier = resources.getIdentifier(sb.toString(), "string", CameraIntentManager.CALLER_MIUI_CAMERA);
        if (identifier > 0) {
            this.name = CameraAppImpl.getAndroidContext().getResources().getString(identifier);
        }
        return this.name;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    public void onDecompressFailed(String str, String str2) {
    }

    public void onDecompressFinished(String str, boolean z) {
        this.baseArchivesFolder = str;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("cover.jpg");
        this.coverPath = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("preview.mov");
        this.previewVideoPath = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(ComposerHelper.CONFIG_FILE_NAME);
        this.configJsonPath = sb3.toString();
    }

    public void parseSummaryData(JSONObject jSONObject, int i) {
        this.index = i;
        this.id = jSONObject.optString("id");
        this.uri = jSONObject.optString("uri");
        this.totalDuration = jSONObject.optLong("duration");
        this.version = jSONObject.optString("version");
        this.name = this.id;
    }

    public boolean simpleVerification(String str) {
        boolean z = new File(str, "cover.jpg").exists() && new File(str, "preview.mov").exists();
        String str2 = DataItemLive.DATA_FILM_VERSION;
        if (z) {
            String str3 = this.version;
            DataItemLive dataItemLive = DataRepository.dataItemLive();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(this.id);
            if (str3.equals(dataItemLive.getString(sb.toString(), ""))) {
                return true;
            }
        }
        ProviderEditor editor = DataRepository.dataItemLive().editor();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(this.id);
        editor.putString(sb2.toString(), this.version).apply();
        return false;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.coverPath);
        parcel.writeString(this.previewVideoPath);
        parcel.writeLong(this.totalDuration);
        parcel.writeString(this.id);
        parcel.writeInt(this.versionCode);
        parcel.writeString(this.uri);
        parcel.writeString(this.baseArchivesFolder);
        parcel.writeString(this.configJsonPath);
    }
}
