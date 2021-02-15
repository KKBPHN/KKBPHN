package miui.hybrid;

import android.view.View;

public class Request {
    private String action;
    private Callback callback;
    private NativeInterface nativeInterface;
    private PageContext pageContext;
    private String rawParams;
    private View view;

    public String getAction() {
        return this.action;
    }

    public Callback getCallback() {
        return this.callback;
    }

    public NativeInterface getNativeInterface() {
        return this.nativeInterface;
    }

    public PageContext getPageContext() {
        return this.pageContext;
    }

    public String getRawParams() {
        return this.rawParams;
    }

    public View getView() {
        return this.view;
    }

    public void setAction(String str) {
        this.action = str;
    }

    public void setCallback(Callback callback2) {
        this.callback = callback2;
    }

    public void setNativeInterface(NativeInterface nativeInterface2) {
        this.nativeInterface = nativeInterface2;
    }

    public void setPageContext(PageContext pageContext2) {
        this.pageContext = pageContext2;
    }

    public void setRawParams(String str) {
        this.rawParams = str;
    }

    public void setView(View view2) {
        this.view = view2;
    }
}
