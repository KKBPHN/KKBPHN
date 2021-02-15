package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.camera.CameraScreenNail.NailListener;
import com.android.camera.CameraScreenNail.RequestRenderListener;
import com.android.camera.aftersales.AftersalesManager;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.dialog.ThermalDialogFragment;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.Module;
import com.android.camera.module.loader.SurfaceStateListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.PresentationDisplay;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.storage.ImageSaver.ImageSaverCallback;
import com.android.camera.storage.Storage;
import com.android.camera.ui.CameraRootView;
import com.android.camera.ui.PopupManager;
import com.android.camera.ui.ScreenHint;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera.ui.V9EdgeShutterView;
import com.android.gallery3d.ui.GLCanvas;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.stat.d;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public abstract class ActivityBase extends FragmentActivity implements AppController, SurfaceStateListener, ImageSaverCallback {
    public static final int MSG_CAMERA_OPEN_EXCEPTION = 10;
    protected static final int MSG_CANCEL_PRESENTATION = 5;
    protected static final int MSG_DEBUG_INFO = 0;
    protected static final int MSG_KEYGUARD_TWICE_RESUME = 1;
    protected static final int MSG_ON_THERMAL_CONTRAINT = 3;
    protected static final int MSG_SHOW_PRESENTATION = 4;
    protected static final int MSG_TRACK_MODE_SWITCH = 2;
    private static final int START_GALLERY_TIMEOUT = 300;
    private static final String TAG = "ActivityBase";
    private static final int THERMAL_CONSTRAINED_EXIT_DELAY = 5000;
    private static final int TOUCH_EVENT_TRACK_TIME_OUT = 1000;
    protected volatile boolean mActivityPaused;
    protected volatile boolean mActivityStarted;
    protected volatile boolean mActivityStopped;
    /* access modifiers changed from: private */
    public HashMap mAppLunchMap;
    protected long mAppStartTime;
    protected CameraAppImpl mApplication;
    private long mBlurStartTime = -1;
    protected CameraBrightness mCameraBrightness;
    protected boolean mCameraErrorShown;
    public CameraIntentManager mCameraIntentManager;
    protected CameraRootView mCameraRootView;
    protected CameraScreenNail mCameraScreenNail;
    protected MiuiCameraSound mCameraSound;
    protected Thread mCloseActivityThread;
    protected Module mCurrentModule;
    private int mCurrentSurfaceState = 1;
    protected TextView mDebugInfoView;
    protected int mDisplayRotation;
    protected V9EdgeShutterView mEdgeShutterView;
    protected AlertDialog mErrorDialog;
    protected Disposable mGLCoverDisposable;
    protected ImageView mGLCoverView;
    protected V6CameraGLSurfaceView mGLView;
    private boolean mGalleryLocked = false;
    protected final Handler mHandler = new ActivityHandler(this);
    protected boolean mIsFinishInKeyguard = false;
    private boolean mIsSwitchingModule;
    protected int mJumpFlag = 0;
    protected boolean mJumpedToGallery;
    protected KeyguardManager mKeyguardManager;
    private boolean mKeyguardSecureLocked = false;
    protected int mLastJumpFlag = 0;
    protected LocationManager mLocationManager;
    protected long mModeSelectGaussianTime = -1;
    protected long mModeSwitchTime;
    protected int mOrientation = -1;
    protected int mOrientationCompensation = 0;
    protected boolean mPreviewThumbnail;
    protected boolean mReleaseByModule;
    protected ScreenHint mScreenHint;
    protected ArrayList mSecureUriList;
    private ShortcutFunction mShortcutFunction;
    protected boolean mStartFromKeyguard = false;
    protected ThumbnailUpdater mThumbnailUpdater;
    protected Disposable mTrackAppLunchDisposable;

    class ActivityHandler extends Handler {
        private final WeakReference mActivity;

        public ActivityHandler(ActivityBase activityBase) {
            this.mActivity = new WeakReference(activityBase);
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x006b  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x006f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            ActivityBase activityBase = (ActivityBase) this.mActivity.get();
            if (activityBase != null) {
                int i = message.what;
                if (i != 0) {
                    String str = ActivityBase.TAG;
                    boolean z = true;
                    if (i == 1) {
                        Log.d(str, "handleMessage:  set mIsFinishInKeyguard = true;");
                        activityBase.mIsFinishInKeyguard = true;
                    } else if (i == 2) {
                        CameraStatUtils.trackModeSwitch();
                    } else if (i == 3) {
                        ThermalDialogFragment thermalDialogFragment = new ThermalDialogFragment();
                        thermalDialogFragment.setStyle(2, R.style.DialogFragmentFullScreen);
                        activityBase.getSupportFragmentManager().beginTransaction().add((Fragment) thermalDialogFragment, "ThermalDialog").commitAllowingStateLoss();
                        Objects.requireNonNull(activityBase);
                        postDelayed(new O0000Oo(activityBase), 5000);
                        Log.w(str, "onThermalNotification finish activity after 5 seconds");
                    } else if (i == 4) {
                        PresentationDisplay presentationDisplay = (PresentationDisplay) ModeCoordinatorImpl.getInstance().getAttachProtocol(945);
                        if (presentationDisplay != null) {
                            presentationDisplay.show(2);
                        }
                    } else if (i == 5) {
                        PresentationDisplay presentationDisplay2 = (PresentationDisplay) ModeCoordinatorImpl.getInstance().getAttachProtocol(945);
                        if (presentationDisplay2 != null) {
                            presentationDisplay2.cancel();
                        }
                    } else if (i == 10) {
                        int i2 = message.arg1;
                        Log.d(str, String.format(Locale.ENGLISH, "exception occurs, msg = %s , exception = 0x%x", new Object[]{message, Integer.valueOf(i2)}));
                        if (!(i2 == 230 || i2 == 231)) {
                            if (i2 != 236) {
                                if (i2 != 237) {
                                    switch (i2) {
                                        case 226:
                                        case 228:
                                            break;
                                        case 227:
                                            Util.showErrorAndFinish(activityBase, R.string.camera_disabled, false);
                                            break;
                                    }
                                }
                            }
                            Util.showErrorAndFinish(activityBase, CameraSettings.updateOpenCameraFailTimes() <= 1 ? R.string.cannot_connect_camera_twice : R.string.cannot_connect_camera_once, z);
                            activityBase.showErrorDialog();
                        }
                        z = false;
                        Util.showErrorAndFinish(activityBase, CameraSettings.updateOpenCameraFailTimes() <= 1 ? R.string.cannot_connect_camera_twice : R.string.cannot_connect_camera_once, z);
                        activityBase.showErrorDialog();
                    }
                } else if (!activityBase.isActivityPaused()) {
                    activityBase.showDebugInfo((String) message.obj);
                }
            }
        }
    }

    public class SaveGaussian2File implements Runnable {
        Bitmap mBitmap;

        public SaveGaussian2File(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        public void run() {
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null && !bitmap.isRecycled()) {
                Util.saveLastFrameGaussian2File(this.mBitmap);
            }
        }
    }

    private void addSecureUriIfNecessary(Uri uri) {
        ArrayList arrayList = this.mSecureUriList;
        if (arrayList != null) {
            if (arrayList.size() == 100) {
                this.mSecureUriList.remove(0);
            }
            this.mSecureUriList.add(uri);
        }
    }

    private long[] getSecureStoreIds() {
        ArrayList arrayList = this.mSecureUriList;
        int i = 0;
        if (arrayList == null || arrayList.isEmpty()) {
            return new long[0];
        }
        long[] jArr = new long[this.mSecureUriList.size()];
        Iterator it = this.mSecureUriList.iterator();
        while (it.hasNext()) {
            jArr[i] = ContentUris.parseId((Uri) it.next());
            i++;
        }
        return jArr;
    }

    /* access modifiers changed from: private */
    public void showBlurView(Bitmap bitmap) {
        Rect displayRect = Util.getDisplayRect();
        ((MarginLayoutParams) this.mGLCoverView.getLayoutParams()).topMargin = displayRect.top;
        this.mGLCoverView.setMaxWidth(displayRect.right - displayRect.left);
        this.mGLCoverView.setMaxHeight(displayRect.bottom - displayRect.top);
        this.mGLCoverView.setImageBitmap(bitmap);
        this.mGLCoverView.setAlpha(1.0f);
        this.mGLCoverView.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void checkGalleryLock() {
        this.mGalleryLocked = Util.isAppLocked(this, Util.REVIEW_ACTIVITY_PACKAGE);
        StringBuilder sb = new StringBuilder();
        sb.append("checkGalleryLock: galleryLocked=");
        sb.append(this.mGalleryLocked);
        Log.v(TAG, sb.toString());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkKeyguardFlag() {
        String str;
        ArrayList arrayList;
        this.mStartFromKeyguard = getKeyguardFlag();
        boolean z = this.mStartFromKeyguard && this.mKeyguardManager.isKeyguardSecure() && this.mKeyguardManager.isKeyguardLocked();
        this.mKeyguardSecureLocked = z;
        if (this.mStartFromKeyguard && !this.mIsFinishInKeyguard) {
            setShowWhenLocked(true);
            this.mIsFinishInKeyguard = false;
            this.mHandler.sendEmptyMessageDelayed(1, 100);
        }
        DataRepository.dataItemGlobal().setStartFromKeyguard(this.mKeyguardSecureLocked);
        if (this.mKeyguardSecureLocked || isGalleryLocked()) {
            if (this.mSecureUriList == null) {
                arrayList = new ArrayList();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("checkKeyguard: fromKeyguard=");
            sb.append(this.mStartFromKeyguard);
            sb.append(" keyguardSecureLocked=");
            sb.append(this.mKeyguardSecureLocked);
            sb.append(" secureUriList is ");
            if (this.mSecureUriList != null) {
                str = "null";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("not null (");
                sb2.append(this.mSecureUriList.size());
                sb2.append(")");
                str = sb2.toString();
            }
            sb.append(str);
            Log.v(TAG, sb.toString());
        }
        arrayList = null;
        this.mSecureUriList = arrayList;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("checkKeyguard: fromKeyguard=");
        sb3.append(this.mStartFromKeyguard);
        sb3.append(" keyguardSecureLocked=");
        sb3.append(this.mKeyguardSecureLocked);
        sb3.append(" secureUriList is ");
        if (this.mSecureUriList != null) {
        }
        sb3.append(str);
        Log.v(TAG, sb3.toString());
    }

    /* access modifiers changed from: protected */
    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public boolean couldShowErrorDialog() {
        return !this.mCameraErrorShown;
    }

    public void createCameraScreenNail(boolean z, boolean z2) {
        if (this.mCameraScreenNail == null) {
            this.mCameraScreenNail = new CameraScreenNail(new NailListener() {
                public int getOrientation() {
                    return ActivityBase.this.mOrientation;
                }

                public boolean isKeptBitmapTexture() {
                    return ActivityBase.this.mCurrentModule.isKeptBitmapTexture();
                }

                public void onFrameAvailable(int i) {
                    String str = ActivityBase.TAG;
                    if (1 == i && ActivityBase.this.mAppStartTime != 0) {
                        try {
                            long currentTimeMillis = System.currentTimeMillis() - ActivityBase.this.mAppStartTime;
                            CameraStatUtils.trackStartAppCost(currentTimeMillis);
                            if (ActivityBase.this.mAppLunchMap != null) {
                                ScenarioTrackUtil.trackAppLunchTimeEnd(ActivityBase.this.mAppLunchMap, ActivityBase.this.getApplicationContext());
                            } else {
                                ScenarioTrackUtil.trackScenarioAbort(ScenarioTrackUtil.sLaunchTimeScenario);
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("onFrameAvailable: trackStartAppCost: ");
                            sb.append(currentTimeMillis);
                            Log.d(str, sb.toString());
                        } catch (IllegalArgumentException e) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(e.getMessage());
                            sb2.append(", start time: ");
                            sb2.append(ActivityBase.this.mAppStartTime);
                            sb2.append(", now: ");
                            sb2.append(System.currentTimeMillis());
                            Log.w(str, sb2.toString());
                        }
                        ActivityBase.this.mAppStartTime = 0;
                    } else if (ActivityBase.this.mModeSwitchTime != 0) {
                        long currentTimeMillis2 = System.currentTimeMillis() - ActivityBase.this.mModeSwitchTime;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("onFrameAvailable: trackModeSwitchCost: ");
                        sb3.append(currentTimeMillis2);
                        Log.d(str, sb3.toString());
                        ActivityBase.this.mModeSwitchTime = 0;
                    }
                    ActivityBase.this.dismissBlurCover();
                    ActivityBase.this.notifyOnFirstFrameArrived(i);
                }

                public void onPreviewPixelsRead(byte[] bArr, int i, int i2) {
                    ActivityBase.this.mCurrentModule.onPreviewPixelsRead(bArr, i, i2);
                }

                public void onPreviewTextureCopied() {
                }

                public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
                }

                public boolean onSurfaceTexturePending(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
                    Module module = ActivityBase.this.mCurrentModule;
                    return module != null && module.onSurfaceTexturePending(gLCanvas, drawExtTexAttribute);
                }

                public void onSurfaceTextureReleased() {
                    Module module = ActivityBase.this.mCurrentModule;
                    if (module != null) {
                        module.onSurfaceTextureReleased();
                    }
                }

                public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
                    ActivityBase.this.onSurfaceTextureUpdated(gLCanvas, drawExtTexAttribute);
                    Module module = ActivityBase.this.mCurrentModule;
                    if (module != null) {
                        module.onSurfaceTextureUpdated(gLCanvas, drawExtTexAttribute);
                    }
                }
            }, new RequestRenderListener() {
                public void requestRender() {
                    if (ActivityBase.this.mCameraScreenNail.isAnimationRunning() || ActivityBase.this.mCameraScreenNail.getExternalFrameProcessor() == null || !ActivityBase.this.mCameraScreenNail.getExternalFrameProcessor().isProcessorReady()) {
                        ActivityBase.this.mGLView.requestRender();
                    }
                    Module module = ActivityBase.this.mCurrentModule;
                    if (module != null) {
                        module.requestRender();
                    }
                }
            });
        }
        initCameraScreenNail();
    }

    public void dismissBlurCover() {
        ImageView imageView = this.mGLCoverView;
        if (imageView != null) {
            if (imageView.getVisibility() == 8) {
                this.mBlurStartTime = -1;
                return;
            }
            this.mGLCoverView.post(new Runnable() {
                public void run() {
                    ActivityBase.this.mGLCoverView.animate().alpha(0.0f).setDuration(C0122O00000o.instance().OOOOOoO() ? 100 : 200).withEndAction(new Runnable() {
                        public void run() {
                            ActivityBase.this.mGLCoverView.setVisibility(8);
                        }
                    }).start();
                }
            });
            if (this.mBlurStartTime > -1 && SystemClock.uptimeMillis() - this.mBlurStartTime > 3000) {
                AftersalesManager.getInstance().count(System.currentTimeMillis(), 3);
            }
        }
        this.mBlurStartTime = -1;
    }

    public void dismissKeyguard() {
        if (this.mStartFromKeyguard) {
            sendBroadcast(new Intent(Util.ACTION_DISMISS_KEY_GUARD));
        }
    }

    public CameraAppImpl getCameraAppImpl() {
        return this.mApplication;
    }

    public CameraIntentManager getCameraIntentManager() {
        this.mCameraIntentManager = CameraIntentManager.getInstance(getIntent());
        return this.mCameraIntentManager;
    }

    public CameraScreenNail getCameraScreenNail() {
        return this.mCameraScreenNail;
    }

    public Module getCurrentModule() {
        return this.mCurrentModule;
    }

    public int getDisplayRotation() {
        return Util.getDisplayRotation(this);
    }

    public V9EdgeShutterView getEdgeShutterView() {
        return this.mEdgeShutterView;
    }

    public V6CameraGLSurfaceView getGLView() {
        return this.mGLView;
    }

    /* access modifiers changed from: protected */
    public boolean getKeyguardFlag() {
        return getCameraIntentManager().isQuickLaunch();
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public ScreenHint getScreenHint() {
        return this.mScreenHint;
    }

    public ArrayList getSecureUriList() {
        return this.mSecureUriList;
    }

    public long getSoundPlayTime() {
        MiuiCameraSound miuiCameraSound = this.mCameraSound;
        if (miuiCameraSound != null) {
            return miuiCameraSound.getLastSoundPlayTime();
        }
        return 0;
    }

    public ThumbnailUpdater getThumbnailUpdater() {
        return this.mThumbnailUpdater;
    }

    public void gotoGallery() {
        Intent intent;
        if (!isActivityPaused()) {
            Thumbnail thumbnail = this.mThumbnailUpdater.getThumbnail();
            if (thumbnail != null) {
                Uri uri = thumbnail.getUri();
                boolean isUriValid = Util.isUriValid(uri, getContentResolver());
                String str = TAG;
                if (!isUriValid) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Uri invalid. uri=");
                    sb.append(uri);
                    Log.e(str, sb.toString());
                    if (!thumbnail.isWaitingForUri()) {
                        getThumbnailUpdater().getLastThumbnailUncached();
                    }
                    return;
                }
                try {
                    boolean OO00ooO = C0122O00000o.instance().OO00ooO();
                    String str2 = Util.REVIEW_ACTIVITY_PACKAGE;
                    String str3 = Util.REVIEW_ACTION;
                    if (OO00ooO) {
                        intent = new Intent(str3, uri);
                        intent.setPackage(Util.ANDROID_ONE_REVIEW_ACTIVITY_PACKAGE);
                    } else {
                        intent = new Intent(Util.INTENT_MIUI_GALLERY_REVIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) == null) {
                            Log.d(str, "gotoGallery: com.android.camera.action.REVIEW");
                            intent = new Intent(str3, uri);
                        } else {
                            Log.d(str, "gotoGallery: com.miui.camera.action.REVIEW");
                            Rect viewRect = this.mThumbnailUpdater.getViewRect();
                            if (viewRect != null) {
                                intent.putExtra(Util.EXTRA_PHOTO_ENTER_TRANSIT_POS_X, viewRect.centerX());
                                intent.putExtra(Util.EXTRA_PHOTO_ENTER_TRANSIT_POS_Y, viewRect.centerY());
                                intent.putExtra(Util.EXTRA_PHOTO_ENTER_TRANSIT_WIDTH, viewRect.width());
                                intent.putExtra(Util.EXTRA_PHOTO_ENTER_TRANSIT_HEIGHT, viewRect.height());
                            }
                            if (this.mOrientation != 90) {
                                if (this.mOrientation != 270) {
                                    Util.enableSeamlessRotation(this, false);
                                }
                            }
                            Util.enableSeamlessRotation(this, true);
                        }
                        intent.setPackage(str2);
                        intent.putExtra(Util.KEY_REVIEW_FROM_MIUICAMERA, true);
                    }
                    if (C0124O00000oO.OOoOooO()) {
                        if (this.mCameraBrightness.getCurrentBrightnessAuto() != 0.0f) {
                            intent.putExtra(Util.KEY_CAMERA_BRIGHTNESS_AUTO, this.mCameraBrightness.getCurrentBrightnessAuto());
                        } else {
                            intent.putExtra(Util.KEY_CAMERA_BRIGHTNESS_MANUAL, this.mCameraBrightness.getCurrentBrightnessManual());
                            intent.putExtra(Util.KEY_CAMERA_BRIGHTNESS, this.mCameraBrightness.getCurrentBrightness());
                        }
                    }
                    if (startFromKeyguard()) {
                        intent.putExtra(C0122O00000o.instance().OO00ooO() ? Util.ANDROID_ONE_EXTRA_IS_SECURE_MODE : "StartActivityWhenLocked", true);
                    }
                    if (Util.isAppLocked(this, str2)) {
                        intent.putExtra(Util.EXTRAS_SKIP_LOCK, true);
                    }
                    if (thumbnail.isGif()) {
                        int i = 300;
                        int gifWidth = thumbnail.getGifWidth() <= 0 ? 300 : thumbnail.getGifWidth();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("gotoGallery: gifWidth: ");
                        sb2.append(gifWidth);
                        Log.d(str, sb2.toString());
                        if (thumbnail.getGifHeight() > 0) {
                            i = thumbnail.getGifHeight();
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("gotoGallery: gifHeight: ");
                        sb3.append(i);
                        Log.d(str, sb3.toString());
                        intent.putExtra(Util.EXTRA_WIDTH_FROM_CAMERA, gifWidth);
                        intent.putExtra(Util.EXTRA_HEIGHT_FROM_CAMERA, i);
                        intent.putExtra(Util.EXTRA_MIME_TYPE_FROM_CAMERA, Storage.MIME_GIF);
                    }
                    if (this.mSecureUriList != null) {
                        if (C0122O00000o.instance().OO00ooO()) {
                            intent.putExtra(Util.ANDROID_ONE_EXTRA_SECURE_MODE_MEDIA_STORE_IDS, getSecureStoreIds());
                        } else {
                            intent.putParcelableArrayListExtra(Util.KEY_SECURE_ITEMS, this.mSecureUriList);
                        }
                    }
                    if (C0122O00000o.instance().OOOOoO()) {
                        intent.putExtra("using_deputy_screen", DataRepository.dataItemGlobal().getDisplayMode() == 2);
                    }
                    intent.putExtra("device_orientation", this.mOrientation);
                    startActivity(intent);
                    this.mJumpFlag = 1;
                    this.mJumpedToGallery = true;
                    if (this.mCurrentModule != null) {
                        this.mCurrentModule.enableCameraControls(false);
                        CameraStatUtils.trackGotoGallery(this.mCurrentModule.getModuleIndex());
                    }
                } catch (ActivityNotFoundException e) {
                    Log.e(str, "review activity not found!", (Throwable) e);
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", uri));
                    } catch (ActivityNotFoundException e2) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("review image fail. uri=");
                        sb4.append(uri);
                        Log.e(str, sb4.toString(), (Throwable) e2);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0036, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean hasSurface() {
        int i = this.mCurrentSurfaceState;
        if (i != 2) {
            if (i == 4) {
                if (getCameraScreenNail().getSurfaceTexture() != null) {
                    return true;
                }
                this.mGLView.onResume();
                return false;
            }
        } else if (Display.isFullScreenNavBarHidden()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ActivityBase.this.mGLView.setVisibility(4);
                    ActivityBase.this.mGLView.setVisibility(0);
                }
            });
        } else {
            this.mGLView.onResume();
        }
    }

    public void initCameraScreenNail() {
        Log.d(TAG, "initCameraScreenNail");
        CameraScreenNail cameraScreenNail = this.mCameraScreenNail;
        if (cameraScreenNail != null && cameraScreenNail.getSurfaceTexture() == null) {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            this.mCameraScreenNail.setPreviewSize(point.x, point.y);
        }
    }

    public boolean isActivityPaused() {
        return this.mActivityPaused;
    }

    public boolean isActivityStarted() {
        return this.mActivityStarted;
    }

    public boolean isActivityStopped() {
        return this.mActivityStopped;
    }

    public boolean isCameraAliveWhenResume() {
        boolean z;
        CookieStore cookieStore = CameraService.getCookieStore();
        if (cookieStore != null) {
            Iterator it = cookieStore.getCookies().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((Cookie) it.next()).mCamera2Device != null) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            Module module = this.mCurrentModule;
            boolean z2 = module == null && module.isCreated() && !this.mCurrentModule.isDeparted();
            Log.d(TAG, String.format("isCameraAliveWhenResume: mRealeaseByModule : %b , isModuleAlive: %b, isCameraDevicesAlive: %b", new Object[]{Boolean.valueOf(this.mReleaseByModule), Boolean.valueOf(z2), Boolean.valueOf(z)}));
            return (!this.mReleaseByModule || z2) && z;
        }
        z = false;
        Module module2 = this.mCurrentModule;
        if (module2 == null) {
        }
        Log.d(TAG, String.format("isCameraAliveWhenResume: mRealeaseByModule : %b , isModuleAlive: %b, isCameraDevicesAlive: %b", new Object[]{Boolean.valueOf(this.mReleaseByModule), Boolean.valueOf(z2), Boolean.valueOf(z)}));
        if (!this.mReleaseByModule) {
        }
    }

    public boolean isGalleryLocked() {
        return this.mGalleryLocked;
    }

    public boolean isGotoGallery() {
        return this.mJumpFlag == 1;
    }

    public boolean isGotoSettings() {
        return this.mJumpFlag == 2;
    }

    public boolean isJumpBack() {
        return this.mLastJumpFlag != 0;
    }

    public boolean isPostProcessing() {
        Module module = this.mCurrentModule;
        return module != null && module.isCreated() && this.mCurrentModule.isPostProcessing();
    }

    public boolean isPreviewThumbnail() {
        return this.mPreviewThumbnail;
    }

    /* access modifiers changed from: protected */
    public boolean isShowBottomIntentDone() {
        if (getCameraIntentManager().isImageCaptureIntent() || getCameraIntentManager().isVideoCaptureIntent()) {
            BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            if (baseDelegate != null && baseDelegate.getActiveFragment(R.id.bottom_action) == 4083) {
                return true;
            }
        }
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public boolean isSwitchingModule() {
        return this.mIsSwitchingModule;
    }

    public void loadCameraSound(int i) {
        MiuiCameraSound miuiCameraSound = this.mCameraSound;
        if (miuiCameraSound != null) {
            miuiCameraSound.load(i);
        }
    }

    @UiThread
    public abstract void notifyOnFirstFrameArrived(int i);

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 35893) {
            this.mCurrentModule.onActivityResult(i, i2, intent);
        } else if (intent != null && intent.getData() != null) {
            addSecureUriIfNecessary(intent.getData());
        }
    }

    public void onCreate(Bundle bundle) {
        if (Display.isContentViewExtendToTopEdges()) {
            CompatibilityUtils.setCutoutModeShortEdges(getWindow());
        }
        super.onCreate(bundle);
        boolean z = true;
        setVolumeControlStream(1);
        this.mScreenHint = new ScreenHint(this);
        this.mThumbnailUpdater = new ThumbnailUpdater(this);
        this.mKeyguardManager = (KeyguardManager) getSystemService("keyguard");
        this.mStartFromKeyguard = getKeyguardFlag();
        if (this.mStartFromKeyguard) {
            boolean z2 = this.mKeyguardManager.isKeyguardSecure() && this.mKeyguardManager.isKeyguardLocked();
            this.mKeyguardSecureLocked = z2;
        }
        this.mShortcutFunction = new ShortcutFunction();
        this.mShortcutFunction.initShortcut(this);
        boolean z3 = getWindowManager().getDefaultDisplay().getState() == 2;
        if (!this.mStartFromKeyguard || (z3 && !getCameraIntentManager().isFromVolumeKey().booleanValue())) {
            z = false;
        }
        String str = TAG;
        if (z) {
            Log.d(str, "onCreate: addFlag --> FLAG_TURN_SCREEN_ON");
            getWindow().addFlags(2097152);
        }
        this.mApplication.addActivity(this);
        this.mCameraBrightness = new CameraBrightness(this);
        this.mLocationManager = LocationManager.instance();
        this.mCloseActivityThread = new Thread(new Runnable() {
            public void run() {
                ActivityBase activityBase = ActivityBase.this;
                activityBase.mApplication.closeAllActivitiesBut(activityBase);
            }
        });
        try {
            this.mCloseActivityThread.start();
        } catch (IllegalThreadStateException e) {
            Log.e(str, e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (!C0122O00000o.instance().OOOOOoO()) {
            CameraScreenNail cameraScreenNail = this.mCameraScreenNail;
            if (cameraScreenNail != null) {
                Bitmap lastFrameGaussianBitmap = cameraScreenNail.getLastFrameGaussianBitmap();
                if (lastFrameGaussianBitmap != null) {
                    Schedulers.io().scheduleDirect(new SaveGaussian2File(lastFrameGaussianBitmap));
                }
            }
        }
        PopupManager.removeInstance(this);
        this.mApplication.removeActivity(this);
        Disposable disposable = this.mTrackAppLunchDisposable;
        if (disposable != null) {
            disposable.dispose();
            this.mTrackAppLunchDisposable = null;
        }
        MiuiCameraSound miuiCameraSound = this.mCameraSound;
        if (miuiCameraSound != null) {
            miuiCameraSound.release();
            this.mCameraSound = null;
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 84 || !keyEvent.isLongPress()) {
            return super.onKeyDown(i, keyEvent);
        }
        return true;
    }

    public void onLayoutChange(Rect rect) {
        int i;
        int i2;
        this.mCameraScreenNail.setDisplayArea(rect);
        int displayRotation = Util.getDisplayRotation(this) % 180;
        CameraScreenNail cameraScreenNail = this.mCameraScreenNail;
        if (displayRotation == 0) {
            i2 = rect.width();
            i = rect.height();
        } else {
            i2 = rect.height();
            i = rect.width();
        }
        cameraScreenNail.setPreviewFrameLayoutSize(i2, i);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkGalleryLock();
        checkKeyguardFlag();
    }

    public void onNewUriArrived(Uri uri, String str) {
        Module module = this.mCurrentModule;
        if (module != null) {
            module.onNewUriArrived(uri, str);
        }
        if (uri != null) {
            addSecureUriIfNecessary(uri);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Module module = this.mCurrentModule;
        if (module != null) {
            module.onSaveInstanceState(bundle);
        }
    }

    public boolean onSearchRequested() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mGLView.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        V6CameraGLSurfaceView v6CameraGLSurfaceView = this.mGLView;
        if (v6CameraGLSurfaceView != null) {
            v6CameraGLSurfaceView.onPause();
        }
    }

    /* access modifiers changed from: protected */
    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
    }

    public void playCameraSound(int i) {
        this.mCameraSound.playSound(i);
    }

    public void playCameraSound(int i, float f) {
        this.mCameraSound.playSound(i, f);
    }

    /* access modifiers changed from: protected */
    public void releaseCameraScreenNail() {
        Log.d(TAG, "releaseCameraScreenNail: ");
        CameraScreenNail cameraScreenNail = this.mCameraScreenNail;
        if (cameraScreenNail != null) {
            cameraScreenNail.releaseSurfaceTexture();
        }
        Module module = this.mCurrentModule;
        if (module != null) {
            module.setFrameAvailable(false);
        }
    }

    public void resetStartTime() {
        this.mAppStartTime = 0;
    }

    public void setErrorDialog(AlertDialog alertDialog) {
        this.mErrorDialog = alertDialog;
    }

    public void setJumpFlag(int i) {
        this.mJumpFlag = i;
    }

    public void setPreviewThumbnail(boolean z) {
        this.mPreviewThumbnail = z;
    }

    public void setSwitchingModule(boolean z) {
        this.mIsSwitchingModule = z;
    }

    /* access modifiers changed from: protected */
    public void showBlurCover() {
        if (!isShowBottomIntentDone() && !isCameraAliveWhenResume() && !isPostProcessing() && !getCameraIntentManager().isFromScreenSlide().booleanValue() && !Util.isHasBackLightSensor() && !isJumpBack()) {
            if (!C0122O00000o.instance().OOOOOoO()) {
                final long currentTimeMillis = System.currentTimeMillis();
                Bitmap bitmap = null;
                CameraScreenNail cameraScreenNail = this.mCameraScreenNail;
                if (cameraScreenNail != null) {
                    bitmap = cameraScreenNail.getLastFrameGaussianBitmap();
                }
                if (bitmap == null || bitmap.isRecycled()) {
                    this.mGLCoverDisposable = new Single() {
                        /* access modifiers changed from: protected */
                        public void subscribeActual(SingleObserver singleObserver) {
                            Bitmap decodeFile = BitmapFactory.decodeFile(new File(ActivityBase.this.getFilesDir(), Util.LAST_FRAME_GAUSSIAN_FILE_NAME).getAbsolutePath());
                            Log.d(ActivityBase.TAG, "showBlurCover: blur bitmap from user blur file!");
                            singleObserver.onSuccess(decodeFile);
                        }
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
                        public void accept(Bitmap bitmap) {
                            if (bitmap == null || bitmap.isRecycled()) {
                                ActivityBase.this.mGLCoverView.setVisibility(8);
                            } else {
                                ActivityBase.this.showBlurView(bitmap);
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("showBlurCover: show... cost time = ");
                            sb.append(System.currentTimeMillis() - currentTimeMillis);
                            sb.append(d.H);
                            Log.d(ActivityBase.TAG, sb.toString());
                        }
                    });
                } else {
                    Log.d(TAG, "showBlurCover: blur bitmap from memory!");
                    showBlurView(bitmap);
                }
            } else {
                Rect displayRect = Util.getDisplayRect();
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mGLCoverView.getLayoutParams();
                int i = displayRect.top;
                marginLayoutParams.topMargin = i;
                marginLayoutParams.height = displayRect.bottom - i;
                this.mGLCoverView.setBackgroundColor(2130706432);
                this.mGLCoverView.setAlpha(1.0f);
                this.mGLCoverView.setVisibility(0);
            }
            this.mBlurStartTime = SystemClock.uptimeMillis();
        }
    }

    public void showDebugInfo(String str) {
        TextView textView = this.mDebugInfoView;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void showErrorDialog() {
        this.mCameraErrorShown = true;
    }

    public boolean startFromKeyguard() {
        return this.mStartFromKeyguard;
    }

    public boolean startFromSecureKeyguard() {
        return this.mKeyguardSecureLocked;
    }

    /* access modifiers changed from: protected */
    public void trackAppLunchTimeStart(boolean z) {
        ScenarioTrackUtil.trackAppLunchTimeStart(z);
        this.mTrackAppLunchDisposable = new Single() {
            /* access modifiers changed from: protected */
            public void subscribeActual(SingleObserver singleObserver) {
                HashMap hashMap = new HashMap();
                String execCommand = Util.execCommand("cat /dev/cpuset/camera-daemon/cpus", false);
                if (execCommand != null) {
                    hashMap.put("cpus", execCommand);
                    String execCommand2 = Util.execCommand("cat $(dirname $(grep -nir \"xo_therm\" /sys/class/thermal/thermal_zone*/type))/temp", false);
                    if (execCommand2 != null) {
                        hashMap.put("temperature", execCommand2);
                        String execCommand3 = Util.execCommand("cat /proc/meminfo|grep -E 'MemFree|MemAvailable'", true);
                        if (execCommand3 != null) {
                            String[] split = execCommand3.split("\r\n");
                            if (split.length == 2) {
                                for (String split2 : split) {
                                    String[] split3 = split2.split(":");
                                    hashMap.put(split3[0], split3[1].replaceAll("\\D", ""));
                                }
                                singleObserver.onSuccess(hashMap);
                            }
                        }
                    }
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
            public void accept(HashMap hashMap) {
                ActivityBase.this.mAppLunchMap = hashMap;
            }
        });
    }

    public synchronized void updateSurfaceState(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateSurfaceState: ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mCurrentSurfaceState = i;
    }
}
