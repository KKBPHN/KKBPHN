package O00000Oo.O00000oO.O000000o;

import com.android.camera2.CameraCapabilities;
import java.util.function.Function;

/* renamed from: O00000Oo.O00000oO.O000000o.O000000o reason: case insensitive filesystem */
public final /* synthetic */ class lambda implements Function {
    public static final /* synthetic */ lambda INSTANCE = new lambda();

    private /* synthetic */ lambda() {
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(((CameraCapabilities) obj).isMtkPipDevicesSupported());
    }
}
