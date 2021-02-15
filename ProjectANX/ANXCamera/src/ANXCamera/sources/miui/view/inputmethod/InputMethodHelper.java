package miui.view.inputmethod;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import miui.util.AppConstants;
import miui.util.SoftReferenceSingleton;

public class InputMethodHelper {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public InputMethodHelper createInstance() {
            return new InputMethodHelper();
        }
    };
    private InputMethodManager mManager;

    private InputMethodHelper() {
        this.mManager = (InputMethodManager) AppConstants.getCurrentApplication().getSystemService("input_method");
    }

    public static InputMethodHelper getInstance() {
        return (InputMethodHelper) INSTANCE.get();
    }

    public InputMethodManager getManager() {
        return this.mManager;
    }

    public void hideKeyBoard(EditText editText) {
        this.mManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
    }

    public void showKeyBoard(EditText editText) {
        editText.requestFocus();
        this.mManager.viewClicked(editText);
        this.mManager.showSoftInput(editText, 0);
    }
}
