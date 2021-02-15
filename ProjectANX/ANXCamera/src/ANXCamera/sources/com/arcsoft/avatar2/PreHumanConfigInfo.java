package com.arcsoft.avatar2;

public class PreHumanConfigInfo {
    private String a;
    private String b;
    private String c;

    public PreHumanConfigInfo() {
        String str = "";
        this.a = str;
        this.b = str;
        this.c = str;
    }

    public String getPreHumanConfigPath() {
        return this.c;
    }

    public String getPreHumanName() {
        return this.a;
    }

    public String getPreHumanThumbnailPath() {
        return this.b;
    }

    public void setPreHumanConfigPath(String str) {
        this.c = str;
    }

    public void setPreHumanName(String str) {
        this.a = str;
    }

    public void setPreHumanThumbnailPath(String str) {
        this.b = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name = ");
        sb.append(this.a);
        sb.append("\n, thumbNail path = ");
        sb.append(this.b);
        sb.append("\n, config path = ");
        sb.append(this.c);
        return sb.toString();
    }
}
