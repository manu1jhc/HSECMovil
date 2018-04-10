package com.pango.hsec.hsec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.adapter.PlanEditAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GetPlanModel;
import com.pango.hsec.hsec.model.PlanModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class obs_planaccion extends Fragment implements IActivity{

    private static View mView;
    private RecyclerView listPlan;
    private PlanEditAdapter listViewAdapter;
    private  Gson gson;
    public static final com.pango.hsec.hsec.obs_planaccion newInstance(String sampleText) {
        obs_planaccion f = new obs_planaccion();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_planaccion, container, false);
        gson = new Gson();
        ImageButton btnAddPlan=(ImageButton) mView.findViewById(R.id.btn_addplan);
       /* mImageSampleRecycler = (RecyclerView) mView.findViewById(R.id.images_sample);
        setupRecycler();*/
        listPlan = (RecyclerView) mView.findViewById(R.id.list_plan);

        String codigo_obs = getArguments().getString("bString");
        if(GlobalVariables.ObjectEditable){ // load data of server
            String url=GlobalVariables.Url_base+"PlanAccion/GetPlanes/"+codigo_obs;
            final ActivityController obj = new ActivityController("get", url, obs_planaccion.this);
            obj.execute("");
        }
        else // new Obserbacion
        {
            if(GlobalVariables.ObserbacionFile==null || !GlobalVariables.ObserbacionFile.contains("XYZ")){

                GlobalVariables.Planes= new ArrayList<>();
            }
            GlobalVariables.ObserbacionFile=codigo_obs;
            setdata();
        }


        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlanAccionEdit.class);
               PlanModel Plan= new PlanModel();
                Plan.CodTabla="TOBS";
                Plan.CodReferencia="01";
                Plan.CodAccion="0";
                i.putExtra("Plan", gson.toJson(Plan));
                startActivityForResult(i, 1);
            }
        });

        return mView;
    }
    public void setdata(){
        LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listPlan.setLayoutManager(horizontalManager);
        listViewAdapter = new PlanEditAdapter(getActivity(),this, GlobalVariables.Planes);
        listPlan.setAdapter(listViewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //listViewAdapter.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == 1 && resultCode == Activity.RESULT_OK) { // new plan
                PlanModel plan= gson.fromJson(data.getStringExtra("planaccion"),PlanModel.class);
                listViewAdapter.add(plan);
            }
            if(requestCode == 2 && resultCode == Activity.RESULT_OK) { // edit plan
                Gson gson = new Gson();
                PlanModel plan= gson.fromJson(data.getStringExtra("planaccion"),PlanModel.class);
                listViewAdapter.replace(plan);
            }
        }
        catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void success(String data, String Tipo) {
        GlobalVariables.Planes = gson.fromJson(data, GetPlanModel.class).Data;
        setdata();
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}