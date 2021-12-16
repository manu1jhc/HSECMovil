package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ObsInspAddAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObsInspModel;
import com.pango.hsec.hsec.model.ObsInspAddModel;
import com.pango.hsec.hsec.model.ObsInspDetModel;
import com.pango.hsec.hsec.model.ObsInspModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.pango.hsec.hsec.GlobalVariables.obsInspDetModel;

public class FragmentAddObservacion extends Fragment implements IActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ObsInspAddAdapter listObsInspAdapter;
    //public ArrayList<ObsInspDetModel> ListLocalObs=new ArrayList<>();

    RecyclerView rec_listObservacion;
    ImageButton add_Observacion;
    public FragmentAddObservacion() {
        // Required empty public constructor
    }

    private View mView;
    // TODO: Rename and change types and number of parameters
    public static FragmentAddObservacion newInstance(String sampleText) {
        FragmentAddObservacion fragment = new FragmentAddObservacion();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_observacion, container, false);
        String codObs=getArguments().getString("bString");
        rec_listObservacion=mView.findViewById(R.id.rec_listObservacion);
        add_Observacion=mView.findViewById(R.id.add_Observacion);

        if(GlobalVariables.ObjectEditable){ // load data of server

            if(GlobalVariables.InspeccionObserbacion==null) {
                String url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccion/"+codObs;
                GlobalVariables.InspeccionObserbacion=codObs;
                final ActivityController obj = new ActivityController("get", url, FragmentAddObservacion.this,getActivity());
                obj.execute("");
            }
            else{
                setdata();
            }
        }
        else // new inspeccion
        {
            if (GlobalVariables.InspeccionObserbacion == null) {//||!GlobalVariables.AddInspeccion.CodInspeccion.contains("XYZ")
                GlobalVariables.InspeccionObserbacion=codObs;
                GlobalVariables.ListobsInspAddModel= new ArrayList<>();
            }
            setdata();
        }

        add_Observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tam=GlobalVariables.ListobsInspAddModel.size();
                int Grupo=1;
                if(tam>0){
                    Grupo = Integer.parseInt(GlobalVariables.ListobsInspAddModel.get(tam-1).obsInspDetModel.NroDetInspeccion) + 1;
                }
                obsInspDetModel=new ObsInspDetModel();
                Intent intent=new Intent(getContext(),ActObsInspEdit.class);
                intent.putExtra("correlativo","0");
                intent.putExtra("Grupo",Grupo+"");
                intent.putExtra("Editar",false);
                intent.putExtra("index","-1");
                startActivityForResult(intent , 1);

            }
        });


        return mView;
    }

    public void setdata(){
        LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_listObservacion.setLayoutManager(horizontalManager);
        listObsInspAdapter = new ObsInspAddAdapter(getActivity(),this, GlobalVariables.ListobsInspAddModel);
        rec_listObservacion.setAdapter(listObsInspAdapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                String json_obs=data.getStringExtra("JsonObsInsp");
                Gson gson = new Gson();
                ObsInspAddModel obsInspAddModel = gson.fromJson(json_obs, ObsInspAddModel.class);
                listObsInspAdapter.add(obsInspAddModel);

            }
            else if (requestCode == 2  && resultCode  == RESULT_OK) {
                String json_obs=data.getStringExtra("JsonObsInsp");
                int index=data.getIntExtra ("index",-1);
                Gson gson = new Gson();
                ObsInspAddModel obsInspAddModel = gson.fromJson(json_obs, ObsInspAddModel.class);
                listObsInspAdapter.replace(obsInspAddModel,index);
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void success(String data, String Tipo) {

            Gson gson = new Gson();
            GetObsInspModel getObsInspModel = gson.fromJson(data, GetObsInspModel.class);
            for (ObsInspModel item:getObsInspModel.Data) {
                GlobalVariables.ListobsInspAddModel.add(new ObsInspAddModel(item));
            }
            GlobalVariables.StrtobsInspAddModel= (ArrayList<ObsInspAddModel>)GlobalVariables.ListobsInspAddModel.clone();
            setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
