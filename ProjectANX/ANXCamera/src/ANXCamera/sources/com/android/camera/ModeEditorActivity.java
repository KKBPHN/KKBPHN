package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.camera.fragment.mode.FragmentMoreModeEdit;
import com.android.camera.log.Log;

public class ModeEditorActivity extends FragmentActivity {
    public static final String FROM_WHERE = "from_where";
    private static final String TAG = "ModeEditor";
    /* access modifiers changed from: private */
    public int mDisplayRotation;
    private FragmentMoreModeEdit mEditFragment;
    private int mFromWhere;
    private boolean mLocked = false;
    private MyOrientationListener mMyOrientationListener;
    /* access modifiers changed from: private */
    public int mOrientation;
    /* access modifiers changed from: private */
    public int mOrientationCompensation;
    /* access modifiers changed from: private */
    public View mRootView;
    /* access modifiers changed from: private */
    public boolean mSupportOrientation;

    class MyOrientationListener extends OrientationEventListener {
        public MyOrientationListener(Context context) {
            super(context);
        }

        public void onOrientationChanged(int i) {
            if (i != -1 && ModeEditorActivity.this.mSupportOrientation) {
                ModeEditorActivity modeEditorActivity = ModeEditorActivity.this;
                modeEditorActivity.mOrientation = Util.roundOrientation(i, modeEditorActivity.mOrientation);
                int displayRotation = Util.getDisplayRotation(ModeEditorActivity.this);
                if (displayRotation != ModeEditorActivity.this.mDisplayRotation) {
                    ModeEditorActivity.this.mDisplayRotation = displayRotation;
                }
                int access$300 = ModeEditorActivity.this.mOrientationCompensation;
                ModeEditorActivity modeEditorActivity2 = ModeEditorActivity.this;
                modeEditorActivity2.mOrientationCompensation = (modeEditorActivity2.mOrientation + ModeEditorActivity.this.mDisplayRotation) % m.cQ;
                Display.updateOrientation(ModeEditorActivity.this.mOrientationCompensation);
                if (ModeEditorActivity.this.mSupportOrientation && ModeEditorActivity.this.mRootView != null) {
                    Util.updateOrientationLayoutRotation(ModeEditorActivity.this.mRootView, access$300, ModeEditorActivity.this.mOrientationCompensation);
                }
            }
        }
    }

    public void onBackPressed() {
        if (!this.mEditFragment.onBackEvent(1)) {
            Log.u(TAG, "onBackPressed");
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mFromWhere = getIntent().getIntExtra("from_where", 160);
        this.mLocked = getIntent().getBooleanExtra("StartActivityWhenLocked", false);
        if (this.mLocked) {
            setShowWhenLocked(true);
        }
        setContentView(R.layout.activity_mode_edit);
        this.mRootView = findViewById(R.id.mode_edit_root);
        this.mEditFragment = new FragmentMoreModeEdit();
        getSupportFragmentManager().beginTransaction().add((int) R.id.mode_edit_root, (Fragment) this.mEditFragment).commit();
        this.mSupportOrientation = C0122O00000o.instance().O000OOoo(Display.getDisplayRatio());
        this.mMyOrientationListener = new MyOrientationListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mMyOrientationListener.disable();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mMyOrientationListener.enable();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.mLocked) {
            finish();
        }
    }
}
