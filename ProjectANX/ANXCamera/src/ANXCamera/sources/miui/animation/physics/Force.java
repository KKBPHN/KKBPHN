package miui.animation.physics;

interface Force {
    float getAcceleration(float f, float f2);

    boolean isAtEquilibrium(float f, float f2);
}
