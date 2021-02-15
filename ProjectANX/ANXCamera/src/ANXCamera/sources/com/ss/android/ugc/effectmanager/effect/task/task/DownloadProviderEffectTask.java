package com.ss.android.ugc.effectmanager.effect.task.task;

import android.accounts.NetworkErrorException;
import android.os.Handler;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.ss.android.ugc.effectmanager.EffectConfiguration;
import com.ss.android.ugc.effectmanager.common.EffectConstants;
import com.ss.android.ugc.effectmanager.common.EffectRequest;
import com.ss.android.ugc.effectmanager.common.ErrorConstants;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import com.ss.android.ugc.effectmanager.common.task.NormalTask;
import com.ss.android.ugc.effectmanager.common.utils.EffectUtils;
import com.ss.android.ugc.effectmanager.context.EffectContext;
import com.ss.android.ugc.effectmanager.effect.model.ProviderEffect;
import com.ss.android.ugc.effectmanager.effect.task.result.DownloadProviderEffectTaskResult;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class DownloadProviderEffectTask extends NormalTask {
    private EffectConfiguration mConfiguration;
    private String mDownLoadUrl;
    private ProviderEffect mEffect;
    private EffectContext mEffectContext;
    private String mRemoteIp;
    private String mRequestedUrl;
    private int mRetryCount;

    public DownloadProviderEffectTask(EffectContext effectContext, String str, @NonNull ProviderEffect providerEffect, Handler handler) {
        super(handler, str, EffectConstants.NETWORK);
        this.mEffect = providerEffect;
        this.mEffectContext = effectContext;
        this.mConfiguration = effectContext.getEffectConfiguration();
        this.mRetryCount = effectContext.getEffectConfiguration().getRetryCount();
        this.mDownLoadUrl = EffectUtils.getUrl(providerEffect);
    }

    private ProviderEffect download(String str, String str2) {
        InputStream execute = this.mEffectContext.getEffectConfiguration().getEffectNetWorker().execute(new EffectRequest("GET", str));
        if (execute != null) {
            EffectUtils.convertStreamToFile(execute, str2);
            return this.mEffect;
        }
        throw new NetworkErrorException(ErrorConstants.EXCEPTION_DOWNLOAD_ERROR);
    }

    public void cancel() {
    }

    public void execute() {
        int i = this.mRetryCount;
        int i2 = 0;
        while (i2 < i) {
            if (isCanceled()) {
                sendMessage(19, new DownloadProviderEffectTaskResult(this.mEffect, new ExceptionResult(10001)));
                return;
            }
            try {
                if (TextUtils.isEmpty(this.mEffect.getPath())) {
                    ProviderEffect providerEffect = this.mEffect;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mConfiguration.getEffectDir());
                    sb.append(File.separator);
                    sb.append(this.mEffect.getId());
                    sb.append(".gif");
                    providerEffect.setPath(sb.toString());
                }
                this.mRequestedUrl = this.mDownLoadUrl;
                try {
                    this.mRemoteIp = InetAddress.getByName(new URL(this.mRequestedUrl).getHost()).getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e2) {
                    e2.printStackTrace();
                }
                ProviderEffect download = download(this.mDownLoadUrl, this.mEffect.getPath());
                if (download != null) {
                    sendMessage(19, new DownloadProviderEffectTaskResult(download, null));
                    return;
                }
                i2++;
            } catch (Exception e3) {
                if (i2 == i - 1) {
                    e3.printStackTrace();
                    ExceptionResult exceptionResult = new ExceptionResult(e3);
                    exceptionResult.setTrackParams(this.mRequestedUrl, "", this.mRemoteIp);
                    sendMessage(19, new DownloadProviderEffectTaskResult(this.mEffect, exceptionResult));
                    return;
                }
            }
        }
    }
}
