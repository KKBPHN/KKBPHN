package com.android.camera.aiwatermark.handler;

import android.location.Location;
import android.os.SystemProperties;
import com.android.camera.LocationManager;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public abstract class CityHandler extends AbstractHandler {
    private static final String TAG = "CityHandler";

    public CityHandler(boolean z) {
        super(z);
    }

    private Location getLocation() {
        LocationManager instance = LocationManager.instance();
        instance.recordLocation(true);
        return instance.getCurrentLocationDirectly();
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        double d;
        double d2;
        Location location = getLocation();
        if (location != null) {
            if (AbstractHandler.debugGPS()) {
                String str = "0";
                d2 = Double.valueOf(SystemProperties.get("test.gps.latitude", str)).doubleValue();
                d = Double.valueOf(SystemProperties.get("test.gps.longtitude", str)).doubleValue();
            } else {
                d2 = location.getLatitude();
                d = location.getLongitude();
            }
            String address = getAddress(d2, d);
            if (address != null) {
                Iterator it = getCityWatermarkList().iterator();
                while (it.hasNext()) {
                    WatermarkItem watermarkItem = (WatermarkItem) it.next();
                    if (address.toLowerCase(Locale.ENGLISH).contains(watermarkItem.getKey().toLowerCase(Locale.ENGLISH))) {
                        return watermarkItem;
                    }
                }
            }
        }
        Log.d(TAG, "CityHandler findWatermark");
        return null;
    }

    public abstract String getAddress(double d, double d2);

    public abstract ArrayList getCityWatermarkList();
}
