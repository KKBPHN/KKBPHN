package com.android.camera.aiwatermark.handler;

import android.location.Location;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.camera.LocationManager;
import com.android.camera.aiwatermark.data.Region;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class ScenicSpotsHandler extends AbstractHandler {
    private static final String TAG = "ScenicSpotsHandler";

    public ScenicSpotsHandler(boolean z) {
        super(z);
    }

    private Location getLocation() {
        LocationManager instance = LocationManager.instance();
        instance.recordLocation(true);
        return instance.getCurrentLocationDirectly();
    }

    /* access modifiers changed from: protected */
    public String findScenicSpot() {
        double d;
        double d2;
        Location location = getLocation();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("loc=");
        sb.append(location);
        Log.d(str, sb.toString());
        if (location != null) {
            if (AbstractHandler.debugGPS()) {
                String str2 = "0";
                d2 = Double.valueOf(SystemProperties.get("test.gps.latitude", str2)).doubleValue();
                d = Double.valueOf(SystemProperties.get("test.gps.longtitude", str2)).doubleValue();
            } else {
                d2 = location.getLatitude();
                d = location.getLongitude();
            }
            HashMap regionMap = getRegionMap();
            if (regionMap != null && !regionMap.isEmpty()) {
                for (String str3 : regionMap.keySet()) {
                    boolean isInRegion = ((Region) getRegionMap().get(str3)).isInRegion(d2, d);
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("key=");
                    sb2.append(str3);
                    sb2.append("; isInRegion=");
                    sb2.append(isInRegion);
                    Log.d(str4, sb2.toString());
                    if (isInRegion) {
                        return str3;
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        String findScenicSpot = findScenicSpot();
        if (findScenicSpot != null) {
            Iterator it = getWatermarkList().iterator();
            while (it.hasNext()) {
                WatermarkItem watermarkItem = (WatermarkItem) it.next();
                if (TextUtils.equals(findScenicSpot, watermarkItem.getKey())) {
                    return watermarkItem;
                }
            }
        }
        return null;
    }

    public abstract HashMap getRegionMap();

    public abstract ArrayList getWatermarkList();
}
