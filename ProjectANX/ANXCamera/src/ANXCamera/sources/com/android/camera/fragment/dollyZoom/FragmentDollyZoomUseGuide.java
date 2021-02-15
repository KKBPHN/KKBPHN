package com.android.camera.fragment.dollyZoom;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import com.android.camera.R;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BaseFragmentUseGuide;
import com.android.camera.fragment.clone.GuideAssetVideoItem;
import com.android.camera.fragment.clone.Utils;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentDollyZoomUseGuide extends BaseFragmentUseGuide {
    public static final String TAG = "FragmentDollyZoomUseGuide";

    /* access modifiers changed from: protected */
    public void fillList(List list) {
        int i;
        String str;
        List list2 = list;
        String str2 = "";
        try {
            Resources resources = getResources();
            if ("cn".equalsIgnoreCase(Locale.getDefault().getCountry())) {
                str = "dollyzoom/dolly_zoom_mode_use_guide.mp4";
                i = R.drawable.dolly_zoom_guide_video_cover;
            } else {
                str = "dollyzoom/dolly_zoom_mode_use_guide_en.mp4";
                i = R.drawable.dolly_zoom_guide_video_cover_en;
            }
            int i2 = i;
            GuideAssetVideoItem guideAssetVideoItem = new GuideAssetVideoItem(getContext().getAssets().openFd(str), this.mVideoPlayerManager, i2, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.dolly_zoom_guide_video_title1), Utils.mergeText(resources.getString(R.string.dolly_zoom_guide_tip1), resources.getString(R.string.dolly_zoom_guide_tip2), resources.getString(R.string.dolly_zoom_guide_tip3), resources.getString(R.string.dolly_zoom_guide_tip4), resources.getString(R.string.dolly_zoom_guide_tip5), resources.getString(R.string.dolly_zoom_guide_tip6)), false);
            list2.add(guideAssetVideoItem);
            AssetFileDescriptor openFd = getContext().getAssets().openFd("dollyzoom/dolly_zoom_mode_samples.mp4");
            VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
            VideoPlayerManager videoPlayerManager2 = videoPlayerManager;
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(openFd, videoPlayerManager2, R.drawable.dolly_zoom_guide_video_samples_cover, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.dolly_zoom_guide_video_title2), Utils.mergeText(resources.getString(R.string.dolly_zoom_guide_tip7), resources.getString(R.string.dolly_zoom_guide_tip8), resources.getString(R.string.dolly_zoom_guide_tip9), resources.getString(R.string.dolly_zoom_guide_tip10), str2, str2), true);
            list2.add(guideAssetVideoItem2);
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("openFd failed ");
            sb.append(e.getMessage());
            Log.w(TAG, sb.toString());
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_DOLLY_ZOOM_USE_GUIDE;
    }

    public boolean onBackEvent(int i) {
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(39);
        return true;
    }
}
