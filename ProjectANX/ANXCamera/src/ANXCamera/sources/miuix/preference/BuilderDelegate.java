package miuix.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import androidx.annotation.Nullable;
import miui.app.AlertDialog;

class BuilderDelegate extends Builder {
    private AlertDialog.Builder mMiuixBuilder;

    public BuilderDelegate(Context context, int i, AlertDialog.Builder builder) {
        super(context, i);
        this.mMiuixBuilder = builder;
    }

    public BuilderDelegate(Context context, AlertDialog.Builder builder) {
        this(context, 0, builder);
    }

    public Builder setAdapter(ListAdapter listAdapter, OnClickListener onClickListener) {
        this.mMiuixBuilder.setAdapter(listAdapter, onClickListener);
        return this;
    }

    public Builder setCancelable(boolean z) {
        this.mMiuixBuilder.setCancelable(z);
        return this;
    }

    public Builder setCursor(Cursor cursor, OnClickListener onClickListener, String str) {
        this.mMiuixBuilder.setCursor(cursor, onClickListener, str);
        return this;
    }

    public Builder setCustomTitle(@Nullable View view) {
        this.mMiuixBuilder.setCustomTitle(view);
        return this;
    }

    public Builder setIcon(int i) {
        this.mMiuixBuilder.setIcon(i);
        return this;
    }

    public Builder setIcon(@Nullable Drawable drawable) {
        this.mMiuixBuilder.setIcon(drawable);
        return this;
    }

    public Builder setIconAttribute(int i) {
        this.mMiuixBuilder.setIconAttribute(i);
        return this;
    }

    public Builder setItems(int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setItems(i, onClickListener);
        return this;
    }

    public Builder setItems(CharSequence[] charSequenceArr, OnClickListener onClickListener) {
        this.mMiuixBuilder.setItems(charSequenceArr, onClickListener);
        return this;
    }

    public Builder setMessage(int i) {
        this.mMiuixBuilder.setMessage(i);
        return this;
    }

    public Builder setMessage(@Nullable CharSequence charSequence) {
        this.mMiuixBuilder.setMessage(charSequence);
        return this;
    }

    public Builder setMultiChoiceItems(int i, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
        this.mMiuixBuilder.setMultiChoiceItems(i, zArr, onMultiChoiceClickListener);
        return this;
    }

    public Builder setMultiChoiceItems(Cursor cursor, String str, String str2, OnMultiChoiceClickListener onMultiChoiceClickListener) {
        this.mMiuixBuilder.setMultiChoiceItems(cursor, str, str2, onMultiChoiceClickListener);
        return this;
    }

    public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
        this.mMiuixBuilder.setMultiChoiceItems(charSequenceArr, zArr, onMultiChoiceClickListener);
        return this;
    }

    public Builder setNegativeButton(int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setNegativeButton(i, onClickListener);
        return this;
    }

    public Builder setNegativeButton(CharSequence charSequence, OnClickListener onClickListener) {
        this.mMiuixBuilder.setNegativeButton(charSequence, onClickListener);
        return this;
    }

    public Builder setNeutralButton(int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setNeutralButton(i, onClickListener);
        return this;
    }

    public Builder setNeutralButton(CharSequence charSequence, OnClickListener onClickListener) {
        this.mMiuixBuilder.setNeutralButton(charSequence, onClickListener);
        return this;
    }

    public Builder setOnCancelListener(OnCancelListener onCancelListener) {
        this.mMiuixBuilder.setOnCancelListener(onCancelListener);
        return this;
    }

    public Builder setOnDismissListener(OnDismissListener onDismissListener) {
        this.mMiuixBuilder.setOnDismissListener(onDismissListener);
        return this;
    }

    public Builder setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mMiuixBuilder.setOnItemSelectedListener(onItemSelectedListener);
        return this;
    }

    public Builder setOnKeyListener(OnKeyListener onKeyListener) {
        this.mMiuixBuilder.setOnKeyListener(onKeyListener);
        return this;
    }

    public Builder setPositiveButton(int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setPositiveButton(i, onClickListener);
        return this;
    }

    public Builder setPositiveButton(CharSequence charSequence, OnClickListener onClickListener) {
        this.mMiuixBuilder.setPositiveButton(charSequence, onClickListener);
        return this;
    }

    public Builder setSingleChoiceItems(int i, int i2, OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(i, i2, onClickListener);
        return this;
    }

    public Builder setSingleChoiceItems(Cursor cursor, int i, String str, OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(cursor, i, str, onClickListener);
        return this;
    }

    public Builder setSingleChoiceItems(ListAdapter listAdapter, int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(listAdapter, i, onClickListener);
        return this;
    }

    public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(charSequenceArr, i, onClickListener);
        return this;
    }

    public Builder setTitle(int i) {
        this.mMiuixBuilder.setTitle(i);
        return this;
    }

    public Builder setTitle(@Nullable CharSequence charSequence) {
        this.mMiuixBuilder.setTitle(charSequence);
        return this;
    }

    public Builder setView(int i) {
        this.mMiuixBuilder.setView(i);
        return this;
    }

    public Builder setView(View view) {
        this.mMiuixBuilder.setView(view);
        return this;
    }
}
