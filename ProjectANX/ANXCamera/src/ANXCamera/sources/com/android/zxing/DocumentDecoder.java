package com.android.zxing;

import android.util.Pair;
import com.android.camera.CameraSettings;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera2.CameraCapabilities;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.DocumentType;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.QuadStatus;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.RotateFlags;
import com.xiaomi.stat.d;
import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.Arrays;

public class DocumentDecoder extends Decoder {
    private static final String TAG = "DocumentDecoder";
    private Pair cachePreview;
    private MainContentProtocol mMainProtocol = ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166));
    private int mSensorOrientation = 90;

    public DocumentDecoder() {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("DocumentDecoder: init cost = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(d.H);
        Log.d(TAG, sb.toString());
        this.mSubjects = PublishSubject.create();
        this.mDecodeDisposable = this.mSubjects.toFlowable(BackpressureStrategy.LATEST).observeOn(Schedulers.computation()).map(new O00000Oo(this)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new O000000o(this));
    }

    private Pair decode(PreviewImage previewImage) {
        QuadStatus quadStatus = QuadStatus.QUAD_NOSHOW;
        float[] fArr = new float[8];
        if (previewImage == null || previewImage.getData() == null || previewImage.getData().length == 0 || previewImage.getWidth() == 0 || previewImage.getHeight() == 0) {
            return Pair.create(quadStatus, fArr);
        }
        RotateFlags rotateFlag = getRotateFlag(this.mSensorOrientation);
        int doScanDocument = DocumentProcess.getInstance().doScanDocument(previewImage.getData(), fArr, previewImage.getWidth(), previewImage.getHeight(), 0, 0, DocumentType.PPT, rotateFlag);
        this.cachePreview = Pair.create(previewImage, fArr);
        float[] rotatePoints = DocumentProcess.getInstance().rotatePoints(fArr, previewImage.getWidth(), previewImage.getHeight(), rotateFlag);
        StringBuilder sb = new StringBuilder();
        sb.append("decode: version = ");
        sb.append(DocumentProcess.getVersion());
        sb.append(", size = ");
        sb.append(previewImage.getWidth());
        sb.append("x");
        sb.append(previewImage.getHeight());
        sb.append(", status = ");
        sb.append(doScanDocument);
        sb.append(", points = ");
        sb.append(Arrays.toString(fArr));
        sb.append(", rotatePoints = ");
        sb.append(Arrays.toString(rotatePoints));
        Log.d(TAG, sb.toString());
        return Pair.create(QuadStatus.values()[doScanDocument], rotatePoints);
    }

    public static RotateFlags getRotateFlag(int i) {
        return i != 0 ? i != 180 ? i != 270 ? RotateFlags.ROTATE_90 : RotateFlags.ROTATE_270 : RotateFlags.ROTATE_180 : RotateFlags.ROTATE_0;
    }

    public /* synthetic */ Pair O000000o(PreviewImage previewImage) {
        StringBuilder sb = new StringBuilder();
        sb.append("decode E: previewImage is null? ");
        sb.append(previewImage == null);
        Log.d(TAG, sb.toString());
        return decode(previewImage);
    }

    public /* synthetic */ void O000000o(Pair pair) {
        this.mMainProtocol.updateDocument(pair);
    }

    public Pair getCachePreview() {
        return this.cachePreview;
    }

    public void init(int i) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i);
        this.mSensorOrientation = capabilities != null ? capabilities.getSensorOrientation() : 90;
    }

    public boolean needPreviewFrame() {
        return CameraSettings.isDocumentMode2On(186) && super.needPreviewFrame();
    }

    public void onPreviewFrame(PreviewImage previewImage) {
        PublishSubject publishSubject = this.mSubjects;
        if (publishSubject != null) {
            publishSubject.onNext(previewImage);
        }
    }

    public void quit() {
        super.quit();
        Disposable disposable = this.mDecodeDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDecodeDisposable.dispose();
        }
    }

    public void reset() {
    }

    public void startDecode() {
        this.mDecoding = true;
    }

    public void stopDecode() {
        this.mDecoding = false;
    }
}
