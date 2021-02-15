package com.android.camera.features.gif;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import java.util.regex.Pattern;

public class LimitInputEditText extends AdaptiveEditText {

    class InnerInputConnection extends InputConnectionWrapper {
        private Pattern emoji = Pattern.compile("[🀀-🏿]|[🐀-🟿]|[🤀-🯿]|[☀-⟿]", 66);

        public InnerInputConnection(InputConnection inputConnection, boolean z) {
            super(inputConnection, z);
        }

        public boolean commitText(CharSequence charSequence, int i) {
            if (this.emoji.matcher(charSequence).find()) {
                return false;
            }
            return super.commitText(charSequence, i);
        }
    }

    public LimitInputEditText(Context context) {
        super(context);
    }

    public LimitInputEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LimitInputEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return new InnerInputConnection(super.onCreateInputConnection(editorInfo), false);
    }
}
