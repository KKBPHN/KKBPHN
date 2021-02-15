package com.google.android.play.core.remote;

final class BindServiceTask extends RemoteTask {
    private final RemoteManager remoteManager;
    private final RemoteTask task;

    BindServiceTask(RemoteManager remoteManager2, RemoteTask remoteTask) {
        this.remoteManager = remoteManager2;
        this.task = remoteTask;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        this.remoteManager.bindServiceInternal(this.task);
    }
}
