//package com.pango.hsec.hsec.firebase;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import androidx.annotation.NonNull;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//
///**
// * Created by BOB on 20/05/2018.
// */
//public class DeleteTokenService extends IntentService
//{
//    public static final String TAG = DeleteTokenService.class.getSimpleName();
//
//    public DeleteTokenService()
//    {
//        super(TAG);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent)
//    {
//        try
//        {
//            // Resets Instance ID and revokes all tokens.
//            FirebaseInstanceId.getInstance().deleteInstanceId();
//
//            // Now manually call onTokenRefresh()
//            Log.d(TAG, "Getting new token");
//            String token=FirebaseInstanceId.getInstance().getToken().toString();
//
//            if(!StringUtils.isEmpty(token)){
//                Toast.makeText(DeleteTokenService.this,"Token generado ",Toast.LENGTH_SHORT).show();
//                saveTokenToPrefs(token);
//            }
//            else {
//                Intent intentService = new Intent(DeleteTokenService.this, MiFirebaseInstanceIdService.class);
//                startService(intentService);
//            }
//
//        }
//        catch (IOException e)
//        {
//            System.out.println("INTENT SERVICE IOException");
//
//            e.printStackTrace();
//        }
//
//       /* FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        if(!StringUtils.isEmpty(token)){
//                            Toast.makeText(DeleteTokenService.this,"Token generado ",Toast.LENGTH_SHORT).show();
//                            saveTokenToPrefs(token);
//                        }
//                        else {
//                            Intent intentService = new Intent(DeleteTokenService.this, MiFirebaseInstanceIdService.class);
//                            startService(intentService);
//                        }
//                    }
//                });*/
//    }
//
//    private void saveTokenToPrefs(String _token)
//    {
//        System.out.println("TOKEN DELETED. NEW TOKEN FROM SERVICE: "+_token);
//        // Access Shared Preferences
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        // Save to SharedPreferences
//        editor.putString("registration_id", _token);
//        editor.apply();
//    }
//}