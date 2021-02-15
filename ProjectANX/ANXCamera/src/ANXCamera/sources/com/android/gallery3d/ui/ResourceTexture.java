package com.android.gallery3d.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ResourceTexture extends UploadedTexture {
    protected final Context mContext;
    protected final int mResId;

    public ResourceTexture(Context context, int i) {
        Utils.checkNotNull(context);
        this.mContext = context;
        this.mResId = i;
        setOpaque(false);
    }

    /* access modifiers changed from: protected */
    public void onFreeBitmap(Bitmap bitmap) {
        if (!BasicTexture.inFinalizer()) {
            bitmap.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public Bitmap onGetBitmap() {
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeResource(this.mContext.getResources(), this.mResId, options);
    }
}
