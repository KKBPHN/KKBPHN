package com.android.camera.fragment.bottom;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.fragment.beauty.MenuItem;
import com.android.camera.ui.ColorActivateTextView;

public class PortraitLightingMenu extends AbBottomMenu implements OnClickListener {
    private static final int PORTRAIT_LIGHTING_TYPE = 0;
    private SparseArray mLiveSpeedMenuTabList;
    private SparseArray mMenuTextViewList;
    private int mPortraitLightingVersion;

    public PortraitLightingMenu(Context context, LinearLayout linearLayout, BeautyMenuAnimator beautyMenuAnimator, int i) {
        super(context, linearLayout, beautyMenuAnimator);
        this.mPortraitLightingVersion = i;
    }

    /* access modifiers changed from: 0000 */
    public void addAllView() {
        this.mMenuTextViewList = new SparseArray();
        SparseArray menuData = getMenuData();
        for (int i = 0; i < menuData.size(); i++) {
            MenuItem menuItem = (MenuItem) menuData.valueAt(i);
            ColorActivateTextView colorActivateTextView = (ColorActivateTextView) LayoutInflater.from(this.mContext).inflate(R.layout.beauty_menu_select_item, this.mContainerView, false);
            colorActivateTextView.setNormalCor(-1);
            colorActivateTextView.setText(menuItem.text);
            colorActivateTextView.setTag(Integer.valueOf(menuItem.type));
            colorActivateTextView.setOnClickListener(this);
            if (menuItem.type == 0) {
                this.mCurrentBeautyTextView = colorActivateTextView;
            }
            colorActivateTextView.setActivated(false);
            this.mMenuTextViewList.put(menuItem.type, colorActivateTextView);
            this.mContainerView.addView(colorActivateTextView);
        }
    }

    /* access modifiers changed from: 0000 */
    public SparseArray getChildMenuViewList() {
        return this.mMenuTextViewList;
    }

    /* access modifiers changed from: 0000 */
    public int getDefaultType() {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public SparseArray getMenuData() {
        int i;
        Context context;
        SparseArray sparseArray = this.mLiveSpeedMenuTabList;
        if (sparseArray != null) {
            return sparseArray;
        }
        this.mLiveSpeedMenuTabList = new SparseArray();
        MenuItem menuItem = new MenuItem();
        menuItem.type = 0;
        if (this.mPortraitLightingVersion >= 2) {
            context = CameraAppImpl.getAndroidContext();
            i = R.string.lighting_movie_light_effect_title;
        } else {
            context = CameraAppImpl.getAndroidContext();
            i = R.string.lighting_hint_title;
        }
        menuItem.text = context.getString(i);
        menuItem.number = 0;
        this.mLiveSpeedMenuTabList.put(0, menuItem);
        return this.mLiveSpeedMenuTabList;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRefreshUI() {
        return true;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: 0000 */
    public void switchMenu() {
        this.mContainerView.removeAllViews();
        addAllView();
    }
}
