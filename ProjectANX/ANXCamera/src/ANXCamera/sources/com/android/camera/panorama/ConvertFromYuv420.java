package com.android.camera.panorama;

import android.media.Image;

public class ConvertFromYuv420 implements IImage2BytesConverter {
    /* access modifiers changed from: private */
    public IImage2BytesConverter mImage2BytesConverter = new ConvertFromYuv420Null();

    class ConvertFromYuv420Null implements IImage2BytesConverter {
        private ConvertFromYuv420Null() {
        }

        public byte[] image2bytes(Image image) {
            ConvertFromYuv420 convertFromYuv420;
            IImage2BytesConverter convertFromYvu420SemiPlanar;
            String imageFormat = PanoramaGP3ImageFormat.getImageFormat(image);
            if (PanoramaGP3ImageFormat.YUV420_PLANAR.equals(imageFormat)) {
                convertFromYuv420 = ConvertFromYuv420.this;
                convertFromYvu420SemiPlanar = new ConvertFromYuv420Planar();
            } else if (PanoramaGP3ImageFormat.YUV420_SEMIPLANAR.equals(imageFormat)) {
                convertFromYuv420 = ConvertFromYuv420.this;
                convertFromYvu420SemiPlanar = new ConvertFromYuv420SemiPlanar();
            } else if (!PanoramaGP3ImageFormat.YVU420_SEMIPLANAR.equals(imageFormat)) {
                return new byte[0];
            } else {
                convertFromYuv420 = ConvertFromYuv420.this;
                convertFromYvu420SemiPlanar = new ConvertFromYvu420SemiPlanar();
            }
            convertFromYuv420.mImage2BytesConverter = convertFromYvu420SemiPlanar;
            return ConvertFromYuv420.this.mImage2BytesConverter.image2bytes(image);
        }
    }

    public byte[] image2bytes(Image image) {
        return this.mImage2BytesConverter.image2bytes(image);
    }
}
