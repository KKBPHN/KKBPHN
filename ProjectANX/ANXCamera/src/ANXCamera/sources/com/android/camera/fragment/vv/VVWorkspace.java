package com.android.camera.fragment.vv;

import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.module.impl.component.LiveSubVVImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VVWorkspace extends ProcessingWorkspace {
    public void add(VVWorkspaceItem vVWorkspaceItem) {
        this.mItemList.add(vVWorkspaceItem);
    }

    public String getWorkspaceDir() {
        return LiveSubVVImpl.WORKSPACE_PATH;
    }

    public void remove(VVWorkspaceItem vVWorkspaceItem) {
        for (VVWorkspaceItem vVWorkspaceItem2 : this.mItemList) {
            if (vVWorkspaceItem2.mItemId.equals(vVWorkspaceItem.mItemId)) {
                this.mItemList.remove(vVWorkspaceItem);
                return;
            }
        }
    }

    public boolean restoreWorkspace() {
        File[] listFiles = new File(getWorkspaceDir()).listFiles();
        ArrayList<VVWorkspaceItem> arrayList = new ArrayList<>();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                VVWorkspaceItem restoreByParseFolder = VVWorkspaceItem.restoreByParseFolder(file.getAbsolutePath());
                if (restoreByParseFolder != null) {
                    arrayList.add(restoreByParseFolder);
                }
            }
        }
        Collections.sort(arrayList, new Comparator() {
            public int compare(VVWorkspaceItem vVWorkspaceItem, VVWorkspaceItem vVWorkspaceItem2) {
                long j = vVWorkspaceItem.mLastModifiedTime;
                long j2 = vVWorkspaceItem2.mLastModifiedTime;
                if (j == j2) {
                    return 0;
                }
                return j < j2 ? 1 : -1;
            }
        });
        for (VVWorkspaceItem vVWorkspaceItem : arrayList) {
            if (this.mItemList.size() >= 50) {
                FileUtils.deleteDir(new File(vVWorkspaceItem.mFolderPath));
            } else {
                this.mItemList.add(vVWorkspaceItem);
            }
        }
        return true;
    }

    public boolean saveWorkspace() {
        for (VVWorkspaceItem saveWorkspace : this.mItemList) {
            saveWorkspace.saveWorkspace();
        }
        return true;
    }
}
