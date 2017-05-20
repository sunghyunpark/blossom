package push;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.yssh1020.blossom.AppSettingManager;
import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import tab5.ArticleActivity;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private AppSettingManager appSettingManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        appSettingManager = new AppSettingManager(getApplicationContext());

        if(remoteMessage.getData().get("flag").equals("bookmark")){
            //북마크 했을 때
            BookMarkSendPushNotification(remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("article_id"));
            appSettingManager.setTab4_State(true);
        }else if(remoteMessage.getData().get("flag").equals("article_comment")){
            //아티클에 댓글이 달렸을 때
            ArticleCommentSendPushNotification(remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("article_id"));
            appSettingManager.setTab4_State(true);
        }else if(remoteMessage.getData().get("flag").equals("comment_like")){
            //댓글을 공감할 때
            CommentLikeSendPushNotification(remoteMessage.getData().get("message"));
            appSettingManager.setTab4_State(true);
        }else if(remoteMessage.getData().get("flag").equals("article_like")){
            //아티클을 공감할 때
            ArticleLikeSendPushNotification(remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("article_id"));
            appSettingManager.setTab4_State(true);
        }

    }

    /**
     * 아티클 좋아요
     * @param message
     * @param article_id
     */
    private void ArticleLikeSendPushNotification(String message, String article_id){
        Resources res = getResources();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from_push","article_like");
        intent.putExtra("article_id", article_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon) )
                .setContentTitle(String.format(res.getString(R.string.app_name)))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * 댓글 공감
     * @param message
     */
    private void CommentLikeSendPushNotification(String message){
        Resources res = getResources();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon) )
                .setContentTitle(String.format(res.getString(R.string.app_name)))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * 아티클에 댓글 남겻을 때
     * @param message
     * @param article_id
     */
    private void ArticleCommentSendPushNotification(String message, String article_id){
        Resources res = getResources();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from_push","article_comment");
        intent.putExtra("article_id", article_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon) )
                .setContentTitle(String.format(res.getString(R.string.app_name)))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * bookmark push
     * @param message
     * @param article_id
     */
    private void BookMarkSendPushNotification(String message, String article_id) {
        Resources res = getResources();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("article_id", article_id);
        //intent.putExtra("from_push", "bookmark");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon) )
                .setContentTitle(String.format(res.getString(R.string.app_name)))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}