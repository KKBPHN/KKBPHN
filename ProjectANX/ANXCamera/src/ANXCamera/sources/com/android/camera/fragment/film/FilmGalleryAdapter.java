package com.android.camera.fragment.film;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.fragment.film.FragmentFilmGallery.EffectItemPadding;
import com.android.camera.statistic.CameraStatUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import java.util.Locale;

public class FilmGalleryAdapter extends Adapter implements OnClickListener {
    private EffectItemPadding mEffectItemPadding;
    private FilmList mFilmList;
    private RequestOptions mGlideOptions;
    private LinearLayoutManager mLayoutManager;
    private OnClickListener mParentClickListener;
    private FilmResourceSelectedListener mResourceSelectedListener;
    private int mSelectedIndex;

    public FilmGalleryAdapter(FilmList filmList, LinearLayoutManager linearLayoutManager, int i, OnClickListener onClickListener, FilmResourceSelectedListener filmResourceSelectedListener, EffectItemPadding effectItemPadding) {
        this.mFilmList = filmList;
        this.mLayoutManager = linearLayoutManager;
        if (i >= 0) {
            this.mSelectedIndex = i;
        }
        this.mParentClickListener = onClickListener;
        this.mResourceSelectedListener = filmResourceSelectedListener;
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

    public int getItemCount() {
        FilmList filmList = this.mFilmList;
        if (filmList != null) {
            return filmList.getSize();
        }
        return 0;
    }

    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        FilmItem filmItem = (FilmItem) this.mFilmList.getItem(i);
        View findViewById = commonRecyclerViewHolder.itemView.findViewById(R.id.vv_gallery_item_root);
        findViewById.setTag(Integer.valueOf(i));
        findViewById.setOnClickListener(this);
        FolmeUtils.touchItemScale(findViewById);
        ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_image);
        ImageView imageView2 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_play);
        ImageView imageView3 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_indicator);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_text);
        ((TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_duration)).setVisibility(8);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) textView.getLayoutParams();
        marginLayoutParams.rightMargin = marginLayoutParams.leftMargin;
        textView.setMaxWidth(Integer.MAX_VALUE);
        textView.setText(filmItem.getName());
        boolean z = true;
        textView.setSelected(true);
        if (this.mSelectedIndex != i) {
            z = false;
        }
        Util.correctionSelectView(imageView, z);
        if (z) {
            imageView2.setVisibility(0);
            imageView3.setVisibility(0);
        } else {
            imageView2.setVisibility(8);
            imageView3.setVisibility(8);
        }
        ViewCompat.setTransitionName(imageView, filmItem.id);
        Glide.with(commonRecyclerViewHolder.itemView).load(filmItem.coverPath).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
    }

    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            onBindViewHolder(commonRecyclerViewHolder, i);
        } else if (list.get(0) instanceof Boolean) {
            ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_image);
            ImageView imageView2 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_play);
            ImageView imageView3 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_indicator);
            TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_duration);
            ((TextView) commonRecyclerViewHolder.getView(R.id.vv_gallery_item_text)).setSelected(true);
            textView.setVisibility(8);
            FilmItem filmItem = (FilmItem) this.mFilmList.getItem(i);
            boolean booleanValue = ((Boolean) list.get(0)).booleanValue();
            commonRecyclerViewHolder.itemView.setActivated(booleanValue);
            Util.updateSelectIndicator(imageView3, booleanValue, true);
            Util.correctionSelectView(imageView, booleanValue);
            if (booleanValue) {
                imageView2.setVisibility(0);
            } else {
                imageView2.setVisibility(8);
            }
        }
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        CameraStatUtils.trackFilmTemplateThumbnailClick(((FilmItem) this.mFilmList.getItem(intValue)).getName());
        onSelected(intValue, view, true);
    }

    @NonNull
    public CommonRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_vv_gallery_item, viewGroup, false));
    }

    public void onSelected(int i, View view, boolean z) {
        int i2 = this.mSelectedIndex;
        if (i2 != i) {
            this.mSelectedIndex = i;
            this.mResourceSelectedListener.onResourceSelected((FilmItem) this.mFilmList.getItem(i));
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
