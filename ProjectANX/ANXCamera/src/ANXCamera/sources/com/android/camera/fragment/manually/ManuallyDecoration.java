package com.android.camera.fragment.manually;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;

public class ManuallyDecoration extends ItemDecoration {
    private Paint mDividerPaint;
    private int mDividerWidth = 1;
    private int mSpanCount;

    public ManuallyDecoration(int i, @ColorInt int i2) {
        this.mDividerWidth = i;
        this.mDividerPaint = new Paint();
        this.mDividerPaint.setAntiAlias(true);
        this.mDividerPaint.setColor(i2);
    }

    public void getItemOffsets(Rect rect, int i, RecyclerView recyclerView) {
        int i2 = this.mSpanCount;
        int i3 = i2 - 1;
        int i4 = this.mDividerWidth;
        int i5 = (i3 * i4) / i2;
        int i6 = (i % i2) * (i4 - i5);
        int i7 = i5 - i6;
        if (i == 0) {
            i6 = i4;
        }
        rect.set(i6, 0, i7, i4);
    }

    public void onDraw(Canvas canvas, RecyclerView recyclerView, State state) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int left = childAt.getLeft();
            int right = childAt.getRight();
            int bottom = childAt.getBottom() + layoutParams.bottomMargin;
            int i2 = this.mDividerWidth + bottom;
            Paint paint = this.mDividerPaint;
            if (paint != null) {
                canvas.drawRect((float) left, (float) bottom, (float) right, (float) i2, paint);
            }
            int top = childAt.getTop();
            int bottom2 = childAt.getBottom() + this.mDividerWidth;
            int right2 = childAt.getRight() + layoutParams.rightMargin;
            int i3 = this.mDividerWidth + right2;
            Paint paint2 = this.mDividerPaint;
            if (paint2 != null) {
                canvas.drawRect((float) right2, (float) top, (float) i3, (float) bottom2, paint2);
            }
        }
    }

    public void setStyle(int i) {
        this.mSpanCount = i;
    }
}
