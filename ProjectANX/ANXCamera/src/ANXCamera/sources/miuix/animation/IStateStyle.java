package miuix.animation;

import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.EaseManager.EaseStyle;

public interface IStateStyle extends IStateContainer {
    IStateStyle add(String str, Object obj, long... jArr);

    IStateStyle add(FloatProperty floatProperty, Object obj, long... jArr);

    IStateStyle addInitProperty(String str, Object obj);

    IStateStyle addInitProperty(FloatProperty floatProperty, Object obj);

    IStateStyle addListener(TransitionListener transitionListener);

    IStateStyle autoSetTo(Object... objArr);

    IStateStyle fromTo(Object obj, Object obj2, AnimConfig... animConfigArr);

    AnimState getCurrentState();

    long predictDuration(Object... objArr);

    IStateStyle removeListener(TransitionListener transitionListener);

    IStateStyle set(Object obj);

    IStateStyle setConfig(AnimConfig animConfig, FloatProperty... floatPropertyArr);

    IStateStyle setEase(int i, float... fArr);

    IStateStyle setEase(FloatProperty floatProperty, int i, float... fArr);

    IStateStyle setEase(EaseStyle easeStyle, FloatProperty... floatPropertyArr);

    IStateStyle setTo(Object obj);

    IStateStyle setTo(Object obj, AnimConfig... animConfigArr);

    IStateStyle setTo(Object... objArr);

    IStateStyle setTransitionFlags(long j, FloatProperty... floatPropertyArr);

    IStateStyle setup(Object obj);

    IStateStyle then(Object obj, AnimConfig... animConfigArr);

    IStateStyle then(Object... objArr);

    IStateStyle to(Object obj, AnimConfig... animConfigArr);

    IStateStyle to(Object... objArr);

    IStateStyle to(AnimConfig... animConfigArr);
}
