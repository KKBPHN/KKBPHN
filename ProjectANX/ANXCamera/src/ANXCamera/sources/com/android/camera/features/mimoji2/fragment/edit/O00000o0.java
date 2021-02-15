package com.android.camera.features.mimoji2.fragment.edit;

import android.view.View;
import com.android.camera.features.mimoji2.widget.baseview.OnRecyclerItemClickListener;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements OnRecyclerItemClickListener {
    private final /* synthetic */ EditLevelListAdapter2 O0OOoO0;
    private final /* synthetic */ MimojiThumbnailRecyclerAdapter2 O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ O00000o0(EditLevelListAdapter2 editLevelListAdapter2, MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2, int i) {
        this.O0OOoO0 = editLevelListAdapter2;
        this.O0OOoOO = mimojiThumbnailRecyclerAdapter2;
        this.O0OOoOo = i;
    }

    public final void OnRecyclerItemClickListener(Object obj, int i, View view) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (ASAvatarConfigInfo) obj, i, view);
    }
}
