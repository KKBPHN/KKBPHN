package com.ss.android.ugc.effectmanager.effect.model.net;

import com.ss.android.ugc.effectmanager.common.model.BaseNetResponse;
import java.util.List;

public class EffectListResponse extends BaseNetResponse {
    private List data;

    public List getData() {
        return this.data;
    }

    public void setData(List list) {
        this.data = list;
    }
}
