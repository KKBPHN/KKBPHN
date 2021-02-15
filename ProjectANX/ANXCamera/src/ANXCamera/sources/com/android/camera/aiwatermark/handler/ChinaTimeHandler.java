package com.android.camera.aiwatermark.handler;

public class ChinaTimeHandler extends TimeHandler {
    private static final String TAG = "ChinaTimeHandler";
    private static final String TEMPLATE = "yyyy-MM-dd HH:mm";
    private static final String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public ChinaTimeHandler(boolean z) {
        super(z);
    }
}
