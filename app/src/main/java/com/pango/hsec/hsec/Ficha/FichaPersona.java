package com.pango.hsec.hsec.Ficha;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pango.hsec.hsec.R;

import layout.FragmentFichaPersonal;

public class FichaPersona extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_persona);

        Fragment fragment = null;
        fragment = FragmentFichaPersonal.newInstance("1","1");
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
