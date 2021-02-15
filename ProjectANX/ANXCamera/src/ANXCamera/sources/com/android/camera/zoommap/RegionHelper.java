package com.android.camera.zoommap;

import android.graphics.Rect;
import android.util.Size;
import android.view.View;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.ZoomMapAttr;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.IIntValueProperty;

public class RegionHelper {
    public static final int MARGIN = Util.dpToPixel(14.55f);
    private static final String TAG = "RegionHelper";
    public static final String TARGET_X = "TARGET_X";
    public static final String TARGET_Y = "TARGET_Y";
    private AnimConfig mConfigX;
    private AnimConfig mConfigY;
    private boolean mIsCinematicAspectRatio;
    private boolean mIsHovering = false;
    /* access modifiers changed from: private */
    public View mMapView;
    private Rect mPreviewRect;
    /* access modifiers changed from: private */
    public float mSpeedX;
    /* access modifiers changed from: private */
    public float mSpeedY;
    /* access modifiers changed from: private */
    public int mTranslationX;
    private int mTranslationXMax;
    private int mTranslationXMid;
    private int mTranslationXMin;
    /* access modifiers changed from: private */
    public int mTranslationY;
    private int mTranslationYMax;
    private int mTranslationYMid;
    private int mTranslationYMin;
    private Size mWindowSize;

    public RegionHelper(View view, Size size, boolean z) {
        this.mMapView = view;
        this.mWindowSize = size;
        this.mIsCinematicAspectRatio = z;
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        int i = 4;
        if (ModuleManager.isSquareModule()) {
            uiStyle = 4;
        }
        if (uiStyle != 4) {
            i = 0;
        }
        this.mPreviewRect = Util.getDisplayRect(i);
        this.mTranslationXMin = -((this.mPreviewRect.width() - size.getWidth()) - (MARGIN * 2));
        if (this.mIsCinematicAspectRatio) {
            this.mTranslationXMin += Util.getCinematicAspectRatioMargin() * 2;
        }
        this.mTranslationXMax = 0;
        this.mTranslationXMid = (this.mTranslationXMin + this.mTranslationXMax) / 2;
        this.mTranslationYMin = 0;
        this.mTranslationYMax = ((this.mPreviewRect.height() - MARGIN) - size.getHeight()) - MARGIN;
        this.mTranslationYMid = (this.mTranslationYMin + this.mTranslationYMax) / 2;
        StringBuilder sb = new StringBuilder();
        sb.append("mTranslationYMin = ");
        sb.append(this.mTranslationYMin);
        sb.append(", mTranslationYMax ");
        sb.append(this.mTranslationYMax);
        Log.d(TAG, sb.toString());
    }

    public Rect getPipWindowDefaultLocation() {
        int width = (this.mPreviewRect.width() - MARGIN) - this.mWindowSize.getWidth();
        if (this.mIsCinematicAspectRatio) {
            width -= Util.getCinematicAspectRatioMargin();
        }
        int i = this.mPreviewRect.top + MARGIN;
        return new Rect(width, i, this.mWindowSize.getWidth() + width, this.mWindowSize.getHeight() + i);
    }

