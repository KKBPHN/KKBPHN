package com.android.camera;

import android.location.Location;
import android.os.Bundle;
import android.os.HandlerThread;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import java.util.Timer;
import java.util.TimerTask;

public class LocationManager {
    private static final int GPS_REQUEST_LOCATION_TIME_OUT = 60000;
    private static final long LOCATION_TIME_THRESHOLD = 3600000;
    private static final String TAG = "LocationManager";
    private static final long VALID_LAST_KNOWN_LOCATION_AGE = 180000;
    private Location mCacheLocation;
    private Location mLastKnownLocation;
    /* access modifiers changed from: private */
    public Listener mListener;
    LocationListener[] mLocationListeners;
    private android.location.LocationManager mLocationManager;
    /* access modifiers changed from: private */
    public boolean mRecordLocation;
    private HandlerThread mThreadHandler;
    /* access modifiers changed from: private */
    public Timer mTimer;

    public interface Listener {
        void hideGpsOnScreenIndicator();

        void showGpsOnScreenIndicator(boolean z);
    }

    class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        String mProvider;
        boolean mValid = false;

        public LocationListener(String str) {
            this.mProvider = str;
            this.mLastLocation = new Location(this.mProvider);
        }

        public Location current() {
            if (this.mValid) {
                return this.mLastLocation;
            }
            return null;
        }

        public void onLocationChanged(Location location) {
            if (location.getLatitude() != 0.0d || location.getLongitude() != 0.0d) {
                if (LocationManager.this.mRecordLocation) {
                    if ("gps".equals(this.mProvider)) {
                        LocationManager.this.cancelTimer();
                        if (LocationManager.this.mListener != null) {
                            LocationManager.this.mListener.showGpsOnScreenIndicator(true);
                        }
                    }
                }
                boolean z = this.mValid;
                String str = LocationManager.TAG;
                if (!z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Got first location, it is from ");
                    sb.append(this.mProvider);
                    Log.d(str, sb.toString());
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("update location, it is from ");
                    sb2.append(this.mProvider);
                    Log.v(str, sb2.toString());
                }
                this.mLastLocation.set(location);
                LocationManager.this.updateCacheLocation(this.mLastLocation);
                this.mValid = true;
            }
        }

