package com.miui.internal.analytics;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.net.ConnectivityHelper;
import miui.security.DigestUtils;
import miui.telephony.TelephonyHelper;
import miui.text.ExtraTextUtils;

public class DispatcherManager {
    private static final String DEVICE_IMEI = "_device_imei_";
    private static final String DEVICE_INFO_EVENT = "_device_info_event_";
    private static final String DEVICE_MODEL = "_device_model_";
    private static final String TAG = "DispatcherManager";
    private ObjectBuilder mBuilder = new ObjectBuilder();
    private Context mContext;
    private Map mDispatcher = new HashMap();
    private EventReader mEventReader = new EventReader();
    private PolicyHelper mPolicyHelper = new PolicyHelper();

    public DispatcherManager(Context context) {
        this.mContext = context;
        this.mBuilder.registerClass(XiaomiDispatcher.class, "xiaomi");
    }

    private void dispatchCommonEvent(String str) {
        Policy policy = this.mPolicyHelper.getPolicy("normal");
        HashMap hashMap = new HashMap();
        String deviceId = TelephonyHelper.getInstance().getDeviceId();
        hashMap.put(DEVICE_IMEI, TextUtils.isEmpty(deviceId) ? "null" : ExtraTextUtils.toHexReadable(DigestUtils.get((CharSequence) deviceId, "MD5")));
        hashMap.put(DEVICE_MODEL, Build.MODEL);
        TrackEvent trackEvent = new TrackEvent(str, DEVICE_INFO_EVENT, hashMap, 0);
        if (policy != null && ConnectivityHelper.getInstance().isUnmeteredNetworkConnected() && EventUtils.enableWrite(this.mContext)) {
            policy.execute(trackEvent);
        }
    }

    private void dispatchData(String str, List list) {
        boolean z;
        this.mEventReader.open(this.mContext);
        EventReader eventReader = this.mEventReader;
        StringBuilder sb = new StringBuilder();
        sb.append("package = \"");
        sb.append(str);
        sb.append("\"");
        Iterator it = eventReader.readEvents(sb.toString(), list).iterator();
        while (true) {
            if (!it.hasNext()) {
                z = true;
                break;
            }
            Event event = (Event) it.next();
            Policy policy = this.mPolicyHelper.getPolicy(event.getPolicy());
            if (policy != null) {
                if (!ConnectivityHelper.getInstance().isUnmeteredNetworkConnected() || !EventUtils.enableWrite(this.mContext)) {
                    z = false;
                } else {
                    policy.execute(event);
                }
            }
        }
        z = false;
        this.mEventReader.close();
        if (z) {
            DatabaseHelper.getInstance().deleteLastDispatchedEvents();
        }
    }

    public void dispatch(String str, List list) {
        for (Dispatchable dispatchable : this.mDispatcher.keySet()) {
            dispatchable.start(this.mContext, (String) this.mDispatcher.get(dispatchable));
        }
        this.mPolicyHelper.clear();
        dispatchData(str, list);
        this.mPolicyHelper.end();
        for (Dispatchable stop : this.mDispatcher.keySet()) {
            stop.stop();
        }
    }

    public void switchDispatcher(Map map) {
        this.mDispatcher.clear();
        if (map != null) {
            for (String str : map.keySet()) {
                Dispatchable dispatchable = (Dispatchable) this.mBuilder.buildObject(str);
                if (dispatchable == null) {
                    Log.d(TAG, String.format("server:%s does not exist", new Object[]{str}));
                } else {
                    this.mDispatcher.put(dispatchable, (String) map.get(str));
                }
            }
            if (map.size() > 0) {
                Event.setDispatcher(new ArrayList(this.mDispatcher.keySet()));
            }
        }
    }
}
