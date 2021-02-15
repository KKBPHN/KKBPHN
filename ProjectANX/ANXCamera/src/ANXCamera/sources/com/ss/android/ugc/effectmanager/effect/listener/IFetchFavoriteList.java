package com.ss.android.ugc.effectmanager.effect.listener;

import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import java.util.List;

public interface IFetchFavoriteList {
    void onFailed(ExceptionResult exceptionResult);

    void onSuccess(List list, String str);
}
