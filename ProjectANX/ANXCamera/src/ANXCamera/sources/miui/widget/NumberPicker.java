package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.util.SimpleNumberFormatter;
import com.miui.internal.util.async.WorkerThreads;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import miui.util.AppConstants;
import miui.util.ArraySet;
import miui.util.HapticFeedbackUtil;
import miui.util.ViewUtils;
import miui.view.MiuiHapticFeedbackConstants;

public class NumberPicker extends LinearLayout {
    private static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.numberpicker_layout;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    /* access modifiers changed from: private */
    public static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int MAX_HEIGHT = 202;
    private static final int[] PRESSED_STATE_SET = {16842919};
    private static final float SELECTION_DIVIDERS_DISTANCE = 45.0f;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
    static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final String SOUND_PLAY_THREAD = "NumberPicker_sound_play";
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;
    static final Formatter TWO_DIGIT_FORMATTER = new NumberFormatter(2);
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private static final AtomicInteger sIdGenerator = new AtomicInteger(0);
    private int MARGIN_LABEL_LEFT;
    private int MARGIN_LABEL_TOP;
    private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
    private final Scroller mAdjustScroller;
    private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
    /* access modifiers changed from: private */
    public int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private final boolean mComputeMaxWidth;
    private int mCurrentScrollOffset;
    /* access modifiers changed from: private */
    public boolean mDecrementVirtualButtonPressed;
    private float mDisplayedMaxTextWidth;
    /* access modifiers changed from: private */
    public String[] mDisplayedValues;
    private final Scroller mFlingScroller;
    private Formatter mFormatter;
    private final boolean mHasSelectorWheel;
    private final int mId;
    /* access modifiers changed from: private */
    public boolean mIncrementVirtualButtonPressed;
    /* access modifiers changed from: private */
    public boolean mIngonreMoveEvents;
    private int mInitialScrollOffset;
    /* access modifiers changed from: private */
    public final EditText mInputText;
    private CharSequence mLabel;
    private Paint mLabelPaint;
    private int mLabelTextColor;
    private int mLabelTextSize;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastHandledDownDpadKeyCode;
    private int mLastHoveredChildVirtualViewId;
    /* access modifiers changed from: private */
    public long mLongPressUpdateInterval;
    private float mMaxFlingSpeedFactor;
    private final int mMaxHeight;
    /* access modifiers changed from: private */
    public int mMaxValue;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private final int mMinHeight;
    /* access modifiers changed from: private */
    public int mMinValue;
    private final int mMinWidth;
    private int mMinimumFlingVelocity;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private final PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private int mScrollState;
    /* access modifiers changed from: private */
    public final int mSelectionDividerHeight;
    private final int mSelectionDividersDistance;
    private int mSelectorElementHeight;
    private final SparseArray mSelectorIndexToStringCache;
    private final int[] mSelectorIndices;
    private int mSelectorTextGapHeight;
    private final Paint mSelectorWheelPaint;
    private SetSelectionCommand mSetSelectionCommand;
    private boolean mShowSoftInputOnTap;
    private SoundPlayHandler mSoundPlayHandler;
    private int mTextColorHilight;
    private int mTextColorHint;
    private int mTextPadding;
    private final int mTextSize;
    private int mTextSizeHighlight;
    private int mTextSizeHint;
    /* access modifiers changed from: private */
    public int mTopSelectionDividerTop;
    private int mTouchSlop;
    private String mUpdateText;
    /* access modifiers changed from: private */
    public int mValue;
    private VelocityTracker mVelocityTracker;
    /* access modifiers changed from: private */
    public boolean mWrapSelectorWheel;

    class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider {
        private static final int UNDEFINED = Integer.MIN_VALUE;
        private static final int VIRTUAL_VIEW_ID_DECREMENT = 3;
        private static final int VIRTUAL_VIEW_ID_INCREMENT = 1;
        private static final int VIRTUAL_VIEW_ID_INPUT = 2;
        private int mAccessibilityFocusedView = Integer.MIN_VALUE;
        private final int[] mTempArray = new int[2];
        private final Rect mTempRect = new Rect();

