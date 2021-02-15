package com.android.camera.fragment.dialog;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.camera.Camera;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;

public class HibernationFragment extends DialogFragment implements OnKeyListener, OnClickListener, HandleBackTrace {
    public static final String TAG = "Hibernation";

    private void adjustViewSize(View view) {
        int i = getResources().getDisplayMetrics().widthPixels;
        int i2 = getResources().getDisplayMetrics().heightPixels;
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width != i || layoutParams.height != i2) {
            layoutParams.width = i;
            layoutParams.height = i2;
        }
    }

    private void resumeMode() {
        ((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).removeBackStack(this);
        if (isAdded()) {
            Camera camera = (Camera) getActivity();
            if (!camera.isActivityPaused() && !camera.isSwitchingModule()) {
                camera.onAwaken();
            }
        }
    }

    public final boolean canProvide() {
        return isAdded();
    }

    public void onActivityCreated(Bundle bundle) {
        Window window = getDialog().getWindow();
        if (C0122O00000o.instance().OO00ooO()) {
            window.requestFeature(1);
        }
        super.onActivityCreated(bundle);
        window.setLayout(-1, -1);
        if (Display.isContentViewExtendToTopEdges()) {
            CompatibilityUtils.setCutoutModeShortEdges(window);
        }
    }

    public boolean onBackEvent(int i) {
        dismissAllowingStateLoss();
        resumeMode();
        return i == 1 || i == 2;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        resumeMode();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.hibernation_cover) {
            onBackEvent(5);
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().setGravity(48);
        return onCreateDialog;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_hibernation, viewGroup, false);
        inflate.findViewById(R.id.hibernation_cover).setOnClickListener(this);
        adjustViewSize(inflate.findViewById(R.id.hibernation_layout));
        return inflate;
    }

    public void onDestroyView() {
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.removeBackStack(this);
        }
        super.onDestroyView();
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || (i != 4 && i != 259)) {
            return false;
        }
        onBackEvent(5);
        return true;
    }

    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(this);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.addInBackStack(this);
        }
    }
}
