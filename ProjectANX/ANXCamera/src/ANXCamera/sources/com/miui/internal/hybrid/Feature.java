package com.miui.internal.hybrid;

import java.util.HashMap;
import java.util.Map;

public class Feature {
    private String name;
    private Map params = new HashMap();

    public void clearParams() {
        this.params.clear();
    }

    public String getName() {
        return this.name;
    }

    public String getParam(String str) {
        return (String) this.params.get(str);
    }

    public Map getParams() {
        return this.params;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setParam(String str, String str2) {
        this.params.put(str, str2);
    }

    public void setParams(Map map) {
        this.params = map;
    }
}
