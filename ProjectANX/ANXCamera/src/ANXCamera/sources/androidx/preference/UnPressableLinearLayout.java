package androidx.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class UnPressableLinearLayout extends LinearLayout {
    public UnPressableLinearLayout(Context context) {
        this(context, null);
    }

    public UnPressableLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void dispatchSetPressed(boolean z) {
    }
}
