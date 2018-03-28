package com.pango.hsec.hsec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.R;

public class obs_planaccion extends Fragment {

    private static View mView;
    private ListView listPlan;
    public static final com.pango.hsec.hsec.obs_planaccion newInstance(String sampleText) {
        obs_planaccion f = new obs_planaccion();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_planaccion,
                container, false);

        ImageButton btnAddPlan=(ImageButton) mView.findViewById(R.id.btn_addplan);
       /* mImageSampleRecycler = (RecyclerView) mView.findViewById(R.id.images_sample);
        setupRecycler();*/
        listPlan = (ListView)  mView.findViewById(R.id.list_plan);

        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlanAccionEdit.class);
                i.putExtra("CodAccion", "-1");
                getActivity().startActivityForResult(i, 1);
            }
        });
        return mView;
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {  // activity Edit
        super.onActivityCreated(savedInstanceState);
        // some code
        Intent i = new Intent(getActivity(), PlanAccionEdit.class);
        i.putExtra("CodAccion", CodAccion);
        getActivity().startActivityForResult(i, 1);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //some code
        }
    }
}