package com.android.camera.fragment.beauty;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.ui.SeekBarCompat;
import com.android.camera.ui.SeekBarCompat.OnSeekBarCompatChangeListener;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

@Deprecated
public class FragmentPopupMakeup extends BaseFragment implements MakeupProtocol, Consumer {
    public static final int FRAGMENT_INFO = 252;
    private static final int INTERVAL = 5;
    /* access modifiers changed from: private */
    public static int SEEKBAR_PROGRESS_MAX = 100;
    private static final int SEEKBAR_PROGRESS_RATIO = 1;
    /* access modifiers changed from: private */
    public int mActiveProgress;
    private Disposable mDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mFlowableEmitter;
    private boolean mIsRTL;
    private boolean mIsShow;
    private View mRootView;
    /* access modifiers changed from: private */
    public SeekBarCompat mSeekBar;
    private BeautyModeSettingBusiness modeSettingBusiness;

    private int centerSeekbarValueMap(int i) {
        return i + (SEEKBAR_PROGRESS_MAX / 2);
    }

    public void accept(@NonNull Integer num) {
    }

    public int getFragmentInto() {
        return 252;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_popup_makeup;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mRootView = view;
        ((MarginLayoutParams) view.getLayoutParams()).bottomMargin = Display.getBottomHeight() + getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height);
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                FragmentPopupMakeup.this.mFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).observeOn(Schedulers.computation()).onBackpressureDrop(new Consumer() {
            public void accept(@NonNull Integer num) {
                StringBuilder sb = new StringBuilder();
                sb.append("seekbar change too fast :");
                sb.append(num.toString());
                Log.e(Log.VIEW_TAG, sb.toString());
            }
        }).subscribe((Consumer) this);
        this.modeSettingBusiness = new BeautyModeSettingBusiness();
        this.mSeekBar = (SeekBarCompat) view.findViewById(R.id.makeup_params_level);
        this.mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
        this.mSeekBar.setOnSeekBarChangeListener(new OnSeekBarCompatChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null && z) {
                    topAlert.alertUpdateValue(2, 0, String.valueOf((FragmentPopupMakeup.this.mSeekBar.isCenterTwoWayMode() ? i / 2 : i) / 1));
                }
                if (i == 0 || i == FragmentPopupMakeup.SEEKBAR_PROGRESS_MAX || Math.abs(i - FragmentPopupMakeup.this.mActiveProgress) > 5) {
                    FragmentPopupMakeup.this.mActiveProgress = i;
                    FragmentPopupMakeup.this.mFlowableEmitter.onNext(Integer.valueOf(i / 1));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public boolean isSeekBarVisible() {
        return false;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mCurrentMode == 163) {
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                boolean z = miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow();
                cameraModuleSpecial.showOrHideChip(z);
            }
        }
    }

    public void onMakeupItemSelected(String str, @StringRes int i, boolean z) {
        BeautyConstant.isSupportTwoWayAdjustable(str);
        this.mRootView.setVisibility(str == "pref_eye_light_type_key" ? 8 : 0);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mCurrentMode == 163) {
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                cameraModuleSpecial.showOrHideChip(false);
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        CompletableOnSubscribe completableOnSubscribe;
        super.provideAnimateElement(i, list, i2);
        int i3 = (i == 163 || i == 165) ? 1 : -1;
        if (getView().getTag() == null || ((Integer) getView().getTag()).intValue() != i3) {
            getView().setTag(Integer.valueOf(i3));
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
                i3 = -1;
            }
            if (i3 == 1) {
                if (getView().getVisibility() != 0) {
                    getView().setVisibility(0);
                }
                if (list == null) {
                    SlideInOnSubscribe.directSetResult(this.mSeekBar, 80);
                }
                completableOnSubscribe = new SlideInOnSubscribe(this.mSeekBar, 80);
            } else if (list == null) {
                SlideOutOnSubscribe.directSetResult(this.mSeekBar, 80);
            } else {
                completableOnSubscribe = new SlideOutOnSubscribe(this.mSeekBar, 80);
            }
            list.add(Completable.create(completableOnSubscribe));
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(161);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(162);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(180, this);
        this.mIsShow = true;
    }

    public void setSeekBarMode(int i) {
        boolean z = false;
        if (i == 1) {
            SEEKBAR_PROGRESS_MAX = 100;
            this.mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
        } else if (i == 2) {
            SEEKBAR_PROGRESS_MAX = 200;
            this.mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.center_seekbar_style));
            z = true;
        }
        this.mSeekBar.setMax(SEEKBAR_PROGRESS_MAX);
        this.mSeekBar.setCenterTwoWayMode(z);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(180, this);
        Disposable disposable = this.mDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDisposable.dispose();
        }
        this.mIsShow = false;
    }
}
