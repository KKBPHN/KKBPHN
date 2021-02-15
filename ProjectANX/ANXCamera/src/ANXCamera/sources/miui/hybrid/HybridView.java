package miui.hybrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.hybrid.HybridManager;
import com.miui.internal.hybrid.HybridProgressView;
import com.miui.internal.hybrid.WebContainerView;
import com.miui.internal.hybrid.provider.AbsWebView;
import com.miui.internal.hybrid.provider.WebViewFactory;
import com.miui.internal.hybrid.provider.WebViewFactoryProvider;
import miui.widget.ProgressBar;

public class HybridView extends FrameLayout {
    private static final int PROGRESS_BAR_CIRCULAR = 1;
    private static final int PROGRESS_BAR_HORIZONTAL = 2;
    private static final int PROGRESS_BAR_NONE = 0;
    private Button mBtnRetry;
    private WebViewFactoryProvider mFactory;
    private HybridProgressView mHorizontalProgressView;
    private boolean mLoadingError;
    private HybridManager mManager;
    private int mProgressBarStyle;
    private ProgressBar mProgressView;
    private boolean mPullable;
    private ViewGroup mReloadView;
    private boolean mShowErrorPage;
    private TextView mTextProvider;
    private WebContainerView mWebContainer = ((WebContainerView) findViewById(R.id.webContainer));
    private HybridSettings mWebSettings;
    private AbsWebView mWebView;

    public HybridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, miui.R.styleable.HybridViewStyle, 0, 0);
        this.mProgressBarStyle = obtainStyledAttributes.getInt(miui.R.styleable.HybridViewStyle_hybridProgressBar, 0);
        this.mShowErrorPage = obtainStyledAttributes.getBoolean(miui.R.styleable.HybridViewStyle_hybridErrorPage, true);
        this.mPullable = obtainStyledAttributes.getBoolean(miui.R.styleable.HybridViewStyle_hybridPullable, true);
        obtainStyledAttributes.recycle();
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.hybrid_view_layout, this, true);
        this.mFactory = WebViewFactory.getProvider(context);
        this.mWebView = this.mFactory.createWebView(context, this);
        this.mWebContainer.setWebView(this.mWebView.getBaseWebView());
        int i = this.mProgressBarStyle;
        if (i == 1) {
            this.mProgressView = (ProgressBar) findViewById(R.id.progress_circular);
        } else if (i == 2) {
            this.mHorizontalProgressView = (HybridProgressView) findViewById(R.id.progress_horizontal);
        }
        this.mTextProvider = (TextView) findViewById(R.id.hybrid_provider);
        if (this.mPullable) {
            this.mTextProvider.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void setReloadContentVisibility(int i) {
        int childCount = this.mReloadView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            this.mReloadView.getChildAt(i2).setVisibility(i);
        }
    }

    public void addJavascriptInterface(Object obj, String str) {
        this.mWebView.addJavascriptInterface(obj, str);
    }

    public boolean canGoBack() {
        return this.mWebView.canGoBack();
    }

    public void clearCache(boolean z) {
        this.mWebView.clearCache(z);
    }

    public HybridBackForwardList copyBackForwardList() {
        return this.mWebView.copyBackForwardList();
    }

    public void destroy() {
        this.mManager.clear();
        this.mWebView.destroy();
    }

    public void drawWebView(Canvas canvas) {
        this.mWebView.draw(canvas);
    }

    public int getContentHeight() {
        return this.mWebView.getContentHeight();
    }

    /* access modifiers changed from: 0000 */
    public HybridManager getHybridManager() {
        return this.mManager;
    }

    public float getScale() {
        return this.mWebView.getScale();
    }

    public HybridSettings getSettings() {
        if (this.mWebSettings == null) {
            this.mWebSettings = this.mWebView.getSettings();
        }
        return this.mWebSettings;
    }

    public String getTitle() {
        return this.mWebView.getTitle();
    }

    public String getUrl() {
        return this.mWebView.getUrl();
    }

    /* access modifiers changed from: 0000 */
    public AbsWebView getWebView() {
        return this.mWebView;
    }

    public void goBack() {
        this.mWebView.goBack();
    }

    /* access modifiers changed from: 0000 */
    public void hideReloadView() {
        if (this.mShowErrorPage) {
            ViewGroup viewGroup = this.mReloadView;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
            }
            this.mWebView.setVisibility(0);
        }
    }

    public void loadUrl(String str) {
        this.mWebView.loadUrl(str);
    }

    public void reload() {
        this.mWebView.reload();
    }

    public void setHybridChromeClient(HybridChromeClient hybridChromeClient) {
        hybridChromeClient.setHybridManager(this.mManager);
        this.mWebView.setWebChromeClient(this.mFactory.createWebChromeClient(hybridChromeClient, this));
    }

    /* access modifiers changed from: 0000 */
    public void setHybridManager(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    public void setHybridViewClient(HybridViewClient hybridViewClient) {
        hybridViewClient.setHybridManager(this.mManager);
        this.mWebView.setWebViewClient(this.mFactory.createWebViewClient(hybridViewClient, this));
    }

    /* access modifiers changed from: 0000 */
    public void setLoadingError(boolean z) {
        this.mLoadingError = z;
    }

    /* access modifiers changed from: 0000 */
    public void setProgress(int i) {
        if (i > 80 && !this.mLoadingError) {
            hideReloadView();
        }
        if (i == 100) {
            this.mWebContainer.setBackground(null);
        }
        if (this.mProgressView != null || this.mHorizontalProgressView != null) {
            int i2 = this.mProgressBarStyle;
            if (i2 == 1) {
                ProgressBar progressBar = this.mProgressView;
                if (progressBar != null) {
                    if (progressBar.getVisibility() == 8) {
                        this.mProgressView.setVisibility(0);
                    }
                    this.mProgressView.setProgress(i);
                    if (i == this.mProgressView.getMax()) {
                        this.mProgressView.setVisibility(8);
                    }
                }
            } else if (i2 == 2) {
                HybridProgressView hybridProgressView = this.mHorizontalProgressView;
                if (hybridProgressView != null) {
                    if (hybridProgressView.getVisibility() == 8) {
                        this.mHorizontalProgressView.setVisibility(0);
                    }
                    this.mHorizontalProgressView.setProgress(i);
                    if (i == this.mHorizontalProgressView.getMax()) {
                        this.mHorizontalProgressView.setVisibility(8);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setWebProvider(String str) {
        String host = Uri.parse(str).getHost();
        if (TextUtils.isEmpty(host)) {
            this.mTextProvider.setText("");
            return;
        }
        this.mTextProvider.setText(String.format(getContext().getString(R.string.hybrid_provider), new Object[]{host}));
    }

    /* access modifiers changed from: 0000 */
    public void showReloadView() {
        if (this.mShowErrorPage) {
            if (this.mReloadView == null) {
                this.mReloadView = (ViewGroup) ((ViewStub) findViewById(R.id.webview_reload_stub)).inflate();
                this.mBtnRetry = (Button) this.mReloadView.findViewById(R.id.reload);
                this.mBtnRetry.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        HybridView.this.reload();
                        HybridView.this.setReloadContentVisibility(8);
                    }
                });
            }
            this.mReloadView.setVisibility(0);
            setReloadContentVisibility(0);
            this.mWebView.setVisibility(8);
        }
    }
}
