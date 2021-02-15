package com.android.camera;

import android.graphics.Color;
import android.os.SystemClock;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.RawTexture;

public class CaptureAnimManager {
    private static final int ANIM_BOTH = 1;
    private static final int ANIM_HOLD = 2;
    private static final int ANIM_NONE = 0;
    private static final int ANIM_SLIDE = 3;
    private static final int TIME_HOLD = 20;
    private static final int TIME_SLIDE = 60;
    private long mAnimStartTime;
    private int mAnimType;
    private int mDrawHeight;
    private int mDrawWidth;
    private float mX;
    private float mY;

    public void animateHold() {
        this.mAnimType = 2;
    }

    public void animateHoldAndSlide() {
        this.mAnimType = 1;
    }

    public void animateSlide() {
        if (this.mAnimType == 2) {
            this.mAnimType = 3;
            this.mAnimStartTime = SystemClock.uptimeMillis();
        }
    }

    public void clearAnimation() {
        this.mAnimType = 0;
    }

    public void draw(GLCanvas gLCanvas, int i, int i2, int i3, int i4, RawTexture rawTexture) {
        rawTexture.draw(gLCanvas, i, i2, i3, i4);
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [com.android.camera.effect.draw_mode.DrawFillRectAttribute] */
    /* JADX WARNING: type inference failed for: r2v4, types: [com.android.camera.effect.draw_mode.DrawAttribute] */
    /* JADX WARNING: type inference failed for: r9v1, types: [com.android.camera.effect.draw_mode.DrawBasicTexAttribute] */
    /* JADX WARNING: type inference failed for: r9v2, types: [com.android.camera.effect.draw_mode.DrawFillRectAttribute] */
    /* JADX WARNING: type inference failed for: r9v3, types: [com.android.camera.effect.draw_mode.DrawBasicTexAttribute] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r9v2, types: [com.android.camera.effect.draw_mode.DrawFillRectAttribute]
  assigns: [com.android.camera.effect.draw_mode.DrawFillRectAttribute, com.android.camera.effect.draw_mode.DrawBasicTexAttribute]
  uses: [com.android.camera.effect.draw_mode.DrawFillRectAttribute, com.android.camera.effect.draw_mode.DrawAttribute, com.android.camera.effect.draw_mode.DrawBasicTexAttribute]
  mth insns count: 57
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawAnimation(GLCanvas gLCanvas, RawTexture rawTexture) {
        ? r2;
        GLCanvas gLCanvas2 = gLCanvas;
        long uptimeMillis = SystemClock.uptimeMillis() - this.mAnimStartTime;
        if (this.mAnimType == 3 && uptimeMillis > 60) {
            return false;
        }
        if (this.mAnimType == 1 && uptimeMillis > 80) {
            return false;
        }
        int i = this.mAnimType;
        if (i == 1) {
            i = uptimeMillis < 20 ? 2 : 3;
        }
        if (i == 2) {
            ? drawBasicTexAttribute = new DrawBasicTexAttribute(rawTexture, (int) this.mX, (int) this.mY, this.mDrawWidth, this.mDrawHeight);
            r2 = drawBasicTexAttribute;
        } else if (i != 3) {
            return false;
        } else {
            DrawBasicTexAttribute drawBasicTexAttribute2 = new DrawBasicTexAttribute(rawTexture, (int) this.mX, (int) this.mY, this.mDrawWidth, this.mDrawHeight);
            gLCanvas2.draw(drawBasicTexAttribute2);
            ? drawFillRectAttribute = new DrawFillRectAttribute((int) this.mX, (int) this.mY, this.mDrawWidth, this.mDrawHeight, Color.argb(178, 0, 0, 0));
            r2 = drawFillRectAttribute;
        }
        gLCanvas2.draw(r2);
        return true;
    }

    public void drawPreview(GLCanvas gLCanvas, RawTexture rawTexture) {
        DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(rawTexture, (int) this.mX, (int) this.mY, this.mDrawWidth, this.mDrawHeight);
        gLCanvas.draw(drawBasicTexAttribute);
    }

    public void startAnimation(int i, int i2, int i3, int i4) {
        this.mAnimStartTime = SystemClock.uptimeMillis();
        this.mDrawWidth = i3;
        this.mDrawHeight = i4;
        this.mX = (float) i;
        this.mY = (float) i2;
    }
}
