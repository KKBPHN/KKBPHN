package com.android.camera;

import android.view.View;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import io.reactivex.Completable;
import java.util.ArrayList;

public class CommonRotationLayout {
    private View mBottomRotationLayout;
    private View mContentRotationLayout;
    private int mLastModuleIndex;
    private int mLastTargetViewDegree;
    private View mScreenRotationLayout;
    private View mTopRotationLayout;
    private Window mWindow;

    public CommonRotationLayout(Window window) {
        this.mWindow = window;
        initView();
    }

    private View findViewById(int i) {
        return this.mWindow.findViewById(i);
    }

    private void initView() {
        this.mContentRotationLayout = findViewById(R.id.content_rotation_layout);
        this.mTopRotationLayout = findViewById(R.id.top_rotation_layout);
        this.mBottomRotationLayout = findViewById(R.id.bottom_rotation_layout);
        this.mScreenRotationLayout = findViewById(R.id.full_screen_rotation_layout);
    }

    private boolean isForceHorizontalOrientation(int i) {
        return i == 185 || i == 179 || i == 212 || i == 189 || i == 207 || i == 213 || i == 208;
    }

    public static void updateOrientationLayoutRotation(int i, View... viewArr) {
        if (viewArr != null) {
            ArrayList arrayList = new ArrayList();
            for (View view : viewArr) {
                view.setAlpha(0.0f);
                arrayList.add(Completable.create(new FolmeAlphaInOnSubscribe(view)));
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                int screenOrientation = Display.getScreenOrientation();
                if (i == 0) {
                    screenOrientation = 0;
                }
                if (screenOrientation == 0) {
                    layoutParams.width = Display.getWindowWidth();
                    layoutParams.height = Display.getWindowHeight();
                    layoutParams.gravity = 0;
                } else if (screenOrientation == 1 || screenOrientation == 2) {
                    layoutParams.width = Display.getWindowWidth();
                    layoutParams.height = Display.getWindowWidth();
                    layoutParams.gravity = 16;
                }
                view.setRotation((float) i);
                view.setLayoutParams(layoutParams);
            }
            Completable.merge((Iterable) arrayList).subscribe();
        }
    }

    public void provideOrientationChanged(int i, int i2) {
        int i3 = (360 - i2) % m.cQ;
        if (!isForceHorizontalOrientation(this.mLastModuleIndex) && !isForceHorizontalOrientation(i) && i3 == this.mLastTargetViewDegree) {
            return;
        }
        if (!isForceHorizontalOrientation(this.mLastModuleIndex) || !isForceHorizontalOrientation(i)) {
            if (isForceHorizontalOrientation(i)) {
                updateOrientationLayoutRotation(0, this.mTopRotationLayout);
            } else {
                updateOrientationLayoutRotation(i3, this.mTopRotationLayout);
            }
            updateOrientationLayoutRotation(i3, this.mContentRotationLayout, this.mBottomRotationLayout, this.mScreenRotationLayout);
            this.mLastTargetViewDegree = i3;
            this.mLastModuleIndex = i;
        }
    }
}
