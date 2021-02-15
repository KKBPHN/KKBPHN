package com.android.camera.fragment.clone;

import android.content.res.AssetManager;
import android.content.res.Resources;
import com.android.camera.R;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BaseFragmentUseGuide;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.xiaomi.fenshen.FenShenCam.Mode;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentCloneUseGuide extends BaseFragmentUseGuide {
    public static final String TAG = "CloneUseGuide";

    private void fillCopyUseGuide(List list) {
        List list2 = list;
        Resources resources = getResources();
        AssetManager assetManager = MultiFeatureManagerImpl.getAssetManager(requireContext());
        try {
            GuideAssetVideoItem guideAssetVideoItem = new GuideAssetVideoItem(assetManager.openFd("cn".equalsIgnoreCase(Locale.getDefault().getCountry()) ? "clone_freeze_frame_mode_use_guide.mp4" : "clone_freeze_frame_mode_use_guide_en.mp4"), this.mVideoPlayerManager, R.drawable.clone_guide_freeze_frame_cover, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.clone_guide_copy_title2), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_tip1), String.format(resources.getQuantityString(R.plurals.clone_guide_tip2, 2), new Object[]{Integer.valueOf(4)}), resources.getString(R.string.clone_guide_copy_tip3), resources.getString(R.string.clone_guide_tip4)), false);
            list2.add(guideAssetVideoItem);
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(assetManager.openFd("clone_freeze_frame_mode_samples.mp4"), this.mVideoPlayerManager, R.drawable.clone_guide_freeze_frame_samples_cover, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.clone_guide_title4), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_copy_tip6), resources.getString(R.string.clone_guide_copy_tip7), resources.getString(R.string.clone_guide_tip8)), true);
            list2.add(guideAssetVideoItem2);
        } catch (IOException e) {
            Log.w(TAG, "fillCopyUseGuide ", (Throwable) e);
        }
    }

    private void fillPhotoUseGuide(List list) {
        List list2 = list;
        Resources resources = getResources();
        try {
            GuideAssetVideoItem guideAssetVideoItem = r10;
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(MultiFeatureManagerImpl.getAssetManager(requireContext()).openFd("cn".equalsIgnoreCase(Locale.getDefault().getCountry()) ? "clone_photo_mode_use_guide.mp4" : "clone_photo_mode_use_guide_en.mp4"), this.mVideoPlayerManager, R.drawable.clone_guide_photo_cover, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.clone_guide_title2), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_tip1), String.format(resources.getQuantityString(R.plurals.clone_guide_tip2, 4), new Object[]{Integer.valueOf(4)}), resources.getString(R.string.clone_guide_tip3), resources.getString(R.string.clone_guide_tip4)), false);
            list2.add(guideAssetVideoItem);
        } catch (IOException e) {
            Log.w(TAG, "fillPhotoUseGuide ", (Throwable) e);
        }
        GuideAssetVideoItem guideAssetVideoItem3 = new GuideAssetVideoItem(null, this.mVideoPlayerManager, R.drawable.clone_guide_photo_samples_cover, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.clone_guide_title4), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_tip5), resources.getString(R.string.clone_guide_tip6), resources.getString(R.string.clone_guide_tip7), resources.getString(R.string.clone_guide_tip8)), true);
        list2.add(guideAssetVideoItem3);
    }

    private void fillVideoUseGuide(List list) {
        List list2 = list;
        Resources resources = getResources();
        AssetManager assetManager = MultiFeatureManagerImpl.getAssetManager(requireContext());
        try {
            GuideAssetVideoItem guideAssetVideoItem = new GuideAssetVideoItem(assetManager.openFd("cn".equalsIgnoreCase(Locale.getDefault().getCountry()) ? "clone_video_mode_use_guide.mp4" : "clone_video_mode_use_guide_en.mp4"), this.mVideoPlayerManager, R.drawable.clone_guide_video_cover, String.format(resources.getString(R.string.clone_guide_title1), new Object[]{Integer.valueOf(1)}), resources.getString(R.string.clone_guide_video_title2), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_tip1), String.format(resources.getQuantityString(R.plurals.clone_guide_tip2, 2), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.clone_guide_tip3), resources.getString(R.string.clone_guide_tip4)), false);
            list2.add(guideAssetVideoItem);
            GuideAssetVideoItem guideAssetVideoItem2 = new GuideAssetVideoItem(assetManager.openFd("clone_video_mode_samples.mp4"), this.mVideoPlayerManager, R.drawable.clone_guide_video_samples_cover, String.format(resources.getString(R.string.clone_guide_title3), new Object[]{Integer.valueOf(2)}), resources.getString(R.string.clone_guide_title4), mergeTextAddBlankLineIfChinese(resources.getString(R.string.clone_guide_tip5), resources.getString(R.string.clone_guide_tip6), resources.getString(R.string.clone_guide_tip7), resources.getString(R.string.clone_guide_tip8)), true);
            list2.add(guideAssetVideoItem2);
        } catch (IOException e) {
            Log.w(TAG, "fillVideoUseGuide ", (Throwable) e);
        }
    }

    private static String mergeTextAddBlankLineIfChinese(String... strArr) {
        String mergeText = Utils.mergeText(strArr);
        if (!Utils.isChineseLanguage()) {
            return mergeText;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(mergeText);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void fillList(List list) {
        if (Config.getCloneMode() == Mode.PHOTO) {
            fillPhotoUseGuide(list);
        } else if (Config.getCloneMode() == Mode.VIDEO) {
            fillVideoUseGuide(list);
        } else if (Config.getCloneMode() == Mode.MCOPY) {
            fillCopyUseGuide(list);
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_CLONE_USE_GUIDE;
    }

    public boolean onBackEvent(int i) {
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(23);
        return true;
    }
}
