package androidx.lifecycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ViewModelStore {
    private final HashMap mMap = new HashMap();

    public final void clear() {
        for (ViewModel clear : this.mMap.values()) {
            clear.clear();
        }
        this.mMap.clear();
    }

    /* access modifiers changed from: 0000 */
    public final ViewModel get(String str) {
        return (ViewModel) this.mMap.get(str);
    }

    /* access modifiers changed from: 0000 */
    public Set keys() {
        return new HashSet(this.mMap.keySet());
    }

    /* access modifiers changed from: 0000 */
    public final void put(String str, ViewModel viewModel) {
        ViewModel viewModel2 = (ViewModel) this.mMap.put(str, viewModel);
        if (viewModel2 != null) {
            viewModel2.onCleared();
        }
    }
}
