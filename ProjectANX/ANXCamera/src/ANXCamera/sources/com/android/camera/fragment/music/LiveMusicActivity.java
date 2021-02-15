package com.android.camera.fragment.music;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import java.util.ArrayList;
import miui.app.ActionBar;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.app.Activity;

public class LiveMusicActivity extends Activity {
    public static final int LOCAL = 1;
    public static final int ONLINE = 0;
    public static final String TAG = "LiveMusicActivity";
    private int mOldOriginVolumeStream;

    public interface Mp3DownloadCallback {
        void onCompleted(LiveMusicInfo liveMusicInfo);

        void onFailed();
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.live_music_change_bgm);
            actionBar.setFragmentViewPagerMode(this, getFragmentManager(), false);
            boolean z = !Util.isGlobalVersion();
            if (z && !C0122O00000o.instance().OOO0o0O()) {
                z = DataRepository.dataItemCloud().supportTMMusic();
            }
            final ArrayList arrayList = new ArrayList();
            if (z) {
                String string = getString(R.string.live_music_hot_title);
                Tab text = actionBar.newTab().setText(string);
                arrayList.add(text);
                getActionBar().addFragmentTab(string, text, FragmentLiveMusicOnline.class, null, false);
            }
            String string2 = getString(R.string.live_music_local_title);
            Tab text2 = actionBar.newTab().setText(string2);
            arrayList.add(text2);
            getActionBar().addFragmentTab(string2, text2, FragmentLiveMusicLocal.class, null, false);
            getActionBar().addOnFragmentViewPagerChangeListener(new FragmentViewPagerChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, boolean z, boolean z2) {
                }

                public void onPageSelected(int i) {
                    String str = LiveMusicActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onPageSelected position=");
                    sb.append(i);
                    sb.append(", title=");
                    sb.append(((Tab) arrayList.get(i)).getText());
                    Log.u(str, sb.toString());
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mOldOriginVolumeStream = getVolumeControlStream();
        setVolumeControlStream(3);
        if (getIntent().getBooleanExtra("StartActivityWhenLocked", false)) {
            setShowWhenLocked(true);
        }
        init();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        setVolumeControlStream(this.mOldOriginVolumeStream);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        DataRepository.dataItemGlobal().resetTimeOut();
    }
}
