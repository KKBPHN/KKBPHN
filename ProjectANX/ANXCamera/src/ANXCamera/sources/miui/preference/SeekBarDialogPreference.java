package miui.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.miui.internal.R;
import miui.widget.SeekBar;

public class SeekBarDialogPreference extends DialogPreference {
    private Drawable mMyIcon = getDialogIcon();

    public SeekBarDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogLayoutResource(R.layout.seekbar_dialog);
        createActionButtons();
        setDialogIcon(null);
    }

    protected static SeekBar getSeekBar(View view) {
        return (SeekBar) view.findViewById(R.id.seekbar);
    }

    /* access modifiers changed from: protected */
    public void createActionButtons() {
        setPositiveButtonText(17039370);
        setNegativeButtonText(17039360);
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        Drawable drawable = this.mMyIcon;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(8);
        }
    }
}
