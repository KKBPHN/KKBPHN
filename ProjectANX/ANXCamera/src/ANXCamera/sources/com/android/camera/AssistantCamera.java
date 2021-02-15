package com.android.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

public class AssistantCamera extends Activity implements OnRequestPermissionsResultCallback {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        String str = CameraIntentManager.EXTRA_FROM_VOICE_ROOT;
        if (intent.hasExtra(str)) {
            intent.removeExtra(str);
        }
        if (isVoiceInteractionRoot()) {
            intent.putExtra(str, true);
        }
        int hashCode = intent.hashCode();
        intent.putExtra(CameraIntentManager.EXTRA_ASSISTANT_HASH, hashCode);
        KeyKeeper.getInstance().setAssistantHash(hashCode);
        startCamera(getIntent());
        finish();
    }

    public void startCamera(Intent intent) {
        intent.setClass(this, Camera.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
