package com.android.camera.fragment.music;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.CtaNoticeFragment.OnCtaNoticeClickListener;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.RecyclerAdapterWrapper;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.network.NetworkDependencies;
import com.android.camera.network.live.BaseRequestException;
import com.android.camera.network.live.TTLiveMusicResourceRequest;
import com.android.camera.network.net.base.ErrorCode;
import com.android.camera.network.net.base.ResponseListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.LiveAudioChanges;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.resource.SimpleNetworkDownloadRequest;
import com.android.camera.resource.tmmusic.TMMusicCacheLoadRequest;
import com.android.camera.resource.tmmusic.TMMusicItemRequest;
import com.android.camera.resource.tmmusic.TMMusicList;
import com.android.camera.resource.tmmusic.TMMusicListMapToMusicInfo;
import com.android.camera.resource.tmmusic.TMMusicStationsRequest;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import miui.app.ProgressDialog;

public abstract class FragmentLiveMusicPager extends Fragment implements OnClickListener, OnTouchListener, OnCtaNoticeClickListener {
    private static final long MAX_REQUEST_TIME = 10800000;
    /* access modifiers changed from: private */
    public static final String TAG = "FragmentLiveMusicPager";
    /* access modifiers changed from: private */
    public LiveMusicInfo mCurrentSelectMusic;
    private LinearLayout mCurrentSelectedMusicLayout;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private boolean mEnableTrack;
    private OnAudioFocusChangeListener mFocusChangeListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int i) {
            if (i == -3 && FragmentLiveMusicPager.this.mMediaPlayer != null) {
                FragmentLiveMusicPager.this.mMediaPlayer.setVolume(0.2f, 0.2f);
            }
        }
    };
    private boolean mIsDestroyed;
    /* access modifiers changed from: private */
    public boolean mIsLoadingAnimationStart;
    /* access modifiers changed from: private */
    public boolean mIsMediaPreparing;
    private int mItemType;
    private WeakReference mLoadErrorWr;
    private ProgressBar mMediaLoadingProgressBar;
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public MusicOperation mMusicOperation;
    private int mMusicPlayPosition;
    private LinearLayout mNetworkExceptionLayout;
    private ImageView mPlayingImageView;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private LinearLayout mUpdateLayout;

    private void initAdapter() {
        this.mMusicOperation = new MusicOperation();
        if (this.mItemType != 0) {
            this.mNetworkExceptionLayout.setVisibility(8);
            this.mUpdateLayout.setVisibility(8);
            this.mRecyclerView.setVisibility(0);
            this.mRecyclerView.setAdapter(new MusicAdapter(getContext(), this, MusicUtils.getMusicListFromLocalFolder(FileUtils.MUSIC_LOCAL, getContext())));
        } else if (CtaNoticeFragment.checkCta(getActivity().getFragmentManager(), this, 1)) {
            loadOnlineMusicByFeature();
        }
    }

    /* access modifiers changed from: private */
    public void initOnlineAdapter(List list, String str) {
        this.mUpdateLayout.setVisibility(8);
        this.mNetworkExceptionLayout.setVisibility(8);
        this.mRecyclerView.setVisibility(0);
        MusicAdapter musicAdapter = new MusicAdapter(getContext(), this, list);
        if (TextUtils.isEmpty(str)) {
            this.mRecyclerView.setAdapter(musicAdapter);
            return;
        }
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_music_adapter_footer, this.mRecyclerView, false);
        ((TextView) inflate.findViewById(R.id.music_hint)).setText(str);
        RecyclerAdapterWrapper recyclerAdapterWrapper = new RecyclerAdapterWrapper(musicAdapter);
        recyclerAdapterWrapper.addFooter(inflate);
        this.mRecyclerView.setAdapter(recyclerAdapterWrapper);
    }

    private boolean isMusicFileExists(LiveMusicInfo liveMusicInfo) {
        String str = liveMusicInfo.mPlayUrl;
        if (this.mItemType == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(FileUtils.MUSIC_ONLINE);
            sb.append(liveMusicInfo.mTitle);
            sb.append(".mp3");
            str = sb.toString();
        }
        return new File(str).exists();
    }

    private void loadOnlineMusicByFeature() {
        if (!NetworkDependencies.isConnected(getContext())) {
            onLoadError();
            return;
        }
        if (C0122O00000o.instance().OOO0o0O()) {
            loadTikTokMusic();
        } else {
            loadTMMusic();
        }
    }

    private void loadTMMusic() {
        if (isAdded()) {
            this.mEnableTrack = true;
            this.mDisposable.add(Observable.concat((ObservableSource) new TMMusicCacheLoadRequest().startObservable(TMMusicList.class), (ObservableSource) new TMMusicStationsRequest(true).startObservable(TMMusicList.class).flatMap(O000000o.INSTANCE)).firstElement().map(new TMMusicListMapToMusicInfo()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C0313O00000oO(this), new O00000Oo(this)));
        }
    }

    private void loadTikTokMusic() {
        if (getContext() != null) {
            TTLiveMusicResourceRequest tTLiveMusicResourceRequest = new TTLiveMusicResourceRequest();
            long liveMusicFirstRequestTime = DataRepository.dataItemRunning().getLiveMusicFirstRequestTime();
            long currentTimeMillis = System.currentTimeMillis() - liveMusicFirstRequestTime;
            if (liveMusicFirstRequestTime <= 0 || currentTimeMillis >= MAX_REQUEST_TIME) {
                tTLiveMusicResourceRequest.execute(new ResponseListener() {
                    public void onResponse(Object... objArr) {
                        final List list = objArr[0];
                        if (FragmentLiveMusicPager.this.isAdded()) {
                            Completable.fromAction(new Action() {
                                public void run() {
                                    DataRepository.dataItemRunning().setLiveMusicFirstRequestTime(System.currentTimeMillis());
                                    FragmentLiveMusicPager.this.initOnlineAdapter(list, null);
                                }
                            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                        }
                    }

                    public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                        Completable.fromAction(new Action() {
                            public void run() {
                                FragmentLiveMusicPager.this.onLoadError();
                            }
                        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                        String access$200 = FragmentLiveMusicPager.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("request online music failed, errorCode =  ");
                        sb.append(errorCode);
                        Log.e(access$200, sb.toString());
                    }
                });
            } else {
                try {
                    initOnlineAdapter(tTLiveMusicResourceRequest.loadFromCache(), null);
                } catch (BaseRequestException e) {
                    onLoadError();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("request online music failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onLoadError() {
        Runnable runnable = (Runnable) this.mLoadErrorWr.get();
        WeakReference weakReference = this.mLoadErrorWr;
        if (weakReference != null && weakReference.get() != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public void onMusicPauseOrStopPlay() {
        this.mPlayingImageView.setImageResource(R.drawable.ic_vector_play);
        this.mPlayingImageView.setContentDescription(getString(R.string.accessibility_live_music_play_button));
    }

    private void onMusicSelectedToPlay(LiveMusicInfo liveMusicInfo) {
        String str = liveMusicInfo.mPlayUrl;
        if (!TextUtils.isEmpty(str) && !this.mIsDestroyed) {
            ((AudioManager) getContext().getSystemService("audio")).requestAudioFocus(this.mFocusChangeListener, 3, 1);
            this.mPlayingImageView.setVisibility(4);
            this.mMediaLoadingProgressBar.setVisibility(0);
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (!(mediaPlayer == null || this.mCurrentSelectMusic == null)) {
                mediaPlayer.stop();
                this.mMediaPlayer.reset();
                this.mMusicOperation.endPlaySession();
            }
            this.mMusicOperation.onSelected(liveMusicInfo.mRequestItemID, liveMusicInfo.mCategoryId);
            this.mCurrentSelectMusic = liveMusicInfo;
            try {
                this.mMediaPlayer.setDataSource(str);
                this.mMediaPlayer.prepareAsync();
                this.mIsMediaPreparing = true;
                this.mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        FragmentLiveMusicPager.this.onMusicStartPlay();
                        FragmentLiveMusicPager.this.mMusicOperation.onNewSessionStartPlay((long) mediaPlayer.getDuration());
                        FragmentLiveMusicPager.this.mIsMediaPreparing = false;
                        mediaPlayer.start();
                    }
                });
                this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        FragmentLiveMusicPager.this.mMediaPlayer.stop();
                        FragmentLiveMusicPager.this.mMediaPlayer.reset();
                        FragmentLiveMusicPager.this.onMusicPauseOrStopPlay();
                        FragmentLiveMusicPager.this.mMusicOperation.endPlaySession();
                        FragmentLiveMusicPager.this.mCurrentSelectMusic = null;
                    }
                });
            } catch (IOException e) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mediaplayer play failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString());
            }
        }
    }

    private void onMusicSelectedToUse(LiveMusicInfo liveMusicInfo) {
        this.mMusicOperation.onSelected(liveMusicInfo.mRequestItemID, liveMusicInfo.mCategoryId);
        this.mMusicOperation.onSelectedToUse();
        String str = liveMusicInfo.mPlayUrl;
        if (this.mItemType == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(FileUtils.MUSIC_ONLINE);
            sb.append(liveMusicInfo.mTitle);
            sb.append(".mp3");
            str = sb.toString();
        }
        LiveAudioChanges liveAudioChanges = (LiveAudioChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(211);
        if (liveAudioChanges != null) {
            liveAudioChanges.setAudioPath(str);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(liveMusicInfo.mTitle);
        sb2.append("-");
        sb2.append(liveMusicInfo.mAuthor);
        String sb3 = sb2.toString();
        String str2 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("onMusicSelectedToUse: musicHint=");
        sb4.append(sb3);
        Log.u(str2, sb4.toString());
        CameraSettings.setCurrentLiveMusic(str, sb3);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.updateConfigItem(245);
        }
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /* access modifiers changed from: private */
    public void onMusicStartPlay() {
        this.mMediaLoadingProgressBar.setVisibility(4);
        this.mPlayingImageView.setVisibility(0);
        this.mPlayingImageView.setImageResource(R.drawable.ic_vector_recording_pause);
        this.mPlayingImageView.setContentDescription(getString(R.string.accessibility_live_music_pause_button));
    }

    private void selectMusic(LiveMusicInfo liveMusicInfo) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            this.mMusicOperation.endPlaySession();
        }
        if (isMusicFileExists(liveMusicInfo)) {
            onMusicSelectedToUse(liveMusicInfo);
        } else if (this.mItemType == 0) {
            startDownloadMusicForUse(liveMusicInfo);
        }
    }

    private void startDownloadAnimation() {
        this.mIsLoadingAnimationStart = true;
        this.mProgressDialog = new ProgressDialog(getActivity());
        String string = getString(R.string.live_music_downloading_tips);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                if (FragmentLiveMusicPager.this.mProgressDialog.isShowing()) {
                    FragmentLiveMusicPager.this.mProgressDialog.dismiss();
                    FragmentLiveMusicPager.this.mIsLoadingAnimationStart;
                }
                return true;
            }
        });
        this.mProgressDialog.setMessage(string);
        this.mProgressDialog.show();
    }

    private void startDownloadMusicForUse(LiveMusicInfo liveMusicInfo) {
        if (this.mItemType == 0) {
            if (!NetworkDependencies.isConnected(getContext())) {
                Toast.makeText(getActivity(), R.string.live_music_network_exception, 1).show();
                return;
            }
            startDownloadAnimation();
            StringBuilder sb = new StringBuilder();
            sb.append(FileUtils.MUSIC_ONLINE);
            sb.append(liveMusicInfo.mTitle);
            sb.append(".mp3");
            String sb2 = sb.toString();
            if (liveMusicInfo.downloadState == 6) {
                startTTMusicDetailInfoRequest(false, sb2, liveMusicInfo);
            } else {
                startDownloadRequest(sb2, liveMusicInfo);
            }
        }
    }

    /* access modifiers changed from: private */
    public void startDownloadRequest(String str, LiveMusicInfo liveMusicInfo) {
        new SimpleNetworkDownloadRequest(liveMusicInfo.mPlayUrl, str).startObservable((Object) liveMusicInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new O00000o(this), new C0314O00000oo(this));
    }

    /* access modifiers changed from: private */
    public void startPlayOrStopMusic(LiveMusicInfo liveMusicInfo) {
        LiveMusicInfo liveMusicInfo2 = this.mCurrentSelectMusic;
        if (liveMusicInfo2 != null && liveMusicInfo2.equals(liveMusicInfo)) {
            if (this.mMediaPlayer.isPlaying()) {
                Log.u(TAG, "startPlayOrStopMusic: stop");
                this.mMediaPlayer.pause();
                this.mMusicOperation.onPausePlay();
                onMusicPauseOrStopPlay();
            } else {
                Log.u(TAG, "startPlayOrStopMusic: play");
                this.mMusicOperation.onResumePlay();
                this.mMediaPlayer.start();
                onMusicStartPlay();
            }
        } else if (this.mItemType != 0 || NetworkDependencies.isConnected(getContext())) {
            this.mPlayingImageView.setVisibility(4);
            this.mMediaLoadingProgressBar.setVisibility(0);
            if (!TextUtils.isEmpty(liveMusicInfo.mPlayUrl)) {
                onMusicSelectedToPlay(liveMusicInfo);
            } else if (this.mItemType == 0 && liveMusicInfo.downloadState == 6) {
                startTTMusicDetailInfoRequest(true, null, liveMusicInfo);
            }
        } else {
            Toast.makeText(getActivity(), R.string.live_music_network_exception, 1).show();
        }
    }

    private void startTTMusicDetailInfoRequest(final boolean z, final String str, LiveMusicInfo liveMusicInfo) {
        new TMMusicItemRequest(liveMusicInfo.mRequestItemID).startObservable((Object) liveMusicInfo).subscribe(new Consumer() {
            public void accept(LiveMusicInfo liveMusicInfo) {
                liveMusicInfo.downloadState = 0;
                if (!z) {
                    FragmentLiveMusicPager.this.startDownloadRequest(str, liveMusicInfo);
                } else if (!TextUtils.isEmpty(liveMusicInfo.mPlayUrl)) {
                    FragmentLiveMusicPager.this.startPlayOrStopMusic(liveMusicInfo);
                }
            }
        }, new Consumer() {
            public void accept(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    private void stopDownloadAnimation() {
        this.mIsLoadingAnimationStart = false;
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public /* synthetic */ void O000000o(LiveMusicInfo liveMusicInfo) {
        stopDownloadAnimation();
        onMusicSelectedToUse(liveMusicInfo);
    }

    public /* synthetic */ void O000000o(Throwable th) {
        th.printStackTrace();
        onLoadError();
    }

    public /* synthetic */ void O000000o(List list) {
        initOnlineAdapter(list, getResources().getString(R.string.music_hint_tt));
    }

    public /* synthetic */ void O00000Oo(Throwable th) {
        stopDownloadAnimation();
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.live_music_network_exception, 1).show();
        }
    }

    public /* synthetic */ void O0000ooo() {
        LinearLayout linearLayout = this.mNetworkExceptionLayout;
        if (linearLayout != null) {
            linearLayout.setVisibility(0);
        }
    }

    public abstract int getType();

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.music_recycler_view);
        this.mRecyclerView.setFocusable(false);
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "music_recycler_view");
        linearLayoutManagerWrapper.setOrientation(1);
        this.mRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mUpdateLayout = (LinearLayout) view.findViewById(R.id.music_updating_layout);
        this.mNetworkExceptionLayout = (LinearLayout) view.findViewById(R.id.music_network_exception);
        this.mNetworkExceptionLayout.setOnClickListener(this);
        this.mLoadErrorWr = new WeakReference(new O00000o0(this));
        this.mItemType = getType();
        this.mMediaPlayer = new MediaPlayer();
        this.mIsLoadingAnimationStart = false;
        this.mIsMediaPreparing = false;
        initAdapter();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_layout /*2131296870*/:
                Log.u(TAG, "onClick: music_layout");
                selectMusic((LiveMusicInfo) view.getTag());
                return;
            case R.id.music_network_exception /*2131296873*/:
                Log.u(TAG, "onClick: music_network_exception");
                if (CtaNoticeFragment.checkCta(getActivity().getFragmentManager(), this, 1)) {
                    this.mUpdateLayout.setVisibility(0);
                    this.mNetworkExceptionLayout.setVisibility(8);
                    loadOnlineMusicByFeature();
                    return;
                }
                return;
            case R.id.music_play /*2131296874*/:
                if (!this.mIsMediaPreparing) {
                    Log.u(TAG, "onClick: music_play");
                    LiveMusicInfo liveMusicInfo = (LiveMusicInfo) view.getTag();
                    if (this.mPlayingImageView != null) {
                        onMusicPauseOrStopPlay();
                    }
                    ImageView imageView = (ImageView) view.findViewById(R.id.music_play);
                    if (!imageView.equals(this.mPlayingImageView)) {
                        this.mPlayingImageView = imageView;
                    }
                    this.mMediaLoadingProgressBar = (ProgressBar) ((ViewGroup) view.getParent()).findViewById(R.id.music_loading);
                    startPlayOrStopMusic(liveMusicInfo);
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_music_pager, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mIsDestroyed = true;
        this.mMediaPlayer.release();
        this.mRecyclerView.setAdapter(null);
    }

    public void onNegativeClick(DialogInterface dialogInterface, int i) {
        this.mUpdateLayout.setVisibility(8);
        this.mNetworkExceptionLayout.setVisibility(0);
    }

    public void onPause() {
        super.onPause();
        if (this.mMediaPlayer.isPlaying()) {
            onMusicPauseOrStopPlay();
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            this.mMusicOperation.endPlaySession();
            this.mCurrentSelectMusic = null;
        }
    }

    public void onPositiveClick(DialogInterface dialogInterface, int i) {
        loadOnlineMusicByFeature();
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
        this.mDisposable.clear();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                selectMusic((LiveMusicInfo) view.getTag());
            } else if (actionMasked != 2) {
            }
        }
        return true;
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (!z) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
                onMusicPauseOrStopPlay();
            }
        }
    }
}
