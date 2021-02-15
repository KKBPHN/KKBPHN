package miuix.animation.controller;

import android.util.ArrayMap;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import miuix.animation.Folme;
import miuix.animation.IAnimTarget;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.internal.AnimRunner;
import miuix.animation.internal.AnimTask;
import miuix.animation.internal.AnimValueUtils;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.EaseManager.EaseStyle;
import miuix.animation.utils.LogUtils;
import miuix.animation.utils.StyleComposer;
import miuix.animation.utils.StyleComposer.IInterceptor;

public class FolmeState implements IFolmeStateStyle {
    private static final IntValueProperty DEFAULT_INT_PROPERTY = new IntValueProperty("defaultIntProperty");
    private static final ValueProperty DEFAULT_PROPERTY = new ValueProperty("defaultProperty");
    private static final String METHOD_GET_STATE = "getState";
    private static final String TAG_AUTO_SET_TO = "autoSetTo";
    private static final String TAG_PREDICT_FROM = "predictFrom";
    private static final String TAG_PREDICT_TO = "predictTo";
    private static final String TAG_SET_TO = "defaultSetTo";
    private static final String TAG_TO = "defaultTo";
    private static final String TARGET_PREDICT = "predictTarget";
    private static final IInterceptor sInterceptor = new IInterceptor() {
        public Object onMethod(Method method, Object[] objArr, IFolmeStateStyle[] iFolmeStateStyleArr) {
            if (iFolmeStateStyleArr.length <= 0 || objArr.length <= 0) {
                return null;
            }
            AnimState state = iFolmeStateStyleArr[0].getState(objArr[0]);
            for (int i = 1; i < iFolmeStateStyleArr.length; i++) {
                iFolmeStateStyleArr[i].addState(state);
            }
            return state;
        }

        public boolean shouldIntercept(Method method, Object[] objArr) {
            return method.getName().equals(FolmeState.METHOD_GET_STATE);
        }
    };
    private AnimState mAutoSetToState = new AnimState(TAG_AUTO_SET_TO);
    private Object mCurTag = this.mToState;
    private List mDelList = new ArrayList();
    private boolean mEnableAnim = true;
    private AnimState mPredictFrom;
    private IAnimTarget mPredictTarget;
    private AnimState mPredictTo;
    private AnimState mSetToState = new AnimState(TAG_SET_TO);
    private Map mStateMap = new ArrayMap();
    /* access modifiers changed from: private */
    public IAnimTarget mTarget;
    private AnimState mToState = new AnimState(TAG_TO);

