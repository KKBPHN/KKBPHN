package com.xiaomi.protocol;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface CameraType {
    public static final int NOT_SAT = 1;
    public static final int SAT_MACRO = 2;
    public static final int SAT_TELE = 5;
    public static final int SAT_ULTRA_WIDE = 4;
    public static final int SAT_UTLRA_TELE = 6;
    public static final int SAT_WIDE = 3;
}
