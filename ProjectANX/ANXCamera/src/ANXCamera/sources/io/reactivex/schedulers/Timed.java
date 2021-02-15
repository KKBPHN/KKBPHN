package io.reactivex.schedulers;

import io.reactivex.annotations.NonNull;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.TimeUnit;

public final class Timed {
    final long time;
    final TimeUnit unit;
    final Object value;

    public Timed(@NonNull Object obj, long j, @NonNull TimeUnit timeUnit) {
        this.value = obj;
        this.time = j;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        this.unit = timeUnit;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Timed)) {
            return false;
        }
        Timed timed = (Timed) obj;
        return ObjectHelper.equals(this.value, timed.value) && this.time == timed.time && ObjectHelper.equals(this.unit, timed.unit);
    }

    public int hashCode() {
        Object obj = this.value;
        int hashCode = (obj != null ? obj.hashCode() : 0) * 31;
        long j = this.time;
        return ((hashCode + ((int) (j ^ (j >>> 31)))) * 31) + this.unit.hashCode();
    }

    public long time() {
        return this.time;
    }

    public long time(@NonNull TimeUnit timeUnit) {
        return timeUnit.convert(this.time, this.unit);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timed[time=");
        sb.append(this.time);
        sb.append(", unit=");
        sb.append(this.unit);
        sb.append(", value=");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }

    @NonNull
    public TimeUnit unit() {
        return this.unit;
    }

    @NonNull
    public Object value() {
        return this.value;
    }
}
