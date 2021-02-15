package miuix.preference;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import miui.util.AttributeResolver;
import miuix.springback.view.SpringBackLayout;

public abstract class PreferenceFragment extends androidx.preference.PreferenceFragment {
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    /* access modifiers changed from: private */
    public boolean mAdapterInvalid = true;
    private FrameDecoration mFrameDecoration;
    /* access modifiers changed from: private */
    public PreferenceGroupAdapter mGroupAdapter;

    class FrameDecoration extends ItemDecoration {
        private Paint mClipPaint;
        private int mHeightPixels;
        private int mMaskPaddingBottom;
        private int mMaskPaddingEnd;
        private int mMaskPaddingStart;
        private int mMaskPaddingTop;
        private int mMaskRadius;
        private PreferenceGroupRect mPreferenceGroupRect;
        private Map mPreferenceGroupRectMaps;

        private FrameDecoration(Context context) {
            this.mMaskPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.miuix_preference_checkable_item_mask_padding_top);
            this.mMaskPaddingBottom = context.getResources().getDimensionPixelSize(R.dimen.miuix_preference_checkable_item_mask_padding_bottom);
            this.mMaskPaddingStart = context.getResources().getDimensionPixelSize(R.dimen.miuix_preference_checkable_item_mask_padding_start);
            this.mMaskPaddingEnd = context.getResources().getDimensionPixelSize(R.dimen.miuix_preference_checkable_item_mask_padding_end);
            this.mMaskRadius = context.getResources().getDimensionPixelSize(R.dimen.miuix_preference_checkable_item_mask_radius);
            this.mClipPaint = new Paint();
            this.mClipPaint.setColor(AttributeResolver.resolveColor(context, R.attr.preferenceCheckableMaskColor));
            this.mClipPaint.setAntiAlias(true);
            this.mPreferenceGroupRectMaps = new HashMap();
            this.mHeightPixels = context.getResources().getDisplayMetrics().heightPixels;
        }

