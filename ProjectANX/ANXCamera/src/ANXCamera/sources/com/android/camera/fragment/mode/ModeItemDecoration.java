package com.android.camera.fragment.mode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.data.DataRepository;

public class ModeItemDecoration extends ItemDecoration {
    private int mBottomMargin;
    private Context mContext;
    private int mHorMargin;
    private int mModeListHorMargin;
    private int mPerLineCount;
    private int mTopMargin;
    private int mType;

    public ModeItemDecoration(Context context, IMoreMode iMoreMode, int i) {
        int i2;
        this.mContext = context;
        this.mType = i;
        this.mPerLineCount = iMoreMode.getCountPerLine();
        boolean z = false;
        int panelWidth = MoreModeHelper.getPanelWidth(context, i == 1);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(i == 3 ? R.dimen.mode_icon_size_new : R.dimen.mode_item_width);
        Resources resources = this.mContext.getResources();
        int i3 = i == 0 ? R.dimen.mode_list_hor_margin_normal : i == 3 ? R.dimen.mode_list_hor_margin_normal_new : R.dimen.mode_list_hor_margin;
        this.mModeListHorMargin = resources.getDimensionPixelSize(i3);
        int i4 = this.mPerLineCount;
        this.mHorMargin = ((panelWidth - (i4 * dimensionPixelSize)) - (this.mModeListHorMargin * 2)) / (i4 * 2);
        this.mTopMargin = MoreModeHelper.getTopMarginForNormal(context, i);
        if (i == 2 || i == 1) {
            i2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_height) - dimensionPixelSize;
        } else {
            int uiStyle = DataRepository.dataItemRunning().getUiStyle();
            if (i == 3) {
                z = true;
            }
            i2 = Display.getMoreModeTabMarginVer(uiStyle, z);
        }
        this.mBottomMargin = i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0056, code lost:
        if (com.android.camera.fragment.mode.MoreModeHelper.isFooter4PopupStyle(r8.getChildAdapterPosition(r7), com.android.camera.data.DataRepository.dataItemGlobal().getComponentModuleList().getMoreItems().size()) != false) goto L_0x001e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005d, code lost:
        if (r8.getChildAdapterPosition(r7) == 0) goto L_0x001e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        int i = this.mHorMargin;
        int i2 = this.mBottomMargin;
        int i3 = this.mTopMargin;
        int i4 = this.mType;
        if (i4 != 0) {
            if (i4 != 1) {
                if (i4 == 2) {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    if (recyclerView.getAdapter() != null) {
                        int itemViewType = recyclerView.getAdapter().getItemViewType(childAdapterPosition);
                        if (itemViewType == 2 || itemViewType == 1 || itemViewType == 6) {
                            i = 0;
                            i2 = 0;
                        }
                    }
                } else if (i4 == 3) {
                    if (recyclerView.getChildAdapterPosition(view) != 0) {
                        i3 = 0;
                    }
                }
                rect.set(i, i3, i, i2);
            }
        }
        i2 = 0;
        rect.set(i, i3, i, i2);
    }

    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull State state) {
        super.onDraw(canvas, recyclerView, state);
    }

    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull State state) {
        super.onDrawOver(canvas, recyclerView, state);
    }
}
