package com.android.camera.dualvideo.render;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.hardware.camera2.CaptureResult;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.opengl.EGLContext;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Size;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.dualvideo.DualVideoRecordModule.JpegPictureCallback;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.dualvideo.util.Assert;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.camera.effect.draw_mode.DrawRectShapeAttributeBase;
import com.android.camera.log.Log;
import com.android.camera.module.encoder.RenderHandler;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera2.CameraCapabilities;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.android.gallery3d.ui.RawTexture;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class RenderManager implements ExternalFrameProcessor {
    private static final String TAG = "RenderManager";
    private Rect mBlurRect;
    private CameraItemManager mCameraItemManager;
    private GLCanvas mCanvas;
    private CaptureResult mCaptureResult;
    private boolean mIsComposeSwitching;
    private JpegPictureCallback mJpegCallback;
    /* access modifiers changed from: private */
    public DualVideoRenderListener mListener;
    private DrawExtTexAttribute mMainDrawAttribute;
    private final MiscTextureManager mMiscTexManager = new MiscTextureManager();
    private boolean mNeedRecording = false;
    private boolean mNeedUpdateBlurTex = false;
    private final SparseArray mRecordRenderHandler = new SparseArray();
    private Handler mRenderHandler;
    private HandlerThread mRenderHandlerThread;
    private final Object mRenderLock = new Object();
    private ArrayList mRenderSources = new ArrayList();
    private Resources mResources;
    private final DualVideoTimer mSnapAnimDualVideoTimer = new DualVideoTimer();
    private int mSnapOrientation = 0;
    private final AtomicBoolean mSnapPending = new AtomicBoolean(false);
    private ArrayList mSnapReader = new ArrayList();
    private ArrayList mSnapRenderHandler = new ArrayList();
    public int mStatCaptureTimes = 0;
    private final ConditionVariable mUpdateBlurConditionVar = new ConditionVariable();

    /* renamed from: com.android.camera.dualvideo.render.RenderManager$3 reason: invalid class name */
    /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$LayoutType = new int[LayoutType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.FULL.ordinal()] = 2;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP_FULL.ordinal()] = 3;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN.ordinal()] = 4;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN_FULL.ordinal()] = 5;
        }
    }

    public interface DualVideoRenderListener {
        void onAuxSourceImageAvailable();

        void onLayoutTypeChanged();

        void onRenderRequestNeeded();
    }

    static /* synthetic */ void O000000o(int i, Size size, RenderSource renderSource) {
        if (renderSource.getSourceDeviceId() == i) {
            renderSource.prepare(size);
        }
    }

    static /* synthetic */ void O000000o(StringBuilder sb, CameraItemInterface cameraItemInterface) {
        DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101);
        sb.append(" id: ");
        sb.append(drawExtTexAttribute.mExtTexture.getId());
    }

    static /* synthetic */ boolean O000000o(int i, RenderSource renderSource) {
        return renderSource.getSourceDeviceId() == i;
    }

    static /* synthetic */ boolean O000000o(LayoutType layoutType, CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getSelectWindowLayoutType() == layoutType;
    }

    static /* synthetic */ boolean O00000Oo(float f, float f2, CameraItemInterface cameraItemInterface) {
        DrawRectShapeAttributeBase renderAttri = cameraItemInterface.getRenderAttri(101);
        int i = renderAttri.mX;
        boolean z = f >= ((float) i) && f <= ((float) (i + renderAttri.mWidth));
        int i2 = renderAttri.mY;
        boolean z2 = f2 >= ((float) i2) && f2 <= ((float) (i2 + renderAttri.mHeight));
        return z && z2 && cameraItemInterface.getRenderLayoutType() != LayoutType.MINI;
    }

    static /* synthetic */ boolean O00000Oo(int i, RenderSource renderSource) {
        return renderSource.getSourceDeviceId() == i;
    }

    static /* synthetic */ boolean O00000o0(float f, float f2, CameraItemInterface cameraItemInterface) {
        DrawRectShapeAttributeBase renderAttri = cameraItemInterface.getRenderAttri(101);
        int i = renderAttri.mX;
        boolean z = f >= ((float) i) && f <= ((float) (i + renderAttri.mWidth));
        int i2 = renderAttri.mY;
        boolean z2 = f2 >= ((float) i2) && f2 <= ((float) (i2 + renderAttri.mHeight));
        return z && z2 && cameraItemInterface.getRenderLayoutType() != LayoutType.MINI && cameraItemInterface.isVisible();
    }

    static /* synthetic */ void O0000O0o(UserSelectData userSelectData) {
        SelectIndex selectIndex;
        if (userSelectData.getSelectIndex() == SelectIndex.INDEX_1) {
            selectIndex = SelectIndex.INDEX_2;
        } else if (userSelectData.getSelectIndex() == SelectIndex.INDEX_2) {
            selectIndex = SelectIndex.INDEX_1;
        } else {
            return;
        }
        userSelectData.setSelectIndex(selectIndex);
    }

    static /* synthetic */ Integer O0000oO(CameraItemInterface cameraItemInterface) {
        int cameraId = DualVideoConfigManager.instance().getCameraId(cameraItemInterface.getSelectWindowLayoutType());
        if (!CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
            return Integer.valueOf(cameraId);
        }
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(cameraId);
        return capabilities == null ? Integer.valueOf(cameraId) : capabilities.getFacing() == 1 ? Integer.valueOf(DualVideoConfigManager.instance().getCameraId(LayoutType.PATCH_0)) : Integer.valueOf(cameraId);
    }

    static /* synthetic */ boolean O0000oO0(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getRenderLayoutType() == LayoutType.DOWN_FULL;
    }

    static /* synthetic */ boolean O0000oOo(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getRenderLayoutType() == LayoutType.MINI;
    }

    static /* synthetic */ boolean O0000oo(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getFaceType() == FaceType.FACE_FRONT;
    }

    static /* synthetic */ boolean O0000oo0(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getFaceType() != FaceType.FACE_BACK && cameraItemInterface.getRenderLayoutType() == LayoutType.FULL;
    }

    static /* synthetic */ boolean O0000ooO(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getFaceType() == FaceType.FACE_BACK;
    }

    static /* synthetic */ boolean O0000ooo(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getFaceType() == FaceType.FACE_REMOTE;
    }

    private void attachSurfaceTexture(GLCanvas gLCanvas) {
        this.mRenderSources.forEach(new C0218O000oO0O(gLCanvas));
        this.mCanvas = gLCanvas;
    }

    private void changeBottomIconBackground() {
        if (CameraSettings.getDualVideoConfig().getRecordType() != RecordType.MERGED) {
            getActionProcess().ifPresent(this.mCameraItemManager.getRenderableList().stream().anyMatch(C0202O000Ooo.INSTANCE) ? O000o.INSTANCE : C0219O000oO0o.INSTANCE);
        }
    }

    private void drawBlur(GLCanvasImpl gLCanvasImpl, CameraScreenNail cameraScreenNail, Rect rect, float[] fArr) {
        cameraScreenNail.drawBlurTexture(gLCanvasImpl, rect.left, rect.top, rect.width(), rect.height(), fArr);
        this.mListener.onRenderRequestNeeded();
    }

    private void drawBottomMask(GLCanvas gLCanvas) {
        if (this.mCameraItemManager.hasMiniCameraItem() && isRecording()) {
            Rect displayRect = Util.getDisplayRect(1);
            DrawFillRectAttribute drawFillRectAttribute = new DrawFillRectAttribute(displayRect.left, displayRect.bottom, displayRect.width(), gLCanvas.getHeight(), ViewCompat.MEASURED_STATE_MASK);
            gLCanvas.draw(drawFillRectAttribute);
        }
    }

    private boolean drawDualVideo(GLCanvas gLCanvas) {
        if (this.mRenderSources.isEmpty()) {
            return false;
        }
        if (!this.mRenderSources.stream().noneMatch(C0231O000oo0o.INSTANCE) || this.mIsComposeSwitching) {
            this.mRenderSources.forEach(C0200O000OoOO.INSTANCE);
            if (!this.mRenderSources.stream().anyMatch(C0231O000oo0o.INSTANCE) && !this.mIsComposeSwitching) {
                return false;
            }
            if (this.mCameraItemManager == null) {
                this.mMiscTexManager.init(gLCanvas, this.mResources);
                attachSurfaceTexture(gLCanvas);
                this.mCameraItemManager = new CameraItemManager(this.mRenderLock);
                this.mCameraItemManager.setTexture(RenderSourceType.MAIN, this.mMainDrawAttribute.mExtTexture);
                this.mRenderSources.forEach(new C0208O000o0O(this));
            }
            drawForRecording();
            drawForPreview(gLCanvas);
            waitVideoDrawFinish();
            drawBottomMask(gLCanvas);
            updateBlurTex(gLCanvas);
            return true;
        }
        this.mRenderSources.forEach(C0206O000o00O.INSTANCE);
        return false;
    }

    private void drawForPreview(GLCanvas gLCanvas) {
        List<CameraItemInterface> list = (List) this.mCameraItemManager.getRenderableList().stream().filter(C0194O000OOoo.INSTANCE).sorted(C0232O000ooO.INSTANCE).collect(Collectors.toList());
        if (!this.mSnapAnimDualVideoTimer.isValid()) {
            for (CameraItemInterface cameraItemInterface : list) {
                gLCanvas.getState().setAlpha(cameraItemInterface.getAlpha());
                cameraItemInterface.draw(gLCanvas, shouldDrawBlur(cameraItemInterface) ? 102 : 101, this.mMiscTexManager);
                cameraItemInterface.draw(gLCanvas, 103, this.mMiscTexManager);
                gLCanvas.getState().setAlpha(1.0f);
                if (cameraItemInterface.getRenderLayoutType().isSelectWindowType()) {
                    cameraItemInterface.draw(gLCanvas, 104, this.mMiscTexManager);
                    cameraItemInterface.draw(gLCanvas, 105, this.mMiscTexManager);
                    cameraItemInterface.draw(gLCanvas, 106, this.mMiscTexManager);
                }
            }
            for (CameraItemInterface cameraItemInterface2 : list) {
                gLCanvas.getState().setAlpha(cameraItemInterface2.getAlpha());
                if (!shouldDrawBlur(cameraItemInterface2)) {
                    cameraItemInterface2.draw(gLCanvas, 107, this.mMiscTexManager);
                }
                gLCanvas.getState().setAlpha(1.0f);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008b, code lost:
        if (r5.mSnapPending.get() != false) goto L_0x008d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawForRecording() {
        if (this.mNeedRecording) {
            if (this.mRecordRenderHandler.size() == 1) {
                List renderableListForRecord = this.mCameraItemManager.getRenderableListForRecord();
                for (int i = 0; i < this.mRecordRenderHandler.size(); i++) {
                    ((RenderHandler) this.mRecordRenderHandler.valueAt(i)).draw(renderableListForRecord);
                }
                if (this.mSnapPending.get()) {
                    this.mSnapRenderHandler.forEach(new C0213O000o0o0(renderableListForRecord));
                }
            }
            if (this.mRecordRenderHandler.size() == 2) {
                for (int i2 = 0; i2 < this.mRecordRenderHandler.size(); i2++) {
                    int keyAt = this.mRecordRenderHandler.keyAt(i2);
                    RenderHandler renderHandler = (RenderHandler) this.mRecordRenderHandler.get(keyAt);
                    DrawAttribute fullTypeRecordAttri = this.mCameraItemManager.getFullTypeRecordAttri(RenderSourceType.getTagByIndex(keyAt));
                    renderHandler.draw(fullTypeRecordAttri);
                    if (this.mSnapPending.get()) {
                        RenderHandler renderHandler2 = (RenderHandler) this.mSnapRenderHandler.get(i2);
                        renderHandler2.draw(fullTypeRecordAttri);
                        renderHandler2.waitDrawFinish();
                    }
                }
            }
            this.mSnapPending.set(false);
        }
    }

    private Optional getActionProcess() {
        return Optional.ofNullable((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162));
    }

    private LayoutType getRecordTypeBySelectType(LayoutType layoutType) {
        return (LayoutType) this.mCameraItemManager.getRenderableList().stream().filter(new C0217O000oO00(layoutType)).findAny().map(O00000o0.INSTANCE).orElse(LayoutType.UNDEFINED);
    }

    @SuppressLint({"SwitchIntDef"})
    private boolean handleExpandOrShrink(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        return ((Boolean) this.mCameraItemManager.getRenderableList().stream().filter(new C0201O000OoOo(this, (int) motionEvent.getX(), (int) motionEvent.getY())).findFirst().map(new O000o0(this)).orElse(Boolean.valueOf(false))).booleanValue();
    }

    private void prepareForCapture(EGLContext eGLContext) {
        ImageReader newInstance = ImageReader.newInstance(RenderUtil.OUTPUT_SIZE.getWidth(), RenderUtil.OUTPUT_SIZE.getHeight(), 1, 1);
        newInstance.setOnImageAvailableListener(new O000Oo0(this), null);
        this.mSnapReader.add(newInstance);
        RenderHandler createHandler = RenderHandler.createHandler(TAG, RenderUtil.OUTPUT_SIZE.getWidth(), RenderUtil.OUTPUT_SIZE.getHeight());
        createHandler.setEglContext(eGLContext, newInstance.getSurface(), true);
        this.mSnapRenderHandler.add(createHandler);
    }

    private void printTexId(List list) {
        StringBuilder sb = new StringBuilder();
        sb.append(" tex id:  ");
        list.forEach(new O00O0Oo(sb));
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("printTexId: ");
        sb2.append(sb.toString());
        Log.d(str, sb2.toString());
    }

    private void releaseRecordingRelated() {
        if (this.mRecordRenderHandler.size() != 0) {
            for (int i = 0; i < this.mRecordRenderHandler.size(); i++) {
                ((RenderHandler) this.mRecordRenderHandler.valueAt(i)).release();
            }
            this.mRecordRenderHandler.clear();
        }
        if (!this.mSnapReader.isEmpty()) {
            this.mSnapReader.forEach(C0228O000oo.INSTANCE);
            this.mSnapReader.clear();
            this.mSnapRenderHandler.forEach(C0229O000oo0.INSTANCE);
            this.mSnapRenderHandler.clear();
        }
    }

    private void releaseRenderHandlerThread() {
        HandlerThread handlerThread = this.mRenderHandlerThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mRenderHandlerThread = null;
            this.mRenderHandler = null;
        }
    }

    private void releaseSurfaceTexture() {
        Log.d(TAG, "releaseSurfaceTexture: ");
        this.mRenderSources.forEach(new C0203O000Ooo0(this));
        this.mRenderSources.clear();
        this.mCanvas = null;
        releaseRenderHandlerThread();
    }

    private void saveJpeg(Image image) {
        Bitmap bitmap;
        Log.d(TAG, "saveJpeg: ");
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride() - (pixelStride * width);
            Bitmap createBitmap = Bitmap.createBitmap((rowStride / pixelStride) + width, height, Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(buffer);
            if (rowStride != 0) {
                bitmap = Bitmap.createBitmap(createBitmap, 0, 0, width, createBitmap.getHeight());
                createBitmap.recycle();
            } else {
                bitmap = createBitmap;
            }
            image.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Util.appendExifToBitmap(bitmap, byteArrayOutputStream, this.mSnapOrientation, this.mCaptureResult);
            bitmap.recycle();
            JpegPictureCallback jpegPictureCallback = this.mJpegCallback;
            if (jpegPictureCallback != null) {
                jpegPictureCallback.onPictureTaken(byteArrayOutputStream.toByteArray(), null);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldDrawBlur(CameraItemInterface cameraItemInterface) {
        return this.mIsComposeSwitching;
    }

    private void startHandlerThread() {
        if (this.mRenderHandlerThread == null) {
            this.mRenderHandlerThread = new HandlerThread("dual video handler");
            this.mRenderHandlerThread.start();
            this.mRenderHandler = new Handler(this.mRenderHandlerThread.getLooper()) {
                public void handleMessage(@NonNull Message message) {
                    super.handleMessage(message);
                }
            };
        }
    }

    private void switchSelectIndex() {
        CameraSettings.getDualVideoConfig().getSelectedData().forEach(O000o000.INSTANCE);
    }

    private void updateBlurTex(GLCanvas gLCanvas) {
        if (this.mNeedUpdateBlurTex) {
            ArrayList visibleRenderList = this.mCameraItemManager.getVisibleRenderList();
            Optional findAny = visibleRenderList.stream().filter(C0221O000oOO0.INSTANCE).findAny();
            Optional findAny2 = visibleRenderList.stream().filter(C0211O000o0Oo.INSTANCE).findAny();
            Optional findAny3 = visibleRenderList.stream().filter(C0198O000OoO.INSTANCE).findAny();
            findAny2.ifPresent(new C0209O000o0O0(this, gLCanvas));
            findAny.ifPresent(new C0207O000o00o(this, gLCanvas));
            findAny3.ifPresent(new C0212O000o0o(this, gLCanvas));
            this.mNeedUpdateBlurTex = false;
            this.mUpdateBlurConditionVar.open();
        }
    }

    private void updateSelectDataWhenRenderLayoutChanged() {
        CameraSettings.getDualVideoConfig().getSelectedData().forEach(new C0205O000Oooo(this));
    }

    private void waitVideoDrawFinish() {
        if (this.mNeedRecording) {
            for (int i = 0; i < this.mRecordRenderHandler.size(); i++) {
                ((RenderHandler) this.mRecordRenderHandler.valueAt(i)).waitDrawFinish();
            }
        }
    }

    public /* synthetic */ void O000000o(ImageReader imageReader) {
        saveJpeg(imageReader.acquireNextImage());
    }

    public /* synthetic */ void O000000o(GLCanvas gLCanvas, CameraItemInterface cameraItemInterface) {
        MiscTextureManager miscTextureManager = this.mMiscTexManager;
        String str = MiscTextureManager.TEX_BACK_BLUR;
        RenderUtil.copyPreviewTexture(gLCanvas, (RawTexture) miscTextureManager.getTexture(str), (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101));
        RenderUtil.blurTex(gLCanvas, (RawTexture) this.mMiscTexManager.getTexture(str));
    }

    public /* synthetic */ boolean O000000o(int i, int i2, CameraItemInterface cameraItemInterface) {
        Rect handleArea = cameraItemInterface.getHandleArea(this.mMiscTexManager);
        return handleArea != null && handleArea.contains(i, i2);
    }

    public /* synthetic */ void O00000Oo(GLCanvas gLCanvas, CameraItemInterface cameraItemInterface) {
        MiscTextureManager miscTextureManager = this.mMiscTexManager;
        String str = MiscTextureManager.TEX_FRONT_BLUR;
        RenderUtil.copyPreviewTexture(gLCanvas, (RawTexture) miscTextureManager.getTexture(str), (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101));
        RenderUtil.blurTex(gLCanvas, (RawTexture) this.mMiscTexManager.getTexture(str));
    }

    public /* synthetic */ void O00000o0(RenderSource renderSource) {
        CameraItemManager cameraItemManager;
        RenderSourceType renderSourceType;
        if (renderSource.getSourceDeviceId() == RenderSourceType.SUB.getIndex()) {
            cameraItemManager = this.mCameraItemManager;
            renderSourceType = RenderSourceType.SUB;
        } else if (renderSource.getSourceDeviceId() == RenderSourceType.REMOTE.getIndex()) {
            cameraItemManager = this.mCameraItemManager;
            renderSourceType = RenderSourceType.REMOTE;
        } else {
            return;
        }
        cameraItemManager.setTexture(renderSourceType, renderSource.getTexture());
    }

    public /* synthetic */ void O00000o0(GLCanvas gLCanvas, CameraItemInterface cameraItemInterface) {
        MiscTextureManager miscTextureManager = this.mMiscTexManager;
        String str = MiscTextureManager.TEX_REMOTE_BLUR;
        RenderUtil.copyPreviewTexture(gLCanvas, (RawTexture) miscTextureManager.getTexture(str), (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101));
        RenderUtil.blurTex(gLCanvas, (RawTexture) this.mMiscTexManager.getTexture(str));
    }

    public /* synthetic */ void O00000oO(RenderSource renderSource) {
        renderSource.release(this.mCanvas);
    }

    public /* synthetic */ void O0000OOo(UserSelectData userSelectData) {
        userSelectData.setmRecordLayoutType(getRecordTypeBySelectType(userSelectData.getmSelectWindowLayoutType()));
    }

    public /* synthetic */ Boolean O0000oOO(CameraItemInterface cameraItemInterface) {
        int i = AnonymousClass3.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[cameraItemInterface.getRenderLayoutType().ordinal()];
        if (i == 1 || i == 2 || i == 3) {
            this.mCameraItemManager.expandOrShrinkTop();
        } else if (i == 4 || i == 5) {
            this.mCameraItemManager.expandBottom();
        }
        return Boolean.valueOf(true);
    }

    public void captureVideoSnapshot(JpegPictureCallback jpegPictureCallback, int i, CaptureResult captureResult) {
        if (!this.mSnapPending.get()) {
            this.mSnapPending.set(true);
            this.mJpegCallback = jpegPictureCallback;
            this.mCaptureResult = captureResult;
            this.mSnapAnimDualVideoTimer.init(60);
            if (i == -1) {
                this.mSnapOrientation = 0;
            } else {
                this.mSnapOrientation = i;
            }
            this.mStatCaptureTimes++;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mRenderSources.stream().anyMatch(C0231O000oo0o.INSTANCE) && this.mCameraItemManager != null) {
            if (handleExpandOrShrink(motionEvent)) {
                this.mListener.onLayoutTypeChanged();
                changeBottomIconBackground();
                updateSelectDataWhenRenderLayoutChanged();
                return true;
            } else if (this.mCameraItemManager.hasMiniCameraItem()) {
                return this.mCameraItemManager.updateMiniWindowLocation(motionEvent);
            } else {
                if (this.mCameraItemManager.is6PatchWindow()) {
                    this.mCameraItemManager.selectItem(motionEvent);
                    return true;
                }
            }
        }
        return false;
    }

    public void enableContinuousRender(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("enableContinuousRender: ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (this.mIsComposeSwitching != z) {
            this.mIsComposeSwitching = z;
            switchTexture();
        }
    }

    public Surface genOrUpdateRenderSource(int i, Size size) {
        Log.d(TAG, "createRemoteCameraSurfaceIfNeed: ");
        startHandlerThread();
        if (this.mRenderSources.stream().noneMatch(new C0214O000o0oo(i))) {
            final RenderSource renderSource = new RenderSource(i, this.mRenderHandler);
            renderSource.prepare(size);
            renderSource.setListener(new SourceListener() {
                public void onImageUpdated(int i) {
                    if (renderSource.canDraw()) {
                        RenderManager.this.mListener.onAuxSourceImageAvailable();
                    }
                }

                public void onNewStreamAvailable() {
                    RenderManager.this.enableContinuousRender(false);
                }
            });
            this.mRenderSources.add(renderSource);
        } else {
            this.mRenderSources.forEach(new C0199O000OoO0(i, size));
        }
        return (Surface) this.mRenderSources.stream().filter(new C0216O000oO0(i)).findFirst().map(C0196O000Oo0O.INSTANCE).orElse(null);
    }

    public CameraItemManager getCameraItemManager() {
        return this.mCameraItemManager;
    }

    public int getIdByPosition(float f, float f2) {
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        if (cameraItemManager == null) {
            return -1;
        }
        return ((Integer) cameraItemManager.getVisibleRenderList().stream().filter(new C0215O000oO(f, f2)).findFirst().map(C0210O000o0OO.INSTANCE).orElse(Integer.valueOf(-1))).intValue();
    }

    public MiscTextureManager getMiscTextureManager() {
        return this.mMiscTexManager;
    }

    public int getProcessorType() {
        return 1;
    }

    public LayoutType getRenderComposeTypeByPosition(float f, float f2) {
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        return cameraItemManager == null ? LayoutType.UNDEFINED : (LayoutType) cameraItemManager.getRenderableList().stream().filter(new C0204O000OooO(f, f2)).findFirst().map(O00000o0.INSTANCE).orElse(LayoutType.UNDEFINED);
    }

    public boolean hasMiniComposeType() {
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        if (cameraItemManager != null && !cameraItemManager.getRenderableList().isEmpty()) {
            return this.mCameraItemManager.getRenderableList().stream().anyMatch(C0220O000oOO.INSTANCE);
        }
        return false;
    }

    public boolean isProcessorReady() {
        return true;
    }

    public boolean isRecording() {
        return this.mNeedRecording;
    }

    public boolean isSwitching() {
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        boolean z = false;
        if (cameraItemManager == null) {
            return false;
        }
        if (this.mIsComposeSwitching || cameraItemManager.isAnimating()) {
            z = true;
        }
        return z;
    }

    public boolean isZoomEnabled() {
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        if (cameraItemManager == null) {
            return false;
        }
        return cameraItemManager.getRenderableList().stream().noneMatch(C0197O000Oo0o.INSTANCE);
    }

    public void onDrawFrame(GLCanvasImpl gLCanvasImpl, CameraScreenNail cameraScreenNail) {
        gLCanvasImpl.clearBuffer();
        float[] fArr = new float[16];
        cameraScreenNail.getSurfaceTexture().getTransformMatrix(fArr);
        Rect displayRect = cameraScreenNail.getDisplayRect();
        if (displayRect == null) {
            Log.d(TAG, "onDrawFrame: display rect is null");
            return;
        }
        if (this.mBlurRect == null) {
            this.mBlurRect = new Rect(displayRect);
        }
        DrawExtTexAttribute drawExtTexAttribute = new DrawExtTexAttribute(cameraScreenNail.getExtTexture(), fArr, displayRect.left, displayRect.top, displayRect.width(), displayRect.height());
        if (!onDrawFrame((GLCanvas) gLCanvasImpl, drawExtTexAttribute)) {
            drawBlur(gLCanvasImpl, cameraScreenNail, this.mBlurRect, null);
        }
    }

    public boolean onDrawFrame(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        boolean drawDualVideo;
        this.mMainDrawAttribute = drawExtTexAttribute;
        synchronized (this.mRenderLock) {
            drawDualVideo = drawDualVideo(gLCanvas);
        }
        if (this.mIsComposeSwitching) {
            this.mListener.onRenderRequestNeeded();
        }
        return drawDualVideo;
    }

    public void reStartRenderWindow() {
        if (this.mCameraItemManager != null && !this.mIsComposeSwitching) {
            triggerUpdateBlurTex();
        }
    }

    public void release() {
        Log.d(TAG, "release: ");
        synchronized (this.mRenderLock) {
            this.mJpegCallback = null;
            releaseRecordingRelated();
            releaseSurfaceTexture();
            this.mMiscTexManager.release();
        }
    }

    public void setListener(DualVideoRenderListener dualVideoRenderListener) {
        this.mListener = dualVideoRenderListener;
    }

    public void setOrientation(int i) {
        this.mMiscTexManager.setTexTransDegree(i);
    }

    public void setResources(Resources resources) {
        this.mResources = resources;
    }

    public void startRecording(EGLContext eGLContext, SparseArray sparseArray) {
        Log.d(TAG, "startRecording: ");
        boolean z = this.mSnapReader.isEmpty() && this.mSnapRenderHandler.isEmpty() && this.mRecordRenderHandler.size() == 0;
        Assert.check(z);
        synchronized (this.mRenderLock) {
            if (this.mRecordRenderHandler.size() == 0) {
                for (int i = 0; i < sparseArray.size(); i++) {
                    int keyAt = sparseArray.keyAt(i);
                    Surface surface = (Surface) sparseArray.get(keyAt);
                    RenderHandler createHandler = RenderHandler.createHandler(TAG, RenderUtil.OUTPUT_SIZE.getWidth(), RenderUtil.OUTPUT_SIZE.getHeight());
                    createHandler.setEglContext(eGLContext, surface, true);
                    this.mRecordRenderHandler.put(keyAt, createHandler);
                    prepareForCapture(eGLContext);
                }
            }
            this.mNeedRecording = true;
        }
        this.mStatCaptureTimes = 0;
    }

    public void stopRecording() {
        Log.d(TAG, "stopRecording: ");
        synchronized (this.mRenderLock) {
            this.mNeedRecording = false;
            this.mJpegCallback = null;
            releaseRecordingRelated();
        }
        this.mStatCaptureTimes = 0;
    }

    public void switch6patch2preview() {
        Log.d(TAG, "switch6patch2preview: ");
        if (this.mCameraItemManager != null && !this.mIsComposeSwitching) {
            triggerUpdateBlurTex();
            this.mCameraItemManager.switchSelectToRecordWindow();
            this.mCameraItemManager.printRenderList();
        }
    }

    public void switchPreviewTo6Patch() {
        Log.d(TAG, "switchPreviewTo6Patch: ");
        if (this.mCameraItemManager != null && !this.mIsComposeSwitching) {
            triggerUpdateBlurTex();
            this.mCameraItemManager.switchRecordToSelectWindow();
            this.mCameraItemManager.printRenderList();
        }
    }

    public void switchTexture() {
        Optional.ofNullable(this.mCameraItemManager).ifPresent(O000o00.INSTANCE);
    }

    public boolean switchTopBottom() {
        Log.d(TAG, "switchTopBottom: ");
        CameraItemManager cameraItemManager = this.mCameraItemManager;
        if (cameraItemManager == null) {
            return false;
        }
        boolean switchTopBottom = cameraItemManager.switchTopBottom();
        if (switchTopBottom) {
            switchSelectIndex();
        }
        updateSelectDataWhenRenderLayoutChanged();
        return switchTopBottom;
    }

    public void triggerUpdateBlurTex() {
        Log.d(TAG, "triggerUpdateBlurTex: ");
        if (!this.mNeedUpdateBlurTex) {
            this.mUpdateBlurConditionVar.close();
            this.mNeedUpdateBlurTex = true;
            this.mUpdateBlurConditionVar.block(500);
        }
    }

    public void updateRenderData() {
        this.mCameraItemManager.updateCameraItemList();
    }
}
