package com.google.android.play.core.splitinstall;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface LoadedSplitFetcher {
    Set loadedSplits();
}
