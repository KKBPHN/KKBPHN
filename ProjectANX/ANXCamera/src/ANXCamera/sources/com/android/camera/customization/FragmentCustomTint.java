package com.android.camera.customization;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.ThermalHelper;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.ColorImageView;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;

public class FragmentCustomTint extends BaseFragment implements OnColorChangeListener, OnClickListener, OnPageChangeListener {
    private static final int IMAGE_HEIGHT = 2340;
    private static final float IMAGE_RATIO = 0.46153846f;
    private static final int IMAGE_WIDTH = 1080;
    private static String LOG_TAG = "FragCustomTint";
    private static int MODE_PREVIEW = 2;
    private static int MODE_SELECT = 1;
    private static final int PADDING_BOTTOM_MAX = 160;
    private static final int PADDING_TOP_MAX = 110;
    private int PREVIEW_MODE_HEIGHT = (Display.getWindowHeight() - Display.getNavigationBarHeight());
    /* access modifiers changed from: private */
    public View actionBar;
    /* access modifiers changed from: private */
    public View colorSelectContainer;
    private TintColorTableView colorView;
    private TintPreviewAdapter mAdapter;
    private int mCurrentIndex = 0;
    private View mCurrentItemView;
    private int mCurrentMode = MODE_SELECT;
    /* access modifiers changed from: private */
    public ViewPager previewList;

    class TintPreviewAdapter extends PagerAdapter {
        private final Context mContext;
        private final List mData;
        private OnClickListener mItemClickListener;

        public TintPreviewAdapter(Context context, List list, OnClickListener onClickListener) {
            this.mContext = context;
            this.mData = list;
            this.mItemClickListener = onClickListener;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            viewGroup.removeView((View) obj);
        }

