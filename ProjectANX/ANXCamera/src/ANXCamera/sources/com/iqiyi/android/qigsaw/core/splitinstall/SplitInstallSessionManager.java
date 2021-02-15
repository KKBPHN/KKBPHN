package com.iqiyi.android.qigsaw.core.splitinstall;

import java.util.List;

interface SplitInstallSessionManager {
    void changeSessionState(int i, int i2);

    void emitSessionState(SplitInstallInternalSessionState splitInstallInternalSessionState);

    SplitInstallInternalSessionState getSessionState(int i);

    List getSessionStates();

    boolean isActiveSessionsLimitExceeded();

    boolean isIncompatibleWithExistingSession(List list);

    void removeSessionState(int i);

    void setSessionState(int i, SplitInstallInternalSessionState splitInstallInternalSessionState);
}
