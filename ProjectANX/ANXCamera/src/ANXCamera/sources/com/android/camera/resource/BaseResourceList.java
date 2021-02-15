package com.android.camera.resource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseResourceList implements BaseResourceUpdatable {
    public static final int RESOURCE_TYPE_FILM = 3;
    public static final int RESOURCE_TYPE_TM_MUSIC = 2;
    public static final int RESOURCE_TYPE_VV = 1;
    protected transient boolean departed;
    private List mResourceList = new ArrayList();
    private int mResourceType;
    public boolean mVerified;
    public String version;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResourceType {
    }

    /* access modifiers changed from: protected */
    public void addItem(JSONObject jSONObject) {
        BaseResourceItem parseSingleItem = parseSingleItem(jSONObject, this.mResourceList.size());
        if (parseSingleItem != null) {
            this.mResourceList.add(parseSingleItem);
        }
    }

    public void compareAndMarkDeparted(BaseResourceList baseResourceList) {
        if (!this.version.equals(baseResourceList.version)) {
            setDeparted();
        }
    }

    public void createResourcesList(JSONObject jSONObject) {
        JSONArray itemJsonArray = getItemJsonArray(jSONObject);
        if (itemJsonArray != null && itemJsonArray.length() != 0) {
            for (int i = 0; i <= itemJsonArray.length(); i++) {
                JSONObject optJSONObject = itemJsonArray.optJSONObject(i);
                if (optJSONObject != null) {
                    addItem(optJSONObject);
                }
            }
        }
    }

    public BaseResourceItem getItem(int i) {
        return (BaseResourceItem) this.mResourceList.get(i);
    }

    public BaseResourceItem getItemById(String str) {
        for (BaseResourceItem baseResourceItem : this.mResourceList) {
            if (baseResourceItem.id.equals(str)) {
                return baseResourceItem;
            }
        }
        return null;
    }

    public abstract JSONArray getItemJsonArray(JSONObject jSONObject);

    public abstract String getLocalVersion();

    public List getResourceList() {
        return this.mResourceList;
    }

    public abstract int getResourceType();

    public int getSize() {
        return this.mResourceList.size();
    }

    public boolean isDeparted() {
        return this.departed;
    }

    public abstract void parseInitialData(JSONObject jSONObject);

    public abstract BaseResourceItem parseSingleItem(JSONObject jSONObject, int i);

    public void setDeparted() {
        this.departed = true;
    }

    public abstract void setLocalVersion(String str);
}
