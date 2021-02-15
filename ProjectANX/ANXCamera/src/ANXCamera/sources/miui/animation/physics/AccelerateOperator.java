package miui.animation.physics;

public class AccelerateOperator implements PhysicsOperator {
    private final float accelerate;

    public AccelerateOperator(float f) {
        this.accelerate = f * 1000.0f;
    }

    public double updateVelocity(double d, float f, double... dArr) {
        return d + ((double) (this.accelerate * f));
    }
}
