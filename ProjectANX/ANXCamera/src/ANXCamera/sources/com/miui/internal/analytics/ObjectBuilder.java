package com.miui.internal.analytics;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class ObjectBuilder {
    private static final String TAG = "ObjectBuilder";
    private ArrayList mBuilderInf = new ArrayList();

    class BuilderData {
        String mTag;
        Class mType;

        BuilderData(Class cls, String str) {
            this.mType = cls;
            this.mTag = str;
        }
    }

    public Object buildObject(String str) {
        String str2;
        String str3 = TAG;
        Iterator it = this.mBuilderInf.iterator();
        Object obj = null;
        while (it.hasNext()) {
            BuilderData builderData = (BuilderData) it.next();
            if (builderData.mTag.equals(str)) {
                try {
                    obj = builderData.mType.newInstance();
                } catch (InstantiationException e) {
                    e = e;
                    str2 = "InstantiationException catched when buildObject";
                    Log.e(str3, str2, e);
                } catch (IllegalAccessException e2) {
                    e = e2;
                    str2 = "IllegalAccessException catched when buildObject";
                    Log.e(str3, str2, e);
                }
            }
        }
        return obj;
    }

    public boolean registerClass(Class cls, String str) {
        Iterator it = this.mBuilderInf.iterator();
        while (it.hasNext()) {
            if (str.equals(((BuilderData) it.next()).mTag)) {
                return false;
            }
        }
        return this.mBuilderInf.add(new BuilderData(cls, str));
    }
}
