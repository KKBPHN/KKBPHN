package miui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import miui.R;

public class RadioButtonPreference extends CheckBoxPreference {
    public RadioButtonPreference(Context context) {
        this(context, null);
    }

    public RadioButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.radioButtonPreferenceStyle);
    }

    public RadioButtonPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.miui.internal.R.styleable.RadioButtonPreference, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(com.miui.internal.R.styleable.RadioButtonPreference_android_layout, com.miui.internal.R.layout.preference_radiobutton);
        int resourceId2 = obtainStyledAttributes.getResourceId(com.miui.internal.R.styleable.RadioButtonPreference_android_widgetLayout, 0);
        obtainStyledAttributes.recycle();
        if (getLayoutResource() != resourceId) {
            setLayoutResource(com.miui.internal.R.layout.preference_radiobutton);
        }
        if (getWidgetLayoutResource() != resourceId2) {
            setWidgetLayoutResource(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onBindView(View view) {
        super.onBindView(view);
        View findViewById = view.findViewById(16908310);
        if (findViewById != null && (findViewById instanceof CheckedTextView)) {
            CheckedTextView checkedTextView = (CheckedTextView) findViewById;
            checkedTextView.setCheckMarkDrawable(0);
            checkedTextView.setChecked(isChecked());
        }
        View findViewById2 = view.findViewById(16908304);
        if (findViewById2 != null && (findViewById2 instanceof Checkable)) {
            ((Checkable) findViewById2).setChecked(isChecked());
        }
        View findViewById3 = view.findViewById(16908289);
        if (findViewById3 != null) {
            findViewById3.setImportantForAccessibility(2);
        }
    }
}
