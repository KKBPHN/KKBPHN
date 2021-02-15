package com.android.camera2.vendortag.struct;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableASDScene implements MarshalQueryable {

    public class ASDScene {
        public int type;
        public int value;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.type);
            sb.append(",,,");
            sb.append(this.value);
            return sb.toString();
        }
    }

    public class MarshalerASDScene extends Marshaler {
        private static final int SIZE = 8;

        protected MarshalerASDScene(TypeReference typeReference, int i) {
            super(MarshalQueryableASDScene.this, typeReference, i);
        }

        public int getNativeSize() {
            return 8;
        }

        public void marshal(ASDScene aSDScene, ByteBuffer byteBuffer) {
            byteBuffer.putInt(aSDScene.type);
            byteBuffer.putInt(aSDScene.value);
        }

        public ASDScene unmarshal(ByteBuffer byteBuffer) {
            ASDScene aSDScene = new ASDScene();
            aSDScene.type = byteBuffer.getInt();
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
