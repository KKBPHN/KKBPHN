package O00000o.O000000o.O000000o;

import android.media.AudioRecord;
import com.ss.android.medialib.audio.AudioDataProcessThread;
import com.ss.android.vesdk.VELogUtil;

class O00000Oo implements Runnable {
    boolean OO0000o;
    private double speed;
    final /* synthetic */ O00000o0 this$0;

    public O00000Oo(O00000o0 o00000o0, double d, boolean z) {
        this.this$0 = o00000o0;
        this.speed = d;
        this.OO0000o = z;
    }

    public void run() {
        boolean z;
        String str = "BufferedAudioRecorder";
        O00000o0 o00000o0 = this.this$0;
        byte[] bArr = new byte[o00000o0.bufferSizeInBytes];
        o00000o0.OO000Oo = false;
        O000000o o000000o = o00000o0.OO000OO;
        o00000o0.OoOOO = new AudioDataProcessThread(o000000o, o000000o);
        this.this$0.OoOOO.start();
        if (this.OO0000o) {
            O00000o0 o00000o02 = this.this$0;
            o00000o02.OoOOO.startFeeding(o00000o02.sampleRateInHz, this.speed);
        }
        try {
            if (this.this$0.audio != null) {
                this.this$0.audio.startRecording();
                if (this.this$0.audio == null || this.this$0.audio.getRecordingState() == 3) {
                    z = false;
                } else {
                    this.this$0.OO000OO.recordStatus(false);
                    z = true;
                }
                boolean z2 = z;
                int i = 0;
                while (true) {
                    O00000o0 o00000o03 = this.this$0;
                    if (!o00000o03.isRecording) {
                        break;
                    }
                    AudioRecord audioRecord = o00000o03.audio;
                    if (audioRecord != null) {
                        i = audioRecord.read(bArr, 0, o00000o03.bufferSizeInBytes);
                    }
                    if (-3 == i) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("bad audio buffer len ");
                        sb.append(i);
                        VELogUtil.e(str, sb.toString());
                    } else if (i > 0) {
                        try {
                            if (this.this$0.isRecording) {
                                this.this$0.OO000OO.addPCMData(bArr, i);
                            }
                            if (this.this$0.OoOOO.isProcessing() && !this.this$0.OO000Oo) {
                                this.this$0.OoOOO.feed(bArr, i);
                            }
                        } catch (Exception unused) {
                        }
                    } else {
                        AudioRecord audioRecord2 = this.this$0.audio;
                        if (!(audioRecord2 == null || audioRecord2.getRecordingState() == 3 || z2)) {
                            this.this$0.OO000OO.recordStatus(false);
                            z2 = true;
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            try {
                if (this.this$0.audio != null) {
                    this.this$0.audio.release();
                }
            } catch (Exception unused2) {
            }
            this.this$0.audio = null;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("audio recording failed!");
            sb2.append(e);
            VELogUtil.e(str, sb2.toString());
        }
    }
}
