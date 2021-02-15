package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import com.miui.internal.R;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import miui.util.ViewUtils;

public class StateEditText extends EditText {
    private static final Class[] WIDGET_MANAGER_CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    private Drawable[] mExtraDrawables;
    private String mLabel;
    private int mLabelLength;
    private boolean mPressed;
    private WidgetManager mWidgetManager;
    private int mWidgetPadding;

    public abstract class WidgetManager {
        public WidgetManager(Context context, AttributeSet attributeSet) {
        }

        public abstract Drawable[] getWidgetDrawables();

        /* access modifiers changed from: protected */
        public void onAttached(StateEditText stateEditText) {
        }

        /* access modifiers changed from: protected */
        public void onDetached() {
        }

        public abstract void onWidgetClick(int i);
    }

    public StateEditText(Context context) {
        this(context, null);
    }

    public StateEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.stateEditTextStyle);
    }

    public StateEditText(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public StateEditText(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context, attributeSet, i, i2);
    }

    private WidgetManager createWidgetManager(Context context, String str, AttributeSet attributeSet) {
        String str2 = "Could not instantiate the WidgetManager: ";
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Constructor constructor = context.getClassLoader().loadClass(str).asSubclass(WidgetManager.class).getConstructor(WIDGET_MANAGER_CONSTRUCTOR_SIGNATURE);
            Object[] objArr = {context, attributeSet};
            constructor.setAccessible(true);
            return (WidgetManager) constructor.newInstance(objArr);
        } catch (InstantiationException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str);
            throw new IllegalStateException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(str);
            throw new IllegalStateException(sb2.toString(), e2);
        } catch (IllegalAccessException e3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Can't access non-public constructor ");
            sb3.append(str);
            throw new IllegalStateException(sb3.toString(), e3);
        } catch (ClassNotFoundException e4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Can't find WidgetManager: ");
            sb4.append(str);
            throw new IllegalStateException(sb4.toString(), e4);
        } catch (NoSuchMethodException e5) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Error creating WidgetManager ");
            sb5.append(str);
            throw new IllegalStateException(sb5.toString(), e5);
        }
    }

    private boolean dispatchEndDrawableTouchEvent(MotionEvent motionEvent) {
        if (this.mWidgetManager != null) {
            return isWidgetResumedEvent(motionEvent);
        }
        return false;
    }

    private void drawExtraWidget(Canvas canvas) {
        int i;
        int i2;
        int i3;
        Drawable drawable;
        int i4;
        if (this.mExtraDrawables != null) {
            int width = getWidth();
            int height = getHeight();
            int scrollX = getScrollX();
            int paddingEnd = getPaddingEnd();
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            int i5 = 0;
            int intrinsicWidth = compoundDrawablesRelative[2] == null ? 0 : compoundDrawablesRelative[2].getIntrinsicWidth() + this.mWidgetPadding;
            int i6 = height / 2;
            int i7 = 0;
            while (true) {
                Drawable[] drawableArr = this.mExtraDrawables;
                if (i5 < drawableArr.length) {
                    int intrinsicWidth2 = drawableArr[i5].getIntrinsicWidth();
                    int intrinsicHeight = this.mExtraDrawables[i5].getIntrinsicHeight();
                    if (ViewUtils.isLayoutRtl(this)) {
                        drawable = this.mExtraDrawables[i5];
                        int i8 = scrollX + paddingEnd + intrinsicWidth;
                        i2 = i8 + i7;
                        i4 = intrinsicHeight / 2;
                        i = i6 - i4;
                        i3 = i8 + intrinsicWidth2 + i7;
                    } else {
                        drawable = this.mExtraDrawables[i5];
                        int i9 = ((scrollX + width) - paddingEnd) - intrinsicWidth;
                        i2 = (i9 - intrinsicWidth2) - i7;
                        i4 = intrinsicHeight / 2;
                        i = i6 - i4;
                        i3 = i9 - i7;
                    }
                    drawable.setBounds(i2, i, i3, i4 + i6);
                    i7 = this.mWidgetPadding + intrinsicWidth2;
                    this.mExtraDrawables[i5].draw(canvas);
                    i5++;
                } else {
                    return;
                }
            }
        }
    }

    private void drawLabel(Canvas canvas) {
        String str;
        float f;
        if (!TextUtils.isEmpty(this.mLabel)) {
            int color = getPaint().getColor();
            getPaint().setColor(getCurrentTextColor());
            int paddingStart = getPaddingStart();
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            int i = 0;
            if (compoundDrawablesRelative[0] != null) {
                i = this.mWidgetPadding + compoundDrawablesRelative[0].getIntrinsicWidth();
            }
            if (ViewUtils.isLayoutRtl(this)) {
                str = this.mLabel;
                f = (float) ((((getScrollX() + getWidth()) - i) - this.mLabelLength) - paddingStart);
            } else {
                str = this.mLabel;
                f = (float) (paddingStart + getScrollX() + i);
            }
            canvas.drawText(str, f, (float) getBaseline(), getPaint());
            getPaint().setColor(color);
        }
    }

    private int getLabelLength() {
        return this.mLabelLength + (this.mLabelLength == 0 ? 0 : this.mWidgetPadding);
    }

    private int getWidgetLength() {
        Drawable[] drawableArr = this.mExtraDrawables;
        if (drawableArr == null) {
            return 0;
        }
        int i = 0;
        for (Drawable intrinsicWidth : drawableArr) {
            i = i + intrinsicWidth.getIntrinsicWidth() + this.mWidgetPadding;
        }
        return i;
    }

    private void initView(Context context, AttributeSet attributeSet, int i, int i2) {
        String str;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.StateEditText, i, i2);
            str = obtainStyledAttributes.getString(R.styleable.StateEditText_widgetManager);
            this.mLabel = obtainStyledAttributes.getString(R.styleable.StateEditText_label);
            this.mWidgetPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.StateEditText_widgetPadding, 0);
            obtainStyledAttributes.recycle();
        } else {
            str = null;
        }
        setWidgetManager(createWidgetManager(context, str, attributeSet));
        this.mExtraDrawables = null;
        WidgetManager widgetManager = this.mWidgetManager;
        if (widgetManager != null) {
            this.mExtraDrawables = widgetManager.getWidgetDrawables();
        }
        setLabel(this.mLabel);
    }

    private boolean isWidgetResumedEvent(MotionEvent motionEvent) {
        if (this.mExtraDrawables != null) {
            int scrollX = getScrollX();
            int i = 0;
            while (true) {
                Drawable[] drawableArr = this.mExtraDrawables;
                if (i >= drawableArr.length) {
                    break;
                }
                Rect bounds = drawableArr[i].getBounds();
                if (motionEvent.getX() >= ((float) (bounds.right - scrollX)) || motionEvent.getX() <= ((float) (bounds.left - scrollX))) {
                    i++;
                } else {
                    onWidgetTouchEvent(motionEvent, i);
                    return true;
                }
            }
        }
        return false;
    }

    private void onWidgetTouchEvent(MotionEvent motionEvent, int i) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mPressed = true;
        } else if (action != 1) {
            if (action == 3 && this.mPressed) {
                this.mPressed = false;
            }
        } else if (this.mPressed) {
            WidgetManager widgetManager = this.mWidgetManager;
            if (widgetManager != null) {
                widgetManager.onWidgetClick(i);
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (dispatchEndDrawableTouchEvent(motionEvent)) {
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + (ViewUtils.isLayoutRtl(this) ? getWidgetLength() : getLabelLength());
    }

    public int getCompoundPaddingRight() {
        return super.getCompoundPaddingRight() + (ViewUtils.isLayoutRtl(this) ? getLabelLength() : getWidgetLength());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawLabel(canvas);
        drawExtraWidget(canvas);
        super.onDraw(canvas);
    }

    public void setLabel(String str) {
        this.mLabel = str;
        this.mLabelLength = TextUtils.isEmpty(this.mLabel) ? 0 : (int) getPaint().measureText(this.mLabel);
        invalidate();
    }

    public void setWidgetManager(WidgetManager widgetManager) {
        WidgetManager widgetManager2 = this.mWidgetManager;
        if (widgetManager2 != null) {
            widgetManager2.onDetached();
        }
        this.mWidgetManager = widgetManager;
        WidgetManager widgetManager3 = this.mWidgetManager;
        if (widgetManager3 != null) {
            widgetManager3.onAttached(this);
        }
    }
}
