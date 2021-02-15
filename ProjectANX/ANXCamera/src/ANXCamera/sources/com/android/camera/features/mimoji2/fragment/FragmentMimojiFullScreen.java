package com.android.camera.features.mimoji2.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.CameraIntentManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.constant.ColorConstant;
import com.android.camera.constant.ShareConstant;
import com.android.camera.customization.BGTintTextView;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.fragment.bottomlist.FragmentMimojiBottomList;
import com.android.camera.features.mimoji2.fragment.edit.FragmentMimojiEdit2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiFullScreenProtocol;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.utils.MimojiViewUtil;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.RecyclerAdapterWrapper;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.fullscreen.ShareAdapter;
import com.android.camera.fragment.fullscreen.ShareInfo;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.storage.Storage;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.ColorImageView;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import io.reactivex.Completable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import miui.view.animation.QuarticEaseInInterpolator;
import miui.view.animation.QuarticEaseOutInterpolator;

public class FragmentMimojiFullScreen extends BaseFragment implements MimojiFullScreenProtocol, HandleBackTrace, OnClickListener {
    public static final int FRAGMENT_INFO = 65523;
    private static final String TAG = "MIMOJIFullScreen";
    private Runnable mAlertTopHintHideRunnable = new O00000Oo(this);
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private ViewGroup mBottomTimbreLayout;
    private CameraSnapView mCameraSnapView;
    private ProgressBar mCancelProgress;
    private ImageView mCombineProgress;
    private ProgressBar mConcatProgress;
    /* access modifiers changed from: private */
    public AlertDialog mExitDialog;
    private FragmentMimojiBottomList mFragmentMimojiBottomList;
    private FragmentMimojiEdit2 mFragmentMimojiEdit2;
    private Handler mHandler = new Handler();
    private boolean mIsOnShare;
    private View mMImojiViewLayout;
    private ColorImageView mMimojiChangeTimbreBtn;
    private FrameLayout mMimojiChangeTimbreLayout;
    private ViewStub mMimojiViewStub;
    private ImageView mPreviewBack;
    private LottieAnimationView mPreviewCombine;
    private Bitmap mPreviewCoverBitmap;
    private ImageView mPreviewCoverView;
    private FrameLayout mPreviewLayout;
    private ImageView mPreviewShare;
    private ImageView mPreviewStart;
    private TextureVideoView mPreviewTextureView;
    private BGTintTextView mPreviewTips;
    private String mSavedPath;
    private ShareAdapter mShareAdapter;
    private View mShareCancel;
    private ViewGroup mShareLayout;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private TextView mTimeView;
    private FrameLayout mTopLayout;

    private void adjustViewBackground() {
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        int i = (uiStyle == 1 || uiStyle == 3) ? R.drawable.bg_thumbnail_background_full : R.drawable.bg_thumbnail_background_half;
        this.mPreviewBack.setBackgroundResource(i);
        this.mPreviewShare.setBackgroundResource(i);
    }

    private int alertTintColor() {
        return TintColor.isYellowTintColor() ? TintColor.tintColor() : getResources().getColor(R.color.white);
    }

