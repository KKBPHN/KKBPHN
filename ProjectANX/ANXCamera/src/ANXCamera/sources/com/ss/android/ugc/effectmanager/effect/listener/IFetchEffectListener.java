package com.ss.android.ugc.effectmanager.effect.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import com.ss.android.ugc.effectmanager.effect.model.Effect;

public interface IFetchEffectListener {
    void onFail(@Nullable Effect effect, @NonNull ExceptionResult exceptionResult);

    void onStart(Effect effect);

    void onSuccess(Effect effect);
}
