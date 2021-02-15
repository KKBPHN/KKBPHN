package com.android.camera.fragment.idcard;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.data.observeable.VMFeature.FeatureModule;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.vv.FragmentVVFeature;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.IDCardModeProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.storage.Storage;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FragmentIDCard extends BaseFragment implements IDCardModeProtocol, HandleBackTrace {
    private String mCurrentPictureName = Storage.ID_CARD_PICTURE_1;
    private ViewGroup mRoot;
    private VMFeature mVMFeature;

    private void changeIDCardView(boolean z) {
        IDCardView iDCardView = new IDCardView(getActivity(), z);
        this.mRoot.removeAllViews();
        this.mRoot.addView(iDCardView);
        if (Util.isAccessible()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertDocumentTip(z ? R.string.id_card_mode_hint_front : R.string.id_card_mode_hint_back);
            }
        }
        setCurrentPictureName(z ? Storage.ID_CARD_PICTURE_1 : Storage.ID_CARD_PICTURE_2);
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                if (((String) entry.getKey()).equals(FeatureModule.MODULE_DOC2)) {
                    int intValue = ((Integer) entry.getValue()).intValue();
                    if (VMFeature.getScope(intValue) == 16) {
                        if (!(intValue == 17 || intValue == 19 || intValue == 21)) {
                            if (intValue == 22) {
                                onInstalled();
                            }
                        }
                    }
                }
            }
        }
    }

    private void onInstalled() {
    }

    private void setCurrentPictureName(String str) {
        this.mCurrentPictureName = str;
    }

    public /* synthetic */ void O00000oO(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public void callBackEvent() {
        if (getCurrentPictureName().equals(Storage.ID_CARD_PICTURE_2)) {
            changeIDCardView(true);
            return;
        }
        ((ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179)).changeModeByNewMode(DataRepository.dataItemRunning().getEntranceMode(186), getString(R.string.pref_document_mode), 0);
    }

    public String getCurrentPictureName() {
        return this.mCurrentPictureName;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_ID_CARD;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_id_card;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRoot = (ViewGroup) view.findViewById(R.id.id_card_layout);
        if (C0122O00000o.instance().OO0ooo() && C0122O00000o.instance().OOO00()) {
            MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
            String str = FeatureModule.MODULE_DOC2;
            if (!multiFeatureManager.hasFeatureInstalled(str)) {
                FragmentVVFeature fragmentVVFeature = new FragmentVVFeature();
                fragmentVVFeature.setFeatureName(str);
                fragmentVVFeature.setFixedMargin(Display.getBottomHeight());
                if (this.mVMFeature == null) {
                    this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
                    this.mVMFeature.startObservable(this, new O000000o(this));
                }
                FragmentUtils.addFragmentWithTag(getChildFragmentManager(), (int) R.id.id_card_feature_holder, (Fragment) fragmentVVFeature, FragmentVVFeature.TAG);
                return;
            }
            onInstalled();
        }
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentMode == 182) {
            this.mRoot.setVisibility(0);
            changeIDCardView(true);
        }
    }

    public boolean onBackEvent(int i) {
        if (i != 1 || this.mCurrentMode != 182) {
            return false;
        }
        callBackEvent();
        return true;
    }

    public void onDestroyView() {
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.removeBackStack(this);
        }
        super.onDestroyView();
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.addInBackStack(this);
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 182) {
            this.mRoot.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        ModeCoordinatorImpl.getInstance().attachProtocol(233, this);
    }

    public void switchNextPage() {
        if (Storage.isIdCardPictureOne(getCurrentPictureName())) {
            changeIDCardView(false);
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        ModeCoordinatorImpl.getInstance().detachProtocol(233, this);
    }
}
