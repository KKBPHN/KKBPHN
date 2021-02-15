package com.android.camera.dualvideo.render;

import android.graphics.Rect;
import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawRectShapeAttributeBase;
import com.android.gallery3d.ui.GLCanvas;

public interface CameraItemInterface {
    void alphaInSelectWindowFlag(boolean z);

    void alphaInSelectedFrame(boolean z);

    void draw(GLCanvas gLCanvas, int i, MiscTextureManager miscTextureManager);

    float getAlpha();

    FaceType getFaceType();

    Rect getHandleArea(MiscTextureManager miscTextureManager);

    LayoutType getLastRenderLayoutType();

    DrawRectShapeAttributeBase getRenderAttri(int i);

    LayoutType getRenderLayoutType();

    float getSelectFrameAlpha();

    float getSelectWindowFlagAlpha();

    LayoutType getSelectWindowLayoutType();

    SelectIndex getSelectedIndex();

    boolean isAnimating();

    boolean isPressedInSelectWindow();

    boolean isVisible();

    void onKeyDown();

    void onKeyUp();

    void setRenderAttri(DrawExtTexAttribute drawExtTexAttribute, int i);

    void setRenderLayoutTypeWithAnim(LayoutType layoutType, RegionHelper regionHelper, boolean z);

    void setSelectTypeWithAnim(SelectIndex selectIndex, boolean z);

    void setVisibilityWithAnim(boolean z, boolean z2);

    void updateRenderAttri(RegionHelper regionHelper);
}
