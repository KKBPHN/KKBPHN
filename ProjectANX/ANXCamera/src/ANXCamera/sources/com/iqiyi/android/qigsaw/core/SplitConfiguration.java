package com.iqiyi.android.qigsaw.core;

import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.common.SplitLog.Logger;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitLoadReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUninstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUpdateReporter;

public class SplitConfiguration {
    final String[] forbiddenWorkProcesses;
    final SplitInstallReporter installReporter;
    final SplitLoadReporter loadReporter;
    final Class obtainUserConfirmationDialogClass;
    final int splitLoadMode;
    final SplitUninstallReporter uninstallReporter;
    final SplitUpdateReporter updateReporter;
    final boolean verifySignature;
    final String[] workProcesses;

    public class Builder {
        /* access modifiers changed from: private */
        public String[] forbiddenWorkProcesses;
        /* access modifiers changed from: private */
        public SplitInstallReporter installReporter;
        /* access modifiers changed from: private */
        public SplitLoadReporter loadReporter;
        /* access modifiers changed from: private */
        public Class obtainUserConfirmationDialogClass;
        /* access modifiers changed from: private */
        public int splitLoadMode;
        /* access modifiers changed from: private */
        public SplitUninstallReporter uninstallReporter;
        /* access modifiers changed from: private */
        public SplitUpdateReporter updateReporter;
        /* access modifiers changed from: private */
        public boolean verifySignature;
        /* access modifiers changed from: private */
        public String[] workProcesses;

        private Builder() {
            this.splitLoadMode = 1;
            this.verifySignature = true;
            this.obtainUserConfirmationDialogClass = DefaultObtainUserConfirmationDialog.class;
        }

        public SplitConfiguration build() {
            return new SplitConfiguration(this);
        }

        public Builder forbiddenWorkProcesses(@NonNull String[] strArr) {
            if (strArr.length > 0) {
                this.forbiddenWorkProcesses = strArr;
            }
            return this;
        }

        public Builder installReporter(@NonNull SplitInstallReporter splitInstallReporter) {
            this.installReporter = splitInstallReporter;
            return this;
        }

        public Builder loadReporter(@NonNull SplitLoadReporter splitLoadReporter) {
            this.loadReporter = splitLoadReporter;
            return this;
        }

        public Builder logger(@NonNull Logger logger) {
            SplitLog.setSplitLogImp(logger);
            return this;
        }

        public Builder obtainUserConfirmationDialogClass(@NonNull Class cls) {
            this.obtainUserConfirmationDialogClass = cls;
            return this;
        }

        public Builder splitLoadMode(int i) {
            this.splitLoadMode = i;
            return this;
        }

        public Builder uninstallReporter(@NonNull SplitUninstallReporter splitUninstallReporter) {
            this.uninstallReporter = splitUninstallReporter;
            return this;
        }

        public Builder updateReporter(@NonNull SplitUpdateReporter splitUpdateReporter) {
            this.updateReporter = splitUpdateReporter;
            return this;
        }

        public Builder verifySignature(boolean z) {
            this.verifySignature = z;
            return this;
        }

        public Builder workProcesses(@NonNull String[] strArr) {
            if (strArr.length > 0) {
                this.workProcesses = strArr;
            }
            return this;
        }
    }

    private SplitConfiguration(Builder builder) {
        if (builder.forbiddenWorkProcesses == null || builder.workProcesses == null) {
            this.splitLoadMode = builder.splitLoadMode;
            this.forbiddenWorkProcesses = builder.forbiddenWorkProcesses;
            this.installReporter = builder.installReporter;
            this.loadReporter = builder.loadReporter;
            this.updateReporter = builder.updateReporter;
            this.uninstallReporter = builder.uninstallReporter;
            this.obtainUserConfirmationDialogClass = builder.obtainUserConfirmationDialogClass;
            this.workProcesses = builder.workProcesses;
            this.verifySignature = builder.verifySignature;
            return;
        }
        throw new RuntimeException("forbiddenWorkProcesses and workProcesses can't be set at the same time, you should choose one of them.");
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
