package com.pango.hsec.hsec.Facilito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.GetObsFHistorialModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsFHistorialModel;
import com.pango.hsec.hsec.model.UsuarioModel;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addAtencionFHistorial extends AppCompatActivity implements IActivity,Picker.PickListener,ViewPager.OnPageChangeListener,ProgressRequestBody.UploadCallbacks{
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    public ObsFHistorialModel ObsHist;
    String fecha_inicio="-";
    String fecha_real="";
    Button btnFechaInicio,btn_hora;
    String fechaEscogida;
    Spinner spinner_estado;
    ProgressBar progressBar;
    private RecyclerView gridView;
    private GridViewAdapter gridViewAdapter;
    private CardView cv_fecha,cv_hora;
    private ArrayList<GaleriaModel> DataImg;
    private ArrayList<GaleriaModel> DataImgList;
    private ImageButton btnFotos,ButtonGuardar;
    private TextView txv_comentario;
    ArrayList<Integer> Actives=new ArrayList();
    private TextView textViewtitle;
    public String obsFHistorialModel;
    String Errores,estado;
    String fecha_fin="-";
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    int hora = c.get(Calendar.HOUR_OF_DAY);
    int minuto = c.get(Calendar.MINUTE);
    Gson gson;
    int indexObd,indexHist;
    String url;
    SimpleDateFormat df,dt;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM");
    SimpleDateFormat dh = new SimpleDateFormat("h:mm a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atencion_fhistorial);
        myCalendar = Calendar.getInstance();
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha);
        spinner_estado=(Spinner) findViewById(R.id.spinner_estado);
        btnFotos=(ImageButton) findViewById(R.id.btn_galeria);
        ButtonGuardar=(ImageButton) findViewById(R.id.ButtonGuardar);
        gridView = (RecyclerView)  findViewById(R.id.grid);
        txv_comentario=(TextView) findViewById(R.id.txv_comentario);
        textViewtitle=(TextView) findViewById(R.id.textViewtitle);
        cv_fecha=(CardView) findViewById(R.id.cv_fecha);
        cv_hora=(CardView) findViewById(R.id.cv_hora);

        btn_hora=(Button) findViewById(R.id.btn_hora);
        btn_hora.setText("SELECCIONAR HORA");
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dt = new SimpleDateFormat("dd 'de' MMMM");
        progressBar = findViewById(R.id.progressBar2);
        boolean[] pass = {false,false};
        Integer[] itemSel = {0,0,0};
        gson = new Gson();
        DataImg = new ArrayList<>();
        DataImgList=new ArrayList<>();
        ObsHist = new ObsFHistorialModel();
        ArrayAdapter adapterNivelR = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, GlobalVariables.ObsFacilito_estadoHistoria);
        adapterNivelR.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterNivelR);
        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(itemSel[0]!=position) {
                    itemSel[0] = position;

                    Maestro ubica = (Maestro) ((Spinner) findViewById(R.id.spinner_estado)).getSelectedItem();
                    estado = ubica.CodTipo;
                    ObsHist.Estado = estado;
                    if(ObsHist.Estado.equals("S")){
                        cv_fecha.setVisibility(View.VISIBLE);
                        cv_hora.setVisibility(View.VISIBLE);
                    }
                    else {
                        cv_fecha.setVisibility(View.GONE);
                        cv_hora.setVisibility(View.GONE);
                    }
                    adapterNivelR.notifyDataSetChanged();
                    if (pass[0]) {
                        pass[0] = false;
                    } else {
                        ObsHist.Estado = estado;
                        spinner_estado.setSelection(position);
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                estado="";
            }
        });
        if(GlobalVariables.flaghistorial) {
            textViewtitle.setText("Agregar Atención");
            ObsHist.Correlativo="-1";
            ObsHist.CodObsFacilito=GlobalVariables.codObsHistorial;
            Bundle data1 = this.getIntent().getExtras();
           // codObs=data1.getString("codObs");
        }
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();
                SimpleDateFormat fecha_envio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String FechaEnvio="";
                FechaEnvio= fecha_envio.format(actual);

                if(StringUtils.isEmpty(ObsHist.FechaFin))
                {
                    FechaEnvio=FechaEnvio.split("T")[0]+"T00:00:00";
                    btn_hora.setText("00:00:00");
                }
                else{
                    String [] hora=ObsHist.FechaFin.split("T");
                    FechaEnvio=FechaEnvio.split("T")[0]+"T"+hora[1];
                }
                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                //ObsHist.FechaFin=FechaEnvio;
                fecha_real=FechaEnvio;
                fecha_fin=dt.format(actual);
                btnFechaInicio.setText(dt.format(actual));
                dt = new SimpleDateFormat("yyyyMMdd");
                fechaEscogida = dt.format(actual);
            }

        };

        btnFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        ButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Errores="";
                    Actives.clear();
                    Gson gson = new Gson();
                    Utils.closeSoftKeyBoard(addAtencionFHistorial.this);
                    if(GlobalVariables.flaghistorial==true) { // save new Historial de atencion
                        try {
                            Actives.add(0);
                            //ObsHist.CodObsFacilito = codObs;
                            if (estado.equals("S")) {
                                ObsHist.FechaFin=fecha_real;
                            } else {
                                ObsHist.FechaFin=null;
                            }
                            ObsHist.Comentario = String.valueOf(txv_comentario.getText());
                            if (!ValifarFormulario(v)) return;
                            String url = GlobalVariables.Url_base + "ObsFacilito/AprobarObsFaci";
                            final ActivityController obj = new ActivityController("post", url, addAtencionFHistorial.this, addAtencionFHistorial.this);
                            obj.execute(gson.toJson(ObsHist));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if(GlobalVariables.flaghistorial==false){  //edit historial de atencion

                        try {
                          //  ObsHist.CodObsFacilito = codObs;
                            //ObsHist.Correlativo=correEdit;
                            if (estado.equals("S")) {
                                ObsHist.FechaFin = fecha_real;
                            } else {
                                ObsHist.FechaFin=null;
                            }
                            ObsHist.Comentario = String.valueOf(txv_comentario.getText());
                            if (!ValifarFormulario(v)) return;
                            String Obsstr=gson.toJson(ObsHist);
                            if(!Obsstr.equals(obsFHistorialModel)){
                                Actives.add(0);
                                String url = GlobalVariables.Url_base + "ObsFacilito/AprobarObsFaci";
                                final ActivityController obj = new ActivityController("post", url, addAtencionFHistorial.this, addAtencionFHistorial.this);
                                obj.execute(gson.toJson(ObsHist));
                            }
                            else {
                                Actives.add(1);
                                UpdateFiles(true);
                            }

                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(addAtencionFHistorial.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.DAY_OF_MONTH,1);
                tempCalendar.set(Calendar.HOUR, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND, 0);
                tempCalendar.set(Calendar.MILLISECOND, 0);

                datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
                tempCalendar.set(Calendar.DAY_OF_MONTH,tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                tempCalendar.set(Calendar.MONTH, (new Date()).getMonth());
                datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        btn_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(ObsHist.FechaFin)){
                    TimePickerDialog recogerHora = new TimePickerDialog(addAtencionFHistorial.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            //Formateo el hora obtenido: antepone el 0 si son menores de 10

                            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                            //String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                            //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                            String AM_PM;
                            int horaFinal;
                            int minutoFinal;
                            //GlobalVariables.AddInspeccion.Fecha=GlobalVariables.AddInspeccion.Fecha+"'"+"T"+"'"+String.valueOf(hourOfDay)+":"+minute;

                            if(hourOfDay < 12) {
                                AM_PM = "a.m.";
                                horaFinal=hourOfDay;
                                minutoFinal=minute;
                            } else {
                                AM_PM = "p.m.";
                                horaFinal=hourOfDay-12;
                                minutoFinal=minute;
                            }
                            //Muestro la hora con el formato deseado
                            String minutoFormateado = (minutoFinal < 10)? String.valueOf(CERO + minutoFinal):String.valueOf(minutoFinal);
                            String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                            String [] fecha=ObsHist.FechaFin.split("T");
                            fecha_real=fecha[0]+"T"+horaFormateada+":"+minutoFormateado+":00";

                            //AddInspeccion.hora=horaFinal + DOS_PUNTOS + minutoFormateado + " " + AM_PM;
                            btn_hora.setText(horaFinal + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                            hora=hourOfDay;
                            minuto=minute;

                        }
                        //Estos valores deben ir en ese orden
                        //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                        //Pero el sistema devuelve la hora en formato 24 horas
                    }, hora, minuto, false);
                    recogerHora.show();
                }
            }
        });
        if(GlobalVariables.flaghistorial==false) {
            textViewtitle.setText("Editar Atención");
            Bundle data1 = this.getIntent().getExtras();
            indexHist=data1.getInt("index");
            ObsHist=GlobalVariables.listaGlobalObsHistorial.get(indexHist);
            ObsHist.CodObsFacilito=GlobalVariables.codObsHistorial;

            String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+ObsHist.CodObsFacilito+"-"+ObsHist.Correlativo;
            final ActivityController obj1 = new ActivityController("get", url1, addAtencionFHistorial.this,this);
            obj1.execute("1");
        }
        else {
            setdata();
        }

    }
    public boolean ValifarFormulario(View view){
        String ErrorForm="";
        if(StringUtils.isEmpty(ObsHist.Estado)) ErrorForm+=" Estado";
        else if(ObsHist.Estado.equals("S")&&StringUtils.isEmpty(ObsHist.FechaFin))ErrorForm+=" Fecha y Hora";
        else if(StringUtils.isEmpty(ObsHist.Comentario)) ErrorForm+=" Acción";
        if(ErrorForm.isEmpty()) return true;
        else{
            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacio\n"+ErrorForm, Snackbar.LENGTH_LONG).show();
            return false;
        }
    }
    public void setdata() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);

        if(GlobalVariables.flaghistorial==false){ // editar historial de atencion
            if(!StringUtils.isEmpty(ObsHist.Estado))spinner_estado.setSelection(GlobalVariables.indexOf(GlobalVariables.ObsFacilito_estadoHistoria,ObsHist.Estado));
            txv_comentario.setText(ObsHist.Comentario);
            try {
                if(ObsHist.FechaFin!=null) {
                    Date fecha = df.parse(ObsHist.FechaFin);
                    btnFechaInicio.setText(formatoRender.format(fecha));
                    btn_hora.setText(dh.format(fecha));
                    if(ObsHist.FechaFin!=null)
                    {
                        fecha = df.parse(ObsHist.FechaFin);
                        btnFechaInicio.setText(dt.format(fecha));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public void close(View view){

        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressLint("NewApi")
    public void loadImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(this, addAtencionFHistorial.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }
    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        try {
            for (ImageEntry image:images) {
                gridViewAdapter.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",new File(image.path).length()+"",new File(image.path).getName())); //image.path.split("/")[image.path.split("/").length-1]
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Notification(){
        String url1=GlobalVariables.Url_base+"ObsFacilito/SendNotification/"+ObsHist.Correlativo+(GlobalVariables.flaghistorial?"-0":"-1");
        final ActivityController obj1 = new ActivityController("get", url1, addAtencionFHistorial.this,this);
        obj1.execute("3");
    }
    public void UpdateFiles(boolean apt){
        try {
            gridViewAdapter.ProcesarImagens();
            ArrayList<GaleriaModel> DataInsert = new ArrayList<>();
            ArrayList<GaleriaModel> DataAll = new ArrayList<>();
            DataAll.addAll(DataImg);
            // delete files
            String DeleteFiles = "";
            for (GaleriaModel item : GlobalVariables.StrFiles) {
                boolean pass = true;
                for (GaleriaModel item2 : DataAll) {
                    if (item.Correlativo == item2.Correlativo) {
                        pass = false;
                        continue;
                    }
                }
                if (pass) {
                    DeleteFiles += item.Correlativo + ";";
                    item.Estado = "E";
                }
            }
            for (GaleriaModel item : DataAll) {
                boolean pass = false;
                for (GaleriaModel item2 : GlobalVariables.StrFiles)
                    if (item.Descripcion.equals(item2.Descripcion))
                        pass = true;
                if (item.Correlativo == -1) {
                    DataInsert.add(item);
                    if (!pass) GlobalVariables.StrFiles.add(item);
                }
            }
            if (DeleteFiles.equals("") && DataInsert.size() == 0) {
                if(apt)Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
                else {
                    Actives.add(1);
                    Actives.add(1);
                    Actives.add(1);
                    Notification();
                }
            }
            else {
//Delete Files
                if (!DeleteFiles.equals("")) {
                    Actives.add(0);
                    String url = GlobalVariables.Url_base + "media/deleteAll/" + DeleteFiles.substring(0, DeleteFiles.length() - 1);
                    ActivityController obj = new ActivityController("get", url, addAtencionFHistorial.this, this);
                    obj.execute("2");
                }
                else Actives.add(1);
                if(DataInsert.size()>0){
                    Actives.add(0);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GlobalVariables.Url_base)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                    List<MultipartBody.Part> Files = new ArrayList<>();
                    for (GaleriaModel item : DataInsert) {
                        Files.add(createPartFromFile(item));
                    }
                    Toast.makeText(addAtencionFHistorial.this, "Guardando Atención, Espere...", Toast.LENGTH_SHORT).show();
                    Call<String> request = service.uploadAllFile("Bearer " + GlobalVariables.token_auth, createPartFromString(ObsHist.CodObsFacilito), createPartFromString("TOBF"), createPartFromString(ObsHist.Correlativo), Files);
                   progressBar.setVisibility(View.VISIBLE);
                    request.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful()) {
                                String respt = response.body();
                                if (respt.contains("-1")) {
                                    Actives.set(2,-1);
                                    Errores += "\nOcurrio un error al subir algunas imagenes";
                                }
                                else  Actives.set(2,1);
                                Utils.DeleteCache(new Compressor(addAtencionFHistorial.this).destinationDirectoryPath); //delete cache Files;
                                for (String file : respt.split(";")) {
                                    String[] datosf = file.split(":");
                                    for (GaleriaModel item : GlobalVariables.StrFiles) {
                                        if (item.Descripcion.equals(datosf[0])) {
                                            item.Correlativo = Integer.parseInt(datosf[1]);
                                            if (item.Correlativo == -1) item.Estado = "E";
                                            else {
                                                if (item.TipoArchivo.equals("TP01"))
                                                    item.Url = "/Media/getImage/" + datosf[1] + "/Image.jpg";
                                                else if (item.TipoArchivo.equals("TP02"))
                                                    item.Url = "/Media/Play/" + datosf[1] + "/Video.mp4";
                                                else
                                                    item.Url = "/Media/Getfile/" + datosf[1] + "/" + datosf[0];
                                            }
                                        }
                                    }
                                }

                            } else {
                                Actives.set(2,-1);
                                Errores += "\nOcurrio un error interno de servidor";
                            }
                            if (!Actives.contains(0))  Notification();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Actives.set(2,-1);
                            Errores += "\nFallo la subida de archivos";
                            if (!Actives.contains(0))  Notification();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
    @NonNull
    private RequestBody createPartFromString(String data){
        return RequestBody.create(MultipartBody.FORM,data);
    }
    @NonNull
    private MultipartBody.Part createPartFromFile(GaleriaModel item){
        ProgressRequestBody fileBody = new ProgressRequestBody(item,addAtencionFHistorial.this,this);
        return  MultipartBody.Part.createFormData("image", item.Descripcion, fileBody);
    }
    boolean ok,fail;
    public void FinishSave(){
        int icon=R.drawable.confirmicon;
        String Mensaje="Se guardaron los datos correctamente";
        ok=false;
        fail=false;
        if(Actives.contains(0)){ok=true;}
        if(Actives.contains(1)){ok=true;}
        if(Actives.contains(2)){ok=true;}
        if(Actives.contains(-1)){
            fail=true;
            icon=R.drawable.erroricon;
            Mensaje="Ocurrio algun error interno, No se pudo guardar los datos.";
        }
        if(fail&&ok){
            Mensaje="Se guardo con los siguientes errores:\n";
            Mensaje+=Errores;//.replace("@","\n");
            icon=R.drawable.warninicon;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Desea Finalizar?");
        alertDialog.setIcon(icon);
        alertDialog.setMessage(Mensaje);
        if(ok&&!fail)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    btn_Salvar.setEnabled(true);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    if(GlobalVariables.flaghistorial==true){
                        Gson gson = new Gson();
                        GlobalVariables.userLoaded=gson.fromJson(GlobalVariables.json_user, UsuarioModel.class);
                        ObsHist.Persona=GlobalVariables.userLoaded.Nombres;
                        DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
                        Date date = Calendar.getInstance().getTime();
                        String formattedDate = formatoRender.format(date);
                        ObsHist.Fecha=formattedDate;
                        ObsHist.UrlObs=GlobalVariables.userLoaded.NroDocumento;
                        Intent intent = getIntent();
                        intent.putExtra("historial",gson.toJson(ObsHist));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    if(GlobalVariables.flaghistorial==false){

                        Intent intent = getIntent();
                        intent.putExtra("historial",gson.toJson(ObsHist));
                        intent.putExtra("index",indexHist);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        alertDialog.show();
        if(GlobalVariables.flaghistorial)GlobalVariables.flaghistorial=false;
        DataImg.clear();
        for (GaleriaModel item: GlobalVariables.StrFiles) {
            DataImg.add(item);
        }
        setdata();
       /* Intent intent = getIntent();
        intent.putExtra("AccionMejora",gson.toJson(AddAccionMejora));
        setResult(RESULT_OK, intent);
        finish();*/
    }
    @Override
    public void onCancel() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {

        if(Tipo.equals("1")){
            Gson gson = new Gson();
            DataImg=gson.fromJson(data, GetGaleriaModel.class).Data;
            DataImgList=gson.fromJson(data, GetGaleriaModel.class).Data;
            GlobalVariables.StrFiles=DataImgList;
            setdata();
        }
        else if(Tipo.equals("2")){
            if(data.contains("false")){
                Actives.set(1,-1);
                Errores+="\nNo se pudo eliminar algunas imagenes";
            }
            else {
                Actives.set(1,1);
                ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                for (GaleriaModel item : GlobalVariables.StrFiles) {
                    if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                        temp.remove(item);
                }
                GlobalVariables.StrFiles=temp;
            }
            //if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("3")){//noticiones
            data= data.substring(1,data.length()-1);
            if(!data.isEmpty()){
                Actives.set(3,-1);
                for(String err:data.split(";"))
                    Errores+="\n"+err;
            }
            else
                Actives.set(3,1);
            FinishSave();
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

        data=data.substring(1, data.length() - 1);
        if(data.equals("-1")){
            Errores+="\nOcurrio un error al guardar cabezera";
            Actives.set(0,-1);
        }
        else{

            if(GlobalVariables.flaghistorial==true){
/*
                Gson gson = new Gson();
                ArrayList<String> CorrelativosNuevos=new ArrayList<>();
                CorrelativosNuevos.add(ObsHist.Correlativo);*/
                ObsHist.Correlativo = data;
                //correEdit= data;
            }
            Actives.set(0,1);
            UpdateFiles(false);
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
    }
    @Override
    public void onError() {

    }
    @Override
    public void onFinish() {
        progressBar.setProgress(100);
    }

    public String Obtenerfecha(String tempcom_fecha) {

        String fecha="";
        try {
            fecha=formatoRender.format(formatoInicial.parse(tempcom_fecha));
        } catch (ParseException e) {
            e.printStackTrace();
            fecha=tempcom_fecha;
        }

        return fecha;

    }
    public void AgregarAtencion(View view){
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
