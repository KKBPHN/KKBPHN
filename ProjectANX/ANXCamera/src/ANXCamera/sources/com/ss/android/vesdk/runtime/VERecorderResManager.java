package com.ss.android.vesdk.runtime;

import com.ss.android.vesdk.VEException;
import com.ss.android.vesdk.VEResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VERecorderResManager {
    private String mConcatSegmentAudioPath;
    private String mConcatSegmentVideoPath;
    private String mConcatSementAudioVideoMixedVideoPath;
    private List mSegmentAudioPathList = new ArrayList();
    private String mSegmentDirPath;
    private List mSegmentVideoPathList = new ArrayList();
    private String mWorkspace;

    public VERecorderResManager(String str) {
        this.mWorkspace = str;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mWorkspace);
        sb.append(File.separator);
        sb.append("segments");
        sb.append(File.separator);
        this.mSegmentDirPath = sb.toString();
    }

    public void addSegmentAudioPath(String str) {
        this.mSegmentAudioPathList.add(str);
    }

    public void addSegmentVideoPath(String str) {
        this.mSegmentVideoPathList.add(str);
    }

    public String delSegmentAudioPath() {
        if (this.mSegmentAudioPathList.size() <= 0) {
            return "";
        }
        List list = this.mSegmentAudioPathList;
        return (String) list.remove(list.size() - 1);
    }

    public String delSegmentVideoPath() {
        if (this.mSegmentVideoPathList.size() > 0) {
            List list = this.mSegmentVideoPathList;
            return (String) list.remove(list.size() - 1);
        }
        throw new VEException(VEResult.TER_INVALID_STAT, "segment video list size is 0");
    }

    public void genConcatSegmentAudioPath() {
        StringBuilder sb = new StringBuilder();
        String str = "concat";
        sb.append(VEResManager.getFolder(this.mWorkspace, str));
        sb.append(File.separator);
        sb.append(str);
        sb.append(".wav");
        this.mConcatSegmentAudioPath = sb.toString();
    }

    public void genConcatSegmentVideoPath() {
        StringBuilder sb = new StringBuilder();
        String str = "concat";
        sb.append(VEResManager.getFolder(this.mWorkspace, str));
        sb.append(File.separator);
        sb.append(str);
        sb.append(".mp4");
        this.mConcatSegmentVideoPath = sb.toString();
    }

    public String genSegmentAudioPath(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(VEResManager.getFolder(this.mWorkspace, "segments"));
        sb.append(File.separator);
        sb.append(i);
        sb.append(".wav");
        return sb.toString();
    }

    public String genSegmentVideoPath(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(VEResManager.getFolder(this.mWorkspace, "segments"));
        sb.append(File.separator);
        sb.append(i);
        sb.append(".mp4");
        return sb.toString();
    }

    public String getConcatSegmentAudioPath() {
        return this.mConcatSegmentAudioPath;
    }

    public String getConcatSegmentVideoPath() {
        return this.mConcatSegmentVideoPath;
    }

    public List getSegmentAudioPathList() {
        return this.mSegmentAudioPathList;
    }

    public List getSegmentVideoPathList() {
        return this.mSegmentVideoPathList;
    }

    public String getTempSegmentDirPath() {
        return this.mSegmentDirPath;
    }

    public void release() {
        List list = this.mSegmentVideoPathList;
        if (list != null) {
            list.clear();
            this.mSegmentVideoPathList = null;
        }
        List list2 = this.mSegmentAudioPathList;
        if (list2 != null) {
            list2.clear();
            this.mSegmentAudioPathList = null;
        }
    }
}
