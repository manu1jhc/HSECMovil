package com.pango.hsec.hsec.Inspecciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.ObsInspAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetObsInspModel;

public class FragmentObsInsp extends Fragment implements IActivity {
    private View mView;
    String codInsp;
    String url;
    ObsInspAdapter obsInspAdapter;
    ListView list_obsinsp;
    GetObsInspModel getObsInspModel;
    String jsonObsIns="";
    public FragmentObsInsp() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentObsInsp newInstance(String sampleText) {
        FragmentObsInsp fragment = new FragmentObsInsp();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_obs_insp, container, false);
        list_obsinsp= (ListView) mView.findViewById(R.id.list_obsinsp);
        codInsp=getArguments().getString("bString");
        //codInsp="INSP0000008356";
        GlobalVariables.view_fragment=mView;

        //GlobalVariables.istabs=true;


        url= GlobalVariables.Url_base+"Inspecciones/GetDetalleInspeccion/"+codInsp;
        if(jsonObsIns.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentObsInsp.this,getActivity());
            obj.execute("");
        }else {
            success(jsonObsIns,"");
        }



                list_obsinsp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Click en "+position,Toast.LENGTH_SHORT).show();
                String CodInspeccion=getObsInspModel.Data.get(position).CodInspeccion;

                //String CodInspeccion="INSP0000008349-"+position+1;
                String correlativo=getObsInspModel.Data.get(position).Correlativo;
                Intent intent = new Intent(getActivity(), ActObsInspDet.class);
                intent.putExtra("codObs",CodInspeccion);
                intent.putExtra("correlativo",correlativo);
                //intent.putExtra("UrlObs",GlobalVariables.listaGlobal.get(position).UrlObs);

                startActivity(intent);
            }
        });




        return mView;

    }


    @Override
    public void success(String data, String Tipo) {
        jsonObsIns=data;
        Gson gson = new Gson();
        getObsInspModel= gson.fromJson(data, GetObsInspModel.class);


        obsInspAdapter = new ObsInspAdapter(getContext(),getObsInspModel.Data);
        list_obsinsp.setAdapter(obsInspAdapter);

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
