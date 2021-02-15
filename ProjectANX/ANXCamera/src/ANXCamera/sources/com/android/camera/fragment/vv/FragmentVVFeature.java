package com.android.camera.fragment.vv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.IdRes;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.O00000oO.C0055O00000Oo;
import com.airbnb.lottie.O00000oO.C0060O0000Ooo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.android.camera.Camera;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.data.observeable.VMFeature.FeatureModule;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import java.util.HashMap;
import java.util.Map.Entry;

public class FragmentVVFeature extends BaseFragment implements OnClickListener, HandleBackTrace {
    public static final String TAG = "VVFeature";
    /* access modifiers changed from: private */
    public AlertDialog mDownloadCancelDialog;
    /* access modifiers changed from: private */
    public AlertDialog mDownloadConfirmDialog;
    /* access modifiers changed from: private */
    public String mDownloadingFeature;
    private FeatureInstallListener mFeatureInstallListener;
    @FeatureModule
    public String mFeatureName;
    private int mFixedLayoutMargin;
    private LottieAnimationView mLoadingImage;
    private TextView mLoadingText;
    @IdRes
    private int mSrcContainerViewId;
    private int mSrcFragmentInfo;
    /* access modifiers changed from: private */
    public VMFeature mVMFeature;

    public interface FeatureInstallListener {
        void onInstalled(String str);
    }

    static /* synthetic */ ColorFilter O00000Oo(C0055O00000Oo o00000Oo) {
        return new PorterDuffColorFilter(TintColor.tintColor(), Mode.SRC_ATOP);
    }

    private void initFeatureLayout() {
        this.mDownloadingFeature = null;
        StringBuilder sb = new StringBuilder();
        sb.append("initFeatureLayout: ");
        sb.append(this.mFeatureName);
        Log.d(TAG, sb.toString());
        String geItemStringName = DataRepository.dataItemGlobal().getComponentModuleList().geItemStringName(VMFeature.getLocalModeByFeatureName(this.mFeatureName), false);
        this.mLoadingText.setText(getString(R.string.update_start, geItemStringName));
        this.mLoadingImage.cancelAnimation();
        this.mLoadingImage.clearAnimation();
        this.mLoadingImage.setImageResource(R.drawable.ic_vector_download_max);
        this.mLoadingImage.setColorFilter(TintColor.tintColor());
    }

    private void onDownloadAndInstallStart(String str) {
        Log.d(TAG, "onDownloadStart");
        this.mDownloadingFeature = str;
    }

