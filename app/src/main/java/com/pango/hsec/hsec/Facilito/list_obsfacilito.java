package com.pango.hsec.hsec.Facilito;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.Inspecciones.ActInspeccionDet;
import com.pango.hsec.hsec.Noticias.ActNoticiaDet;
import com.pango.hsec.hsec.Observaciones.ActMuroDet;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.MuroAdapter;
import com.pango.hsec.hsec.adapter.ObsFacilitoAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObsFacilitoModel;
import com.pango.hsec.hsec.model.GetPublicacionModel;
import com.pango.hsec.hsec.model.ObsFacilitoModel;

import java.util.Arrays;
import java.util.List;

import layout.FragmentMuro;
import layout.FragmentObsFacilito;

//import static com.pango.hsec.hsec.MainActivity.jsonMuro;

public class list_obsfacilito extends AppCompatActivity implements IActivity {
    ImageButton close;
    ListView list_Obs;
    int paginacion2=0;
    boolean flagObsFiltro=true;
    private FragmentMuro.OnFragmentInteractionListener mListener;
    boolean upFlag;
    boolean downFlag;
    boolean listenerFlag;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag_enter=true;
    ConstraintLayout constraintLayout;
    String url="";
    TextView tx_texto, tx_mensajeb;
    LayoutInflater layoutInflater;
    int contPublicacion;
    View popupView;
    PopupWindow popupWindow;
    boolean flagpopup=false;
    boolean loadingTop=true;
    private ArrayAdapter arrayAdapter;
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_obsfacilito);
        close = findViewById(R.id.imageButton);

        list_Obs = (ListView) findViewById(R.id.list_Obs);
        tx_texto = (TextView) findViewById(R.id.tx_texto);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        tx_mensajeb=(TextView) findViewById(R.id.tx_mensajeb);
        constraintLayout=(ConstraintLayout) findViewById(R.id.const_main);
        paginacion2+=1;
        url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacilito/" + paginacion2+ "/" + "7";
        final ActivityController obj = new ActivityController("get-"+paginacion2, url, list_obsfacilito.this,this);
        obj.execute("");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GlobalVariables.istabs=false;// para que no entre al flag de tabs
                swipeRefreshLayout.setRefreshing(true);
                tx_texto.setVisibility(View.VISIBLE);
                upFlag=false;
                downFlag=false;
                swipeRefreshLayout.setRefreshing(true);
                loadingTop=true;
                tx_texto.setVisibility(View.VISIBLE);

                GlobalVariables.listaGlobalObsFacilito.clear();
                GlobalVariables.flagUpSc=true;
                GlobalVariables.flag_up_toast=true;
                GlobalVariables.isFragment=false;
                paginacion2=1;
                url=GlobalVariables.Url_base+"ObsFacilito/GetObservacionFacilito/"+paginacion2+"/"+"7";
                Utils.isActivity=true;
                GlobalVariables.istabs=false;
                //GlobalVariables.count=5;//para que no entre al flag
                final ActivityController obj = new ActivityController("get-0", url, list_obsfacilito.this,list_obsfacilito.this);
                obj.execute("");

            } }
        );
        list_Obs.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //     if(is_swipe) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    listenerFlag = false;
                    Log.d("--:", "---------------------------");
                }
                if (upFlag && scrollState == SCROLL_STATE_IDLE) {
                    upFlag = false;
                    swipeRefreshLayout.setEnabled(true);

                }
                if (downFlag && scrollState == SCROLL_STATE_IDLE) {
                    downFlag = false;
                    if (GlobalVariables.listaGlobalObsFacilito.size() != contPublicacion && flag_enter) {
                        GlobalVariables.istabs=false;// para que no entre al flag de tabs
                        flag_enter = false;
                        constraintLayout.setVisibility(View.VISIBLE);
                        GlobalVariables.isFragment=false;
                        Utils.isActivity=true;
                        paginacion2+=1;
                        url = GlobalVariables.Url_base + "ObsFacilito/GetObservacionFacilito/" + paginacion2+ "/" + "7";
                        GlobalVariables.count=5;
                        final ActivityController obj = new ActivityController("get-"+paginacion2, url, list_obsfacilito.this,list_obsfacilito.this);
                        obj.execute("");

                        layoutInflater =(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        popupView = layoutInflater.inflate(R.layout.popup_opcionfacilito, null);

                        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT ,580,false);
                        popupWindow.showAtLocation(list_Obs, Gravity.CENTER, 0, 0);
                        flagpopup=true;
                    }
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    listenerFlag = true;
                    swipeRefreshLayout.setEnabled(false);
                    Log.d("started", "comenzo");
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.d("+1:", "" + view.canScrollVertically(1));
                Log.d("-1:", "" + view.canScrollVertically(-1));
                // Log.d("x:",""+view.getScrollX());
                if (listenerFlag && !view.canScrollVertically(1)) {
                    downFlag = true;
                    upFlag = false;
                }
                if (listenerFlag && !view.canScrollVertically(-1)) {
                    upFlag = true;
                    downFlag = false;
                }  }
        });

        listenerFlag = false;
        list_Obs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String Codigo=GlobalVariables.listaGlobalObsFacilito.get(position).CodObsFacilito;
                Intent intent = new Intent(list_obsfacilito.this, obsFacilitoDet.class);
                intent.putExtra("codObs",Codigo);
                startActivity(intent);
            }
        });
    }
//    public void DeleteObject(String Url, int index){
//        String url= GlobalVariables.Url_base+Url;
//        ActivityController obj = new ActivityController("get", url, list_obsfacilito.this,this);
//        obj.execute(""+index);
//    }
    public void close(View view){
        finish();
    }

    @Override
    public void success(String data, String Tipo) {

        //jsonMuro=data;
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
        Gson gson = new Gson();
        GetObsFacilitoModel getObsFacilitoModel = gson.fromJson(data, GetObsFacilitoModel.class);
        contPublicacion=getObsFacilitoModel.Count;
        if(GlobalVariables.listaGlobalObsFacilito.size()==0) {
            GlobalVariables.listaGlobalObsFacilito = getObsFacilitoModel.Data;
        }else  //{

            if(!(GlobalVariables.listaGlobalObsFacilito.get(GlobalVariables.listaGlobalObsFacilito.size()-1).CodObsFacilito.equals(getObsFacilitoModel.Data.get(getObsFacilitoModel.Data.size()-1).CodObsFacilito))){
                GlobalVariables.listaGlobalObsFacilito.addAll(getObsFacilitoModel.Data);
            }
        ObsFacilitoAdapter ca = new ObsFacilitoAdapter(this,getObsFacilitoModel.Data,new FragmentObsFacilito());
        list_Obs.setAdapter(ca);
        ca.notifyDataSetChanged();

        flag_enter=true;
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {
        if(flagpopup){
            popupWindow.dismiss();
            flagpopup=false;
        }
        constraintLayout.setVisibility(View.GONE);
        paginacion2-=1;
        flag_enter=true;

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}
