package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.sin.mobilesafe.R;
import com.example.sin.mobilesafe.SplashActivity;

/**
 * Created by Sin on 2016/10/7.
 * Description:
 */

public class ProtectedService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("黑马手机卫士时刻保护您的安全");
        //        builder.setTicker("黑马手机卫士时刻保护您的安全");
        Notification notification = builder.build();
        /* Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "黑马手机卫士时刻保护您的安全";
        notification.contentView = new RemoteViews(getPackageName(), R.layout.protected_item);*/
        Intent intent = new Intent(this, SplashActivity.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        notification.contentView = new RemoteViews(getPackageName(), R.layout.protected_item);
        notification.contentIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startForeground(100, notification);
    }
}
