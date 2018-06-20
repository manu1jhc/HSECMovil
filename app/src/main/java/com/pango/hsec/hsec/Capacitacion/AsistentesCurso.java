package com.pango.hsec.hsec.Capacitacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.pango.hsec.hsec.Busquedas.B_personas;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.AsistenteAdapter;
import com.pango.hsec.hsec.adapter.OnLoadMoreListener;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPersonaModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class AsistentesCurso extends AppCompatActivity implements ZXingScannerView.ResultHandler,IActivity {

    int contPublicacion;
    int paginacion=1,paginacion2=1;
    Spinner spinnerDias;
    SwipeRefreshLayout swipeRefreshLayout;
    PersonaModel AsistenteAdd;
    RecyclerView list_Personas;
    //ConstraintLayout constraintLayout;
    //TextView tx_texto;
    String CodCurso, Indice;
    ArrayList<PersonaModel>personasList;
    int itemSel=-1;
    boolean loadingTop=false;
    AsistenteAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    CardView carAction,carScan;
    //popup
    LayoutInflater layoutInflater,layoutInflater2,layoutInflater3;
    View popupView,popupView2,popupView3;
    PopupWindow popupWindow,popupWindow2,popupWindow3;

    protected Handler handler;

//scar DNI
    LinearLayout ly;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    boolean activeSound,activeflash,activeScan;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistentes_curso);
        list_Personas = (RecyclerView) findViewById(R.id.AsistentesRv);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        //constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);
       // tx_texto =(TextView) findViewById(R.id.tx_texto); carViewTitle
        //scan DNI
        carAction=(CardView) findViewById(R.id.carViewTitle);
        carScan=(CardView) findViewById(R.id.card_title);
        ImageView bntLinterna=(ImageView) findViewById(R.id.btn_linterna);
        ImageView btnSound=(ImageView) findViewById(R.id.btn_sound);
        ImageView btnClose=(ImageView) findViewById(R.id.btn_close);
        ly=(LinearLayout)   findViewById(R.id.idscanner);
        activeSound=true;

        spinnerDias=findViewById(R.id.sp_dia);

        handler = new Handler();
        Bundle datos = this.getIntent().getExtras();
        CodCurso=datos.getString("CodCurso");
        Indice=datos.getString("Indice");
        personasList= new ArrayList<>();
        ArrayAdapter adapterDias = new ArrayAdapter(this,R.layout.custom_spinner_item, GlobalVariables.CapaCursoDias);
        adapterDias.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerDias.setAdapter(adapterDias);

        spinnerDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(itemSel!=position) {
                    itemSel=position;
                    Maestro Dia = (Maestro) ((Spinner) findViewById(R.id.sp_dia)).getSelectedItem();
                    paginacion=1;
                    Indice=Dia.CodTipo;
                    getdata();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerDias.setSelection(GlobalVariables.indexOf(GlobalVariables.CapaCursoDias,Indice));
        if(GlobalVariables.CapaCursoDias.size()==1)
            spinnerDias.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
               // tx_texto.setVisibility(View.VISIBLE);
                loadingTop=true;
                paginacion=2;
                getdata();
            } });

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) bntLinterna.setVisibility(View.GONE);
        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                scannerView.stopCamera();
                carAction.setVisibility(View.VISIBLE);
                carScan.setVisibility(View.GONE);
                ly.setVisibility(View.GONE);
            }
        });

        btnSound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(activeSound)
                    btnSound.setImageResource(R.drawable.ic_volume_off);
                else
                    btnSound.setImageResource(R.drawable.ic_volume_up);
                activeSound=!activeSound;
            }
        });

        bntLinterna.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                activeflash=!activeflash;
                if(activeflash) bntLinterna.setImageResource(R.drawable.ic_flash_off);
                else bntLinterna.setImageResource(R.drawable.ic_flash_on);
                scannerView.setFlash(activeflash);
            }
        });
    }
    public void DeleteObject(String Url, int index){
        Url=GlobalVariables.Url_base+Url+"&CodCurso="+CodCurso+"&Fecha="+Indice;
        ActivityController obj = new ActivityController("get-2", Url, AsistentesCurso.this,this);
        obj.execute(index+"");
    }

    public void getdata(){
        paginacion2=1;
        String url=GlobalVariables.Url_base+"Capacitacion/GetAsistentesCurso?CodCurso="+CodCurso+"&Fecha="+Indice+"&Pagenumber=1&Elemperpage=14";
        ActivityController obj = new ActivityController("get-"+paginacion, url, AsistentesCurso.this,this);
        obj.execute("1");
    }

    public void close(View view){
        finish();
    }

    public void scanbarcode(View view){

        //inicialize variables
        activeflash=false;
        activeScan=true;
        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(!checkPermission())
                requestPermission();
        }
        carAction.setVisibility(View.GONE);
        carScan.setVisibility(View.VISIBLE);
        scannerView = new ZXingScannerView(this);
        ly.setVisibility(View.VISIBLE);
        ly.addView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    private void playSound() {
        if(activeSound)
        {
            mp = MediaPlayer.create(AsistentesCurso.this, R.raw.scan_dni);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }
            });
            mp.start();
        }
    }
    public void addAsistente(View view)
    {
        //Intent intent = new Intent(AsistentesCurso.this, B_personasM.class);
        Intent intent = new Intent(AsistentesCurso.this, B_personas.class);
        intent.putExtra("title","Capacitacion/Curso/Asistencia");
        startActivityForResult(intent , 1);
    }

    @Override
    public void success(String data, String Tipo) throws CloneNotSupportedException {
        Gson gson = new Gson();
        if(Tipo.equals("1")){  // new adapter
            GetPersonaModel getpersonaModel = gson.fromJson(data, GetPersonaModel.class);
            contPublicacion=getpersonaModel.Count;

            list_Personas.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            // use a linear layout manager
            list_Personas.setLayoutManager(mLayoutManager);
            personasList= new ArrayList<>();
            personasList=getpersonaModel.Data;
            mAdapter = new AsistenteAdapter(this,personasList,list_Personas,swipeRefreshLayout);
            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    if(personasList.size()<contPublicacion)
                    {
                        personasList.add(null);
                        mAdapter.notifyItemInserted(personasList.size() - 1);
                        paginacion2++;
                        //paginacion2+=1;
                        String url=GlobalVariables.Url_base+"Capacitacion/GetAsistentesCurso?CodCurso="+CodCurso+"&Fecha="+Indice+"&Pagenumber="+paginacion2+"&Elemperpage=14";
                        ActivityController obj = new ActivityController("get-2", url, AsistentesCurso.this,AsistentesCurso.this);
                        obj.execute("2");
                    }
                }
            });
            list_Personas.setAdapter(mAdapter);
            if(loadingTop)
            {
                loadingTop=false;
                swipeRefreshLayout.setRefreshing(false);
                //tx_texto.setVisibility(View.GONE);
                swipeRefreshLayout.setEnabled( false );
            }
        }
        else if(Tipo.equals("2")){  // add adapter
            //constraintLayout.setVisibility(View.GONE);
            GetPersonaModel getpersonaModel = gson.fromJson(data, GetPersonaModel.class);
            //   remove progress item
            personasList.remove(personasList.size() - 1);
            mAdapter.notifyItemRemoved(personasList.size());
            for(PersonaModel item:getpersonaModel.Data)
            {
                personasList.add(item);
                mAdapter.notifyItemInserted(personasList.size());
            }
            mAdapter.setLoaded();
        }
        else
        {
            if(data.contains("-1")) confirmUpdate("E","Ocurrio un error al eliminar registro.");
            else {
                mAdapter.remove(Integer.parseInt(Tipo)-3);
                confirmUpdate("A","");
            }
        }
    }

    @Override
    public void successpost(String data, String Tipo) throws CloneNotSupportedException {
        Gson gson = new Gson();
        if(Tipo.equals("1")){
            PersonaModel personaModel = gson.fromJson(data, PersonaModel.class);
            scannerView.resumeCameraPreview(AsistentesCurso.this);
          if(personaModel==null || personaModel.CodPersona==null) {
              activeScan=true;
              confirmUpdate("E","Ocurrio un error al registrar asistente.");
          }
          else if(personaModel.Estado.equals("N")){
              activeScan=true;
              confirmUpdate("W",personaModel.CodPersona +": "+ personaModel.Nombres);
          }
          else if(personaModel.Estado.equals("P")){
              AlertDialog alertDialog = new AlertDialog.Builder(this).create();
              alertDialog.setCancelable(false);
              alertDialog.setTitle("Asistente no registrado");
              alertDialog.setIcon(R.drawable.warninicon);
              alertDialog.setMessage("Desea agregarlo a la lista de participantes?");
              alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      activeScan=true;
                  }
              });
              alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      Gson gson = new Gson();
                      PersonaModel temp= new PersonaModel();
                      temp.CodPersona=AsistenteAdd.CodPersona;
                      temp.NroDocumento= CodCurso;
                      temp.Estado= Indice;
                      String url= GlobalVariables.Url_base+"Capacitacion/InsertParticipante";
                      final ActivityController obj = new ActivityController("post", url, AsistentesCurso.this,AsistentesCurso.this);
                      obj.execute(gson.toJson(temp),"2");
                      activeScan=true;
                  }
              });
              alertDialog.show();
              AsistenteAdd=personaModel;
          }
          else if(personaModel.Estado.equals("R") || personaModel.Estado.equals("A")){
              activeScan=true;
              if(personaModel.Estado.equals("A")){
                  mAdapter.add(personaModel);
              }
              AsistenteAdd=personaModel;
              confirmAsistencia();
          }
        }
        else if(Tipo.equals("2")){
            if (data.contains("-1")) confirmUpdate("E","Ocurrio un error al registrar participante.");

            else {
                mAdapter.add(AsistenteAdd);
                confirmAsistencia();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) { // add persona

                PersonaModel temp= new PersonaModel();
                temp.CodPersona=data.getStringExtra("codpersona");
                temp.NroDocumento=CodCurso;
                temp.Estado=Indice;
                Gson gson = new Gson();
                String url= GlobalVariables.Url_base+"Capacitacion/InsertAsistentente";
                final ActivityController obj = new ActivityController("post", url, AsistentesCurso.this,this);
                obj.execute(gson.toJson(temp),"1");
            }
            /*if (requestCode == 2  && resultCode  == RESULT_OK) { //agregar Lista
                for(PersonaModel item: GlobalVariables.lista_Personas)
                    if(item.Check) listPersonAdapter.add(item);
                listPersonAdapter.notifyDataSetChanged();
            }*/
        } catch (Exception ex) {
            Toast.makeText(AsistentesCurso.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmAsistencia(){

        if(popupWindow!=null && popupWindow.isShowing())popupWindow.dismiss();
        layoutInflater =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_asistencia, null);

        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(list_Personas, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
        popupWindow.setOutsideTouchable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        ConstraintLayout constDissmiss=(ConstraintLayout) popupView.findViewById(R.id.constdismiss);
        ImageView avatar=(ImageView) popupView.findViewById(R.id.img_avatar);
        ImageView imgTitle=(ImageView) popupView.findViewById(R.id.btn_sound);
        CardView cardResult=(CardView) popupView.findViewById(R.id.card_title);
        TextView txtNombres=(TextView) popupView.findViewById(R.id.txt_nombres);
        TextView txtTitle=(TextView) popupView.findViewById(R.id.txt_title);
        TextView txtCargo=(TextView) popupView.findViewById(R.id.txt_cargo);
        TextView txtDNI=(TextView) popupView.findViewById(R.id.txt_dni);

        constDissmiss.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });

        if(AsistenteAdd.Estado.equals("R")){
            imgTitle.setImageResource(R.drawable.warninicon);
            //imgTitle.setimageT
            txtTitle.setText("ASISTENTE YA REGISTRADO");
            cardResult.setCardBackgroundColor(Color.parseColor("#FFAB00"));
        }
        if (AsistenteAdd.NroDocumento == null) {
            avatar.setImageResource(R.drawable.ic_loginusuario);
        }else {
            txtDNI.setText(AsistenteAdd.NroDocumento);
            String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + AsistenteAdd.NroDocumento + "/Carnet.jpg";
            Glide.with(this)
                    .load(Url_img)
                    //.override(50, 50)
                    //.transform(new CircleTransform(AsistentesCurso.this)) // applying the image transformer
                    .into(avatar);
        }
        txtNombres.setText(AsistenteAdd.Nombres);
        if(StringUtils.isEmpty(AsistenteAdd.Cargo)) txtCargo.setVisibility(View.GONE);
        else txtCargo.setText(AsistenteAdd.Cargo);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 5000);
    }

    public void confirmUpdate(String Tipo,String sms){

        if(popupWindow2!=null && popupWindow2.isShowing())popupWindow2.dismiss();
        layoutInflater2 =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView2 = layoutInflater2.inflate(R.layout.popup_snackbar, null);

        popupWindow2 = new PopupWindow(popupView2, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow2.showAtLocation(list_Personas, Gravity.BOTTOM, 0, 0);
        popupWindow2.setFocusable(true);
        popupWindow2.update();
        popupWindow2.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow2.dismiss();
            }
        });
        ImageView imgTitle=(ImageView) popupView2.findViewById(R.id.img_content);
        CardView cardResult=(CardView) popupView2.findViewById(R.id.card_content);
        TextView txtContenet=(TextView) popupView2.findViewById(R.id.txt_content);

        if(Tipo.equals("E")){
            imgTitle.setImageResource(R.drawable.erroricon);
            txtContenet.setText(sms);
            cardResult.setCardBackgroundColor(Color.parseColor("#C23829"));
        }
        if(Tipo.equals("W")){
            imgTitle.setImageResource(R.drawable.warninicon);
            txtContenet.setText(sms);
            cardResult.setCardBackgroundColor(Color.parseColor("#F79306"));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow2.dismiss();
            }
        }, 5000);
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }


    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(popupWindow3!=null && popupWindow3.isShowing())
                {
                    if(scannerView == null) {
                        scannerView = new ZXingScannerView(this);
                        //setContentView(scannerView);
                        ly.addView(scannerView);
                    }
                    scannerView.setResultHandler(this);
                    scannerView.startCamera();
                }
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(AsistentesCurso.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        if(activeScan)
        {
            activeScan=false;
            playSound();
            final String myResult = result.getText();
            PersonaModel temp= new PersonaModel();
            temp.CodPersona="D"+myResult;
            temp.NroDocumento=CodCurso;
            temp.Estado=Indice;
            Gson gson = new Gson();
            String url= GlobalVariables.Url_base+"Capacitacion/InsertAsistentente";
            final ActivityController obj = new ActivityController("post", url, AsistentesCurso.this,this);
            obj.execute(gson.toJson(temp),"1");
        }
    }
}
