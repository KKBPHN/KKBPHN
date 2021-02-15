package com.android.camera.module.loader.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Intent;
import androidx.annotation.WorkerThread;
import androidx.core.util.Pair;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.cloud.DataCloudItemFeature;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.db.DbRepository;
import com.android.camera.external.NotifyExternalManager;
import com.android.camera.log.Log;
import com.android.camera.module.Module;
import com.android.camera.module.loader.StartControl;
import com.android.camera.resource.conf.ConfCacheRequest;
import com.android.camera.resource.conf.ConfSettingRequest;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CompletablePreFixCamera2Setup implements CompletableOnSubscribe, Observer {
    private static final String TAG = "PreFixCamera2Setup";
    private boolean isFromVoiceControl;
    private CameraScreenNail mCameraScreenNail;
    private int mCurrentActivityHashCode;
    private CompletableEmitter mEmitter;
    private boolean mFromScreenSlide;
    private Intent mIntent;
    private Module mLastMode;
    private boolean mModuleChanged;
    private boolean mNeedBlur;
    private boolean mStartFromKeyguard;

    public CompletablePreFixCamera2Setup(Module module, StartControl startControl, CameraScreenNail cameraScreenNail, Intent intent, int i, boolean z, boolean z2) {
        this.mLastMode = module;
        this.mCurrentActivityHashCode = i;
        if (startControl != null) {
            this.mNeedBlur = startControl.mNeedBlurAnimation;
            this.mFromScreenSlide = startControl.mFromScreenSlide;
        }
        boolean z3 = startControl == null || module == null || (startControl.mTargetMode != module.getModuleIndex() && !isSameModule(startControl.mTargetMode, module.getModuleIndex()));
        this.mModuleChanged = z3;
        this.mCameraScreenNail = cameraScreenNail;
        this.mIntent = intent;
        this.mStartFromKeyguard = z;
        this.isFromVoiceControl = z2;
    }

    static /* synthetic */ void O000000o(DataCloudItemFeature dataCloudItemFeature) {
        StringBuilder sb = new StringBuilder();
        sb.append(dataCloudItemFeature.getVersion());
        sb.append("");
        Log.d("loadSettings:", sb.toString());
    }

    private void closeLastModule() {
        Module module = this.mLastMode;
        if (module != null) {
            module.unRegisterProtocol();
            this.mLastMode.onPause();
            this.mLastMode.onStop();
            this.mLastMode.onDestroy();
        }
    }

    private boolean isLastModuleAlive() {
        Module module = this.mLastMode;
        return module != null && module.isCreated();
    }

    private boolean isSameModule(int i, int i2) {
        return (i == 163 || i == 165) && (i2 == 163 || i2 == 165);
    }

    private void loadSettings() {
        DataCloudItemFeature dataItemCloud = DataRepository.dataItemCloud();
        Observable.concat((ObservableSource) new ConfCacheRequest().startObservable((Object) dataItemCloud), (ObservableSource) new ConfSettingRequest().startObservable((Object) dataItemCloud)).firstElement().subscribe(C0410O00000oO.INSTANCE, C0411O00000oo.INSTANCE);
    }

    public void onComplete() {
    }

    public void onError(Throwable th) {
    }

    public void onNext(Camera2Result camera2Result) {
        this.mEmitter.onComplete();
    }

    public void onSubscribe(Disposable disposable) {
    }

    @WorkerThread
    public void subscribe(CompletableEmitter completableEmitter) {
        int i;
        int i2;
        this.mEmitter = completableEmitter;
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        StringBuilder sb = new StringBuilder();
        sb.append("mModuleChanged ");
        sb.append(this.mModuleChanged);
        sb.append(" LastMode is ");
        Module module = this.mLastMode;
        sb.append(module == null ? "null" : Integer.valueOf(module.getModuleIndex()));
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mNeedBlur) {
            this.mCameraScreenNail.animateModuleCopyTexture(this.mFromScreenSlide);
        }
        if (this.mModuleChanged) {
            Module module2 = this.mLastMode;
            if (module2 != null) {
                module2.unRegisterModulePersistProtocol();
            }
            if (this.mStartFromKeyguard || dataItemGlobal.getIntentType() == 1 || dataItemGlobal.getIntentType() == 2) {
                CameraSettings.resetRetainZoom();
            }
        }
        if (isLastModuleAlive()) {
            closeLastModule();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("subscribe: mIntent = ");
        sb3.append(this.mIntent);
        Log.d(str, sb3.toString());
        Intent intent = this.mIntent;
        if (intent != null) {
            Pair parseIntent = dataItemGlobal.parseIntent(intent, Boolean.valueOf(this.isFromVoiceControl), this.mStartFromKeyguard, true, true);
            i2 = ((Integer) parseIntent.first).intValue();
            i = ((Integer) parseIntent.second).intValue();
            loadSettings();
            if (C0122O00000o.instance().OOo000O()) {
                DbRepository.dbItemSaveTask().markAllDepartedTask();
            }
        } else {
            int currentCameraId = dataItemGlobal.getCurrentCameraId();
            int currentMode = dataItemGlobal.getCurrentMode();
            i2 = currentCameraId;
            i = currentMode;
        }
        NotifyExternalManager.getInstance(CameraAppImpl.getAndroidContext()).notifyModeAndFacing(i, i2);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("subscribe: request to open ");
        sb4.append(i2);
        Log.d(str, sb4.toString());
        Camera2OpenManager.getInstance().openCamera(i2, i, this, C0122O00000o.instance().OO0OO0o());
    }
}
