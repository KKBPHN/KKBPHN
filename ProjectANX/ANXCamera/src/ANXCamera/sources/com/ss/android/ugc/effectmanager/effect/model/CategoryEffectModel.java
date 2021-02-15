package com.ss.android.ugc.effectmanager.effect.model;

import java.util.List;

public class CategoryEffectModel {
    private String category_key;
    private List collection;
    private int cursor;
    private List effects;
    private boolean has_more;
    private int sorting_position;
    private String version;

    public String getCategoryKey() {
        return this.category_key;
    }

    public List getCollectEffects() {
        return this.collection;
    }

    public int getCursor() {
        return this.cursor;
    }

    public List getEffects() {
        return this.effects;
    }

    public int getSortingPosition() {
        return this.sorting_position;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean hasMore() {
        return this.has_more;
    }

    public void setCategoryKey(String str) {
        this.category_key = str;
    }

    public void setCollectEffects(List list) {
        this.collection = list;
    }

    public void setCursor(int i) {
        this.cursor = i;
    }

    public void setEffects(List list) {
        this.effects = list;
    }

    public void setHasMore(boolean z) {
        this.has_more = z;
    }

    public void setSortingPosition(int i) {
        this.sorting_position = this.sorting_position;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
