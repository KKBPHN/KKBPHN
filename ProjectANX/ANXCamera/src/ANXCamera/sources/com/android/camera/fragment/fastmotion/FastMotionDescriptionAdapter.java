package com.android.camera.fragment.fastmotion;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.ui.FastmotionTextureVideoView;
import com.android.camera.ui.FastmotionTextureVideoView.MediaPlayerCallback;
import com.android.camera.ui.MaskCircleCornerView;
import java.util.List;

public class FastMotionDescriptionAdapter extends Adapter {
    private static final String TAG = "FastMotionDescriptionAdapter";
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_VIDEO = 2;
    private boolean isInit = false;
    private Context mContext;
    private List mLists;
    private FastMotionViewHolder mPlayingHolder;
    private int mPlayingPosition = 1;
    private RecyclerView mRecyclerView;

    public class FastMotionViewHolder extends ViewHolder {
        public View mCoverView;
        public TextView mTextDesc;
        public TextView mTextTitle;
        public TextView mVideoDesc;
        public FastmotionTextureVideoView mVideoView;

        public FastMotionViewHolder(@NonNull View view) {
            super(view);
            if (view.getId() == R.id.fastmotion_description_item_text) {
                this.mTextTitle = (TextView) view.findViewById(R.id.fastmotion_description_item_text_title);
                this.mTextDesc = (TextView) view.findViewById(R.id.fastmotion_description_item_text_description);
            } else if (view.getId() == R.id.fastmotion_description_item_video) {
                this.mVideoDesc = (TextView) view.findViewById(R.id.fastmotion_description_item_video_desc);
                this.mVideoView = (FastmotionTextureVideoView) view.findViewById(R.id.fastmotion_description_item_video_vv);
                this.mCoverView = view.findViewById(R.id.fastmotion_description_item_video_cover);
            }
        }
    }

    public FastMotionDescriptionAdapter(RecyclerView recyclerView, Context context, List list) {
        this.mLists = list;
        this.mContext = context;
        this.mRecyclerView = recyclerView;
    }

    public int getItemCount() {
        return this.mLists.size();
    }

    public int getItemViewType(int i) {
        return (i == 0 || i == this.mLists.size() - 1) ? 1 : 2;
    }

    public void onBindViewHolder(@NonNull final FastMotionViewHolder fastMotionViewHolder, int i) {
        FastMotionDescriptionItem fastMotionDescriptionItem = (FastMotionDescriptionItem) this.mLists.get(i);
        if (fastMotionViewHolder.getItemViewType() == 1) {
            fastMotionViewHolder.mTextTitle.setText(fastMotionDescriptionItem.mType);
            fastMotionViewHolder.mTextDesc.setText(fastMotionDescriptionItem.mTypeDesc);
            return;
        }
        fastMotionViewHolder.itemView.setTag(this.mLists.get(i));
        fastMotionViewHolder.mVideoDesc.setText(fastMotionDescriptionItem.mVideoDesc);
        fastMotionViewHolder.mCoverView.setVisibility(0);
        fastMotionViewHolder.mCoverView.setBackgroundResource(fastMotionDescriptionItem.mVideoCover);
        this.mPlayingPosition = i;
        this.mPlayingHolder = fastMotionViewHolder;
        fastMotionViewHolder.mVideoView.setVisibility(0);
        fastMotionViewHolder.mVideoView.setLoop(true);
        FastmotionTextureVideoView fastmotionTextureVideoView = fastMotionViewHolder.mVideoView;
        StringBuilder sb = new StringBuilder();
        sb.append("android.resource://");
        sb.append(this.mContext.getPackageName());
        sb.append("/");
        sb.append(fastMotionDescriptionItem.mVideoId);
        fastmotionTextureVideoView.setVideoURI(Uri.parse(sb.toString()));
        fastMotionViewHolder.mVideoView.setMediaPlayerCallback(new MediaPlayerCallback() {
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            }

            public void onCompletion(MediaPlayer mediaPlayer) {
            }

            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return false;
            }

            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                if (i == 3) {
                    fastMotionViewHolder.mCoverView.setVisibility(8);
                }
                return false;
            }

            public void onPrepared(MediaPlayer mediaPlayer) {
            }

            public void onSurfaceReady(Surface surface) {
            }

            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            }

            public void stop() {
                fastMotionViewHolder.mCoverView.setVisibility(0);
            }
        });
        fastMotionViewHolder.mVideoView.start();
    }

    @NonNull
    public FastMotionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new FastMotionViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.fastmotion_description_item_text, viewGroup, false));
        }
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.fastmotion_description_item_video, viewGroup, false);
        FastmotionTextureVideoView fastmotionTextureVideoView = (FastmotionTextureVideoView) inflate.findViewById(R.id.fastmotion_description_item_video_vv);
        View findViewById = inflate.findViewById(R.id.fastmotion_description_item_video_cover);
        View findViewById2 = inflate.findViewById(R.id.fastmotion_description_item_video_top_cover_line);
        MaskCircleCornerView maskCircleCornerView = (MaskCircleCornerView) inflate.findViewById(R.id.fastmotion_description_item_video_vv_corner);
        int windowWidth = Display.getWindowWidth() - (this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_description_recycler_margin_start) * 2);
        float f = (((float) windowWidth) / 16.0f) * 9.0f;
        LayoutParams layoutParams = fastmotionTextureVideoView.getLayoutParams();
        LayoutParams layoutParams2 = findViewById.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) findViewById2.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) maskCircleCornerView.getLayoutParams();
        int i2 = (int) f;
        layoutParams.height = i2;
        layoutParams2.height = i2;
        double d = ((double) windowWidth) / 2.39d;
        int i3 = (int) ((((double) f) - d) / 2.0d);
        layoutParams3.topMargin = i3 - 5;
        layoutParams3.height = 5;
        layoutParams4.topMargin = i3;
        layoutParams4.height = (int) d;
        return new FastMotionViewHolder(inflate);
    }
}
