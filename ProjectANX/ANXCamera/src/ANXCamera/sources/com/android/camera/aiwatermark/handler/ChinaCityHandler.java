package com.android.camera.aiwatermark.handler;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.android.camera.aiwatermark.data.CityWatermark;
import com.android.camera.log.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChinaCityHandler extends CityHandler {
    private static final String TAG = "ChinaCityHandler";
    private Context mContext = null;

    public ChinaCityHandler(boolean z, Context context) {
        super(z);
        this.mContext = context;
    }

    private String format(String str) {
        String str2 = "";
        return str != null ? str.replace(" ", str2) : str2;
    }

    public String getAddress(double d, double d2) {
        Geocoder geocoder = new Geocoder(this.mContext, Locale.ENGLISH);
        StringBuilder sb = new StringBuilder();
        try {
            List<Address> fromLocation = geocoder.getFromLocation(d, d2, 1);
            if (fromLocation != null) {
                for (Address address : fromLocation) {
                    if (address != null) {
                        sb.append(format(address.getLocality()));
                        sb.append(format(address.getSubAdminArea()));
                        sb.append(format(address.getAdminArea()));
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            String str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[getAddress] ex = ");
            sb2.append(e.getMessage());
            Log.e(str, sb2.toString());
        }
        return sb.toString();
    }

    public ArrayList getCityWatermarkList() {
        return new CityWatermark().getForAI();
    }
}
