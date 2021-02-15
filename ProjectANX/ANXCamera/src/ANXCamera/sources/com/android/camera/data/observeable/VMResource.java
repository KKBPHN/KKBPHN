package com.android.camera.data.observeable;

import android.app.Activity;
import android.widget.Toast;
import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import com.android.camera.R;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.log.Log;
import com.android.camera.multi.PluginInfo;
import com.android.camera.multi.PluginInfoRequest;
import com.android.camera.network.NetworkDependencies;
import com.android.camera.resource.BaseResourceItem;
import com.android.camera.statistic.CameraStatUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;

public class VMResource extends VMBase {
    private static final String TAG = "VMResource";
    private CompositeDisposable mDisposable;
    public RxData mRxDownloadState = new RxData(new HashMap());

    private void removeItem(BaseResourceItem baseResourceItem) {
        ((HashMap) this.mRxDownloadState.get()).remove(baseResourceItem.id);
    }

    public /* synthetic */ void O000000o(BaseResourceItem baseResourceItem, Throwable th) {
        th.printStackTrace();
        CameraStatUtils.trackResourceDownloadResult(baseResourceItem.id, 4);
        StringBuilder sb = new StringBuilder();
        sb.append("download error");
        sb.append(th.toString());
        Log.e(TAG, sb.toString());
        updateItemState(baseResourceItem, Integer.valueOf(4));
        updateItemState(baseResourceItem, Integer.valueOf(0));
    }

    public /* synthetic */ void O00000Oo(BaseResourceItem baseResourceItem) {
        CameraStatUtils.trackResourceDownloadResult(baseResourceItem.id, 5);
        StringBuilder sb = new StringBuilder();
        sb.append("download ok: ");
        sb.append(baseResourceItem.id);
        Log.d(TAG, sb.toString());
        updateItemState(baseResourceItem, Integer.valueOf(5));
        removeItem(baseResourceItem);
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return false;
    }

    public void onDestroy() {
        CompositeDisposable compositeDisposable = this.mDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
    }

    @MainThread
    public Disposable startAndGetDownloadDisposable(BaseResourceItem baseResourceItem, Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append("check :");
        sb.append(baseResourceItem.id);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (!CtaNoticeFragment.checkCta(activity.getFragmentManager(), null, 5)) {
            Log.d(str, "check cta");
            return null;
        } else if (!NetworkDependencies.isConnected(activity)) {
            Log.d(str, "check network");
            Toast.makeText(activity, R.string.live_music_network_exception, 0).show();
            return null;
        } else {
            CameraStatUtils.trackResourceDownloadStart(baseResourceItem.id);
            updateItemState(baseResourceItem, Integer.valueOf(2));
            Disposable subscribe = new PluginInfoRequest(baseResourceItem.getDownloadUrl(), baseResourceItem).startObservable(PluginInfo.class).flatMap(C0136O00000oO.INSTANCE).observeOn(Schedulers.io()).flatMap(O00000Oo.INSTANCE).observeOn(AndroidSchedulers.mainThread()).subscribe(new O00000o0(this), new O00000o(this, baseResourceItem));
            if (this.mDisposable == null) {
                this.mDisposable = new CompositeDisposable();
            }
            CompositeDisposable compositeDisposable = this.mDisposable;
            compositeDisposable.add(compositeDisposable);
            return subscribe;
        }
    }

    @MainThread
    public void startObservable(LifecycleOwner lifecycleOwner, Consumer consumer) {
        this.mRxDownloadState.observable(lifecycleOwner).subscribe(consumer);
    }

    @MainThread
    public void updateItemState(BaseResourceItem baseResourceItem, Integer num) {
        baseResourceItem.setState(num.intValue());
        HashMap hashMap = (HashMap) this.mRxDownloadState.get();
        hashMap.put(baseResourceItem.id, num);
        this.mRxDownloadState.set(hashMap);
        judge();
    }
}
