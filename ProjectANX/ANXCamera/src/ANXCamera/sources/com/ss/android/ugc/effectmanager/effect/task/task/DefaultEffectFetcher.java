package com.ss.android.ugc.effectmanager.effect.task.task;

import android.text.TextUtils;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import com.ss.android.ugc.effectmanager.common.utils.EffectUtils;
import com.ss.android.ugc.effectmanager.common.utils.FileUtils;
import com.ss.android.ugc.effectmanager.effect.bridge.EffectFetcher;
import com.ss.android.ugc.effectmanager.effect.bridge.EffectFetcherArguments;
import com.ss.android.ugc.effectmanager.effect.model.Effect;
import com.ss.android.ugc.effectmanager.effect.sync.SyncTask;
import com.ss.android.ugc.effectmanager.effect.task.result.EffectTaskResult;
import com.ss.android.ugc.effectmanager.network.EffectNetWorkerWrapper;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class DefaultEffectFetcher implements EffectFetcher {
    /* access modifiers changed from: private */
    public EffectNetWorkerWrapper mNetworker;

    public DefaultEffectFetcher(EffectNetWorkerWrapper effectNetWorkerWrapper) {
        this.mNetworker = effectNetWorkerWrapper;
    }

    public SyncTask fetchEffect(final EffectFetcherArguments effectFetcherArguments) {
        return new SyncTask() {
            public void execute() {
                ExceptionResult exceptionResult;
                int i;
                String str;
                onStart(this);
                Effect effect = effectFetcherArguments.getEffect();
                if (effect == null || effectFetcherArguments.getDownloadUrl() == null || effectFetcherArguments.getDownloadUrl().isEmpty() || EffectUtils.isUrlModelEmpty(effect.getFileUrl())) {
                    exceptionResult = new ExceptionResult(10003);
                } else {
                    i = 0;
                    int size = effectFetcherArguments.getDownloadUrl().size();
                    while (true) {
                        if (i >= size) {
                            break;
                        } else if (isCanceled()) {
                            exceptionResult = new ExceptionResult(10001);
                            break;
                        } else {
                            String str2 = (String) effectFetcherArguments.getDownloadUrl().get(i);
                            String str3 = null;
                            try {
                                if (TextUtils.isEmpty(effect.getZipPath()) || TextUtils.isEmpty(effect.getUnzipPath())) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(effectFetcherArguments.getEffectDir());
                                    sb.append(File.separator);
                                    sb.append(effect.getId());
                                    sb.append(".zip");
                                    effect.setZipPath(sb.toString());
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(effectFetcherArguments.getEffectDir());
                                    sb2.append(File.separator);
                                    sb2.append(effect.getId());
                                    effect.setUnzipPath(sb2.toString());
                                }
                                try {
                                    str = InetAddress.getByName(new URL(str2).getHost()).getHostAddress();
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e2) {
                                    e2.printStackTrace();
                                }
                                try {
                                    EffectUtils.download(DefaultEffectFetcher.this.mNetworker, (String) effectFetcherArguments.getDownloadUrl().get(i), effect.getZipPath());
                                    FileUtils.unZip(effect.getZipPath(), effect.getUnzipPath());
                                    onResponse(this, new EffectTaskResult(effect, null));
                                    break;
                                } catch (Exception e3) {
                                    String str4 = str;
                                    e = e3;
                                    str3 = str4;
                                }
                            } catch (Exception e4) {
                                e = e4;
                                if (i == size - 1) {
                                    e.printStackTrace();
                                    ExceptionResult exceptionResult2 = new ExceptionResult(e);
                                    exceptionResult2.setTrackParams(str2, "", str3);
                                    FileUtils.removeDir(effect.getUnzipPath());
                                    onFailed(this, exceptionResult2);
                                    onFinally(this);
                                }
                                i++;
                            }
                        }
                        i++;
                    }
                }
                onFailed(this, exceptionResult);
                onFinally(this);
                str = null;
                EffectUtils.download(DefaultEffectFetcher.this.mNetworker, (String) effectFetcherArguments.getDownloadUrl().get(i), effect.getZipPath());
                FileUtils.unZip(effect.getZipPath(), effect.getUnzipPath());
                onResponse(this, new EffectTaskResult(effect, null));
                break;
                onFinally(this);
            }
        };
    }
}
