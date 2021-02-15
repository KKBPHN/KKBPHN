package com.ss.android.ugc.effectmanager.common.task;

import android.accounts.NetworkErrorException;
import com.ss.android.ugc.effectmanager.common.ErrorConstants;
import com.ss.android.ugc.effectmanager.common.exception.MD5Exception;
import com.ss.android.ugc.effectmanager.common.exception.StatusCodeException;
import com.ss.android.ugc.effectmanager.common.exception.UnzipException;
import com.ss.android.ugc.effectmanager.common.exception.UrlNotExistException;
import com.ss.android.ugc.effectmanager.common.model.NetException;
import java.io.IOException;
import org.json.JSONException;

public class ExceptionResult {
    private int errorCode;
    private Exception exception;
    private String msg;
    private String remoteIp;
    private String requestUrl;
    private String selectedHost;

    public ExceptionResult(int i) {
        this.errorCode = -1;
        this.errorCode = i;
        this.msg = ErrorConstants.APIErrorHandle(i);
        this.exception = null;
    }

    public ExceptionResult(int i, Exception exc) {
        this.errorCode = -1;
        this.errorCode = i;
        this.msg = ErrorConstants.APIErrorHandle(i);
        this.exception = exc;
    }

    public ExceptionResult(Exception exc) {
        this(exc, null, null, null);
    }

    public ExceptionResult(Exception exc, String str, String str2, String str3) {
        String APIErrorHandle;
        int i;
        this.errorCode = -1;
        this.requestUrl = str;
        this.selectedHost = str2;
        this.remoteIp = str3;
        this.exception = exc;
        if (exc instanceof NetException) {
            i = ((NetException) exc).getStatus_code().intValue();
        } else if (exc instanceof StatusCodeException) {
            i = ((StatusCodeException) exc).getStatusCode();
        } else if (exc instanceof JSONException) {
            i = 10008;
        } else if (exc instanceof NetworkErrorException) {
            i = 10002;
        } else if (exc instanceof UrlNotExistException) {
            i = ErrorConstants.CODE_URL_NOT_EXIST;
        } else if (exc instanceof UnzipException) {
            i = ErrorConstants.CODE_UNZIP_FAIL;
        } else if (exc instanceof MD5Exception) {
            i = ErrorConstants.CODE_MD5_ERROR;
        } else if (exc instanceof IOException) {
            i = ErrorConstants.CODE_IO_FAIL;
        } else if (exc != null) {
            i = ErrorConstants.EXCEPTION_NO_NETWORK.equals(exc.getMessage()) ? ErrorConstants.CODE_NO_NETWORK : 10005;
        } else {
            this.errorCode = 1;
            APIErrorHandle = ErrorConstants.APIErrorHandle(this.errorCode);
            this.msg = APIErrorHandle;
        }
        this.errorCode = i;
        APIErrorHandle = exc.getMessage();
        this.msg = APIErrorHandle;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public Exception getException() {
        return this.exception;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    public void setException(Exception exc) {
        this.exception = exc;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setTrackParams(String str, String str2, String str3) {
        this.requestUrl = str;
        this.selectedHost = str2;
        this.remoteIp = str3;
    }

    public String toString() {
        StringBuilder sb;
        String str = ", remoteIp='";
        String str2 = ", selectedHost='";
        String str3 = ", requestUrl='";
        String str4 = ", msg='";
        String str5 = "ExceptionResult{errorCode=";
        if (this.exception != null) {
            sb = new StringBuilder();
            sb.append(str5);
            sb.append(this.errorCode);
            sb.append(str4);
            sb.append(this.msg);
            sb.append('\'');
            sb.append(str3);
            sb.append(this.requestUrl);
            sb.append('\'');
            sb.append(str2);
            sb.append(this.selectedHost);
            sb.append('\'');
            sb.append(str);
            sb.append(this.remoteIp);
            sb.append('\'');
            sb.append(", exception=");
            sb.append(this.exception.getMessage());
        } else {
            sb = new StringBuilder();
            sb.append(str5);
            sb.append(this.errorCode);
            sb.append(str4);
            sb.append(this.msg);
            sb.append(str3);
            sb.append(this.requestUrl);
            sb.append('\'');
            sb.append(str2);
            sb.append(this.selectedHost);
            sb.append('\'');
            sb.append(str);
            sb.append(this.remoteIp);
            sb.append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
