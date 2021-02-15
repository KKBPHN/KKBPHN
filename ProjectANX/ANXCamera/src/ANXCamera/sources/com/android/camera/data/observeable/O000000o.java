package com.android.camera.data.observeable;

import com.android.camera.fragment.film.FilmList;
import io.reactivex.functions.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Function {
    private final /* synthetic */ FilmViewModel O0OOoO0;

    public /* synthetic */ O000000o(FilmViewModel filmViewModel) {
        this.O0OOoO0 = filmViewModel;
    }

    public final Object apply(Object obj) {
        return this.O0OOoO0.O00000Oo((FilmList) obj);
    }
}
