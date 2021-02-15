package com.android.camera.fragment.music;

import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.resource.RequestHelper;
import com.android.camera.resource.tmmusic.TMMusicOperationPost;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.ss.android.ttve.monitor.MonitorUtils;
import com.ss.android.ugc.effectmanager.EffectConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class MusicOperation {
    private boolean mPaused;
    private long mPlayTotalTime;
    private String mSongId;
    private String mSongListId;
    private long mStartTime;
    private long mTotalDuration;

    static /* synthetic */ void O0000Oo(Throwable th) {
    }

    static /* synthetic */ void O0000OoO(Throwable th) {
    }

    static /* synthetic */ void O000OO00(String str) {
    }

    static /* synthetic */ void O000OO0o(String str) {
    }

    private void startRequest() {
        String str = "-";
        if (!TextUtils.isEmpty(this.mSongId) && !TextUtils.isEmpty(this.mSongListId)) {
            if (this.mStartTime != 0) {
                this.mPlayTotalTime += System.currentTimeMillis() - this.mStartTime;
                this.mStartTime = 0;
            }
            if (this.mPlayTotalTime != 0 && this.mTotalDuration != 0) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("report_type", "800100");
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("from_source", "0");
                    jSONObject2.put(MonitorUtils.KEY_USER_ID, RequestHelper.getIdentityID());
                    jSONObject2.put("song_id", this.mSongId);
                    jSONObject2.put("song_list_id", this.mSongListId);
                    jSONObject2.put("timestamp", Util.toHumanString(System.currentTimeMillis()));
                    jSONObject2.put(IjkMediaMeta.IJKM_KEY_BITRATE, "MP3-64K-FTD-P");
                    jSONObject2.put("is_online", 1);
                    jSONObject2.put(EffectConfiguration.KEY_DEVICE_TYPE, 3);
                    int ceil = (int) Math.ceil((double) (this.mPlayTotalTime / 1000));
                    int i = ((int) this.mTotalDuration) / 1000;
                    jSONObject2.put("duration_of_play", Math.min(ceil, i));
                    jSONObject2.put("song_duration", i);
                    jSONObject2.put("play_count", 1);
                    jSONObject2.put("entrance", 1);
                    jSONObject2.put("is_share", 0);
                    jSONObject2.put(OnNativeInvokeListener.ARG_IP, str);
                    jSONObject2.put("device_id", str);
                    jSONObject2.put("song_from", 0);
                    jSONObject2.put("function_type", 0);
                    jSONObject2.put(EffectConfiguration.KEY_CITY_CODE, str);
                    jSONObject2.put("errcode", str);
                    jSONObject2.put("ua", str);
                    jSONObject2.put("AB", str);
                    jSONObject2.put("stay_duration", 0);
                    jSONObject.put(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, jSONObject2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new TMMusicOperationPost(jSONObject.toString()).startObservable((Object) "").subscribe(O0000Oo0.INSTANCE, O0000Oo.INSTANCE);
            }
        }
    }

    public void endPlaySession() {
        startRequest();
    }

    public void onNewSessionStartPlay(long j) {
        this.mStartTime = System.currentTimeMillis();
        this.mTotalDuration = j;
    }

    public void onPausePlay() {
        this.mPaused = true;
        if (this.mStartTime != 0) {
            this.mPlayTotalTime += System.currentTimeMillis() - this.mStartTime;
            this.mStartTime = 0;
        }
    }

    public void onResumePlay() {
        this.mStartTime = System.currentTimeMillis();
    }

    public void onSelected(String str, String str2) {
        this.mSongId = str;
        this.mSongListId = str2;
    }

    public void onSelectedToUse() {
        String str = "-";
        if (!TextUtils.isEmpty(this.mSongId) && !TextUtils.isEmpty(this.mSongListId)) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("report_type", "800200");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("from_source", "0");
                jSONObject2.put("device_id", str);
                jSONObject2.put(OnNativeInvokeListener.ARG_IP, str);
                jSONObject2.put(MonitorUtils.KEY_USER_ID, RequestHelper.getIdentityID());
                jSONObject2.put("object_id", this.mSongId);
                jSONObject2.put("object_type", 1);
                jSONObject2.put("timestamp", Util.toHumanString(System.currentTimeMillis()));
                jSONObject2.put(EffectConfiguration.KEY_DEVICE_TYPE, 3);
                jSONObject2.put("action", 4);
                jSONObject2.put("entrance", 1);
                jSONObject2.put(IjkMediaMeta.IJKM_KEY_BITRATE, "MP3-64K-FTD-P");
                jSONObject2.put(EffectConfiguration.KEY_CITY_CODE, str);
                jSONObject2.put("ua", str);
                jSONObject2.put("AB", str);
                jSONObject.put(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, jSONObject2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new TMMusicOperationPost(jSONObject.toString()).startObservable((Object) "").subscribe(O0000OOo.INSTANCE, O0000O0o.INSTANCE);
        }
    }
}
