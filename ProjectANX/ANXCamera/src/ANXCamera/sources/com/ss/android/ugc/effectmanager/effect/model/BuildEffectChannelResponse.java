package com.ss.android.ugc.effectmanager.effect.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildEffectChannelResponse {
    private String mFileDirectory;
    private boolean mIsCache;
    private String mPanel;

    public BuildEffectChannelResponse(String str, String str2, boolean z) {
        this.mPanel = str;
        this.mFileDirectory = str2;
        this.mIsCache = z;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r4v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fillEffectPath(List<Effect> list) {
        if (list != null && !list.isEmpty()) {
            for (Effect effect : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.mFileDirectory);
                sb.append(File.separator);
                sb.append(effect.getId());
                sb.append(".zip");
                effect.setZipPath(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mFileDirectory);
                sb2.append(File.separator);
                sb2.append(effect.getId());
                effect.setUnzipPath(sb2.toString());
            }
        }
    }

    private List getCategoryAllEffects(EffectCategoryModel effectCategoryModel, Map map) {
        ArrayList arrayList = new ArrayList();
        for (String str : effectCategoryModel.getEffects()) {
            Effect effect = (Effect) map.get(str);
            if (effect != null) {
                arrayList.add(effect);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r4v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getChildEffect(List<Effect> list, Map map) {
        for (Effect effect : list) {
            if (effect.getEffectType() == 1) {
                ArrayList arrayList = new ArrayList();
                for (String str : effect.getChildren()) {
                    Effect effect2 = (Effect) map.get(str);
                    if (effect2 != null) {
                        arrayList.add(effect2);
                    }
                }
                effect.setChildEffects(arrayList);
            }
        }
    }

    private Effect getEffect(String str, Map map) {
        return (Effect) map.get(str);
    }

    private List initCategory(EffectChannelModel effectChannelModel, Map map) {
        ArrayList arrayList = new ArrayList();
        if (!effectChannelModel.getCategory().isEmpty()) {
            for (EffectCategoryModel effectCategoryModel : effectChannelModel.getCategory()) {
                EffectCategoryResponse effectCategoryResponse = new EffectCategoryResponse();
                effectCategoryResponse.setId(effectCategoryModel.getId());
                effectCategoryResponse.setName(effectCategoryModel.getName());
                effectCategoryResponse.setKey(effectCategoryModel.getKey());
                if (!effectCategoryModel.getIcon().getUrl_list().isEmpty()) {
                    effectCategoryResponse.setIcon_normal_url((String) effectCategoryModel.getIcon().getUrl_list().get(0));
                }
                if (!effectCategoryModel.getIcon_selected().getUrl_list().isEmpty()) {
                    effectCategoryResponse.setIcon_selected_url((String) effectCategoryModel.getIcon_selected().getUrl_list().get(0));
                }
                effectCategoryResponse.setTotalEffects(getCategoryAllEffects(effectCategoryModel, map));
                effectCategoryResponse.setTags(effectCategoryModel.getTags());
                effectCategoryResponse.setTagsUpdateTime(effectCategoryModel.getTagsUpdateTime());
                effectCategoryResponse.setCollectionEffect(effectChannelModel.getCollection());
                arrayList.add(effectCategoryResponse);
            }
        }
        return arrayList;
    }

    public EffectChannelResponse buildChannelResponse(EffectChannelModel effectChannelModel) {
        System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (Effect effect : effectChannelModel.getEffects()) {
            hashMap.put(effect.getEffectId(), effect);
        }
        for (Effect effect2 : effectChannelModel.getCollection()) {
            hashMap2.put(effect2.getEffectId(), effect2);
        }
        EffectChannelResponse effectChannelResponse = new EffectChannelResponse();
        effectChannelResponse.setPanel(this.mPanel);
        effectChannelResponse.setVersion(effectChannelModel.getVersion());
        effectChannelResponse.setAllCategoryEffects(effectChannelModel.getEffects());
        effectChannelResponse.setCollections(effectChannelModel.getCollection());
        effectChannelResponse.setUrlPrefix(effectChannelModel.getUrlPrefix());
        effectChannelResponse.setCategoryResponseList(initCategory(effectChannelModel, hashMap));
        getChildEffect(effectChannelModel.getEffects(), hashMap2);
        effectChannelResponse.setPanelModel(effectChannelModel.getPanel());
        effectChannelResponse.setFrontEffect(getEffect(effectChannelModel.getFront_effect_id(), hashMap));
        effectChannelResponse.setRearEffect(getEffect(effectChannelModel.getRear_effect_id(), hashMap));
        if (!this.mIsCache) {
            fillEffectPath(effectChannelModel.getEffects());
            fillEffectPath(effectChannelModel.getCollection());
        }
        return effectChannelResponse;
    }
}
