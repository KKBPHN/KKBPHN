package miui.analytics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.analytics.AnalyticsService;
import com.miui.internal.analytics.Event;
import com.miui.internal.analytics.IAnalytics.Stub;
import com.miui.internal.analytics.LogEvent;
import com.miui.internal.analytics.TrackEvent;
import com.miui.internal.analytics.TrackPageViewEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.os.SystemProperties;
import miui.util.SoftReferenceSingleton;

public class Analytics {
    private static final long DEFAULT_EVENT_VALUE = 0;
    private static final String EVENT_DEFAULT_PARAM = "_event_default_param_";
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public Analytics createInstance() {
            return new Analytics();
        }
    };
    private static final boolean IS_INTERNATIONAL_BUILD = SystemProperties.get("ro.product.mod_device", "").contains("_global");
    private static final String KEY_IMEI = "imei";
    private static final String TAG = "Analytics";
    private static final String TIMED_EVENT = "_timed_event_";
    private static final String TIMED_EVENT_ID = "_timed_event_id_";
    private List mAllEvents;
    private Context mContext;
    private int mCount;
    private String mPackageName;
    private List mTimedEvents;

    private Analytics() {
        this.mPackageName = "";
        this.mContext = null;
        this.mTimedEvents = null;
        this.mCount = 0;
        this.mAllEvents = null;
    }

    public static Analytics getInstance() {
        return (Analytics) INSTANCE.get();
    }

    private boolean isEnabled() {
        return !IS_INTERNATIONAL_BUILD;
    }

    private boolean isTrackedReady() {
        if (this.mAllEvents != null) {
            return true;
        }
        Log.i(TAG, "method: startSession should be called before tracking events");
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0067, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void endSession() {
        if (isEnabled()) {
            if (this.mCount > 0) {
                int i = this.mCount - 1;
                this.mCount = i;
                if (i == 0) {
                    if (isTrackedReady()) {
                        Log.i(TAG, String.format("end session(%s)", new Object[]{this.mContext.getPackageName()}));
                        this.mTimedEvents.clear();
                        this.mTimedEvents = null;
                        final Event[] eventArr = new Event[this.mAllEvents.size()];
                        this.mAllEvents.toArray(eventArr);
                        final Context context = this.mContext;
                        this.mAllEvents = null;
                        this.mContext = null;
                        Intent intent = new Intent();
                        intent.setClassName("com.miui.core", AnalyticsService.class.getName());
                        context.bindService(intent, new ServiceConnection() {
                            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                                try {
                                    Stub.asInterface(iBinder).track(eventArr);
                                } catch (Exception e) {
                                    Log.e(Analytics.TAG, "endSession() remote exception caught", e);
                                }
                                context.unbindService(this);
                            }

                            public void onServiceDisconnected(ComponentName componentName) {
                            }
                        }, 1);
                    }
                }
            }
        }
    }

    public void endTimedEvent(String str) {
        boolean z;
        if (isEnabled()) {
            List list = this.mTimedEvents;
            String str2 = TAG;
            if (list == null) {
                Log.i(str2, "there is no timed event");
                return;
            }
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                TrackEvent trackEvent = (TrackEvent) it.next();
                if (str.equals(trackEvent.getEventId())) {
                    long currentTimeMillis = System.currentTimeMillis();
                    HashMap hashMap = new HashMap();
                    hashMap.put(TIMED_EVENT_ID, str);
                    trackEvent(TIMED_EVENT, hashMap, currentTimeMillis - trackEvent.getTrackTime());
                    it.remove();
                    z = true;
                    break;
                }
            }
            if (!z) {
                Log.i(str2, String.format("the ended event (%s) is not timed", new Object[]{str}));
            }
        }
    }

    public void onTrackPageView() {
        if (isTrackedReady()) {
            this.mAllEvents.add(new TrackPageViewEvent(this.mPackageName));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void startSession(Context context) {
        if (isEnabled()) {
            if (context != null) {
                int i = this.mCount;
                this.mCount = i + 1;
                if (i == 0) {
                    this.mContext = context.getApplicationContext();
                    this.mPackageName = this.mContext.getPackageName();
                    this.mTimedEvents = Collections.synchronizedList(new ArrayList());
                    this.mAllEvents = Collections.synchronizedList(new ArrayList());
                    Log.i(TAG, String.format("start session(%s)", new Object[]{this.mContext.getPackageName()}));
                }
            }
        }
    }

    public void trackError(String str, String str2, String str3) {
        if (isEnabled()) {
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3)) {
                Log.i(TAG, "the id or error class of logged event is null or empty");
            } else if (isTrackedReady()) {
                if (str2 == null) {
                    str2 = "";
                }
                this.mAllEvents.add(new LogEvent(this.mPackageName, str, str2, str3));
            }
        }
    }

    public void trackEvent(String str) {
        trackTimedEvent(str, null, false, 0);
    }

    public void trackEvent(String str, long j) {
        trackTimedEvent(str, null, false, j);
    }

    public void trackEvent(String str, Object obj) {
        if (isEnabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(EVENT_DEFAULT_PARAM, obj.toString());
            trackEvent(str, (Map) hashMap);
        }
    }

    public void trackEvent(String str, Map map) {
        trackTimedEvent(str, map, false, 0);
    }

    public void trackEvent(String str, Map map, long j) {
        trackTimedEvent(str, map, false, j);
    }

    public void trackTimedEvent(String str, Map map, boolean z) {
        trackTimedEvent(str, map, z, 0);
    }

    public void trackTimedEvent(String str, Map map, boolean z, long j) {
        if (isEnabled()) {
            if (TextUtils.isEmpty(str)) {
                Log.i(TAG, "the id of tracked event is null or empty");
            } else if (isTrackedReady()) {
                if (map == null) {
                    map = new HashMap();
                }
                TrackEvent trackEvent = new TrackEvent(this.mPackageName, str, map, j);
                this.mAllEvents.add(trackEvent);
                if (z) {
                    this.mTimedEvents.add(trackEvent);
                }
            }
        }
    }

    public void trackTimedEvent(String str, boolean z) {
        trackTimedEvent(str, null, z, 0);
    }

    public void trackTimedEvent(String str, boolean z, long j) {
        trackTimedEvent(str, null, z, j);
    }
}
