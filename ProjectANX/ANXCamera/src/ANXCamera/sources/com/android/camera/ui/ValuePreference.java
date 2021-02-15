package com.android.camera.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.camera.R;
import miuix.preference.TextPreference;

public class ValuePreference extends TextPreference {
    private int mMaxEms;

    public ValuePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ValuePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ValuePreference, 0, 0);
        this.mMaxEms = obtainStyledAttributes.getInt(1, -1);
        obtainStyledAttributes.recycle();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.text_right);
        if (textView != null && this.mMaxEms != -1) {
            textView.setEllipsize(TruncateAt.END);
            textView.setMaxEms(this.mMaxEms);
            textView.setSingleLine();
        }
    }

    public void setValue(String str) {
        setText(str);
    }
}
