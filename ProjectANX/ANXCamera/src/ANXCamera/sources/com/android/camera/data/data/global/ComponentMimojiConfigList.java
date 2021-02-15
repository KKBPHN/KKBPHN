package com.android.camera.data.data.global;

import androidx.annotation.NonNull;
import com.android.camera.data.data.ComponentData;
import java.util.ArrayList;
import java.util.List;

public class ComponentMimojiConfigList extends ComponentData {
    public ComponentMimojiConfigList(DataItemGlobal dataItemGlobal) {
        super(dataItemGlobal);
    }

    private List initItems() {
        return new ArrayList();
    }

    @NonNull
    public String getDefaultValue(int i) {
        return null;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return null;
    }
}
