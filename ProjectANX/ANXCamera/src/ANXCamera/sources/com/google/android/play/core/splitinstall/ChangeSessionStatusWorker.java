package com.google.android.play.core.splitinstall;

final class ChangeSessionStatusWorker implements Runnable {
    private final SplitSessionStatusChanger changer;
    private final int errorCode;
    private final int status;

    ChangeSessionStatusWorker(SplitSessionStatusChanger splitSessionStatusChanger, int i) {
        this(splitSessionStatusChanger, i, 0);
    }

    ChangeSessionStatusWorker(SplitSessionStatusChanger splitSessionStatusChanger, int i, int i2) {
        this.changer = splitSessionStatusChanger;
        this.status = i;
        this.errorCode = i2;
    }

    public void run() {
        int i = this.errorCode;
        if (i != 0) {
            SplitSessionStatusChanger splitSessionStatusChanger = this.changer;
            splitSessionStatusChanger.mRegistry.notifyListeners(splitSessionStatusChanger.sessionState.a(this.status, i));
            return;
        }
        SplitSessionStatusChanger splitSessionStatusChanger2 = this.changer;
        splitSessionStatusChanger2.mRegistry.notifyListeners(splitSessionStatusChanger2.sessionState.a(this.status));
    }
}
