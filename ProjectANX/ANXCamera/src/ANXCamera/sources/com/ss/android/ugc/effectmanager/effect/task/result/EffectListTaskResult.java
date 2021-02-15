package com.ss.android.ugc.effectmanager.effect.task.result;

import com.ss.android.ugc.effectmanager.common.task.BaseTaskResult;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import java.util.List;

public class EffectListTaskResult extends BaseTaskResult {
    private List effectList;
    private ExceptionResult exception;

    public EffectListTaskResult(List list, ExceptionResult exceptionResult) {
        this.effectList = list;
        this.exception = exceptionResult;
    }

    public List getEffectList() {
        return this.effectList;
    }

    public ExceptionResult getException() {
        return this.exception;
    }

    public void setEffectList(List list) {
        this.effectList = list;
    }

    public void setException(ExceptionResult exceptionResult) {
        this.exception = exceptionResult;
    }
}
