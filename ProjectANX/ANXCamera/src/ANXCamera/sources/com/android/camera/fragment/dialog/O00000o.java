package com.android.camera.fragment.dialog;

import com.android.camera.fragment.fullscreen.ShareInfo;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Comparator {
    public static final /* synthetic */ O00000o INSTANCE = new O00000o();

    private /* synthetic */ O00000o() {
    }

    public final int compare(Object obj, Object obj2) {
        return Integer.compare(((ShareInfo) obj).index, ((ShareInfo) obj2).index);
    }
}
