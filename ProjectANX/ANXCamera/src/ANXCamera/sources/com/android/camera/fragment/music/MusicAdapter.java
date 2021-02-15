package com.android.camera.fragment.music;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class MusicAdapter extends Adapter {
    private Context mContext;
    private List mMusicList;
    private OnClickListener mOnClickListener;

    public MusicAdapter(Context context, OnClickListener onClickListener, List list) {
        this.mContext = context;
        this.mOnClickListener = onClickListener;
        this.mMusicList = list;
    }

    public int getItemCount() {
        return this.mMusicList.size();
    }

    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        LiveMusicInfo liveMusicInfo = (LiveMusicInfo) this.mMusicList.get(i);
        FolmeUtils.touchTintDefaultDayNight(commonRecyclerViewHolder.itemView);
        commonRecyclerViewHolder.itemView.setOnClickListener(this.mOnClickListener);
        commonRecyclerViewHolder.itemView.setTag(liveMusicInfo);
        float f = this.mContext.getResources().getConfiguration().fontScale;
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.music_author);
        textView.setText(liveMusicInfo.mAuthor.trim());
        TextView textView2 = (TextView) commonRecyclerViewHolder.getView(R.id.music_title);
        textView2.setEllipsize(TruncateAt.MARQUEE);
        textView2.setSingleLine();
        textView2.setText(liveMusicInfo.mTitle.trim());
        Glide.with(this.mContext).load(liveMusicInfo.mThumbnailUrl).apply((BaseRequestOptions) RequestOptions.bitmapTransform(new RoundedCornersTransformation(this.mContext.getResources().getDimensionPixelSize(R.dimen.music_thumbnail_image_radius), 1))).into((ImageView) commonRecyclerViewHolder.getView(R.id.music_thumbnail));
        ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.music_play);
        FolmeUtils.touchScaleTint(imageView);
        imageView.setOnClickListener(this.mOnClickListener);
        imageView.setTag(liveMusicInfo);
        ((ProgressBar) commonRecyclerViewHolder.getView(R.id.music_loading)).setTag(liveMusicInfo);
        TextView textView3 = (TextView) commonRecyclerViewHolder.getView(R.id.music_duration);
        String trim = liveMusicInfo.mDurationText.trim();
        if (trim.split(":").length < 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("00 : ");
            sb.append(trim);
            trim = sb.toString();
        }
        textView3.setText(trim);
        LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
        if (f > 1.25f) {
            layoutParams.topMargin = 0;
        } else {
            layoutParams.topMargin = this.mContext.getResources().getDimensionPixelOffset(R.dimen.music_author_margin_top);
        }
        textView.setLayoutParams(layoutParams);
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_music_adapter, viewGroup, false));
    }

    public void setData(List list) {
        this.mMusicList = list;
        notifyDataSetChanged();
    }
}
