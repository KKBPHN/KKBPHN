package com.android.camera.dualvideo.render;

import android.content.res.Resources;
import android.opengl.Matrix;
import com.android.camera.R;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.BitmapTexture;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.RawTexture;
import com.ss.android.vesdk.VEResult;
import java.util.ArrayList;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.IIntValueProperty;

public class MiscTextureManager {
    private static final String TAG = "MiscTextureManager";
    public static final String TEX_BACK_BLUR = "b_b";
    public static final String TEX_DARK_CORNER_BOTTOM = "d_c_b";
    public static final String TEX_DARK_CORNER_TOP = "d_c_t";
    public static final String TEX_EXPAND = "exp";
    public static final String TEX_FRONT_BLUR = "f_b";
    public static final String TEX_REMOTE_BLUR = "r_b";
    public static final String TEX_SELECTED_BG = "s_bg";
    public static final String TEX_SELECTED_FIRST = "s_1";
    public static final String TEX_SELECTED_SECOND = "s_2";
    public static final String TEX_SELECT_FRAME_BOTTOM = "s_f_bom";
    public static final String TEX_SELECT_FRAME_TOP = "s_f_top";
    public static final String TEX_SHRINK = "shr";
    /* access modifiers changed from: private */
    public final float[] mAnimTexTransMatrix = new float[16];
    private final ArrayList mMiscRenderList = new ArrayList();
    /* access modifiers changed from: private */
    public int mTexOrientation = 0;
    private final float[] mTexTransMatrix = new float[16];

    public MiscTextureManager() {
        Matrix.setIdentityM(this.mTexTransMatrix, 0);
        Matrix.setIdentityM(this.mAnimTexTransMatrix, 0);
    }

    private void animTexTransMatrix(int i) {
        final int i2 = i - this.mTexOrientation;
        if (i2 < -180) {
            i2 += m.cQ;
        } else if (i2 > 180) {
            i2 += VEResult.TER_EGL_BAD_MATCH;
        }
        AnimConfig animConfig = new AnimConfig();
        animConfig.setEase(6, 300.0f);
        animConfig.addListeners(new TransitionListener() {
            final int srcRotation = MiscTextureManager.this.mTexOrientation;

            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                int i2 = this.srcRotation + ((int) ((((float) i) / 1000.0f) * ((float) i2)));
                Matrix.setIdentityM(MiscTextureManager.this.mAnimTexTransMatrix, 0);
                MiscTextureManager miscTextureManager = MiscTextureManager.this;
                miscTextureManager.rotateTexTransMatrix(miscTextureManager.mAnimTexTransMatrix, i2);
            }
        });
        Folme.useValue("animTexTrans").setTo((Object) Integer.valueOf(0)).to(Integer.valueOf(1000), animConfig);
    }

    private void createTagTex(Resources resources) {
        DualVideoConfigManager.instance().getConfigs().forEach(new C0193O000OOoO(this, resources));
        this.mMiscRenderList.add(new MiscRenderItem(DualVideoConfigManager.REMOTE_NAME, new BitmapTexture(RenderUtil.textAsBitmap(resources.getString(R.string.pref_multi_camera_dual_video_remote), -1), false, false)));
    }

    public /* synthetic */ void O000000o(Resources resources, ConfigItem configItem) {
        StringBuilder sb = new StringBuilder();
        sb.append("createTagTex: ");
        sb.append(configItem.mDescription);
        Log.d(TAG, sb.toString());
        String str = "front";
        if (configItem.mDescription.equals(str)) {
            this.mMiscRenderList.add(new MiscRenderItem(str, new BitmapTexture(RenderUtil.textAsBitmap(resources.getString(R.string.pref_multi_camera_dual_video_front), -1), false, false)));
        }
        ArrayList arrayList = this.mMiscRenderList;
        String str2 = configItem.mDescription;
        arrayList.add(new MiscRenderItem(str2, new BitmapTexture(RenderUtil.textAsBitmap(str2, -1), false, false)));
    }

    public synchronized float[] gemAnimTexTransMatrix() {
        return this.mAnimTexTransMatrix;
    }

    public synchronized int getTexOrientation() {
        return this.mTexOrientation;
    }

    public synchronized float[] getTexTransMatrix() {
        return this.mTexTransMatrix;
    }

    public BasicTexture getTexture(String str) {
        return (BasicTexture) this.mMiscRenderList.stream().filter(new O000OOo(str)).findFirst().map(C0234O000ooOO.INSTANCE).orElse(null);
    }

    public void init(GLCanvas gLCanvas, Resources resources) {
        createTagTex(resources);
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SELECTED_BG, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_selected_bg), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SELECTED_FIRST, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_selected_1), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SELECTED_SECOND, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_selected_2), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SELECT_FRAME_TOP, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_round_corner_top), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SELECT_FRAME_BOTTOM, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_round_corner_bottom), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_DARK_CORNER_TOP, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_dark_corner_top), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_DARK_CORNER_BOTTOM, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_dark_corner_bottom), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_EXPAND, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_preview_expand), false, false)));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_SHRINK, new BitmapTexture(RenderUtil.getBitmap(resources, R.drawable.ic_dual_video_preview_shrink), false, false)));
        RawTexture rawTexture = new RawTexture(RenderUtil.FRONT_PREVIEW.getWidth() / 8, RenderUtil.FRONT_PREVIEW.getHeight() / 8, true);
        RawTexture rawTexture2 = new RawTexture(RenderUtil.FRONT_PREVIEW.getWidth() / 8, RenderUtil.FRONT_PREVIEW.getHeight() / 8, true);
        RawTexture rawTexture3 = new RawTexture(RenderUtil.REMOTE_SIZE.getWidth() / 8, RenderUtil.REMOTE_SIZE.getHeight() / 8, true);
        this.mMiscRenderList.add(new MiscRenderItem(TEX_BACK_BLUR, rawTexture));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_FRONT_BLUR, rawTexture2));
        this.mMiscRenderList.add(new MiscRenderItem(TEX_REMOTE_BLUR, rawTexture3));
        this.mMiscRenderList.forEach(new O000OOOo(gLCanvas));
    }

    public void release() {
        this.mMiscRenderList.forEach(O000OOo0.INSTANCE);
        this.mMiscRenderList.clear();
    }

    public synchronized void rotateTexTransMatrix(float[] fArr, int i) {
        if (i != 0) {
            Matrix.translateM(fArr, 0, 0.5f, 0.5f, 0.0f);
            Matrix.rotateM(fArr, 0, (float) i, 0.0f, 0.0f, 1.0f);
            Matrix.translateM(fArr, 0, -0.5f, -0.5f, 0.0f);
        }
    }

    public synchronized void setTexTransDegree(int i) {
        String str = "TAG";
        StringBuilder sb = new StringBuilder();
        sb.append("setTexTransDegree: src: ");
        sb.append(this.mTexOrientation);
        sb.append("  dst: ");
        sb.append(i);
        Log.d(str, sb.toString());
        animTexTransMatrix(i);
        Matrix.setIdentityM(this.mTexTransMatrix, 0);
        rotateTexTransMatrix(this.mTexTransMatrix, i);
        this.mTexOrientation = i;
    }
}