        private boolean checkEndRadioButtonPreferenceCategory(RecyclerView recyclerView, int i, int i2) {
            int i3 = i + 1;
            if (i3 >= i2) {
                return false;
            }
            return !(PreferenceFragment.this.mGroupAdapter.getItem(recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i3))) instanceof RadioSetPreferenceCategory);
        }

        private void drawMask(@NonNull Canvas canvas, int i, int i2, int i3, int i4, boolean z, boolean z2, boolean z3, boolean z4) {
            PorterDuffXfermode porterDuffXfermode;
            if (!PreferenceFragment.this.mAdapterInvalid) {
                float f = (float) i2;
                float f2 = (float) i4;
                RectF rectF = new RectF((float) i, f, (float) i3, f2);
                RectF rectF2 = new RectF((float) (i + (z4 ? this.mMaskPaddingEnd : this.mMaskPaddingStart)), f, (float) (i3 - (z4 ? this.mMaskPaddingStart : this.mMaskPaddingEnd)), f2);
                Path path = new Path();
                float f3 = 0.0f;
                float f4 = z ? (float) this.mMaskRadius : 0.0f;
                if (z2) {
                    f3 = (float) this.mMaskRadius;
                }
                path.addRoundRect(rectF2, new float[]{f4, f4, f4, f4, f3, f3, f3, f3}, Direction.CW);
                int saveLayer = canvas.saveLayer(rectF, this.mClipPaint, 31);
                canvas.drawRect(rectF, this.mClipPaint);
                Paint paint = this.mClipPaint;
                if (z3) {
                    porterDuffXfermode = new PorterDuffXfermode(Mode.SRC);
                } else {
                    porterDuffXfermode = new PorterDuffXfermode(Mode.XOR);
                }
                paint.setXfermode(porterDuffXfermode);
                canvas.drawPath(path, this.mClipPaint);
                this.mClipPaint.setXfermode(null);
                canvas.restoreToCount(saveLayer);
            }
        }

        private int findNearViewY(RecyclerView recyclerView, View view, int i, int i2, boolean z) {
            if (z) {
                if (view != null && view.getBottom() + view.getHeight() < this.mHeightPixels) {
                    while (true) {
                        i++;
                        if (i >= i2) {
                            break;
                        }
                        View childAt = recyclerView.getChildAt(i);
                        if (childAt != null) {
                            return (int) childAt.getY();
                        }
                    }
                } else {
                    return -1;
                }
            } else {
                for (int i3 = i - 1; i3 > i2; i3--) {
                    View childAt2 = recyclerView.getChildAt(i3);
                    if (childAt2 != null) {
                        return ((int) childAt2.getY()) + childAt2.getHeight();
                    }
                }
            }
            return -1;
        }

        private void updateOperationTopAndBottom(RecyclerView recyclerView, PreferenceGroupRect preferenceGroupRect) {
            int size = preferenceGroupRect.preferenceList.size();
            int i = 0;
            int i2 = 0;
            int i3 = -1;
            int i4 = -1;
            for (int i5 = 0; i5 < size; i5++) {
                View childAt = recyclerView.getChildAt(((Integer) preferenceGroupRect.preferenceList.get(i5)).intValue());
                if (childAt != null) {
                    int top = childAt.getTop();
                    int bottom = childAt.getBottom();
                    if (i5 == 0) {
                        i2 = bottom;
                        i = top;
                    }
                    if (i > top) {
                        i = top;
                    }
                    if (i2 < bottom) {
                        i2 = bottom;
                    }
                }
                int i6 = preferenceGroupRect.nextViewY;
                if (i6 != -1 && i6 > preferenceGroupRect.preViewHY) {
                    i3 = i6 - this.mMaskPaddingTop;
                }
                int i7 = preferenceGroupRect.preViewHY;
                if (i7 != -1) {
                    int i8 = preferenceGroupRect.nextViewY;
                    if (i7 < i8) {
                        i4 = i8 - this.mMaskPaddingTop;
                    }
                }
            }
            preferenceGroupRect.currentEndtb = new int[]{i, i2};
            if (i3 != -1) {
                i2 = i3;
            }
            if (i4 != -1) {
                i = i4;
            }
            preferenceGroupRect.currentMovetb = new int[]{i, i2};
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
            if (r2 == 4) goto L_0x0044;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
            if (!PreferenceFragment.this.mAdapterInvalid) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                Preference item = PreferenceFragment.this.mGroupAdapter.getItem(childAdapterPosition);
                if (item != null && (item.getParent() instanceof RadioSetPreferenceCategory)) {
                    boolean isLayoutRtl = ViewUtils.isLayoutRtl(recyclerView);
                    int scrollBarSize = recyclerView.getScrollBarSize();
                    if (isLayoutRtl) {
                        rect.left = scrollBarSize;
                    } else {
                        rect.right = scrollBarSize;
                    }
                    int positionType = PreferenceFragment.this.mGroupAdapter.getPositionType(childAdapterPosition);
                    if (positionType == 1) {
                        rect.top += this.mMaskPaddingTop;
                    } else if (positionType == 2) {
                        rect.top += this.mMaskPaddingTop;
                    }
                    rect.bottom += this.mMaskPaddingBottom;
                }
            }
        }

        public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull State state) {
            int i;
            int i2;
            View view;
            int i3;
            RecyclerView recyclerView2 = recyclerView;
            if (!PreferenceFragment.this.mAdapterInvalid) {
                this.mPreferenceGroupRectMaps.clear();
                int childCount = recyclerView.getChildCount();
                int scrollBarSize = recyclerView.getScrollBarSize();
                boolean isLayoutRtl = ViewUtils.isLayoutRtl(recyclerView);
                int width = recyclerView.getWidth();
                if (isLayoutRtl) {
                    i2 = scrollBarSize;
                    i = width;
                } else {
                    i = width - scrollBarSize;
                    i2 = 0;
                }
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = recyclerView2.getChildAt(i4);
                    int childAdapterPosition = recyclerView2.getChildAdapterPosition(childAt);
                    Preference item = PreferenceFragment.this.mGroupAdapter.getItem(childAdapterPosition);
                    if (item != null && (item.getParent() instanceof RadioSetPreferenceCategory)) {
                        int positionType = PreferenceFragment.this.mGroupAdapter.getPositionType(childAdapterPosition);
                        if (positionType == 1 || positionType == 2) {
                            this.mPreferenceGroupRect = new PreferenceGroupRect();
                            PreferenceGroupRect preferenceGroupRect = this.mPreferenceGroupRect;
                            preferenceGroupRect.startRadioButtonCategory = true;
                            i3 = positionType;
                            view = childAt;
                            preferenceGroupRect.preViewHY = findNearViewY(recyclerView, childAt, i4, 0, false);
                            this.mPreferenceGroupRect.addPreference(i4);
                        } else {
                            i3 = positionType;
                            view = childAt;
                        }
                        if (this.mPreferenceGroupRect != null && (i3 == 4 || i3 == 3)) {
                            this.mPreferenceGroupRect.addPreference(i4);
                        }
                        if (this.mPreferenceGroupRect != null && (i3 == 1 || i3 == 4)) {
                            this.mPreferenceGroupRect.nextViewY = findNearViewY(recyclerView, view, i4, childCount, true);
                            this.mPreferenceGroupRect.index = this.mPreferenceGroupRectMaps.size();
                            this.mPreferenceGroupRect.endRadioButtonCategory = checkEndRadioButtonPreferenceCategory(recyclerView2, i4, childCount);
                            PreferenceGroupRect preferenceGroupRect2 = this.mPreferenceGroupRect;
                            preferenceGroupRect2.type = 4;
                            this.mPreferenceGroupRectMaps.put(Integer.valueOf(preferenceGroupRect2.index), this.mPreferenceGroupRect);
                            this.mPreferenceGroupRect = null;
                        }
                    }
                }
                PreferenceGroupRect preferenceGroupRect3 = this.mPreferenceGroupRect;
                if (preferenceGroupRect3 != null && preferenceGroupRect3.preferenceList.size() > 0) {
                    PreferenceGroupRect preferenceGroupRect4 = this.mPreferenceGroupRect;
                    preferenceGroupRect4.nextViewY = -1;
                    preferenceGroupRect4.index = this.mPreferenceGroupRectMaps.size();
                    PreferenceGroupRect preferenceGroupRect5 = this.mPreferenceGroupRect;
                    preferenceGroupRect5.endRadioButtonCategory = false;
                    preferenceGroupRect5.type = -1;
                    this.mPreferenceGroupRectMaps.put(Integer.valueOf(preferenceGroupRect5.index), this.mPreferenceGroupRect);
                    this.mPreferenceGroupRect = null;
                }
                Map map = this.mPreferenceGroupRectMaps;
                if (map != null && map.size() > 0) {
                    for (Entry value : this.mPreferenceGroupRectMaps.entrySet()) {
                        updateOperationTopAndBottom(recyclerView2, (PreferenceGroupRect) value.getValue());
                    }
                    for (Entry entry : this.mPreferenceGroupRectMaps.entrySet()) {
                        int intValue = ((Integer) entry.getKey()).intValue();
                        PreferenceGroupRect preferenceGroupRect6 = (PreferenceGroupRect) entry.getValue();
                        int i5 = preferenceGroupRect6.currentMovetb[1];
                        int i6 = intValue == 0 ? preferenceGroupRect6.currentEndtb[0] : preferenceGroupRect6.preViewHY + this.mMaskPaddingBottom;
                        Canvas canvas2 = canvas;
                        int i7 = i2;
                        int i8 = i;
                        boolean z = isLayoutRtl;
                        drawMask(canvas2, i7, i6 - this.mMaskPaddingTop, i8, i6, false, false, true, z);
                        drawMask(canvas2, i7, i5, i8, i5 + this.mMaskPaddingBottom, false, false, true, z);
                        drawMask(canvas2, i7, i6, i8, i5, true, true, false, z);
                    }
                }
            }
        }
    }

    class PreferenceGroupRect {
        public int[] currentEndtb;
        public int[] currentMovetb;
        public boolean endRadioButtonCategory;
        public int index;
        public int nextViewY;
        public int preViewHY;
        public List preferenceList;
        public boolean startRadioButtonCategory;
        public int type;

        private PreferenceGroupRect() {
            this.preferenceList = new ArrayList();
            this.currentMovetb = null;
            this.currentEndtb = null;
            this.index = 0;
            this.preViewHY = -1;
            this.nextViewY = -1;
            this.endRadioButtonCategory = false;
            this.startRadioButtonCategory = false;
            this.type = -1;
        }

        public void addPreference(int i) {
            this.preferenceList.add(Integer.valueOf(i));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PreferenceGroupRect{preferenceList=");
            sb.append(this.preferenceList);
            sb.append(", currentMovetb=");
            sb.append(Arrays.toString(this.currentMovetb));
            sb.append(", currentEndtb=");
            sb.append(Arrays.toString(this.currentEndtb));
            sb.append(", index=");
            sb.append(this.index);
            sb.append(", preViewHY=");
            sb.append(this.preViewHY);
            sb.append(", nextViewY=");
            sb.append(this.nextViewY);
            sb.append(", end=");
            sb.append(this.endRadioButtonCategory);
            sb.append('}');
            return sb.toString();
        }
    }

    /* access modifiers changed from: protected */
    public final Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        this.mGroupAdapter = new PreferenceGroupAdapter(preferenceScreen);
        boolean z = true;
        if (this.mGroupAdapter.getItemCount() >= 1) {
            z = false;
        }
        this.mAdapterInvalid = z;
        return this.mGroupAdapter;
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.miuix_preference_recyclerview, viewGroup, false);
        if (recyclerView instanceof miuix.recyclerview.widget.RecyclerView) {
            ((miuix.recyclerview.widget.RecyclerView) recyclerView).setSpringEnabled(false);
        }
        recyclerView.setLayoutManager(onCreateLayoutManager());
        this.mFrameDecoration = new FrameDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(this.mFrameDecoration);
        if (viewGroup instanceof SpringBackLayout) {
            ((SpringBackLayout) viewGroup).setTarget(recyclerView);
        }
        return recyclerView;
    }

    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment;
        boolean onPreferenceDisplayDialog = getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback ? ((OnPreferenceDisplayDialogCallback) getCallbackFragment()).onPreferenceDisplayDialog(this, preference) : false;
        if (!onPreferenceDisplayDialog && (getActivity() instanceof OnPreferenceDisplayDialogCallback)) {
            onPreferenceDisplayDialog = ((OnPreferenceDisplayDialogCallback) getActivity()).onPreferenceDisplayDialog(this, preference);
        }
        if (!onPreferenceDisplayDialog) {
            FragmentManager fragmentManager = getFragmentManager();
            String str = DIALOG_FRAGMENT_TAG;
            if (fragmentManager.findFragmentByTag(str) == null) {
                if (preference instanceof EditTextPreference) {
                    dialogFragment = EditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else if (preference instanceof ListPreference) {
                    dialogFragment = ListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else if (preference instanceof MultiSelectListPreference) {
                    dialogFragment = MultiSelectListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot display dialog for an unknown Preference type: ");
                    sb.append(preference.getClass().getSimpleName());
                    sb.append(". Make sure to implement onPreferenceDisplayDialog() to handle displaying a custom dialog for this Preference.");
                    throw new IllegalArgumentException(sb.toString());
                }
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), str);
            }
        }
    }
}
