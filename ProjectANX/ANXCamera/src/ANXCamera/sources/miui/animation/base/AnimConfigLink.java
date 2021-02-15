package miui.animation.base;

import java.util.ArrayList;
import java.util.List;
import miui.animation.property.FloatProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.EaseManager;
import miui.animation.utils.EaseManager.EaseStyle;

public class AnimConfigLink {
    private static IMapOperation mDelayOp = new IMapOperation() {
        public Long process(AnimConfig animConfig, FloatProperty floatProperty, Long l) {
            return Long.valueOf(l.longValue() + animConfig.delay);
        }
    };
    private static IMapOperation mFlagsOp = new IMapOperation() {
        public Long process(AnimConfig animConfig, FloatProperty floatProperty, Long l) {
            return Long.valueOf(animConfig.flags | l.longValue());
        }
    };
    private static IMapOperation mFromSpeedOp = new IMapOperation() {
        public Float process(AnimConfig animConfig, FloatProperty floatProperty, Float f) {
            if (animConfig.fromSpeed == Float.MAX_VALUE) {
                return f;
            }
            return Float.valueOf(f.floatValue() == Float.MAX_VALUE ? animConfig.fromSpeed : Math.max(f.floatValue(), animConfig.fromSpeed));
        }
    };
    private static IMapOperation mMinDurationOp = new IMapOperation() {
        public Long process(AnimConfig animConfig, FloatProperty floatProperty, Long l) {
            return Long.valueOf(Math.max(l.longValue(), animConfig.minDuration));
        }
    };
    private static IMapOperation mTintModeOp = new IMapOperation() {
        public Integer process(AnimConfig animConfig, FloatProperty floatProperty, Integer num) {
            return Integer.valueOf(Math.max(num.intValue(), animConfig.tintMode));
        }
    };
    public List configList = new ArrayList();

    interface IMapOperation {
        Object process(AnimConfig animConfig, FloatProperty floatProperty, Object obj);
    }

    private static boolean canNotApply(AnimConfig animConfig, FloatProperty floatProperty) {
        return !CommonUtils.isArrayEmpty(animConfig.relatedProperty) && !CommonUtils.inArray(animConfig.relatedProperty, floatProperty);
    }

    private boolean isSameTag(Object obj, Object obj2) {
        return obj.getClass() == obj2.getClass() && obj.toString().equals(obj2.toString());
    }

    public static AnimConfigLink linkConfig(AnimConfig... animConfigArr) {
        AnimConfigLink animConfigLink = new AnimConfigLink();
        for (AnimConfig add : animConfigArr) {
            animConfigLink.add(add);
        }
        return animConfigLink;
    }

    private Object mapLink(Object obj, FloatProperty floatProperty, IMapOperation iMapOperation, Object obj2) {
        for (AnimConfig animConfig : this.configList) {
            if (obj != null) {
                Object obj3 = animConfig.tag;
                if (obj3 != null && isSameTag(obj3, obj)) {
                }
            }
            if (CommonUtils.isArrayEmpty(animConfig.relatedProperty)) {
                obj2 = iMapOperation.process(animConfig, null, obj2);
            } else if (CommonUtils.inArray(animConfig.relatedProperty, floatProperty)) {
                obj2 = iMapOperation.process(animConfig, floatProperty, obj2);
            }
        }
        return obj2;
    }

    private static boolean srcPreferProperty(AnimConfig animConfig, AnimConfig animConfig2) {
        boolean z = true;
        if (animConfig == null) {
            if (animConfig2 == null) {
                z = false;
            }
            return z;
        }
        if (!CommonUtils.isArrayEmpty(animConfig.relatedProperty) || CommonUtils.isArrayEmpty(animConfig2.relatedProperty)) {
            z = false;
        }
        return z;
    }

    public void add(AnimConfig animConfig) {
        if (animConfig != null && !this.configList.contains(animConfig)) {
            this.configList.add(new AnimConfig(animConfig));
        }
    }

    public void add(AnimConfigLink animConfigLink) {
        if (animConfigLink != null) {
            for (AnimConfig add : animConfigLink.configList) {
                add(add);
            }
        }
    }

    public void clear() {
        this.configList.clear();
    }

    public long getDelay(Object obj, FloatProperty floatProperty) {
        return ((Long) mapLink(obj, floatProperty, mDelayOp, Long.valueOf(0))).longValue();
    }

    public EaseStyle getEase(FloatProperty floatProperty) {
        EaseStyle easeStyle = null;
        AnimConfig animConfig = null;
        for (AnimConfig animConfig2 : this.configList) {
            if (animConfig2.ease != null && animConfig2.effectFromValue <= 0.0d) {
                if (!canNotApply(animConfig2, floatProperty)) {
                    if (easeStyle == null || srcPreferProperty(animConfig, animConfig2) || EaseManager.isPhysicsStyle(animConfig2.ease.style)) {
                        easeStyle = animConfig2.ease;
                        animConfig = animConfig2;
                    }
                }
            }
        }
        return easeStyle == null ? CommonUtils.sDefEase : easeStyle;
    }

    public EaseStyle getEaseEffectFromValue(FloatProperty floatProperty, double d, double d2) {
        for (AnimConfig animConfig : this.configList) {
            if (!(animConfig.ease == null || animConfig.effectFromValue == 0.0d)) {
                if (!canNotApply(animConfig, floatProperty)) {
                    double d3 = animConfig.effectFromValue;
                    if (d3 > d && d3 <= d2) {
                        return animConfig.ease;
                    }
                }
            }
        }
        return null;
    }

    public long getFlags(Object obj, FloatProperty floatProperty) {
        return ((Long) mapLink(obj, floatProperty, mFlagsOp, Long.valueOf(0))).longValue();
    }

    public float getFromSpeed(Object obj, FloatProperty floatProperty) {
        return ((Float) mapLink(obj, floatProperty, mFromSpeedOp, Float.valueOf(Float.MAX_VALUE))).floatValue();
    }

    public AnimConfig getHead() {
        return (AnimConfig) this.configList.get(0);
    }

    public long getMinDuration(Object obj, FloatProperty floatProperty) {
        return ((Long) mapLink(obj, floatProperty, mMinDurationOp, Long.valueOf(0))).longValue();
    }

    public int getTintMode(Object obj, FloatProperty floatProperty) {
        return ((Integer) mapLink(obj, floatProperty, mTintModeOp, Integer.valueOf(0))).intValue();
    }

    public void remove(AnimConfig animConfig) {
        this.configList.remove(animConfig);
    }

    public int size() {
        return this.configList.size();
    }
}
