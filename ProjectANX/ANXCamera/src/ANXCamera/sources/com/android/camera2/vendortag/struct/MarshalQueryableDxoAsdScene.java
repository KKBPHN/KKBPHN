package com.android.camera2.vendortag.struct;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import androidx.annotation.Nullable;
import java.nio.ByteBuffer;

public class MarshalQueryableDxoAsdScene implements MarshalQueryable {

    public class ASDScene {
        public float confident;
        public int value;

        public boolean equals(@Nullable Object obj) {
            if (obj == null || !(obj instanceof ASDScene)) {
                return super.equals(obj);
            }
            ASDScene aSDScene = (ASDScene) obj;
            boolean z = aSDScene.confident == this.confident && aSDScene.value == this.value;
            return z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.confident);
            sb.append(",");
            sb.append(this.value);
            return sb.toString();
        }
    }

    public class MarshalerASDScene extends Marshaler {
        private static final int SIZE = 8;

        protected MarshalerASDScene(TypeReference typeReference, int i) {
            super(MarshalQueryableDxoAsdScene.this, typeReference, i);
        }

        public int getNativeSize() {
            return 8;
        }

        public void marshal(ASDScene aSDScene, ByteBuffer byteBuffer) {
            byteBuffer.putFloat(aSDScene.confident);
            byteBuffer.putInt(aSDScene.value);
        }

        public ASDScene unmarshal(ByteBuffer byteBuffer) {
            ASDScene aSDScene = new ASDScene();
            aSDScene.confident = byteBuffer.getFloat();
            aSDScene.value = byteBuffer.getInt();
            return aSDScene;
        }
    }

    public Marshaler createMarshaler(TypeReference typeReference, int i) {
        return new MarshalerASDScene(typeReference, i);
    }

    public boolean isTypeMappingSupported(TypeReference typeReference, int i) {
        return ASDScene.class.equals(typeReference.getType());
    }
}
