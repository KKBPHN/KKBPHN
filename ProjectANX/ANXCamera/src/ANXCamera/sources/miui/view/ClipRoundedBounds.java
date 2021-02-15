package miui.view;

import android.graphics.RectF;

public interface ClipRoundedBounds {
    boolean isClipRoundedCorner();

    void setClipRoundedBounds(RectF rectF, int i);

    void setClipRoundedBounds(RectF rectF, float[] fArr);
}
