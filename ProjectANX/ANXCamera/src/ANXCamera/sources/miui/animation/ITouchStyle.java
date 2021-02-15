package miui.animation;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import miui.animation.base.AnimConfig;

public interface ITouchStyle extends IStateContainer {

    public enum TouchType {
        UP,
        DOWN
    }

    void bindViewOfListItem(View view, AnimConfig... animConfigArr);

    void handleTouchOf(View view, boolean z, AnimConfig... animConfigArr);

    void handleTouchOf(View view, AnimConfig... animConfigArr);

    void ignoreTouchOf(View view);

    void onMotionEvent(MotionEvent motionEvent);

    void onMotionEventEx(View view, MotionEvent motionEvent, AnimConfig... animConfigArr);

    ITouchStyle setAlpha(float f, TouchType... touchTypeArr);

    ITouchStyle setBackgroundColor(float f, float f2, float f3, float f4);

    ITouchStyle setBackgroundColor(int i);

    ITouchStyle setScale(float f, TouchType... touchTypeArr);

    ITouchStyle setTint(float f, float f2, float f3, float f4);

    ITouchStyle setTint(int i);

    ITouchStyle setTintMode(int i);

    void setTouchDown();

    void setTouchUp();

    void touchDown(AnimConfig... animConfigArr);

    void touchUp(AnimConfig... animConfigArr);

    ITouchStyle useVarFont(TextView textView, int i, int i2, int i3);
}
