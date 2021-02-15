package com.android.camera.features.mimoji2.widget.helper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;
import android.media.Image.Plane;
import android.os.Build.VERSION;
import android.os.Environment;
import com.android.camera.R;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MimojiHelper2 {
    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;
    public static final String CUSTOM_DIR;
    public static final String DATA_DIR;
    public static final String EMOTICON_CACHE_DIR;
    public static final String EMOTICON_GIF_CACHE_DIR;
    public static final String EMOTICON_JPEG_CACHE_DIR;
    public static final String EMOTICON_MP4_CACHE_DIR;
    public static final String GIF_CACHE_DIR;
    public static final String GIF_NORMAL_CACHE_FILE;
    private static final int[] HUMAN_DESC = {R.string.mimoji_human_astronaut, R.string.mimoji_human_motorcyclists, R.string.mimoji_human_witches, R.string.mimoji_human_blue_haired_girls, R.string.mimoji_human_doggy_headgear_girls, R.string.mimoji_human_clowns, R.string.mimoji_human_chinese_princesses, R.string.mimoji_human_boys_with_glasses, R.string.mimoji_human_boys_with_wavy_hair};
    public static final String MIMOJI_DIR;
    public static final String MIMOJI_PREFIX = "vendor/camera/mimoji/";
    public static final String MODEL_PATH;
    private static final int ORIENTATION_HYSTERESIS = 5;
    public static final String ROOT_DIR;
    public static final String VIDEO_CACHE_DIR;
    public static final String VIDEO_DEAL_CACHE_FILE;
    public static final String VIDEO_NORMAL_CACHE_FILE;
    private static final int[] human = {4, 5, 6, 7, 2, 3, 0, 1, 8};
    private static int mCurrentOrientation = -1;

    static {
        String str;
        if (VERSION.SDK_INT < 30) {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getPath());
            sb.append("/MIUI/Camera/");
            str = sb.toString();
        } else {
            str = FileUtils.ROOT_DIR;
        }
        ROOT_DIR = str;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(ROOT_DIR);
        sb2.append("mimoji/");
        MIMOJI_DIR = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(MIMOJI_DIR);
        sb3.append("data/");
        DATA_DIR = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(MIMOJI_DIR);
        sb4.append("model/");
        MODEL_PATH = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(ROOT_DIR);
        sb5.append("custom/");
        CUSTOM_DIR = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(MIMOJI_DIR);
        sb6.append("video");
        sb6.append(File.separator);
        VIDEO_CACHE_DIR = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(VIDEO_CACHE_DIR);
        sb7.append("mimoji_normal.mp4");
        VIDEO_NORMAL_CACHE_FILE = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(VIDEO_CACHE_DIR);
        sb8.append("mimoji_deal.mp4");
        VIDEO_DEAL_CACHE_FILE = sb8.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(MIMOJI_DIR);
        String str2 = "gif";
        sb9.append(str2);
        sb9.append(File.separator);
        GIF_CACHE_DIR = sb9.toString();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(GIF_CACHE_DIR);
        sb10.append("gif_normal.mp4");
        GIF_NORMAL_CACHE_FILE = sb10.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(MIMOJI_DIR);
        sb11.append("emoticon");
        sb11.append(File.separator);
        EMOTICON_CACHE_DIR = sb11.toString();
        StringBuilder sb12 = new StringBuilder();
        sb12.append(EMOTICON_CACHE_DIR);
        sb12.append("mp4");
        sb12.append(File.separator);
        EMOTICON_MP4_CACHE_DIR = sb12.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(EMOTICON_CACHE_DIR);
        sb13.append(str2);
        sb13.append(File.separator);
        EMOTICON_GIF_CACHE_DIR = sb13.toString();
        StringBuilder sb14 = new StringBuilder();
        sb14.append(EMOTICON_CACHE_DIR);
        sb14.append("jpeg");
        sb14.append(File.separator);
        EMOTICON_JPEG_CACHE_DIR = sb14.toString();
    }

    public static byte[] getDataFromImage(Image image, int i) {
        Rect rect;
        int i2;
        int i3 = i;
        int i4 = 2;
        int i5 = 1;
        if (i3 == 1 || i3 == 2) {
            Rect cropRect = image.getCropRect();
            int format = image.getFormat();
            int width = cropRect.width();
            int height = cropRect.height();
            Plane[] planes = image.getPlanes();
            int i6 = width * height;
            byte[] bArr = new byte[((ImageFormat.getBitsPerPixel(format) * i6) / 8)];
            int i7 = 0;
            byte[] bArr2 = new byte[planes[0].getRowStride()];
            int i8 = 1;
            int i9 = 0;
            int i10 = 0;
            while (i9 < planes.length) {
                if (i9 != 0) {
                    if (i9 != i5) {
                        if (i9 == i4) {
                            if (i3 == i5) {
                                i10 = (int) (((double) i6) * 1.25d);
                                i8 = i5;
                            } else if (i3 == i4) {
                                i8 = i4;
                            }
                        }
                    } else if (i3 == i5) {
                        i8 = i5;
                    } else if (i3 == i4) {
                        i10 = i6 + 1;
                        i8 = i4;
                    }
                    i10 = i6;
                } else {
                    i8 = i5;
                    i10 = i7;
                }
                ByteBuffer buffer = planes[i9].getBuffer();
                int rowStride = planes[i9].getRowStride();
                int pixelStride = planes[i9].getPixelStride();
                int i11 = i9 == 0 ? i7 : i5;
                int i12 = width >> i11;
                int i13 = height >> i11;
                int i14 = width;
                buffer.position(((cropRect.top >> i11) * rowStride) + ((cropRect.left >> i11) * pixelStride));
                int i15 = 0;
                while (i15 < i13) {
                    if (pixelStride == 1 && i8 == 1) {
                        buffer.get(bArr, i10, i12);
                        i10 += i12;
                        rect = cropRect;
                        i2 = i12;
                    } else {
                        rect = cropRect;
                        i2 = ((i12 - 1) * pixelStride) + 1;
                        buffer.get(bArr2, 0, i2);
                        int i16 = i10;
                        for (int i17 = 0; i17 < i12; i17++) {
                            bArr[i16] = bArr2[i17 * pixelStride];
                            i16 += i8;
                        }
                        i10 = i16;
                    }
                    if (i15 < i13 - 1) {
                        buffer.position((buffer.position() + rowStride) - i2);
                    }
                    i15++;
                    cropRect = rect;
                }
                Rect rect2 = cropRect;
                i9++;
                i3 = i;
                width = i14;
                i4 = 2;
                i5 = 1;
                i7 = 0;
            }
            return bArr;
        }
        throw new IllegalArgumentException("only support COLOR_FormatI420 and COLOR_FormatNV21");
    }

    public static List getMimojiCartoonList() {
        ArrayList arrayList = new ArrayList();
        try {
            MimojiInfo2 mimojiInfo2 = new MimojiInfo2();
            mimojiInfo2.mConfigPath = "close_state";
            mimojiInfo2.mDirectoryName = Long.MAX_VALUE;
            mimojiInfo2.mName = R.string.lighting_pattern_null;
            arrayList.add(mimojiInfo2);
            MimojiInfo2 mimojiInfo22 = new MimojiInfo2();
            mimojiInfo22.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_CAT;
            mimojiInfo22.mConfigPath = "cat";
            StringBuilder sb = new StringBuilder();
            sb.append(DATA_DIR);
            sb.append("cat.png");
            mimojiInfo22.mThumbnailUrl = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(DATA_DIR);
            sb2.append("cat1.png");
            mimojiInfo22.mThumbnailUrl2 = sb2.toString();
            mimojiInfo22.mName = R.string.mimoji_cartoon_cat;
            mimojiInfo22.mName2 = R.string.mimoji_cartoon_cat_with_bow;
            mimojiInfo22.setDefaultFrame(1);
            mimojiInfo22.setFrame(1);
            arrayList.add(mimojiInfo22);
            MimojiInfo2 mimojiInfo23 = new MimojiInfo2();
            mimojiInfo23.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_FROG;
            mimojiInfo23.mConfigPath = AvatarEngineManager2.CONFIG_PATH_FAKE_FROG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(DATA_DIR);
            sb3.append("frog.png");
            mimojiInfo23.mThumbnailUrl = sb3.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(DATA_DIR);
            sb4.append("frog1.png");
            mimojiInfo23.mThumbnailUrl2 = sb4.toString();
            mimojiInfo23.mName = R.string.mimoji_cartoon_frog;
            mimojiInfo23.mName2 = R.string.mimoji_cartoon_frog_prince;
            mimojiInfo23.setDefaultFrame(1);
            mimojiInfo23.setFrame(1);
            arrayList.add(mimojiInfo23);
            MimojiInfo2 mimojiInfo24 = new MimojiInfo2();
            mimojiInfo24.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_RABBIT2;
            mimojiInfo24.mConfigPath = AvatarEngineManager2.CONFIG_PATH_FAKE_RABBIT2;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(DATA_DIR);
            sb5.append("rabbit.png");
            mimojiInfo24.mThumbnailUrl = sb5.toString();
            StringBuilder sb6 = new StringBuilder();
            sb6.append(DATA_DIR);
            sb6.append("rabbit1.png");
            mimojiInfo24.mThumbnailUrl2 = sb6.toString();
            mimojiInfo24.mName = R.string.mimoji_cartoon_rabbit;
            mimojiInfo24.mName2 = R.string.mimoji_cartoon_rabbit_mechanical;
            mimojiInfo24.setDefaultFrame(1);
            mimojiInfo24.setFrame(1);
            arrayList.add(mimojiInfo24);
            MimojiInfo2 mimojiInfo25 = new MimojiInfo2();
            mimojiInfo25.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_BEAR;
            mimojiInfo25.mConfigPath = "bear";
            StringBuilder sb7 = new StringBuilder();
            sb7.append(DATA_DIR);
            sb7.append("bear.png");
            mimojiInfo25.mThumbnailUrl = sb7.toString();
            mimojiInfo25.mName = R.string.mimoji_cartoon_bear;
            arrayList.add(mimojiInfo25);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static List getMimojiHumanList() {
        File[] listFiles;
        String str = "/";
        ArrayList arrayList = new ArrayList();
        MimojiInfo2 mimojiInfo2 = new MimojiInfo2();
        mimojiInfo2.mConfigPath = "close_state";
        mimojiInfo2.mDirectoryName = Long.MAX_VALUE;
        mimojiInfo2.mName = R.string.lighting_pattern_null;
        arrayList.add(mimojiInfo2);
        MimojiInfo2 mimojiInfo22 = new MimojiInfo2();
        mimojiInfo22.mConfigPath = "add_state";
        mimojiInfo22.mDirectoryName = Long.MAX_VALUE;
        mimojiInfo22.mName = R.string.accessibility_add;
        arrayList.add(mimojiInfo22);
        try {
            ArrayList arrayList2 = new ArrayList();
            File file = new File(CUSTOM_DIR);
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    MimojiInfo2 mimojiInfo23 = new MimojiInfo2();
                    mimojiInfo23.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_HUMAN;
                    String name = file2.getName();
                    String absolutePath = file2.getAbsolutePath();
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    sb.append("config.dat");
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(name);
                    sb3.append("pic.png");
                    String sb4 = sb3.toString();
                    if (file2.isDirectory()) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(CUSTOM_DIR);
                        sb5.append(name);
                        sb5.append(str);
                        sb5.append(sb2);
                        String sb6 = sb5.toString();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(CUSTOM_DIR);
                        sb7.append(name);
                        sb7.append(str);
                        sb7.append(sb4);
                        String sb8 = sb7.toString();
                        if (FileUtils.checkFileConsist(sb6) && FileUtils.checkFileConsist(sb8)) {
                            mimojiInfo23.mConfigPath = sb6;
                            mimojiInfo23.mThumbnailUrl = sb8;
                            mimojiInfo23.mPackPath = absolutePath;
                            mimojiInfo23.mDirectoryName = Long.valueOf(name).longValue();
                            arrayList.add(mimojiInfo23);
                        }
                    }
                    arrayList2.add(absolutePath);
                }
                Collections.sort(arrayList);
            }
            ArrayList arrayList3 = new ArrayList();
            File file3 = new File(AvatarEngineManager2.CONFIG_PATH_PRE_HUMAN);
            if (file3.exists() && file3.isDirectory()) {
                for (int i = 0; i < human.length; i++) {
                    MimojiInfo2 mimojiInfo24 = new MimojiInfo2();
                    mimojiInfo24.mAvatarTemplatePath = AvatarEngineManager2.TEMPLATE_PATH_HUMAN;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("preconfig");
                    sb9.append(i);
                    String sb10 = sb9.toString();
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(AvatarEngineManager2.CONFIG_PATH_PRE_HUMAN);
                    sb11.append(File.separator);
                    sb11.append(sb10);
                    sb11.append(".dat");
                    String sb12 = sb11.toString();
                    StringBuilder sb13 = new StringBuilder();
                    sb13.append(AvatarEngineManager2.CONFIG_PATH_PRE_HUMAN);
                    sb13.append(File.separator);
                    sb13.append(sb10);
                    sb13.append(FileUtils.FILTER_FILE_SUFFIX);
                    String sb14 = sb13.toString();
                    if (!FileUtils.checkFileConsist(sb12) || !FileUtils.checkFileConsist(sb14)) {
                        arrayList2.add(sb12);
                        arrayList2.add(sb14);
                    } else {
                        mimojiInfo24.mConfigPath = sb12;
                        mimojiInfo24.mPackPath = sb12;
                        mimojiInfo24.mThumbnailUrl = sb14;
                        mimojiInfo24.mName = HUMAN_DESC[i];
                        mimojiInfo24.setIsPreHuman(true);
                        arrayList3.add(mimojiInfo24);
                    }
                }
                Collections.sort(arrayList3);
            }
            arrayList.addAll(arrayList3);
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                FileUtils.deleteFile((String) arrayList2.get(i2));
            }
        } catch (Exception e) {
            StringBuilder sb15 = new StringBuilder();
            sb15.append("mimoji getMimojiHumanList[] Exception");
            sb15.append(e.getMessage());
            Log.e("MimojiHelper2", sb15.toString());
        }
        return arrayList;
    }

    public static int getOutlineOrientation(int i, int i2, boolean z) {
        mCurrentOrientation = roundOrientation(i2, mCurrentOrientation);
        int i3 = mCurrentOrientation;
        int i4 = (z ? (i - i3) + m.cQ : i3 + i) % m.cQ;
        StringBuilder sb = new StringBuilder();
        sb.append("cameraRotation = ");
        sb.append(i);
        sb.append(" sensorOrientation = ");
        sb.append(mCurrentOrientation);
        sb.append("outlineOrientation = ");
        sb.append(i4);
        Log.d("OrientationUtil", sb.toString());
        return i4;
    }

    public static Bitmap getThumbnailBitmapFromData(byte[] bArr, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
        return createBitmap;
    }

    public static int getTipsResId(int i) {
        switch (i) {
            case 1:
                return R.string.mimoji_check_no_face;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return R.string.mimoji_check_face_occlusion;
            case 9:
                return R.string.mimoji_check_beyond_20_degrees;
            case 10:
                return R.string.mimoji_check_multi_face;
            default:
                return -1;
        }
    }

    public static int getTipsResIdFace(int i) {
        if (i == 3) {
            return R.string.mimoji_check_face_too_close;
        }
        if (i == 4) {
            return R.string.mimoji_check_face_too_far;
        }
        if (i != 7) {
            return -1;
        }
        return R.string.mimoji_check_low_light;
    }

    private static int roundOrientation(int i, int i2) {
        boolean z = true;
        if (i2 != -1) {
            int abs = Math.abs(i - i2);
            if (Math.min(abs, 360 - abs) < 50) {
                z = false;
            }
        }
        return z ? (((i + 45) / 90) * 90) % m.cQ : i2;
    }
}
