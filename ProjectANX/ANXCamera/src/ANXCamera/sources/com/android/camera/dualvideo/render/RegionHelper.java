package com.android.camera.dualvideo.render;

import android.graphics.Rect;
import com.android.camera.Util;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.log.Log;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.IIntValueProperty;

public class RegionHelper {
    private static final String FOLME_TARGET_X = "xSpeed";
    private static final String FOLME_TARGET_Y = "ySpeed";
    private static final int MARGIN_BOTTOM = Util.dpToPixel(14.5f);
    private static final int MINI_MARGIN = Util.dpToPixel(14.5f);
    private static final float MINI_SCALE_X = 0.3333f;
    private static final float MINI_SCALE_Y = 0.3333f;
    private static final int NUM_PER_ROW = 2;
    private static final int PATCH_SEPARATOR = Util.dpToPixel(4.364f);
    private static final String TAG = "RegionHelper";
    private AnimConfig mConfigX;
    private AnimConfig mConfigY;
    Rect mDrawRect;
    public boolean mIsHovering = false;
    /* access modifiers changed from: private */
    public UpdatedListener mListener;
    /* access modifiers changed from: private */
    public int mMiniMarginLeft = 0;
    /* access modifiers changed from: private */
    public int mMiniMarginTop = MINI_MARGIN;
    /* access modifiers changed from: private */
    public float mSpeedX;
    /* access modifiers changed from: private */
    public float mSpeedY;
    public float mStartX = 0.0f;
    public float mStartY = 0.0f;
    private Rect mValidMiniRect;

