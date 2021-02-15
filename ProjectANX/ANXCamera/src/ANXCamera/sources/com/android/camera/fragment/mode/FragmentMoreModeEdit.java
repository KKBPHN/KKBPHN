package com.android.camera.fragment.mode;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.ModeEditorActivity;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.ThermalHelper;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.EditMode;
import java.util.List;

public class FragmentMoreModeEdit extends FragmentMoreModeBase implements OnClickListener, HandleBackTrace {
    private static final String TAG = "MoreModeEdit";
    /* access modifiers changed from: private */
    public AlertDialog mExitDialog;

    private void confirmEdit() {
        List createChangeItems = getModeAdapter().createChangeItems();
        int[] iArr = new int[createChangeItems.size()];
        for (int i = 0; i < createChangeItems.size(); i++) {
            iArr[i] = Integer.parseInt(((ComponentDataItem) createChangeItems.get(i)).mValue);
        }
        DataRepository.dataItemGlobal().setSortModes(iArr);
        DataRepository.dataItemGlobal().editor().putBoolean(DataItemGlobal.DATA_COMMON_USER_EDIT_MODES, true).apply();
        this.mComponentModuleList.reInit(true);
        if (CameraSettings.isPopupMoreStyle()) {
            DataRepository.dataItemGlobal().setCurrentMode(163);
        }
        hide();
        CameraStatUtils.trackModeEditClick(EditMode.VALUE_EDIT_MODE_CLICK_CONFIRM);
        CameraStatUtils.trackModeEditInfo();
    }

    private boolean showExitConfirm() {
        CameraStatUtils.trackModeEditClick(EditMode.VALUE_EDIT_MODE_CLICK_EXIT);
        if (!getModeAdapter().isDataChange()) {
            hide();
            return false;
        } else if (this.mExitDialog != null) {
            return false;
        } else {
            this.mExitDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.mode_edit_exit_message), getString(R.string.mimoji_confirm), new Runnable() {
                public void run() {
                    Log.u(FragmentMoreModeEdit.TAG, "showExitConfirm onClick positive");
                    CameraStatUtils.trackModeEditClick(EditMode.VALUE_EDIT_MODE_CLICK_EXIT_CONFIRM);
                    FragmentMoreModeEdit.this.mExitDialog.dismiss();
                    FragmentMoreModeEdit.this.hide();
                }
            }, null, null, getString(R.string.mode_edit_cancel), new Runnable() {
                public void run() {
                    Log.u(FragmentMoreModeEdit.TAG, "showExitConfirm onClick negative");
                    CameraStatUtils.trackModeEditClick(EditMode.VALUE_EDIT_MODE_CLICK_EXIT_CANCEL);
                }
            });
            this.mExitDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMoreModeEdit.this.mExitDialog = null;
                }
            });
            return true;
        }
    }

    public LayoutManager createLayoutManager(Context context) {
        return new GridLayoutManager(context, getCountPerLine(), 1, false);
    }

    public ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode) {
        return new ModeItemDecoration(getContext(), iMoreMode, getType());
    }

    public int getCountPerLine() {
        return Display.getMoreModeTabCol(DataRepository.dataItemRunning().getUiStyle(), false);
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_MODES_EDIT;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_module_edit;
    }

    public RecyclerView getModeList(View view) {
        return (RecyclerView) this.mRootView.findViewById(R.id.mode_edit_list);
    }

    public int getType() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean hide() {
        if (getActivity() == null || !(getActivity() instanceof ModeEditorActivity)) {
            if (getActivity() != null && (getActivity() instanceof Camera)) {
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                if (configChanges != null) {
                    configChanges.configModeEdit();
                    return true;
                }
            }
            return false;
        }
        getActivity().finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        super.initView(view);
        view.findViewById(R.id.mode_edit_done).setOnClickListener(this);
        view.findViewById(R.id.mode_edit_exit).setOnClickListener(this);
    }

    public boolean onBackEvent(int i) {
        return showExitConfirm();
    }

    public void onClick(View view) {
        int id = view.getId();
        String str = TAG;
        switch (id) {
            case R.id.mode_edit_done /*2131296832*/:
                Log.u(str, "onClick: mode_edit_done");
                confirmEdit();
                return;
            case R.id.mode_edit_exit /*2131296833*/:
                Log.u(str, "onClick: mode_edit_exit");
                showExitConfirm();
                return;
            default:
                return;
        }
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getActivity().getApplication());
        }
    }

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getActivity().getApplication());
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        Log.d(TAG, "register");
        registerBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        Log.d(TAG, "unRegister");
        unRegisterBackStack(modeCoordinator, this);
    }
}
