package com.xiaomi.camera.imagecodec;

import android.media.ImageReader;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ImageReaderHelper {
    private static String IMAGEREADER_NAME_WITHOUT_CACHE_PREFIX = "MiuiCamera-Snapshot";
    private static String IMAGEREADER_NAME_WITH_CACHE_PREFIX = "MiuiCamera-BQ";

    /* renamed from: com.xiaomi.camera.imagecodec.ImageReaderHelper$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType = new int[ImageReaderType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.ORIGINAL.ordinal()] = 1;
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.EFFECT.ordinal()] = 2;
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.IMAGEPOOL.ordinal()] = 3;
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.YUV.ordinal()] = 4;
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.RAW.ordinal()] = 5;
            $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.VIDEOSNAP.ordinal()] = 6;
            try {
                $SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[ImageReaderType.JPEG.ordinal()] = 7;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public enum ImageReaderType {
        ORIGINAL,
        EFFECT,
        IMAGEPOOL,
        YUV,
        RAW,
        DEPTH,
        VIDEOSNAP,
        JPEG
    }

    private static String getImageReaderName(ImageReaderType imageReaderType, boolean z) {
        StringBuilder sb;
        String str;
        String str2 = z ? IMAGEREADER_NAME_WITHOUT_CACHE_PREFIX : IMAGEREADER_NAME_WITH_CACHE_PREFIX;
        switch (AnonymousClass1.$SwitchMap$com$xiaomi$camera$imagecodec$ImageReaderHelper$ImageReaderType[imageReaderType.ordinal()]) {
            case 1:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-orignal";
                break;
            case 2:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-effect";
                break;
            case 3:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-imagepool";
                break;
            case 4:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-yuv";
                break;
            case 5:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-raw";
                break;
            case 6:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-videosnap";
                break;
            case 7:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-jpeg";
                break;
            default:
                sb = new StringBuilder();
                sb.append(str2);
                str = "-default";
                break;
        }
        sb.append(str);
        return sb.toString();
    }

    public static void setImageReaderNameDepends(ImageReader imageReader, ImageReaderType imageReaderType, boolean z) {
        Method method;
        String imageReaderName = getImageReaderName(imageReaderType, z);
        try {
            method = ImageReader.class.getMethod("setName", new Class[]{String.class});
        } catch (NoSuchMethodException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("NoSuchMethodException: ");
            sb.append(e.getMessage());
            Log.w("ImageReaderHelper", sb.toString());
            method = null;
        }
        if (method != null) {
            method.setAccessible(true);
            try {
                method.invoke(imageReader, new Object[]{imageReaderName});
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }
}
