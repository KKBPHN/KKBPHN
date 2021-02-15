package com.android.camera.module;

import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.IDCardModeProtocol;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo0 implements Runnable {
    public static final /* synthetic */ O0000Oo0 INSTANCE = new O0000Oo0();

    private /* synthetic */ O0000Oo0() {
    }

    public final void run() {
        ((IDCardModeProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(233)).switchNextPage();
    }
}
