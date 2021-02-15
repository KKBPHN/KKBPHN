package miui.hybrid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import com.miui.internal.hybrid.HybridManager;
import com.miui.internal.webkit.WebViewClientDelegate;
import java.io.IOException;
import java.util.UUID;

public class HybridViewClient {
    private static final String ASSET_PATH = "hybrid/";
    private static final String VIRTUAL_PATH = "android_asset/hybrid/";
    private HybridManager mManager;
    private WebViewClientDelegate mWebViewClientDelegate = new WebViewClientDelegate();

    public void onPageFinished(HybridView hybridView, String str) {
        if (this.mManager.getActivity().getActionBar() != null) {
            this.mManager.getActivity().getActionBar().setTitle(hybridView.getTitle());
        }
        this.mWebViewClientDelegate.onPageFinished(hybridView.getWebView(), str);
    }

    public void onPageStarted(HybridView hybridView, String str, Bitmap bitmap) {
        PageContext pageContext = new PageContext();
        pageContext.setId(UUID.randomUUID().toString());
        pageContext.setUrl(str);
        this.mManager.setPageContext(pageContext);
        this.mManager.onPageChange();
        hybridView.setWebProvider(str);
        hybridView.setLoadingError(false);
        this.mWebViewClientDelegate.onPageStarted(hybridView.getWebView(), str, bitmap);
    }

    public void onReceivedError(HybridView hybridView, int i, String str, String str2) {
        hybridView.setLoadingError(true);
        hybridView.showReloadView();
    }

    public void onReceivedLoginRequest(HybridView hybridView, String str, String str2, String str3) {
        this.mWebViewClientDelegate.onReceivedLoginRequest(hybridView.getWebView(), str, str2, str3);
    }

    public void onReceivedSslError(HybridView hybridView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.cancel();
    }

    public void setHybridManager(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    public HybridResourceResponse shouldInterceptRequest(HybridView hybridView, String str) {
        if (str == null || !str.startsWith("http")) {
            return null;
        }
        int indexOf = str.indexOf(VIRTUAL_PATH);
        if (indexOf < 0) {
            return null;
        }
        int i = indexOf + 21;
        if (i >= str.length()) {
            return null;
        }
        String substring = str.substring(i);
        try {
            AssetManager assets = this.mManager.getActivity().getAssets();
            StringBuilder sb = new StringBuilder();
            sb.append(ASSET_PATH);
            sb.append(substring);
            return new HybridResourceResponse(null, null, assets.open(sb.toString()));
        } catch (IOException unused) {
            return null;
        }
    }

    public boolean shouldOverrideUrlLoading(HybridView hybridView, String str) {
        return this.mWebViewClientDelegate.shouldOverrideUrlLoading(hybridView.getWebView(), str);
    }
}
