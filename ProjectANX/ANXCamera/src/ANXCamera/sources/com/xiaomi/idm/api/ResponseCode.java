package com.xiaomi.idm.api;

import com.xiaomi.mi_connect_sdk.util.LogUtil;

public class ResponseCode {
    public static final int CODE_READY_FOR_IDM_REQUEST = 3;
    public static final int CODE_READY_FOR_LOCAL_REQUEST = 1;
    public static final int CODE_READY_FOR_RPC_REQUEST = 2;
    public static final int CODE_SUCCEED = 0;
    public static final int ERR_ACTION_NOT_FOUND = -7;
    public static final String ERR_ACTION_NOT_FOUND_MSG = "Action not found";
    public static final int ERR_REQUEST_NULL = -1;
    public static final String ERR_REQUEST_NULL_MSG = "Error when request bytes is null";
    public static final int ERR_REQUEST_PARSE = -2;
    public static final String ERR_REQUEST_PARSE_MSG = "Error when parse request bytes to IDMRequest";
    public static final int ERR_REQUEST_RPC = -5;
    public static final String ERR_REQUEST_RPC_MSG = "Request failed when call RPC service";
    public static final int ERR_RESPONSE_NULL = -3;
    public static final String ERR_RESPONSE_NULL_MSG = "Response bytes null when do request";
    public static final int ERR_RESPONSE_PARSE = -4;
    public static final int ERR_RESPONSE_PARSE_IN_ACTION = -8;
    public static final String ERR_RESPONSE_PARSE_IN_ACTION_MSG = "Response parse error in action";
    public static final String ERR_RESPONSE_PARSE_MSG = "Response parse error when do request";
    public static final int ERR_RMI_CANCElED = -11;
    public static final String ERR_RMI_CANCElED_MSG = "Call is canceled";
    public static final int ERR_RMI_THREAD_INTERRUPTED = -10;
    public static final String ERR_RMI_THREAD_INTERRUPTED_MSG = "Calling thread is interrupted";
    public static final int ERR_RMI_TIME_OUT = -9;
    public static final String ERR_RMI_TIME_OUT_MSG = "RMI call time out";
    public static final int ERR_SERVICE_NOT_FOUND = -6;
    public static final String ERR_SERVICE_NOT_FOUND_MSG = "Do not found service";
    private static final String TAG = "ResponseCode";

    public static final String getResponseMsg(int i) {
        String str;
        if (i >= 0) {
            return "";
        }
        switch (i) {
            case -11:
                str = ERR_RMI_CANCElED_MSG;
                break;
            case -10:
                str = ERR_RMI_THREAD_INTERRUPTED_MSG;
                break;
            case -9:
                str = ERR_RMI_TIME_OUT_MSG;
                break;
            case -8:
                str = ERR_RESPONSE_PARSE_IN_ACTION_MSG;
                break;
            case -7:
                str = ERR_ACTION_NOT_FOUND_MSG;
                break;
            case -6:
                str = ERR_SERVICE_NOT_FOUND_MSG;
                break;
            case -5:
                str = ERR_REQUEST_RPC_MSG;
                break;
            case -4:
                str = ERR_RESPONSE_PARSE_MSG;
                break;
            case -3:
                str = ERR_RESPONSE_NULL_MSG;
                break;
            case -2:
                str = ERR_REQUEST_PARSE_MSG;
                break;
            case -1:
                str = ERR_REQUEST_NULL_MSG;
                break;
            default:
                StringBuilder sb = new StringBuilder();
                String str2 = "Unknown response code:";
                sb.append(str2);
                sb.append(i);
                sb.append(" need to define here");
                LogUtil.e(TAG, sb.toString(), new Object[0]);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(i);
                str = sb2.toString();
                break;
        }
        return str;
    }
}
