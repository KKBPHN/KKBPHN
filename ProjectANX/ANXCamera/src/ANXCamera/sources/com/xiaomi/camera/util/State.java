package com.xiaomi.camera.util;

import android.os.Message;

public class State implements IState {
    protected State() {
    }

    public void enter() {
    }

    public void exit() {
    }

    public String getName() {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf(36) + 1);
    }

    public boolean processMessage(Message message) {
        return false;
    }
}
