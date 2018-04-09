package com.pango.hsec.hsec.Facilito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.util.Picker.PickListener;

import java.util.ArrayList;
import java.util.Locale;

public class report_obs extends AppCompatActivity implements IActivity,Picker.PickListener  {
    ArrayList<Maestro> area_data;
    private EditText mtxvResult,mtxvUbicacion,mdt_accion;
    private ImageButton mbtnmicrofono;
    private ImageButton btnFotos,mbtn_microaccion,mbtn_microubi;
    private RecyclerView gridView;
    private ArrayList<GaleriaModel> DataImg;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<ImageEntry> mSelectedImages;
    private static View mView;
    private Button mbtn_acto,mbtn_condicion;
    Spinner spinnerArea;
    String flag="",area, area_pos="0";
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_obs);
        spinnerArea = (Spinner) findViewById(R.id.sp_area);
        mbtnmicrofono=(ImageButton) findViewById(R.id.btnmicrofono);

        mtxvResult=(EditText) findViewById(R.id.txvResult);
        mtxvUbicacion=(EditText) findViewById(R.id.txvUbicacion);
        mdt_accion=(EditText) findViewById(R.id.dt_accion);

        mbtn_acto=(Button) findViewById(R.id.btn_acto);
        mbtn_condicion=(Button) findViewById(R.id.btn_condicion);

        btnFotos=(ImageButton) findViewById(R.id.btn_galeria);
        mbtn_microubi=(ImageButton) findViewById(R.id.btn_microubi);
        mbtn_microaccion=(ImageButton) findViewById(R.id.btn_microaccion);

        gridView = (RecyclerView)  findViewById(R.id.grid);
        DataImg = new ArrayList<>();
        mbtnmicrofono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="observacion";
                askSpeechInput();
            }

        });
        mbtn_microubi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="ubicacion";
                askSpeechInput();
            }

        });
        mbtn_microaccion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag="accion";
                askSpeechInput();
            }

        });
        mbtn_condicion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mbtn_acto.setBackgroundColor(Color.parseColor("#cdf2d8"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#ff2222"));
            }
        });
        mbtn_acto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mbtn_acto.setBackgroundColor(Color.parseColor("#22b14c"));
                mbtn_condicion.setBackgroundColor(Color.parseColor("#fce8e8"));
            }
        });
        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );
        area_data= new ArrayList<>();
        area_data.add(new Maestro(null,"-  Seleccione  -"));
        area_data.addAll(GlobalVariables.Area_obs);
        ArrayAdapter adapterArea = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, area_data);
        adapterArea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    Utils.observacionModel.CodAreaHSEC=area_data.get(position).CodTipo;

                    //area = area_data.get(position).CodTipo;
                    area_pos= String.valueOf(position);
                }else {
                    Utils.observacionModel.CodAreaHSEC=null;
                    area="";
                    area_pos=String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable algo");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
    public void close(View view){
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(flag=="observacion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mtxvResult.setText(result.get(0));
                    }
                    break;
                }

            }
        }
        if(flag=="ubicacion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mtxvUbicacion.setText(result.get(0));
                    }
                    break;
                }

            }
        }
        if(flag=="accion"){
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mdt_accion.setText(result.get(0));
                    }
                    break;
                }

            }
        }

    }
    @SuppressLint("NewApi")
    public void loadImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(this, report_obs.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }
    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        for (ImageEntry image:images) {
            DataImg.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",image.dateAdded+"",image.imageId+""));
        }
        GlobalVariables.listaGaleria=DataImg;
        mSelectedImages = images;
        //  gridView.setHasFixedSize(true);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);

    }

    @Override
    public void onCancel() {

    }


    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
