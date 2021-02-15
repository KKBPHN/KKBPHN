package com.android.camera.dualvideo.render;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ContentType {
    public static final int BLUR = 102;
    public static final int DARK_CORNER = 106;
    public static final int GRID_LINE = 103;
    public static final int HANDLER = 107;
    public static final int LABEL = 105;
    public static final int PREVIEW = 101;
    public static final int SELECTED_FRAME = 104;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Item {
    }
}
