package com.google.android.play.core.splitinstall;

import java.util.ArrayList;
import java.util.List;

public class SplitInstallRequest {
    private final List moduleNames;

    public class Builder {
        /* access modifiers changed from: private */
        public final List moduleNames;

        private Builder() {
            this.moduleNames = new ArrayList();
        }

        public Builder addModule(String str) {
            this.moduleNames.add(str);
            return this;
        }

        public SplitInstallRequest build() {
            return new SplitInstallRequest(this);
        }
    }

    private SplitInstallRequest(Builder builder) {
        this.moduleNames = new ArrayList(builder.moduleNames);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List getModuleNames() {
        return this.moduleNames;
    }

    public String toString() {
        String valueOf = String.valueOf(this.moduleNames);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 34);
        sb.append("SplitInstallRequest{modulesNames=");
        sb.append(valueOf);
        sb.append("}");
        return sb.toString();
    }
}
