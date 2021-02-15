package miui.animation.controller;

import miui.animation.IAnimTarget;
import miui.animation.IStateStyle;

public interface IFolmeStateStyle extends IStateStyle {
    void addState(AnimState animState);

    AnimState getState(Object obj);

    IAnimTarget getTarget();
}
