package com.android.camera.panorama;

import android.media.Image;
import android.os.Environment;
import com.android.camera.log.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputSave {
    public static final String TAG = "InputSave";
    private String FOLDER_PATH;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private String mFileNamePrefix;
    private int mIndex;

    class InputSaveRunnable implements Runnable {
        final String imageFormat;
        byte[] mImageBytes;

        public InputSaveRunnable(CaptureImage captureImage, String str) {
            byte[] image2bytes;
            this.imageFormat = str;
            Image image = captureImage.mImage;
            String str2 = InputSave.TAG;
            if (image == null) {
                Log.w(str2, "save failed, image is null");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(image.getWidth());
            sb.append("X");
            sb.append(image.getHeight());
            sb.append(", imageFormat = ");
            sb.append(this.imageFormat);
            Log.d(str2, sb.toString());
            if (PanoramaGP3ImageFormat.YUV420_PLANAR.equals(this.imageFormat)) {
                image2bytes = new ConvertFromYuv420Planar().image2bytes(image);
            } else {
                if (PanoramaGP3ImageFormat.YUV420_SEMIPLANAR.equals(this.imageFormat)) {
                    image2bytes = new ConvertFromYuv420SemiPlanar().image2bytes(image);
                } else {
                    if (PanoramaGP3ImageFormat.YVU420_SEMIPLANAR.equals(this.imageFormat)) {
                        image2bytes = new ConvertFromYvu420SemiPlanar().image2bytes(image);
                    } else {
                        this.mImageBytes = null;
                        Log.e(str2, "Image format error.");
                    }
                }
            }
            this.mImageBytes = image2bytes;
        }

        public void run() {
            byte[] bArr = this.mImageBytes;
            if (bArr != null) {
                InputSave.this.saveImage(bArr, this.imageFormat);
            }
        }
    }

    public InputSave() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Panorama/");
        this.FOLDER_PATH = sb.toString();
        this.mFileNamePrefix = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0066, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006f, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveImage(byte[] bArr, String str) {
        this.mIndex++;
        String format = String.format(Locale.US, "%06d.yuv", new Object[]{Integer.valueOf(this.mIndex)});
        StringBuilder sb = new StringBuilder();
        sb.append(this.FOLDER_PATH);
        sb.append(this.mFileNamePrefix);
        String generalFileName = generalFileName(sb.toString(), format);
        String str2 = "saveImage() error.";
        String str3 = "InputSaveState";
        if (generalFileName == null) {
            Log.e(str3, str2);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(generalFileName));
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("write file success,  ");
            sb2.append(generalFileName);
            Log.d(str4, sb2.toString());
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(str3, str2, (Throwable) e);
        } catch (Throwable th) {
            r4.addSuppressed(th);
        }
    }

    public String generalFileName(String str, String str2) {
        File file = new File(str);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        File file2 = new File(str, str2);
        int i = 0;
        while (file2.exists()) {
            i++;
            String[] split = str2.split("\\.");
            String num = Integer.toString(i);
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append("-");
            sb.append(num);
            sb.append(".");
            sb.append(split[1]);
            String sb2 = sb.toString();
            File file3 = new File(str, sb2);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("NewFilename:");
            sb3.append(sb2);
            String sb4 = sb3.toString();
            String str3 = TAG;
            Log.d(str3, sb4);
            if (i >= 1000) {
                Log.e(str3, "NewFilename 1000 count over!!");
                return null;
            }
            file2 = file3;
        }
        return file2.getAbsolutePath();
    }

    public void onSaveImage(CaptureImage captureImage, String str) {
        this.mExecutor.submit(new InputSaveRunnable(captureImage, str));
    }
}
