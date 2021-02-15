package androidx.core.content.pm;

import androidx.annotation.AnyThread;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.WorkerThread;
import java.util.ArrayList;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public abstract class ShortcutInfoCompatSaver {

    @RestrictTo({Scope.LIBRARY})
    public class NoopImpl extends ShortcutInfoCompatSaver {
        public Void addShortcuts(List list) {
            return null;
        }

        public Void removeAllShortcuts() {
            return null;
        }

        public Void removeShortcuts(List list) {
            return null;
        }
    }

    @AnyThread
    public abstract Object addShortcuts(List list);

    @WorkerThread
    public List getShortcuts() {
        return new ArrayList();
    }

    @AnyThread
    public abstract Object removeAllShortcuts();

    @AnyThread
    public abstract Object removeShortcuts(List list);
}
