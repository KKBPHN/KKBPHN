package com.android.camera.features.mimoji2.fragment.bottomlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.features.mimoji2.widget.baseview.BaseItemAnimator;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerAdapter;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerViewHolder;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.music.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class MimojiAvatarAdapter extends BaseRecyclerAdapter {
    private int mLastSelectPosition = 0;
    /* access modifiers changed from: private */
    public int mResourceBg;

    class AvatarViewViewHolder extends BaseRecyclerViewHolder {
        private String mAdapterSelectState;
        private View mCartoonSwitchView;
        private ImageView mItemImageView;
        private View mLongSelectedView;
        private MimojiStatusManager2 mMimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        private View mSelectItemView;

        public AvatarViewViewHolder(@NonNull View view) {
            super(view);
            FolmeUtils.handleListItemTouch(view, BaseItemAnimator.DEFAULT_LIST_DURATION);
            this.mItemImageView = (ImageView) view.findViewById(R.id.mimoji_item_image);
            this.mSelectItemView = view.findViewById(R.id.mimoji_item_selected_indicator);
            this.mLongSelectedView = view.findViewById(R.id.mimoji_long_item_selected_indicator);
            this.mCartoonSwitchView = view.findViewById(R.id.mimoji_item_cartoon_switch);
            view.setBackgroundResource(MimojiAvatarAdapter.this.mResourceBg);
        }

        public /* synthetic */ void O00O0Ooo() {
            this.itemView.sendAccessibilityEvent(128);
        }

        public View[] getRotateViews() {
            return new View[]{this.mCartoonSwitchView, this.mItemImageView, this.mLongSelectedView};
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x00f6  */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x010a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setData(MimojiInfo2 mimojiInfo2, int i) {
            Drawable drawable;
            View view;
            Context context;
            int i2;
            RoundedCornersTransformation roundedCornersTransformation;
            RequestBuilder requestBuilder;
            ImageView imageView;
            int i3;
            this.mAdapterSelectState = this.mMimojiStatusManager2.getCurrentMimojiState();
            this.mCartoonSwitchView.setVisibility(8);
            if (mimojiInfo2 != null && !TextUtils.isEmpty(mimojiInfo2.mConfigPath)) {
                String str = "close_state";
                String str2 = "add_state";
                if (str.equals(mimojiInfo2.mConfigPath)) {
                    imageView = this.mItemImageView;
                    i3 = R.drawable.ic_mimoji_off;
                } else if (str2.equals(mimojiInfo2.mConfigPath)) {
                    imageView = this.mItemImageView;
                    i3 = R.drawable.ic_mimoji_add;
                } else {
                    if (mimojiInfo2.getFrame() > 0) {
                        requestBuilder = (RequestBuilder) ((RequestBuilder) Glide.with(this.itemView.getContext()).load(mimojiInfo2.mThumbnailUrl2).skipMemoryCache(false)).diskCacheStrategy(DiskCacheStrategy.ALL);
                        roundedCornersTransformation = new RoundedCornersTransformation(10, 1);
                    } else {
                        requestBuilder = (RequestBuilder) ((RequestBuilder) Glide.with(this.itemView.getContext()).load(mimojiInfo2.mThumbnailUrl).skipMemoryCache(false)).diskCacheStrategy(DiskCacheStrategy.ALL);
                        roundedCornersTransformation = new RoundedCornersTransformation(10, 1);
                    }
                    requestBuilder.apply((BaseRequestOptions) RequestOptions.bitmapTransform(roundedCornersTransformation)).into(this.mItemImageView);
                    if (mimojiInfo2.getDefaultFrame() > 0) {
                        MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
                        if (currentMimojiInfo != null && !TextUtils.isEmpty(currentMimojiInfo.mAvatarTemplatePath) && currentMimojiInfo.mAvatarTemplatePath.equals(mimojiInfo2.mAvatarTemplatePath)) {
                            this.mCartoonSwitchView.setVisibility(0);
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.itemView.getContext().getString(R.string.mimoji_custom_image));
                    sb.append(i - 1);
                    String sb2 = sb.toString();
                    if (mimojiInfo2.getFrame() > 0 || mimojiInfo2.mName2 <= 0) {
                        if (mimojiInfo2.mName > 0) {
                            context = this.itemView.getContext();
                            i2 = mimojiInfo2.mName;
                        }
                        if (!TextUtils.isEmpty(this.mAdapterSelectState) || !this.mAdapterSelectState.equals(mimojiInfo2.mConfigPath) || mimojiInfo2.mConfigPath.equals(str2)) {
                            alphaGone(BaseItemAnimator.DEFAULT_LIST_DURATION, this.mSelectItemView);
                            this.mLongSelectedView.setVisibility(8);
                            this.mCartoonSwitchView.setVisibility(8);
                            this.itemView.setContentDescription(sb2);
                        }
                        if (AvatarEngineManager2.isPrefabModel(mimojiInfo2.mConfigPath) || this.mAdapterSelectState.equals(str) || mimojiInfo2.mConfigPath.equals(str2)) {
                            alphaGone(BaseItemAnimator.DEFAULT_LIST_DURATION, this.mLongSelectedView);
                            view = this.mSelectItemView;
                            drawable = this.itemView.getContext().getResources().getDrawable(R.drawable.bg_mimoji_animal_selected);
                        } else {
                            showView(this.mLongSelectedView);
                            view = this.mSelectItemView;
                            drawable = this.itemView.getContext().getResources().getDrawable(R.drawable.bg_mimoji_selected);
                        }
                        view.setBackground(drawable);
                        if (mimojiInfo2.getDefaultFrame() > 0 && this.mAdapterSelectState.equals(mimojiInfo2.mConfigPath)) {
                            this.mCartoonSwitchView.setVisibility(0);
                            if (mimojiInfo2.isIsNeedAnim()) {
                                ViewCompat.animate(this.mCartoonSwitchView).rotationBy(mimojiInfo2.getFrame() == 0 ? -180.0f : 180.0f).setDuration((long) BaseItemAnimator.DEFAULT_LIST_DURATION).start();
                                mimojiInfo2.setIsNeedAnim(false);
                            }
                            this.mSelectItemView.setBackground(this.itemView.getContext().getResources().getDrawable(R.drawable.bg_mimoji_selected));
                        }
                        showView(this.mSelectItemView);
                        View view2 = this.itemView;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(sb2);
                        sb3.append(", ");
                        sb3.append(this.itemView.getContext().getString(R.string.accessibility_selected));
                        view2.setContentDescription(sb3.toString());
                        if (Util.isAccessible()) {
                            this.itemView.postDelayed(new O0000Oo(this), 100);
                            return;
                        }
                        return;
                    }
                    context = this.itemView.getContext();
                    i2 = mimojiInfo2.mName2;
                    sb2 = context.getString(i2);
                    if (!TextUtils.isEmpty(this.mAdapterSelectState)) {
                    }
                    alphaGone(BaseItemAnimator.DEFAULT_LIST_DURATION, this.mSelectItemView);
                    this.mLongSelectedView.setVisibility(8);
                    this.mCartoonSwitchView.setVisibility(8);
                    this.itemView.setContentDescription(sb2);
                }
                imageView.setImageResource(i3);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.itemView.getContext().getString(R.string.mimoji_custom_image));
                sb4.append(i - 1);
                String sb22 = sb4.toString();
                if (mimojiInfo2.getFrame() > 0) {
                }
                if (mimojiInfo2.mName > 0) {
                }
                if (!TextUtils.isEmpty(this.mAdapterSelectState)) {
                }
                alphaGone(BaseItemAnimator.DEFAULT_LIST_DURATION, this.mSelectItemView);
                this.mLongSelectedView.setVisibility(8);
                this.mCartoonSwitchView.setVisibility(8);
                this.itemView.setContentDescription(sb22);
            }
        }
    }

    public MimojiAvatarAdapter(List list) {
        super(list);
    }

    /* access modifiers changed from: protected */
    public BaseRecyclerViewHolder onCreateBaseRecyclerViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AvatarViewViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_mimoji_item, viewGroup, false));
    }

    public void setLastSelectPosition(int i) {
        this.mLastSelectPosition = i;
    }

    public void setResourceBg(int i) {
        this.mResourceBg = i;
    }

    public void setSelectState(int i) {
        int i2 = this.mLastSelectPosition;
        Boolean valueOf = Boolean.valueOf(false);
        if (i2 != i || !isAvailablePosion(i)) {
            if (isAvailablePosion(this.mLastSelectPosition)) {
                notifyItemChanged(this.mLastSelectPosition, valueOf);
            }
            if (isAvailablePosion(i)) {
                notifyItemChanged(i, valueOf);
                this.mLastSelectPosition = i;
            }
            return;
        }
        notifyItemChanged(i, valueOf);
    }
}