    private boolean checkAndShare() {
        this.mIsOnShare = true;
        if (FileUtils.checkFileConsist(this.mSavedPath)) {
            try {
                if (FileUtils.checkFileConsist(this.mSavedPath)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Storage.DIRECTORY);
                    sb.append(File.separator);
                    sb.append(FileUtils.createtFileName("MIMOJI_", "mp4"));
                    String sb2 = sb.toString();
                    FileUtils.copyFile(new File(this.mSavedPath), new File(sb2));
                    this.mSavedPath = sb2;
                    ((ActivityBase) getActivity()).getImageSaver().addVideoSync(this.mSavedPath, Util.genContentValues(2, this.mSavedPath), false);
                    Util.shareMediaToMore(getContext(), this.mSavedPath, getString(R.string.share), true);
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private FragmentMimojiEdit2 getFragmentMiMoji() {
        String str;
        FragmentMimojiEdit2 fragmentMimojiEdit2 = this.mFragmentMimojiEdit2;
        String str2 = TAG;
        if (fragmentMimojiEdit2 == null) {
            str = "getFragmentMiMoji(): fragment is null";
        } else if (fragmentMimojiEdit2.isAdded()) {
            return this.mFragmentMimojiEdit2;
        } else {
            str = "getFragmentMiMoji(): fragment is not added yet";
        }
        Log.d(str2, str);
        return null;
    }

    private Intent getShareIntent() {
        String str = FileUtils.checkFileConsist(this.mSavedPath) ? this.mSavedPath : null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ((ActivityBase) getActivity()).getImageSaver().addVideoSync(str, Util.genContentValues(2, str), false);
        return Util.getShareMediaIntent(getContext(), str, true);
    }

    private void hideShareSheet() {
        if (this.mShareLayout.getVisibility() == 0) {
            Completable.create(new SlideOutOnSubscribe(this.mShareLayout, 80).setInterpolator(new QuarticEaseInInterpolator()).setDurationTime(200)).subscribe();
        }
    }

    private void hideTimbreLayout() {
        if (!MimojiViewUtil.getViewIsVisible(this.mMimojiChangeTimbreBtn)) {
            DataRepository.dataItemLive().getMimojiStatusManager2().setMimojiPanelState(0);
            Completable.create(new AlphaOutOnSubscribe(this.mMimojiChangeTimbreLayout).targetGone()).subscribe();
            MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, true);
        }
    }

    private void initMimojiView(View view) {
        this.mPreviewLayout = (FrameLayout) view.findViewById(R.id.live_preview_layout);
        this.mBottomTimbreLayout = (ViewGroup) view.findViewById(R.id.fl_mimoji_change_timbre);
        this.mPreviewTextureView = (TextureVideoView) view.findViewById(R.id.tvv_mimoji_preview);
        this.mMimojiChangeTimbreBtn = (ColorImageView) view.findViewById(R.id.btn_mimoji_change_timbre);
        this.mMimojiChangeTimbreLayout = (FrameLayout) view.findViewById(R.id.fl_bottom_mimoji_change_timbre);
        this.mPreviewCoverView = (ImageView) view.findViewById(R.id.image_mimoji_cover);
        this.mPreviewTips = (BGTintTextView) view.findViewById(R.id.tv_mimoji_top_tips);
        this.mConcatProgress = (ProgressBar) view.findViewById(R.id.live_concat_progress);
        this.mCombineProgress = (ImageView) view.findViewById(R.id.live_save_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.live_share_progress);
        this.mCancelProgress = (ProgressBar) view.findViewById(R.id.live_back_progress);
        this.mTimeView = (TextView) view.findViewById(R.id.live_preview_recording_time_view);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.live_preview_save_circle);
        this.mCameraSnapView.setParameters(PaintConditionReferred.create(this.mCurrentMode));
        this.mCameraSnapView.hideCirclePaintItem();
        this.mCameraSnapView.hideRoundPaintItem();
        this.mCameraSnapView.setSnapClickEnable(false);
        this.mPreviewCombine = (LottieAnimationView) view.findViewById(R.id.live_preview_save);
        this.mPreviewBack = (ImageView) view.findViewById(R.id.live_preview_back);
        this.mPreviewShare = (ImageView) view.findViewById(R.id.live_preview_share);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.live_preview_play);
        this.mShareLayout = (ViewGroup) view.findViewById(R.id.live_share_layout);
        this.mShareRecyclerView = (RecyclerView) this.mShareLayout.findViewById(R.id.live_share_list);
        this.mShareRecyclerView.setFocusable(false);
        this.mShareCancel = this.mShareLayout.findViewById(R.id.live_share_cancel);
        this.mShareCancel.setOnClickListener(this);
        this.mPreviewLayout.setOnClickListener(this);
        this.mCameraSnapView.setOnClickListener(this);
        this.mPreviewCombine.setOnClickListener(this);
        this.mPreviewBack.setOnClickListener(this);
        this.mPreviewShare.setOnClickListener(this);
        this.mPreviewStart.setOnClickListener(this);
        this.mMimojiChangeTimbreBtn.setOnClickListener(this);
        this.mTopLayout = (FrameLayout) view.findViewById(R.id.live_preview_top);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopLayout.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        this.mTopLayout.setLayoutParams(marginLayoutParams);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.live_preview_bottom_action);
        ((MarginLayoutParams) this.mBottomActionView.getLayoutParams()).height = Display.getBottomHeight();
        this.mBottomActionView.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mPreviewBack, this.mPreviewShare, this.mPreviewStart);
        FolmeUtils.touchScale(this.mPreviewCombine);
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.live_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams2.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams2.bottomMargin = Display.getBottomMargin();
        marginLayoutParams2.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        ((MarginLayoutParams) this.mTimeView.getLayoutParams()).height = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        this.mBottomActionView.setBackgroundResource(R.color.fullscreen_background);
    }

    private void onPreviewResume() {
        if (!this.mIsOnShare) {
            onCombineError();
        }
    }

    private void pausePlay() {
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor != null && mimojiVideoEditor.pausePlay()) {
            MimojiViewUtil.setViewVisible(this.mPreviewStart, true);
        }
    }

    private void removeTimbreLayout() {
        try {
            DataRepository.dataItemLive().getMimojiStatusManager2().setMimojiPanelState(0);
            if (this.mFragmentMimojiBottomList != null) {
                this.mFragmentMimojiBottomList.unRegisterProtocol();
                FragmentManager childFragmentManager = getChildFragmentManager();
                StringBuilder sb = new StringBuilder();
                sb.append(getFragmentTag());
                sb.append(this.mFragmentMimojiBottomList.getFragmentTag());
                FragmentUtils.removeFragmentByTag(childFragmentManager, sb.toString());
                this.mFragmentMimojiBottomList = null;
            }
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mimoji void removeTimbreLayout[] Exception ");
            sb2.append(e);
            Log.e(TAG, sb2.toString());
        }
    }

    private void resumePlay() {
        String str = TAG;
        Log.d(str, "mimoji void resumePlay[]");
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor == null) {
            return;
        }
        if (mimojiVideoEditor.resumePlay()) {
            MimojiViewUtil.setViewVisible(this.mPreviewStart, false);
        } else {
            Log.e(str, "mimoji void resumePlay fail");
        }
    }

    private void shareMore() {
        try {
            getContext().startActivity(Intent.createChooser(getShareIntent(), getString(R.string.live_edit_share_title)));
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "failed to share video shareMore ", (Throwable) e);
        }
    }

    private void showExitConfirm() {
        this.mPreviewBack.setVisibility(0);
        this.mCancelProgress.setVisibility(8);
        if (this.mExitDialog == null) {
            CameraStatUtils.trackLiveClick("exit");
            this.mExitDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.live_edit_exit_message), getString(R.string.live_edit_exit_confirm), new O00000o(this), null, null, getString(R.string.snap_cancel), O00000o0.INSTANCE);
            this.mExitDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMimojiFullScreen.this.mExitDialog = null;
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showShareSheet() {
        boolean z;
        String str;
        Intent shareIntent = getShareIntent();
        if (shareIntent == null) {
            onCombineError();
            return;
        }
        List<ResolveInfo> queryIntentActivities = getContext().getPackageManager().queryIntentActivities(shareIntent, 65536);
        String str2 = TAG;
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            Log.d(str2, "no IntentActivities");
            return;
        }
        ArrayList arrayList = new ArrayList();
        int length = ShareConstant.DEFAULT_SHARE_LIST.length;
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (arrayList.size() == length) {
                break;
            }
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (ShareConstant.DEFAULT_SHARE_LIST[i].equals(resolveInfo.activityInfo.name)) {
                    ShareInfo shareInfo = new ShareInfo(i, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, ShareConstant.DEFAULT_SHARE_LIST_ICON[i], ShareConstant.DEFAULT_SHARE_LIST_NAME[i]);
                    arrayList.add(shareInfo);
                    break;
                } else {
                    i++;
                }
            }
        }
        if (arrayList.isEmpty()) {
            str = "no default share entry";
        } else if (((ShareInfo) arrayList.get(0)).index <= 1 || arrayList.size() >= 2) {
            z = false;
            if (!z) {
                shareMore();
                return;
            }
            ShareInfo shareInfo2 = new ShareInfo(length + 1, CameraIntentManager.CALLER_MIUI_CAMERA, "more", R.drawable.ic_live_share_more, R.string.accessibility_more);
            arrayList.add(shareInfo2);
            Collections.sort(arrayList, new Comparator() {
                public int compare(ShareInfo shareInfo, ShareInfo shareInfo2) {
                    int i = shareInfo.index;
                    int i2 = shareInfo2.index;
                    if (i == i2) {
                        return 0;
                    }
                    return i > i2 ? 1 : -1;
                }
            });
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.live_share_item_margin);
            int max = Math.max((Display.getWindowWidth() - (dimensionPixelSize * 2)) / arrayList.size(), (int) (((float) (Display.getWindowWidth() - dimensionPixelSize)) / 5.5f));
            ShareAdapter shareAdapter = this.mShareAdapter;
            if (shareAdapter == null || shareAdapter.getItemCount() != arrayList.size()) {
                this.mShareAdapter = new ShareAdapter(arrayList, this, max);
                LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "FullScreen");
                linearLayoutManagerWrapper.setOrientation(0);
                this.mShareRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
                RecyclerAdapterWrapper recyclerAdapterWrapper = new RecyclerAdapterWrapper(this.mShareAdapter);
                Space space = new Space(getContext());
                space.setMinimumWidth(dimensionPixelSize);
                recyclerAdapterWrapper.addHeader(space);
                Space space2 = new Space(getContext());
                space2.setMinimumWidth(dimensionPixelSize);
                recyclerAdapterWrapper.addFooter(space2);
                this.mShareRecyclerView.setAdapter(recyclerAdapterWrapper);
            } else {
                this.mShareAdapter.setShareInfoList(arrayList);
                this.mShareAdapter.notifyDataSetChanged();
            }
            if (Display.getNavigationBarHeight() > 0) {
                ((MarginLayoutParams) this.mShareLayout.getLayoutParams()).bottomMargin = Display.getNavigationBarHeight();
            }
            Completable.create(new SlideInOnSubscribe(this.mShareLayout, 80).setInterpolator(new QuarticEaseOutInterpolator()).setDurationTime(200)).subscribe();
            return;
        } else {
            str = "not match default share strategy";
        }
        Log.d(str2, str);
        z = true;
        if (!z) {
        }
    }

    private void showTimbreLayout() {
        DataRepository.dataItemLive().getMimojiStatusManager2().setMimojiPanelState(3);
        MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreLayout, true);
        FragmentMimojiBottomList fragmentMimojiBottomList = this.mFragmentMimojiBottomList;
        if (fragmentMimojiBottomList == null) {
            this.mFragmentMimojiBottomList = new FragmentMimojiBottomList();
            this.mFragmentMimojiBottomList.registerProtocol();
            this.mFragmentMimojiBottomList.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentMimojiBottomList fragmentMimojiBottomList2 = this.mFragmentMimojiBottomList;
            StringBuilder sb = new StringBuilder();
            sb.append(getFragmentTag());
            sb.append(this.mFragmentMimojiBottomList.getFragmentTag());
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.fl_bottom_mimoji_change_timbre, (Fragment) fragmentMimojiBottomList2, sb.toString());
        } else {
            fragmentMimojiBottomList.refreshMimojiList();
            Completable.create(new AlphaInOnSubscribe(this.mFragmentMimojiBottomList.getView())).subscribe();
        }
        MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, false);
    }

    private void startCombine() {
        if (FileUtils.checkFileConsist(this.mSavedPath)) {
            try {
                if (this.mSavedPath.contains("mimoji_normal") || this.mSavedPath.contains("mimoji_deal")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Storage.DIRECTORY);
                    sb.append(File.separator);
                    sb.append(FileUtils.createtFileName("MIMOJI_", "mp4"));
                    String sb2 = sb.toString();
                    FileUtils.copyFile(new File(this.mSavedPath), new File(sb2));
                    this.mSavedPath = sb2;
                }
                onCombineSuccess(new String[0]);
                return;
            } catch (Exception unused) {
            }
        }
        onCombineError();
    }

    private void startPlay() {
        MimojiViewUtil.setViewVisible(this.mConcatProgress, false);
        MimojiViewUtil.setViewVisible(this.mPreviewStart, false);
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor != null) {
            mimojiVideoEditor.startPlay();
        }
    }

    public /* synthetic */ void O000000o(int i, String str) {
        if (!canProvide()) {
            onCombineError();
        } else if (this.mPreviewLayout == null || i != 0 || DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreview()) {
            onCombineError();
        } else {
            Rect previewRect = Util.getPreviewRect(getActivity());
            LayoutParams layoutParams = (LayoutParams) this.mPreviewLayout.getLayoutParams();
            layoutParams.topMargin = previewRect.top;
            layoutParams.height = previewRect.height();
            this.mPreviewLayout.setLayoutParams(layoutParams);
            boolean z = true;
            MimojiViewUtil.setViewVisible(this.mPreviewLayout, true);
            MimojiViewUtil.setViewVisible(this.mBottomTimbreLayout, true);
            MimojiViewUtil.setViewVisible(this.mPreviewTextureView, true);
            boolean z2 = false;
            if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiPanelState() == 3) {
                MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, false);
            } else {
                MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, true);
            }
            if (this.mMimojiChangeTimbreBtn != null) {
                if (DataRepository.dataItemLive().getMimojiStatusManager2().getCurrentMimojiTimbreInfo() == null) {
                    z = false;
                }
                this.mMimojiChangeTimbreBtn.setColor(z ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
            }
            MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
            if (mimojiVideoEditor != null) {
                z2 = mimojiVideoEditor.init(this.mPreviewTextureView, str);
            }
            if (z2) {
                this.mSavedPath = str;
                startPlay();
            } else {
                onCombineError();
            }
        }
    }

    public /* synthetic */ void O000OoO() {
        quitMimojiRecordPreview();
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null) {
            cameraAction.onReviewCancelClicked();
            return;
        }
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onFinish();
        }
    }

    public /* synthetic */ void O000OoOO() {
        quitMimojiRecordPreview();
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null) {
            cameraAction.onReviewDoneClicked();
        }
    }

    public /* synthetic */ void O000OoOo() {
        Log.u(TAG, "showExitConfirm onClick positive");
        CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_EXIT_CONFIRM);
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 != null) {
            mimojiAvatarEngine2.deleteMimojiCache(1);
        }
        onCombineError();
    }

    public /* synthetic */ void O00O0Oo() {
        alertTop(8, 0, 0);
    }

    public void alertTop(int i, int i2, long j) {
        Handler handler = this.mHandler;
        if (handler != null && this.mPreviewTips != null) {
            handler.removeCallbacks(this.mAlertTopHintHideRunnable);
            if (i == 0) {
                this.mPreviewTips.setText(i2);
                this.mPreviewTips.setBGColor(alertTintColor());
                this.mPreviewTips.setVisibility(0);
                if (j > 0) {
                    this.mHandler.postDelayed(this.mAlertTopHintHideRunnable, j);
                    return;
                }
                return;
            }
            this.mPreviewTips.setText("");
            this.mPreviewTips.setVisibility(8);
        }
    }

    public void concatResult(String str, int i) {
        Log.d(TAG, "mimoji void concatResult[]");
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C0256O00000oO(this, i, str));
        }
    }

    public int getFragmentInto() {
        return 65523;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_mimoji_full_screen;
    }

    public String getMimojiVideoSavePath() {
        return this.mSavedPath;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mMimojiViewStub = (ViewStub) view.findViewById(R.id.mimoji_record_preview);
    }

    public boolean isMimojiRecordPreviewShowing() {
        return MimojiViewUtil.getViewIsVisible(this.mMImojiViewLayout);
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mFragmentMimojiEdit2 == null && C0122O00000o.instance().OOO0ooo()) {
            this.mFragmentMimojiEdit2 = new FragmentMimojiEdit2();
            this.mFragmentMimojiEdit2.registerProtocol();
            this.mFragmentMimojiEdit2.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentMimojiEdit2 fragmentMimojiEdit2 = this.mFragmentMimojiEdit2;
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.fragment_full_screen_mimoji_edit, (Fragment) fragmentMimojiEdit2, fragmentMimojiEdit2.getFragmentTag());
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (this.mFragmentMimojiEdit2 == null && C0122O00000o.instance().OOO0ooo()) {
            this.mFragmentMimojiEdit2 = new FragmentMimojiEdit2();
            this.mFragmentMimojiEdit2.registerProtocol();
            this.mFragmentMimojiEdit2.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentMimojiEdit2 fragmentMimojiEdit2 = this.mFragmentMimojiEdit2;
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.fragment_full_screen_mimoji_edit, (Fragment) fragmentMimojiEdit2, fragmentMimojiEdit2.getFragmentTag());
        }
    }

    public boolean onBackEvent(int i) {
        if (i != 1) {
            return false;
        }
        if (MimojiViewUtil.getViewIsVisible(this.mMImojiViewLayout)) {
            if (MimojiViewUtil.getViewIsVisible(this.mShareLayout)) {
                hideShareSheet();
            } else {
                showExitConfirm();
            }
            return true;
        }
        FragmentMimojiEdit2 fragmentMiMoji = getFragmentMiMoji();
        if (fragmentMiMoji != null) {
            return fragmentMiMoji.onBackEvent(i);
        }
        return false;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (!(this.mConcatProgress.getVisibility() == 0 || ((mimojiVideoEditor != null && !mimojiVideoEditor.isAvaliable()) || this.mCombineProgress.getVisibility() == 0 || this.mShareProgress.getVisibility() == 0 || this.mCancelProgress.getVisibility() == 0))) {
            int id = view.getId();
            String str = TAG;
            switch (id) {
                case R.id.btn_mimoji_change_timbre /*2131296395*/:
                    Log.u(str, "onClick: btn_mimoji_change_timbre");
                    showTimbreLayout();
                    break;
                case R.id.live_preview_back /*2131296712*/:
                    Log.u(str, "onClick: live_preview_back");
                    this.mCancelProgress.setVisibility(0);
                    this.mPreviewBack.setVisibility(8);
                    showExitConfirm();
                    break;
                case R.id.live_preview_bottom_action /*2131296713*/:
                    Log.u(str, "onClick: live_preview_bottom_action");
                    break;
                case R.id.live_preview_layout /*2131296715*/:
                    Log.u(str, "onClick: live_preview_layout");
                    FragmentMimojiBottomList fragmentMimojiBottomList = this.mFragmentMimojiBottomList;
                    if (fragmentMimojiBottomList != null && MimojiViewUtil.getViewIsVisible(fragmentMimojiBottomList.getView())) {
                        hideTimbreLayout();
                        return;
                    }
                case R.id.live_preview_play /*2131296716*/:
                    Log.u(str, "onClick: live_preview_play");
                    hideShareSheet();
                    startPlay();
                    break;
                case R.id.live_preview_save /*2131296718*/:
                case R.id.live_preview_save_circle /*2131296719*/:
                    Log.u(str, "onClick: live_preview_save");
                    CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_SAVE);
                    pausePlay();
                    this.mPreviewStart.setVisibility(8);
                    this.mPreviewCombine.setVisibility(8);
                    MimojiViewUtil.setViewVisible(this.mCombineProgress, true);
                    startCombine();
                    break;
                case R.id.live_preview_share /*2131296720*/:
                    Log.u(str, "onClick: live_preview_share");
                    if (this.mConcatProgress.getVisibility() == 0) {
                        Log.d(str, "concat not finished, skip share~");
                        return;
                    }
                    CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_SHARE);
                    if (!checkAndShare()) {
                        Log.d(str, "uri null");
                    }
                    break;
                case R.id.live_share_cancel /*2131296724*/:
                    Log.u(str, "onClick: live_share_cancel");
                    hideShareSheet();
                    break;
                case R.id.live_share_item /*2131296726*/:
                    Log.u(str, "onClick: live_share_item");
                    ShareInfo shareInfo = (ShareInfo) view.getTag();
                    hideShareSheet();
                    StringBuilder sb = new StringBuilder();
                    sb.append(Live.VALUE_LIVE_CLICK_PLAY_SHARE_SHEET);
                    sb.append(shareInfo.index);
                    CameraStatUtils.trackLiveClick(sb.toString());
                    if (shareInfo.className.equals("more")) {
                        shareMore();
                        break;
                    } else {
                        String str2 = null;
                        if (FileUtils.checkFileConsist(this.mSavedPath)) {
                            str2 = this.mSavedPath;
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            try {
                                ((ActivityBase) getActivity()).getImageSaver().addVideoSync(str2, Util.genContentValues(2, str2), false);
                                Util.startShareMedia(shareInfo.packageName, shareInfo.className, getContext(), str2, true);
                                break;
                            } catch (Exception unused) {
                            }
                        }
                    }
            }
        }
        onCombineError();
    }

    public void onCombineError() {
        if (this.mCurrentMode == 184 && getActivity() != null && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
            getActivity().runOnUiThread(new C0257O00000oo(this));
        }
    }

    public void onCombineSuccess(String... strArr) {
        if (this.mCurrentMode == 184 && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
            Log.d(TAG, "combineSuccess");
            if (strArr != null && strArr.length > 0) {
                this.mSavedPath = strArr[0];
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(new O000000o(this));
            }
        }
    }

    public void onMimojiSaveToLocalFinished(Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append("MIMOJI onMimojiSaveToLocalFinished");
        sb.append(uri);
        Log.d(TAG, sb.toString());
    }

    public void onPause() {
        super.onPause();
        pausePlay();
    }

    public void onResume() {
        super.onResume();
        onPreviewResume();
    }

    public void onStop() {
        onCombineError();
        super.onStop();
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i2 == 3) {
            AlertDialog alertDialog = this.mExitDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mExitDialog = null;
            }
            onCombineError();
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        View view = this.mMImojiViewLayout;
        if (view != null && view.getVisibility() == 0) {
            list.add(this.mPreviewStart);
            list.add(this.mCameraSnapView);
            list.add(this.mPreviewCombine);
            list.add(this.mPreviewBack);
            list.add(this.mPreviewShare);
            ColorImageView colorImageView = this.mMimojiChangeTimbreBtn;
            if (colorImageView != null) {
                list.add(colorImageView);
            }
            ColorImageView colorImageView2 = this.mMimojiChangeTimbreBtn;
            if (colorImageView2 != null) {
                list.add(colorImageView2);
            }
        }
        FragmentMimojiBottomList fragmentMimojiBottomList = this.mFragmentMimojiBottomList;
        if (fragmentMimojiBottomList != null) {
            fragmentMimojiBottomList.provideRotateItem(list, i);
        }
    }

    public void quitMimojiRecordPreview() {
        MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        if (mimojiStatusManager2.isInMimojiPreviewPlay()) {
            mimojiStatusManager2.setMode(2);
        }
        this.mIsOnShare = false;
        setPreviewCover(null);
        showPreviewCover(false);
        MimojiViewUtil.setViewVisible(this.mConcatProgress, false);
        MimojiViewUtil.setViewVisible(this.mCombineProgress, false);
        MimojiViewUtil.setViewVisible(this.mShareProgress, false);
        MimojiViewUtil.setViewVisible(this.mMImojiViewLayout, false);
        removeTimbreLayout();
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor != null) {
            mimojiVideoEditor.onDestory();
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(249, this);
        registerBackStack(modeCoordinator, this);
    }

    public void setPreviewCover(Bitmap bitmap) {
        Bitmap bitmap2 = this.mPreviewCoverBitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mPreviewCoverBitmap.recycle();
        }
        this.mPreviewCoverBitmap = bitmap;
    }

    public void showPreviewCover(boolean z) {
        boolean z2;
        ImageView imageView;
        if (z) {
            Bitmap bitmap = this.mPreviewCoverBitmap;
            if (bitmap != null && !bitmap.isRecycled()) {
                ImageView imageView2 = this.mPreviewCoverView;
                if (imageView2 != null) {
                    imageView2.setImageBitmap(this.mPreviewCoverBitmap);
                    imageView = this.mPreviewCoverView;
                    z2 = true;
                    MimojiViewUtil.setViewVisible(imageView, z2);
                }
            }
        }
        imageView = this.mPreviewCoverView;
        z2 = false;
        MimojiViewUtil.setViewVisible(imageView, z2);
    }

    public void startMimojiRecordPreview() {
        this.mSavedPath = null;
        if (this.mMImojiViewLayout == null) {
            this.mMImojiViewLayout = this.mMimojiViewStub.inflate();
            initMimojiView(this.mMImojiViewLayout);
        }
        MimojiViewUtil.setViewVisible(this.mPreviewLayout, false);
        MimojiViewUtil.setViewVisible(this.mCombineProgress, false);
        MimojiViewUtil.setViewVisible(this.mShareProgress, false);
        MimojiViewUtil.setViewVisible(this.mCancelProgress, false);
        MimojiViewUtil.setViewVisible(this.mShareLayout, false);
        MimojiViewUtil.setViewVisible(this.mMImojiViewLayout, true);
        showPreviewCover(true);
        adjustViewBackground();
        if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiPanelState() == 3) {
            MimojiViewUtil.setViewVisible(this.mBottomTimbreLayout, true);
            MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreLayout, true);
            MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, false);
        } else {
            MimojiViewUtil.setViewVisible(this.mBottomTimbreLayout, false);
            MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreLayout, false);
            MimojiViewUtil.setViewVisible(this.mMimojiChangeTimbreBtn, true);
        }
        ViewCompat.setRotation(this.mPreviewStart, (float) this.mDegree);
        ViewCompat.setRotation(this.mCameraSnapView, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewCombine, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewBack, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewShare, (float) this.mDegree);
        Completable.create(new AlphaInOnSubscribe(this.mCameraSnapView)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewCombine)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewBack)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewStart)).subscribe();
        this.mPreviewCombine.setScale(0.38f);
        this.mPreviewCombine.O0000O0o((int) R.raw.vv_save);
        this.mPreviewCombine.O0000oO();
        if (((ActivityBase) getContext()).startFromKeyguard()) {
            this.mPreviewShare.setVisibility(8);
        } else {
            Completable.create(new AlphaInOnSubscribe(this.mPreviewShare)).subscribe();
        }
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 != null) {
            this.mTimeView.setText(mimojiAvatarEngine2.getTimeValue());
            this.mTimeView.setVisibility(0);
        }
        MimojiViewUtil.setViewVisible(this.mPreviewStart, false);
        MimojiViewUtil.setViewVisible(this.mConcatProgress, true);
    }

    public void startMimojiRecordSaving() {
        this.mCameraSnapView.performClick();
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(249, this);
        removeTimbreLayout();
        unRegisterBackStack(modeCoordinator, this);
    }
}
