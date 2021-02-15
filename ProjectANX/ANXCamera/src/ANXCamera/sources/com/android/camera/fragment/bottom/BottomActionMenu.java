package com.android.camera.fragment.bottom;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.constant.ColorConstant;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentRunningMasterFilter;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningFastMotion;
import com.android.camera.data.data.runing.ComponentRunningFastMotionPro;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.FastMotionProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.FilterAttr;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.ColorActivateTextView;
import io.reactivex.Completable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottomActionMenu implements OnClickListener {
    public static final int ANIM_EXPAND = 160;
    public static final int ANIM_SHRINK = 161;
    public static final int BEAUTY_BOTTOM_MENU = 1;
    public static final int CAMERA_OPERATE_BOTTOM_MENU = 0;
    public static final int KALEIDOSCOPE_BOTTOM_MENU = 3;
    public static final int LIVE_BOTTOM_MENU = 2;
    public static final int MIMOJI_BOTTOM_MENU = 4;
    /* access modifiers changed from: private */
    public static final String TAG = "BottomActionMenu";
    private LinearLayout beautyOperateMenuView;
    private BeautyMenuGroupManager mBeautyOperateMenuViewWrapper;
    private FrameLayout mContainerView;
    private Context mContext;
    private OnClickListener mFastMotionListener = new OnClickListener() {
        public void onClick(View view) {
            if (BottomActionMenu.this.mLastSelectedView != null) {
                BottomActionMenu.this.mLastSelectedView.setActivated(false);
                BottomActionMenu.this.mLastSelectedView = (ColorActivateTextView) view;
                BottomActionMenu.this.mLastSelectedView.setActivated(true);
            }
            ComponentDataItem componentDataItem = (ComponentDataItem) view.getTag();
            if (componentDataItem != null) {
                String str = componentDataItem.mValue;
                String string = componentDataItem.mDisplayNameRes > 0 ? view.getContext().getString(componentDataItem.mDisplayNameRes) : "";
                String access$100 = BottomActionMenu.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onClick: type=");
                sb.append(str);
                sb.append(", name=");
                sb.append(string);
                Log.u(access$100, sb.toString());
                FastMotionProtocol fastMotionProtocol = (FastMotionProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(674);
                if (fastMotionProtocol != null) {
                    fastMotionProtocol.switchType(str, true);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public ColorActivateTextView mLastSelectedView;
    private OnClickListener mListener = new OnClickListener() {
        public void onClick(View view) {
            if (BottomActionMenu.this.mLastSelectedView != null) {
                BottomActionMenu.this.mLastSelectedView.setActivated(false);
                BottomActionMenu.this.mLastSelectedView = (ColorActivateTextView) view;
                BottomActionMenu.this.mLastSelectedView.setActivated(true);
            }
            ComponentDataItem componentDataItem = (ComponentDataItem) view.getTag();
            if (componentDataItem != null) {
                String str = componentDataItem.mValue;
                String string = componentDataItem.mDisplayNameRes > 0 ? view.getContext().getString(componentDataItem.mDisplayNameRes) : "";
                String access$100 = BottomActionMenu.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onClick: type=");
                sb.append(str);
                sb.append(", name=");
                sb.append(string);
                Log.u(access$100, sb.toString());
                WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
                if (watermarkProtocol != null) {
                    watermarkProtocol.switchType(str, true);
                }
            }
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface BottomActionMenuAnimType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BottomActionMode {
    }

    public BottomActionMenu(Context context, FrameLayout frameLayout) {
        this.mContext = context;
        initView(frameLayout);
    }

    private void beautyOperateMenuHandle(int i, boolean z) {
        BeautyMenuGroupManager beautyMenuGroupManager = this.mBeautyOperateMenuViewWrapper;
        if (beautyMenuGroupManager != null) {
            beautyMenuGroupManager.setCurrentBeautyMenuType(i);
            this.mBeautyOperateMenuViewWrapper.switchMenu();
            this.mBeautyOperateMenuViewWrapper.setVisibility(0);
            if (z) {
                enterAnim(this.mBeautyOperateMenuViewWrapper.getView());
            }
        }
    }

    private void cameraOperateMenuHandle(boolean z) {
        BeautyMenuGroupManager beautyMenuGroupManager = this.mBeautyOperateMenuViewWrapper;
        if (beautyMenuGroupManager != null) {
            beautyMenuGroupManager.setVisibility(8);
            if (z) {
                exitAnim(this.mBeautyOperateMenuViewWrapper.getView());
            }
        }
    }

    private void changeMenuView() {
        this.beautyOperateMenuView.setVisibility(0);
        enterAnim(this.beautyOperateMenuView);
    }

    private void enterAnim(@NonNull View view) {
        Completable.create(new FolmeAlphaInOnSubscribe(view)).subscribe();
    }

    private void exitAnim(@NonNull View view) {
        FolmeAlphaOutOnSubscribe.directSetResult(view);
    }

    private void hideNearRangeTip() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.hideNearRangeTip();
        }
    }

    private void hideQrCodeTip() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.hideQrCodeTip();
        }
    }

    private void initView(FrameLayout frameLayout) {
        this.mContainerView = frameLayout;
        switchMenuMode(0, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0085 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void animateShineBeauty(boolean z) {
        char c;
        int childCount = this.beautyOperateMenuView.getChildCount();
        int i = 0;
        boolean z2 = false;
        while (i < childCount) {
            ColorActivateTextView colorActivateTextView = (ColorActivateTextView) this.beautyOperateMenuView.getChildAt(i);
            ComponentDataItem componentDataItem = (ComponentDataItem) colorActivateTextView.getTag();
            if (componentDataItem != null) {
                String str = componentDataItem.mValue;
                int hashCode = str.hashCode();
                if (hashCode != 49) {
                    if (hashCode != 1572) {
                        switch (hashCode) {
                            case 51:
                                if (str.equals("3")) {
                                    c = 0;
                                    break;
                                }
                            case 52:
                                if (str.equals("4")) {
                                    c = 1;
                                    break;
                                }
                            case 53:
                                if (str.equals("5")) {
                                    c = 3;
                                    break;
                                }
                            case 54:
                                if (str.equals("6")) {
                                    c = 4;
                                    break;
                                }
                            case 55:
                                if (str.equals("7")) {
                                    c = 6;
                                    break;
                                }
                        }
                    } else if (str.equals("15")) {
                        c = 2;
                        switch (c) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                colorActivateTextView.setVisibility(z ? 8 : 0);
                                z2 = true;
                                break;
                        }
                        i++;
                    }
                } else if (str.equals("1")) {
                    c = 5;
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            break;
                    }
                    i++;
                }
                c = 65535;
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                }
                i++;
            } else {
                return;
            }
        }
        if (z2) {
            for (int i2 = 0; i2 < childCount; i2++) {
                ColorActivateTextView colorActivateTextView2 = (ColorActivateTextView) this.beautyOperateMenuView.getChildAt(i2);
                if (colorActivateTextView2.getVisibility() == 0) {
                    Completable.create(new AlphaInOnSubscribe(colorActivateTextView2)).subscribe();
                }
            }
        }
    }

    public void bottomMenuAnimate(int i, int i2) {
        if (i == 1) {
            if (160 == i2) {
                this.mBeautyOperateMenuViewWrapper.animateExpanding(true);
            } else if (161 == i2) {
                this.mBeautyOperateMenuViewWrapper.animateExpanding(false);
            }
        }
    }

    public void clearBottomMenu() {
        LinearLayout linearLayout = this.beautyOperateMenuView;
        if (linearLayout != null && linearLayout.getVisibility() == 0) {
            FolmeAlphaOutOnSubscribe.directSetGone(this.beautyOperateMenuView);
        }
    }

    public void expandAIWatermark(ComponentRunningAIWatermark componentRunningAIWatermark, int i) {
        List items = componentRunningAIWatermark.getItems();
        String currentType = componentRunningAIWatermark.getCurrentType();
        this.beautyOperateMenuView.removeAllViews();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        boolean z = items.size() > 1;
        for (int i2 = 0; i2 < items.size(); i2++) {
            ComponentDataItem componentDataItem = (ComponentDataItem) items.get(i2);
            ColorActivateTextView colorActivateTextView = (ColorActivateTextView) from.inflate(R.layout.watermark_menu_select_item, this.mContainerView, false);
            colorActivateTextView.setNormalCor(this.mContext.getColor(R.color.mode_name_color));
            colorActivateTextView.setActivateColor(ColorConstant.COLOR_COMMON_SELECTED);
            colorActivateTextView.setText(this.mContext.getString(componentDataItem.mDisplayNameRes));
            colorActivateTextView.setTag(componentDataItem);
            if (z) {
                colorActivateTextView.setOnClickListener(this.mListener);
                if (TextUtils.equals(currentType, componentDataItem.mValue)) {
                    this.mLastSelectedView = colorActivateTextView;
                    colorActivateTextView.setActivated(true);
                }
            }
            this.beautyOperateMenuView.addView(colorActivateTextView);
        }
        changeMenuView();
        hideQrCodeTip();
        hideNearRangeTip();
    }

    public void expandMasterFilter(ComponentRunningMasterFilter componentRunningMasterFilter) {
        this.beautyOperateMenuView.removeAllViews();
        ColorActivateTextView colorActivateTextView = (ColorActivateTextView) LayoutInflater.from(this.mContext).inflate(R.layout.beauty_menu_select_item, this.mContainerView, false);
        colorActivateTextView.setNormalCor(this.mContext.getColor(R.color.mode_name_color));
        colorActivateTextView.setText(this.mContext.getString(componentRunningMasterFilter.getDisplayTitleString()));
        this.beautyOperateMenuView.addView(colorActivateTextView);
        changeMenuView();
        hideQrCodeTip();
        hideNearRangeTip();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void expandShine(ComponentRunningShine componentRunningShine, int i) {
        boolean z;
        int size;
        int i2;
        int i3;
        int i4;
        int intValue;
        char c;
        List items = componentRunningShine.getItems();
        String currentType = componentRunningShine.getCurrentType();
        this.beautyOperateMenuView.removeAllViews();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        boolean z2 = false;
        if (!componentRunningShine.isSmoothDependBeautyVersion()) {
            if (BeautyConstant.LEVEL_CLOSE.equals(CameraSettings.getFaceBeautifyLevel())) {
                z = true;
                size = items.size();
                boolean z3 = size <= 1;
                int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                ArrayList arrayList = new ArrayList(size);
                i2 = 0;
                while (i2 < size) {
                    ComponentDataItem componentDataItem = (ComponentDataItem) items.get(i2);
                    ColorActivateTextView colorActivateTextView = (ColorActivateTextView) from.inflate(R.layout.beauty_menu_select_item, this.mContainerView, z2);
                    colorActivateTextView.setNormalCor(this.mContext.getColor(R.color.mode_name_color));
                    colorActivateTextView.setText(this.mContext.getString(componentDataItem.mDisplayNameRes));
                    colorActivateTextView.setTag(componentDataItem);
                    if (z3) {
                        colorActivateTextView.setOnClickListener(this);
                        if (currentType.equals(componentDataItem.mValue)) {
                            this.mLastSelectedView = colorActivateTextView;
                            colorActivateTextView.setActivated(true);
                        }
                    }
                    colorActivateTextView.measure(makeMeasureSpec, makeMeasureSpec);
                    arrayList.add(Integer.valueOf(colorActivateTextView.getMeasuredWidth()));
                    this.beautyOperateMenuView.addView(colorActivateTextView);
                    if (z) {
                        String str = componentDataItem.mValue;
                        int hashCode = str.hashCode();
                        if (hashCode != 1572) {
                            switch (hashCode) {
                                case 51:
                                    if (str.equals("3")) {
                                        c = 0;
                                        break;
                                    }
                                case 52:
                                    if (str.equals("4")) {
                                        c = 1;
                                        break;
                                    }
                                case 53:
                                    if (str.equals("5")) {
                                        c = 3;
                                        break;
                                    }
                                case 54:
                                    if (str.equals("6")) {
                                        c = 4;
                                        break;
                                    }
                            }
                        } else if (str.equals("15")) {
                            c = 2;
                            if (c != 0 || c == 1 || c == 2 || c == 3 || c == 4) {
                                colorActivateTextView.setVisibility(8);
                            }
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        colorActivateTextView.setVisibility(8);
                    }
                    i2++;
                    z2 = false;
                }
                if (size != 2) {
                    intValue = ((Integer) arrayList.get(1)).intValue();
                    i4 = 0;
                } else {
                    i4 = 0;
                    if (size == 3) {
                        intValue = ((Integer) arrayList.get(2)).intValue();
                    } else {
                        i3 = 0;
                        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.beautyOperateMenuView.getLayoutParams();
                        if (i3 > 0) {
                            marginLayoutParams.setMarginStart(i3);
                        } else {
                            marginLayoutParams.setMarginStart(i4);
                            i4 = -i3;
                        }
                        marginLayoutParams.setMarginEnd(i4);
                        this.beautyOperateMenuView.setLayoutParams(marginLayoutParams);
                        changeMenuView();
                        hideQrCodeTip();
                        hideNearRangeTip();
                    }
                }
                i3 = intValue - ((Integer) arrayList.get(i4)).intValue();
                MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.beautyOperateMenuView.getLayoutParams();
                if (i3 > 0) {
                }
                marginLayoutParams2.setMarginEnd(i4);
                this.beautyOperateMenuView.setLayoutParams(marginLayoutParams2);
                changeMenuView();
                hideQrCodeTip();
                hideNearRangeTip();
            }
        }
        z = false;
        size = items.size();
        if (size <= 1) {
        }
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, 0);
        ArrayList arrayList2 = new ArrayList(size);
        i2 = 0;
        while (i2 < size) {
        }
        if (size != 2) {
        }
        i3 = intValue - ((Integer) arrayList2.get(i4)).intValue();
        MarginLayoutParams marginLayoutParams22 = (MarginLayoutParams) this.beautyOperateMenuView.getLayoutParams();
        if (i3 > 0) {
        }
        marginLayoutParams22.setMarginEnd(i4);
        this.beautyOperateMenuView.setLayoutParams(marginLayoutParams22);
        changeMenuView();
        hideQrCodeTip();
        hideNearRangeTip();
    }

    public SparseArray getMenuData() {
        return this.mBeautyOperateMenuViewWrapper.getBottomMenu().getMenuData();
    }

    public View getView() {
        return this.mContainerView;
    }

    public void initBeautyMenuView(int i) {
        if (this.mBeautyOperateMenuViewWrapper == null) {
            this.beautyOperateMenuView = (LinearLayout) this.mContainerView.findViewById(R.id.beauty_operate_menu);
            this.beautyOperateMenuView.setVisibility(8);
            this.mBeautyOperateMenuViewWrapper = new BeautyMenuGroupManager(this.mContext, this.beautyOperateMenuView, i);
        }
    }

    public boolean isBottomMenuVisible() {
        LinearLayout linearLayout = this.beautyOperateMenuView;
        return linearLayout != null && linearLayout.getVisibility() == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e9, code lost:
        if (com.android.camera.module.ModuleManager.isMiLiveModule() != false) goto L_0x0108;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        HashMap hashMap;
        ColorActivateTextView colorActivateTextView = this.mLastSelectedView;
        if (colorActivateTextView != null) {
            if (colorActivateTextView != view) {
                colorActivateTextView.setActivated(false);
                this.mLastSelectedView = (ColorActivateTextView) view;
                this.mLastSelectedView.setActivated(true);
            } else {
                return;
            }
        }
        ComponentDataItem componentDataItem = (ComponentDataItem) view.getTag();
        if (componentDataItem != null) {
            String str = componentDataItem.mValue;
            String string = componentDataItem.mDisplayNameRes > 0 ? view.getContext().getString(componentDataItem.mDisplayNameRes) : "";
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClick: shineType=");
            sb.append(str);
            sb.append(", name=");
            sb.append(string);
            Log.u(str2, sb.toString());
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (miBeautyProtocol != null) {
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != 50) {
                    if (hashCode != 1571) {
                        if (hashCode != 54) {
                            if (hashCode != 55) {
                                if (hashCode != 1567) {
                                    if (hashCode == 1568 && str.equals("11")) {
                                        c = 2;
                                    }
                                } else if (str.equals("10")) {
                                    c = 3;
                                }
                            } else if (str.equals("7")) {
                                c = 1;
                            }
                        } else if (str.equals("6")) {
                            c = 4;
                        }
                    } else if (str.equals("14")) {
                        c = 5;
                    }
                } else if (str.equals("2")) {
                    c = 0;
                }
                String str3 = BeautyAttr.VALUE_BEAUTY_BOTTOM_TAB;
                String str4 = BeautyAttr.KEY_BEAUTY_CLICK;
                String str5 = BaseEvent.OPERATE_STATE;
                if (c != 0) {
                    String str6 = MiLive.VALUE_MI_LIVE_CLICK_FILTER;
                    if (c != 1) {
                        if (c != 2) {
                            if (c != 3) {
                                if (c == 4) {
                                    hashMap = new HashMap();
                                } else if (c == 5) {
                                    hashMap = new HashMap();
                                    str3 = BeautyAttr.VALUE_BOKEH_BOTTOM_TAB;
                                }
                            } else if (!ModuleManager.isLiveModule()) {
                            }
                            miBeautyProtocol.switchShineType(str, true);
                        } else if (!ModuleManager.isLiveModule()) {
                            if (ModuleManager.isMiLiveModule()) {
                                CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_BEAUTY);
                            }
                            miBeautyProtocol.switchShineType(str, true);
                        }
                        CameraStatUtils.trackLiveBeautyClick(str);
                        miBeautyProtocol.switchShineType(str, true);
                    } else if (!ModuleManager.isMiLiveModule()) {
                        hashMap = new HashMap();
                        str3 = FilterAttr.VALUE_FILTER_BOTTOM_TAB;
                    }
                    CameraStatUtils.trackMiLiveClick(str6);
                    miBeautyProtocol.switchShineType(str, true);
                } else {
                    hashMap = new HashMap();
                }
                hashMap.put(str5, str3);
                MistatsWrapper.mistatEvent(str4, hashMap);
                miBeautyProtocol.switchShineType(str, true);
            }
        }
    }

    public void switchFastMotion(ComponentRunningFastMotion componentRunningFastMotion) {
        this.beautyOperateMenuView.removeAllViews();
        List items = componentRunningFastMotion.getItems();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        boolean z = items.size() > 1;
        for (int i = 0; i < items.size(); i++) {
            ComponentDataItem componentDataItem = (ComponentDataItem) items.get(i);
            ColorActivateTextView colorActivateTextView = (ColorActivateTextView) from.inflate(R.layout.beauty_menu_select_item, this.mContainerView, false);
            colorActivateTextView.setNormalCor(this.mContext.getColor(R.color.mode_name_color));
            colorActivateTextView.setText(this.mContext.getString(componentDataItem.mDisplayNameRes));
            colorActivateTextView.setTag(componentDataItem);
            if (z) {
                colorActivateTextView.setOnClickListener(this.mFastMotionListener);
                if (TextUtils.equals(componentRunningFastMotion.getCurrentType(), componentDataItem.mValue)) {
                    this.mLastSelectedView = colorActivateTextView;
                    colorActivateTextView.setActivated(true);
                }
            }
            this.beautyOperateMenuView.addView(colorActivateTextView);
        }
        changeMenuView();
        hideQrCodeTip();
        hideNearRangeTip();
    }

    public void switchFastMotionPro(ComponentRunningFastMotionPro componentRunningFastMotionPro) {
        this.beautyOperateMenuView.removeAllViews();
        List items = componentRunningFastMotionPro.getItems();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        for (int i = 0; i < items.size(); i++) {
            ComponentDataItem componentDataItem = (ComponentDataItem) items.get(i);
            ColorActivateTextView colorActivateTextView = (ColorActivateTextView) from.inflate(R.layout.beauty_menu_select_item, this.mContainerView, false);
            colorActivateTextView.setNormalCor(this.mContext.getColor(R.color.mode_name_color));
            colorActivateTextView.setText(this.mContext.getString(componentDataItem.mDisplayNameRes));
            colorActivateTextView.setTag(componentDataItem);
            this.beautyOperateMenuView.addView(colorActivateTextView);
        }
        changeMenuView();
        hideQrCodeTip();
        hideNearRangeTip();
    }

    public void switchMenuMode(int i, int i2, boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        if (i != 0) {
            if (i == 1) {
                str3 = TAG;
                str4 = "switch menu mode:beauty_operate";
            } else if (i == 2) {
                str3 = TAG;
                str4 = "switch menu mode:live_operate";
            } else if (i == 3) {
                str3 = TAG;
                str4 = "switch menu mode:kaleidoscope_operate";
            } else if (i != 4) {
                str = TAG;
                str2 = "default switch menu mode:camera_operate";
            } else {
                str3 = TAG;
                str4 = "switch menu mode:MIMOJI_operate";
            }
            Log.i(str3, str4);
            beautyOperateMenuHandle(i2, z);
            return;
        }
        str = TAG;
        str2 = "switch menu mode:camera_operate";
        Log.i(str, str2);
        cameraOperateMenuHandle(z);
    }

    public void switchMenuMode(int i, boolean z) {
        switchMenuMode(i, 161, z);
    }
}
