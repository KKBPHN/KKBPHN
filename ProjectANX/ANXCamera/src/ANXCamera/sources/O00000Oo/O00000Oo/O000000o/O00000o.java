package O00000Oo.O00000Oo.O000000o;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.Image;
import android.os.SystemProperties;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.google.android.libraries.lens.lenslite.LensliteApi;
import com.google.android.libraries.lens.lenslite.LensliteUiContainer;
import com.google.android.libraries.lens.lenslite.LensliteUiController;
import com.google.android.libraries.lens.lenslite.LensliteUiController.FocusType;
import com.google.android.libraries.lens.lenslite.api.LinkImage;
import com.xiaomi.stat.d;

public class O00000o {
    private static final String TAG = "LensAgent";
    private volatile boolean O0Oo00;
    private LensliteApi O0Oo000;
    private volatile boolean mInitialized;

    private O00000o() {
    }

    private void O000000o(Context context, ViewGroup viewGroup) {
        LensliteUiController uiController = this.O0Oo000.getUiController();
        Resources resources = context.getResources();
        Rect displayRect = Util.getDisplayRect(0);
        uiController.setChipLocation(new PointF(0.0f, (((float) displayRect.bottom) - resources.getDimension(R.dimen.chips_margin_preview_rect_bottom)) / Display.getPixelDensity()));
        uiController.setChipDrawable(R.drawable.chips_bg);
        uiController.setOobeLocation(1, ((float) Display.getNavigationBarHeight()) / Display.getPixelDensity());
        TextView textView = (TextView) viewGroup.findViewById(R.id.smarts_chip_text);
        if (textView != null) {
            LayoutParams layoutParams = new LayoutParams(textView.getLayoutParams());
            layoutParams.gravity = 17;
            layoutParams.width = -2;
            layoutParams.height = -2;
            layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.chips_text_margin_bottom));
            textView.setLayoutParams(layoutParams);
            textView.setGravity(17);
            textView.setTextColor(-1);
            textView.setTextSize(0, resources.getDimension(R.dimen.chips_text_size));
        }
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.smarts_chip_close_button);
        String str = ", marginRight = ";
        String str2 = TAG;
        if (imageView != null) {
            imageView.setImageResource(R.drawable.chips_close);
            imageView.setImageTintList(ColorStateList.valueOf(-1));
            int dimensionPixelOffset = resources.getDimensionPixelOffset(R.dimen.chips_close_padding);
            imageView.setPadding(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            LayoutParams layoutParams2 = new LayoutParams(imageView.getLayoutParams());
            layoutParams2.gravity = 16;
            layoutParams2.width = -2;
            layoutParams2.height = -2;
            int i = SystemProperties.getInt("cancel_margin_left", resources.getDimensionPixelOffset(R.dimen.chips_close_margin_left));
            int i2 = SystemProperties.getInt("cancel_margin_right", resources.getDimensionPixelOffset(R.dimen.chips_close_margin_right));
            StringBuilder sb = new StringBuilder();
            sb.append("applyCustomStyle: cancel button marginLeft = ");
            sb.append(i);
            sb.append(str);
            sb.append(i2);
            Log.d(str2, sb.toString());
            layoutParams2.setMargins(i, 0, i2, 0);
            imageView.setLayoutParams(layoutParams2);
        }
        LinearLayout linearLayout = (LinearLayout) viewGroup.findViewById(R.id.chip_view);
        if (linearLayout != null) {
            linearLayout.setPadding(0, 0, 0, 0);
        }
        FrameLayout frameLayout = (FrameLayout) viewGroup.findViewById(R.id.chip_animation_container);
        if (frameLayout != null) {
            LayoutParams layoutParams3 = new LayoutParams(frameLayout.getLayoutParams());
            layoutParams3.gravity = 16;
            int i3 = SystemProperties.getInt("icon_margin_left", resources.getDimensionPixelOffset(R.dimen.chips_icon_margin_left));
            int i4 = SystemProperties.getInt("icon_margin_right", resources.getDimensionPixelOffset(R.dimen.chips_icon_margin_right));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("applyCustomStyle: icon marginLeft = ");
            sb2.append(i3);
            sb2.append(str);
            sb2.append(i4);
            Log.d(str2, sb2.toString());
            layoutParams3.setMargins(i3, 0, i4, 0);
            frameLayout.setLayoutParams(layoutParams3);
        }
        uiController.setIconForResultType(0, context.getDrawable(R.drawable.chips_location));
        uiController.setIconForResultType(1, context.getDrawable(R.drawable.chips_mail));
        uiController.setIconForResultType(2, context.getDrawable(R.drawable.chips_browser));
        uiController.setIconForResultType(3, context.getDrawable(R.drawable.chips_phone));
        uiController.setIconForResultType(4, context.getDrawable(R.drawable.chips_contact));
        uiController.setIconForResultType(5, context.getDrawable(R.drawable.chips_qrcode));
        uiController.setIconForResultType(6, context.getDrawable(R.drawable.chips_qrcode));
        uiController.setIconForResultType(7, context.getDrawable(R.drawable.chips_qrcode));
        uiController.setIconForResultType(8, context.getDrawable(R.drawable.chips_qrcode));
        uiController.setIconForResultType(12, context.getDrawable(R.drawable.chips_qrcode));
        uiController.setIconForResultType(13, context.getDrawable(R.drawable.chips_mail));
        uiController.setIconForResultType(14, context.getDrawable(R.drawable.chips_calendar));
        uiController.setIconForResultType(18, context.getDrawable(R.drawable.chips_no_result));
    }

    static /* synthetic */ void O0000oo(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("onOobeStatusUpdated: ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        String str = CameraSettings.KEY_GOOGLE_LENS_OOBE;
        if (dataItemGlobal.getBoolean(str, false)) {
            CameraStatUtils.trackGoogleLensOobeContinue(i == 3);
            DataRepository.dataItemGlobal().editor().putBoolean(str, false).apply();
            if (i != 3) {
                DataRepository.dataItemGlobal().editor().remove(CameraSettings.KEY_LONG_PRESS_VIEWFINDER).apply();
            }
        }
    }

    public static boolean O0000oo0(int i) {
        if (!(i == -1 || i == 19 || i == 25)) {
            if (i == 31) {
                return C0122O00000o.instance().OOo0Oo0();
            }
            if (!(i == 34 || i == 37)) {
                return false;
            }
        }
        return true;
    }

    public static O00000o getInstance() {
        return O00000o0.O0OOooo;
    }

    public void O000000o(@FocusType int i, float f, float f2) {
        if (this.mInitialized) {
            StringBuilder sb = new StringBuilder();
            sb.append("onFocusChange: type = ");
            sb.append(i);
            sb.append(", ");
            sb.append(f);
            sb.append("x");
            sb.append(f2);
            String sb2 = sb.toString();
            String str = TAG;
            Log.d(str, sb2);
            try {
                this.O0Oo000.getUiController().onFocusChange(i, new PointF(f, f2));
            } catch (Exception e) {
                Log.e(str, "onFocusChange: ", (Throwable) e);
            }
        }
    }

    public void O000000o(Activity activity, View view, ViewGroup viewGroup) {
        this.O0Oo000 = LensliteApi.create(activity.getApplicationContext(), 3);
        long currentTimeMillis = System.currentTimeMillis();
        this.O0Oo000.onStart(new LensliteUiContainer(view, viewGroup), activity, O000000o.INSTANCE);
        StringBuilder sb = new StringBuilder();
        sb.append("LensliteApi init cost ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(d.H);
        Log.d(TAG, sb.toString());
        O000000o(activity.getApplicationContext(), viewGroup);
        this.mInitialized = true;
    }

    public void O000000o(Image image, int i) {
        if (this.mInitialized) {
            if (this.O0Oo00) {
                this.O0Oo000.onNewImage(LinkImage.create(image, i));
            } else {
                Log.w(TAG, "onNewImage: lens api not resume...");
            }
        }
    }

    public void onPause() {
        if (this.mInitialized) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.O0Oo00) {
                this.O0Oo00 = false;
                this.O0Oo000.onPause();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("LensliteApi onPause cost ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append(d.H);
            Log.d(TAG, sb.toString());
        }
    }

    public void onResume() {
        if (this.mInitialized) {
            long currentTimeMillis = System.currentTimeMillis();
            if (!this.O0Oo00) {
                this.O0Oo000.onResume();
                this.O0Oo00 = true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("LensliteApi onResume cost ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append(d.H);
            Log.d(TAG, sb.toString());
        }
    }

    public void release() {
        if (this.mInitialized) {
            long currentTimeMillis = System.currentTimeMillis();
            this.O0Oo000.onStop();
            StringBuilder sb = new StringBuilder();
            sb.append("LensliteApi release cost ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append(d.H);
            Log.d(TAG, sb.toString());
            this.mInitialized = false;
        }
    }

    public void showOrHideChip(boolean z) {
        if (this.mInitialized) {
            this.O0Oo000.getUiController().setLensSuggestionsEnabled(z);
        }
    }
}
