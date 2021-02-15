package androidx.concurrent.futures;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.common.util.concurrent.ListenableFuture;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public final class ResolvableFuture extends AbstractResolvableFuture {
    private ResolvableFuture() {
    }

    public static ResolvableFuture create() {
        return new ResolvableFuture();
    }

    public boolean set(@Nullable Object obj) {
        return super.set(obj);
    }

    public boolean setException(Throwable th) {
        return super.setException(th);
    }

    public boolean setFuture(ListenableFuture listenableFuture) {
        return super.setFuture(listenableFuture);
    }
}
