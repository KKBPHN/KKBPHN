package androidx.core.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Pair {
    @Nullable
    public final Object first;
    @Nullable
    public final Object second;

    public Pair(@Nullable Object obj, @Nullable Object obj2) {
        this.first = obj;
        this.second = obj2;
    }

    @NonNull
    public static Pair create(@Nullable Object obj, @Nullable Object obj2) {
        return new Pair(obj, obj2);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        if (ObjectsCompat.equals(pair.first, this.first) && ObjectsCompat.equals(pair.second, this.second)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        Object obj = this.first;
        int i = 0;
        int hashCode = obj == null ? 0 : obj.hashCode();
        Object obj2 = this.second;
        if (obj2 != null) {
            i = obj2.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
