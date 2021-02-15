package com.android.camera2.vendortag.struct;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableChiRect implements MarshalQueryable {
    private static final int SIZE = 16;

    public class ChiRect {
        public int height;
        public int left;
        public int top;
        public int width;

        public ChiRect(int i, int i2, int i3, int i4) {
            this.left = i;
            this.top = i2;
            this.width = i3;
            this.height = i4;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ChiRect{left=");
            sb.append(this.left);
            sb.append(", top=");
            sb.append(this.top);
            sb.append(", width=");
            sb.append(this.width);
            sb.append(", height=");
            sb.append(this.height);
            sb.append('}');
            return sb.toString();
        }
    }

    class MarshalerRect extends Marshaler {
        protected MarshalerRect(TypeReference typeReference, int i) {
            super(MarshalQueryableChiRect.this, typeReference, i);
        }

        public int getNativeSize() {
            return 16;
        }

        public void marshal(ChiRect chiRect, ByteBuffer byteBuffer) {
            byteBuffer.putInt(chiRect.left);
            byteBuffer.putInt(chiRect.top);
            byteBuffer.putInt(chiRect.width);
            byteBuffer.putInt(chiRect.height);
        }

        public ChiRect unmarshal(ByteBuffer byteBuffer) {
            return new ChiRect(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
        }
    }

    public Marshaler createMarshaler(TypeReference typeReference, int i) {
        return new MarshalerRect(typeReference, i);
    }

    public boolean isTypeMappingSupported(TypeReference typeReference, int i) {
        return ChiRect.class.equals(typeReference.getType());
    }
}
