package miuix.view.inputmethod;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import miuix.core.util.SoftReferenceSingleton;

public class InputMethodHelper {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public InputMethodHelper createInstance(Object obj) {
            return new InputMethodHelper((Context) obj);
        }
    };
    private InputMethodManager mManager;

    private InputMethodHelper(Context context) {
        this.mManager = (InputMethodManager) context.getApplicationContext().getSystemService("input_method");
    }

    public static InputMethodHelper getInstance(Context context) {
        return (InputMethodHelper) INSTANCE.get(context);
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
