package miuix.animation.internal;

import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.styles.PropertyStyle;
import miuix.animation.styles.StyleFactory;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.EaseManager.EaseStyle;
import miuix.animation.utils.LogUtils;

public class AnimRunningInfo {
    static final int STATUS_BEGIN = 0;
    private static final int STATUS_PENDING = -1;
    static final int STATUS_RUNNING = 1;
    static final int STATUS_RUN_TO_MINIMUM = 2;
    static final int STATUS_STOP = 3;
    public PropertyStyle anim;
    public AnimConfigLink config;
    public EaseStyle ease;
    public long flags;
    private Number fromValue;
    long initTime;
    private UpdateInfo mUpdate = new UpdateInfo();
    AnimRunningInfo pending;
    public FloatProperty property;
    private long startTime = -1;
    public int status = 0;
    public IAnimTarget target;
    private long toFlags;
    public Object toTag;
    Number toValue;
    long transId;

    private void setFromValue() {
        Number number = this.fromValue;
        if (number != null) {
            FloatProperty floatProperty = this.property;
            boolean z = floatProperty instanceof IIntValueProperty;
            IAnimTarget iAnimTarget = this.target;
            if (z) {
                iAnimTarget.setIntValue((IIntValueProperty) floatProperty, number.intValue());
            } else {
                iAnimTarget.setValue(floatProperty, number.floatValue());
            }
        }
    }

    private void setStartTime(long j) {
        this.startTime = j;
        PropertyStyle propertyStyle = this.anim;
        if (propertyStyle != null) {
            propertyStyle.resetRunningTime();
        }
    }

    private boolean setValue(IAnimTarget iAnimTarget, PropertyStyle propertyStyle) {
        if (this.property instanceof IIntValueProperty) {
            return AnimValueUtils.setIntValues(iAnimTarget, propertyStyle, this, this.toValue, this.toFlags);
        }
        return AnimValueUtils.setFloatValues(iAnimTarget, propertyStyle, this, this.toValue, this.toFlags);
    }

