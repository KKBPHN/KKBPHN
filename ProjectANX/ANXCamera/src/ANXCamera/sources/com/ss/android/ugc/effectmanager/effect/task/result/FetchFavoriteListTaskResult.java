package com.ss.android.ugc.effectmanager.effect.task.result;

import com.ss.android.ugc.effectmanager.common.task.BaseTaskResult;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import java.util.ArrayList;
import java.util.List;

public class FetchFavoriteListTaskResult extends BaseTaskResult {
    private List mEffects = new ArrayList();
    private ExceptionResult mException;
    private String mType;

    public FetchFavoriteListTaskResult(ExceptionResult exceptionResult) {
        this.mException = exceptionResult;
    }

    public FetchFavoriteListTaskResult(List list, String str) {
        this.mEffects.clear();
        this.mEffects.addAll(list);
        this.mType = str;
    }

    public List getEffects() {
        return this.mEffects;
    }

    public ExceptionResult getException() {
        return this.mException;
    }

    public String getType() {
        return this.mType;
    }

    public void setEffects(List list) {
        this.mEffects = list;
    }

    public void setType(String str) {
        this.mType = str;
    }
}
