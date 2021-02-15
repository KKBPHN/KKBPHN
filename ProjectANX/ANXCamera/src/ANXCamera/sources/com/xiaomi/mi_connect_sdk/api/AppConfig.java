package com.xiaomi.mi_connect_sdk.api;

public final class AppConfig {
    private final byte[] advData;
    private final int appCommDataType;
    private final int appCommType;
    private final int appRoleType;
    private final byte[] commData;
    private final int[] discAppIds;
    private final int discType;

    public class Builder {
        private byte[] advData;
        private int appCommDataType;
        private int appCommType;
        private int appRoleType;
        private byte[] commData;
        private int[] discAppIds;
        private int discType;

        public Builder advData(byte[] bArr) {
            this.advData = bArr;
            return this;
        }

        public AppConfig build() {
            AppConfig appConfig = new AppConfig(this.appRoleType, this.discType, this.appCommType, this.advData, this.commData, this.appCommDataType, this.discAppIds);
            return appConfig;
        }

        public Builder commData(byte[] bArr) {
            this.commData = bArr;
            return this;
        }

        public Builder commDataType(int i) {
            this.appCommDataType = i;
            return this;
        }

        public Builder commType(int i) {
            this.appCommType = i;
            return this;
        }

        public Builder discAppIds(int[] iArr) {
            this.discAppIds = iArr;
            return this;
        }

        public Builder discType(int i) {
            this.discType = i;
            return this;
        }

        public Builder roleType(int i) {
            this.appRoleType = i;
            return this;
        }
    }

    public AppConfig(int i, int i2, int i3, byte[] bArr, byte[] bArr2, int i4, int[] iArr) {
        this.appRoleType = i;
        this.discType = i2;
        this.appCommType = i3;
        this.advData = bArr != null ? (byte[]) bArr.clone() : new byte[0];
        this.commData = bArr2 != null ? (byte[]) bArr2.clone() : new byte[0];
        this.appCommDataType = i4;
        this.discAppIds = iArr != null ? (int[]) iArr.clone() : new int[0];
    }

    public byte[] getAdvData() {
        return (byte[]) this.advData.clone();
    }

    public int getAppCommDataType() {
        return this.appCommDataType;
    }

    public int getAppCommType() {
        return this.appCommType;
    }

    public int getAppRoleType() {
        return this.appRoleType;
    }

    public byte[] getCommData() {
        return (byte[]) this.commData.clone();
    }

    public int[] getDiscAppIds() {
        return (int[]) this.discAppIds.clone();
    }

    public int getDiscType() {
        return this.discType;
    }
}
