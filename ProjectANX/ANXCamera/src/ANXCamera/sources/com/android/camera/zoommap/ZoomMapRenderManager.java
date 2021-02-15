package com.android.camera.zoommap;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.Size;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawRectAttribute;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.ExtTexture;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.GLPaint;
import com.android.gallery3d.ui.ResourceTexture;

public class ZoomMapRenderManager {
    private static final int LINE_WIDTH = 3;
    public static final String TAG = "ZoomMapRender";
    private ResourceTexture mBorderTexture = new ResourceTexture(CameraAppImpl.getAndroidContext(), R.drawable.bg_zoom_map_pip);
    private GLPaint mGlPaint = new GLPaint(3.0f, -1);
    private volatile Rect mMapRect = new Rect();
    private DrawRectAttribute mRectAttribute = new DrawRectAttribute();
    private final float[] mTextureTransformMatrix = new float[16];
    private Size mWindowSize;
    private SurfaceTexture mZoomMapSurfaceTexture;
    private ExtTexture mZoomMapTexture;

    public ZoomMapRenderManager(SurfaceTexture surfaceTexture, ExtTexture extTexture, Size size) {
        this.mZoomMapSurfaceTexture = surfaceTexture;
        this.mZoomMapTexture = extTexture;
        this.mWindowSize = new Size(size.getHeight(), size.getWidth());
        StringBuilder sb = new StringBuilder();
        sb.append("mWindowSize = ");
        sb.append(this.mWindowSize);
        Log.d(TAG, sb.toString());
    }

    public boolean drawZoomMap(GLCanvas gLCanvas) {
        this.mZoomMapSurfaceTexture.updateTexImage();
        this.mZoomMapSurfaceTexture.getTransformMatrix(this.mTextureTransformMatrix);
        DrawExtTexAttribute drawExtTexAttribute = new DrawExtTexAttribute(this.mZoomMapTexture, this.mTextureTransformMatrix, 0, 0, this.mWindowSize.getWidth(), this.mWindowSize.getHeight());
        gLCanvas.draw(drawExtTexAttribute);
        gLCanvas.draw(this.mRectAttribute.init((float) (this.mMapRect.left - 3), (float) (this.mMapRect.top - 3), (float) (this.mMapRect.width() + 6), (float) (this.mMapRect.height() + 6), this.mGlPaint));
        DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(this.mBorderTexture, 0, 0, this.mWindowSize.getWidth(), this.mWindowSize.getHeight());
        gLCanvas.draw(drawBasicTexAttribute);
        return false;
    }

    public void updateZoomMapRect(Rect rect) {
        this.mMapRect = rect;
    }
}
