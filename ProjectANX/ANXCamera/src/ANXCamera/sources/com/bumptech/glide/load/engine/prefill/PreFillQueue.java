package com.bumptech.glide.load.engine.prefill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PreFillQueue {
    private final Map bitmapsPerType;
    private int bitmapsRemaining;
    private int keyIndex;
    private final List keyList;

    public PreFillQueue(Map map) {
        this.bitmapsPerType = map;
        this.keyList = new ArrayList(map.keySet());
        for (Integer intValue : map.values()) {
            this.bitmapsRemaining += intValue.intValue();
        }
    }

    public int getSize() {
        return this.bitmapsRemaining;
    }

    public boolean isEmpty() {
        return this.bitmapsRemaining == 0;
    }

    public PreFillType remove() {
        PreFillType preFillType = (PreFillType) this.keyList.get(this.keyIndex);
        Integer num = (Integer) this.bitmapsPerType.get(preFillType);
        if (num.intValue() == 1) {
            this.bitmapsPerType.remove(preFillType);
            this.keyList.remove(this.keyIndex);
        } else {
            this.bitmapsPerType.put(preFillType, Integer.valueOf(num.intValue() - 1));
        }
        this.bitmapsRemaining--;
        this.keyIndex = this.keyList.isEmpty() ? 0 : (this.keyIndex + 1) % this.keyList.size();
        return preFillType;
    }
}