    public void initAnimConfig() {
        this.mConfigX = new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mSpeedX = f;
            }
        });
        this.mConfigY = new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mSpeedY = f;
            }
        });
        this.mSpeedX = 0.0f;
        this.mSpeedY = 0.0f;
        Folme.useValue(TARGET_X).setTo(Integer.valueOf(this.mTranslationX), this.mConfigX);
        Folme.useValue(TARGET_Y).setTo(Integer.valueOf(this.mTranslationY), this.mConfigY);
    }

    public void initTranslation() {
        Log.d(TAG, "initTranslation");
        this.mTranslationX = 0;
        this.mTranslationY = 0;
        this.mMapView.setTranslationX(0.0f);
        this.mMapView.setTranslationY(0.0f);
    }

    public boolean isHovering() {
        return this.mIsHovering;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b6 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c0 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c7 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean moveToEdge() {
        int i;
        boolean z;
        String str;
        int cinematicAspectRatioMargin = this.mIsCinematicAspectRatio ? Util.getCinematicAspectRatioMargin() : 0;
        if (this.mTranslationX < ((this.mTranslationXMin - cinematicAspectRatioMargin) - MARGIN) - (this.mWindowSize.getWidth() / 3)) {
            i = ((this.mTranslationXMin - cinematicAspectRatioMargin) - (MARGIN * 2)) - this.mWindowSize.getWidth();
        } else if (this.mTranslationX > MARGIN + cinematicAspectRatioMargin + (this.mWindowSize.getWidth() / 3)) {
            i = this.mTranslationXMax + this.mWindowSize.getWidth() + (MARGIN * 2) + cinematicAspectRatioMargin;
        } else {
            float f = this.mSpeedX;
            i = (f <= 2000.0f && (f < -2000.0f || this.mTranslationX < this.mTranslationXMid)) ? this.mTranslationXMin : this.mTranslationXMax;
            z = false;
            float f2 = this.mSpeedY;
            int i2 = (f2 <= 2000.0f && (f2 < -2000.0f || this.mTranslationY < this.mTranslationYMid)) ? this.mTranslationYMin : this.mTranslationYMax;
            StringBuilder sb = new StringBuilder();
            sb.append("moveToEdge mSpeedX: ");
            sb.append(this.mSpeedX);
            sb.append(", mSpeedY: ");
            sb.append(this.mSpeedY);
            sb.append(", destX: ");
            sb.append(i);
            sb.append(", destY: ");
            sb.append(i2);
            Log.d(TAG, sb.toString());
            if (i != 0 && i2 == 0) {
                str = ZoomMapAttr.MOVE_DIRECTION_RIGHT_TOP;
            } else if (i != 0 && i2 > 0) {
                str = ZoomMapAttr.MOVE_DIRECTION_RIGHT_BOTTOM;
            } else if (i < 0 || i2 != 0) {
                if (i < 0 && i2 > 0) {
                    str = ZoomMapAttr.MOVE_DIRECTION_LEFT_BOTTOM;
                }
                final Boolean valueOf = Boolean.valueOf(z);
                Folme.useValue(TARGET_X).to(Integer.valueOf(i), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                    public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                        RegionHelper.this.mTranslationX = i;
                        RegionHelper.this.mMapView.setTranslationX((float) RegionHelper.this.mTranslationX);
                        if (valueOf.booleanValue() && z) {
                            Log.d(RegionHelper.TAG, "hidden zoom map view");
                            RegionHelper.this.mMapView.setVisibility(4);
                        }
                    }
                }));
                Folme.useValue(TARGET_Y).to(Integer.valueOf(i2), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                    public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                        RegionHelper.this.mTranslationY = i;
                        RegionHelper.this.mMapView.setTranslationY((float) RegionHelper.this.mTranslationY);
                    }
                }));
                return z;
            } else {
                str = ZoomMapAttr.MOVE_DIRECTION_LEFT_TOP;
            }
            CameraStatUtils.trackZoomMapMoveWindow(str);
            final Boolean valueOf2 = Boolean.valueOf(z);
            Folme.useValue(TARGET_X).to(Integer.valueOf(i), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                    RegionHelper.this.mTranslationX = i;
                    RegionHelper.this.mMapView.setTranslationX((float) RegionHelper.this.mTranslationX);
                    if (valueOf2.booleanValue() && z) {
                        Log.d(RegionHelper.TAG, "hidden zoom map view");
                        RegionHelper.this.mMapView.setVisibility(4);
                    }
                }
            }));
            Folme.useValue(TARGET_Y).to(Integer.valueOf(i2), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                    RegionHelper.this.mTranslationY = i;
                    RegionHelper.this.mMapView.setTranslationY((float) RegionHelper.this.mTranslationY);
                }
            }));
            return z;
        }
        z = true;
        float f22 = this.mSpeedY;
        if (f22 <= 2000.0f) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("moveToEdge mSpeedX: ");
            sb2.append(this.mSpeedX);
            sb2.append(", mSpeedY: ");
            sb2.append(this.mSpeedY);
            sb2.append(", destX: ");
            sb2.append(i);
            sb2.append(", destY: ");
            sb2.append(i2);
            Log.d(TAG, sb2.toString());
            if (i != 0) {
            }
            if (i != 0) {
            }
            if (i < 0) {
            }
            str = ZoomMapAttr.MOVE_DIRECTION_LEFT_BOTTOM;
            CameraStatUtils.trackZoomMapMoveWindow(str);
            final Boolean valueOf22 = Boolean.valueOf(z);
            Folme.useValue(TARGET_X).to(Integer.valueOf(i), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                    RegionHelper.this.mTranslationX = i;
                    RegionHelper.this.mMapView.setTranslationX((float) RegionHelper.this.mTranslationX);
                    if (valueOf22.booleanValue() && z) {
                        Log.d(RegionHelper.TAG, "hidden zoom map view");
                        RegionHelper.this.mMapView.setVisibility(4);
                    }
                }
            }));
            Folme.useValue(TARGET_Y).to(Integer.valueOf(i2), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
                public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                    RegionHelper.this.mTranslationY = i;
                    RegionHelper.this.mMapView.setTranslationY((float) RegionHelper.this.mTranslationY);
                }
            }));
            return z;
        }
        StringBuilder sb22 = new StringBuilder();
        sb22.append("moveToEdge mSpeedX: ");
        sb22.append(this.mSpeedX);
        sb22.append(", mSpeedY: ");
        sb22.append(this.mSpeedY);
        sb22.append(", destX: ");
        sb22.append(i);
        sb22.append(", destY: ");
        sb22.append(i2);
        Log.d(TAG, sb22.toString());
        if (i != 0) {
        }
        if (i != 0) {
        }
        if (i < 0) {
        }
        str = ZoomMapAttr.MOVE_DIRECTION_LEFT_BOTTOM;
        CameraStatUtils.trackZoomMapMoveWindow(str);
        final Boolean valueOf222 = Boolean.valueOf(z);
        Folme.useValue(TARGET_X).to(Integer.valueOf(i), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mTranslationX = i;
                RegionHelper.this.mMapView.setTranslationX((float) RegionHelper.this.mTranslationX);
                if (valueOf222.booleanValue() && z) {
                    Log.d(RegionHelper.TAG, "hidden zoom map view");
                    RegionHelper.this.mMapView.setVisibility(4);
                }
            }
        }));
        Folme.useValue(TARGET_Y).to(Integer.valueOf(i2), new AnimConfig().setEase(-2, 0.7f, 0.5f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                RegionHelper.this.mTranslationY = i;
                RegionHelper.this.mMapView.setTranslationY((float) RegionHelper.this.mTranslationY);
            }
        }));
        return z;
    }

    public void setHovering(boolean z) {
        this.mIsHovering = z;
    }

    public void updateTranslation(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateTranslation ");
        sb.append(i);
        sb.append(" ");
        sb.append(i2);
        Log.d(TAG, sb.toString());
        this.mTranslationX = i;
        int i3 = this.mTranslationYMin;
        int i4 = MARGIN;
        this.mTranslationY = Util.getValidValue(i2, i3 - i4, this.mTranslationYMax + i4);
        this.mMapView.setTranslationX((float) this.mTranslationX);
        this.mMapView.setTranslationY((float) this.mTranslationY);
        Folme.useValue(TARGET_X).setTo(Integer.valueOf(this.mTranslationX), this.mConfigX);
        Folme.useValue(TARGET_Y).setTo(Integer.valueOf(this.mTranslationY), this.mConfigY);
    }
}
