package com.pango.hsec.hsec.Observaciones;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ViewPagerAdapter;
import com.pango.hsec.hsec.utilitario.TouchImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Galeria_detalle extends AppCompatActivity {
    //public static final String VIEW_NAME_HEADER_IMAGE = "";
    TouchImageView imagenExtendida;
    PhotoViewAttacher photoViewAttacher;
    int positionIn;

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_detalle);

        Bundle datos = this.getIntent().getExtras();
        positionIn=datos.getInt("post");

        imagenExtendida = (TouchImageView) findViewById(R.id.imagen_extendida);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(Galeria_detalle.this, GlobalVariables.listaGaleria,positionIn);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(positionIn,true);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("image_array", GlobalVariables.listdetimg);
        outState.putInt("savedImagePosition",positionIn);

    }

}
