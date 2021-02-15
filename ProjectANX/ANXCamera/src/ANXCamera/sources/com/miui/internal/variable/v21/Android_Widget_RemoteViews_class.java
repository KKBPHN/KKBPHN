package com.miui.internal.variable.v21;

import android.content.Context;
import android.widget.RemoteViews;

public class Android_Widget_RemoteViews_class extends com.miui.internal.variable.Android_Widget_RemoteViews_class {
    public void buildProxy() {
        attachMethod("getContextForResources", "(Landroid/content/Context;)Landroid/content/Context;");
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleGetContextForResources(0, null, null);
    }

    /* access modifiers changed from: protected */
    public Context handleGetContextForResources(long j, RemoteViews remoteViews, Context context) {
        Context originalGetContextForResources = originalGetContextForResources(j, remoteViews, context);
        if (context != originalGetContextForResources) {
            if ("com.android.systemui".equals(context.getApplicationInfo().packageName)) {
                originalGetContextForResources.setTheme(com.miui.internal.variable.Android_Widget_RemoteViews_class.REMOTE_VIEWS_TEMPLATE_THEME);
            }
        }
        return originalGetContextForResources;
    }

    /* access modifiers changed from: protected */
    public Context originalGetContextForResources(long j, RemoteViews remoteViews, Context context) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_Widget_RemoteViews_class.originalGetContextForResources(long, RemoteViews, Context)");
    }
}
