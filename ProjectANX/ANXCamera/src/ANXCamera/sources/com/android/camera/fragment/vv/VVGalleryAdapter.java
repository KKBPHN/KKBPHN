package com.android.camera.fragment.vv;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.fragment.vv.FragmentVVGallery.EffectItemPadding;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import java.util.Locale;

public class VVGalleryAdapter extends Adapter implements OnClickListener {
    private static final String TAG = "VVGalleryAdapter";
    private EffectItemPadding mEffectItemPadding;
    private RequestOptions mGlideOptions;
    private LinearLayoutManager mLayoutManager;
    private OnClickListener mParentClickListener;
    private ResourceSelectedListener mResourceSelectedListener;
    private int mSelectedIndex;
    private VVList mVVList;

    public VVGalleryAdapter(VVList vVList, LinearLayoutManager linearLayoutManager, int i, OnClickListener onClickListener, ResourceSelectedListener resourceSelectedListener, EffectItemPadding effectItemPadding) {
        this.mVVList = vVList;
        this.mLayoutManager = linearLayoutManager;
        if (i >= 0) {
            this.mSelectedIndex = i;
        }
        this.mParentClickListener = onClickListener;
        this.mResourceSelectedListener = resourceSelectedListener;
        this.mEffectItemPadding = effectItemPadding;
        this.mGlideOptions = new RequestOptions();
        this.mGlideOptions.skipMemoryCache(false);
        this.mGlideOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    private String getDurationString(long j) {
        int floor = (int) Math.floor((double) (((float) j) / 1000.0f));
        return String.format(Locale.ENGLISH, "00:%02d", new Object[]{Integer.valueOf(Math.abs(floor))});
    }

    private void notifyItemChanged(int i, int i2) {
        if (i > -1) {
            notifyItemChanged(i, Boolean.valueOf(false));
        }
        if (i2 > -1) {
            notifyItemChanged(i2, Boolean.valueOf(true));
        }
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            int i2 = this.mEffectItemPadding.mHorizontalPadding;
            View findViewByPosition = this.mLayoutManager.findViewByPosition(i);
            if (i > 0 && findViewByPosition != null) {
                i2 = (this.mEffectItemPadding.mHorizontalPadding * 2) + findViewByPosition.getWidth();
            }
            this.mLayoutManager.scrollToPositionWithOffset(Math.max(0, i), i2);
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, getItemCount() - 1));
        }
    }

    private void setAccessible(View view, boolean z, String str, long j) {
        if (z) {
            int floor = (int) Math.floor((double) (((float) j) / 1000.0f));
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str2 = ", ";
            sb.append(str2);
            sb.append(view.getResources().getQuantityString(R.plurals.accessibility_vv_duartion, floor, new Object[]{Integer.valueOf(floor)}));
            sb.append(str2);
            sb.append(view.getContext().getString(R.string.accessibility_selected));
            view.setContentDescription(sb.toString());
            if (Util.isAccessible()) {
                view.postDelayed(new C0337O0000oO0(view), 100);
                return;
            }
            return;
        }
        view.setContentDescription(str);
    }

    public VVItem getItemAt(int i) {
        return (VVItem) this.mVVList.getItem(i);
    }

    public int getItemCount() {
        VVList vVList = this.mVVList;
        if (vVList != null) {
            return vVList.getSize();
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x017a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        int currentState;
        String str;
        RequestManager requestManager;
        CommonRecyclerViewHolder commonRecyclerViewHolder2 = commonRecyclerViewHolder;
        int i2 = i;
        final VVItem vVItem = (VVItem) this.mVVList.getItem(i2);
        View findViewById = commonRecyclerViewHolder2.itemView.findViewById(R.id.vv_gallery_item_root);
        findViewById.setTag(Integer.valueOf(i));
        findViewById.setOnClickListener(this);
        FolmeUtils.touchItemScale(findViewById);
        ImageView imageView = (ImageView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_image);
        ImageView imageView2 = (ImageView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_play);
        ImageView imageView3 = (ImageView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_indicator);
        TextView textView = (TextView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_text);
        TextView textView2 = (TextView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_duration);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_state);
        ProgressBar progressBar = (ProgressBar) commonRecyclerViewHolder2.getView(R.id.vv_gallery_item_loading);
        textView.setText(vVItem.name);
        boolean z = true;
        textView.setSelected(true);
        if (!(this.mSelectedIndex == i2 && vVItem.getCurrentState() == 7)) {
            z = false;
        }
        Util.correctionSelectView(imageView, z);
        if (z) {
            textView2.setVisibility(0);
            textView2.setText(getDurationString(vVItem.getTotalDuration()));
            imageView2.setVisibility(0);
            imageView3.setVisibility(0);
        } else {
            textView2.setVisibility(8);
            imageView2.setVisibility(8);
            imageView3.setVisibility(8);
        }
        setAccessible(commonRecyclerViewHolder2.itemView, z, vVItem.name, vVItem.getTotalDuration());
        ViewCompat.setTransitionName(imageView, vVItem.name);
        if (!vVItem.isCloudItem() || vVItem.getCurrentState() == 5 || vVItem.getCurrentState() == 7) {
            imageView.setBackground(null);
            requestManager = Glide.with(commonRecyclerViewHolder2.itemView);
            str = vVItem.coverPath;
        } else {
            int parseColor = Color.parseColor(vVItem.placeholder);
            imageView.setImageDrawable(null);
            imageView.setBackgroundColor(parseColor);
            if (!TextUtils.isEmpty(vVItem.iconUrl)) {
                requestManager = Glide.with(commonRecyclerViewHolder2.itemView);
                str = vVItem.iconUrl;
            }
            lottieAnimationView.cancelAnimation();
            FolmeUtils.clean(lottieAnimationView);
            currentState = vVItem.getCurrentState();
            if (currentState != 0) {
                lottieAnimationView.setVisibility(0);
                lottieAnimationView.setImageResource(R.drawable.ic_vector_download);
                progressBar.setVisibility(8);
                return;
            } else if (currentState == 2) {
                lottieAnimationView.setVisibility(8);
                progressBar.setVisibility(0);
                return;
            } else if (currentState == 7) {
                progressBar.setVisibility(8);
                lottieAnimationView.setVisibility(8);
                return;
            } else if (currentState == 4) {
                lottieAnimationView.setVisibility(0);
                lottieAnimationView.setImageResource(R.drawable.ic_vector_download);
                progressBar.setVisibility(8);
                Toast.makeText(commonRecyclerViewHolder2.itemView.getContext(), R.string.live_music_network_exception, 0).show();
                return;
            } else if (currentState == 5) {
                progressBar.setVisibility(8);
                if (vVItem.isCloudItem()) {
                    lottieAnimationView.setVisibility(0);
                    lottieAnimationView.setScale(0.34f);
                    lottieAnimationView.O0000O0o((int) R.raw.resource_anim_finish);
                    lottieAnimationView.setProgress(1.0f);
                    lottieAnimationView.O0000oO();
                    FolmeUtils.animateShrink(lottieAnimationView, new Runnable() {
                        public void run() {
                            vVItem.setState(7);
                            VVGalleryAdapter.this.notifyItemChanged(vVItem.index);
                        }
                    });
                    return;
                }
                return;
            } else {
                throw new RuntimeException("unknown state");
            }
        }
        requestManager.load(str).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
        lottieAnimationView.cancelAnimation();
        FolmeUtils.clean(lottieAnimationView);
        currentState = vVItem.getCurrentState();
        if (currentState != 0) {
        }
    }

    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            onBindViewHolder(commonRecyclerViewHolder, i);
            return;
        }
        int i2 = 0;
        if (list.get(0) instanceof Boolean) {
            ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_image);
            ImageView imageView2 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_play);
            ImageView imageView3 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_indicator);
            TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_duration);
            ((TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_text)).setSelected(true);
            VVItem vVItem = (VVItem) this.mVVList.getItem(i);
            boolean booleanValue = ((Boolean) list.get(0)).booleanValue();
            commonRecyclerViewHolder.itemView.setActivated(booleanValue);
            Util.updateSelectIndicator(imageView3, booleanValue, true);
            Util.correctionSelectView(imageView, booleanValue);
            if (booleanValue) {
                textView.setVisibility(0);
                textView.setText(getDurationString(vVItem.getTotalDuration()));
            } else {
                i2 = 8;
                textView.setVisibility(8);
            }
            imageView2.setVisibility(i2);
            setAccessible(commonRecyclerViewHolder.itemView, booleanValue, vVItem.name, vVItem.getTotalDuration());
        }
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        VVItem vVItem = (VVItem) this.mVVList.getItem(intValue);
        int currentState = vVItem.getCurrentState();
        if (currentState == 0) {
            this.mParentClickListener.onClick(view);
        } else if (currentState != 7) {
            return;
        }
        CameraStatUtils.trackVVTemplateThumbnailClick(vVItem.name);
        onSelected(intValue, view, true);
    }

    @NonNull
    public CommonRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_vv_gallery_item, viewGroup, false));
    }

    public void onSelected(int i, View view, boolean z) {
        if (this.mSelectedIndex != i) {
            StringBuilder sb = new StringBuilder();
            sb.append("onSelected newIndex=");
            sb.append(i);
            Log.u(TAG, sb.toString());
            int i2 = this.mSelectedIndex;
            this.mSelectedIndex = i;
            this.mResourceSelectedListener.onResourceSelected((VVItem) this.mVVList.getItem(i));
            if (z) {
                scrollIfNeed(i);
                notifyItemChanged(i2, this.mSelectedIndex);
                return;
            }
            notifyDataSetChanged();
        } else if (view != null) {
            this.mParentClickListener.onClick(view);
        }
    }
}
