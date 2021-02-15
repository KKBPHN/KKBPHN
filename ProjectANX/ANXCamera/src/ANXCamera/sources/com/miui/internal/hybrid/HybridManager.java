package com.miui.internal.hybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import miui.hybrid.Callback;
import miui.hybrid.HybridChromeClient;
import miui.hybrid.HybridFeature;
import miui.hybrid.HybridFeature.Mode;
import miui.hybrid.HybridSettings;
import miui.hybrid.HybridView;
import miui.hybrid.HybridViewClient;
import miui.hybrid.LifecycleListener;
import miui.hybrid.NativeInterface;
import miui.hybrid.PageContext;
import miui.hybrid.Request;
import miui.hybrid.Response;

public class HybridManager {
    public static final String TAG = "hybrid";
    private static ExecutorService sPool = Executors.newCachedThreadPool();
    private static String sUserAgent;
    private Activity mActivity;
    /* access modifiers changed from: private */
    public boolean mDetached;
    private FeatureManager mFM;
    private Set mListeners = new CopyOnWriteArraySet();
    private NativeInterface mNativeInterface;
    private PermissionManager mPM;
    private PageContext mPageContext;
    /* access modifiers changed from: private */
    public ConcurrentHashMap mReqMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public HybridView mView;

    class AsyncInvocation implements Runnable {
        private HybridFeature mFeature;
        private WeakReference mHybridManager;
        private String mJsCallback;
        private String mRequestKey;

        public AsyncInvocation(HybridManager hybridManager, HybridFeature hybridFeature, String str, String str2) {
            this.mHybridManager = new WeakReference(hybridManager);
            this.mFeature = hybridFeature;
            this.mRequestKey = str;
            this.mJsCallback = str2;
        }

        public void run() {
            HybridManager hybridManager = (HybridManager) this.mHybridManager.get();
            if (hybridManager != null) {
                Request request = (Request) hybridManager.mReqMap.remove(this.mRequestKey);
                if (request != null && !hybridManager.getActivity().isFinishing() && !hybridManager.getActivity().isDestroyed()) {
                    Response invoke = this.mFeature.invoke(request);
                    if (this.mFeature.getInvocationMode(request) == Mode.ASYNC) {
                        hybridManager.callback(invoke, request.getPageContext(), this.mJsCallback);
                    }
                }
            }
        }
    }

    class JsInvocation implements Runnable {
        private String mJsCallback;
        private Response mResponse;

        public JsInvocation(Response response, String str) {
            this.mResponse = response;
            this.mJsCallback = str;
        }

        public void run() {
            String access$200 = HybridManager.this.buildCallbackJavascript(this.mResponse, this.mJsCallback);
            HybridView access$300 = HybridManager.this.mView;
            StringBuilder sb = new StringBuilder();
            sb.append("javascript:");
            sb.append(access$200);
            access$300.loadUrl(sb.toString());
        }
    }

    public HybridManager(Activity activity, HybridView hybridView) {
        this.mActivity = activity;
        this.mView = hybridView;
    }

    /* access modifiers changed from: private */
    public String buildCallbackJavascript(Response response, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("('");
        sb.append(response.toString().replace("\\", "\\\\").replace("'", "\\'"));
        sb.append("');");
        return sb.toString();
    }

    private HybridFeature buildFeature(String str) {
        if (this.mPM.isValid(this.mPageContext.getUrl())) {
            return this.mFM.lookupFeature(str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("feature not permitted: ");
        sb.append(str);
        throw new HybridException(203, sb.toString());
    }

    private Request buildRequest(String str, String str2, String str3) {
        Request request = new Request();
        request.setAction(str2);
        request.setRawParams(str3);
        request.setPageContext(this.mPageContext);
        request.setView(this.mView);
        request.setNativeInterface(this.mNativeInterface);
        return request;
    }

    private String config(Config config, boolean z) {
        Response response;
        if (z) {
            SecurityManager securityManager = new SecurityManager(config, this.mActivity.getApplicationContext());
            if (securityManager.isExpired() || !securityManager.isValidSignature()) {
                response = new Response(202);
                return response.toString();
            }
        }
        this.mFM = new FeatureManager(config, this.mActivity.getClassLoader());
        this.mPM = new PermissionManager(config);
        response = new Response(0);
        return response.toString();
    }

    private String getKey(String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        return sb.toString();
    }

    private static PackageInfo getPackageInfo(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 128);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getUserAgent(String str) {
        if (sUserAgent == null) {
            StringBuilder sb = new StringBuilder(str);
            sb.append(" XiaoMi/HybridView/");
            sb.append(getPackageInfo(this.mActivity, "com.miui.core").versionName);
            sb.append(" ");
            sb.append(this.mActivity.getPackageName());
            sb.append("/");
            Activity activity = this.mActivity;
            sb.append(getPackageInfo(activity, activity.getPackageName()).versionName);
            sUserAgent = sb.toString();
        }
        return sUserAgent;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initSettings(HybridSettings hybridSettings) {
        hybridSettings.setJavaScriptEnabled(true);
        hybridSettings.setUserAgentString(getUserAgent(hybridSettings.getUserAgentString()));
    }

    private void initView() {
        initSettings(this.mView.getSettings());
        this.mView.setHybridViewClient(new HybridViewClient());
        this.mView.setHybridChromeClient(new HybridChromeClient());
        this.mView.addJavascriptInterface(new JsInterface(this), JsInterface.INTERFACE_NAME);
        this.mView.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                HybridManager.this.mDetached = false;
            }

            public void onViewDetachedFromWindow(View view) {
                HybridManager.this.mDetached = true;
            }
        });
    }

