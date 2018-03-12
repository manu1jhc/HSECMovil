package com.pango.hsec.hsec.Ficha;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pango.hsec.hsec.R;

import layout.FragmentFichaPersonal;
import layout.FragmentMuro;

public class FichaPersona extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_persona);

        Fragment fragment = null;
        fragment = new FragmentFichaPersonal();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment)
                .commit();


        //GlobalVariables.dniUser




    }

    public void close(View view){
        finish();
    }
}
