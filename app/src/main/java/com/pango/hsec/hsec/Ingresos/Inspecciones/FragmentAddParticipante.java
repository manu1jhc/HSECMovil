package com.pango.hsec.hsec.Ingresos.Inspecciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;

public class FragmentAddParticipante extends Fragment {


    public FragmentAddParticipante() {
        // Required empty public constructor
    }

    private View mView;
    String codObs;
    // TODO: Rename and change types and number of parameters
    public static FragmentAddParticipante newInstance(String sampleText) {
        FragmentAddParticipante fragment = new FragmentAddParticipante();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_participante, container, false);
        codObs=getArguments().getString("bString");
        return mView;
    }


}
