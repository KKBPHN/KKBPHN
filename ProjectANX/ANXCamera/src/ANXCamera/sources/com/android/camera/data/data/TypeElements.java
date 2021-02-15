package com.android.camera.data.data;

import java.util.List;

public abstract class TypeElements {
    protected ComponentData mComponentData;
    private List mTypeItemList;

    public TypeElements(ComponentData componentData) {
        this.mComponentData = componentData;
    }
}
