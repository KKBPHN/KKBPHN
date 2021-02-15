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

public class FragmentSlowShutterUseGuide extends BaseFragmentUseGuide {
    public static final String TAG = "FragmentSlowShutterUseGuide";

    /* access modifiers changed from: protected */
    public void fillList(List list) {
        List list2 = list;
        String str = " ";
        try {
            Resources resources = getResources();
            AssetManager assetManager = MultiFeatureManagerImpl.getAssetManager(requireContext());
            GuideAssetVideoItem guideAssetVideoItem = new GuideAssetVideoItem(assetManager.openFd("slow_shutter_sport_use_guide.mp4"), this.mVideoPlayerManager, R.drawable.slow_shutter_guide_sport, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.slow_shutter_title2), Utils.mergeText(str, resources.getString(R.string.slow_shutter_tip1), resources.getString(R.string.slow_shutter_tip2), resources.getString(R.string.slow_shutter_tip3)), false);
            list2.add(guideAssetVideoItem);
            AssetFileDescriptor openFd = assetManager.openFd("slow_shutter_stop_use_guide.mp4");
            VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
            VideoPlayerManager videoPlayerManager2 = videoPlayerManager;
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(openFd, videoPlayerManager2, R.drawable.slow_shutter_guide_stop, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.slow_shutter_title4), Utils.mergeText(str, resources.getString(R.string.slow_shutter_tip4), resources.getString(R.string.slow_shutter_tip2), resources.getString(R.string.slow_shutter_tip5)), true);
            list2.add(guideAssetVideoItem2);
        } catch (IOException e) {
            Log.w(TAG, "fillSlowShutterUseGuide ", (Throwable) e);
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_SLOW_SHUTTER_USE_GUIDE;
    }

    public boolean onBackEvent(int i) {
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(35);
        return true;
    }
}
