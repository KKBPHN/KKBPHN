package com.ss.android.ugc.effectmanager.effect.model;

import java.util.ArrayList;
import java.util.List;

public class EffectChannelResponse {
    private List allCategoryEffects;
    private List categoryResponseList;
    private List collections;
    private Effect frontEffect;
    private String panel;
    private EffectPanelModel panelModel;
    private Effect rearEffect;
    private List urlPrefix;
    private String version;

    public EffectChannelResponse() {
        this.allCategoryEffects = new ArrayList();
        this.categoryResponseList = new ArrayList();
    }

    public EffectChannelResponse(String str) {
        this.panel = str;
        this.allCategoryEffects = new ArrayList();
        this.categoryResponseList = new ArrayList();
    }

    public EffectChannelResponse(String str, List list, List list2) {
        this.version = str;
        this.allCategoryEffects = list;
        this.categoryResponseList = list2;
    }

    public List getAllCategoryEffects() {
        return this.allCategoryEffects;
    }

    public List getCategoryResponseList() {
        return this.categoryResponseList;
    }

    public List getCollections() {
        return this.collections;
    }

    public Effect getFrontEffect() {
        return this.frontEffect;
    }

    public String getPanel() {
        return this.panel;
    }

    public EffectPanelModel getPanelModel() {
        if (this.panelModel == null) {
            this.panelModel = new EffectPanelModel();
        }
        this.panelModel.setId(this.panel);
        return this.panelModel;
    }

    public Effect getRearEffect() {
        return this.rearEffect;
    }

    public List getUrlPrefix() {
        return this.urlPrefix;
    }

    public String getVersion() {
        return this.version;
    }

    public void setAllCategoryEffects(List list) {
        this.allCategoryEffects = list;
    }

    public void setCategoryResponseList(List list) {
        this.categoryResponseList = list;
    }

    public void setCollections(List list) {
        this.collections = list;
    }

    public void setFrontEffect(Effect effect) {
        this.frontEffect = effect;
    }

    public void setPanel(String str) {
        this.panel = str;
    }

    public void setPanelModel(EffectPanelModel effectPanelModel) {
        if (effectPanelModel == null) {
            effectPanelModel = new EffectPanelModel();
        }
        this.panelModel = effectPanelModel;
    }

    public void setRearEffect(Effect effect) {
        this.rearEffect = effect;
    }

    public void setUrlPrefix(List list) {
        this.urlPrefix = list;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
