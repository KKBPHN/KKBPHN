package miui.autoinstall.config.download;

import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.miui.internal.R;
import miui.autoinstall.config.activity.PromptRestoreDialogActivity;
import org.jcodec.containers.mp4.boxes.Box;

public class AutoInstallNotification {
    private static final String NOTIFICATION_CHANNEL_ID = "auto_install_notification";
    private static final int NOTIFICATION_ID = 520;
    private static final String PROGRESS_NOTIFICATION_CHANNEL_ID = "auto_install_progress_notification";
    private Context mContext;
    private NotificationManager mNotificationManager;
    private Builder mProgressBuilder;

    public AutoInstallNotification(Context context) {
        this.mContext = context;
        initSystemNotification(context);
    }

    private Builder createBuilder() {
        Builder builder = new Builder(this.mContext, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_main).setAutoCancel(false);
        return builder;
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        String str = "auto_install";
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, str, 4);
        notificationChannel.setDescription(str);
        notificationChannel.setSound(null, null);
        notificationChannel.enableVibration(false);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private Builder createProgressBuilder() {
        Builder builder = new Builder(this.mContext, PROGRESS_NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_main).setAutoCancel(false);
        return builder;
    }

    private void createProgressNotificationChannel(NotificationManager notificationManager) {
        String str = "auto_install_progress";
        NotificationChannel notificationChannel = new NotificationChannel(PROGRESS_NOTIFICATION_CHANNEL_ID, str, 2);
        notificationChannel.setDescription(str);
        notificationChannel.setSound(null, null);
        notificationChannel.enableVibration(false);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this.mContext, PromptRestoreDialogActivity.class);
        intent.addFlags(268435456);
        return PendingIntent.getActivity(this.mContext, 0, intent, Box.MAX_BOX_SIZE);
    }

    private void initSystemNotification(Context context) {
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        createNotificationChannel(this.mNotificationManager);
        createProgressNotificationChannel(this.mNotificationManager);
    }

    public void clearAll() {
        this.mNotificationManager.cancelAll();
    }

    public void showNotRestoreNotification() {
        Builder createBuilder = createBuilder();
        createBuilder.setOngoing(true);
        createBuilder.setContentTitle(this.mContext.getString(R.string.system_restore));
        createBuilder.setContentText(this.mContext.getString(R.string.click_to_restore));
        createBuilder.setContentIntent(getContentIntent());
        this.mNotificationManager.notify(NOTIFICATION_ID, createBuilder.build());
    }

    public void showStartNotification(int i) {
        Builder createBuilder = createBuilder();
        createBuilder.setOngoing(true);
        createBuilder.setContentTitle(this.mContext.getString(R.string.system_app_is_restore));
        createBuilder.setContentText(this.mContext.getString(R.string.recovery_progress, new Object[]{Integer.valueOf(1), Integer.valueOf(i)}));
        this.mNotificationManager.notify(NOTIFICATION_ID, createBuilder.build());
    }

    public void showSuccessNotification() {
        Builder createBuilder = createBuilder();
        createBuilder.setOngoing(false);
        createBuilder.setContentTitle(this.mContext.getString(R.string.system_app_restore_completed));
        createBuilder.setContentText(this.mContext.getString(R.string.system_app_restore_completed_detail));
        this.mNotificationManager.cancel(NOTIFICATION_ID);
        this.mNotificationManager.notify(NOTIFICATION_ID, createBuilder.build());
    }

    public void updateProgress(int i, int i2) {
        if (this.mProgressBuilder == null) {
            this.mProgressBuilder = createProgressBuilder();
            this.mProgressBuilder.setOngoing(true);
        }
        this.mProgressBuilder.setContentTitle(this.mContext.getString(R.string.system_app_is_restore));
        this.mProgressBuilder.setContentText(this.mContext.getString(R.string.recovery_progress, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
        this.mNotificationManager.notify(NOTIFICATION_ID, this.mProgressBuilder.build());
    }
}