    private void onInstallFailed() {
        this.mDownloadingFeature = null;
        AlertDialog alertDialog = this.mDownloadCancelDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDownloadCancelDialog = null;
        }
        Toast.makeText(getContext(), R.string.live_music_network_exception, 0).show();
        initFeatureLayout();
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                String str = (String) entry.getKey();
                if (str.equals(this.mFeatureName)) {
                    int intValue = ((Integer) entry.getValue()).intValue();
                    int scope = VMFeature.getScope(intValue);
                    String str2 = TAG;
                    if (scope == 16) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onStateChange: ");
                        sb.append(intValue);
                        Log.d(str2, sb.toString());
                        switch (intValue) {
                            case 17:
                            case 19:
                                break;
                            case 18:
                                initFeatureLayout();
                                break;
                            case 21:
                                this.mLoadingText.setText(getString(R.string.update_loading, Integer.valueOf(100)));
                                this.mLoadingText.postDelayed(new Runnable() {
                                    public void run() {
                                        FragmentVVFeature.this.mVMFeature.updateState(FragmentVVFeature.this.mFeatureName, Integer.valueOf(22));
                                        FragmentVVFeature.this.mVMFeature.removeFeature(FragmentVVFeature.this.mFeatureName);
                                    }
                                }, 800);
                                this.mLoadingImage.clearAnimation();
                                this.mLoadingImage.O0000O0o((int) R.raw.resource_anim_finish_max);
                                this.mLoadingImage.setScaleType(ScaleType.CENTER_CROP);
                                this.mLoadingImage.setProgress(1.0f);
                                this.mLoadingImage.O000000o(new C0102O00000oO("**"), (Object) C0087O000Ooo0.OO00ooO, (C0060O0000Ooo) O00000o0.INSTANCE);
                                this.mLoadingImage.O0000oO();
                                break;
                            case 22:
                                onInstalled(str);
                                break;
                        }
                    } else if (scope == 256) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onStateError: ");
                        sb2.append(intValue);
                        Log.d(str2, sb2.toString());
                        onInstallFailed();
                    } else if (scope == 4096) {
                        int downloadingProgress = VMFeature.getDownloadingProgress(intValue);
                        if (downloadingProgress == 0) {
                            this.mLoadingImage.setImageResource(R.drawable.ic_vector_loading_max);
                            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
                            rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                            rotateAnimation.setInterpolator(new LinearInterpolator());
                            rotateAnimation.setRepeatMode(1);
                            rotateAnimation.setRepeatCount(-1);
                            this.mLoadingImage.startAnimation(rotateAnimation);
                        }
                        if (this.mDownloadingFeature == null) {
                            onDownloadAndInstallStart(str);
                        }
                        this.mLoadingText.setText(getString(R.string.update_loading, Integer.valueOf(downloadingProgress)));
                    }
                }
            }
        }
    }

    private void onInstalled(String str) {
        this.mDownloadingFeature = null;
        AlertDialog alertDialog = this.mDownloadCancelDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDownloadCancelDialog = null;
        }
        View view = getView();
        if (getView() != null) {
            view.setVisibility(8);
        }
        if (this.mFeatureInstallListener != null) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            this.mFeatureInstallListener.onInstalled(str);
            return;
        }
        ((Camera) getContext()).onModeSelected(StartControl.create(this.mCurrentMode).setViewConfigType(2).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
    }

    private void showDownloadCancelDialog() {
        this.mDownloadCancelDialog = ((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).showDownloadCancelDialog(this.mDownloadingFeature, getActivity(), false, new Runnable() {
            public void run() {
                Log.u(FragmentVVFeature.TAG, "onClick showDownloadCancelDialog cancel");
                FragmentVVFeature.this.mDownloadingFeature = null;
            }
        });
        AlertDialog alertDialog = this.mDownloadCancelDialog;
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentVVFeature.this.mDownloadCancelDialog = null;
                }
            });
        }
    }

    public /* synthetic */ void O0000OOo(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public /* synthetic */ void O00O0O0o() {
        Log.u(TAG, "onClick onDownloadAndInstallStart");
        onDownloadAndInstallStart(this.mFeatureName);
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_VV_FEATURE;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_vv_feature;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        if (TextUtils.isEmpty(this.mFeatureName)) {
            this.mFeatureName = VMFeature.getFeatureNameByLocalMode(this.mCurrentMode);
            if (!TextUtils.isEmpty(this.mFeatureName)) {
                this.mFixedLayoutMargin = Display.getBottomHeight();
            } else {
                return;
            }
        }
        register(ModeCoordinatorImpl.getInstance());
        ((MarginLayoutParams) view.getLayoutParams()).bottomMargin = this.mFixedLayoutMargin + getResources().getDimensionPixelSize(R.dimen.vv_start_radius);
        this.mLoadingText = (TextView) view.findViewById(R.id.vv_feature_text);
        this.mLoadingText.setSelected(true);
        this.mLoadingImage = (LottieAnimationView) view.findViewById(R.id.vv_feature_loading);
        View findViewById = view.findViewById(R.id.vv_feature_layout);
        findViewById.setOnClickListener(this);
        FolmeUtils.touchScaleTint(findViewById);
        initFeatureLayout();
        if (this.mVMFeature == null) {
            this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
            this.mVMFeature.startObservable(this, new O00000Oo(this));
        }
    }

    public boolean onBackEvent(int i) {
        if (i != 1 || TextUtils.isEmpty(this.mDownloadingFeature)) {
            return false;
        }
        showDownloadCancelDialog();
        return true;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.vv_feature_layout && TextUtils.isEmpty(this.mDownloadingFeature)) {
            StringBuilder sb = new StringBuilder();
            sb.append("confirmDownload : ");
            sb.append(this.mFeatureName);
            Log.u(TAG, sb.toString());
            this.mDownloadConfirmDialog = ((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).checkAndShowDownloadConfirmDialog(this.mFeatureName, getContext(), false, new O000000o(this));
            AlertDialog alertDialog = this.mDownloadConfirmDialog;
            if (alertDialog != null) {
                alertDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        FragmentVVFeature.this.mDownloadConfirmDialog = null;
                    }
                });
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mLoadingImage.clearAnimation();
    }

    public void onPause() {
        super.onPause();
        AlertDialog alertDialog = this.mDownloadConfirmDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDownloadConfirmDialog = null;
        }
        AlertDialog alertDialog2 = this.mDownloadCancelDialog;
        if (alertDialog2 != null) {
            alertDialog2.dismiss();
            this.mDownloadCancelDialog = null;
        }
    }

    public void onStop() {
        super.onStop();
        unRegister(ModeCoordinatorImpl.getInstance());
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
    }

    public void setFeatureInstallListener(FeatureInstallListener featureInstallListener) {
        this.mFeatureInstallListener = featureInstallListener;
    }

    public void setFeatureName(@FeatureModule String str) {
        this.mFeatureName = str;
    }

    public void setFixedMargin(int i) {
        this.mFixedLayoutMargin = i;
    }

    public void setFrom(int i, int i2) {
        this.mSrcContainerViewId = i;
        this.mSrcFragmentInfo = i2;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}
