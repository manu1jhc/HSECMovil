package com.pango.hsec.hsec.Observaciones;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ViewPagerAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.utilitario.TouchImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Galeria_detalle extends AppCompatActivity implements IActivity {
    //public static final String VIEW_NAME_HEADER_IMAGE = "";
    TouchImageView imagenExtendida;
    PhotoViewAttacher photoViewAttacher;
    int positionIn;
    String NroDocReferencia="";

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_detalle);

        Bundle datos = this.getIntent().getExtras();
        positionIn=datos.getInt("post");
        NroDocReferencia=datos.getString("codigo");
        imagenExtendida = (TouchImageView) findViewById(R.id.imagen_extendida);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        if(NroDocReferencia==null)setdata();
        else{
            String url=GlobalVariables.Url_base+"media/GetMultimedia/"+NroDocReferencia;
            final ActivityController obj = new ActivityController("get", url, this,this);
            obj.execute("");
        }
    }

    public void setdata(){

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

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
        int count = getGaleriaModel.Count;
        GlobalVariables.listaGaleria=getGaleriaModel.Data;
        for (int i = 0; i < GlobalVariables.listaGaleria.size(); i++) {
                if (GlobalVariables.listaGaleria.get(i).Correlativo==positionIn) {
                    {
                        positionIn=i;
                        continue;
                    }
                }
        }
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
