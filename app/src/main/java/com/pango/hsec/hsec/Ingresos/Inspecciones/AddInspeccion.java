package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.FragmentInspeccion;
import com.pango.hsec.hsec.Inspecciones.FragmentObsInsp;
import com.pango.hsec.hsec.Inspecciones.FragmentParticipante;
import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.EquipoModel;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetEquipoModel;
import com.pango.hsec.hsec.model.InspeccionModel;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.ProgressRequestBody;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddInspeccion extends FragmentActivity implements IActivity,TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks{
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs="";
    String urlObs="";
    int pos=0;
    HorizontalScrollView horizontalscroll;
    Button btn_Salvar;
    ArrayList<Integer> Actives=new ArrayList();
    String CodInspeccion,Errores="";
    TextView tx_titulo;
    ActivityController activityTask;

    Call<String> request;
    ConstraintLayout ll_bar_carga;
    ProgressBar progressBar;
    ImageButton btncancelar;
    TextView txt_percent;
    Boolean cancel, enableSave=true;
    long L,G,T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_inspeccion);
        reiniciadata();

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        horizontalscroll=findViewById(R.id.horizontalscroll);
        btn_Salvar=findViewById(R.id.btnguardar_insp);

        progressBar = findViewById(R.id.progressBar2);
        ll_bar_carga=findViewById(R.id.ll_bar_carga);
        ll_bar_carga.setVisibility(View.GONE);
        btncancelar= (ImageButton)findViewById(R.id.cancel_upload);
        txt_percent= (TextView)findViewById(R.id.txt_percent);

        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        GlobalVariables.ListResponsables=new ArrayList<>();
        GlobalVariables.ListAtendidos=new ArrayList<>();
        GlobalVariables.ListobsInspAddModel=new ArrayList<>();
        GlobalVariables.countObsInsp=1;
        tx_titulo=findViewById(R.id.tx_titulo);
        if(GlobalVariables.ObjectEditable){
            tx_titulo.setText("Editar Inspección");
        }else{
            tx_titulo.setText("Nueva Inspección");
        }

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(AddInspeccion.this);

    }

    public void reiniciadata(){
        GlobalVariables.AddInspeccion=new InspeccionModel(); //cabecera
        GlobalVariables.StrInspeccion=null;

        GlobalVariables.ListResponsables=new ArrayList<>();
        GlobalVariables.StrResponsables=new ArrayList<>();

        GlobalVariables.ListAtendidos=new ArrayList<>();
        GlobalVariables.StrAtendidos=new ArrayList<>();

        GlobalVariables.ListobsInspAddModel=new ArrayList<>();
        GlobalVariables.StrtobsInspAddModel=new ArrayList<>();
        GlobalVariables.InspeccionObserbacion=null;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        outInspeccion();
    }
    public void outInspeccion(){
        boolean Nochangues=true;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
       // String Observacion=  gson.toJson(GlobalVariables.Obserbacion);
        String Inspeccion=  gson.toJson(GlobalVariables.AddInspeccion);

        if(!GlobalVariables.StrInspeccion.equals(Inspeccion)) Nochangues=false;
        if(Nochangues&&!GlobalVariables.ObjectEditable&&GlobalVariables.ListResponsables.size()>0) Nochangues=false;
        else if(GlobalVariables.ObjectEditable&&GlobalVariables.ListResponsables.size()>0){
            // get Lider
            String LiderPer="",LiderOld="";
            for (EquipoModel item:GlobalVariables.StrResponsables) {
                if(item.Lider.equals("1")) LiderOld=item.CodPersona;
            }
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                if(item.Lider.equals("1")) LiderPer=item.CodPersona;
            }

            ArrayList<EquipoModel> updateResponsables = new ArrayList<>();
            //Insert equipo
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrResponsables)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateResponsables.add(new EquipoModel(item.CodPersona,"A"));
                    if(!pass)GlobalVariables.StrResponsables.add(item);
                }
            }
            if(updateResponsables.size()>0||!LiderPer.equals(LiderOld))Nochangues=false;
        }
        if(Nochangues&&GlobalVariables.StrResponsables.size()>0){
            String DeletePlanes="";
            for (EquipoModel item:GlobalVariables.StrResponsables) {
                boolean pass=true;
                for (EquipoModel item2:GlobalVariables.ListResponsables) {
                    if(item.CodPersona.equals(item2.CodPersona)){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeletePlanes+=item.CodPersona+";";
                }
            }
            if(!DeletePlanes.equals(""))Nochangues=false;
        }
        if(Nochangues&&!GlobalVariables.ObjectEditable&&GlobalVariables.ListAtendidos.size()>0) Nochangues=false;
        else if(GlobalVariables.ObjectEditable&&GlobalVariables.ListAtendidos.size()>0){
            ArrayList<EquipoModel> updateAtentidos = new ArrayList<>();
            //Insert Atentidos
            for (EquipoModel item:GlobalVariables.ListAtendidos) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrAtendidos)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateAtentidos.add(new EquipoModel(item.CodPersona,"A"));
                    if(!pass)GlobalVariables.StrAtendidos.add(item);
                }
            }
            if(updateAtentidos.size()>0)Nochangues=false;
        }
        if(Nochangues&&GlobalVariables.StrAtendidos.size()>0){
            String DeletePlanes="";
            for (EquipoModel item:GlobalVariables.StrAtendidos) {
                boolean pass=true;
                for (EquipoModel item2:GlobalVariables.ListAtendidos) {
                    if(item.CodPersona.equals(item2.CodPersona)){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeletePlanes+=item.CodPersona+";";
                }
            }
            if(!DeletePlanes.equals(""))Nochangues=false;
        }
        if(Nochangues) {
            if (!GlobalVariables.ObjectEditable&&GlobalVariables.ListobsInspAddModel.size() > 0) Nochangues=false;
            else if (GlobalVariables.StrtobsInspAddModel.size() > 0) {
                String DeleteObservaciones = "";
                for (ObsInspAddModel item : GlobalVariables.StrtobsInspAddModel) {
                    boolean pass = true;
                    for (ObsInspAddModel item2 : GlobalVariables.ListobsInspAddModel) {
                        if (item.obsInspDetModel.Correlativo.equals(item2.obsInspDetModel.Correlativo)) {
                            pass = false;
                            continue;
                        }
                    }
                    if (pass) {
                        DeleteObservaciones += item.obsInspDetModel.Correlativo + ";";
                    }
                }
                if (!DeleteObservaciones.equals("")) Nochangues = false;
            }
        }
        if(!Nochangues)
        {
            String Mensaje="Esta seguro de salir sin guardar cambios?\n";
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            //alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos sin guardar");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }
        else finish();
    }
    public boolean ValifarFormulario(View view){
             String ErrorForm="";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.Gerencia)) {ErrorForm+="Gerencia";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.FechaP)) {ErrorForm+="Fecha Programada";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.Fecha)) {ErrorForm+="Fecha Inspeccion";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodUbicacion)) {ErrorForm+="Ubicación";pos=0;}
        else if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodTipo)) {ErrorForm+="Tipo de inspección";pos=0;}

        else if(GlobalVariables.ListResponsables.size()==0) {ErrorForm+="Personal responsables";pos=1;}
        else{
            boolean passlider=true;
            for (EquipoModel item: GlobalVariables.ListResponsables)
                if(item.Lider.equals("1")){
                    passlider=false;
                    continue;
                }
            if(passlider) ErrorForm+=" ->Selección de Lider en personal responsable\n";
        }
     //   if(GlobalVariables.ListAtendidos.size()==0) ErrorForm+=" ->Personal que atendio\n";
        //if(GlobalVariables.ListobsInspAddModel.size()==0) ErrorForm+=" ->Observaciones\n";

        if(ErrorForm.isEmpty()) return true;
        else{

/*
            String Mensaje="Complete los siguientes campos obligatorios:\n"+ErrorForm;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos incompletos");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
*/


            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacío", Snackbar.LENGTH_LONG).setActionTextColor(Color.CYAN).setAction("Ver pestaña", new View.OnClickListener() {
                //public TabHost mTabHost;

                @Override
                public void onClick(View v) {
                    //initialiseTabHost();
                    //pos=1;
                    mTabHost.setCurrentTab(pos);

                    //onPageScrolled(1, 0, 0);

                }
            }).show();



            return false;
        }
    }

    public void SaveInspeccion(View view){
        if(!enableSave)return;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario(view)) return;
        enableSave=(false);
        cancel=false;
        btncancelar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        txt_percent.setText("");

        String Inspeccion=  gson.toJson(GlobalVariables.AddInspeccion);
        Errores="";
        Actives.clear();
        if(GlobalVariables.ObjectEditable){
            String Cabecera,equipo, atentidos, ObsDelete;
            Cabecera=equipo=atentidos=ObsDelete="-";

            if(!GlobalVariables.StrInspeccion.equals(Inspeccion)) Cabecera=Inspeccion;

        //update equipo

            // get Lider
            String LiderPer="",LiderOld="";
            for (EquipoModel item:GlobalVariables.StrResponsables) {
                if(item.Lider.equals("1")) LiderOld=item.CodPersona;
            }
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                if(item.Lider.equals("1")) LiderPer=item.CodPersona;
            }

            ArrayList<EquipoModel> updateResponsables = new ArrayList<>();
            //Insert equipo
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrResponsables)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateResponsables.add(new EquipoModel(item.CodPersona,"A"));
                    if(!pass)GlobalVariables.StrResponsables.add(item);
                }
            }

            //Delete equipo
            for (EquipoModel item:GlobalVariables.StrResponsables) {
                boolean pass=true;
                for (EquipoModel item2:GlobalVariables.ListResponsables) {
                    if(item.CodPersona==item2.CodPersona){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    item.Estado="E";
                    updateResponsables.add(new EquipoModel(item.CodPersona,"E"));
                }
            }

            if(updateResponsables.size()>0|| LiderPer!=LiderOld){
                if(LiderPer!=LiderOld)
                    for (EquipoModel item:GlobalVariables.StrResponsables) {
                        if(item.CodPersona.equals(LiderPer)) item.Lider="1";
                        else item.Lider="0";
                    }

                EquipoModel Lider= new EquipoModel();
                Lider.NroReferencia=GlobalVariables.InspeccionObserbacion;
                Lider.Lider=LiderPer;
                Lider.Estado="L";
                updateResponsables.add(0,Lider);

                equipo=gson.toJson(updateResponsables);
            }

        //update Atentidos
            ArrayList<EquipoModel> updateAtentidos = new ArrayList<>();
            //Insert Atentidos
            for (EquipoModel item:GlobalVariables.ListAtendidos) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrAtendidos)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateAtentidos.add(new EquipoModel(item.CodPersona,"A"));
                    if(!pass)GlobalVariables.StrAtendidos.add(item);
                }
            }

            //Delete Atentidos
            for (EquipoModel item:GlobalVariables.StrAtendidos) {
                boolean pass=true;
                for (EquipoModel item2:GlobalVariables.ListAtendidos) {
                    if(item.CodPersona==item2.CodPersona){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    item.Estado="E";
                    updateAtentidos.add(new EquipoModel(item.CodPersona,"E"));
                }
            }
            if(updateAtentidos.size()>0){
                updateAtentidos.get(0).NroReferencia=GlobalVariables.InspeccionObserbacion;
                atentidos=gson.toJson(updateAtentidos);
            }

        //delete Observaciones.
            if(GlobalVariables.StrtobsInspAddModel.size()>0){
                String DeleteObservaciones="";
                for (ObsInspAddModel item:GlobalVariables.StrtobsInspAddModel) {
                    boolean pass=true;
                    for (ObsInspAddModel item2:GlobalVariables.ListobsInspAddModel) {
                        if(item.obsInspDetModel.Correlativo.equals(item2.obsInspDetModel.Correlativo)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        DeleteObservaciones+=item.obsInspDetModel.Correlativo+";";
                    }
                }
                if(!DeleteObservaciones.equals(""))ObsDelete=DeleteObservaciones.substring(0,DeleteObservaciones.length()-1);
            }//else Actives.add(1);

            if(Cabecera.equals("-")&& equipo.equals("-")&& atentidos.equals("-")&& ObsDelete.equals("-"))
            {
                enableSave=(true);
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
            else{
                Actives.add(0);
                ll_bar_carga.setVisibility(View.VISIBLE);
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                request = service.actualizarInspeccion("Bearer "+GlobalVariables.token_auth,createPartFromString(Cabecera),createPartFromString(equipo),createPartFromString(atentidos),createPartFromString(ObsDelete));
                onProgressUpdate();
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        onFinish();
                        if(response.isSuccessful()){
                            Log.i("Update Insp:", response.body());
                            Actives.set(0,1);
                            String respta[]  = response.body().split(";"); //"-;-;-;-;Carbones_de_Colombia9513.gif:57758"
                            for(int i =0;i<4;i++){
                                try{
                                String rpt=respta[i];
                                if(!rpt.equals("-")){
                                    if(rpt.contains("-1")){
                                        Actives.add(-1);
                                        switch (i){
                                            case 0:
                                                Errores+="\nOcurrio un error al guardar cabecera";
                                                break;
                                            case 1:
                                                Errores+="\nOcurrio un error al editar *Personas que realizan la inspección";
                                                break;
                                            case 2:
                                                Errores+="\nOcurrio un error al editar  *Personas que atendieron";
                                                break;
                                            case 3:
                                                Errores+="\nOcurrio un error al eliminar Observacion(es)";
                                                break;
                                        }
                                    }
                                    switch (i){
                                        case 0:
                                            GlobalVariables.StrInspeccion=gson.toJson(GlobalVariables.AddInspeccion);
                                            break;
                                        case 1:

                                            String[] respt=rpt.split(",");
                                            boolean pass=false;
                                            if(respt[respt.length-1].equals("-1")) {
                                                Errores+="Error al actualizar lider\n";
                                                pass=true;
                                            }
                                            for(EquipoModel item:GlobalVariables.StrResponsables){
                                                //for (String equipoid:data.split(","))
                                                for(int j=0;j<respt.length-1;j++)
                                                {
                                                    String[] value= respt[j].split(":");
                                                    if(item.CodPersona.equals(value[0])){
                                                        if(value[1].equals("-1")){
                                                            item.Estado="E";
                                                            pass=true;
                                                        }
                                                        else {
                                                            if(item.Estado.equals("E"))  item.Estado="D";
                                                            else item.NroReferencia=null;
                                                        }
                                                        continue;
                                                    }
                                                }
                                            }

                                            ArrayList<EquipoModel> temp=new ArrayList<>(GlobalVariables.StrResponsables);
                                            for (EquipoModel item:GlobalVariables.StrResponsables) {
                                                if(item.Estado.equals("D")) temp.remove(item);
                                            }
                                            GlobalVariables.StrResponsables=new ArrayList<>(temp);
                                            GlobalVariables.ListResponsables.clear();
                                            for(EquipoModel item:GlobalVariables.StrResponsables)
                                                    GlobalVariables.ListResponsables.add((EquipoModel)item.clone());

                                            break;
                                        case 2:

                                            for(EquipoModel item:GlobalVariables.StrAtendidos){
                                                for (String equipoid:rpt.split(","))
                                                {
                                                    String[] value= equipoid.split(":");
                                                    if(item.CodPersona.equals(value[0])){
                                                        if(value[1].equals("-1")){
                                                            item.Estado="E";
                                                            pass=true;
                                                        }
                                                        else {
                                                            if(item.Estado.equals("E"))  item.Estado="D";
                                                            else item.NroReferencia=null;
                                                        }
                                                        continue;
                                                    }
                                                }
                                            }

                                            ArrayList<EquipoModel> tempe=new ArrayList<>(GlobalVariables.StrAtendidos);
                                            for (EquipoModel item:GlobalVariables.StrAtendidos) {
                                                if(item.Estado.equals("D")) tempe.remove(item);
                                            }
                                            GlobalVariables.StrAtendidos=new ArrayList<>(tempe);
                                            GlobalVariables.ListAtendidos.clear();
                                            for(EquipoModel item:GlobalVariables.StrAtendidos)
                                                GlobalVariables.ListAtendidos.add((EquipoModel)item.clone());
                                            break;
                                        case 3:
                                            GlobalVariables.StrtobsInspAddModel=GlobalVariables.ListobsInspAddModel;
                                            break;
                                    }
                                }
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            }

                        }else{
                            if(response.code()==401){
                                Utils.reloadTokenAuth(AddInspeccion.this,AddInspeccion.this);
                                progressBar.setProgress(0);
                                txt_percent.setText(0+"%");
                            }
                            else {
                                Actives.set(0,-1);
                                Errores+="\nOcurrio un error interno de servidor";
                            }
                        }
                        if(!Actives.contains(0)) FinishSave();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(!cancel){
                            Actives.set(0,-1);
                            if(t.getMessage().equals("timeout"))Errores+="\nConexión a servidor perdida, intente de nuevo";
                            else Errores+="\nOcurrio un error al intentar guardar los datos.";
                            if(!Actives.contains(0)) FinishSave();
                        }
                    }
                });
            }
        }
        else{  //Insert new Inspeccion
            Actives.add(0);
            ll_bar_carga.setVisibility(View.VISIBLE);
            ArrayList<ObsInspDetModel> Observaciones = new ArrayList<>();
            ArrayList<PlanModel> Planes = new ArrayList<>();
            ArrayList<GaleriaModel> AllFiles = new ArrayList<>();
            if(GlobalVariables.ListobsInspAddModel.size()>0){
                ArrayList<GaleriaModel> Images = new ArrayList<>();
                ArrayList<GaleriaModel> Archives = new ArrayList<>();
                for (ObsInspAddModel item:GlobalVariables.ListobsInspAddModel) {
                    Observaciones.add(item.obsInspDetModel);
                    for (GaleriaModel file: item.listaGaleria ) {
                        file.Estado=item.obsInspDetModel.NroDetInspeccion;
                    }
                    for (GaleriaModel file: item.listaArchivos ) {
                        file.Estado=item.obsInspDetModel.NroDetInspeccion;
                    }
                    Images.addAll(item.listaGaleria);
                    Archives.addAll(item.listaArchivos);
                    Planes.addAll(item.Planes);
                }
                if(Images.size()>0){
                    GridViewAdapter gridViewAdapter = new GridViewAdapter(this,Images);
                    gridViewAdapter.ProcesarImagens();
                }
                AllFiles.addAll(Images);
                AllFiles.addAll(Archives);
            }
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalVariables.Url_base)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WebServiceAPI service = retrofit.create(WebServiceAPI.class);
            List<MultipartBody.Part> Files = new ArrayList<>();

            G=T=L=0;
            for (GaleriaModel item:AllFiles) {
                Files.add(createPartFromFile(item));
                T+=Long.parseLong(item.Tamanio);
            }
          //  Toast.makeText(this, "Guardando Inspeccion, Espere..." , Toast.LENGTH_SHORT).show();

            request = service.insertarInspeccion("Bearer "+GlobalVariables.token_auth,createPartFromString(Inspeccion),createPartFromString(gson.toJson(GlobalVariables.ListResponsables)),createPartFromString(gson.toJson(GlobalVariables.ListAtendidos)),createPartFromString(gson.toJson(Observaciones)),createPartFromString(gson.toJson(Planes)),Files);
            if(T==0)onProgressUpdate();
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful()){
                        String respta  = response.body();
                        Log.i("Insert Insp: ", respta);
                        if(respta.equals("-1")){
                            Actives.set(0,-1);
                            Errores+="\nOcurrio un error al guardar inspección";
                        }
                        else {
                            Actives.set(0,1);  // update value Cabecera
                            Utils.DeleteCache(new Compressor(AddInspeccion.this).destinationDirectoryPath); //delete cache Files;
                            String [] respts= respta.split(";");
                            GlobalVariables.AddInspeccion.CodInspeccion=respts[0];
                            GlobalVariables.InspeccionObserbacion= respts[0];
                            GlobalVariables.StrInspeccion=gson.toJson(GlobalVariables.AddInspeccion);

                            FragmentAddCabecera cabecera = (FragmentAddCabecera) pageAdapter.getItem(0);
                            cabecera.updateCodigo(respts[0]);

                            //update responsable(Equipo)
                            if(respts[1].equals("-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar cambios de personas que realizan la inspección";
                            }
                            // update responsable (Equipo)
                            boolean passPlan=false;
                            if(!respts[1].equals("0"))
                            {
                                String[] respt=respts[1].split(",");
                                boolean pass=false;
                                if(respt[respt.length-1].equals("-1")) {
                                    Errores+="Error al actualizar lider\n";
                                    pass=true;
                                }
                                for(EquipoModel item:GlobalVariables.ListResponsables){
                                    for(int i=0;i<respt.length-1;i++)
                                    {
                                        String[] value= respt[i].split(":");
                                        if(item.CodPersona.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                pass=true;
                                            }
                                            else item.NroReferencia=null;
                                            continue;
                                        }
                                    }
                                }

                                for(EquipoModel item:GlobalVariables.ListResponsables)
                                    try {
                                        GlobalVariables.StrResponsables.add((EquipoModel)item.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                //GlobalVariables.ListResponsables=new ArrayList<>(temp);
                                if(pass) {
                                    Actives.add(-1);
                                    Errores+="Error al agregar algunas personas que realizan la inspección";
                                }
                            }

                            //update Atendidos
                            if(respts[2].equals("-1")){
                                Actives.add(-1);
                                Errores+="\nError al guardar personas que atendieron";
                            }
                            // update Atendidos
                            boolean passAtendidos=false;
                            if(!respts[2].equals("0"))
                            {
                                boolean pass=false;
                                for(EquipoModel item:GlobalVariables.StrAtendidos){
                                    for (String equipoid:respts[2].split(","))
                                    {
                                        String[] value= equipoid.split(":");
                                        if(item.CodPersona.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                pass=true;
                                            }
                                            else item.NroReferencia=null;

                                            continue;
                                        }
                                    }
                                }

                                for(EquipoModel item:GlobalVariables.ListAtendidos)
                                    try {
                                        GlobalVariables.StrAtendidos.add((EquipoModel)item.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                if(pass) {
                                    Actives.add(-1);
                                    Errores+="Error al agregar algunas personas que atendieron";
                                }
                            }

                            //update Observaciones
                            if(!respts[3].equals("0"))
                            {
                                boolean passObs=false;
                                for (String file:respts[3].split(",")) {
                                    String[] datosf= file.split(":");
                                    for (ObsInspAddModel item:GlobalVariables.ListobsInspAddModel) {
                                        if(item.obsInspDetModel.NroDetInspeccion.equals(datosf[0]))
                                        {
                                            if(datosf[1].equals("-1")){
                                                item.obsInspDetModel.Estado="E";
                                                passObs=true;
                                            }
                                            else {
                                                item.obsInspDetModel.Correlativo=datosf[1];
                                                item.obsInspDetModel.CodInspeccion=respts[0];
                                            }
                                            continue;
                                        }
                                    }
                                }
                                for(ObsInspAddModel item:GlobalVariables.ListobsInspAddModel)
                                    try {
                                        GlobalVariables.StrtobsInspAddModel.add((ObsInspAddModel)item.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }

                                if(passObs){
                                    Actives.add(-1);
                                    Errores+="\nError al guardar algunas observaciones";
                                }
                            }
                        }
                    }else{
                        if(response.code()==401){
                            Utils.reloadTokenAuth(AddInspeccion.this,AddInspeccion.this);
                            progressBar.setProgress(0);
                            txt_percent.setText(0+"%");
                        }
                        else {
                            Actives.set(0,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                    }
                    if(!Actives.contains(0)) FinishSave();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(!cancel)
                    {
                        Actives.set(0,-1);
                        if(t.getMessage().equals("timeout"))Errores+="\nConexión a servidor perdida, intente de nuevo";
                        else Errores+="\nFallo la subida de la inspección.";
                        if(!Actives.contains(0)) FinishSave();
                    }
                }
            });
        }
    }

    public void close(View view){
        outInspeccion();
    }


    // Method to add a TabHost
    private static void AddTab(AddInspeccion activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {


        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs

    //numero de tab que quieres mostrar


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(pos);

        View tabView = mTabHost.getTabWidget().getChildAt(position);
        if (tabView != null)
        {
            final int width = horizontalscroll.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            horizontalscroll.scrollTo(scrollPos, 0);
        } else {
            horizontalscroll.scrollBy(positionOffsetPixels, 0);
        }


    }

    @Override
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();


        // TODO Put here your Fragments
        FragmentAddCabecera f1 = FragmentAddCabecera.newInstance(codObs);
        FragmentAddParticipante f2 = FragmentAddParticipante.newInstance(codObs);
        FragmentAddObservacion f3 = FragmentAddObservacion.newInstance(codObs);

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        AddInspeccion.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("Cabecera"));
        AddInspeccion.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("Participantes"));
        AddInspeccion.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Observaciones"));
        mTabHost.setOnTabChangedListener(this);

    }

    @NonNull
    private RequestBody createPartFromString(String data){
        return RequestBody.create(MultipartBody.FORM,data);
    }

    @NonNull
    private MultipartBody.Part createPartFromFile(GaleriaModel item){
        ProgressRequestBody fileBody = new ProgressRequestBody(item, this,this);
        return  MultipartBody.Part.createFormData(item.Estado, item.Descripcion, fileBody);
    }

    boolean ok,fail;
    public void FinishSave(){

        String Mensaje="Se guardo los datos correctamente";
        String Titulo="Desea Finalizar?";
        int icon=R.drawable.confirmicon;
        ok=false;
        fail=false;
        if(Actives.contains(1))ok=true;
        if(Actives.contains(-1)){
            fail=true;
            icon=R.drawable.erroricon;
            Mensaje="Ocurrio un error al intentar guardar los datos.";
            Titulo="Ocurrio un Error";
        }
        if(fail&&ok){
            Titulo="Intente de nuevo";
            Mensaje="Se guardo con los siguientes errores:\n";
            Mensaje+=Errores;//.replace("@","\n");
            icon=R.drawable.warninicon;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(Titulo);
        alertDialog.setIcon(icon);
        alertDialog.setMessage(Mensaje);
        if(ok&&!fail)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    enableSave=(true);
                    ll_bar_carga.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    finish();
                }
                enableSave=(true);
                ll_bar_carga.setVisibility(View.GONE);
                progressBar.setProgress(0);
                if(!fail)GlobalVariables.ObjectEditable=true;
            }
        });
        FragmentAddParticipante fragment = (FragmentAddParticipante) pageAdapter.getItem(1);
        fragment.setdata();
        alertDialog.show();
    }

    @Override
    public void success(String data, String Tipo) {

        if(Tipo.equals("1")){ //delete Observaciones
            if(data.contains("-1")){
                Actives.set(3,-1);
                Errores+="\nNo se pudo eliminar algunas observaciones";
            }
            else {
                GlobalVariables.StrtobsInspAddModel=GlobalVariables.ListobsInspAddModel;
                Actives.set(3,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else { // tipo reloadToken
            enableSave=(true);
            SaveInspeccion(null);
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
        progressBar.setProgress(100);
        ll_bar_carga.setVisibility(View.GONE);

        Gson gson = new Gson();
        if(Tipo.equals("1")){ // post inpeccion
            if(data.contains("-1"))  {
                Actives.set(0,-1);
                Errores+="Error al guardar cabecera inpeccion\n";
            }
            else{
                GlobalVariables.StrInspeccion=gson.toJson(GlobalVariables.AddInspeccion);
                Actives.set(0,1);
            }
        }
        else if(Tipo.equals("2")){  // post equipo
            if(data.contains("-1")) {
                Actives.set(1,-1);
                Errores+="Error al guardar cambios de personas que realizan la inspección\n";
            }
            else{

                data=data.substring(1,data.length()-1);
                String[] respt=data.split(",");
                boolean pass=false;
                if(respt[respt.length-1].equals("-1")) {
                    Errores+="Error al actualizar lider\n";
                    pass=true;
                }
                    for(EquipoModel item:GlobalVariables.StrResponsables){
                        //for (String equipoid:data.split(","))
                        for(int i=0;i<respt.length-1;i++)
                        {
                            String[] value= respt[i].split(":");
                            if(item.CodPersona.equals(value[0])){
                                if(value[1].equals("-1")){
                                    item.Estado="E";
                                    pass=true;
                                }
                                else {
                                    if(item.Estado.equals("E"))  item.Estado="D";
                                    else item.NroReferencia=null;
                                }
                                continue;
                            }
                        }
                    }

                ArrayList<EquipoModel> temp=new ArrayList<>(GlobalVariables.StrResponsables);
                for (EquipoModel item:GlobalVariables.StrResponsables) {
                    if(item.Estado.equals("D")) temp.remove(item);
                }
                GlobalVariables.StrResponsables=new ArrayList<>(temp);
                GlobalVariables.ListResponsables.clear();
                for(EquipoModel item:GlobalVariables.StrResponsables)
                    GlobalVariables.ListResponsables.add((EquipoModel)item.clone());
                //GlobalVariables.ListResponsables=new ArrayList<>(temp);
                if(pass) {
                        Actives.set(1,-1);
                        Errores+="Error al actualizar algunas personas que realizan la inspección\n";
                }
                else  Actives.set(1,1);
            }
        }

        else if(Tipo.equals("3")){  // post atentidoa
            if(data.contains("-1")) {
                Actives.set(2,-1);
                Errores+="Error al guardar cambios de personas que atendieron\n";
            }
            else{
                data=data.substring(1,data.length()-1);
                boolean pass=false;
                for(EquipoModel item:GlobalVariables.StrAtendidos){
                    for (String equipoid:data.split(","))
                    {
                        String[] value= equipoid.split(":");
                        if(item.CodPersona.equals(value[0])){
                            if(value[1].equals("-1")){
                                item.Estado="E";
                                pass=true;
                            }
                            else {
                                if(item.Estado.equals("E"))  item.Estado="D";
                                else item.NroReferencia=null;
                            }
                            continue;
                        }
                    }
                }

                ArrayList<EquipoModel> temp=new ArrayList<>(GlobalVariables.StrAtendidos);
                for (EquipoModel item:GlobalVariables.StrAtendidos) {
                    if(item.Estado.equals("D")) temp.remove(item);
                }
                GlobalVariables.StrAtendidos=new ArrayList<>(temp);
                GlobalVariables.ListAtendidos.clear();
                for(EquipoModel item:GlobalVariables.StrAtendidos)
                    GlobalVariables.ListAtendidos.add((EquipoModel)item.clone());
                // GlobalVariables.ListAtendidos=new ArrayList<>(temp);
                if(pass) {
                    Actives.set(2,-1);
                    Errores+="Error al actualizar algunas personas que atendieron\n";
                }
                else  Actives.set(2,1);
            }
        }
        if(!Actives.contains(0)) FinishSave();
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public void onProgressUpdate(){

        progressBar.setProgress(50);
        txt_percent.setText(50+"%");

    }
    @Override
    public void onProgressUpdate(long percentage) {
        if(percentage==0)L=G;
        G=L + percentage;
        int percent=(int)Math.round(100 * (double)G / (double)T);
        progressBar.setProgress(percent);
        txt_percent.setText(percent+"%");//String.format("%.2f", 100*(double)G / (double)T)+"%");
        if(percent==100){
            btncancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        btncancelar.setVisibility(View.GONE);
        progressBar.setProgress(100);
        txt_percent.setText("100%");
    }

    public void cancelUpload(View view) {
        if(request!=null){
            request.cancel();
            ll_bar_carga.setVisibility(View.GONE); enableSave=true;
            cancel=true;
        }
    }


}
