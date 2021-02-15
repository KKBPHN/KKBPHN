package miuix.animation.property;

public class IntValueProperty extends ValueProperty implements IIntValueProperty {
    public IntValueProperty(String str) {
        super(str);
    }

    public int getIntValue(Object obj) {
        if (obj instanceof ValueTargetObject) {
            Integer num = (Integer) ((ValueTargetObject) obj).getPropertyValue(getName(), Integer.TYPE);
            if (num != null) {
                return num.intValue();
            }
        }
        return 0;
    }

    public void setIntValue(Object obj, int i) {
        if (obj instanceof ValueTargetObject) {
            ((ValueTargetObject) obj).setPropertyValue(getName(), Integer.TYPE, Integer.valueOf(i));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IntValueProperty{name=");
        sb.append(getName());
        sb.append('}');
        return sb.toString();
    }
}
