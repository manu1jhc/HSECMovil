package com.pango.hsec.hsec.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
//import com.pango.hsec.hsec.ActComDetalle;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;

import static android.content.ContentValues.TAG;

public class MiFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de: " + from);

        if (remoteMessage.getNotification() != null) {
           // GlobalVariables.flag_notificacion=true;
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            remoteMessage.getData().get("codigo");
            Log.d("notificacion", remoteMessage.getData().toString());
            Log.d("notificacion", remoteMessage.getData().get("codigo"));

            //getOfferDetails(id_offer);


            Log.d(TAG, "NOTIFICACION RECIBIDA");

            Log.d(TAG, "Título: " + titulo);
            Log.d(TAG, "Texto: " + texto);

         //   GlobalVariables.cod_public=remoteMessage.getData().get("codigo");

            //Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());
            // showNotification(titulo, texto);

            mostrarNotificacion(titulo, texto);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }


    private void mostrarNotificacion(String title, String body) {
        //abrir el activity necesario ///////////////
        //para abrir el evento notificado, se necesita el codigo, titulo y fecha de la apk ///////////////////

       /* Intent intent = new Intent(this.getApplicationContext(), ActComDetalle.class);
       // intent.putExtra("titulo",title);
        //intent.putExtra("fecha","fecha");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        */
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_logo_hsec2)
                .setSound(soundUri)
                .setContentTitle(title)
                .setContentText(body);


        //android.R.drawable.stat_sys_warning
        //     .setAutoCancel(true)
        //  .setSound(soundUri)
        //            .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       /* Notification notification = new Notification(R.mipmap.ic_logofinal, body, 100);

        Intent notificationIntent = new Intent(this.getApplicationContext(), HomeActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;*/
        notificationManager.notify(0, notificationBuilder.build());



    }




}
