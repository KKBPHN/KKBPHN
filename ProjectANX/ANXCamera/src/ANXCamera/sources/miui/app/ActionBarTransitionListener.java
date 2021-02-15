package miui.app;

import java.util.Collection;

public interface ActionBarTransitionListener {
    void onActionBarMove(float f, float f2);

    void onTransitionBegin(Object obj);

    void onTransitionComplete(Object obj);

    void onTransitionUpdate(Object obj, Collection collection);
}
