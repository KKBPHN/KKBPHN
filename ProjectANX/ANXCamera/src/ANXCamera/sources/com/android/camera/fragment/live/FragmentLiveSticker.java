package com.android.camera.fragment.live;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.CtaNoticeFragment.OnCtaNoticeClickListener;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.sticker.download.DownloadView;
import com.android.camera.fragment.sticker.download.DownloadView.OnDownloadSuccessListener;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.network.download.Request;
import com.android.camera.network.download.Verifier.Md5;
import com.android.camera.network.live.TTLiveStickerResourceRequest;
import com.android.camera.network.net.base.ErrorCode;
import com.android.camera.network.net.base.ResponseListener;
import com.android.camera.network.resource.LiveDownloadHelper;
import com.android.camera.network.resource.LiveResourceDownloadManager;
import com.android.camera.network.resource.OnLiveDownloadListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.StickerProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.sticker.LiveStickerInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ss.android.ugc.effectmanager.common.EffectConstants;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FragmentLiveSticker extends FragmentLiveBase implements OnCtaNoticeClickListener {
    private static final int FRAGMENT_INFO = 4092;
    private static final String MAIN_URI = "snssdk1128://feed/";
    private static final String MARKET_URI = "market://details?id=com.ss.android.ugc.aweme&back=true&ref=camera&startDownload=false";
    private static final int MIN_SUPPORT_VERSION = 320;
    private static final String MORE_URI = "snssdk1128://openRecord/?recordOrigin=system&recordParam=withStickerPanel&gd_label=open_camera&label=";
    private static final String PACKAGE_NAME = "com.ss.android.ugc.aweme";
    private static final int STICKER_ITEM_SIZE = 10;
    private static final String TAG = "FragmentLiveSticker";
    /* access modifiers changed from: private */
    public static final LiveStickerInfo[] sLocalStickerList;
    /* access modifiers changed from: private */
    public static final LiveStickerInfo sMoreSticker;
    private static final LiveStickerInfo sNoneSticker;
    private static List sPersistStickerList = new ArrayList(Arrays.asList(sLocalStickerList));
    /* access modifiers changed from: private */
    public StickerItemAdapter mAdapter;
    private LiveDownloadHelper mDownloadHelper = new LiveDownloadHelper() {
        public Request createDownloadRequest(LiveStickerInfo liveStickerInfo) {
            StringBuilder sb = new StringBuilder();
            sb.append(FileUtils.STICKER_RESOURCE_DIR);
            sb.append(liveStickerInfo.hash);
            sb.append(".zip");
            Request request = new Request(liveStickerInfo.id, Uri.parse(liveStickerInfo.url), new File(sb.toString()));
            request.setVerifier(new Md5(liveStickerInfo.hash));
            return request;
        }

        public boolean isDownloaded(LiveStickerInfo liveStickerInfo) {
            return false;
        }
    };
    private OnLiveDownloadListener mDownloadListener = new OnLiveDownloadListener() {
        public void onFinish(String str, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("finish ");
            sb.append(str);
            sb.append(": ");
            sb.append(i);
            String sb2 = sb.toString();
            String str2 = FragmentLiveSticker.TAG;
            Log.v(str2, sb2);
            final int i2 = 0;
            while (true) {
                if (i2 >= FragmentLiveSticker.this.mStickerList.size()) {
                    i2 = -1;
                    break;
                } else if (str.equals(((LiveStickerInfo) FragmentLiveSticker.this.mStickerList.get(i2)).id)) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 == -1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("sticker ");
                sb3.append(str);
                sb3.append(" not found");
                Log.w(str2, sb3.toString());
                return;
            }
            final LiveStickerInfo liveStickerInfo = (LiveStickerInfo) FragmentLiveSticker.this.mStickerList.get(i2);
            liveStickerInfo.downloadState = i;
            if (i == 3 || i == 1) {
                CameraStatUtils.trackLiveStickerDownload(liveStickerInfo.name, true);
                Completable.fromAction(new Action() {
                    public void run() {
                        StringBuilder sb = new StringBuilder();
                        sb.append(FileUtils.STICKER_RESOURCE_DIR);
                        sb.append(liveStickerInfo.hash);
                        sb.append(".zip");
                        FileUtils.UnZipFileFolder(sb.toString(), FileUtils.STICKER_RESOURCE_DIR);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((CompletableObserver) new CompletableObserver() {
                    public void onComplete() {
                        FragmentLiveSticker.this.mAdapter.notifyItemChanged(i2);
                        FragmentLiveSticker fragmentLiveSticker = FragmentLiveSticker.this;
                        int i = fragmentLiveSticker.mFutureSelectIndex;
                        int i2 = i2;
                        if (i == i2) {
                            fragmentLiveSticker.onItemSelected(i2, null);
                        }
                    }

                    public void onError(Throwable th) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("unzip ");
                        sb.append(liveStickerInfo.hash);
                        sb.append(".zip failed");
                        Log.e(FragmentLiveSticker.TAG, sb.toString(), th);
                        liveStickerInfo.downloadState = 4;
                        FragmentLiveSticker.this.mAdapter.notifyItemChanged(i2);
                    }

                    public void onSubscribe(Disposable disposable) {
                    }
                });
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("download ");
                sb4.append(str);
                sb4.append(" failed, state = ");
                sb4.append(i);
                Log.e(str2, sb4.toString());
                CameraStatUtils.trackLiveStickerDownload(liveStickerInfo.name, false);
                FragmentLiveSticker.this.showNetworkErrorHint();
                FragmentLiveSticker.this.mAdapter.notifyItemChanged(i2);
            }
        }

        public void onProgressUpdate(String str, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("update ");
            sb.append(str);
            sb.append(": ");
            sb.append(i);
            Log.v(FragmentLiveSticker.TAG, sb.toString());
        }
    };
    int mFutureSelectIndex;
    private int mItemWidth;
    private LinearLayoutManagerWrapper mLayoutManager;
    private View mNoneItemView;
    private View mNoneSelectView;
    private View mRootView;
    int mSelectIndex = -1;
    /* access modifiers changed from: private */
    public List mStickerList = sPersistStickerList;
    private RecyclerView mStickerListView;
    private int mTotalWidth;
    private View mUpdatingView;

    class StickerItemAdapter extends Adapter {
        Context mContext;
        int mDegree;
        RequestOptions mGlideOptions = ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_live_sticker_placeholder));
        LayoutInflater mLayoutInflater;
        OnItemClickListener mListener;
        int mSelectIndex;
        List mStickerList;

        class StickerItemHolder extends CommonRecyclerViewHolder implements OnClickListener, OnDownloadSuccessListener {
            boolean mWaitingDownloadSuccess;

            public StickerItemHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                FolmeUtils.handleListItemTouch(view);
                ((LiveDownloadView) getView(R.id.item_download)).setOnDownloadSuccessListener(this);
            }

            public void onClick(View view) {
                int adapterPosition = getAdapterPosition();
                StickerItemAdapter stickerItemAdapter = StickerItemAdapter.this;
                if (adapterPosition != stickerItemAdapter.mSelectIndex) {
                    stickerItemAdapter.mListener.onItemClick(null, view, adapterPosition, getItemId());
                }
            }

            public void onDownloadSuccess(DownloadView downloadView) {
                int layoutPosition = getLayoutPosition();
                this.mWaitingDownloadSuccess = false;
                StickerItemAdapter.this.notifyItemChanged(layoutPosition);
            }
        }

        public StickerItemAdapter(Context context, List list, int i, OnItemClickListener onItemClickListener) {
            this.mContext = context;
            this.mStickerList = list;
            this.mSelectIndex = i;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mListener = onItemClickListener;
        }

        public int getItemCount() {
            return this.mStickerList.size();
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x0066  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0078  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0080  */
        /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x0062  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onBindViewHolder(StickerItemHolder stickerItemHolder, int i) {
            int downloadState;
            LiveDownloadView liveDownloadView = (LiveDownloadView) stickerItemHolder.getView(R.id.item_download);
            ImageView imageView = (ImageView) stickerItemHolder.getView(R.id.item_image);
            View view = stickerItemHolder.getView(R.id.item_selected_indicator);
            stickerItemHolder.itemView.setRotation((float) this.mDegree);
            LiveStickerInfo liveStickerInfo = (LiveStickerInfo) this.mStickerList.get(i);
            stickerItemHolder.itemView.setTag(liveStickerInfo);
            if (liveStickerInfo.isLocal) {
                int i2 = liveStickerInfo.iconId;
                if (i2 > 0) {
                    imageView.setImageResource(i2);
                    downloadState = liveStickerInfo.getDownloadState();
                    if (FragmentLiveSticker.sMoreSticker.hash.equals(liveStickerInfo.hash)) {
                        downloadState = 1;
                    }
                    if (downloadState == 3) {
                        stickerItemHolder.mWaitingDownloadSuccess = true;
                        liveStickerInfo.downloadState = 1;
                    }
                    if (!stickerItemHolder.mWaitingDownloadSuccess || downloadState != 5) {
                        liveDownloadView.setStateImage(downloadState);
                        if (i == this.mSelectIndex) {
                            view.setVisibility(0);
                        }
                    }
                    if (i == this.mSelectIndex) {
                        view.setVisibility(8);
                        return;
                    }
                    return;
                }
            }
            Glide.with(this.mContext).load(liveStickerInfo.icon).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
            downloadState = liveStickerInfo.getDownloadState();
            if (FragmentLiveSticker.sMoreSticker.hash.equals(liveStickerInfo.hash)) {
            }
            if (downloadState == 3) {
            }
            liveDownloadView.setStateImage(downloadState);
            if (i == this.mSelectIndex) {
            }
            if (i == this.mSelectIndex) {
            }
        }

        public StickerItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new StickerItemHolder(this.mLayoutInflater.inflate(R.layout.fragment_live_sticker_item, viewGroup, false));
        }

        public void setRotation(int i) {
            this.mDegree = i;
        }

        public void setSelectIndex(int i) {
            this.mSelectIndex = i;
        }
    }

    static {
        String str = "24991e783f23920397ac8aeed15994c2";
        sLocalStickerList = Util.isGlobalVersion() ? new LiveStickerInfo[]{new LiveStickerInfo("Film", str, "c34a7d7c2d512b3778c47c060bc10169.png"), new LiveStickerInfo("WhiteCat", "9b74385fe7e8cb81e1b88ce3b293bdf2", "9ec3702f2e5e96041e7e5b00d1fb39d2.png"), new LiveStickerInfo("ThreeScreens", "0a673064d64fce91ee41b405c6f74dca", "5063ed1901467ac59fb746d566fc22ad.png")} : new LiveStickerInfo[]{new LiveStickerInfo("瘦身", "0eb0e0214f7bc7f7bbfb4e9f4dba7f99", "f2e24fea41e33a1c0fc9a79b8d3b91e2.png", "保持全身在画面中哦"), new LiveStickerInfo("星空喵", "a75682e81788cc12f68682b9c9067f70", "8ca064318882fa610f4623b852accd36.png"), new LiveStickerInfo("浮生若梦", str, "e42237f75eeff4e5162f9b0130492e36.png")};
        String str2 = "";
        sNoneSticker = new LiveStickerInfo(str2, str2, "off.png");
        sMoreSticker = new LiveStickerInfo(str2, "_hide_more_", (int) R.drawable.ic_live_sticker_more);
        sPersistStickerList.add(sMoreSticker);
    }

    private void reload() {
        this.mUpdatingView.setVisibility(0);
        this.mStickerListView.setVisibility(4);
        this.mSelectIndex = -1;
        this.mAdapter.setSelectIndex(this.mSelectIndex);
        updateData();
    }

    private boolean scrollIfNeed(int i) {
        int i2 = (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) ? Math.max(0, i - 1) : (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) ? Math.min(i + 1, this.mLayoutManager.getItemCount() - 1) : i;
        if (i2 == i) {
            return false;
        }
        this.mLayoutManager.scrollToPosition(i2);
        return true;
    }

    private void setItemInCenter(int i) {
        this.mLayoutManager.scrollToPositionWithOffset(i, (this.mTotalWidth / 2) - (this.mItemWidth / 2));
    }

    /* access modifiers changed from: private */
    public void showNetworkErrorHint() {
        ToastUtils.showToast((Context) getActivity(), getResources().getString(R.string.live_sticker_network_error_hint), 80);
    }

    private void updateData() {
        String str = "default";
        new TTLiveStickerResourceRequest(CameraSettings.isLiveStickerInternalChannel() ? EffectConstants.CHANNEL_LOCAL_TEST : str, str).execute(!CameraSettings.isLiveStickerInternalChannel(), new ResponseListener() {
            public void onResponse(Object... objArr) {
                final List list = objArr[0];
                Completable.fromAction(new Action() {
                    public void run() {
                        File[] listFiles;
                        HashSet hashSet = new HashSet();
                        for (LiveStickerInfo liveStickerInfo : list) {
                            liveStickerInfo.downloadState = LiveResourceDownloadManager.getInstance().getDownloadState(liveStickerInfo.id);
                            liveStickerInfo.getDownloadState();
                            hashSet.add(liveStickerInfo.hash);
                            StringBuilder sb = new StringBuilder();
                            sb.append(liveStickerInfo.hash);
                            sb.append(".zip");
                            hashSet.add(sb.toString());
                        }
                        for (LiveStickerInfo liveStickerInfo2 : FragmentLiveSticker.sLocalStickerList) {
                            hashSet.add(liveStickerInfo2.hash);
                        }
                        for (File file : new File(FileUtils.STICKER_RESOURCE_DIR).listFiles()) {
                            String name = file.getName();
                            if (!hashSet.contains(name)) {
                                file.delete();
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("remove deprecated sticker ");
                                sb2.append(name);
                                Log.i(FragmentLiveSticker.TAG, sb2.toString());
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Action) new Action() {
                    public void run() {
                        FragmentLiveSticker.this.updateStickerList(list);
                    }
                });
                Object[] objArr2 = new Object[1];
                objArr2[0] = Integer.valueOf(list == null ? -1 : list.size());
                Log.d(FragmentLiveSticker.TAG, String.format("getStickerList %d ", objArr2));
            }

            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                final ArrayList arrayList = new ArrayList();
                int length = (10 - FragmentLiveSticker.sLocalStickerList.length) + 1;
                for (int i = 0; i < length; i++) {
                    LiveStickerInfo liveStickerInfo = new LiveStickerInfo();
                    liveStickerInfo.iconId = R.drawable.ic_live_sticker_placeholder;
                    liveStickerInfo.url = "https://mi.com/";
                    liveStickerInfo.hash = "0123456789abcdef0123456789abcdef";
                    liveStickerInfo.id = String.valueOf(i);
                    arrayList.add(liveStickerInfo);
                }
                Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe((Action) new Action() {
                    public void run() {
                        FragmentLiveSticker.this.updateStickerList(arrayList);
                    }
                });
                Log.e(FragmentLiveSticker.TAG, String.format("errorCode %d msg: %s", new Object[]{Integer.valueOf(errorCode.CODE), str}));
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateStickerList(List list) {
        this.mStickerList.clear();
        this.mStickerList.addAll(Arrays.asList(sLocalStickerList));
        if (list != null) {
            this.mStickerList.addAll(list);
            boolean isPackageAvailable = Util.isPackageAvailable(getActivity(), PACKAGE_NAME);
            if (!Util.isGlobalVersion() && DataRepository.dataItemGlobal().isTiktokMoreButtonEnabled(isPackageAvailable)) {
                this.mStickerList.add(sMoreSticker);
            }
            sPersistStickerList = this.mStickerList;
        }
        this.mSelectIndex = -1;
        String currentLiveSticker = CameraSettings.getCurrentLiveSticker();
        if (currentLiveSticker != null) {
            int i = 0;
            while (true) {
                if (i >= this.mStickerList.size()) {
                    break;
                } else if (currentLiveSticker.equals(((LiveStickerInfo) this.mStickerList.get(i)).hash)) {
                    this.mSelectIndex = i;
                    break;
                } else {
                    i++;
                }
            }
        }
        this.mNoneSelectView.setVisibility(this.mSelectIndex == -1 ? 0 : 8);
        this.mAdapter.setSelectIndex(this.mSelectIndex);
        this.mAdapter.notifyDataSetChanged();
        setItemInCenter(this.mSelectIndex);
        this.mUpdatingView.setVisibility(8);
        this.mStickerListView.setVisibility(0);
    }

    public int getFragmentInto() {
        return 4092;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_live_sticker;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mRootView = view;
        this.mItemWidth = getResources().getDimensionPixelSize(R.dimen.live_sticker_item_size);
        this.mTotalWidth = getResources().getDisplayMetrics().widthPixels;
        final boolean isLayoutRTL = Util.isLayoutRTL(getContext());
        this.mUpdatingView = this.mRootView.findViewById(R.id.live_sticker_updating);
        this.mStickerListView = (RecyclerView) this.mRootView.findViewById(R.id.live_sticker_list);
        this.mStickerListView.setFocusable(false);
        this.mNoneItemView = this.mRootView.findViewById(R.id.live_sticker_none_item);
        this.mNoneSelectView = this.mRootView.findViewById(R.id.live_sticker_none_selected_indicator);
        this.mSelectIndex = -1;
        String currentLiveSticker = CameraSettings.getCurrentLiveSticker();
        if (currentLiveSticker != null) {
            int i = 0;
            while (true) {
                if (i >= this.mStickerList.size()) {
                    break;
                } else if (currentLiveSticker.equals(((LiveStickerInfo) this.mStickerList.get(i)).hash)) {
                    this.mSelectIndex = i;
                    break;
                } else {
                    i++;
                }
            }
        }
        this.mNoneSelectView.setVisibility(this.mSelectIndex == -1 ? 0 : 8);
        this.mNoneItemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_STICKER_OFF);
                FragmentLiveSticker.this.onItemSelected(-1, null);
            }
        });
        this.mAdapter = new StickerItemAdapter(getContext(), this.mStickerList, this.mSelectIndex, new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                FragmentLiveSticker.this.onItemSelected(i, view);
            }
        });
        this.mAdapter.setRotation(this.mDegree);
        this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "live_sticker_list");
        this.mLayoutManager.setOrientation(0);
        this.mStickerListView.setLayoutManager(this.mLayoutManager);
        this.mStickerListView.addItemDecoration(new ItemDecoration() {
            final int mLeftMargin = FragmentLiveSticker.this.getResources().getDimensionPixelSize(R.dimen.live_sticker_list_margin_left);
            final int mRightMargin = FragmentLiveSticker.this.getResources().getDimensionPixelSize(R.dimen.live_sticker_list_margin_right);

            public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
                int i;
                int i2;
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                if (isLayoutRTL) {
                    if (childAdapterPosition == 0) {
                        i2 = this.mLeftMargin;
                    } else if (childAdapterPosition + 1 == recyclerView.getAdapter().getItemCount()) {
                        i = this.mRightMargin;
                        rect.set(i, 0, 0, 0);
                        return;
                    } else {
                        return;
                    }
                } else if (childAdapterPosition == 0) {
                    i = this.mLeftMargin;
                    rect.set(i, 0, 0, 0);
                    return;
                } else if (childAdapterPosition + 1 == recyclerView.getAdapter().getItemCount()) {
                    i2 = this.mRightMargin;
                } else {
                    return;
                }
                rect.set(0, 0, i2, 0);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.mStickerListView.setItemAnimator(defaultItemAnimator);
        this.mStickerListView.setAdapter(this.mAdapter);
        setItemInCenter(this.mSelectIndex);
        LiveResourceDownloadManager.getInstance().addDownloadListener(this.mDownloadListener);
        if (this.mSelectIndex == -1) {
            reload();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        LiveResourceDownloadManager.getInstance().removeDownloadListener(this.mDownloadListener);
    }

    /* access modifiers changed from: protected */
    public void onItemSelected(final int i, final View view) {
        this.mFutureSelectIndex = i;
        final LiveStickerInfo liveStickerInfo = i >= 0 ? (LiveStickerInfo) this.mStickerList.get(i) : sNoneSticker;
        int downloadState = liveStickerInfo.getDownloadState();
        StringBuilder sb = new StringBuilder();
        sb.append("select sticker ");
        sb.append(i);
        sb.append(": ");
        sb.append(liveStickerInfo.hash);
        String str = ", ";
        sb.append(str);
        sb.append(downloadState);
        sb.append(str);
        sb.append(liveStickerInfo.isLocal);
        sb.append(str);
        sb.append(liveStickerInfo.url);
        Log.v(TAG, sb.toString());
        boolean z = true;
        if (liveStickerInfo == sMoreSticker) {
            PackageManager packageManager = getActivity().getPackageManager();
            String str2 = "android.intent.action.VIEW";
            Intent intent = new Intent(str2, Uri.parse(MORE_URI));
            if (packageManager.queryIntentActivities(intent, 65536).size() > 0) {
                try {
                    if (packageManager.getPackageInfo(PACKAGE_NAME, 0).versionCode < 320) {
                        intent = new Intent(str2, Uri.parse(MAIN_URI));
                    }
                    z = false;
                } catch (NameNotFoundException unused) {
                }
            }
            if (z) {
                intent = new Intent(str2, Uri.parse(MARKET_URI));
            }
            CameraStatUtils.trackLiveStickerMore(z);
            startActivity(intent);
        } else if (liveStickerInfo.isLocal || downloadState == 5) {
            int i2 = this.mSelectIndex;
            this.mSelectIndex = i;
            this.mNoneSelectView.setVisibility(liveStickerInfo == sNoneSticker ? 0 : 8);
            this.mAdapter.setSelectIndex(this.mSelectIndex);
            this.mAdapter.notifyItemChanged(i2);
            this.mAdapter.notifyItemChanged(this.mSelectIndex);
            scrollIfNeed(this.mSelectIndex);
            CameraSettings.setCurrentLiveSticker(liveStickerInfo.hash, liveStickerInfo.name, liveStickerInfo.hint);
            StickerProtocol stickerProtocol = (StickerProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(178);
            if (stickerProtocol != null) {
                stickerProtocol.onStickerChanged(liveStickerInfo.hash);
            }
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                String str3 = liveStickerInfo.hint;
                String str4 = "";
                if (str3 == null || str3.equals(str4)) {
                    String str5 = liveStickerInfo.hintIcon;
                    if (str5 == null || str5.equals(str4)) {
                        mainContentProtocol.setCenterHint(8, null, null, 0);
                        return;
                    }
                }
                mainContentProtocol.setCenterHint(0, liveStickerInfo.hint, liveStickerInfo.hintIcon, 5000);
            }
        } else if (downloadState == 1 || downloadState == 3) {
            Completable.fromAction(new Action() {
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileUtils.STICKER_RESOURCE_DIR);
                    sb.append(liveStickerInfo.hash);
                    sb.append(".zip");
                    String sb2 = sb.toString();
                    try {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(FileUtils.STICKER_RESOURCE_DIR);
                        sb3.append(liveStickerInfo.hash);
                        Util.verifyZip(sb2, sb3.toString(), 32768);
                    } catch (IOException e) {
                        new File(sb2).delete();
                        throw e;
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((CompletableObserver) new CompletableObserver() {
                public void onComplete() {
                    liveStickerInfo.downloadState = 5;
                    FragmentLiveSticker.this.onItemSelected(i, view);
                }

                public void onError(Throwable th) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("verify ");
                    sb.append(liveStickerInfo.hash);
                    sb.append(".zip failed");
                    Log.e(FragmentLiveSticker.TAG, sb.toString(), th);
                    liveStickerInfo.downloadState = 0;
                    FragmentLiveSticker.this.mAdapter.notifyItemChanged(i);
                    FragmentLiveSticker.this.onItemSelected(i, view);
                }

                public void onSubscribe(Disposable disposable) {
                }
            });
        } else if (downloadState != 2 && CtaNoticeFragment.checkCta(getActivity().getFragmentManager(), this, 1)) {
            liveStickerInfo.downloadState = 2;
            scrollIfNeed(i);
            this.mAdapter.notifyItemChanged(i);
            LiveResourceDownloadManager.getInstance().download(liveStickerInfo, this.mDownloadHelper);
        }
    }

    public void onNegativeClick(DialogInterface dialogInterface, int i) {
    }

    public void onPositiveClick(DialogInterface dialogInterface, int i) {
        reload();
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mStickerListView != null) {
            for (int i2 = 0; i2 < this.mStickerListView.getChildCount(); i2++) {
                list.add(this.mStickerListView.getChildAt(i2));
            }
        }
        StickerItemAdapter stickerItemAdapter = this.mAdapter;
        if (stickerItemAdapter != null) {
            stickerItemAdapter.setRotation(i);
            int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
            for (int i3 = 0; i3 < findFirstVisibleItemPosition; i3++) {
                this.mAdapter.notifyItemChanged(i3);
            }
            while (true) {
                findLastVisibleItemPosition++;
                if (findLastVisibleItemPosition < this.mAdapter.getItemCount()) {
                    this.mAdapter.notifyItemChanged(findLastVisibleItemPosition);
                } else {
                    return;
                }
            }
        }
    }
}
