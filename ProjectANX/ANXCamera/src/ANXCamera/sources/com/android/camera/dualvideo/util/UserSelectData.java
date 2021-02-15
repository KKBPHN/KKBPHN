package com.android.camera.dualvideo.util;

import com.android.camera.Util;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.log.Log;

public class UserSelectData {
    private static final String TAG = "UserSelectData";
    private LayoutType mRecordLayoutType;
    private SelectIndex mSelectIndex;
    private LayoutType mSelectWindowLayoutType;

    public UserSelectData(SelectIndex selectIndex, LayoutType layoutType, LayoutType layoutType2) {
        this.mSelectIndex = selectIndex;
        this.mSelectWindowLayoutType = layoutType;
        this.mRecordLayoutType = layoutType2;
    }

    public UserSelectData(UserSelectData userSelectData) {
        this(userSelectData.mSelectIndex, userSelectData.mSelectWindowLayoutType, userSelectData.mRecordLayoutType);
    }

    public int getCameraId() {
        return DualVideoConfigManager.instance().getCameraId(this.mSelectWindowLayoutType);
    }

    public SelectIndex getSelectIndex() {
        return this.mSelectIndex;
    }

    public LayoutType getmRecordLayoutType() {
        return this.mRecordLayoutType;
    }

    public LayoutType getmSelectWindowLayoutType() {
        return this.mSelectWindowLayoutType;
    }

    public void setSelectIndex(SelectIndex selectIndex) {
        if (selectIndex == SelectIndex.INDEX_0) {
            StringBuilder sb = new StringBuilder();
            sb.append("setSelectIndex: ");
            sb.append(Util.getCallers(3));
            Log.d(TAG, sb.toString());
        }
        this.mSelectIndex = selectIndex;
    }

    public void setSelectWindowLayoutType(LayoutType layoutType) {
        this.mSelectWindowLayoutType = layoutType;
    }

    public void setmRecordLayoutType(LayoutType layoutType) {
        this.mRecordLayoutType = layoutType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserSelectData{mSelectWindowLayoutType=");
        sb.append(this.mSelectWindowLayoutType);
        sb.append(", mRenderLayoutType=");
        sb.append(this.mRecordLayoutType);
        sb.append(", mSelectIndex=");
        sb.append(this.mSelectIndex);
        sb.append('}');
        return sb.toString();
    }
}
