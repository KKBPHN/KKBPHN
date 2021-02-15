package com.android.camera.dualvideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.dualvideo.render.CameraItemInterface;
import com.android.camera.dualvideo.render.FaceType;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.render.MiscTextureManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.effect.draw_mode.DrawRectShapeAttributeBase;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TouchEventView extends View {
    private static final String TAG = "TouchEventView";
    /* access modifiers changed from: private */
    public TouchEventListener mListener;
    private TouchHelper mTouchHelper;

    /* renamed from: com.android.camera.dualvideo.view.TouchEventView$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$FaceType = new int[FaceType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$LayoutType = new int[LayoutType.values().length];

        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_FRONT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_BACK.ordinal()] = 2;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_REMOTE.ordinal()] = 3;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.FULL.ordinal()] = 1;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.MINI.ordinal()] = 2;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public interface TouchEventListener {
        boolean onTouchEvent(MotionEvent motionEvent);
    }

    class TouchHelper extends ExploreByTouchHelper {
        private static final int BLANK_VIEW = 0;
        private static final int EXPAND_VIEW = 1;
        private static final int SHRINK_VIEW = 2;
        private int mLayoutTypeIndex;
        private final PointF mTouchPoint = new PointF();

        public TouchHelper(@NonNull View view) {
            super(view);
        }

        static /* synthetic */ CameraItemInterface O000000o(int i, ArrayList arrayList) {
            return (CameraItemInterface) arrayList.stream().filter(new O0000Oo0(i)).findAny().get();
        }

        static /* synthetic */ void O000000o(Rect rect, CameraItemInterface cameraItemInterface) {
            DrawRectShapeAttributeBase renderAttri = cameraItemInterface.getRenderAttri(101);
            int i = renderAttri.mX;
            int i2 = renderAttri.mY;
            rect.set(i, i2, renderAttri.mWidth + i, renderAttri.mHeight + i2);
        }

        static /* synthetic */ boolean O000000o(int i, CameraItemInterface cameraItemInterface) {
            return cameraItemInterface.getRenderLayoutType().getIndex() == i;
        }

        static /* synthetic */ boolean O00000Oo(int i, CameraItemInterface cameraItemInterface) {
            return cameraItemInterface.getRenderLayoutType().getIndex() == i;
        }

        private String getDescription(int i) {
            Resources resources;
            int i2;
            String str = "";
            if (i == 0) {
                return str;
            }
            if (isSelectWindow()) {
                return getSelectDescription(i);
            }
            if (i == 1) {
                resources = TouchEventView.this.getResources();
                i2 = R.string.accessibility_preview_expand;
            } else if (i != 2) {
                return str;
            } else {
                resources = TouchEventView.this.getResources();
                i2 = R.string.accessibility_preview_shrink;
            }
            return resources.getString(i2);
        }

        private MotionEvent getEventDown(PointF pointF) {
            return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100, 0, pointF.x, pointF.y, 0);
        }

        private MotionEvent getEventUp(PointF pointF) {
            return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100, 1, pointF.x, pointF.y, 0);
        }

        private MiscTextureManager getMiscTexManager() {
            return (MiscTextureManager) getRenderProtocol().map(O00000o0.INSTANCE).map(C0249O0000o0O.INSTANCE).get();
        }

        private Rect getRect(int i) {
            Rect rect = new Rect();
            rect.setEmpty();
            if (i == 0) {
                return rect;
            }
            if (isSelectWindow()) {
                getRenderProtocol().ifPresent(new C0247O0000OoO(i, rect));
            } else {
                getRenderList().ifPresent(new C0245O00000oO(this, rect));
            }
            return rect;
        }

        private Optional getRenderList() {
            return getRenderProtocol().map(O00000o0.INSTANCE).map(O000000o.INSTANCE).map(O00000Oo.INSTANCE);
        }

        private Optional getRenderProtocol() {
            return Optional.ofNullable((DualVideoRenderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(430));
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x009e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private String getSelectDescription(int i) {
            String str;
            Resources resources;
            int i2;
            if (i == LayoutType.UNDEFINED.getIndex()) {
                return " ";
            }
            Optional map = getRenderList().map(new O0000o0(i));
            if (!map.isPresent()) {
                return "";
            }
            float presentZoom = DualVideoConfigManager.instance().getPresentZoom(((CameraItemInterface) map.get()).getSelectWindowLayoutType());
            int index = ((CameraItemInterface) map.get()).getSelectedIndex().getIndex();
            FaceType faceType = ((CameraItemInterface) map.get()).getFaceType();
            StringBuilder sb = new StringBuilder();
            int i3 = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$FaceType[faceType.ordinal()];
            if (i3 == 1) {
                resources = TouchEventView.this.getResources();
                i2 = R.string.accessibility_preview_front;
            } else if (i3 != 2) {
                if (i3 == 3) {
                    resources = TouchEventView.this.getResources();
                    i2 = R.string.accessibility_preview_remote;
                }
                sb.append(", ");
                if (index != 0) {
                    sb.append(String.format(TouchEventView.this.getResources().getString(R.string.accessibility_preview_select_tip), new Object[]{Integer.valueOf(index)}));
                }
                return sb.toString();
            } else {
                str = String.format(TouchEventView.this.getResources().getString(R.string.accessibility_preview_zoom_tip), new Object[]{Float.valueOf(presentZoom)});
                sb.append(str);
                sb.append(", ");
                if (index != 0) {
                }
                return sb.toString();
            }
            str = resources.getString(i2);
            sb.append(str);
            sb.append(", ");
            if (index != 0) {
            }
            return sb.toString();
        }

        private boolean isSelectWindow() {
            return CameraSettings.getDualVideoConfig().ismDrawSelectWindow();
        }

        public /* synthetic */ void O000000o(Rect rect, ArrayList arrayList) {
            arrayList.stream().filter(new O0000OOo(this)).findAny().ifPresent(new O0000Oo(this, rect));
        }

        public /* synthetic */ boolean O000000o(float f, float f2, CameraItemInterface cameraItemInterface) {
            Rect handleArea = cameraItemInterface.getHandleArea(getMiscTexManager());
            if (handleArea == null) {
                return false;
            }
            return handleArea.contains((int) f, (int) f2);
        }

        public /* synthetic */ boolean O000000o(CameraItemInterface cameraItemInterface) {
            if (cameraItemInterface.getRenderLayoutType() == LayoutType.MINI) {
                return false;
            }
            Rect handleArea = cameraItemInterface.getHandleArea(getMiscTexManager());
            PointF pointF = this.mTouchPoint;
            return handleArea.contains((int) pointF.x, (int) pointF.y);
        }

        public /* synthetic */ void O00000Oo(Rect rect, CameraItemInterface cameraItemInterface) {
            rect.set(cameraItemInterface.getHandleArea(getMiscTexManager()));
        }

        /* access modifiers changed from: protected */
        public int getVirtualViewAt(float f, float f2) {
            this.mTouchPoint.set(f, f2);
            if (isSelectWindow()) {
                LayoutType layoutType = (LayoutType) getRenderProtocol().map(new O0000O0o(f, f2)).orElse(LayoutType.UNDEFINED);
                if (layoutType != LayoutType.UNDEFINED) {
                    this.mLayoutTypeIndex = layoutType.getIndex();
                    return this.mLayoutTypeIndex;
                }
            } else {
                Optional findAny = ((ArrayList) getRenderList().get()).stream().filter(new C0246O00000oo(this, f, f2)).findAny();
                if (findAny.isPresent()) {
                    int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[((CameraItemInterface) findAny.get()).getRenderLayoutType().ordinal()];
                    if (i == 1) {
                        this.mLayoutTypeIndex = 2;
                    } else if (i != 2) {
                        this.mLayoutTypeIndex = 1;
                    }
                    return this.mLayoutTypeIndex;
                }
            }
            this.mLayoutTypeIndex = 0;
            return this.mLayoutTypeIndex;
        }

        /* access modifiers changed from: protected */
        public void getVisibleVirtualViews(List list) {
        }

        /* access modifiers changed from: protected */
        public boolean onPerformActionForVirtualView(int i, int i2, @Nullable Bundle bundle) {
            if (i2 == 16) {
                TouchEventView.this.mListener.onTouchEvent(getEventDown(this.mTouchPoint));
                TouchEventView.this.mListener.onTouchEvent(getEventUp(this.mTouchPoint));
                sendEventForVirtualView(i, 65536);
                if (isSelectWindow()) {
                    TouchEventView touchEventView = TouchEventView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append(getSelectDescription(i));
                    sb.append(TouchEventView.this.getResources().getString(R.string.accessibility_preview_selected));
                    touchEventView.announceForAccessibility(sb.toString());
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForVirtualView(int i, @NonNull AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setBoundsInParent(getRect(i));
            accessibilityNodeInfoCompat.setContentDescription(getDescription(i));
            accessibilityNodeInfoCompat.addAction(16);
            accessibilityNodeInfoCompat.setClickable(true);
        }
    }

    public TouchEventView(Context context) {
        super(context);
        this.mTouchHelper = new TouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, this.mTouchHelper);
    }

    public TouchEventView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TouchEventView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TouchEventView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    private void handleBackStack() {
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.handleBackStackFromKeyBack();
        }
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        TouchHelper touchHelper = this.mTouchHelper;
        if (touchHelper == null || !touchHelper.dispatchHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        TouchEventListener touchEventListener = this.mListener;
        if (touchEventListener == null) {
            return super.onTouchEvent(motionEvent);
        }
        boolean onTouchEvent = touchEventListener.onTouchEvent(motionEvent);
        if (onTouchEvent) {
            handleBackStack();
        }
        return onTouchEvent;
    }

    public void setListener(TouchEventListener touchEventListener) {
        this.mListener = touchEventListener;
    }
}
