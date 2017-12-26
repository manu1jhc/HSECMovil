package com.pango.hsec.hsec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.GetTokenController;
import com.pango.hsec.hsec.model.UsuarioModel;

public class Login extends AppCompatActivity implements IActivity{

    private TextInputEditText et_User;
    private TextInputEditText et_Password;
    private TextInputLayout ti_User;
    private TextInputLayout ti_Password;
    String user="";
    String pass="";
    String dom="";
    String url="";
    String url_token="";
    //String data;
    CheckBox check_rec;
    TextView tx_rec_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalVariables.isFragment=false;
        et_User = (TextInputEditText) findViewById(R.id.et_User);
        et_Password = (TextInputEditText) findViewById(R.id.et_Password);
        ti_User = (TextInputLayout) findViewById(R.id.ti_User);
        ti_Password = (TextInputLayout) findViewById(R.id.ti_Password);
////recordar contraseña y olvide contraseña
        check_rec=(CheckBox) findViewById(R.id.checkBox);
        tx_rec_pass=(TextView) findViewById(R.id.tx_rec_pass);


        if(obtener_status()){
            check_rec.setChecked(true);
            String usuario_saved=obtener_usuario();
            String pass_saved=obtener_pass();
            et_User.setText(usuario_saved);
            et_Password.setText(pass_saved);
        }

        tx_rec_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Recuperar_password.class);
                startActivity(intent);

            }
        });




    }

    public void clearUser(View view){
        et_User.setText("");
        //et_Password.setText("");
    }
    public void clearPass(View view){
       // et_User.setText("");
        et_Password.setText("");
    }


    //////////////////////VALIDACION DE CLIC//////////////////////////////////
    public void validate(View view) {
        if(TextUtils.isEmpty(et_User.getText())||TextUtils.isEmpty(et_Password.getText())) {
            String userError = null;
            if (TextUtils.isEmpty(et_User.getText())) {
                userError = getString(R.string.m_Error);
            }
            toggleTextInputLayoutError(ti_User, userError);
            String passError = null;
            if (TextUtils.isEmpty(et_Password.getText())) {
                passError = getString(R.string.m_Error);
            }
            toggleTextInputLayoutError(ti_Password, passError);
            clearFocus();
        }else if (et_Password.getText().length()<5||et_Password.getText().length()>20){
            Toast.makeText(Login.this,"La contraseña debe tener entre 5 a 20 caracteres",Toast.LENGTH_SHORT).show();
        }else {

            user=et_User.getText().toString();
            pass=et_Password.getText().toString();
            dom="anyaccess";
            url_token="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/membership/authenticate?"+"username="+user+"&password="+pass+"&domain="+dom;

            //https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/usuario/getdata/

            final GetTokenController objT = new GetTokenController(url_token,Login.this);
            objT.execute();
/*
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (objT.getStatus() == AsyncTask.Status.FINISHED) {
                        if(GlobalVariables.token_auth.length()<40) {
                            Toast.makeText(Login.this,GlobalVariables.token_auth,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final ActivityController obj = new ActivityController("get", url, Login.this);
                            obj.execute();
                        }

                    } else {
                        h.postDelayed(this, 50);
                    }


                }
            }, 50);*/

        }
    }

    /**
     * Display/hides TextInputLayout error.
     * @param msg the message, or null to hide
     */
    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }


    @Override
    public void success(String data1) {

        if(GlobalVariables.con_status==200){
            if(check_rec.isChecked()){
                //guardar estado del check, user pass
                Save_status(true);
                Save_Datalogin(et_User.getText().toString(),et_Password.getText().toString());
            }else
            {
                Save_status(false);
                Save_Datalogin("","");
                //guarde el estado false ""en todos los campos
            }
        }else{
            et_User.setText("");
            et_Password.setText("");
            check_rec.setChecked(false);
        }

        //Gson gson = new Gson();
        //List<UsuarioModel> Data=new ArrayList<UsuarioModel>();
        //Data = Arrays.asList(gson.fromJson(data, UsuarioModel[].class));

        //List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
       // UsuarioModel getUsuarioModel = gson.fromJson(data1, UsuarioModel.class);


        //int contPasajeros= getUsuarioModel.Count;
       /*
        Intent data2 = new Intent();
        data2.setData(Uri.parse(gson.toJson(getUsuarioModel)));
        setResult(RESULT_OK, data2);
        */
        //Toast.makeText(this,"O",Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void error(String mensaje) {

    }

    public void Save_status(boolean ischecked){
        SharedPreferences check_status = this.getSharedPreferences("checked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_estado = check_status.edit();
        editor_estado.putBoolean("check", ischecked);
        editor_estado.commit();
    }
    public void Save_Datalogin(String user,String password){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_user = user_login.edit();
        editor_user.putString("user", user);
        editor_user.putString("password",password);
        editor_user.commit();
    }

    public boolean obtener_status(){
        SharedPreferences check_status = this.getSharedPreferences("checked", Context.MODE_PRIVATE);
        Boolean status = check_status.getBoolean("check",false);
        return status;
    }
    public String obtener_usuario(){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String usuario = user_login.getString("user","");
        return usuario;
    }
    public String obtener_pass(){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String password = user_login.getString("password","");
        return password;
    }


}
