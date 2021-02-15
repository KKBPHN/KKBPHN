package com.ss.android.medialib;

import com.bef.effectsdk.message.MessageCenter;
import com.bef.effectsdk.message.MessageCenter.Listener;
import java.util.ArrayList;
import java.util.List;

public class MessageCenterWrapper implements Listener {
    private static MessageCenterWrapper INSTANCE;
    private List listeners = new ArrayList();
    private int mCount = 0;

    private MessageCenterWrapper() {
    }

    public static MessageCenterWrapper getInstance() {
        if (INSTANCE == null) {
            synchronized (MessageCenterWrapper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageCenterWrapper();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void addListener(Listener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void destroy() {
        if (this.mCount > 0) {
            this.mCount--;
            if (this.mCount == 0) {
                MessageCenter.destroy();
            }
        }
    }

    public synchronized void init() {
        if (this.mCount == 0) {
            MessageCenter.init();
            MessageCenter.setListener(this);
        }
        this.mCount++;
    }

    public void onMessageReceived(int i, int i2, int i3, String str) {
        synchronized (this) {
            for (Listener onMessageReceived : this.listeners) {
                onMessageReceived.onMessageReceived(i, i2, i3, str);
            }
        }
    }

    public synchronized void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }
}
