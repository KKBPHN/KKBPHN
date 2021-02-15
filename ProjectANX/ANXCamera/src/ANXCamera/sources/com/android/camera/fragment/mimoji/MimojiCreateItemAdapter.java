package com.android.camera.fragment.mimoji;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.fragment.music.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class MimojiCreateItemAdapter extends Adapter {
    private String adapterSelectState;
    /* access modifiers changed from: private */
    public List datas = new ArrayList();
    private Context mContext;
    LayoutInflater mLayoutInflater;
    private View mSelectItemView;
    /* access modifiers changed from: private */
    public OnItemClickListener onItemClickListener;

    class MimojiItemHolder extends CommonRecyclerViewHolder implements OnClickListener {
        public MimojiItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            FolmeUtils.touchItemScale(view);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                MimojiInfo mimojiInfo = (MimojiInfo) MimojiCreateItemAdapter.this.datas.get(adapterPosition);
                if (MimojiCreateItemAdapter.this.onItemClickListener != null) {
                    MimojiCreateItemAdapter.this.onItemClickListener.onItemClick(mimojiInfo, adapterPosition, view);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MimojiInfo mimojiInfo, int i, View view);
    }

    public MimojiCreateItemAdapter(Context context, String str) {
        this.mContext = context;
        this.adapterSelectState = str;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public int getItemCount() {
        List list = this.datas;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public MimojiInfo getMimojiInfoSelected() {
        if (this.datas == null || TextUtils.isEmpty(this.adapterSelectState)) {
            return null;
        }
        for (MimojiInfo mimojiInfo : this.datas) {
            if (this.adapterSelectState.equals(mimojiInfo.mConfigPath)) {
                return mimojiInfo;
            }
        }
        return null;
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x015d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(MimojiItemHolder mimojiItemHolder, int i) {
        boolean z;
        boolean z2;
        String str;
        int i2;
        ImageView imageView = (ImageView) mimojiItemHolder.getView(R.id.mimoji_item_image);
        this.mSelectItemView = mimojiItemHolder.getView(R.id.mimoji_item_selected_indicator);
        View view = mimojiItemHolder.getView(R.id.mimoji_long_item_selected_indicator);
        MimojiInfo mimojiInfo = (MimojiInfo) this.datas.get(i);
        mimojiItemHolder.itemView.setTag(mimojiInfo);
        if (mimojiInfo != null && mimojiInfo.mConfigPath != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) imageView.getLayoutParams();
            String str2 = "close_state";
            String str3 = "add_state";
            if (str2.equals(mimojiInfo.mConfigPath)) {
                marginLayoutParams.width = -2;
                marginLayoutParams.height = -2;
                i2 = R.drawable.ic_mimoji_off;
            } else if (str3.equals(mimojiInfo.mConfigPath)) {
                marginLayoutParams.width = -2;
                marginLayoutParams.height = -2;
                i2 = R.drawable.mimoji_add;
            } else {
                marginLayoutParams.width = this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_item_bottom_list);
                marginLayoutParams.height = this.mContext.getResources().getDimensionPixelSize(R.dimen.mimoji_item_bottom_list);
                Glide.with(this.mContext).load(mimojiInfo.mThumbnailUrl).apply((BaseRequestOptions) RequestOptions.bitmapTransform(new RoundedCornersTransformation(10, 1))).into(imageView);
                int i3 = 0;
                z = str3.equals(mimojiInfo.mConfigPath) && !TextUtils.isEmpty(this.adapterSelectState) && this.adapterSelectState.equals(mimojiInfo.mConfigPath);
                z2 = !z && !str2.equals(mimojiInfo.mConfigPath) && !AvatarEngineManager.isPrefabModel(mimojiInfo.mConfigPath);
                this.mSelectItemView.setVisibility(!z ? 0 : 8);
                if (!z2) {
                    i3 = 8;
                }
                view.setVisibility(i3);
                if (mimojiInfo.mName <= 0) {
                    str = mimojiItemHolder.itemView.getContext().getString(mimojiInfo.mName);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(mimojiItemHolder.itemView.getContext().getString(R.string.mimoji_custom_image));
                    sb.append(i - 1);
                    str = sb.toString();
                }
                if (!z) {
                    this.mSelectItemView.setBackground(this.mContext.getResources().getDrawable(z2 ? R.drawable.bg_mimoji_selected : R.drawable.bg_mimoji_animal_selected));
                    View view2 = mimojiItemHolder.itemView;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(", ");
                    sb2.append(mimojiItemHolder.itemView.getContext().getString(R.string.accessibility_selected));
                    view2.setContentDescription(sb2.toString());
                    if (Util.isAccessible()) {
                        mimojiItemHolder.itemView.postDelayed(new O0000o00(mimojiItemHolder), 100);
                        return;
                    }
                    return;
                }
                mimojiItemHolder.itemView.setContentDescription(str);
                return;
            }
            imageView.setImageResource(i2);
            int i32 = 0;
            if (str3.equals(mimojiInfo.mConfigPath)) {
            }
            if (!z) {
            }
            this.mSelectItemView.setVisibility(!z ? 0 : 8);
            if (!z2) {
            }
            view.setVisibility(i32);
            if (mimojiInfo.mName <= 0) {
            }
            if (!z) {
            }
        }
    }

    public MimojiItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MimojiItemHolder(this.mLayoutInflater.inflate(R.layout.fragment_mimoji_item, viewGroup, false));
    }

    public void setMimojiInfoList(List list) {
        this.datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void updateSelect() {
        this.adapterSelectState = DataRepository.dataItemLive().getMimojiStatusManager().getCurrentMimojiState();
        notifyDataSetChanged();
    }
}
