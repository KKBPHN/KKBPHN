package miui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.util.FolmeAnimHelper;

public class MessageView extends LinearLayout {
    private boolean mClosable;
    private Drawable mCloseBackground;
    private int mCloseIndex;
    private TextView mTextView;

    public MessageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MessageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet, i);
    }

    private void addCloseIcon() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.message_view_text_margin_right);
        imageView.setId(R.id.close);
        imageView.setBackground(this.mCloseBackground);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MessageView.this.setVisibility(8);
            }
        });
        addView(imageView, layoutParams);
    }

    private void initView(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MessageView, i, 0);
        String string = obtainStyledAttributes.getString(R.styleable.MessageView_android_text);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.MessageView_android_textColor);
        this.mCloseBackground = obtainStyledAttributes.getDrawable(R.styleable.MessageView_closeBackground);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.MessageView_closable, true);
        obtainStyledAttributes.recycle();
        this.mTextView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.weight = 1.0f;
        this.mTextView.setId(16908308);
        this.mTextView.setPaddingRelative(10, 0, 0, 0);
        this.mTextView.setText(string);
        this.mTextView.setTextColor(colorStateList);
        this.mTextView.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.message_view_text_size));
        addView(this.mTextView, layoutParams);
        setClosable(z);
        setGravity(16);
        FolmeAnimHelper.addPressAnim(this);
    }

    public void setClosable(boolean z) {
        View findViewById = findViewById(R.id.close);
        if (z) {
            if (findViewById == null) {
                addCloseIcon();
            }
        } else if (findViewById != null) {
            removeView(findViewById);
        }
    }

    public void setMessage(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }
}
