
package com.pango.hsec.hsec;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.annotation.NonNull;
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
        import android.widget.ProgressBar;
        import android.widget.TabHost;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.pango.hsec.hsec.Ingresos.Inspecciones.ActObsInspEdit;
        import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
        import com.pango.hsec.hsec.Observaciones.MyTabFactory;
        import com.pango.hsec.hsec.adapter.GridViewAdapter;
        import com.pango.hsec.hsec.controller.ActivityController;
        import com.pango.hsec.hsec.controller.WebServiceAPI;
        import com.pango.hsec.hsec.model.GaleriaModel;
        import com.pango.hsec.hsec.model.GetPlanModel;
        import com.pango.hsec.hsec.model.ObsDetalleModel;
        import com.pango.hsec.hsec.model.ObservacionModel;
        import com.pango.hsec.hsec.model.PlanModel;
        import com.pango.hsec.hsec.util.Compressor;
        import com.pango.hsec.hsec.util.ProgressRequestBody;

        import org.apache.commons.lang3.StringUtils;

        import java.util.ArrayList;
        import java.util.List;

        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class observacion_edit extends FragmentActivity implements IActivity,TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ProgressRequestBody.UploadCallbacks{
    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    Button btn_Salvar;
    ProgressBar progressBar;
    HorizontalScrollView horizontalsv;
    int pos=0;
    ArrayList<Integer> Actives=new ArrayList();
    String CodObservacion,CodTipo,Errores="";
    ;
    //TabHost tabHost;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion_edit);
        reiniciadata();
        close=findViewById(R.id.imageButton);
        btn_Salvar=(Button)findViewById(R.id.btn_Salvar);
        horizontalsv=findViewById(R.id.HorizontalObsedit);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        progressBar = findViewById(R.id.progressBar2);
        Bundle datos = this.getIntent().getExtras();
        CodObservacion=datos.getString("codObs");
        CodTipo=datos.getString("tipoObs");
        pos=datos.getInt("posTab");
        //if(GlobalVariables.ObjectEditable) CodObservacion=datos.getString("Observacion");

        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mTabHost.setCurrentTab(pos);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(observacion_edit.this);

    }

