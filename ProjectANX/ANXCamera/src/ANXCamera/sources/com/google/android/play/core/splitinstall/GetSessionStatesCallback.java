package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;
import java.util.ArrayList;
import java.util.List;

final class GetSessionStatesCallback extends SplitInstallServiceCallbackImpl {
    GetSessionStatesCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.os.Bundle>, for r3v0, types: [java.util.List, java.util.List<android.os.Bundle>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onGetSessionStates(List<Bundle> list) {
        super.onGetSessionStates(list);
        ArrayList arrayList = new ArrayList(list.size());
        for (Bundle createFrom : list) {
            arrayList.add(SplitInstallSessionState.createFrom(createFrom));
        }
        this.mTask.setResult(arrayList);
    }
}
