package com.pango.hsec.hsec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Recuperar_password extends AppCompatActivity {

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

                   // ResPassController restaurar_pass = new ResPassController(email, "post", Recuperar_password.this);
                   /// restaurar_pass.execute();
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




}
