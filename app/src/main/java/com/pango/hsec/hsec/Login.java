package com.pango.hsec.hsec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.GetTokenController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.firebase.DeleteTokenService;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements IActivity{

    private TextInputEditText et_User;
    private TextInputEditText et_Password;
    private TextInputLayout ti_User;
    private TextInputLayout ti_Password;
    String user="";
    String pass="";
    String dom="";
    String url=GlobalVariables.Url_base+"membership/GetVersion";
    String url_token="";
    //String data;
    CheckBox check_rec;
    TextView tx_rec_pass;
    Button btn_entrar;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    ProgressBar progresbar;
    ConstraintLayout constraintLayout4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalVariables.isFragment=false;

        final ActivityController objVersion = new ActivityController("get-2", url, Login.this,this);
        objVersion.execute("2");

        et_User = (TextInputEditText) findViewById(R.id.et_User);
        et_Password = (TextInputEditText) findViewById(R.id.et_Password);
        ti_User = (TextInputLayout) findViewById(R.id.ti_User);
        ti_Password = (TextInputLayout) findViewById(R.id.ti_Password);
////recordar contraseña y olvide contraseña
        check_rec=(CheckBox) findViewById(R.id.checkBox);
        tx_rec_pass=(TextView) findViewById(R.id.tx_rec_pass);
        btn_entrar=findViewById(R.id.btn_entrar);
        constraintLayout4=findViewById(R.id.constraintLayout4);
        progresbar= findViewById(R.id.progressBar3);

        tx_rec_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Recuperar_password.class);
                startActivity(intent);
            }
        });

    }

    public void passVersion(){
        if(obtener_status()){
            check_rec.setChecked(true);
            String usuario_saved=obtener_usuario();
            String pass_saved=obtener_pass();
            et_User.setText(usuario_saved);
            et_Password.setText(pass_saved);

            validate(this.findViewById(android.R.id.content));
            //flagpopup=true;

        }else{
            constraintLayout4.setVisibility(View.VISIBLE);
            progresbar.setVisibility(View.GONE);
        }
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
            constraintLayout4.setVisibility(View.GONE);
            user=et_User.getText().toString();
            pass=et_Password.getText().toString();
            dom="anyaccess";
            url_token=GlobalVariables.Url_base+"membership/authenticate";//?"+"username="+user+"&password="+pass+"&domain="+dom;

            //https://app.antapaccay.com.pe/HSECWeb/WHSEC_Service/api/usuario/getdata/
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("username",user);
                jsonObject.accumulate("password",pass);
                jsonObject.accumulate("domain",dom);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final GetTokenController objT = new GetTokenController(url_token,Login.this,progresbar);
            objT.execute(jsonObject.toString());
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

    public void inicialiceApp(){
        if(GlobalVariables.pasnotification){
            Intent intent = new Intent(Login.this, obsFacilitoDet.class);
            intent.putExtra("codObs", GlobalVariables.codFacilito);
            intent.putExtra("verBoton", "-1");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void success(String data1,String Tipo) {

        if(Tipo=="1") {
            if (GlobalVariables.con_status == 200) {

                if (check_rec.isChecked()) {
                    //guardar estado del check, user pass
                    Save_status(true);
                    Save_Datalogin(et_User.getText().toString(), et_Password.getText().toString());


                } else {
                    Save_status(false);
                    Save_Datalogin("", "");
                    //guarde el estado false ""en todos los campos
                }

            } else {
                et_User.setText("");
                et_Password.setText("");
                check_rec.setChecked(false);
            }
            String token_movil=getTokenFromPrefs();
            if(token_movil.equals("")){
                GlobalVariables.token_refresh=true;
                Intent intentService = new Intent(Login.this, DeleteTokenService.class);
                startService(intentService);
                finish();
            }
            else if(!GlobalVariables.token_ok)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);

                Call<String> request = service.updateToken("Bearer "+GlobalVariables.token_auth,token_movil);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.isSuccessful()){
                            String respt  = response.body();
                            if(respt.contains("-1")){
                                Toast.makeText(Login.this,"Ocurrio un error al guardar token de instalacion",Toast.LENGTH_SHORT).show();
                            }
                            else inicialiceApp();
                        }else{
                            Toast.makeText(Login.this,"Ocurrio un error al guardar token de instalacion",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(Login.this,"Ocurrio un error al guardar token de instalacion",Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else inicialiceApp();

        }else if(Tipo=="2"){
                String version_app=obtener_version();
                String version_server=data1.substring(1, data1.length() - 1);
                if (version_app == "" || Float.parseFloat(version_app) >= Float.parseFloat(version_server)) {
                    save_version(version_server);
                    passVersion();
                }
                else {
                    //save_versionAnt(version_app);
                    save_version(version_server);
                    Intent mainIntent = new Intent().setClass(Login.this, ActActualizar.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {

        if(mensaje.equals("Se perdio la conexión a internet")){
            Utils.cargar_alerta(this,Login.this);
        }else {

            constraintLayout4.setVisibility(View.VISIBLE);
            Save_status(false);
            Save_Datalogin("", "");

            et_User.setText("");
            et_Password.setText("");
            check_rec.setChecked(false);

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    private String getTokenFromPrefs()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", "");
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

    public void save_version(String version){
        SharedPreferences check_version = this.getSharedPreferences("versiones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_version = check_version.edit();
        editor_version.putString("version", version);
        editor_version.commit();
    }

/*
    public void save_versionAnt(String version){
        SharedPreferences check_versionA = this.getSharedPreferences("versionAnt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_versionA = check_versionA.edit();
        editor_versionA.putString("versionA", version);
        editor_versionA.commit();
    }
*/

    public  String  obtener_version(){
        SharedPreferences check_version = this.getSharedPreferences("versiones", Context.MODE_PRIVATE);
        String version = check_version.getString("version","");
        return version;
    }




}
