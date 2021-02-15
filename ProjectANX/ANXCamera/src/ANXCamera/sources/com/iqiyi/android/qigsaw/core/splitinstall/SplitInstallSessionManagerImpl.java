package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class SplitInstallSessionManagerImpl implements SplitInstallSessionManager {
    private final SparseArray mActiveSessionStates = new SparseArray();
    private final Context mContext;
    private final Object mLock = new Object();
    private final String mPackageName;

    SplitInstallSessionManagerImpl(Context context) {
        this.mContext = context;
        this.mPackageName = context.getPackageName();
    }

    private static List asList(SparseArray sparseArray) {
        ArrayList arrayList = new ArrayList(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    public void changeSessionState(int i, int i2) {
        synchronized (this.mLock) {
            SplitInstallInternalSessionState splitInstallInternalSessionState = (SplitInstallInternalSessionState) this.mActiveSessionStates.get(i);
            if (splitInstallInternalSessionState != null) {
                splitInstallInternalSessionState.setStatus(i2);
                if (i2 == 7 || i2 == 6 || i2 == 10) {
                    removeSessionState(i);
                }
            }
        }
    }

    public void emitSessionState(SplitInstallInternalSessionState splitInstallInternalSessionState) {
        Bundle transform2Bundle = SplitInstallInternalSessionState.transform2Bundle(splitInstallInternalSessionState);
        Intent intent = new Intent();
        intent.putExtra("session_state", transform2Bundle);
        intent.setPackage(this.mPackageName);
        intent.setAction("com.iqiyi.android.play.core.splitinstall.receiver.SplitInstallUpdateIntentService");
        this.mContext.sendBroadcast(intent);
    }

    public SplitInstallInternalSessionState getSessionState(int i) {
        SplitInstallInternalSessionState splitInstallInternalSessionState;
        synchronized (this.mLock) {
            splitInstallInternalSessionState = (SplitInstallInternalSessionState) this.mActiveSessionStates.get(i);
        }
        return splitInstallInternalSessionState;
    }

    public List getSessionStates() {
        List asList;
        synchronized (this.mLock) {
            asList = asList(this.mActiveSessionStates);
        }
        return asList;
    }

    public boolean isActiveSessionsLimitExceeded() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mActiveSessionStates.size(); i++) {
                if (((SplitInstallInternalSessionState) this.mActiveSessionStates.valueAt(i)).status() == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isIncompatibleWithExistingSession(List list) {
        boolean z;
        synchronized (this.mLock) {
            List sessionStates = getSessionStates();
            z = false;
            for (int i = 0; i < sessionStates.size(); i++) {
                SplitInstallInternalSessionState splitInstallInternalSessionState = (SplitInstallInternalSessionState) sessionStates.get(i);
                Iterator it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (!splitInstallInternalSessionState.moduleNames().contains((String) it.next())) {
                        if (z) {
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
            }
        }
        return z;
    }

    public void removeSessionState(int i) {
        synchronized (this.mLock) {
            if (i != 0) {
                this.mActiveSessionStates.remove(i);
            }
        }
    }

    public void setSessionState(int i, SplitInstallInternalSessionState splitInstallInternalSessionState) {
        synchronized (this.mLock) {
            if (i != 0) {
                if (this.mActiveSessionStates.get(i) == null) {
                    this.mActiveSessionStates.put(i, splitInstallInternalSessionState);
                }
            }
        }
    }
}
