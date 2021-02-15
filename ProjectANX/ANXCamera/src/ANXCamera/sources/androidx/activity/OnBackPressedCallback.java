package androidx.activity;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback {
    private CopyOnWriteArrayList mCancellables = new CopyOnWriteArrayList();
    private boolean mEnabled;

    public OnBackPressedCallback(boolean z) {
        this.mEnabled = z;
    }

    /* access modifiers changed from: 0000 */
    public void addCancellable(@NonNull Cancellable cancellable) {
        this.mCancellables.add(cancellable);
    }

    @MainThread
    public abstract void handleOnBackPressed();

    @MainThread
    public final boolean isEnabled() {
        return this.mEnabled;
    }

    @MainThread
    public final void remove() {
        Iterator it = this.mCancellables.iterator();
        while (it.hasNext()) {
            ((Cancellable) it.next()).cancel();
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeCancellable(@NonNull Cancellable cancellable) {
        this.mCancellables.remove(cancellable);
    }

    @MainThread
    public final void setEnabled(boolean z) {
        this.mEnabled = z;
    }
}
