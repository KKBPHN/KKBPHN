package com.google.lens.sdk;

import android.app.PendingIntent;
import android.support.annotation.Keep;

@Keep
public interface PendingIntentConsumer {
    void onReceivedPendingIntent(PendingIntent pendingIntent);
}
