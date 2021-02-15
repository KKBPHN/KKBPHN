package miuix.animation.styles;

import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.property.ColorProperty;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewPropertyExt;

public class StyleFactory {
    private StyleFactory() {
    }

    public static PropertyStyle createAnimStyle(IAnimTarget iAnimTarget, Object obj, FloatProperty floatProperty, AnimConfigLink animConfigLink) {
        PropertyStyle propertyStyle = floatProperty == ViewPropertyExt.FOREGROUND ? new ForegroundColorStyle(obj) : (floatProperty == ViewPropertyExt.BACKGROUND || (floatProperty instanceof ColorProperty)) ? new ColorStyle(obj, floatProperty) : new PropertyStyle(obj, floatProperty);
        propertyStyle.setTarget(iAnimTarget);
        propertyStyle.setConfig(animConfigLink);
        return propertyStyle;
    }
}
