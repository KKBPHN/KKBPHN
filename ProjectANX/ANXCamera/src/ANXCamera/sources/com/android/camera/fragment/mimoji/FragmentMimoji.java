package com.android.camera.fragment.mimoji;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.widget.baseview.BaseLinearLayoutManager;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.EffectItemAdapter.EffectItemPadding;
import com.android.camera.fragment.live.FragmentLiveBase;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiAlert;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.MimojiEditor;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentMimoji extends FragmentLiveBase implements MimojiAlert, OnClickListener {
    public static final String ADD_STATE = "add_state";
    public static final String CLOSE_STATE = "close_state";
    private static final int FRAGMENT_INFO = 65520;
    /* access modifiers changed from: private */
    public static final String TAG = "FragmentMimoji";
    /* access modifiers changed from: private */
    public BubbleEditMimojiPresenter bubbleEditMimojiPresenter;
    private Context mContext;
    /* access modifiers changed from: private */
    public AlertDialog mDeleteDialog;
    private boolean mIsRTL;
    private int mItemWidth;
    private BaseLinearLayoutManager mLayoutManager;
    private LinearLayout mLlProgress;
    /* access modifiers changed from: private */
    public MimojiCreateItemAdapter mMimojiCreateItemAdapter;
    /* access modifiers changed from: private */
    public List mMimojiInfoList;
    /* access modifiers changed from: private */
    public MimojiInfo mMimojiInfoSelect;
    private RecyclerView mMimojiRecylerView;
    /* access modifiers changed from: private */
    public int mSelectIndex;
    private String mSelectState = "close_state";
    private int mTotalWidth;
    private RelativeLayout popContainer;
    private RelativeLayout popParent;

    static /* synthetic */ void O000ooo0() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0054 A[Catch:{ Exception -> 0x0062 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean autoMove(int i, Adapter adapter) {
        int i2;
        boolean z = false;
        if (!(this.mMimojiRecylerView == null || this.mLayoutManager == null || adapter == null || adapter.getItemCount() == 0)) {
            try {
                int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
                int findFirstCompletelyVisibleItemPosition = this.mLayoutManager.findFirstCompletelyVisibleItemPosition();
                int itemCount = adapter == null ? 0 : adapter.getItemCount();
                if (!(i == findFirstVisibleItemPosition || i == findFirstCompletelyVisibleItemPosition)) {
                    if (i != findFirstCompletelyVisibleItemPosition - 2) {
                        if (i != this.mLayoutManager.findLastVisibleItemPosition()) {
                            if (i != this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
                                i2 = i;
                                if (i2 != i) {
                                    z = true;
                                }
                                if (z || i2 == 0 || i2 == itemCount - 1) {
                                    this.mMimojiRecylerView.smoothScrollToPosition(i2);
                                }
                            }
                        }
                        i2 = Math.min(i + 1, itemCount - 1);
                        if (i2 != i) {
                        }
                        this.mMimojiRecylerView.smoothScrollToPosition(i2);
                    }
                }
                i2 = Math.max(0, i - 1);
                if (i2 != i) {
                }
                this.mMimojiRecylerView.smoothScrollToPosition(i2);
            } catch (Exception unused) {
                Log.d(TAG, "mimoji boolean autoMove[position, adapter]");
            }
        }
        return z;
    }

    private boolean scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.max(0, i - 1));
            return true;
        } else if (i != this.mLayoutManager.findLastVisibleItemPosition() && i != this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            return false;
        } else {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mMimojiCreateItemAdapter.getItemCount() - 1));
            return true;
        }
    }

    private void setItemInCenter(int i) {
        this.mLayoutManager.scrollToPositionWithOffset(i, (this.mTotalWidth / 2) - (this.mItemWidth / 2));
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = this.mDeleteDialog;
        if (alertDialog == null) {
            this.mDeleteDialog = RotateDialogController.showSystemAlertDialog(getActivity(), getString(R.string.mimoji_delete_dialog_title), null, null, null, getText(R.string.mimoji_delete), new Runnable() {
                public void run() {
                    if (FragmentMimoji.this.mMimojiInfoSelect != null && !TextUtils.isEmpty(FragmentMimoji.this.mMimojiInfoSelect.mPackPath)) {
                        FileUtils.deleteFile(FragmentMimoji.this.mMimojiInfoSelect.mPackPath);
                        FragmentMimoji.this.bubbleEditMimojiPresenter.processBubbleAni(-2, -2, null);
                        FragmentMimoji.this.filelistToMinojiInfo();
                        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
                        if (mimojiAvatarEngine != null) {
                            mimojiAvatarEngine.onMimojiDeleted();
                        }
                        FragmentMimoji.this.mSelectIndex = 0;
                        FragmentMimoji.this.mMimojiCreateItemAdapter.updateSelect();
                        Log.u(FragmentMimoji.TAG, "delete onClick positive");
                        CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_DELETE, "delete");
                        CameraStatUtils.trackMimojiCount(Integer.toString(FragmentMimoji.this.mMimojiInfoList.size() - 4));
                    }
                }
            }, getString(R.string.mimoji_cancle), C0310O00000oo.INSTANCE);
            this.mDeleteDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMimoji.this.mDeleteDialog = null;
                }
            });
        } else if (!alertDialog.isShowing()) {
            this.mDeleteDialog.show();
        }
    }

    public /* synthetic */ void O000000o(MimojiInfo mimojiInfo, int i, View view) {
        onItemSelected(mimojiInfo, i, view, false);
    }

    public void filelistToMinojiInfo() {
        File[] listFiles;
        String str = "/";
        this.mMimojiInfoList = new ArrayList();
        MimojiInfo mimojiInfo = new MimojiInfo();
        mimojiInfo.mConfigPath = "close_state";
        mimojiInfo.mDirectoryName = Long.MAX_VALUE;
        mimojiInfo.mName = R.string.lighting_pattern_null;
        this.mMimojiInfoList.add(mimojiInfo);
        MimojiInfo mimojiInfo2 = new MimojiInfo();
        mimojiInfo2.mConfigPath = "add_state";
        mimojiInfo2.mDirectoryName = Long.MAX_VALUE;
        mimojiInfo2.mName = R.string.accessibility_add;
        this.mMimojiInfoList.add(mimojiInfo2);
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(MimojiHelper.CUSTOM_DIR);
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    MimojiInfo mimojiInfo3 = new MimojiInfo();
                    mimojiInfo3.mAvatarTemplatePath = AvatarEngineManager.PersonTemplatePath;
                    String name = file2.getName();
                    String absolutePath = file2.getAbsolutePath();
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    sb.append("config.dat");
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(name);
                    sb3.append("pic.png");
                    String sb4 = sb3.toString();
                    if (file2.isDirectory()) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(MimojiHelper.CUSTOM_DIR);
                        sb5.append(name);
                        sb5.append(str);
                        sb5.append(sb2);
                        String sb6 = sb5.toString();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(MimojiHelper.CUSTOM_DIR);
                        sb7.append(name);
                        sb7.append(str);
                        sb7.append(sb4);
                        String sb8 = sb7.toString();
                        if (FileUtils.checkFileConsist(sb6) && FileUtils.checkFileConsist(sb8)) {
                            mimojiInfo3.mConfigPath = sb6;
                            mimojiInfo3.mThumbnailUrl = sb8;
                            mimojiInfo3.mPackPath = absolutePath;
                            mimojiInfo3.mDirectoryName = Long.valueOf(name).longValue();
                            this.mMimojiInfoList.add(mimojiInfo3);
                        }
                    }
                    arrayList.add(absolutePath);
                }
                Collections.sort(this.mMimojiInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MimojiInfo mimojiInfo4 = new MimojiInfo();
        mimojiInfo4.mAvatarTemplatePath = AvatarEngineManager.PigTemplatePath;
        mimojiInfo4.mConfigPath = "pig";
        StringBuilder sb9 = new StringBuilder();
        sb9.append(MimojiHelper.DATA_DIR);
        sb9.append("/pig.png");
        mimojiInfo4.mThumbnailUrl = sb9.toString();
        mimojiInfo4.mName = R.string.mimoji_cartoon_pig;
        this.mMimojiInfoList.add(mimojiInfo4);
        if (C0124O00000oO.O0o00o || C0124O00000oO.O0o00oO || C0124O00000oO.O0o00oo) {
            MimojiInfo mimojiInfo5 = new MimojiInfo();
            mimojiInfo5.mAvatarTemplatePath = AvatarEngineManager.RoyanTemplatePath;
            mimojiInfo5.mConfigPath = "royan";
            StringBuilder sb10 = new StringBuilder();
            sb10.append(MimojiHelper.DATA_DIR);
            sb10.append("/royan.png");
            mimojiInfo5.mThumbnailUrl = sb10.toString();
            mimojiInfo5.mName = R.string.mimoji_cartoon_lion;
            this.mMimojiInfoList.add(mimojiInfo5);
        }
        MimojiInfo mimojiInfo6 = new MimojiInfo();
        mimojiInfo6.mAvatarTemplatePath = AvatarEngineManager.BearTemplatePath;
        mimojiInfo6.mConfigPath = "bear";
        StringBuilder sb11 = new StringBuilder();
        sb11.append(MimojiHelper.DATA_DIR);
        sb11.append("/bear.png");
        mimojiInfo6.mThumbnailUrl = sb11.toString();
        mimojiInfo6.mName = R.string.mimoji_cartoon_bear;
        this.mMimojiInfoList.add(mimojiInfo6);
        MimojiInfo mimojiInfo7 = new MimojiInfo();
        mimojiInfo7.mAvatarTemplatePath = AvatarEngineManager.RabbitTemplatePath;
        mimojiInfo7.mConfigPath = "rabbit";
        StringBuilder sb12 = new StringBuilder();
        sb12.append(MimojiHelper.DATA_DIR);
        sb12.append("/rabbit.png");
        mimojiInfo7.mThumbnailUrl = sb12.toString();
        mimojiInfo7.mName = R.string.mimoji_cartoon_rabbit;
        this.mMimojiInfoList.add(mimojiInfo7);
        this.mMimojiCreateItemAdapter.setMimojiInfoList(this.mMimojiInfoList);
        for (int i = 0; i < arrayList.size(); i++) {
            FileUtils.deleteFile((String) arrayList.get(i));
        }
    }

    public void firstProgressShow(boolean z) {
        if (getActivity() == null) {
            Log.e(TAG, "not attached to Activity , skip     firstProgressShow........");
            return;
        }
        if (this.mLlProgress == null) {
            initView(getView());
        }
        if (z) {
            this.mLlProgress.setVisibility(0);
            this.mMimojiRecylerView.setVisibility(8);
        } else {
            this.mLlProgress.setVisibility(8);
            this.mMimojiRecylerView.setVisibility(0);
        }
    }

    public int getFragmentInto() {
        return 65520;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_mimoji;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mItemWidth = getResources().getDimensionPixelSize(R.dimen.live_sticker_item_size);
        this.mTotalWidth = getResources().getDisplayMetrics().widthPixels;
        this.mContext = getContext();
        this.mIsRTL = Util.isLayoutRTL(this.mContext);
        this.mMimojiRecylerView = (RecyclerView) view.findViewById(R.id.mimoji_list);
        this.mMimojiRecylerView.setFocusable(false);
        this.popContainer = (RelativeLayout) view.findViewById(R.id.ll_bubble_pop_occupation);
        this.popParent = (RelativeLayout) view.findViewById(R.id.rl_bubble_pop_parent);
        this.mLlProgress = (LinearLayout) view.findViewById(R.id.ll_updating);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(0);
        defaultItemAnimator.setMoveDuration(0);
        defaultItemAnimator.setAddDuration(0);
        this.mMimojiRecylerView.setItemAnimator(defaultItemAnimator);
        this.bubbleEditMimojiPresenter = new BubbleEditMimojiPresenter(getContext(), this, this.popParent);
        this.mMimojiRecylerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                FragmentMimoji.this.bubbleEditMimojiPresenter.processBubbleAni(-2, -2, null);
            }
        });
        this.mMimojiCreateItemAdapter = new MimojiCreateItemAdapter(getContext(), this.mSelectState);
        this.mMimojiCreateItemAdapter.setOnItemClickListener(new C0309O00000oO(this));
        firstProgressShow(DataRepository.dataItemLive().getMimojiStatusManager().IsLoading());
        filelistToMinojiInfo();
        this.mLayoutManager = new BaseLinearLayoutManager(getContext());
        this.mLayoutManager.setOrientation(0);
        this.mMimojiRecylerView.setLayoutManager(this.mLayoutManager);
        this.mMimojiRecylerView.addItemDecoration(new EffectItemPadding(getContext()));
        this.mMimojiRecylerView.setAdapter(this.mMimojiCreateItemAdapter);
        this.mSelectIndex = -1;
        String currentMimojiState = DataRepository.dataItemLive().getMimojiStatusManager().getCurrentMimojiState();
        int i = 1;
        while (true) {
            if (i < this.mMimojiInfoList.size()) {
                if (!TextUtils.isEmpty(((MimojiInfo) this.mMimojiInfoList.get(i)).mConfigPath) && currentMimojiState.equals(((MimojiInfo) this.mMimojiInfoList.get(i)).mConfigPath)) {
                    this.mSelectIndex = i;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        setItemInCenter(this.mSelectIndex);
        if (currentMimojiState.equals("close_state")) {
            this.mSelectIndex = 0;
        } else {
            this.mMimojiCreateItemAdapter.updateSelect();
            MimojiInfo mimojiInfoSelected = this.mMimojiCreateItemAdapter.getMimojiInfoSelected();
            if (mimojiInfoSelected != null) {
                onItemSelected(mimojiInfoSelected, -1, null, true);
            }
        }
        DataRepository.dataItemLive().getMimojiStatusManager().setMimojiPannelState(true);
    }

    /* access modifiers changed from: protected */
    public void onAddItemSelected() {
        Log.u(TAG, "onAddItemSelected");
        this.mIsNeedShowWhenExit = false;
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.onMimojiCreate();
        }
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.forceSwitchFront();
        }
    }

    public boolean onBackEvent(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onBackEvent = ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiEdit() && i != 4) {
            return false;
        }
        DataRepository.dataItemLive().getMimojiStatusManager().setMimojiPannelState(false);
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate == null) {
            return false;
        }
        if (baseDelegate.getActiveFragment(R.id.bottom_beauty) != getFragmentInto() && baseDelegate.getActiveFragment(R.id.bottom_mimoji) != getFragmentInto()) {
            return false;
        }
        this.mRemoveFragment = true;
        baseDelegate.delegateEvent(14);
        ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i);
        FolmeUtils.animateDeparture(getView(), O00000o.INSTANCE);
        onAnimationEnd();
        return true;
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue == 101) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClick EDIT_PROCESS ");
            sb.append(intValue);
            Log.u(str, sb.toString());
            MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
            if (mimojiAvatarEngine != null) {
                mimojiAvatarEngine.releaseRender();
            }
            MimojiEditor mimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
            if (mimojiEditor != null) {
                mimojiEditor.directlyEnterEditMode(this.mMimojiInfoSelect, 101);
            }
            CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_EDIT, BaseEvent.EDIT);
            this.bubbleEditMimojiPresenter.processBubbleAni(-2, -2, null);
        } else if (intValue == 102) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onClick DELETE_PROCESS ");
            sb2.append(intValue);
            Log.u(str2, sb2.toString());
            showAlertDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onItemSelected(MimojiInfo mimojiInfo, int i, View view, boolean z) {
        boolean z2;
        View view2;
        String str;
        MimojiInfo mimojiInfo2;
        FragmentMimoji fragmentMimoji;
        String str2;
        String str3;
        if (mimojiInfo != null && !TextUtils.isEmpty(mimojiInfo.mConfigPath)) {
            this.mSelectIndex = i;
            String str4 = mimojiInfo.mConfigPath;
            String currentMimojiState = DataRepository.dataItemLive().getMimojiStatusManager().getCurrentMimojiState();
            String str5 = "add_state";
            if (!str4.equals(str5)) {
                DataRepository.dataItemLive().getMimojiStatusManager().setmCurrentMimojiInfo(mimojiInfo);
            }
            String str6 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("click　currentState:");
            sb.append(str4);
            sb.append(" lastState:");
            sb.append(currentMimojiState);
            Log.i(str6, sb.toString());
            this.bubbleEditMimojiPresenter.processBubbleAni(-2, -2, null);
            if (str5.equals(mimojiInfo.mConfigPath)) {
                onAddItemSelected();
                CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_ADD, BaseEvent.ADD);
                return;
            }
            if (!z) {
                autoMove(i, this.mMimojiCreateItemAdapter);
                z2 = false;
                fragmentMimoji = this;
                mimojiInfo2 = mimojiInfo;
                str = str4;
                view2 = view;
            } else {
                fragmentMimoji = this;
                mimojiInfo2 = mimojiInfo;
                str = str4;
                view2 = view;
                z2 = z;
            }
            fragmentMimoji.processBubble(mimojiInfo2, str, currentMimojiState, view2, z2);
            setAvatarAndSelect(str4, mimojiInfo);
            String str7 = "onItemSelected position=";
            if (mimojiInfo.mName > 0) {
                str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str7);
                sb2.append(i);
                sb2.append(" name=");
                sb2.append(CameraApplicationDelegate.getAndroidContext().getString(mimojiInfo.mName));
                str2 = sb2.toString();
            } else {
                str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str7);
                sb3.append(i);
                str2 = sb3.toString();
            }
            Log.u(str3, str2);
        }
    }

    public void processBubble(MimojiInfo mimojiInfo, String str, String str2, View view, boolean z) {
        boolean isPrefabModel = AvatarEngineManager.isPrefabModel(mimojiInfo.mConfigPath);
        if (str.equals(str2) && !str2.equals("add_state") && !str2.equals("close_state") && !z && !isPrefabModel) {
            this.mMimojiInfoSelect = mimojiInfo;
            int width = view.getWidth();
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int height = this.popContainer.getHeight();
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.manually_indicator_layout_width);
            float f = (float) dimensionPixelSize;
            int i2 = width / 2;
            float dimensionPixelSize2 = f / ((1.0f * f) / ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_edit_bubble_width)));
            int i3 = (int) (((float) (i + i2)) - dimensionPixelSize2);
            if (this.mIsRTL) {
                i3 = (int) (((float) ((this.mTotalWidth - i) - width)) + (((float) i2) - dimensionPixelSize2));
            }
            int i4 = height - (dimensionPixelSize / 2);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("coordinateY:");
            sb.append(i4);
            Log.i(str3, sb.toString());
            this.bubbleEditMimojiPresenter.processBubbleAni(i3, i4, view);
        }
    }

    public int refreshMimojiList() {
        if (this.mMimojiCreateItemAdapter == null) {
            return 0;
        }
        Log.d(TAG, "refreshMimojiList");
        filelistToMinojiInfo();
        this.mSelectIndex = -1;
        String currentMimojiState = DataRepository.dataItemLive().getMimojiStatusManager().getCurrentMimojiState();
        int i = 1;
        while (true) {
            if (i < this.mMimojiInfoList.size()) {
                if (!TextUtils.isEmpty(((MimojiInfo) this.mMimojiInfoList.get(i)).mConfigPath) && currentMimojiState.equals(((MimojiInfo) this.mMimojiInfoList.get(i)).mConfigPath)) {
                    this.mSelectIndex = i;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        setItemInCenter(this.mSelectIndex);
        this.mMimojiCreateItemAdapter.updateSelect();
        return this.mMimojiInfoList.size() - 4;
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        ModeCoordinatorImpl.getInstance().attachProtocol(226, this);
    }

    public void setAvatarAndSelect(String str, MimojiInfo mimojiInfo) {
        if ("close_state".equals(mimojiInfo.mConfigPath)) {
            mimojiInfo = null;
        }
        DataRepository.dataItemLive().getMimojiStatusManager().setmCurrentMimojiInfo(mimojiInfo);
        this.mMimojiCreateItemAdapter.updateSelect();
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.onMimojiSelect(mimojiInfo);
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        this.bubbleEditMimojiPresenter.processBubbleAni(-2, -2, null);
        ModeCoordinatorImpl.getInstance().detachProtocol(226, this);
        DataRepository.dataItemLive().getMimojiStatusManager().setMimojiPannelState(false);
    }
}
