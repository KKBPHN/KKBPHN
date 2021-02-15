package com.android.camera.fragment.clone;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class VideoViewHolder extends ViewHolder {
    public final ImageView mCover;
    public View mFooter = this.itemView.findViewById(R.id.clone_guide_footer_view);
    public TextView mTextViewTip1 = ((TextView) this.itemView.findViewById(R.id.clone_guide_tip_tv1));
    public TextView mTextViewTitle1 = ((TextView) this.itemView.findViewById(R.id.clone_guide_tv1));
    public TextView mTextViewTitle2 = ((TextView) this.itemView.findViewById(R.id.clone_guide_tv2));
    public final VideoPlayerView mTextureView;
    public final FrameLayout mTextureViewContainer;
    public RelativeLayout mTipsContainer = ((RelativeLayout) this.itemView.findViewById(R.id.clone_guide_tips_container));

    public VideoViewHolder(View view) {
        super(view);
        this.mTextureView = (VideoPlayerView) view.findViewById(R.id.clone_guide_texture_view);
        this.mTextureViewContainer = (FrameLayout) view.findViewById(R.id.clone_guide_texture_view_container);
        this.mCover = (ImageView) view.findViewById(R.id.clone_guide_video_colver);
    }
}
