package miuix.animation;

import miuix.animation.property.FloatProperty;

public interface ICancelableStyle {
    void cancel();

    void cancel(String... strArr);

    void cancel(FloatProperty... floatPropertyArr);

    void end(Object... objArr);
}
