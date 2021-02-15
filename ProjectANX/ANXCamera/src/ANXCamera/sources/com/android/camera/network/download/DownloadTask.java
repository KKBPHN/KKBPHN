package com.android.camera.network.download;

import android.os.AsyncTask;
import com.android.camera.log.Log;
import com.android.camera.network.net.base.HTTP;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

class DownloadTask {
    private static final int BUFFER_SIZE = 8192;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;
    private static final long RETRY_INTERVAL_MILLI = TimeUnit.SECONDS.toMillis(10);
    private static final String TAG = "DownloadTask";
    private CoreTask mCoreTask;
    /* access modifiers changed from: private */
    public OnCompleteListener mOnCompleteListener;
    /* access modifiers changed from: private */
    public OnProgressListener mOnProgressListener;
    private TaskInfo mTaskInfo;

    class CoreTask extends AsyncTask {
        /* access modifiers changed from: private */
        public Request mRequest;

        CoreTask(Request request) {
            this.mRequest = request;
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Void... voidArr) {
            return Integer.valueOf(DownloadTask.this.performRequest(this.mRequest));
        }

        /* access modifiers changed from: protected */
        public void onCancelled(Integer num) {
            Log.d(DownloadTask.TAG, String.format("process download finish %d", new Object[]{Integer.valueOf(num != null ? num.intValue() : -2)}));
            if (DownloadTask.this.mOnCompleteListener != null) {
                DownloadTask.this.mOnCompleteListener.onRequestComplete(this.mRequest, 5);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            Log.d(DownloadTask.TAG, String.format("process download finish %d", new Object[]{num}));
            if (DownloadTask.this.mOnCompleteListener != null) {
                DownloadTask.this.mOnCompleteListener.onRequestComplete(this.mRequest, num.intValue());
            }
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... numArr) {
            if (DownloadTask.this.mOnProgressListener != null) {
                DownloadTask.this.mOnProgressListener.onProgressUpdate(this.mRequest, numArr[0].intValue());
            }
        }

        /* access modifiers changed from: 0000 */
        public void publishProgress(int i) {
            super.publishProgress(new Integer[]{Integer.valueOf(i)});
        }
    }

    interface InternalConstants {
        public static final int RESULT_HTTP_TIMEOUT = 12;
        public static final int RESULT_NONE_RESULT = -2;
    }

    interface OnCompleteListener {
        void onRequestComplete(Request request, int i);
    }

    interface OnProgressListener {
        void onProgressUpdate(Request request, int i);
    }

    interface OpenConstants {
        public static final int RESULT_CANCELLED = 5;
        public static final int RESULT_DOWNLOAD_FILE_ERROR = 9;
        public static final int RESULT_HTTP_ERROR = 7;
        public static final int RESULT_HTTP_SERVER_ERROR = 8;
        public static final int RESULT_ILLEGAL_ACTIVE_NETWORK = 3;
        public static final int RESULT_NETWORK_DENIED = 2;
        public static final int RESULT_NO_NETWORK = 1;
        public static final int RESULT_OPEN_FILE_FAILED = 4;
        public static final int RESULT_SUCCESSFUL = 0;
        public static final int RESULT_UNKNOWN = 11;
        public static final int RESULT_VALIDATION_FAILED = 6;
    }

    class TaskInfo {
        long mContentLength;
        MessageDigest mDigest;
        long mDownloadSize;

        private TaskInfo() {
        }
    }

    DownloadTask(Request request) {
        this.mCoreTask = new CoreTask(request);
    }

    DownloadTask(Request request, OnCompleteListener onCompleteListener) {
        this.mCoreTask = new CoreTask(request);
        this.mOnCompleteListener = onCompleteListener;
    }

    private void configure(HttpURLConnection httpURLConnection) {
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setRequestProperty(HttpRequest.HEADER_ACCEPT_ENCODING, HTTP.IDENTITY_CODING);
    }

    private static File getTempFile(File file) {
        String parent = file.getParent();
        StringBuilder sb = new StringBuilder();
        sb.append(file.getName());
        sb.append(".download");
        return new File(parent, sb.toString());
    }

    private static boolean isRetryState(int i) {
        return i == 12 || i == 11;
    }

