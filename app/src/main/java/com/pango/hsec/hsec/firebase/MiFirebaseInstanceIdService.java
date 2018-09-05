package com.pango.hsec.hsec.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Login;
import com.pango.hsec.hsec.controller.ActivityController;

/**
 * Created by Andre on 02/11/2017.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService { //implements IActivity

    public static final String TAG = "ATENCION";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: " + token);
        String Oldtoken=getTokenFromPrefs();
        /*if(!Oldtoken.equals(token)){
            GlobalVariables.token_ok=false;
            saveTokenToPrefs(token);
            if(GlobalVariables.token_refresh){
                Intent dialogIntent = new Intent(this, Login.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        }*/
       // enviarTokenAlServidor(token);

    }

    private void saveTokenToPrefs(String _token)
    {
        // Access Shared Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString("registration_id", _token);
        editor.apply();
    }

    private String getTokenFromPrefs()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", "");
    }
    /*
    private void enviarTokenAlServidor(String token) {
        String url= GlobalVariables.Url_base+"Usuario/updatetoken/"+token;
        ActivityController obj = new ActivityController("get", url, MiFirebaseInstanceIdService.this,new Login());
        obj.execute("");
    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
        Log.d(TAG, "Rspt Server: " + data);
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }*/
}
