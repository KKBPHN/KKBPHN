package miuix.animation;

import android.widget.TextView;
import miuix.animation.base.AnimConfig;

public interface IVarFontStyle extends IStateContainer {
    IVarFontStyle fromTo(int i, int i2, AnimConfig... animConfigArr);

    IVarFontStyle setTo(int i);

    IVarFontStyle to(int i, AnimConfig... animConfigArr);

    IVarFontStyle useAt(TextView textView, int i, int i2);
}
