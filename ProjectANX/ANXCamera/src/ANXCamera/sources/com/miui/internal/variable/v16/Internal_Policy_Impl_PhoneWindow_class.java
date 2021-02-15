package com.miui.internal.variable.v16;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.app.ActivityDelegate;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Internal_Policy_Impl_PhoneWindow.Extension;
import com.miui.internal.variable.api.v29.Internal_Policy_Impl_PhoneWindow.Interface;
import java.lang.reflect.Field;
import miui.os.Environment;
import miui.util.AttributeResolver;
import miui.util.DrawableUtil;

public class Internal_Policy_Impl_PhoneWindow_class extends com.miui.internal.variable.Internal_Policy_Impl_PhoneWindow_class implements Overridable {
    private static final Field FieldLayoutInflater = getField(com.miui.internal.variable.Internal_Policy_Impl_PhoneWindow_class.TARGET_CLASS, "mLayoutInflater");
    private Interface mImpl = new Interface() {
        public void installDecor(Object obj) {
            Internal_Policy_Impl_PhoneWindow_class.this.handleInstallDecor(-1, obj);
        }
    };
    private Interface mOriginal;

    class LayoutInflaterWrapper extends LayoutInflater {
        private static final miui.reflect.Field FieldContext = miui.reflect.Field.of(LayoutInflater.class, "mContext", "Landroid/content/Context;");
        private static final Field FieldTitle = Internal_Policy_Impl_PhoneWindow_class.getField(com.miui.internal.variable.Internal_Policy_Impl_PhoneWindow_class.TARGET_CLASS, "mTitle");
        private Window mDecorViewWindow;
        /* access modifiers changed from: private */
        public LayoutInflater mLayoutInflater;

        private LayoutInflaterWrapper(LayoutInflater layoutInflater, Window window) {
            super((Context) FieldContext.get(layoutInflater));
            this.mLayoutInflater = layoutInflater;
            this.mDecorViewWindow = window;
        }

        private int getStatusBarHeight(Context context) {
            int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : context.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        }

        private void initDefaultStartingWindow(Context context, View view) {
            CharSequence charSequence;
            int i = 0;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, miui.R.styleable.ActionBar, 16843470, 0);
            int resourceId = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_android_titleTextStyle, 0);
            boolean z = obtainStyledAttributes.getBoolean(R.styleable.ActionBar_titleCenter, false);
            int i2 = obtainStyledAttributes.getInt(miui.R.styleable.ActionBar_android_displayOptions, 0);
            obtainStyledAttributes.recycle();
            View findViewById = view.findViewById(R.id.action_bar);
            if (DrawableUtil.isPlaceholder(findViewById.getBackground())) {
                findViewById.setBackground(new ColorDrawable(AttributeResolver.resolveColor(context, miui.R.attr.colorPrimary)));
            }
            View findViewById2 = view.findViewById(R.id.title_layout);
            TextView textView = (TextView) findViewById2.findViewById(R.id.action_bar_title);
            ImageView imageView = (ImageView) findViewById.findViewById(R.id.up);
            if ((i2 & 8) == 0) {
                findViewById2.setVisibility(4);
                imageView.setVisibility(4);
                return;
            }
            LayoutParams layoutParams = (LayoutParams) findViewById2.getLayoutParams();
            if (!z || (i2 & 2) != 0) {
                layoutParams.setMarginStart(imageView.getMeasuredWidth());
            } else {
                findViewById2.setPadding(0, findViewById2.getPaddingTop(), 0, findViewById2.getPaddingBottom());
                layoutParams.gravity = 17;
                layoutParams.setMarginStart(0);
            }
            findViewById2.setLayoutParams(layoutParams);
            if ((i2 & 4) == 0) {
                i = 8;
            }
            imageView.setVisibility(i);
            if (resourceId != 0) {
                textView.setTextAppearance(context, resourceId);
            }
            try {
                charSequence = (CharSequence) FieldTitle.get(this.mDecorViewWindow);
            } catch (IllegalAccessException unused) {
                charSequence = null;
            }
            if (charSequence != null) {
                textView.setText(charSequence);
            }
        }

        private void setStatusBarHeight(Context context, View view) {
            View findViewById = view.findViewById(miui.R.id.status_bar);
            if (findViewById != null) {
                ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
                layoutParams.height = getStatusBarHeight(context);
                findViewById.setLayoutParams(layoutParams);
            }
        }

        public LayoutInflater cloneInContext(Context context) {
            return new LayoutInflaterWrapper(this.mLayoutInflater.cloneInContext(context), this.mDecorViewWindow);
        }

        public View inflate(int i, ViewGroup viewGroup, boolean z) {
            int decorViewLayoutRes = ActivityDelegate.getDecorViewLayoutRes(this.mDecorViewWindow);
            View inflate = this.mLayoutInflater.inflate(decorViewLayoutRes, viewGroup, z);
            if (!(decorViewLayoutRes == R.layout.screen_action_bar || decorViewLayoutRes == R.layout.screen_action_bar_movable || decorViewLayoutRes == R.layout.screen_simple)) {
                Context context = this.mDecorViewWindow.getContext();
                setStatusBarHeight(context, inflate);
                if (decorViewLayoutRes == R.layout.starting_window_simple) {
                    initDefaultStartingWindow(context, inflate);
                }
            }
            return inflate;
        }
    }

    /* access modifiers changed from: private */
    public static Field getField(Class cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LayoutInflaterWrapper replaceLayoutInflater(Window window) {
        try {
            LayoutInflaterWrapper layoutInflaterWrapper = new LayoutInflaterWrapper((LayoutInflater) FieldLayoutInflater.get(window), window);
            FieldLayoutInflater.set(window, layoutInflaterWrapper);
            return layoutInflaterWrapper;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void restoreLayoutInflater(Window window, LayoutInflaterWrapper layoutInflaterWrapper) {
        try {
            FieldLayoutInflater.set(window, layoutInflaterWrapper.mLayoutInflater);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("installDecor", "()V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalInstallDecor(long j, Object obj) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.installDecor(obj);
        } else {
            originalInstallDecor(j, obj);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleInstallDecor(0, null);
    }

    /* access modifiers changed from: protected */
    public void handleInstallDecor(long j, Object obj) {
        Window window = (Window) obj;
        if (Environment.isUsingMiui(window.getContext())) {
            LayoutInflaterWrapper replaceLayoutInflater = replaceLayoutInflater(window);
            callOriginalInstallDecor(j, obj);
            restoreLayoutInflater(window, replaceLayoutInflater);
            if (DeviceHelper.IS_OLED) {
                window.getAttributes().dimAmount = ((float) AttributeResolver.resolveInt(window.getContext(), R.attr.backgroundDim, 20)) / 100.0f;
                return;
            }
            return;
        }
        callOriginalInstallDecor(j, obj);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalInstallDecor(long j, Object obj) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Internal_Policy_Impl_PhoneWindow_class.originalInstallDecor(long, Object)");
    }
}
