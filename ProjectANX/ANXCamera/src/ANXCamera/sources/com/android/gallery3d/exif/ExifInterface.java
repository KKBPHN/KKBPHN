package com.android.gallery3d.exif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.SparseIntArray;
import com.android.camera.log.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class ExifInterface {
    private static final String DATETIME_FORMAT_STR = "yyyy:MM:dd HH:mm:ss";
    public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    public static final int DEFINITION_NULL = 0;
    private static final String GPS_DATE_FORMAT_STR = "yyyy:MM:dd";
    public static final int IFD_NULL = -1;
    private static final String NULL_ARGUMENT_STRING = "Argument is null";
    protected static final String TAG = "ExifInterface";
    public static final int TAG_AI_TYPE = defineTag(2, -30571);
    public static final int TAG_ALGORITHM_COMMENT = defineTag(2, -30583);
    public static final int TAG_APERTURE_VALUE = defineTag(2, -28158);
    public static final int TAG_ARTIST = defineTag(0, 315);
    public static final int TAG_BITS_PER_SAMPLE = defineTag(0, 258);
    public static final int TAG_BRIGHTNESS_VALUE = defineTag(2, -28157);
    public static final int TAG_CFA_PATTERN = defineTag(2, -23806);
    public static final int TAG_COLOR_SPACE = defineTag(2, -24575);
    public static final int TAG_COMPONENTS_CONFIGURATION = defineTag(2, -28415);
    public static final int TAG_COMPRESSED_BITS_PER_PIXEL = defineTag(2, -28414);
    public static final int TAG_COMPRESSION = defineTag(0, 259);
    public static final int TAG_CONTRAST = defineTag(2, -23544);
    public static final int TAG_COPYRIGHT = defineTag(0, -32104);
    public static final int TAG_CUSTOM_RENDERED = defineTag(2, -23551);
    public static final int TAG_DATE_TIME = defineTag(0, 306);
    public static final int TAG_DATE_TIME_DIGITIZED = defineTag(2, -28668);
    public static final int TAG_DATE_TIME_ORIGINAL = defineTag(2, -28669);
    public static final int TAG_DEPTH_MAP_BLUR_LEVEL = defineTag(2, -30575);
    public static final int TAG_DEPTH_MAP_DULCAMERA_WATER_MARK = defineTag(2, -30574);
    public static final int TAG_DEPTH_MAP_FOCUS_POINT = defineTag(2, -30576);
    public static final int TAG_DEPTH_MAP_TIME_WATER_MARK = defineTag(2, -30573);
    public static final int TAG_DEVICE_SETTING_DESCRIPTION = defineTag(2, -23541);
    public static final int TAG_DIGITAL_ZOOM_RATIO = defineTag(2, -23548);
    public static final int TAG_EXIF_IFD = defineTag(0, -30871);
    public static final int TAG_EXIF_VERSION = defineTag(2, -28672);
    public static final int TAG_EXPOSURE_BIAS_VALUE = defineTag(2, -28156);
    public static final int TAG_EXPOSURE_INDEX = defineTag(2, -24043);
    public static final int TAG_EXPOSURE_MODE = defineTag(2, -23550);
    public static final int TAG_EXPOSURE_PROGRAM = defineTag(2, -30686);
    public static final int TAG_EXPOSURE_TIME = defineTag(2, -32102);
    public static final int TAG_FILE_SOURCE = defineTag(2, -23808);
    public static final int TAG_FLASH = defineTag(2, -28151);
    public static final int TAG_FLASHPIX_VERSION = defineTag(2, -24576);
    public static final int TAG_FLASH_ENERGY = defineTag(2, -24053);
    public static final int TAG_FOCAL_LENGTH = defineTag(2, -28150);
    public static final int TAG_FOCAL_LENGTH_IN_35_MM_FILE = defineTag(2, -23547);
    public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT = defineTag(2, -24048);
    public static final int TAG_FOCAL_PLANE_X_RESOLUTION = defineTag(2, -24050);
    public static final int TAG_FOCAL_PLANE_Y_RESOLUTION = defineTag(2, -24049);
    public static final int TAG_FRONT_MIRROR = defineTag(2, -30570);
    public static final int TAG_F_NUMBER = defineTag(2, -32099);
    public static final int TAG_GAIN_CONTROL = defineTag(2, -23545);
    public static final int TAG_GPS_ALTITUDE = defineTag(4, 6);
    public static final int TAG_GPS_ALTITUDE_REF = defineTag(4, 5);
    public static final int TAG_GPS_AREA_INFORMATION = defineTag(4, 28);
    public static final int TAG_GPS_DATE_STAMP = defineTag(4, 29);
    public static final int TAG_GPS_DEST_BEARING = defineTag(4, 24);
    public static final int TAG_GPS_DEST_BEARING_REF = defineTag(4, 23);
    public static final int TAG_GPS_DEST_DISTANCE = defineTag(4, 26);
    public static final int TAG_GPS_DEST_DISTANCE_REF = defineTag(4, 25);
    public static final int TAG_GPS_DEST_LATITUDE = defineTag(4, 20);
    public static final int TAG_GPS_DEST_LATITUDE_REF = defineTag(4, 19);
    public static final int TAG_GPS_DEST_LONGITUDE = defineTag(4, 22);
    public static final int TAG_GPS_DEST_LONGITUDE_REF = defineTag(4, 21);
    public static final int TAG_GPS_DIFFERENTIAL = defineTag(4, 30);
    public static final int TAG_GPS_DOP = defineTag(4, 11);
    public static final int TAG_GPS_IFD = defineTag(0, -30683);
    public static final int TAG_GPS_IMG_DIRECTION = defineTag(4, 17);
    public static final int TAG_GPS_IMG_DIRECTION_REF = defineTag(4, 16);
    public static final int TAG_GPS_LATITUDE = defineTag(4, 2);
    public static final int TAG_GPS_LATITUDE_REF = defineTag(4, 1);
    public static final int TAG_GPS_LONGITUDE = defineTag(4, 4);
    public static final int TAG_GPS_LONGITUDE_REF = defineTag(4, 3);
    public static final int TAG_GPS_MAP_DATUM = defineTag(4, 18);
    public static final int TAG_GPS_MEASURE_MODE = defineTag(4, 10);
    public static final int TAG_GPS_PROCESSING_METHOD = defineTag(4, 27);
    public static final int TAG_GPS_SATTELLITES = defineTag(4, 8);
    public static final int TAG_GPS_SPEED = defineTag(4, 13);
    public static final int TAG_GPS_SPEED_REF = defineTag(4, 12);
    public static final int TAG_GPS_STATUS = defineTag(4, 9);
    public static final int TAG_GPS_TIME_STAMP = defineTag(4, 7);
    public static final int TAG_GPS_TRACK = defineTag(4, 15);
    public static final int TAG_GPS_TRACK_REF = defineTag(4, 14);
    public static final int TAG_GPS_VERSION_ID = defineTag(4, 0);
    public static final int TAG_IMAGE_DESCRIPTION = defineTag(0, 270);
    public static final int TAG_IMAGE_LENGTH = defineTag(0, 257);
    public static final int TAG_IMAGE_UNIQUE_ID = defineTag(2, -23520);
    public static final int TAG_IMAGE_WIDTH = defineTag(0, 256);
    public static final int TAG_INTEROPERABILITY_IFD = defineTag(2, -24571);
    public static final int TAG_INTEROPERABILITY_INDEX = defineTag(3, 1);
    public static final int TAG_ISO_SPEED_RATINGS = defineTag(2, -30681);
    public static final int TAG_JPEG_INTERCHANGE_FORMAT = defineTag(1, 513);
    public static final int TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = defineTag(1, 514);
    public static final int TAG_LIGHT_SOURCE = defineTag(2, -28152);
    public static final int TAG_MAKE = defineTag(0, 271);
    public static final int TAG_MAKER_NOTE = defineTag(2, -28036);
    public static final int TAG_MAX_APERTURE_VALUE = defineTag(2, -28155);
    public static final int TAG_METERING_MODE = defineTag(2, -28153);
    public static final int TAG_MODEL = defineTag(0, 272);
    public static final int TAG_NULL = -1;
    public static final int TAG_OECF = defineTag(2, -30680);
    public static final int TAG_ORIENTATION = defineTag(0, 274);
    public static final int TAG_PARALLEL_PROCESS_COMMENT = defineTag(2, -30584);
    public static final int TAG_PHOTOMETRIC_INTERPRETATION = defineTag(0, 262);
    public static final int TAG_PIXEL_X_DIMENSION = defineTag(2, -24574);
    public static final int TAG_PIXEL_Y_DIMENSION = defineTag(2, -24573);
    public static final int TAG_PLANAR_CONFIGURATION = defineTag(0, 284);
    public static final int TAG_PORTRAIT_LIGHTING_PATTERN = defineTag(2, -30572);
    public static final int TAG_PRIMARY_CHROMATICITIES = defineTag(0, 319);
    public static final int TAG_REFERENCE_BLACK_WHITE = defineTag(0, 532);
    public static final int TAG_RELATED_SOUND_FILE = defineTag(2, -24572);
    public static final int TAG_RESOLUTION_UNIT = defineTag(0, 296);
    public static final int TAG_ROWS_PER_STRIP = defineTag(0, 278);
    public static final int TAG_SAMPLES_PER_PIXEL = defineTag(0, 277);
    public static final int TAG_SATURATION = defineTag(2, -23543);
    public static final int TAG_SCENE_CAPTURE_TYPE = defineTag(2, -23546);
    public static final int TAG_SCENE_TYPE = defineTag(2, -23807);
    public static final int TAG_SENSING_METHOD = defineTag(2, -24041);
    public static final int TAG_SHARPNESS = defineTag(2, -23542);
    public static final int TAG_SHUTTER_SPEED_VALUE = defineTag(2, -28159);
    public static final int TAG_SOFTWARE = defineTag(0, 305);
    public static final int TAG_SPATIAL_FREQUENCY_RESPONSE = defineTag(2, -24052);
    public static final int TAG_SPECTRAL_SENSITIVITY = defineTag(2, -30684);
    public static final int TAG_STRIP_BYTE_COUNTS = defineTag(0, 279);
    public static final int TAG_STRIP_OFFSETS = defineTag(0, 273);
    public static final int TAG_SUBJECT_AREA = defineTag(2, -28140);
    public static final int TAG_SUBJECT_DISTANCE = defineTag(2, -28154);
    public static final int TAG_SUBJECT_DISTANCE_RANGE = defineTag(2, -23540);
    public static final int TAG_SUBJECT_LOCATION = defineTag(2, -24044);
    public static final int TAG_SUB_SEC_TIME = defineTag(2, -28016);
    public static final int TAG_SUB_SEC_TIME_DIGITIZED = defineTag(2, -28014);
    public static final int TAG_SUB_SEC_TIME_ORIGINAL = defineTag(2, -28015);
    public static final int TAG_TRANSFER_FUNCTION = defineTag(0, 301);
    public static final int TAG_USER_COMMENT = defineTag(2, -28026);
    public static final int TAG_WHITE_BALANCE = defineTag(2, -23549);
    public static final int TAG_WHITE_POINT = defineTag(0, 318);
    public static final int TAG_XIAOMI_ALGO_COMMENT = defineTag(2, -25942);
    public static final int TAG_XIAOMI_COMMENT = defineTag(2, -26215);
    public static final int TAG_XIAOMI_DEPTHMAP_VERSION = defineTag(2, -30568);
    public static final int TAG_XIAOMI_DOC_PHOTO = defineTag(2, -30567);
    public static final int TAG_XIAOMI_LIVESHOT_PHOTO = defineTag(2, -30569);
    public static final int TAG_XIAOMI_PRODUCT = defineTag(2, -26112);
    public static final int TAG_X_RESOLUTION = defineTag(0, 282);
    public static final int TAG_Y_CB_CR_COEFFICIENTS = defineTag(0, 529);
    public static final int TAG_Y_CB_CR_POSITIONING = defineTag(0, 531);
    public static final int TAG_Y_CB_CR_SUB_SAMPLING = defineTag(0, 530);
    public static final int TAG_Y_RESOLUTION = defineTag(0, 283);
    protected static HashSet sBannedDefines = new HashSet(sOffsetTags);
    private static HashSet sOffsetTags = new HashSet();
    private ExifData mData = new ExifData(DEFAULT_BYTE_ORDER);
    private final DateFormat mDateTimeStampFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
    private final DateFormat mGPSDateStampFormat = new SimpleDateFormat(GPS_DATE_FORMAT_STR);
    private final Calendar mGPSTimeStampCalendar;
    private int mMimeType = 0;
    private SparseIntArray mTagInfo;

    public interface ColorSpace {
        public static final short SRGB = 1;
        public static final short UNCALIBRATED = -1;
    }

    public interface ComponentsConfiguration {
        public static final short B = 6;
        public static final short CB = 2;
        public static final short CR = 3;
        public static final short G = 5;
        public static final short NOT_EXIST = 0;
        public static final short R = 4;
        public static final short Y = 1;
    }

    public interface Compression {
        public static final short JPEG = 6;
        public static final short UNCOMPRESSION = 1;
    }

    public interface Contrast {
        public static final short HARD = 2;
        public static final short NORMAL = 0;
        public static final short SOFT = 1;
    }

    public interface ExifOrientationFlag {
        public static final short BOTTOM_LEFT = 3;
        public static final short BOTTOM_RIGHT = 4;
        public static final short LEFT_BOTTOM = 7;
        public static final short LEFT_TOP = 5;
        public static final short RIGHT_BOTTOM = 8;
        public static final short RIGHT_TOP = 6;
        public static final short TOP_LEFT = 1;
        public static final short TOP_RIGHT = 2;
    }

    public interface ExposureMode {
        public static final short AUTO_BRACKET = 2;
        public static final short AUTO_EXPOSURE = 0;
        public static final short MANUAL_EXPOSURE = 1;
    }

    public interface ExposureProgram {
        public static final short ACTION_PROGRAM = 6;
        public static final short APERTURE_PRIORITY = 3;
        public static final short CREATIVE_PROGRAM = 5;
        public static final short LANDSCAPE_MODE = 8;
        public static final short MANUAL = 1;
        public static final short NORMAL_PROGRAM = 2;
        public static final short NOT_DEFINED = 0;
        public static final short PROTRAIT_MODE = 7;
        public static final short SHUTTER_PRIORITY = 4;
    }

    public interface FileSource {
        public static final short DSC = 3;
    }

    public interface Flash {
        public static final short DID_NOT_FIRED = 0;
        public static final short FIRED = 1;
        public static final short FUNCTION_NO_FUNCTION = 32;
        public static final short FUNCTION_PRESENT = 0;
        public static final short MODE_AUTO_MODE = 24;
        public static final short MODE_COMPULSORY_FLASH_FIRING = 8;
        public static final short MODE_COMPULSORY_FLASH_SUPPRESSION = 16;
        public static final short MODE_UNKNOWN = 0;
        public static final short RED_EYE_REDUCTION_NO_OR_UNKNOWN = 0;
        public static final short RED_EYE_REDUCTION_SUPPORT = 64;
        public static final short RETURN_NO_STROBE_RETURN_DETECTION_FUNCTION = 0;
        public static final short RETURN_STROBE_RETURN_LIGHT_DETECTED = 6;
        public static final short RETURN_STROBE_RETURN_LIGHT_NOT_DETECTED = 4;
    }

    public interface GainControl {
        public static final short HIGH_DOWN = 4;
        public static final short HIGH_UP = 2;
        public static final short LOW_DOWN = 3;
        public static final short LOW_UP = 1;
        public static final short NONE = 0;
    }

    public interface GpsAltitudeRef {
        public static final short SEA_LEVEL = 0;
        public static final short SEA_LEVEL_NEGATIVE = 1;
    }

    public interface GpsDifferential {
        public static final short DIFFERENTIAL_CORRECTION_APPLIED = 1;
        public static final short WITHOUT_DIFFERENTIAL_CORRECTION = 0;
    }

    public interface GpsLatitudeRef {
        public static final String NORTH = "N";
        public static final String SOUTH = "S";
    }

    public interface GpsLongitudeRef {
        public static final String EAST = "E";
        public static final String WEST = "W";
    }

    public interface GpsMeasureMode {
        public static final String MODE_2_DIMENSIONAL = "2";
        public static final String MODE_3_DIMENSIONAL = "3";
    }

    public interface GpsSpeedRef {
        public static final String KILOMETERS = "K";
        public static final String KNOTS = "N";
        public static final String MILES = "M";
    }

    public interface GpsStatus {
        public static final String INTEROPERABILITY = "V";
        public static final String IN_PROGRESS = "A";
    }

    public interface GpsTrackRef {
        public static final String MAGNETIC_DIRECTION = "M";
        public static final String TRUE_DIRECTION = "T";
    }

    public interface LightSource {
        public static final short CLOUDY_WEATHER = 10;
        public static final short COOL_WHITE_FLUORESCENT = 14;
        public static final short D50 = 23;
        public static final short D55 = 20;
        public static final short D65 = 21;
        public static final short D75 = 22;
        public static final short DAYLIGHT = 1;
        public static final short DAYLIGHT_FLUORESCENT = 12;
        public static final short DAY_WHITE_FLUORESCENT = 13;
        public static final short FINE_WEATHER = 9;
        public static final short FLASH = 4;
        public static final short FLUORESCENT = 2;
        public static final short ISO_STUDIO_TUNGSTEN = 24;
        public static final short OTHER = 255;
        public static final short SHADE = 11;
        public static final short STANDARD_LIGHT_A = 17;
        public static final short STANDARD_LIGHT_B = 18;
        public static final short STANDARD_LIGHT_C = 19;
        public static final short TUNGSTEN = 3;
        public static final short UNKNOWN = 0;
        public static final short WHITE_FLUORESCENT = 15;
    }

    public interface MeteringMode {
        public static final short AVERAGE = 1;
        public static final short CENTER_WEIGHTED_AVERAGE = 2;
        public static final short MULTISPOT = 4;
        public static final short OTHER = 255;
        public static final short PARTAIL = 6;
        public static final short PATTERN = 5;
        public static final short SPOT = 3;
        public static final short UNKNOWN = 0;
    }

    public interface PhotometricInterpretation {
        public static final short RGB = 2;
        public static final short YCBCR = 6;
    }

    public interface PlanarConfiguration {
        public static final short CHUNKY = 1;
        public static final short PLANAR = 2;
    }

    public interface ResolutionUnit {
        public static final short CENTIMETERS = 3;
        public static final short INCHES = 2;
    }

    public interface Saturation {
        public static final short HIGH = 2;
        public static final short LOW = 1;
        public static final short NORMAL = 0;
    }

    public interface SceneCapture {
        public static final short LANDSCAPE = 1;
        public static final short NIGHT_SCENE = 3;
        public static final short PROTRAIT = 2;
        public static final short STANDARD = 0;
    }

    public interface SceneType {
        public static final short DIRECT_PHOTOGRAPHED = 1;
    }

    public interface SensingMethod {
        public static final short COLOR_SEQUENTIAL_AREA = 5;
        public static final short COLOR_SEQUENTIAL_LINEAR = 8;
        public static final short NOT_DEFINED = 1;
        public static final short ONE_CHIP_COLOR = 2;
        public static final short THREE_CHIP_COLOR = 4;
        public static final short TRILINEAR = 7;
        public static final short TWO_CHIP_COLOR = 3;
    }

    public interface Sharpness {
        public static final short HARD = 2;
        public static final short NORMAL = 0;
        public static final short SOFT = 1;
    }

    public interface SubjectDistance {
        public static final short CLOSE_VIEW = 2;
        public static final short DISTANT_VIEW = 3;
        public static final short MACRO = 1;
        public static final short UNKNOWN = 0;
    }

    public interface WhiteBalance {
        public static final short AUTO = 0;
        public static final short MANUAL = 1;
    }

    public interface YCbCrPositioning {
        public static final short CENTERED = 1;
        public static final short CO_SITED = 2;
    }

    static {
        sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_GPS_IFD)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_EXIF_IFD)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_INTEROPERABILITY_IFD)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_STRIP_OFFSETS)));
        sBannedDefines.add(Short.valueOf(getTrueTagKey(-1)));
        sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)));
        sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_STRIP_BYTE_COUNTS)));
    }

    public ExifInterface() {
        String str = "UTC";
        this.mGPSTimeStampCalendar = Calendar.getInstance(TimeZone.getTimeZone(str));
        this.mTagInfo = null;
        this.mGPSDateStampFormat.setTimeZone(TimeZone.getTimeZone(str));
    }

    private boolean addExifTag(int i, Object obj) {
        ExifTag buildTag = buildTag(i, obj);
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public static byte[] addXiaomiComment(byte[] bArr, String str) {
        long currentTimeMillis = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(bArr);
            exifInterface.addXiaomiComment(str);
            exifInterface.writeExif(bArr, (OutputStream) byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            bArr = byteArray;
        } catch (IOException e) {
            Log.e(ExifInterface.class.getName(), e.getMessage(), (Throwable) e);
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addXiaomiComment cost=");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.v(str2, sb.toString());
        return bArr;
    }

    protected static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static double convertLatOrLongToDouble(Rational[] rationalArr, String str) {
        try {
            double d = rationalArr[0].toDouble() + (rationalArr[1].toDouble() / 60.0d) + (rationalArr[2].toDouble() / 3600.0d);
            return (str.equals("S") || str.equals("W")) ? -d : d;
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new IllegalArgumentException();
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r2v0, types: [int, short] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int defineTag(int i, int i2) {
        return (i << 16) | (i2 & 65535);
    }

    private void doExifStreamIO(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr, 0, 1024);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    protected static int getAllowedIfdFlagsFromInfo(int i) {
        return i >>> 24;
    }

    protected static int[] getAllowedIfdsFromInfo(int i) {
        int allowedIfdFlagsFromInfo = getAllowedIfdFlagsFromInfo(i);
        int[] ifds = IfdData.getIfds();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < 5; i3++) {
            if (((allowedIfdFlagsFromInfo >> i3) & 1) == 1) {
                arrayList.add(Integer.valueOf(ifds[i3]));
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        int[] iArr = new int[arrayList.size()];
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            int i4 = i2 + 1;
            iArr[i2] = ((Integer) it.next()).intValue();
            i2 = i4;
        }
        return iArr;
    }

    protected static int getComponentCountFromInfo(int i) {
        return i & 65535;
    }

    public static ExifInterface getExif(byte[] bArr) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(bArr);
        } catch (IOException e) {
            Log.w(TAG, "Failed to read EXIF data", (Throwable) e);
        }
        return exifInterface;
    }

    public static short getExifOrientationValue(int i) {
        int i2 = i % m.cQ;
        if (i2 < 0) {
            i2 += m.cQ;
        }
        if (i2 < 90) {
            return 1;
        }
        if (i2 < 180) {
            return 6;
        }
        return i2 < 270 ? (short) 3 : 8;
    }

    protected static int getFlagsFromAllowedIfds(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return 0;
        }
        int[] ifds = IfdData.getIfds();
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            int length = iArr.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (ifds[i2] == iArr[i3]) {
                    i |= 1 << i2;
                    break;
                }
                i3++;
            }
        }
        return i;
    }

    public static int getImageHeight(ExifInterface exifInterface) {
        Integer tagIntValue = exifInterface.getTagIntValue(TAG_IMAGE_LENGTH);
        if (tagIntValue == null) {
            return 0;
        }
        return tagIntValue.intValue();
    }

    public static int getImageWidth(ExifInterface exifInterface) {
        Integer tagIntValue = exifInterface.getTagIntValue(TAG_IMAGE_WIDTH);
        if (tagIntValue == null) {
            return 0;
        }
        return tagIntValue.intValue();
    }

    public static int getOrientation(ExifInterface exifInterface) {
        Integer tagIntValue = exifInterface.getTagIntValue(TAG_ORIENTATION);
        if (tagIntValue == null) {
            return 0;
        }
        return getRotationForOrientationValue(tagIntValue.shortValue());
    }

    public static int getRotationForOrientationValue(short s) {
        if (s == 1) {
            return 0;
        }
        if (s == 3) {
            return 180;
        }
        if (s != 6) {
            return s != 8 ? 0 : 270;
        }
        return 90;
    }

    public static int getTrueIfd(int i) {
        return i >>> 16;
    }

    public static short getTrueTagKey(int i) {
        return (short) i;
    }

    protected static short getTypeFromInfo(int i) {
        return (short) ((i >> 16) & 255);
    }

    private void initTagInfo() {
        int flagsFromAllowedIfds = getFlagsFromAllowedIfds(new int[]{0, 1}) << 24;
        int i = flagsFromAllowedIfds | 131072;
        int i2 = i | 0;
        this.mTagInfo.put(TAG_MAKE, i2);
        int i3 = flagsFromAllowedIfds | 262144;
        int i4 = i3 | 1;
        this.mTagInfo.put(TAG_IMAGE_WIDTH, i4);
        this.mTagInfo.put(TAG_IMAGE_LENGTH, i4);
        int i5 = flagsFromAllowedIfds | 196608;
        this.mTagInfo.put(TAG_BITS_PER_SAMPLE, i5 | 3);
        int i6 = i5 | 1;
        this.mTagInfo.put(TAG_COMPRESSION, i6);
        this.mTagInfo.put(TAG_PHOTOMETRIC_INTERPRETATION, i6);
        this.mTagInfo.put(TAG_ORIENTATION, i6);
        this.mTagInfo.put(TAG_SAMPLES_PER_PIXEL, i6);
        this.mTagInfo.put(TAG_PLANAR_CONFIGURATION, i6);
        this.mTagInfo.put(TAG_Y_CB_CR_SUB_SAMPLING, i5 | 2);
        this.mTagInfo.put(TAG_Y_CB_CR_POSITIONING, i6);
        int i7 = flagsFromAllowedIfds | 327680;
        int i8 = i7 | 1;
        this.mTagInfo.put(TAG_X_RESOLUTION, i8);
        this.mTagInfo.put(TAG_Y_RESOLUTION, i8);
        this.mTagInfo.put(TAG_RESOLUTION_UNIT, i6);
        int i9 = i3 | 0;
        this.mTagInfo.put(TAG_STRIP_OFFSETS, i9);
        this.mTagInfo.put(TAG_ROWS_PER_STRIP, i4);
        this.mTagInfo.put(TAG_STRIP_BYTE_COUNTS, i9);
        this.mTagInfo.put(TAG_TRANSFER_FUNCTION, i5 | 768);
        this.mTagInfo.put(TAG_WHITE_POINT, i7 | 2);
        int i10 = i7 | 6;
        this.mTagInfo.put(TAG_PRIMARY_CHROMATICITIES, i10);
        this.mTagInfo.put(TAG_Y_CB_CR_COEFFICIENTS, i7 | 3);
        this.mTagInfo.put(TAG_REFERENCE_BLACK_WHITE, i10);
        this.mTagInfo.put(TAG_DATE_TIME, i | 20);
        this.mTagInfo.put(TAG_IMAGE_DESCRIPTION, i2);
        this.mTagInfo.put(TAG_MAKE, i2);
        this.mTagInfo.put(TAG_MODEL, i2);
        this.mTagInfo.put(TAG_SOFTWARE, i2);
        this.mTagInfo.put(TAG_ARTIST, i2);
        this.mTagInfo.put(TAG_COPYRIGHT, i2);
        this.mTagInfo.put(TAG_EXIF_IFD, i4);
        this.mTagInfo.put(TAG_GPS_IFD, i4);
        int flagsFromAllowedIfds2 = (getFlagsFromAllowedIfds(new int[]{1}) << 24) | 262144 | 1;
        this.mTagInfo.put(TAG_JPEG_INTERCHANGE_FORMAT, flagsFromAllowedIfds2);
        this.mTagInfo.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, flagsFromAllowedIfds2);
        int flagsFromAllowedIfds3 = getFlagsFromAllowedIfds(new int[]{2}) << 24;
        int i11 = flagsFromAllowedIfds3 | 458752;
        int i12 = i11 | 4;
        this.mTagInfo.put(TAG_EXIF_VERSION, i12);
        this.mTagInfo.put(TAG_FLASHPIX_VERSION, i12);
        int i13 = flagsFromAllowedIfds3 | 196608;
        int i14 = i13 | 1;
        this.mTagInfo.put(TAG_COLOR_SPACE, i14);
        this.mTagInfo.put(TAG_COMPONENTS_CONFIGURATION, i12);
        int i15 = flagsFromAllowedIfds3 | 327680 | 1;
        this.mTagInfo.put(TAG_COMPRESSED_BITS_PER_PIXEL, i15);
        int i16 = 262144 | flagsFromAllowedIfds3 | 1;
        this.mTagInfo.put(TAG_PIXEL_X_DIMENSION, i16);
        this.mTagInfo.put(TAG_PIXEL_Y_DIMENSION, i16);
        int i17 = i11 | 0;
        this.mTagInfo.put(TAG_MAKER_NOTE, i17);
        this.mTagInfo.put(TAG_USER_COMMENT, i17);
        int i18 = flagsFromAllowedIfds3 | 131072;
        this.mTagInfo.put(TAG_RELATED_SOUND_FILE, i18 | 13);
        int i19 = i18 | 20;
        this.mTagInfo.put(TAG_DATE_TIME_ORIGINAL, i19);
        this.mTagInfo.put(TAG_DATE_TIME_DIGITIZED, i19);
        int i20 = i18 | 0;
        this.mTagInfo.put(TAG_SUB_SEC_TIME, i20);
        this.mTagInfo.put(TAG_SUB_SEC_TIME_ORIGINAL, i20);
        this.mTagInfo.put(TAG_SUB_SEC_TIME_DIGITIZED, i20);
        this.mTagInfo.put(TAG_IMAGE_UNIQUE_ID, i18 | 33);
        this.mTagInfo.put(TAG_EXPOSURE_TIME, i15);
        this.mTagInfo.put(TAG_F_NUMBER, i15);
        this.mTagInfo.put(TAG_EXPOSURE_PROGRAM, i14);
        this.mTagInfo.put(TAG_SPECTRAL_SENSITIVITY, i20);
        int i21 = i13 | 0;
        this.mTagInfo.put(TAG_ISO_SPEED_RATINGS, i21);
        this.mTagInfo.put(TAG_OECF, i17);
        int i22 = flagsFromAllowedIfds3 | 655360 | 1;
        this.mTagInfo.put(TAG_SHUTTER_SPEED_VALUE, i22);
        this.mTagInfo.put(TAG_APERTURE_VALUE, i15);
        this.mTagInfo.put(TAG_BRIGHTNESS_VALUE, i22);
        this.mTagInfo.put(TAG_EXPOSURE_BIAS_VALUE, i22);
        this.mTagInfo.put(TAG_MAX_APERTURE_VALUE, i15);
        this.mTagInfo.put(TAG_SUBJECT_DISTANCE, i15);
        this.mTagInfo.put(TAG_METERING_MODE, i14);
        this.mTagInfo.put(TAG_LIGHT_SOURCE, i14);
        this.mTagInfo.put(TAG_FLASH, i14);
        this.mTagInfo.put(TAG_FOCAL_LENGTH, i15);
        this.mTagInfo.put(TAG_SUBJECT_AREA, i21);
        this.mTagInfo.put(TAG_FLASH_ENERGY, i15);
        this.mTagInfo.put(TAG_SPATIAL_FREQUENCY_RESPONSE, i17);
        this.mTagInfo.put(TAG_FOCAL_PLANE_X_RESOLUTION, i15);
        this.mTagInfo.put(TAG_FOCAL_PLANE_Y_RESOLUTION, i15);
        this.mTagInfo.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, i14);
        this.mTagInfo.put(TAG_SUBJECT_LOCATION, 2 | i13);
        this.mTagInfo.put(TAG_EXPOSURE_INDEX, i15);
        this.mTagInfo.put(TAG_SENSING_METHOD, i14);
        int i23 = i11 | 1;
        this.mTagInfo.put(TAG_FILE_SOURCE, i23);
        this.mTagInfo.put(TAG_SCENE_TYPE, i23);
        this.mTagInfo.put(TAG_CFA_PATTERN, i17);
        this.mTagInfo.put(TAG_CUSTOM_RENDERED, i14);
        this.mTagInfo.put(TAG_EXPOSURE_MODE, i14);
        this.mTagInfo.put(TAG_WHITE_BALANCE, i14);
        this.mTagInfo.put(TAG_DIGITAL_ZOOM_RATIO, i15);
        this.mTagInfo.put(TAG_FOCAL_LENGTH_IN_35_MM_FILE, i14);
        this.mTagInfo.put(TAG_SCENE_CAPTURE_TYPE, i14);
        this.mTagInfo.put(TAG_GAIN_CONTROL, i15);
        this.mTagInfo.put(TAG_CONTRAST, i14);
        this.mTagInfo.put(TAG_SATURATION, i14);
        this.mTagInfo.put(TAG_SHARPNESS, i14);
        this.mTagInfo.put(TAG_DEVICE_SETTING_DESCRIPTION, i17);
        this.mTagInfo.put(TAG_SUBJECT_DISTANCE_RANGE, i14);
        this.mTagInfo.put(TAG_INTEROPERABILITY_IFD, i16);
        this.mTagInfo.put(TAG_PARALLEL_PROCESS_COMMENT, i20);
        this.mTagInfo.put(TAG_ALGORITHM_COMMENT, i20);
        this.mTagInfo.put(TAG_DEPTH_MAP_BLUR_LEVEL, i14);
        this.mTagInfo.put(TAG_DEPTH_MAP_FOCUS_POINT, i20);
        int i24 = flagsFromAllowedIfds3 | 65536;
        int i25 = i24 | 0;
        this.mTagInfo.put(TAG_DEPTH_MAP_DULCAMERA_WATER_MARK, i25);
        this.mTagInfo.put(TAG_DEPTH_MAP_TIME_WATER_MARK, i25);
        this.mTagInfo.put(TAG_PORTRAIT_LIGHTING_PATTERN, i14);
        this.mTagInfo.put(TAG_AI_TYPE, i14);
        this.mTagInfo.put(TAG_FRONT_MIRROR, i14);
        int i26 = i24 | 1;
        this.mTagInfo.put(TAG_XIAOMI_LIVESHOT_PHOTO, i26);
        this.mTagInfo.put(TAG_XIAOMI_DEPTHMAP_VERSION, i14);
        this.mTagInfo.put(TAG_XIAOMI_DOC_PHOTO, i26);
        this.mTagInfo.put(TAG_XIAOMI_COMMENT, i20);
        this.mTagInfo.put(TAG_XIAOMI_PRODUCT, i20);
        this.mTagInfo.put(TAG_XIAOMI_ALGO_COMMENT, i25);
        int flagsFromAllowedIfds4 = getFlagsFromAllowedIfds(new int[]{4}) << 24;
        int i27 = 65536 | flagsFromAllowedIfds4;
        this.mTagInfo.put(TAG_GPS_VERSION_ID, i27 | 4);
        int i28 = flagsFromAllowedIfds4 | 131072;
        int i29 = i28 | 2;
        this.mTagInfo.put(TAG_GPS_LATITUDE_REF, i29);
        this.mTagInfo.put(TAG_GPS_LONGITUDE_REF, i29);
        int i30 = 327680 | flagsFromAllowedIfds4;
        int i31 = i30 | 3;
        this.mTagInfo.put(TAG_GPS_LATITUDE, i31);
        this.mTagInfo.put(TAG_GPS_LONGITUDE, i31);
        this.mTagInfo.put(TAG_GPS_ALTITUDE_REF, i27 | 1);
        int i32 = i30 | 1;
        this.mTagInfo.put(TAG_GPS_ALTITUDE, i32);
        this.mTagInfo.put(TAG_GPS_TIME_STAMP, i31);
        int i33 = i28 | 0;
        this.mTagInfo.put(TAG_GPS_SATTELLITES, i33);
        this.mTagInfo.put(TAG_GPS_STATUS, i29);
        this.mTagInfo.put(TAG_GPS_MEASURE_MODE, i29);
        this.mTagInfo.put(TAG_GPS_DOP, i32);
        this.mTagInfo.put(TAG_GPS_SPEED_REF, i29);
        this.mTagInfo.put(TAG_GPS_SPEED, i32);
        this.mTagInfo.put(TAG_GPS_TRACK_REF, i29);
        this.mTagInfo.put(TAG_GPS_TRACK, i32);
        this.mTagInfo.put(TAG_GPS_IMG_DIRECTION_REF, i29);
        this.mTagInfo.put(TAG_GPS_IMG_DIRECTION, i32);
        this.mTagInfo.put(TAG_GPS_MAP_DATUM, i33);
        this.mTagInfo.put(TAG_GPS_DEST_LATITUDE_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_LATITUDE, i32);
        this.mTagInfo.put(TAG_GPS_DEST_BEARING_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_BEARING, i32);
        this.mTagInfo.put(TAG_GPS_DEST_DISTANCE_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_DISTANCE, i32);
        int i34 = 458752 | flagsFromAllowedIfds4 | 0;
        this.mTagInfo.put(TAG_GPS_PROCESSING_METHOD, i34);
        this.mTagInfo.put(TAG_GPS_AREA_INFORMATION, i34);
        this.mTagInfo.put(TAG_GPS_DATE_STAMP, i28 | 11);
        this.mTagInfo.put(TAG_GPS_DIFFERENTIAL, flagsFromAllowedIfds4 | 196608 | 11);
        this.mTagInfo.put(TAG_INTEROPERABILITY_INDEX, (getFlagsFromAllowedIfds(new int[]{3}) << 24) | 131072 | 0);
    }

    protected static boolean isIfdAllowed(int i, int i2) {
        int[] ifds = IfdData.getIfds();
        int allowedIfdFlagsFromInfo = getAllowedIfdFlagsFromInfo(i);
        int i3 = 0;
        while (i3 < ifds.length) {
            if (i2 == ifds[i3] && ((allowedIfdFlagsFromInfo >> i3) & 1) == 1) {
                return true;
            }
            i3++;
        }
        return false;
    }

    protected static boolean isOffsetTag(short s) {
        return sOffsetTags.contains(Short.valueOf(s));
    }

    public static byte[] removeParallelProcessComment(byte[] bArr) {
        Log.i(TAG, "algo exif: removeParallel");
        long currentTimeMillis = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(bArr);
            exifInterface.removeParallelProcessComment();
            exifInterface.writeExif(bArr, (OutputStream) byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            bArr = byteArray;
        } catch (IOException e) {
            Log.e(ExifInterface.class.getName(), e.getMessage(), (Throwable) e);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("removeParallelProcessComment cost=");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.v(str, sb.toString());
        return bArr;
    }

    public static Rational[] toExifLatLong(double d) {
        double abs = Math.abs(d);
        int i = (int) abs;
        double d2 = (abs - ((double) i)) * 60.0d;
        int i2 = (int) d2;
        return new Rational[]{new Rational((long) i, 1), new Rational((long) i2, 1), new Rational((long) ((int) ((d2 - ((double) i2)) * 6000.0d)), 100)};
    }

    public boolean addAiType(int i) {
        return addExifTag(TAG_AI_TYPE, Integer.valueOf(i));
    }

    public boolean addAlgorithmComment(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addAlgorithmComment: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        return addExifTag(TAG_ALGORITHM_COMMENT, str);
    }

    public boolean addDateTimeStampTag(int i, long j, TimeZone timeZone) {
        if (i != TAG_DATE_TIME && i != TAG_DATE_TIME_DIGITIZED && i != TAG_DATE_TIME_ORIGINAL) {
            return false;
        }
        this.mDateTimeStampFormat.setTimeZone(timeZone);
        ExifTag buildTag = buildTag(i, this.mDateTimeStampFormat.format(Long.valueOf(j)));
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public boolean addDepthMapBlurLevel(int i) {
        return addExifTag(TAG_DEPTH_MAP_BLUR_LEVEL, Integer.valueOf(i));
    }

    public boolean addDepthMapFocusPoint(Point point) {
        return addExifTag(TAG_DEPTH_MAP_FOCUS_POINT, point.toString());
    }

    public boolean addDulCameraWaterMark(byte[] bArr) {
        return addExifTag(TAG_DEPTH_MAP_DULCAMERA_WATER_MARK, bArr);
    }

    public boolean addFileTypeLiveShot(boolean z) {
        return addExifTag(TAG_XIAOMI_LIVESHOT_PHOTO, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public boolean addFrontMirror(int i) {
        return addExifTag(TAG_FRONT_MIRROR, Integer.valueOf(i));
    }

    public boolean addGpsDateTimeStampTag(long j) {
        ExifTag buildTag = buildTag(TAG_GPS_DATE_STAMP, this.mGPSDateStampFormat.format(Long.valueOf(j)));
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        this.mGPSTimeStampCalendar.setTimeInMillis(j);
        ExifTag buildTag2 = buildTag(TAG_GPS_TIME_STAMP, new Rational[]{new Rational((long) this.mGPSTimeStampCalendar.get(11), 1), new Rational((long) this.mGPSTimeStampCalendar.get(12), 1), new Rational((long) this.mGPSTimeStampCalendar.get(13), 1)});
        if (buildTag2 == null) {
            return false;
        }
        setTag(buildTag2);
        return true;
    }

    public boolean addGpsTags(double d, double d2) {
        ExifTag buildTag = buildTag(TAG_GPS_LATITUDE, toExifLatLong(d));
        ExifTag buildTag2 = buildTag(TAG_GPS_LONGITUDE, toExifLatLong(d2));
        ExifTag buildTag3 = buildTag(TAG_GPS_LATITUDE_REF, d >= 0.0d ? "N" : "S");
        ExifTag buildTag4 = buildTag(TAG_GPS_LONGITUDE_REF, d2 >= 0.0d ? "E" : "W");
        if (buildTag == null || buildTag2 == null || buildTag3 == null || buildTag4 == null) {
            return false;
        }
        setTag(buildTag);
        setTag(buildTag2);
        setTag(buildTag3);
        setTag(buildTag4);
        return true;
    }

    public boolean addParallelProcessComment(String str, int i, int i2, int i3) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("algo exif: addParallel ");
        sb.append(str);
        Log.i(str2, sb.toString());
        return addExifTag(TAG_PARALLEL_PROCESS_COMMENT, str) && addExifTag(TAG_ORIENTATION, Short.valueOf(getExifOrientationValue(i))) && addExifTag(TAG_IMAGE_WIDTH, Integer.valueOf(i2)) && addExifTag(TAG_IMAGE_LENGTH, Integer.valueOf(i3));
    }

    public boolean addPortraitLighting(int i) {
        return addExifTag(TAG_PORTRAIT_LIGHTING_PATTERN, Integer.valueOf(i));
    }

    public boolean addSubTagSecTime(int i, long j, TimeZone timeZone) {
        if (i != TAG_SUB_SEC_TIME && i != TAG_SUB_SEC_TIME_DIGITIZED && i != TAG_SUB_SEC_TIME_ORIGINAL) {
            return false;
        }
        this.mDateTimeStampFormat.setTimeZone(timeZone);
        try {
            ExifTag buildTag = buildTag(i, String.valueOf(j - this.mDateTimeStampFormat.parse(this.mDateTimeStampFormat.format(Long.valueOf(j))).getTime()));
            if (buildTag == null) {
                return false;
            }
            setTag(buildTag);
            return true;
        } catch (ParseException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("addSubTagSecTime: occurs ParseException: ");
            sb.append(e.getLocalizedMessage());
            Log.d(str, sb.toString());
            return false;
        }
    }

    public boolean addTimeWaterMark(byte[] bArr) {
        return addExifTag(TAG_DEPTH_MAP_TIME_WATER_MARK, bArr);
    }

    public boolean addXiaomiComment(String str) {
        ExifTag buildTag = buildTag(TAG_XIAOMI_COMMENT, str);
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public boolean addXiaomiDepthmapVersion(int i) {
        return addExifTag(TAG_XIAOMI_DEPTHMAP_VERSION, Integer.valueOf(i));
    }

    public boolean addXiaomiDocPhotoVersion(byte b) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addXiaomiDocPhotoVersion: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return addExifTag(TAG_XIAOMI_DOC_PHOTO, Byte.valueOf(b));
    }

    public boolean addXiaomiProduct(String str) {
        ExifTag buildTag = buildTag(TAG_XIAOMI_PRODUCT, str);
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public boolean addXpComment(byte[] bArr) {
        return addExifTag(TAG_XIAOMI_ALGO_COMMENT, bArr);
    }

    public ExifTag buildTag(int i, int i2, Object obj) {
        int i3 = getTagInfo().get(i);
        if (i3 == 0 || obj == null) {
            return null;
        }
        short typeFromInfo = getTypeFromInfo(i3);
        int componentCountFromInfo = getComponentCountFromInfo(i3);
        boolean z = componentCountFromInfo != 0;
        if (!isIfdAllowed(i3, i2)) {
            return null;
        }
        ExifTag exifTag = new ExifTag(getTrueTagKey(i), typeFromInfo, componentCountFromInfo, i2, z);
        if (!exifTag.setValue(obj)) {
            return null;
        }
        return exifTag;
    }

    public ExifTag buildTag(int i, Object obj) {
        return buildTag(i, getTrueIfd(i), obj);
    }

    /* access modifiers changed from: protected */
    public ExifTag buildUninitializedTag(int i) {
        int i2 = getTagInfo().get(i);
        if (i2 == 0) {
            return null;
        }
        short typeFromInfo = getTypeFromInfo(i2);
        int componentCountFromInfo = getComponentCountFromInfo(i2);
        ExifTag exifTag = new ExifTag(getTrueTagKey(i), typeFromInfo, componentCountFromInfo, getTrueIfd(i), componentCountFromInfo != 0);
        return exifTag;
    }

    public void clearExif() {
        this.mData = new ExifData(DEFAULT_BYTE_ORDER);
    }

    public void deleteTag(int i) {
        deleteTag(i, getDefinedTagDefaultIfd(i));
    }

    public void deleteTag(int i, int i2) {
        this.mData.removeTag(getTrueTagKey(i), i2);
    }

    public int getActualTagCount(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return 0;
        }
        return tag.getComponentCount();
    }

    public List getAllTags() {
        return this.mData.getAllTags();
    }

    public int getDefinedTagCount(int i) {
        int i2 = getTagInfo().get(i);
        if (i2 == 0) {
            return 0;
        }
        return getComponentCountFromInfo(i2);
    }

    public int getDefinedTagDefaultIfd(int i) {
        if (getTagInfo().get(i) == 0) {
            return -1;
        }
        return getTrueIfd(i);
    }

    public short getDefinedTagType(int i) {
        int i2 = getTagInfo().get(i);
        if (i2 == 0) {
            return -1;
        }
        return getTypeFromInfo(i2);
    }

    public OutputStream getExifWriterStream(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        } else if (this.mMimeType == 12) {
            Log.d(TAG, "HEIC does not support exif mutation");
            return outputStream;
        } else {
            ExifOutputStream exifOutputStream = new ExifOutputStream(outputStream, this);
            exifOutputStream.setExifData(this.mData);
            return exifOutputStream;
        }
    }

    public OutputStream getExifWriterStream(String str) {
        if (str != null) {
            try {
                return getExifWriterStream((OutputStream) new FileOutputStream(str));
            } catch (FileNotFoundException e) {
                closeSilently(null);
                throw e;
            }
        } else {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
    }

    public double[] getLatLongAsDoubles() {
        Rational[] tagRationalValues = getTagRationalValues(TAG_GPS_LATITUDE);
        String tagStringValue = getTagStringValue(TAG_GPS_LATITUDE_REF);
        Rational[] tagRationalValues2 = getTagRationalValues(TAG_GPS_LONGITUDE);
        String tagStringValue2 = getTagStringValue(TAG_GPS_LONGITUDE_REF);
        if (tagRationalValues == null || tagRationalValues2 == null || tagStringValue == null || tagStringValue2 == null || tagRationalValues.length < 3 || tagRationalValues2.length < 3) {
            return null;
        }
        return new double[]{convertLatOrLongToDouble(tagRationalValues, tagStringValue), convertLatOrLongToDouble(tagRationalValues2, tagStringValue2)};
    }

    public int getMimeType() {
        if (this.mData != null) {
            return this.mMimeType;
        }
        throw new IllegalStateException("please call #readExif first");
    }

    public ExifTag getTag(int i) {
        return getTag(i, getDefinedTagDefaultIfd(i));
    }

    public ExifTag getTag(int i, int i2) {
        if (!ExifTag.isValidIfd(i2)) {
            return null;
        }
        return this.mData.getTag(getTrueTagKey(i), i2);
    }

    public Byte getTagByteValue(int i) {
        return getTagByteValue(i, getDefinedTagDefaultIfd(i));
    }

    public Byte getTagByteValue(int i, int i2) {
        byte[] tagByteValues = getTagByteValues(i, i2);
        if (tagByteValues == null || tagByteValues.length <= 0) {
            return null;
        }
        return Byte.valueOf(tagByteValues[0]);
    }

    public byte[] getTagByteValues(int i) {
        return getTagByteValues(i, getDefinedTagDefaultIfd(i));
    }

    public byte[] getTagByteValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsBytes();
    }

    /* access modifiers changed from: protected */
    public int getTagDefinition(short s, int i) {
        return getTagInfo().get(defineTag(i, s));
    }

    /* access modifiers changed from: protected */
    public int getTagDefinitionForTag(ExifTag exifTag) {
        return getTagDefinitionForTag(exifTag.getTagId(), exifTag.getDataType(), exifTag.getComponentCount(), exifTag.getIfd());
    }

    /* access modifiers changed from: protected */
    public int getTagDefinitionForTag(short s, short s2, int i, int i2) {
        int i3;
        boolean z;
        int[] tagDefinitionsForTagId = getTagDefinitionsForTagId(s);
        int i4 = -1;
        if (tagDefinitionsForTagId == null) {
            return -1;
        }
        SparseIntArray tagInfo = getTagInfo();
        int length = tagDefinitionsForTagId.length;
        int i5 = 0;
        while (true) {
            if (i5 >= length) {
                break;
            }
            i3 = tagDefinitionsForTagId[i5];
            int i6 = tagInfo.get(i3);
            short typeFromInfo = getTypeFromInfo(i6);
            int componentCountFromInfo = getComponentCountFromInfo(i6);
            int[] allowedIfdsFromInfo = getAllowedIfdsFromInfo(i6);
            int length2 = allowedIfdsFromInfo.length;
            int i7 = 0;
            while (true) {
                if (i7 >= length2) {
                    z = false;
                    break;
                } else if (allowedIfdsFromInfo[i7] == i2) {
                    z = true;
                    break;
                } else {
                    i7++;
                }
            }
            if (!(z && s2 == typeFromInfo && (i == componentCountFromInfo || componentCountFromInfo == 0))) {
                i5++;
            }
        }
        i4 = i3;
        return i4;
    }

    /* access modifiers changed from: protected */
    public int[] getTagDefinitionsForTagId(short s) {
        int[] ifds = IfdData.getIfds();
        int[] iArr = new int[ifds.length];
        SparseIntArray tagInfo = getTagInfo();
        int i = 0;
        for (int defineTag : ifds) {
            int defineTag2 = defineTag(defineTag, s);
            if (tagInfo.get(defineTag2) != 0) {
                int i2 = i + 1;
                iArr[i] = defineTag2;
                i = i2;
            }
        }
        if (i == 0) {
            return null;
        }
        return Arrays.copyOfRange(iArr, 0, i);
    }

    /* access modifiers changed from: protected */
    public SparseIntArray getTagInfo() {
        if (this.mTagInfo == null) {
            this.mTagInfo = new SparseIntArray();
            initTagInfo();
        }
        return this.mTagInfo;
    }

    public Integer getTagIntValue(int i) {
        return getTagIntValue(i, getDefinedTagDefaultIfd(i));
    }

    public Integer getTagIntValue(int i, int i2) {
        int[] tagIntValues = getTagIntValues(i, i2);
        if (tagIntValues == null || tagIntValues.length <= 0) {
            return null;
        }
        return Integer.valueOf(tagIntValues[0]);
    }

    public int[] getTagIntValues(int i) {
        return getTagIntValues(i, getDefinedTagDefaultIfd(i));
    }

    public int[] getTagIntValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsInts();
    }

    public Long getTagLongValue(int i) {
        return getTagLongValue(i, getDefinedTagDefaultIfd(i));
    }

    public Long getTagLongValue(int i, int i2) {
        long[] tagLongValues = getTagLongValues(i, i2);
        if (tagLongValues == null || tagLongValues.length <= 0) {
            return null;
        }
        return Long.valueOf(tagLongValues[0]);
    }

    public long[] getTagLongValues(int i) {
        return getTagLongValues(i, getDefinedTagDefaultIfd(i));
    }

    public long[] getTagLongValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsLongs();
    }

    public Rational getTagRationalValue(int i) {
        return getTagRationalValue(i, getDefinedTagDefaultIfd(i));
    }

    public Rational getTagRationalValue(int i, int i2) {
        Rational[] tagRationalValues = getTagRationalValues(i, i2);
        if (tagRationalValues == null || tagRationalValues.length == 0) {
            return null;
        }
        return new Rational(tagRationalValues[0]);
    }

    public Rational[] getTagRationalValues(int i) {
        return getTagRationalValues(i, getDefinedTagDefaultIfd(i));
    }

    public Rational[] getTagRationalValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsRationals();
    }

    public String getTagStringValue(int i) {
        return getTagStringValue(i, getDefinedTagDefaultIfd(i));
    }

    public String getTagStringValue(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsString();
    }

    public Object getTagValue(int i) {
        return getTagValue(i, getDefinedTagDefaultIfd(i));
    }

    public Object getTagValue(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValue();
    }

    public List getTagsForIfdId(int i) {
        return this.mData.getAllTagsForIfd(i);
    }

    public List getTagsForTagId(short s) {
        return this.mData.getAllTagsForTagId(s);
    }

    public byte[] getThumbnail() {
        return this.mData.getCompressedThumbnail();
    }

    public Bitmap getThumbnailBitmap() {
        boolean hasCompressedThumbnail = this.mData.hasCompressedThumbnail();
        ExifData exifData = this.mData;
        if (hasCompressedThumbnail) {
            byte[] compressedThumbnail = exifData.getCompressedThumbnail();
            return BitmapFactory.decodeByteArray(compressedThumbnail, 0, compressedThumbnail.length);
        }
        exifData.hasUncompressedStrip();
        return null;
    }

    public byte[] getThumbnailBytes() {
        boolean hasCompressedThumbnail = this.mData.hasCompressedThumbnail();
        ExifData exifData = this.mData;
        if (hasCompressedThumbnail) {
            return exifData.getCompressedThumbnail();
        }
        exifData.hasUncompressedStrip();
        return null;
    }

    public String getUserComment() {
        return this.mData.getUserComment();
    }

    public String getXiaomiComment() {
        return this.mData.getXiaomiComment();
    }

    public boolean hasThumbnail() {
        return this.mData.hasCompressedThumbnail();
    }

    public boolean isTagCountDefined(int i) {
        int i2 = getTagInfo().get(i);
        boolean z = false;
        if (i2 == 0) {
            return false;
        }
        if (getComponentCountFromInfo(i2) != 0) {
            z = true;
        }
        return z;
    }

    public boolean isThumbnailCompressed() {
        return this.mData.hasCompressedThumbnail();
    }

    public void readExif(InputStream inputStream) {
        ExifData exifData;
        if (inputStream != null) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 5000);
                this.mMimeType = ExifHelper.getMimeType(bufferedInputStream);
                if (this.mMimeType == 12) {
                    exifData = new ExifReader(this).read(new ByteArrayInputStream(ExifHelper.extractExifFromHeif(new ByteOrderedDataInputStream((InputStream) bufferedInputStream))));
                } else {
                    exifData = new ExifReader(this).read(bufferedInputStream);
                }
                this.mData = exifData;
            } catch (ExifInvalidFormatException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid exif format : ");
                sb.append(e);
                throw new IOException(sb.toString());
            }
        } else {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001e, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readExif(String str) {
        if (str != null) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(str));
                readExif((InputStream) bufferedInputStream);
                bufferedInputStream.close();
            } catch (IOException e) {
                throw e;
            } catch (Throwable th) {
                r2.addSuppressed(th);
            }
        } else {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
    }

    public void readExif(byte[] bArr) {
        readExif((InputStream) new ByteArrayInputStream(bArr));
    }

    public void removeCompressedThumbnail() {
        this.mData.setCompressedThumbnail(null);
    }

    public void removeParallelProcessComment() {
        deleteTag(TAG_PARALLEL_PROCESS_COMMENT);
    }

    public void removeTagDefinition(int i) {
        getTagInfo().delete(i);
    }

    public void resetTagDefinitions() {
        this.mTagInfo = null;
    }

    public boolean setCompressedThumbnail(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!bitmap.compress(CompressFormat.JPEG, 90, byteArrayOutputStream)) {
            return false;
        }
        return setCompressedThumbnail(byteArrayOutputStream.toByteArray());
    }

    public boolean setCompressedThumbnail(byte[] bArr) {
        this.mData.clearThumbnailAndStrips();
        this.mData.setCompressedThumbnail(bArr);
        return true;
    }

    public void setExif(Collection collection) {
        clearExif();
        setTags(collection);
    }

    public ExifTag setTag(ExifTag exifTag) {
        return this.mData.addTag(exifTag);
    }

    public int setTagDefinition(short s, int i, short s2, short s3, int[] iArr) {
        if (sBannedDefines.contains(Short.valueOf(s)) || !ExifTag.isValidType(s2) || !ExifTag.isValidIfd(i)) {
            return -1;
        }
        int defineTag = defineTag(i, s);
        if (defineTag == -1) {
            return -1;
        }
        int[] tagDefinitionsForTagId = getTagDefinitionsForTagId(s);
        SparseIntArray tagInfo = getTagInfo();
        boolean z = false;
        for (int i2 : iArr) {
            if (i == i2) {
                z = true;
            }
            if (!ExifTag.isValidIfd(i2)) {
                return -1;
            }
        }
        if (!z) {
            return -1;
        }
        int flagsFromAllowedIfds = getFlagsFromAllowedIfds(iArr);
        if (tagDefinitionsForTagId != null) {
            for (int i3 : tagDefinitionsForTagId) {
                if ((getAllowedIfdFlagsFromInfo(tagInfo.get(i3)) & flagsFromAllowedIfds) != 0) {
                    return -1;
                }
            }
        }
        getTagInfo().put(defineTag, (flagsFromAllowedIfds << 24) | (s2 << 16) | s3);
        return defineTag;
    }

    public boolean setTagValue(int i, int i2, Object obj) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return false;
        }
        return tag.setValue(obj);
    }

    public boolean setTagValue(int i, Object obj) {
        return setTagValue(i, getDefinedTagDefaultIfd(i), obj);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.android.gallery3d.exif.ExifTag>, for r2v0, types: [java.util.Collection<com.android.gallery3d.exif.ExifTag>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTags(Collection<ExifTag> collection) {
        for (ExifTag tag : collection) {
            setTag(tag);
        }
    }

    public void writeExif(Bitmap bitmap, OutputStream outputStream) {
        if (bitmap == null || outputStream == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream exifWriterStream = getExifWriterStream(outputStream);
        bitmap.compress(CompressFormat.JPEG, 90, exifWriterStream);
        exifWriterStream.flush();
    }

    public void writeExif(Bitmap bitmap, String str) {
        if (bitmap == null || str == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream outputStream = null;
        try {
            outputStream = getExifWriterStream(str);
            bitmap.compress(CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            closeSilently(outputStream);
            throw e;
        }
    }

    public void writeExif(InputStream inputStream, OutputStream outputStream) {
        if (inputStream == null || outputStream == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream exifWriterStream = getExifWriterStream(outputStream);
        doExifStreamIO(inputStream, exifWriterStream);
        exifWriterStream.flush();
    }

    public void writeExif(InputStream inputStream, String str) {
        if (inputStream == null || str == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream outputStream = null;
        try {
            outputStream = getExifWriterStream(str);
            doExifStreamIO(inputStream, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            closeSilently(outputStream);
            throw e;
        }
    }

    public void writeExif(String str, String str2) {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(str);
            try {
                writeExif((InputStream) fileInputStream2, str2);
                fileInputStream2.close();
            } catch (IOException e) {
                e = e;
                fileInputStream = fileInputStream2;
                closeSilently(fileInputStream);
                throw e;
            }
        } catch (IOException e2) {
            e = e2;
            closeSilently(fileInputStream);
            throw e;
        }
    }

    public void writeExif(byte[] bArr, OutputStream outputStream) {
        if (bArr == null || outputStream == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream exifWriterStream = getExifWriterStream(outputStream);
        exifWriterStream.write(bArr, 0, bArr.length);
        exifWriterStream.flush();
    }

    public void writeExif(byte[] bArr, String str) {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_STRING);
        }
        OutputStream outputStream = null;
        try {
            outputStream = getExifWriterStream(str);
            outputStream.write(bArr, 0, bArr.length);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            closeSilently(outputStream);
            throw e;
        }
    }
}
