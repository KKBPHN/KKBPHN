package androidx.exifinterface.media;

import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build.VERSION;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.android.camera2.CameraCapabilities;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExifInterface {
    public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
    public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
    static final Charset ASCII = Charset.forName("US-ASCII");
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = {4};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = {8};
    public static final int[] BITS_PER_SAMPLE_RGB = {8, 8, 8};
    static final short BYTE_ALIGN_II = 18761;
    static final short BYTE_ALIGN_MM = 19789;
    public static final int COLOR_SPACE_S_RGB = 1;
    public static final int COLOR_SPACE_UNCALIBRATED = 65535;
    public static final short CONTRAST_HARD = 2;
    public static final short CONTRAST_NORMAL = 0;
    public static final short CONTRAST_SOFT = 1;
    public static final int DATA_DEFLATE_ZIP = 8;
    public static final int DATA_HUFFMAN_COMPRESSED = 2;
    public static final int DATA_JPEG = 6;
    public static final int DATA_JPEG_COMPRESSED = 7;
    public static final int DATA_LOSSY_JPEG = 34892;
    public static final int DATA_PACK_BITS_COMPRESSED = 32773;
    public static final int DATA_UNCOMPRESSED = 1;
    private static final boolean DEBUG = false;
    static final byte[] EXIF_ASCII_PREFIX = {65, 83, 67, 73, 73, 0, 0, 0};
    private static final ExifTag[] EXIF_POINTER_TAGS = {new ExifTag(TAG_SUB_IFD_POINTER, m.cq, 4), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 1), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 1)};
    static final ExifTag[][] EXIF_TAGS;
    public static final short EXPOSURE_MODE_AUTO = 0;
    public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
    public static final short EXPOSURE_MODE_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_ACTION = 6;
    public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
    public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
    public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
    public static final short EXPOSURE_PROGRAM_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_NORMAL = 2;
    public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
    public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
    public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
    public static final short FILE_SOURCE_DSC = 3;
    public static final short FILE_SOURCE_OTHER = 0;
    public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
    public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
    public static final short FLAG_FLASH_FIRED = 1;
    public static final short FLAG_FLASH_MODE_AUTO = 24;
    public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
    public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
    public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
    public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
    public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
    public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
    private static final List FLIPPED_ROTATION_ORDER;
    public static final short FORMAT_CHUNKY = 1;
    public static final short FORMAT_PLANAR = 2;
    public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
    public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
    public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
    public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
    public static final short GAIN_CONTROL_NONE = 0;
    public static final String GPS_DIRECTION_MAGNETIC = "M";
    public static final String GPS_DIRECTION_TRUE = "T";
    public static final String GPS_DISTANCE_KILOMETERS = "K";
    public static final String GPS_DISTANCE_MILES = "M";
    public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
    public static final String GPS_MEASUREMENT_2D = "2";
    public static final String GPS_MEASUREMENT_3D = "3";
    public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
    public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
    public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
    public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
    public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
    public static final String GPS_SPEED_KNOTS = "N";
    public static final String GPS_SPEED_MILES_PER_HOUR = "M";
    private static final byte[] HEIF_BRAND_HEIC = {104, 101, 105, 99};
    private static final byte[] HEIF_BRAND_MIF1 = {109, 105, 102, 49};
    private static final byte[] HEIF_TYPE_FTYP = {102, 116, 121, 112};
    static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final byte[] IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(ASCII);
    private static final ExifTag[] IFD_EXIF_TAGS = {new ExifTag(TAG_EXPOSURE_TIME, 33434, 5), new ExifTag(TAG_F_NUMBER, 33437, 5), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3), new ExifTag(TAG_SPECTRAL_SENSITIVITY, 34852, 2), new ExifTag(TAG_PHOTOGRAPHIC_SENSITIVITY, 34855, 3), new ExifTag(TAG_OECF, 34856, 7), new ExifTag(TAG_EXIF_VERSION, 36864, 2), new ExifTag(TAG_DATETIME_ORIGINAL, CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_SINGLE_BOKEH, 2), new ExifTag(TAG_DATETIME_DIGITIZED, CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_HD, 2), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, 37122, 5), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10), new ExifTag(TAG_APERTURE_VALUE, 37378, 5), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5), new ExifTag(TAG_METERING_MODE, 37383, 3), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3), new ExifTag(TAG_FLASH, 37385, 3), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5), new ExifTag(TAG_SUBJECT_AREA, 37396, 3), new ExifTag(TAG_MAKER_NOTE, 37500, 7), new ExifTag(TAG_USER_COMMENT, 37510, 7), new ExifTag(TAG_SUBSEC_TIME, 37520, 2), new ExifTag(TAG_SUBSEC_TIME_ORIGINAL, 37521, 2), new ExifTag(TAG_SUBSEC_TIME_DIGITIZED, 37522, 2), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7), new ExifTag(TAG_COLOR_SPACE, 40961, 3), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_FLASH_ENERGY, 41483, 5), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5), new ExifTag(TAG_SENSING_METHOD, 41495, 3), new ExifTag(TAG_FILE_SOURCE, 41728, 7), new ExifTag(TAG_SCENE_TYPE, 41729, 7), new ExifTag(TAG_CFA_PATTERN, 41730, 7), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3), new ExifTag(TAG_WHITE_BALANCE, 41987, 3), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3), new ExifTag(TAG_GAIN_CONTROL, 41991, 3), new ExifTag(TAG_CONTRAST, 41992, 3), new ExifTag(TAG_SATURATION, 41993, 3), new ExifTag(TAG_SHARPNESS, 41994, 3), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
    private static final int IFD_FORMAT_BYTE = 1;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    static final String[] IFD_FORMAT_NAMES = {"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS = {new ExifTag(TAG_GPS_VERSION_ID, 0, 1), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2), new ExifTag(TAG_GPS_LATITUDE, 2, 5), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2), new ExifTag(TAG_GPS_LONGITUDE, 4, 5), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1), new ExifTag(TAG_GPS_ALTITUDE, 6, 5), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5), new ExifTag(TAG_GPS_SATELLITES, 8, 2), new ExifTag(TAG_GPS_STATUS, 9, 2), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2), new ExifTag(TAG_GPS_DOP, 11, 5), new ExifTag(TAG_GPS_SPEED_REF, 12, 2), new ExifTag(TAG_GPS_SPEED, 13, 5), new ExifTag(TAG_GPS_TRACK_REF, 14, 2), new ExifTag(TAG_GPS_TRACK, 15, 5), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7), new ExifTag(TAG_GPS_DATESTAMP, 29, 2), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3)};
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = {new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2)};
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = {new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, m.bJ, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_ORIENTATION, m.bL, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, af.bV, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, af.ca, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, af.cm, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, af.ct, 2), new ExifTag(TAG_DATETIME, af.cu, 2), new ExifTag(TAG_ARTIST, m.cd, 2), new ExifTag(TAG_WHITE_POINT, m.cf, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, m.cg, 5), new ExifTag(TAG_SUB_IFD_POINTER, m.cq, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
    private static final ExifTag[] IFD_TIFF_TAGS = {new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, m.bJ, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_ORIENTATION, m.bL, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, af.bV, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, af.ca, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, af.cm, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, af.ct, 2), new ExifTag(TAG_DATETIME, af.cu, 2), new ExifTag(TAG_ARTIST, m.cd, 2), new ExifTag(TAG_WHITE_POINT, m.cf, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, m.cg, 5), new ExifTag(TAG_SUB_IFD_POINTER, m.cq, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_RW2_SENSOR_TOP_BORDER, 4, 4), new ExifTag(TAG_RW2_SENSOR_LEFT_BORDER, 5, 4), new ExifTag(TAG_RW2_SENSOR_BOTTOM_BORDER, 6, 4), new ExifTag(TAG_RW2_SENSOR_RIGHT_BORDER, 7, 4), new ExifTag(TAG_RW2_ISO, 23, 3), new ExifTag(TAG_RW2_JPG_FROM_RAW, 46, 7), new ExifTag(TAG_XMP, 700, 1)};
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    static final int IFD_TYPE_PREVIEW = 5;
    static final int IFD_TYPE_PRIMARY = 0;
    static final int IFD_TYPE_THUMBNAIL = 4;
    private static final int IMAGE_TYPE_ARW = 1;
    private static final int IMAGE_TYPE_CR2 = 2;
    private static final int IMAGE_TYPE_DNG = 3;
    private static final int IMAGE_TYPE_HEIF = 12;
    private static final int IMAGE_TYPE_JPEG = 4;
    private static final int IMAGE_TYPE_NEF = 5;
    private static final int IMAGE_TYPE_NRW = 6;
    private static final int IMAGE_TYPE_ORF = 7;
    private static final int IMAGE_TYPE_PEF = 8;
    private static final int IMAGE_TYPE_RAF = 9;
    private static final int IMAGE_TYPE_RW2 = 10;
    private static final int IMAGE_TYPE_SRW = 11;
    private static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4);
    static final byte[] JPEG_SIGNATURE = {MARKER, MARKER_SOI, MARKER};
    public static final String LATITUDE_NORTH = "N";
    public static final String LATITUDE_SOUTH = "S";
    public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
    public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
    public static final short LIGHT_SOURCE_D50 = 23;
    public static final short LIGHT_SOURCE_D55 = 20;
    public static final short LIGHT_SOURCE_D65 = 21;
    public static final short LIGHT_SOURCE_D75 = 22;
    public static final short LIGHT_SOURCE_DAYLIGHT = 1;
    public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
    public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
    public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
    public static final short LIGHT_SOURCE_FLASH = 4;
    public static final short LIGHT_SOURCE_FLUORESCENT = 2;
    public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
    public static final short LIGHT_SOURCE_OTHER = 255;
    public static final short LIGHT_SOURCE_SHADE = 11;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
    public static final short LIGHT_SOURCE_TUNGSTEN = 3;
    public static final short LIGHT_SOURCE_UNKNOWN = 0;
    public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
    public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
    public static final String LONGITUDE_EAST = "E";
    public static final String LONGITUDE_WEST = "W";
    static final byte MARKER = -1;
    static final byte MARKER_APP1 = -31;
    private static final byte MARKER_COM = -2;
    static final byte MARKER_EOI = -39;
    private static final byte MARKER_SOF0 = -64;
    private static final byte MARKER_SOF1 = -63;
    private static final byte MARKER_SOF10 = -54;
    private static final byte MARKER_SOF11 = -53;
    private static final byte MARKER_SOF13 = -51;
    private static final byte MARKER_SOF14 = -50;
    private static final byte MARKER_SOF15 = -49;
    private static final byte MARKER_SOF2 = -62;
    private static final byte MARKER_SOF3 = -61;
    private static final byte MARKER_SOF5 = -59;
    private static final byte MARKER_SOF6 = -58;
    private static final byte MARKER_SOF7 = -57;
    private static final byte MARKER_SOF9 = -55;
    private static final byte MARKER_SOI = -40;
    private static final byte MARKER_SOS = -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    private static final int METADATA_KEY_EXIF_LENGTH = 34;
    private static final int METADATA_KEY_EXIF_OFFSET = 33;
    public static final short METERING_MODE_AVERAGE = 1;
    public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
    public static final short METERING_MODE_MULTI_SPOT = 4;
    public static final short METERING_MODE_OTHER = 255;
    public static final short METERING_MODE_PARTIAL = 6;
    public static final short METERING_MODE_PATTERN = 5;
    public static final short METERING_MODE_SPOT = 3;
    public static final short METERING_MODE_UNKNOWN = 0;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = {new ExifTag(TAG_ORF_PREVIEW_IMAGE_START, 257, 4), new ExifTag(TAG_ORF_PREVIEW_IMAGE_LENGTH, 258, 4)};
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = {new ExifTag(TAG_ORF_ASPECT_FRAME, 4371, 3)};
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = {79, 76, 89, 77, 80, 0};
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = {new ExifTag(TAG_ORF_THUMBNAIL_IMAGE, 256, 7), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 4), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 4)};
    private static final short ORF_SIGNATURE_1 = 20306;
    private static final short ORF_SIGNATURE_2 = 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS = {new ExifTag(TAG_COLOR_SPACE, 55, 3)};
    public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int RAF_INFO_SIZE = 160;
    private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    public static final int REDUCED_RESOLUTION_IMAGE = 1;
    public static final short RENDERED_PROCESS_CUSTOM = 1;
    public static final short RENDERED_PROCESS_NORMAL = 0;
    public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
    public static final short RESOLUTION_UNIT_INCHES = 2;
    private static final List ROTATION_ORDER;
    private static final short RW2_SIGNATURE = 85;
    public static final short SATURATION_HIGH = 0;
    public static final short SATURATION_LOW = 0;
    public static final short SATURATION_NORMAL = 0;
    public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
    public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
    public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
    public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
    public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
    public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
    public static final short SENSITIVITY_TYPE_REI = 2;
    public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
    public static final short SENSITIVITY_TYPE_SOS = 1;
    public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
    public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
    public static final short SENSOR_TYPE_NOT_DEFINED = 1;
    public static final short SENSOR_TYPE_ONE_CHIP = 2;
    public static final short SENSOR_TYPE_THREE_CHIP = 4;
    public static final short SENSOR_TYPE_TRILINEAR = 7;
    public static final short SENSOR_TYPE_TWO_CHIP = 3;
    public static final short SHARPNESS_HARD = 2;
    public static final short SHARPNESS_NORMAL = 0;
    public static final short SHARPNESS_SOFT = 1;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    static final byte START_CODE = 42;
    public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
    public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
    public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
    public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    @Deprecated
    public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CAMERA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GAMMA = "Gamma";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO_SPEED = "ISOSpeed";
    public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
    public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";
    @Deprecated
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LENS_MAKE = "LensMake";
    public static final String TAG_LENS_MODEL = "LensModel";
    public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
    public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag(TAG_STRIP_OFFSETS, 273, 3);
    public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
    private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_XMP = "Xmp";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    @Deprecated
    public static final int WHITEBALANCE_AUTO = 0;
    @Deprecated
    public static final int WHITEBALANCE_MANUAL = 1;
    public static final short WHITE_BALANCE_AUTO = 0;
    public static final short WHITE_BALANCE_MANUAL = 1;
    public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
    public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
    private static final HashMap sExifPointerTagMap = new HashMap();
    private static final HashMap[] sExifTagMapsForReading;
    private static final HashMap[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{TAG_F_NUMBER, TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP}));
    private AssetInputStream mAssetInputStream;
    private final HashMap[] mAttributes;
    private Set mAttributesOffsets;
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    private String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private boolean mModified;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private FileDescriptor mSeekableFileDescriptor;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;

        public ByteOrderedDataInputStream(InputStream inputStream) {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(inputStream);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }

        public ByteOrderedDataInputStream(byte[] bArr) {
            this((InputStream) new ByteArrayInputStream(bArr));
        }

        public int available() {
            return this.mDataInputStream.available();
        }

        public int getLength() {
            return this.mLength;
        }

        public int peek() {
            return this.mPosition;
        }

        public int read() {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int read(byte[] bArr, int i, int i2) {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        public boolean readBoolean() {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public byte readByte() {
            this.mPosition++;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                if (read >= 0) {
                    return (byte) read;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public char readChar() {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public double readDouble() {
            return Double.longBitsToDouble(readLong());
        }

        public float readFloat() {
            return Float.intBitsToFloat(readInt());
        }

        public void readFully(byte[] bArr) {
            this.mPosition += bArr.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, 0, bArr.length) != bArr.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public void readFully(byte[] bArr, int i, int i2) {
            this.mPosition += i2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, i, i2) != i2) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public int readInt() {
            this.mPosition += 4;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public String readLine() {
            Log.d(ExifInterface.TAG, "Currently unsupported");
            return null;
        }

        public long readLong() {
            this.mPosition += 8;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                int read5 = this.mDataInputStream.read();
                int read6 = this.mDataInputStream.read();
                int read7 = this.mDataInputStream.read();
                int read8 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((long) read8) << 56) + (((long) read7) << 48) + (((long) read6) << 40) + (((long) read5) << 32) + (((long) read4) << 24) + (((long) read3) << 16) + (((long) read2) << 8) + ((long) read);
                    }
                    int i = read2;
                    if (byteOrder == BIG_ENDIAN) {
                        return (((long) read) << 56) + (((long) i) << 48) + (((long) read3) << 40) + (((long) read4) << 32) + (((long) read5) << 24) + (((long) read6) << 16) + (((long) read7) << 8) + ((long) read8);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public short readShort() {
            int i;
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        i = (read2 << 8) + read;
                    } else if (byteOrder == BIG_ENDIAN) {
                        i = (read << 8) + read2;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid byte order: ");
                        sb.append(this.mByteOrder);
                        throw new IOException(sb.toString());
                    }
                    return (short) i;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public String readUTF() {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public int readUnsignedByte() {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public long readUnsignedInt() {
            return ((long) readInt()) & 4294967295L;
        }

        public int readUnsignedShort() {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 8) + read2;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public void seek(long j) {
            int i = this.mPosition;
            if (((long) i) > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                j -= (long) i;
            }
            int i2 = (int) j;
            if (skipBytes(i2) != i2) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public int skipBytes(int i) {
            int min = Math.min(i, this.mLength - this.mPosition);
            int i2 = 0;
            while (i2 < min) {
                i2 += this.mDataInputStream.skipBytes(min - i2);
            }
            this.mPosition += i2;
            return i2;
        }
    }

    class ByteOrderedDataOutputStream extends FilterOutputStream {
        private ByteOrder mByteOrder;
        private final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void write(byte[] bArr) {
            this.mOutputStream.write(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            this.mOutputStream.write(bArr, i, i2);
        }

        public void writeByte(int i) {
            this.mOutputStream.write(i);
        }

        public void writeInt(int i) {
            OutputStream outputStream;
            int i2;
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((i >>> 0) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                outputStream = this.mOutputStream;
                i2 = i >>> 24;
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((i >>> 24) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                outputStream = this.mOutputStream;
                i2 = i >>> 0;
            } else {
                return;
            }
            outputStream.write(i2 & 255);
        }

        public void writeShort(short s) {
            OutputStream outputStream;
            int i;
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((s >>> 0) & 255);
                outputStream = this.mOutputStream;
                i = s >>> 8;
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((s >>> 8) & 255);
                outputStream = this.mOutputStream;
                i = s >>> 0;
            } else {
                return;
            }
            outputStream.write(i & 255);
        }

        public void writeUnsignedInt(long j) {
            writeInt((int) j);
        }

        public void writeUnsignedShort(int i) {
            writeShort((short) i);
        }
    }

    class ExifAttribute {
        public static final long BYTES_OFFSET_UNKNOWN = -1;
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        ExifAttribute(int i, int i2, long j, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytesOffset = j;
            this.bytes = bArr;
        }

        ExifAttribute(int i, int i2, byte[] bArr) {
            this(i, i2, -1, bArr);
        }

        public static ExifAttribute createByte(String str) {
            if (str.length() != 1 || str.charAt(0) < '0' || str.charAt(0) > '1') {
                byte[] bytes2 = str.getBytes(ExifInterface.ASCII);
                return new ExifAttribute(1, bytes2.length, bytes2);
            }
            byte[] bArr = {(byte) (str.charAt(0) - '0')};
            return new ExifAttribute(1, bArr.length, bArr);
        }

        public static ExifAttribute createDouble(double d, ByteOrder byteOrder) {
            return createDouble(new double[]{d}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] dArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * dArr.length)]);
            wrap.order(byteOrder);
            for (double putDouble : dArr) {
                wrap.putDouble(putDouble);
            }
            return new ExifAttribute(12, dArr.length, wrap.array());
        }

        public static ExifAttribute createSLong(int i, ByteOrder byteOrder) {
            return createSLong(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * iArr.length)]);
            wrap.order(byteOrder);
            for (int putInt : iArr) {
                wrap.putInt(putInt);
            }
            return new ExifAttribute(9, iArr.length, wrap.array());
        }

        public static ExifAttribute createSRational(Rational rational, ByteOrder byteOrder) {
            return createSRational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(10, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createString(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(0);
            byte[] bytes2 = sb.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes2.length, bytes2);
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            return createULong(new long[]{j}, byteOrder);
        }

        public static ExifAttribute createULong(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * jArr.length)]);
            wrap.order(byteOrder);
            for (long j : jArr) {
                wrap.putInt((int) j);
            }
            return new ExifAttribute(4, jArr.length, wrap.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return createURational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createURational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(5, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            return createUShort(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createUShort(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * iArr.length)]);
            wrap.order(byteOrder);
            for (int i : iArr) {
                wrap.putShort((short) i);
            }
            return new ExifAttribute(3, iArr.length, wrap.array());
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (double) jArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return (double) iArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        return rationalArr[0].calculate();
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            int i;
            int i2;
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder sb = new StringBuilder();
            String str = ",";
            int i3 = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i3 < jArr.length) {
                    sb.append(jArr[i3]);
                    i3++;
                    if (i3 != jArr.length) {
                        sb.append(str);
                    }
                }
                return sb.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i3 < iArr.length) {
                    sb.append(iArr[i3]);
                    i3++;
                    if (i3 != iArr.length) {
                        sb.append(str);
                    }
                }
                return sb.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i2 < dArr.length) {
                    sb.append(dArr[i2]);
                    i2++;
                    if (i2 != dArr.length) {
                        sb.append(str);
                    }
                }
                return sb.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    sb.append(rationalArr[i].numerator);
                    sb.append('/');
                    sb.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        sb.append(str);
                    }
                }
                return sb.toString();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:164:0x01ab A[SYNTHETIC, Splitter:B:164:0x01ab] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Object getValue(ByteOrder byteOrder) {
            ByteOrderedDataInputStream byteOrderedDataInputStream;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            String str = "IOException occurred while closing InputStream";
            String str2 = ExifInterface.TAG;
            try {
                byteOrderedDataInputStream = new ByteOrderedDataInputStream(this.bytes);
                try {
                    byteOrderedDataInputStream.setByteOrder(byteOrder);
                    boolean z = true;
                    int i8 = 0;
                    switch (this.format) {
                        case 1:
                        case 6:
                            if (this.bytes.length != 1 || this.bytes[0] < 0 || this.bytes[0] > 1) {
                                String str3 = new String(this.bytes, ExifInterface.ASCII);
                                try {
                                    byteOrderedDataInputStream.close();
                                } catch (IOException e) {
                                    Log.e(str2, str, e);
                                }
                                return str3;
                            }
                            String str4 = new String(new char[]{(char) (this.bytes[0] + 48)});
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e2) {
                                Log.e(str2, str, e2);
                            }
                            return str4;
                        case 2:
                        case 7:
                            if (this.numberOfComponents >= ExifInterface.EXIF_ASCII_PREFIX.length) {
                                int i9 = 0;
                                while (true) {
                                    if (i9 < ExifInterface.EXIF_ASCII_PREFIX.length) {
                                        if (this.bytes[i9] != ExifInterface.EXIF_ASCII_PREFIX[i9]) {
                                            z = false;
                                        } else {
                                            i9++;
                                        }
                                    }
                                }
                                if (z) {
                                    i = ExifInterface.EXIF_ASCII_PREFIX.length;
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            while (true) {
                                if (i < this.numberOfComponents) {
                                    byte b = this.bytes[i];
                                    if (b != 0) {
                                        if (b >= 32) {
                                            sb.append((char) b);
                                        } else {
                                            sb.append('?');
                                        }
                                        i++;
                                    }
                                }
                            }
                            String sb2 = sb.toString();
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e3) {
                                Log.e(str2, str, e3);
                            }
                            return sb2;
                        case 3:
                            int[] iArr = new int[this.numberOfComponents];
                            while (i2 < this.numberOfComponents) {
                                iArr[i2] = byteOrderedDataInputStream.readUnsignedShort();
                                i2++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e4) {
                                Log.e(str2, str, e4);
                            }
                            return iArr;
                        case 4:
                            long[] jArr = new long[this.numberOfComponents];
                            while (i3 < this.numberOfComponents) {
                                jArr[i3] = byteOrderedDataInputStream.readUnsignedInt();
                                i3++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e5) {
                                Log.e(str2, str, e5);
                            }
                            return jArr;
                        case 5:
                            Rational[] rationalArr = new Rational[this.numberOfComponents];
                            while (i4 < this.numberOfComponents) {
                                rationalArr[i4] = new Rational(byteOrderedDataInputStream.readUnsignedInt(), byteOrderedDataInputStream.readUnsignedInt());
                                i4++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e6) {
                                Log.e(str2, str, e6);
                            }
                            return rationalArr;
                        case 8:
                            int[] iArr2 = new int[this.numberOfComponents];
                            while (i5 < this.numberOfComponents) {
                                iArr2[i5] = byteOrderedDataInputStream.readShort();
                                i5++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e7) {
                                Log.e(str2, str, e7);
                            }
                            return iArr2;
                        case 9:
                            int[] iArr3 = new int[this.numberOfComponents];
                            while (i6 < this.numberOfComponents) {
                                iArr3[i6] = byteOrderedDataInputStream.readInt();
                                i6++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e8) {
                                Log.e(str2, str, e8);
                            }
                            return iArr3;
                        case 10:
                            Rational[] rationalArr2 = new Rational[this.numberOfComponents];
                            while (i7 < this.numberOfComponents) {
                                rationalArr2[i7] = new Rational((long) byteOrderedDataInputStream.readInt(), (long) byteOrderedDataInputStream.readInt());
                                i7++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e9) {
                                Log.e(str2, str, e9);
                            }
                            return rationalArr2;
                        case 11:
                            double[] dArr = new double[this.numberOfComponents];
                            while (i8 < this.numberOfComponents) {
                                dArr[i8] = (double) byteOrderedDataInputStream.readFloat();
                                i8++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e10) {
                                Log.e(str2, str, e10);
                            }
                            return dArr;
                        case 12:
                            double[] dArr2 = new double[this.numberOfComponents];
                            while (i8 < this.numberOfComponents) {
                                dArr2[i8] = byteOrderedDataInputStream.readDouble();
                                i8++;
                            }
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e11) {
                                Log.e(str2, str, e11);
                            }
                            return dArr2;
                        default:
                            try {
                                byteOrderedDataInputStream.close();
                            } catch (IOException e12) {
                                Log.e(str2, str, e12);
                            }
                            return null;
                    }
                } catch (IOException e13) {
                    e = e13;
                }
                e = e13;
            } catch (IOException e14) {
                e = e14;
                byteOrderedDataInputStream = null;
            } catch (Throwable th) {
                th = th;
                byteOrderedDataInputStream = null;
                if (byteOrderedDataInputStream != null) {
                }
                throw th;
            }
            try {
                Log.w(str2, "IOException occurred during reading a value", e);
                if (byteOrderedDataInputStream != null) {
                    try {
                        byteOrderedDataInputStream.close();
                    } catch (IOException e15) {
                        Log.e(str2, str, e15);
                    }
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (byteOrderedDataInputStream != null) {
                    try {
                        byteOrderedDataInputStream.close();
                    } catch (IOException e16) {
                        Log.e(str2, str, e16);
                    }
                }
                throw th;
            }
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            sb.append(", data length:");
            sb.append(this.bytes.length);
            sb.append(")");
            return sb.toString();
        }
    }

    class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String str, int i, int i2) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        ExifTag(String str, int i, int i2, int i3) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }

        /* access modifiers changed from: 0000 */
        public boolean isFormatCompatible(int i) {
            int i2 = this.primaryFormat;
            if (!(i2 == 7 || i == 7 || i2 == i)) {
                int i3 = this.secondaryFormat;
                if (i3 != i) {
                    if ((i2 == 4 || i3 == 4) && i == 3) {
                        return true;
                    }
                    if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && i == 8) {
                        return true;
                    }
                    return (this.primaryFormat == 12 || this.secondaryFormat == 12) && i == 11;
                }
            }
            return true;
        }
    }

    @RestrictTo({Scope.LIBRARY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IfdType {
    }

    class Rational {
        public final long denominator;
        public final long numerator;

        Rational(double d) {
            this((long) (d * 10000.0d), FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
        }

        Rational(long j, long j2) {
            if (j2 == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }

        public double calculate() {
            return ((double) this.numerator) / ((double) this.denominator);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.numerator);
            sb.append("/");
            sb.append(this.denominator);
            return sb.toString();
        }
    }

    static {
        ExifTag[] exifTagArr;
        Integer valueOf = Integer.valueOf(1);
        Integer valueOf2 = Integer.valueOf(3);
        Integer valueOf3 = Integer.valueOf(2);
        Integer valueOf4 = Integer.valueOf(8);
        ROTATION_ORDER = Arrays.asList(new Integer[]{valueOf, Integer.valueOf(6), valueOf2, valueOf4});
        Integer valueOf5 = Integer.valueOf(7);
        Integer valueOf6 = Integer.valueOf(5);
        FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[]{valueOf3, valueOf5, Integer.valueOf(4), valueOf6});
        ExifTag[] exifTagArr2 = IFD_TIFF_TAGS;
        EXIF_TAGS = new ExifTag[][]{exifTagArr2, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, exifTagArr2, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS};
        ExifTag[][] exifTagArr3 = EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[exifTagArr3.length];
        sExifTagMapsForWriting = new HashMap[exifTagArr3.length];
        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            sExifTagMapsForReading[i] = new HashMap();
            sExifTagMapsForWriting[i] = new HashMap();
            for (ExifTag exifTag : EXIF_TAGS[i]) {
                sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
            }
        }
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), valueOf6);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), valueOf);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), valueOf3);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[3].number), valueOf2);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), valueOf5);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), valueOf4);
    }

    public ExifInterface(@NonNull File file) {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (file != null) {
            initForFilename(file.getAbsolutePath());
            return;
        }
        throw new NullPointerException("file cannot be null");
    }

    public ExifInterface(@NonNull FileDescriptor fileDescriptor) {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (fileDescriptor != null) {
            FileInputStream fileInputStream = null;
            this.mAssetInputStream = null;
            this.mFilename = null;
            if (VERSION.SDK_INT < 21 || !isSeekableFD(fileDescriptor)) {
                this.mSeekableFileDescriptor = null;
            } else {
                this.mSeekableFileDescriptor = fileDescriptor;
                try {
                    fileDescriptor = Os.dup(fileDescriptor);
                } catch (Exception e) {
                    throw new IOException("Failed to duplicate file descriptor", e);
                }
            }
            try {
                FileInputStream fileInputStream2 = new FileInputStream(fileDescriptor);
                try {
                    loadAttributes(fileInputStream2);
                    closeQuietly(fileInputStream2);
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    closeQuietly(fileInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                closeQuietly(fileInputStream);
                throw th;
            }
        } else {
            throw new NullPointerException("fileDescriptor cannot be null");
        }
    }

    public ExifInterface(@NonNull InputStream inputStream) {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (inputStream != null) {
            FileDescriptor fileDescriptor = null;
            this.mFilename = null;
            if (inputStream instanceof AssetInputStream) {
                this.mAssetInputStream = (AssetInputStream) inputStream;
            } else {
                if (inputStream instanceof FileInputStream) {
                    FileInputStream fileInputStream = (FileInputStream) inputStream;
                    if (isSeekableFD(fileInputStream.getFD())) {
                        this.mAssetInputStream = null;
                        fileDescriptor = fileInputStream.getFD();
                    }
                }
                this.mAssetInputStream = null;
            }
            this.mSeekableFileDescriptor = fileDescriptor;
            loadAttributes(inputStream);
            return;
        }
        throw new NullPointerException("inputStream cannot be null");
    }

    public ExifInterface(@NonNull String str) {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (str != null) {
            initForFilename(str);
            return;
        }
        throw new NullPointerException("filename cannot be null");
    }

    private void addDefaultValuesForCompatibility() {
        String attribute = getAttribute(TAG_DATETIME_ORIGINAL);
        if (attribute != null) {
            String str = TAG_DATETIME;
            if (getAttribute(str) == null) {
                this.mAttributes[0].put(str, ExifAttribute.createString(attribute));
            }
        }
        String str2 = TAG_IMAGE_WIDTH;
        if (getAttribute(str2) == null) {
            this.mAttributes[0].put(str2, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        String str3 = TAG_IMAGE_LENGTH;
        if (getAttribute(str3) == null) {
            this.mAttributes[0].put(str3, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        String str4 = TAG_ORIENTATION;
        if (getAttribute(str4) == null) {
            this.mAttributes[0].put(str4, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        String str5 = TAG_LIGHT_SOURCE;
        if (getAttribute(str5) == null) {
            this.mAttributes[1].put(str5, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    private String convertDecimalDegree(double d) {
        long j = (long) d;
        double d2 = d - ((double) j);
        long j2 = (long) (d2 * 60.0d);
        long round = Math.round((d2 - (((double) j2) / 60.0d)) * 3600.0d * 1.0E7d);
        StringBuilder sb = new StringBuilder();
        sb.append(j);
        String str = "/1,";
        sb.append(str);
        sb.append(j2);
        sb.append(str);
        sb.append(round);
        sb.append("/10000000");
        return sb.toString();
    }

    private static double convertRationalLatLonToDouble(String str, String str2) {
        String str3 = "/";
        try {
            String[] split = str.split(",", -1);
            String[] split2 = split[0].split(str3, -1);
            double parseDouble = Double.parseDouble(split2[0].trim()) / Double.parseDouble(split2[1].trim());
            String[] split3 = split[1].split(str3, -1);
            double parseDouble2 = Double.parseDouble(split3[0].trim()) / Double.parseDouble(split3[1].trim());
            String[] split4 = split[2].split(str3, -1);
            double parseDouble3 = parseDouble + (parseDouble2 / 60.0d) + ((Double.parseDouble(split4[0].trim()) / Double.parseDouble(split4[1].trim())) / 3600.0d);
            if (!str2.equals("S")) {
                if (!str2.equals("W")) {
                    if (!str2.equals("N")) {
                        if (!str2.equals("E")) {
                            throw new IllegalArgumentException();
                        }
                    }
                    return parseDouble3;
                }
            }
            return -parseDouble3;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException unused) {
            throw new IllegalArgumentException();
        }
    }

    private static long[] convertToLongArray(Object obj) {
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            long[] jArr = new long[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                jArr[i] = (long) iArr[i];
            }
            return jArr;
        } else if (obj instanceof long[]) {
            return (long[]) obj;
        } else {
            return null;
        }
    }

    private static int copy(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[8192];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return i;
            }
            i += read;
            outputStream.write(bArr, 0, read);
        }
    }

    @Nullable
    private ExifAttribute getExifAttribute(@NonNull String str) {
        if (str != null) {
            if (TAG_ISO_SPEED_RATINGS.equals(str)) {
                str = TAG_PHOTOGRAPHIC_SENSITIVITY;
            }
            for (int i = 0; i < EXIF_TAGS.length; i++) {
                ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(str);
                if (exifAttribute != null) {
                    return exifAttribute;
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    private void getHeifAttributes(final ByteOrderedDataInputStream byteOrderedDataInputStream) {
        String str;
        String str2;
        String str3 = "yes";
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            if (VERSION.SDK_INT >= 23) {
                mediaMetadataRetriever.setDataSource(new MediaDataSource() {
                    long mPosition;

                    public void close() {
                    }

                    public long getSize() {
                        return -1;
                    }

                    public int readAt(long j, byte[] bArr, int i, int i2) {
                        if (i2 == 0) {
                            return 0;
                        }
                        if (j < 0) {
                            return -1;
                        }
                        try {
                            if (this.mPosition != j) {
                                if (this.mPosition >= 0 && j >= this.mPosition + ((long) byteOrderedDataInputStream.available())) {
                                    return -1;
                                }
                                byteOrderedDataInputStream.seek(j);
                                this.mPosition = j;
                            }
                            if (i2 > byteOrderedDataInputStream.available()) {
                                i2 = byteOrderedDataInputStream.available();
                            }
                            int read = byteOrderedDataInputStream.read(bArr, i, i2);
                            if (read >= 0) {
                                this.mPosition += (long) read;
                                return read;
                            }
                        } catch (IOException unused) {
                        }
                        this.mPosition = -1;
                        return -1;
                    }
                });
            } else if (this.mSeekableFileDescriptor != null) {
                mediaMetadataRetriever.setDataSource(this.mSeekableFileDescriptor);
            } else if (this.mFilename != null) {
                mediaMetadataRetriever.setDataSource(this.mFilename);
            } else {
                mediaMetadataRetriever.release();
                return;
            }
            String extractMetadata = mediaMetadataRetriever.extractMetadata(33);
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(34);
            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(26);
            String extractMetadata4 = mediaMetadataRetriever.extractMetadata(17);
            String str4 = null;
            if (str3.equals(extractMetadata3)) {
                str4 = mediaMetadataRetriever.extractMetadata(29);
                str2 = mediaMetadataRetriever.extractMetadata(30);
                str = mediaMetadataRetriever.extractMetadata(31);
            } else if (str3.equals(extractMetadata4)) {
                str4 = mediaMetadataRetriever.extractMetadata(18);
                str2 = mediaMetadataRetriever.extractMetadata(19);
                str = mediaMetadataRetriever.extractMetadata(24);
            } else {
                str2 = null;
                str = null;
            }
            if (str4 != null) {
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createUShort(Integer.parseInt(str4), this.mExifByteOrder));
            }
            if (str2 != null) {
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createUShort(Integer.parseInt(str2), this.mExifByteOrder));
            }
            if (str != null) {
                int parseInt = Integer.parseInt(str);
                int i = parseInt != 90 ? parseInt != 180 ? parseInt != 270 ? 1 : 8 : 3 : 6;
                this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createUShort(i, this.mExifByteOrder));
            }
            if (!(extractMetadata == null || extractMetadata2 == null)) {
                int parseInt2 = Integer.parseInt(extractMetadata);
                int parseInt3 = Integer.parseInt(extractMetadata2);
                if (parseInt3 > 6) {
                    byteOrderedDataInputStream.seek((long) parseInt2);
                    byte[] bArr = new byte[6];
                    if (byteOrderedDataInputStream.read(bArr) == 6) {
                        int i2 = parseInt3 - 6;
                        if (Arrays.equals(bArr, IDENTIFIER_EXIF_APP1)) {
                            byte[] bArr2 = new byte[i2];
                            if (byteOrderedDataInputStream.read(bArr2) == i2) {
                                readExifSegment(bArr2, 0);
                            } else {
                                throw new IOException("Can't read exif");
                            }
                        } else {
                            throw new IOException("Invalid identifier");
                        }
                    } else {
                        throw new IOException("Can't read identifier");
                    }
                } else {
                    throw new IOException("Invalid exif length");
                }
            }
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0121 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x008f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getJpegAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream, int i, int i2) {
        ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        int i3 = i;
        int i4 = i2;
        byteOrderedDataInputStream2.setByteOrder(ByteOrder.BIG_ENDIAN);
        byteOrderedDataInputStream2.seek((long) i3);
        byte readByte = byteOrderedDataInputStream.readByte();
        String str = "Invalid marker: ";
        byte b = MARKER;
        if (readByte == -1) {
            int i5 = i3 + 1;
            if (byteOrderedDataInputStream.readByte() == -40) {
                int i6 = i5 + 1;
                while (true) {
                    byte readByte2 = byteOrderedDataInputStream.readByte();
                    if (readByte2 == b) {
                        int i7 = i6 + 1;
                        byte readByte3 = byteOrderedDataInputStream.readByte();
                        int i8 = i7 + 1;
                        if (readByte3 == -39 || readByte3 == -38) {
                            byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
                        } else {
                            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort() - 2;
                            int i9 = i8 + 2;
                            String str2 = "Invalid length";
                            if (readUnsignedShort >= 0) {
                                if (readByte3 == -31) {
                                    byte[] bArr = new byte[readUnsignedShort];
                                    byteOrderedDataInputStream2.readFully(bArr);
                                    int i10 = readUnsignedShort + i9;
                                    if (startsWith(bArr, IDENTIFIER_EXIF_APP1)) {
                                        byte[] bArr2 = IDENTIFIER_EXIF_APP1;
                                        long length = (long) (i9 + bArr2.length);
                                        readExifSegment(Arrays.copyOfRange(bArr, bArr2.length, bArr.length), i4);
                                        this.mExifOffset = (int) length;
                                    } else if (startsWith(bArr, IDENTIFIER_XMP_APP1)) {
                                        byte[] bArr3 = IDENTIFIER_XMP_APP1;
                                        long length2 = (long) (i9 + bArr3.length);
                                        byte[] copyOfRange = Arrays.copyOfRange(bArr, bArr3.length, bArr.length);
                                        String str3 = TAG_XMP;
                                        if (getAttribute(str3) == null) {
                                            HashMap hashMap = this.mAttributes[0];
                                            ExifAttribute exifAttribute = r11;
                                            ExifAttribute exifAttribute2 = new ExifAttribute(1, copyOfRange.length, length2, copyOfRange);
                                            hashMap.put(str3, exifAttribute);
                                        }
                                    }
                                    i9 = i10;
                                    readUnsignedShort = 0;
                                } else if (readByte3 != -2) {
                                    switch (readByte3) {
                                        case -64:
                                        case -63:
                                        case -62:
                                        case -61:
                                            if (byteOrderedDataInputStream2.skipBytes(1) != 1) {
                                            }
                                            break;
                                        default:
                                            switch (readByte3) {
                                                case -59:
                                                case -58:
                                                case -57:
                                                    break;
                                                default:
                                                    switch (readByte3) {
                                                        case -55:
                                                        case -54:
                                                        case -53:
                                                            break;
                                                        default:
                                                            switch (readByte3) {
                                                                case -51:
                                                                case -50:
                                                                case -49:
                                                                    break;
                                                            }
                                                    }
                                            }
                                            if (byteOrderedDataInputStream2.skipBytes(1) != 1) {
                                                this.mAttributes[i4].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong((long) byteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                                this.mAttributes[i4].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong((long) byteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                                readUnsignedShort -= 5;
                                                break;
                                            } else {
                                                throw new IOException("Invalid SOFx");
                                            }
                                    }
                                    if (readUnsignedShort >= 0) {
                                        throw new IOException(str2);
                                    } else if (byteOrderedDataInputStream2.skipBytes(readUnsignedShort) == readUnsignedShort) {
                                        i6 = i9 + readUnsignedShort;
                                        b = MARKER;
                                    } else {
                                        throw new IOException("Invalid JPEG segment");
                                    }
                                }
                                byte[] bArr4 = new byte[readUnsignedShort];
                                if (byteOrderedDataInputStream2.read(bArr4) == readUnsignedShort) {
                                    String str4 = TAG_USER_COMMENT;
                                    if (getAttribute(str4) == null) {
                                        this.mAttributes[1].put(str4, ExifAttribute.createString(new String(bArr4, ASCII)));
                                    }
                                    readUnsignedShort = 0;
                                    if (readUnsignedShort >= 0) {
                                    }
                                } else {
                                    throw new IOException("Invalid exif");
                                }
                            } else {
                                throw new IOException(str2);
                            }
                        }
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid marker:");
                        sb.append(Integer.toHexString(readByte2 & MARKER));
                        throw new IOException(sb.toString());
                    }
                }
                byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
                return;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(Integer.toHexString(readByte & MARKER));
            throw new IOException(sb2.toString());
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(Integer.toHexString(readByte & MARKER));
        throw new IOException(sb3.toString());
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        if (isRafFormat(bArr)) {
            return 9;
        }
        if (isHeifFormat(bArr)) {
            return 12;
        }
        if (isOrfFormat(bArr)) {
            return 7;
        }
        return isRw2Format(bArr) ? 10 : 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getOrfAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        long j;
        getRawAttributes(byteOrderedDataInputStream);
        ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (exifAttribute3 != null) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute3.bytes);
            byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
            byte[] bArr = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            byteOrderedDataInputStream2.readFully(bArr);
            byteOrderedDataInputStream2.seek(0);
            byte[] bArr2 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            byteOrderedDataInputStream2.readFully(bArr2);
            if (Arrays.equals(bArr, ORF_MAKER_NOTE_HEADER_1)) {
                j = 8;
            } else {
                if (Arrays.equals(bArr2, ORF_MAKER_NOTE_HEADER_2)) {
                    j = 12;
                }
                readImageFileDirectory(byteOrderedDataInputStream2, 6);
                exifAttribute = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
                ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
                if (!(exifAttribute == null || exifAttribute4 == null)) {
                    this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, exifAttribute);
                    this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, exifAttribute4);
                }
                exifAttribute2 = (ExifAttribute) this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME);
                if (exifAttribute2 == null) {
                    int[] iArr = (int[]) exifAttribute2.getValue(this.mExifByteOrder);
                    if (iArr == null || iArr.length != 4) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid aspect frame values. frame=");
                        sb.append(Arrays.toString(iArr));
                        Log.w(TAG, sb.toString());
                        return;
                    } else if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
                        int i = (iArr[2] - iArr[0]) + 1;
                        int i2 = (iArr[3] - iArr[1]) + 1;
                        if (i < i2) {
                            int i3 = i + i2;
                            i2 = i3 - i2;
                            i = i3 - i2;
                        }
                        ExifAttribute createUShort = ExifAttribute.createUShort(i, this.mExifByteOrder);
                        ExifAttribute createUShort2 = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                        this.mAttributes[0].put(TAG_IMAGE_WIDTH, createUShort);
                        this.mAttributes[0].put(TAG_IMAGE_LENGTH, createUShort2);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            byteOrderedDataInputStream2.seek(j);
            readImageFileDirectory(byteOrderedDataInputStream2, 6);
            exifAttribute = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
            ExifAttribute exifAttribute42 = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
            this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, exifAttribute);
            this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, exifAttribute42);
            exifAttribute2 = (ExifAttribute) this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME);
            if (exifAttribute2 == null) {
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        byteOrderedDataInputStream.skipBytes(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(bArr2);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        getJpegAttributes(byteOrderedDataInputStream, i, 5);
        byteOrderedDataInputStream.seek((long) i2);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        int readInt = byteOrderedDataInputStream.readInt();
        for (int i3 = 0; i3 < readInt; i3++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                short readShort = byteOrderedDataInputStream.readShort();
                short readShort2 = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort((int) readShort, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort((int) readShort2, this.mExifByteOrder);
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, createUShort);
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, createUShort2);
                return;
            }
            byteOrderedDataInputStream.skipBytes(readUnsignedShort2);
        }
    }

    private void getRawAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        parseTiffHeaders(byteOrderedDataInputStream, byteOrderedDataInputStream.available());
        readImageFileDirectory(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 5);
        updateImageSizeValues(byteOrderedDataInputStream, 4);
        validateImages(byteOrderedDataInputStream);
        if (this.mMimeType == 8) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
            if (exifAttribute != null) {
                ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
                byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
                byteOrderedDataInputStream2.seek(6);
                readImageFileDirectory(byteOrderedDataInputStream2, 9);
                HashMap hashMap = this.mAttributes[9];
                String str = TAG_COLOR_SPACE;
                ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(str);
                if (exifAttribute2 != null) {
                    this.mAttributes[1].put(str, exifAttribute2);
                }
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        getRawAttributes(byteOrderedDataInputStream);
        if (((ExifAttribute) this.mAttributes[0].get(TAG_RW2_JPG_FROM_RAW)) != null) {
            getJpegAttributes(byteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[0].get(TAG_RW2_ISO);
        HashMap hashMap = this.mAttributes[1];
        String str = TAG_PHOTOGRAPHIC_SENSITIVITY;
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(str);
        if (exifAttribute != null && exifAttribute2 == null) {
            this.mAttributes[1].put(str, exifAttribute);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:68|69|70) */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        java.lang.Double.parseDouble(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x015c, code lost:
        return new android.util.Pair(java.lang.Integer.valueOf(12), r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0162, code lost:
        return new android.util.Pair(r5, r7);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:68:0x014e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Pair guessDataFormat(String str) {
        String str2 = ",";
        boolean contains = str.contains(str2);
        Integer valueOf = Integer.valueOf(2);
        Integer valueOf2 = Integer.valueOf(-1);
        if (contains) {
            String[] split = str.split(str2, -1);
            Pair guessDataFormat = guessDataFormat(split[0]);
            if (((Integer) guessDataFormat.first).intValue() == 2) {
                return guessDataFormat;
            }
            for (int i = 1; i < split.length; i++) {
                Pair guessDataFormat2 = guessDataFormat(split[i]);
                int intValue = (((Integer) guessDataFormat2.first).equals(guessDataFormat.first) || ((Integer) guessDataFormat2.second).equals(guessDataFormat.first)) ? ((Integer) guessDataFormat.first).intValue() : -1;
                int intValue2 = (((Integer) guessDataFormat.second).intValue() == -1 || (!((Integer) guessDataFormat2.first).equals(guessDataFormat.second) && !((Integer) guessDataFormat2.second).equals(guessDataFormat.second))) ? -1 : ((Integer) guessDataFormat.second).intValue();
                if (intValue == -1 && intValue2 == -1) {
                    return new Pair(valueOf, valueOf2);
                }
                if (intValue == -1) {
                    guessDataFormat = new Pair(Integer.valueOf(intValue2), valueOf2);
                } else if (intValue2 == -1) {
                    guessDataFormat = new Pair(Integer.valueOf(intValue), valueOf2);
                }
            }
            return guessDataFormat;
        }
        String str3 = "/";
        if (str.contains(str3)) {
            String[] split2 = str.split(str3, -1);
            if (split2.length == 2) {
                try {
                    long parseDouble = (long) Double.parseDouble(split2[0]);
                    long parseDouble2 = (long) Double.parseDouble(split2[1]);
                    if (parseDouble >= 0) {
                        if (parseDouble2 >= 0) {
                            if (parseDouble <= 2147483647L) {
                                if (parseDouble2 <= 2147483647L) {
                                    return new Pair(Integer.valueOf(10), Integer.valueOf(5));
                                }
                            }
                            return new Pair(Integer.valueOf(5), valueOf2);
                        }
                    }
                    return new Pair(Integer.valueOf(10), valueOf2);
                } catch (NumberFormatException unused) {
                }
            }
            return new Pair(valueOf, valueOf2);
        }
        Long valueOf3 = Long.valueOf(Long.parseLong(str));
        return (valueOf3.longValue() < 0 || valueOf3.longValue() > 65535) ? valueOf3.longValue() < 0 ? new Pair(Integer.valueOf(9), valueOf2) : new Pair(Integer.valueOf(4), valueOf2) : new Pair(Integer.valueOf(3), Integer.valueOf(4));
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) {
        int i;
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_JPEG_INTERCHANGE_FORMAT);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int min = Math.min(exifAttribute2.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.getLength() - intValue);
            int i2 = this.mMimeType;
            if (i2 == 4 || i2 == 9 || i2 == 10) {
                i = this.mExifOffset;
            } else {
                if (i2 == 7) {
                    i = this.mOrfMakerNoteOffset;
                }
                if (intValue > 0 && min > 0) {
                    this.mHasThumbnail = true;
                    this.mThumbnailOffset = intValue;
                    this.mThumbnailLength = min;
                    if (this.mFilename == null && this.mAssetInputStream == null && this.mSeekableFileDescriptor == null) {
                        byte[] bArr = new byte[min];
                        byteOrderedDataInputStream.seek((long) intValue);
                        byteOrderedDataInputStream.readFully(bArr);
                        this.mThumbnailBytes = bArr;
                        return;
                    }
                    return;
                }
            }
            intValue += i;
            if (intValue > 0) {
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) {
        String str;
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_STRIP_OFFSETS);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_STRIP_BYTE_COUNTS);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            long[] convertToLongArray = convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            long[] convertToLongArray2 = convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            String str2 = TAG;
            if (convertToLongArray == null) {
                str = "stripOffsets should not be null.";
            } else if (convertToLongArray2 == null) {
                str = "stripByteCounts should not be null.";
            } else {
                long j = 0;
                for (long j2 : convertToLongArray2) {
                    j += j2;
                }
                byte[] bArr = new byte[((int) j)];
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < convertToLongArray.length; i3++) {
                    int i4 = (int) convertToLongArray2[i3];
                    int i5 = ((int) convertToLongArray[i3]) - i;
                    if (i5 < 0) {
                        Log.d(str2, "Invalid strip offset value");
                    }
                    byteOrderedDataInputStream.seek((long) i5);
                    int i6 = i + i5;
                    byte[] bArr2 = new byte[i4];
                    byteOrderedDataInputStream.read(bArr2);
                    i = i6 + i4;
                    System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
                    i2 += bArr2.length;
                }
                this.mHasThumbnail = true;
                this.mThumbnailBytes = bArr;
                this.mThumbnailLength = bArr.length;
            }
            Log.w(str2, str);
        }
    }

    private void initForFilename(String str) {
        FileInputStream fileInputStream;
        if (str != null) {
            this.mAssetInputStream = null;
            this.mFilename = str;
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    if (isSeekableFD(fileInputStream.getFD())) {
                        this.mSeekableFileDescriptor = fileInputStream.getFD();
                    } else {
                        this.mSeekableFileDescriptor = null;
                    }
                    loadAttributes(fileInputStream);
                    closeQuietly(fileInputStream);
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(fileInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
                closeQuietly(fileInputStream);
                throw th;
            }
        } else {
            throw new NullPointerException("filename cannot be null");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0092, code lost:
        if (r1 != null) goto L_0x0094;
     */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isHeifFormat(byte[] bArr) {
        ByteOrderedDataInputStream byteOrderedDataInputStream;
        try {
            byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
            try {
                byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
                long readInt = (long) byteOrderedDataInputStream.readInt();
                byte[] bArr2 = new byte[4];
                byteOrderedDataInputStream.read(bArr2);
                if (!Arrays.equals(bArr2, HEIF_TYPE_FTYP)) {
                    byteOrderedDataInputStream.close();
                    return false;
                }
                long j = 16;
                if (readInt == 1) {
                    readInt = byteOrderedDataInputStream.readLong();
                    if (readInt < 16) {
                        byteOrderedDataInputStream.close();
                        return false;
                    }
                } else {
                    j = 8;
                }
                if (readInt > ((long) bArr.length)) {
                    readInt = (long) bArr.length;
                }
                long j2 = readInt - j;
                if (j2 < 8) {
                    byteOrderedDataInputStream.close();
                    return false;
                }
                byte[] bArr3 = new byte[4];
                boolean z = false;
                boolean z2 = false;
                for (long j3 = 0; j3 < j2 / 4; j3++) {
                    if (byteOrderedDataInputStream.read(bArr3) != bArr3.length) {
                        byteOrderedDataInputStream.close();
                        return false;
                    }
                    if (j3 != 1) {
                        if (Arrays.equals(bArr3, HEIF_BRAND_MIF1)) {
                            z = true;
                        } else if (Arrays.equals(bArr3, HEIF_BRAND_HEIC)) {
                            z2 = true;
                        }
                        if (z && z2) {
                            byteOrderedDataInputStream.close();
                            return true;
                        }
                    }
                }
                byteOrderedDataInputStream.close();
                return false;
            } catch (Exception unused) {
            } catch (Throwable th) {
                th = th;
                if (byteOrderedDataInputStream != null) {
                    byteOrderedDataInputStream.close();
                }
                throw th;
            }
        } catch (Exception unused2) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = null;
        } catch (Throwable th2) {
            th = th2;
            byteOrderedDataInputStream = null;
            if (byteOrderedDataInputStream != null) {
            }
            throw th;
        }
    }

    private static boolean isJpegFormat(byte[] bArr) {
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    private boolean isOrfFormat(byte[] bArr) {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == 20306 || readShort == 21330;
    }

    private boolean isRafFormat(byte[] bArr) {
        byte[] bytes = RAF_SIGNATURE.getBytes(Charset.defaultCharset());
        for (int i = 0; i < bytes.length; i++) {
            if (bArr[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isRw2Format(byte[] bArr) {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == 85;
    }

    private static boolean isSeekableFD(FileDescriptor fileDescriptor) {
        if (VERSION.SDK_INT >= 21) {
            try {
                Os.lseek(fileDescriptor, 0, OsConstants.SEEK_CUR);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private boolean isSupportedDataType(HashMap hashMap) {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_BITS_PER_SAMPLE);
        if (exifAttribute != null) {
            int[] iArr = (int[]) exifAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, iArr)) {
                return true;
            }
            if (this.mMimeType == 3) {
                ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_PHOTOMETRIC_INTERPRETATION);
                if (exifAttribute2 != null) {
                    int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if ((intValue == 1 && Arrays.equals(iArr, BITS_PER_SAMPLE_GREYSCALE_2)) || (intValue == 6 && Arrays.equals(iArr, BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isThumbnail(HashMap hashMap) {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_IMAGE_LENGTH);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_IMAGE_WIDTH);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (intValue <= 512 && intValue2 <= 512) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r4.mIsSupportedFile = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0053, code lost:
        addDefaultValuesForCompatibility();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadAttributes(@NonNull InputStream inputStream) {
        if (inputStream != null) {
            int i = 0;
            while (true) {
                if (i < EXIF_TAGS.length) {
                    this.mAttributes[i] = new HashMap();
                    i++;
                } else {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 5000);
                    this.mMimeType = getMimeType(bufferedInputStream);
                    ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream((InputStream) bufferedInputStream);
                    switch (this.mMimeType) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 11:
                            getRawAttributes(byteOrderedDataInputStream);
                            break;
                        case 4:
                            getJpegAttributes(byteOrderedDataInputStream, 0, 0);
                            break;
                        case 7:
                            getOrfAttributes(byteOrderedDataInputStream);
                            break;
                        case 9:
                            getRafAttributes(byteOrderedDataInputStream);
                            break;
                        case 10:
                            getRw2Attributes(byteOrderedDataInputStream);
                            break;
                        case 12:
                            getHeifAttributes(byteOrderedDataInputStream);
                            break;
                    }
                    setThumbnailData(byteOrderedDataInputStream);
                    this.mIsSupportedFile = true;
                }
            }
            addDefaultValuesForCompatibility();
            return;
        }
        throw new NullPointerException("inputstream shouldn't be null");
    }

    private static long parseDateTime(@Nullable String str, @Nullable String str2) {
        if (str != null && sNonZeroTimePattern.matcher(str).matches()) {
            try {
                Date parse = sFormatter.parse(str, new ParsePosition(0));
                if (parse == null) {
                    return -1;
                }
                long time = parse.getTime();
                if (str2 != null) {
                    try {
                        long parseLong = Long.parseLong(str2);
                        while (parseLong > 1000) {
                            parseLong /= 10;
                        }
                        time += parseLong;
                    } catch (NumberFormatException unused) {
                    }
                }
                return time;
            } catch (IllegalArgumentException unused2) {
            }
        }
        return -1;
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) {
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i2 = this.mMimeType;
        if (i2 == 7 || i2 == 10 || readUnsignedShort == 42) {
            int readInt = byteOrderedDataInputStream.readInt();
            if (readInt < 8 || readInt >= i) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid first Ifd offset: ");
                sb.append(readInt);
                throw new IOException(sb.toString());
            }
            int i3 = readInt - 8;
            if (i3 > 0 && byteOrderedDataInputStream.skipBytes(i3) != i3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Couldn't jump to first Ifd: ");
                sb2.append(i3);
                throw new IOException(sb2.toString());
            }
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Invalid start code: ");
        sb3.append(Integer.toHexString(readUnsignedShort));
        throw new IOException(sb3.toString());
    }

    private void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("The size of tag group[");
            sb.append(i);
            sb.append("]: ");
            sb.append(this.mAttributes[i].size());
            String sb2 = sb.toString();
            String str = TAG;
            Log.d(str, sb2);
            for (Entry entry : this.mAttributes[i].entrySet()) {
                ExifAttribute exifAttribute = (ExifAttribute) entry.getValue();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("tagName: ");
                sb3.append((String) entry.getKey());
                sb3.append(", tagType: ");
                sb3.append(exifAttribute.toString());
                sb3.append(", tagValue: '");
                sb3.append(exifAttribute.getStringValue(this.mExifByteOrder));
                sb3.append("'");
                Log.d(str, sb3.toString());
            }
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (readShort == 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid byte order: ");
        sb.append(Integer.toHexString(readShort));
        throw new IOException(sb.toString());
    }

    private void readExifSegment(byte[] bArr, int i) {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        parseTiffHeaders(byteOrderedDataInputStream, bArr.length);
        readImageFileDirectory(byteOrderedDataInputStream, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x02a4, code lost:
        r1.seek(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x02f0, code lost:
        if (r0.mAttributes[5].isEmpty() != false) goto L_0x02e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0276, code lost:
        if (TAG_MODEL.equals(r7.name) != false) goto L_0x0278;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0284, code lost:
        if (r12.getStringValue(r0.mExifByteOrder).contains(PEF_SIGNATURE) == false) goto L_0x0286;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x028c, code lost:
        if (r2.equals(r7.name) == false) goto L_0x029b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0297, code lost:
        if (r12.getIntValue(r0.mExifByteOrder) != 65535) goto L_0x029b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0299, code lost:
        r0.mMimeType = 8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x02a2, code lost:
        if (((long) r23.peek()) == r9) goto L_0x02a7;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01b3  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0234  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readImageFileDirectory(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) {
        String str;
        String str2;
        StringBuilder sb;
        boolean z;
        long j;
        short s;
        short s2;
        ExifTag exifTag;
        int i2;
        int i3;
        Integer num;
        int i4;
        String sb2;
        StringBuilder sb3;
        String sb4;
        ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        int i5 = i;
        this.mAttributesOffsets.add(Integer.valueOf(byteOrderedDataInputStream2.mPosition));
        if (byteOrderedDataInputStream2.mPosition + 2 <= byteOrderedDataInputStream2.mLength) {
            short readShort = byteOrderedDataInputStream.readShort();
            if (byteOrderedDataInputStream2.mPosition + (readShort * 12) <= byteOrderedDataInputStream2.mLength && readShort > 0) {
                short s3 = 0;
                while (true) {
                    str = TAG;
                    if (s3 >= readShort) {
                        break;
                    }
                    int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
                    int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
                    int readInt = byteOrderedDataInputStream.readInt();
                    long peek = ((long) byteOrderedDataInputStream.peek()) + 4;
                    ExifTag exifTag2 = (ExifTag) sExifTagMapsForReading[i5].get(Integer.valueOf(readUnsignedShort));
                    if (exifTag2 == null) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Skip the tag entry since tag number is not defined: ");
                        sb5.append(readUnsignedShort);
                        sb4 = sb5.toString();
                    } else {
                        if (readUnsignedShort2 <= 0 || readUnsignedShort2 >= IFD_FORMAT_BYTES_PER_FORMAT.length) {
                            sb3 = new StringBuilder();
                            sb3.append("Skip the tag entry since data format is invalid: ");
                            sb3.append(readUnsignedShort2);
                        } else if (!exifTag2.isFormatCompatible(readUnsignedShort2)) {
                            sb3 = new StringBuilder();
                            sb3.append("Skip the tag entry since data format (");
                            sb3.append(IFD_FORMAT_NAMES[readUnsignedShort2]);
                            sb3.append(") is unexpected for tag: ");
                            sb3.append(exifTag2.name);
                        } else {
                            if (readUnsignedShort2 == 7) {
                                readUnsignedShort2 = exifTag2.primaryFormat;
                            }
                            String str3 = str;
                            j = ((long) readInt) * ((long) IFD_FORMAT_BYTES_PER_FORMAT[readUnsignedShort2]);
                            if (j < 0 || j > 2147483647L) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("Skip the tag entry since the number of components is invalid: ");
                                sb6.append(readInt);
                                str = str3;
                                Log.w(str, sb6.toString());
                                z = false;
                                if (!z) {
                                    byteOrderedDataInputStream2.seek(peek);
                                    s2 = readShort;
                                    s = s3;
                                } else {
                                    int i6 = (j > 4 ? 1 : (j == 4 ? 0 : -1));
                                    String str4 = TAG_COMPRESSION;
                                    if (i6 > 0) {
                                        int readInt2 = byteOrderedDataInputStream.readInt();
                                        s2 = readShort;
                                        int i7 = this.mMimeType;
                                        s = s3;
                                        if (i7 == 7) {
                                            if (TAG_MAKER_NOTE.equals(exifTag2.name)) {
                                                this.mOrfMakerNoteOffset = readInt2;
                                            } else if (i5 == 6) {
                                                if (TAG_ORF_THUMBNAIL_IMAGE.equals(exifTag2.name)) {
                                                    this.mOrfThumbnailOffset = readInt2;
                                                    this.mOrfThumbnailLength = readInt;
                                                    ExifAttribute createUShort = ExifAttribute.createUShort(6, this.mExifByteOrder);
                                                    i3 = readUnsignedShort;
                                                    i2 = readUnsignedShort2;
                                                    ExifAttribute createULong = ExifAttribute.createULong((long) this.mOrfThumbnailOffset, this.mExifByteOrder);
                                                    ExifAttribute createULong2 = ExifAttribute.createULong((long) this.mOrfThumbnailLength, this.mExifByteOrder);
                                                    this.mAttributes[4].put(str4, createUShort);
                                                    this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT, createULong);
                                                    this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, createULong2);
                                                }
                                            }
                                            i3 = readUnsignedShort;
                                            i2 = readUnsignedShort2;
                                        } else {
                                            i3 = readUnsignedShort;
                                            i2 = readUnsignedShort2;
                                            if (i7 == 10) {
                                                if (TAG_RW2_JPG_FROM_RAW.equals(exifTag2.name)) {
                                                    this.mRw2JpgFromRawOffset = readInt2;
                                                }
                                            }
                                        }
                                        long j2 = (long) readInt2;
                                        exifTag = exifTag2;
                                        if (j2 + j <= ((long) byteOrderedDataInputStream2.mLength)) {
                                            byteOrderedDataInputStream2.seek(j2);
                                            num = (Integer) sExifPointerTagMap.get(Integer.valueOf(i3));
                                            if (num == null) {
                                                long j3 = -1;
                                                int i8 = i2;
                                                if (i8 == 3) {
                                                    i4 = byteOrderedDataInputStream.readUnsignedShort();
                                                    j3 = (long) i4;
                                                } else if (i8 != 4) {
                                                    if (i8 == 8) {
                                                        i4 = byteOrderedDataInputStream.readShort();
                                                    } else if (i8 == 9 || i8 == 13) {
                                                        i4 = byteOrderedDataInputStream.readInt();
                                                    }
                                                    j3 = (long) i4;
                                                } else {
                                                    j3 = byteOrderedDataInputStream.readUnsignedInt();
                                                }
                                                if (j3 <= 0 || j3 >= ((long) byteOrderedDataInputStream2.mLength)) {
                                                    StringBuilder sb7 = new StringBuilder();
                                                    sb7.append("Skip jump into the IFD since its offset is invalid: ");
                                                    sb7.append(j3);
                                                    sb2 = sb7.toString();
                                                } else if (!this.mAttributesOffsets.contains(Integer.valueOf((int) j3))) {
                                                    byteOrderedDataInputStream2.seek(j3);
                                                    readImageFileDirectory(byteOrderedDataInputStream2, num.intValue());
                                                    byteOrderedDataInputStream2.seek(peek);
                                                } else {
                                                    StringBuilder sb8 = new StringBuilder();
                                                    sb8.append("Skip jump into the IFD since it has already been read: IfdType ");
                                                    sb8.append(num);
                                                    sb8.append(" (at ");
                                                    sb8.append(j3);
                                                    sb8.append(")");
                                                    sb2 = sb8.toString();
                                                }
                                            } else {
                                                int i9 = i2;
                                                int peek2 = byteOrderedDataInputStream.peek();
                                                byte[] bArr = new byte[((int) j)];
                                                byteOrderedDataInputStream2.readFully(bArr);
                                                long j4 = (long) peek2;
                                                long j5 = peek;
                                                String str5 = str4;
                                                ExifAttribute exifAttribute = new ExifAttribute(i9, readInt, j4, bArr);
                                                ExifTag exifTag3 = exifTag;
                                                this.mAttributes[i].put(exifTag3.name, exifAttribute);
                                                if (TAG_DNG_VERSION.equals(exifTag3.name)) {
                                                    this.mMimeType = 3;
                                                }
                                                if (!TAG_MAKE.equals(exifTag3.name)) {
                                                }
                                            }
                                        } else {
                                            StringBuilder sb9 = new StringBuilder();
                                            sb9.append("Skip the tag entry since data offset is invalid: ");
                                            sb9.append(readInt2);
                                            sb2 = sb9.toString();
                                        }
                                    } else {
                                        s2 = readShort;
                                        exifTag = exifTag2;
                                        s = s3;
                                        i3 = readUnsignedShort;
                                        i2 = readUnsignedShort2;
                                        num = (Integer) sExifPointerTagMap.get(Integer.valueOf(i3));
                                        if (num == null) {
                                        }
                                    }
                                    Log.w(str, sb2);
                                    byteOrderedDataInputStream2.seek(peek);
                                }
                                s3 = (short) (s + 1);
                                i5 = i;
                                readShort = s2;
                            } else {
                                z = true;
                                str = str3;
                                if (!z) {
                                }
                                s3 = (short) (s + 1);
                                i5 = i;
                                readShort = s2;
                            }
                        }
                        sb4 = sb3.toString();
                    }
                    Log.w(str, sb4);
                    j = 0;
                    z = false;
                    if (!z) {
                    }
                    s3 = (short) (s + 1);
                    i5 = i;
                    readShort = s2;
                }
                if (byteOrderedDataInputStream.peek() + 4 <= byteOrderedDataInputStream2.mLength) {
                    int readInt3 = byteOrderedDataInputStream.readInt();
                    long j6 = (long) readInt3;
                    if (j6 <= 0 || readInt3 >= byteOrderedDataInputStream2.mLength) {
                        sb = new StringBuilder();
                        str2 = "Stop reading file since a wrong offset may cause an infinite loop: ";
                    } else if (!this.mAttributesOffsets.contains(Integer.valueOf(readInt3))) {
                        byteOrderedDataInputStream2.seek(j6);
                        int i10 = 4;
                        if (!this.mAttributes[4].isEmpty()) {
                            i10 = 5;
                        }
                        readImageFileDirectory(byteOrderedDataInputStream2, i10);
                    } else {
                        sb = new StringBuilder();
                        str2 = "Stop reading file since re-reading an IFD may cause an infinite loop: ";
                    }
                    sb.append(str2);
                    sb.append(readInt3);
                    Log.w(str, sb.toString());
                }
            }
        }
    }

    private void removeAttribute(String str) {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            this.mAttributes[i].remove(str);
        }
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) {
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_WIDTH);
        if (((ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_LENGTH)) == null || exifAttribute == null) {
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get(TAG_JPEG_INTERCHANGE_FORMAT);
            if (exifAttribute2 != null) {
                getJpegAttributes(byteOrderedDataInputStream, exifAttribute2.getIntValue(this.mExifByteOrder), i);
            }
        }
    }

    private void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        String str = "Invalid marker";
        if (dataInputStream.readByte() == -1) {
            byteOrderedDataOutputStream.writeByte(-1);
            if (dataInputStream.readByte() == -40) {
                byteOrderedDataOutputStream.writeByte(-40);
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(-31);
                writeExifSegment(byteOrderedDataOutputStream, 6);
                byte[] bArr = new byte[4096];
                while (dataInputStream.readByte() == -1) {
                    byte readByte = dataInputStream.readByte();
                    if (readByte == -39 || readByte == -38) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        copy(dataInputStream, byteOrderedDataOutputStream);
                        return;
                    }
                    String str2 = "Invalid length";
                    if (readByte != -31) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        int readUnsignedShort = dataInputStream.readUnsignedShort();
                        byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort);
                        int i = readUnsignedShort - 2;
                        if (i >= 0) {
                            while (i > 0) {
                                int read = dataInputStream.read(bArr, 0, Math.min(i, bArr.length));
                                if (read < 0) {
                                    break;
                                }
                                byteOrderedDataOutputStream.write(bArr, 0, read);
                                i -= read;
                            }
                        } else {
                            throw new IOException(str2);
                        }
                    } else {
                        int readUnsignedShort2 = dataInputStream.readUnsignedShort() - 2;
                        if (readUnsignedShort2 >= 0) {
                            byte[] bArr2 = new byte[6];
                            if (readUnsignedShort2 >= 6) {
                                if (dataInputStream.read(bArr2) != 6) {
                                    throw new IOException("Invalid exif");
                                } else if (Arrays.equals(bArr2, IDENTIFIER_EXIF_APP1)) {
                                    int i2 = readUnsignedShort2 - 6;
                                    if (dataInputStream.skipBytes(i2) != i2) {
                                        throw new IOException(str2);
                                    }
                                }
                            }
                            byteOrderedDataOutputStream.writeByte(-1);
                            byteOrderedDataOutputStream.writeByte(readByte);
                            byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort2 + 2);
                            if (readUnsignedShort2 >= 6) {
                                readUnsignedShort2 -= 6;
                                byteOrderedDataOutputStream.write(bArr2);
                            }
                            while (readUnsignedShort2 > 0) {
                                int read2 = dataInputStream.read(bArr, 0, Math.min(readUnsignedShort2, bArr.length));
                                if (read2 < 0) {
                                    break;
                                }
                                byteOrderedDataOutputStream.write(bArr, 0, read2);
                                readUnsignedShort2 -= read2;
                            }
                        } else {
                            throw new IOException(str2);
                        }
                    }
                }
                throw new IOException(str);
            }
            throw new IOException(str);
        }
        throw new IOException(str);
    }

    private void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) {
        HashMap hashMap = this.mAttributes[4];
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_COMPRESSION);
        if (exifAttribute != null) {
            this.mThumbnailCompression = exifAttribute.getIntValue(this.mExifByteOrder);
            int i = this.mThumbnailCompression;
            if (i != 1) {
                if (i != 6) {
                    if (i != 7) {
                        return;
                    }
                }
            }
            if (isSupportedDataType(hashMap)) {
                handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                return;
            }
            return;
        }
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
    }

    private static boolean startsWith(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    private void swapBasedOnImageSize(int i, int i2) {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            HashMap hashMap = this.mAttributes[i];
            String str = TAG_IMAGE_LENGTH;
            ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(str);
            HashMap hashMap2 = this.mAttributes[i];
            String str2 = TAG_IMAGE_WIDTH;
            ExifAttribute exifAttribute2 = (ExifAttribute) hashMap2.get(str2);
            ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i2].get(str);
            ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i2].get(str2);
            if (exifAttribute != null && exifAttribute2 != null && exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap[] hashMapArr = this.mAttributes;
                    HashMap hashMap3 = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap3;
                }
            }
        }
    }

    private boolean updateAttribute(String str, ExifAttribute exifAttribute) {
        boolean z = false;
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            if (this.mAttributes[i].containsKey(str)) {
                this.mAttributes[i].put(str, exifAttribute);
                z = true;
            }
        }
        return z;
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        StringBuilder sb;
        String arrays;
        ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i].get(TAG_DEFAULT_CROP_SIZE);
        ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_TOP_BORDER);
        ExifAttribute exifAttribute5 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_LEFT_BORDER);
        ExifAttribute exifAttribute6 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_BOTTOM_BORDER);
        ExifAttribute exifAttribute7 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_RIGHT_BORDER);
        String str = TAG_IMAGE_LENGTH;
        String str2 = TAG_IMAGE_WIDTH;
        if (exifAttribute3 != null) {
            int i2 = exifAttribute3.format;
            String str3 = "Invalid crop size values. cropSize=";
            String str4 = TAG;
            if (i2 == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    sb = new StringBuilder();
                    sb.append(str3);
                    arrays = Arrays.toString(rationalArr);
                } else {
                    exifAttribute2 = ExifAttribute.createURational(rationalArr[0], this.mExifByteOrder);
                    exifAttribute = ExifAttribute.createURational(rationalArr[1], this.mExifByteOrder);
                    this.mAttributes[i].put(str2, exifAttribute2);
                    this.mAttributes[i].put(str, exifAttribute);
                }
            } else {
                int[] iArr = (int[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    sb = new StringBuilder();
                    sb.append(str3);
                    arrays = Arrays.toString(iArr);
                } else {
                    exifAttribute2 = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                    exifAttribute = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
                    this.mAttributes[i].put(str2, exifAttribute2);
                    this.mAttributes[i].put(str, exifAttribute);
                }
            }
            sb.append(arrays);
            Log.w(str4, sb.toString());
        } else if (exifAttribute4 == null || exifAttribute5 == null || exifAttribute6 == null || exifAttribute7 == null) {
            retrieveJpegImageSize(byteOrderedDataInputStream, i);
        } else {
            int intValue = exifAttribute4.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute6.getIntValue(this.mExifByteOrder);
            int intValue3 = exifAttribute7.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute5.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                int i3 = intValue3 - intValue4;
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(i3, this.mExifByteOrder);
                this.mAttributes[i].put(str, createUShort);
                this.mAttributes[i].put(str2, createUShort2);
            }
        }
    }

    private void validateImages(InputStream inputStream) {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_X_DIMENSION);
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_Y_DIMENSION);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, exifAttribute);
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, exifAttribute2);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d(TAG, "No image meets the size requirements of a thumbnail image.");
        }
    }

    private int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream, int i) {
        ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = byteOrderedDataOutputStream;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        int[] iArr = new int[exifTagArr.length];
        int[] iArr2 = new int[exifTagArr.length];
        for (ExifTag exifTag : EXIF_POINTER_TAGS) {
            removeAttribute(exifTag.name);
        }
        removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
        removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        for (int i2 = 0; i2 < EXIF_TAGS.length; i2++) {
            for (Object obj : this.mAttributes[i2].entrySet().toArray()) {
                Entry entry = (Entry) obj;
                if (entry.getValue() == null) {
                    this.mAttributes[i2].remove(entry.getKey());
                }
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0, this.mExifByteOrder));
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong((long) this.mThumbnailLength, this.mExifByteOrder));
        }
        for (int i3 = 0; i3 < EXIF_TAGS.length; i3++) {
            int i4 = 0;
            for (Entry value : this.mAttributes[i3].entrySet()) {
                int size = ((ExifAttribute) value.getValue()).size();
                if (size > 4) {
                    i4 += size;
                }
            }
            iArr2[i3] = iArr2[i3] + i4;
        }
        int i5 = 8;
        for (int i6 = 0; i6 < EXIF_TAGS.length; i6++) {
            if (!this.mAttributes[i6].isEmpty()) {
                iArr[i6] = i5;
                i5 += (this.mAttributes[i6].size() * 12) + 2 + 4 + iArr2[i6];
            }
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong((long) i5, this.mExifByteOrder));
            this.mThumbnailOffset = i + i5;
            i5 += this.mThumbnailLength;
        }
        int i7 = i5 + 8;
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong((long) iArr[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong((long) iArr[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong((long) iArr[3], this.mExifByteOrder));
        }
        byteOrderedDataOutputStream2.writeUnsignedShort(i7);
        byteOrderedDataOutputStream2.write(IDENTIFIER_EXIF_APP1);
        byteOrderedDataOutputStream2.writeShort(this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? BYTE_ALIGN_MM : BYTE_ALIGN_II);
        byteOrderedDataOutputStream2.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream2.writeUnsignedShort(42);
        byteOrderedDataOutputStream2.writeUnsignedInt(8);
        for (int i8 = 0; i8 < EXIF_TAGS.length; i8++) {
            if (!this.mAttributes[i8].isEmpty()) {
                byteOrderedDataOutputStream2.writeUnsignedShort(this.mAttributes[i8].size());
                int size2 = iArr[i8] + 2 + (this.mAttributes[i8].size() * 12) + 4;
                for (Entry entry2 : this.mAttributes[i8].entrySet()) {
                    int i9 = ((ExifTag) sExifTagMapsForWriting[i8].get(entry2.getKey())).number;
                    ExifAttribute exifAttribute = (ExifAttribute) entry2.getValue();
                    int size3 = exifAttribute.size();
                    byteOrderedDataOutputStream2.writeUnsignedShort(i9);
                    byteOrderedDataOutputStream2.writeUnsignedShort(exifAttribute.format);
                    byteOrderedDataOutputStream2.writeInt(exifAttribute.numberOfComponents);
                    if (size3 > 4) {
                        byteOrderedDataOutputStream2.writeUnsignedInt((long) size2);
                        size2 += size3;
                    } else {
                        byteOrderedDataOutputStream2.write(exifAttribute.bytes);
                        if (size3 < 4) {
                            while (size3 < 4) {
                                byteOrderedDataOutputStream2.writeByte(0);
                                size3++;
                            }
                        }
                    }
                }
                if (i8 != 0 || this.mAttributes[4].isEmpty()) {
                    byteOrderedDataOutputStream2.writeUnsignedInt(0);
                } else {
                    byteOrderedDataOutputStream2.writeUnsignedInt((long) iArr[4]);
                }
                for (Entry value2 : this.mAttributes[i8].entrySet()) {
                    byte[] bArr = ((ExifAttribute) value2.getValue()).bytes;
                    if (bArr.length > 4) {
                        byteOrderedDataOutputStream2.write(bArr, 0, bArr.length);
                    }
                }
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream2.write(getThumbnailBytes());
        }
        byteOrderedDataOutputStream2.setByteOrder(ByteOrder.BIG_ENDIAN);
        return i7;
    }

    public void flipHorizontally() {
        int i = 1;
        String str = TAG_ORIENTATION;
        switch (getAttributeInt(str, 1)) {
            case 1:
                i = 2;
                break;
            case 2:
                break;
            case 3:
                i = 4;
                break;
            case 4:
                i = 3;
                break;
            case 5:
                i = 6;
                break;
            case 6:
                i = 5;
                break;
            case 7:
                i = 8;
                break;
            case 8:
                i = 7;
                break;
            default:
                i = 0;
                break;
        }
        setAttribute(str, Integer.toString(i));
    }

    public void flipVertically() {
        int i = 1;
        String str = TAG_ORIENTATION;
        switch (getAttributeInt(str, 1)) {
            case 1:
                i = 4;
                break;
            case 2:
                i = 3;
                break;
            case 3:
                i = 2;
                break;
            case 4:
                break;
            case 5:
                i = 8;
                break;
            case 6:
                i = 7;
                break;
            case 7:
                i = 6;
                break;
            case 8:
                i = 5;
                break;
            default:
                i = 0;
                break;
        }
        setAttribute(str, Integer.toString(i));
    }

    public double getAltitude(double d) {
        double attributeDouble = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
        int attributeInt = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (attributeDouble < 0.0d || attributeInt < 0) {
            return d;
        }
        int i = 1;
        if (attributeInt == 1) {
            i = -1;
        }
        return attributeDouble * ((double) i);
    }

    @Nullable
    public String getAttribute(@NonNull String str) {
        String sb;
        if (str != null) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute != null) {
                if (!sTagSetForCompatibility.contains(str)) {
                    return exifAttribute.getStringValue(this.mExifByteOrder);
                }
                if (str.equals(TAG_GPS_TIMESTAMP)) {
                    int i = exifAttribute.format;
                    String str2 = TAG;
                    if (i == 5 || i == 10) {
                        Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                        if (rationalArr == null || rationalArr.length != 3) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Invalid GPS Timestamp array. array=");
                            sb2.append(Arrays.toString(rationalArr));
                            sb = sb2.toString();
                        } else {
                            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) rationalArr[0].numerator) / ((float) rationalArr[0].denominator))), Integer.valueOf((int) (((float) rationalArr[1].numerator) / ((float) rationalArr[1].denominator))), Integer.valueOf((int) (((float) rationalArr[2].numerator) / ((float) rationalArr[2].denominator)))});
                        }
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("GPS Timestamp format is not rational. format=");
                        sb3.append(exifAttribute.format);
                        sb = sb3.toString();
                    }
                    Log.w(str2, sb);
                    return null;
                }
                try {
                    return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
                } catch (NumberFormatException unused) {
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    @Nullable
    public byte[] getAttributeBytes(@NonNull String str) {
        if (str != null) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute != null) {
                return exifAttribute.bytes;
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public double getAttributeDouble(@NonNull String str, double d) {
        if (str != null) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute == null) {
                return d;
            }
            try {
                return exifAttribute.getDoubleValue(this.mExifByteOrder);
            } catch (NumberFormatException unused) {
                return d;
            }
        } else {
            throw new NullPointerException("tag shouldn't be null");
        }
    }

    public int getAttributeInt(@NonNull String str, int i) {
        if (str != null) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute == null) {
                return i;
            }
            try {
                return exifAttribute.getIntValue(this.mExifByteOrder);
            } catch (NumberFormatException unused) {
                return i;
            }
        } else {
            throw new NullPointerException("tag shouldn't be null");
        }
    }

    @Nullable
    public long[] getAttributeRange(@NonNull String str) {
        if (str == null) {
            throw new NullPointerException("tag shouldn't be null");
        } else if (!this.mModified) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute == null) {
                return null;
            }
            return new long[]{exifAttribute.bytesOffset, (long) exifAttribute.bytes.length};
        } else {
            throw new IllegalStateException("The underlying file has been modified since being parsed");
        }
    }

    @RestrictTo({Scope.LIBRARY})
    public long getDateTime() {
        return parseDateTime(getAttribute(TAG_DATETIME), getAttribute(TAG_SUBSEC_TIME));
    }

    @RestrictTo({Scope.LIBRARY})
    public long getDateTimeDigitized() {
        return parseDateTime(getAttribute(TAG_DATETIME_DIGITIZED), getAttribute(TAG_SUBSEC_TIME_DIGITIZED));
    }

    @RestrictTo({Scope.LIBRARY})
    public long getDateTimeOriginal() {
        return parseDateTime(getAttribute(TAG_DATETIME_ORIGINAL), getAttribute(TAG_SUBSEC_TIME_ORIGINAL));
    }

    @RestrictTo({Scope.LIBRARY})
    public long getGpsDateTime() {
        String attribute = getAttribute(TAG_GPS_DATESTAMP);
        String attribute2 = getAttribute(TAG_GPS_TIMESTAMP);
        if (!(attribute == null || attribute2 == null || (!sNonZeroTimePattern.matcher(attribute).matches() && !sNonZeroTimePattern.matcher(attribute2).matches()))) {
            StringBuilder sb = new StringBuilder();
            sb.append(attribute);
            sb.append(' ');
            sb.append(attribute2);
            try {
                Date parse = sFormatter.parse(sb.toString(), new ParsePosition(0));
                if (parse == null) {
                    return -1;
                }
                return parse.getTime();
            } catch (IllegalArgumentException unused) {
            }
        }
        return -1;
    }

    @Deprecated
    public boolean getLatLong(float[] fArr) {
        double[] latLong = getLatLong();
        if (latLong == null) {
            return false;
        }
        fArr[0] = (float) latLong[0];
        fArr[1] = (float) latLong[1];
        return true;
    }

    @Nullable
    public double[] getLatLong() {
        String attribute = getAttribute(TAG_GPS_LATITUDE);
        String attribute2 = getAttribute(TAG_GPS_LATITUDE_REF);
        String attribute3 = getAttribute(TAG_GPS_LONGITUDE);
        String attribute4 = getAttribute(TAG_GPS_LONGITUDE_REF);
        if (!(attribute == null || attribute2 == null || attribute3 == null || attribute4 == null)) {
            try {
                return new double[]{convertRationalLatLonToDouble(attribute, attribute2), convertRationalLatLonToDouble(attribute3, attribute4)};
            } catch (IllegalArgumentException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("Latitude/longitude values are not parseable. ");
                sb.append(String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", new Object[]{attribute, attribute2, attribute3, attribute4}));
                Log.w(TAG, sb.toString());
            }
        }
        return null;
    }

    public int getRotationDegrees() {
        switch (getAttributeInt(TAG_ORIENTATION, 1)) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 8:
                return 270;
            case 6:
            case 7:
                return 90;
            default:
                return 0;
        }
    }

    @Nullable
    public byte[] getThumbnail() {
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return getThumbnailBytes();
        }
        return null;
    }

    @Nullable
    public Bitmap getThumbnailBitmap() {
        if (!this.mHasThumbnail) {
            return null;
        }
        if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = getThumbnailBytes();
        }
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength);
        }
        if (i == 1) {
            int[] iArr = new int[(this.mThumbnailBytes.length / 3)];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                byte[] bArr = this.mThumbnailBytes;
                int i3 = i2 * 3;
                iArr[i2] = (bArr[i3] << 16) + 0 + (bArr[i3 + 1] << 8) + bArr[i3 + 2];
            }
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_LENGTH);
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_WIDTH);
            if (!(exifAttribute == null || exifAttribute2 == null)) {
                return Bitmap.createBitmap(iArr, exifAttribute2.getIntValue(this.mExifByteOrder), exifAttribute.getIntValue(this.mExifByteOrder), Config.ARGB_8888);
            }
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v3, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v7, types: [java.io.Closeable, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r3v9, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* JADX WARNING: type inference failed for: r1v15, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r1v18 */
    /* JADX WARNING: type inference failed for: r1v19 */
    /* JADX WARNING: type inference failed for: r1v20 */
    /* JADX WARNING: type inference failed for: r1v21 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public byte[] getThumbnailBytes() {
        ? r1;
        ? r12;
        ? r13;
        String str = TAG;
        if (!this.mHasThumbnail) {
            return null;
        }
        byte[] bArr = this.mThumbnailBytes;
        if (bArr != null) {
            return bArr;
        }
        try {
            if (this.mAssetInputStream != null) {
                AssetInputStream assetInputStream = this.mAssetInputStream;
                try {
                    if (assetInputStream.markSupported()) {
                        assetInputStream.reset();
                        r13 = assetInputStream;
                    } else {
                        Log.d(str, "Cannot read thumbnail from inputstream without mark/reset support");
                        closeQuietly(assetInputStream);
                        return null;
                    }
                } catch (Exception e) {
                    e = e;
                    r12 = r13;
                    try {
                        Log.d(str, "Encountered exception while getting thumbnail", e);
                        closeQuietly(r12);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        r1 = r12;
                        closeQuietly(r1);
                        throw th;
                    }
                }
            } else if (this.mFilename != null) {
                r13 = new FileInputStream(this.mFilename);
            } else if (VERSION.SDK_INT < 21 || this.mSeekableFileDescriptor == null) {
                r13 = 0;
            } else {
                FileDescriptor dup = Os.dup(this.mSeekableFileDescriptor);
                Os.lseek(dup, 0, OsConstants.SEEK_SET);
                r13 = new FileInputStream(dup);
            }
            if (r13 != 0) {
                String str2 = "Corrupted image";
                if (r13.skip((long) this.mThumbnailOffset) == ((long) this.mThumbnailOffset)) {
                    byte[] bArr2 = new byte[this.mThumbnailLength];
                    if (r13.read(bArr2) == this.mThumbnailLength) {
                        this.mThumbnailBytes = bArr2;
                        closeQuietly(r13);
                        return bArr2;
                    }
                    throw new IOException(str2);
                }
                throw new IOException(str2);
            }
            throw new FileNotFoundException();
        } catch (Exception e2) {
            e = e2;
            r12 = 0;
            Log.d(str, "Encountered exception while getting thumbnail", e);
            closeQuietly(r12);
            return null;
        } catch (Throwable th2) {
            th = th2;
            r1 = 0;
            closeQuietly(r1);
            throw th;
        }
    }

    @Nullable
    public long[] getThumbnailRange() {
        if (this.mModified) {
            throw new IllegalStateException("The underlying file has been modified since being parsed");
        } else if (!this.mHasThumbnail) {
            return null;
        } else {
            return new long[]{(long) this.mThumbnailOffset, (long) this.mThumbnailLength};
        }
    }

    public boolean hasAttribute(@NonNull String str) {
        return getExifAttribute(str) != null;
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean isFlipped() {
        int attributeInt = getAttributeInt(TAG_ORIENTATION, 1);
        return attributeInt == 2 || attributeInt == 7 || attributeInt == 4 || attributeInt == 5;
    }

    public boolean isThumbnailCompressed() {
        if (!this.mHasThumbnail) {
            return false;
        }
        int i = this.mThumbnailCompression;
        return i == 6 || i == 7;
    }

    public void resetOrientation() {
        setAttribute(TAG_ORIENTATION, Integer.toString(1));
    }

    public void rotate(int i) {
        int i2;
        int i3;
        List list;
        if (i % 90 == 0) {
            String str = TAG_ORIENTATION;
            int attributeInt = getAttributeInt(str, 1);
            int i4 = 0;
            if (ROTATION_ORDER.contains(Integer.valueOf(attributeInt))) {
                int indexOf = (ROTATION_ORDER.indexOf(Integer.valueOf(attributeInt)) + (i / 90)) % 4;
                if (indexOf < 0) {
                    i4 = 4;
                }
                i3 = indexOf + i4;
                list = ROTATION_ORDER;
            } else {
                if (FLIPPED_ROTATION_ORDER.contains(Integer.valueOf(attributeInt))) {
                    int indexOf2 = (FLIPPED_ROTATION_ORDER.indexOf(Integer.valueOf(attributeInt)) + (i / 90)) % 4;
                    if (indexOf2 < 0) {
                        i2 = 4;
                    }
                    i3 = indexOf2 + i2;
                    list = FLIPPED_ROTATION_ORDER;
                }
                setAttribute(str, Integer.toString(i4));
                return;
            }
            i4 = ((Integer) list.get(i3)).intValue();
            setAttribute(str, Integer.toString(i4));
            return;
        }
        throw new IllegalArgumentException("degree should be a multiple of 90");
    }

    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v6, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r7v4, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r6v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r6v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v8, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r6v5, types: [java.io.Closeable, java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r3v9, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v10, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r7v15, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r7v19 */
    /* JADX WARNING: type inference failed for: r1v16 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r6v19 */
    /* JADX WARNING: type inference failed for: r7v20 */
    /* JADX WARNING: type inference failed for: r7v21 */
    /* JADX WARNING: type inference failed for: r7v22 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r7v1
  assigns: []
  uses: []
  mth insns count: 134
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
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00b4 A[Catch:{ Exception -> 0x00e7, all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00bc A[Catch:{ Exception -> 0x00e7, all -> 0x00e5 }] */
    /* JADX WARNING: Unknown variable types count: 19 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveAttributes() {
        ? r7;
        ? r72;
        ? r73;
        ? r6;
        File file;
        ? r62;
        ? r63;
        ? r3;
        ? r32;
        ? r33;
        ? r64;
        ? r74;
        ? r75;
        String str = "Failed to copy file";
        if (!this.mIsSupportedFile || this.mMimeType != 4) {
            throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
        } else if (this.mSeekableFileDescriptor == null && this.mFilename == null) {
            throw new IOException("ExifInterface does not support saving attributes for the current input.");
        } else {
            this.mModified = true;
            this.mThumbnailBytes = getThumbnail();
            ? r1 = 0;
            try {
                if (this.mFilename != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mFilename);
                    sb.append(".tmp");
                    file = new File(sb.toString());
                    if (new File(this.mFilename).renameTo(file)) {
                        r64 = 0;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Couldn't rename to ");
                        sb2.append(file.getAbsolutePath());
                        throw new IOException(sb2.toString());
                    }
                } else if (VERSION.SDK_INT < 21 || this.mSeekableFileDescriptor == null) {
                    file = null;
                    r64 = 0;
                } else {
                    file = File.createTempFile("temp", "jpg");
                    Os.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);
                    ? fileInputStream = new FileInputStream(this.mSeekableFileDescriptor);
                    try {
                        r75 = new FileOutputStream(file);
                    } catch (Exception unused) {
                        ? r76 = 0;
                        ? r12 = fileInputStream;
                        r72 = r76;
                        try {
                            throw new IOException(str);
                        } catch (Throwable th) {
                            th = th;
                            r7 = r72;
                            r1 = r12;
                            closeQuietly(r1);
                            closeQuietly(r7);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r75 = 0;
                        r1 = fileInputStream;
                        r7 = r74;
                        closeQuietly(r1);
                        closeQuietly(r7);
                        throw th;
                    }
                    try {
                        copy(fileInputStream, r75);
                        r6 = fileInputStream;
                        r73 = r75;
                        closeQuietly(r6);
                        closeQuietly(r73);
                        r62 = new FileInputStream(file);
                        if (this.mFilename == null) {
                            r33 = new FileOutputStream(this.mFilename);
                        } else if (VERSION.SDK_INT < 21 || this.mSeekableFileDescriptor == null) {
                            r33 = 0;
                        } else {
                            Os.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);
                            r33 = new FileOutputStream(this.mSeekableFileDescriptor);
                        }
                        try {
                            saveJpegAttributes(r62, r33);
                            closeQuietly(r62);
                            closeQuietly(r33);
                            file.delete();
                            this.mThumbnailBytes = null;
                        } catch (Exception unused2) {
                            ? r13 = r62;
                            r32 = r33;
                            try {
                                throw new IOException(str);
                            } catch (Throwable th3) {
                                th = th3;
                                r63 = r13;
                                r3 = r32;
                                r1 = r3;
                                r62 = r63;
                                closeQuietly(r62);
                                closeQuietly(r1);
                                file.delete();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            r63 = r62;
                            r3 = r33;
                            r1 = r3;
                            r62 = r63;
                            closeQuietly(r62);
                            closeQuietly(r1);
                            file.delete();
                            throw th;
                        }
                    } catch (Exception unused3) {
                    } catch (Throwable th5) {
                        th = th5;
                        r1 = fileInputStream;
                        r7 = r74;
                        closeQuietly(r1);
                        closeQuietly(r7);
                        throw th;
                    }
                }
                r73 = r64;
                r6 = r64;
                closeQuietly(r6);
                closeQuietly(r73);
                try {
                    r62 = new FileInputStream(file);
                    try {
                        if (this.mFilename == null) {
                        }
                        saveJpegAttributes(r62, r33);
                        closeQuietly(r62);
                        closeQuietly(r33);
                        file.delete();
                        this.mThumbnailBytes = null;
                    } catch (Exception unused4) {
                        r33 = 0;
                        ? r132 = r62;
                        r32 = r33;
                        throw new IOException(str);
                    } catch (Throwable th6) {
                        th = th6;
                        closeQuietly(r62);
                        closeQuietly(r1);
                        file.delete();
                        throw th;
                    }
                } catch (Exception unused5) {
                    r32 = 0;
                    throw new IOException(str);
                } catch (Throwable th7) {
                    th = th7;
                    r62 = 0;
                    r1 = r1;
                    closeQuietly(r62);
                    closeQuietly(r1);
                    file.delete();
                    throw th;
                }
            } catch (Exception unused6) {
                r72 = 0;
                throw new IOException(str);
            } catch (Throwable th8) {
                th = th8;
                r7 = 0;
                closeQuietly(r1);
                closeQuietly(r7);
                throw th;
            }
        }
    }

    public void setAltitude(double d) {
        String str = d >= 0.0d ? "0" : "1";
        setAttribute(TAG_GPS_ALTITUDE, new Rational(Math.abs(d)).toString());
        setAttribute(TAG_GPS_ALTITUDE_REF, str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:75:0x023d, code lost:
        r3.put(r1, r4);
        r4 = r17;
        r15 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x02e1, code lost:
        r3.put(r1, r4);
        r4 = r17;
     */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01b9  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01de  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x021b  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0280  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x02a5  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x02ca  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x02d6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAttribute(@NonNull String str, @Nullable String str2) {
        int i;
        int i2;
        String str3;
        ExifAttribute exifAttribute;
        HashMap hashMap;
        String str4;
        ExifAttribute exifAttribute2;
        HashMap hashMap2;
        String str5;
        StringBuilder sb;
        String str6 = str;
        String str7 = str2;
        if (str6 != null) {
            if (TAG_ISO_SPEED_RATINGS.equals(str6)) {
                str6 = TAG_PHOTOGRAPHIC_SENSITIVITY;
            }
            int i3 = 2;
            String str8 = TAG;
            int i4 = 1;
            if (str7 != null && sTagSetForCompatibility.contains(str6)) {
                String str9 = " : ";
                String str10 = "Invalid value for ";
                if (str6.equals(TAG_GPS_TIMESTAMP)) {
                    Matcher matcher = sGpsTimestampPattern.matcher(str7);
                    if (!matcher.find()) {
                        sb = new StringBuilder();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(Integer.parseInt(matcher.group(1)));
                        String str11 = "/1,";
                        sb2.append(str11);
                        sb2.append(Integer.parseInt(matcher.group(2)));
                        sb2.append(str11);
                        sb2.append(Integer.parseInt(matcher.group(3)));
                        sb2.append("/1");
                        str7 = sb2.toString();
                    }
                } else {
                    try {
                        str7 = new Rational(Double.parseDouble(str2)).toString();
                    } catch (NumberFormatException unused) {
                        sb = new StringBuilder();
                    }
                }
                sb.append(str10);
                sb.append(str6);
                sb.append(str9);
                sb.append(str7);
                Log.w(str8, sb.toString());
                return;
            }
            int i5 = 0;
            int i6 = 0;
            while (i6 < EXIF_TAGS.length) {
                if (i6 != 4 || this.mHasThumbnail) {
                    ExifTag exifTag = (ExifTag) sExifTagMapsForWriting[i6].get(str6);
                    if (exifTag != null) {
                        if (str7 != null) {
                            Pair guessDataFormat = guessDataFormat(str7);
                            int i7 = -1;
                            if (!(exifTag.primaryFormat == ((Integer) guessDataFormat.first).intValue() || exifTag.primaryFormat == ((Integer) guessDataFormat.second).intValue())) {
                                int i8 = exifTag.secondaryFormat;
                                if (i8 != -1 && (i8 == ((Integer) guessDataFormat.first).intValue() || exifTag.secondaryFormat == ((Integer) guessDataFormat.second).intValue())) {
                                    i2 = exifTag.secondaryFormat;
                                    String str12 = "/";
                                    String str13 = ",";
                                    switch (i2) {
                                        case 1:
                                            str3 = str8;
                                            i = i4;
                                            hashMap = this.mAttributes[i6];
                                            exifAttribute = ExifAttribute.createByte(str7);
                                            break;
                                        case 2:
                                        case 7:
                                            str3 = str8;
                                            i = i4;
                                            hashMap = this.mAttributes[i6];
                                            exifAttribute = ExifAttribute.createString(str7);
                                            break;
                                        case 3:
                                            str3 = str8;
                                            i = i4;
                                            String[] split = str7.split(str13, -1);
                                            int[] iArr = new int[split.length];
                                            for (int i9 = 0; i9 < split.length; i9++) {
                                                iArr[i9] = Integer.parseInt(split[i9]);
                                            }
                                            hashMap = this.mAttributes[i6];
                                            exifAttribute = ExifAttribute.createUShort(iArr, this.mExifByteOrder);
                                            break;
                                        case 4:
                                            str3 = str8;
                                            i = i4;
                                            String[] split2 = str7.split(str13, -1);
                                            long[] jArr = new long[split2.length];
                                            for (int i10 = 0; i10 < split2.length; i10++) {
                                                jArr[i10] = Long.parseLong(split2[i10]);
                                            }
                                            hashMap = this.mAttributes[i6];
                                            exifAttribute = ExifAttribute.createULong(jArr, this.mExifByteOrder);
                                            break;
                                        case 5:
                                            str3 = str8;
                                            String[] split3 = str7.split(str13, -1);
                                            Rational[] rationalArr = new Rational[split3.length];
                                            int i11 = 0;
                                            while (i11 < split3.length) {
                                                String[] split4 = split3[i11].split(str12, i7);
                                                rationalArr[i11] = new Rational((long) Double.parseDouble(split4[0]), (long) Double.parseDouble(split4[1]));
                                                i11++;
                                                i7 = -1;
                                            }
                                            i = 1;
                                            hashMap = this.mAttributes[i6];
                                            exifAttribute = ExifAttribute.createURational(rationalArr, this.mExifByteOrder);
                                            break;
                                        case 9:
                                            str4 = str8;
                                            String[] split5 = str7.split(str13, -1);
                                            int[] iArr2 = new int[split5.length];
                                            for (int i12 = 0; i12 < split5.length; i12++) {
                                                iArr2[i12] = Integer.parseInt(split5[i12]);
                                            }
                                            hashMap2 = this.mAttributes[i6];
                                            exifAttribute2 = ExifAttribute.createSLong(iArr2, this.mExifByteOrder);
                                            break;
                                        case 10:
                                            String[] split6 = str7.split(str13, -1);
                                            Rational[] rationalArr2 = new Rational[split6.length];
                                            int i13 = i5;
                                            while (i13 < split6.length) {
                                                String[] split7 = split6[i13].split(str12, -1);
                                                String str14 = str8;
                                                rationalArr2[i13] = new Rational((long) Double.parseDouble(split7[i5]), (long) Double.parseDouble(split7[i4]));
                                                i13++;
                                                str8 = str14;
                                                i4 = 1;
                                                i5 = 0;
                                            }
                                            str4 = str8;
                                            hashMap2 = this.mAttributes[i6];
                                            exifAttribute2 = ExifAttribute.createSRational(rationalArr2, this.mExifByteOrder);
                                            break;
                                        case 12:
                                            String[] split8 = str7.split(str13, -1);
                                            double[] dArr = new double[split8.length];
                                            for (int i14 = i5; i14 < split8.length; i14++) {
                                                dArr[i14] = Double.parseDouble(split8[i14]);
                                            }
                                            this.mAttributes[i6].put(str6, ExifAttribute.createDouble(dArr, this.mExifByteOrder));
                                            break;
                                        default:
                                            String str15 = str8;
                                            i = i4;
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append("Data format isn't one of expected formats: ");
                                            sb3.append(i2);
                                            str8 = str15;
                                            Log.w(str8, sb3.toString());
                                            break;
                                    }
                                } else {
                                    int i15 = exifTag.primaryFormat;
                                    if (!(i15 == i4 || i15 == 7 || i15 == i3)) {
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append("Given tag (");
                                        sb4.append(str6);
                                        sb4.append(") value didn't match with one of expected formats: ");
                                        sb4.append(IFD_FORMAT_NAMES[exifTag.primaryFormat]);
                                        String str16 = "";
                                        String str17 = ", ";
                                        if (exifTag.secondaryFormat == -1) {
                                            str5 = str16;
                                        } else {
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append(str17);
                                            sb5.append(IFD_FORMAT_NAMES[exifTag.secondaryFormat]);
                                            str5 = sb5.toString();
                                        }
                                        sb4.append(str5);
                                        sb4.append(" (guess: ");
                                        sb4.append(IFD_FORMAT_NAMES[((Integer) guessDataFormat.first).intValue()]);
                                        if (((Integer) guessDataFormat.second).intValue() != -1) {
                                            StringBuilder sb6 = new StringBuilder();
                                            sb6.append(str17);
                                            sb6.append(IFD_FORMAT_NAMES[((Integer) guessDataFormat.second).intValue()]);
                                            str16 = sb6.toString();
                                        }
                                        sb4.append(str16);
                                        sb4.append(")");
                                        Log.w(str8, sb4.toString());
                                    }
                                }
                            }
                            i2 = exifTag.primaryFormat;
                            String str122 = "/";
                            String str132 = ",";
                            switch (i2) {
                                case 1:
                                    break;
                                case 2:
                                case 7:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 9:
                                    break;
                                case 10:
                                    break;
                                case 12:
                                    break;
                            }
                        } else {
                            this.mAttributes[i6].remove(str6);
                        }
                    }
                }
                i = i4;
                i6++;
                i4 = i;
                i3 = 2;
                i5 = 0;
            }
            return;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    @RestrictTo({Scope.LIBRARY})
    public void setDateTime(long j) {
        long j2 = j % 1000;
        setAttribute(TAG_DATETIME, sFormatter.format(new Date(j)));
        setAttribute(TAG_SUBSEC_TIME, Long.toString(j2));
    }

    public void setGpsInfo(Location location) {
        if (location != null) {
            setAttribute(TAG_GPS_PROCESSING_METHOD, location.getProvider());
            setLatLong(location.getLatitude(), location.getLongitude());
            setAltitude(location.getAltitude());
            setAttribute(TAG_GPS_SPEED_REF, "K");
            setAttribute(TAG_GPS_SPEED, new Rational((double) ((location.getSpeed() * ((float) TimeUnit.HOURS.toSeconds(1))) / 1000.0f)).toString());
            String[] split = sFormatter.format(new Date(location.getTime())).split("\\s+", -1);
            setAttribute(TAG_GPS_DATESTAMP, split[0]);
            setAttribute(TAG_GPS_TIMESTAMP, split[1]);
        }
    }

    public void setLatLong(double d, double d2) {
        String str = " is not valid.";
        if (d < -90.0d || d > 90.0d || Double.isNaN(d)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Latitude value ");
            sb.append(d);
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        } else if (d2 < -180.0d || d2 > 180.0d || Double.isNaN(d2)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Longitude value ");
            sb2.append(d2);
            sb2.append(str);
            throw new IllegalArgumentException(sb2.toString());
        } else {
            setAttribute(TAG_GPS_LATITUDE_REF, d >= 0.0d ? "N" : "S");
            setAttribute(TAG_GPS_LATITUDE, convertDecimalDegree(Math.abs(d)));
            setAttribute(TAG_GPS_LONGITUDE_REF, d2 >= 0.0d ? "E" : "W");
            setAttribute(TAG_GPS_LONGITUDE, convertDecimalDegree(Math.abs(d2)));
        }
    }
}