    private Config loadConfig(int i) {
        XmlConfigParser xmlConfigParser;
        if (i == 0) {
            try {
                xmlConfigParser = XmlConfigParser.create(this.mActivity);
            } catch (HybridException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("cannot load config: ");
                sb.append(e.getMessage());
                throw new RuntimeException(sb.toString());
            }
        } else {
            xmlConfigParser = XmlConfigParser.createFromResId(this.mActivity, i);
        }
        return xmlConfigParser.parse(null);
    }

    private String resolveUri(String str) {
        if (Pattern.compile("^[a-z-]+://").matcher(str).find()) {
            return str;
        }
        if (str.charAt(0) == '/') {
            str = str.substring(1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("file:///android_asset/hybrid/");
        sb.append(str);
        return sb.toString();
    }

    public void addLifecycleListener(LifecycleListener lifecycleListener) {
        this.mListeners.add(lifecycleListener);
    }

    public void callback(Response response, PageContext pageContext, String str) {
        if (response != null && !TextUtils.isEmpty(str) && pageContext.equals(this.mPageContext) && !this.mDetached && !this.mActivity.isFinishing()) {
            String str2 = TAG;
            if (Log.isLoggable(str2, 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("non-blocking response is ");
                sb.append(response.toString());
                Log.d(str2, sb.toString());
            }
            this.mActivity.runOnUiThread(new JsInvocation(response, str));
        }
    }

    public void clear() {
        this.mReqMap.clear();
    }

    public String config(String str) {
        try {
            return config(JsonConfigParser.createFromString(str).parse(null), true);
        } catch (HybridException e) {
            return new Response(201, e.getMessage()).toString();
        }
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public HybridView getHybridView() {
        return this.mView;
    }

    public void init(int i, String str) {
        this.mNativeInterface = new NativeInterface(this);
        Config loadConfig = loadConfig(i);
        config(loadConfig, false);
        initView();
        if (str == null && !TextUtils.isEmpty(loadConfig.getContent())) {
            str = resolveUri(loadConfig.getContent());
        }
        if (str != null) {
            this.mView.loadUrl(str);
        }
    }

    public String invoke(String str, String str2, String str3, String str4) {
        try {
            HybridFeature buildFeature = buildFeature(str);
            Request buildRequest = buildRequest(str, str2, str3);
            Mode invocationMode = buildFeature.getInvocationMode(buildRequest);
            if (invocationMode == Mode.SYNC) {
                callback(new Response(1), this.mPageContext, str4);
                return buildFeature.invoke(buildRequest).toString();
            }
            String key = getKey(str, str2, str3, str4);
            this.mReqMap.put(key, buildRequest);
            if (invocationMode == Mode.ASYNC) {
                sPool.execute(new AsyncInvocation(this, buildFeature, key, str4));
                return new Response(2).toString();
            }
            buildRequest.setCallback(new Callback(this, this.mPageContext, str4));
            sPool.execute(new AsyncInvocation(this, buildFeature, key, str4));
            return new Response(3).toString();
        } catch (HybridException e) {
            Response response = e.getResponse();
            callback(response, this.mPageContext, str4);
            return response.toString();
        }
    }

    public boolean isDetached() {
        return this.mDetached;
    }

    public String lookup(String str, String str2) {
        Response response;
        try {
            if (buildFeature(str).getInvocationMode(buildRequest(str, str2, null)) == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("action not supported: ");
                sb.append(str2);
                response = new Response(205, sb.toString());
            } else {
                response = new Response(0);
            }
        } catch (HybridException e) {
            response = e.getResponse();
        }
        return response.toString();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        for (LifecycleListener onActivityResult : this.mListeners) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public void onDestroy() {
        for (LifecycleListener onDestroy : this.mListeners) {
            onDestroy.onDestroy();
        }
    }

    public void onPageChange() {
        for (LifecycleListener onPageChange : this.mListeners) {
            onPageChange.onPageChange();
        }
    }

    public void onPause() {
        for (LifecycleListener onPause : this.mListeners) {
            onPause.onPause();
        }
    }

    public void onResume() {
        for (LifecycleListener onResume : this.mListeners) {
            onResume.onResume();
        }
    }

    public void onStart() {
        for (LifecycleListener onStart : this.mListeners) {
            onStart.onStart();
        }
    }

    public void onStop() {
        for (LifecycleListener onStop : this.mListeners) {
            onStop.onStop();
        }
    }

    public void removeLifecycleListener(LifecycleListener lifecycleListener) {
        this.mListeners.remove(lifecycleListener);
    }

    public void setPageContext(PageContext pageContext) {
        this.mPageContext = pageContext;
    }
}
