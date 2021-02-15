package com.bumptech.glide.manager;

import androidx.annotation.NonNull;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class TargetTracker implements LifecycleListener {
    private final Set targets = Collections.newSetFromMap(new WeakHashMap());

    public void clear() {
        this.targets.clear();
    }

    @NonNull
    public List getAll() {
        return Util.getSnapshot(this.targets);
    }

    public void onDestroy() {
        for (Target onDestroy : Util.getSnapshot(this.targets)) {
            onDestroy.onDestroy();
        }
    }

    public void onStart() {
        for (Target onStart : Util.getSnapshot(this.targets)) {
            onStart.onStart();
        }
    }

    public void onStop() {
        for (Target onStop : Util.getSnapshot(this.targets)) {
            onStop.onStop();
        }
    }

    public void track(@NonNull Target target) {
        this.targets.add(target);
    }

    public void untrack(@NonNull Target target) {
        this.targets.remove(target);
    }
}
