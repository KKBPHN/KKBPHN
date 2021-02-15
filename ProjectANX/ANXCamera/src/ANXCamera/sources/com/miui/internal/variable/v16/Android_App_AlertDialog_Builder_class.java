package com.miui.internal.variable.v16;

import android.app.AlertDialog.Builder;
import android.content.Context;
import com.miui.internal.variable.AlertControllerWrapper.AlertParams;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_App_AlertDialog_Builder.Extension;
import com.miui.internal.variable.api.v29.Android_App_AlertDialog_Builder.Interface;
import miui.reflect.Field;

public class Android_App_AlertDialog_Builder_class extends com.miui.internal.variable.Android_App_AlertDialog_Builder_class implements Overridable {
    private static final Field P = Field.of(Builder.class, "P", "Lcom/android/internal/app/AlertController$AlertParams;");
    private Interface mImpl = new Interface() {
        public void init(Builder builder, Context context, int i) {
            Android_App_AlertDialog_Builder_class.this.handle_init_(0, builder, context, i);
        }
    };
    private Interface mOriginal;

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachConstructor("(Landroid/content/Context;I)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, Builder builder, Context context, int i) {
        if (this.mOriginal == null) {
            original_init_(j, builder, context, i);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handle_init_(0, null, null, 0);
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, Builder builder, Context context, int i) {
        callOriginal_init_(j, builder, context, i);
        try {
            P.set((Object) builder, (Object) new AlertParams(builder.getContext()));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("P", e);
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, Builder builder, Context context, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_App_AlertDialog_Builder_class.original_init_(long, AlertDialog.Builder, Context, int)");
    }
}
