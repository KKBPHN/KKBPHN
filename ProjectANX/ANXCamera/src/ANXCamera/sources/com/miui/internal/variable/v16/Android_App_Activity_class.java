package com.miui.internal.variable.v16;

import android.app.Activity;
import miui.app.ActionBar;

public class Android_App_Activity_class extends com.miui.internal.variable.Android_App_Activity_class {
    public void buildProxy() {
        String str = "(Z)V";
        attachMethod("setProgressBarVisibility", str);
        attachMethod("setProgressBarIndeterminateVisibility", str);
        attachMethod("setProgressBarIndeterminate", str);
        attachMethod("setProgress", "(I)V");
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleSetProgressBarVisibility(0, null, false);
        handleSetProgressBarIndeterminateVisibility(0, null, false);
        handleSetProgressBarIndeterminate(0, null, false);
        handleSetProgress(0, null, 0);
    }

    /* access modifiers changed from: protected */
    public void handleSetProgress(long j, Activity activity, int i) {
        if (activity instanceof miui.app.Activity) {
            ActionBar actionBar = ((miui.app.Activity) activity).getActionBar();
            if (actionBar != null) {
                actionBar.setProgress(i);
                return;
            }
            return;
        }
        originalSetProgress(j, activity, i);
    }

    /* access modifiers changed from: protected */
    public void handleSetProgressBarIndeterminate(long j, Activity activity, boolean z) {
        if (activity instanceof miui.app.Activity) {
            ActionBar actionBar = ((miui.app.Activity) activity).getActionBar();
            if (actionBar != null) {
                actionBar.setProgressBarIndeterminate(z);
                return;
            }
            return;
        }
        originalSetProgressBarIndeterminate(j, activity, z);
    }

    /* access modifiers changed from: protected */
    public void handleSetProgressBarIndeterminateVisibility(long j, Activity activity, boolean z) {
        if (activity instanceof miui.app.Activity) {
            ActionBar actionBar = ((miui.app.Activity) activity).getActionBar();
            if (actionBar != null) {
                actionBar.setProgressBarIndeterminateVisibility(z);
                return;
            }
            return;
        }
        originalSetProgressBarIndeterminateVisibility(j, activity, z);
    }

    /* access modifiers changed from: protected */
    public void handleSetProgressBarVisibility(long j, Activity activity, boolean z) {
        if (activity instanceof miui.app.Activity) {
            ActionBar actionBar = ((miui.app.Activity) activity).getActionBar();
            if (actionBar != null) {
                actionBar.setProgressBarVisibility(z);
                return;
            }
            return;
        }
        originalSetProgressBarVisibility(j, activity, z);
    }

    /* access modifiers changed from: protected */
    public void originalSetProgress(long j, Activity activity, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_App_Activity_class.originalSetProgress(long, Activity, int)");
    }

    /* access modifiers changed from: protected */
    public void originalSetProgressBarIndeterminate(long j, Activity activity, boolean z) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_App_Activity_class.originalSetProgressBarIndeterminate(long, Activity, boolean)");
    }

    /* access modifiers changed from: protected */
    public void originalSetProgressBarIndeterminateVisibility(long j, Activity activity, boolean z) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_App_Activity_class.originalSetProgressBarIndeterminateVisibility(long, Activity, boolean)");
    }

    /* access modifiers changed from: protected */
    public void originalSetProgressBarVisibility(long j, Activity activity, boolean z) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_App_Activity_class.originalSetProgressBarVisibility(long, Activity, boolean)");
    }
}
