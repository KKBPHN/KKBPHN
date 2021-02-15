package com.android.camera.module;

import android.content.ContentValues;
import java.util.List;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000oO0o reason: case insensitive filesystem */
public final /* synthetic */ class C0385O000oO0o implements OnTagsListener {
    private final /* synthetic */ VideoModule O0OOoO0;
    private final /* synthetic */ String O0OOoOO;
    private final /* synthetic */ ContentValues O0OOoOo;
    private final /* synthetic */ boolean O0OOoo0;

    public /* synthetic */ C0385O000oO0o(VideoModule videoModule, String str, ContentValues contentValues, boolean z) {
        this.O0OOoO0 = videoModule;
        this.O0OOoOO = str;
        this.O0OOoOo = contentValues;
        this.O0OOoo0 = z;
    }

    public final void onTagsReady(List list) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0, list);
    }
}
