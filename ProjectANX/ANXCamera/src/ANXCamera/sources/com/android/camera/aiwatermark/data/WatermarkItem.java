package com.android.camera.aiwatermark.data;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import com.android.camera.CameraAppImpl;
import com.android.camera.LocationManager;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WatermarkItem {
    private static final String TAG = "WatermarkItem";
    private static final ArrayList mLocationList = new ArrayList(16);
    private final String TIMER;
    private final long TIMER_INTERVAL;
    private int[] mCaptureCoordinate;
    private int[] mCoordinate;
    private int mCountry;
    private boolean mHasAltitude;
    private boolean mHasMove;
    private String mKey;
    private Rect mLimitArea;
    private int mLocation;
    private Queue mQueueCoordinate;
    private int mResId;
    private int mResRvItem;
    private String mText;
    private Bitmap mTextBitmap;
    private int mType;

    public WatermarkItem() {
        this.mKey = "";
        this.mType = 1;
        this.mText = null;
        this.mResId = -1;
        this.mLocation = 1;
        this.mCountry = -1;
        this.mResRvItem = -1;
        this.mCoordinate = new int[4];
        this.mCaptureCoordinate = new int[4];
        this.mHasMove = false;
        this.mTextBitmap = null;
        this.TIMER = "timer";
        this.TIMER_INTERVAL = 180000;
        this.mQueueCoordinate = new ConcurrentLinkedQueue();
        this.mHasAltitude = false;
    }

    public WatermarkItem(String str, int i, int i2, int i3) {
        this(str, i, null, i2, i3);
    }

    public WatermarkItem(String str, int i, String str2, int i2, int i3) {
        this.mKey = "";
        this.mType = 1;
        this.mText = null;
        this.mResId = -1;
        this.mLocation = 1;
        this.mCountry = -1;
        this.mResRvItem = -1;
        this.mCoordinate = new int[4];
        this.mCaptureCoordinate = new int[4];
        this.mHasMove = false;
        this.mTextBitmap = null;
        this.TIMER = "timer";
        this.TIMER_INTERVAL = 180000;
        this.mQueueCoordinate = new ConcurrentLinkedQueue();
        this.mHasAltitude = false;
        this.mKey = str;
        this.mType = i;
        this.mText = str2;
        this.mResId = i2;
        this.mLocation = i3;
    }

    public WatermarkItem(String str, int i, String str2, int i2, int i3, int i4) {
        this(str, i, str2, i2, i3);
        this.mCountry = i4;
    }

    public WatermarkItem(String str, int i, String str2, int i2, int i3, int i4, int i5) {
        this(str, i, str2, i2, i3, i4);
        this.mResRvItem = i5;
    }

    private ArrayList getDataFromGeocoder(Location location, Geocoder geocoder) {
        ArrayList arrayList = new ArrayList(16);
        try {
            List fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (fromLocation != null && fromLocation.size() > 0) {
                Address address = (Address) fromLocation.get(0);
                arrayList.add(getLocationName(address.getAdminArea(), address.getLocality()));
                for (int i = 0; i < 11; i++) {
                    String addressLine = address.getAddressLine(i);
                    if (addressLine != null) {
                        arrayList.add(addressLine);
                    }
                }
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[getAddress] ex = ");
            sb.append(e.getMessage());
            Log.w(str, sb.toString());
        }
        return arrayList;
    }

    private String getHourFormat(boolean z) {
        return z ? "HH" : "hh";
    }

    private String getLocationName(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (!TextUtils.isEmpty(str2)) {
                return str2;
            }
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
            return null;
        } else if (TextUtils.equals(str, str2)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str2);
            return sb.toString();
        }
    }

    private String getLocationText(String str, List list, int i, int i2) {
        String string = DataRepository.dataItemGlobal().getString(str, WatermarkConstant.DEFAULT_LOCATION);
        if (list != null && !list.isEmpty()) {
            for (int i3 = 0; i3 < list.size(); i3++) {
                if (TextUtils.equals((String) list.get(i3), string)) {
                    return string;
                }
            }
            if (i < 0 || i >= list.size()) {
                string = (String) list.get(list.size() - 1);
            } else {
                while (i >= 0 && i < list.size()) {
                    String str2 = (String) list.get(i);
                    if (!TextUtils.isEmpty(str2)) {
                        return str2;
                    }
                    i += i2;
                }
                return string;
            }
        }
        return string;
    }

    public void clearCaptureCoordinate() {
        this.mQueueCoordinate.clear();
    }

    public int[] getCaptureCoordinate() {
        return !this.mQueueCoordinate.isEmpty() ? (int[]) this.mQueueCoordinate.poll() : this.mCaptureCoordinate;
    }

    public int[] getCoordinate() {
        return this.mCoordinate;
    }

    public int getCountry() {
        return this.mCountry;
    }

    public String getKey() {
        return this.mKey;
    }

    public Rect getLimitArea() {
        return this.mLimitArea;
    }

    public int getLocation() {
        return this.mLocation;
    }

    public ArrayList getLocationList() {
        return mLocationList;
    }

    public int getResId() {
        return this.mResId;
    }

    public int getResRvItem() {
        return this.mResRvItem;
    }

    public String getText() {
        return this.mText;
    }

    public Bitmap getTextBitmap() {
        return this.mTextBitmap;
    }

    public String getTimeWatermarkString() {
        SimpleDateFormat simpleDateFormat;
        if (getTimeWatermarkType() != 2) {
            simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        return simpleDateFormat.format(Long.valueOf(System.currentTimeMillis()));
    }

    public int getTimeWatermarkType() {
        if (TextUtils.equals("time", this.mKey)) {
            return 1;
        }
        if (TextUtils.equals(WatermarkConstant.EXTEND_TIME_1, this.mKey)) {
            return 2;
        }
        return TextUtils.equals(WatermarkConstant.EXTEND_TIME_2, this.mKey) ? 3 : 0;
    }

    public int getType() {
        return this.mType;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        return r10.format(java.lang.Long.valueOf(java.lang.System.currentTimeMillis())).split(r6);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String[] getWatermarkText(String str, boolean z) {
        char c;
        SimpleDateFormat simpleDateFormat;
        String[] strArr;
        String[] strArr2;
        switch (str.hashCode()) {
            case 3560141:
                if (str.equals("time")) {
                    c = 1;
                    break;
                }
            case 37820169:
                if (str.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 5;
                    break;
                }
            case 37820170:
                if (str.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 6;
                    break;
                }
            case 1888438524:
                if (str.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                    c = 7;
                    break;
                }
            case 1901043637:
                if (str.equals("location")) {
                    c = 4;
                    break;
                }
            case 2089596377:
                if (str.equals(WatermarkConstant.EXTEND_TIME_1)) {
                    c = 2;
                    break;
                }
            case 2089596380:
                if (str.equals(WatermarkConstant.EXTEND_TIME_2)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        String str2 = ":mm";
        String str3 = "-";
        switch (c) {
            case 1:
                simpleDateFormat = new SimpleDateFormat("MMM-dd-hh:mm a", Locale.ENGLISH);
                break;
            case 2:
                simpleDateFormat = new SimpleDateFormat("HH:mm-MMM dd'th'.yyyy", Locale.ENGLISH);
                break;
            case 3:
                simpleDateFormat = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
                break;
            case 4:
                return new String[]{getLocationText(str, initLocationList(), 1, 1)};
            case 5:
                StringBuilder sb = new StringBuilder();
                sb.append("dd-MM.dd ");
                sb.append(getHourFormat(z));
                sb.append(str2);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(sb.toString(), Locale.ENGLISH);
                strArr = new String[]{simpleDateFormat2.format(Long.valueOf(System.currentTimeMillis())).split(str3)[0], simpleDateFormat2.format(Long.valueOf(System.currentTimeMillis())).split(str3)[1], getLocationText(str, initLocationList(), 2, -1)};
                break;
            case 6:
                strArr = new String[2];
                strArr[0] = WatermarkConstant.DEFAULT_LOCATION;
                strArr[0] = getLocationText(str, initLocationList(), 0, 1);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("yyyy.MM.dd ");
                sb2.append(getHourFormat(z));
                sb2.append(str2);
                strArr[1] = new SimpleDateFormat(sb2.toString(), Locale.ENGLISH).format(Long.valueOf(System.currentTimeMillis()));
                break;
            case 7:
                Location currentLocationDirectly = LocationManager.instance().getCurrentLocationDirectly();
                if (currentLocationDirectly != null) {
                    boolean hasAltitude = currentLocationDirectly.hasAltitude();
                    setHasAltitude(hasAltitude);
                    if (hasAltitude) {
                        strArr2 = new String[3];
                        String format = String.format("%.1f", new Object[]{Double.valueOf(currentLocationDirectly.getAltitude())});
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(format);
                        sb3.append("m");
                        strArr2[2] = sb3.toString();
                    } else {
                        strArr2 = new String[2];
                    }
                    double longitude = currentLocationDirectly.getLongitude();
                    String str4 = longitude > 0.0d ? WatermarkConstant.EAST : WatermarkConstant.WEST;
                    Object[] objArr = {Double.valueOf(Math.abs(longitude))};
                    String str5 = "%.3f";
                    String format2 = String.format(str5, objArr);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(format2);
                    sb4.append(str4);
                    strArr2[0] = sb4.toString();
                    double latitude = currentLocationDirectly.getLatitude();
                    String str6 = latitude > 0.0d ? WatermarkConstant.NORTH : WatermarkConstant.SOUTH;
                    String format3 = String.format(str5, new Object[]{Double.valueOf(Math.abs(latitude))});
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(format3);
                    sb5.append(str6);
                    strArr2[1] = sb5.toString();
                    return strArr2;
                }
                break;
        }
    }

    public boolean hasAltitude() {
        return this.mHasAltitude;
    }

    public boolean hasMove() {
        return this.mHasMove;
    }

    public ArrayList initLocationList() {
        long currentTimeMillis = System.currentTimeMillis();
        String str = "timer";
        if (currentTimeMillis - DataRepository.dataItemGlobal().getLong(str, 0) > 180000 || mLocationList.isEmpty()) {
            DataRepository.dataItemGlobal().putLong(str, currentTimeMillis);
            DataRepository.dataItemGlobal().commit();
            mLocationList.clear();
            Location currentLocationDirectly = LocationManager.instance().getCurrentLocationDirectly();
            if (currentLocationDirectly != null) {
                mLocationList.addAll(getDataFromGeocoder(currentLocationDirectly, new Geocoder(CameraAppImpl.getAndroidContext(), Locale.getDefault())));
            }
        } else {
            Log.d(TAG, "In 3 min do not fresh the location list");
        }
        return mLocationList;
    }

    public boolean isTextWatermark() {
        if (!TextUtils.equals("time", this.mKey)) {
            if (!TextUtils.equals(WatermarkConstant.EXTEND_TIME_1, this.mKey)) {
                if (!TextUtils.equals(WatermarkConstant.EXTEND_TIME_2, this.mKey)) {
                    if (!TextUtils.equals("location", this.mKey)) {
                        if (!TextUtils.equals(WatermarkConstant.LOCATION_TIME_1, this.mKey)) {
                            if (!TextUtils.equals(WatermarkConstant.LOCATION_TIME_2, this.mKey)) {
                                if (!TextUtils.equals(WatermarkConstant.LONGITUDE_LATITUDE, this.mKey)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void setCaptureCoordinate(int[] iArr) {
        this.mQueueCoordinate.add(iArr);
        this.mCaptureCoordinate = iArr;
    }

    public void setCountry(int i) {
        this.mCountry = i;
    }

    public void setHasAltitude(boolean z) {
        this.mHasAltitude = z;
    }

    public void setHasMove(boolean z) {
        this.mHasMove = z;
    }

    public void setKey(String str) {
        this.mKey = str;
    }

    public void setLimitArea(Rect rect) {
        this.mLimitArea = rect;
    }

    public void setLocation(int i) {
        this.mLocation = i;
    }

    public void setResId(int i) {
        this.mResId = i;
    }

    public void setResRvItem(int i) {
        this.mResRvItem = i;
    }

    public void setText(String str) {
        this.mText = str;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WatermarkItem: key is ");
        sb.append(getKey());
        sb.append("; type is ");
        sb.append(getType());
        sb.append("; text is ");
        sb.append(getText());
        sb.append("; res id is ");
        sb.append(getResId());
        sb.append("; location is ");
        sb.append(getLocation());
        sb.append("; country is ");
        sb.append(getCountry());
        return sb.toString();
    }

    public void updateCoordinate(int[] iArr) {
        this.mCoordinate = iArr;
    }

    public void updateTextBitmap(View view) {
        if (view == null) {
            this.mTextBitmap = null;
            return;
        }
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int drawingCacheBackgroundColor = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (drawingCacheBackgroundColor != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap drawingCache = view.getDrawingCache();
        if (drawingCache == null) {
            Log.w(TAG, "[updateTimeBitmap] cacheBitmap is null");
            return;
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawingCache);
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheBackgroundColor(drawingCacheBackgroundColor);
        this.mTextBitmap = createBitmap;
    }
}
