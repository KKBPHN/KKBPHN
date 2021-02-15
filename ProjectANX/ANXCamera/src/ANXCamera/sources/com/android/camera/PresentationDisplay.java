package com.android.camera;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.camera.CameraScreenNail.RequestRenderListener;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.framework.gles.FullFramenOESTexture;
import com.android.camera.ui.GLTextureView;
import com.android.camera.ui.GLTextureView.EGLShareContextGetter;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class PresentationDisplay extends Presentation implements Renderer, RequestRenderListener {
    private static final String TAG = "PresentationDisplay";
    /* access modifiers changed from: private */
    public Camera mCameraActivity;
    private FullFramenOESTexture mFullFrameTexture;
    private int mHeight;
    private GLTextureView mTextureView;
    private int mWidth;

    public PresentationDisplay(Context context) {
        this(context, getDisplay(context));
        this.mCameraActivity = (Camera) context;
    }

    private PresentationDisplay(Context context, Display display) {
        super(context, display);
        this.mWidth = 880;
        this.mHeight = 1173;
    }

    private static Display getDisplay(Context context) {
        return ((DisplayManager) context.getSystemService("display")).getDisplays()[1];
    }

    private void initStillPreviewRender(GLTextureView gLTextureView) {
        Camera camera = this.mCameraActivity;
        if (camera != null) {
            camera.getCameraScreenNail().addRequestListener(this);
        }
        if (gLTextureView.getRenderer() == null) {
            gLTextureView.setEGLContextClientVersion(2);
            gLTextureView.setEGLShareContextGetter(new EGLShareContextGetter() {
                public EGLContext getShareContext() {
                    return PresentationDisplay.this.mCameraActivity.getGLView().getEGLContext();
                }
            });
            gLTextureView.setRenderer(this);
            gLTextureView.setRenderMode(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_presentation);
        this.mTextureView = (GLTextureView) findViewById(R.id.presenation_textureview);
        initStillPreviewRender(this.mTextureView);
        updateTextureSize();
    }

    public void onDrawFrame(GL10 gl10) {
        this.mFullFrameTexture.draw(this.mCameraActivity.getCameraScreenNail().getExtTexture().getId());
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        if (this.mFullFrameTexture == null) {
            this.mFullFrameTexture = new FullFramenOESTexture();
            this.mFullFrameTexture.enableMirror();
        }
    }

    public void requestRender() {
        this.mTextureView.requestRender();
    }

    public void updateTextureSize() {
        this.mHeight = (int) (((float) this.mWidth) * Util.getRatio(DataRepository.dataItemConfig().getComponentConfigRatio().getPictureSizeRatioString(this.mCameraActivity.getCurrentModuleIndex())));
        GLTextureView gLTextureView = this.mTextureView;
        if (gLTextureView != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) gLTextureView.getLayoutParams();
            marginLayoutParams.width = this.mWidth;
            marginLayoutParams.height = this.mHeight;
            marginLayoutParams.topMargin = Display.getTopHeight();
            this.mTextureView.setLayoutParams(marginLayoutParams);
        }
    }
}