        public int getCount() {
            return this.mData.size();
        }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_tint_preview_item, viewGroup, false);
            inflate.setTag(Integer.valueOf(i));
            ((ImageView) inflate.findViewById(R.id.custom_tint_item_image)).setImageResource(((TintColor) this.mData.get(i)).previewResource());
            viewGroup.addView(inflate);
            View findViewById = inflate.findViewById(R.id.item_container);
            findViewById.setTag(Integer.valueOf(i));
            FragmentCustomTint.this.refreshPreviewItem(inflate);
            FragmentCustomTint.this.refreshPreviewImage(inflate);
            FragmentCustomTint fragmentCustomTint = FragmentCustomTint.this;
            fragmentCustomTint.refreshColorTable(fragmentCustomTint.getView());
            findViewById.setOnClickListener(this.mItemClickListener);
            return inflate;
        }

        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }
    }

    private void enterPreviewMode(View view) {
        if (view instanceof FrameLayout) {
            this.mCurrentItemView = view;
            toggleMode(view);
        }
    }

    private int imageHeight() {
        return isSelectMode() ? selectModeHeight() : previewModeHeight();
    }

    private int imageWidth() {
        return isSelectMode() ? selectModeWidth() : Display.getWindowWidth();
    }

    /* access modifiers changed from: private */
    public boolean isSelectMode() {
        return this.mCurrentMode == MODE_SELECT;
    }

    private int previewModeHeight() {
        int windowHeight = Display.getWindowHeight();
        if (Display.checkDeviceHasNavigationBar(getActivity())) {
            windowHeight -= Display.getNavigationBarHeight();
        }
        return windowHeight - (Display.isNotchDevice() ? Display.getStatusBarHeight(getActivity()) : 0);
    }

    /* access modifiers changed from: private */
    public void refreshColorTable(View view) {
        ((LayoutParams) view.findViewById(R.id.color_select_view).getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(Display.checkDeviceHasNavigationBar(getActivity()) ? R.dimen.custom_tint_color_table_bottom_margin : R.dimen.custom_tint_color_table_bottom_margin_full);
    }

    /* access modifiers changed from: private */
    public void refreshPreviewImage(View view) {
        int i;
        int i2;
        ImageView imageView = (ImageView) view.findViewById(R.id.custom_tint_item_image);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.custom_tint_preview_image_margin);
        layoutParams.bottomMargin = isSelectMode() ? dimensionPixelSize : 0;
        layoutParams.topMargin = isSelectMode() ? dimensionPixelSize : 0;
        layoutParams.leftMargin = isSelectMode() ? dimensionPixelSize : 0;
        if (!isSelectMode()) {
            dimensionPixelSize = 0;
        }
        layoutParams.rightMargin = dimensionPixelSize;
        if (!isSelectMode()) {
            int imageHeight = imageHeight();
            int round = Math.round((((float) imageWidth()) * 2340.0f) / 1080.0f);
            if (round > imageHeight) {
                int i3 = round - imageHeight;
                int i4 = i3 / 2;
                int min = Math.min(i4, 110) * -1;
                i2 = Math.min(i3 - i4, 160) * -1;
                i = min;
                imageView.setPadding(0, i, 0, i2);
                imageView.setScaleType(ScaleType.FIT_XY);
            } else if (round < imageHeight) {
                i = (imageHeight - round) / 2;
                i2 = i;
                imageView.setPadding(0, i, 0, i2);
                imageView.setScaleType(ScaleType.FIT_XY);
            }
        }
        i = 0;
        i2 = i;
        imageView.setPadding(0, i, 0, i2);
        imageView.setScaleType(ScaleType.FIT_XY);
    }

    /* access modifiers changed from: private */
    public void refreshPreviewItem(View view) {
        int i = 0;
        view.findViewById(R.id.background).setVisibility(isSelectMode() ? 0 : 8);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.findViewById(R.id.item_container).getLayoutParams();
        layoutParams.width = imageWidth();
        layoutParams.height = imageHeight();
        if (isSelectMode()) {
            i = getResources().getDimensionPixelSize(R.dimen.custom_tint_preview_bottom_margin);
        }
        layoutParams.bottomMargin = i;
    }

    private int selectModeHeight() {
        int windowHeight = ((Display.getWindowHeight() - Display.getStatusBarHeight(getActivity())) - getResources().getDimensionPixelSize(R.dimen.custom_tint_preview_bottom_height_full)) - getResources().getDimensionPixelSize(R.dimen.custom_tint_actionbar_height);
        if (Display.checkDeviceHasNavigationBar(getActivity())) {
            windowHeight -= Display.getNavigationBarHeight();
        }
        return (windowHeight - getResources().getDimensionPixelSize(R.dimen.custom_tint_preview_top_margin)) - getResources().getDimensionPixelSize(R.dimen.custom_tint_preview_bottom_margin);
    }

    private int selectModeWidth() {
        return Math.round(((float) selectModeHeight()) * IMAGE_RATIO);
    }

    private void toggleMode(View view) {
        int i;
        int intValue = ((Integer) view.getTag()).intValue();
        if (this.mCurrentMode == MODE_SELECT) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Enter preview mode for ");
            sb.append(intValue);
            Log.u(str, sb.toString());
            i = MODE_PREVIEW;
        } else {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Enter select mode for ");
            sb2.append(intValue);
            Log.u(str2, sb2.toString());
            i = MODE_SELECT;
        }
        this.mCurrentMode = i;
        AnonymousClass1 r0 = new TransitionListener() {
            public void onBegin(Object obj) {
                super.onBegin(obj);
                WindowManager.LayoutParams attributes = FragmentCustomTint.this.getActivity().getWindow().getAttributes();
                attributes.flags = FragmentCustomTint.this.isSelectMode() ? attributes.flags & -1025 : attributes.flags | 1024;
                FragmentCustomTint.this.getActivity().getWindow().setAttributes(attributes);
                int i = 8;
                FragmentCustomTint.this.actionBar.setVisibility(FragmentCustomTint.this.isSelectMode() ? 0 : 8);
                View access$200 = FragmentCustomTint.this.colorSelectContainer;
                if (FragmentCustomTint.this.isSelectMode()) {
                    i = 0;
                }
                access$200.setVisibility(i);
                for (int i2 = 0; i2 < FragmentCustomTint.this.previewList.getChildCount(); i2++) {
                    View childAt = FragmentCustomTint.this.previewList.getChildAt(i2);
                    FragmentCustomTint.this.refreshPreviewItem(childAt);
                    FragmentCustomTint.this.refreshPreviewImage(childAt);
                }
            }

            public void onComplete(Object obj) {
                super.onComplete(obj);
            }
        };
        AnimState add = new AnimState(BaseEvent.PREVIEW).add(ViewProperty.HEIGHT, previewModeHeight(), new long[0]).add(ViewProperty.WIDTH, Display.getWindowWidth(), new long[0]);
        AnimState add2 = new AnimState("select").add(ViewProperty.HEIGHT, selectModeHeight(), new long[0]).add(ViewProperty.WIDTH, selectModeWidth(), new long[0]);
        if (isSelectMode()) {
            AnimConfig addListeners = new AnimConfig(ViewProperty.WIDTH, ViewProperty.HEIGHT).setEase(6, 300.0f).addListeners(r0);
            Folme.useAt(view).state().fromTo(add, add2, addListeners);
            return;
        }
        AnimConfig addListeners2 = new AnimConfig(ViewProperty.WIDTH, ViewProperty.HEIGHT).addListeners(r0);
        Folme.useAt(view).state().fromTo(add2, add, addListeners2);
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_CUSTOM_TINT;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.activity_tint;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mCurrentIndex = TintColor.readColorId();
        this.colorSelectContainer = view.findViewById(R.id.color_select_container);
        this.actionBar = view.findViewById(R.id.actionbar);
        ColorImageView colorImageView = (ColorImageView) view.findViewById(R.id.back);
        colorImageView.setColor(getResources().getColor(R.color.white_80));
        colorImageView.setOnClickListener(this);
        this.colorView = (TintColorTableView) view.findViewById(R.id.color_select_view);
        this.colorView.initialize(TintColor.getAvailableColors(), this.mCurrentIndex);
        this.colorView.setOnColorChangeListener(this);
        View findViewById = view.findViewById(R.id.apply);
        FolmeUtils.handleListItemTouch(findViewById);
        findViewById.setOnClickListener(this);
        this.mAdapter = new TintPreviewAdapter(getContext(), TintColor.getAvailableColors(), this);
        this.previewList = (ViewPager) view.findViewById(R.id.preview_list);
        this.previewList.setAdapter(this.mAdapter);
        this.previewList.setCurrentItem(this.mCurrentIndex);
        this.previewList.addOnPageChangeListener(this);
    }

    public boolean onBackPressed() {
        if (this.mCurrentMode != MODE_PREVIEW) {
            return false;
        }
        toggleMode(this.mCurrentItemView);
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.apply) {
            Log.u(LOG_TAG, "onClick apply");
            TintColor.persistColorId(this.mCurrentIndex);
            MistatsWrapper.settingClickEvent("attr_edit_tint", Integer.valueOf(this.mCurrentIndex));
        } else if (id != R.id.back) {
            enterPreviewMode(view);
            return;
        } else {
            Log.u(LOG_TAG, "onClick back");
        }
        getActivity().finish();
    }

    public void onColorChange(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onColorChange ");
        sb.append(i);
        Log.u(str, sb.toString());
        this.mCurrentIndex = i;
        this.previewList.setCurrentItem(i, true);
    }

    public void onDestroy() {
        super.onDestroy();
        ViewPager viewPager = this.previewList;
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPageSelected ");
        sb.append(i);
        Log.u(str, sb.toString());
        this.mCurrentIndex = i;
        this.colorView.setCurrent(this.mCurrentIndex);
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getActivity().getApplication());
        }
    }

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getActivity().getApplication());
        }
    }
}
