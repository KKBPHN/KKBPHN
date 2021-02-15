package com.android.camera.fragment.clone;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.android.camera.R;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BaseFragmentUseGuide;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentTimeFreezeUseGuide extends BaseFragmentUseGuide {
    public static final String TAG = "FragmentTimeFreezeUseGuide";

    /* access modifiers changed from: protected */
    public void fillList(List list) {
        int i;
        String str;
        List list2 = list;
        try {
            Resources resources = getResources();
            AssetManager assetManager = MultiFeatureManagerImpl.getAssetManager(requireContext());
            if ("cn".equalsIgnoreCase(Locale.getDefault().getCountry())) {
                str = "time_freeze_use_guide.mp4";
                i = R.drawable.time_freeze_use_guide_cover;
            } else {
                str = "time_freeze_use_guide_en.mp4";
                i = R.drawable.time_freeze_use_guide_cover_en;
            }
            int i2 = i;
            GuideAssetVideoItem guideAssetVideoItem = new GuideAssetVideoItem(assetManager.openFd(str), this.mVideoPlayerManager, i2, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.timefreeze_guide_title), Utils.mergeText(String.format(resources.getQuantityString(R.plurals.timefreeze_guide_tip1, 1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.timefreeze_guide_tip2), resources.getString(R.string.timefreeze_guide_tip3), ""), false);
            list2.add(guideAssetVideoItem);
            AssetFileDescriptor openFd = assetManager.openFd("time_freeze_use_samples.mp4");
            VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
            VideoPlayerManager videoPlayerManager2 = videoPlayerManager;
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(openFd, videoPlayerManager2, R.drawable.time_freeze_use_samples_cover, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.clone_guide_title4), Utils.mergeText(resources.getString(R.string.timefreeze_guide_tip4), resources.getString(R.string.timefreeze_guide_tip5), resources.getString(R.string.timefreeze_guide_tip6), resources.getString(R.string.timefreeze_guide_tip7)), true);
            list2.add(guideAssetVideoItem2);
        } catch (IOException unused) {
            Log.e(TAG, "fill List exception.");
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_TIME_FREEZE_USE_GUIDE;
    }

    public boolean onBackEvent(int i) {
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(38);
        return true;
    }
}
