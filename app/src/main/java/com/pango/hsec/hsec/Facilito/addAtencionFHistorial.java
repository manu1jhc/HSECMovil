package com.pango.hsec.hsec.Facilito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
    Button btnFechaInicio;
    String fechaEscogida;
    Spinner spinner_estado;
    ArrayList<Maestro> ObsFacilito_estadoHistoria;
    ProgressBar progressBar;
    private RecyclerView gridView;
    private GridViewAdapter gridViewAdapter;
    private CardView cv_fecha;
    private ArrayList<GaleriaModel> DataImg;
    private ArrayList<GaleriaModel> DataImgList;
    private ImageButton btnFotos,ButtonGuardar;
    private TextView txv_comentario;
    ArrayList<Integer> Actives=new ArrayList();
    private TextView textViewtitle;
    public String obsFHistorialModel;
    String Errores,estado;
    String codObs,correEdit;
    Gson gson;
    String url;
    SimpleDateFormat df,dt;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM");
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
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dt = new SimpleDateFormat("dd 'de' MMMM");
        progressBar = findViewById(R.id.progressBar2);
        boolean[] pass = {false,false};
        Integer[] itemSel = {0,0,0};
        gson = new Gson();
        DataImg = new ArrayList<>();
        DataImgList=new ArrayList<>();
        ObsHist = new ObsFHistorialModel();
        ObsFacilito_estadoHistoria= new ArrayList<>();
        ObsFacilito_estadoHistoria.addAll(GlobalVariables.ObsFacilito_estadoHistoria);
        ArrayAdapter adapterNivelR = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, ObsFacilito_estadoHistoria);
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
        if(GlobalVariables.flaghistorial==true) {
            textViewtitle.setText("Agregar Atención");
            ObsHist.Correlativo="-1";
            Bundle data1 = this.getIntent().getExtras();
            codObs=data1.getString("codObs");

        }
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();

                SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
                fecha_real=df.format(actual);
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
                    if(GlobalVariables.flaghistorial==true) {
                        try {
                            Actives.add(0);
                            ObsHist.CodObsFacilito = codObs;
                            if (estado.equals("S")) {
                                ObsHist.FechaFin=fecha_real;
//                                ObsHist.FechaFin = String.valueOf(btnFechaInicio.getText());
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
                    if(GlobalVariables.flaghistorial==false){

                        try {
                            ObsHist.CodObsFacilito = codObs;
                            if (estado.equals("S")) {
                                ObsHist.FechaFin = String.valueOf(btnFechaInicio.getText());
                            } else {
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
        if(GlobalVariables.flaghistorial==false) {
            textViewtitle.setText("Editar Atención");
            Bundle data1 = this.getIntent().getExtras();

            codObs=GlobalVariables.codObsHistorial;
            correEdit=data1.getString("correlativo");
            Actives.add(0);
            String url2=GlobalVariables.Url_base+"ObsFacilito/GetHistorialAtencion/"+codObs;
            final ActivityController obj2 = new ActivityController("get", url2, addAtencionFHistorial.this,this);
            obj2.execute("1");
            Actives.add(0);
            String url1=GlobalVariables.Url_base+"media/GetMultimedia/"+codObs+"-"+correEdit;
            final ActivityController obj1 = new ActivityController("get", url1, addAtencionFHistorial.this,this);
            obj1.execute("2");

        }
        else {
            setdata();
        }

    }
    public boolean ValifarFormulario(View view){
        String ErrorForm="Detalle:\n";
        if(ErrorForm.equals("Detalle:\n")) ErrorForm="";
        if(StringUtils.isEmpty(ObsHist.Estado)) ErrorForm+=" ->Estado\n";
        if(StringUtils.isEmpty(ObsHist.Comentario)) ErrorForm+=" ->Comentario\n";
        if(ErrorForm.isEmpty()) return true;
        else{
            Snackbar.make(view, "Complete los siguientes campos obligatorios:\n"+ErrorForm, Snackbar.LENGTH_LONG).show();
            return false;
        }
    }
    public void setdata() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);

        if(GlobalVariables.flaghistorial==false){
            if(!StringUtils.isEmpty(ObsHist.Estado))spinner_estado.setSelection(GlobalVariables.indexOf(GlobalVariables.ObsFacilito_estadoHistoria,ObsHist.Estado));
            txv_comentario.setText(ObsHist.Comentario);
            try {
                String data=ObsHist.FechaFin;

                if(data==null){

                }
                else {
                    Date fecha = df.parse(ObsHist.FechaFin);
                    btnFechaInicio.setText(formatoRender.format(fecha));
                    if(ObsHist.FechaFin!=null)
                    {
                        fecha = df.parse(ObsHist.FechaFin);
                        btnFechaInicio.setText(dt.format(fecha));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String s="";
        }
    }
    public void close(View view){
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void escogerFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog.getDatePicker().setMaxDate(tempCalendar.getTimeInMillis());
        datePickerDialog.show();
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
            if (apt && DeleteFiles.equals("") && DataInsert.size() == 0) {
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            } else {
//Delete Files
                if (!DeleteFiles.equals("")) {
                    Actives.add(0);
                    String url = GlobalVariables.Url_base + "media/deleteAll/" + DeleteFiles.substring(0, DeleteFiles.length() - 1);
                    ActivityController obj = new ActivityController("get", url, addAtencionFHistorial.this, this);
                    obj.execute("3");
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
                    Call<String> request = service.uploadAllFile("Bearer " + GlobalVariables.token_auth, createPartFromString(codObs), createPartFromString("OBF"), createPartFromString(ObsHist.Correlativo), Files);
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
                            if (!Actives.contains(0)) FinishSave();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Actives.set(2,-1);
                            Errores += "\nFallo la subida de archivos";
                            if (!Actives.contains(0)) FinishSave();
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
        if(Actives.contains(1))ok=true;
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
                    finish();
                }
//                btn_Salvar.setEnabled(true);
            }
        });

        alertDialog.show();

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

        if (Tipo.equals("1")){
            Actives.set(0,1);
            Gson gson = new Gson();
            int contPublicacion;
            GetObsFHistorialModel getObsFHistorialModel = gson.fromJson(data, GetObsFHistorialModel.class);
            contPublicacion=getObsFHistorialModel.Count;
            if(GlobalVariables.listaHistorial.size()==0) {
                GlobalVariables.listaHistorial = getObsFHistorialModel.Data;
            }
            for(int i=0;i<GlobalVariables.listaHistorial.size();i++){
                if (GlobalVariables.listaHistorial.get(i).Correlativo.equals(correEdit)) {
                    ObsHist=GlobalVariables.listaHistorial.get(i);
                    obsFHistorialModel = gson.toJson(ObsHist);
                }
            }
        }
        if(Tipo.equals("2")){
            Actives.set(1,1);
            Gson gson = new Gson();
            DataImg=gson.fromJson(data, GetGaleriaModel.class).Data;
            DataImgList=gson.fromJson(data, GetGaleriaModel.class).Data;
            GlobalVariables.StrFiles=DataImgList;
        }
        else if(Tipo.equals("3")){
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
            if(!Actives.contains(0)) FinishSave();
        }
        if(!Actives.contains(0)){
            setdata();
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
       if(GlobalVariables.flaghistorial==true){

           Gson gson = new Gson();
           ObsHist.Correlativo = data.substring(1, data.length() - 1);

       }
        if(data.equals("-1")){
//            UpdateFiles(false);
            Errores+="\nOcurrio un error al guardar cabezera";
            Actives.set(0,-1);
        }
        else{
            UpdateFiles(false);
            Actives.set(0,1);
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

}
