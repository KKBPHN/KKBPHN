package com.android.camera.fragment.mimoji;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.widget.autoselectview.AutoSelectAdapter;
import com.android.camera.features.mimoji2.widget.autoselectview.AutoSelectHorizontalView;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.mimoji.EditLevelListAdapter.ItfGvOnItemClickListener;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.module.impl.component.MimojiStatusManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MimojiAlert;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.MimojiEditor;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.ui.MimojiEditGLTextureView;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigType;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar.AvatarEngine;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class FragmentMimojiEdit extends BaseFragment implements MimojiEditor, OnClickListener, HandleBackTrace {
    private static final int EDIT_ABANDON = 4;
    private static final int EDIT_ABANDON_CAPTURE = 3;
    private static final int EDIT_BACK = 1;
    private static final int EDIT_CANCEL = 5;
    private static final int EDIT_RECAPTURE = 2;
    public static final int EDIT_STATE_STEP1 = 1;
    public static final int EDIT_STATE_STEP2_1 = 2;
    public static final int EDIT_STATE_STEP2_2 = 4;
    private static final int EDIT_STATE_STEP3 = 3;
    private static final int EDIT_STATE_STEP4 = 5;
    private static final int FRAGMENT_INFO = 65521;
    public static final int FROM_ALL_PROCESS = 105;
    public static final String TAG = "FragmentMimojiEdit";
    /* access modifiers changed from: private */
    public int fromTag;
    /* access modifiers changed from: private */
    public AvatarEngine mAvatar;
    private AvatarEngineManager mAvatarEngineManager;
    private TextView mBackTextView;
    private ClickCheck mClickCheck;
    private TextView mConfirmTextView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public AlertDialog mCurrentAlertDialog;
    /* access modifiers changed from: private */
    public String mCurrentConfigPath;
    /* access modifiers changed from: private */
    public int mCurrentTopPannelState = -1;
    /* access modifiers changed from: private */
    public EditLevelListAdapter mEditLevelListAdapter;
    /* access modifiers changed from: private */
    public boolean mEditState = false;
    private TextView mEditTextView;
    /* access modifiers changed from: private */
    public boolean mEnterFromMimoji = false;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler;
    private boolean mIsSaveBtnClicked = false;
    /* access modifiers changed from: private */
    public boolean mIsStartEdit;
    private RecyclerView mLevelRecyleView;
    /* access modifiers changed from: private */
    public MimojiEditGLTextureView mMimojiEditGLTextureView;
    /* access modifiers changed from: private */
    public View mMimojiEditViewLayout;
    private ViewStub mMimojiEditViewStub;
    /* access modifiers changed from: private */
    public MimojiPageChangeAnimManager mMimojiPageChangeAnimManager;
    private AutoSelectAdapter mMimojiTypeAdapter;
    private AutoSelectHorizontalView mMimojiTypeSelectView;
    private TextView mMimojiTypeView;
    private LinearLayout mOperateSelectLayout;
    /* access modifiers changed from: private */
    public String mPopSaveDeletePath;
    private TextView mReCaptureTextView;
    /* access modifiers changed from: private */
    public MimojiThumbnailRenderThread mRenderThread;
    private LinearLayout mRlAllEditContent;
    private TextView mSaveTextView;
    private volatile boolean mSetupCompleted;
    private Thread mSetupThread;

    public FragmentMimojiEdit() {
        String str = "";
        this.mCurrentConfigPath = str;
        this.mPopSaveDeletePath = str;
        this.mSetupCompleted = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 4) {
                    Bitmap thumbnailBitmapFromData = MimojiHelper.getThumbnailBitmapFromData((byte[]) message.obj, 200, 200);
                    String format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append(MimojiHelper.CUSTOM_DIR);
                    sb.append(format);
                    sb.append("/");
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(sb2);
                    sb3.append(format);
                    sb3.append("config.dat");
                    String sb4 = sb3.toString();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(sb2);
                    sb5.append(format);
                    sb5.append("pic.png");
                    String sb6 = sb5.toString();
                    FileUtils.saveBitmap(thumbnailBitmapFromData, sb6);
                    int saveConfig = FragmentMimojiEdit.this.mAvatar.saveConfig(sb4);
                    FragmentMimojiEdit.this.mAvatar.loadConfig(sb4);
                    String str = FragmentMimojiEdit.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("res = ");
                    sb7.append(saveConfig);
                    sb7.append("  save path : ");
                    sb7.append(sb4);
                    Log.d(str, sb7.toString());
                    if (FragmentMimojiEdit.this.mCurrentTopPannelState == 4) {
                        FileUtils.deleteFile(FragmentMimojiEdit.this.mPopSaveDeletePath);
                    }
                    MimojiInfo mimojiInfo = new MimojiInfo();
                    mimojiInfo.mConfigPath = sb4;
                    mimojiInfo.mAvatarTemplatePath = AvatarEngineManager.PersonTemplatePath;
                    mimojiInfo.mThumbnailUrl = sb6;
                    DataRepository.dataItemLive().getMimojiStatusManager().setmCurrentMimojiInfo(mimojiInfo);
                    FragmentMimojiEdit.this.goBack(false, true);
                } else if (i == 5) {
                    Bundle bundle = (Bundle) message.obj;
                    int i2 = bundle.getInt("OUTER");
                    int i3 = bundle.getInt("INNER");
                    FragmentMimojiEdit.this.mEditLevelListAdapter.notifyThumbnailUpdate(bundle.getInt("TYPE"), i2, i3);
                } else if (i == 6) {
                    int selectType = AvatarEngineManager.getInstance().getSelectType();
                    boolean isColorSelected = AvatarEngineManager.getInstance().isColorSelected();
                    CopyOnWriteArrayList subConfigList = AvatarEngineManager.getInstance().getSubConfigList(FragmentMimojiEdit.this.mContext, selectType);
                    boolean isNeedUpdate = AvatarEngineManager.getInstance().isNeedUpdate(selectType);
                    FragmentMimojiEdit.this.mEditLevelListAdapter.refreshData(subConfigList, !isNeedUpdate, isColorSelected);
                    if (isNeedUpdate) {
                        FragmentMimojiEdit.this.mRenderThread.draw(false);
                    }
                }
            }
        };
    }

    private void initConfigList() {
        this.mRenderThread.initAvatar(this.mEnterFromMimoji ? this.mCurrentConfigPath : AvatarEngineManager.TempOriginalConfigPath);
        ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
        this.mAvatar.getConfigValue(aSAvatarConfigValue);
        this.mAvatarEngineManager.setASAvatarConfigValue(aSAvatarConfigValue);
        this.mAvatarEngineManager.setConfigTypeList(this.mAvatar.getSupportConfigType(this.mAvatarEngineManager.getASAvatarConfigValue().gender));
        if (this.mLevelRecyleView.getAdapter() == null || this.mEditLevelListAdapter == null) {
            if (this.mEditLevelListAdapter == null) {
                this.mEditLevelListAdapter = new EditLevelListAdapter(this.mContext, new O0000OOo(this));
            }
            this.mLevelRecyleView.setAdapter(this.mEditLevelListAdapter);
        }
        this.mEditLevelListAdapter.setIsColorNeedNotify(true);
        if (this.mMimojiTypeAdapter == null) {
            this.mMimojiTypeAdapter = new AutoSelectAdapter(null);
            this.mMimojiTypeAdapter.setOnSelectListener(new O0000Oo0(this));
        }
        ArrayList configTypeList = AvatarEngineManager.getInstance().getConfigTypeList();
        ArrayList arrayList = new ArrayList();
        Iterator it = configTypeList.iterator();
        while (it.hasNext()) {
            ASAvatarConfigType aSAvatarConfigType = (ASAvatarConfigType) it.next();
            ArrayList config = AvatarEngineManager.getInstance().queryAvatar().getConfig(aSAvatarConfigType.configType, AvatarEngineManager.getInstance().getASAvatarConfigValue().gender);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("putConfigList:");
            sb.append(aSAvatarConfigType.configTypeDesc);
            sb.append(":");
            sb.append(aSAvatarConfigType.configType);
            Log.i(str, sb.toString());
            AvatarEngineManager.getInstance().putConfigList(aSAvatarConfigType.configType, config);
            if (!AvatarEngineManager.filterTypeTitle(aSAvatarConfigType.configType)) {
                MimojiTypeBean mimojiTypeBean = new MimojiTypeBean();
                String replaceTabTitle = AvatarEngineManager.replaceTabTitle(getContext(), aSAvatarConfigType.configType);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(replaceTabTitle);
                sb2.append("");
                mimojiTypeBean.setText(sb2.toString());
                mimojiTypeBean.setCurLength(this.mMimojiTypeView.getPaint().measureText(mimojiTypeBean.getText()));
                mimojiTypeBean.setCurTotalLength(arrayList.size() > 0 ? ((MimojiTypeBean) arrayList.get(arrayList.size() - 1)).getCurTotalLength() + mimojiTypeBean.getCurLength() : mimojiTypeBean.getCurLength());
                mimojiTypeBean.setAlpha(0);
                mimojiTypeBean.setASAvatarConfigType(aSAvatarConfigType);
                arrayList.add(mimojiTypeBean);
            }
        }
        if (arrayList.size() == 0) {
            Log.e(TAG, " initConfigList() size 0 error");
            return;
        }
        this.mMimojiTypeAdapter.setDataList(arrayList);
        this.mMimojiTypeSelectView.setAdapter(this.mMimojiTypeAdapter);
    }

    private void initMimojiEdit(View view) {
        ((RelativeLayout) view.findViewById(R.id.rv_navigation_layout)).setOnClickListener(this);
        ((RelativeLayout) view.findViewById(R.id.rl_fragment_mimoji_edit_container)).setOnClickListener(this);
        this.mRlAllEditContent = (LinearLayout) view.findViewById(R.id.ll_bottom_editoperate_content);
        this.mReCaptureTextView = (TextView) view.findViewById(R.id.tv_recapture);
        this.mReCaptureTextView.setOnClickListener(this);
        this.mEditTextView = (TextView) view.findViewById(R.id.tv_edit);
        this.mEditTextView.setOnClickListener(this);
        this.mSaveTextView = (TextView) view.findViewById(R.id.tv_save);
        this.mSaveTextView.setOnClickListener(this);
        this.mBackTextView = (TextView) view.findViewById(R.id.tv_back);
        this.mBackTextView.setOnClickListener(this);
        this.mConfirmTextView = (TextView) view.findViewById(R.id.btn_confirm);
        this.mConfirmTextView.setOnClickListener(this);
        FolmeUtils.touchButtonTint(R.color.mimoji_btn_pressed_bg, this.mReCaptureTextView, this.mSaveTextView, this.mEditTextView);
        updateTitleState(1);
        this.mMimojiEditGLTextureView = (MimojiEditGLTextureView) view.findViewById(R.id.mimoji_edit_preview);
        this.mMimojiEditGLTextureView.setHandler(this.mHandler);
        this.mOperateSelectLayout = (LinearLayout) view.findViewById(R.id.operate_select_layout);
        this.mOperateSelectLayout.setVisibility(0);
        if (this.mMimojiTypeView == null) {
            this.mMimojiTypeView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.mimoij_type_item, null, false).findViewById(R.id.tv_type);
        }
        this.mMimojiTypeSelectView = (AutoSelectHorizontalView) view.findViewById(R.id.mimoji_type_view);
        this.mMimojiTypeSelectView.getItemAnimator().setChangeDuration(0);
        this.mLevelRecyleView = (RecyclerView) view.findViewById(R.id.color_level);
        this.mLevelRecyleView.setFocusable(false);
        if (this.mLevelRecyleView.getLayoutManager() == null) {
            LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(this.mContext, "color_level");
            linearLayoutManagerWrapper.setOrientation(1);
            this.mLevelRecyleView.setLayoutManager(linearLayoutManagerWrapper);
        }
        this.mEditLevelListAdapter = new EditLevelListAdapter(this.mContext, new ItfGvOnItemClickListener() {
            public void notifyUIChanged() {
                FragmentMimojiEdit.this.mEditState = true;
                FragmentMimojiEdit.this.updateTitleState(FragmentMimojiEdit.this.fromTag == 105 ? 3 : 5);
            }
        });
        this.mEditLevelListAdapter.setmClickCheck(this.mClickCheck);
        this.mLevelRecyleView.setAdapter(this.mEditLevelListAdapter);
        this.mMimojiPageChangeAnimManager = new MimojiPageChangeAnimManager();
        this.mMimojiPageChangeAnimManager.initView(this.mContext, this.mMimojiEditGLTextureView, this.mRlAllEditContent, 1);
    }

    private void resetData() {
        this.mHandler.removeMessages(6);
        this.mHandler.removeMessages(16);
        this.mAvatarEngineManager.resetData();
        this.mEditLevelListAdapter.setIsColorNeedNotify(true);
        this.mEditLevelListAdapter.setLevelDatas(AvatarEngineManager.getInstance().getSubConfigList(this.mContext, AvatarEngineManager.getInstance().getSelectType()));
        if (this.mRenderThread.getIsRendering()) {
            this.mRenderThread.setResetStopRender(true);
        } else {
            this.mRenderThread.draw(true);
        }
        this.mEditLevelListAdapter.notifyDataSetChanged();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("resetData   mEnterFromMimoji :");
        sb.append(this.mEnterFromMimoji);
        Log.i(str, sb.toString());
        this.mAvatar.loadConfig(this.mEnterFromMimoji ? this.mCurrentConfigPath : AvatarEngineManager.TempOriginalConfigPath);
    }

    /* access modifiers changed from: private */
    /* renamed from: setupAvatar */
    public void O00oOOoo() {
        Log.d(TAG, "setup avatar");
        this.mSetupCompleted = false;
        this.mAvatarEngineManager = AvatarEngineManager.getInstance();
        this.mAvatar = this.mAvatarEngineManager.queryAvatar();
        this.mAvatar.loadColorValue(AvatarEngineManager.PersonTemplatePath);
        if (!this.mEnterFromMimoji) {
            this.mAvatar.setTemplatePath(AvatarEngineManager.PersonTemplatePath);
        }
        ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
        this.mAvatar.getConfigValue(aSAvatarConfigValue);
        this.mAvatarEngineManager.setASAvatarConfigValue(aSAvatarConfigValue);
        this.mAvatarEngineManager.setASAvatarConfigValueDefault(aSAvatarConfigValue);
        this.mAvatar.setRenderScene(false, 0.85f);
        this.mRenderThread = new MimojiThumbnailRenderThread("MimojiEdit", 200, 200, this.mContext);
        this.mRenderThread.start();
        this.mRenderThread.waitUntilReady();
        this.mRenderThread.setUpdateHandler(this.mHandler);
        EditLevelListAdapter editLevelListAdapter = this.mEditLevelListAdapter;
        if (editLevelListAdapter != null) {
            editLevelListAdapter.setRenderThread(this.mRenderThread);
        }
        this.mAvatarEngineManager.initUpdatePara();
        this.mAvatar.saveConfig(AvatarEngineManager.TempOriginalConfigPath);
        this.mRenderThread.setClickCheck(this.mClickCheck);
        this.mSetupCompleted = true;
        this.mMimojiEditGLTextureView.setIsStopRenderForce(false);
        this.mMimojiEditGLTextureView.setStopRender(false);
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.onMimojiInitFinish();
        }
    }

    private void showAlertDialog(final int i) {
        if (this.mCurrentAlertDialog == null) {
            int i2 = (i == 1 || i == 2) ? R.string.mimoji_edit_cancel_alert : i != 3 ? (i == 4 || i == 5) ? R.string.mimoji_edit_abandon_alert : -1 : R.string.mimoji_edit_abandon_capture_alert;
            if (i2 != -1) {
                this.mCurrentAlertDialog = RotateDialogController.showSystemAlertDialog(getActivity(), getString(i2), null, getString(R.string.mimoji_confirm), new Runnable() {
                    public void run() {
                        String str;
                        int i = i;
                        boolean z = i == 2 || i == 1;
                        if (!z && FragmentMimojiEdit.this.mIsStartEdit) {
                            FragmentMimojiEdit.this.mAvatar.loadConfig(FragmentMimojiEdit.this.mEnterFromMimoji ? FragmentMimojiEdit.this.mCurrentConfigPath : AvatarEngineManager.TempOriginalConfigPath);
                        }
                        FragmentMimojiEdit.this.goBack(z, false);
                        int i2 = i;
                        String str2 = Mimoji.PREVIEW_MID;
                        if (i2 == 1) {
                            Log.u(FragmentMimojiEdit.TAG, "onClick mimoji_click_preview_mid_back");
                            str = Mimoji.MIMOJI_CLICK_PREVIEW_MID_BACK;
                        } else if (i2 == 2) {
                            Log.u(FragmentMimojiEdit.TAG, "onClick preview_mid_recapture");
                            str = Mimoji.MIMOJI_CLICK_PREVIEW_MID_RECAPTURE;
                        } else if (i2 == 3) {
                            Log.u(FragmentMimojiEdit.TAG, "onClick preview_mid_soft_back");
                            str = Mimoji.MIMOJI_CLICK_PREVIEW_MID_SOFT_BACK;
                        } else if (i2 == 4) {
                            Log.u(FragmentMimojiEdit.TAG, "onClick edit_soft_back");
                            str = Mimoji.MIMOJI_CLICK_EDIT_SOFT_BACK;
                            str2 = BaseEvent.EDIT;
                        } else if (i2 == 5) {
                            Log.u(FragmentMimojiEdit.TAG, "onClick edit_cancel");
                            str = Mimoji.MIMOJI_CLICK_EDIT_CANCEL;
                        } else {
                            return;
                        }
                        CameraStatUtils.trackMimojiClick(str, str2);
                    }
                }, null, null, getString(R.string.mimoji_cancle), C0311O0000OoO.INSTANCE);
                this.mCurrentAlertDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        FragmentMimojiEdit.this.mCurrentAlertDialog = null;
                    }
                });
            }
        }
    }

    public /* synthetic */ void O000000o(MimojiTypeBean mimojiTypeBean, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSelectListener position=");
        sb.append(i);
        sb.append(", text=");
        sb.append(mimojiTypeBean.getText());
        Log.u(str, sb.toString());
        this.mMimojiPageChangeAnimManager.updateLayoutPosition();
        EditLevelListAdapter editLevelListAdapter = this.mEditLevelListAdapter;
        if (editLevelListAdapter != null) {
            editLevelListAdapter.setIsColorNeedNotify(true);
        }
        ASAvatarConfigType aSAvatarConfigType = mimojiTypeBean.getASAvatarConfigType();
        MimojiEditor mimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
        if (!(mimojiEditor == null || aSAvatarConfigType == null)) {
            mimojiEditor.onTypeConfigSelect(aSAvatarConfigType.configType);
        }
        this.mLevelRecyleView.scrollToPosition(0);
    }

    public /* synthetic */ void O000oooO() {
        this.mEditState = true;
        if (this.fromTag == 105) {
            updateTitleState(3);
            this.mMimojiPageChangeAnimManager.resetLayoutPosition(4);
            return;
        }
        updateTitleState(5);
    }

    public /* synthetic */ void O000oooo() {
        startMimojiEdit(true, 105);
        this.mAvatar.loadConfig(this.mIsStartEdit ? AvatarEngineManager.TempEditConfigPath : AvatarEngineManager.TempOriginalConfigPath);
        this.mAvatar.setRenderScene(false, 0.85f);
        AutoSelectAdapter autoSelectAdapter = this.mMimojiTypeAdapter;
        if (autoSelectAdapter == null || autoSelectAdapter.getItemCount() <= 0) {
            initConfigList();
        }
        this.mMimojiEditGLTextureView.setIsStopRenderForce(false);
        this.mMimojiEditGLTextureView.setStopRender(false);
    }

    public /* synthetic */ void O00O000o() {
        if (isAdded()) {
            this.mSaveTextView.sendAccessibilityEvent(128);
        }
    }

    public void directlyEnterEditMode(MimojiInfo mimojiInfo, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configPath = ");
        sb.append(this.mCurrentConfigPath);
        Log.d(str, sb.toString());
        this.mPopSaveDeletePath = mimojiInfo.mPackPath;
        this.mCurrentConfigPath = mimojiInfo.mConfigPath;
        this.mEnterFromMimoji = true;
        this.mIsStartEdit = true;
        DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_EIDT);
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.forceSwitchFront();
        }
        startMimojiEdit(false, i);
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.setDisableSingleTapUp(true);
        }
        ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).disableMenuItem(true, 197, 193);
        if (101 == i) {
            updateTitleState(4);
        } else {
            updateTitleState(2);
        }
        this.mOperateSelectLayout.setVisibility(8);
        initConfigList();
    }

    public int getFragmentInto() {
        return 65521;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_full_screen_mimoji;
    }

    public void goBack(boolean z, boolean z2) {
        AvatarEngineManager.getInstance().clear();
        if (this.mMimojiEditGLTextureView != null) {
            releaseRender();
        }
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.backToPreview(z2, !z);
            if (z) {
                mimojiAvatarEngine.onMimojiCreate();
            }
        }
        if (z2) {
            MimojiAlert mimojiAlert = (MimojiAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(226);
            if (mimojiAlert != null) {
                CameraStatUtils.trackMimojiCount(Integer.toString(mimojiAlert.refreshMimojiList()));
            }
        }
        this.mEnterFromMimoji = false;
        this.mIsStartEdit = false;
        View view = this.mMimojiEditViewLayout;
        if (view != null) {
            view.setVisibility(8);
            this.mOperateSelectLayout.setVisibility(0);
            updateTitleState(1);
        }
        this.mMimojiEditGLTextureView.setVisibility(8);
        this.mRenderThread.quit();
        this.mSetupThread = null;
        FragmentUtils.removeFragmentByTag(getFragmentManager(), TAG);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mContext = getActivity();
        this.mMimojiEditViewStub = (ViewStub) view.findViewById(R.id.mimoji_edit);
        this.mClickCheck = new ClickCheck();
    }

    public boolean onBackEvent(int i) {
        int i2;
        if (i == 1 && !this.mIsSaveBtnClicked) {
            if (this.mIsStartEdit) {
                i2 = 4;
            } else {
                View view = this.mMimojiEditViewLayout;
                if (!(view == null || view.getVisibility() == 8)) {
                    i2 = 3;
                }
            }
            showAlertDialog(i2);
            return true;
        }
        return false;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        String str;
        if (this.mSetupCompleted) {
            int id = view.getId();
            String str2 = Mimoji.PREVIEW_MID;
            switch (id) {
                case R.id.btn_confirm /*2131296394*/:
                case R.id.tv_save /*2131297138*/:
                    Log.u(TAG, "onClick: tv_save");
                    if (!this.mIsSaveBtnClicked) {
                        this.mIsSaveBtnClicked = true;
                        this.mMimojiEditGLTextureView.setSaveConfigThum(true);
                        if (this.mIsStartEdit) {
                            ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
                            this.mAvatar.getConfigValue(aSAvatarConfigValue);
                            Map mimojiConfigValue = AvatarEngineManager.getMimojiConfigValue(aSAvatarConfigValue);
                            mimojiConfigValue.put(BaseEvent.EVENT_NAME, "click");
                            boolean z = this.mEnterFromMimoji;
                            String str3 = Mimoji.MIMOJI_CLICK_EDIT_SAVE;
                            mimojiConfigValue.put(Mimoji.PARAM_MIMOJI_EDIT_COUNT, z ? "second" : "first");
                            CameraStatUtils.trackMimojiSavePara(str3, mimojiConfigValue);
                            break;
                        } else {
                            str = Mimoji.MIMOJI_CLICK_PREVIEW_MID_SAVE;
                        }
                    }
                    break;
                case R.id.tv_back /*2131297120*/:
                    Log.u(TAG, "onClick: tv_back");
                    if (!this.mIsSaveBtnClicked) {
                        int i = this.fromTag;
                        if (i != 101) {
                            if (i == 105 && this.mCurrentTopPannelState == 1) {
                                showAlertDialog(1);
                                break;
                            } else if (this.mEditState) {
                                ClickCheck clickCheck = this.mClickCheck;
                                if (clickCheck == null || clickCheck.checkClickable()) {
                                    this.mEditState = false;
                                    updateTitleState(2);
                                    resetData();
                                    CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_EDIT_RESET, BaseEvent.EDIT);
                                    break;
                                } else {
                                    return;
                                }
                            }
                        } else {
                            showAlertDialog(5);
                            break;
                        }
                    }
                    break;
                case R.id.tv_edit /*2131297131*/:
                    Log.u(TAG, "onClick: tv_edit");
                    updateTitleState(2);
                    this.mOperateSelectLayout.setVisibility(8);
                    this.mRlAllEditContent.setVisibility(0);
                    initConfigList();
                    this.mMimojiPageChangeAnimManager.updateOperateState(2);
                    DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_EIDT);
                    this.mIsStartEdit = true;
                    str = Mimoji.MIMOJI_CLICK_PREVIEW_MID_EDIT;
                    CameraStatUtils.trackMimojiClick(str, str2);
                    break;
                case R.id.tv_recapture /*2131297135*/:
                    Log.u(TAG, "onClick: tv_recapture");
                    if (!this.mIsSaveBtnClicked) {
                        showAlertDialog(2);
                        break;
                    }
                    break;
            }
            CameraStatUtils.trackMimojiClick(str, str2);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDeviceRotationChange(int i) {
        MimojiEditGLTextureView mimojiEditGLTextureView = this.mMimojiEditGLTextureView;
        if (mimojiEditGLTextureView != null) {
            mimojiEditGLTextureView.onDeviceRotationChange(i);
        }
    }

    public void onResume() {
        this.mClickCheck.setForceDisabled(true);
        super.onResume();
    }

    public void onStart() {
        super.onStart();
        if (this.mBackTextView != null && !this.mIsStartEdit) {
            this.mEditState = false;
            updateTitleState(1);
        }
    }

    public void onStop() {
        this.mClickCheck.setForceDisabled(true);
        super.onStop();
    }

    public void onTypeConfigSelect(int i) {
        this.mAvatarEngineManager.setIsColorSelected(false);
        this.mAvatarEngineManager.setSelectType(i);
        if (!this.mRenderThread.getIsRendering()) {
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 6;
            this.mHandler.sendMessage(obtainMessage);
            return;
        }
        this.mRenderThread.setStopRender(true);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("provideAnimateElement, animateInElements");
        sb.append(list);
        sb.append("resetType = ");
        sb.append(i2);
        Log.d(str, sb.toString());
        View view = this.mMimojiEditViewLayout;
        if (view != null && view.getVisibility() == 0 && i2 == 3) {
            Log.d(TAG, "mimoji edit timeout");
            goBack(false, false);
            DataRepository.dataItemLive().getMimojiStatusManager().reset();
            AlertDialog alertDialog = this.mCurrentAlertDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mCurrentAlertDialog = null;
            }
            ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).getAnimationComposite().remove(getFragmentInto());
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(224, this);
    }

    public void releaseRender() {
        this.mMimojiEditGLTextureView.setIsStopRenderForce(true);
        this.mMimojiEditGLTextureView.queueEvent(new Runnable() {
            public void run() {
                if (FragmentMimojiEdit.this.mAvatar != null) {
                    Log.d(FragmentMimojiEdit.TAG, "avatar releaseRender 2");
                    FragmentMimojiEdit.this.mAvatar.releaseRender();
                }
            }
        });
    }

    public void requestRender(boolean z) {
        MimojiEditGLTextureView mimojiEditGLTextureView = this.mMimojiEditGLTextureView;
        if (mimojiEditGLTextureView != null) {
            mimojiEditGLTextureView.setStopRender(z);
            this.mMimojiEditGLTextureView.requestRender();
        }
    }

    public void resetClickEnable(boolean z) {
        ClickCheck clickCheck = this.mClickCheck;
        if (clickCheck != null) {
            clickCheck.setForceDisabled(!z);
        }
    }

    public void resetConfig() {
        this.mAvatarEngineManager = AvatarEngineManager.getInstance();
        this.mAvatar = this.mAvatarEngineManager.queryAvatar();
        this.mMimojiEditGLTextureView.setStopRender(true);
        if (this.mMimojiEditGLTextureView == null) {
            this.mHandler.post(new C0312O0000Ooo(this));
            return;
        }
        this.mAvatar.loadConfig(this.mIsStartEdit ? AvatarEngineManager.TempEditConfigPath : AvatarEngineManager.TempOriginalConfigPath);
        this.mAvatar.setRenderScene(false, 0.85f);
        this.mMimojiEditGLTextureView.setIsStopRenderForce(false);
        this.mMimojiEditGLTextureView.setStopRender(false);
    }

    public void startMimojiEdit(boolean z, final int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startMimojiEdit：");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mSetupCompleted = false;
        if (this.mMimojiEditViewLayout == null) {
            this.mMimojiEditViewLayout = this.mMimojiEditViewStub.inflate();
            initMimojiEdit(this.mMimojiEditViewLayout);
        }
        RecyclerView recyclerView = this.mLevelRecyleView;
        if (recyclerView != null) {
            LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
            if (layoutParams != null) {
                if (!Util.isFullScreenNavBarHidden(getContext())) {
                    layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.mimoji_edit_config_bottom);
                } else {
                    layoutParams.bottomMargin = 0;
                }
            }
        }
        this.mIsSaveBtnClicked = false;
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).getAnimationComposite().put(getFragmentInto(), this);
        this.mMimojiEditViewLayout.setVisibility(0);
        this.mMimojiEditGLTextureView.setStopRender(true);
        this.mMimojiEditGLTextureView.setVisibility(4);
        this.fromTag = i;
        this.mMimojiEditViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                FragmentMimojiEdit.this.mMimojiEditViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FragmentMimojiEdit.this.mMimojiEditGLTextureView.setVisibility(0);
                FragmentMimojiEdit.this.mMimojiPageChangeAnimManager.resetLayoutPosition(i == 101 ? 4 : 1);
            }
        });
        if (z) {
            this.mSetupThread = new Thread(new O0000O0o(this));
            this.mSetupThread.start();
        } else {
            O00oOOoo();
        }
        if (Util.isAccessible()) {
            this.mSaveTextView.postDelayed(new O0000Oo(this), 100);
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(224, this);
        DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_NONE);
        this.mIsStartEdit = false;
    }

    public void updateTitleState(int i) {
        if (this.mCurrentTopPannelState != i) {
            if (i == 1) {
                this.mCurrentTopPannelState = 1;
                this.mBackTextView.setText(getResources().getString(R.string.mimoji_back));
                this.mBackTextView.setTextColor(getResources().getColor(R.color.white));
                this.mBackTextView.setClickable(true);
                this.mBackTextView.setVisibility(0);
                this.mConfirmTextView.setVisibility(8);
                LinearLayout linearLayout = this.mRlAllEditContent;
                if (linearLayout != null && !this.mIsStartEdit) {
                    linearLayout.setVisibility(8);
                }
            } else if (i == 2) {
                this.mCurrentTopPannelState = 2;
                this.mRlAllEditContent.setVisibility(0);
                this.mBackTextView.setVisibility(0);
                this.mConfirmTextView.setVisibility(0);
                this.mBackTextView.setTextColor(getResources().getColor(R.color.white_alpha_4d));
                this.mBackTextView.setClickable(false);
                this.mConfirmTextView.setText(getResources().getString(R.string.mimoji_save));
                this.mBackTextView.setText(getResources().getString(R.string.mimoji_reset));
                this.mConfirmTextView.setClickable(true);
                this.mConfirmTextView.setTextColor(getResources().getColor(R.color.white));
            } else if (i == 3) {
                this.mCurrentTopPannelState = 3;
                this.mRlAllEditContent.setVisibility(0);
                this.mBackTextView.setVisibility(0);
                this.mConfirmTextView.setVisibility(0);
                this.mBackTextView.setTextColor(getResources().getColor(R.color.white));
                this.mConfirmTextView.setTextColor(getResources().getColor(R.color.white));
                this.mConfirmTextView.setClickable(true);
                this.mBackTextView.setClickable(true);
                this.mConfirmTextView.setClickable(true);
                this.mConfirmTextView.setText(getResources().getString(R.string.mimoji_save));
                this.mBackTextView.setText(getResources().getString(R.string.mimoji_reset));
            } else if (i == 4) {
                this.mCurrentTopPannelState = 4;
                this.mRlAllEditContent.setVisibility(0);
                this.mBackTextView.setVisibility(0);
                this.mConfirmTextView.setVisibility(0);
                this.mBackTextView.setTextColor(getResources().getColor(R.color.white));
                this.mBackTextView.setClickable(true);
                this.mBackTextView.setText(getResources().getString(R.string.mimoji_cancle));
                this.mConfirmTextView.setText(getResources().getString(R.string.mimoji_save));
                this.mConfirmTextView.setTextColor(getResources().getColor(R.color.white_alpha_4d));
                this.mConfirmTextView.setClickable(false);
            } else if (i == 5) {
                this.mConfirmTextView.setTextColor(getResources().getColor(R.color.white));
                this.mConfirmTextView.setClickable(true);
            }
        }
    }
}
