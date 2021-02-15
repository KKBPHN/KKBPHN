package com.android.camera;

import androidx.annotation.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public interface IFuncPreviewMetadataListener extends Function, IPreviewMetadataListener, Consumer {
    void accept(Object obj);

    Object apply(@NonNull Object obj);
}
