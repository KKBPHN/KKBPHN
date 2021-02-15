package com.xiaomi.camera.imagecodec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    @Retention(RetentionPolicy.SOURCE)
    public @interface FeatureType {
        public static final int AIHDR = 4;
        public static final int AINR = 2;
        public static final int MFNR = 1;
    }
}
