package com.pango.hsec.hsec.Ficha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Login;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.UsuarioModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CambiarPassword extends AppCompatActivity implements IActivity {
EditText pass,new_pass,rep_new_pass;
Button change_pass;
TextView tx_mensaje;
ImageButton i1,i2,i3;
boolean btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_password);

        //user=(EditText) findViewById(R.id.tx_user);
        pass=(EditText) findViewById(R.id.tx_pass_act);
        new_pass=(EditText) findViewById(R.id.tx_new_pass);
        rep_new_pass=(EditText) findViewById(R.id.tx_rep_new_pass);
        tx_mensaje=(TextView) findViewById(R.id.tx_mensaje);
        tx_mensaje.setText("");


        i1=(ImageButton) findViewById(R.id.btn_eliminar1);
        i2=(ImageButton) findViewById(R.id.btn_eliminar2);
        i3=(ImageButton) findViewById(R.id.btn_eliminar3);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.setText("");
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pass.setText("");
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rep_new_pass.setText("");
            }
        });


    }

    protected void SavePassword(View view){
        if(!btnSend){
            btnSend=true;
            String a= GlobalVariables.json_user;
            String b=pass.getText().toString();
            String c=new_pass.getText().toString();
            String d=rep_new_pass.getText().toString();

            if(b.isEmpty()||c.isEmpty()||d.isEmpty()){
                tx_mensaje.setText("Los campos no pueden estar vacíos");
            }else  if(c.length()<5||c.length()>20){
                tx_mensaje.setText("La contraseña debe tener entre 5 a 20 caracteres");
            }else  if(c.equals(d)){
                tx_mensaje.setText("");
                JSONObject jsonObject = new JSONObject();
                Gson gson= new Gson();
                try {
                    jsonObject.accumulate("username",gson.fromJson(GlobalVariables.json_user, UsuarioModel.class).CodPersona);
                    jsonObject.accumulate("password",b);
                    jsonObject.accumulate("domain",c);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url2= GlobalVariables.Url_base+"membership/Changuepass";
                ActivityController obj2 = new ActivityController("post", url2, this,this);
                obj2.execute(jsonObject.toString());

            }else {
                //Toast.makeText(CambiarPassword.this,"Los campos de nueva contraseña y repetir nueva contraseña no coinciden",Toast.LENGTH_SHORT).show();
                tx_mensaje.setText("Los campos de nueva contraseña y repetir nueva contraseña no coinciden");
            }
        }

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
        alertDialog.setTitle("Operación exitosa!");
        alertDialog.setIcon(R.drawable.confirmicon);
        alertDialog.setMessage(mesage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CambiarPassword.this, Login.class);
                intent.putExtra("Check","False");
                startActivity(intent);
                alertDialog.dismiss();
                tx_mensaje.setText("");
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
        if(data.contains("correctamente")){
            FinishChangue(data);
        }
        tx_mensaje.setText(data);
        btnSend=false;
    }

    @Override
    public void error(String mensaje, String Tipo) {

        tx_mensaje.setText(mensaje);
        btnSend=false;
    }
}
