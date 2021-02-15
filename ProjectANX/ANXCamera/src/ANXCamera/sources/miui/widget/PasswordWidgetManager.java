package miui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import com.miui.internal.R;
import miui.util.AttributeResolver;
import miui.widget.StateEditText.WidgetManager;

public class PasswordWidgetManager extends WidgetManager {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private boolean mIsChecked = false;
    private StateEditText mMaster;
    private Drawable mWidgetDrawable;

    public PasswordWidgetManager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWidgetDrawable = AttributeResolver.resolveDrawable(context, R.attr.visibilityIcon);
    }

    public Drawable[] getWidgetDrawables() {
        return new Drawable[]{this.mWidgetDrawable};
    }

    public void onAttached(StateEditText stateEditText) {
        this.mMaster = stateEditText;
    }

    public void onWidgetClick(int i) {
        this.mIsChecked = !this.mIsChecked;
        StateEditText stateEditText = this.mMaster;
        if (stateEditText != null) {
            int selectionStart = stateEditText.getSelectionStart();
            int selectionEnd = this.mMaster.getSelectionEnd();
            this.mMaster.setTransformationMethod(this.mIsChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            this.mMaster.setSelection(selectionStart, selectionEnd);
        }
        this.mWidgetDrawable.setState(this.mIsChecked ? CHECKED_STATE_SET : new int[0]);
    }
}
