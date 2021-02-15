package com.android.camera.features.mimoji2.fragment.edit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.camera.ActivityBase;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.features.mimoji2.bean.MimojiLevelBean2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2.MimojiEmoticon;
import com.android.camera.features.mimoji2.utils.ClickCheck2;
import com.android.camera.features.mimoji2.utils.ConfigInfoThumUtils2;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.storage.Storage;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;
import com.arcsoft.avatar2.AvatarEngine;
import com.arcsoft.avatar2.emoticon.AvatarEmoManager;
import com.arcsoft.avatar2.emoticon.AvatarEmoManager.AvatarEmoResCallback;
import com.arcsoft.avatar2.emoticon.EmoInfo;
import com.arcsoft.avatar2.emoticon.EmoInfo.EmoExtraInfo;
import com.arcsoft.avatar2.util.AvatarConfigUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class MimojiThumbnailRenderThread2 extends Thread {
    private static float[] BACKGROUND_COLOR = {0.1098f, 0.1176f, 0.1254f, 1.0f};
    private static final int MSG_AVATAR_INIT = 32;
    public static final int MSG_DRAW_REQUESTED = 16;
    private static final int MSG_QUIT_REQUESTED = 64;
    private static final int MSG_RESET_DATA = 96;
    private static final int MSG_SET_CONFIG = 80;
    private static final int MSG_START_EMO_PICTURE = 128;
    private static final int MSG_START_EMO_VIDEO = 112;
    private static final int MSG_UPDATE_THUMB = 48;
    private static final int[] RESOURCE_DESC = {R.string.mimoji_cartoon_angry, R.string.mimoji_cartoon_sadness, R.string.mimoji_cartoon_expect, R.string.mimoji_cartoon_love, R.string.mimoji_cartoon_ok, R.string.mimoji_cartoon_love_you};
    /* access modifiers changed from: private */
    public static final String TAG = "MimojiThumbnailRenderThread2";
    private AvatarEngine mAvatarForEdit;
    private ConfigInfoThumUtils2 mConfigInfoThumUtils2;
    private Context mContext;
    /* access modifiers changed from: private */
    public AtomicInteger mCountEmotGif = new AtomicInteger();
    private String mCurrentConfigPath;
    private EGLWrapper2 mEGLWrapper2;
    private volatile boolean mEglContextPrepared = false;
    /* access modifiers changed from: private */
    public AvatarEmoManager mEmoManager;
    private RenderHandler mHandler;
    private final int mHeight;
    private volatile boolean mIsEmoticonForThumbnail = false;
    private volatile boolean mIsRendering = false;
    private final Object mLock = new Object();
    private volatile boolean mReady = false;
    private volatile int mRequestDraw;
    private volatile boolean mRequestRelease = false;
    private volatile boolean mRestStopRenderThumbnail = false;
    private volatile boolean mStopRenderThumbnail = false;
    private Handler mUpdateHandler;
    private final int mWidth;

    public class RenderHandler extends Handler {
        private final WeakReference mRenderThread;

        private RenderHandler(MimojiThumbnailRenderThread2 mimojiThumbnailRenderThread2) {
            this.mRenderThread = new WeakReference(mimojiThumbnailRenderThread2);
        }

        public void handleMessage(Message message) {
            MimojiThumbnailRenderThread2 mimojiThumbnailRenderThread2 = (MimojiThumbnailRenderThread2) this.mRenderThread.get();
            if (mimojiThumbnailRenderThread2 != null) {
                int i = message.what;
                if (i == 16) {
                    mimojiThumbnailRenderThread2.doDraw(((Boolean) message.obj).booleanValue());
                } else if (i == 32) {
                    mimojiThumbnailRenderThread2.doInit((String) message.obj);
                } else if (i == 48) {
                    mimojiThumbnailRenderThread2.doUpdate();
                } else if (i == 64) {
                    mimojiThumbnailRenderThread2.doQuit();
                } else if (i == 80) {
                    mimojiThumbnailRenderThread2.doSetConfig((ASAvatarConfigInfo) message.obj);
                } else if (i == 96) {
                    mimojiThumbnailRenderThread2.doReset();
                } else if (i == 112) {
                    mimojiThumbnailRenderThread2.doEmoVideo((ArrayList) message.obj);
                } else if (i == 128) {
                    mimojiThumbnailRenderThread2.doEmoPicture((ArrayList) message.obj);
                }
            }
        }
    }

    public MimojiThumbnailRenderThread2(String str, int i, int i2, Context context) {
        super(str);
        this.mWidth = i;
        this.mHeight = i2;
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public void doDraw(boolean z) {
        boolean z2;
        if (this.mRequestRelease || !this.mEglContextPrepared) {
            return;
        }
        if (this.mIsEmoticonForThumbnail) {
            getEmoticonThumbnail();
            return;
        }
        synchronized (this.mLock) {
            z2 = this.mRequestDraw > 0;
            if (z2) {
                this.mRequestDraw--;
            }
        }
        if (z2) {
            drawThumbnail(z);
        }
    }

    /* access modifiers changed from: private */
    public void doEmoPicture(ArrayList arrayList) {
        FileUtils.delDir(MimojiHelper2.EMOTICON_JPEG_CACHE_DIR);
        FileUtils.makeNoMediaDir(MimojiHelper2.EMOTICON_JPEG_CACHE_DIR);
        if (this.mAvatarForEdit == null) {
            Log.e(TAG, "mimoji void doDraw[reset]  mAvatarForEdit null");
            return;
        }
        initEmoticon();
        AvatarEmoManager avatarEmoManager = this.mEmoManager;
        if (avatarEmoManager != null) {
            avatarEmoManager.stopRecording();
            MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
            int i = 0;
            while (i < arrayList.size()) {
                EmoInfo emoInfo = (EmoInfo) arrayList.get(i);
                try {
                    ByteBuffer renderImageData = this.mEmoManager.renderImageData(emoInfo, emoInfo.getEmoImageSize().getWidth(), emoInfo.getEmoImageSize().getHeight());
                    StringBuilder sb = new StringBuilder();
                    sb.append(Storage.DIRECTORY);
                    sb.append(File.separator);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("MIMOJI_GIF_");
                    sb2.append(emoInfo.getEmoName());
                    sb2.append("_");
                    sb.append(FileUtils.createtFileName(sb2.toString(), "jpg"));
                    String sb3 = sb.toString();
                    if (renderImageData != null) {
                        ((ActivityBase) this.mContext).getImageSaver().addSimpleImage(renderImageData.array(), true, Util.createJpegName(System.currentTimeMillis()), null, System.currentTimeMillis(), null, null, emoInfo.getEmoImageSize().getWidth(), emoInfo.getEmoImageSize().getHeight(), null, 180, false, false, true, false, false, null, null, -1, null);
                    }
                    if (mimojiEmoticon != null) {
                        mimojiEmoticon.updateEmoticonPictureProgress(sb3, emoInfo, i == arrayList.size() - 1);
                    }
                } catch (Exception e) {
                    String str = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("mimoji void getEmoticonPicture[] ");
                    sb4.append(e.getMessage());
                    Log.e(str, sb4.toString());
                    if (i == arrayList.size() - 1) {
                        mimojiEmoticon.coverEmoticonError();
                    }
                }
                i++;
            }
        }
    }

    /* access modifiers changed from: private */
    public void doEmoVideo(ArrayList arrayList) {
        initEmoticon();
        AvatarEmoManager avatarEmoManager = this.mEmoManager;
        if (avatarEmoManager != null) {
            avatarEmoManager.stopRecording();
            this.mCountEmotGif.set(arrayList.size());
            FileUtils.delDir(MimojiHelper2.EMOTICON_MP4_CACHE_DIR);
            FileUtils.makeNoMediaDir(MimojiHelper2.EMOTICON_MP4_CACHE_DIR);
            FileUtils.delDir(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
            FileUtils.makeNoMediaDir(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                EmoInfo emoInfo = (EmoInfo) it.next();
                StringBuilder sb = new StringBuilder();
                sb.append(MimojiHelper2.EMOTICON_MP4_CACHE_DIR);
                sb.append(emoInfo.getEmoName());
                sb.append(".mp4");
                String sb2 = sb.toString();
                String str = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" videoPath :");
                sb3.append(sb2);
                Log.d(str, sb3.toString());
                this.mEmoManager.startRecording(sb2, 0, emoInfo.getEmoImageSize().getWidth(), emoInfo.getEmoImageSize().getHeight(), 100000000, "video/avc");
                this.mEmoManager.emoProcess(emoInfo);
                AvatarEmoManager avatarEmoManager2 = this.mEmoManager;
                if (avatarEmoManager2 != null) {
                    if (avatarEmoManager2.isRelease()) {
                        break;
                    }
                } else {
                    break;
                }
            }
            Log.e("release_avatar", "-> for break ---");
            if (this.mCountEmotGif.get() != 0) {
                this.mCountEmotGif.set(0);
                MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
                if (mimojiEmoticon != null) {
                    mimojiEmoticon.coverEmoticonError();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean doInit(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "mimoji void doInit[configPath] null");
            return false;
        }
        Log.d(TAG, "init mAvatarForEdit");
        ClickCheck2.getInstance().setForceDisabled(true);
        this.mRequestRelease = true;
        if (this.mAvatarForEdit == null) {
            this.mAvatarForEdit = new AvatarEngine();
            this.mAvatarForEdit.init(AvatarEngineManager2.TRACK_DATA, AvatarEngineManager2.FACE_MODEL);
            this.mAvatarForEdit.setRenderScene(false, 0.85f);
        }
        if (TextUtils.isEmpty(this.mAvatarForEdit.getTemplatePath()) || !this.mAvatarForEdit.getTemplatePath().equals(AvatarEngineManager2.TEMPLATE_PATH_HUMAN)) {
            this.mAvatarForEdit.setTemplatePath(AvatarEngineManager2.TEMPLATE_PATH_HUMAN);
        }
        this.mAvatarForEdit.loadConfig(str);
        this.mCurrentConfigPath = str;
        this.mConfigInfoThumUtils2 = new ConfigInfoThumUtils2();
        this.mRequestRelease = false;
        doUpdate();
        return true;
    }

    /* access modifiers changed from: private */
    public void doQuit() {
        if (!this.mRequestRelease) {
            this.mRequestRelease = true;
            release();
            Looper.myLooper().quit();
        }
    }

    /* access modifiers changed from: private */
    public void doReset() {
        this.mStopRenderThumbnail = false;
        if (this.mAvatarForEdit != null) {
            draw(true);
        }
    }

    /* access modifiers changed from: private */
    public void doSetConfig(ASAvatarConfigInfo aSAvatarConfigInfo) {
        AvatarEngine avatarEngine = this.mAvatarForEdit;
        if (avatarEngine != null) {
            avatarEngine.setConfig(aSAvatarConfigInfo);
        }
    }

    /* access modifiers changed from: private */
    public void doUpdate() {
        draw(false);
    }

    private void getEmoticonThumbnail() {
        if (this.mAvatarForEdit == null) {
            Log.e(TAG, "mimoji void doDraw[reset]  mAvatarForEdit null");
            return;
        }
        initEmoticon();
        ArrayList emoList = this.mEmoManager.getEmoList();
        int i = 0;
        boolean z = false;
        while (true) {
            if (i >= emoList.size()) {
                break;
            }
            EmoInfo emoInfo = (EmoInfo) emoList.get(i);
            boolean renderEmoThumb = this.mEmoManager.renderEmoThumb(emoInfo, AvatarEngineManager2.CONFIG_EMO_THUM_SIZE.getWidth(), AvatarEngineManager2.CONFIG_EMO_THUM_SIZE.getHeight());
            if (!renderEmoThumb) {
                z = renderEmoThumb;
                break;
            }
            MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
            if (mimojiEmoticon != null) {
                mimojiEmoticon.updateEmoticonThumbnailProgress(i, emoInfo, RESOURCE_DESC[i]);
            }
            i++;
            z = renderEmoThumb;
        }
        this.mIsEmoticonForThumbnail = false;
        if (!z) {
            Log.d(TAG, "mimoji void doDraw[reset] mEmoManager.renderEmoThumb fail");
            MimojiEmoticon mimojiEmoticon2 = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
            if (mimojiEmoticon2 != null) {
                mimojiEmoticon2.coverEmoticonError();
            }
        }
    }

    private void prepare() {
        this.mEGLWrapper2 = new EGLWrapper2(this.mWidth, this.mHeight);
        this.mEGLWrapper2.makeCurrent();
    }

    private void release() {
        AvatarEngine avatarEngine = this.mAvatarForEdit;
        if (avatarEngine != null) {
            avatarEngine.releaseRender();
            this.mAvatarForEdit.unInit();
            this.mAvatarForEdit.destroy();
            this.mAvatarForEdit = null;
        }
        EGLWrapper2 eGLWrapper2 = this.mEGLWrapper2;
        if (eGLWrapper2 != null) {
            eGLWrapper2.release();
            this.mEGLWrapper2 = null;
        }
        AvatarEmoManager avatarEmoManager = this.mEmoManager;
        if (avatarEmoManager != null) {
            avatarEmoManager.release();
            this.mEmoManager = null;
        }
    }

    private void resetConfig(ArrayList arrayList) {
        AvatarEngine avatarEngine = this.mAvatarForEdit;
        if (avatarEngine == null) {
            doInit(this.mCurrentConfigPath);
            Log.e(TAG, "mimoji  resetConfig mAvatarForEdit null");
            return;
        }
        this.mConfigInfoThumUtils2.reset(avatarEngine, AvatarEngineManager2.getInstance().getASAvatarConfigValue());
        int i = 0;
        int currentConfigIdWithType = AvatarConfigUtils.getCurrentConfigIdWithType(((ASAvatarConfigInfo) arrayList.get(0)).configType, AvatarEngineManager2.getInstance().getASAvatarConfigValue());
        if (currentConfigIdWithType != -1) {
            i = currentConfigIdWithType;
        }
        ASAvatarConfigInfo aSAvatarConfigInfo = null;
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ASAvatarConfigInfo aSAvatarConfigInfo2 = (ASAvatarConfigInfo) it.next();
            if (aSAvatarConfigInfo2.configID == i) {
                aSAvatarConfigInfo = aSAvatarConfigInfo2;
                break;
            }
        }
        if (aSAvatarConfigInfo != null) {
            this.mAvatarForEdit.setConfig(aSAvatarConfigInfo);
        }
    }

    public void draw(boolean z) {
        if (this.mHandler != null) {
            synchronized (this.mLock) {
                if (!this.mRequestRelease) {
                    if (this.mEglContextPrepared) {
                        this.mRequestDraw++;
                        Message obtainMessage = this.mHandler.obtainMessage();
                        obtainMessage.what = 16;
                        obtainMessage.obj = Boolean.valueOf(z);
                        this.mHandler.sendMessage(obtainMessage);
                    }
                }
            }
        }
    }

    public void drawForEmoticonPicture(List list) {
        if (this.mHandler != null) {
            initEmoticon();
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 128;
            obtainMessage.obj = list;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void drawForEmoticonThumbnail() {
        quitEmoticonVideo();
        this.mIsEmoticonForThumbnail = true;
        draw(false);
    }

    public void drawThumbnail(boolean z) {
        if (!TextUtils.isEmpty(this.mCurrentConfigPath)) {
            AvatarEngine avatarEngine = this.mAvatarForEdit;
            if (avatarEngine == null) {
                doInit(this.mCurrentConfigPath);
                Log.i(TAG, "mimoji  drawThumbnail mAvatarForEdit null");
                return;
            }
            if (z) {
                avatarEngine.loadConfig(this.mCurrentConfigPath);
                AvatarEngineManager2.getInstance().resetData();
            }
            this.mIsRendering = true;
            int selectType = AvatarEngineManager2.getInstance().getSelectType();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("select  Type : ");
            sb.append(selectType);
            Log.i(str, sb.toString());
            CopyOnWriteArrayList subConfigList = AvatarEngineManager2.getInstance().getSubConfigList(this.mContext, selectType);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mimojiLevelBean2s.size   :");
            sb2.append(subConfigList.size());
            Log.i(str2, sb2.toString());
            for (int i = 0; i < subConfigList.size(); i++) {
                MimojiLevelBean2 mimojiLevelBean2 = (MimojiLevelBean2) subConfigList.get(i);
                if (mimojiLevelBean2 != null) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("tempMimojiLevelBeans2 mConfigTypeName : ");
                    sb3.append(mimojiLevelBean2.mConfigTypeName);
                    Log.i(str3, sb3.toString());
                    ArrayList arrayList = mimojiLevelBean2.mThumnails;
                    if (arrayList == null) {
                        continue;
                    } else {
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            ASAvatarConfigInfo aSAvatarConfigInfo = (ASAvatarConfigInfo) arrayList.get(i2);
                            if (aSAvatarConfigInfo == null) {
                                String str4 = TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("asainfo is null   curIndex : ");
                                sb4.append(i2);
                                Log.i(str4, sb4.toString());
                            } else {
                                this.mConfigInfoThumUtils2.renderThumb(this.mAvatarForEdit, aSAvatarConfigInfo, AvatarEngineManager2.getInstance().getASAvatarConfigValue().gender, BACKGROUND_COLOR);
                                Message obtainMessage = this.mUpdateHandler.obtainMessage();
                                if (this.mRestStopRenderThumbnail) {
                                    this.mStopRenderThumbnail = false;
                                    this.mRestStopRenderThumbnail = false;
                                    this.mIsRendering = false;
                                    AvatarEngineManager2.getInstance().resetData();
                                    AvatarEngineManager2.getInstance().setTypeNeedUpdate(selectType, false);
                                    resetConfig(arrayList);
                                    draw(true);
                                    return;
                                } else if (this.mStopRenderThumbnail) {
                                    this.mStopRenderThumbnail = false;
                                    this.mIsRendering = false;
                                    resetConfig(arrayList);
                                    AvatarEngineManager2.getInstance().setTypeNeedUpdate(selectType, true);
                                    obtainMessage.what = 6;
                                    this.mUpdateHandler.sendMessage(obtainMessage);
                                    return;
                                } else {
                                    obtainMessage.what = 5;
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("OUTER", i);
                                    bundle.putInt("INNER", i2);
                                    bundle.putInt("TYPE", selectType);
                                    obtainMessage.obj = bundle;
                                    Handler handler = this.mUpdateHandler;
                                    if (handler != null) {
                                        handler.sendMessage(obtainMessage);
                                    }
                                }
                            }
                        }
                        resetConfig(arrayList);
                    }
                }
            }
            AvatarEngineManager2.getInstance().setTypeNeedUpdate(selectType, false);
            ClickCheck2.getInstance().setForceDisabled(false);
            this.mIsRendering = false;
        }
    }

    public RenderHandler getHandler() {
        synchronized (this.mLock) {
            if (!this.mReady) {
                throw new IllegalStateException("render thread is not ready yet");
            }
        }
        return this.mHandler;
    }

    public boolean getIsRendering() {
        return this.mIsRendering;
    }

    public void initAvatar(String str) {
        RenderHandler renderHandler = this.mHandler;
        if (renderHandler != null) {
            Message obtainMessage = renderHandler.obtainMessage();
            obtainMessage.what = 32;
            obtainMessage.obj = str;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void initEmoticon() {
        if (this.mAvatarForEdit != null) {
            AvatarEmoManager avatarEmoManager = this.mEmoManager;
            if (avatarEmoManager == null || avatarEmoManager.isRelease()) {
                this.mEmoManager = new AvatarEmoManager(this.mAvatarForEdit, AvatarEngineManager2.TEMPLATE_PATH_GIF, AvatarEngineManager2.getInstance().getASAvatarConfigValue().configFaceColorID, new AvatarEmoResCallback() {
                    public void onFrameRefresh(EmoExtraInfo emoExtraInfo) {
                        String access$100 = MimojiThumbnailRenderThread2.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onFrameRefresh emoExtraInfo : ");
                        sb.append(emoExtraInfo.index);
                        Log.d(access$100, sb.toString());
                        if (MimojiThumbnailRenderThread2.this.mEmoManager != null && !MimojiThumbnailRenderThread2.this.mEmoManager.isRelease()) {
                            MimojiThumbnailRenderThread2.this.mEmoManager.emoGLRender(emoExtraInfo);
                        }
                    }

                    public void onMakeMediaEnd() {
                        String access$100 = MimojiThumbnailRenderThread2.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onMakeMediaEnd 时间: ");
                        sb.append(System.currentTimeMillis());
                        Log.d(access$100, sb.toString());
                        if (MimojiThumbnailRenderThread2.this.mEmoManager == null || MimojiThumbnailRenderThread2.this.mEmoManager.isRelease()) {
                            Log.e("release_avatar", "-> for break ---");
                            return;
                        }
                        MimojiThumbnailRenderThread2.this.mEmoManager.stopRecording();
                        int decrementAndGet = MimojiThumbnailRenderThread2.this.mCountEmotGif.decrementAndGet();
                        MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
                        if (mimojiEmoticon != null) {
                            mimojiEmoticon.updateEmoticonGifProgress(decrementAndGet);
                        }
                    }
                });
            }
            this.mEmoManager.setFaceColorId(AvatarEngineManager2.getInstance().getASAvatarConfigValue().configFaceColorID);
            AvatarEngineManager2.getInstance().setEmoManager(this.mEmoManager);
        }
    }

    public void quit() {
        RenderHandler renderHandler = this.mHandler;
        if (renderHandler != null) {
            renderHandler.obtainMessage(64).sendToTarget();
        }
    }

    public void quitEmoticonVideo() {
        AvatarEmoManager avatarEmoManager = this.mEmoManager;
        if (avatarEmoManager != null) {
            avatarEmoManager.release();
        }
        this.mEmoManager = null;
    }

    public void recordForEmoticonVideo(List list) {
        if (this.mHandler != null) {
            initEmoticon();
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 112;
            obtainMessage.obj = list;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void reset() {
        if (this.mHandler != null) {
            setStopRender(true);
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 96;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void run() {
        Looper.prepare();
        this.mHandler = new RenderHandler();
        Log.d(TAG, "prepare render thread: E");
        try {
            this.mEglContextPrepared = false;
            prepare();
            this.mEglContextPrepared = true;
        } catch (Exception e) {
            Log.d(TAG, "FATAL: failed to prepare render thread", (Throwable) e);
            release();
        }
        synchronized (this.mLock) {
            this.mReady = true;
            this.mLock.notify();
        }
        Looper.loop();
        synchronized (this.mLock) {
            this.mReady = false;
            this.mHandler = null;
        }
        Log.d(TAG, "prepare render thread: X");
    }

    public void setConfig(ASAvatarConfigInfo aSAvatarConfigInfo) {
        RenderHandler renderHandler = this.mHandler;
        if (renderHandler != null) {
            Message obtainMessage = renderHandler.obtainMessage();
            obtainMessage.what = 80;
            obtainMessage.obj = aSAvatarConfigInfo;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void setResetStopRender(boolean z) {
        if (this.mIsRendering) {
            this.mRestStopRenderThumbnail = z;
        }
    }

    public void setStopRender(boolean z) {
        if (this.mIsRendering) {
            this.mStopRenderThumbnail = z;
        }
    }

    public void setUpdateHandler(Handler handler) {
        this.mUpdateHandler = handler;
    }

    public void waitUntilReady() {
        synchronized (this.mLock) {
            if (!this.mReady) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("waitUntilReady() interrupted: ");
                    sb.append(e);
                    Log.e(str, sb.toString());
                }
            }
        }
    }
}
