package com.android.camera.aiwatermark.data;

import com.android.camera.aiwatermark.util.WatermarkConstant;
import java.util.ArrayList;

public class FestivalWatermark extends AbstractWatermarkData {
    private ArrayList mChinaTraditionlist = new ArrayList();
    private ArrayList mInternationallist = new ArrayList();

    private ArrayList getChinaTraditionWM() {
        if (this.mChinaTraditionlist.isEmpty()) {
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.SPRING_FESTIVAL, 2, 1, 12));
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.DRAGON_BOAT_FESTIVAL, 2, 1, 12));
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.MID_AUTUMN_FESTIVAL, 2, 1, 12));
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.CHINESE_EVE, 2, 1, 12));
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.CHINESE_VALENTINE_DAY, 2, 1, 12));
            this.mChinaTraditionlist.add(new WatermarkItem(WatermarkConstant.LABA_FESTIVAL, 2, 1, 12));
        }
        return this.mChinaTraditionlist;
    }

    private ArrayList getInternationalFestivalWM() {
        if (this.mInternationallist.isEmpty()) {
            this.mInternationallist.add(new WatermarkItem(WatermarkConstant.NEW_YEAR_DAY, 2, 1, 12));
            this.mInternationallist.add(new WatermarkItem(WatermarkConstant.VALENTINE_DAY, 2, 1, 12));
            this.mInternationallist.add(new WatermarkItem(WatermarkConstant.CHILDREN_DAY, 2, 1, 12));
            this.mInternationallist.add(new WatermarkItem(WatermarkConstant.CHRISTMAS_DAY, 2, 1, 12));
        }
        return this.mInternationallist;
    }

    public ArrayList getFestivalWatermark(int i) {
        return i != 0 ? i != 1 ? new ArrayList() : getChinaTraditionWM() : getInternationalFestivalWM();
    }

    public ArrayList getForAI() {
        return getWatermarkByType(6);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(2);
    }
}
