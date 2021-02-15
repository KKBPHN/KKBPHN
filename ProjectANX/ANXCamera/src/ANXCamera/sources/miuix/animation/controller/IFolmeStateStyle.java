package miuix.animation.controller;

import miuix.animation.IAnimTarget;
import miuix.animation.IStateStyle;

public interface IFolmeStateStyle extends IStateStyle {
    void addState(AnimState animState);

    AnimState getState(Object obj);

    IAnimTarget getTarget();
}
