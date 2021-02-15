package com.ss.android.ugc.effectmanager.link.task.task;

import android.os.Handler;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.ss.android.ugc.effectmanager.common.EffectConstants;
import com.ss.android.ugc.effectmanager.common.task.NormalTask;
import com.ss.android.ugc.effectmanager.common.utils.LogUtils;
import com.ss.android.ugc.effectmanager.link.LinkSelector;
import com.ss.android.ugc.effectmanager.link.model.host.Host;
import com.ss.android.ugc.effectmanager.link.model.host.HostStatus;
import com.ss.android.ugc.effectmanager.link.task.result.HostListStatusUpdateTaskResult;
import com.ss.android.ugc.effectmanager.link.task.result.HostStatusUpdateResult;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HostListStatusUpdateTask extends NormalTask {
    private static final long MAX_SORT_TIME = 2147483647L;
    private static final String TAG = "HostListStatusUpdateTask";
    private List mHosts = new ArrayList();
    private LinkSelector mLinkSelector;
    private String mSpeedApi;

    public HostListStatusUpdateTask(LinkSelector linkSelector, Handler handler, String str) {
        super(handler, str, EffectConstants.NORMAL);
        this.mHosts.clear();
        this.mHosts.addAll(linkSelector.getOriginHosts());
        this.mSpeedApi = linkSelector.getSpeedApi();
        this.mLinkSelector = linkSelector;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0140, code lost:
        if (r22 != null) goto L_0x01ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0154, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0155, code lost:
        r20 = r2;
        r22 = r7;
        r10 = r0;
        r9 = null;
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0160, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0161, code lost:
        r22 = r7;
        r10 = r0;
        r5 = -1;
        r9 = null;
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0169, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x016a, code lost:
        r22 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x016d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x016e, code lost:
        r22 = r7;
        r10 = r0;
        r5 = -1;
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01aa, code lost:
        if (r22 == null) goto L_0x01af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x01ac, code lost:
        r22.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01af, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x01b3, code lost:
        r22.disconnect();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0169 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getHostStatus(Host host, long j) {
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2;
        Exception exc;
        String str;
        long j2;
        int i;
        int responseCode;
        long currentTimeMillis;
        long j3;
        String headerField;
        int i2;
        StringBuilder sb;
        int i3;
        long j4;
        Host host2 = host;
        String str2 = " ";
        String str3 = TAG;
        if (host2 != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(host.getSchema());
            String str4 = "://";
            sb2.append(str4);
            sb2.append(host.getHost());
            sb2.append(this.mSpeedApi);
            sb2.append(System.currentTimeMillis());
            long currentTimeMillis2 = System.currentTimeMillis();
            try {
                URL url = new URL(sb2.toString());
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) url.openConnection();
                try {
                    httpURLConnection3.setConnectTimeout(this.mLinkSelector.getSpeedTimeOut());
                    httpURLConnection3.setReadTimeout(this.mLinkSelector.getSpeedTimeOut());
                    httpURLConnection3.setRequestProperty("X-SS-No-Cookie", BaseEvent.VALUE_TRUE);
                    responseCode = httpURLConnection3.getResponseCode();
                    currentTimeMillis = System.currentTimeMillis() - currentTimeMillis2;
                    headerField = httpURLConnection3.getHeaderField("X-TT-LOGID");
                    if (responseCode == 200) {
                        host2.setSortTime(currentTimeMillis + j);
                        host.resetStatus();
                        String url2 = url.toString();
                        long j5 = currentTimeMillis;
                        long j6 = j5;
                        httpURLConnection2 = httpURLConnection3;
                        i3 = responseCode;
                        String str5 = str4;
                        try {
                            sendEvent(url2, host, responseCode, j5, currentTimeMillis2, headerField, null, true);
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("sort speed time = ");
                            j4 = j6;
                            try {
                                sb3.append(j4);
                                sb3.append(str2);
                                sb3.append(host.getSchema());
                                sb3.append(str5);
                                sb3.append(host.getHost());
                                LogUtils.d(str3, sb3.toString());
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("sort weight time = ");
                                sb4.append(host.getWeightTime());
                                sb4.append(str2);
                                sb4.append(host.getSchema());
                                sb4.append(str5);
                                sb4.append(host.getHost());
                                LogUtils.d(str3, sb4.toString());
                            } catch (Exception e) {
                                e = e;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            j4 = j6;
                            j2 = j4;
                            str = headerField;
                            i = i3;
                            exc = e;
                            try {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("sort speed error = ");
                                sb5.append(exc);
                                LogUtils.e(str3, sb5.toString());
                                host2.setSortTime(MAX_SORT_TIME);
                                exc.printStackTrace();
                                sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
                            } catch (Throwable th) {
                                th = th;
                                if (httpURLConnection != null) {
                                }
                                throw th;
                            }
                        }
                    } else {
                        long j7 = currentTimeMillis;
                        httpURLConnection2 = httpURLConnection3;
                        int i4 = responseCode;
                        try {
                            sb = new StringBuilder();
                            sb.append("sort speed error code = ");
                            i2 = i4;
                        } catch (Exception e3) {
                            e = e3;
                            j3 = j7;
                            i2 = i4;
                            exc = e;
                            i = i2;
                            str = headerField;
                            j2 = j3;
                            StringBuilder sb52 = new StringBuilder();
                            sb52.append("sort speed error = ");
                            sb52.append(exc);
                            LogUtils.e(str3, sb52.toString());
                            host2.setSortTime(MAX_SORT_TIME);
                            exc.printStackTrace();
                            sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
                        }
                        try {
                            sb.append(i2);
                            LogUtils.e(str3, sb.toString());
                            host2.setSortTime(MAX_SORT_TIME);
                            j3 = j7;
                            try {
                                sendEvent(url.toString(), host, i2, j7, currentTimeMillis2, headerField, null, false);
                            } catch (Exception e4) {
                                e = e4;
                                exc = e;
                                i = i2;
                                str = headerField;
                                j2 = j3;
                                StringBuilder sb522 = new StringBuilder();
                                sb522.append("sort speed error = ");
                                sb522.append(exc);
                                LogUtils.e(str3, sb522.toString());
                                host2.setSortTime(MAX_SORT_TIME);
                                exc.printStackTrace();
                                sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
                            }
                        } catch (Exception e5) {
                            e = e5;
                            j3 = j7;
                            exc = e;
                            i = i2;
                            str = headerField;
                            j2 = j3;
                            StringBuilder sb5222 = new StringBuilder();
                            sb5222.append("sort speed error = ");
                            sb5222.append(exc);
                            LogUtils.e(str3, sb5222.toString());
                            host2.setSortTime(MAX_SORT_TIME);
                            exc.printStackTrace();
                            sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
                        }
                    }
                } catch (Exception e6) {
                    e = e6;
                    j4 = currentTimeMillis;
                    httpURLConnection2 = httpURLConnection3;
                    i3 = responseCode;
                    j2 = j4;
                    str = headerField;
                    i = i3;
                    exc = e;
                    StringBuilder sb52222 = new StringBuilder();
                    sb52222.append("sort speed error = ");
                    sb52222.append(exc);
                    LogUtils.e(str3, sb52222.toString());
                    host2.setSortTime(MAX_SORT_TIME);
                    exc.printStackTrace();
                    sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
                } catch (Throwable th2) {
                }
            } catch (Exception e7) {
                Exception exc2 = e7;
                j2 = -1;
                String str6 = null;
                HttpURLConnection httpURLConnection4 = null;
                i = -1;
                StringBuilder sb522222 = new StringBuilder();
                sb522222.append("sort speed error = ");
                sb522222.append(exc);
                LogUtils.e(str3, sb522222.toString());
                host2.setSortTime(MAX_SORT_TIME);
                exc.printStackTrace();
                sendEvent(sb2.toString(), host, i, j2, currentTimeMillis2, str, exc, false);
            } catch (Throwable th3) {
                th = th3;
                httpURLConnection = null;
                if (httpURLConnection != null) {
                }
                throw th;
            }
        }
    }

    private void sendEvent(String str, Host host, int i, long j, long j2, String str2, Exception exc, boolean z) {
        HostStatus hostStatus = new HostStatus(str, host, i, j, j2, str2, exc, z);
        sendMessage(30, new HostStatusUpdateResult(hostStatus, null));
    }

    private void sendResults() {
        sendMessage(31, new HostListStatusUpdateTaskResult(this.mHosts, null));
    }

    private void sortHost() {
        Collections.sort(this.mHosts, new Comparator() {
            public int compare(Host host, Host host2) {
                return (int) (host.getSortTime() - host2.getSortTime());
            }
        });
        ArrayList arrayList = new ArrayList(this.mHosts);
        arrayList.clear();
        arrayList.addAll(this.mHosts);
        int i = 0;
        while (true) {
            int size = this.mHosts.size();
            String str = TAG;
            if (i < size) {
                Host host = (Host) this.mHosts.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("weight sort = ");
                sb.append(host.getSortTime());
                sb.append(" ");
                sb.append(host.getSchema());
                sb.append("://");
                sb.append(host.getHost());
                sb.append(this.mSpeedApi);
                LogUtils.d(str, sb.toString());
                i++;
                for (int i2 = i; i2 < this.mHosts.size(); i2++) {
                    Host host2 = (Host) this.mHosts.get(i2);
                    if (host.getHost().equals(host2.getHost())) {
                        arrayList.remove(host2);
                    }
                }
            } else {
                this.mHosts.clear();
                this.mHosts.addAll(arrayList);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("speed distinct = ");
                sb2.append(this.mHosts.size());
                sb2.append(" thread = ");
                sb2.append(Thread.currentThread());
                LogUtils.d(str, sb2.toString());
                return;
            }
        }
    }

    private void speedMeasure() {
        for (int i = 0; i < this.mHosts.size(); i++) {
            ((Host) this.mHosts.get(i)).setSortTime(0);
            for (int i2 = 0; i2 < this.mLinkSelector.getRepeatTime(); i2++) {
                getHostStatus((Host) this.mHosts.get(i), ((Host) this.mHosts.get(i)).getSortTime());
            }
        }
    }

    public void execute() {
        speedMeasure();
        sortHost();
        sendResults();
    }
}
