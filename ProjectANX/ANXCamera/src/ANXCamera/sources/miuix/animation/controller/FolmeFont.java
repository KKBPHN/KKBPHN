package miuix.animation.controller;

import android.widget.TextView;
import miuix.animation.Folme;
import miuix.animation.IAnimTarget;
import miuix.animation.IVarFontStyle;
import miuix.animation.ViewTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.font.FontWeightProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.CommonUtils;

public class FolmeFont extends FolmeBase implements IVarFontStyle {
    private AnimConfig mDefaultTo = new AnimConfig();
    private int mInitValue;
    private boolean mIsInitSet;
    private FontWeightProperty mProperty;

    public enum FontType {
        INIT,
        TARGET
    }

    public FolmeFont() {
        super(new IAnimTarget[0]);
    }

    public void clean() {
        super.clean();
        this.mState = null;
        this.mProperty = null;
        this.mInitValue = 0;
    }

    public IVarFontStyle fromTo(int i, int i2, AnimConfig... animConfigArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.getState(FontType.INIT).add((ViewProperty) this.mProperty, i, new long[0]);
            this.mState.getState(FontType.TARGET).add((ViewProperty) this.mProperty, i2, new long[0]);
            this.mState.fromTo(FontType.INIT, FontType.TARGET, animConfigArr);
        }
        return this;
    }

    public IVarFontStyle setTo(int i) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.getState(FontType.TARGET).add((ViewProperty) this.mProperty, i, new long[0]);
            this.mState.setTo((Object) FontType.TARGET);
        }
        return this;
    }

    public IVarFontStyle to(int i, AnimConfig... animConfigArr) {
        IFolmeStateStyle iFolmeStateStyle;
        FontType fontType;
        IFolmeStateStyle iFolmeStateStyle2 = this.mState;
        if (iFolmeStateStyle2 != null) {
            if (!this.mIsInitSet) {
                this.mIsInitSet = true;
                iFolmeStateStyle2.setTo((Object) FontType.INIT);
            }
            AnimConfig[] animConfigArr2 = (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDefaultTo);
            if (this.mInitValue == i) {
                iFolmeStateStyle = this.mState;
                fontType = FontType.INIT;
            } else {
                this.mState.getState(FontType.TARGET).add((ViewProperty) this.mProperty, i, new long[0]);
                iFolmeStateStyle = this.mState;
                fontType = FontType.TARGET;
            }
            iFolmeStateStyle.to(fontType, animConfigArr2);
        }
        return this;
    }

    public IVarFontStyle useAt(TextView textView, int i, int i2) {
        this.mState = new FolmeState(Folme.getTarget(textView, ViewTarget.sCreator));
        this.mProperty = new FontWeightProperty(textView, i);
        this.mInitValue = i2;
        this.mState.getState(FontType.INIT).add((ViewProperty) this.mProperty, i2, new long[0]);
        this.mIsInitSet = false;
        return this;
    }
}
