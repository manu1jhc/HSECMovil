package com.pango.hsec.hsec.Ficha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.adapter.ListViewAdapter;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.obs_archivos;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.utilitario.InputFilterMinMax;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddRegistroAvance extends AppCompatActivity implements IActivity, Picker.PickListener {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText tx_avance,et_mensaje;
    Button decrement, increment,btnFechaInicio,btn_guardar;
    String fecha_inicio="-";
    boolean escogioFecha;
    String fechaEscogida;
    AccionMejoraModel AddAccionMejora=new AccionMejoraModel();
    String fecha="";
    //String PlanEnviar="";
    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<GaleriaModel> DataFiles;
    private ArrayList<GaleriaModel> DataImg;
    private ArrayList<GaleriaModel> Data=new ArrayList<>();
    Spinner spinnerUsuario;
    String CodAccion="",CodResponsable="",Responsable="";
    ArrayList<Maestro> usuario_data;

    private ArrayList<ImageEntry> mSelectedImages;
    ProgressDialog progressDialog;
    final String[] ACCEPT_MIME_TYPES = {
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/odt",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_registro_avance);
        tx_avance=findViewById(R.id.tx_avance);
        tx_avance.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        decrement=findViewById(R.id.decrement);
        increment=findViewById(R.id.increment);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha);
        et_mensaje=findViewById(R.id.et_mensaje);
        btn_guardar=findViewById(R.id.btn_guardar);
        //////////////
        ImageButton btnFotos=(ImageButton) findViewById(R.id.btn_addfotos);
        ImageButton btnFiles=(ImageButton) findViewById(R.id.btn_addfiles);
        gridView = (RecyclerView)  findViewById(R.id.grid);
        spinnerUsuario=(Spinner) findViewById(R.id.sp_persona);

        DataImg = new ArrayList<>();

        listView = (RecyclerView) findViewById(R.id.list);
        DataFiles = new ArrayList<>();


        Bundle datos = this.getIntent().getExtras();
        CodAccion=datos.getString("CodAccion");
        CodResponsable=datos.getString("CodResponsable");
        Responsable=datos.getString("Responsable");

        usuario_data= new ArrayList<>();
        usuario_data.add(new Maestro(null,"-  Seleccione  -"));
        String[] cod_Responsables=CodResponsable.split(";");

        String[] nom_Responsables=Responsable.split(";");

        for(int i=0;i<cod_Responsables.length;i++){
            usuario_data.add(new Maestro(cod_Responsables[i],nom_Responsables[i]));
        }

        ArrayAdapter adapterUsuario = new ArrayAdapter(this.getBaseContext(),android.R.layout.simple_spinner_item, usuario_data);
        adapterUsuario.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUsuario.setAdapter(adapterUsuario);



        spinnerUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    AddAccionMejora.CodResponsable=usuario_data.get(position).CodTipo;
                    AddAccionMejora.Responsable=usuario_data.get(position).Descripcion;
                    //area = area_data.get(position).CodTipo;
                    //area_pos= String.valueOf(position);
                }else {
                    AddAccionMejora.CodResponsable="";
                    //Utils.observacionModel.CodAreaHSEC=null;
                    //area="";
                    //area_pos=String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        //usuario_data.addAll();

        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );

        btnFiles.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setType("application/pdf");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
                                            startActivityForResult(Intent.createChooser(intent, "Choose Document"), 1);
                                        }
                                    }
        );


        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=tx_avance.getText().toString();
                int val_dec=Integer.parseInt(text)-1;
                if(val_dec>0&&val_dec<=100) {
                    //tx_avance.setText(val_dec+"");
                    tx_avance.setText(String.valueOf(val_dec), TextView.BufferType.EDITABLE);

                }
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=tx_avance.getText().toString();
                int val_dec=Integer.parseInt(text)+1;
                if(val_dec>0&&val_dec<=100) {
                    //tx_avance.setText(val_dec+"");
                    tx_avance.setText(String.valueOf(val_dec), TextView.BufferType.EDITABLE);

                }
            }
        });




        ////////////////////////////////////////////////
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();

                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd");
                //Utils.observacionModel.FechaInicio= String.valueOf(fecha_envio.format(actual));
                fecha=String.valueOf(fecha_envio.format(actual));

                fecha_inicio=dt.format(actual);
                btnFechaInicio.setText(dt.format(actual));
                // btnFechaFin.setText(dt.format(actual));
                // fecha_inicio=dt.format(actual);
                escogioFecha = true;
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
              /*  if (escogioOrigen && escogioDestino && escogioFecha)
                    botonBuscarTickets.setEnabled(true);
                    */
            }

        };


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String avance=tx_avance.getText().toString();
                String tarea_realizada=et_mensaje.getText().toString();

                Data.addAll(DataImg);
                Data.addAll(DataFiles);
                AddAccionMejora.CodAccion=CodAccion;
                AddAccionMejora.Correlativo="-1";
                AddAccionMejora.PorcentajeAvance=avance;
                AddAccionMejora.Descripcion=tarea_realizada;
                AddAccionMejora.Fecha=fecha;
                AddAccionMejora.Files=new GetGaleriaModel(Data);

                //AddAccionMejora.Files.Data.addAll(DataImg); //=new ArrayList<GaleriaModel>(DataImg);

                //AddAccionMejora.Files.Data.addAll(DataFiles);


                //DataFiles
                        //.add(new GaleriaModel(DataImg));



                Intent intent = getIntent();
                //intent.putExtra("Tipo_Busqueda",1);

                //intent.putExtra("nombreP",nombre);
                //intent.putExtra("codpersona",CodPersona);
                setResult(RESULT_OK, intent);
                finish();




            }
        });

    }

    public void close(View view){finish();}

    public void escogerFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        //datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
        //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        //datePickerDialog.getDatePicker().setMaxDate(myCalendar2.getTimeInMillis());
        datePickerDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_CANCELED)
        {
            // action cancelled
        }
        if(resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String displayName="";
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }

            String [] exts=displayName.split("\\.");
            String ext=exts[exts.length-1];
            switch (ext){
                case "pdf":case "doc":case "docx":case "ppt":case "pptx":case "xls":case "xlsx":case "odt":
                    DataFiles.add(new GaleriaModel(myFile.getAbsolutePath(),"TP03", myFile.getTotalSpace()+"", displayName));
                    GlobalVariables.listaArchivos=DataFiles;
                    //listView.setHasFixedSize(true);
                    //set layout manager and adapter for "ListView"
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    listView.setLayoutManager(horizontalManager);
                    listViewAdapter = new ListViewAdapter(this, DataFiles);
                    listView.setAdapter(listViewAdapter);

                    break;
                default: Toast.makeText(this, "Archivo no permitido", Toast.LENGTH_LONG).show();
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
            new Picker.Builder(this, AddRegistroAvance.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }




    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1011: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder agregar archivos multimedia, necesita otorgar los persmisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1022: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder utilizar la camara, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1033: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder grabar un video, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* final int id = item.getItemId();

        if (id == R.id.action_about) {
            showAbout();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
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
        //Log.i(TAG, "User canceled picker activity");
        // Toast.makeText(getActivity(), "User canceld picker activtiy", Toast.LENGTH_SHORT).show();

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


    private class ImageSamplesAdapter extends RecyclerView.Adapter<AddRegistroAvance.ImageSampleViewHolder> {

        @Override
        public AddRegistroAvance.ImageSampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ImageView imageView = new ImageView(parent.getContext());
            return new AddRegistroAvance.ImageSampleViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(AddRegistroAvance.ImageSampleViewHolder holder, int position) {

            final String path = mSelectedImages.get(position).path;
            loadImage(path, holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return mSelectedImages.size();
        }

        private void loadImage(final String path, final ImageView imageView) {
            imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 440));

            Glide.with(getApplicationContext())
                    .load(path)
                    .asBitmap()
                    .into(imageView);
        }
    }


    class ImageSampleViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnail;

        public ImageSampleViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView;
        }
    }


}
