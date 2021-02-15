package com.ss.android.ugc.effectmanager.effect.model;

import java.util.List;

public class ProviderEffectModel {
    private int cursor;
    private boolean has_more;
    private String search_tips;
    private List sticker_list;

    public int getCursor() {
        return this.cursor;
    }

    public String getSearchTips() {
        return this.search_tips;
    }

    public List getStickerList() {
        return this.sticker_list;
    }

    public boolean hasMore() {
        return this.has_more;
    }

    public void setCursor(int i) {
        this.cursor = i;
    }

    public void setHasMore(boolean z) {
        this.has_more = z;
    }

    public void setSearchTips(String str) {
        this.search_tips = str;
    }

    public void setStickerList(List list) {
        this.sticker_list = list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProviderEffectModel{search_tips='");
        sb.append(this.search_tips);
        sb.append('\'');
        sb.append(", cursor=");
        sb.append(this.cursor);
        sb.append(", has_more=");
        sb.append(this.has_more);
        sb.append('}');
        return sb.toString();
    }
}
