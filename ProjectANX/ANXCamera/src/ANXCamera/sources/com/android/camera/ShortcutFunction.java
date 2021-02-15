package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo.Builder;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import com.android.camera.CameraIntentManager.CameraExtras;
import com.android.camera.CameraIntentManager.CameraMode;
import java.util.ArrayList;
import java.util.List;

public class ShortcutFunction {
    private List infos;
    private ShortcutManager mShortcutManager;

    private void setupShortcutsCapture(Context context) {
        this.mShortcutManager = (ShortcutManager) context.getSystemService(ShortcutManager.class);
        this.infos = new ArrayList();
        Intent intent = new Intent();
        String str = "CAPTURE";
        intent.putExtra(CameraExtras.CAMERA_MODE, str);
        intent.putExtra(CameraExtras.USE_FRONT_CAMERA, true);
        intent.setAction(CameraIntentManager.ACTION_VOICE_CONTROL);
        this.infos.add(new Builder(context, str).setShortLabel(context.getResources().getString(R.string.capture_shortcut_short)).setIcon(Icon.createWithResource(context, R.drawable.shortcut_capture_mode)).setIntent(intent).build());
        this.mShortcutManager.setDynamicShortcuts(this.infos);
    }

    private void setupShortcutsDoc(Context context) {
        Intent intent = new Intent();
        String str = CameraMode.DOC;
        intent.putExtra(CameraExtras.CAMERA_MODE, str);
        intent.setAction(CameraIntentManager.ACTION_VOICE_CONTROL);
        this.infos.add(new Builder(context, str).setShortLabel(context.getResources().getString(R.string.doc_shortcut_short)).setIcon(Icon.createWithResource(context, R.drawable.shortcut_document_mode)).setIntent(intent).build());
        this.mShortcutManager.setDynamicShortcuts(this.infos);
    }

    private void setupShortcutsManual(Context context) {
        Intent intent = new Intent();
        String str = CameraMode.MANUAL;
        intent.putExtra(CameraExtras.CAMERA_MODE, str);
        intent.setAction(CameraIntentManager.ACTION_VOICE_CONTROL);
        this.infos.add(new Builder(context, str).setShortLabel(context.getResources().getString(R.string.professional_shortcut_short)).setIcon(Icon.createWithResource(context, R.drawable.shortcut_professional_mode)).setIntent(intent).build());
        this.mShortcutManager.setDynamicShortcuts(this.infos);
    }

    private void setupShortcutsVideo(Context context) {
        Intent intent = new Intent();
        String str = CameraMode.VIDEO;
        intent.putExtra(CameraExtras.CAMERA_MODE, str);
        intent.setAction(CameraIntentManager.ACTION_VOICE_CONTROL);
        this.infos.add(new Builder(context, str).setShortLabel(context.getResources().getString(R.string.video_shortcut_short)).setIcon(Icon.createWithResource(context, R.drawable.shortcut_video_mode)).setIntent(intent).build());
        this.mShortcutManager.setDynamicShortcuts(this.infos);
    }

    public void initShortcut(Context context) {
        setupShortcutsCapture(context);
        setupShortcutsVideo(context);
        setupShortcutsManual(context);
        if (C0122O00000o.instance().OO0ooo() || C0122O00000o.instance().OO0ooo0()) {
            setupShortcutsDoc(context);
        }
    }
}
