package com.pango.hsec.hsec.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.R;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.firebase.MessageShowActivity;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.obs_cabecera;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class MiFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MiFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Bitmap bitmap;
    LayoutInflater layoutInflater2;
    View popupView2;
    PopupWindow popupWindow2;
    protected Handler handler;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de: " + from);

        if (remoteMessage.getNotification() != null) {
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            //GlobalVariables.codFacilito=remoteMessage.getData().get("codigo");

            Log.d("notificacion", remoteMessage.getData().toString());
            Log.d("notificacion", remoteMessage.getData().get("codigo"));
            //getOfferDetails(id_offer);
            Log.d(TAG, "NOTIFICACION RECIBIDA");
            Log.d(TAG, "Título: " + titulo);
            Log.d(TAG, "Texto: " + texto);

            String Codigo=remoteMessage.getData().get("codigo");
            String CodPersona=remoteMessage.getData().get("codpersona");
            String Tipo=remoteMessage.getData().get("tipo");
            String imageUri = remoteMessage.getData().get("image");
            Gson gson = new Gson();
            UsuarioModel UserLoged= gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
            if(!CodPersona.equals(UserLoged.CodPersona)) return;
            //confirmUpdate(titulo, texto);
            if(StringUtils.isEmpty(imageUri)) mostrarNotificacion(titulo, texto,Codigo);
            else {
                bitmap = getBitmapfromUrl(imageUri);
                sendNotification(titulo, texto,Codigo,bitmap);
            }

            Intent intent = new Intent(this, MessageShowActivity.class);
            intent.putExtra("title", titulo);
            intent.putExtra("descripcion", texto);
            intent.putExtra("codigo", Codigo);
            intent.putExtra("tipo", Tipo);
            startActivity(intent);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }


    private void mostrarNotificacion(String title, String body, String Codigo) {
        //GlobalVariables.codFacilito=Codigo;
        Intent intent = new Intent(this.getApplicationContext(), obsFacilitoDet.class);
        intent.putExtra("codObs", Codigo);
        intent.putExtra("verBoton", "-1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       /* intent.putExtra("codObs", Codigo);
        intent.putExtra("verBoton", "-1");*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_logo_hsec2)
                .setSound(soundUri)
                .setContentTitle(title)
                .setContentText(body);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendNotification(String title, String body, String Codigo,Bitmap image) {

        //GlobalVariables.codFacilito=Codigo;
        Intent intent = new Intent(this, obsFacilitoDet.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("codObs", Codigo);
        intent.putExtra("verBoton", "-1");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
                .setSmallIcon(R.mipmap.ic_logo_hsec2)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public void confirmUpdate(String Titulo,String Contenido){
        //handler = new Handler();
        obs_cabecera f1 = obs_cabecera.newInstance("");
        //f1.txtObservado2=(TextView) f1.getView().findViewById(R.id.txt_nombres);
        if(popupWindow2!=null && popupWindow2.isShowing())popupWindow2.dismiss();
        layoutInflater2 =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView2 = layoutInflater2.inflate(R.layout.popup_notificacion, null);

        popupWindow2 = new PopupWindow(popupView2, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow2.showAtLocation(f1.getView(), Gravity.NO_GRAVITY, 0,0);
        popupWindow2.setFocusable(true);
        popupWindow2.update();
        popupWindow2.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow2.dismiss();
            }
        });
       /* ImageView imgTitle=(ImageView) popupView2.findViewById(R.id.txt_nombres);
        CardView cardResult=(CardView) popupView2.findViewById(R.id.card_content);*/
        TextView txtTitle=(TextView) popupView2.findViewById(R.id.txt_nombres);
        TextView txtContenet=(TextView) popupView2.findViewById(R.id.txt_cargo);

        txtTitle.setText(Titulo);
        txtContenet.setText(Contenido);
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow2.dismiss();
            }
        }, 5000);*/
    }


   /* @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        System.out.println("Data Message: "+json);

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            String articleData = payload.getString("article_data");

            //Send notification data to MessageShowActivity class for showing
            Intent resultIntent = new Intent(getApplicationContext(), MessageShowActivity.class);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("timestamp", timestamp);
            resultIntent.putExtra("article_data", articleData);
            resultIntent.putExtra("image", imageUrl);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            System.out.println("in 1st CATCH");
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            System.out.println("in 2nd CATCH");
        }
    }

    *//**
     * Showing notification with text only
     *//*
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    *//**
     * Showing notification with text and image
     *//*
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
    */
}