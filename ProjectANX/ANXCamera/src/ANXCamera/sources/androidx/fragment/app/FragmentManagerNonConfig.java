package androidx.fragment.app;

import androidx.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

@Deprecated
public class FragmentManagerNonConfig {
    @Nullable
    private final Map mChildNonConfigs;
    @Nullable
    private final Collection mFragments;
    @Nullable
    private final Map mViewModelStores;

    FragmentManagerNonConfig(@Nullable Collection collection, @Nullable Map map, @Nullable Map map2) {
        this.mFragments = collection;
        this.mChildNonConfigs = map;
        this.mViewModelStores = map2;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Map getChildNonConfigs() {
        return this.mChildNonConfigs;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Collection getFragments() {
        return this.mFragments;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Map getViewModelStores() {
        return this.mViewModelStores;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRetaining(Fragment fragment) {
        Collection collection = this.mFragments;
        if (collection == null) {
            return false;
        }
        return collection.contains(fragment);
    }
}
