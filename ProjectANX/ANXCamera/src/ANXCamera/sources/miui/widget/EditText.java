package miui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

public class EditText extends android.widget.EditText {
    private static final int LEVEL_ERROR = 404;
    private static final int LEVEL_NORMAL = 0;

    public EditText(Context context) {
        this(context, null);
    }

    public EditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842862);
    }

    public EditText(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public EditText(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setMiuiStyleError(CharSequence charSequence) {
        getBackground().setLevel(TextUtils.isEmpty(charSequence) ? 0 : 404);
    }
}
