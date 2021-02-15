package com.google.android.play.core.splitinstall;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface SplitSessionLoader {
    void load(List list, SplitSessionStatusChanger splitSessionStatusChanger);
}
