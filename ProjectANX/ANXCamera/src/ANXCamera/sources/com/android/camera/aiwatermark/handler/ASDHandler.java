package com.android.camera.aiwatermark.handler;

import android.content.Context;
import android.text.TextUtils;
import com.android.camera.aiwatermark.algo.ASDEngine;
import com.android.camera.aiwatermark.data.ASDWatermark;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.lisenter.IASDListener;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AIWatermarkDetect;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ASDHandler extends AbstractHandler {
    /* access modifiers changed from: private */
    public static final String TAG = "ASDHandler";
    private IASDListener mASDListener = null;
    protected Context mContext = null;
    private ArrayList mList = new ArrayList();
    /* access modifiers changed from: private */
    public int mSpots = 0;

    public ASDHandler(boolean z, Context context) {
        super(z);
        this.mContext = context;
        this.mData = new ASDWatermark();
    }

    private IASDListener createASDListner() {
        return new IASDListener() {
            public void onASDChange(int i) {
                String access$000 = ASDHandler.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onASDChange spots = ");
                sb.append(i);
                Log.d(access$000, sb.toString());
                String spots2ASDKey = ASDEngine.spots2ASDKey(ASDHandler.this.mSpots);
                String str = WatermarkConstant.ASD_NEGATIVE;
                boolean z = (spots2ASDKey == str && ASDEngine.spots2ASDKey(i) == str) ? false : true;
                ASDHandler.this.mSpots = i;
                if (z) {
                    WatermarkItem findWatermark = ASDHandler.this.findWatermark();
                    String access$0002 = ASDHandler.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("item=");
                    sb2.append(findWatermark);
                    Log.d(access$0002, sb2.toString());
                    if (findWatermark != null) {
                        ASDHandler.this.updateWatermark(findWatermark);
                        return;
                    }
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.findBestWatermarkItem(89);
                    }
                }
            }
        };
    }

    private void registerASDListener(IASDListener iASDListener) {
        AIWatermarkDetect aIWatermarkDetect = (AIWatermarkDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(254);
        if (aIWatermarkDetect != null) {
            aIWatermarkDetect.setListener(iASDListener);
        }
    }

    /* access modifiers changed from: private */
    public void updateWatermark(WatermarkItem watermarkItem) {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.updateWatermarkSample(watermarkItem);
        }
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ASDHandler findWatermark mASDListener = ");
        sb.append(this.mASDListener);
        Log.d(str, sb.toString());
        DataRepository.dataItemRunning().getComponentRunningAIWatermark().setIWatermarkEnable(true);
        if (this.mASDListener == null) {
            this.mASDListener = createASDListner();
            registerASDListener(this.mASDListener);
        }
        if (this.mList.size() == 0) {
            this.mList = getASDWatermarkList();
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("findWatermark mList.size() = ");
        sb2.append(this.mList.size());
        Log.d(str2, sb2.toString());
        String spots2ASDKey = ASDEngine.spots2ASDKey(this.mSpots);
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("key = ");
        sb3.append(spots2ASDKey);
        Log.d(str3, sb3.toString());
        Iterator it = this.mList.iterator();
        while (it.hasNext()) {
            WatermarkItem watermarkItem = (WatermarkItem) it.next();
            if (TextUtils.equals(spots2ASDKey, watermarkItem.getKey())) {
                return watermarkItem;
            }
        }
        Log.d(TAG, "ASD watermark null");
        return null;
    }

    public abstract ArrayList getASDWatermarkList();
}
