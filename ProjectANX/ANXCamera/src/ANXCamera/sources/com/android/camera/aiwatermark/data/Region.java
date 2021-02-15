package com.android.camera.aiwatermark.data;

public class Region {
    private double mGreatLatitude = 0.0d;
    private double mGreatLongitude = 0.0d;
    private double mLittleLatitude = 0.0d;
    private double mLittleLongitude = 0.0d;

    public Region(double d, double d2, double d3, double d4) {
        this.mLittleLatitude = d;
        this.mGreatLatitude = d2;
        this.mLittleLongitude = d3;
        this.mGreatLongitude = d4;
    }

    private boolean checkInScope(double d, double d2, double d3) {
        if (d3 < d2) {
            double d4 = d2;
            d2 = d3;
            d3 = d4;
        }
        return d >= d2 && d <= d3;
    }

    public boolean isInRegion(double d, double d2) {
        return checkInScope(d, this.mLittleLatitude, this.mGreatLatitude) && checkInScope(d2, this.mLittleLongitude, this.mGreatLongitude);
    }
}