    /* access modifiers changed from: 0000 */
    public void begin(IAnimTarget iAnimTarget, long j) {
        this.status = 1;
        this.mUpdate.reset();
        if (this.anim == null) {
            this.anim = StyleFactory.createAnimStyle(iAnimTarget, this.toTag, this.property, this.config);
        }
        setStartTime(j);
        setFromValue();
        float fromSpeed = this.config.getFromSpeed(this.toTag, this.property);
        String str = "AnimRunningInfo, begin ";
        if (fromSpeed != Float.MAX_VALUE) {
            if (LogUtils.isLogEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(this.property.getName());
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("fromSpeed = ");
                sb3.append(fromSpeed);
                LogUtils.debug(sb2, sb3.toString());
            }
            iAnimTarget.setVelocity(this.property, (double) fromSpeed);
        }
        if (!setValue(iAnimTarget, this.anim)) {
            stop(true);
        } else if (!this.anim.isRunning()) {
            if (LogUtils.isLogEnabled()) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(this.property.getName());
                String sb5 = sb4.toString();
                StringBuilder sb6 = new StringBuilder();
                sb6.append("toTag = ");
                sb6.append(this.toTag);
                StringBuilder sb7 = new StringBuilder();
                sb7.append("target object = ");
                sb7.append(iAnimTarget.getTargetObject());
                StringBuilder sb8 = new StringBuilder();
                sb8.append("begin velocity = ");
                sb8.append(iAnimTarget.getVelocity(this.property));
                LogUtils.debug(sb5, sb6.toString(), sb7.toString(), sb8.toString());
            }
            this.anim.start();
        }
    }

    /* access modifiers changed from: 0000 */
    public long getRunningTime() {
        PropertyStyle propertyStyle = this.anim;
        if (propertyStyle == null) {
            return 0;
        }
        return propertyStyle.getRunningTime();
    }

    /* access modifiers changed from: 0000 */
    public boolean isAnimEnd(long j) {
        if (this.startTime < j) {
            PropertyStyle propertyStyle = this.anim;
            if (propertyStyle == null || !propertyStyle.isRunning()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isFinished() {
        return this.status == 3;
    }

    /* access modifiers changed from: 0000 */
    public boolean isPhysicsWithVelocity() {
        return EaseManager.isPhysicsStyle(this.ease.style) && this.target.getVelocity(this.property) != 0.0d;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRunning() {
        if (this.anim != null) {
            int i = this.status;
            if (i == 1 || i == 2) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void run(long j) {
        if (isRunning()) {
            this.anim.update(j);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setConfig(AnimConfigLink animConfigLink) {
        this.config = animConfigLink;
        this.ease = animConfigLink.getEase(this.property);
        this.flags = animConfigLink.getFlags(this.toTag, this.property);
    }

    /* access modifiers changed from: 0000 */
    public void setFromInfo(TransitionInfo transitionInfo) {
        this.fromValue = (Number) transitionInfo.fromPropValues.get(this.property);
    }

    /* access modifiers changed from: 0000 */
    public void setPending(AnimRunningInfo animRunningInfo) {
        this.status = 2;
        this.pending = animRunningInfo;
        animRunningInfo.status = -1;
    }

    /* access modifiers changed from: 0000 */
    public void setToInfo(TransitionInfo transitionInfo) {
        this.toValue = (Number) transitionInfo.toPropValues.get(this.property);
        this.toTag = transitionInfo.toTag;
        Long l = (Long) transitionInfo.toFlags.get(this.property);
        if (l != null) {
            this.toFlags = l.longValue();
        }
    }

    /* access modifiers changed from: 0000 */
    public void stop() {
        stop(false);
    }

    /* access modifiers changed from: 0000 */
    public void stop(boolean z) {
        if (isRunning()) {
            if (LogUtils.isLogEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("AnimRunningInfo, stop ");
                sb.append(this.property.getName());
                String sb2 = sb.toString();
                Object[] objArr = new Object[3];
                StringBuilder sb3 = new StringBuilder();
                sb3.append("toTag = ");
                sb3.append(this.toTag);
                objArr[0] = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append("property = ");
                sb4.append(this.property.getName());
                objArr[1] = sb4.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append("anim.getCurrentValue = ");
                sb5.append(this.property instanceof IIntValueProperty ? (float) this.anim.getCurrentIntValue() : this.anim.getCurrentValue());
                objArr[2] = sb5.toString();
                LogUtils.debug(sb2, objArr);
            }
            this.status = 3;
            if (z) {
                this.anim.end();
            } else {
                this.mUpdate.isCanceled = true;
                this.anim.cancel();
            }
            AnimRunningInfo animRunningInfo = this.pending;
            if (animRunningInfo != null) {
                animRunningInfo.status = 0;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void update(TransitionInfo transitionInfo, long j) {
        boolean isPhysicsStyle = EaseManager.isPhysicsStyle(this.ease.style);
        this.ease = transitionInfo.config.getEase(this.property);
        boolean isPhysicsStyle2 = EaseManager.isPhysicsStyle(this.ease.style);
        setToInfo(transitionInfo);
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("update anim for ");
            sb.append(this.property.getName());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("to = ");
            sb3.append(this.toTag);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("value ");
            sb4.append(this.toValue);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("newEase = ");
            sb5.append(this.ease);
            LogUtils.debug(sb2, sb3.toString(), sb4.toString(), sb5.toString());
        }
        if (this.anim == null || isPhysicsStyle != isPhysicsStyle2 || !isPhysicsStyle2) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("update anim, clear old and begin new", new Object[0]);
            }
            PropertyStyle propertyStyle = this.anim;
            if (propertyStyle != null) {
                propertyStyle.clear();
                this.fromValue = null;
                this.anim.setConfig(transitionInfo.config);
            } else {
                this.anim = StyleFactory.createAnimStyle(this.target, this.toTag, this.property, transitionInfo.config);
            }
            begin(this.target, j);
            return;
        }
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("update anim values", new Object[0]);
        }
        this.anim.setConfig(transitionInfo.config);
        setValue(this.target, this.anim);
    }

    /* access modifiers changed from: 0000 */
    public UpdateInfo updateToDate() {
        float f;
        UpdateInfo updateInfo;
        UpdateInfo updateInfo2 = this.mUpdate;
        updateInfo2.transId = this.transId;
        FloatProperty floatProperty = this.property;
        updateInfo2.setValue(floatProperty instanceof IIntValueProperty ? Integer.valueOf(this.target.getIntValue((IIntValueProperty) floatProperty)) : Float.valueOf(this.target.getValue(floatProperty)));
        this.mUpdate.property = this.property;
        if (EaseManager.isPhysicsStyle(this.ease.style)) {
            updateInfo = this.mUpdate;
            f = (float) this.target.getVelocity(this.property);
        } else {
            updateInfo = this.mUpdate;
            f = 0.0f;
        }
        updateInfo.velocity = f;
        UpdateInfo updateInfo3 = this.mUpdate;
        updateInfo3.anim = this.anim;
        updateInfo3.setComplete(isFinished());
        return this.mUpdate;
    }
}
