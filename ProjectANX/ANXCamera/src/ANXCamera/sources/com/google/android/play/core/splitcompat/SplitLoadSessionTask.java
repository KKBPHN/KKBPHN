package com.google.android.play.core.splitcompat;

import com.google.android.play.core.splitinstall.SplitSessionStatusChanger;
import com.iqiyi.android.qigsaw.core.splitload.SplitLoadManager;
import com.iqiyi.android.qigsaw.core.splitload.SplitLoadManagerService;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import java.util.List;

final class SplitLoadSessionTask implements Runnable, OnSplitLoadListener {
    private final SplitSessionStatusChanger changer;
    private final List splitFileIntents;

    SplitLoadSessionTask(List list, SplitSessionStatusChanger splitSessionStatusChanger) {
        this.splitFileIntents = list;
        this.changer = splitSessionStatusChanger;
    }

    public void onCompleted() {
        this.changer.changeStatus(5);
    }

    public void onFailed(int i) {
        this.changer.changeStatus(6, i);
    }

    public void run() {
        if (this.splitFileIntents == null) {
            onFailed(-100);
            return;
        }
        SplitLoadManager instance = SplitLoadManagerService.getInstance();
        if (instance != null) {
            instance.createSplitLoadTask(this.splitFileIntents, this).run();
        }
    }
}
