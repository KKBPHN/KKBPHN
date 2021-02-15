package com.ss.android.ugc.effectmanager.effect.task.task;

import android.os.Handler;
import android.text.TextUtils;
import com.ss.android.ugc.effectmanager.common.listener.ICache;
import com.ss.android.ugc.effectmanager.common.listener.IJsonConverter;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import com.ss.android.ugc.effectmanager.common.task.NormalTask;
import com.ss.android.ugc.effectmanager.common.utils.EffectCacheKeyGenerator;
import com.ss.android.ugc.effectmanager.context.EffectContext;
import com.ss.android.ugc.effectmanager.effect.model.Effect;
import com.ss.android.ugc.effectmanager.effect.model.EffectCategoryModel;
import com.ss.android.ugc.effectmanager.effect.model.EffectCategoryResponse;
import com.ss.android.ugc.effectmanager.effect.model.EffectChannelModel;
import com.ss.android.ugc.effectmanager.effect.model.EffectChannelResponse;
import com.ss.android.ugc.effectmanager.effect.task.result.EffectChannelTaskResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FetchExistEffectListTask extends NormalTask {
    private List allDownloadedCategoryEffects;
    private ICache mCache = this.mEffectContext.getEffectConfiguration().getCache();
    private EffectContext mEffectContext;
    private IJsonConverter mJsonConverter = this.mEffectContext.getEffectConfiguration().getJsonConverter();
    private String panel;

    public FetchExistEffectListTask(String str, String str2, EffectContext effectContext, Handler handler) {
        super(handler, str2);
        this.panel = str;
        this.mEffectContext = effectContext;
    }

    private EffectChannelModel getCachedChannelModel() {
        InputStream queryToStream = this.mCache.queryToStream(EffectCacheKeyGenerator.generatePanelKey(this.mEffectContext.getEffectConfiguration().getChannel(), this.panel));
        return queryToStream != null ? (EffectChannelModel) this.mJsonConverter.convertJsonToObj(queryToStream, EffectChannelModel.class) : new EffectChannelModel();
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r6v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List getCategoryAllEffects(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            for (Effect effect : this.allDownloadedCategoryEffects) {
                if (TextUtils.equals(str, effect.getEffectId())) {
                    arrayList.add(effect);
                }
            }
        }
        return arrayList;
    }

    private List getCategoryEffectResponse(EffectChannelModel effectChannelModel) {
        List<EffectCategoryModel> category = effectChannelModel.getCategory();
        ArrayList arrayList = new ArrayList();
        for (EffectCategoryModel effectCategoryModel : category) {
            if (effectCategoryModel.checkValued()) {
                EffectCategoryResponse effectCategoryResponse = new EffectCategoryResponse(effectCategoryModel.getId(), effectCategoryModel.getName(), effectCategoryModel.getKey(), getCategoryAllEffects(effectCategoryModel.getEffects()), effectCategoryModel.getTags(), effectCategoryModel.getTagsUpdateTime());
                effectCategoryResponse.setCollectionEffect(effectChannelModel.getCollection());
                arrayList.add(effectCategoryResponse);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r5v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List getDownloadedEffectList(List<Effect> list) {
        ArrayList arrayList = new ArrayList();
        for (Effect effect : list) {
            if (this.mCache.has(effect.getId())) {
                arrayList.add(effect);
            }
        }
        return arrayList;
    }

    public void execute() {
        if (TextUtils.isEmpty(this.panel)) {
            sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), new ExceptionResult(10007)));
            return;
        }
        EffectChannelResponse effectChannelResponse = new EffectChannelResponse();
        EffectChannelModel cachedChannelModel = getCachedChannelModel();
        if (cachedChannelModel == null) {
            sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), new ExceptionResult(10004)));
        } else if (!cachedChannelModel.checkValued()) {
            sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), null));
        } else {
            this.allDownloadedCategoryEffects = getDownloadedEffectList(cachedChannelModel.getEffects());
            if (this.allDownloadedCategoryEffects.isEmpty()) {
                sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), null));
                return;
            }
            effectChannelResponse.setAllCategoryEffects(this.allDownloadedCategoryEffects);
            effectChannelResponse.setCategoryResponseList(getCategoryEffectResponse(cachedChannelModel));
            effectChannelResponse.setPanel(this.panel);
            effectChannelResponse.setPanelModel(cachedChannelModel.getPanel());
            sendMessage(14, new EffectChannelTaskResult(effectChannelResponse, null));
        }
    }
}
