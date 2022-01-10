package com.pango.hsec.hsec.Ubicaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.Maps.MapsActivity2;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.Maestro;

import java.text.DecimalFormat;

public class ActUbicacion extends AppCompatActivity {

    TextView insp_maps;
    public ArrayAdapter adapterUbicEspc,adapterSubN;
    String latitudFinal, longitudFinal;

    ImageButton btn_buscar_m;
    Integer[] itemSel = {0,0};
    Spinner  spinner_ubicacion, spinner_sububicacion, spinner_ubic_esp;
    //Button button;
    String Ubicacionfinal="", Ubicacion="";
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);


        spinner_ubicacion=(Spinner) findViewById(R.id.spinner_ubicacion);
        spinner_sububicacion=(Spinner) findViewById(R.id.spinner_sububicacion);
        spinner_ubic_esp=(Spinner) findViewById(R.id.spinner_ubic_esp);
        //button = findViewById(R.id.button);
        btn_buscar_m = findViewById(R.id.btn_buscar_m);
        insp_maps = findViewById(R.id.insp_maps);
        //Ubicaciones
        GlobalVariables.reloadUbicacion();
        ArrayAdapter adapterUbic = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.Ubicacion_obs);
        adapterUbic.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_ubicacion.setAdapter(adapterUbic);

        adapterSubN = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item,GlobalVariables.SubUbicacion_obs);
        adapterSubN.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_sububicacion.setAdapter(adapterSubN);

        adapterUbicEspc = new ArrayAdapter(getBaseContext(),R.layout.custom_spinner_item, GlobalVariables.UbicacionEspecifica_obs);
        adapterUbicEspc.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_ubic_esp.setAdapter(adapterUbicEspc);
        obtenerCoordenadas();
        spinner_ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(itemSel[0]!=position) {
                    itemSel[0]=position;
                    Maestro ubica = (Maestro) ((Spinner) findViewById(R.id.spinner_ubicacion)).getSelectedItem();
                    Ubicacionfinal = ubica.CodTipo;
                    GlobalVariables.SubUbicacion_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 2)) {
                        GlobalVariables.SubUbicacion_obs.add(item);
                    }
                    adapterSubN.notifyDataSetChanged();
                   /* if(!pass[0]&&GlobalVariables.AddIncidenteSeg.CodUbicacion!=null)
                    {
                        pass[0] =true;
                        String data[]= Ubicacion.split("\\.");
                        if(data.length>1)
                            spinner_sububicacion.setSelection(GlobalVariables.indexOf(GlobalVariables.SubUbicacion_obs,data[0]+"."+data[1]));
                        else pass[1] =true;
                    }
                    else {
                        GlobalVariables.AddIncidenteSeg.CodUbicacion=Ubicacionfinal;
                        spinner_sububicacion.setSelection(0);
                    }*/
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = "";
            }
        });
        spinner_sububicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(itemSel[1]!=position) {
                    itemSel[1]=position;
                    Maestro Sububica = (Maestro) ((Spinner) findViewById(R.id.spinner_sububicacion)).getSelectedItem();
                    Ubicacionfinal = Sububica.CodTipo;
                    GlobalVariables.UbicacionEspecifica_obs.clear();
                    for (Maestro item : GlobalVariables.loadUbicacion(Ubicacionfinal, 3)) {
                        GlobalVariables.UbicacionEspecifica_obs.add(item);
                    }
                    adapterUbicEspc.notifyDataSetChanged();
                   /* if(!pass[1]&&GlobalVariables.AddIncidenteSeg.CodUbicacion!=null)
                    {
                        pass[1] =true;
                        if(Ubicacion.split("\\.").length==3)
                            spinner_ubic_esp.setSelection(GlobalVariables.indexOf(GlobalVariables.UbicacionEspecifica_obs, GlobalVariables.Obserbacion.CodUbicacion));
                    }
                    else{
                        GlobalVariables.AddIncidenteSeg.CodUbicacion=Ubicacionfinal;
                    }*/
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Ubicacionfinal = Ubicacionfinal.split("\\.")[0];
            }
        });
        spinner_ubic_esp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position>0)
                {
                    Maestro UbicaEspec = (Maestro) ( (Spinner) findViewById(R.id.spinner_ubic_esp) ).getSelectedItem();
                    Ubicacionfinal=UbicaEspec.CodTipo;
                    //GlobalVariables.AddIncidenteSeg.CodUbicacion=UbicaEspec.CodTipo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String Ubic[] =  Ubicacionfinal.split("\\.");
                Ubicacionfinal = Ubic[0]+"."+ Ubic[1];
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ActUbicacion.this, MapsActivity2.class);
//                startActivity(intent);
//            }
//        });

        btn_buscar_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActUbicacion.this, MapsActivity2.class);
                startActivityForResult(intent , 2);

            }
        });


    }

    public void obtenerCoordenadas() {
        if (ActivityCompat.checkSelfPermission(ActUbicacion.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActUbicacion.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         DecimalFormat precisionDecimal = new DecimalFormat("0.000000"); //6 decimales
        latitudFinal = precisionDecimal.format(loc.getLatitude());
        longitudFinal = precisionDecimal.format(loc.getLongitude());
         insp_maps.setText(latitudFinal + "," + longitudFinal);

    }
    public void close(View view){
        Utils.closeSoftKeyBoard(this);
        finish();
    }

    public void SaveUbicacion(View view){

    }

}