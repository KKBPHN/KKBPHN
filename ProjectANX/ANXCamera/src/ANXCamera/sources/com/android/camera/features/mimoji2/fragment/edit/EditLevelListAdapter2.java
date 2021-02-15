package com.android.camera.features.mimoji2.fragment.edit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.features.mimoji2.bean.MimojiLevelBean2;
import com.android.camera.features.mimoji2.utils.ClickCheck2;
import com.android.camera.features.mimoji2.widget.baseview.BaseNoScrollGridLayoutManager;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.log.Log;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;
import com.arcsoft.avatar2.util.AvatarConfigUtils;
import com.arcsoft.avatar2.util.LOG;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditLevelListAdapter2 extends Adapter {
    private static final int LIST_COLUMN_NUM = 3;
    /* access modifiers changed from: private */
    public static final String TAG = "EditLevelListAdapter2";
    private AtomicBoolean isColorNeedNotify = new AtomicBoolean(true);
    private AvatarConfigItemClick2 mAvatarConfigItemClick2 = new AvatarConfigItemClick2() {
        public void onConfigItemClick(ASAvatarConfigInfo aSAvatarConfigInfo, boolean z, int i) {
            if (aSAvatarConfigInfo == null) {
                Log.d(EditLevelListAdapter2.TAG, "onConfigItemClick, ASAvatarConfigInfo is null");
                return;
            }
            String access$000 = EditLevelListAdapter2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConfigItemClick, ASAvatarConfigInfo = ");
            sb.append(aSAvatarConfigInfo);
            Log.d(access$000, sb.toString());
            EditLevelListAdapter2.this.mItfGvOnItemClickListener.notifyUIChanged();
            AvatarEngineManager2.getInstance().setAllNeedUpdate(true, z);
            AvatarEngineManager2.getInstance().addAvatarConfig(aSAvatarConfigInfo);
            AvatarConfigUtils.updateConfigID(aSAvatarConfigInfo.configType, aSAvatarConfigInfo.configID, AvatarEngineManager2.getInstance().getASAvatarConfigValue());
            EditLevelListAdapter2.this.mRenderThread.setConfig(aSAvatarConfigInfo);
            if (z) {
                boolean isRendering = EditLevelListAdapter2.this.mRenderThread.getIsRendering();
                MimojiThumbnailRenderThread2 access$200 = EditLevelListAdapter2.this.mRenderThread;
                if (!isRendering) {
                    access$200.draw(false);
                } else {
                    access$200.setStopRender(true);
                }
            }
        }
    };
    private Context mContext;
    /* access modifiers changed from: private */
    public ItfGvOnItemClickListener mItfGvOnItemClickListener;
    private CopyOnWriteArrayList mLevelDatas;
    private MimojiLevelBean2 mMimojiLevelBean2;
    private List mMimojiThumbnailAdapters;
    /* access modifiers changed from: private */
    public MimojiThumbnailRenderThread2 mRenderThread;

    public interface ItfGvOnItemClickListener {
        void notifyUIChanged();
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        RecyclerView mColorRecycleView;
        RecyclerView mThumbnailGV;
        TextView mTvSubTitle;

        public ViewHolder(View view) {
            super(view);
            this.mTvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
            this.mColorRecycleView = (RecyclerView) view.findViewById(R.id.color_select);
            this.mColorRecycleView.setFocusable(false);
            this.mThumbnailGV = (RecyclerView) view.findViewById(R.id.thumbnail_gride_view);
            this.mThumbnailGV.setFocusable(false);
        }
    }

    EditLevelListAdapter2(Context context, ItfGvOnItemClickListener itfGvOnItemClickListener) {
        this.mContext = context;
        this.mLevelDatas = new CopyOnWriteArrayList();
        this.mMimojiThumbnailAdapters = Collections.synchronizedList(new ArrayList());
        this.mItfGvOnItemClickListener = itfGvOnItemClickListener;
    }

    private void onGvItemClick(MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2, int i, int i2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("outerPosition = ");
        sb.append(i);
        sb.append(" Select index = ");
        sb.append(i2);
        Log.u(str, sb.toString());
        CopyOnWriteArrayList copyOnWriteArrayList = this.mLevelDatas;
        if (copyOnWriteArrayList == null || i < 0 || i >= copyOnWriteArrayList.size()) {
            Log.e(TAG, "gv mLevelDatas error");
            return;
        }
        MimojiLevelBean2 mimojiLevelBean2 = (MimojiLevelBean2) this.mLevelDatas.get(i);
        if (i2 >= 0 && i2 < mimojiLevelBean2.mThumnails.size()) {
            ASAvatarConfigInfo aSAvatarConfigInfo = (ASAvatarConfigInfo) mimojiLevelBean2.mThumnails.get(i2);
            AvatarEngineManager2.getInstance().setInnerConfigSelectIndex(mimojiLevelBean2.mConfigType, (float) i2);
            if (aSAvatarConfigInfo != null) {
                updateSelectView(mimojiThumbnailRecyclerAdapter2, i, i2);
                this.mAvatarConfigItemClick2.onConfigItemClick(aSAvatarConfigInfo, false, i);
                return;
            }
            Log.e(TAG, "onGvItemClick AvatarConfig.ASAvatarConfigInfo is null");
            mimojiThumbnailRecyclerAdapter2.notifyDataSetChanged();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void showColor(ViewHolder viewHolder, MimojiLevelBean2 mimojiLevelBean2, int i) {
        LinearLayoutManagerWrapper colorLayoutManagerMap;
        int i2;
        RecyclerView recyclerView = viewHolder.mColorRecycleView;
        TextView textView = viewHolder.mTvSubTitle;
        int itemCount = getItemCount();
        AvatarEngineManager2.getInstance();
        if (AvatarEngineManager2.showConfigTypeName(mimojiLevelBean2.mConfigType)) {
            textView.setVisibility(0);
            textView.setText(this.mMimojiLevelBean2.mConfigTypeName);
        } else {
            textView.setVisibility(8);
        }
        ArrayList colorConfigInfos = mimojiLevelBean2.getColorConfigInfos();
        if (colorConfigInfos != null && AvatarEngineManager2.getInstance().getColorType(mimojiLevelBean2.mConfigType) >= 0) {
            if (colorConfigInfos.size() != 0) {
                recyclerView.setVisibility(0);
                if ((!this.isColorNeedNotify.get() || recyclerView.getChildCount() == 0) && recyclerView.getVisibility() == 0) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("fmoji topic:");
                    sb.append(mimojiLevelBean2.mConfigTypeName);
                    sb.append("----");
                    sb.append(mimojiLevelBean2.mConfigType);
                    sb.append("----");
                    AvatarEngineManager2.getInstance();
                    sb.append(AvatarEngineManager2.showConfigTypeName(mimojiLevelBean2.mConfigType));
                    Log.i(str, sb.toString());
                    colorLayoutManagerMap = AvatarEngineManager2.getInstance().getColorLayoutManagerMap(recyclerView.hashCode() + i);
                    if (recyclerView.getLayoutManager() == null || colorLayoutManagerMap == null) {
                        if (colorLayoutManagerMap == null) {
                            colorLayoutManagerMap = new LinearLayoutManagerWrapper(this.mContext, "color_select");
                            colorLayoutManagerMap.setOrientation(0);
                            AvatarEngineManager2.getInstance().putColorLayoutManagerMap(recyclerView.hashCode() + i, colorLayoutManagerMap);
                        }
                        recyclerView.setLayoutManager(colorLayoutManagerMap);
                    }
                    ColorListAdapter2 colorListAdapter2 = new ColorListAdapter2(this.mContext, this.mAvatarConfigItemClick2, colorLayoutManagerMap);
                    recyclerView.setAdapter(colorListAdapter2);
                    colorListAdapter2.setData(colorConfigInfos);
                    float innerConfigSelectIndex = AvatarEngineManager2.getInstance().getInnerConfigSelectIndex(((ASAvatarConfigInfo) colorConfigInfos.get(0)).configType);
                    int i3 = this.mContext.getResources().getDisplayMetrics().widthPixels;
                    int i4 = 0;
                    for (i2 = 0; i2 < colorConfigInfos.size(); i2++) {
                        if (innerConfigSelectIndex == ((float) ((ASAvatarConfigInfo) colorConfigInfos.get(i2)).configID)) {
                            i4 = i2;
                        }
                    }
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("fmoji show color :");
                    sb2.append(mimojiLevelBean2.mConfigTypeName);
                    sb2.append("color size:");
                    sb2.append(colorConfigInfos.size());
                    sb2.append(" colorSelectPositon : ");
                    sb2.append(i4);
                    sb2.append("   curHolderPosition : ");
                    sb2.append(i);
                    Log.i(str2, sb2.toString());
                    colorLayoutManagerMap.scrollToPositionWithOffset(i4, i3 / 2);
                    if (this.isColorNeedNotify.get() && i >= itemCount - 1) {
                        this.isColorNeedNotify.set(false);
                    }
                } else {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("fmoji show color isColorNeedNotify : ");
                    sb3.append(this.isColorNeedNotify.get());
                    LOG.d(str3, sb3.toString());
                }
            }
        }
        recyclerView.setVisibility(8);
        if (!this.isColorNeedNotify.get()) {
        }
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("fmoji topic:");
        sb4.append(mimojiLevelBean2.mConfigTypeName);
        sb4.append("----");
        sb4.append(mimojiLevelBean2.mConfigType);
        sb4.append("----");
        AvatarEngineManager2.getInstance();
        sb4.append(AvatarEngineManager2.showConfigTypeName(mimojiLevelBean2.mConfigType));
        Log.i(str4, sb4.toString());
        colorLayoutManagerMap = AvatarEngineManager2.getInstance().getColorLayoutManagerMap(recyclerView.hashCode() + i);
        if (colorLayoutManagerMap == null) {
        }
        recyclerView.setLayoutManager(colorLayoutManagerMap);
        ColorListAdapter2 colorListAdapter22 = new ColorListAdapter2(this.mContext, this.mAvatarConfigItemClick2, colorLayoutManagerMap);
        recyclerView.setAdapter(colorListAdapter22);
        colorListAdapter22.setData(colorConfigInfos);
        float innerConfigSelectIndex2 = AvatarEngineManager2.getInstance().getInnerConfigSelectIndex(((ASAvatarConfigInfo) colorConfigInfos.get(0)).configType);
        int i32 = this.mContext.getResources().getDisplayMetrics().widthPixels;
        int i42 = 0;
        while (i2 < colorConfigInfos.size()) {
        }
        String str22 = TAG;
        StringBuilder sb22 = new StringBuilder();
        sb22.append("fmoji show color :");
        sb22.append(mimojiLevelBean2.mConfigTypeName);
        sb22.append("color size:");
        sb22.append(colorConfigInfos.size());
        sb22.append(" colorSelectPositon : ");
        sb22.append(i42);
        sb22.append("   curHolderPosition : ");
        sb22.append(i);
        Log.i(str22, sb22.toString());
        colorLayoutManagerMap.scrollToPositionWithOffset(i42, i32 / 2);
        this.isColorNeedNotify.set(false);
    }

    public /* synthetic */ void O000000o(MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2, int i, ASAvatarConfigInfo aSAvatarConfigInfo, int i2, View view) {
        if (ClickCheck2.getInstance().checkClickable()) {
            onGvItemClick(mimojiThumbnailRecyclerAdapter2, i, i2);
        }
    }

    public boolean getIsColorNeedNotify() {
        return this.isColorNeedNotify.get();
    }

    public int getItemCount() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mLevelDatas getItemCount size: ");
        sb.append(this.mLevelDatas.size());
        Log.i(str, sb.toString());
        CopyOnWriteArrayList copyOnWriteArrayList = this.mLevelDatas;
        if (copyOnWriteArrayList == null) {
            return 0;
        }
        return copyOnWriteArrayList.size();
    }

    public void notifyThumbnailUpdate(int i, int i2, int i3) {
        if (i != AvatarEngineManager2.getInstance().getSelectType()) {
            Log.d(TAG, "update wrong !!!!");
            return;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("notifyThumbnailUpdate.... index = ");
        sb.append(i2);
        sb.append(", position = ");
        sb.append(i3);
        Log.d(str, sb.toString());
        CopyOnWriteArrayList copyOnWriteArrayList = this.mLevelDatas;
        if (copyOnWriteArrayList == null || copyOnWriteArrayList.size() == 0 || i2 > this.mLevelDatas.size() - 1) {
            Log.e(TAG, "mLevelDatas Exception !!!!");
            return;
        }
        List list = this.mMimojiThumbnailAdapters;
        if (list == null || i2 < 0 || i2 > list.size() - 1) {
            Log.e(TAG, "mHandler message error !!!!");
            return;
        }
        this.mMimojiLevelBean2 = (MimojiLevelBean2) this.mLevelDatas.get(i2);
        ArrayList arrayList = this.mMimojiLevelBean2.mThumnails;
        MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2 = (MimojiThumbnailRecyclerAdapter2) this.mMimojiThumbnailAdapters.get(i2);
        if (arrayList == null || i3 < 0 || i3 >= arrayList.size()) {
            Log.e(TAG, "fmoji position message error !!!!");
        } else {
            mimojiThumbnailRecyclerAdapter2.updateData(i3, (ASAvatarConfigInfo) arrayList.get(i3));
        }
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        this.mMimojiLevelBean2 = (MimojiLevelBean2) this.mLevelDatas.get(i);
        RecyclerView recyclerView = viewHolder.mThumbnailGV;
        showColor(viewHolder, this.mMimojiLevelBean2, i);
        if (i < this.mMimojiThumbnailAdapters.size()) {
            ArrayList arrayList = this.mMimojiLevelBean2.mThumnails;
            int i2 = 0;
            int size = arrayList == null ? 0 : arrayList.size();
            if (size != 0) {
                recyclerView.setVisibility(0);
                MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2 = (MimojiThumbnailRecyclerAdapter2) this.mMimojiThumbnailAdapters.get(i);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.getItemAnimator().setChangeDuration(0);
                recyclerView.getItemAnimator().setRemoveDuration(0);
                recyclerView.getItemAnimator().setMoveDuration(0);
                if (recyclerView.getLayoutManager() == null) {
                    recyclerView.setLayoutManager(new BaseNoScrollGridLayoutManager(this.mContext, 3));
                }
                recyclerView.setAdapter(mimojiThumbnailRecyclerAdapter2);
                LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
                int i3 = size / 3;
                if (size % 3 != 0) {
                    i2 = 1;
                }
                int i4 = i3 + i2;
                int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_level_icon_margin);
                int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_level_icon_size);
                int dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_level_icon_margin);
                if (i != 0) {
                    dimensionPixelSize3 = (dimensionPixelSize3 / 3) * 2;
                } else if (viewHolder.mColorRecycleView.getVisibility() == 0) {
                    dimensionPixelSize3 /= 2;
                }
                layoutParams.topMargin = dimensionPixelSize3;
                layoutParams.height = (dimensionPixelSize2 * i4) + (dimensionPixelSize * (i4 - 1));
                recyclerView.setLayoutParams(layoutParams);
                mimojiThumbnailRecyclerAdapter2.setOnRecyclerItemClickListener(new O00000o0(this, mimojiThumbnailRecyclerAdapter2, i));
                return;
            }
        }
        recyclerView.setVisibility(8);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.mimoji_edit_level_item, viewGroup, false));
    }

    public synchronized void refreshData(List list, boolean z, boolean z2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mLevelDatas refreshData list size: ");
        sb.append(list.size());
        sb.append("   mLevelDatas size");
        sb.append(this.mLevelDatas.size());
        sb.append("  isColor : ");
        sb.append(z2);
        sb.append("   loadThumbnailFromCache : ");
        sb.append(z);
        Log.i(str, sb.toString());
        if (list != null) {
            if (list.size() != 0) {
                int i = 0;
                if (this.mLevelDatas == null || this.mLevelDatas.size() != list.size() || this.mMimojiThumbnailAdapters.size() == 0 || ((MimojiThumbnailRecyclerAdapter2) this.mMimojiThumbnailAdapters.get(0)).getItemCount() <= 0 || getItemCount() == 0) {
                    z2 = false;
                }
                if (z2) {
                    while (true) {
                        if (i >= list.size()) {
                            break;
                        } else if (i >= this.mMimojiThumbnailAdapters.size()) {
                            break;
                        } else {
                            if (z) {
                                ((MimojiThumbnailRecyclerAdapter2) this.mMimojiThumbnailAdapters.get(i)).setDataList(((MimojiLevelBean2) list.get(i)).mThumnails);
                            }
                            i++;
                        }
                    }
                } else {
                    setLevelDatas(list);
                    this.mMimojiThumbnailAdapters.clear();
                    while (i < this.mLevelDatas.size()) {
                        this.mMimojiThumbnailAdapters.add(new MimojiThumbnailRecyclerAdapter2(this.mContext, ((MimojiLevelBean2) this.mLevelDatas.get(i)).mConfigType, ((MimojiLevelBean2) this.mLevelDatas.get(i)).mConfigTypeName));
                        i++;
                    }
                    notifyDataSetChanged();
                }
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("fmoji refreshData isColorNeedNotify = ");
                sb2.append(this.isColorNeedNotify.get());
                Log.d(str2, sb2.toString());
                return;
            }
        }
        Log.i(TAG, "mLevelDatas refreshData list size error");
    }

    public void setIsColorNeedNotify(boolean z) {
        this.isColorNeedNotify.set(z);
    }

    public void setLevelDatas(List list) {
        this.mLevelDatas.clear();
        if (list != null && !list.isEmpty()) {
            this.mLevelDatas.addAll(list);
        }
    }

    public void setRenderThread(MimojiThumbnailRenderThread2 mimojiThumbnailRenderThread2) {
        this.mRenderThread = mimojiThumbnailRenderThread2;
    }

    public void updateSelectView(MimojiThumbnailRecyclerAdapter2 mimojiThumbnailRecyclerAdapter2, int i, int i2) {
        MimojiLevelBean2 mimojiLevelBean2 = (MimojiLevelBean2) this.mLevelDatas.get(i);
        if (i2 < mimojiLevelBean2.mThumnails.size()) {
            String str = FragmentMimojiEdit2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("click Thumbnail mConfigType:");
            sb.append(mimojiLevelBean2.mConfigType);
            sb.append(" configName:");
            sb.append(mimojiLevelBean2.mConfigTypeName);
            sb.append(" configId:");
            sb.append(((ASAvatarConfigInfo) mimojiLevelBean2.mThumnails.get(i2)).configID);
            Log.u(str, sb.toString());
            mimojiThumbnailRecyclerAdapter2.setSelectItem(mimojiLevelBean2.mConfigType, ((ASAvatarConfigInfo) mimojiLevelBean2.mThumnails.get(i2)).configID);
            mimojiThumbnailRecyclerAdapter2.notifyDataSetChanged();
        }
    }
}
