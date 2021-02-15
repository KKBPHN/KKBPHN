package miui.animation;

import miui.animation.base.AnimConfig;

public interface IStateContainer extends ICancelableStyle {
    void addConfig(Object obj, AnimConfig... animConfigArr);

    void clean();

    void enableDefaultAnim(boolean z);
}
