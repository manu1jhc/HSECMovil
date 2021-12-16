package com.pango.hsec.hsec.firebase;

import android.content.Intent;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.TextView;

import com.pango.hsec.hsec.Facilito.obsFacilitoDet;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;

public class MessageShowActivity extends AppCompatActivity {

    //private ImageView imageView;
    private TextView titleTextView;
    //private TextView timeStampTextView;
    private TextView articleTextView;
    private ConstraintLayout clclose;
    private CardView cardview;
    private Handler handler;
    public String Codigo,tipoObs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_notificacion);
        viewInitialization();
        handler = new Handler();
        Bundle datos = this.getIntent().getExtras();
        String title="",article="";
        Codigo="";
        tipoObs="TO01";
        //receive data from MyFirebaseMessagingService class
        if(datos==null){
            title=  "Titulo"; article="Descripcion de la notificacion";
        }
        else{
            title = datos.getString("title","");// getIntent().getStringExtra("title");
            article = datos.getString("descripcion","");
            Codigo =  datos.getString("codigo","");
            tipoObs =  datos.getString("tipo","TO01");
        }
        //String imageUrl = getIntent().getStringExtra("image");

        //Set data on UI
        titleTextView.setText(title);
        articleTextView.setText(article);
        /*Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);*/
        clclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });
        cardview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (Codigo.substring(0,3)){
                    case "OBF":
                        Intent intent = new Intent(MessageShowActivity.this, obsFacilitoDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("verBoton", "-1");
                        startActivity(intent);
                        break;
                    case "OBS":
                        intent = new Intent(MessageShowActivity.this, ActMuroDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",4);
                        intent.putExtra("tipoObs",tipoObs);
                        startActivity(intent);
                        break;
                    case "INS":
                        intent = new Intent(MessageShowActivity.this, ActInspeccionDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",3);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);
                        break;
                    case "NOT":
                        intent = new Intent(MessageShowActivity.this, ActNoticiaDet.class);
                        intent.putExtra("codObs",Codigo);
                        intent.putExtra("posTab",0);
                        //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);
                        startActivity(intent);
                        break;
                }

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    private void viewInitialization() {
        //imageView = (ImageView) findViewById(R.id.featureGraphics);
        titleTextView = (TextView) findViewById(R.id.txt_nombres);
        //timeStampTextView = (TextView) findViewById(R.id.timeStamp);
        articleTextView = (TextView) findViewById(R.id.txt_cargo);
        clclose = (ConstraintLayout) findViewById(R.id.clnotification);
        cardview = (CardView) findViewById(R.id.cardView6);


    }
}
