package com.ss.android.vesdk.runtime.cloudconfig;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoInjector implements IInjector {
    public static final String TAG = "AutoInjector";

    /* JADX WARNING: Removed duplicated region for block: B:103:0x028e  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x02a0  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x02ce  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x02e2  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0310  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x0324  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x034a  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x035c  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x0388  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x039a  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x03c2  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x03d4  */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x03f8  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x040a  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0430  */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0442  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0469  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x047b  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x04a2  */
    /* JADX WARNING: Removed duplicated region for block: B:196:0x04b4  */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x04d8  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x04ea  */
    /* JADX WARNING: Removed duplicated region for block: B:208:0x050e  */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x0520  */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x0544  */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x0556  */
    /* JADX WARNING: Removed duplicated region for block: B:228:0x0584  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x0598  */
    /* JADX WARNING: Removed duplicated region for block: B:237:0x05be  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:241:0x05d0  */
    /* JADX WARNING: Removed duplicated region for block: B:247:0x05fc  */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x060e  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x0636  */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x0648  */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x066c  */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x067e  */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x06a4  */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x06b6  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:284:0x06dd  */
    /* JADX WARNING: Removed duplicated region for block: B:288:0x06ef  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x0716  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x0728  */
    /* JADX WARNING: Removed duplicated region for block: B:301:0x074c  */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x075e  */
    /* JADX WARNING: Removed duplicated region for block: B:309:0x0782  */
    /* JADX WARNING: Removed duplicated region for block: B:313:0x0794  */
    /* JADX WARNING: Removed duplicated region for block: B:317:0x07b8  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x07ca  */
    /* JADX WARNING: Removed duplicated region for block: B:326:0x07f1  */
    /* JADX WARNING: Removed duplicated region for block: B:330:0x0803  */
    /* JADX WARNING: Removed duplicated region for block: B:335:0x082a  */
    /* JADX WARNING: Removed duplicated region for block: B:339:0x083c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:344:0x0863  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0178  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01c5  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01fb  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0222  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0234  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0258  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x026a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Map createParamMap(JSONObject jSONObject) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        String str26;
        String str27;
        String str28;
        String str29;
        String str30;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        String str36;
        String str37;
        String str38;
        String str39;
        String str40;
        String str41;
        String str42;
        HashMap hashMap = new HashMap();
        String str43 = "record_camera_type";
        boolean has = jSONObject.has(str43);
        String str44 = "1";
        String str45 = TAG;
        if (has) {
            int i = jSONObject.getInt(str43);
            if (i >= 1) {
                hashMap.put(str43, String.valueOf(i));
                str = "record_camera_compat_level";
                if (!jSONObject.has(str)) {
                    int i2 = jSONObject.getInt(str);
                    if (i2 >= 0) {
                        hashMap.put(str, String.valueOf(i2));
                        str2 = "record_video_sw_crf";
                        String str46 = "15";
                        if (jSONObject.has(str2)) {
                            int i3 = jSONObject.getInt(str2);
                            if (i3 < 1 || i3 > 50) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Fetched config doesn't pass:(value >= 1 && value <= 50) record_video_sw_crf = ");
                                sb.append(i3);
                                str40 = sb.toString();
                            } else {
                                hashMap.put(str2, String.valueOf(i3));
                                str3 = "record_video_sw_maxrate";
                                String str47 = "15000000";
                                if (!jSONObject.has(str3)) {
                                    int i4 = jSONObject.getInt(str3);
                                    if (i4 < 100000 || i4 > 100000000) {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Fetched config doesn't pass:(value >= 100000 && value <= 100000000) record_video_sw_maxrate = ");
                                        sb2.append(i4);
                                        str39 = sb2.toString();
                                    } else {
                                        hashMap.put(str3, String.valueOf(i4));
                                        str4 = "record_video_sw_preset";
                                        String str48 = "0";
                                        if (jSONObject.has(str4)) {
                                            int i5 = jSONObject.getInt(str4);
                                            if (i5 < 0 || i5 > 9) {
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("Fetched config doesn't pass:(value >= 0 && value <= 9) record_video_sw_preset = ");
                                                sb3.append(i5);
                                                str38 = sb3.toString();
                                            } else {
                                                hashMap.put(str4, String.valueOf(i5));
                                                String str49 = "35";
                                                if (!jSONObject.has("record_video_sw_gop")) {
                                                    int i6 = jSONObject.getInt("record_video_sw_gop");
                                                    if (i6 >= 1) {
                                                        hashMap.put("record_video_sw_gop", String.valueOf(i6));
                                                        String str50 = "2";
                                                        if (jSONObject.has("record_video_sw_qp")) {
                                                            int i7 = jSONObject.getInt("record_video_sw_qp");
                                                            if (i7 < 1 || i7 > 50) {
                                                                StringBuilder sb4 = new StringBuilder();
                                                                sb4.append("Fetched config doesn't pass:(value >= 1 && value <= 50) record_video_sw_qp = ");
                                                                sb4.append(i7);
                                                                str36 = sb4.toString();
                                                            } else {
                                                                hashMap.put("record_video_sw_qp", String.valueOf(i7));
                                                                if (!jSONObject.has("record_sw_bitrate_mode")) {
                                                                    int i8 = jSONObject.getInt("record_sw_bitrate_mode");
                                                                    if (i8 < 0 || i8 > 2) {
                                                                        StringBuilder sb5 = new StringBuilder();
                                                                        sb5.append("Fetched config doesn't pass:(value >= 0 && value <= 2) record_sw_bitrate_mode = ");
                                                                        sb5.append(i8);
                                                                        str35 = sb5.toString();
                                                                    } else {
                                                                        hashMap.put("record_sw_bitrate_mode", String.valueOf(i8));
                                                                        String str51 = "4194304";
                                                                        if (jSONObject.has("record_video_hw_bitrate")) {
                                                                            int i9 = jSONObject.getInt("record_video_hw_bitrate");
                                                                            if (i9 > 0) {
                                                                                hashMap.put("record_video_hw_bitrate", String.valueOf(i9));
                                                                                if (!jSONObject.has("record_encode_mode")) {
                                                                                    int i10 = jSONObject.getInt("record_encode_mode");
                                                                                    if (i10 == 0 || i10 == 1) {
                                                                                        hashMap.put("record_encode_mode", String.valueOf(i10));
                                                                                        if (jSONObject.has("record_hw_bitrate_mode")) {
                                                                                            int i11 = jSONObject.getInt("record_hw_bitrate_mode");
                                                                                            if (i11 >= 0) {
                                                                                                hashMap.put("record_hw_bitrate_mode", String.valueOf(i11));
                                                                                                if (!jSONObject.has("record_hw_profile")) {
                                                                                                    int i12 = jSONObject.getInt("record_hw_profile");
                                                                                                    if (i12 >= 0) {
                                                                                                        hashMap.put("record_hw_profile", String.valueOf(i12));
                                                                                                        if (jSONObject.has("record_resolution_width")) {
                                                                                                            int i13 = jSONObject.getInt("record_resolution_width");
                                                                                                            if (i13 % 16 != 0 || i13 < 160 || i13 > 5120) {
                                                                                                                StringBuilder sb6 = new StringBuilder();
                                                                                                                sb6.append("Fetched config doesn't pass:(value % 16 == 0 && value >= 160 && value <= 5120) record_resolution_width = ");
                                                                                                                sb6.append(i13);
                                                                                                                str30 = sb6.toString();
                                                                                                            } else {
                                                                                                                hashMap.put("record_resolution_width", String.valueOf(i13));
                                                                                                                if (!jSONObject.has("record_resolution_height")) {
                                                                                                                    int i14 = jSONObject.getInt("record_resolution_height");
                                                                                                                    if (i14 % 16 != 0 || i14 < 160 || i14 > 5120) {
                                                                                                                        StringBuilder sb7 = new StringBuilder();
                                                                                                                        sb7.append("Fetched config doesn't pass:(value % 16 == 0 && value >= 160 && value <= 5120) record_resolution_height = ");
                                                                                                                        sb7.append(i14);
                                                                                                                        str29 = sb7.toString();
                                                                                                                    } else {
                                                                                                                        hashMap.put("record_resolution_height", String.valueOf(i14));
                                                                                                                        if (jSONObject.has("import_video_sw_crf")) {
                                                                                                                            int i15 = jSONObject.getInt("import_video_sw_crf");
                                                                                                                            if (i15 < 1 || i15 > 50) {
                                                                                                                                StringBuilder sb8 = new StringBuilder();
                                                                                                                                sb8.append("Fetched config doesn't pass:(value >= 1 && value <= 50) import_video_sw_crf = ");
                                                                                                                                sb8.append(i15);
                                                                                                                                str28 = sb8.toString();
                                                                                                                            } else {
                                                                                                                                hashMap.put("import_video_sw_crf", String.valueOf(i15));
                                                                                                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                                                                    int i16 = jSONObject.getInt("import_video_sw_maxrate");
                                                                                                                                    if (i16 < 100000 || i16 > 100000000) {
                                                                                                                                        StringBuilder sb9 = new StringBuilder();
                                                                                                                                        sb9.append("Fetched config doesn't pass:(value >= 100000 && value <= 100000000) import_video_sw_maxrate = ");
                                                                                                                                        sb9.append(i16);
                                                                                                                                        str27 = sb9.toString();
                                                                                                                                    } else {
                                                                                                                                        hashMap.put("import_video_sw_maxrate", String.valueOf(i16));
                                                                                                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                                                                                                            int i17 = jSONObject.getInt("import_video_sw_preset");
                                                                                                                                            if (i17 < 0 || i17 > 9) {
                                                                                                                                                StringBuilder sb10 = new StringBuilder();
                                                                                                                                                sb10.append("Fetched config doesn't pass:(value >= 0 && value <= 9) import_video_sw_preset = ");
                                                                                                                                                sb10.append(i17);
                                                                                                                                                str26 = sb10.toString();
                                                                                                                                            } else {
                                                                                                                                                hashMap.put("import_video_sw_preset", String.valueOf(i17));
                                                                                                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                                                                    int i18 = jSONObject.getInt("import_video_sw_gop");
                                                                                                                                                    if (i18 >= 1) {
                                                                                                                                                        hashMap.put("import_video_sw_gop", String.valueOf(i18));
                                                                                                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                                                            int i19 = jSONObject.getInt("import_video_sw_qp");
                                                                                                                                                            if (i19 < 1 || i19 > 50) {
                                                                                                                                                                StringBuilder sb11 = new StringBuilder();
                                                                                                                                                                sb11.append("Fetched config doesn't pass:(value >= 1 && value <= 50) import_video_sw_qp = ");
                                                                                                                                                                sb11.append(i19);
                                                                                                                                                                str24 = sb11.toString();
                                                                                                                                                            } else {
                                                                                                                                                                hashMap.put("import_video_sw_qp", String.valueOf(i19));
                                                                                                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                                                                    int i20 = jSONObject.getInt("import_sw_bitrate_mode");
                                                                                                                                                                    if (i20 < 0 || i20 > 2) {
                                                                                                                                                                        StringBuilder sb12 = new StringBuilder();
                                                                                                                                                                        sb12.append("Fetched config doesn't pass:(value >= 0 && value <= 2) import_sw_bitrate_mode = ");
                                                                                                                                                                        sb12.append(i20);
                                                                                                                                                                        str23 = sb12.toString();
                                                                                                                                                                    } else {
                                                                                                                                                                        hashMap.put("import_sw_bitrate_mode", String.valueOf(i20));
                                                                                                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                                                                                                            int i21 = jSONObject.getInt("import_encode_mode");
                                                                                                                                                                            if (i21 == 0 || i21 == 1) {
                                                                                                                                                                                hashMap.put("import_encode_mode", String.valueOf(i21));
                                                                                                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                                                                    int i22 = jSONObject.getInt("import_video_hw_bitrate");
                                                                                                                                                                                    if (i22 > 0) {
                                                                                                                                                                                        hashMap.put("import_video_hw_bitrate", String.valueOf(i22));
                                                                                                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                                                            int i23 = jSONObject.getInt("import_hw_bitrate_mode");
                                                                                                                                                                                            if (i23 >= 0) {
                                                                                                                                                                                                hashMap.put("import_hw_bitrate_mode", String.valueOf(i23));
                                                                                                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                                                                    int i24 = jSONObject.getInt("import_hw_profile");
                                                                                                                                                                                                    if (i24 >= 0) {
                                                                                                                                                                                                        hashMap.put("import_hw_profile", String.valueOf(i24));
                                                                                                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                                                            int i25 = jSONObject.getInt("import_shorter_pixels");
                                                                                                                                                                                                            if (i25 % 16 != 0 || i25 < 160 || i25 > 5120) {
                                                                                                                                                                                                                StringBuilder sb13 = new StringBuilder();
                                                                                                                                                                                                                sb13.append("Fetched config doesn't pass:(value % 16 == 0 && value >= 160 && value <= 5120) import_shorter_pixels = ");
                                                                                                                                                                                                                sb13.append(i25);
                                                                                                                                                                                                                str18 = sb13.toString();
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                hashMap.put("import_shorter_pixels", String.valueOf(i25));
                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                                                                    int i26 = jSONObject.getInt("synthetic_video_sw_crf");
                                                                                                                                                                                                                    if (i26 < 1 || i26 > 50) {
                                                                                                                                                                                                                        StringBuilder sb14 = new StringBuilder();
                                                                                                                                                                                                                        sb14.append("Fetched config doesn't pass:(value >= 1 && value <= 50) synthetic_video_sw_crf = ");
                                                                                                                                                                                                                        sb14.append(i26);
                                                                                                                                                                                                                        str17 = sb14.toString();
                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_crf", String.valueOf(i26));
                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                                                            int i27 = jSONObject.getInt("synthetic_video_sw_maxrate");
                                                                                                                                                                                                                            if (i27 < 100000 || i27 > 100000000) {
                                                                                                                                                                                                                                StringBuilder sb15 = new StringBuilder();
                                                                                                                                                                                                                                sb15.append("Fetched config doesn't pass:(value >= 100000 && value <= 100000000) synthetic_video_sw_maxrate = ");
                                                                                                                                                                                                                                sb15.append(i27);
                                                                                                                                                                                                                                str16 = sb15.toString();
                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", String.valueOf(i27));
                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                                                                    int i28 = jSONObject.getInt("synthetic_video_sw_preset");
                                                                                                                                                                                                                                    if (i28 < 0 || i28 > 9) {
                                                                                                                                                                                                                                        StringBuilder sb16 = new StringBuilder();
                                                                                                                                                                                                                                        sb16.append("Fetched config doesn't pass:(value >= 0 && value <= 9) synthetic_video_sw_preset = ");
                                                                                                                                                                                                                                        sb16.append(i28);
                                                                                                                                                                                                                                        str15 = sb16.toString();
                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", String.valueOf(i28));
                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                                                            int i29 = jSONObject.getInt("synthetic_video_sw_gop");
                                                                                                                                                                                                                                            if (i29 >= 1) {
                                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", String.valueOf(i29));
                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                                                                    int i30 = jSONObject.getInt("synthetic_video_sw_qp");
                                                                                                                                                                                                                                                    if (i30 < 1 || i30 > 50) {
                                                                                                                                                                                                                                                        StringBuilder sb17 = new StringBuilder();
                                                                                                                                                                                                                                                        sb17.append("Fetched config doesn't pass:(value >= 1 && value <= 50) synthetic_video_sw_qp = ");
                                                                                                                                                                                                                                                        sb17.append(i30);
                                                                                                                                                                                                                                                        str13 = sb17.toString();
                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", String.valueOf(i30));
                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                                                            int i31 = jSONObject.getInt("synthetic_sw_bitrate_mode");
                                                                                                                                                                                                                                                            if (i31 < 0 || i31 > 2) {
                                                                                                                                                                                                                                                                StringBuilder sb18 = new StringBuilder();
                                                                                                                                                                                                                                                                sb18.append("Fetched config doesn't pass:(value >= 0 && value <= 2) synthetic_sw_bitrate_mode = ");
                                                                                                                                                                                                                                                                sb18.append(i31);
                                                                                                                                                                                                                                                                str12 = sb18.toString();
                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", String.valueOf(i31));
                                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                                                                    int i32 = jSONObject.getInt("synthetic_encode_mode");
                                                                                                                                                                                                                                                                    if (i32 == 0 || i32 == 1) {
                                                                                                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", String.valueOf(i32));
                                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                                                            int i33 = jSONObject.getInt("synthetic_video_hw_bitrate");
                                                                                                                                                                                                                                                                            if (i33 >= 0) {
                                                                                                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", String.valueOf(i33));
                                                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                                                                    int i34 = jSONObject.getInt("synthetic_hw_bitrate_mode");
                                                                                                                                                                                                                                                                                    if (i34 >= 0) {
                                                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", String.valueOf(i34));
                                                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                                                            int i35 = jSONObject.getInt("synthetic_hw_profile");
                                                                                                                                                                                                                                                                                            if (i35 >= 0) {
                                                                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", String.valueOf(i35));
                                                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                                                                    int i36 = jSONObject.getInt("earphone_echo_normal");
                                                                                                                                                                                                                                                                                                    if (i36 == 0 || i36 == 1) {
                                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", String.valueOf(i36));
                                                                                                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                                                            int i37 = jSONObject.getInt("earphone_echo_aaudio");
                                                                                                                                                                                                                                                                                                            if (i37 == 0 || i37 == 1) {
                                                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", String.valueOf(i37));
                                                                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                                                                    int i38 = jSONObject.getInt("earphone_echo_huawei");
                                                                                                                                                                                                                                                                                                                    if (i38 == 0 || i38 == 1) {
                                                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", String.valueOf(i38));
                                                                                                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    StringBuilder sb19 = new StringBuilder();
                                                                                                                                                                                                                                                                                                                    sb19.append("Fetched config doesn't pass:(value == 0 || value == 1) earphone_echo_huawei = ");
                                                                                                                                                                                                                                                                                                                    sb19.append(i38);
                                                                                                                                                                                                                                                                                                                    str5 = sb19.toString();
                                                                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                                                                    str5 = "Fetched config doesn't contain: earphone_echo_huawei";
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            StringBuilder sb20 = new StringBuilder();
                                                                                                                                                                                                                                                                                                            sb20.append("Fetched config doesn't pass:(value == 0 || value == 1) earphone_echo_aaudio = ");
                                                                                                                                                                                                                                                                                                            sb20.append(i37);
                                                                                                                                                                                                                                                                                                            str6 = sb20.toString();
                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                            str6 = "Fetched config doesn't contain: earphone_echo_aaudio";
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    StringBuilder sb21 = new StringBuilder();
                                                                                                                                                                                                                                                                                                    sb21.append("Fetched config doesn't pass:(value == 0 || value == 1) earphone_echo_normal = ");
                                                                                                                                                                                                                                                                                                    sb21.append(i36);
                                                                                                                                                                                                                                                                                                    str7 = sb21.toString();
                                                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                                                    str7 = "Fetched config doesn't contain: earphone_echo_normal";
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            StringBuilder sb22 = new StringBuilder();
                                                                                                                                                                                                                                                                                            sb22.append("Fetched config doesn't pass:(value >= 0) synthetic_hw_profile = ");
                                                                                                                                                                                                                                                                                            sb22.append(i35);
                                                                                                                                                                                                                                                                                            str8 = sb22.toString();
                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                            str8 = "Fetched config doesn't contain: synthetic_hw_profile";
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    StringBuilder sb23 = new StringBuilder();
                                                                                                                                                                                                                                                                                    sb23.append("Fetched config doesn't pass:(value >= 0) synthetic_hw_bitrate_mode = ");
                                                                                                                                                                                                                                                                                    sb23.append(i34);
                                                                                                                                                                                                                                                                                    str9 = sb23.toString();
                                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                                    str9 = "Fetched config doesn't contain: synthetic_hw_bitrate_mode";
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            StringBuilder sb24 = new StringBuilder();
                                                                                                                                                                                                                                                                            sb24.append("Fetched config doesn't pass:(value >= 0) synthetic_video_hw_bitrate = ");
                                                                                                                                                                                                                                                                            sb24.append(i33);
                                                                                                                                                                                                                                                                            str10 = sb24.toString();
                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                            str10 = "Fetched config doesn't contain: synthetic_video_hw_bitrate";
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    StringBuilder sb25 = new StringBuilder();
                                                                                                                                                                                                                                                                    sb25.append("Fetched config doesn't pass:(value == 0 || value == 1) synthetic_encode_mode = ");
                                                                                                                                                                                                                                                                    sb25.append(i32);
                                                                                                                                                                                                                                                                    str11 = sb25.toString();
                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                    str11 = "Fetched config doesn't contain: synthetic_encode_mode";
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                            str12 = "Fetched config doesn't contain: synthetic_sw_bitrate_mode";
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                    str13 = "Fetched config doesn't contain: synthetic_video_sw_qp";
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            StringBuilder sb26 = new StringBuilder();
                                                                                                                                                                                                                                            sb26.append("Fetched config doesn't pass:(value >= 1) synthetic_video_sw_gop = ");
                                                                                                                                                                                                                                            sb26.append(i29);
                                                                                                                                                                                                                                            str14 = sb26.toString();
                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                            str14 = "Fetched config doesn't contain: synthetic_video_sw_gop";
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                    str15 = "Fetched config doesn't contain: synthetic_video_sw_preset";
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                            str16 = "Fetched config doesn't contain: synthetic_video_sw_maxrate";
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str16);
                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str15);
                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    str17 = "Fetched config doesn't contain: synthetic_video_sw_crf";
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str17);
                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str16);
                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                                            }
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            str18 = "Fetched config doesn't contain: import_shorter_pixels";
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str18);
                                                                                                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str17);
                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str16);
                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str15);
                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    StringBuilder sb27 = new StringBuilder();
                                                                                                                                                                                                    sb27.append("Fetched config doesn't pass:(value >= 0) import_hw_profile = ");
                                                                                                                                                                                                    sb27.append(i24);
                                                                                                                                                                                                    str19 = sb27.toString();
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    str19 = "Fetched config doesn't contain: import_hw_profile";
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str19);
                                                                                                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str18);
                                                                                                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str17);
                                                                                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str16);
                                                                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                                }
                                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                                return hashMap;
                                                                                                                                                                                            }
                                                                                                                                                                                            StringBuilder sb28 = new StringBuilder();
                                                                                                                                                                                            sb28.append("Fetched config doesn't pass:(value >= 0) import_hw_bitrate_mode = ");
                                                                                                                                                                                            sb28.append(i23);
                                                                                                                                                                                            str20 = sb28.toString();
                                                                                                                                                                                        } else {
                                                                                                                                                                                            str20 = "Fetched config doesn't contain: import_hw_bitrate_mode";
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str20);
                                                                                                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str19);
                                                                                                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str18);
                                                                                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str17);
                                                                                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str16);
                                                                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str15);
                                                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                        }
                                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                        return hashMap;
                                                                                                                                                                                    }
                                                                                                                                                                                    StringBuilder sb29 = new StringBuilder();
                                                                                                                                                                                    sb29.append("Fetched config doesn't pass:(value > 0) import_video_hw_bitrate = ");
                                                                                                                                                                                    sb29.append(i22);
                                                                                                                                                                                    str21 = sb29.toString();
                                                                                                                                                                                } else {
                                                                                                                                                                                    str21 = "Fetched config doesn't contain: import_video_hw_bitrate";
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str21);
                                                                                                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str20);
                                                                                                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str19);
                                                                                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str18);
                                                                                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str17);
                                                                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str16);
                                                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                                }
                                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                                return hashMap;
                                                                                                                                                                            }
                                                                                                                                                                            StringBuilder sb30 = new StringBuilder();
                                                                                                                                                                            sb30.append("Fetched config doesn't pass:(value == 0 || value == 1) import_encode_mode = ");
                                                                                                                                                                            sb30.append(i21);
                                                                                                                                                                            str22 = sb30.toString();
                                                                                                                                                                        } else {
                                                                                                                                                                            str22 = "Fetched config doesn't contain: import_encode_mode";
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str22);
                                                                                                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str21);
                                                                                                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str20);
                                                                                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str19);
                                                                                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str18);
                                                                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str17);
                                                                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str16);
                                                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str15);
                                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                        }
                                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                        return hashMap;
                                                                                                                                                                    }
                                                                                                                                                                } else {
                                                                                                                                                                    str23 = "Fetched config doesn't contain: import_sw_bitrate_mode";
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str23);
                                                                                                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str22);
                                                                                                                                                                hashMap.put("import_encode_mode", str48);
                                                                                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str21);
                                                                                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str20);
                                                                                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str19);
                                                                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str18);
                                                                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str17);
                                                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str16);
                                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                                }
                                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                                return hashMap;
                                                                                                                                                            }
                                                                                                                                                        } else {
                                                                                                                                                            str24 = "Fetched config doesn't contain: import_video_sw_qp";
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str24);
                                                                                                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                                                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str23);
                                                                                                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str22);
                                                                                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str21);
                                                                                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str20);
                                                                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str19);
                                                                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str18);
                                                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str17);
                                                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str16);
                                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str15);
                                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str14);
                                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str13);
                                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str12);
                                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str11);
                                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str10);
                                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str9);
                                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str8);
                                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str7);
                                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str6);
                                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                        }
                                                                                                                                                        Log.w(str45, str5);
                                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                        return hashMap;
                                                                                                                                                    }
                                                                                                                                                    StringBuilder sb31 = new StringBuilder();
                                                                                                                                                    sb31.append("Fetched config doesn't pass:(value >= 1) import_video_sw_gop = ");
                                                                                                                                                    sb31.append(i18);
                                                                                                                                                    str25 = sb31.toString();
                                                                                                                                                } else {
                                                                                                                                                    str25 = "Fetched config doesn't contain: import_video_sw_gop";
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str25);
                                                                                                                                                hashMap.put("import_video_sw_gop", str49);
                                                                                                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str24);
                                                                                                                                                hashMap.put("import_video_sw_qp", str50);
                                                                                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str23);
                                                                                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str22);
                                                                                                                                                hashMap.put("import_encode_mode", str48);
                                                                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str21);
                                                                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str20);
                                                                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str19);
                                                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str18);
                                                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str17);
                                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str16);
                                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str15);
                                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str14);
                                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str13);
                                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str12);
                                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str11);
                                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str10);
                                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str9);
                                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str8);
                                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str7);
                                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str6);
                                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                                }
                                                                                                                                                Log.w(str45, str5);
                                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                                return hashMap;
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            str26 = "Fetched config doesn't contain: import_video_sw_preset";
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str26);
                                                                                                                                        hashMap.put("import_video_sw_preset", str48);
                                                                                                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str25);
                                                                                                                                        hashMap.put("import_video_sw_gop", str49);
                                                                                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str24);
                                                                                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str23);
                                                                                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str22);
                                                                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str21);
                                                                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str20);
                                                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str19);
                                                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str18);
                                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str17);
                                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str16);
                                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str15);
                                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str14);
                                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str13);
                                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str12);
                                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str11);
                                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str10);
                                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str9);
                                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str8);
                                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str7);
                                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str6);
                                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                        }
                                                                                                                                        Log.w(str45, str5);
                                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                        return hashMap;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    str27 = "Fetched config doesn't contain: import_video_sw_maxrate";
                                                                                                                                }
                                                                                                                                Log.w(str45, str27);
                                                                                                                                hashMap.put("import_video_sw_maxrate", str47);
                                                                                                                                if (jSONObject.has("import_video_sw_preset")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str26);
                                                                                                                                hashMap.put("import_video_sw_preset", str48);
                                                                                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str25);
                                                                                                                                hashMap.put("import_video_sw_gop", str49);
                                                                                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str24);
                                                                                                                                hashMap.put("import_video_sw_qp", str50);
                                                                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str23);
                                                                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str22);
                                                                                                                                hashMap.put("import_encode_mode", str48);
                                                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str21);
                                                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str20);
                                                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str19);
                                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str18);
                                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str17);
                                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str16);
                                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str15);
                                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str14);
                                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str13);
                                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str12);
                                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str11);
                                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str10);
                                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str9);
                                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str8);
                                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str7);
                                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str6);
                                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                                }
                                                                                                                                Log.w(str45, str5);
                                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                                return hashMap;
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            str28 = "Fetched config doesn't contain: import_video_sw_crf";
                                                                                                                        }
                                                                                                                        Log.w(str45, str28);
                                                                                                                        hashMap.put("import_video_sw_crf", str46);
                                                                                                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str27);
                                                                                                                        hashMap.put("import_video_sw_maxrate", str47);
                                                                                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str26);
                                                                                                                        hashMap.put("import_video_sw_preset", str48);
                                                                                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str25);
                                                                                                                        hashMap.put("import_video_sw_gop", str49);
                                                                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str24);
                                                                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str23);
                                                                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str22);
                                                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str21);
                                                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str20);
                                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str19);
                                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str18);
                                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str17);
                                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str16);
                                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str15);
                                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str14);
                                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str13);
                                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str12);
                                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str11);
                                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str10);
                                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str9);
                                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str8);
                                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str7);
                                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str6);
                                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                        }
                                                                                                                        Log.w(str45, str5);
                                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                                        return hashMap;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    str29 = "Fetched config doesn't contain: record_resolution_height";
                                                                                                                }
                                                                                                                Log.w(str45, str29);
                                                                                                                hashMap.put("record_resolution_height", "1024");
                                                                                                                if (jSONObject.has("import_video_sw_crf")) {
                                                                                                                }
                                                                                                                Log.w(str45, str28);
                                                                                                                hashMap.put("import_video_sw_crf", str46);
                                                                                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                                                }
                                                                                                                Log.w(str45, str27);
                                                                                                                hashMap.put("import_video_sw_maxrate", str47);
                                                                                                                if (jSONObject.has("import_video_sw_preset")) {
                                                                                                                }
                                                                                                                Log.w(str45, str26);
                                                                                                                hashMap.put("import_video_sw_preset", str48);
                                                                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                                }
                                                                                                                Log.w(str45, str25);
                                                                                                                hashMap.put("import_video_sw_gop", str49);
                                                                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                                                                }
                                                                                                                Log.w(str45, str24);
                                                                                                                hashMap.put("import_video_sw_qp", str50);
                                                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str23);
                                                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str22);
                                                                                                                hashMap.put("import_encode_mode", str48);
                                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                                }
                                                                                                                Log.w(str45, str21);
                                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str20);
                                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                                }
                                                                                                                Log.w(str45, str19);
                                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                                }
                                                                                                                Log.w(str45, str18);
                                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                                }
                                                                                                                Log.w(str45, str17);
                                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                                }
                                                                                                                Log.w(str45, str16);
                                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                                }
                                                                                                                Log.w(str45, str15);
                                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                                }
                                                                                                                Log.w(str45, str14);
                                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                                }
                                                                                                                Log.w(str45, str13);
                                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str12);
                                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str11);
                                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                                }
                                                                                                                Log.w(str45, str10);
                                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                                }
                                                                                                                Log.w(str45, str9);
                                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                                }
                                                                                                                Log.w(str45, str8);
                                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                                }
                                                                                                                Log.w(str45, str7);
                                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                                }
                                                                                                                Log.w(str45, str6);
                                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                                }
                                                                                                                Log.w(str45, str5);
                                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                                return hashMap;
                                                                                                            }
                                                                                                        } else {
                                                                                                            str30 = "Fetched config doesn't contain: record_resolution_width";
                                                                                                        }
                                                                                                        Log.w(str45, str30);
                                                                                                        hashMap.put("record_resolution_width", "576");
                                                                                                        if (!jSONObject.has("record_resolution_height")) {
                                                                                                        }
                                                                                                        Log.w(str45, str29);
                                                                                                        hashMap.put("record_resolution_height", "1024");
                                                                                                        if (jSONObject.has("import_video_sw_crf")) {
                                                                                                        }
                                                                                                        Log.w(str45, str28);
                                                                                                        hashMap.put("import_video_sw_crf", str46);
                                                                                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                                        }
                                                                                                        Log.w(str45, str27);
                                                                                                        hashMap.put("import_video_sw_maxrate", str47);
                                                                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                                                                        }
                                                                                                        Log.w(str45, str26);
                                                                                                        hashMap.put("import_video_sw_preset", str48);
                                                                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                        }
                                                                                                        Log.w(str45, str25);
                                                                                                        hashMap.put("import_video_sw_gop", str49);
                                                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                                                        }
                                                                                                        Log.w(str45, str24);
                                                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str23);
                                                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str22);
                                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                        }
                                                                                                        Log.w(str45, str21);
                                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str20);
                                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                                        }
                                                                                                        Log.w(str45, str19);
                                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                                        }
                                                                                                        Log.w(str45, str18);
                                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                        }
                                                                                                        Log.w(str45, str17);
                                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                        }
                                                                                                        Log.w(str45, str16);
                                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                        }
                                                                                                        Log.w(str45, str15);
                                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                        }
                                                                                                        Log.w(str45, str14);
                                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                        }
                                                                                                        Log.w(str45, str13);
                                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str12);
                                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str11);
                                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                        }
                                                                                                        Log.w(str45, str10);
                                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                        }
                                                                                                        Log.w(str45, str9);
                                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                        }
                                                                                                        Log.w(str45, str8);
                                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                        }
                                                                                                        Log.w(str45, str7);
                                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                        }
                                                                                                        Log.w(str45, str6);
                                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                        }
                                                                                                        Log.w(str45, str5);
                                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                                        return hashMap;
                                                                                                    }
                                                                                                    StringBuilder sb32 = new StringBuilder();
                                                                                                    sb32.append("Fetched config doesn't pass:(value >= 0) record_hw_profile = ");
                                                                                                    sb32.append(i12);
                                                                                                    str31 = sb32.toString();
                                                                                                } else {
                                                                                                    str31 = "Fetched config doesn't contain: record_hw_profile";
                                                                                                }
                                                                                                Log.w(str45, str31);
                                                                                                hashMap.put("record_hw_profile", str48);
                                                                                                if (jSONObject.has("record_resolution_width")) {
                                                                                                }
                                                                                                Log.w(str45, str30);
                                                                                                hashMap.put("record_resolution_width", "576");
                                                                                                if (!jSONObject.has("record_resolution_height")) {
                                                                                                }
                                                                                                Log.w(str45, str29);
                                                                                                hashMap.put("record_resolution_height", "1024");
                                                                                                if (jSONObject.has("import_video_sw_crf")) {
                                                                                                }
                                                                                                Log.w(str45, str28);
                                                                                                hashMap.put("import_video_sw_crf", str46);
                                                                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                                }
                                                                                                Log.w(str45, str27);
                                                                                                hashMap.put("import_video_sw_maxrate", str47);
                                                                                                if (jSONObject.has("import_video_sw_preset")) {
                                                                                                }
                                                                                                Log.w(str45, str26);
                                                                                                hashMap.put("import_video_sw_preset", str48);
                                                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                                                }
                                                                                                Log.w(str45, str25);
                                                                                                hashMap.put("import_video_sw_gop", str49);
                                                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                                                }
                                                                                                Log.w(str45, str24);
                                                                                                hashMap.put("import_video_sw_qp", str50);
                                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str23);
                                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str22);
                                                                                                hashMap.put("import_encode_mode", str48);
                                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                                }
                                                                                                Log.w(str45, str21);
                                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str20);
                                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                                }
                                                                                                Log.w(str45, str19);
                                                                                                hashMap.put("import_hw_profile", str48);
                                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                                }
                                                                                                Log.w(str45, str18);
                                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                                }
                                                                                                Log.w(str45, str17);
                                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                                }
                                                                                                Log.w(str45, str16);
                                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                                }
                                                                                                Log.w(str45, str15);
                                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                                }
                                                                                                Log.w(str45, str14);
                                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                                }
                                                                                                Log.w(str45, str13);
                                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str12);
                                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str11);
                                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                                }
                                                                                                Log.w(str45, str10);
                                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                                }
                                                                                                Log.w(str45, str9);
                                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                                }
                                                                                                Log.w(str45, str8);
                                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                                }
                                                                                                Log.w(str45, str7);
                                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                                }
                                                                                                Log.w(str45, str6);
                                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                                }
                                                                                                Log.w(str45, str5);
                                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                                return hashMap;
                                                                                            }
                                                                                            StringBuilder sb33 = new StringBuilder();
                                                                                            sb33.append("Fetched config doesn't pass:(value >= 0) record_hw_bitrate_mode = ");
                                                                                            sb33.append(i11);
                                                                                            str32 = sb33.toString();
                                                                                        } else {
                                                                                            str32 = "Fetched config doesn't contain: record_hw_bitrate_mode";
                                                                                        }
                                                                                        Log.w(str45, str32);
                                                                                        hashMap.put("record_hw_bitrate_mode", str48);
                                                                                        if (!jSONObject.has("record_hw_profile")) {
                                                                                        }
                                                                                        Log.w(str45, str31);
                                                                                        hashMap.put("record_hw_profile", str48);
                                                                                        if (jSONObject.has("record_resolution_width")) {
                                                                                        }
                                                                                        Log.w(str45, str30);
                                                                                        hashMap.put("record_resolution_width", "576");
                                                                                        if (!jSONObject.has("record_resolution_height")) {
                                                                                        }
                                                                                        Log.w(str45, str29);
                                                                                        hashMap.put("record_resolution_height", "1024");
                                                                                        if (jSONObject.has("import_video_sw_crf")) {
                                                                                        }
                                                                                        Log.w(str45, str28);
                                                                                        hashMap.put("import_video_sw_crf", str46);
                                                                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                        }
                                                                                        Log.w(str45, str27);
                                                                                        hashMap.put("import_video_sw_maxrate", str47);
                                                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                                                        }
                                                                                        Log.w(str45, str26);
                                                                                        hashMap.put("import_video_sw_preset", str48);
                                                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                                                        }
                                                                                        Log.w(str45, str25);
                                                                                        hashMap.put("import_video_sw_gop", str49);
                                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                                        }
                                                                                        Log.w(str45, str24);
                                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str23);
                                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str22);
                                                                                        hashMap.put("import_encode_mode", str48);
                                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                        }
                                                                                        Log.w(str45, str21);
                                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str20);
                                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                                        }
                                                                                        Log.w(str45, str19);
                                                                                        hashMap.put("import_hw_profile", str48);
                                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                                        }
                                                                                        Log.w(str45, str18);
                                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                        }
                                                                                        Log.w(str45, str17);
                                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                        }
                                                                                        Log.w(str45, str16);
                                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                        }
                                                                                        Log.w(str45, str15);
                                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                        }
                                                                                        Log.w(str45, str14);
                                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                        }
                                                                                        Log.w(str45, str13);
                                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str12);
                                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str11);
                                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                        }
                                                                                        Log.w(str45, str10);
                                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                        }
                                                                                        Log.w(str45, str9);
                                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                                        }
                                                                                        Log.w(str45, str8);
                                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                                        }
                                                                                        Log.w(str45, str7);
                                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                        }
                                                                                        Log.w(str45, str6);
                                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                        }
                                                                                        Log.w(str45, str5);
                                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                                        return hashMap;
                                                                                    }
                                                                                    StringBuilder sb34 = new StringBuilder();
                                                                                    sb34.append("Fetched config doesn't pass:(value == 0 || value == 1) record_encode_mode = ");
                                                                                    sb34.append(i10);
                                                                                    str33 = sb34.toString();
                                                                                } else {
                                                                                    str33 = "Fetched config doesn't contain: record_encode_mode";
                                                                                }
                                                                                Log.w(str45, str33);
                                                                                hashMap.put("record_encode_mode", str48);
                                                                                if (jSONObject.has("record_hw_bitrate_mode")) {
                                                                                }
                                                                                Log.w(str45, str32);
                                                                                hashMap.put("record_hw_bitrate_mode", str48);
                                                                                if (!jSONObject.has("record_hw_profile")) {
                                                                                }
                                                                                Log.w(str45, str31);
                                                                                hashMap.put("record_hw_profile", str48);
                                                                                if (jSONObject.has("record_resolution_width")) {
                                                                                }
                                                                                Log.w(str45, str30);
                                                                                hashMap.put("record_resolution_width", "576");
                                                                                if (!jSONObject.has("record_resolution_height")) {
                                                                                }
                                                                                Log.w(str45, str29);
                                                                                hashMap.put("record_resolution_height", "1024");
                                                                                if (jSONObject.has("import_video_sw_crf")) {
                                                                                }
                                                                                Log.w(str45, str28);
                                                                                hashMap.put("import_video_sw_crf", str46);
                                                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                                }
                                                                                Log.w(str45, str27);
                                                                                hashMap.put("import_video_sw_maxrate", str47);
                                                                                if (jSONObject.has("import_video_sw_preset")) {
                                                                                }
                                                                                Log.w(str45, str26);
                                                                                hashMap.put("import_video_sw_preset", str48);
                                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                                }
                                                                                Log.w(str45, str25);
                                                                                hashMap.put("import_video_sw_gop", str49);
                                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                                }
                                                                                Log.w(str45, str24);
                                                                                hashMap.put("import_video_sw_qp", str50);
                                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                                }
                                                                                Log.w(str45, str23);
                                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                                if (jSONObject.has("import_encode_mode")) {
                                                                                }
                                                                                Log.w(str45, str22);
                                                                                hashMap.put("import_encode_mode", str48);
                                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                                }
                                                                                Log.w(str45, str21);
                                                                                hashMap.put("import_video_hw_bitrate", str51);
                                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                                }
                                                                                Log.w(str45, str20);
                                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                                }
                                                                                Log.w(str45, str19);
                                                                                hashMap.put("import_hw_profile", str48);
                                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                                }
                                                                                Log.w(str45, str18);
                                                                                hashMap.put("import_shorter_pixels", "576");
                                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                                }
                                                                                Log.w(str45, str17);
                                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                                }
                                                                                Log.w(str45, str16);
                                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                                }
                                                                                Log.w(str45, str15);
                                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                                }
                                                                                Log.w(str45, str14);
                                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                                }
                                                                                Log.w(str45, str13);
                                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                                }
                                                                                Log.w(str45, str12);
                                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                                }
                                                                                Log.w(str45, str11);
                                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                                }
                                                                                Log.w(str45, str10);
                                                                                hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                                }
                                                                                Log.w(str45, str9);
                                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                                }
                                                                                Log.w(str45, str8);
                                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                                }
                                                                                Log.w(str45, str7);
                                                                                hashMap.put("earphone_echo_normal", str44);
                                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                                }
                                                                                Log.w(str45, str6);
                                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                                }
                                                                                Log.w(str45, str5);
                                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                                return hashMap;
                                                                            }
                                                                            StringBuilder sb35 = new StringBuilder();
                                                                            sb35.append("Fetched config doesn't pass:(value > 0) record_video_hw_bitrate = ");
                                                                            sb35.append(i9);
                                                                            str34 = sb35.toString();
                                                                        } else {
                                                                            str34 = "Fetched config doesn't contain: record_video_hw_bitrate";
                                                                        }
                                                                        Log.w(str45, str34);
                                                                        hashMap.put("record_video_hw_bitrate", str51);
                                                                        if (!jSONObject.has("record_encode_mode")) {
                                                                        }
                                                                        Log.w(str45, str33);
                                                                        hashMap.put("record_encode_mode", str48);
                                                                        if (jSONObject.has("record_hw_bitrate_mode")) {
                                                                        }
                                                                        Log.w(str45, str32);
                                                                        hashMap.put("record_hw_bitrate_mode", str48);
                                                                        if (!jSONObject.has("record_hw_profile")) {
                                                                        }
                                                                        Log.w(str45, str31);
                                                                        hashMap.put("record_hw_profile", str48);
                                                                        if (jSONObject.has("record_resolution_width")) {
                                                                        }
                                                                        Log.w(str45, str30);
                                                                        hashMap.put("record_resolution_width", "576");
                                                                        if (!jSONObject.has("record_resolution_height")) {
                                                                        }
                                                                        Log.w(str45, str29);
                                                                        hashMap.put("record_resolution_height", "1024");
                                                                        if (jSONObject.has("import_video_sw_crf")) {
                                                                        }
                                                                        Log.w(str45, str28);
                                                                        hashMap.put("import_video_sw_crf", str46);
                                                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                        }
                                                                        Log.w(str45, str27);
                                                                        hashMap.put("import_video_sw_maxrate", str47);
                                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                                        }
                                                                        Log.w(str45, str26);
                                                                        hashMap.put("import_video_sw_preset", str48);
                                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                                        }
                                                                        Log.w(str45, str25);
                                                                        hashMap.put("import_video_sw_gop", str49);
                                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                                        }
                                                                        Log.w(str45, str24);
                                                                        hashMap.put("import_video_sw_qp", str50);
                                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                        }
                                                                        Log.w(str45, str23);
                                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                                        if (jSONObject.has("import_encode_mode")) {
                                                                        }
                                                                        Log.w(str45, str22);
                                                                        hashMap.put("import_encode_mode", str48);
                                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                        }
                                                                        Log.w(str45, str21);
                                                                        hashMap.put("import_video_hw_bitrate", str51);
                                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                        }
                                                                        Log.w(str45, str20);
                                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                                        if (!jSONObject.has("import_hw_profile")) {
                                                                        }
                                                                        Log.w(str45, str19);
                                                                        hashMap.put("import_hw_profile", str48);
                                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                                        }
                                                                        Log.w(str45, str18);
                                                                        hashMap.put("import_shorter_pixels", "576");
                                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                        }
                                                                        Log.w(str45, str17);
                                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                        }
                                                                        Log.w(str45, str16);
                                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                        }
                                                                        Log.w(str45, str15);
                                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                        }
                                                                        Log.w(str45, str14);
                                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                        }
                                                                        Log.w(str45, str13);
                                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                        }
                                                                        Log.w(str45, str12);
                                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                                        }
                                                                        Log.w(str45, str11);
                                                                        hashMap.put("synthetic_encode_mode", str48);
                                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                        }
                                                                        Log.w(str45, str10);
                                                                        hashMap.put("synthetic_video_hw_bitrate", str51);
                                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                        }
                                                                        Log.w(str45, str9);
                                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                                        }
                                                                        Log.w(str45, str8);
                                                                        hashMap.put("synthetic_hw_profile", str48);
                                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                                        }
                                                                        Log.w(str45, str7);
                                                                        hashMap.put("earphone_echo_normal", str44);
                                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                                        }
                                                                        Log.w(str45, str6);
                                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                                        }
                                                                        Log.w(str45, str5);
                                                                        hashMap.put("earphone_echo_huawei", str44);
                                                                        return hashMap;
                                                                    }
                                                                } else {
                                                                    str35 = "Fetched config doesn't contain: record_sw_bitrate_mode";
                                                                }
                                                                Log.w(str45, str35);
                                                                hashMap.put("record_sw_bitrate_mode", str44);
                                                                String str512 = "4194304";
                                                                if (jSONObject.has("record_video_hw_bitrate")) {
                                                                }
                                                                Log.w(str45, str34);
                                                                hashMap.put("record_video_hw_bitrate", str512);
                                                                if (!jSONObject.has("record_encode_mode")) {
                                                                }
                                                                Log.w(str45, str33);
                                                                hashMap.put("record_encode_mode", str48);
                                                                if (jSONObject.has("record_hw_bitrate_mode")) {
                                                                }
                                                                Log.w(str45, str32);
                                                                hashMap.put("record_hw_bitrate_mode", str48);
                                                                if (!jSONObject.has("record_hw_profile")) {
                                                                }
                                                                Log.w(str45, str31);
                                                                hashMap.put("record_hw_profile", str48);
                                                                if (jSONObject.has("record_resolution_width")) {
                                                                }
                                                                Log.w(str45, str30);
                                                                hashMap.put("record_resolution_width", "576");
                                                                if (!jSONObject.has("record_resolution_height")) {
                                                                }
                                                                Log.w(str45, str29);
                                                                hashMap.put("record_resolution_height", "1024");
                                                                if (jSONObject.has("import_video_sw_crf")) {
                                                                }
                                                                Log.w(str45, str28);
                                                                hashMap.put("import_video_sw_crf", str46);
                                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                                }
                                                                Log.w(str45, str27);
                                                                hashMap.put("import_video_sw_maxrate", str47);
                                                                if (jSONObject.has("import_video_sw_preset")) {
                                                                }
                                                                Log.w(str45, str26);
                                                                hashMap.put("import_video_sw_preset", str48);
                                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                                }
                                                                Log.w(str45, str25);
                                                                hashMap.put("import_video_sw_gop", str49);
                                                                if (jSONObject.has("import_video_sw_qp")) {
                                                                }
                                                                Log.w(str45, str24);
                                                                hashMap.put("import_video_sw_qp", str50);
                                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                                }
                                                                Log.w(str45, str23);
                                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                                if (jSONObject.has("import_encode_mode")) {
                                                                }
                                                                Log.w(str45, str22);
                                                                hashMap.put("import_encode_mode", str48);
                                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                                }
                                                                Log.w(str45, str21);
                                                                hashMap.put("import_video_hw_bitrate", str512);
                                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                                }
                                                                Log.w(str45, str20);
                                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                                if (!jSONObject.has("import_hw_profile")) {
                                                                }
                                                                Log.w(str45, str19);
                                                                hashMap.put("import_hw_profile", str48);
                                                                if (jSONObject.has("import_shorter_pixels")) {
                                                                }
                                                                Log.w(str45, str18);
                                                                hashMap.put("import_shorter_pixels", "576");
                                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                                }
                                                                Log.w(str45, str17);
                                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                                }
                                                                Log.w(str45, str16);
                                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                                }
                                                                Log.w(str45, str15);
                                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                                }
                                                                Log.w(str45, str14);
                                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                                }
                                                                Log.w(str45, str13);
                                                                hashMap.put("synthetic_video_sw_qp", str50);
                                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                                }
                                                                Log.w(str45, str12);
                                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                                }
                                                                Log.w(str45, str11);
                                                                hashMap.put("synthetic_encode_mode", str48);
                                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                                }
                                                                Log.w(str45, str10);
                                                                hashMap.put("synthetic_video_hw_bitrate", str512);
                                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                                }
                                                                Log.w(str45, str9);
                                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                                }
                                                                Log.w(str45, str8);
                                                                hashMap.put("synthetic_hw_profile", str48);
                                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                                }
                                                                Log.w(str45, str7);
                                                                hashMap.put("earphone_echo_normal", str44);
                                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                                }
                                                                Log.w(str45, str6);
                                                                hashMap.put("earphone_echo_aaudio", str44);
                                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                                }
                                                                Log.w(str45, str5);
                                                                hashMap.put("earphone_echo_huawei", str44);
                                                                return hashMap;
                                                            }
                                                        } else {
                                                            str36 = "Fetched config doesn't contain: record_video_sw_qp";
                                                        }
                                                        Log.w(str45, str36);
                                                        hashMap.put("record_video_sw_qp", str50);
                                                        if (!jSONObject.has("record_sw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str35);
                                                        hashMap.put("record_sw_bitrate_mode", str44);
                                                        String str5122 = "4194304";
                                                        if (jSONObject.has("record_video_hw_bitrate")) {
                                                        }
                                                        Log.w(str45, str34);
                                                        hashMap.put("record_video_hw_bitrate", str5122);
                                                        if (!jSONObject.has("record_encode_mode")) {
                                                        }
                                                        Log.w(str45, str33);
                                                        hashMap.put("record_encode_mode", str48);
                                                        if (jSONObject.has("record_hw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str32);
                                                        hashMap.put("record_hw_bitrate_mode", str48);
                                                        if (!jSONObject.has("record_hw_profile")) {
                                                        }
                                                        Log.w(str45, str31);
                                                        hashMap.put("record_hw_profile", str48);
                                                        if (jSONObject.has("record_resolution_width")) {
                                                        }
                                                        Log.w(str45, str30);
                                                        hashMap.put("record_resolution_width", "576");
                                                        if (!jSONObject.has("record_resolution_height")) {
                                                        }
                                                        Log.w(str45, str29);
                                                        hashMap.put("record_resolution_height", "1024");
                                                        if (jSONObject.has("import_video_sw_crf")) {
                                                        }
                                                        Log.w(str45, str28);
                                                        hashMap.put("import_video_sw_crf", str46);
                                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                                        }
                                                        Log.w(str45, str27);
                                                        hashMap.put("import_video_sw_maxrate", str47);
                                                        if (jSONObject.has("import_video_sw_preset")) {
                                                        }
                                                        Log.w(str45, str26);
                                                        hashMap.put("import_video_sw_preset", str48);
                                                        if (!jSONObject.has("import_video_sw_gop")) {
                                                        }
                                                        Log.w(str45, str25);
                                                        hashMap.put("import_video_sw_gop", str49);
                                                        if (jSONObject.has("import_video_sw_qp")) {
                                                        }
                                                        Log.w(str45, str24);
                                                        hashMap.put("import_video_sw_qp", str50);
                                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str23);
                                                        hashMap.put("import_sw_bitrate_mode", str48);
                                                        if (jSONObject.has("import_encode_mode")) {
                                                        }
                                                        Log.w(str45, str22);
                                                        hashMap.put("import_encode_mode", str48);
                                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                                        }
                                                        Log.w(str45, str21);
                                                        hashMap.put("import_video_hw_bitrate", str5122);
                                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str20);
                                                        hashMap.put("import_hw_bitrate_mode", str48);
                                                        if (!jSONObject.has("import_hw_profile")) {
                                                        }
                                                        Log.w(str45, str19);
                                                        hashMap.put("import_hw_profile", str48);
                                                        if (jSONObject.has("import_shorter_pixels")) {
                                                        }
                                                        Log.w(str45, str18);
                                                        hashMap.put("import_shorter_pixels", "576");
                                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                        }
                                                        Log.w(str45, str17);
                                                        hashMap.put("synthetic_video_sw_crf", str46);
                                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                        }
                                                        Log.w(str45, str16);
                                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                        }
                                                        Log.w(str45, str15);
                                                        hashMap.put("synthetic_video_sw_preset", str48);
                                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                                        }
                                                        Log.w(str45, str14);
                                                        hashMap.put("synthetic_video_sw_gop", str49);
                                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                        }
                                                        Log.w(str45, str13);
                                                        hashMap.put("synthetic_video_sw_qp", str50);
                                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str12);
                                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                                        }
                                                        Log.w(str45, str11);
                                                        hashMap.put("synthetic_encode_mode", str48);
                                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                        }
                                                        Log.w(str45, str10);
                                                        hashMap.put("synthetic_video_hw_bitrate", str5122);
                                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                        }
                                                        Log.w(str45, str9);
                                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                        if (jSONObject.has("synthetic_hw_profile")) {
                                                        }
                                                        Log.w(str45, str8);
                                                        hashMap.put("synthetic_hw_profile", str48);
                                                        if (!jSONObject.has("earphone_echo_normal")) {
                                                        }
                                                        Log.w(str45, str7);
                                                        hashMap.put("earphone_echo_normal", str44);
                                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                                        }
                                                        Log.w(str45, str6);
                                                        hashMap.put("earphone_echo_aaudio", str44);
                                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                                        }
                                                        Log.w(str45, str5);
                                                        hashMap.put("earphone_echo_huawei", str44);
                                                        return hashMap;
                                                    }
                                                    StringBuilder sb36 = new StringBuilder();
                                                    sb36.append("Fetched config doesn't pass:(value >= 1) record_video_sw_gop = ");
                                                    sb36.append(i6);
                                                    str37 = sb36.toString();
                                                } else {
                                                    str37 = "Fetched config doesn't contain: record_video_sw_gop";
                                                }
                                                Log.w(str45, str37);
                                                hashMap.put("record_video_sw_gop", str49);
                                                String str502 = "2";
                                                if (jSONObject.has("record_video_sw_qp")) {
                                                }
                                                Log.w(str45, str36);
                                                hashMap.put("record_video_sw_qp", str502);
                                                if (!jSONObject.has("record_sw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str35);
                                                hashMap.put("record_sw_bitrate_mode", str44);
                                                String str51222 = "4194304";
                                                if (jSONObject.has("record_video_hw_bitrate")) {
                                                }
                                                Log.w(str45, str34);
                                                hashMap.put("record_video_hw_bitrate", str51222);
                                                if (!jSONObject.has("record_encode_mode")) {
                                                }
                                                Log.w(str45, str33);
                                                hashMap.put("record_encode_mode", str48);
                                                if (jSONObject.has("record_hw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str32);
                                                hashMap.put("record_hw_bitrate_mode", str48);
                                                if (!jSONObject.has("record_hw_profile")) {
                                                }
                                                Log.w(str45, str31);
                                                hashMap.put("record_hw_profile", str48);
                                                if (jSONObject.has("record_resolution_width")) {
                                                }
                                                Log.w(str45, str30);
                                                hashMap.put("record_resolution_width", "576");
                                                if (!jSONObject.has("record_resolution_height")) {
                                                }
                                                Log.w(str45, str29);
                                                hashMap.put("record_resolution_height", "1024");
                                                if (jSONObject.has("import_video_sw_crf")) {
                                                }
                                                Log.w(str45, str28);
                                                hashMap.put("import_video_sw_crf", str46);
                                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                                }
                                                Log.w(str45, str27);
                                                hashMap.put("import_video_sw_maxrate", str47);
                                                if (jSONObject.has("import_video_sw_preset")) {
                                                }
                                                Log.w(str45, str26);
                                                hashMap.put("import_video_sw_preset", str48);
                                                if (!jSONObject.has("import_video_sw_gop")) {
                                                }
                                                Log.w(str45, str25);
                                                hashMap.put("import_video_sw_gop", str49);
                                                if (jSONObject.has("import_video_sw_qp")) {
                                                }
                                                Log.w(str45, str24);
                                                hashMap.put("import_video_sw_qp", str502);
                                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str23);
                                                hashMap.put("import_sw_bitrate_mode", str48);
                                                if (jSONObject.has("import_encode_mode")) {
                                                }
                                                Log.w(str45, str22);
                                                hashMap.put("import_encode_mode", str48);
                                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                                }
                                                Log.w(str45, str21);
                                                hashMap.put("import_video_hw_bitrate", str51222);
                                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str20);
                                                hashMap.put("import_hw_bitrate_mode", str48);
                                                if (!jSONObject.has("import_hw_profile")) {
                                                }
                                                Log.w(str45, str19);
                                                hashMap.put("import_hw_profile", str48);
                                                if (jSONObject.has("import_shorter_pixels")) {
                                                }
                                                Log.w(str45, str18);
                                                hashMap.put("import_shorter_pixels", "576");
                                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                                }
                                                Log.w(str45, str17);
                                                hashMap.put("synthetic_video_sw_crf", str46);
                                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                                }
                                                Log.w(str45, str16);
                                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                                }
                                                Log.w(str45, str15);
                                                hashMap.put("synthetic_video_sw_preset", str48);
                                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                                }
                                                Log.w(str45, str14);
                                                hashMap.put("synthetic_video_sw_gop", str49);
                                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                                }
                                                Log.w(str45, str13);
                                                hashMap.put("synthetic_video_sw_qp", str502);
                                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str12);
                                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                                if (!jSONObject.has("synthetic_encode_mode")) {
                                                }
                                                Log.w(str45, str11);
                                                hashMap.put("synthetic_encode_mode", str48);
                                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                                }
                                                Log.w(str45, str10);
                                                hashMap.put("synthetic_video_hw_bitrate", str51222);
                                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                                }
                                                Log.w(str45, str9);
                                                hashMap.put("synthetic_hw_bitrate_mode", str48);
                                                if (jSONObject.has("synthetic_hw_profile")) {
                                                }
                                                Log.w(str45, str8);
                                                hashMap.put("synthetic_hw_profile", str48);
                                                if (!jSONObject.has("earphone_echo_normal")) {
                                                }
                                                Log.w(str45, str7);
                                                hashMap.put("earphone_echo_normal", str44);
                                                if (jSONObject.has("earphone_echo_aaudio")) {
                                                }
                                                Log.w(str45, str6);
                                                hashMap.put("earphone_echo_aaudio", str44);
                                                if (!jSONObject.has("earphone_echo_huawei")) {
                                                }
                                                Log.w(str45, str5);
                                                hashMap.put("earphone_echo_huawei", str44);
                                                return hashMap;
                                            }
                                        } else {
                                            str38 = "Fetched config doesn't contain: record_video_sw_preset";
                                        }
                                        Log.w(str45, str38);
                                        hashMap.put(str4, str48);
                                        String str492 = "35";
                                        if (!jSONObject.has("record_video_sw_gop")) {
                                        }
                                        Log.w(str45, str37);
                                        hashMap.put("record_video_sw_gop", str492);
                                        String str5022 = "2";
                                        if (jSONObject.has("record_video_sw_qp")) {
                                        }
                                        Log.w(str45, str36);
                                        hashMap.put("record_video_sw_qp", str5022);
                                        if (!jSONObject.has("record_sw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str35);
                                        hashMap.put("record_sw_bitrate_mode", str44);
                                        String str512222 = "4194304";
                                        if (jSONObject.has("record_video_hw_bitrate")) {
                                        }
                                        Log.w(str45, str34);
                                        hashMap.put("record_video_hw_bitrate", str512222);
                                        if (!jSONObject.has("record_encode_mode")) {
                                        }
                                        Log.w(str45, str33);
                                        hashMap.put("record_encode_mode", str48);
                                        if (jSONObject.has("record_hw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str32);
                                        hashMap.put("record_hw_bitrate_mode", str48);
                                        if (!jSONObject.has("record_hw_profile")) {
                                        }
                                        Log.w(str45, str31);
                                        hashMap.put("record_hw_profile", str48);
                                        if (jSONObject.has("record_resolution_width")) {
                                        }
                                        Log.w(str45, str30);
                                        hashMap.put("record_resolution_width", "576");
                                        if (!jSONObject.has("record_resolution_height")) {
                                        }
                                        Log.w(str45, str29);
                                        hashMap.put("record_resolution_height", "1024");
                                        if (jSONObject.has("import_video_sw_crf")) {
                                        }
                                        Log.w(str45, str28);
                                        hashMap.put("import_video_sw_crf", str46);
                                        if (!jSONObject.has("import_video_sw_maxrate")) {
                                        }
                                        Log.w(str45, str27);
                                        hashMap.put("import_video_sw_maxrate", str47);
                                        if (jSONObject.has("import_video_sw_preset")) {
                                        }
                                        Log.w(str45, str26);
                                        hashMap.put("import_video_sw_preset", str48);
                                        if (!jSONObject.has("import_video_sw_gop")) {
                                        }
                                        Log.w(str45, str25);
                                        hashMap.put("import_video_sw_gop", str492);
                                        if (jSONObject.has("import_video_sw_qp")) {
                                        }
                                        Log.w(str45, str24);
                                        hashMap.put("import_video_sw_qp", str5022);
                                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str23);
                                        hashMap.put("import_sw_bitrate_mode", str48);
                                        if (jSONObject.has("import_encode_mode")) {
                                        }
                                        Log.w(str45, str22);
                                        hashMap.put("import_encode_mode", str48);
                                        if (!jSONObject.has("import_video_hw_bitrate")) {
                                        }
                                        Log.w(str45, str21);
                                        hashMap.put("import_video_hw_bitrate", str512222);
                                        if (jSONObject.has("import_hw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str20);
                                        hashMap.put("import_hw_bitrate_mode", str48);
                                        if (!jSONObject.has("import_hw_profile")) {
                                        }
                                        Log.w(str45, str19);
                                        hashMap.put("import_hw_profile", str48);
                                        if (jSONObject.has("import_shorter_pixels")) {
                                        }
                                        Log.w(str45, str18);
                                        hashMap.put("import_shorter_pixels", "576");
                                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                                        }
                                        Log.w(str45, str17);
                                        hashMap.put("synthetic_video_sw_crf", str46);
                                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                        }
                                        Log.w(str45, str16);
                                        hashMap.put("synthetic_video_sw_maxrate", str47);
                                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                                        }
                                        Log.w(str45, str15);
                                        hashMap.put("synthetic_video_sw_preset", str48);
                                        if (jSONObject.has("synthetic_video_sw_gop")) {
                                        }
                                        Log.w(str45, str14);
                                        hashMap.put("synthetic_video_sw_gop", str492);
                                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                                        }
                                        Log.w(str45, str13);
                                        hashMap.put("synthetic_video_sw_qp", str5022);
                                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str12);
                                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                                        if (!jSONObject.has("synthetic_encode_mode")) {
                                        }
                                        Log.w(str45, str11);
                                        hashMap.put("synthetic_encode_mode", str48);
                                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                        }
                                        Log.w(str45, str10);
                                        hashMap.put("synthetic_video_hw_bitrate", str512222);
                                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                        }
                                        Log.w(str45, str9);
                                        hashMap.put("synthetic_hw_bitrate_mode", str48);
                                        if (jSONObject.has("synthetic_hw_profile")) {
                                        }
                                        Log.w(str45, str8);
                                        hashMap.put("synthetic_hw_profile", str48);
                                        if (!jSONObject.has("earphone_echo_normal")) {
                                        }
                                        Log.w(str45, str7);
                                        hashMap.put("earphone_echo_normal", str44);
                                        if (jSONObject.has("earphone_echo_aaudio")) {
                                        }
                                        Log.w(str45, str6);
                                        hashMap.put("earphone_echo_aaudio", str44);
                                        if (!jSONObject.has("earphone_echo_huawei")) {
                                        }
                                        Log.w(str45, str5);
                                        hashMap.put("earphone_echo_huawei", str44);
                                        return hashMap;
                                    }
                                } else {
                                    str39 = "Fetched config doesn't contain: record_video_sw_maxrate";
                                }
                                Log.w(str45, str39);
                                hashMap.put(str3, str47);
                                str4 = "record_video_sw_preset";
                                String str482 = "0";
                                if (jSONObject.has(str4)) {
                                }
                                Log.w(str45, str38);
                                hashMap.put(str4, str482);
                                String str4922 = "35";
                                if (!jSONObject.has("record_video_sw_gop")) {
                                }
                                Log.w(str45, str37);
                                hashMap.put("record_video_sw_gop", str4922);
                                String str50222 = "2";
                                if (jSONObject.has("record_video_sw_qp")) {
                                }
                                Log.w(str45, str36);
                                hashMap.put("record_video_sw_qp", str50222);
                                if (!jSONObject.has("record_sw_bitrate_mode")) {
                                }
                                Log.w(str45, str35);
                                hashMap.put("record_sw_bitrate_mode", str44);
                                String str5122222 = "4194304";
                                if (jSONObject.has("record_video_hw_bitrate")) {
                                }
                                Log.w(str45, str34);
                                hashMap.put("record_video_hw_bitrate", str5122222);
                                if (!jSONObject.has("record_encode_mode")) {
                                }
                                Log.w(str45, str33);
                                hashMap.put("record_encode_mode", str482);
                                if (jSONObject.has("record_hw_bitrate_mode")) {
                                }
                                Log.w(str45, str32);
                                hashMap.put("record_hw_bitrate_mode", str482);
                                if (!jSONObject.has("record_hw_profile")) {
                                }
                                Log.w(str45, str31);
                                hashMap.put("record_hw_profile", str482);
                                if (jSONObject.has("record_resolution_width")) {
                                }
                                Log.w(str45, str30);
                                hashMap.put("record_resolution_width", "576");
                                if (!jSONObject.has("record_resolution_height")) {
                                }
                                Log.w(str45, str29);
                                hashMap.put("record_resolution_height", "1024");
                                if (jSONObject.has("import_video_sw_crf")) {
                                }
                                Log.w(str45, str28);
                                hashMap.put("import_video_sw_crf", str46);
                                if (!jSONObject.has("import_video_sw_maxrate")) {
                                }
                                Log.w(str45, str27);
                                hashMap.put("import_video_sw_maxrate", str47);
                                if (jSONObject.has("import_video_sw_preset")) {
                                }
                                Log.w(str45, str26);
                                hashMap.put("import_video_sw_preset", str482);
                                if (!jSONObject.has("import_video_sw_gop")) {
                                }
                                Log.w(str45, str25);
                                hashMap.put("import_video_sw_gop", str4922);
                                if (jSONObject.has("import_video_sw_qp")) {
                                }
                                Log.w(str45, str24);
                                hashMap.put("import_video_sw_qp", str50222);
                                if (!jSONObject.has("import_sw_bitrate_mode")) {
                                }
                                Log.w(str45, str23);
                                hashMap.put("import_sw_bitrate_mode", str482);
                                if (jSONObject.has("import_encode_mode")) {
                                }
                                Log.w(str45, str22);
                                hashMap.put("import_encode_mode", str482);
                                if (!jSONObject.has("import_video_hw_bitrate")) {
                                }
                                Log.w(str45, str21);
                                hashMap.put("import_video_hw_bitrate", str5122222);
                                if (jSONObject.has("import_hw_bitrate_mode")) {
                                }
                                Log.w(str45, str20);
                                hashMap.put("import_hw_bitrate_mode", str482);
                                if (!jSONObject.has("import_hw_profile")) {
                                }
                                Log.w(str45, str19);
                                hashMap.put("import_hw_profile", str482);
                                if (jSONObject.has("import_shorter_pixels")) {
                                }
                                Log.w(str45, str18);
                                hashMap.put("import_shorter_pixels", "576");
                                if (!jSONObject.has("synthetic_video_sw_crf")) {
                                }
                                Log.w(str45, str17);
                                hashMap.put("synthetic_video_sw_crf", str46);
                                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                                }
                                Log.w(str45, str16);
                                hashMap.put("synthetic_video_sw_maxrate", str47);
                                if (!jSONObject.has("synthetic_video_sw_preset")) {
                                }
                                Log.w(str45, str15);
                                hashMap.put("synthetic_video_sw_preset", str482);
                                if (jSONObject.has("synthetic_video_sw_gop")) {
                                }
                                Log.w(str45, str14);
                                hashMap.put("synthetic_video_sw_gop", str4922);
                                if (!jSONObject.has("synthetic_video_sw_qp")) {
                                }
                                Log.w(str45, str13);
                                hashMap.put("synthetic_video_sw_qp", str50222);
                                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                                }
                                Log.w(str45, str12);
                                hashMap.put("synthetic_sw_bitrate_mode", str44);
                                if (!jSONObject.has("synthetic_encode_mode")) {
                                }
                                Log.w(str45, str11);
                                hashMap.put("synthetic_encode_mode", str482);
                                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                                }
                                Log.w(str45, str10);
                                hashMap.put("synthetic_video_hw_bitrate", str5122222);
                                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                                }
                                Log.w(str45, str9);
                                hashMap.put("synthetic_hw_bitrate_mode", str482);
                                if (jSONObject.has("synthetic_hw_profile")) {
                                }
                                Log.w(str45, str8);
                                hashMap.put("synthetic_hw_profile", str482);
                                if (!jSONObject.has("earphone_echo_normal")) {
                                }
                                Log.w(str45, str7);
                                hashMap.put("earphone_echo_normal", str44);
                                if (jSONObject.has("earphone_echo_aaudio")) {
                                }
                                Log.w(str45, str6);
                                hashMap.put("earphone_echo_aaudio", str44);
                                if (!jSONObject.has("earphone_echo_huawei")) {
                                }
                                Log.w(str45, str5);
                                hashMap.put("earphone_echo_huawei", str44);
                                return hashMap;
                            }
                        } else {
                            str40 = "Fetched config doesn't contain: record_video_sw_crf";
                        }
                        Log.w(str45, str40);
                        hashMap.put(str2, str46);
                        str3 = "record_video_sw_maxrate";
                        String str472 = "15000000";
                        if (!jSONObject.has(str3)) {
                        }
                        Log.w(str45, str39);
                        hashMap.put(str3, str472);
                        str4 = "record_video_sw_preset";
                        String str4822 = "0";
                        if (jSONObject.has(str4)) {
                        }
                        Log.w(str45, str38);
                        hashMap.put(str4, str4822);
                        String str49222 = "35";
                        if (!jSONObject.has("record_video_sw_gop")) {
                        }
                        Log.w(str45, str37);
                        hashMap.put("record_video_sw_gop", str49222);
                        String str502222 = "2";
                        if (jSONObject.has("record_video_sw_qp")) {
                        }
                        Log.w(str45, str36);
                        hashMap.put("record_video_sw_qp", str502222);
                        if (!jSONObject.has("record_sw_bitrate_mode")) {
                        }
                        Log.w(str45, str35);
                        hashMap.put("record_sw_bitrate_mode", str44);
                        String str51222222 = "4194304";
                        if (jSONObject.has("record_video_hw_bitrate")) {
                        }
                        Log.w(str45, str34);
                        hashMap.put("record_video_hw_bitrate", str51222222);
                        if (!jSONObject.has("record_encode_mode")) {
                        }
                        Log.w(str45, str33);
                        hashMap.put("record_encode_mode", str4822);
                        if (jSONObject.has("record_hw_bitrate_mode")) {
                        }
                        Log.w(str45, str32);
                        hashMap.put("record_hw_bitrate_mode", str4822);
                        if (!jSONObject.has("record_hw_profile")) {
                        }
                        Log.w(str45, str31);
                        hashMap.put("record_hw_profile", str4822);
                        if (jSONObject.has("record_resolution_width")) {
                        }
                        Log.w(str45, str30);
                        hashMap.put("record_resolution_width", "576");
                        if (!jSONObject.has("record_resolution_height")) {
                        }
                        Log.w(str45, str29);
                        hashMap.put("record_resolution_height", "1024");
                        if (jSONObject.has("import_video_sw_crf")) {
                        }
                        Log.w(str45, str28);
                        hashMap.put("import_video_sw_crf", str46);
                        if (!jSONObject.has("import_video_sw_maxrate")) {
                        }
                        Log.w(str45, str27);
                        hashMap.put("import_video_sw_maxrate", str472);
                        if (jSONObject.has("import_video_sw_preset")) {
                        }
                        Log.w(str45, str26);
                        hashMap.put("import_video_sw_preset", str4822);
                        if (!jSONObject.has("import_video_sw_gop")) {
                        }
                        Log.w(str45, str25);
                        hashMap.put("import_video_sw_gop", str49222);
                        if (jSONObject.has("import_video_sw_qp")) {
                        }
                        Log.w(str45, str24);
                        hashMap.put("import_video_sw_qp", str502222);
                        if (!jSONObject.has("import_sw_bitrate_mode")) {
                        }
                        Log.w(str45, str23);
                        hashMap.put("import_sw_bitrate_mode", str4822);
                        if (jSONObject.has("import_encode_mode")) {
                        }
                        Log.w(str45, str22);
                        hashMap.put("import_encode_mode", str4822);
                        if (!jSONObject.has("import_video_hw_bitrate")) {
                        }
                        Log.w(str45, str21);
                        hashMap.put("import_video_hw_bitrate", str51222222);
                        if (jSONObject.has("import_hw_bitrate_mode")) {
                        }
                        Log.w(str45, str20);
                        hashMap.put("import_hw_bitrate_mode", str4822);
                        if (!jSONObject.has("import_hw_profile")) {
                        }
                        Log.w(str45, str19);
                        hashMap.put("import_hw_profile", str4822);
                        if (jSONObject.has("import_shorter_pixels")) {
                        }
                        Log.w(str45, str18);
                        hashMap.put("import_shorter_pixels", "576");
                        if (!jSONObject.has("synthetic_video_sw_crf")) {
                        }
                        Log.w(str45, str17);
                        hashMap.put("synthetic_video_sw_crf", str46);
                        if (jSONObject.has("synthetic_video_sw_maxrate")) {
                        }
                        Log.w(str45, str16);
                        hashMap.put("synthetic_video_sw_maxrate", str472);
                        if (!jSONObject.has("synthetic_video_sw_preset")) {
                        }
                        Log.w(str45, str15);
                        hashMap.put("synthetic_video_sw_preset", str4822);
                        if (jSONObject.has("synthetic_video_sw_gop")) {
                        }
                        Log.w(str45, str14);
                        hashMap.put("synthetic_video_sw_gop", str49222);
                        if (!jSONObject.has("synthetic_video_sw_qp")) {
                        }
                        Log.w(str45, str13);
                        hashMap.put("synthetic_video_sw_qp", str502222);
                        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                        }
                        Log.w(str45, str12);
                        hashMap.put("synthetic_sw_bitrate_mode", str44);
                        if (!jSONObject.has("synthetic_encode_mode")) {
                        }
                        Log.w(str45, str11);
                        hashMap.put("synthetic_encode_mode", str4822);
                        if (jSONObject.has("synthetic_video_hw_bitrate")) {
                        }
                        Log.w(str45, str10);
                        hashMap.put("synthetic_video_hw_bitrate", str51222222);
                        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                        }
                        Log.w(str45, str9);
                        hashMap.put("synthetic_hw_bitrate_mode", str4822);
                        if (jSONObject.has("synthetic_hw_profile")) {
                        }
                        Log.w(str45, str8);
                        hashMap.put("synthetic_hw_profile", str4822);
                        if (!jSONObject.has("earphone_echo_normal")) {
                        }
                        Log.w(str45, str7);
                        hashMap.put("earphone_echo_normal", str44);
                        if (jSONObject.has("earphone_echo_aaudio")) {
                        }
                        Log.w(str45, str6);
                        hashMap.put("earphone_echo_aaudio", str44);
                        if (!jSONObject.has("earphone_echo_huawei")) {
                        }
                        Log.w(str45, str5);
                        hashMap.put("earphone_echo_huawei", str44);
                        return hashMap;
                    }
                    StringBuilder sb37 = new StringBuilder();
                    sb37.append("Fetched config doesn't pass:(value >= 0) record_camera_compat_level = ");
                    sb37.append(i2);
                    str41 = sb37.toString();
                } else {
                    str41 = "Fetched config doesn't contain: record_camera_compat_level";
                }
                Log.w(str45, str41);
                hashMap.put(str, str44);
                str2 = "record_video_sw_crf";
                String str462 = "15";
                if (jSONObject.has(str2)) {
                }
                Log.w(str45, str40);
                hashMap.put(str2, str462);
                str3 = "record_video_sw_maxrate";
                String str4722 = "15000000";
                if (!jSONObject.has(str3)) {
                }
                Log.w(str45, str39);
                hashMap.put(str3, str4722);
                str4 = "record_video_sw_preset";
                String str48222 = "0";
                if (jSONObject.has(str4)) {
                }
                Log.w(str45, str38);
                hashMap.put(str4, str48222);
                String str492222 = "35";
                if (!jSONObject.has("record_video_sw_gop")) {
                }
                Log.w(str45, str37);
                hashMap.put("record_video_sw_gop", str492222);
                String str5022222 = "2";
                if (jSONObject.has("record_video_sw_qp")) {
                }
                Log.w(str45, str36);
                hashMap.put("record_video_sw_qp", str5022222);
                if (!jSONObject.has("record_sw_bitrate_mode")) {
                }
                Log.w(str45, str35);
                hashMap.put("record_sw_bitrate_mode", str44);
                String str512222222 = "4194304";
                if (jSONObject.has("record_video_hw_bitrate")) {
                }
                Log.w(str45, str34);
                hashMap.put("record_video_hw_bitrate", str512222222);
                if (!jSONObject.has("record_encode_mode")) {
                }
                Log.w(str45, str33);
                hashMap.put("record_encode_mode", str48222);
                if (jSONObject.has("record_hw_bitrate_mode")) {
                }
                Log.w(str45, str32);
                hashMap.put("record_hw_bitrate_mode", str48222);
                if (!jSONObject.has("record_hw_profile")) {
                }
                Log.w(str45, str31);
                hashMap.put("record_hw_profile", str48222);
                if (jSONObject.has("record_resolution_width")) {
                }
                Log.w(str45, str30);
                hashMap.put("record_resolution_width", "576");
                if (!jSONObject.has("record_resolution_height")) {
                }
                Log.w(str45, str29);
                hashMap.put("record_resolution_height", "1024");
                if (jSONObject.has("import_video_sw_crf")) {
                }
                Log.w(str45, str28);
                hashMap.put("import_video_sw_crf", str462);
                if (!jSONObject.has("import_video_sw_maxrate")) {
                }
                Log.w(str45, str27);
                hashMap.put("import_video_sw_maxrate", str4722);
                if (jSONObject.has("import_video_sw_preset")) {
                }
                Log.w(str45, str26);
                hashMap.put("import_video_sw_preset", str48222);
                if (!jSONObject.has("import_video_sw_gop")) {
                }
                Log.w(str45, str25);
                hashMap.put("import_video_sw_gop", str492222);
                if (jSONObject.has("import_video_sw_qp")) {
                }
                Log.w(str45, str24);
                hashMap.put("import_video_sw_qp", str5022222);
                if (!jSONObject.has("import_sw_bitrate_mode")) {
                }
                Log.w(str45, str23);
                hashMap.put("import_sw_bitrate_mode", str48222);
                if (jSONObject.has("import_encode_mode")) {
                }
                Log.w(str45, str22);
                hashMap.put("import_encode_mode", str48222);
                if (!jSONObject.has("import_video_hw_bitrate")) {
                }
                Log.w(str45, str21);
                hashMap.put("import_video_hw_bitrate", str512222222);
                if (jSONObject.has("import_hw_bitrate_mode")) {
                }
                Log.w(str45, str20);
                hashMap.put("import_hw_bitrate_mode", str48222);
                if (!jSONObject.has("import_hw_profile")) {
                }
                Log.w(str45, str19);
                hashMap.put("import_hw_profile", str48222);
                if (jSONObject.has("import_shorter_pixels")) {
                }
                Log.w(str45, str18);
                hashMap.put("import_shorter_pixels", "576");
                if (!jSONObject.has("synthetic_video_sw_crf")) {
                }
                Log.w(str45, str17);
                hashMap.put("synthetic_video_sw_crf", str462);
                if (jSONObject.has("synthetic_video_sw_maxrate")) {
                }
                Log.w(str45, str16);
                hashMap.put("synthetic_video_sw_maxrate", str4722);
                if (!jSONObject.has("synthetic_video_sw_preset")) {
                }
                Log.w(str45, str15);
                hashMap.put("synthetic_video_sw_preset", str48222);
                if (jSONObject.has("synthetic_video_sw_gop")) {
                }
                Log.w(str45, str14);
                hashMap.put("synthetic_video_sw_gop", str492222);
                if (!jSONObject.has("synthetic_video_sw_qp")) {
                }
                Log.w(str45, str13);
                hashMap.put("synthetic_video_sw_qp", str5022222);
                if (jSONObject.has("synthetic_sw_bitrate_mode")) {
                }
                Log.w(str45, str12);
                hashMap.put("synthetic_sw_bitrate_mode", str44);
                if (!jSONObject.has("synthetic_encode_mode")) {
                }
                Log.w(str45, str11);
                hashMap.put("synthetic_encode_mode", str48222);
                if (jSONObject.has("synthetic_video_hw_bitrate")) {
                }
                Log.w(str45, str10);
                hashMap.put("synthetic_video_hw_bitrate", str512222222);
                if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
                }
                Log.w(str45, str9);
                hashMap.put("synthetic_hw_bitrate_mode", str48222);
                if (jSONObject.has("synthetic_hw_profile")) {
                }
                Log.w(str45, str8);
                hashMap.put("synthetic_hw_profile", str48222);
                if (!jSONObject.has("earphone_echo_normal")) {
                }
                Log.w(str45, str7);
                hashMap.put("earphone_echo_normal", str44);
                if (jSONObject.has("earphone_echo_aaudio")) {
                }
                Log.w(str45, str6);
                hashMap.put("earphone_echo_aaudio", str44);
                if (!jSONObject.has("earphone_echo_huawei")) {
                }
                Log.w(str45, str5);
                hashMap.put("earphone_echo_huawei", str44);
                return hashMap;
            }
            StringBuilder sb38 = new StringBuilder();
            sb38.append("Fetched config doesn't pass:(value >= 1) record_camera_type = ");
            sb38.append(i);
            str42 = sb38.toString();
        } else {
            str42 = "Fetched config doesn't contain: record_camera_type";
        }
        Log.w(str45, str42);
        hashMap.put(str43, str44);
        str = "record_camera_compat_level";
        if (!jSONObject.has(str)) {
        }
        Log.w(str45, str41);
        hashMap.put(str, str44);
        str2 = "record_video_sw_crf";
        String str4622 = "15";
        if (jSONObject.has(str2)) {
        }
        Log.w(str45, str40);
        hashMap.put(str2, str4622);
        str3 = "record_video_sw_maxrate";
        String str47222 = "15000000";
        if (!jSONObject.has(str3)) {
        }
        Log.w(str45, str39);
        hashMap.put(str3, str47222);
        str4 = "record_video_sw_preset";
        String str482222 = "0";
        if (jSONObject.has(str4)) {
        }
        Log.w(str45, str38);
        hashMap.put(str4, str482222);
        String str4922222 = "35";
        if (!jSONObject.has("record_video_sw_gop")) {
        }
        Log.w(str45, str37);
        hashMap.put("record_video_sw_gop", str4922222);
        String str50222222 = "2";
        if (jSONObject.has("record_video_sw_qp")) {
        }
        Log.w(str45, str36);
        hashMap.put("record_video_sw_qp", str50222222);
        if (!jSONObject.has("record_sw_bitrate_mode")) {
        }
        Log.w(str45, str35);
        hashMap.put("record_sw_bitrate_mode", str44);
        String str5122222222 = "4194304";
        if (jSONObject.has("record_video_hw_bitrate")) {
        }
        Log.w(str45, str34);
        hashMap.put("record_video_hw_bitrate", str5122222222);
        if (!jSONObject.has("record_encode_mode")) {
        }
        Log.w(str45, str33);
        hashMap.put("record_encode_mode", str482222);
        if (jSONObject.has("record_hw_bitrate_mode")) {
        }
        Log.w(str45, str32);
        hashMap.put("record_hw_bitrate_mode", str482222);
        if (!jSONObject.has("record_hw_profile")) {
        }
        Log.w(str45, str31);
        hashMap.put("record_hw_profile", str482222);
        if (jSONObject.has("record_resolution_width")) {
        }
        Log.w(str45, str30);
        hashMap.put("record_resolution_width", "576");
        if (!jSONObject.has("record_resolution_height")) {
        }
        Log.w(str45, str29);
        hashMap.put("record_resolution_height", "1024");
        if (jSONObject.has("import_video_sw_crf")) {
        }
        Log.w(str45, str28);
        hashMap.put("import_video_sw_crf", str4622);
        if (!jSONObject.has("import_video_sw_maxrate")) {
        }
        Log.w(str45, str27);
        hashMap.put("import_video_sw_maxrate", str47222);
        if (jSONObject.has("import_video_sw_preset")) {
        }
        Log.w(str45, str26);
        hashMap.put("import_video_sw_preset", str482222);
        if (!jSONObject.has("import_video_sw_gop")) {
        }
        Log.w(str45, str25);
        hashMap.put("import_video_sw_gop", str4922222);
        if (jSONObject.has("import_video_sw_qp")) {
        }
        Log.w(str45, str24);
        hashMap.put("import_video_sw_qp", str50222222);
        if (!jSONObject.has("import_sw_bitrate_mode")) {
        }
        Log.w(str45, str23);
        hashMap.put("import_sw_bitrate_mode", str482222);
        if (jSONObject.has("import_encode_mode")) {
        }
        Log.w(str45, str22);
        hashMap.put("import_encode_mode", str482222);
        if (!jSONObject.has("import_video_hw_bitrate")) {
        }
        Log.w(str45, str21);
        hashMap.put("import_video_hw_bitrate", str5122222222);
        if (jSONObject.has("import_hw_bitrate_mode")) {
        }
        Log.w(str45, str20);
        hashMap.put("import_hw_bitrate_mode", str482222);
        if (!jSONObject.has("import_hw_profile")) {
        }
        Log.w(str45, str19);
        hashMap.put("import_hw_profile", str482222);
        if (jSONObject.has("import_shorter_pixels")) {
        }
        Log.w(str45, str18);
        hashMap.put("import_shorter_pixels", "576");
        if (!jSONObject.has("synthetic_video_sw_crf")) {
        }
        Log.w(str45, str17);
        hashMap.put("synthetic_video_sw_crf", str4622);
        if (jSONObject.has("synthetic_video_sw_maxrate")) {
        }
        Log.w(str45, str16);
        hashMap.put("synthetic_video_sw_maxrate", str47222);
        if (!jSONObject.has("synthetic_video_sw_preset")) {
        }
        Log.w(str45, str15);
        hashMap.put("synthetic_video_sw_preset", str482222);
        if (jSONObject.has("synthetic_video_sw_gop")) {
        }
        Log.w(str45, str14);
        hashMap.put("synthetic_video_sw_gop", str4922222);
        if (!jSONObject.has("synthetic_video_sw_qp")) {
        }
        Log.w(str45, str13);
        hashMap.put("synthetic_video_sw_qp", str50222222);
        if (jSONObject.has("synthetic_sw_bitrate_mode")) {
        }
        Log.w(str45, str12);
        hashMap.put("synthetic_sw_bitrate_mode", str44);
        if (!jSONObject.has("synthetic_encode_mode")) {
        }
        Log.w(str45, str11);
        hashMap.put("synthetic_encode_mode", str482222);
        if (jSONObject.has("synthetic_video_hw_bitrate")) {
        }
        Log.w(str45, str10);
        hashMap.put("synthetic_video_hw_bitrate", str5122222222);
        if (!jSONObject.has("synthetic_hw_bitrate_mode")) {
        }
        Log.w(str45, str9);
        hashMap.put("synthetic_hw_bitrate_mode", str482222);
        if (jSONObject.has("synthetic_hw_profile")) {
        }
        Log.w(str45, str8);
        hashMap.put("synthetic_hw_profile", str482222);
        if (!jSONObject.has("earphone_echo_normal")) {
        }
        Log.w(str45, str7);
        hashMap.put("earphone_echo_normal", str44);
        if (jSONObject.has("earphone_echo_aaudio")) {
        }
        Log.w(str45, str6);
        hashMap.put("earphone_echo_aaudio", str44);
        if (!jSONObject.has("earphone_echo_huawei")) {
        }
        Log.w(str45, str5);
        hashMap.put("earphone_echo_huawei", str44);
        return hashMap;
    }

    private void fillWithDefaultValue(@NonNull VECloudConfig vECloudConfig) {
        vECloudConfig.mRecordCameraType = 1;
        vECloudConfig.mRecordCameraCompatLevel = 1;
        vECloudConfig.mRecordSWEncodeCRF = 15;
        vECloudConfig.mRecordVideoSWMaxrate = 15000000;
        vECloudConfig.mRecordVideoSWPreset = 0;
        vECloudConfig.mRecordVideoSWGop = 35;
        vECloudConfig.mRecordVideoSWQP = 2;
        vECloudConfig.mRecordSWBitrateMode = 1;
        vECloudConfig.mRecordHWEncodeBPS = 4194304;
        vECloudConfig.mRecordEncodeMode = 0;
        vECloudConfig.mRecordHwBitrateMode = 0;
        vECloudConfig.mRecordHwProfile = 0;
        vECloudConfig.mRecordResolutionWidth = 576;
        vECloudConfig.mRecordResolutionHeight = 1024;
        vECloudConfig.mImportSWEncodeCRF = 15;
        vECloudConfig.mImportVideoSWMaxrate = 15000000;
        vECloudConfig.mImportVideoSWPreset = 0;
        vECloudConfig.mImportVideoSWGop = 35;
        vECloudConfig.mImportVideoSWQP = 2;
        vECloudConfig.mImportSWBitrateMode = 0;
        vECloudConfig.mImportEncodeMode = 0;
        vECloudConfig.mImportHWEncodeBPS = 4194304;
        vECloudConfig.mImportHwBitrateMode = 0;
        vECloudConfig.mImportHwProfile = 0;
        vECloudConfig.mImportShortEdgeValue = 576;
        vECloudConfig.mCompileEncodeSWCRF = 15;
        vECloudConfig.mCompileEncodeSWMaxrate = 15000000;
        vECloudConfig.mCompileEncodeSWCRFPreset = 0;
        vECloudConfig.mCompileEncodeSWGOP = 35;
        vECloudConfig.mCompileVideoSWQP = 2;
        vECloudConfig.mCompileSWBitrateMode = 1;
        vECloudConfig.mCompileEncodeMode = 0;
        vECloudConfig.mCompileEncodeHWBPS = 4194304;
        vECloudConfig.mCompileHwBitrateMode = 0;
        vECloudConfig.mCompileHwProfile = 0;
        vECloudConfig.mEarphoneEchoNormal = 1;
        vECloudConfig.mEarphoneEchoAAudio = 1;
        vECloudConfig.mEarphoneEchoHuawei = 1;
    }

    public void inject(Map map, @NonNull VECloudConfig vECloudConfig) {
        fillWithDefaultValue(vECloudConfig);
        if (map == null) {
            Log.w(TAG, "Inject source map is null. Everything will be overridden with default value in CloudConfig!!!");
        }
        String str = "record_camera_type";
        if (map.containsKey(str)) {
            vECloudConfig.mRecordCameraType = Integer.parseInt((String) map.get(str));
        }
        String str2 = "record_camera_compat_level";
        if (map.containsKey(str2)) {
            vECloudConfig.mRecordCameraCompatLevel = Integer.parseInt((String) map.get(str2));
        }
        String str3 = "record_video_sw_crf";
        if (map.containsKey(str3)) {
            vECloudConfig.mRecordSWEncodeCRF = Integer.parseInt((String) map.get(str3));
        }
        String str4 = "record_video_sw_maxrate";
        if (map.containsKey(str4)) {
            vECloudConfig.mRecordVideoSWMaxrate = Integer.parseInt((String) map.get(str4));
        }
        String str5 = "record_video_sw_preset";
        if (map.containsKey(str5)) {
            vECloudConfig.mRecordVideoSWPreset = Integer.parseInt((String) map.get(str5));
        }
        String str6 = "record_video_sw_gop";
        if (map.containsKey(str6)) {
            vECloudConfig.mRecordVideoSWGop = Integer.parseInt((String) map.get(str6));
        }
        String str7 = "record_video_sw_qp";
        if (map.containsKey(str7)) {
            vECloudConfig.mRecordVideoSWQP = Integer.parseInt((String) map.get(str7));
        }
        String str8 = "record_sw_bitrate_mode";
        if (map.containsKey(str8)) {
            vECloudConfig.mRecordSWBitrateMode = Integer.parseInt((String) map.get(str8));
        }
        String str9 = "record_video_hw_bitrate";
        if (map.containsKey(str9)) {
            vECloudConfig.mRecordHWEncodeBPS = Integer.parseInt((String) map.get(str9));
        }
        String str10 = "record_encode_mode";
        if (map.containsKey(str10)) {
            vECloudConfig.mRecordEncodeMode = Integer.parseInt((String) map.get(str10));
        }
        String str11 = "record_hw_bitrate_mode";
        if (map.containsKey(str11)) {
            vECloudConfig.mRecordHwBitrateMode = Integer.parseInt((String) map.get(str11));
        }
        String str12 = "record_hw_profile";
        if (map.containsKey(str12)) {
            vECloudConfig.mRecordHwProfile = Integer.parseInt((String) map.get(str12));
        }
        String str13 = "record_resolution_width";
        if (map.containsKey(str13)) {
            vECloudConfig.mRecordResolutionWidth = Integer.parseInt((String) map.get(str13));
        }
        String str14 = "record_resolution_height";
        if (map.containsKey(str14)) {
            vECloudConfig.mRecordResolutionHeight = Integer.parseInt((String) map.get(str14));
        }
        String str15 = "import_video_sw_crf";
        if (map.containsKey(str15)) {
            vECloudConfig.mImportSWEncodeCRF = Integer.parseInt((String) map.get(str15));
        }
        if (map.containsKey("import_video_sw_maxrate")) {
            vECloudConfig.mImportVideoSWMaxrate = Integer.parseInt((String) map.get("import_video_sw_maxrate"));
        }
        if (map.containsKey("import_video_sw_preset")) {
            vECloudConfig.mImportVideoSWPreset = Integer.parseInt((String) map.get("import_video_sw_preset"));
        }
        if (map.containsKey("import_video_sw_gop")) {
            vECloudConfig.mImportVideoSWGop = Integer.parseInt((String) map.get("import_video_sw_gop"));
        }
        if (map.containsKey("import_video_sw_qp")) {
            vECloudConfig.mImportVideoSWQP = Integer.parseInt((String) map.get("import_video_sw_qp"));
        }
        if (map.containsKey("import_sw_bitrate_mode")) {
            vECloudConfig.mImportSWBitrateMode = Integer.parseInt((String) map.get("import_sw_bitrate_mode"));
        }
        if (map.containsKey("import_encode_mode")) {
            vECloudConfig.mImportEncodeMode = Integer.parseInt((String) map.get("import_encode_mode"));
        }
        if (map.containsKey("import_video_hw_bitrate")) {
            vECloudConfig.mImportHWEncodeBPS = Integer.parseInt((String) map.get("import_video_hw_bitrate"));
        }
        if (map.containsKey("import_hw_bitrate_mode")) {
            vECloudConfig.mImportHwBitrateMode = Integer.parseInt((String) map.get("import_hw_bitrate_mode"));
        }
        if (map.containsKey("import_hw_profile")) {
            vECloudConfig.mImportHwProfile = Integer.parseInt((String) map.get("import_hw_profile"));
        }
        if (map.containsKey("import_shorter_pixels")) {
            vECloudConfig.mImportShortEdgeValue = Integer.parseInt((String) map.get("import_shorter_pixels"));
        }
        if (map.containsKey("synthetic_video_sw_crf")) {
            vECloudConfig.mCompileEncodeSWCRF = Integer.parseInt((String) map.get("synthetic_video_sw_crf"));
        }
        if (map.containsKey("synthetic_video_sw_maxrate")) {
            vECloudConfig.mCompileEncodeSWMaxrate = Integer.parseInt((String) map.get("synthetic_video_sw_maxrate"));
        }
        if (map.containsKey("synthetic_video_sw_preset")) {
            vECloudConfig.mCompileEncodeSWCRFPreset = Integer.parseInt((String) map.get("synthetic_video_sw_preset"));
        }
        if (map.containsKey("synthetic_video_sw_gop")) {
            vECloudConfig.mCompileEncodeSWGOP = Integer.parseInt((String) map.get("synthetic_video_sw_gop"));
        }
        if (map.containsKey("synthetic_video_sw_qp")) {
            vECloudConfig.mCompileVideoSWQP = Integer.parseInt((String) map.get("synthetic_video_sw_qp"));
        }
        if (map.containsKey("synthetic_sw_bitrate_mode")) {
            vECloudConfig.mCompileSWBitrateMode = Integer.parseInt((String) map.get("synthetic_sw_bitrate_mode"));
        }
        if (map.containsKey("synthetic_encode_mode")) {
            vECloudConfig.mCompileEncodeMode = Integer.parseInt((String) map.get("synthetic_encode_mode"));
        }
        if (map.containsKey("synthetic_video_hw_bitrate")) {
            vECloudConfig.mCompileEncodeHWBPS = Integer.parseInt((String) map.get("synthetic_video_hw_bitrate"));
        }
        if (map.containsKey("synthetic_hw_bitrate_mode")) {
            vECloudConfig.mCompileHwBitrateMode = Integer.parseInt((String) map.get("synthetic_hw_bitrate_mode"));
        }
        if (map.containsKey("synthetic_hw_profile")) {
            vECloudConfig.mCompileHwProfile = Integer.parseInt((String) map.get("synthetic_hw_profile"));
        }
        if (map.containsKey("earphone_echo_normal")) {
            vECloudConfig.mEarphoneEchoNormal = Integer.parseInt((String) map.get("earphone_echo_normal"));
        }
        if (map.containsKey("earphone_echo_aaudio")) {
            vECloudConfig.mEarphoneEchoAAudio = Integer.parseInt((String) map.get("earphone_echo_aaudio"));
        }
        if (map.containsKey("earphone_echo_huawei")) {
            vECloudConfig.mEarphoneEchoHuawei = Integer.parseInt((String) map.get("earphone_echo_huawei"));
        }
    }

    public Map parse(@NonNull JSONObject jSONObject) {
        String str = TAG;
        String str2 = "code";
        try {
            if (jSONObject.getInt(str2) == 0) {
                return createParamMap(jSONObject.getJSONObject(PhotosOemApi.PATH_SPECIAL_TYPE_DATA));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Fetched Config return code is not 0 but ");
            sb.append(jSONObject.getInt(str2));
            Log.e(str, sb.toString());
            return null;
        } catch (JSONException e) {
            Log.e(str, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
