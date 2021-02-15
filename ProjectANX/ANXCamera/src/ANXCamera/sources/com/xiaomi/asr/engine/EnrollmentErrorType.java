package com.xiaomi.asr.engine;

public class EnrollmentErrorType {
    public static final int ERROR_ASR_NOT_MATCH = 18;
    public static final int ERROR_FAIL_INPROXY = 11;
    public static final int ERROR_INSERT_TO_SQL_ERR = 10;
    public static final int ERROR_INVALID_BUFFER = 9;
    public static final int ERROR_NOT_REGISTERED = 6;
    public static final int ERROR_NO_RESULT = 3;
    public static final int ERROR_NULL_BUFFER = 8;
    public static final int ERROR_NULL_TASK = 2;
    public static final int ERROR_ODD_SIZE_BUFFER = 19;
    public static final int ERROR_REGISTER_FINAL_SUCCESS = 7;
    public static final int ERROR_SUCCESS = 0;
    public static final int ERROR_TOO_BADLY_SPRAYED = 17;
    public static final int ERROR_TOO_LARGE_VOLUME = 12;
    public static final int ERROR_TOO_LONG_BUFFER = 4;
    public static final int ERROR_TOO_NOISY_BACKGROUND = 14;
    public static final int ERROR_TOO_QUICKLY = 15;
    public static final int ERROR_TOO_SHORT_BUFFER = 5;
    public static final int ERROR_TOO_SLOWLY = 16;
    public static final int ERROR_TOO_SMALL_VOLUME = 13;
    public static final int ERROR_UNKNOW_ERROR = 1;

    private EnrollmentErrorType() {
    }
}
