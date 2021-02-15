package com.android.camera.fragment.bottom;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiBottomList;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.beauty.MenuItem;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.ui.ColorActivateTextView;

public class MimojiMenu extends AbBottomMenu implements OnClickListener {
    public static final int MIMOJI_CARTOON = 1;
    public static final int MIMOJI_HUMEN = 0;
    public static final int MIMOJI_NULL = -1;
    private static final String TAG = "MimojiMenu";
    private int currentModule;
    private boolean mIsMimoji = C0122O00000o.instance().OOO0Oo0();
    private boolean mIsMimoji2 = C0122O00000o.instance().OOO0ooo();
    private SparseArray mMenuTextViewList;
    private SparseArray mMimojiMenuTabList;
    private MimojiStatusManager2 mMimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();

    public MimojiMenu(Context context, LinearLayout linearLayout, BeautyMenuAnimator beautyMenuAnimator) {
        super(context, linearLayout, beautyMenuAnimator);
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
            if ((this.mMimojiStatusManager2.getAvatarPanelState() == 101 && menuItem.type == 1) || (this.mMimojiStatusManager2.getAvatarPanelState() == 100 && menuItem.type == 0)) {
                this.mCurrentBeautyTextView = colorActivateTextView;
                colorActivateTextView.setActivated(true);
            } else {
                colorActivateTextView.setActivated(false);
            }
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
        return 8;
    }

    /* access modifiers changed from: 0000 */
    public SparseArray getMenuData() {
        int i;
        Context context;
        MenuItem menuItem;
        SparseArray sparseArray = this.mMimojiMenuTabList;
        if (sparseArray != null) {
            sparseArray.clear();
        } else {
            this.mMimojiMenuTabList = new SparseArray();
        }
        this.currentModule = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        if (this.mIsMimoji && this.currentModule == 177) {
            MenuItem menuItem2 = new MenuItem();
            menuItem2.type = -1;
            menuItem2.text = CameraAppImpl.getAndroidContext().getString(R.string.mimoji_fragment_tab_name);
            menuItem2.number = 0;
            this.mMimojiMenuTabList.put(-1, menuItem2);
        }
        if (this.mIsMimoji2 && this.currentModule == 184) {
            int mimojiPanelState = this.mMimojiStatusManager2.getMimojiPanelState();
            if (mimojiPanelState == 2) {
                menuItem = new MenuItem();
                menuItem.type = -1;
                context = this.mContext;
                i = R.string.background;
                menuItem.text = context.getString(i);
                menuItem.number = 0;
            } else if (mimojiPanelState != 3) {
                MenuItem menuItem3 = new MenuItem();
                menuItem3.type = 0;
                menuItem3.text = this.mContext.getString(R.string.mimoji_tab_human);
                menuItem3.number = 0;
                this.mMimojiMenuTabList.put(menuItem3.type, menuItem3);
                menuItem = new MenuItem();
                menuItem.type = 1;
                menuItem.text = this.mContext.getString(R.string.mimoji_tab_cartoon);
                menuItem.number = 1;
            } else {
                menuItem = new MenuItem();
                menuItem.type = -1;
                context = this.mContext;
                i = R.string.timbre;
                menuItem.text = context.getString(i);
                menuItem.number = 0;
            }
            this.mMimojiMenuTabList.put(menuItem.type, menuItem);
        }
        return this.mMimojiMenuTabList;
    }

    /* access modifiers changed from: protected */
    public boolean isClickEnable() {
        return super.isClickEnable() && this.mIsMimoji2 && this.mMimojiStatusManager2.getMimojiPanelState() == 1;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRefreshUI() {
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0052, code lost:
        if (r0 != null) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006f, code lost:
        if (r0 != null) goto L_0x0071;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        if (isClickEnable()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                MimojiBottomList mimojiBottomList = (MimojiBottomList) ModeCoordinatorImpl.getInstance().getAttachProtocol(248);
                int intValue = ((Integer) view.getTag()).intValue();
                if (intValue != 0) {
                    if (intValue == 1) {
                        Log.u(TAG, "onClick MIMOJI_CARTOON");
                        if (this.mMimojiStatusManager2.getAvatarPanelState() != 101) {
                            this.mMimojiStatusManager2.setAvatarPanelState(101);
                            switchMenu();
                        } else {
                            return;
                        }
                    }
                }
                Log.u(TAG, "onClick MIMOJI_HUMEN");
                if (this.mMimojiStatusManager2.getAvatarPanelState() != 100) {
                    this.mMimojiStatusManager2.setAvatarPanelState(100);
                    switchMenu();
                } else {
                    return;
                }
                mimojiBottomList.switchMimojiList();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void switchMenu() {
        this.mContainerView.removeAllViews();
        addAllView();
    }
}
