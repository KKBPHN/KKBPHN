package com.android.camera.features.mimoji2.fragment.edit;

import com.arcsoft.avatar2.emoticon.EmoInfo;

/* compiled from: lambda */
/* renamed from: com.android.camera.features.mimoji2.fragment.edit.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0262O0000OoO implements Runnable {
    private final /* synthetic */ FragmentMimojiEmoticon O0OOoO0;
    private final /* synthetic */ EmoInfo O0OOoOO;
    private final /* synthetic */ int O0OOoOo;
    private final /* synthetic */ int O0OOoo0;

    public /* synthetic */ C0262O0000OoO(FragmentMimojiEmoticon fragmentMimojiEmoticon, EmoInfo emoInfo, int i, int i2) {
        this.O0OOoO0 = fragmentMimojiEmoticon;
        this.O0OOoOO = emoInfo;
        this.O0OOoOo = i;
        this.O0OOoo0 = i2;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0);
    }
}
