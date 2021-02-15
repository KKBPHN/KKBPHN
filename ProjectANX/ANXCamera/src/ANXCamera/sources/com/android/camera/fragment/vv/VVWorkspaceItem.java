package com.android.camera.fragment.vv;

import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.module.impl.component.FileUtils;
import com.xiaomi.camera.rx.CameraSchedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class VVWorkspaceItem implements Comparable {
    private static final String DELETE_MARKED = "d_";
    public long mCreateTime;
    public String mFolderPath;
    private List mFragments;
    public String mItemId;
    private List mLastFragments;
    public long mLastModifiedTime;
    public String mLastThumbPath;
    public transient boolean mMarked;
    public String mNewThumbPath;
    public String mTemplateId;

    private VVWorkspaceItem() {
    }

    private VVWorkspaceItem(String str, String str2) {
        this.mFragments = new ArrayList();
        this.mLastFragments = new ArrayList();
        this.mTemplateId = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mTemplateId);
        sb.append("_");
        sb.append(System.currentTimeMillis());
        this.mItemId = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.mItemId);
        this.mFolderPath = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFolderPath);
        sb3.append(File.separator);
        sb3.append("thumb");
        this.mLastThumbPath = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFolderPath);
        sb4.append(File.separator);
        sb4.append("thumb_new");
        this.mNewThumbPath = sb4.toString();
    }

    public static final VVWorkspaceItem createNew(String str, String str2) {
        return new VVWorkspaceItem(str, str2);
    }

    public static final VVWorkspaceItem restoreByParseFolder(String str) {
        VVWorkspaceItem vVWorkspaceItem = new VVWorkspaceItem();
        if (vVWorkspaceItem.restoreFromFolder(str)) {
            return vVWorkspaceItem;
        }
        return null;
    }

    private boolean restoreFromFolder(String str) {
        File file = new File(str);
        String name = file.getName();
        try {
            this.mItemId = name;
            String[] split = name.split("\\_");
            if (split != null) {
                if (split.length != 0) {
                    this.mTemplateId = split[0];
                    this.mCreateTime = Long.parseLong(split[1]);
                    File[] listFiles = file.listFiles();
                    if (listFiles.length == 0) {
                        return false;
                    }
                    ArrayList<File> arrayList = new ArrayList<>();
                    for (File file2 : listFiles) {
                        if (file2.getName().startsWith(DELETE_MARKED)) {
                            FileUtils.deleteFile(file2);
                        } else {
                            arrayList.add(file2);
                        }
                    }
                    Collections.sort(arrayList, new Comparator() {
                        public int compare(File file, File file2) {
                            if (file.lastModified() == file2.lastModified()) {
                                return 0;
                            }
                            return file.lastModified() > file2.lastModified() ? 1 : -1;
                        }
                    });
                    this.mLastModifiedTime = ((File) arrayList.get(arrayList.size() - 1)).lastModified();
                    this.mFragments = new ArrayList();
                    for (File file3 : arrayList) {
                        if (TextUtils.equals("video/mp4", Util.getMimeType(file3))) {
                            this.mFragments.add(file3.getAbsolutePath());
                        }
                    }
                    if (this.mFragments.size() == 0) {
                        return false;
                    }
                    this.mLastFragments = new ArrayList(this.mFragments);
                    this.mFolderPath = str;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mFolderPath);
                    sb.append(File.separator);
                    sb.append("thumb");
                    this.mLastThumbPath = sb.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.mFolderPath);
                    sb2.append(File.separator);
                    sb2.append("thumb_new");
                    this.mNewThumbPath = sb2.toString();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void add(String str) {
        this.mFragments.add(str);
    }

    public int compareTo(VVWorkspaceItem vVWorkspaceItem) {
        return 0;
    }

    public int completeSize() {
        return this.mFragments.size();
    }

    public void confirmOperation() {
        File[] listFiles;
        for (File file : new File(this.mFolderPath).listFiles()) {
            if (file.getName().startsWith(DELETE_MARKED)) {
                FileUtils.deleteFile(file);
            }
        }
        File file2 = new File(this.mNewThumbPath);
        File file3 = new File(this.mLastThumbPath);
        if (file2.exists()) {
            file2.renameTo(file3);
        }
    }

    public String getFragmentAt(int i) {
        List list = this.mFragments;
        return (list == null || list.size() <= 0) ? "" : (String) this.mFragments.get(i);
    }

    public List getFragments() {
        return this.mFragments;
    }

    public String getLastPath() {
        return getFragmentAt(completeSize() - 1);
    }

    public int getLastSize() {
        return this.mLastFragments.size();
    }

    public String getRawInfoPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFolderPath);
        sb.append(File.separator);
        sb.append("raw_info");
        return sb.toString();
    }

    public String getTargetThumbPath() {
        return !new File(this.mLastThumbPath).exists() ? this.mLastThumbPath : this.mNewThumbPath;
    }

    public boolean isEmpty() {
        return completeSize() == 0;
    }

    public void remove(int i) {
        boolean z;
        String fragmentAt = getFragmentAt(i);
        Iterator it = this.mLastFragments.iterator();
        while (true) {
            if (it.hasNext()) {
                if (fragmentAt.equals((String) it.next())) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        this.mFragments.remove(i);
        if (z) {
            File file = new File(fragmentAt);
            String str = this.mFolderPath;
            StringBuilder sb = new StringBuilder();
            sb.append(DELETE_MARKED);
            sb.append(file.getName());
            file.renameTo(new File(str, sb.toString()));
            return;
        }
        FileUtils.deleteFile(fragmentAt);
    }

    public void removeSelf() {
        if (new File(this.mFolderPath).exists()) {
            CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new Runnable() {
                public void run() {
                    File file = new File(VVWorkspaceItem.this.mFolderPath);
                    if (file.exists()) {
                        FileUtils.deleteDir(file);
                    }
                }
            });
        }
    }

    public void saveWorkspace() {
    }

    public void undo() {
        int i;
        Iterator it = this.mFragments.iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            Iterator it2 = this.mLastFragments.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (str.equals((String) it2.next())) {
                        i = 1;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (i == 0) {
                FileUtils.deleteFile(new File(str));
            }
        }
        File[] listFiles = new File(this.mFolderPath).listFiles();
        int length = listFiles.length;
        while (i < length) {
            File file = listFiles[i];
            String name = file.getName();
            if (name.startsWith(DELETE_MARKED)) {
                file.renameTo(new File(this.mFolderPath, name.substring(2)));
            }
            i++;
        }
        File file2 = new File(this.mNewThumbPath);
        if (file2.exists()) {
            file2.delete();
        }
    }
}
