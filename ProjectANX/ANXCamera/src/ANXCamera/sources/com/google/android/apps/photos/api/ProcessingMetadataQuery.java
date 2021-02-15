package com.google.android.apps.photos.api;

public interface ProcessingMetadataQuery {
    public static final String MEDIA_PATH = "media_path";
    public static final String MEDIA_STORE_ID = "media_store_id";
    public static final String NO_GAUSSIAN = "no_gaussian";
    public static final String PROGRESS_ANIM_TYPE = "progress_anim_type";
    public static final String PROGRESS_PERCENTAGE = "progress_percentage";
    public static final String PROGRESS_STATUS = "progress_status";
    public static final String START_TIME = "start_time";

    public enum ProgressStatus {
        INDETERMINATE(1),
        DETERMINATE(2);
        
        private final int identifier;

        private ProgressStatus(int i) {
            this.identifier = i;
        }

        public static ProgressStatus fromIdentifier(int i) {
            return i == DETERMINATE.getIdentifier() ? DETERMINATE : INDETERMINATE;
        }

        public int getIdentifier() {
            return this.identifier;
        }
    }
}
