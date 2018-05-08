package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TabHost;
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

import okhttp3.MultipartBody;
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
    ProgressBar progressBar;
    ArrayList<Integer> Actives=new ArrayList();
    String CodInspeccion,Errores="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_inspeccion);
        reiniciadata();

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        horizontalscroll=findViewById(R.id.horizontalscroll);
        btn_Salvar=findViewById(R.id.btnguardar_insp);
        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        progressBar = findViewById(R.id.progressBar2);
        GlobalVariables.ListResponsables=new ArrayList<>();
        GlobalVariables.ListAtendidos=new ArrayList<>();
        GlobalVariables.ListobsInspAddModel=new ArrayList<>();
        GlobalVariables.countObsInsp=1;

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

    public boolean ValifarFormulario(){
             String ErrorForm="";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.Gerencia)) ErrorForm+=" ->Gerencia\n";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.FechaP)) ErrorForm+=" ->Fecha Programada\n";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.Fecha)) ErrorForm+=" ->Fecha Inspeccion\n";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodUbicacion)) ErrorForm+=" ->Ubicación\n";
        if(StringUtils.isEmpty(GlobalVariables.AddInspeccion.CodTipo)) ErrorForm+=" ->Tipo de inspección\n";

        if(GlobalVariables.ListResponsables.size()==0) ErrorForm+=" ->Personal responsables\n";
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
            return false;
        }
    }

    public void SaveInspeccion(View view){

        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario()) return;
        btn_Salvar.setEnabled(false);
        String Inspeccion=  gson.toJson(GlobalVariables.AddInspeccion);
        Errores="";
        Actives.clear();
        if(GlobalVariables.ObjectEditable){
        //update Inspeccion Cabecera
            if(!GlobalVariables.StrInspeccion.equals(Inspeccion)){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"Inspecciones/Post";
                final ActivityController obj = new ActivityController("post", url, AddInspeccion.this,this);
                obj.execute(Inspeccion,"1");
            }
            else Actives.add(1);

        //update equipo

            // get Lider
            String LiderPer="",LiderOld="";
            for (EquipoModel item:GlobalVariables.StrResponsables) {
                if(item.Lider.equals("1")) LiderOld=item.CodPersona;
            }
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                if(item.Lider.equals("1")) LiderPer=item.CodPersona;
            }

            GetEquipoModel updateResponsables = new GetEquipoModel();
            //Insert equipo
            for (EquipoModel item:GlobalVariables.ListResponsables) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrResponsables)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateResponsables.Data.add(new EquipoModel(item.CodPersona,"A"));
                    if(!pass)GlobalVariables.StrAtendidos.add(item);
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
                    updateResponsables.Data.add(new EquipoModel(item.CodPersona,"E"));
                }
            }

            if(updateResponsables.size()>0|| LiderPer!=LiderOld){
                Actives.add(0);

                if(LiderPer!=LiderOld)
                    for (EquipoModel item:GlobalVariables.StrResponsables) {
                        if(item.CodPersona.equals(LiderPer)) item.Lider="1";
                        else item.Lider="0";
                    }

                EquipoModel Lider= new EquipoModel();
                Lider.NroReferencia=GlobalVariables.InspeccionObserbacion;
                Lider.Lider=LiderPer;
                Lider.Estado="L";
                updateResponsables.Data.add(0,Lider);

                String url= GlobalVariables.Url_base+"Inspecciones/PostEquipo";
                final ActivityController obj = new ActivityController("post", url, AddInspeccion.this,this);
                obj.execute(gson.toJson(updateResponsables),"2");
            }
            else Actives.add(1);


        //update Atentidos
            GetEquipoModel updateAtentidos = new GetEquipoModel();
            //Insert Atentidos
            for (EquipoModel item:GlobalVariables.ListAtendidos) {
                boolean pass=false;
                for(EquipoModel item2:GlobalVariables.StrAtendidos)
                    if(item.CodPersona.equals(item2.CodPersona))
                        pass=true;
                if(!StringUtils.isEmpty(item.NroReferencia)&&item.NroReferencia.equals("-1")) {
                    updateAtentidos.Data.add(new EquipoModel(item.CodPersona,"A"));
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
                    updateAtentidos.Data.add(new EquipoModel(item.CodPersona,"E"));
                }
            }
            if(updateAtentidos.size()>0){
                updateAtentidos.Data.get(0).NroReferencia=GlobalVariables.InspeccionObserbacion;
                Actives.add(0);
                String url= GlobalVariables.Url_base+"Inspecciones/PostAtendidos";
                final ActivityController obj = new ActivityController("post", url, AddInspeccion.this,this);
                obj.execute(gson.toJson(updateAtentidos),"3");
            }
            else Actives.add(1);

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
                if(!DeleteObservaciones.equals("")){
                    Actives.add(0);
                    String url= GlobalVariables.Url_base+"Inspecciones/deleteAll/"+DeleteObservaciones.substring(0,DeleteObservaciones.length()-1);
                    ActivityController obj = new ActivityController("get", url, AddInspeccion.this,this);
                    obj.execute("1");
                }
                else Actives.add(1);
            }//else Actives.add(1);

            if(!Actives.contains(0)){
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
                btn_Salvar.setEnabled(true);
            }
        }
        else{  //Insert new Inspeccion
            Actives.add(0);
            ArrayList<ObsInspDetModel> Observaciones = new ArrayList<>();
            ArrayList<PlanModel> Planes = new ArrayList<>();
            ArrayList<GaleriaModel> AllFiles = new ArrayList<>();
            if(GlobalVariables.ListobsInspAddModel.size()>0){
                ArrayList<GaleriaModel> Images = new ArrayList<>();
                ArrayList<GaleriaModel> Archives = new ArrayList<>();
                for (ObsInspAddModel item:GlobalVariables.ListobsInspAddModel) {
                    Observaciones.add(item.obsInspDetModel);
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

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalVariables.Url_base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WebServiceAPI service = retrofit.create(WebServiceAPI.class);
            List<MultipartBody.Part> Files = new ArrayList<>();
            for (GaleriaModel item:AllFiles) {
                Files.add(createPartFromFile(item));
            }
            Toast.makeText(this, "Guardando Inspeccion, Espere..." , Toast.LENGTH_SHORT).show();

            Call<String> request = service.insertarInspeccion("Bearer "+GlobalVariables.token_auth,createPartFromString(Inspeccion),createPartFromString(gson.toJson(GlobalVariables.ListResponsables)),createPartFromString(gson.toJson(GlobalVariables.ListAtendidos)),createPartFromString(gson.toJson(Observaciones)),createPartFromString(gson.toJson(Planes)),Files);
            progressBar.setVisibility(View.VISIBLE);
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful()){
                        String respta  = response.body();
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
                        Actives.set(0,-1);
                        Errores+="\nOcurrio un error interno de servidor";
                    }
                    if(!Actives.contains(0)) FinishSave();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Actives.set(0,-1);
                    Errores+="\nFallo la subida de la inspección";
                    if(!Actives.contains(0)) FinishSave();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    public void close(View view){
        finish();
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
        return  MultipartBody.Part.createFormData("image", item.Descripcion, fileBody);
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
            Mensaje="Se guardo con los siguientes errores:\n\n";
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
                    btn_Salvar.setEnabled(true);
                    GlobalVariables.ObjectEditable=true;
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail){
                    finish();
                }
                btn_Salvar.setEnabled(true);
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
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {

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

}
