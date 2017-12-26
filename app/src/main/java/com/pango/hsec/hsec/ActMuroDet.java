package com.pango.hsec.hsec;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

public class ActMuroDet extends AppCompatActivity {
    TabHost tabHost;
    ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_muro_det);
        close=findViewById(R.id.imageButton);
        Resources res = getResources();

        tabHost=(TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec=tabHost.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);

        spec.setIndicator("Observaciones",
                res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Fotos",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabHost.addTab(spec);


        spec=tabHost.newTabSpec("mitab3");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Plan de acción",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabHost.addTab(spec);


        spec=tabHost.newTabSpec("mitab4");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Comentarios",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabHost.addTab(spec);

        //tabHost.setCurrentTab(0);


    }


    public void close(View view){
    finish();
    }


}
