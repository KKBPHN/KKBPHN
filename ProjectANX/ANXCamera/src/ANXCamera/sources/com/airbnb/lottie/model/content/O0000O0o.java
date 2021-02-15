package com.airbnb.lottie.model.content;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0010O0000o0O;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000O0o implements O00000Oo {
    private final boolean hidden;
    private final MergePaths$MergePathsMode mode;
    private final String name;

    public O0000O0o(String str, MergePaths$MergePathsMode mergePaths$MergePathsMode, boolean z) {
        this.name = str;
        this.mode = mergePaths$MergePathsMode;
        this.hidden = z;
    }

    @Nullable
    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        if (o000OoO0.O000O00o()) {
            return new C0010O0000o0O(this);
        }
        O00000o.warning("Animation contains merge paths but they are disabled.");
        return null;
    }

    public MergePaths$MergePathsMode getMode() {
        return this.mode;
    }

    public String getName() {
        return this.name;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MergePaths{mode=");
        sb.append(this.mode);
        sb.append('}');
        return sb.toString();
    }
}
