package com.android.camera.aiwatermark.data;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.aiwatermark.parser.AIWatermarkParser;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractWatermarkData {
    private static final String TAG = "AbstractWatermarkData";
    private static ArrayList mAllWatermarks = new ArrayList();
    private static volatile Object mLock = new Object();

    static {
        AIWatermarkParser aIWatermarkParser = new AIWatermarkParser();
        Log.d(TAG, "parser start");
        synchronized (AbstractWatermarkData.class) {
            if (C0122O00000o.instance().OOOoooo() || C0122O00000o.instance().OOOOo0o()) {
                mAllWatermarks.addAll(aIWatermarkParser.parseByPattern(2));
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mAllWatermarks.size()=");
                sb.append(mAllWatermarks.size());
                Log.d(str, sb.toString());
            }
            AbstractWatermarkData.class.notifyAll();
        }
        Log.d(TAG, "parser end");
    }

    public abstract ArrayList getForAI();

    public abstract ArrayList getForManual();

    /* JADX WARNING: Can't wrap try/catch for region: R(5:4|5|(3:7|8|9)|10|11) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ArrayList getWatermarkByType(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getWatermarkByType type = ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (mLock == null) {
            synchronized (AbstractWatermarkData.class) {
                if (mLock == null) {
                    AbstractWatermarkData.class.wait();
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = mAllWatermarks.iterator();
        while (it.hasNext()) {
            WatermarkItem watermarkItem = (WatermarkItem) it.next();
            if (watermarkItem.getType() == i) {
                arrayList.add(watermarkItem);
            }
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getWatermarkByType list.size() = ");
        sb2.append(arrayList.size());
        Log.d(str2, sb2.toString());
        return arrayList;
    }
}
