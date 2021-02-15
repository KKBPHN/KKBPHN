package com.android.camera.fragment.live;

import android.content.Context;
import android.util.AttributeSet;
import com.android.camera.R;
import com.android.camera.fragment.sticker.download.DownloadView;

public class LiveDownloadView extends DownloadView {
    public LiveDownloadView(Context context) {
        super(context);
    }

    public LiveDownloadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public float getAlphaNormal() {
        return 0.9f;
    }

    /* access modifiers changed from: protected */
    public int getStateImageResource(int i) {
        int i2 = R.drawable.icon_live_sticker_download;
        if (i != 0) {
            if (i == 2) {
                i2 = R.drawable.icon_live_sticker_downloading;
            } else if (i == 3) {
                return R.drawable.icon_live_sticker_downloaded;
            } else {
                if (i != 4) {
                    i2 = 0;
                }
                return i2;
            }
        }
        return i2;
    }
}
