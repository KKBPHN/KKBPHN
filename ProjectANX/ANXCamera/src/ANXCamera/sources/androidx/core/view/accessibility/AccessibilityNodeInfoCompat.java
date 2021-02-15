package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.R;
import androidx.core.view.accessibility.AccessibilityViewCommand.CommandArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.MoveAtGranularityArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.MoveHtmlArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.MoveWindowArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.ScrollToPositionArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.SetProgressArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.SetSelectionArguments;
import androidx.core.view.accessibility.AccessibilityViewCommand.SetTextArguments;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AccessibilityNodeInfoCompat {
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 1048576;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    private static final int BOOLEAN_PROPERTY_IS_HEADING = 2;
    private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 4;
    private static final int BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY = 8;
    private static final String BOOLEAN_PROPERTY_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY";
    private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 1;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final String HINT_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY";
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private static final String PANE_TITLE_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY";
    private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";
    private static final String SPANS_ACTION_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY";
    private static final String SPANS_END_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY";
    private static final String SPANS_FLAGS_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY";
    private static final String SPANS_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY";
    private static final String SPANS_START_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY";
    private static final String TOOLTIP_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY";
    private static int sClickableSpanId;
    private final AccessibilityNodeInfo mInfo;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int mParentVirtualDescendantId = -1;

    public class AccessibilityActionCompat {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
        public static final AccessibilityActionCompat ACTION_CLICK = new AccessibilityActionCompat(16, null);
        public static final AccessibilityActionCompat ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY = new AccessibilityActionCompat(16384, null);
        public static final AccessibilityActionCompat ACTION_CUT = new AccessibilityActionCompat(65536, null);
        public static final AccessibilityActionCompat ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
        public static final AccessibilityActionCompat ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
        public static final AccessibilityActionCompat ACTION_FOCUS = new AccessibilityActionCompat(1, null);
        public static final AccessibilityActionCompat ACTION_HIDE_TOOLTIP;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
        public static final AccessibilityActionCompat ACTION_MOVE_WINDOW;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, (CharSequence) null, MoveAtGranularityArguments.class);
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, (CharSequence) null, MoveHtmlArguments.class);
        @NonNull
        public static final AccessibilityActionCompat ACTION_PAGE_DOWN;
        @NonNull
        public static final AccessibilityActionCompat ACTION_PAGE_LEFT;
        @NonNull
        public static final AccessibilityActionCompat ACTION_PAGE_RIGHT;
        @NonNull
        public static final AccessibilityActionCompat ACTION_PAGE_UP;
        public static final AccessibilityActionCompat ACTION_PASTE = new AccessibilityActionCompat(32768, null);
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, (CharSequence) null, MoveAtGranularityArguments.class);
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, (CharSequence) null, MoveHtmlArguments.class);
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT = new AccessibilityActionCompat(4, null);
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, (CharSequence) null, SetSelectionArguments.class);
        public static final AccessibilityActionCompat ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, (CharSequence) null, SetTextArguments.class);
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        public static final AccessibilityActionCompat ACTION_SHOW_TOOLTIP;
        private static final String TAG = "A11yActionCompat";
        final Object mAction;
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        protected final AccessibilityViewCommand mCommand;
        private final int mId;
        private final CharSequence mLabel;
        private final Class mViewCommandArgumentClass;

        static {
            AccessibilityAction accessibilityAction = null;
            AccessibilityActionCompat accessibilityActionCompat = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SHOW_ON_SCREEN : null, 16908342, null, null, null);
            ACTION_SHOW_ON_SCREEN = accessibilityActionCompat;
            AccessibilityActionCompat accessibilityActionCompat2 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SCROLL_TO_POSITION : null, 16908343, null, null, ScrollToPositionArguments.class);
            ACTION_SCROLL_TO_POSITION = accessibilityActionCompat2;
            AccessibilityActionCompat accessibilityActionCompat3 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SCROLL_UP : null, 16908344, null, null, null);
            ACTION_SCROLL_UP = accessibilityActionCompat3;
            AccessibilityActionCompat accessibilityActionCompat4 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SCROLL_LEFT : null, 16908345, null, null, null);
            ACTION_SCROLL_LEFT = accessibilityActionCompat4;
            AccessibilityActionCompat accessibilityActionCompat5 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SCROLL_DOWN : null, 16908346, null, null, null);
            ACTION_SCROLL_DOWN = accessibilityActionCompat5;
            AccessibilityActionCompat accessibilityActionCompat6 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_SCROLL_RIGHT : null, 16908347, null, null, null);
            ACTION_SCROLL_RIGHT = accessibilityActionCompat6;
            AccessibilityActionCompat accessibilityActionCompat7 = new AccessibilityActionCompat(VERSION.SDK_INT >= 29 ? AccessibilityAction.ACTION_PAGE_UP : null, 16908358, null, null, null);
            ACTION_PAGE_UP = accessibilityActionCompat7;
            AccessibilityActionCompat accessibilityActionCompat8 = new AccessibilityActionCompat(VERSION.SDK_INT >= 29 ? AccessibilityAction.ACTION_PAGE_DOWN : null, 16908359, null, null, null);
            ACTION_PAGE_DOWN = accessibilityActionCompat8;
            AccessibilityActionCompat accessibilityActionCompat9 = new AccessibilityActionCompat(VERSION.SDK_INT >= 29 ? AccessibilityAction.ACTION_PAGE_LEFT : null, 16908360, null, null, null);
            ACTION_PAGE_LEFT = accessibilityActionCompat9;
            AccessibilityActionCompat accessibilityActionCompat10 = new AccessibilityActionCompat(VERSION.SDK_INT >= 29 ? AccessibilityAction.ACTION_PAGE_RIGHT : null, 16908361, null, null, null);
            ACTION_PAGE_RIGHT = accessibilityActionCompat10;
            AccessibilityActionCompat accessibilityActionCompat11 = new AccessibilityActionCompat(VERSION.SDK_INT >= 23 ? AccessibilityAction.ACTION_CONTEXT_CLICK : null, 16908348, null, null, null);
            ACTION_CONTEXT_CLICK = accessibilityActionCompat11;
            AccessibilityActionCompat accessibilityActionCompat12 = new AccessibilityActionCompat(VERSION.SDK_INT >= 24 ? AccessibilityAction.ACTION_SET_PROGRESS : null, 16908349, null, null, SetProgressArguments.class);
            ACTION_SET_PROGRESS = accessibilityActionCompat12;
            AccessibilityActionCompat accessibilityActionCompat13 = new AccessibilityActionCompat(VERSION.SDK_INT >= 26 ? AccessibilityAction.ACTION_MOVE_WINDOW : null, 16908354, null, null, MoveWindowArguments.class);
            ACTION_MOVE_WINDOW = accessibilityActionCompat13;
            AccessibilityActionCompat accessibilityActionCompat14 = new AccessibilityActionCompat(VERSION.SDK_INT >= 28 ? AccessibilityAction.ACTION_SHOW_TOOLTIP : null, 16908356, null, null, null);
            ACTION_SHOW_TOOLTIP = accessibilityActionCompat14;
            if (VERSION.SDK_INT >= 28) {
                accessibilityAction = AccessibilityAction.ACTION_HIDE_TOOLTIP;
            }
            AccessibilityActionCompat accessibilityActionCompat15 = new AccessibilityActionCompat(accessibilityAction, 16908357, null, null, null);
            ACTION_HIDE_TOOLTIP = accessibilityActionCompat15;
        }

        public AccessibilityActionCompat(int i, CharSequence charSequence) {
            this(null, i, charSequence, null, null);
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public AccessibilityActionCompat(int i, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            this(null, i, charSequence, accessibilityViewCommand, null);
        }

        private AccessibilityActionCompat(int i, CharSequence charSequence, Class cls) {
            this(null, i, charSequence, null, cls);
        }

        AccessibilityActionCompat(Object obj) {
            this(obj, 0, null, null, null);
        }

        AccessibilityActionCompat(Object obj, int i, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand, Class cls) {
            this.mId = i;
            this.mLabel = charSequence;
            this.mCommand = accessibilityViewCommand;
            if (VERSION.SDK_INT >= 21 && obj == null) {
                obj = new AccessibilityAction(i, charSequence);
            }
            this.mAction = obj;
            this.mViewCommandArgumentClass = cls;
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public AccessibilityActionCompat createReplacementAction(CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            AccessibilityActionCompat accessibilityActionCompat = new AccessibilityActionCompat(null, this.mId, charSequence, accessibilityViewCommand, this.mViewCommandArgumentClass);
            return accessibilityActionCompat;
        }

        public int getId() {
            if (VERSION.SDK_INT >= 21) {
                return ((AccessibilityAction) this.mAction).getId();
            }
            return 0;
        }

        public CharSequence getLabel() {
            if (VERSION.SDK_INT >= 21) {
                return ((AccessibilityAction) this.mAction).getLabel();
            }
            return null;
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0025  */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x0028  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public boolean perform(View view, Bundle bundle) {
            if (this.mCommand == null) {
                return false;
            }
            CommandArguments commandArguments = null;
            Class cls = this.mViewCommandArgumentClass;
            if (cls != null) {
                try {
                    CommandArguments commandArguments2 = (CommandArguments) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    try {
                        commandArguments2.setBundle(bundle);
                        commandArguments = commandArguments2;
                    } catch (Exception e) {
                        e = e;
                        commandArguments = commandArguments2;
                        Class cls2 = this.mViewCommandArgumentClass;
                        if (cls2 != null) {
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to execute command with argument class ViewCommandArgument: ");
                        sb.append(r1);
                        Log.e(TAG, sb.toString(), e);
                        return this.mCommand.perform(view, commandArguments);
                    }
                } catch (Exception e2) {
                    e = e2;
                    Class cls22 = this.mViewCommandArgumentClass;
                    String name = cls22 != null ? "null" : cls22.getName();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to execute command with argument class ViewCommandArgument: ");
                    sb2.append(name);
                    Log.e(TAG, sb2.toString(), e);
                    return this.mCommand.perform(view, commandArguments);
                }
            }
            return this.mCommand.perform(view, commandArguments);
        }
    }

    public class CollectionInfoCompat {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;

        CollectionInfoCompat(Object obj) {
            this.mInfo = obj;
        }

        public static CollectionInfoCompat obtain(int i, int i2, boolean z) {
            return VERSION.SDK_INT >= 19 ? new CollectionInfoCompat(CollectionInfo.obtain(i, i2, z)) : new CollectionInfoCompat(null);
        }

        public static CollectionInfoCompat obtain(int i, int i2, boolean z, int i3) {
            int i4 = VERSION.SDK_INT;
            return i4 >= 21 ? new CollectionInfoCompat(CollectionInfo.obtain(i, i2, z, i3)) : i4 >= 19 ? new CollectionInfoCompat(CollectionInfo.obtain(i, i2, z)) : new CollectionInfoCompat(null);
        }

        public int getColumnCount() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionInfo) this.mInfo).getColumnCount();
            }
            return 0;
        }

        public int getRowCount() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionInfo) this.mInfo).getRowCount();
            }
            return 0;
        }

        public int getSelectionMode() {
            if (VERSION.SDK_INT >= 21) {
                return ((CollectionInfo) this.mInfo).getSelectionMode();
            }
            return 0;
        }

        public boolean isHierarchical() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionInfo) this.mInfo).isHierarchical();
            }
            return false;
        }
    }

    public class CollectionItemInfoCompat {
        final Object mInfo;

        CollectionItemInfoCompat(Object obj) {
            this.mInfo = obj;
        }

        public static CollectionItemInfoCompat obtain(int i, int i2, int i3, int i4, boolean z) {
            return VERSION.SDK_INT >= 19 ? new CollectionItemInfoCompat(CollectionItemInfo.obtain(i, i2, i3, i4, z)) : new CollectionItemInfoCompat(null);
        }

        public static CollectionItemInfoCompat obtain(int i, int i2, int i3, int i4, boolean z, boolean z2) {
            int i5 = VERSION.SDK_INT;
            return i5 >= 21 ? new CollectionItemInfoCompat(CollectionItemInfo.obtain(i, i2, i3, i4, z, z2)) : i5 >= 19 ? new CollectionItemInfoCompat(CollectionItemInfo.obtain(i, i2, i3, i4, z)) : new CollectionItemInfoCompat(null);
        }

        public int getColumnIndex() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionItemInfo) this.mInfo).getColumnIndex();
            }
            return 0;
        }

        public int getColumnSpan() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionItemInfo) this.mInfo).getColumnSpan();
            }
            return 0;
        }

        public int getRowIndex() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionItemInfo) this.mInfo).getRowIndex();
            }
            return 0;
        }

        public int getRowSpan() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionItemInfo) this.mInfo).getRowSpan();
            }
            return 0;
        }

        public boolean isHeading() {
            if (VERSION.SDK_INT >= 19) {
                return ((CollectionItemInfo) this.mInfo).isHeading();
            }
            return false;
        }

        public boolean isSelected() {
            if (VERSION.SDK_INT >= 21) {
                return ((CollectionItemInfo) this.mInfo).isSelected();
            }
            return false;
        }
    }

    public class RangeInfoCompat {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        final Object mInfo;

        RangeInfoCompat(Object obj) {
            this.mInfo = obj;
        }

        public static RangeInfoCompat obtain(int i, float f, float f2, float f3) {
            return VERSION.SDK_INT >= 19 ? new RangeInfoCompat(RangeInfo.obtain(i, f, f2, f3)) : new RangeInfoCompat(null);
        }

        public float getCurrent() {
            if (VERSION.SDK_INT >= 19) {
                return ((RangeInfo) this.mInfo).getCurrent();
            }
            return 0.0f;
        }

        public float getMax() {
            if (VERSION.SDK_INT >= 19) {
                return ((RangeInfo) this.mInfo).getMax();
            }
            return 0.0f;
        }

        public float getMin() {
            if (VERSION.SDK_INT >= 19) {
                return ((RangeInfo) this.mInfo).getMin();
            }
            return 0.0f;
        }

        public int getType() {
            if (VERSION.SDK_INT >= 19) {
                return ((RangeInfo) this.mInfo).getType();
            }
            return 0;
        }
    }

    public final class TouchDelegateInfoCompat {
        final TouchDelegateInfo mInfo;

        TouchDelegateInfoCompat(@NonNull TouchDelegateInfo touchDelegateInfo) {
            this.mInfo = touchDelegateInfo;
        }

        public TouchDelegateInfoCompat(@NonNull Map map) {
            if (VERSION.SDK_INT >= 29) {
                this.mInfo = new TouchDelegateInfo(map);
            } else {
                this.mInfo = null;
            }
        }

        @Nullable
        public Region getRegionAt(@IntRange(from = 0) int i) {
            if (VERSION.SDK_INT >= 29) {
                return this.mInfo.getRegionAt(i);
            }
            return null;
        }

        @IntRange(from = 0)
        public int getRegionCount() {
            if (VERSION.SDK_INT >= 29) {
                return this.mInfo.getRegionCount();
            }
            return 0;
        }

        @Nullable
        public AccessibilityNodeInfoCompat getTargetForRegion(@NonNull Region region) {
            if (VERSION.SDK_INT >= 29) {
                AccessibilityNodeInfo targetForRegion = this.mInfo.getTargetForRegion(region);
                if (targetForRegion != null) {
                    return AccessibilityNodeInfoCompat.wrap(targetForRegion);
                }
            }
            return null;
        }
    }

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.mInfo = accessibilityNodeInfo;
    }

    @Deprecated
    public AccessibilityNodeInfoCompat(Object obj) {
        this.mInfo = (AccessibilityNodeInfo) obj;
    }

    private void addSpanLocationToExtras(ClickableSpan clickableSpan, Spanned spanned, int i) {
        extrasIntList(SPANS_START_KEY).add(Integer.valueOf(spanned.getSpanStart(clickableSpan)));
        extrasIntList(SPANS_END_KEY).add(Integer.valueOf(spanned.getSpanEnd(clickableSpan)));
        extrasIntList(SPANS_FLAGS_KEY).add(Integer.valueOf(spanned.getSpanFlags(clickableSpan)));
        extrasIntList(SPANS_ID_KEY).add(Integer.valueOf(i));
    }

    private void clearExtrasSpans() {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().remove(SPANS_START_KEY);
            this.mInfo.getExtras().remove(SPANS_END_KEY);
            this.mInfo.getExtras().remove(SPANS_FLAGS_KEY);
            this.mInfo.getExtras().remove(SPANS_ID_KEY);
        }
    }

    private List extrasCharSequenceList(String str) {
        if (VERSION.SDK_INT < 19) {
            return new ArrayList();
        }
        ArrayList charSequenceArrayList = this.mInfo.getExtras().getCharSequenceArrayList(str);
        if (charSequenceArrayList == null) {
            charSequenceArrayList = new ArrayList();
            this.mInfo.getExtras().putCharSequenceArrayList(str, charSequenceArrayList);
        }
        return charSequenceArrayList;
    }

    private List extrasIntList(String str) {
        if (VERSION.SDK_INT < 19) {
            return new ArrayList();
        }
        ArrayList integerArrayList = this.mInfo.getExtras().getIntegerArrayList(str);
        if (integerArrayList == null) {
            integerArrayList = new ArrayList();
            this.mInfo.getExtras().putIntegerArrayList(str, integerArrayList);
        }
        return integerArrayList;
    }

    private static String getActionSymbolicName(int i) {
        if (i == 1) {
            return "ACTION_FOCUS";
        }
        if (i == 2) {
            return "ACTION_CLEAR_FOCUS";
        }
        switch (i) {
            case 4:
                return "ACTION_SELECT";
            case 8:
                return "ACTION_CLEAR_SELECTION";
            case 16:
                return "ACTION_CLICK";
            case 32:
                return "ACTION_LONG_CLICK";
            case 64:
                return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
                return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
                return "ACTION_SCROLL_FORWARD";
            case 8192:
                return "ACTION_SCROLL_BACKWARD";
            case 16384:
                return "ACTION_COPY";
            case 32768:
                return "ACTION_PASTE";
            case 65536:
                return "ACTION_CUT";
            case 131072:
                return "ACTION_SET_SELECTION";
            default:
                return "ACTION_UNKNOWN";
        }
    }

    private boolean getBooleanProperty(int i) {
        Bundle extras = getExtras();
        boolean z = false;
        if (extras == null) {
            return false;
        }
        if ((extras.getInt(BOOLEAN_PROPERTY_KEY, 0) & i) == i) {
            z = true;
        }
        return z;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static ClickableSpan[] getClickableSpans(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            return (ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
        }
        return null;
    }

    private SparseArray getOrCreateSpansFromViewTags(View view) {
        SparseArray spansFromViewTags = getSpansFromViewTags(view);
        if (spansFromViewTags != null) {
            return spansFromViewTags;
        }
        SparseArray sparseArray = new SparseArray();
        view.setTag(R.id.tag_accessibility_clickable_spans, sparseArray);
        return sparseArray;
    }

    private SparseArray getSpansFromViewTags(View view) {
        return (SparseArray) view.getTag(R.id.tag_accessibility_clickable_spans);
    }

    private boolean hasSpans() {
        return !extrasIntList(SPANS_START_KEY).isEmpty();
    }

    private int idForClickableSpan(ClickableSpan clickableSpan, SparseArray sparseArray) {
        if (sparseArray != null) {
            for (int i = 0; i < sparseArray.size(); i++) {
                if (clickableSpan.equals((ClickableSpan) ((WeakReference) sparseArray.valueAt(i)).get())) {
                    return sparseArray.keyAt(i);
                }
            }
        }
        int i2 = sClickableSpanId;
        sClickableSpanId = i2 + 1;
        return i2;
    }

    public static AccessibilityNodeInfoCompat obtain() {
        return wrap(AccessibilityNodeInfo.obtain());
    }

    public static AccessibilityNodeInfoCompat obtain(View view) {
        return wrap(AccessibilityNodeInfo.obtain(view));
    }

    public static AccessibilityNodeInfoCompat obtain(View view, int i) {
        if (VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(AccessibilityNodeInfo.obtain(view, i));
        }
        return null;
    }

    public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrap(AccessibilityNodeInfo.obtain(accessibilityNodeInfoCompat.mInfo));
    }

    private void removeCollectedSpans(View view) {
        SparseArray spansFromViewTags = getSpansFromViewTags(view);
        if (spansFromViewTags != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < spansFromViewTags.size(); i++) {
                if (((WeakReference) spansFromViewTags.valueAt(i)).get() == null) {
                    arrayList.add(Integer.valueOf(i));
                }
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                spansFromViewTags.remove(((Integer) arrayList.get(i2)).intValue());
            }
        }
    }

    private void setBooleanProperty(int i, boolean z) {
        Bundle extras = getExtras();
        if (extras != null) {
            String str = BOOLEAN_PROPERTY_KEY;
            int i2 = extras.getInt(str, 0) & (~i);
            if (!z) {
                i = 0;
            }
            extras.putInt(str, i | i2);
        }
    }

    public static AccessibilityNodeInfoCompat wrap(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }

    static AccessibilityNodeInfoCompat wrapNonNullInstance(Object obj) {
        if (obj != null) {
            return new AccessibilityNodeInfoCompat(obj);
        }
        return null;
    }

    public void addAction(int i) {
        this.mInfo.addAction(i);
    }

    public void addAction(AccessibilityActionCompat accessibilityActionCompat) {
        if (VERSION.SDK_INT >= 21) {
            this.mInfo.addAction((AccessibilityAction) accessibilityActionCompat.mAction);
        }
    }

    public void addChild(View view) {
        this.mInfo.addChild(view);
    }

    public void addChild(View view, int i) {
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.addChild(view, i);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void addSpansToExtras(CharSequence charSequence, View view) {
        int i = VERSION.SDK_INT;
        if (i >= 19 && i < 26) {
            clearExtrasSpans();
            removeCollectedSpans(view);
            ClickableSpan[] clickableSpans = getClickableSpans(charSequence);
            if (clickableSpans != null && clickableSpans.length > 0) {
                getExtras().putInt(SPANS_ACTION_ID_KEY, R.id.accessibility_action_clickable_span);
                SparseArray orCreateSpansFromViewTags = getOrCreateSpansFromViewTags(view);
                int i2 = 0;
                while (clickableSpans != null && i2 < clickableSpans.length) {
                    int idForClickableSpan = idForClickableSpan(clickableSpans[i2], orCreateSpansFromViewTags);
                    orCreateSpansFromViewTags.put(idForClickableSpan, new WeakReference(clickableSpans[i2]));
                    addSpanLocationToExtras(clickableSpans[i2], (Spanned) charSequence, idForClickableSpan);
                    i2++;
                }
            }
        }
    }

    public boolean canOpenPopup() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.canOpenPopup();
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || AccessibilityNodeInfoCompat.class != obj.getClass()) {
            return false;
        }
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat) obj;
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        if (accessibilityNodeInfo == null) {
            if (accessibilityNodeInfoCompat.mInfo != null) {
                return false;
            }
        } else if (!accessibilityNodeInfo.equals(accessibilityNodeInfoCompat.mInfo)) {
            return false;
        }
        return true;
    }

    public List findAccessibilityNodeInfosByText(String str) {
        ArrayList arrayList = new ArrayList();
        List findAccessibilityNodeInfosByText = this.mInfo.findAccessibilityNodeInfosByText(str);
        int size = findAccessibilityNodeInfosByText.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(wrap((AccessibilityNodeInfo) findAccessibilityNodeInfosByText.get(i)));
        }
        return arrayList;
    }

    public List findAccessibilityNodeInfosByViewId(String str) {
        if (VERSION.SDK_INT < 18) {
            return Collections.emptyList();
        }
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = this.mInfo.findAccessibilityNodeInfosByViewId(str);
        ArrayList arrayList = new ArrayList();
        for (AccessibilityNodeInfo wrap : findAccessibilityNodeInfosByViewId) {
            arrayList.add(wrap(wrap));
        }
        return arrayList;
    }

    public AccessibilityNodeInfoCompat findFocus(int i) {
        if (VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(this.mInfo.findFocus(i));
        }
        return null;
    }

    public AccessibilityNodeInfoCompat focusSearch(int i) {
        if (VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(this.mInfo.focusSearch(i));
        }
        return null;
    }

    public List getActionList() {
        List actionList = VERSION.SDK_INT >= 21 ? this.mInfo.getActionList() : null;
        if (actionList == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        int size = actionList.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(new AccessibilityActionCompat(actionList.get(i)));
        }
        return arrayList;
    }

    public int getActions() {
        return this.mInfo.getActions();
    }

    @Deprecated
    public void getBoundsInParent(Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }

    public void getBoundsInScreen(Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }

    public AccessibilityNodeInfoCompat getChild(int i) {
        return wrapNonNullInstance(this.mInfo.getChild(i));
    }

    public int getChildCount() {
        return this.mInfo.getChildCount();
    }

    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }

    public CollectionInfoCompat getCollectionInfo() {
        if (VERSION.SDK_INT >= 19) {
            CollectionInfo collectionInfo = this.mInfo.getCollectionInfo();
            if (collectionInfo != null) {
                return new CollectionInfoCompat(collectionInfo);
            }
        }
        return null;
    }

    public CollectionItemInfoCompat getCollectionItemInfo() {
        if (VERSION.SDK_INT >= 19) {
            CollectionItemInfo collectionItemInfo = this.mInfo.getCollectionItemInfo();
            if (collectionItemInfo != null) {
                return new CollectionItemInfoCompat(collectionItemInfo);
            }
        }
        return null;
    }

    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }

    public int getDrawingOrder() {
        if (VERSION.SDK_INT >= 24) {
            return this.mInfo.getDrawingOrder();
        }
        return 0;
    }

    public CharSequence getError() {
        if (VERSION.SDK_INT >= 21) {
            return this.mInfo.getError();
        }
        return null;
    }

    public Bundle getExtras() {
        return VERSION.SDK_INT >= 19 ? this.mInfo.getExtras() : new Bundle();
    }

    @Nullable
    public CharSequence getHintText() {
        int i = VERSION.SDK_INT;
        if (i >= 26) {
            return this.mInfo.getHintText();
        }
        if (i >= 19) {
            return this.mInfo.getExtras().getCharSequence(HINT_TEXT_KEY);
        }
        return null;
    }

    @Deprecated
    public Object getInfo() {
        return this.mInfo;
    }

    public int getInputType() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.getInputType();
        }
        return 0;
    }

    public AccessibilityNodeInfoCompat getLabelFor() {
        if (VERSION.SDK_INT >= 17) {
            return wrapNonNullInstance(this.mInfo.getLabelFor());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getLabeledBy() {
        if (VERSION.SDK_INT >= 17) {
            return wrapNonNullInstance(this.mInfo.getLabeledBy());
        }
        return null;
    }

    public int getLiveRegion() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.getLiveRegion();
        }
        return 0;
    }

    public int getMaxTextLength() {
        if (VERSION.SDK_INT >= 21) {
            return this.mInfo.getMaxTextLength();
        }
        return -1;
    }

    public int getMovementGranularities() {
        if (VERSION.SDK_INT >= 16) {
            return this.mInfo.getMovementGranularities();
        }
        return 0;
    }

    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }

    @Nullable
    public CharSequence getPaneTitle() {
        int i = VERSION.SDK_INT;
        if (i >= 28) {
            return this.mInfo.getPaneTitle();
        }
        if (i >= 19) {
            return this.mInfo.getExtras().getCharSequence(PANE_TITLE_KEY);
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(this.mInfo.getParent());
    }

    public RangeInfoCompat getRangeInfo() {
        if (VERSION.SDK_INT >= 19) {
            RangeInfo rangeInfo = this.mInfo.getRangeInfo();
            if (rangeInfo != null) {
                return new RangeInfoCompat(rangeInfo);
            }
        }
        return null;
    }

    @Nullable
    public CharSequence getRoleDescription() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence(ROLE_DESCRIPTION_KEY);
        }
        return null;
    }

    public CharSequence getText() {
        if (!hasSpans()) {
            return this.mInfo.getText();
        }
        List extrasIntList = extrasIntList(SPANS_START_KEY);
        List extrasIntList2 = extrasIntList(SPANS_END_KEY);
        List extrasIntList3 = extrasIntList(SPANS_FLAGS_KEY);
        List extrasIntList4 = extrasIntList(SPANS_ID_KEY);
        SpannableString spannableString = new SpannableString(TextUtils.substring(this.mInfo.getText(), 0, this.mInfo.getText().length()));
        for (int i = 0; i < extrasIntList.size(); i++) {
            spannableString.setSpan(new AccessibilityClickableSpanCompat(((Integer) extrasIntList4.get(i)).intValue(), this, getExtras().getInt(SPANS_ACTION_ID_KEY)), ((Integer) extrasIntList.get(i)).intValue(), ((Integer) extrasIntList2.get(i)).intValue(), ((Integer) extrasIntList3.get(i)).intValue());
        }
        return spannableString;
    }

    public int getTextSelectionEnd() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionEnd();
        }
        return -1;
    }

    public int getTextSelectionStart() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionStart();
        }
        return -1;
    }

    @Nullable
    public CharSequence getTooltipText() {
        int i = VERSION.SDK_INT;
        if (i >= 28) {
            return this.mInfo.getTooltipText();
        }
        if (i >= 19) {
            return this.mInfo.getExtras().getCharSequence(TOOLTIP_TEXT_KEY);
        }
        return null;
    }

    @Nullable
    public TouchDelegateInfoCompat getTouchDelegateInfo() {
        if (VERSION.SDK_INT >= 29) {
            TouchDelegateInfo touchDelegateInfo = this.mInfo.getTouchDelegateInfo();
            if (touchDelegateInfo != null) {
                return new TouchDelegateInfoCompat(touchDelegateInfo);
            }
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getTraversalAfter() {
        if (VERSION.SDK_INT >= 22) {
            return wrapNonNullInstance(this.mInfo.getTraversalAfter());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getTraversalBefore() {
        if (VERSION.SDK_INT >= 22) {
            return wrapNonNullInstance(this.mInfo.getTraversalBefore());
        }
        return null;
    }

    public String getViewIdResourceName() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.getViewIdResourceName();
        }
        return null;
    }

    public AccessibilityWindowInfoCompat getWindow() {
        if (VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(this.mInfo.getWindow());
        }
        return null;
    }

    public int getWindowId() {
        return this.mInfo.getWindowId();
    }

    public int hashCode() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        return accessibilityNodeInfo.hashCode();
    }

    public boolean isAccessibilityFocused() {
        if (VERSION.SDK_INT >= 16) {
            return this.mInfo.isAccessibilityFocused();
        }
        return false;
    }

    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }

    public boolean isChecked() {
        return this.mInfo.isChecked();
    }

    public boolean isClickable() {
        return this.mInfo.isClickable();
    }

    public boolean isContentInvalid() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.isContentInvalid();
        }
        return false;
    }

    public boolean isContextClickable() {
        if (VERSION.SDK_INT >= 23) {
            return this.mInfo.isContextClickable();
        }
        return false;
    }

    public boolean isDismissable() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.isDismissable();
        }
        return false;
    }

    public boolean isEditable() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.isEditable();
        }
        return false;
    }

    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }

    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }

    public boolean isFocused() {
        return this.mInfo.isFocused();
    }

    public boolean isHeading() {
        if (VERSION.SDK_INT >= 28) {
            return this.mInfo.isHeading();
        }
        boolean z = true;
        if (getBooleanProperty(2)) {
            return true;
        }
        CollectionItemInfoCompat collectionItemInfo = getCollectionItemInfo();
        if (collectionItemInfo == null || !collectionItemInfo.isHeading()) {
            z = false;
        }
        return z;
    }

    public boolean isImportantForAccessibility() {
        if (VERSION.SDK_INT >= 24) {
            return this.mInfo.isImportantForAccessibility();
        }
        return true;
    }

    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }

    public boolean isMultiLine() {
        if (VERSION.SDK_INT >= 19) {
            return this.mInfo.isMultiLine();
        }
        return false;
    }

    public boolean isPassword() {
        return this.mInfo.isPassword();
    }

    public boolean isScreenReaderFocusable() {
        return VERSION.SDK_INT >= 28 ? this.mInfo.isScreenReaderFocusable() : getBooleanProperty(1);
    }

    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }

    public boolean isSelected() {
        return this.mInfo.isSelected();
    }

    public boolean isShowingHintText() {
        return VERSION.SDK_INT >= 26 ? this.mInfo.isShowingHintText() : getBooleanProperty(4);
    }

    public boolean isTextEntryKey() {
        return VERSION.SDK_INT >= 29 ? this.mInfo.isTextEntryKey() : getBooleanProperty(8);
    }

    public boolean isVisibleToUser() {
        if (VERSION.SDK_INT >= 16) {
            return this.mInfo.isVisibleToUser();
        }
        return false;
    }

    public boolean performAction(int i) {
        return this.mInfo.performAction(i);
    }

    public boolean performAction(int i, Bundle bundle) {
        if (VERSION.SDK_INT >= 16) {
            return this.mInfo.performAction(i, bundle);
        }
        return false;
    }

    public void recycle() {
        this.mInfo.recycle();
    }

    public boolean refresh() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.refresh();
        }
        return false;
    }

    public boolean removeAction(AccessibilityActionCompat accessibilityActionCompat) {
        if (VERSION.SDK_INT >= 21) {
            return this.mInfo.removeAction((AccessibilityAction) accessibilityActionCompat.mAction);
        }
        return false;
    }

    public boolean removeChild(View view) {
        if (VERSION.SDK_INT >= 21) {
            return this.mInfo.removeChild(view);
        }
        return false;
    }

    public boolean removeChild(View view, int i) {
        if (VERSION.SDK_INT >= 21) {
            return this.mInfo.removeChild(view, i);
        }
        return false;
    }

    public void setAccessibilityFocused(boolean z) {
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.setAccessibilityFocused(z);
        }
    }

    @Deprecated
    public void setBoundsInParent(Rect rect) {
        this.mInfo.setBoundsInParent(rect);
    }

    public void setBoundsInScreen(Rect rect) {
        this.mInfo.setBoundsInScreen(rect);
    }

    public void setCanOpenPopup(boolean z) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setCanOpenPopup(z);
        }
    }

    public void setCheckable(boolean z) {
        this.mInfo.setCheckable(z);
    }

    public void setChecked(boolean z) {
        this.mInfo.setChecked(z);
    }

    public void setClassName(CharSequence charSequence) {
        this.mInfo.setClassName(charSequence);
    }

    public void setClickable(boolean z) {
        this.mInfo.setClickable(z);
    }

    public void setCollectionInfo(Object obj) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setCollectionInfo(obj == null ? null : (CollectionInfo) ((CollectionInfoCompat) obj).mInfo);
        }
    }

    public void setCollectionItemInfo(Object obj) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setCollectionItemInfo(obj == null ? null : (CollectionItemInfo) ((CollectionItemInfoCompat) obj).mInfo);
        }
    }

    public void setContentDescription(CharSequence charSequence) {
        this.mInfo.setContentDescription(charSequence);
    }

    public void setContentInvalid(boolean z) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setContentInvalid(z);
        }
    }

    public void setContextClickable(boolean z) {
        if (VERSION.SDK_INT >= 23) {
            this.mInfo.setContextClickable(z);
        }
    }

    public void setDismissable(boolean z) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setDismissable(z);
        }
    }

    public void setDrawingOrder(int i) {
        if (VERSION.SDK_INT >= 24) {
            this.mInfo.setDrawingOrder(i);
        }
    }

    public void setEditable(boolean z) {
        if (VERSION.SDK_INT >= 18) {
            this.mInfo.setEditable(z);
        }
    }

    public void setEnabled(boolean z) {
        this.mInfo.setEnabled(z);
    }

    public void setError(CharSequence charSequence) {
        if (VERSION.SDK_INT >= 21) {
            this.mInfo.setError(charSequence);
        }
    }

    public void setFocusable(boolean z) {
        this.mInfo.setFocusable(z);
    }

    public void setFocused(boolean z) {
        this.mInfo.setFocused(z);
    }

    public void setHeading(boolean z) {
        if (VERSION.SDK_INT >= 28) {
            this.mInfo.setHeading(z);
        } else {
            setBooleanProperty(2, z);
        }
    }

    public void setHintText(@Nullable CharSequence charSequence) {
        int i = VERSION.SDK_INT;
        if (i >= 26) {
            this.mInfo.setHintText(charSequence);
        } else if (i >= 19) {
            this.mInfo.getExtras().putCharSequence(HINT_TEXT_KEY, charSequence);
        }
    }

    public void setImportantForAccessibility(boolean z) {
        if (VERSION.SDK_INT >= 24) {
            this.mInfo.setImportantForAccessibility(z);
        }
    }

    public void setInputType(int i) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setInputType(i);
        }
    }

    public void setLabelFor(View view) {
        if (VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(view);
        }
    }

    public void setLabelFor(View view, int i) {
        if (VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(view, i);
        }
    }

    public void setLabeledBy(View view) {
        if (VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(view);
        }
    }

    public void setLabeledBy(View view, int i) {
        if (VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(view, i);
        }
    }

    public void setLiveRegion(int i) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setLiveRegion(i);
        }
    }

    public void setLongClickable(boolean z) {
        this.mInfo.setLongClickable(z);
    }

    public void setMaxTextLength(int i) {
        if (VERSION.SDK_INT >= 21) {
            this.mInfo.setMaxTextLength(i);
        }
    }

    public void setMovementGranularities(int i) {
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.setMovementGranularities(i);
        }
    }

    public void setMultiLine(boolean z) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setMultiLine(z);
        }
    }

    public void setPackageName(CharSequence charSequence) {
        this.mInfo.setPackageName(charSequence);
    }

    public void setPaneTitle(@Nullable CharSequence charSequence) {
        int i = VERSION.SDK_INT;
        if (i >= 28) {
            this.mInfo.setPaneTitle(charSequence);
        } else if (i >= 19) {
            this.mInfo.getExtras().putCharSequence(PANE_TITLE_KEY, charSequence);
        }
    }

    public void setParent(View view) {
        this.mInfo.setParent(view);
    }

    public void setParent(View view, int i) {
        this.mParentVirtualDescendantId = i;
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.setParent(view, i);
        }
    }

    public void setPassword(boolean z) {
        this.mInfo.setPassword(z);
    }

    public void setRangeInfo(RangeInfoCompat rangeInfoCompat) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setRangeInfo((RangeInfo) rangeInfoCompat.mInfo);
        }
    }

    public void setRoleDescription(@Nullable CharSequence charSequence) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence(ROLE_DESCRIPTION_KEY, charSequence);
        }
    }

    public void setScreenReaderFocusable(boolean z) {
        if (VERSION.SDK_INT >= 28) {
            this.mInfo.setScreenReaderFocusable(z);
        } else {
            setBooleanProperty(1, z);
        }
    }

    public void setScrollable(boolean z) {
        this.mInfo.setScrollable(z);
    }

    public void setSelected(boolean z) {
        this.mInfo.setSelected(z);
    }

    public void setShowingHintText(boolean z) {
        if (VERSION.SDK_INT >= 26) {
            this.mInfo.setShowingHintText(z);
        } else {
            setBooleanProperty(4, z);
        }
    }

    public void setSource(View view) {
        this.mInfo.setSource(view);
    }

    public void setSource(View view, int i) {
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.setSource(view, i);
        }
    }

    public void setText(CharSequence charSequence) {
        this.mInfo.setText(charSequence);
    }

    public void setTextEntryKey(boolean z) {
        if (VERSION.SDK_INT >= 29) {
            this.mInfo.setTextEntryKey(z);
        } else {
            setBooleanProperty(8, z);
        }
    }

    public void setTextSelection(int i, int i2) {
        if (VERSION.SDK_INT >= 18) {
            this.mInfo.setTextSelection(i, i2);
        }
    }

    public void setTooltipText(@Nullable CharSequence charSequence) {
        int i = VERSION.SDK_INT;
        if (i >= 28) {
            this.mInfo.setTooltipText(charSequence);
        } else if (i >= 19) {
            this.mInfo.getExtras().putCharSequence(TOOLTIP_TEXT_KEY, charSequence);
        }
    }

    public void setTouchDelegateInfo(@NonNull TouchDelegateInfoCompat touchDelegateInfoCompat) {
        if (VERSION.SDK_INT >= 29) {
            this.mInfo.setTouchDelegateInfo(touchDelegateInfoCompat.mInfo);
        }
    }

    public void setTraversalAfter(View view) {
        if (VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(view);
        }
    }

    public void setTraversalAfter(View view, int i) {
        if (VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(view, i);
        }
    }

    public void setTraversalBefore(View view) {
        if (VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(view);
        }
    }

    public void setTraversalBefore(View view, int i) {
        if (VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(view, i);
        }
    }

    public void setViewIdResourceName(String str) {
        if (VERSION.SDK_INT >= 18) {
            this.mInfo.setViewIdResourceName(str);
        }
    }

    public void setVisibleToUser(boolean z) {
        if (VERSION.SDK_INT >= 16) {
            this.mInfo.setVisibleToUser(z);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        Rect rect = new Rect();
        getBoundsInParent(rect);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("; boundsInParent: ");
        sb2.append(rect);
        sb.append(sb2.toString());
        getBoundsInScreen(rect);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("; boundsInScreen: ");
        sb3.append(rect);
        sb.append(sb3.toString());
        sb.append("; packageName: ");
        sb.append(getPackageName());
        sb.append("; className: ");
        sb.append(getClassName());
        sb.append("; text: ");
        sb.append(getText());
        sb.append("; contentDescription: ");
        sb.append(getContentDescription());
        sb.append("; viewId: ");
        sb.append(getViewIdResourceName());
        sb.append("; checkable: ");
        sb.append(isCheckable());
        sb.append("; checked: ");
        sb.append(isChecked());
        sb.append("; focusable: ");
        sb.append(isFocusable());
        sb.append("; focused: ");
        sb.append(isFocused());
        sb.append("; selected: ");
        sb.append(isSelected());
        sb.append("; clickable: ");
        sb.append(isClickable());
        sb.append("; longClickable: ");
        sb.append(isLongClickable());
        sb.append("; enabled: ");
        sb.append(isEnabled());
        sb.append("; password: ");
        sb.append(isPassword());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("; scrollable: ");
        sb4.append(isScrollable());
        sb.append(sb4.toString());
        sb.append("; [");
        int actions = getActions();
        while (actions != 0) {
            int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(actions);
            actions &= ~numberOfTrailingZeros;
            sb.append(getActionSymbolicName(numberOfTrailingZeros));
            if (actions != 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }
}
