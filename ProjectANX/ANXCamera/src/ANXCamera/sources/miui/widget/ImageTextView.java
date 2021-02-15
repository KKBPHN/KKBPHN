package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.HashMap;

public class ImageTextView extends LinearLayout {
    private static final int ENTRIES_INDEX = 0;
    private static final int ENTRYVALUES_INDEX = 1;
    private static final int[] ImageTextView_Styleable = {16842930, 16843256};
    private final LayoutParams VIEW_PARAMS;
    private HashMap mCharMap;
    private CharSequence mText;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImageTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.VIEW_PARAMS = new LayoutParams(-2, -2);
        setOrientation(0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ImageTextView_Styleable, i, 0);
        CharSequence[] textArray = obtainStyledAttributes.getTextArray(0);
        Integer[] drawableIds = getDrawableIds(obtainStyledAttributes);
        if (!(textArray == null || drawableIds == null || textArray.length != drawableIds.length)) {
            this.mCharMap = new HashMap();
            for (int i2 = 0; i2 < textArray.length; i2++) {
                this.mCharMap.put(Character.valueOf(textArray[i2].charAt(0)), drawableIds[i2]);
            }
        }
        obtainStyledAttributes.recycle();
    }

    private Integer[] getDrawableIds(TypedArray typedArray) {
        TypedValue typedValue = new TypedValue();
        if (!typedArray.getValue(1, typedValue)) {
            return null;
        }
        TypedArray obtainTypedArray = getContext().getResources().obtainTypedArray(typedValue.resourceId);
        Integer[] numArr = new Integer[obtainTypedArray.length()];
        for (int i = 0; i < obtainTypedArray.length(); i++) {
            numArr[i] = Integer.valueOf(obtainTypedArray.peekValue(i).resourceId);
        }
        obtainTypedArray.recycle();
        return numArr;
    }

    public CharSequence getText() {
        CharSequence charSequence = this.mText;
        return charSequence == null ? "" : charSequence;
    }

    public void setCharMap(HashMap hashMap) {
        this.mCharMap = hashMap;
    }

    public void setText(CharSequence charSequence) {
        View view;
        if (this.mCharMap != null && charSequence != null && !charSequence.equals(this.mText)) {
            this.mText = charSequence;
            for (int i = 0; i < charSequence.length(); i++) {
                if (i < getChildCount()) {
                    view = getChildAt(i);
                } else {
                    view = new ImageView(getContext());
                    view.setLayoutParams(this.VIEW_PARAMS);
                    addView(view);
                }
                Integer num = (Integer) this.mCharMap.get(Character.valueOf(charSequence.charAt(i)));
                if (num != null) {
                    view.setBackground(getResources().getDrawable(num.intValue()));
                }
            }
            for (int childCount = getChildCount(); childCount > charSequence.length(); childCount--) {
                removeViewAt(childCount - 1);
            }
        }
    }
}
