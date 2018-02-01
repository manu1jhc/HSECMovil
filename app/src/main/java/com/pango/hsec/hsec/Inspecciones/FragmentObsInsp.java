package com.pango.hsec.hsec.Inspecciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;

import static com.pango.hsec.hsec.Inspecciones.ActInspeccionDet.jsonObsIns;


public class FragmentObsInsp extends Fragment implements IActivity {
    private View mView;
    String codObs;

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
        codObs=getArguments().getString("bString");

        return mView;

    }


    @Override
    public void success(String data, String Tipo) {
        jsonObsIns=data;
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
