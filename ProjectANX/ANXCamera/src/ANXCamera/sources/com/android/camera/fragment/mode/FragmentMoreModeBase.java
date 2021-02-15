package com.android.camera.fragment.mode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.global.ComponentModuleList;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.mode.ModeAdapter.PlayLoad;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.ui.ModeBackground;
import com.android.camera.ui.WaterBox;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public abstract class FragmentMoreModeBase extends BaseFragment implements HandleBackTrace, OnClickListener, IMoreMode {
    private static final int DOWN_POS_IDLE = -1;
    private static final int DOWN_POS_NOT_FOUND = -2;
    private static final String TAG = "MoreModeBase";
    protected ComponentModuleList mComponentModuleList;
    /* access modifiers changed from: private */
    public AlertDialog mDownloadCancelDialog;
    /* access modifiers changed from: private */
    public AlertDialog mDownloadConfirmDialog;
    private int mDownloaderPosition = -1;
    protected String mDownloadingFeature;
    protected View mRootView;
    private VMFeature mVMFeature;

    private boolean checkInstallState(int i) {
        String featureNameByLocalMode = VMFeature.getFeatureNameByLocalMode(i);
        if (TextUtils.isEmpty(featureNameByLocalMode) || ((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).hasFeatureInstalled(featureNameByLocalMode)) {
            return true;
        }
        confirmDownload(featureNameByLocalMode);
        return false;
    }

    private void confirmDownload(final String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("confirmDownload: ");
        sb.append(str);
        Log.u(TAG, sb.toString());
        this.mDownloadConfirmDialog = ((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).checkAndShowDownloadConfirmDialog(str, getContext(), true, new Runnable() {
            public void run() {
                Log.u(FragmentMoreModeBase.TAG, "onClick confirmDownload confirm");
                FragmentMoreModeBase.this.onDownloadAndInstallStart(str);
            }
        });
        AlertDialog alertDialog = this.mDownloadConfirmDialog;
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMoreModeBase.this.mDownloadConfirmDialog = null;
                }
            });
        }
    }

    private ModeAdapter createModeAdapter() {
        return new ModeAdapter(getContext(), this);
    }

    private int getDownloadPosition(int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= getComponentModuleList().getMoreItems().size()) {
                break;
            } else if (!((ComponentDataItem) getComponentModuleList().getMoreItems().get(i2)).mValue.equals(String.valueOf(i))) {
                i2++;
            } else if (getType() == 0 || getType() == 3) {
                return i2 + 1;
            } else {
                if (getType() == 1) {
                    return i2;
                }
            }
        }
        return -2;
    }

    private void notifyDownloadProgressChange(int i, int i2) {
        PlayLoad playLoad = new PlayLoad(1);
        playLoad.progress = i2;
        if (this.mDownloaderPosition == -1) {
            this.mDownloaderPosition = getDownloadPosition(i);
            StringBuilder sb = new StringBuilder();
            sb.append("start down position ");
            sb.append(this.mDownloaderPosition);
            Log.d(TAG, sb.toString());
        }
        int i3 = this.mDownloaderPosition;
        if (i3 != -1 && i3 != -2) {
            getModeAdapter().notifyItemChanged(this.mDownloaderPosition, playLoad);
        }
    }

    /* access modifiers changed from: private */
    public void onDownloadAndInstallStart(String str) {
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
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                String str = (String) entry.getKey();
                int intValue = ((Integer) entry.getValue()).intValue();
                int localModeByFeatureName = VMFeature.getLocalModeByFeatureName(str);
                int scope = VMFeature.getScope(intValue);
                String str2 = TAG;
                if (scope == 16) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onStateChange = ");
                    sb.append(intValue);
                    sb.append(", mode = ");
                    sb.append(localModeByFeatureName);
                    Log.d(str2, sb.toString());
                    switch (intValue) {
                        case 17:
                            if (this.mDownloaderPosition != -1) {
                                break;
                            } else {
                                this.mDownloaderPosition = getDownloadPosition(localModeByFeatureName);
                                break;
                            }
                        case 18:
                            this.mDownloadingFeature = null;
                            notifyDownloadProgressChange(localModeByFeatureName, 0);
                            getModeAdapter().notifyDataSetChanged();
                            this.mDownloaderPosition = -1;
                            break;
                        case 19:
                            break;
                        case 21:
                            getModeAdapter().notifyDataSetChanged();
                            this.mDownloaderPosition = -1;
                            onInstalledOK(localModeByFeatureName, true);
                            break;
                    }
                } else if (scope == 256) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onStateError: ");
                    sb2.append(intValue);
                    Log.d(str2, sb2.toString());
                    notifyDownloadProgressChange(localModeByFeatureName, 0);
                    getModeAdapter().notifyDataSetChanged();
                    this.mDownloaderPosition = -1;
                    onInstallFailed();
                } else if (scope == 4096) {
                    notifyDownloadProgressChange(localModeByFeatureName, VMFeature.getDownloadingProgress(intValue));
                    if (this.mDownloadingFeature == null) {
                        onDownloadAndInstallStart(str);
                    }
                }
            }
        }
    }

    private void onInstalledOK(int i, boolean z) {
        this.mDownloadingFeature = null;
        AlertDialog alertDialog = this.mDownloadCancelDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDownloadCancelDialog = null;
        }
        if (isResumed() && !z) {
            hide();
            ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
            if (modeChangeController != null) {
                modeChangeController.changeModeByNewMode(i, getComponentModuleList().geItemStringName(i, true), 0);
            }
        }
    }

    public /* synthetic */ void O00000oo(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public ComponentModuleList getComponentModuleList() {
        return this.mComponentModuleList;
    }

    public ModeAdapter getModeAdapter() {
        return (ModeAdapter) getModeList(this.mRootView).getAdapter();
    }

    public abstract boolean hide();

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        RecyclerView modeList = getModeList(view);
        modeList.setLayoutManager(createLayoutManager(getContext()));
        if (modeList.getItemDecorationCount() == 0) {
            modeList.addItemDecoration(createModeItemDecoration(getContext(), this));
        }
        ModeAdapter createModeAdapter = createModeAdapter();
        createModeAdapter.setRotate(this.mDegree);
        modeList.setAdapter(createModeAdapter);
        modeList.setClickable(false);
    }

    public boolean modeShouldDownload(int i) {
        String featureNameByLocalMode = VMFeature.getFeatureNameByLocalMode(i);
        if (TextUtils.isEmpty(featureNameByLocalMode)) {
            return false;
        }
        MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
        if (multiFeatureManager == null || multiFeatureManager.getInstalledFeatures() == null || multiFeatureManager.getInstalledFeatures().contains(featureNameByLocalMode)) {
            return false;
        }
        return this.mVMFeature.getState().get(featureNameByLocalMode) == null || ((Integer) this.mVMFeature.getState().get(featureNameByLocalMode)).intValue() != 21;
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        getModeAdapter().notifyDataSetChanged();
    }

    public boolean onBackEvent(int i) {
        if (i != 1 || TextUtils.isEmpty(this.mDownloadingFeature)) {
            return false;
        }
        showDownloadCancelDialog();
        return true;
    }

    public void onClick(View view) {
        int i;
        switch (view.getId()) {
            case R.id.mode_item /*2131296842*/:
            case R.id.mode_item_new /*2131296843*/:
                boolean isEmpty = TextUtils.isEmpty(this.mDownloadingFeature);
                String str = TAG;
                if (!isEmpty) {
                    View findViewById = view.findViewById(R.id.mode_bg);
                    if (findViewById instanceof ModeBackground) {
                        ModeBackground modeBackground = (ModeBackground) findViewById;
                        StringBuilder sb = new StringBuilder();
                        sb.append("old click downloading: ");
                        sb.append(modeBackground.getProgress());
                        Log.u(str, sb.toString());
                        i = modeBackground.getProgress();
                    } else if (findViewById instanceof WaterBox) {
                        WaterBox waterBox = (WaterBox) findViewById;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("new click downloading: ");
                        sb2.append(waterBox.getValue());
                        Log.u(str, sb2.toString());
                        i = (int) (waterBox.getValue() * 100.0f);
                    } else {
                        i = 100;
                    }
                    if (i < 100) {
                        ToastUtils.showToast(getContext(), getString(R.string.update_loading, Integer.valueOf(i)), 80);
                        return;
                    }
                }
                int parseInt = Integer.parseInt(((ComponentDataItem) view.getTag()).mValue);
                Log.u(str, String.format(Locale.ENGLISH, "onClick mode_item 0x%x", new Object[]{Integer.valueOf(parseInt)}));
                if (parseInt != 255) {
                    if (checkInstallState(parseInt)) {
                        onInstalledOK(parseInt, false);
                        break;
                    } else {
                        return;
                    }
                } else {
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.configModeEdit();
                        break;
                    }
                }
                break;
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mComponentModuleList = DataRepository.dataItemGlobal().getComponentModuleList();
        MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
        if (multiFeatureManager != null) {
            multiFeatureManager.init();
        }
        this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
        this.mVMFeature.startObservable(this, new O000000o(this));
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        return null;
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

    public void provideOrientationChanged(int i, List list, int i2) {
        super.provideOrientationChanged(i, list, i2);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        this.mDegree = i;
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    public void showDownloadCancelDialog() {
        this.mDownloadCancelDialog = ((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).showDownloadCancelDialog(this.mDownloadingFeature, getContext(), true, new Runnable() {
            public void run() {
                Log.u(FragmentMoreModeBase.TAG, "onClick showDownloadCancelDialog cancel");
                FragmentMoreModeBase.this.mDownloadingFeature = null;
            }
        });
        AlertDialog alertDialog = this.mDownloadCancelDialog;
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMoreModeBase.this.mDownloadCancelDialog = null;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}
