package com.android.camera.resource;

import com.android.camera.log.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleNetworkDownloadRequest extends BaseObservableRequest {
    protected static final OkHttpClient CLIENT = new Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
    protected static final String TAG = "DownloadRequest";
    private String downloadUrl;
    /* access modifiers changed from: private */
    public ObservableEmitter mProgressEmitter;
    private Observable mProgressObservable;
    /* access modifiers changed from: private */
    public String outputPath;
    /* access modifiers changed from: private */
    public Call responseCall;

    public SimpleNetworkDownloadRequest(String str, String str2) {
        this.downloadUrl = str;
        this.outputPath = str2;
    }

    public Observable observableProgress(int i) {
        this.mProgressObservable = Observable.create(new ObservableOnSubscribe() {
            public void subscribe(ObservableEmitter observableEmitter) {
                SimpleNetworkDownloadRequest.this.mProgressEmitter = observableEmitter;
            }
        }).sample((long) i, TimeUnit.MILLISECONDS);
        return this.mProgressObservable;
    }

    /* access modifiers changed from: protected */
    public void onDispose() {
        Call call = this.responseCall;
        if (call != null && !call.isCanceled()) {
            this.responseCall.cancel();
        }
    }

    /* access modifiers changed from: protected */
    public void scheduleRequest(final ResponseListener responseListener, final Object obj) {
        this.responseCall = CLIENT.newCall(new Request.Builder().get().url(this.downloadUrl).build());
        this.responseCall.enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                StringBuilder sb = new StringBuilder();
                sb.append("download async failed with exception ");
                sb.append(iOException.getMessage());
                Log.e(SimpleNetworkDownloadRequest.TAG, sb.toString());
                ResponseListener responseListener = responseListener;
                if (responseListener != null) {
                    responseListener.onResponseError(0, iOException.getMessage(), iOException);
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:49:0x00f2 A[Catch:{ all -> 0x0129 }] */
            /* JADX WARNING: Removed duplicated region for block: B:52:0x00f9 A[Catch:{ all -> 0x0129 }] */
            /* JADX WARNING: Removed duplicated region for block: B:55:0x010b A[Catch:{ all -> 0x0129 }] */
            /* JADX WARNING: Removed duplicated region for block: B:57:0x0116 A[SYNTHETIC, Splitter:B:57:0x0116] */
            /* JADX WARNING: Removed duplicated region for block: B:62:0x0120 A[SYNTHETIC, Splitter:B:62:0x0120] */
            /* JADX WARNING: Removed duplicated region for block: B:69:0x012d A[SYNTHETIC, Splitter:B:69:0x012d] */
            /* JADX WARNING: Removed duplicated region for block: B:74:0x0137 A[SYNTHETIC, Splitter:B:74:0x0137] */
            /* JADX WARNING: Removed duplicated region for block: B:86:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onResponse(Call call, Response response) {
                BufferedOutputStream bufferedOutputStream;
                InputStream inputStream;
                File file;
                String str = SimpleNetworkDownloadRequest.TAG;
                Log.d(str, "onResponse");
                InputStream inputStream2 = null;
                try {
                    inputStream = response.body().byteStream();
                    try {
                        byte[] bArr = new byte[8192];
                        long contentLength = response.body().contentLength();
                        long j = 0;
                        File parentFile = new File(SimpleNetworkDownloadRequest.this.outputPath).getParentFile();
                        if (parentFile != null && !parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(SimpleNetworkDownloadRequest.this.outputPath));
                        while (true) {
                            try {
                                int read = inputStream.read(bArr, 0, 8192);
                                if (read == -1) {
                                    break;
                                }
                                bufferedOutputStream.write(bArr, 0, read);
                                j += (long) read;
                                if (responseListener != null) {
                                    responseListener.onResponseProgress(j, contentLength);
                                }
                                if (SimpleNetworkDownloadRequest.this.mProgressEmitter != null) {
                                    SimpleNetworkDownloadRequest.this.mProgressEmitter.onNext(Long.valueOf(j));
                                }
                            } catch (IOException e) {
                                e = e;
                                inputStream2 = inputStream;
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("download async failed with exception ");
                                    sb.append(e.getMessage());
                                    Log.e(str, sb.toString());
                                    file = new File(SimpleNetworkDownloadRequest.this.outputPath);
                                    if (file.exists()) {
                                    }
                                    if (responseListener != null) {
                                    }
                                    if (SimpleNetworkDownloadRequest.this.mProgressEmitter != null) {
                                    }
                                    if (inputStream2 != null) {
                                    }
                                    if (bufferedOutputStream != null) {
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    inputStream = inputStream2;
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                    if (bufferedOutputStream != null) {
                                        try {
                                            bufferedOutputStream.close();
                                        } catch (Exception e3) {
                                            e3.printStackTrace();
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (inputStream != null) {
                                }
                                if (bufferedOutputStream != null) {
                                }
                                throw th;
                            }
                        }
                        if (SimpleNetworkDownloadRequest.this.mProgressEmitter != null) {
                            SimpleNetworkDownloadRequest.this.mProgressEmitter.onNext(Long.valueOf(contentLength));
                            SimpleNetworkDownloadRequest.this.mProgressEmitter.onComplete();
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e5) {
                            e5.printStackTrace();
                        }
                        if (responseListener != null) {
                            SimpleNetworkDownloadRequest.this.responseCall = null;
                            responseListener.onResponse(obj, false);
                        }
                    } catch (IOException e6) {
                        e = e6;
                        bufferedOutputStream = null;
                        inputStream2 = inputStream;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("download async failed with exception ");
                        sb2.append(e.getMessage());
                        Log.e(str, sb2.toString());
                        file = new File(SimpleNetworkDownloadRequest.this.outputPath);
                        if (file.exists()) {
                        }
                        if (responseListener != null) {
                        }
                        if (SimpleNetworkDownloadRequest.this.mProgressEmitter != null) {
                        }
                        if (inputStream2 != null) {
                        }
                        if (bufferedOutputStream != null) {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedOutputStream = null;
                        if (inputStream != null) {
                        }
                        if (bufferedOutputStream != null) {
                        }
                        throw th;
                    }
                } catch (IOException e7) {
                    e = e7;
                    bufferedOutputStream = null;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("download async failed with exception ");
                    sb22.append(e.getMessage());
                    Log.e(str, sb22.toString());
                    file = new File(SimpleNetworkDownloadRequest.this.outputPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    if (responseListener != null) {
                        responseListener.onResponseError(3, e.getMessage(), response);
                    }
                    if (SimpleNetworkDownloadRequest.this.mProgressEmitter != null) {
                        SimpleNetworkDownloadRequest.this.mProgressEmitter.onComplete();
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Exception e8) {
                            e8.printStackTrace();
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e9) {
                            e9.printStackTrace();
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    inputStream = null;
                    bufferedOutputStream = null;
                    if (inputStream != null) {
                    }
                    if (bufferedOutputStream != null) {
                    }
                    throw th;
                }
            }
        });
    }
}
