package com.android.camera.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public static void addFragmentWithTag(FragmentActivity fragmentActivity, int i, Fragment fragment, String str) {
        if (!fragmentActivity.isFinishing()) {
            FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(i, fragment, str);
            try {
                beginTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addFragmentWithTag(FragmentManager fragmentManager, int i, Fragment fragment, String str) {
        if (fragmentManager != null) {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.replace(i, fragment, str);
            try {
                beginTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static Fragment getFragmentByTag(FragmentActivity fragmentActivity, String str) {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(str);
    }

    public static Fragment getFragmentByTag(FragmentManager fragmentManager, String str) {
        return fragmentManager.findFragmentByTag(str);
    }

    public static boolean hasFragment(FragmentManager fragmentManager, String str) {
        return fragmentManager.findFragmentByTag(str) != null;
    }

    public static boolean removeFragmentByTag(FragmentActivity fragmentActivity, String str) {
        if (!fragmentActivity.isFinishing() && str != null) {
            Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag(str);
            if (findFragmentByTag != null) {
                FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                beginTransaction.remove(findFragmentByTag);
                try {
                    beginTransaction.commitAllowingStateLoss();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public static boolean removeFragmentByTag(FragmentManager fragmentManager, String str) {
        if (!(fragmentManager == null || str == null)) {
            Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
            if (findFragmentByTag != null) {
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.remove(findFragmentByTag);
                try {
                    beginTransaction.commitAllowingStateLoss();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
