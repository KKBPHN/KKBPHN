package com.miui.internal.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class OutsideCompoundDrawable extends Drawable implements Callback {
    private static int ATTR_DRAWABLE;
    private Drawable mBase;
    private Drawable mBottom;
    private Drawable mTop;

    public OutsideCompoundDrawable() {
        if (ATTR_DRAWABLE == 0) {
            try {
                ATTR_DRAWABLE = Class.forName("com.android.internal.R$attr").getDeclaredField("drawable").getInt(null);
            } catch (Exception unused) {
            }
        }
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mBase;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        Drawable drawable2 = this.mTop;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            drawable3.draw(canvas);
        }
    }

    public int getIntrinsicHeight() {
        Drawable drawable = this.mBase;
        if (drawable == null) {
            return -1;
        }
        return drawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        Drawable drawable = this.mBase;
        if (drawable == null) {
            return -1;
        }
        return drawable.getIntrinsicWidth();
    }

    public int getOpacity() {
        Drawable drawable = this.mBase;
        if (drawable == null || !drawable.isVisible()) {
            return -2;
        }
        return this.mBase.getOpacity();
    }

    public boolean getPadding(Rect rect) {
        Drawable drawable = this.mBase;
        if (drawable != null) {
            return drawable.getPadding(rect);
        }
        rect.set(0, 0, 0, 0);
        return false;
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        boolean z;
        Drawable drawable;
        Drawable drawable2;
        int next;
        int depth = xmlPullParser.getDepth() + 1;
        while (true) {
            int next2 = xmlPullParser.next();
            if (next2 != 1) {
                int depth2 = xmlPullParser.getDepth();
                if (depth2 < depth && next2 == 3) {
                    return;
                }
                if (next2 == 2) {
                    if (depth2 <= depth) {
                        String name = xmlPullParser.getName();
                        int i = 0;
                        if (name.equals("item-top")) {
                            z = false;
                        } else if (name.equals("item-base")) {
                            z = true;
                        } else if (name.equals("item-bottom")) {
                            z = true;
                        } else {
                            continue;
                        }
                        int attributeCount = attributeSet.getAttributeCount();
                        int i2 = 0;
                        while (true) {
                            if (i2 >= attributeCount) {
                                break;
                            }
                            int attributeNameResource = attributeSet.getAttributeNameResource(i2);
                            if (attributeNameResource == 0) {
                                break;
                            } else if (attributeNameResource == ATTR_DRAWABLE) {
                                i = attributeSet.getAttributeResourceValue(i2, 0);
                                break;
                            } else {
                                i2++;
                            }
                        }
                        if (i != 0) {
                            drawable = resources.getDrawable(i);
                        } else {
                            while (true) {
                                next = xmlPullParser.next();
                                if (next != 4) {
                                    break;
                                }
                            }
                            if (next == 2) {
                                drawable = Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(xmlPullParser.getPositionDescription());
                                sb.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                                throw new XmlPullParserException(sb.toString());
                            }
                        }
                        if (drawable != null) {
                            if (!z) {
                                this.mTop = drawable;
                                drawable2 = this.mTop;
                            } else if (z) {
                                this.mBase = drawable;
                                drawable2 = this.mBase;
                            } else if (z) {
                                this.mBottom = drawable;
                                drawable2 = this.mBottom;
                            }
                            drawable2.setCallback(this);
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    public void invalidateDrawable(Drawable drawable) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public boolean isStateful() {
        Drawable drawable = this.mTop;
        if (drawable == null || !drawable.isStateful()) {
            Drawable drawable2 = this.mBase;
            if (drawable2 == null || !drawable2.isStateful()) {
                Drawable drawable3 = this.mBottom;
                if (drawable3 == null || !drawable3.isStateful()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void jumpToCurrentState() {
        Drawable drawable = this.mTop;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mBase;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        Drawable drawable = this.mTop;
        boolean z = drawable != null && drawable.setLevel(i);
        Drawable drawable2 = this.mBase;
        if (drawable2 != null) {
            z = drawable2.setLevel(i) || z;
        }
        Drawable drawable3 = this.mBottom;
        return drawable3 != null ? drawable3.setLevel(i) || z : z;
    }

    public boolean onStateChange(int[] iArr) {
        Drawable drawable = this.mTop;
        boolean z = drawable != null && drawable.isStateful() && this.mTop.setState(iArr);
        Drawable drawable2 = this.mBase;
        if (drawable2 != null && drawable2.isStateful()) {
            z = this.mBase.setState(iArr) || z;
        }
        Drawable drawable3 = this.mBottom;
        return (drawable3 == null || !drawable3.isStateful()) ? z : this.mBottom.setState(iArr) || z;
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(this, runnable, j);
        }
    }

    public void setAlpha(int i) {
        Drawable drawable = this.mTop;
        if (drawable != null) {
            drawable.setAlpha(i);
        }
        Drawable drawable2 = this.mBase;
        if (drawable2 != null) {
            drawable2.setAlpha(i);
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            drawable3.setAlpha(i);
        }
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        Drawable drawable = this.mBase;
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
        }
        Drawable drawable2 = this.mTop;
        if (drawable2 != null) {
            int intrinsicHeight = drawable2.getIntrinsicHeight();
            if (intrinsicHeight <= 0) {
                intrinsicHeight = 1;
            }
            this.mTop.setBounds(i, i2 - intrinsicHeight, i3, i2);
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            int intrinsicHeight2 = drawable3.getIntrinsicHeight();
            if (intrinsicHeight2 <= 0) {
                intrinsicHeight2 = 1;
            }
            this.mBottom.setBounds(i, i4, i3, intrinsicHeight2 + i4);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        Drawable drawable = this.mTop;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
        Drawable drawable2 = this.mBase;
        if (drawable2 != null) {
            drawable2.setColorFilter(colorFilter);
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            drawable3.setColorFilter(colorFilter);
        }
    }

    public boolean setVisible(boolean z, boolean z2) {
        Drawable drawable = this.mTop;
        if (drawable != null) {
            drawable.setVisible(z, z2);
        }
        Drawable drawable2 = this.mBase;
        if (drawable2 != null) {
            drawable2.setVisible(z, z2);
        }
        Drawable drawable3 = this.mBottom;
        if (drawable3 != null) {
            drawable3.setVisible(z, z2);
        }
        return super.setVisible(z, z2);
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(this, runnable);
        }
    }
}
