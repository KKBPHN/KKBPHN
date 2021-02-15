package com.miui.internal.analytics;

import android.content.Context;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.util.AppConstants;
import miui.util.SoftReferenceSingleton;

public class PersistenceHelper {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public PersistenceHelper createInstance() {
            return new PersistenceHelper();
        }
    };
    private static final int MSG_WRITE_EVENT = 1;
    private static final String TAG = "PersistenceHelper";
    private Context mContext;
    private ObjectBuilder mEventBuilder;
    private Storable mStore;
    private ObjectBuilder mStoreBuilder;
    /* access modifiers changed from: private */
    public Handler mWorkHandler;
    /* access modifiers changed from: private */
    public final AtomicBoolean mWorkerReady;

    class RunThread extends Thread {
        private RunThread() {
        }

        public void run() {
            Looper.prepare();
            synchronized (PersistenceHelper.this.mWorkerReady) {
                PersistenceHelper.this.mWorkHandler = new Handler() {
                    public void handleMessage(Message message) {
                        if (message.what == 1) {
                            PersistenceHelper.this.writeEventImp((Event) message.obj);
                        }
                    }
                };
                PersistenceHelper.this.mWorkerReady.set(true);
                PersistenceHelper.this.mWorkerReady.notify();
            }
            Looper.loop();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:4|5|(2:7|8)(0)|9) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x005f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private PersistenceHelper() {
        this.mWorkerReady = new AtomicBoolean();
        this.mStoreBuilder = new ObjectBuilder();
        this.mEventBuilder = new ObjectBuilder();
        this.mEventBuilder.registerClass(TrackEvent.class, "2");
        this.mEventBuilder.registerClass(LogEvent.class, "1");
        this.mEventBuilder.registerClass(TrackPageViewEvent.class, "3");
        this.mStoreBuilder.registerClass(SQLiteStore.class, SQLiteStore.TAG);
        this.mContext = AppConstants.getCurrentApplication();
        createStore();
        synchronized (this.mWorkerReady) {
            new RunThread().start();
            while (true) {
                if (!this.mWorkerReady.get()) {
                    this.mWorkerReady.wait();
                }
                break;
            }
        }
    }

    private void createStore() {
        this.mStore = (Storable) this.mStoreBuilder.buildObject(SQLiteStore.TAG);
        this.mStore.create(this.mContext);
    }

    public static PersistenceHelper getInstance() {
        return (PersistenceHelper) INSTANCE.get();
    }

    /* access modifiers changed from: private */
    public void writeEventImp(Event event) {
        Storable storable = this.mStore;
        if (storable != null) {
            event.writeEvent(storable);
        }
    }

    public void close() {
        this.mWorkHandler.getLooper().quit();
        this.mWorkHandler = null;
        Storable storable = this.mStore;
        if (storable != null) {
            storable.close();
            this.mStore = null;
        }
        this.mContext = null;
    }

    public List readEvents(String str, List list) {
        Storable storable = this.mStore;
        if (storable != null) {
            Cursor readDataset = storable.readDataset(str);
            if (readDataset != null) {
                ArrayList arrayList = new ArrayList();
                while (readDataset.moveToNext()) {
                    try {
                        int i = readDataset.getInt(readDataset.getColumnIndexOrThrow("type"));
                        ObjectBuilder objectBuilder = this.mEventBuilder;
                        StringBuilder sb = new StringBuilder();
                        sb.append(i);
                        sb.append("");
                        Event event = (Event) objectBuilder.buildObject(sb.toString());
                        if (event != null) {
                            event.restore(readDataset);
                            Iterator it = list.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Item item = (Item) it.next();
                                if (item.idMatches(event.getEventId())) {
                                    if (item.isDispatch(VERSION.INCREMENTAL)) {
                                        event.setPolicy(item.getPolicy());
                                        arrayList.add(event);
                                    }
                                }
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, "IllegalArgumentException catched when readEvents from storage", e);
                    }
                }
                readDataset.close();
                return arrayList;
            }
        }
        return Collections.emptyList();
    }

    public boolean selectStore(String str) {
        Storable storable = (Storable) this.mStoreBuilder.buildObject(str);
        if (storable == null) {
            return false;
        }
        Storable storable2 = this.mStore;
        if (storable2 != null) {
            storable2.close();
        }
        this.mStore = storable;
        return true;
    }

    public void writeEvent(Event event) {
        if (this.mWorkHandler != null) {
            Message message = new Message();
            message.what = 1;
            message.obj = event;
            this.mWorkHandler.sendMessage(message);
        }
    }

    public void writeEvent(String str, String str2, long j) {
        TrackEvent trackEvent = new TrackEvent(str, str2, null, j);
        writeEvent(trackEvent);
    }
}
