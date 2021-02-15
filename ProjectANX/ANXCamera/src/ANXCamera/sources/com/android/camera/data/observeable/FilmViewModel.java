package com.android.camera.data.observeable;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.fragment.film.FilmList;
import com.android.camera.module.impl.component.FilmDreamImpl;
import com.android.camera.resource.SimpleMultiJsonInfoRequest;
import io.reactivex.Observable;
import java.util.LinkedHashMap;

public class FilmViewModel extends VMBase {
    private FilmList mFilmList;

    public /* synthetic */ FilmList O00000Oo(FilmList filmList) {
        this.mFilmList = filmList;
        return this.mFilmList;
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return false;
    }

    public FilmList getFilmList() {
        return this.mFilmList;
    }

    public Observable getFilmListObservable() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        String str = "info.json";
        if (C0122O00000o.instance().OO0oooO()) {
            String str2 = "dollyzoom/";
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(FilmDreamImpl.TEMPLATE_PATH);
            sb3.append(str2);
            linkedHashMap.put(sb2, sb3.toString());
        }
        if (C0122O00000o.instance().OOooOo()) {
            String str3 = "slowshutter/";
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(str);
            String sb5 = sb4.toString();
            StringBuilder sb6 = new StringBuilder();
            sb6.append(FilmDreamImpl.TEMPLATE_PATH);
            sb6.append(str3);
            linkedHashMap.put(sb5, sb6.toString());
        }
        if (C0122O00000o.instance().OOOOoO0()) {
            String str4 = "timestop/";
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str4);
            sb7.append(str);
            String sb8 = sb7.toString();
            StringBuilder sb9 = new StringBuilder();
            sb9.append(FilmDreamImpl.TEMPLATE_PATH);
            sb9.append(str4);
            linkedHashMap.put(sb8, sb9.toString());
        }
        if (C0122O00000o.instance().OOO0oOO()) {
            String str5 = "longexposuredelay/";
            StringBuilder sb10 = new StringBuilder();
            sb10.append(str5);
            sb10.append(str);
            String sb11 = sb10.toString();
            StringBuilder sb12 = new StringBuilder();
            sb12.append(FilmDreamImpl.TEMPLATE_PATH);
            sb12.append(str5);
            linkedHashMap.put(sb11, sb12.toString());
        }
        if (C0122O00000o.instance().OOOO0o0()) {
            String str6 = "paralleldream/";
            StringBuilder sb13 = new StringBuilder();
            sb13.append(str6);
            sb13.append(str);
            String sb14 = sb13.toString();
            StringBuilder sb15 = new StringBuilder();
            sb15.append(FilmDreamImpl.TEMPLATE_PATH);
            sb15.append(str6);
            linkedHashMap.put(sb14, sb15.toString());
        }
        return new SimpleMultiJsonInfoRequest(linkedHashMap, FilmList.class).startObservable(FilmList.class).map(new O000000o(this));
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
        this.mFilmList = null;
    }
}
