package com.android.camera.lib.compatibility.related.v29;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.util.Size;
import android.view.IWindowManager;
import android.view.ViewConfiguration;
import com.android.camera.log.Log;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@TargetApi(29)
public class V29Utils {
    private static final Paint DEFAULT_PAINT = new Paint(6);
    private static final String TAG = "V29Utils";

    class Resizer implements OnHeaderDecodedListener {
        private final Size size;

        private Resizer(Size size2) {
            this.size = size2;
        }

        public void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
            imageDecoder.setAllocator(1);
            int max = Math.max(imageInfo.getSize().getWidth() / this.size.getWidth(), imageInfo.getSize().getHeight() / this.size.getHeight());
            if (max > 1) {
                imageDecoder.setTargetSampleSize(max);
            }
        }
    }

    static /* synthetic */ boolean O0000oO(int i) {
        return i == 1212500294;
    }

    public static StreamConfigurationMap createStreamConfigMap(List list, CameraCharacteristics cameraCharacteristics) {
        CameraCharacteristics cameraCharacteristics2 = cameraCharacteristics;
        StreamConfigurationMap streamConfigurationMap = new StreamConfigurationMap((StreamConfiguration[]) list.toArray(new StreamConfiguration[0]), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS), (StreamConfiguration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS), (StreamConfiguration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STALL_DURATIONS), (StreamConfiguration[]) cameraCharacteristics2.get(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.HEIC_AVAILABLE_HEIC_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics2.get(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STALL_DURATIONS), (HighSpeedVideoConfiguration[]) cameraCharacteristics2.get(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS), (ReprocessFormatsMap) cameraCharacteristics2.get(CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP), true);
        return streamConfigurationMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x008c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0095, code lost:
        throw r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Bitmap createVideoThumbnailBitmap(String str, FileDescriptor fileDescriptor, int i, int i2) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            if (str != null) {
                mediaMetadataRetriever.setDataSource(str);
            } else {
                mediaMetadataRetriever.setDataSource(fileDescriptor);
            }
            int parseInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
            if (parseInt == 90 || parseInt == 270) {
                int i3 = i2;
                i2 = i;
                i = i3;
            }
            Size size = new Size(i, i2);
            Resizer resizer = new Resizer(size);
            byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
            if (embeddedPicture != null) {
                Bitmap decodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(embeddedPicture), resizer);
                mediaMetadataRetriever.close();
                return decodeBitmap;
            }
            int parseInt2 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
            int parseInt3 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            if (size.getWidth() <= parseInt2 || size.getHeight() <= parseInt3) {
                Bitmap scaledFrameAtTime = mediaMetadataRetriever.getScaledFrameAtTime(parseLong / 2, 2, size.getWidth(), size.getHeight());
                mediaMetadataRetriever.close();
                return scaledFrameAtTime;
            }
            Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(parseLong / 2, 2);
            mediaMetadataRetriever.close();
            return frameAtTime;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
            return null;
        } catch (Throwable th) {
            r9.addSuppressed(th);
        }
    }

    private static Bitmap downsample(Bitmap bitmap, int i, int i2, DownsampleStrategy downsampleStrategy) {
        float scaleFactor = downsampleStrategy.getScaleFactor(bitmap.getWidth(), bitmap.getHeight(), i, i2);
        if (Float.compare(scaleFactor, 1.0f) == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        Bitmap createBitmap = Bitmap.createBitmap((int) (((float) bitmap.getWidth()) * scaleFactor), (int) (((float) bitmap.getHeight()) * scaleFactor), Config.ARGB_8888);
        createBitmap.setHasAlpha(bitmap.hasAlpha());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, matrix, DEFAULT_PAINT);
        canvas.setBitmap(null);
        bitmap.recycle();
        return createBitmap;
    }

    public static Uri getMediaUri(Context context, boolean z, boolean z2) {
        Uri uri = z ? Media.EXTERNAL_CONTENT_URI : Images.Media.EXTERNAL_CONTENT_URI;
        if (!z2) {
            return uri;
        }
        ArrayList<String> arrayList = new ArrayList<>(MediaStore.getExternalVolumeNames(context));
        if (arrayList.isEmpty() || arrayList.size() <= 1) {
            return uri;
        }
        for (String str : arrayList) {
            if (!"external_primary".equals(str)) {
                return z ? Media.getContentUri(str) : Images.Media.getContentUri(str);
            }
        }
        return uri;
    }

    public static Set getPhysicalCameraIds(CameraCharacteristics cameraCharacteristics) {
        return cameraCharacteristics.getPhysicalCameraIds();
    }

    public static int getScaledMinimumScalingSpan(Context context) {
        return ViewConfiguration.get(context).getScaledMinimumScalingSpan();
    }

    public static boolean hasNavigationBar(Context context, IWindowManager iWindowManager) {
        try {
            return iWindowManager.hasNavigationBar(context.getDisplayId());
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isHeicSupported(CameraCharacteristics cameraCharacteristics) {
        if (VERSION.SDK_INT < 29) {
            return false;
        }
        if (C0124O00000oO.O0o0o0) {
            return true;
        }
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            return false;
        }
        int[] outputFormats = streamConfigurationMap.getOutputFormats();
        if (outputFormats == null || outputFormats.length == 0) {
            return false;
        }
        return Arrays.stream(outputFormats).anyMatch(O000000o.INSTANCE);
    }
}
