package com.miui.internal.analytics;

import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class PolicyHelper {
    private static final String SPLITTER = ":";
    private static final String TAG = "PolicyHelper";
    private ObjectBuilder mObjBuilder = new ObjectBuilder();
    private Map mPolicy = new HashMap();

    public PolicyHelper() {
        this.mObjBuilder.registerClass(LastPolicy.class, LastPolicy.TAG);
        this.mObjBuilder.registerClass(NormalPolicy.class, "normal");
        this.mObjBuilder.registerClass(CountPolicy.class, "count");
    }

    public void clear() {
        this.mPolicy.clear();
    }

    public void end() {
        for (String str : this.mPolicy.keySet()) {
            ((Policy) this.mPolicy.get(str)).end();
        }
    }

    public Policy getPolicy(String str) {
        Policy policy = null;
        if (TextUtils.isEmpty(str)) {
            return policy;
        }
        try {
            String[] split = str.split(SPLITTER);
            if (split.length <= 0) {
                return policy;
            }
            Policy policy2 = (Policy) this.mPolicy.get(split[0]);
            try {
                String str2 = split.length > 1 ? split[1] : "";
                if (policy2 == null) {
                    Policy policy3 = (Policy) this.mObjBuilder.buildObject(str);
                    if (policy3 == null) {
                        return policy3;
                    }
                    this.mPolicy.put(str, policy3);
                    policy3.setParam(str2);
                    policy3.prepare();
                    return policy3;
                }
                policy2.setParam(str2);
                return policy2;
            } catch (PatternSyntaxException e) {
                e = e;
                policy = policy2;
                Log.e(TAG, "PatternSyntaxException catched when getPolicy", e);
                return policy;
            }
        } catch (PatternSyntaxException e2) {
            e = e2;
        }
    }
}
