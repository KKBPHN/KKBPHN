package com.android.camera.features.gif;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.camera.ActivityBase;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;
import java.util.regex.Pattern;

public class GifEditLayout extends FrameLayout implements OnClickListener, OnTouchListener {
    public static final int LIMIT_BYTE = 16;
    public static final String TAG = "gif";
    private InputMethodManager imm;
    /* access modifiers changed from: private */
    public AdaptiveEditText mEtFakeGifWords;
    /* access modifiers changed from: private */
    public LimitInputEditText mEtGifWords;
    private GifTextWatcher mGifTextWatcher;
    /* access modifiers changed from: private */
    public boolean mIsAllowInput;
    private boolean mIsShowBorder;
    private FrameLayout mIvBorder;
    private ImageView mIvClear;
    /* access modifiers changed from: private */
    public String mLastString;
    private Rect mRect;
    /* access modifiers changed from: private */
    public StringBuilder stringBuilder;
    private Typeface tf;

    public class GifTextWatcher implements TextWatcher {
        public GifTextWatcher() {
        }

        public void afterTextChanged(Editable editable) {
            float f;
            TextPaint textPaint;
            String str = "";
            if (editable == null) {
                GifEditLayout.this.mEtFakeGifWords.setText(str);
            } else if (!GifEditLayout.this.mIsAllowInput) {
                if (!editable.toString().equals(GifEditLayout.this.mLastString)) {
                    GifEditLayout.this.mEtGifWords.setText(GifEditLayout.this.mLastString);
                }
            } else {
                if (editable.length() > 0) {
                    textPaint = GifEditLayout.this.mEtFakeGifWords.getPaint();
                    f = 12.0f;
                } else {
                    textPaint = GifEditLayout.this.mEtFakeGifWords.getPaint();
                    f = 3.0f;
                }
                textPaint.setStrokeWidth(f);
                textPaint.setStyle(Style.STROKE);
                try {
                    boolean z = false;
                    GifEditLayout.this.stringBuilder.delete(0, GifEditLayout.this.stringBuilder.length());
                    int i = 0;
                    int i2 = 0;
                    boolean z2 = false;
                    while (true) {
                        if (i >= editable.length()) {
                            break;
                        }
                        char charAt = editable.charAt(i);
                        if (charAt != 10) {
                            i2 += GifEditLayout.this.isDoubleByte(charAt) ? 2 : 1;
                            if (i2 > 32) {
                                i2 = 32;
                                break;
                            }
                            GifEditLayout.this.stringBuilder.append(charAt);
                            if (!z2 && i2 >= 16 && i != editable.length() - 1) {
                                GifEditLayout.this.stringBuilder.append(10);
                                z2 = true;
                            }
                        }
                        i++;
                    }
                    GifEditLayout gifEditLayout = GifEditLayout.this;
                    if (editable.length() == 0) {
                        z = true;
                    }
                    gifEditLayout.justShowBorder(z);
                    String sb = GifEditLayout.this.stringBuilder.toString();
                    if (i2 != 0) {
                        if (!sb.equals(GifEditLayout.this.mEtFakeGifWords.getText().toString())) {
                            GifEditLayout.this.mEtFakeGifWords.setText(sb);
                            GifEditLayout.this.mEtGifWords.setText(sb);
                            GifEditLayout.this.mEtGifWords.setSelection(sb.length());
                            return;
                        }
                    }
                    if (i2 == 32 && !sb.equals(GifEditLayout.this.mEtGifWords.getText().toString())) {
                        GifEditLayout.this.mEtGifWords.setText(sb);
                        GifEditLayout.this.mEtGifWords.setSelection(sb.length());
                    }
                    if (i2 == 0) {
                        GifEditLayout.this.mEtFakeGifWords.setText(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (GifEditLayout.this.mIsAllowInput) {
                GifEditLayout.this.mLastString = charSequence == null ? "" : charSequence.toString();
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append("");
            Log.e("gif", sb.toString());
        }
    }

    public GifEditLayout(Context context) {
        this(context, null);
        initVidew();
    }

    public GifEditLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        initVidew();
    }

    public GifEditLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRect = new Rect();
        this.mLastString = "";
        initVidew();
    }

    static /* synthetic */ boolean O000000o(TextView textView, int i, KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == 66 || keyEvent.getKeyCode() == 61;
    }

    private void initEdit() {
        this.mEtGifWords.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.mEtGifWords.setTypeface(this.tf);
        this.mEtGifWords.setVerticalScrollBarEnabled(false);
        this.mEtGifWords.setVerticalFadingEdgeEnabled(false);
        this.mEtGifWords.setHintTextColor(getResources().getColor(R.color.white_alpha_60));
        this.mEtGifWords.setTextSize(35.0f);
        if (this.mGifTextWatcher == null) {
            this.mGifTextWatcher = new GifTextWatcher();
        }
        this.mEtGifWords.addTextChangedListener(this.mGifTextWatcher);
        this.mEtGifWords.setOnTouchListener(this);
        this.mEtGifWords.setOnEditorActionListener(O00000o0.INSTANCE);
        AnonymousClass1 r0 = new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^` ，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．\n]");

            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                if (!this.pattern.matcher(charSequence).find()) {
                    return null;
                }
                Log.w("gif", "filter: Illegal input");
                return "";
            }
        };
        this.mEtGifWords.setFilters(new InputFilter[]{r0});
        this.mEtFakeGifWords.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.mEtFakeGifWords.setTypeface(this.tf);
        this.mEtFakeGifWords.setClickable(false);
        TextPaint paint = this.mEtFakeGifWords.getPaint();
        paint.setStrokeWidth(3.0f);
        paint.setStyle(Style.STROKE);
        this.mEtFakeGifWords.setTextColor(Color.parseColor("#1F1F1F"));
        this.mEtFakeGifWords.setTextSize(35.0f);
        this.mIsShowBorder = true;
        StringBuilder sb = this.stringBuilder;
        if (sb == null) {
            this.stringBuilder = new StringBuilder();
        } else {
            sb.delete(0, sb.length());
        }
        String str = "";
        this.mEtGifWords.setText(str);
        this.mEtFakeGifWords.setText(str);
    }

    private void initVidew() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gif_edit, null, false);
        this.mIvClear = (ImageView) inflate.findViewById(R.id.image_delete_gif_txt);
        this.mIvClear.setOnClickListener(this);
        this.mIvBorder = (FrameLayout) inflate.findViewById(R.id.fl_border);
        this.mEtGifWords = (LimitInputEditText) inflate.findViewById(R.id.edit_gif_words);
        this.mEtFakeGifWords = (AdaptiveEditText) inflate.findViewById(R.id.edit_gif_fake_words);
        addView(inflate);
        this.imm = (InputMethodManager) getContext().getSystemService("input_method");
        this.tf = Util.getFZMiaoWuJWTypeface(getContext());
        setIsAllowInput(true);
        initEdit();
    }

    /* access modifiers changed from: private */
    public boolean isDoubleByte(char c) {
        if (c >= 'a' && c <= 'z') {
            return false;
        }
        if (c < 'A' || c > 'Z') {
            return c < '0' || c > '9';
        }
        return false;
    }

    private boolean isShowSoftButtonsBarHeight() {
        int height = ((ActivityBase) getContext()).getWindow().getDecorView().getHeight();
        ((ActivityBase) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(this.mRect);
        return (height * 2) / 3 > this.mRect.bottom;
    }

    /* access modifiers changed from: private */
    public void justShowBorder(boolean z) {
        if (z) {
            this.mIsShowBorder = true;
            this.mIvBorder.setBackgroundResource(R.drawable.shape_gif_edit_words);
        }
        ImageView imageView = this.mIvClear;
        int i = (z || !this.mIsShowBorder) ? 8 : 0;
        imageView.setVisibility(i);
    }

    public String getResult() {
        return this.mEtFakeGifWords.getText().toString();
    }

    public void hideBorder(boolean z) {
        if (isShowSoftButtonsBarHeight()) {
            this.imm.hideSoftInputFromWindow(this.mEtGifWords.getWindowToken(), 0);
        } else if (!z) {
            if (this.mEtGifWords.getText() == null || this.mEtGifWords.getText().length() == 0) {
                this.mEtGifWords.clearFocus();
                return;
            }
            this.mIsShowBorder = false;
            this.mIvClear.setVisibility(8);
            this.mIvBorder.setBackgroundResource(R.color.transparent);
            this.mEtGifWords.clearFocus();
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.image_delete_gif_txt) {
            String str = "";
            this.mEtGifWords.setText(str);
            this.mEtFakeGifWords.setText(str);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.mIsAllowInput) {
            return true;
        }
        if (motionEvent.getAction() == 0 && view.getId() == R.id.edit_gif_words) {
            return showBorder();
        }
        return false;
    }

    public void setIsAllowInput(boolean z) {
        this.mIsAllowInput = z;
        if (!z) {
            this.mEtGifWords.clearFocus();
        }
    }

    public boolean showBorder() {
        int i = 0;
        if (!this.mIsShowBorder) {
            this.mIsShowBorder = true;
            this.mIvBorder.setBackgroundResource(R.drawable.shape_gif_edit_words);
            ImageView imageView = this.mIvClear;
            if (this.mEtGifWords.length() <= 0) {
                i = 8;
            }
            imageView.setVisibility(i);
            return true;
        }
        if (!isShowSoftButtonsBarHeight()) {
            this.imm.showSoftInput(this.mEtGifWords, 2);
            this.imm.toggleSoftInput(2, 1);
            this.mEtGifWords.requestFocus();
            LimitInputEditText limitInputEditText = this.mEtGifWords;
            limitInputEditText.setSelection(limitInputEditText.length());
        }
        return false;
    }
}
