package com.android.camera.features.mimoji2.widget.autoselectview;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class AutoSelectViewHolder extends ViewHolder {
    private TextView textView;

    public AutoSelectViewHolder(@NonNull View view) {
        super(view);
        this.textView = (TextView) view.findViewById(R.id.tv_type);
    }

    public /* synthetic */ void O00O0OoO() {
        this.itemView.sendAccessibilityEvent(128);
    }

    public void setData(SelectItemBean selectItemBean, final int i) {
        this.textView.setTextColor(this.itemView.getContext().getColor(selectItemBean.getAlpha() == 1 ? R.color.mimoji_edit_type_text_selected : R.color.mimoji_edit_type_text_normal));
        this.textView.setText(selectItemBean.getText());
        if (selectItemBean.getAlpha() == 1) {
            View view = this.itemView;
            StringBuilder sb = new StringBuilder();
            sb.append(selectItemBean.getText());
            sb.append(", ");
            sb.append(this.itemView.getContext().getString(R.string.accessibility_selected));
            view.setContentDescription(sb.toString());
            if (Util.isAccessible()) {
                this.itemView.postDelayed(new O000000o(this), 100);
            }
        } else {
            this.itemView.setContentDescription(selectItemBean.getText());
        }
        this.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    ((AutoSelectHorizontalView) AutoSelectViewHolder.this.itemView.getParent()).moveToPosition(i);
                } catch (ClassCastException unused) {
                    Log.e(AnonymousClass1.class.getSimpleName(), "recyclerview 类型不正确");
                }
            }
        });
    }
}
