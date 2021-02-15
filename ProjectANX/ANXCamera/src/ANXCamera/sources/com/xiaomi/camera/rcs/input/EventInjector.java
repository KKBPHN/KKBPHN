package com.xiaomi.camera.rcs.input;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.core.view.InputDeviceCompat;
import com.xiaomi.camera.rcs.util.RCSDebug;
import java.lang.reflect.Method;

public class EventInjector {
    private static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;
    private static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;
    private static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;
    public static final String INPUT_COMMAND_STRING = "input";
    public static final int MAXIMUM_COMMAND_LENGTH = 100;
    public static final int MINIMUM_COMMAND_LENGTH = 5;
    private static final String TAG = RCSDebug.createTag(EventInjector.class);
    private final InputManager mInputManager;

    public EventInjector(Context context) {
        this.mInputManager = (InputManager) context.getSystemService(INPUT_COMMAND_STRING);
    }

    public EventInjector(InputManager inputManager) {
        this.mInputManager = inputManager;
    }

    private void injectInputEvent(InputEvent inputEvent) {
        Object[] objArr = {inputEvent, Integer.valueOf(2)};
        try {
            Method method = this.mInputManager.getClass().getMethod("injectInputEvent", new Class[]{InputEvent.class, Integer.TYPE});
            method.setAccessible(true);
            method.invoke(this.mInputManager, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectKeyEvent(KeyEvent keyEvent) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("injectKeyEvent: ");
        sb.append(keyEvent);
        RCSDebug.i(str, sb.toString());
        injectInputEvent(keyEvent);
    }

    private void injectMotionEvent(int i, int i2, long j, float f, float f2, float f3) {
        MotionEvent obtain = MotionEvent.obtain(j, j, i2, f, f2, f3, 1.0f, 0, 1.0f, 1.0f, 0, 0);
        int i3 = i;
        obtain.setSource(i);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("injectMotionEvent: ");
        sb.append(obtain);
        RCSDebug.i(str, sb.toString());
        injectInputEvent(obtain);
    }

    private static final float lerp(float f, float f2, float f3) {
        return ((f2 - f) * f3) + f;
    }

    private void sendKeyEvent(int i, int i2, boolean z) {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = uptimeMillis;
        KeyEvent keyEvent = r1;
        KeyEvent keyEvent2 = new KeyEvent(uptimeMillis, uptimeMillis, 0, i2, 0, 0, -1, 0, 0, i);
        injectKeyEvent(keyEvent);
        if (z) {
            KeyEvent keyEvent3 = new KeyEvent(j, j, 0, i2, 1, 0, -1, 0, 128, i);
            injectKeyEvent(keyEvent3);
        }
        KeyEvent keyEvent4 = new KeyEvent(j, j, 1, i2, 0, 0, -1, 0, 0, i);
        injectKeyEvent(keyEvent4);
    }

    private void sendMove(int i, float f, float f2) {
        injectMotionEvent(i, 2, SystemClock.uptimeMillis(), f, f2, 0.0f);
    }

    private void sendSwipe(int i, float f, float f2, float f3, float f4, int i2) {
        int i3 = i2 < 0 ? 300 : i2;
        long uptimeMillis = SystemClock.uptimeMillis();
        injectMotionEvent(i, 0, uptimeMillis, f, f2, 1.0f);
        long j = ((long) i3) + uptimeMillis;
        long j2 = uptimeMillis;
        while (j2 < j) {
            float f5 = ((float) (j2 - uptimeMillis)) / ((float) i3);
            injectMotionEvent(i, 2, j2, lerp(f, f3, f5), lerp(f2, f4, f5), 1.0f);
            j2 = SystemClock.uptimeMillis();
        }
        float f6 = f3;
        float f7 = f4;
        injectMotionEvent(i, 1, j2, f3, f4, 0.0f);
    }

    private void sendTap(int i, float f, float f2) {
        int i2 = i;
        long uptimeMillis = SystemClock.uptimeMillis();
        float f3 = f;
        float f4 = f2;
        injectMotionEvent(i2, 0, uptimeMillis, f3, f4, 1.0f);
        injectMotionEvent(i2, 1, uptimeMillis, f3, f4, 0.0f);
    }

    private void sendText(int i, String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        int i2 = 0;
        boolean z = false;
        while (i2 < stringBuffer.length()) {
            if (z) {
                if (stringBuffer.charAt(i2) == 's') {
                    stringBuffer.setCharAt(i2, ' ');
                    i2--;
                    stringBuffer.deleteCharAt(i2);
                }
                z = false;
            }
            if (stringBuffer.charAt(i2) == '%') {
                z = true;
            }
            i2++;
        }
        KeyEvent[] events = KeyCharacterMap.load(-1).getEvents(stringBuffer.toString().toCharArray());
        for (KeyEvent keyEvent : events) {
            keyEvent.setSource(i);
            injectKeyEvent(keyEvent);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
        if (r0 == false) goto L_0x00ba;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0049, code lost:
        if (r0 == true) goto L_0x009a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004b, code lost:
        if (r0 == true) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004d, code lost:
        if (r0 == true) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004f, code lost:
        r7 = TAG;
        r8 = new java.lang.StringBuilder();
        r8.append("Unknown command: ");
        r8.append(r3);
        com.xiaomi.camera.rcs.util.RCSDebug.e(r7, r8.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0065, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0068, code lost:
        if (r8.length != 6) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006a, code lost:
        swipe(java.lang.Float.parseFloat(r8[2]), java.lang.Float.parseFloat(r8[3]), java.lang.Float.parseFloat(r8[4]), java.lang.Float.parseFloat(r8[5]));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0086, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0088, code lost:
        if (r8.length != 4) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008a, code lost:
        tap(java.lang.Float.parseFloat(r8[2]), java.lang.Float.parseFloat(r8[3]));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0099, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009b, code lost:
        if (r8.length != 3) goto L_0x00a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x009d, code lost:
        keyEvent(java.lang.Integer.parseInt(r8[2]));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a6, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a8, code lost:
        if (r8.length != 4) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00aa, code lost:
        keyEvent(java.lang.Integer.parseInt(r8[2]), java.lang.Boolean.parseBoolean(r8[3]));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b9, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bb, code lost:
        if (r8.length != 3) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00bd, code lost:
        text(r8[2]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00c2, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute(String[] strArr) {
        String str;
        if (strArr.length >= 3) {
            boolean z = false;
            if (INPUT_COMMAND_STRING.equalsIgnoreCase(strArr[0])) {
                str = strArr[1];
                try {
                    switch (str.hashCode()) {
                        case 114595:
                            if (str.equals("tap")) {
                                z = true;
                                break;
                            }
                        case 3556653:
                            if (str.equals("text")) {
                                break;
                            }
                        case 109854522:
                            if (str.equals("swipe")) {
                                z = true;
                                break;
                            }
                        case 506722203:
                            if (str.equals("keyevent")) {
                                z = true;
                                break;
                            }
                    }
                } catch (NumberFormatException e) {
                    RCSDebug.e(TAG, "Invalid keycode", e);
                }
            }
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid arguments for command: ");
        sb.append(str);
        RCSDebug.e(str2, sb.toString());
    }

    public void keyEvent(int i) {
        keyEvent(i, false);
    }

    public void keyEvent(int i, boolean z) {
        sendKeyEvent(257, i, z);
    }

    public void press() {
        sendTap(InputDeviceCompat.SOURCE_TRACKBALL, 0.0f, 0.0f);
    }

    public void roll(float f, float f2) {
        sendMove(InputDeviceCompat.SOURCE_TRACKBALL, f, f2);
    }

    public void swipe(float f, float f2, float f3, float f4) {
        swipe(f, f2, f3, f4, -1);
    }

    public void swipe(float f, float f2, float f3, float f4, int i) {
        sendSwipe(InputDeviceCompat.SOURCE_TOUCHPAD, f, f2, f3, f4, i);
    }

    public void tap(float f, float f2) {
        sendTap(InputDeviceCompat.SOURCE_TOUCHPAD, f, f2);
    }

    public void text(String str) {
        sendText(257, str);
    }
}
