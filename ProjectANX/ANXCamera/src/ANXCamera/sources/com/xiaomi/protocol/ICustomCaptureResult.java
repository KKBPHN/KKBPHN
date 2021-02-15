package com.xiaomi.protocol;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ICustomCaptureResult implements Parcelable {
    private static final String CAPTURE_RESULT_EXTRA_CLASS = "android.hardware.camera2.impl.CaptureResultExtras";
    public static final Creator CREATOR = new Creator() {
        public ICustomCaptureResult createFromParcel(Parcel parcel) {
            return new ICustomCaptureResult(parcel);
        }

        public ICustomCaptureResult[] newArray(int i) {
            return new ICustomCaptureResult[i];
        }
    };
    private static final String PHYSICAL_CAPTURE_RESULT_CLASS = "android.hardware.camera2.impl.PhysicalCaptureResultInfo";
    private static final String TAG = "ICustomCaptureResult";
    private long mFrameNumber;
    private Parcelable mMainPhysicalResult;
    private Parcelable mParcelRequest;
    private CaptureRequest mRequest;
    private Parcelable mResults;
    private int mSequenceId;
    private int mSessionId;
    private Parcelable mSubPhysicalResult;
    private long mTimestamp;

    public ICustomCaptureResult() {
    }

    public ICustomCaptureResult(int i, int i2, long j, Parcelable parcelable, CaptureRequest captureRequest) {
        this.mSessionId = i;
        this.mSequenceId = i2;
        this.mFrameNumber = j;
        this.mResults = parcelable;
        this.mRequest = captureRequest;
    }

    public ICustomCaptureResult(int i, int i2, long j, Parcelable parcelable, Parcelable parcelable2) {
        this.mSessionId = i;
        this.mSequenceId = i2;
        this.mFrameNumber = j;
        this.mResults = parcelable;
        this.mParcelRequest = parcelable2;
    }

    protected ICustomCaptureResult(Parcel parcel) {
        this.mSessionId = parcel.readInt();
        this.mSequenceId = parcel.readInt();
        this.mFrameNumber = parcel.readLong();
        this.mTimestamp = parcel.readLong();
        this.mResults = parcel.readParcelable(Parcelable.class.getClassLoader());
        this.mRequest = (CaptureRequest) parcel.readParcelable(Parcelable.class.getClassLoader());
    }

    public static Object getCameraMetaDataCopy(Object obj) {
        try {
            Class cls = Class.forName("android.hardware.camera2.impl.CameraMetadataNative");
            return cls.getDeclaredConstructor(new Class[]{cls}).newInstance(new Object[]{obj});
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "getCameraMetaDataCopy: failed", e);
            return null;
        }
    }

    public static TotalCaptureResult toTotalCaptureResult(ICustomCaptureResult iCustomCaptureResult, int i) {
        return toTotalCaptureResult(iCustomCaptureResult, i, false);
    }

    /* JADX WARNING: type inference failed for: r0v5, types: [java.lang.Class] */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r4v3, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r0v17, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r0v18, types: [java.lang.Class] */
    /* JADX WARNING: type inference failed for: r0v20, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r0v21 */
    /* JADX WARNING: type inference failed for: r0v22 */
    /* JADX WARNING: type inference failed for: r0v23 */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:8|9|10|11|12) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x00c6 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v21
  assigns: [java.lang.Object]
  uses: [java.lang.Class, ?[OBJECT, ARRAY]]
  mth insns count: 296
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x020c A[Catch:{ Exception -> 0x0264 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x021a A[Catch:{ Exception -> 0x0264 }] */
    /* JADX WARNING: Unknown variable types count: 6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static TotalCaptureResult toTotalCaptureResult(ICustomCaptureResult iCustomCaptureResult, int i, boolean z) {
        Constructor constructor;
        ? r0;
        Constructor constructor2;
        Parcelable parcelable;
        Object cameraMetaDataCopy;
        Object[] objArr;
        String str = "|";
        try {
            int sequenceId = iCustomCaptureResult.getSequenceId();
            long frameNumber = iCustomCaptureResult.getFrameNumber();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("toTotalCaptureResult: ");
            sb.append(i);
            sb.append(str);
            sb.append(sequenceId);
            sb.append(str);
            sb.append(frameNumber);
            Log.d(str2, sb.toString());
            ? cls = Class.forName(CAPTURE_RESULT_EXTRA_CLASS);
            if (VERSION.SDK_INT >= 30) {
                ? newInstance = cls;
                constructor = cls.getDeclaredConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE, String.class, Long.TYPE, Long.TYPE, Long.TYPE});
                ? newInstance2 = constructor.newInstance(new Object[]{Integer.valueOf(sequenceId), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Long.valueOf(frameNumber), Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(-1), Integer.valueOf(-1), Integer.valueOf(-1)});
                r0 = newInstance2;
                Log.d(TAG, "could not reflect constructor with more params, try anthor");
                constructor = newInstance.getDeclaredConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE, String.class});
                objArr = new Object[]{Integer.valueOf(sequenceId), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Long.valueOf(frameNumber), Integer.valueOf(0), Integer.valueOf(0), null};
            } else if (VERSION.SDK_INT >= 29) {
                constructor = cls.getDeclaredConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE, String.class});
                objArr = new Object[]{Integer.valueOf(sequenceId), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Long.valueOf(frameNumber), Integer.valueOf(0), Integer.valueOf(0), null};
            } else {
                constructor = cls.getDeclaredConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE});
                objArr = new Object[]{Integer.valueOf(sequenceId), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Long.valueOf(frameNumber), Integer.valueOf(0), Integer.valueOf(0)};
            }
            r0 = constructor.newInstance(objArr);
            Constructor[] declaredConstructors = TotalCaptureResult.class.getDeclaredConstructors();
            int length = declaredConstructors.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    constructor2 = constructor;
                    break;
                }
                constructor2 = declaredConstructors[i2];
                if (constructor2.getParameters().length > 2) {
                    break;
                }
                i2++;
            }
            if (z) {
                Log.d(TAG, "prefer physical meta");
                parcelable = iCustomCaptureResult.getMainPhysicalResult();
                if (parcelable == null) {
                    Log.d(TAG, "no physical meta, use logical");
                }
                cameraMetaDataCopy = getCameraMetaDataCopy(parcelable);
                if (cameraMetaDataCopy != null) {
                    Log.e(TAG, "null native metadata", new RuntimeException());
                    return null;
                } else if (VERSION.SDK_INT < 28) {
                    return (TotalCaptureResult) constructor2.newInstance(new Object[]{cameraMetaDataCopy, iCustomCaptureResult.getRequest(), r0, 0, Integer.valueOf(i)});
                } else {
                    return (TotalCaptureResult) constructor2.newInstance(new Object[]{cameraMetaDataCopy, iCustomCaptureResult.getRequest(), r0, 0, Integer.valueOf(i), Array.newInstance(Class.forName(PHYSICAL_CAPTURE_RESULT_CLASS), 0)});
                }
            }
            parcelable = iCustomCaptureResult.getResults();
            cameraMetaDataCopy = getCameraMetaDataCopy(parcelable);
            if (cameraMetaDataCopy != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "null capture result!", new RuntimeException());
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public long getFrameNumber() {
        return this.mFrameNumber;
    }

    public Parcelable getMainPhysicalResult() {
        return this.mMainPhysicalResult;
    }

    public Parcelable getParcelRequest() {
        return this.mParcelRequest;
    }

    public CaptureRequest getRequest() {
        return this.mRequest;
    }

    public Parcelable getResults() {
        return this.mResults;
    }

    public int getSequenceId() {
        return this.mSequenceId;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public Parcelable getSubPhysicalResult() {
        return this.mSubPhysicalResult;
    }

    public long getTimeStamp() {
        return this.mTimestamp;
    }

    public void setFrameNumber(long j) {
        this.mFrameNumber = j;
    }

    public void setMainPhysicalResult(Parcelable parcelable) {
        this.mMainPhysicalResult = parcelable;
    }

    public void setParcelRequest(Parcelable parcelable) {
        this.mParcelRequest = parcelable;
    }

    public void setRequest(CaptureRequest captureRequest) {
        this.mRequest = captureRequest;
    }

    public void setResults(Parcelable parcelable) {
        this.mResults = parcelable;
    }

    public void setSequenceId(int i) {
        this.mSequenceId = i;
    }

    public void setSessionId(int i) {
        this.mSessionId = i;
    }

    public void setSubPhysicalResult(Parcelable parcelable) {
        this.mSubPhysicalResult = parcelable;
    }

    public void setTimeStamp(long j) {
        this.mTimestamp = j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ICustomCaptureResult{mSessionId=");
        sb.append(this.mSessionId);
        sb.append(", mSequenceId=");
        sb.append(this.mSequenceId);
        sb.append(", mFrameNumber=");
        sb.append(this.mFrameNumber);
        sb.append(", mResults=");
        sb.append(this.mResults);
        sb.append(", mRequest=");
        sb.append(this.mRequest);
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSessionId);
        parcel.writeInt(this.mSequenceId);
        parcel.writeLong(this.mFrameNumber);
        parcel.writeLong(this.mTimestamp);
        parcel.writeParcelable(this.mResults, i);
        parcel.writeParcelable(this.mRequest, i);
    }
}