        public void onProviderDisabled(String str) {
            this.mValid = false;
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            if (i == 0 || i == 1) {
                this.mValid = false;
                if (LocationManager.this.mListener != null && LocationManager.this.mRecordLocation && "gps".equals(str)) {
                    LocationManager.this.mListener.showGpsOnScreenIndicator(false);
                }
            }
        }
    }

    class LocationManagerHolder {
        /* access modifiers changed from: private */
        public static LocationManager sLocationManager = new LocationManager();

        private LocationManagerHolder() {
        }
    }

    private LocationManager() {
        this.mLocationListeners = new LocationListener[]{new LocationListener("gps"), new LocationListener("network")};
        this.mThreadHandler = new HandlerThread("Camera Handler Thread");
        this.mThreadHandler.start();
    }

    /* access modifiers changed from: private */
    public void cancelTimer() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        if ("gps".equals(r6.getProvider()) != false) goto L_0x0029;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Location getBetterLocation(Location location, Location location2) {
        if (location2 == null) {
            return location;
        }
        if (location != null && location.getTime() >= location2.getTime()) {
            if (location.getTime() == location2.getTime()) {
            }
            return location;
        }
        location = location2;
        return location;
    }

    private void getLastLocation() {
        Location location;
        String str = TAG;
        try {
            this.mLastKnownLocation = getBetterLocation(this.mLocationManager.getLastKnownLocation("gps"), this.mLocationManager.getLastKnownLocation("network"));
            location = getBetterLocation(this.mCacheLocation, this.mLastKnownLocation);
        } catch (SecurityException e) {
            Log.e(str, "fail to request last location update, ignore", (Throwable) e);
            location = this.mCacheLocation;
        }
        if (!isValidLastKnownLocation(location)) {
            location = null;
        }
        this.mCacheLocation = location;
        StringBuilder sb = new StringBuilder();
        sb.append("last cache location is ");
        sb.append(this.mCacheLocation != null ? "not null" : "null");
        Log.d(str, sb.toString());
    }

    public static LocationManager instance() {
        return LocationManagerHolder.sLocationManager;
    }

    private boolean isValidLastKnownLocation(Location location) {
        return location != null && Math.abs(System.currentTimeMillis() - location.getTime()) < VALID_LAST_KNOWN_LOCATION_AGE;
    }

    private void startReceivingLocationUpdates() {
        String str;
        String str2 = "provider does not exist ";
        String str3 = "fail to request location update, ignore";
        String str4 = TAG;
        if (this.mLocationManager == null) {
            this.mLocationManager = (android.location.LocationManager) CameraAppImpl.getAndroidContext().getSystemService("location");
        }
        android.location.LocationManager locationManager = this.mLocationManager;
        if (locationManager != null) {
            try {
                locationManager.requestLocationUpdates("network", 1000, 0.0f, this.mLocationListeners[1], this.mThreadHandler.getLooper());
            } catch (SecurityException e) {
                Log.i(str4, str3, e);
            } catch (IllegalArgumentException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(e2.getMessage());
                Log.d(str4, sb.toString());
            }
            try {
                this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, this.mLocationListeners[0], this.mThreadHandler.getLooper());
                cancelTimer();
                this.mTimer = new Timer(true);
                this.mTimer.schedule(new TimerTask() {
                    public void run() {
                        LocationManager.this.stopReceivingGPSLocationUpdates();
                        LocationManager.this.mTimer = null;
                    }
                }, 60000);
                if (this.mListener != null) {
                    this.mListener.showGpsOnScreenIndicator(false);
                }
            } catch (SecurityException e3) {
                Log.i(str4, str3, e3);
            } catch (IllegalArgumentException e4) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(e4.getMessage());
                str = sb2.toString();
                Log.d(str4, str);
            } catch (IllegalStateException e5) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("timer maybe canceled ");
                sb3.append(e5.getMessage());
                str = sb3.toString();
                Log.d(str4, str);
            }
            Log.d(str4, "startReceivingLocationUpdates");
            getLastLocation();
        }
    }

    /* access modifiers changed from: private */
    public void stopReceivingGPSLocationUpdates() {
        String str = TAG;
        android.location.LocationManager locationManager = this.mLocationManager;
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this.mLocationListeners[0]);
            } catch (Exception e) {
                Log.i(str, "fail to remove location listeners, ignore", e);
            }
            this.mLocationListeners[0].mValid = false;
            Log.d(str, "stopReceivingGPSLocationUpdates");
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.hideGpsOnScreenIndicator();
        }
    }

    private void stopReceivingLocationUpdates() {
        String str;
        cancelTimer();
        if (this.mLocationManager != null) {
            int i = 0;
            while (true) {
                LocationListener[] locationListenerArr = this.mLocationListeners;
                int length = locationListenerArr.length;
                str = TAG;
                if (i >= length) {
                    break;
                }
                try {
                    this.mLocationManager.removeUpdates(locationListenerArr[i]);
                } catch (Exception e) {
                    Log.i(str, "fail to remove location listeners, ignore", e);
                }
                this.mLocationListeners[i].mValid = false;
                i++;
            }
            Log.d(str, "stopReceivingLocationUpdates");
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.hideGpsOnScreenIndicator();
        }
    }

    /* access modifiers changed from: private */
    public void updateCacheLocation(Location location) {
        Location betterLocation = getBetterLocation(this.mCacheLocation, location);
        Location location2 = this.mCacheLocation;
        if (location2 != null) {
            location2.set(betterLocation);
        } else {
            this.mCacheLocation = new Location(betterLocation);
        }
    }

    private static Location validateLocation(Location location) {
        long currentTimeMillis = System.currentTimeMillis();
        if (location != null && Math.abs(location.getTime() - currentTimeMillis) > LOCATION_TIME_THRESHOLD) {
            StringBuilder sb = new StringBuilder();
            sb.append("validateLocation: modify to now from ");
            sb.append(location.getTime());
            Log.d(TAG, sb.toString());
            location.setTime(currentTimeMillis);
        }
        return location;
    }

    public Location getCurrentLocation() {
        if (!this.mRecordLocation) {
            return null;
        }
        return getCurrentLocationDirectly();
    }

    public Location getCurrentLocationDirectly() {
        int i = 0;
        while (true) {
            LocationListener[] locationListenerArr = this.mLocationListeners;
            int length = locationListenerArr.length;
            String str = TAG;
            if (i < length) {
                Location current = locationListenerArr[i].current();
                if (current != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("get current location, it is from ");
                    sb.append(this.mLocationListeners[i].mProvider);
                    Log.v(str, sb.toString());
                    validateLocation(current);
                    return current;
                }
                i++;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("No location received yet. cache location is ");
                sb2.append(this.mCacheLocation != null ? "not null" : "null");
                Log.d(str, sb2.toString());
                Location location = this.mCacheLocation;
                validateLocation(location);
                return location;
            }
        }
    }

    public Location getLastKnownLocation() {
        if (!this.mRecordLocation) {
            return null;
        }
        return this.mLastKnownLocation;
    }

    public void recordLocation(boolean z) {
        if (this.mRecordLocation != z) {
            this.mRecordLocation = z;
            if (!z || !PermissionManager.checkCameraLocationPermissions()) {
                stopReceivingLocationUpdates();
            } else {
                startReceivingLocationUpdates();
            }
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void unsetListener(Listener listener) {
        if (this.mListener == listener) {
            this.mListener = null;
        }
    }
}
