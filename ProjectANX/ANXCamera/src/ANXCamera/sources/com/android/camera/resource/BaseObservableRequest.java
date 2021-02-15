package com.android.camera.resource;

import android.content.Context;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class BaseObservableRequest implements ResponseListener {
    protected ObservableEmitter mEmitter;
    private Observable mOutPutObservable;

    public /* synthetic */ void O000000o(Class cls, ObservableEmitter observableEmitter) {
        this.mEmitter = observableEmitter;
        scheduleRequest(this, create(cls));
    }

    public /* synthetic */ void O000000o(Object obj, ObservableEmitter observableEmitter) {
        this.mEmitter = observableEmitter;
        scheduleRequest(this, obj);
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v5
  assigns: []
  uses: []
  mth insns count: 16
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
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @NonNull
    public final Object create(@NonNull Class cls) {
        ? r2;
        StringBuilder sb;
        ? r22;
        ? r23;
        ? r24 = "Cannot create an instance of ";
        String str = "newInstanceError";
        try {
            r23 = r24;
            r22 = cls.newInstance();
            r23 = r22;
            return r22;
        } catch (InstantiationException unused) {
            sb = new StringBuilder();
            r2 = r22;
            sb.append(r2);
            sb.append(cls);
            Log.e(str, sb.toString());
            return null;
        } catch (IllegalAccessException unused2) {
            sb = new StringBuilder();
            r2 = r23;
            sb.append(r2);
            sb.append(cls);
            Log.e(str, sb.toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Scheduler getWorkThread() {
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: onDispose */
    public void O00ooo0() {
    }

    public void onResponse(Object obj, boolean z) {
        ObservableEmitter observableEmitter = this.mEmitter;
        if (observableEmitter != null) {
            if (!z) {
                observableEmitter.onNext(obj);
            }
            this.mEmitter.onComplete();
        }
    }

    public void onResponseError(int i, String str, Object obj) {
        if (!this.mEmitter.isDisposed()) {
            this.mEmitter.onError(new BaseRequestException(i, str));
        }
    }

    public void onResponseProgress(long j, long j2) {
    }

    public abstract void scheduleRequest(ResponseListener responseListener, Object obj);

    public Observable startObservable(@NonNull Class cls) {
        this.mOutPutObservable = Observable.create(new O00000o0(this, cls));
        return this.mOutPutObservable;
    }

    public Observable startObservable(@NonNull Object obj) {
        if (obj != null) {
            this.mOutPutObservable = Observable.create(new O00000Oo(this, obj)).doOnDispose(new O000000o(this));
            return this.mOutPutObservable;
        }
        throw new IllegalArgumentException("no null!");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0034 A[SYNTHETIC, Splitter:B:21:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x003e A[SYNTHETIC, Splitter:B:27:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0049 A[SYNTHETIC, Splitter:B:32:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:24:0x0039=Splitter:B:24:0x0039, B:18:0x002f=Splitter:B:18:0x002f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void writeToCache(String str, Context context, String str2) {
        File file = new File(context.getCacheDir(), str);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            try {
                fileOutputStream2.write(str2.getBytes());
                try {
                    fileOutputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                e.printStackTrace();
                if (fileOutputStream == null) {
                    fileOutputStream.close();
                }
            } catch (IOException e3) {
                e = e3;
                fileOutputStream = fileOutputStream2;
                try {
                    e.printStackTrace();
                    if (fileOutputStream == null) {
                        fileOutputStream.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (fileOutputStream == null) {
            }
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (fileOutputStream == null) {
            }
        }
    }
}