public void reiniciadata(){
    GlobalVariables.Obserbacion= new ObservacionModel();
    GlobalVariables.ObserbacionDetalle= new ObsDetalleModel();
    GlobalVariables.listaArchivos=new ArrayList<>();
    GlobalVariables.listaGaleria=new ArrayList<>();
    GlobalVariables.ObserbacionFile=null;
    GlobalVariables.ObserbacionPlan=null;
    GlobalVariables.Planes=new ArrayList<>();
    //save data Inicial
    GlobalVariables.StrObservacion=null;
    GlobalVariables.StrObsDetalle=null;
    GlobalVariables.StrPlanes=new ArrayList<>();
    GlobalVariables.StrFiles=new ArrayList<>();

    //inicialize options
    GlobalVariables.obsInspDetModel=null;
    ActObsInspEdit.editar=true;

}
    public void close(View view){
        outObservacion();
    }
    @Override
    public void onBackPressed() {
        outObservacion();
        //super.onBackPressed();
    }
    // Method to add a TabHost
    private static void AddTab(observacion_edit activity, TabHost tabHost,TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        if(pos==1){ //cambios solo cuando ingresemos al tab de detalle de obs.
            obs_detalle1 detalle = (obs_detalle1) pageAdapter.getItem(1);
            detalle.changueTipo(GlobalVariables.Obserbacion.CodTipo);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(pos==2){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(pos);
            pos=0;
        }else{
           int posi = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(posi);
        }

        View tabView = mTabHost.getTabWidget().getChildAt(position);
        if (tabView != null)
        {
            final int width = horizontalsv.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            horizontalsv.scrollTo(scrollPos, 0);
        } else {
            horizontalsv.scrollBy(positionOffsetPixels, 0);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        obs_cabecera f1 = obs_cabecera.newInstance(CodObservacion);
        obs_detalle1 f2 = obs_detalle1.newInstance(CodObservacion,CodTipo);
        obs_archivos f4 = obs_archivos.newInstance(CodObservacion);
        obs_planaccion f5=obs_planaccion.newInstance(CodObservacion);


        fList.add(f1);
        fList.add(f2);
        fList.add(f4);
        fList.add(f5);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab1").setIndicator("Observación"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab2").setIndicator("Detalle"));
        observacion_edit.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator("Archivos"));
        observacion_edit.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Plan Accion"));
        mTabHost.setOnTabChangedListener(this);
    }

    public void outObservacion(){
        boolean Nochangues=true;
        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        String Observacion=  gson.toJson(GlobalVariables.Obserbacion);
        if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO02")){
            GlobalVariables.ObserbacionDetalle.CodError=null;
            GlobalVariables.ObserbacionDetalle.CodEstado=null;
        }
        String DetalleObs=  gson.toJson(GlobalVariables.ObserbacionDetalle);
        if(!GlobalVariables.StrObservacion.equals(Observacion))Nochangues=false;
        if(Nochangues&&!GlobalVariables.StrObsDetalle.equals(DetalleObs)) Nochangues=false;
        if(Nochangues&&GlobalVariables.StrPlanes.size()>0){
            String DeletePlanes="";
            for (PlanModel item:GlobalVariables.StrPlanes) {
                boolean pass=true;
                for (PlanModel item2:GlobalVariables.Planes) {
                    if(item.CodAccion.equals(item2.CodAccion)){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeletePlanes+=item.CodAccion+";";
                }
            }
            if(!DeletePlanes.equals(""))Nochangues=false;
        }
        if(Nochangues)
        {
            ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
            ArrayList<GaleriaModel> DataAll=new ArrayList<>();

            DataAll.addAll(GlobalVariables.listaGaleria);
            DataAll.addAll(GlobalVariables.listaArchivos);

            //delete files
            String DeleteFiles="";
            for (GaleriaModel item:GlobalVariables.StrFiles) {
                boolean pass=true;
                for (GaleriaModel item2:DataAll) {
                    if(item.Correlativo==item2.Correlativo){
                        pass=false;
                        continue;
                    }
                }
                if(pass){
                    DeleteFiles+=item.Correlativo+";";
                    item.Estado="E";
                }
            }
//Insert Files
            for (GaleriaModel item:DataAll) {
                boolean pass=false;
                for(GaleriaModel item2:GlobalVariables.StrFiles)
                    if(item.Descripcion.equals(item2.Descripcion))
                        pass=true;
                if(item.Correlativo==-1) {
                    DataInsert.add(item);
                    if(!pass)GlobalVariables.StrFiles.add(item);
                }
            }
            if(!DeleteFiles.equals("")||DataInsert.size()>0)Nochangues=false;
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
    public boolean ValifarFormulario(){
        String ErrorForm="Cabecera:\n";
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodObservadoPor)) ErrorForm+=" ->Observado Por\n";
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodAreaHSEC)) ErrorForm+=" ->Area HSEC\n";
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodNivelRiesgo)) ErrorForm+=" ->Nivel de riesgo\n";
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.Fecha)) ErrorForm+=" ->Fecha\n";
        if(StringUtils.isEmpty(GlobalVariables.Obserbacion.CodUbicacion)) ErrorForm+=" ->Ubicación\n";
        if(ErrorForm.equals("Cabecera:\n")) ErrorForm="";
        ErrorForm+="Detalle:\n";
        if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Observacion.trim())) ErrorForm+=" ->Observacion\n";
        if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.Accion.trim())) ErrorForm+=" ->Accion\n";
        if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodActiRel)) ErrorForm+=" ->Actividad Relacionada\n";
        if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodHHA)) ErrorForm+=" ->HHA Relacionada\n";
        if(GlobalVariables.Obserbacion.CodTipo.equals("TO01")){
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)) ErrorForm+=" ->Acto SubEstandar\n";
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodEstado)) ErrorForm+=" ->Estado\n";
            if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodError)) ErrorForm+=" ->Error\n";
        }else if(StringUtils.isEmpty(GlobalVariables.ObserbacionDetalle.CodSubEstandar)) ErrorForm+=" ->Condicion SubEstandar\n";
        if(ErrorForm.equals("Detalle:\n")) ErrorForm="";
        if(ErrorForm.isEmpty()) return true;
        else{
            String Mensaje="Complete los siguientes campos obligatorios:\n"+ErrorForm;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos incorrectos");
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

    public void SalvarObservacion(View view){

        Gson gson = new Gson();
        Utils.closeSoftKeyBoard(this);
        if(!ValifarFormulario()) return;
        btn_Salvar.setEnabled(false);
        String Observacion=  gson.toJson(GlobalVariables.Obserbacion);
        if(GlobalVariables.ObserbacionDetalle.CodTipo.equals("TO02")){
            GlobalVariables.ObserbacionDetalle.CodError=null;
            GlobalVariables.ObserbacionDetalle.CodEstado=null;
        }
        String DetalleObs=  gson.toJson(GlobalVariables.ObserbacionDetalle);
        Errores="";
        Actives.clear();
        if(GlobalVariables.ObjectEditable){
            if(!GlobalVariables.StrObservacion.equals(Observacion)){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"Observaciones/Post";
                final ActivityController obj = new ActivityController("post", url, observacion_edit.this,observacion_edit.this);
                obj.execute(Observacion,"1");
            }
            else Actives.add(1);
            if(!GlobalVariables.StrObsDetalle.equals(DetalleObs)){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"Observaciones/PostDetalle";
                final ActivityController obj = new ActivityController("post", url, observacion_edit.this,this);
                obj.execute(DetalleObs,"2");
            }
            else Actives.add(1);

            //delete planes
            if(GlobalVariables.StrPlanes.size()>0){
                String DeletePlanes="";
                for (PlanModel item:GlobalVariables.StrPlanes) {
                    boolean pass=true;
                    for (PlanModel item2:GlobalVariables.Planes) {
                        if(item.CodAccion.equals(item2.CodAccion)){
                            pass=false;
                            continue;
                        }
                    }
                    if(pass){
                        DeletePlanes+=item.CodAccion+";";
                    }
                }
                if(!DeletePlanes.equals("")){
                    Actives.add(0);
                    String url= GlobalVariables.Url_base+"PlanAccion/deleteAll/"+DeletePlanes.substring(0,DeletePlanes.length()-1);
                    ActivityController obj = new ActivityController("get", url, observacion_edit.this,this);
                    obj.execute("1");
                }
                else Actives.add(1);
            }else Actives.add(1);
            //edicion files
            UpdateFiles();
        }
        else{  //Insert new Observacion
            Actives.add(0);
           // GridViewAdapter gridViewAdapter = new GridViewAdapter(this,GlobalVariables.listaGaleria);
            obs_archivos archivos = (obs_archivos) pageAdapter.getItem(2);
            archivos.gridViewAdapter.ProcesarImagens();
            GlobalVariables.StrFiles= new ArrayList();

            GlobalVariables.StrFiles.addAll(GlobalVariables.listaGaleria);
            GlobalVariables.StrFiles.addAll(GlobalVariables.listaArchivos);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalVariables.Url_base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WebServiceAPI service = retrofit.create(WebServiceAPI.class);
            List<MultipartBody.Part> Files = new ArrayList<>();
            for (GaleriaModel item:GlobalVariables.StrFiles) {
                Files.add(createPartFromFile(item));
            }
            Toast.makeText(this, "Guardando Observacion, Espere..." , Toast.LENGTH_SHORT).show();

            Call<String> request = service.insertarObservacion("Bearer "+GlobalVariables.token_auth,createPartFromString(Observacion),createPartFromString(DetalleObs),createPartFromString(gson.toJson(GlobalVariables.Planes)),Files);
            progressBar.setVisibility(View.VISIBLE);
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful()){
                        String respt  = response.body();
                        if(respt.equals("-1")){
                            Actives.set(0,-1);
                            Errores+="\nOcurrio un error al guardar observacion";
                    }
                        else {

                            Actives.set(0,1);  // update value Cabecera
                            Utils.DeleteCache(new Compressor(observacion_edit.this).destinationDirectoryPath); //delete cache Files;
                            String [] respts= respt.split(";");
                            GlobalVariables.Obserbacion.CodObservacion=respts[0];
                            GlobalVariables.ObserbacionDetalle.CodObservacion= respts[0];
                            GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                            GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
                            GlobalVariables.ObserbacionFile= respts[0];
                            GlobalVariables.ObserbacionPlan= respts[0];

                            obs_cabecera cabecera = (obs_cabecera) pageAdapter.getItem(0);
                            cabecera.updateCodigo(respts[0]);

                            //update value Detalle
                            if(respts[1].equals("-1")){
                                Actives.add(-1);
                                Errores+="Ocurrio un error al gurdar detalle";
                            }
                            // update Planes de accion
                            boolean passPlan=false;
                            if(!respts[2].equals("0"))
                            {
                                for(PlanModel item:GlobalVariables.Planes){
                                    for (String planid:respts[2].split(",")) {
                                        String[] value= planid.split(":");
                                        if(item.CodAccion.equals(value[0])){
                                            if(value[1].equals("-1")){
                                                item.Estado="E";
                                                passPlan=true;
                                            }
                                            else {
                                                item.CodAccion=value[1];
                                                item.NroDocReferencia=respts[0];
                                            }
                                            continue;
                                        }
                                    }
                                }
                                obs_planaccion fragment = (obs_planaccion) pageAdapter.getItem(3);
                                fragment.setdata();
                                GlobalVariables.StrPlanes=GlobalVariables.Planes;

                                if(passPlan) {
                                    Actives.add(-1);
                                    Errores+="Error al guardar algunos planes de accion";
                                }
                            }

                            //update file
                            if(!respts[3].equals("0"))
                            {
                                for (String file:respts[3].split(",")) {
                                    String[] datosf= file.split(":");
                                    for (GaleriaModel item:GlobalVariables.StrFiles) {
                                        if(item.Descripcion.equals(datosf[0]))
                                        {
                                            item.Correlativo=Integer.parseInt(datosf[1]);
                                            if(item.Correlativo==-1) item.Estado="E";
                                            else {
                                                if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                                else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                                else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                            }
                                        }
                                    }
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
                    Errores+="\nFallo la subida de archivos";
                    if(!Actives.contains(0)) FinishSave();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void UpdateFiles (){
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this,GlobalVariables.listaGaleria);
        gridViewAdapter.ProcesarImagens();
        ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
        ArrayList<GaleriaModel> DataAll=new ArrayList<>();

        DataAll.addAll(GlobalVariables.listaGaleria);
        DataAll.addAll(GlobalVariables.listaArchivos);

        //delete files
        String DeleteFiles="";
        for (GaleriaModel item:GlobalVariables.StrFiles) {
            boolean pass=true;
            for (GaleriaModel item2:DataAll) {
                if(item.Correlativo==item2.Correlativo){
                    pass=false;
                    continue;
                }
            }
            if(pass){
                DeleteFiles+=item.Correlativo+";";
                item.Estado="E";
            }
        }
//Insert Files
        for (GaleriaModel item:DataAll) {
            boolean pass=false;
            for(GaleriaModel item2:GlobalVariables.StrFiles)
                if(item.Descripcion.equals(item2.Descripcion))
                    pass=true;
            if(item.Correlativo==-1) {
                DataInsert.add(item);
                if(!pass)GlobalVariables.StrFiles.add(item);
            }
        }

        if(DeleteFiles.equals("")&&DataInsert.size()==0){
            if(!Actives.contains(0)){ // no hubo ningun gambio
                    Actives.clear();
                    btn_Salvar.setEnabled(true);
                    Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
        }
        else{
//Delete Files
            if(!DeleteFiles.equals("")){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"media/deleteAll/"+DeleteFiles.substring(0,DeleteFiles.length()-1);
                ActivityController obj = new ActivityController("get", url, observacion_edit.this,this);
                obj.execute("2");
            }
            else Actives.add(1);
//Insert Files
            if(DataInsert.size()>0){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                Actives.add(0);
                List<MultipartBody.Part> Files = new ArrayList<>();
                for (GaleriaModel item:DataInsert) {
                    Files.add(createPartFromFile(item));
                }
                Toast.makeText(this, "Subiendo Archivos, Espere..." , Toast.LENGTH_SHORT).show();

                Call<String> request = service.uploadAllFile("Bearer "+GlobalVariables.token_auth,createPartFromString(CodObservacion),createPartFromString("TOBS"),createPartFromString("1"),Files);
                progressBar.setVisibility(View.VISIBLE);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.isSuccessful()){
                            String respt  = response.body();
                            if(respt.contains("-1")){
                                Actives.set(4,-1);
                                Errores+="\nOcurrio un error al subir algunos archivos";
                            }
                            else  Actives.set(4,1);
                            Utils.DeleteCache(new Compressor(observacion_edit.this).destinationDirectoryPath); //delete cache Files;
                            for (String file:respt.split(";")) {
                                String[] datosf= file.split(":");
                                for (GaleriaModel item:GlobalVariables.StrFiles) {
                                    if(item.Descripcion.equals(datosf[0]))
                                    {
                                        item.Correlativo=Integer.parseInt(datosf[1]);
                                        if(item.Correlativo==-1) item.Estado="E";
                                        else {
                                            if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                            else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                            else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                        }
                                    }
                                }
                            }

                        }else{
                            Actives.set(4,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Actives.set(4,-1);
                        Errores+="\nFallo la subida de archivos";
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else  Actives.add(1);
        }
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
        if(GlobalVariables.StrFiles.size()>0)
        {
            GlobalVariables.listaGaleria.clear();
            GlobalVariables.listaArchivos.clear();
            for (int i = 0; i < GlobalVariables.StrFiles.size(); i++) {
                if (GlobalVariables.StrFiles.get(i).TipoArchivo.equals("TP03")) {
                    GlobalVariables.listaArchivos.add(GlobalVariables.StrFiles.get(i));
                } else {
                    GlobalVariables.listaGaleria.add(GlobalVariables.StrFiles.get(i));
                }
            }
            obs_archivos fragment = (obs_archivos) pageAdapter.getItem(2);
            fragment.setdata();
        }

            alertDialog.show();
    }

    @Override
    public void success(String data, String Tipo) {
        if(Tipo.equals("1")){ //delete Planes
            if(data.contains("-1")){
                Actives.set(2,-1);
                Errores+="\nNo se pudo eliminar algunos planes de accion";
            }
            else {
                GlobalVariables.StrPlanes=GlobalVariables.Planes;
                Actives.set(2,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){ //delete Files
            if(data.contains("false")){
                Actives.set(3,-1);
                Errores+="\nNo se pudo eliminar algunas imagenes/archivos";
            }
            else {
                Actives.set(3,1);
                ArrayList<GaleriaModel> temp= new ArrayList<>(GlobalVariables.StrFiles);

                for (GaleriaModel item : GlobalVariables.StrFiles) {
                    if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                        temp.remove(item);
                }
                GlobalVariables.StrFiles=temp;
            }
            if(!Actives.contains(0)) FinishSave();
        }
    }

    @Override
    public void successpost(String data, String Tipo) {

        Gson gson = new Gson();
        if(Tipo.equals("1")){
            if(data.contains("-1"))  {
                Actives.set(0,-1);
                Errores+="Error al guardar cabecera\n";
            }
            else{
                GlobalVariables.StrObservacion=gson.toJson(GlobalVariables.Obserbacion);
                Actives.set(0,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){
            if(data.contains("-1")) {
                Actives.set(1,-1);
                Errores+="Error al guardar detalle\n";
            }
            else{
                GlobalVariables.StrObsDetalle=gson.toJson(GlobalVariables.ObserbacionDetalle);
                Actives.set(1,1);
            }
            if(!Actives.contains(0)) FinishSave();
        }
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

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,"Su data fue actualizada "+ requestCode ,Toast.LENGTH_SHORT).show();
    }*/
}
