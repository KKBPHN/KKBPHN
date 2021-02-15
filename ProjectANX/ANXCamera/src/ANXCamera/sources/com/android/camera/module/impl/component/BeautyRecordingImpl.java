package com.android.camera.module.impl.component;

import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BeautyRecording;
import com.android.camera.protocol.ModeProtocol.HandleBeautyRecording;
import java.util.ArrayList;
import java.util.Iterator;

public class BeautyRecordingImpl implements BeautyRecording {
    private ArrayList recordingArrayList = new ArrayList();

    public static BeautyRecordingImpl create() {
        return new BeautyRecordingImpl();
    }

    public void addBeautyStack(HandleBeautyRecording handleBeautyRecording) {
        this.recordingArrayList.add(handleBeautyRecording);
    }

    public void handleAngleChang(float f) {
        Iterator it = this.recordingArrayList.iterator();
        while (it.hasNext()) {
            ((HandleBeautyRecording) it.next()).onAngleChanged(f);
        }
    }

    public void handleBeautyRecordingStart() {
        Iterator it = this.recordingArrayList.iterator();
        while (it.hasNext()) {
            ((HandleBeautyRecording) it.next()).onBeautyRecordingStart();
        }
    }

    public void handleBeautyRecordingStop() {
        Iterator it = this.recordingArrayList.iterator();
        while (it.hasNext()) {
            ((HandleBeautyRecording) it.next()).onBeautyRecordingStop();
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(173, this);
    }

    public void removeBeautyStack(HandleBeautyRecording handleBeautyRecording) {
        this.recordingArrayList.remove(handleBeautyRecording);
    }

    public void unRegisterProtocol() {
        this.recordingArrayList.clear();
        ModeCoordinatorImpl.getInstance().detachProtocol(173, this);
    }
}