        AccessibilityNodeProviderImpl() {
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int i, int i2, int i3, int i4) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setClassName(NumberPicker.class.getName());
            obtain.setPackageName(NumberPicker.this.getContext().getPackageName());
            obtain.setSource(NumberPicker.this);
            if (hasVirtualDecrementButton()) {
                obtain.addChild(NumberPicker.this, 3);
            }
            obtain.addChild(NumberPicker.this, 2);
            if (hasVirtualIncrementButton()) {
                obtain.addChild(NumberPicker.this, 1);
            }
            obtain.setParent((View) NumberPicker.this.getParentForAccessibility());
            obtain.setEnabled(NumberPicker.this.isEnabled());
            obtain.setScrollable(true);
            Rect rect = this.mTempRect;
            rect.set(i, i2, i3, i4);
            obtain.setBoundsInParent(rect);
            obtain.setVisibleToUser(NumberPicker.this.getVisibility() == 0);
            int[] iArr = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            obtain.setBoundsInScreen(rect);
            if (this.mAccessibilityFocusedView != -1) {
                obtain.addAction(64);
            }
            if (this.mAccessibilityFocusedView == -1) {
                obtain.addAction(128);
            }
            if (NumberPicker.this.isEnabled()) {
                if (NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() < NumberPicker.this.getMaxValue()) {
                    obtain.addAction(4096);
                }
                if (NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() > NumberPicker.this.getMinValue()) {
                    obtain.addAction(8192);
                }
            }
            return obtain;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int i, String str, int i2, int i3, int i4, int i5) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setClassName(Button.class.getName());
            obtain.setPackageName(NumberPicker.this.getContext().getPackageName());
            obtain.setSource(NumberPicker.this, i);
            obtain.setParent(NumberPicker.this);
            obtain.setText(str);
            obtain.setClickable(true);
            obtain.setLongClickable(true);
            obtain.setEnabled(NumberPicker.this.isEnabled());
            Rect rect = this.mTempRect;
            rect.set(i2, i3, i4, i5);
            obtain.setVisibleToUser(NumberPicker.this.getVisibility() == 0);
            obtain.setBoundsInParent(rect);
            int[] iArr = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            obtain.setBoundsInScreen(rect);
            if (this.mAccessibilityFocusedView != i) {
                obtain.addAction(64);
            }
            if (this.mAccessibilityFocusedView == i) {
                obtain.addAction(128);
            }
            if (NumberPicker.this.isEnabled()) {
                obtain.addAction(16);
            }
            return obtain;
        }

        private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText(int i, int i2, int i3, int i4) {
            AccessibilityNodeInfo createAccessibilityNodeInfo = NumberPicker.this.mInputText.createAccessibilityNodeInfo();
            createAccessibilityNodeInfo.setSource(NumberPicker.this, 2);
            if (this.mAccessibilityFocusedView != 2) {
                createAccessibilityNodeInfo.addAction(64);
            }
            if (this.mAccessibilityFocusedView == 2) {
                createAccessibilityNodeInfo.addAction(128);
            }
            Rect rect = this.mTempRect;
            rect.set(i, i2, i3, i4);
            createAccessibilityNodeInfo.setVisibleToUser(NumberPicker.this.getVisibility() == 0);
            createAccessibilityNodeInfo.setBoundsInParent(rect);
            int[] iArr = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            createAccessibilityNodeInfo.setBoundsInScreen(rect);
            return createAccessibilityNodeInfo;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:21:0x006c, code lost:
            if (r4.toString().toLowerCase().contains(r3) != false) goto L_0x0048;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void findAccessibilityNodeInfosByTextInChild(String str, int i, List list) {
            if (i != 1) {
                if (i == 2) {
                    Editable text = NumberPicker.this.mInputText.getText();
                    if (TextUtils.isEmpty(text) || !text.toString().toLowerCase().contains(str)) {
                        Editable text2 = NumberPicker.this.mInputText.getText();
                        if (!TextUtils.isEmpty(text2)) {
                        }
                    }
                    list.add(createAccessibilityNodeInfo(2));
                    return;
                } else if (i == 3) {
                    String virtualDecrementButtonText = getVirtualDecrementButtonText();
                    if (!TextUtils.isEmpty(virtualDecrementButtonText) && virtualDecrementButtonText.toString().toLowerCase().contains(str)) {
                        list.add(createAccessibilityNodeInfo(3));
                    }
                    return;
                }
                return;
            }
            String virtualIncrementButtonText = getVirtualIncrementButtonText();
            if (!TextUtils.isEmpty(virtualIncrementButtonText) && virtualIncrementButtonText.toString().toLowerCase().contains(str)) {
                list.add(createAccessibilityNodeInfo(1));
            }
        }

        private String getVirtualDecrementButtonText() {
            int access$1900 = NumberPicker.this.mValue - 1;
            if (NumberPicker.this.mWrapSelectorWheel) {
                access$1900 = NumberPicker.this.getWrappedSelectorIndex(access$1900);
            }
            if (access$1900 < NumberPicker.this.mMinValue) {
                return null;
            }
            return NumberPicker.this.mDisplayedValues == null ? NumberPicker.this.formatNumber(access$1900) : NumberPicker.this.mDisplayedValues[access$1900 - NumberPicker.this.mMinValue];
        }

        private String getVirtualIncrementButtonText() {
            int access$1900 = NumberPicker.this.mValue + 1;
            if (NumberPicker.this.mWrapSelectorWheel) {
                access$1900 = NumberPicker.this.getWrappedSelectorIndex(access$1900);
            }
            if (access$1900 > NumberPicker.this.mMaxValue) {
                return null;
            }
            return NumberPicker.this.mDisplayedValues == null ? NumberPicker.this.formatNumber(access$1900) : NumberPicker.this.mDisplayedValues[access$1900 - NumberPicker.this.mMinValue];
        }

        private boolean hasVirtualDecrementButton() {
            return NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() > NumberPicker.this.getMinValue();
        }

        private boolean hasVirtualIncrementButton() {
            return NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() < NumberPicker.this.getMaxValue();
        }

        private void sendAccessibilityEventForVirtualButton(int i, int i2, String str) {
            if (((AccessibilityManager) NumberPicker.this.getContext().getSystemService("accessibility")).isEnabled()) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
                obtain.setClassName(Button.class.getName());
                obtain.setPackageName(NumberPicker.this.getContext().getPackageName());
                obtain.getText().add(str);
                obtain.setEnabled(NumberPicker.this.isEnabled());
                obtain.setSource(NumberPicker.this, i);
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.requestSendAccessibilityEvent(numberPicker, obtain);
            }
        }

        private void sendAccessibilityEventForVirtualText(int i) {
            if (((AccessibilityManager) NumberPicker.this.getContext().getSystemService("accessibility")).isEnabled()) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
                NumberPicker.this.mInputText.onInitializeAccessibilityEvent(obtain);
                NumberPicker.this.mInputText.onPopulateAccessibilityEvent(obtain);
                obtain.setSource(NumberPicker.this, 2);
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.requestSendAccessibilityEvent(numberPicker, obtain);
            }
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            if (i == -1) {
                return createAccessibilityNodeInfoForNumberPicker(NumberPicker.this.getScrollX(), NumberPicker.this.getScrollY(), NumberPicker.this.getScrollX() + (NumberPicker.this.getRight() - NumberPicker.this.getLeft()), NumberPicker.this.getScrollY() + (NumberPicker.this.getBottom() - NumberPicker.this.getTop()));
            }
            if (i == 1) {
                return createAccessibilityNodeInfoForVirtualButton(1, getVirtualIncrementButtonText(), NumberPicker.this.getScrollX(), NumberPicker.this.mBottomSelectionDividerBottom - NumberPicker.this.mSelectionDividerHeight, NumberPicker.this.getScrollX() + (NumberPicker.this.getRight() - NumberPicker.this.getLeft()), NumberPicker.this.getScrollY() + (NumberPicker.this.getBottom() - NumberPicker.this.getTop()));
            } else if (i == 2) {
                return createAccessibiltyNodeInfoForInputText(NumberPicker.this.getScrollX(), NumberPicker.this.mTopSelectionDividerTop + NumberPicker.this.mSelectionDividerHeight, NumberPicker.this.getScrollX() + (NumberPicker.this.getRight() - NumberPicker.this.getLeft()), NumberPicker.this.mBottomSelectionDividerBottom - NumberPicker.this.mSelectionDividerHeight);
            } else {
                if (i != 3) {
                    return super.createAccessibilityNodeInfo(i);
                }
                return createAccessibilityNodeInfoForVirtualButton(3, getVirtualDecrementButtonText(), NumberPicker.this.getScrollX(), NumberPicker.this.getScrollY(), NumberPicker.this.getScrollX() + (NumberPicker.this.getRight() - NumberPicker.this.getLeft()), NumberPicker.this.mTopSelectionDividerTop + NumberPicker.this.mSelectionDividerHeight);
            }
        }

        public List findAccessibilityNodeInfosByText(String str, int i) {
            if (TextUtils.isEmpty(str)) {
                return Collections.emptyList();
            }
            String lowerCase = str.toLowerCase();
            ArrayList arrayList = new ArrayList();
            if (i == -1) {
                findAccessibilityNodeInfosByTextInChild(lowerCase, 3, arrayList);
                findAccessibilityNodeInfosByTextInChild(lowerCase, 2, arrayList);
                findAccessibilityNodeInfosByTextInChild(lowerCase, 1, arrayList);
                return arrayList;
            } else if (i != 1 && i != 2 && i != 3) {
                return super.findAccessibilityNodeInfosByText(str, i);
            } else {
                findAccessibilityNodeInfosByTextInChild(lowerCase, i, arrayList);
                return arrayList;
            }
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            boolean z = false;
            if (i != -1) {
                if (i != 1) {
                    if (i != 2) {
                        if (i == 3) {
                            if (i2 != 16) {
                                if (i2 != 64) {
                                    if (i2 != 128 || this.mAccessibilityFocusedView != i) {
                                        return false;
                                    }
                                    this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                                    sendAccessibilityEventForVirtualView(i, 65536);
                                    NumberPicker numberPicker = NumberPicker.this;
                                    numberPicker.invalidate(0, 0, numberPicker.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                                    return true;
                                } else if (this.mAccessibilityFocusedView == i) {
                                    return false;
                                } else {
                                    this.mAccessibilityFocusedView = i;
                                    sendAccessibilityEventForVirtualView(i, 32768);
                                    NumberPicker numberPicker2 = NumberPicker.this;
                                    numberPicker2.invalidate(0, 0, numberPicker2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                                    return true;
                                }
                            } else if (!NumberPicker.this.isEnabled()) {
                                return false;
                            } else {
                                if (i == 1) {
                                    z = true;
                                }
                                NumberPicker.this.changeValueByOne(z);
                                sendAccessibilityEventForVirtualView(i, 1);
                                return true;
                            }
                        }
                    } else if (i2 != 1) {
                        if (i2 != 2) {
                            if (i2 == 16) {
                                return NumberPicker.this.isEnabled();
                            }
                            if (i2 != 64) {
                                if (i2 != 128) {
                                    return NumberPicker.this.mInputText.performAccessibilityAction(i2, bundle);
                                }
                                if (this.mAccessibilityFocusedView != i) {
                                    return false;
                                }
                                this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                                sendAccessibilityEventForVirtualView(i, 65536);
                                NumberPicker.this.mInputText.invalidate();
                                return true;
                            } else if (this.mAccessibilityFocusedView == i) {
                                return false;
                            } else {
                                this.mAccessibilityFocusedView = i;
                                sendAccessibilityEventForVirtualView(i, 32768);
                                NumberPicker.this.mInputText.invalidate();
                                return true;
                            }
                        } else if (!NumberPicker.this.isEnabled() || !NumberPicker.this.mInputText.isFocused()) {
                            return false;
                        } else {
                            NumberPicker.this.mInputText.clearFocus();
                            return true;
                        }
                    } else if (!NumberPicker.this.isEnabled() || NumberPicker.this.mInputText.isFocused()) {
                        return false;
                    } else {
                        return NumberPicker.this.mInputText.requestFocus();
                    }
                } else if (i2 != 16) {
                    if (i2 != 64) {
                        if (i2 != 128 || this.mAccessibilityFocusedView != i) {
                            return false;
                        }
                        this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                        sendAccessibilityEventForVirtualView(i, 65536);
                        NumberPicker numberPicker3 = NumberPicker.this;
                        numberPicker3.invalidate(0, numberPicker3.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                        return true;
                    } else if (this.mAccessibilityFocusedView == i) {
                        return false;
                    } else {
                        this.mAccessibilityFocusedView = i;
                        sendAccessibilityEventForVirtualView(i, 32768);
                        NumberPicker numberPicker4 = NumberPicker.this;
                        numberPicker4.invalidate(0, numberPicker4.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                        return true;
                    }
                } else if (!NumberPicker.this.isEnabled()) {
                    return false;
                } else {
                    NumberPicker.this.changeValueByOne(true);
                    sendAccessibilityEventForVirtualView(i, 1);
                    return true;
                }
            } else if (i2 != 64) {
                if (i2 != 128) {
                    if (i2 != 4096) {
                        if (i2 == 8192) {
                            if (!NumberPicker.this.isEnabled() || (!NumberPicker.this.getWrapSelectorWheel() && NumberPicker.this.getValue() <= NumberPicker.this.getMinValue())) {
                                return false;
                            }
                            NumberPicker.this.changeValueByOne(false);
                            return true;
                        }
                    } else if (!NumberPicker.this.isEnabled() || (!NumberPicker.this.getWrapSelectorWheel() && NumberPicker.this.getValue() >= NumberPicker.this.getMaxValue())) {
                        return false;
                    } else {
                        NumberPicker.this.changeValueByOne(true);
                        return true;
                    }
                } else if (this.mAccessibilityFocusedView != i) {
                    return false;
                } else {
                    this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                    return true;
                }
            } else if (this.mAccessibilityFocusedView == i) {
                return false;
            } else {
                this.mAccessibilityFocusedView = i;
                return true;
            }
            return super.performAction(i, i2, bundle);
        }

        public void sendAccessibilityEventForVirtualView(int i, int i2) {
            String str;
            if (i != 1) {
                if (i == 2) {
                    sendAccessibilityEventForVirtualText(i2);
                    return;
                } else if (i == 3 && hasVirtualDecrementButton()) {
                    str = getVirtualDecrementButtonText();
                } else {
                    return;
                }
            } else if (hasVirtualIncrementButton()) {
                str = getVirtualIncrementButtonText();
            } else {
                return;
            }
            sendAccessibilityEventForVirtualButton(i, i2, str);
        }
    }

    class BeginSoftInputOnLongPressCommand implements Runnable {
        BeginSoftInputOnLongPressCommand() {
        }

        public void run() {
            NumberPicker.this.mIngonreMoveEvents = true;
        }
    }

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        /* access modifiers changed from: private */
        public void setStep(boolean z) {
            this.mIncrement = z;
        }

        public void run() {
            NumberPicker.this.changeValueByOne(this.mIncrement);
            NumberPicker numberPicker = NumberPicker.this;
            numberPicker.postDelayed(this, numberPicker.mLongPressUpdateInterval);
        }
    }

    public class CustomEditText extends EditText {
        public CustomEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public void onEditorAction(int i) {
            super.onEditorAction(i);
            if (i == 6) {
                clearFocus();
            }
        }
    }

    public interface Formatter {
        String format(int i);
    }

    class InputTextFilter extends NumberKeyListener {
        InputTextFilter() {
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            String[] access$700;
            String str = "";
            if (NumberPicker.this.mDisplayedValues == null) {
                CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                if (filter == null) {
                    filter = charSequence.subSequence(i, i2);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(spanned.subSequence(0, i3)));
                sb.append(filter);
                sb.append(spanned.subSequence(i4, spanned.length()));
                String sb2 = sb.toString();
                return str.equals(sb2) ? sb2 : (NumberPicker.this.getSelectedPos(sb2) > NumberPicker.this.mMaxValue || sb2.length() > String.valueOf(NumberPicker.this.mMaxValue).length()) ? str : filter;
            }
            String valueOf = String.valueOf(charSequence.subSequence(i, i2));
            if (TextUtils.isEmpty(valueOf)) {
                return str;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(String.valueOf(spanned.subSequence(0, i3)));
            sb3.append(valueOf);
            sb3.append(spanned.subSequence(i4, spanned.length()));
            String sb4 = sb3.toString();
            String lowerCase = String.valueOf(sb4).toLowerCase();
            for (String str2 : NumberPicker.this.mDisplayedValues) {
                if (str2.toLowerCase().startsWith(lowerCase)) {
                    NumberPicker.this.postSetSelectionCommand(sb4.length(), str2.length());
                    return str2.subSequence(i3, str2.length());
                }
            }
            return str;
        }

        /* access modifiers changed from: protected */
        public char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        public int getInputType() {
            return 1;
        }
    }

    class NumberFormatter implements Formatter {
        private final int iWidth;

        public NumberFormatter() {
            this.iWidth = -1;
        }

        public NumberFormatter(int i) {
            this.iWidth = i;
        }

        public String format(int i) {
            return SimpleNumberFormatter.format(this.iWidth, i);
        }
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScrollStateChange(NumberPicker numberPicker, int i);
    }

    public interface OnValueChangeListener {
        void onValueChange(NumberPicker numberPicker, int i, int i2);
    }

    class PressedStateHelper implements Runnable {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;
        private int mManagedButton;
        private int mMode;

        PressedStateHelper() {
        }

        public void buttonPressDelayed(int i) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = i;
            NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int i) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = i;
            NumberPicker.this.post(this);
        }

        public void cancel() {
            this.mMode = 0;
            this.mManagedButton = 0;
            NumberPicker.this.removeCallbacks(this);
            if (NumberPicker.this.mIncrementVirtualButtonPressed) {
                NumberPicker.this.mIncrementVirtualButtonPressed = false;
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
            }
            if (NumberPicker.this.mDecrementVirtualButtonPressed) {
                NumberPicker.this.mDecrementVirtualButtonPressed = false;
                NumberPicker numberPicker2 = NumberPicker.this;
                numberPicker2.invalidate(0, 0, numberPicker2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
            }
        }

        public void run() {
            int i = this.mMode;
            if (i == 1) {
                int i2 = this.mManagedButton;
                if (i2 == 1) {
                    NumberPicker.this.mIncrementVirtualButtonPressed = true;
                    NumberPicker numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                    return;
                } else if (i2 == 2) {
                    NumberPicker.this.mDecrementVirtualButtonPressed = true;
                } else {
                    return;
                }
            } else if (i == 2) {
                int i3 = this.mManagedButton;
                if (i3 == 1) {
                    if (!NumberPicker.this.mIncrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker.access$1180(NumberPicker.this, 1);
                    NumberPicker numberPicker2 = NumberPicker.this;
                    numberPicker2.invalidate(0, numberPicker2.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                    return;
                } else if (i3 == 2) {
                    if (!NumberPicker.this.mDecrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker.access$1380(NumberPicker.this, 1);
                } else {
                    return;
                }
            } else {
                return;
            }
            NumberPicker numberPicker3 = NumberPicker.this;
            numberPicker3.invalidate(0, 0, numberPicker3.getRight(), NumberPicker.this.mTopSelectionDividerTop);
        }
    }

    class SetSelectionCommand implements Runnable {
        /* access modifiers changed from: private */
        public int mSelectionEnd;
        /* access modifiers changed from: private */
        public int mSelectionStart;

        SetSelectionCommand() {
        }

        public void run() {
            if (this.mSelectionEnd < NumberPicker.this.mInputText.length()) {
                NumberPicker.this.mInputText.setSelection(this.mSelectionStart, this.mSelectionEnd);
            }
        }
    }

    class SoundPlayHandler extends Handler {
        private static final int MSG_INIT = 0;
        private static final int MSG_PLAY = 1;
        private static final int MSG_RELEASE = 2;
        private static final SoundPlayerContainer sPlayerContainer = new SoundPlayerContainer();

        class SoundPlayerContainer {
            private static final long INTERVAL = 50;
            private long mPrevPlayTime;
            private Set mRefs;
            private int mSoundId;
            private SoundPool mSoundPlayer;

            private SoundPlayerContainer() {
                this.mRefs = new ArraySet();
            }

            /* access modifiers changed from: 0000 */
            public void init(int i) {
                if (this.mSoundPlayer == null) {
                    this.mSoundPlayer = new SoundPool(1, 1, 0);
                    this.mSoundId = this.mSoundPlayer.load(AppConstants.getCurrentApplication(), R.raw.numberpicker_value_change, 1);
                }
                this.mRefs.add(Integer.valueOf(i));
            }

            /* access modifiers changed from: 0000 */
            public void play() {
                long currentTimeMillis = System.currentTimeMillis();
                SoundPool soundPool = this.mSoundPlayer;
                if (soundPool != null && currentTimeMillis - this.mPrevPlayTime > INTERVAL) {
                    soundPool.play(this.mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
                    this.mPrevPlayTime = currentTimeMillis;
                }
            }

            /* access modifiers changed from: 0000 */
            public void release(int i) {
                if (this.mRefs.remove(Integer.valueOf(i)) && this.mRefs.isEmpty()) {
                    SoundPool soundPool = this.mSoundPlayer;
                    if (soundPool != null) {
                        soundPool.release();
                        this.mSoundPlayer = null;
                    }
                }
            }
        }

        SoundPlayHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 0) {
                sPlayerContainer.init(message.arg1);
            } else if (i == 1) {
                sPlayerContainer.play();
            } else if (i == 2) {
                sPlayerContainer.release(message.arg1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void init(int i) {
            sendMessage(obtainMessage(0, i, 0));
        }

        /* access modifiers changed from: 0000 */
        public void play() {
            sendMessage(obtainMessage(1));
        }

        /* access modifiers changed from: 0000 */
        public void release(int i) {
            sendMessage(obtainMessage(2, i, 0));
        }

        /* access modifiers changed from: 0000 */
        public void stop() {
            removeMessages(1);
        }
    }

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.numberPickerStyle);
    }

    public NumberPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mId = sIdGenerator.incrementAndGet();
        this.MARGIN_LABEL_LEFT = 1;
        this.MARGIN_LABEL_TOP = 2;
        this.mMaxWidth = 400;
        this.mLongPressUpdateInterval = 300;
        this.mSelectorIndexToStringCache = new SparseArray();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.mMaxFlingSpeedFactor = 1.0f;
        Resources resources = getResources();
        float f = resources.getDisplayMetrics().density;
        this.MARGIN_LABEL_LEFT = resources.getDimensionPixelOffset(R.dimen.number_picker_label_margin_left);
        this.MARGIN_LABEL_TOP = resources.getDimensionPixelOffset(R.dimen.number_picker_label_margin_top);
        parseStyle(attributeSet, i);
        initSoundPlayer();
        this.mHasSelectorWheel = true;
        this.mSelectionDividerHeight = (int) TypedValue.applyDimension(1, 2.0f, resources.getDisplayMetrics());
        this.mSelectionDividersDistance = (int) (SELECTION_DIVIDERS_DISTANCE * f);
        this.mMinHeight = -1;
        this.mMaxHeight = (int) (f * 202.0f);
        int i2 = this.mMinHeight;
        if (i2 != -1) {
            int i3 = this.mMaxHeight;
            if (i3 != -1 && i2 > i3) {
                throw new IllegalArgumentException("minHeight > maxHeight");
            }
        }
        this.mMinWidth = -1;
        this.mMaxWidth = -1;
        int i4 = this.mMinWidth;
        if (i4 != -1) {
            int i5 = this.mMaxWidth;
            if (i5 != -1 && i4 > i5) {
                throw new IllegalArgumentException("minWidth > maxWidth");
            }
        }
        this.mComputeMaxWidth = this.mMaxWidth == -1;
        this.mPressedStateHelper = new PressedStateHelper();
        setWillNotDraw(!this.mHasSelectorWheel);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.numberpicker_layout, this, true);
        this.mInputText = (EditText) findViewById(R.id.numberpicker_input);
        this.mInputText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    NumberPicker.this.mInputText.selectAll();
                    return;
                }
                NumberPicker.this.mInputText.setSelection(0, 0);
                NumberPicker.this.validateInputTextView(view);
            }
        });
        this.mInputText.setFilters(new InputFilter[]{new InputTextFilter()});
        this.mInputText.setRawInputType(2);
        this.mInputText.setImeOptions(6);
        this.mInputText.setVisibility(4);
        this.mInputText.setGravity(GravityCompat.START);
        this.mInputText.setScaleX(0.0f);
        this.mInputText.setSaveEnabled(false);
        EditText editText = this.mInputText;
        editText.setPadding(this.mTextPadding, editText.getPaddingTop(), this.mTextPadding, this.mInputText.getPaddingRight());
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / 8;
        this.mTextSize = (int) this.mInputText.getTextSize();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize((float) this.mTextSizeHighlight);
        paint.setTypeface(this.mInputText.getTypeface());
        paint.setColor(this.mInputText.getTextColors().getColorForState(LinearLayout.ENABLED_STATE_SET, -1));
        this.mSelectorWheelPaint = paint;
        this.mLabelPaint = new Paint();
        this.mLabelPaint.setAntiAlias(true);
        this.mLabelPaint.setFakeBoldText(true);
        this.mLabelPaint.setColor(this.mLabelTextColor);
        this.mLabelPaint.setTextSize((float) this.mLabelTextSize);
        this.mFlingScroller = new Scroller(getContext(), null, true);
        this.mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5f));
        updateInputTextView();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    /* JADX WARNING: type inference failed for: r2v2, types: [byte, boolean] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v2, types: [byte, boolean]
  assigns: [byte]
  uses: [boolean]
  mth insns count: 5
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ boolean access$1180(NumberPicker numberPicker, int i) {
        ? r2 = (byte) (i ^ numberPicker.mIncrementVirtualButtonPressed);
        numberPicker.mIncrementVirtualButtonPressed = r2;
        return r2;
    }

    /* JADX WARNING: type inference failed for: r2v2, types: [byte, boolean] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v2, types: [byte, boolean]
  assigns: [byte]
  uses: [boolean]
  mth insns count: 5
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ boolean access$1380(NumberPicker numberPicker, int i) {
        ? r2 = (byte) (i ^ numberPicker.mDecrementVirtualButtonPressed);
        numberPicker.mDecrementVirtualButtonPressed = r2;
        return r2;
    }

    /* access modifiers changed from: private */
    public void changeValueByOne(boolean z) {
        if (this.mHasSelectorWheel) {
            this.mInputText.setVisibility(4);
            if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
                moveToFinalScrollerPosition(this.mAdjustScroller);
            }
            this.mPreviousScrollerY = 0;
            if (z) {
                this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, 300);
            } else {
                this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, 300);
            }
            invalidate();
            return;
        }
        setValueInternal(z ? this.mValue + 1 : this.mValue - 1, true);
    }

    private void decrementSelectorIndices(int[] iArr) {
        for (int length = iArr.length - 1; length > 0; length--) {
            iArr[length] = iArr[length - 1];
        }
        int i = iArr[1] - 1;
        if (this.mWrapSelectorWheel && i < this.mMinValue) {
            i = this.mMaxValue;
        }
        iArr[0] = i;
        ensureCachedScrollSelectorValue(i);
    }

    private void ensureCachedScrollSelectorValue(int i) {
        String str;
        SparseArray sparseArray = this.mSelectorIndexToStringCache;
        if (((String) sparseArray.get(i)) == null) {
            int i2 = this.mMinValue;
            if (i < i2 || i > this.mMaxValue) {
                str = "";
            } else {
                String[] strArr = this.mDisplayedValues;
                str = strArr != null ? strArr[i - i2] : formatNumber(i);
            }
            sparseArray.put(i, str);
        }
    }

    private boolean ensureScrollWheelAdjusted() {
        int i = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (i == 0) {
            return false;
        }
        this.mPreviousScrollerY = 0;
        int abs = Math.abs(i);
        int i2 = this.mSelectorElementHeight;
        if (abs > i2 / 2) {
            if (i > 0) {
                i2 = -i2;
            }
            i += i2;
        }
        this.mAdjustScroller.startScroll(0, 0, 0, i, 800);
        invalidate();
        return true;
    }

    private void fling(int i) {
        this.mPreviousScrollerY = 0;
        this.mFlingScroller.fling(0, i > 0 ? 0 : Integer.MAX_VALUE, 0, i, 0, 0, 0, Integer.MAX_VALUE);
        invalidate();
    }

    /* access modifiers changed from: private */
    public String formatNumber(int i) {
        Formatter formatter = this.mFormatter;
        return formatter != null ? formatter.format(i) : SimpleNumberFormatter.format(i);
    }

    private int getAlphaGradient(float f, int i, boolean z) {
        if (f >= 1.0f) {
            return i;
        }
        return ((z ? (int) (((-f) * ((float) Color.alpha(i))) + ((float) Color.alpha(i))) : (int) (f * ((float) Color.alpha(i)))) << 24) | (16777215 & i);
    }

    /* access modifiers changed from: private */
    public int getSelectedPos(String str) {
        if (this.mDisplayedValues == null) {
            try {
                r2 = Integer.parseInt(str);
                r2 = r2;
                return r2;
            } catch (NumberFormatException unused) {
                return r2.mMinValue;
            }
        } else {
            for (int i = 0; i < this.mDisplayedValues.length; i++) {
                str = str.toLowerCase();
                if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return this.mMinValue + i;
                }
            }
            this = this;
            this = Integer.parseInt(str);
            r2 = this;
            return this;
        }
    }

    private float getTextSize(float f, int i, int i2) {
        return f >= 1.0f ? (float) i2 : (f * ((float) (i2 - i))) + ((float) i);
    }

    /* access modifiers changed from: private */
    public int getWrappedSelectorIndex(int i) {
        int i2 = this.mMaxValue;
        int i3 = this.mMinValue;
        return i > i2 ? (i3 + ((i - i2) % (i2 - i3))) - 1 : i < i3 ? (i2 - ((i3 - i) % (i2 - i3))) + 1 : i;
    }

    private void incrementSelectorIndices(int[] iArr) {
        int i = 0;
        while (i < iArr.length - 1) {
            int i2 = i + 1;
            iArr[i] = iArr[i2];
            i = i2;
        }
        int i3 = iArr[iArr.length - 2] + 1;
        if (this.mWrapSelectorWheel && i3 > this.mMaxValue) {
            i3 = this.mMinValue;
        }
        iArr[iArr.length - 1] = i3;
        ensureCachedScrollSelectorValue(i3);
    }

    private void initSoundPlayer() {
        if (this.mSoundPlayHandler == null) {
            this.mSoundPlayHandler = new SoundPlayHandler(WorkerThreads.aquireWorker(SOUND_PLAY_THREAD));
            this.mSoundPlayHandler.init(this.mId);
        }
    }

    private void initializeFadingEdges() {
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(((getBottom() - getTop()) - this.mTextSize) / 2);
    }

    private void initializeSelectorWheel() {
        initializeSelectorWheelIndices();
        int[] iArr = this.mSelectorIndices;
        float bottom = (float) ((getBottom() - getTop()) - (iArr.length * this.mTextSize));
        if (bottom < 0.0f) {
            bottom = 0.0f;
        }
        this.mSelectorTextGapHeight = (int) ((bottom / ((float) iArr.length)) + 0.5f);
        this.mSelectorElementHeight = this.mTextSize + this.mSelectorTextGapHeight;
        this.mInitialScrollOffset = (this.mInputText.getBaseline() + this.mInputText.getTop()) - (this.mSelectorElementHeight * 1);
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        updateInputTextView();
    }

    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] iArr = this.mSelectorIndices;
        int value = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int i2 = (i - 1) + value;
            if (this.mWrapSelectorWheel) {
                i2 = getWrappedSelectorIndex(i2);
            }
            iArr[i] = i2;
            ensureCachedScrollSelectorValue(iArr[i]);
        }
    }

    private int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            return MeasureSpec.makeMeasureSpec(Math.min(size, i2), 1073741824);
        }
        if (mode == 0) {
            return MeasureSpec.makeMeasureSpec(i2, 1073741824);
        }
        if (mode == 1073741824) {
            return i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown measure mode: ");
        sb.append(mode);
        throw new IllegalArgumentException(sb.toString());
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int finalY = scroller.getFinalY() - scroller.getCurrY();
        int i = this.mInitialScrollOffset - ((this.mCurrentScrollOffset + finalY) % this.mSelectorElementHeight);
        if (i == 0) {
            return false;
        }
        int abs = Math.abs(i);
        int i2 = this.mSelectorElementHeight;
        if (abs > i2 / 2) {
            i = i > 0 ? i - i2 : i + i2;
        }
        scrollBy(0, finalY + i);
        return true;
    }

    private void notifyChange(int i) {
        sendAccessibilityEvent(4);
        playSound();
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL)) {
            performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL);
        }
        OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(this, i, this.mValue);
        }
    }

    private void onScrollStateChange(int i) {
        if (this.mScrollState != i) {
            if (i == 0) {
                String str = this.mUpdateText;
                if (str != null && !str.equals(this.mInputText.getText().toString())) {
                    this.mInputText.setText(this.mUpdateText);
                }
                this.mUpdateText = null;
                stopSoundPlay();
            }
            this.mScrollState = i;
            OnScrollListener onScrollListener = this.mOnScrollListener;
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChange(this, i);
            }
        }
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }
            onScrollStateChange(0);
        } else if (this.mScrollState != 1) {
            updateInputTextView();
        }
    }

    private void parseStyle(AttributeSet attributeSet, int i) {
        Resources resources = getResources();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.NumberPicker, i, 0);
        this.mLabel = obtainStyledAttributes.getText(R.styleable.NumberPicker_android_text);
        this.mTextSizeHighlight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPicker_textSizeHighlight, resources.getDimensionPixelOffset(R.dimen.number_picker_text_size_highlight_normal));
        this.mTextSizeHint = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPicker_textSizeHint, resources.getDimensionPixelOffset(R.dimen.number_picker_text_size_hint_normal));
        this.mLabelTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPicker_android_labelTextSize, resources.getDimensionPixelOffset(R.dimen.number_picker_label_text_size));
        this.mTextColorHilight = obtainStyledAttributes.getColor(R.styleable.NumberPicker_android_textColorHighlight, resources.getColor(R.color.number_picker_highlight_text_color));
        this.mTextColorHint = obtainStyledAttributes.getColor(R.styleable.NumberPicker_android_textColorHint, resources.getColor(R.color.number_picker_hint_text_color));
        this.mLabelTextColor = obtainStyledAttributes.getColor(R.styleable.NumberPicker_labelTextColor, resources.getColor(R.color.number_picker_label_color));
        this.mTextPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPicker_labelPadding, resources.getDimensionPixelOffset(R.dimen.number_picker_label_padding));
        obtainStyledAttributes.recycle();
    }

    private void playSound() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.play();
        }
    }

    private void postBeginSoftInputOnLongPressCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand == null) {
            this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
        } else {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        postDelayed(this.mBeginSoftInputOnLongPressCommand, (long) ViewConfiguration.getLongPressTimeout());
    }

    private void postChangeCurrentByOneFromLongPress(boolean z, long j) {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(z);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j);
    }

    /* access modifiers changed from: private */
    public void postSetSelectionCommand(int i, int i2) {
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand == null) {
            this.mSetSelectionCommand = new SetSelectionCommand();
        } else {
            removeCallbacks(setSelectionCommand);
        }
        this.mSetSelectionCommand.mSelectionStart = i;
        this.mSetSelectionCommand.mSelectionEnd = i2;
        post(this.mSetSelectionCommand);
    }

    private void refreshWheel() {
        initializeSelectorWheelIndices();
        invalidate();
    }

    private void releaseSoundPlayer() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.release(this.mId);
            this.mSoundPlayHandler = null;
        }
    }

    private void removeAllCallbacks() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand != null) {
            removeCallbacks(setSelectionCommand);
        }
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    private void removeBeginSoftInputCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
    }

    private void removeChangeCurrentByOneFromLongPress() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    private int resolveSizeAndStateRespectingMinSize(int i, int i2, int i3) {
        return i != -1 ? LinearLayout.resolveSizeAndState(Math.max(i, i2), i3, 0) : i2;
    }

    private void setValueInternal(int i, boolean z) {
        int wrappedSelectorIndex = this.mWrapSelectorWheel ? getWrappedSelectorIndex(i) : Math.min(Math.max(i, this.mMinValue), this.mMaxValue);
        int i2 = this.mValue;
        if (i2 != wrappedSelectorIndex) {
            this.mValue = wrappedSelectorIndex;
            updateInputTextView();
            if (z) {
                notifyChange(i2);
            }
            initializeSelectorWheelIndices();
            invalidate();
        }
    }

    private void stopSoundPlay() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.stop();
        }
    }

    private void tryComputeMaxWidth() {
        if (this.mComputeMaxWidth) {
            float f = -1.0f;
            this.mSelectorWheelPaint.setTextSize((float) this.mTextSizeHighlight);
            String[] strArr = this.mDisplayedValues;
            int i = 0;
            if (strArr == null) {
                float f2 = 0.0f;
                while (i < 9) {
                    float measureText = this.mSelectorWheelPaint.measureText(String.valueOf(i));
                    if (measureText > f2) {
                        f2 = measureText;
                    }
                    i++;
                }
                f = (float) ((int) (((float) formatNumber(this.mMaxValue).length()) * f2));
            } else {
                int length = strArr.length;
                while (i < length) {
                    float measureText2 = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i]);
                    if (measureText2 > f) {
                        f = measureText2;
                    }
                    i++;
                }
            }
            this.mDisplayedMaxTextWidth = f;
            float paddingLeft = f + ((float) (this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight())) + ((float) getPaddingLeft()) + ((float) getPaddingRight());
            if (((float) this.mMaxWidth) != paddingLeft) {
                int i2 = this.mMinWidth;
                if (paddingLeft > ((float) i2)) {
                    this.mMaxWidth = (int) paddingLeft;
                } else {
                    this.mMaxWidth = i2;
                }
            }
        }
    }

    private boolean updateInputTextView() {
        String[] strArr = this.mDisplayedValues;
        String formatNumber = strArr == null ? formatNumber(this.mValue) : strArr[this.mValue - this.mMinValue];
        if (TextUtils.isEmpty(formatNumber)) {
            return false;
        }
        if (this.mScrollState != 0) {
            this.mUpdateText = formatNumber;
        } else if (!formatNumber.equals(this.mInputText.getText().toString())) {
            this.mInputText.setText(formatNumber);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void validateInputTextView(View view) {
        String valueOf = String.valueOf(((TextView) view).getText());
        if (TextUtils.isEmpty(valueOf)) {
            updateInputTextView();
        } else {
            setValueInternal(getSelectedPos(valueOf.toString()), true);
        }
    }

    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        int currY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        scrollBy(0, currY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currY;
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (!this.mHasSelectorWheel) {
            return super.dispatchHoverEvent(motionEvent);
        }
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isEnabled()) {
            int y = (int) motionEvent.getY();
            int i = y < this.mTopSelectionDividerTop ? 3 : y > this.mBottomSelectionDividerBottom ? 1 : 2;
            int actionMasked = motionEvent.getActionMasked();
            AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
            if (actionMasked == 7) {
                int i2 = this.mLastHoveredChildVirtualViewId;
                if (!(i2 == i || i2 == -1)) {
                    accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i2, 256);
                }
            } else if (actionMasked != 9) {
                if (actionMasked == 10) {
                    accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i, 256);
                    this.mLastHoveredChildVirtualViewId = -1;
                }
            }
            accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i, 128);
            this.mLastHoveredChildVirtualViewId = i;
            accessibilityNodeProviderImpl.performAction(i, 64, null);
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
        requestFocus();
        r5.mLastHandledDownDpadKeyCode = r0;
        removeAllCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005a, code lost:
        if (r5.mFlingScroller.isFinished() == false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005c, code lost:
        if (r0 != 20) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005e, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0060, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0061, code lost:
        changeValueByOne(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0064, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19 || keyCode == 20) {
            if (this.mHasSelectorWheel) {
                int action = keyEvent.getAction();
                if (action != 0) {
                    if (action == 1 && this.mLastHandledDownDpadKeyCode == keyCode) {
                        this.mLastHandledDownDpadKeyCode = -1;
                        return true;
                    }
                } else if (!this.mWrapSelectorWheel) {
                }
            }
        } else if (keyCode == 23 || keyCode == 66) {
            removeAllCallbacks();
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        tryComputeMaxWidth();
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (!this.mHasSelectorWheel) {
            return super.getAccessibilityNodeProvider();
        }
        if (this.mAccessibilityNodeProvider == null) {
            this.mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
        }
        return this.mAccessibilityNodeProvider;
    }

    /* access modifiers changed from: protected */
    public float getBottomFadingEdgeStrength() {
        return 0.9f;
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    /* access modifiers changed from: protected */
    public float getTopFadingEdgeStrength() {
        return 0.9f;
    }

    public int getValue() {
        return this.mValue;
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initSoundPlayer();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSoundPlayer();
        removeAllCallbacks();
        WorkerThreads.releaseWorker(SOUND_PLAY_THREAD);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!this.mHasSelectorWheel) {
            super.onDraw(canvas);
            return;
        }
        float right = (float) ((((getRight() - getLeft()) + getPaddingLeft()) - getPaddingRight()) / 2);
        float f = (float) this.mCurrentScrollOffset;
        float f2 = (float) (this.mInitialScrollOffset + (this.mSelectorElementHeight * 1));
        SparseArray sparseArray = this.mSelectorIndexToStringCache;
        int[] iArr = this.mSelectorIndices;
        float f3 = f;
        for (int i : iArr) {
            String str = (String) sparseArray.get(i);
            float abs = Math.abs(f2 - f3) / ((float) this.mSelectorElementHeight);
            float textSize = getTextSize(abs, this.mTextSizeHighlight, this.mTextSizeHint);
            this.mSelectorWheelPaint.setTextSize(textSize);
            this.mSelectorWheelPaint.setColor(getAlphaGradient(abs, this.mTextColorHint, false));
            canvas.drawText(str, right, ((textSize - ((float) this.mTextSizeHint)) / 2.0f) + f3, this.mSelectorWheelPaint);
            if (abs < 1.0f) {
                this.mSelectorWheelPaint.setColor(getAlphaGradient(abs, this.mTextColorHilight, true));
                canvas.drawText(str, right, ((textSize - ((float) this.mTextSizeHint)) / 2.0f) + f3, this.mSelectorWheelPaint);
            }
            f3 += (float) this.mSelectorElementHeight;
        }
        if (!TextUtils.isEmpty(this.mLabel) && !DeviceHelper.IS_INTERNATIONAL_BUILD) {
            float measureText = this.mLabelPaint.measureText(this.mLabel.toString());
            canvas.drawText(this.mLabel.toString(), ViewUtils.isLayoutRtl(this) ? Math.max(((right - (this.mDisplayedMaxTextWidth / 2.0f)) - ((float) this.MARGIN_LABEL_LEFT)) - measureText, 0.0f) : Math.min(right + (this.mDisplayedMaxTextWidth / 2.0f) + ((float) this.MARGIN_LABEL_LEFT), ((float) getWidth()) - measureText), (f2 - ((float) (this.mTextSizeHighlight / 2))) + ((float) (this.mLabelTextSize / 2)) + ((float) this.MARGIN_LABEL_TOP), this.mLabelPaint);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(NumberPicker.class.getName());
        accessibilityEvent.setScrollable(true);
        accessibilityEvent.setScrollY((this.mMinValue + this.mValue) * this.mSelectorElementHeight);
        accessibilityEvent.setMaxScrollY((this.mMaxValue - this.mMinValue) * this.mSelectorElementHeight);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mHasSelectorWheel || !isEnabled() || motionEvent.getActionMasked() != 0) {
            return false;
        }
        removeAllCallbacks();
        this.mInputText.setVisibility(4);
        float y = motionEvent.getY();
        this.mLastDownEventY = y;
        this.mLastDownOrMoveEventY = y;
        this.mLastDownEventTime = motionEvent.getEventTime();
        this.mIngonreMoveEvents = false;
        this.mShowSoftInputOnTap = false;
        float f = this.mLastDownEventY;
        if (f < ((float) this.mTopSelectionDividerTop)) {
            if (this.mScrollState == 0) {
                this.mPressedStateHelper.buttonPressDelayed(2);
            }
        } else if (f > ((float) this.mBottomSelectionDividerBottom) && this.mScrollState == 0) {
            this.mPressedStateHelper.buttonPressDelayed(1);
        }
        if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            onScrollStateChange(0);
        } else if (!this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
        } else {
            float f2 = this.mLastDownEventY;
            if (f2 < ((float) this.mTopSelectionDividerTop)) {
                postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
            } else if (f2 > ((float) this.mBottomSelectionDividerBottom)) {
                postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
            } else {
                this.mShowSoftInputOnTap = true;
                postBeginSoftInputOnLongPressCommand();
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mHasSelectorWheel) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth2 = this.mInputText.getMeasuredWidth();
        int measuredHeight2 = this.mInputText.getMeasuredHeight();
        int i5 = (measuredWidth - measuredWidth2) / 2;
        int i6 = (measuredHeight - measuredHeight2) / 2;
        this.mInputText.layout(i5, i6, measuredWidth2 + i5, measuredHeight2 + i6);
        if (z) {
            initializeSelectorWheel();
            initializeFadingEdges();
            int height = getHeight();
            int i7 = this.mSelectionDividersDistance;
            int i8 = (height - i7) / 2;
            int i9 = this.mSelectionDividerHeight;
            this.mTopSelectionDividerTop = i8 - i9;
            this.mBottomSelectionDividerBottom = this.mTopSelectionDividerTop + (i9 * 2) + i7;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (!this.mHasSelectorWheel) {
            super.onMeasure(i, i2);
            return;
        }
        super.onMeasure(makeMeasureSpec(i, this.mMaxWidth), makeMeasureSpec(i2, this.mMaxHeight));
        setMeasuredDimension(resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), i), resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), i2));
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        String obj = this.mScrollState != 0 ? this.mUpdateText : this.mInputText.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append(obj);
        Object obj2 = this.mLabel;
        if (obj2 == null) {
            obj2 = "";
        }
        sb.append(obj2);
        accessibilityEvent.getText().add(sb.toString());
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || !this.mHasSelectorWheel) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            removeBeginSoftInputCommand();
            removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
            int yVelocity = (int) velocityTracker.getYVelocity();
            if (Math.abs(yVelocity) >= Math.abs(this.mMaximumFlingVelocity)) {
                yVelocity = (int) (((float) yVelocity) * this.mMaxFlingSpeedFactor);
            }
            if (Math.abs(yVelocity) > this.mMinimumFlingVelocity) {
                fling(yVelocity);
                onScrollStateChange(2);
            } else {
                int y = (int) motionEvent.getY();
                long eventTime = motionEvent.getEventTime() - this.mLastDownEventTime;
                if (((int) Math.abs(((float) y) - this.mLastDownEventY)) > this.mTouchSlop || eventTime >= ((long) ViewConfiguration.getTapTimeout())) {
                    ensureScrollWheelAdjusted();
                } else if (this.mShowSoftInputOnTap) {
                    this.mShowSoftInputOnTap = false;
                } else {
                    int i = (y / this.mSelectorElementHeight) - 1;
                    if (i > 0) {
                        changeValueByOne(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    } else if (i < 0) {
                        changeValueByOne(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    }
                }
                onScrollStateChange(0);
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (actionMasked == 2 && !this.mIngonreMoveEvents) {
            float y2 = motionEvent.getY();
            if (this.mScrollState == 1) {
                scrollBy(0, (int) (y2 - this.mLastDownOrMoveEventY));
                invalidate();
            } else if (((int) Math.abs(y2 - this.mLastDownEventY)) > this.mTouchSlop) {
                removeAllCallbacks();
                onScrollStateChange(1);
            }
            this.mLastDownOrMoveEventY = y2;
        }
        return true;
    }

    public void scrollBy(int i, int i2) {
        int[] iArr = this.mSelectorIndices;
        if ((this.mWrapSelectorWheel || i2 <= 0 || iArr[1] > this.mMinValue) && (this.mWrapSelectorWheel || i2 >= 0 || iArr[1] < this.mMaxValue)) {
            this.mCurrentScrollOffset += i2;
            while (true) {
                int i3 = this.mCurrentScrollOffset;
                if (i3 - this.mInitialScrollOffset <= this.mSelectorTextGapHeight) {
                    break;
                }
                this.mCurrentScrollOffset = i3 - this.mSelectorElementHeight;
                decrementSelectorIndices(iArr);
                setValueInternal(iArr[1], true);
                if (!this.mWrapSelectorWheel && iArr[1] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            while (true) {
                int i4 = this.mCurrentScrollOffset;
                if (i4 - this.mInitialScrollOffset < (-this.mSelectorTextGapHeight)) {
                    this.mCurrentScrollOffset = i4 + this.mSelectorElementHeight;
                    incrementSelectorIndices(iArr);
                    setValueInternal(iArr[1], true);
                    if (!this.mWrapSelectorWheel && iArr[1] >= this.mMaxValue) {
                        this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    }
                } else {
                    return;
                }
            }
        } else {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
    }

    public void setDisplayedValues(String[] strArr) {
        EditText editText;
        int i;
        this.mDisplayedValues = strArr;
        if (this.mDisplayedValues != null) {
            editText = this.mInputText;
            i = 524289;
        } else {
            editText = this.mInputText;
            i = 2;
        }
        editText.setRawInputType(i);
        updateInputTextView();
        initializeSelectorWheelIndices();
        tryComputeMaxWidth();
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != this.mFormatter) {
            this.mFormatter = formatter;
            initializeSelectorWheelIndices();
            updateInputTextView();
        }
    }

    public void setLabel(String str) {
        if (this.mLabel != null || str == null) {
            CharSequence charSequence = this.mLabel;
            if (charSequence == null || charSequence.equals(str)) {
                return;
            }
        }
        this.mLabel = str;
        invalidate();
    }

    public void setMaxFlingSpeedFactor(float f) {
        if (f >= 0.0f) {
            this.mMaxFlingSpeedFactor = f;
        }
    }

    public void setMaxValue(int i) {
        if (this.mMaxValue != i) {
            if (i >= 0) {
                this.mMaxValue = i;
                int i2 = this.mMaxValue;
                if (i2 < this.mValue) {
                    this.mValue = i2;
                }
                setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
                initializeSelectorWheelIndices();
                updateInputTextView();
                tryComputeMaxWidth();
                invalidate();
                return;
            }
            throw new IllegalArgumentException("maxValue must be >= 0");
        }
    }

    public void setMinValue(int i) {
        if (this.mMinValue != i) {
            if (i >= 0) {
                this.mMinValue = i;
                int i2 = this.mMinValue;
                if (i2 > this.mValue) {
                    this.mValue = i2;
                }
                setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
                initializeSelectorWheelIndices();
                updateInputTextView();
                tryComputeMaxWidth();
                invalidate();
                return;
            }
            throw new IllegalArgumentException("minValue must be >= 0");
        }
    }

    public void setOnLongPressUpdateInterval(long j) {
        this.mLongPressUpdateInterval = j;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setValue(int i) {
        setValueInternal(i, false);
    }

    public void setWrapSelectorWheel(boolean z) {
        boolean z2 = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length;
        if ((!z || z2) && z != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = z;
        }
        refreshWheel();
    }
}
