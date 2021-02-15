package com.android.camera.features.mimoji2.fragment.bottomlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.features.mimoji2.bean.MimojiTimbreInfo;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiBottomList;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.widget.baseview.BaseItemAnimator;
import com.android.camera.features.mimoji2.widget.baseview.BaseLinearLayoutManager;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerAdapter;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerViewHolder;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.features.mimoji2.widget.helper.BubbleEditMimojiPresenter2;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.EffectItemAdapter.EffectItemPadding;
import com.android.camera.fragment.live.FragmentLiveBase;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.arcsoft.avatar2.BackgroundInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FragmentMimojiBottomList extends FragmentLiveBase implements MimojiBottomList, OnClickListener {
    public static final String ADD_STATE = "add_state";
    public static final String CLOSE_STATE = "close_state";
    private static final int FRAGMENT_INFO = 65520;
    /* access modifiers changed from: private */
    public static final String TAG = "FragmentMimojiBottomList";
    /* access modifiers changed from: private */
    public BubbleEditMimojiPresenter2 bubbleEditMimojiPresenter2;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    private long mClickTime;
    private Context mContext;
    private HandlerThread mHanderThread;
    private Handler mHandler;
    private boolean mIsRTL;
    private int mItemWidth;
    private BaseLinearLayoutManager mLayoutManager;
    private LinearLayout mLlProgress;
    private MimojiBgAdapter mMimojiBgAdapter;
    /* access modifiers changed from: private */
    public MimojiAvatarAdapter mMimojiCreateItemAdapter2;
    /* access modifiers changed from: private */
    public List mMimojiInfo2List = new ArrayList();
    /* access modifiers changed from: private */
    public MimojiInfo2 mMimojiInfo2Select;
    private RecyclerView mMimojiRecylerView;
    private MimojiTimbreAdapter mMimojiTimbreAdapter;
    /* access modifiers changed from: private */
    public int mSelectIndex;
    private int mTotalWidth;
    private MimojiStatusManager2 mimojiStatusManager2;
    private RelativeLayout popContainer;
    private RelativeLayout popParent;
    private final int[] resourceBg = {R.drawable.ic_mimoji_bg_wave, R.drawable.ic_mimoji_bg_rotate, R.drawable.ic_mimoji_bg_circle, R.drawable.ic_mimoji_bg_white, R.drawable.ic_mimoji_bg_pink, R.drawable.ic_mimoji_bg_blue};
    private final int[] resourceBgDesc = {R.string.mimoji_bg_water_wave, R.string.mimoji_bg_radiate_light, R.string.mimoji_bg_rotating_circle, R.string.mimoji_bg_warm_white, R.string.mimoji_bg_light_pink, R.string.mimoji_bg_light_blue};
    private final int[] resourceTimbre = {R.drawable.ic_mimoji_timbre_gentlemen, R.drawable.ic_mimoji_timbre_lady, R.drawable.ic_mimoji_timbre_girl, R.drawable.ic_mimoji_timbre_baby, R.drawable.ic_mimoji_timbre_robot};
    private final int[] resourceTimbreDesc = {R.string.timbre_gentlemen, R.string.timbre_lady, R.string.timbre_girl, R.string.timbre_baby, R.string.timbre_robot};

    static /* synthetic */ void O000ooO() {
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

    private void initAvatarList() {
        this.mSelectIndex = 0;
        HandlerThread handlerThread = this.mHanderThread;
        if (handlerThread == null || !handlerThread.isAlive()) {
            this.mHanderThread = new HandlerThread("mimojilist") {
            };
            this.mHanderThread.start();
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler(this.mHanderThread.getLooper()) {
                public void handleMessage(@NonNull Message message) {
                    MimojiInfo2 mimojiInfo2;
                    super.handleMessage(message);
                    if (message.what == 65520) {
                        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
                        if (mimojiAvatarEngine2 != null) {
                            Object obj = message.obj;
                            if (obj == null) {
                                mimojiInfo2 = null;
                                mimojiAvatarEngine2.onMimojiChangeBg(null);
                            } else {
                                mimojiInfo2 = (MimojiInfo2) obj;
                            }
                            mimojiAvatarEngine2.onMimojiSelect(mimojiInfo2, false);
                        }
                    }
                }
            };
        }
        int i = DataRepository.dataItemRunning().getUiStyle() == 4 ? R.drawable.makeup_item_bg_white : R.drawable.makeup_item_bg;
        this.mMimojiCreateItemAdapter2 = new MimojiAvatarAdapter(null);
        this.mMimojiCreateItemAdapter2.setResourceBg(i);
        this.mMimojiCreateItemAdapter2.setOnRecyclerItemClickListener(new O00000Oo(this));
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        int i2 = 1;
        boolean z = this.mimojiStatusManager2.IsLoading() || (mimojiAvatarEngine2 != null && !mimojiAvatarEngine2.isAvatarInited());
        firstProgressShow(z);
        filelistToMinojiInfo();
        this.mLayoutManager = new BaseLinearLayoutManager(getContext());
        this.mLayoutManager.setOrientation(0);
        this.mMimojiRecylerView.setLayoutManager(this.mLayoutManager);
        this.mMimojiRecylerView.setAdapter(this.mMimojiCreateItemAdapter2);
        String currentMimojiState = this.mimojiStatusManager2.getCurrentMimojiState();
        while (true) {
            if (i2 < this.mMimojiInfo2List.size()) {
                if (!TextUtils.isEmpty(((MimojiInfo2) this.mMimojiInfo2List.get(i2)).mConfigPath) && currentMimojiState.equals(((MimojiInfo2) this.mMimojiInfo2List.get(i2)).mConfigPath)) {
                    this.mSelectIndex = i2;
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        this.mMimojiCreateItemAdapter2.setLastSelectPosition(this.mSelectIndex);
        this.mMimojiCreateItemAdapter2.setRotation(this.mDegree);
        this.mMimojiCreateItemAdapter2.notifyDataSetChanged();
    }

    private void initBgList() {
        ArrayList arrayList = new ArrayList();
        CopyOnWriteArrayList backgroundInfos = AvatarEngineManager2.getInstance().getBackgroundInfos();
        if (backgroundInfos.size() != this.resourceBg.length) {
            Log.e(TAG, "mimoji bg resource size error");
            return;
        }
        this.mSelectIndex = 0;
        MimojiBgInfo currentMimojiBgInfo = this.mimojiStatusManager2.getCurrentMimojiBgInfo();
        arrayList.add(new MimojiBgInfo(currentMimojiBgInfo == null));
        int i = 0;
        while (i < backgroundInfos.size()) {
            int i2 = i + 1;
            MimojiBgInfo mimojiBgInfo = new MimojiBgInfo((BackgroundInfo) backgroundInfos.get(i), this.resourceBg[i], this.resourceBgDesc[i], i2);
            if (currentMimojiBgInfo != null && currentMimojiBgInfo.getBackgroundInfo().getName().equals(mimojiBgInfo.getBackgroundInfo().getName())) {
                mimojiBgInfo.setSelected(true);
                this.mSelectIndex = i;
            }
            arrayList.add(mimojiBgInfo);
            i = i2;
        }
        this.mMimojiBgAdapter = new MimojiBgAdapter(arrayList);
        this.mMimojiBgAdapter.setRotation(this.mDegree);
        this.mMimojiBgAdapter.setOnRecyclerItemClickListener(new O00000o0(this));
        this.mLayoutManager = new BaseLinearLayoutManager(getContext());
        this.mLayoutManager.setOrientation(0);
        this.mMimojiRecylerView.setLayoutManager(this.mLayoutManager);
        this.mMimojiRecylerView.setAdapter(this.mMimojiBgAdapter);
    }

    private void initMargin() {
        int i;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.popParent.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
        if (this.mimojiStatusManager2.isInMimojiPreviewPlay()) {
            i = 0;
        } else if (CameraSettings.isGifOn()) {
            marginLayoutParams.bottomMargin = Display.getBottomHeight() - ((dimensionPixelSize / 2) * 3);
            this.popParent.setLayoutParams(marginLayoutParams);
        } else {
            i = Display.getBottomHeight();
        }
        marginLayoutParams.bottomMargin = i;
        this.popParent.setLayoutParams(marginLayoutParams);
    }

    private void initTimbreList() {
        ArrayList arrayList = new ArrayList();
        if (this.resourceTimbre.length != MimojiTimbreInfo.timbreTypes.length) {
            Log.e(TAG, "mimoji timbre resource size error");
            return;
        }
        boolean z = false;
        this.mSelectIndex = 0;
        MimojiTimbreInfo currentMimojiTimbreInfo = this.mimojiStatusManager2.getCurrentMimojiTimbreInfo();
        arrayList.add(new MimojiTimbreInfo(currentMimojiTimbreInfo == null));
        int i = 0;
        while (true) {
            int[] iArr = this.resourceTimbre;
            if (i >= iArr.length) {
                break;
            }
            MimojiTimbreInfo mimojiTimbreInfo = new MimojiTimbreInfo(MimojiTimbreInfo.timbreTypes[i], iArr[i], this.resourceTimbreDesc[i]);
            if (currentMimojiTimbreInfo != null && currentMimojiTimbreInfo.getTimbreId() == mimojiTimbreInfo.getTimbreId()) {
                mimojiTimbreInfo.setSelected(true);
                this.mSelectIndex = i;
                this.mSelectIndex++;
            }
            arrayList.add(mimojiTimbreInfo);
            i++;
        }
        this.mMimojiTimbreAdapter = new MimojiTimbreAdapter(arrayList);
        this.mMimojiTimbreAdapter.setRotation(this.mDegree);
        this.mMimojiTimbreAdapter.setOnRecyclerItemClickListener(new O0000O0o(this));
        this.mLayoutManager = new BaseLinearLayoutManager(getContext());
        this.mLayoutManager.setOrientation(0);
        this.mMimojiRecylerView.setLayoutManager(this.mLayoutManager);
        this.mMimojiRecylerView.setAdapter(this.mMimojiTimbreAdapter);
        MimojiTimbreAdapter mimojiTimbreAdapter = this.mMimojiTimbreAdapter;
        int i2 = this.mSelectIndex;
        if (this.mimojiStatusManager2.isInMimojiPreviewPlay() && this.mMimojiTimbreAdapter.getItemCount() >= this.mSelectIndex && ((MimojiTimbreInfo) this.mMimojiTimbreAdapter.getDataList().get(this.mSelectIndex)).getTimbreId() > 0) {
            z = true;
        }
        mimojiTimbreAdapter.setSelectState(i2, z);
        this.mMimojiTimbreAdapter.hideProgress();
    }

    private boolean scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.max(0, i - 1));
            return true;
        } else if (i != this.mLayoutManager.findLastVisibleItemPosition() && i != this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            return false;
        } else {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mMimojiCreateItemAdapter2.getItemCount() - 1));
            return true;
        }
    }

    private void setSelectItemInCenter() {
        if (this.mimojiStatusManager2.getMimojiPanelState() != 0) {
            this.mLayoutManager.scrollToPositionWithOffset(this.mSelectIndex, (this.mTotalWidth / 2) - (this.mItemWidth / 2));
        }
    }

    private void showAlertDialog() {
        if (this.mAlertDialog == null) {
            this.mAlertDialog = RotateDialogController.showSystemAlertDialog(getActivity(), getString(R.string.mimoji_delete_dialog_title), null, null, null, getText(R.string.mimoji_delete), new Runnable() {
                public void run() {
                    if (FragmentMimojiBottomList.this.mMimojiInfo2Select != null && !TextUtils.isEmpty(FragmentMimojiBottomList.this.mMimojiInfo2Select.mPackPath)) {
                        FileUtils.deleteFile(FragmentMimojiBottomList.this.mMimojiInfo2Select.mPackPath);
                        FragmentMimojiBottomList.this.bubbleEditMimojiPresenter2.processBubbleAni(-2, -2, null);
                        FragmentMimojiBottomList.this.mSelectIndex = 0;
                        FragmentMimojiBottomList.this.mMimojiCreateItemAdapter2.setLastSelectPosition(0);
                        FragmentMimojiBottomList.this.mMimojiCreateItemAdapter2.notifyDataSetChanged();
                        FragmentMimojiBottomList.this.filelistToMinojiInfo();
                        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
                        if (mimojiAvatarEngine2 != null) {
                            mimojiAvatarEngine2.onMimojiDeleted();
                        }
                        Log.u(FragmentMimojiBottomList.TAG, "delete onClick positive");
                        CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_DELETE, "delete");
                        CameraStatUtils.trackMimojiCount(Integer.toString(FragmentMimojiBottomList.this.mMimojiInfo2List.size() - 4));
                    }
                }
            }, getString(R.string.mimoji_cancle), O00000o.INSTANCE);
            this.mAlertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentMimojiBottomList.this.mAlertDialog = null;
                }
            });
        }
    }

    public /* synthetic */ void O000000o(MimojiBgInfo mimojiBgInfo, int i, View view) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onMimojiChangeBg position=");
        sb.append(i);
        sb.append(", text=");
        sb.append(CameraApplicationDelegate.getAndroidContext().getString(mimojiBgInfo.getDescId()));
        Log.u(str, sb.toString());
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 != null) {
            mimojiAvatarEngine2.onMimojiChangeBg(mimojiBgInfo);
        }
        this.mMimojiBgAdapter.setSelectState(i);
        autoMove(i, this.mMimojiBgAdapter);
        if (Util.isAccessible()) {
            view.postDelayed(new O0000OOo(this, view), 100);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0102  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O000000o(MimojiInfo2 mimojiInfo2, int i, View view) {
        String str;
        String str2;
        StringBuilder sb;
        int i2;
        Context context;
        if (mimojiInfo2 != null && !TextUtils.isEmpty(mimojiInfo2.mConfigPath)) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mClickTime >= ((long) BaseItemAnimator.DEFAULT_LIST_DURATION)) {
                this.mClickTime = currentTimeMillis;
                this.mSelectIndex = i;
                this.bubbleEditMimojiPresenter2.processBubbleAni(-2, -2, null);
                if ("add_state".equals(mimojiInfo2.mConfigPath)) {
                    onAddItemSelected();
                    CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_ADD, BaseEvent.ADD);
                    return;
                }
                String str3 = mimojiInfo2.mConfigPath;
                String currentMimojiState = this.mimojiStatusManager2.getCurrentMimojiState();
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("click　currentState:");
                sb2.append(str3);
                sb2.append(" lastState:");
                sb2.append(currentMimojiState);
                Log.i(str4, sb2.toString());
                if (currentMimojiState.equals(str3) && mimojiInfo2.getDefaultFrame() > 0) {
                    mimojiInfo2.nextFrame();
                    mimojiInfo2.setIsNeedAnim(true);
                }
                this.mimojiStatusManager2.setCurrentMimojiInfo(mimojiInfo2);
                this.mMimojiCreateItemAdapter2.setSelectState(i);
                String str5 = " name=";
                String str6 = "onItemSelected position=";
                if (mimojiInfo2.getFrame() > 0 && mimojiInfo2.mName2 > 0) {
                    str2 = TAG;
                    sb = new StringBuilder();
                    sb.append(str6);
                    sb.append(i);
                    sb.append(str5);
                    context = view.getContext();
                    i2 = mimojiInfo2.mName2;
                } else if (mimojiInfo2.mName > 0) {
                    str2 = TAG;
                    sb = new StringBuilder();
                    sb.append(str6);
                    sb.append(i);
                    sb.append(str5);
                    context = view.getContext();
                    i2 = mimojiInfo2.mName;
                } else {
                    str2 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str6);
                    sb3.append(i);
                    str = sb3.toString();
                    Log.u(str2, str);
                    if (!autoMove(i, this.mMimojiCreateItemAdapter2)) {
                        processBubble(mimojiInfo2, str3, currentMimojiState, view, false);
                    }
                    Message obtainMessage = this.mHandler.obtainMessage();
                    obtainMessage.what = 65520;
                    if ("close_state".equals(mimojiInfo2.mConfigPath)) {
                        mimojiInfo2 = null;
                    }
                    obtainMessage.obj = mimojiInfo2;
                    this.mHandler.sendMessageDelayed(obtainMessage, 200);
                }
                sb.append(context.getString(i2));
                str = sb.toString();
                Log.u(str2, str);
                if (!autoMove(i, this.mMimojiCreateItemAdapter2)) {
                }
                Message obtainMessage2 = this.mHandler.obtainMessage();
                obtainMessage2.what = 65520;
                if ("close_state".equals(mimojiInfo2.mConfigPath)) {
                }
                obtainMessage2.obj = mimojiInfo2;
                this.mHandler.sendMessageDelayed(obtainMessage2, 200);
            }
        }
    }

    public /* synthetic */ void O000000o(MimojiTimbreInfo mimojiTimbreInfo, int i, View view) {
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor == null || !mimojiVideoEditor.isComposing()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onMimojiChangeTimbre position=");
            sb.append(i);
            sb.append(", text=");
            sb.append(CameraApplicationDelegate.getAndroidContext().getString(mimojiTimbreInfo.getDescId()));
            Log.u(str, sb.toString());
            MimojiTimbreAdapter mimojiTimbreAdapter = this.mMimojiTimbreAdapter;
            boolean z = this.mimojiStatusManager2.isInMimojiPreviewPlay() && mimojiTimbreInfo.getTimbreId() > 0;
            if (mimojiTimbreAdapter.setSelectState(i, z)) {
                MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
                if (mimojiAvatarEngine2 != null) {
                    mimojiAvatarEngine2.onMimojiChangeTimbre(mimojiTimbreInfo, i);
                }
                autoMove(i, this.mMimojiTimbreAdapter);
            }
            if (Util.isAccessible()) {
                view.postDelayed(new C0259O00000oo(this, view), 100);
            }
        }
    }

    public /* synthetic */ void O0000Ooo(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(32768);
        }
    }

    public /* synthetic */ void O0000o00(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(32768);
        }
    }

    public /* synthetic */ void O000ooO0() {
        if (this.mimojiStatusManager2.getMimojiPanelState() == 3) {
            MimojiTimbreAdapter mimojiTimbreAdapter = this.mMimojiTimbreAdapter;
            if (mimojiTimbreAdapter != null) {
                mimojiTimbreAdapter.hideProgress();
            }
        }
    }

    public /* synthetic */ void O000ooOO() {
        int i = 1;
        if (this.mimojiStatusManager2.getMimojiPanelState() != 1 || this.mMimojiCreateItemAdapter2 == null) {
            if (this.mimojiStatusManager2.getMimojiPanelState() == 3) {
                MimojiTimbreAdapter mimojiTimbreAdapter = this.mMimojiTimbreAdapter;
                if (mimojiTimbreAdapter != null) {
                    mimojiTimbreAdapter.hideProgress();
                }
            }
            if (this.mMimojiRecylerView.getAdapter() != null) {
                this.mMimojiRecylerView.getAdapter().notifyDataSetChanged();
            }
            return;
        }
        filelistToMinojiInfo();
        this.bubbleEditMimojiPresenter2.processBubbleAni(-2, -2, null);
        Log.d(TAG, "refreshMimojiList AVATAR");
        this.mSelectIndex = 0;
        String currentMimojiState = this.mimojiStatusManager2.getCurrentMimojiState();
        while (true) {
            if (i < this.mMimojiInfo2List.size()) {
                if (!TextUtils.isEmpty(((MimojiInfo2) this.mMimojiInfo2List.get(i)).mConfigPath) && currentMimojiState.equals(((MimojiInfo2) this.mMimojiInfo2List.get(i)).mConfigPath)) {
                    this.mSelectIndex = i;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        this.mMimojiCreateItemAdapter2.setLastSelectPosition(-1);
        this.mMimojiCreateItemAdapter2.setSelectState(this.mSelectIndex);
        CameraStatUtils.trackMimojiCount(Integer.toString(this.mMimojiCreateItemAdapter2.getItemCount()));
    }

    public void filelistToMinojiInfo() {
        if (this.mMimojiCreateItemAdapter2 != null && !this.mimojiStatusManager2.IsLoading()) {
            if (this.mimojiStatusManager2.getAvatarPanelState() == 100) {
                this.mMimojiInfo2List = MimojiHelper2.getMimojiHumanList();
            } else {
                this.mMimojiInfo2List = MimojiHelper2.getMimojiCartoonList();
                MimojiInfo2 currentMimojiInfo = this.mimojiStatusManager2.getCurrentMimojiInfo();
                if (currentMimojiInfo != null && !TextUtils.isEmpty(currentMimojiInfo.mAvatarTemplatePath)) {
                    Iterator it = this.mMimojiInfo2List.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        MimojiInfo2 mimojiInfo2 = (MimojiInfo2) it.next();
                        if (mimojiInfo2 != null && !TextUtils.isEmpty(mimojiInfo2.mAvatarTemplatePath) && mimojiInfo2.mAvatarTemplatePath.equals(currentMimojiInfo.mAvatarTemplatePath)) {
                            mimojiInfo2.setFrame(currentMimojiInfo.getFrame());
                            Message obtainMessage = this.mHandler.obtainMessage();
                            obtainMessage.what = 65520;
                            obtainMessage.obj = mimojiInfo2;
                            this.mHandler.sendMessageDelayed(obtainMessage, 200);
                            break;
                        }
                    }
                }
            }
            this.mMimojiCreateItemAdapter2.setDataList(this.mMimojiInfo2List);
        }
    }

    public void firstProgressShow(boolean z) {
        if (getActivity() == null) {
            Log.e(TAG, "not attached to Activity , skip     firstProgressShow........");
            return;
        }
        LinearLayout linearLayout = this.mLlProgress;
        if (linearLayout == null) {
            initView(getView());
            return;
        }
        if (z) {
            linearLayout.setVisibility(0);
            this.mMimojiRecylerView.setVisibility(8);
        } else {
            linearLayout.setVisibility(8);
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

    public int hideTimbreProgress() {
        if (this.mMimojiRecylerView == null || getActivity() == null) {
            return -1;
        }
        getActivity().runOnUiThread(new O0000Oo0(this));
        return 0;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mItemWidth = getResources().getDimensionPixelSize(R.dimen.live_sticker_item_size);
        this.mTotalWidth = getResources().getDisplayMetrics().widthPixels;
        this.mContext = getContext();
        this.mIsRTL = Util.isLayoutRTL(this.mContext);
        this.mMimojiRecylerView = (RecyclerView) view.findViewById(R.id.mimoji_list);
        this.mMimojiRecylerView.setFocusable(false);
        this.popContainer = (RelativeLayout) view.findViewById(R.id.ll_bubble_pop_occupation);
        this.popParent = (RelativeLayout) view.findViewById(R.id.rl_bubble_pop_parent);
        this.mLlProgress = (LinearLayout) view.findViewById(R.id.ll_updating);
        this.bubbleEditMimojiPresenter2 = new BubbleEditMimojiPresenter2(getContext(), this, this.popParent);
        this.mMimojiRecylerView.setItemAnimator(new BaseItemAnimator());
        this.mMimojiRecylerView.addItemDecoration(new EffectItemPadding(getContext()));
        this.mMimojiRecylerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                FragmentMimojiBottomList.this.bubbleEditMimojiPresenter2.processBubbleAni(-2, -2, null);
            }
        });
        this.mSelectIndex = -1;
        this.mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        initMargin();
        int mimojiPanelState = this.mimojiStatusManager2.getMimojiPanelState();
        if (mimojiPanelState == 0) {
            Log.d(TAG, "init MimojiPanelState close");
        } else if (mimojiPanelState == 1) {
            initAvatarList();
        } else if (mimojiPanelState == 2) {
            initBgList();
        } else if (mimojiPanelState == 3) {
            initTimbreList();
        }
        setSelectItemInCenter();
    }

    /* access modifiers changed from: protected */
    public void onAddItemSelected() {
        Log.u(TAG, "onAddItemSelected");
        this.mIsNeedShowWhenExit = false;
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 != null) {
            mimojiAvatarEngine2.onMimojiCreate();
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
        if (this.mimojiStatusManager2.isInMimojiEdit() && i != 4) {
            return false;
        }
        if (this.mimojiStatusManager2.isInMimojiPreview()) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (!(bottomPopupTips == null || this.mimojiStatusManager2.getMimojiPanelState() == 0)) {
                boolean showMimojiPanel = bottomPopupTips.showMimojiPanel(0);
                if (i == 1 && showMimojiPanel) {
                    return true;
                }
            }
        }
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
        FolmeUtils.animateDeparture(getView(), O000000o.INSTANCE);
        onAnimationEnd();
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0065, code lost:
        if (r13 != null) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00c2, code lost:
        if (r13 != null) goto L_0x00c4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        BottomPopupTips bottomPopupTips;
        int intValue = ((Integer) view.getTag()).intValue();
        String str = BaseEvent.EDIT;
        String str2 = Mimoji.MIMOJI_CLICK_EDIT;
        if (intValue == 201) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClick EDIT_PROCESS ");
            sb.append(intValue);
            Log.u(str3, sb.toString());
            MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine2 != null) {
                mimojiAvatarEngine2.releaseRender();
            }
            MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
            if (mimojiEditor2 != null) {
                mimojiEditor2.directlyEnterEditMode(this.mMimojiInfo2Select, 201);
            }
            bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        } else if (intValue == 202) {
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onClick DELETE_PROCESS ");
            sb2.append(intValue);
            Log.u(str4, sb2.toString());
            showAlertDialog();
            return;
        } else if (intValue == 204) {
            String str5 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onClick EMOTICON_PROCESS ");
            sb3.append(intValue);
            Log.u(str5, sb3.toString());
            MimojiAvatarEngine2 mimojiAvatarEngine22 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine22 != null) {
                mimojiAvatarEngine22.releaseRender();
            }
            MimojiEditor2 mimojiEditor22 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
            if (mimojiEditor22 != null) {
                mimojiEditor22.directlyEnterEditMode(this.mMimojiInfo2Select, 204);
            }
            bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        } else {
            return;
        }
        bottomPopupTips.showMimojiPanel(0);
        CameraStatUtils.trackMimojiClick(str2, str);
        this.bubbleEditMimojiPresenter2.processBubbleAni(-2, -2, null);
    }

    public void processBubble(MimojiInfo2 mimojiInfo2, String str, String str2, View view, boolean z) {
        boolean isPreCartoonModel = AvatarEngineManager2.isPreCartoonModel(mimojiInfo2.mConfigPath);
        if (str.equals(str2) && !str2.equals("add_state") && !str2.equals("close_state") && !z && !isPreCartoonModel) {
            this.mMimojiInfo2Select = mimojiInfo2;
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
            BubbleEditMimojiPresenter2 bubbleEditMimojiPresenter22 = this.bubbleEditMimojiPresenter2;
            if (bubbleEditMimojiPresenter22 != null) {
                bubbleEditMimojiPresenter22.getBubblePop().mIvDeleteFisrt.setRotation((float) this.mDegree);
                this.bubbleEditMimojiPresenter2.getBubblePop().mIvEditFirst.setRotation((float) this.mDegree);
                this.bubbleEditMimojiPresenter2.getBubblePop().mIvEmoticonFirst.setRotation((float) this.mDegree);
            }
            this.bubbleEditMimojiPresenter2.processBubbleAni(i3, i4, view);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        BubbleEditMimojiPresenter2 bubbleEditMimojiPresenter22 = this.bubbleEditMimojiPresenter2;
        if (bubbleEditMimojiPresenter22 != null) {
            list.add(bubbleEditMimojiPresenter22.getBubblePop().mIvDeleteFisrt);
            list.add(this.bubbleEditMimojiPresenter2.getBubblePop().mIvEditFirst);
            list.add(this.bubbleEditMimojiPresenter2.getBubblePop().mIvEmoticonFirst);
        }
        if (this.mMimojiRecylerView != null) {
            for (int i2 = 0; i2 < this.mMimojiRecylerView.getChildCount(); i2++) {
                BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) this.mMimojiRecylerView.getChildViewHolder(this.mMimojiRecylerView.getChildAt(i2));
                if (!(baseRecyclerViewHolder == null || baseRecyclerViewHolder.getRotateViews() == null)) {
                    list.addAll(Arrays.asList(baseRecyclerViewHolder.getRotateViews()));
                }
            }
        }
        if (this.mMimojiRecylerView.getAdapter() != null) {
            ((BaseRecyclerAdapter) this.mMimojiRecylerView.getAdapter()).setRotation(i);
            int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
            for (int i3 = 0; i3 < findFirstVisibleItemPosition; i3++) {
                this.mMimojiRecylerView.getAdapter().notifyItemChanged(i3);
            }
            while (true) {
                findLastVisibleItemPosition++;
                if (findLastVisibleItemPosition < this.mMimojiRecylerView.getAdapter().getItemCount()) {
                    this.mMimojiRecylerView.getAdapter().notifyItemChanged(findLastVisibleItemPosition);
                } else {
                    return;
                }
            }
        }
    }

    public int refreshMimojiList() {
        if (this.mMimojiRecylerView == null || getActivity() == null || this.mimojiStatusManager2.IsLoading()) {
            return -1;
        }
        getActivity().runOnUiThread(new C0258O00000oO(this));
        return 0;
    }

    public int refreshMimojiList(int i) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        this.mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        ModeCoordinatorImpl.getInstance().attachProtocol(248, this);
    }

    public int switchMimojiList() {
        if (this.mimojiStatusManager2.IsLoading()) {
            return -1;
        }
        FolmeUtils.animateHide(this.mMimojiRecylerView);
        refreshMimojiList();
        FolmeUtils.animateShow(this.mMimojiRecylerView);
        return 0;
    }

    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        BubbleEditMimojiPresenter2 bubbleEditMimojiPresenter22 = this.bubbleEditMimojiPresenter2;
        if (bubbleEditMimojiPresenter22 != null) {
            bubbleEditMimojiPresenter22.processBubbleAni(-2, -2, null);
        }
        ModeCoordinatorImpl.getInstance().detachProtocol(248, this);
        this.mimojiStatusManager2.setMimojiPanelState(0);
        HandlerThread handlerThread = this.mHanderThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mHanderThread = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(65520);
            this.mHandler = null;
        }
    }
}
