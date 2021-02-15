package miuix.animation.listener;

import java.util.Collection;
import miuix.animation.internal.AnimRunner;
import miuix.animation.property.FloatProperty;
import miuix.animation.styles.PropertyStyle;

public class UpdateInfo {
    public PropertyStyle anim;
    public long endTime;
    public boolean isCanceled;
    public boolean isCompleted;
    public boolean isEndByUser;
    private Number mValue;
    public FloatProperty property;
    public long transId;
    public float velocity;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miuix.animation.listener.UpdateInfo>, for r2v0, types: [java.util.Collection, java.util.Collection<miuix.animation.listener.UpdateInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static UpdateInfo findBy(Collection<UpdateInfo> collection, FloatProperty floatProperty) {
        for (UpdateInfo updateInfo : collection) {
            if (updateInfo.property.equals(floatProperty)) {
                return updateInfo;
            }
        }
        return null;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miuix.animation.listener.UpdateInfo>, for r2v0, types: [java.util.Collection, java.util.Collection<miuix.animation.listener.UpdateInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static UpdateInfo findByName(Collection<UpdateInfo> collection, String str) {
        for (UpdateInfo updateInfo : collection) {
            if (updateInfo.property.getName().equals(str)) {
                return updateInfo;
            }
        }
        return null;
    }

    public float getFloatValue() {
        return this.mValue.floatValue();
    }

    public int getIntValue() {
        return this.mValue.intValue();
    }

    public Class getType() {
        return this.mValue.getClass();
    }

    public Object getValue(Class cls) {
        return (cls == Float.class || cls == Float.TYPE) ? Float.valueOf(this.mValue.floatValue()) : (cls == Double.class || cls == Double.TYPE) ? Double.valueOf(this.mValue.doubleValue()) : Integer.valueOf(this.mValue.intValue());
    }

    public void reset() {
        this.isEndByUser = false;
        this.isCompleted = false;
        this.isCanceled = false;
    }

    public void setComplete(boolean z) {
        this.isCompleted = z;
        if (this.isCompleted) {
            this.endTime = AnimRunner.getInst().getRunningTime();
        }
    }

    public void setValue(Number number) {
        this.mValue = number;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateInfo{property=");
        sb.append(this.property);
        sb.append(", mValue=");
        sb.append(this.mValue);
        sb.append(", velocity=");
        sb.append(this.velocity);
        sb.append(", isCompleted=");
        sb.append(this.isCompleted);
        sb.append('}');
        return sb.toString();
    }
}
