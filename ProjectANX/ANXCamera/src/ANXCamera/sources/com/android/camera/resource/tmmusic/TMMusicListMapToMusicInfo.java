package com.android.camera.resource.tmmusic;

import com.android.camera.fragment.music.LiveMusicInfo;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

public class TMMusicListMapToMusicInfo implements Function {
    public List apply(TMMusicList tMMusicList) {
        List<TMMusicItem> resourceList = tMMusicList.getResourceList();
        ArrayList arrayList = new ArrayList(resourceList.size());
        for (TMMusicItem tMMusicItem : resourceList) {
            LiveMusicInfo liveMusicInfo = new LiveMusicInfo();
            liveMusicInfo.mAuthor = tMMusicItem.artistName;
            liveMusicInfo.mTitle = tMMusicItem.detailName;
            liveMusicInfo.mDurationText = tMMusicItem.getDurationText();
            liveMusicInfo.mThumbnailUrl = tMMusicItem.albumImage320;
            liveMusicInfo.mRequestItemID = tMMusicItem.itemID;
            liveMusicInfo.mCategoryId = tMMusicList.getCategoryID();
            liveMusicInfo.downloadState = 6;
            arrayList.add(liveMusicInfo);
        }
        return arrayList;
    }
}
