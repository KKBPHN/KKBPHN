package com.android.camera.fragment;

import android.view.View;
import android.view.ViewGroup;
import com.android.camera.R;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ModuleContent;

public class FragmentModuleContent extends BaseFragment implements ModuleContent {
    private static final String TAG = "FragmentModuleContent";
    private ViewGroup mParent;

    public int getFragmentInto() {
        return BaseFragmentDelegate.MODULE_CONTENT;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_module_content;
    }

    public ViewGroup getParent() {
        return this.mParent;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mParent = (ViewGroup) view;
        ModeCoordinatorImpl.getInstance().attachProtocol(431, this);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        this.mParent.removeAllViews();
        super.onDestroy();
    }

    public void onDestroyView() {
        ModeCoordinatorImpl.getInstance().detachProtocol(431, this);
        super.onDestroyView();
    }
}
