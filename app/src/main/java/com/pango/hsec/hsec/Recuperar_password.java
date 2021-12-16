package com.pango.hsec.hsec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pango.hsec.hsec.controller.ActivityController;

import org.json.JSONException;
import org.json.JSONObject;

public class Recuperar_password extends AppCompatActivity implements IActivity{

    Button btn_enviar;
    EditText tx_email;
    ImageButton btn_borrarUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_password);
        setupToolBar();

        btn_enviar=(Button) findViewById(R.id.btn_enviar);
        tx_email=(EditText) findViewById(R.id.tx_email);

        btn_borrarUser=(ImageButton) findViewById(R.id.btn_deleteUser);

        btn_borrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx_email.setText("");
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=tx_email.getText().toString();

                if(GlobalVariables.validarEmail(email)) {

                    String json = "";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("Url",email);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json += jsonObject.toString();
                    //http://servidorpango/whsec_Servicedmz/api/Inspecciones/Get/INSP0000008309
                    String url= GlobalVariables.Url_base+"membership/Recoverypass";
                    ActivityController obj = new ActivityController("post", url, Recuperar_password.this,Recuperar_password.this);
                    obj.execute(json);
                }else {
                    Toast.makeText(Recuperar_password.this,"Ingrese un correo valido",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recuperar);
        //toolbar.setLogo(R.drawable.imagen1234);
        toolbar.setTitle("Recuperar Contraseña");
        if (toolbar == null) return;

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_retroceder);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }


    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

    }

    public void FinishChangue(String mesage){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Mensaje de servidor");
        alertDialog.setIcon(R.drawable.confirmicon);
        alertDialog.setMessage(mesage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Recuperar_password.this, Login.class);
                intent.putExtra("Check","False");
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
            FinishChangue(data);
    }

    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,"Ocurrio un error interno de servidor",Toast.LENGTH_SHORT).show();
    }
}
