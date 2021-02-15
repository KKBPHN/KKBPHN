package com.xiaomi.idm.task;

import com.xiaomi.idm.api.IDMApi;
import java.util.HashMap;

public class CallManager {
    private HashMap mCalls;
    private IDMApi mIDMApi;

    CallManager(IDMApi iDMApi) {
        this.mIDMApi = iDMApi;
    }
}
