package miui.animation.styles;

import miui.animation.IAnimTarget;
import miui.animation.base.AnimConfigLink;
import miui.animation.font.FontWeightProperty;
import miui.animation.font.FontWeightStyle;
import miui.animation.property.ColorProperty;
import miui.animation.property.FloatProperty;
import miui.animation.property.ViewPropertyExt;

public class StyleFactory {
    private StyleFactory() {
    }

    public static PropertyStyle createAnimStyle(IAnimTarget iAnimTarget, Object obj, FloatProperty floatProperty, AnimConfigLink animConfigLink) {
        PropertyStyle propertyStyle = floatProperty == ViewPropertyExt.FOREGROUND ? new ForegroundColorStyle(obj) : (floatProperty == ViewPropertyExt.BACKGROUND || (floatProperty instanceof ColorProperty)) ? new ColorStyle(obj, floatProperty) : floatProperty instanceof FontWeightProperty ? new FontWeightStyle(obj, floatProperty) : new PropertyStyle(obj, floatProperty);
        propertyStyle.setTarget(iAnimTarget);
        propertyStyle.setConfig(animConfigLink);
        return propertyStyle;
    }
}
