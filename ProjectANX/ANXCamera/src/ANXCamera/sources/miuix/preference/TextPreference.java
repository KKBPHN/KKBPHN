package miuix.preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class TextPreference extends Preference {
    private CharSequence mText;
    private TextProvider mTextProvider;
    private int mTextRes;

    public interface TextProvider {
        CharSequence provideText(TextPreference textPreference);
    }

    public TextPreference(Context context) {
        this(context, null);
    }

    public TextPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textPreferenceStyle);
    }

    public TextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CharSequence getText() {
        return getTextProvider() != null ? getTextProvider().provideText(this) : this.mText;
    }

    @Nullable
    public final TextProvider getTextProvider() {
        return this.mTextProvider;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.text_right);
        if (textView != null) {
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                textView.setText(text);
                i = 0;
            } else {
                i = 8;
            }
            textView.setVisibility(i);
        }
    }

    public void setText(int i) {
        setText(getContext().getString(i));
        this.mTextRes = i;
    }

    public void setText(String str) {
        if (getTextProvider() != null) {
            throw new IllegalStateException("Preference already has a TextProvider set.");
        } else if (!TextUtils.equals(str, this.mText)) {
            this.mTextRes = 0;
            this.mText = str;
            notifyChanged();
        }
    }

    public final void setTextProvider(@Nullable TextProvider textProvider) {
        this.mTextProvider = textProvider;
        notifyChanged();
    }
}
