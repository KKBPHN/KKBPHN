package com.ss.android.ugc.effectmanager.effect.task.task;

import android.os.Handler;
import android.text.TextUtils;
import com.ss.android.ugc.effectmanager.EffectConfiguration;
import com.ss.android.ugc.effectmanager.common.EffectConstants;
import com.ss.android.ugc.effectmanager.common.EffectRequest;
import com.ss.android.ugc.effectmanager.common.exception.StatusCodeException;
import com.ss.android.ugc.effectmanager.common.listener.ICache;
import com.ss.android.ugc.effectmanager.common.listener.IJsonConverter;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;
import com.ss.android.ugc.effectmanager.common.task.NormalTask;
import com.ss.android.ugc.effectmanager.common.utils.EffectCacheKeyGenerator;
import com.ss.android.ugc.effectmanager.common.utils.EffectUtils;
import com.ss.android.ugc.effectmanager.common.utils.NetworkUtils;
import com.ss.android.ugc.effectmanager.context.EffectContext;
import com.ss.android.ugc.effectmanager.effect.model.BuildEffectChannelResponse;
import com.ss.android.ugc.effectmanager.effect.model.Effect;
import com.ss.android.ugc.effectmanager.effect.model.EffectCategoryModel;
import com.ss.android.ugc.effectmanager.effect.model.EffectCategoryResponse;
import com.ss.android.ugc.effectmanager.effect.model.EffectChannelModel;
import com.ss.android.ugc.effectmanager.effect.model.EffectChannelResponse;
import com.ss.android.ugc.effectmanager.effect.model.net.EffectNetListResponse;
import com.ss.android.ugc.effectmanager.effect.task.result.EffectChannelTaskResult;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchEffectChannelTask extends NormalTask {
    private static final String TAG = "SDK_FETCH_LIST";
    private EffectConfiguration mConfiguration = this.mEffectContext.getEffectConfiguration();
    private int mCurCnt = this.mConfiguration.getRetryCount();
    private EffectContext mEffectContext;
    private ICache mFileCache = this.mConfiguration.getCache();
    private IJsonConverter mJsonConverter = this.mConfiguration.getJsonConverter();
    private String mRemoteIp;
    private String mRequestedUrl;
    private String mSelectedHost;
    private String panel;

    public FetchEffectChannelTask(EffectContext effectContext, String str, String str2, Handler handler) {
        super(handler, str2, EffectConstants.NETWORK);
        this.panel = str;
        this.mEffectContext = effectContext;
    }

    private EffectRequest buildEffectListRequest() {
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(this.mConfiguration.getAccessKey())) {
            hashMap.put(EffectConfiguration.KEY_ACCESS_KEY, this.mConfiguration.getAccessKey());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getAppVersion())) {
            hashMap.put("app_version", this.mConfiguration.getAppVersion());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getSdkVersion())) {
            hashMap.put(EffectConfiguration.KEY_SDK_VERSION, this.mConfiguration.getSdkVersion());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getChannel())) {
            hashMap.put("channel", this.mConfiguration.getChannel());
        }
        if (!TextUtils.isEmpty(this.panel)) {
            hashMap.put(EffectConfiguration.KEY_PANEL, this.panel);
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getPlatform())) {
            hashMap.put(EffectConfiguration.KEY_DEVICE_PLATFORM, this.mConfiguration.getPlatform());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getDeviceId())) {
            hashMap.put("device_id", this.mConfiguration.getDeviceId());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getRegion())) {
            hashMap.put("region", this.mConfiguration.getRegion());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getDeviceType())) {
            hashMap.put(EffectConfiguration.KEY_DEVICE_TYPE, this.mConfiguration.getDeviceType());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getAppID())) {
            hashMap.put(EffectConfiguration.KEY_APP_ID, this.mConfiguration.getAppID());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getAppLanguage())) {
            hashMap.put(EffectConfiguration.KEY_APP_LANGUAGE, this.mConfiguration.getAppLanguage());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getSysLanguage())) {
            hashMap.put("language", this.mConfiguration.getSysLanguage());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getLongitude())) {
            hashMap.put("longitude", this.mConfiguration.getLongitude());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getLatitude())) {
            hashMap.put("latitude", this.mConfiguration.getLatitude());
        }
        if (!TextUtils.isEmpty(this.mConfiguration.getCityCode())) {
            hashMap.put(EffectConfiguration.KEY_CITY_CODE, this.mConfiguration.getCityCode());
        }
        this.mSelectedHost = this.mEffectContext.getLinkSelector().getBestHostUrl();
        StringBuilder sb = new StringBuilder();
        sb.append(this.mSelectedHost);
        sb.append(this.mConfiguration.getApiAdress());
        sb.append(EffectConstants.ROUTE_EFFECT_LIST);
        String buildRequestUrl = NetworkUtils.buildRequestUrl(hashMap, sb.toString());
        this.mRequestedUrl = buildRequestUrl;
        try {
            this.mRemoteIp = InetAddress.getByName(new URL(buildRequestUrl).getHost()).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        return new EffectRequest("GET", buildRequestUrl);
    }

    private EffectChannelResponse dealResponse(EffectChannelModel effectChannelModel) {
        EffectChannelResponse effectChannelResponse = new EffectChannelResponse();
        effectChannelResponse.setPanel(this.panel);
        effectChannelResponse.setVersion(effectChannelModel.getVersion());
        effectChannelResponse.setAllCategoryEffects(effectChannelModel.getEffects());
        effectChannelResponse.setCollections(effectChannelModel.getCollection());
        effectChannelResponse.setCategoryResponseList(initCategory(effectChannelModel));
        effectChannelResponse.setPanelModel(effectChannelModel.getPanel());
        effectChannelResponse.setFrontEffect(getEffect(effectChannelModel.getFront_effect_id(), effectChannelModel));
        effectChannelResponse.setRearEffect(getEffect(effectChannelModel.getRear_effect_id(), effectChannelModel));
        fillEffectPath(effectChannelModel.getEffects());
        fillEffectPath(effectChannelModel.getCollection());
        saveEffectList(effectChannelModel);
        return effectChannelResponse;
    }

    private void deleteEffect(Effect effect) {
        if (EffectUtils.isEffectValid(effect)) {
            this.mFileCache.remove(effect.getId());
            ICache iCache = this.mFileCache;
            StringBuilder sb = new StringBuilder();
            sb.append(effect.getId());
            sb.append(".zip");
            iCache.remove(sb.toString());
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r4v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fillEffectPath(List<Effect> list) {
        if (list != null && !list.isEmpty()) {
            for (Effect effect : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.mConfiguration.getEffectDir());
                sb.append(File.separator);
                sb.append(effect.getId());
                sb.append(".zip");
                effect.setZipPath(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mConfiguration.getEffectDir());
                sb2.append(File.separator);
                sb2.append(effect.getId());
                effect.setUnzipPath(sb2.toString());
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r6v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r7v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List getCategoryAddedEffects(List<Effect> list, List<Effect> list2) {
        if (list == null || list.isEmpty()) {
            return list2;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            boolean z = true;
            for (Effect effect : list2) {
                for (Effect equals : list) {
                    if (effect.equals(equals)) {
                        z = false;
                    }
                }
                if (z) {
                    arrayList.add(effect);
                }
            }
            return arrayList;
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

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r6v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>, for r7v0, types: [java.util.List, java.util.List<com.ss.android.ugc.effectmanager.effect.model.Effect>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List getCategoryDeletedEffects(List<Effect> list, List<Effect> list2) {
        ArrayList arrayList = new ArrayList();
        if (list != null && !list.isEmpty()) {
            loop0:
            while (true) {
                boolean z = true;
                for (Effect effect : list) {
                    for (Effect equals : list2) {
                        if (effect.equals(equals)) {
                            z = false;
                        }
                    }
                    if (z) {
                        arrayList.add(effect);
                    }
                }
                break loop0;
            }
        }
        return arrayList;
    }

    private Effect getEffect(String str, EffectChannelModel effectChannelModel) {
        Effect effect = null;
        for (Effect effect2 : effectChannelModel.getEffects()) {
            if (TextUtils.equals(str, effect2.getEffectId())) {
                effect = effect2;
            }
        }
        return effect;
    }

    private List initCategory(EffectChannelModel effectChannelModel) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (Effect effect : effectChannelModel.getEffects()) {
            hashMap.put(effect.getEffectId(), effect);
        }
        if (!effectChannelModel.getCategory().isEmpty()) {
            for (EffectCategoryModel effectCategoryModel : effectChannelModel.getCategory()) {
                EffectCategoryResponse effectCategoryResponse = new EffectCategoryResponse();
                effectCategoryResponse.setId(effectCategoryModel.getId());
                effectCategoryResponse.setName(effectCategoryModel.getName());
                if (!effectCategoryModel.getIcon().getUrl_list().isEmpty()) {
                    effectCategoryResponse.setIcon_normal_url((String) effectCategoryModel.getIcon().getUrl_list().get(0));
                }
                if (!effectCategoryModel.getIcon_selected().getUrl_list().isEmpty()) {
                    effectCategoryResponse.setIcon_selected_url((String) effectCategoryModel.getIcon_selected().getUrl_list().get(0));
                }
                effectCategoryResponse.setTotalEffects(getCategoryAllEffects(effectCategoryModel, hashMap));
                effectCategoryResponse.setTags(effectCategoryModel.getTags());
                effectCategoryResponse.setTagsUpdateTime(effectCategoryModel.getTagsUpdateTime());
                effectCategoryResponse.setCollectionEffect(effectChannelModel.getCollection());
                arrayList.add(effectCategoryResponse);
            }
        }
        return arrayList;
    }

    private void saveEffectList(EffectChannelModel effectChannelModel) {
        this.mFileCache.save(EffectCacheKeyGenerator.generatePanelKey(this.mConfiguration.getChannel(), this.panel), this.mJsonConverter.convertObjToJson(effectChannelModel));
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("version", effectChannelModel.getVersion());
            ICache iCache = this.mFileCache;
            StringBuilder sb = new StringBuilder();
            sb.append("effect_version");
            sb.append(this.panel);
            iCache.save(sb.toString(), jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0030, code lost:
        sendMessage(14, r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute() {
        EffectChannelTaskResult effectChannelTaskResult;
        EffectRequest buildEffectListRequest = buildEffectListRequest();
        while (true) {
            int i = this.mCurCnt;
            this.mCurCnt = i - 1;
            if (i != 0) {
                try {
                    if (isCanceled()) {
                        ExceptionResult exceptionResult = new ExceptionResult(10001);
                        exceptionResult.setTrackParams(this.mRequestedUrl, this.mSelectedHost, this.mRemoteIp);
                        effectChannelTaskResult = new EffectChannelTaskResult(new EffectChannelResponse(this.panel), exceptionResult);
                        break;
                    }
                    EffectNetListResponse effectNetListResponse = (EffectNetListResponse) this.mConfiguration.getEffectNetWorker().execute(buildEffectListRequest, this.mJsonConverter, EffectNetListResponse.class);
                    if (effectNetListResponse.checkValued()) {
                        EffectChannelModel data = effectNetListResponse.getData();
                        EffectChannelResponse buildChannelResponse = new BuildEffectChannelResponse(this.panel, this.mEffectContext.getEffectConfiguration().getEffectDir().getAbsolutePath(), false).buildChannelResponse(data);
                        saveEffectList(data);
                        sendMessage(14, new EffectChannelTaskResult(buildChannelResponse, null));
                        return;
                    } else if (this.mCurCnt == 0) {
                        ExceptionResult exceptionResult2 = new ExceptionResult(10002);
                        exceptionResult2.setTrackParams(this.mRequestedUrl, this.mSelectedHost, this.mRemoteIp);
                        effectChannelTaskResult = new EffectChannelTaskResult(new EffectChannelResponse(this.panel), exceptionResult2);
                        break;
                    }
                } catch (Exception e) {
                    if (this.mCurCnt == 0 || (e instanceof StatusCodeException)) {
                        sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), new ExceptionResult(e)));
                    }
                }
            } else {
                return;
            }
        }
        sendMessage(14, new EffectChannelTaskResult(new EffectChannelResponse(this.panel), new ExceptionResult(e)));
    }
}
