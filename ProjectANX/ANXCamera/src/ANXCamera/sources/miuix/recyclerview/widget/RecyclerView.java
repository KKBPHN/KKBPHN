package miuix.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.SpringRecyclerView;

public class RecyclerView extends SpringRecyclerView {
    private static final int MIN_FLING_VELOCITY = 300;

    public RecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.recyclerViewStyle);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setItemAnimator(new MiuiDefaultItemAnimator());
    }

    public boolean fling(int i, int i2) {
        if (Math.abs(i) < 300) {
            i = 0;
        }
        if (Math.abs(i2) < 300) {
            i2 = 0;
        }
        if (i == 0 && i2 == 0) {
            return false;
        }
        return super.fling(i, i2);
    }
}
