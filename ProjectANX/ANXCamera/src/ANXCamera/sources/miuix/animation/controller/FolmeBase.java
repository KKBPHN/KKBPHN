package miuix.animation.controller;

import miuix.animation.IAnimTarget;
import miuix.animation.IStateContainer;
import miuix.animation.base.AnimConfig;
import miuix.animation.property.FloatProperty;

public abstract class FolmeBase implements IStateContainer {
    IFolmeStateStyle mState;

    FolmeBase(IAnimTarget... iAnimTargetArr) {
        this.mState = FolmeState.composeStyle(iAnimTargetArr);
    }

    public void addConfig(Object obj, AnimConfig... animConfigArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.addConfig(obj, animConfigArr);
        }
    }

    /* access modifiers changed from: 0000 */
    public void alignState(Object obj, Object obj2) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            AnimState.alignState(iFolmeStateStyle.getTarget(), this.mState.getState(obj), this.mState.getState(obj2));
        }
    }

    public void cancel() {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.cancel();
        }
    }

    public void cancel(String... strArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.cancel(strArr);
        }
    }

    public void cancel(FloatProperty... floatPropertyArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.cancel(floatPropertyArr);
        }
    }

    public void clean() {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.clean();
        }
    }

    public void enableDefaultAnim(boolean z) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.enableDefaultAnim(z);
        }
    }

    public void end(Object... objArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.end(objArr);
        }
    }

    /* access modifiers changed from: 0000 */
    public FloatProperty getProperty(int i) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            return iFolmeStateStyle.getTarget().getProperty(i);
        }
        return null;
    }
}
