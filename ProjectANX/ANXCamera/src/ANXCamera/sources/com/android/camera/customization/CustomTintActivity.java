package com.android.camera.customization;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.camera.R;
import com.android.camera.log.Log;

public class CustomTintActivity extends FragmentActivity {
    public static final String FROM_WHERE = "from_where";
    private static final String LOG_TAG = "CustomTintActivity";
    private FragmentCustomTint mCustomTint;
    private int mFromWhere;
    private boolean mLocked = false;

    public void onBackPressed() {
        if (!this.mCustomTint.onBackPressed()) {
            Log.u(LOG_TAG, "onBackPressed");
            super.onBackPressed();
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
        this.mCustomTint = new FragmentCustomTint();
        getSupportFragmentManager().beginTransaction().add((int) R.id.mode_edit_root, (Fragment) this.mCustomTint).commit();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.mLocked) {
            finish();
        }
    }
}
