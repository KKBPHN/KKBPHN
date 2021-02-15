package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import java.util.List;

final class SplitDetails {
    private final List abiFilters;
    private final String appVersionName;
    private final String qigsawId;
    private final List splitEntryFragments;
    private final SplitInfoListing splitInfoListing;
    private final List updateSplits;

    SplitDetails(String str, String str2, List list, List list2, List list3, SplitInfoListing splitInfoListing2) {
        this.qigsawId = str;
        this.appVersionName = str2;
        this.abiFilters = list;
        this.updateSplits = list2;
        this.splitEntryFragments = list3;
        this.splitInfoListing = splitInfoListing2;
    }

    /* access modifiers changed from: 0000 */
    public List getAbiFilters() {
        return this.abiFilters;
    }

    /* access modifiers changed from: 0000 */
    public String getAppVersionName() {
        return this.appVersionName;
    }

    /* access modifiers changed from: 0000 */
    public String getQigsawId() {
        return "5.0.0.0";
    }

    /* access modifiers changed from: 0000 */
    public List getSplitEntryFragments() {
        return this.splitEntryFragments;
    }

    /* access modifiers changed from: 0000 */
    public SplitInfoListing getSplitInfoListing() {
        return this.splitInfoListing;
    }

    /* access modifiers changed from: 0000 */
    public List getUpdateSplits() {
        return this.updateSplits;
    }

    /* access modifiers changed from: 0000 */
    public boolean verifySplitInfoListing() {
        SplitInfoListing splitInfoListing2 = this.splitInfoListing;
        if (splitInfoListing2 == null || splitInfoListing2.getSplitInfoMap() == null) {
            return false;
        }
        boolean z = true;
        for (SplitInfo isValid : this.splitInfoListing.getSplitInfoMap().values()) {
            if (!isValid.isValid()) {
                z = false;
            }
        }
        return z;
    }
}
