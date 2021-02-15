package com.ss.android.ttve.monitor;

import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class TEMonitorFilterMgr {
    public static final int FILTER_TYPE_EFFECT = 0;
    public static final int FILTER_TYPE_INFO_STICKER = 1;
    public static final int REPEAT_EFFECT = 1;
    public static final int REVERSE_EFFECT = 3;
    public static final int SLOW_MOTION_EFFECT = 2;
    public static final int TIME_EFFECT_NONE = 0;
    private static TEMonitorFilterMgr sInstance;
    public Map effectMap = new HashMap();
    public Map infoStickerMap = new HashMap();
    private int timeEffectType = -1;

    public class TEMonitorFilter {
        public int duration;
        public String path;
        public int start;
    }

    public synchronized void addFilter(int i, int i2, TEMonitorFilter tEMonitorFilter) {
        Map map;
        Integer valueOf;
        if (i == 0) {
            map = this.effectMap;
            valueOf = Integer.valueOf(i2);
        } else if (i == 1) {
            map = this.infoStickerMap;
            valueOf = Integer.valueOf(i2);
        }
        map.put(valueOf, tEMonitorFilter);
    }

    public int getTimeEffectType() {
        return this.timeEffectType;
    }

    public synchronized boolean isEffectAdd() {
        return this.effectMap.isEmpty();
    }

    public synchronized boolean isInfoStickerAdd() {
        return this.infoStickerMap.isEmpty();
    }

    public synchronized void removeFilter(int i, int i2) {
        Map map;
        Integer valueOf;
        if (i == 0) {
            map = this.effectMap;
            valueOf = Integer.valueOf(i2);
        } else if (i == 1) {
            map = this.infoStickerMap;
            valueOf = Integer.valueOf(i2);
        }
        map.remove(valueOf);
    }

    public synchronized void reset() {
        this.effectMap.clear();
        this.infoStickerMap.clear();
        this.timeEffectType = -1;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:16|17|18|19|29|27|14|13) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0023 */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0021 A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0023 A[LOOP:0: B:13:0x0023->B:27:0x0023, LOOP_START, SYNTHETIC, Splitter:B:13:0x0023] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized String serializeMap(int i) {
        Iterator it;
        Set entrySet;
        JSONArray jSONArray = new JSONArray();
        if (i == 0) {
            entrySet = this.effectMap.entrySet();
        } else if (i == 1) {
            entrySet = this.infoStickerMap.entrySet();
        } else {
            it = null;
            if (it != null) {
                return null;
            }
            while (it.hasNext()) {
                TEMonitorFilter tEMonitorFilter = (TEMonitorFilter) ((Entry) it.next()).getValue();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(ComposerHelper.CONFIG_PATH, tEMonitorFilter.path);
                jSONObject.put("start", tEMonitorFilter.start);
                jSONObject.put("duration", tEMonitorFilter.duration);
                jSONArray.put(jSONObject);
            }
            return jSONArray.toString();
        }
        it = entrySet.iterator();
        if (it != null) {
        }
    }

    public void setTimeEffectType(int i) {
        this.timeEffectType = i;
    }
}
