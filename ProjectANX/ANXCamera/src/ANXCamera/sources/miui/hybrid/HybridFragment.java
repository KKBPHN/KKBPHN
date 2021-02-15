package miui.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.internal.R;
import com.miui.internal.hybrid.HybridManager;
import java.util.HashSet;
import java.util.Set;
import miui.app.Fragment;

public class HybridFragment extends Fragment {
    private Set mHybridViews = new HashSet();

    private void destroyHybridView() {
        for (HybridView hybridView : this.mHybridViews) {
            if (hybridView != null) {
                if (hybridView.getParent() != null) {
                    ((ViewGroup) hybridView.getParent()).removeView(hybridView);
                }
                hybridView.destroy();
            }
        }
        this.mHybridViews.clear();
    }

    /* access modifiers changed from: protected */
    public int getConfigResId() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public View getContentView() {
        return getActivity().getLayoutInflater().inflate(R.layout.hybrid_main, null);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onActivityResult(i, i2, intent);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onDestroy();
        }
        destroyHybridView();
    }

    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return getContentView();
    }

    public void onPause() {
        super.onPause();
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onPause();
        }
    }

    public void onResume() {
        super.onResume();
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onResume();
        }
    }

    public void onStart() {
        super.onStart();
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onStart();
        }
    }

    public void onStop() {
        super.onStop();
        for (HybridView hybridManager : this.mHybridViews) {
            hybridManager.getHybridManager().onStop();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(R.id.hybrid_view);
        if (findViewById != null && (findViewById instanceof HybridView)) {
            String str = null;
            String str2 = HybridActivity.EXTRA_URL;
            if (bundle != null) {
                str = bundle.getString(str2);
            }
            if (str == null) {
                Intent intent = getActivity().getIntent();
                if (intent != null) {
                    str = intent.getStringExtra(str2);
                }
            }
            registerHybridView((HybridView) findViewById, getConfigResId(), str);
        }
    }

    /* access modifiers changed from: protected */
    public final void registerHybridView(View view) {
        registerHybridView(view, getConfigResId());
    }

    /* access modifiers changed from: protected */
    public final void registerHybridView(View view, int i) {
        registerHybridView(view, i, null);
    }

    /* access modifiers changed from: protected */
    public final void registerHybridView(View view, int i, String str) {
        if (view instanceof HybridView) {
            HybridView hybridView = (HybridView) view;
            HybridManager hybridManager = new HybridManager(getActivity(), hybridView);
            hybridView.setHybridManager(hybridManager);
            this.mHybridViews.add(hybridView);
            hybridManager.init(i, str);
            return;
        }
        throw new IllegalArgumentException("view being registered is not a hybrid view");
    }

    /* access modifiers changed from: protected */
    public final void unregisterHybridView(View view) {
        if (view instanceof HybridView) {
            this.mHybridViews.remove(view);
            return;
        }
        throw new IllegalArgumentException("view being unregistered is not a hybrid view");
    }
}
