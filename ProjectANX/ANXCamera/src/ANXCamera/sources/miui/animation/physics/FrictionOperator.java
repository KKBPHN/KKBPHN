package miui.animation.physics;

public class FrictionOperator implements PhysicsOperator {
    private final double friction;

    public FrictionOperator(float f) {
        this.friction = 1.0d - Math.pow(2.718281828459045d, (double) (f * -4.2f));
    }

    public double updateVelocity(double d, float f, double... dArr) {
        return d * Math.pow(1.0d - this.friction, (double) f);
    }
}
