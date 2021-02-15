package miui.animation.controller;

import miui.animation.IAnimTarget;
import miui.animation.IVisibleStyle;
import miui.animation.IVisibleStyle.VisibleType;
import miui.animation.base.AnimConfig;
import miui.animation.property.FloatProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.EaseManager;
import miui.animation.utils.EaseManager.EaseStyle;

public class FolmeVisible extends FolmeBase implements IVisibleStyle {
    private AnimConfig mDefConfig = new AnimConfig();
    private boolean mHasMove;
    private boolean mHasScale;

    public FolmeVisible(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        useAutoAlpha(true);
    }

    private AnimConfig[] getConfig(VisibleType visibleType, AnimConfig... animConfigArr) {
        AnimConfig animConfig;
        float[] fArr;
        EaseStyle easeStyle;
        if (!this.mHasScale && !this.mHasMove) {
            animConfig = this.mDefConfig;
            if (visibleType == VisibleType.SHOW) {
                easeStyle = EaseManager.getStyle(16, 300.0f);
                animConfig.ease = easeStyle;
                return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDefConfig);
            }
            fArr = new float[]{1.0f, 0.15f};
        } else if (this.mHasScale && !this.mHasMove) {
            animConfig = this.mDefConfig;
            fArr = visibleType == VisibleType.SHOW ? new float[]{0.6f, 0.35f} : new float[]{0.75f, 0.2f};
        } else if (!this.mHasScale) {
            animConfig = this.mDefConfig;
            fArr = visibleType == VisibleType.SHOW ? new float[]{0.75f, 0.35f} : new float[]{0.75f, 0.25f};
        } else {
            animConfig = this.mDefConfig;
            fArr = visibleType == VisibleType.SHOW ? new float[]{0.65f, 0.35f} : new float[]{0.75f, 0.25f};
        }
        easeStyle = EaseManager.getStyle(-2, fArr);
        animConfig.ease = easeStyle;
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDefConfig);
    }

    private VisibleType getType(VisibleType... visibleTypeArr) {
        return visibleTypeArr.length > 0 ? visibleTypeArr[0] : VisibleType.HIDE;
    }

    public void clean() {
        super.clean();
        this.mHasScale = false;
        this.mHasMove = false;
    }

    public void hide(AnimConfig... animConfigArr) {
        alignState(VisibleType.SHOW, VisibleType.HIDE);
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        VisibleType visibleType = VisibleType.HIDE;
        iFolmeStateStyle.to(visibleType, getConfig(visibleType, animConfigArr));
    }

    public IVisibleStyle setAlpha(float f, VisibleType... visibleTypeArr) {
        this.mState.getState(getType(visibleTypeArr)).add(getProperty(14), f, new long[0]);
        return this;
    }

    public IVisibleStyle setBound(int i, int i2, int i3, int i4) {
        this.mState.getState(VisibleType.SHOW).add(getProperty(0), i, new long[0]).add(getProperty(1), i2, new long[0]).add(getProperty(6), i3, new long[0]).add(getProperty(5), i4, new long[0]);
        return this;
    }

    public IVisibleStyle setHide() {
        alignState(VisibleType.SHOW, VisibleType.HIDE);
        this.mState.setTo((Object) VisibleType.HIDE);
        return this;
    }

    public IVisibleStyle setMove(int i, int i2) {
        return setMove(i, i2, VisibleType.HIDE);
    }

    public IVisibleStyle setMove(int i, int i2, VisibleType... visibleTypeArr) {
        boolean z = Math.abs(i) > 0 || Math.abs(i2) > 0;
        this.mHasMove = z;
        if (this.mHasMove) {
            this.mState.getState(getType(visibleTypeArr)).add(getProperty(0), i, 1).add(getProperty(1), i2, 1);
        }
        return this;
    }

    public IVisibleStyle setScale(float f, VisibleType... visibleTypeArr) {
        this.mHasScale = true;
        this.mState.getState(getType(visibleTypeArr)).add(getProperty(3), f, new long[0]).add(getProperty(2), f, new long[0]);
        return this;
    }

    public IVisibleStyle setShow() {
        alignState(VisibleType.HIDE, VisibleType.SHOW);
        this.mState.setTo((Object) VisibleType.SHOW);
        return this;
    }

    public IVisibleStyle setShowDelay(long j) {
        this.mState.getState(VisibleType.SHOW).getGlobalConfig().delay = j;
        return this;
    }

    public void show(AnimConfig... animConfigArr) {
        alignState(VisibleType.HIDE, VisibleType.SHOW);
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        VisibleType visibleType = VisibleType.SHOW;
        iFolmeStateStyle.to(visibleType, getConfig(visibleType, animConfigArr));
    }

    public IVisibleStyle useAutoAlpha(boolean z) {
        FloatProperty property = getProperty(14);
        FloatProperty property2 = getProperty(4);
        if (z) {
            this.mState.getState(VisibleType.SHOW).remove(property2).add(property, 1.0f, new long[0]);
            this.mState.getState(VisibleType.HIDE).remove(property2).add(property, 0.0f, new long[0]);
        } else {
            this.mState.getState(VisibleType.SHOW).remove(property).add(property2, 1.0f, new long[0]);
            this.mState.getState(VisibleType.HIDE).remove(property).add(property2, 0.0f, new long[0]);
        }
        return this;
    }
}
