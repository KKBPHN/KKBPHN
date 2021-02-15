package O00000o.O000000o.O000000o;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.ss.android.medialib.audio.AudioDataProcessThread.OnProcessDataListener;

public interface O000000o extends OnProcessDataListener {
    int addPCMData(byte[] bArr, int i);

    int closeWavFile(boolean z);

    @RestrictTo({Scope.LIBRARY})
    int initAudioConfig(int i, int i2);

    int initWavFile(int i, int i2, double d);

    void lackPermission();

    int onProcessData(byte[] bArr, int i);

    void recordStatus(boolean z);
}