    private static OutputStream openOutputStream(File file) {
        Object[] objArr;
        String str;
        File parentFile = file.getParentFile();
        boolean exists = parentFile.exists();
        String str2 = TAG;
        if (exists || parentFile.mkdirs()) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    objArr = new Object[0];
                    str = "output file is a directory";
                } else {
                    Log.w(str2, String.format("output file will be overwritten", new Object[0]));
                }
            }
            File tempFile = getTempFile(file);
            if (tempFile.exists()) {
                Log.w(str2, String.format("temp file exists, try delete", new Object[0]));
                if (!tempFile.delete()) {
                    Log.w(str2, String.format("temp file delete failed, will overwrite", new Object[0]));
                }
            }
            try {
                return new FileOutputStream(tempFile);
            } catch (FileNotFoundException e) {
                Log.w(str2, (Throwable) e);
                return null;
            }
        } else {
            objArr = new Object[0];
            str = "create folder failed";
        }
        Log.d(str2, String.format(str, objArr));
        return null;
    }

    private void performProgressUpdate(byte[] bArr, int i) {
        TaskInfo taskInfo = this.mTaskInfo;
        long j = taskInfo.mDownloadSize;
        taskInfo.mDownloadSize = ((long) i) + j;
        MessageDigest messageDigest = taskInfo.mDigest;
        if (messageDigest != null) {
            messageDigest.update(bArr, 0, i);
        }
        if (this.mOnProgressListener != null) {
            TaskInfo taskInfo2 = this.mTaskInfo;
            long j2 = taskInfo2.mContentLength;
            if (j2 > 0) {
                int i2 = (int) ((((double) taskInfo2.mDownloadSize) / ((double) j2)) * 100.0d);
                if (((int) ((((double) j) / ((double) j2)) * 100.0d)) != i2) {
                    this.mCoreTask.publishProgress(i2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public int performRequest(Request request) {
        int process;
        String format = String.format("start to download request[%s, %s, %s]", new Object[]{request.getTag(), request.getUri(), request.getDestination()});
        String str = TAG;
        Log.d(str, format);
        preRequest();
        int maxRetryTimes = request.getMaxRetryTimes();
        int i = 0;
        do {
            process = process(request);
            if (!isRetryState(process)) {
                break;
            }
            Log.d(str, String.format("retry for %d", new Object[]{Integer.valueOf(process)}));
            try {
                Thread.sleep(RETRY_INTERVAL_MILLI, 0);
                i++;
            } catch (InterruptedException unused) {
                process = 5;
            }
        } while (i <= maxRetryTimes);
        return postRequest(process);
    }

    private int postDownload() {
        Verifier verifier = this.mCoreTask.mRequest.getVerifier();
        String str = TAG;
        if (verifier == null || this.mCoreTask.mRequest.getVerifier().verify(this.mTaskInfo.mDigest.digest())) {
            Log.d(str, String.format("verify success", new Object[0]));
            return 0;
        }
        Log.d(str, String.format("verify fail", new Object[0]));
        return 6;
    }

    private int postRequest(int i) {
        Object[] objArr;
        String str;
        String str2 = TAG;
        File destination = this.mCoreTask.mRequest.getDestination();
        if (i != 0) {
            File tempFile = getTempFile(destination);
            if (tempFile.exists() && !tempFile.delete()) {
                Log.d(str2, String.format("delete tmp file failed %s", new Object[]{tempFile}));
            }
        } else {
            File tempFile2 = getTempFile(destination);
            if (!tempFile2.exists()) {
                objArr = new Object[0];
                str = "downloaded file missing";
            } else if (!tempFile2.renameTo(destination)) {
                objArr = new Object[0];
                str = "downloaded file rename failed";
            } else {
                Log.w(str2, String.format("rename tmp file success", new Object[0]));
            }
            Log.w(str2, String.format(str, objArr));
            return 9;
        }
        return i;
    }

    private void preDownload(Request request) {
        Verifier verifier = request.getVerifier();
        if (verifier != null) {
            Log.d(TAG, String.format("need verify, try to get MessageDigest", new Object[0]));
            this.mTaskInfo.mDigest = verifier.getInstance();
        }
    }

    private void preRequest() {
        this.mTaskInfo = new TaskInfo();
    }

    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r4v0, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r9v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r0v4, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v5, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v8
  assigns: [?[OBJECT, ARRAY], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.io.OutputStream]
  uses: [?[OBJECT, ARRAY], ?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.InputStream, java.io.OutputStream]
  mth insns count: 139
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0107 A[SYNTHETIC, Splitter:B:76:0x0107] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0111 A[SYNTHETIC, Splitter:B:81:0x0111] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0122 A[SYNTHETIC, Splitter:B:90:0x0122] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x012c A[SYNTHETIC, Splitter:B:95:0x012c] */
    /* JADX WARNING: Unknown variable types count: 6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int process(Request request) {
        ? r4;
        ? r9;
        ? r0;
        ? r02;
        Holder open = ConnectionHelper.open(request.getUri(), request.getNetworkType());
        HttpURLConnection httpURLConnection = (HttpURLConnection) open.value;
        String str = TAG;
        if (httpURLConnection == null) {
            Log.d(str, String.format("open connection failed", new Object[0]));
            return translateErrorCode(open.reason);
        }
        ? r03 = 0;
        try {
            configure(httpURLConnection);
            httpURLConnection.connect();
            int translateResponseCode = translateResponseCode(httpURLConnection.getResponseCode());
            if (translateResponseCode != 0) {
                Log.d(str, String.format("response code not valid", new Object[0]));
                httpURLConnection.disconnect();
                return translateResponseCode;
            }
            processHeader(httpURLConnection);
            ? inputStream = httpURLConnection.getInputStream();
            try {
                r02 = r03;
                r02 = openOutputStream(request.getDestination());
                if (r02 == 0) {
                    Log.d(str, String.format("open output stream failed", new Object[0]));
                    httpURLConnection.disconnect();
                    if (inputStream != 0) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.w(str, (Throwable) e);
                        }
                    }
                    if (r02 != 0) {
                        try {
                            r02.close();
                        } catch (IOException e2) {
                            Log.w(str, (Throwable) e2);
                        }
                    }
                    return 4;
                }
                r02 = r02;
                preDownload(request);
                Log.d(str, String.format("start to transfer data", new Object[0]));
                byte[] bArr = new byte[8192];
                int i = 0;
                while (!this.mCoreTask.isCancelled()) {
                    i = inputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    r02.write(bArr, 0, i);
                    performProgressUpdate(bArr, i);
                }
                if (i == -1) {
                    Log.d(str, String.format("download success", new Object[0]));
                    int postDownload = postDownload();
                    httpURLConnection.disconnect();
                    if (inputStream != 0) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                            Log.w(str, (Throwable) e3);
                        }
                    }
                    if (r02 != 0) {
                        try {
                            r02.close();
                        } catch (IOException e4) {
                            Log.w(str, (Throwable) e4);
                        }
                    }
                    return postDownload;
                }
                Log.d(str, String.format("cancelled, during download", new Object[0]));
                httpURLConnection.disconnect();
                if (inputStream != 0) {
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        Log.w(str, (Throwable) e5);
                    }
                }
                if (r02 != 0) {
                    try {
                        r02.close();
                    } catch (IOException e6) {
                        Log.w(str, (Throwable) e6);
                    }
                }
                return 5;
            } catch (IOException e7) {
                e = e7;
                r9 = r03;
                r0 = inputStream;
                try {
                    Log.w(str, (Throwable) e);
                    httpURLConnection.disconnect();
                    if (r0 != 0) {
                        try {
                            r0.close();
                        } catch (IOException e8) {
                            Log.w(str, (Throwable) e8);
                        }
                    }
                    if (r9 != 0) {
                        try {
                            r9.close();
                        } catch (IOException e9) {
                            Log.w(str, (Throwable) e9);
                        }
                    }
                    return 11;
                } catch (Throwable th) {
                    th = th;
                    r4 = r0;
                    r03 = r9;
                    httpURLConnection.disconnect();
                    if (r4 != 0) {
                    }
                    if (r03 != 0) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r4 = inputStream;
                r03 = r02;
                httpURLConnection.disconnect();
                if (r4 != 0) {
                    try {
                        r4.close();
                    } catch (IOException e10) {
                        Log.w(str, (Throwable) e10);
                    }
                }
                if (r03 != 0) {
                    try {
                        r03.close();
                    } catch (IOException e11) {
                        Log.w(str, (Throwable) e11);
                    }
                }
                throw th;
            }
        } catch (IOException e12) {
            e = e12;
            r9 = 0;
            Log.w(str, (Throwable) e);
            httpURLConnection.disconnect();
            if (r0 != 0) {
            }
            if (r9 != 0) {
            }
            return 11;
        } catch (Throwable th3) {
            th = th3;
            r4 = 0;
            httpURLConnection.disconnect();
            if (r4 != 0) {
            }
            if (r03 != 0) {
            }
            throw th;
        }
    }

    private void processHeader(HttpURLConnection httpURLConnection) {
        this.mTaskInfo.mContentLength = (long) httpURLConnection.getContentLength();
        Log.d(TAG, String.format("content length: %d", new Object[]{Long.valueOf(this.mTaskInfo.mContentLength)}));
    }

    private static int translateErrorCode(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    return 11;
                }
            }
        }
        return i2;
    }

    private static int translateResponseCode(int i) {
        String str = TAG;
        if (i != 200) {
            Log.d(str, String.format("processing http code %d", new Object[]{Integer.valueOf(i)}));
            int i2 = i / 100;
            if (i2 == 3) {
                return 7;
            }
            if (i2 == 4) {
                return i == 408 ? 12 : 7;
            }
            if (i2 == 5) {
                return i == 504 ? 12 : 8;
            }
            return 7;
        }
        Log.d(str, String.format("http status is ok", new Object[0]));
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean cancel(boolean z) {
        return this.mCoreTask.cancel(z);
    }

    /* access modifiers changed from: 0000 */
    public int execute() {
        return this.mCoreTask.doInBackground(new Void[0]).intValue();
    }

    /* access modifiers changed from: 0000 */
    public void execute(Executor executor) {
        this.mCoreTask.executeOnExecutor(executor, new Void[0]);
    }

    /* access modifiers changed from: 0000 */
    public OnProgressListener getOnProgressListener() {
        return this.mOnProgressListener;
    }

    /* access modifiers changed from: 0000 */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }
}
