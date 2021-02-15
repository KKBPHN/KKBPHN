package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitLoadManagerService {
    private static final AtomicReference sReference = new AtomicReference();

    private static SplitLoadManager create(Context context, int i, boolean z, boolean z2, String str, String[] strArr, String[] strArr2) {
        SplitLoadManagerImpl splitLoadManagerImpl = new SplitLoadManagerImpl(context, i, z, z2, str, strArr, strArr2);
        return splitLoadManagerImpl;
    }

    public static SplitLoadManager getInstance() {
        if (sReference.get() != null) {
            return (SplitLoadManager) sReference.get();
        }
        throw new RuntimeException("Have you invoke SplitLoadManagerService#install(Context) method?");
    }

    public static boolean hasInstance() {
        return sReference.get() != null;
    }

    public static void install(Context context, int i, boolean z, boolean z2, String str, String[] strArr, String[] strArr2) {
        sReference.set(create(context, i, z, z2, str, strArr, strArr2));
    }
}
