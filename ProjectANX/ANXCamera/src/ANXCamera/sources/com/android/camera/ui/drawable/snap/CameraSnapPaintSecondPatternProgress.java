package com.android.camera.ui.drawable.snap;

import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintSecondPatternProgress extends PaintPattern {
    public CameraSnapPaintSecondPatternProgress(CameraSnapPaintSecond cameraSnapPaintSecond) {
        super(cameraSnapPaintSecond);
    }

    public void directlyResult() {
        CameraPaintBase cameraPaintBase = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelCurrent = ((CameraSnapPaintSecond) cameraPaintBase).baseAngel;
        ((CameraSnapPaintSecond) cameraPaintBase).squashAngelCurrent = ((CameraSnapPaintSecond) cameraPaintBase).baseAngel;
        ((CameraSnapPaintSecond) cameraPaintBase).motionLineWidthCurrent = ((CameraSnapPaintSecond) cameraPaintBase).baseLineWidth;
        ((CameraSnapPaintSecond) cameraPaintBase).commonLineWidthCurrent = ((CameraSnapPaintSecond) cameraPaintBase).baseLineWidth;
    }

    public boolean interceptDraw() {
        return false;
    }

    public void prepareTargetPattern() {
        CameraPaintBase cameraPaintBase = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelSrc = ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelCurrent;
        ((CameraSnapPaintSecond) cameraPaintBase).squashAngelSrc = ((CameraSnapPaintSecond) cameraPaintBase).squashAngelCurrent;
        ((CameraSnapPaintSecond) cameraPaintBase).motionLineWidthSrc = ((CameraSnapPaintSecond) cameraPaintBase).motionLineWidthCurrent;
        ((CameraSnapPaintSecond) cameraPaintBase).commonLineWidthSrc = ((CameraSnapPaintSecond) cameraPaintBase).commonLineWidthCurrent;
        ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelDst = ((CameraSnapPaintSecond) cameraPaintBase).baseAngel;
        ((CameraSnapPaintSecond) cameraPaintBase).squashAngelDst = ((CameraSnapPaintSecond) cameraPaintBase).baseAngel;
        ((CameraSnapPaintSecond) cameraPaintBase).motionLineWidthDst = ((CameraSnapPaintSecond) cameraPaintBase).baseLineWidth;
        ((CameraSnapPaintSecond) cameraPaintBase).commonLineWidthDst = ((CameraSnapPaintSecond) cameraPaintBase).baseLineWidth;
    }

    public void updateValue(float f) {
        CameraPaintBase cameraPaintBase = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelCurrent = calculateCurrentValue(((CameraSnapPaintSecond) cameraPaintBase).quarterAngelSrc, ((CameraSnapPaintSecond) cameraPaintBase).quarterAngelDst, f);
        CameraPaintBase cameraPaintBase2 = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase2).squashAngelCurrent = calculateCurrentValue(((CameraSnapPaintSecond) cameraPaintBase2).squashAngelSrc, ((CameraSnapPaintSecond) cameraPaintBase2).squashAngelDst, f);
        CameraPaintBase cameraPaintBase3 = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase3).motionLineWidthCurrent = calculateCurrentValue(((CameraSnapPaintSecond) cameraPaintBase3).motionLineWidthSrc, ((CameraSnapPaintSecond) cameraPaintBase3).motionLineWidthDst, f);
        CameraPaintBase cameraPaintBase4 = this.paintBase;
        ((CameraSnapPaintSecond) cameraPaintBase4).commonLineWidthCurrent = calculateCurrentValue(((CameraSnapPaintSecond) cameraPaintBase4).commonLineWidthSrc, ((CameraSnapPaintSecond) cameraPaintBase4).commonLineWidthDst, f);
    }
}
