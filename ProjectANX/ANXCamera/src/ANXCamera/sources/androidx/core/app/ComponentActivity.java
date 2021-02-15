package androidx.core.app;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.KeyEventDispatcher.Component;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class ComponentActivity extends Activity implements Component {
    private SimpleArrayMap mExtraDataMap = new SimpleArrayMap();

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public class ExtraData {
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return KeyEventDispatcher.dispatchKeyEvent(this, decorView, this, keyEvent);
        }
        return true;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return super.dispatchKeyShortcutEvent(keyEvent);
        }
        return true;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public ExtraData getExtraData(Class cls) {
        return (ExtraData) this.mExtraDataMap.get(cls);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
}
