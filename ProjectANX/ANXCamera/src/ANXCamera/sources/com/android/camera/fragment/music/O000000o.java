package com.android.camera.fragment.music;

import com.android.camera.resource.tmmusic.TMMusicCatrgoryRequest;
import com.android.camera.resource.tmmusic.TMMusicList;
import io.reactivex.functions.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Function {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final Object apply(Object obj) {
        return new TMMusicCatrgoryRequest(((TMMusicList) obj).getCategoryID()).startObservable((Object) (TMMusicList) obj);
    }
}
