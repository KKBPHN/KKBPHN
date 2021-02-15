package com.android.camera.fragment.clone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import java.util.List;

public class VideoRecyclerViewAdapter extends Adapter {
    private final Context mContext;
    private final List mList;
    private final VideoPlayerManager mVideoPlayerManager;

    public VideoRecyclerViewAdapter(VideoPlayerManager videoPlayerManager, Context context, List list) {
        this.mVideoPlayerManager = videoPlayerManager;
        this.mContext = context;
        this.mList = list;
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public void onBindViewHolder(VideoViewHolder videoViewHolder, int i) {
        BaseVideoItem baseVideoItem = (BaseVideoItem) this.mList.get(i);
        baseVideoItem.update(i, videoViewHolder, this.mVideoPlayerManager);
        videoViewHolder.itemView.setContentDescription(baseVideoItem.getContentDescription());
        if (Util.isAccessible()) {
            videoViewHolder.itemView.setOnClickListener(C0297O00000oo.INSTANCE);
        }
    }

    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoViewHolder(((BaseVideoItem) this.mList.get(i)).createView(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_base_use_guide_item, viewGroup, false), this.mContext.getResources().getDisplayMetrics().widthPixels));
    }
}
