package com.android.camera.protocol;

import android.util.SparseArray;
import androidx.annotation.Nullable;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import java.util.concurrent.ConcurrentHashMap;

public class ModeCoordinatorImpl implements ModeCoordinator {
    private static ModeCoordinatorImpl sInstance;
    private SparseArray featureLocalMap;
    private int mHolderKey;
    private ConcurrentHashMap protocolMap = new ConcurrentHashMap();

    public static void create(int i) {
        getInstance();
        sInstance.mHolderKey = i;
    }

    @Deprecated
    public static void destroyAll(int i) {
        ModeCoordinatorImpl modeCoordinatorImpl = sInstance;
        if (modeCoordinatorImpl != null && modeCoordinatorImpl.mHolderKey == i) {
            modeCoordinatorImpl.destroyWorkspace();
            sInstance = null;
        }
    }

    private void destroyWorkspace() {
        this.protocolMap.clear();
    }

    public static void forceDestroy() {
        ModeCoordinatorImpl modeCoordinatorImpl = sInstance;
        if (modeCoordinatorImpl != null) {
            modeCoordinatorImpl.destroyWorkspace();
            sInstance = null;
        }
    }

    public static ModeCoordinatorImpl getInstance() {
        if (sInstance == null) {
            synchronized (ModeCoordinatorImpl.class) {
                if (sInstance == null) {
                    sInstance = new ModeCoordinatorImpl();
                }
            }
        }
        return sInstance;
    }

    public static boolean isAlive(int i) {
        ModeCoordinatorImpl modeCoordinatorImpl = sInstance;
        return modeCoordinatorImpl != null && modeCoordinatorImpl.mHolderKey == i;
    }

    private BaseProtocol nullCatcher(Class cls) {
        try {
            return (BaseProtocol) cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public void attachProtocol(int i, @Nullable BaseProtocol baseProtocol) {
        this.protocolMap.put(Integer.valueOf(i), baseProtocol);
    }

    public void detachProtocol(int i, BaseProtocol baseProtocol) {
        if (getAttachProtocol(i) == baseProtocol) {
            this.protocolMap.remove(Integer.valueOf(i));
        }
    }

    public int getActiveProtocolSize() {
        return this.protocolMap.size();
    }

    public BaseProtocol getAttachProtocol(int i) {
        return (BaseProtocol) this.protocolMap.get(Integer.valueOf(i));
    }
}