    FolmeState(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    private boolean addConfigToLink(AnimConfigLink animConfigLink, Object obj) {
        if (obj instanceof AnimConfig) {
            animConfigLink.add((AnimConfig) obj);
            return true;
        }
        if (obj instanceof AnimConfigLink) {
            animConfigLink.add((AnimConfigLink) obj);
        }
        return false;
    }

    private int addProperty(AnimState animState, FloatProperty floatProperty, int i, Object... objArr) {
        if (floatProperty != null) {
            Object propertyValue = getPropertyValue(i, objArr);
            if (propertyValue != null && addPropertyValue(animState, floatProperty, propertyValue)) {
                return setInitVelocity(floatProperty, i + 1, objArr) ? 2 : 1;
            }
        }
        return 0;
    }

    private void addProperty(Object obj, FloatProperty floatProperty, Object obj2, long... jArr) {
        AnimState state = getState(obj);
        if (floatProperty instanceof IIntValueProperty) {
            state.add(floatProperty, CommonUtils.toIntValue(obj2), jArr);
        } else {
            state.add(floatProperty, CommonUtils.toFloatValue(obj2), jArr);
        }
    }

    private boolean addPropertyValue(AnimState animState, FloatProperty floatProperty, Object obj) {
        boolean z = obj instanceof Integer;
        if (!z && !(obj instanceof Float) && !(obj instanceof Double)) {
            return false;
        }
        if (floatProperty instanceof IIntValueProperty) {
            animState.add(floatProperty, toInt(obj, z), new long[0]);
        } else {
            animState.add(floatProperty, toFloat(obj, z), new long[0]);
        }
        return true;
    }

    private boolean checkAndSetAnimConfig(AnimConfigLink animConfigLink, Object obj) {
        if ((obj instanceof TransitionListener) || (obj instanceof EaseStyle)) {
            setTempConfig(animConfigLink.getHead(), obj);
            return true;
        } else if (!obj.getClass().isArray()) {
            return addConfigToLink(animConfigLink, obj);
        } else {
            int length = Array.getLength(obj);
            boolean z = false;
            for (int i = 0; i < length; i++) {
                z = addConfigToLink(animConfigLink, Array.get(obj, i)) || z;
            }
            return z;
        }
    }

    private void clearDefaultState(AnimState animState) {
        if (animState == this.mSetToState || animState == this.mToState || animState == this.mAutoSetToState) {
            animState.clear();
        }
    }

    public static IFolmeStateStyle composeStyle(IAnimTarget... iAnimTargetArr) {
        if (iAnimTargetArr == null || iAnimTargetArr.length == 0) {
            return null;
        }
        if (iAnimTargetArr.length == 1) {
            return new FolmeState(iAnimTargetArr[0]);
        }
        FolmeState[] folmeStateArr = new FolmeState[iAnimTargetArr.length];
        for (int i = 0; i < iAnimTargetArr.length; i++) {
            folmeStateArr[i] = new FolmeState(iAnimTargetArr[i]);
        }
        return (IFolmeStateStyle) StyleComposer.compose(IFolmeStateStyle.class, sInterceptor, folmeStateArr);
    }

    private IStateStyle fromTo(Object obj, Object obj2, AnimConfigLink animConfigLink) {
        if (this.mEnableAnim) {
            this.mCurTag = obj2;
            AnimState state = getState(obj2);
            AnimState animState = this.mToState;
            if (state != animState) {
                animState.getAllConfig(animConfigLink);
            }
            AnimRunner.getInst().run(this.mTarget, obj != null ? getState(obj) : null, getState(obj2), animConfigLink);
        }
        return this;
    }

    private FloatProperty getProperty(Object obj, Object obj2) {
        Class cls = null;
        if (obj instanceof FloatProperty) {
            return (FloatProperty) obj;
        }
        if (obj instanceof String) {
            if (obj2 != null) {
                cls = obj2.getClass();
            }
            return getTarget().createProperty((String) obj, cls);
        } else if (obj instanceof Float) {
            return DEFAULT_PROPERTY;
        } else {
            if (!(obj instanceof Integer)) {
                return null;
            }
            FloatProperty property = getTarget().getProperty(((Integer) obj).intValue());
            return property == null ? DEFAULT_INT_PROPERTY : property;
        }
    }

    private Object getPropertyValue(int i, Object... objArr) {
        if (i < objArr.length) {
            return objArr[i];
        }
        return null;
    }

    private AnimState getState(Object obj, boolean z) {
        if (obj instanceof AnimState) {
            return (AnimState) obj;
        }
        AnimState animState = (AnimState) this.mStateMap.get(obj);
        if (animState != null || !z) {
            return animState;
        }
        AnimState animState2 = new AnimState(obj);
        addState(animState2);
        return animState2;
    }

    private AnimState getStateByArgs(Object obj, Object... objArr) {
        AnimState animState;
        if (objArr.length > 0) {
            animState = getState(objArr[0], false);
            if (animState == null) {
                animState = getStateByName(objArr);
            }
        } else {
            animState = null;
        }
        return animState == null ? getState(obj) : animState;
    }

    private AnimState getStateByName(Object... objArr) {
        Object obj = objArr[0];
        Object obj2 = objArr.length > 1 ? objArr[1] : null;
        if (!(obj instanceof String) || !(obj2 instanceof String)) {
            return null;
        }
        return getState(obj, true);
    }

    private void handleFloatProperty(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
        float f = this.mAutoSetToState.get(iAnimTarget, floatProperty);
        if (Math.abs(iAnimTarget.getValue(floatProperty) - f) > iAnimTarget.getMinVisibleChange(floatProperty)) {
            getCurrentState().add(floatProperty, f, new long[0]);
            this.mDelList.add(floatProperty);
        }
    }

    private void handleIntProperty(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
        int i = this.mAutoSetToState.getInt(floatProperty);
        if (((float) Math.abs(iAnimTarget.getIntValue((IIntValueProperty) floatProperty) - i)) > iAnimTarget.getMinVisibleChange(floatProperty)) {
            getCurrentState().add(floatProperty, i, new long[0]);
            this.mDelList.add(floatProperty);
        }
    }

    private void initPredictTarget() {
        if (this.mPredictTarget == null) {
            this.mPredictTarget = Folme.getValueTarget(TARGET_PREDICT);
            this.mPredictFrom = new AnimState(TAG_PREDICT_FROM);
            this.mPredictTo = new AnimState(TAG_PREDICT_TO);
        } else {
            this.mPredictFrom.clear();
            this.mPredictTo.clear();
        }
        IAnimTarget target = getTarget();
        for (FloatProperty floatProperty : this.mPredictTo.keySet()) {
            this.mPredictTarget.setMinVisibleChange((Object) floatProperty, target.getMinVisibleChange(floatProperty));
        }
    }

    private boolean isDefaultProperty(FloatProperty floatProperty) {
        return floatProperty == DEFAULT_PROPERTY || floatProperty == DEFAULT_INT_PROPERTY;
    }

    private FloatProperty[] nameToProperty(String... strArr) {
        FloatProperty[] floatPropertyArr = new FloatProperty[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            floatPropertyArr[i] = new ValueProperty(strArr[i]);
        }
        return floatPropertyArr;
    }

    private void parse(AnimState animState, AnimConfigLink animConfigLink, Object... objArr) {
        if (objArr.length != 0) {
            int equals = animState.getTag().equals(objArr[0]);
            while (equals < objArr.length) {
                int i = equals + 1;
                equals = setPropertyAndValue(animState, animConfigLink, objArr[equals], i < objArr.length ? objArr[i] : null, equals, objArr);
            }
        }
    }

    private AnimConfigLink setAnimState(AnimState animState, Object... objArr) {
        AnimConfigLink animConfigLink = new AnimConfigLink();
        animConfigLink.add(new AnimConfig());
        clearDefaultState(animState);
        parse(animState, animConfigLink, objArr);
        return animConfigLink;
    }

    private boolean setInitVelocity(FloatProperty floatProperty, int i, Object... objArr) {
        if (i >= objArr.length) {
            return false;
        }
        Float f = objArr[i];
        if (!(f instanceof Float)) {
            return false;
        }
        getTarget().setVelocity(floatProperty, (double) f.floatValue());
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001d  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int setPropertyAndValue(AnimState animState, AnimConfigLink animConfigLink, Object obj, Object obj2, int i, Object... objArr) {
        int i2;
        if (!checkAndSetAnimConfig(animConfigLink, obj)) {
            FloatProperty property = getProperty(obj, obj2);
            if (property != null) {
                if (!isDefaultProperty(property)) {
                    i++;
                }
                i2 = addProperty(animState, property, i, objArr);
                return i2 <= 0 ? i + i2 : i + 1;
            }
        }
        i2 = 0;
        if (i2 <= 0) {
        }
    }

    private void setTempConfig(AnimConfig animConfig, Object obj) {
        if (obj instanceof TransitionListener) {
            animConfig.addListeners((TransitionListener) obj);
        } else if (obj instanceof EaseStyle) {
            animConfig.setEase((EaseStyle) obj);
        }
    }

    private IStateStyle setTo(final Object obj, final AnimConfigLink animConfigLink) {
        IAnimTarget iAnimTarget = this.mTarget;
        if (iAnimTarget == null) {
            return this;
        }
        if ((obj instanceof Integer) || (obj instanceof Float)) {
            return setTo(obj, animConfigLink);
        }
        iAnimTarget.executeOnInitialized(new Runnable() {
            public void run() {
                AnimState state = FolmeState.this.getState(obj);
                IAnimTarget target = FolmeState.this.getTarget();
                AnimTask animTask = target.getAnimTask();
                if (animTask.isValid()) {
                    animTask.cancel((FloatProperty[]) state.keySet().toArray(new FloatProperty[0]));
                }
                if (LogUtils.isLogEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("FolmeState.setTo, state = ");
                    sb.append(state);
                    LogUtils.debug(sb.toString(), new Object[0]);
                }
                AnimValueUtils.applyProperty(FolmeState.this.mTarget, state, new long[0]);
                for (FloatProperty floatProperty : state.keySet()) {
                    target.trackVelocity(floatProperty, (double) (floatProperty instanceof IIntValueProperty ? target.getIntValue((IIntValueProperty) floatProperty) : target.getValue(floatProperty)));
                }
                animTask.setToNotify(state, animConfigLink);
            }
        });
        return this;
    }

    private float toFloat(Object obj, boolean z) {
        return z ? (float) ((Integer) obj).intValue() : ((Float) obj).floatValue();
    }

    private int toInt(Object obj, boolean z) {
        return z ? ((Integer) obj).intValue() : (int) ((Float) obj).floatValue();
    }

    public IStateStyle add(String str, Object obj, long... jArr) {
        return add((FloatProperty) new ValueProperty(str), obj, jArr);
    }

    public IStateStyle add(FloatProperty floatProperty, Object obj, long... jArr) {
        addProperty((Object) getCurrentState(), floatProperty, obj, jArr);
        return this;
    }

    public void addConfig(Object obj, AnimConfig... animConfigArr) {
        getState(obj).addConfig(animConfigArr);
    }

    public IStateStyle addInitProperty(String str, Object obj) {
        return addInitProperty((FloatProperty) new ValueProperty(str), obj);
    }

    public IStateStyle addInitProperty(FloatProperty floatProperty, Object obj) {
        addProperty(this.mCurTag, floatProperty, obj, 2);
        return this;
    }

    public IStateStyle addListener(TransitionListener transitionListener) {
        getCurrentState().getGlobalConfig().addListeners(transitionListener);
        return this;
    }

    public void addState(AnimState animState) {
        this.mStateMap.put(animState.getTag(), animState);
    }

    public IStateStyle autoSetTo(Object... objArr) {
        IAnimTarget target = getTarget();
        AnimConfigLink animState = setAnimState(this.mAutoSetToState, objArr);
        getCurrentState().clear();
        this.mDelList.clear();
        for (FloatProperty floatProperty : this.mAutoSetToState.keySet()) {
            if (floatProperty instanceof IIntValueProperty) {
                handleIntProperty(target, floatProperty);
            } else {
                handleFloatProperty(target, floatProperty);
            }
        }
        for (FloatProperty remove : this.mDelList) {
            this.mAutoSetToState.remove(remove);
        }
        to(getCurrentState(), animState);
        setTo((Object) this.mAutoSetToState, animState);
        return this;
    }

    public void cancel() {
        AnimRunner.getInst().cancel(this.mTarget, new FloatProperty[0]);
    }

    public void cancel(String... strArr) {
        cancel(nameToProperty(strArr));
    }

    public void cancel(FloatProperty... floatPropertyArr) {
        AnimRunner.getInst().cancel(this.mTarget, floatPropertyArr);
    }

    public void clean() {
        cancel();
    }

    public void clear() {
        this.mStateMap.clear();
    }

    public void enableDefaultAnim(boolean z) {
        this.mEnableAnim = z;
    }

    public void end(Object... objArr) {
        ArrayList arrayList = new ArrayList();
        for (FloatProperty floatProperty : objArr) {
            if (floatProperty instanceof FloatProperty) {
                arrayList.add(floatProperty);
            } else if (floatProperty instanceof String) {
                arrayList.add(new ValueProperty((String) floatProperty));
            }
        }
        AnimRunner.getInst().end(this.mTarget, (FloatProperty[]) arrayList.toArray(new FloatProperty[0]));
    }

    public IStateStyle fromTo(Object obj, Object obj2, AnimConfig... animConfigArr) {
        fromTo(obj, obj2, AnimConfigLink.linkConfig(animConfigArr));
        return this;
    }

    public AnimState getCurrentState() {
        if (this.mCurTag == null) {
            this.mCurTag = this.mToState;
        }
        return getState(this.mCurTag);
    }

    public AnimState getState(Object obj) {
        return getState(obj, true);
    }

    public IAnimTarget getTarget() {
        return this.mTarget;
    }

    public long predictDuration(Object... objArr) {
        initPredictTarget();
        AnimConfigLink animState = setAnimState(this.mPredictTo, objArr);
        AnimState.alignState(getTarget(), this.mPredictFrom, this.mPredictTo);
        IAnimTarget iAnimTarget = this.mPredictTarget;
        AnimState animState2 = this.mPredictFrom;
        AnimState animState3 = this.mPredictTo;
        long j = 0;
        AnimTask animTask = AnimRunner.getAnimTask(iAnimTarget, 0, animState2, animState3, animState);
        long averageDelta = AnimRunner.getInst().getAverageDelta();
        while (animTask.isValid() && !animTask.isFinished()) {
            animTask.run(j, averageDelta, new long[0]);
            j += averageDelta;
        }
        return j;
    }

    public IStateStyle removeListener(TransitionListener transitionListener) {
        getCurrentState().getGlobalConfig().removeListeners(transitionListener);
        return this;
    }

    public IStateStyle set(Object obj) {
        return setup(obj);
    }

    public IStateStyle setConfig(AnimConfig animConfig, FloatProperty... floatPropertyArr) {
        AnimState currentState = getCurrentState();
        if (floatPropertyArr.length > 0) {
            AnimConfig animConfig2 = new AnimConfig(animConfig);
            animConfig2.relatedProperty = floatPropertyArr;
            currentState.addConfig(animConfig2);
        } else {
            currentState.setGlobalConfig(animConfig);
        }
        return this;
    }

    public IStateStyle setEase(int i, float... fArr) {
        getCurrentState().getGlobalConfig().ease = EaseManager.getStyle(i, fArr);
        return this;
    }

    public IStateStyle setEase(FloatProperty floatProperty, int i, float... fArr) {
        getCurrentState().getConfig(floatProperty).ease = EaseManager.getStyle(i, fArr);
        return this;
    }

    public IStateStyle setEase(EaseStyle easeStyle, FloatProperty... floatPropertyArr) {
        (floatPropertyArr.length == 0 ? getCurrentState().getGlobalConfig() : getCurrentState().getConfig(floatPropertyArr[0])).ease = easeStyle;
        return this;
    }

    public IStateStyle setTo(Object obj) {
        return setTo(obj, new AnimConfig[0]);
    }

    public IStateStyle setTo(Object obj, AnimConfig... animConfigArr) {
        return setTo(obj, AnimConfigLink.linkConfig(animConfigArr));
    }

    public IStateStyle setTo(Object... objArr) {
        AnimState stateByArgs = getStateByArgs(this.mSetToState, objArr);
        setTo((Object) stateByArgs, setAnimState(stateByArgs, objArr));
        return this;
    }

    public IStateStyle setTransitionFlags(long j, FloatProperty... floatPropertyArr) {
        AnimState currentState = getCurrentState();
        (floatPropertyArr.length == 0 ? currentState.getGlobalConfig() : currentState.getConfig(floatPropertyArr[0])).flags = j;
        return this;
    }

    public IStateStyle setup(Object obj) {
        this.mCurTag = obj;
        return this;
    }

    public IStateStyle then(Object obj, AnimConfig... animConfigArr) {
        AnimConfig animConfig = new AnimConfig();
        animConfig.flags = 1;
        return to(obj, (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, animConfig));
    }

    public IStateStyle then(Object... objArr) {
        AnimConfig animConfig = new AnimConfig();
        animConfig.flags = 1;
        return to(objArr, animConfig);
    }

    public IStateStyle to(Object obj, AnimConfig... animConfigArr) {
        if ((obj instanceof AnimState) || this.mStateMap.get(obj) != null) {
            return fromTo((Object) null, (Object) getState(obj), animConfigArr);
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Object[] objArr = new Object[(animConfigArr.length + length)];
            System.arraycopy(obj, 0, objArr, 0, length);
            System.arraycopy(animConfigArr, 0, objArr, length, animConfigArr.length);
            return to(objArr);
        }
        return to(obj, animConfigArr);
    }

    public IStateStyle to(Object... objArr) {
        AnimState stateByArgs = getStateByArgs(getCurrentState(), objArr);
        fromTo((Object) null, (Object) stateByArgs, setAnimState(stateByArgs, objArr));
        return this;
    }

    public IStateStyle to(AnimConfig... animConfigArr) {
        return to(getCurrentState(), animConfigArr);
    }
}
