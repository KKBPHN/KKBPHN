package com.arcsoft.camera.utils;

import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;

final class O0000O0o {
    public static final String a = "_id";
    public static final String b = "_data";
    public static final String c = "_size";
    public static final String d = "datetaken";
    public static final String e;
    public static final String f = "bucket_id";
    public static final String g = "mime_type";
    public static final String h = "date_modified";
    public static final String i = "latitude";
    public static final String j = "longitude";
    public static final String k = "orientation";
    public static final String l = "media_type";
    public static final String m = "duration";
    public static final String n = "resolution";
    public static final String o = "tags";
    public static final String p = "width";
    public static final String q = "height";
    public static final String r = "title";
    public static final String s = "_display_name";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("case media_type when 1 then \"");
        sb.append(Media.EXTERNAL_CONTENT_URI);
        sb.append("\" else \"");
        sb.append(Video.Media.EXTERNAL_CONTENT_URI);
        sb.append("\" end");
        e = sb.toString();
    }

    private O0000O0o() {
    }
}
