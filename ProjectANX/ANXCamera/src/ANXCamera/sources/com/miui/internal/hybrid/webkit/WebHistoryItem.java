package com.miui.internal.hybrid.webkit;

import android.graphics.Bitmap;
import miui.hybrid.HybridHistoryItem;

public class WebHistoryItem extends HybridHistoryItem {
    private android.webkit.WebHistoryItem mWebHistoryItem;

    public WebHistoryItem(android.webkit.WebHistoryItem webHistoryItem) {
        this.mWebHistoryItem = webHistoryItem;
    }

    public Bitmap getFavicon() {
        return this.mWebHistoryItem.getFavicon();
    }

    public String getOriginalUrl() {
        return this.mWebHistoryItem.getOriginalUrl();
    }

    public String getTitle() {
        return this.mWebHistoryItem.getTitle();
    }

    public String getUrl() {
        return this.mWebHistoryItem.getUrl();
    }
}