    /* renamed from: com.android.camera.dualvideo.render.RegionHelper$5 reason: invalid class name */
    /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$LayoutType = new int[LayoutType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|(3:11|12|14)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP_FULL.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN_FULL.ordinal()] = 2;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP.ordinal()] = 3;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN.ordinal()] = 4;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.MINI.ordinal()] = 5;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.FULL.ordinal()] = 6;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public interface UpdatedListener {
        void onUpdated();
    }

    public RegionHelper(Rect rect) {
        setDrawRect(rect);
    }

    private void checkLocation() {
        int i = this.mMiniMarginLeft;
        int i2 = this.mValidMiniRect.left;
        if (i < i2) {
            this.mMiniMarginLeft = i2;
        }
        int i3 = this.mMiniMarginLeft;
        int i4 = this.mValidMiniRect.right;
        if (i3 > i4) {
            this.mMiniMarginLeft = i4;
        }
        int i5 = this.mMiniMarginTop;
        int i6 = this.mValidMiniRect.top;
        if (i5 < i6) {
            this.mMiniMarginTop = i6;
        }
        int i7 = this.mMiniMarginTop;
        int i8 = this.mValidMiniRect.bottom;
        if (i7 > i8) {
            this.mMiniMarginTop = i8;
        }
    }

    private Rect getLayoutRect(LayoutType layoutType) {
        int size = DualVideoConfigManager.instance().getConfigs().size();
        int index = layoutType.getIndex() - LayoutType.PATCH_0.getIndex();
        int i = index / 2;
        int i2 = index % 2;
        boolean z = size == index + 1;
        int width = (this.mDrawRect.width() - (PATCH_SEPARATOR * 1)) / 2;
        int i3 = ((size - 1) / 2) + 1;
        int height = this.mDrawRect.height();
        int i4 = PATCH_SEPARATOR;
        int i5 = (height - ((i3 - 1) * i4)) / i3;
        Rect rect = this.mDrawRect;
        int i6 = rect.left + (i2 * i4) + (i2 * width);
        int i7 = rect.top + (i4 * i) + (i * i5);
        return z ? new Rect(i6, i7, rect.right, i5 + i7) : new Rect(i6, i7, width + i6, i5 + i7);
    }

    public void clearListener() {
        this.mListener = null;
    }

    public synchronized int[] getMargin() {
        return new int[]{this.mMiniMarginLeft, this.mMiniMarginTop};
    }

    public synchronized Rect getRenderAreaFor(LayoutType layoutType) {
        switch (AnonymousClass5.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[layoutType.ordinal()]) {
            case 1:
                return new Rect(this.mDrawRect.left + (this.mDrawRect.width() / 4), this.mDrawRect.top, this.mDrawRect.left + ((this.mDrawRect.width() * 3) / 4), (this.mDrawRect.top + (this.mDrawRect.height() / 2)) - 5);
            case 2:
                return new Rect(this.mDrawRect.left + (this.mDrawRect.width() / 4), this.mDrawRect.top + (this.mDrawRect.height() / 2) + 5, this.mDrawRect.left + ((this.mDrawRect.width() * 3) / 4), this.mDrawRect.top + this.mDrawRect.height());
            case 3:
                return new Rect(this.mDrawRect.left, this.mDrawRect.top, this.mDrawRect.left + this.mDrawRect.width(), this.mDrawRect.top + (this.mDrawRect.height() / 2));
            case 4:
                return new Rect(this.mDrawRect.left, this.mDrawRect.top + (this.mDrawRect.height() / 2), this.mDrawRect.left + this.mDrawRect.width(), this.mDrawRect.top + this.mDrawRect.height());
            case 5:
                return new Rect(this.mDrawRect.left + this.mMiniMarginLeft, this.mDrawRect.top + this.mMiniMarginTop, ((int) (((float) this.mDrawRect.width()) * 0.3333f)) + this.mDrawRect.left + this.mMiniMarginLeft, ((int) (((float) this.mDrawRect.height()) * 0.3333f)) + this.mDrawRect.top + this.mMiniMarginTop);
            case 6:
                return new Rect(this.mDrawRect.left, this.mDrawRect.top, this.mDrawRect.width() + this.mDrawRect.left, this.mDrawRect.height() + this.mDrawRect.top);
            default:
                return getLayoutRect(layoutType);
        }
    }

    public void moveToEdge() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("moveToEdge xSpeed: ");
        sb.append(this.mSpeedX);
        sb.append("ySpeed: ");
        sb.append(this.mSpeedY);
        Log.d(str, sb.toString());
        float f = this.mSpeedX;
        int i = (f <= 2000.0f && (f <= -2000.0f || this.mMiniMarginLeft < this.mValidMiniRect.centerX())) ? this.mValidMiniRect.left : this.mValidMiniRect.right;
        float f2 = this.mSpeedY;
        int i2 = (f2 <= 2000.0f && (f2 <= -2000.0f || this.mMiniMarginTop < this.mValidMiniRect.centerY())) ? this.mValidMiniRect.top : this.mValidMiniRect.bottom;
        Folme.useValue(FOLME_TARGET_X).to(Integer.valueOf(i), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mMiniMarginLeft = i;
                RegionHelper.this.mListener.onUpdated();
            }
        }));
        Folme.useValue(FOLME_TARGET_Y).to(Integer.valueOf(i2), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mMiniMarginTop = i;
                RegionHelper.this.mListener.onUpdated();
            }
        }));
    }

    public void setDrawRect(Rect rect) {
        this.mDrawRect = rect;
        int i = MINI_MARGIN;
        this.mValidMiniRect = new Rect(i, i, (this.mDrawRect.width() - ((int) (((float) this.mDrawRect.width()) * 0.3333f))) - MINI_MARGIN, (this.mDrawRect.height() - ((int) (((float) this.mDrawRect.height()) * 0.3333f))) - MARGIN_BOTTOM);
        if (this.mMiniMarginLeft == 0) {
            this.mMiniMarginLeft = this.mValidMiniRect.right;
        }
    }

    public void setListener(UpdatedListener updatedListener) {
        this.mConfigX = new AnimConfig().addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mSpeedX = f;
            }
        });
        this.mConfigY = new AnimConfig().addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mSpeedY = f;
            }
        });
        this.mSpeedX = 0.0f;
        this.mStartY = 0.0f;
        Folme.useValue(FOLME_TARGET_X).setTo(Integer.valueOf(this.mMiniMarginLeft), this.mConfigX);
        Folme.useValue(FOLME_TARGET_Y).setTo(Integer.valueOf(this.mMiniMarginTop), this.mConfigY);
        this.mListener = updatedListener;
    }

    public void setStartPosition(float f, float f2) {
        this.mStartX = f;
        this.mStartY = f2;
    }

    public synchronized void updateMarginOffset(int i, int i2) {
        this.mMiniMarginLeft += i;
        this.mMiniMarginTop += i2;
        checkLocation();
        this.mListener.onUpdated();
        Folme.useValue(FOLME_TARGET_X).setTo(Integer.valueOf(this.mMiniMarginLeft), this.mConfigX);
        Folme.useValue(FOLME_TARGET_Y).setTo(Integer.valueOf(this.mMiniMarginTop), this.mConfigY);
    }
}
