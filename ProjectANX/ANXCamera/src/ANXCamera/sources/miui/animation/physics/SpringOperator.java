package miui.animation.physics;

public class SpringOperator implements PhysicsOperator {
    private final double damping;
    private final double tension;

    public SpringOperator(float f, float f2) {
        double d = (double) f2;
        this.tension = Math.pow(6.283185307179586d / d, 2.0d);
        this.damping = (((double) f) * 12.566370614359172d) / d;
    }

    public double updateVelocity(double d, float f, double... dArr) {
        double d2 = (double) f;
        return (d * (1.0d - (this.damping * d2))) + ((double) ((float) (this.tension * (dArr[0] - dArr[1]) * d2)));
    }
}
