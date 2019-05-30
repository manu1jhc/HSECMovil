package com.pango.hsec.hsec.Verificaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;


public class FragmentVerificacion extends Fragment {


    public FragmentVerificacion() {
        // Required empty public constructor
    }
    private View mView;
    String codVer;
    // TODO: Rename and change types and number of parameters
    public static FragmentVerificacion newInstance(String sampleText) {
        FragmentVerificacion fragment = new FragmentVerificacion();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_verificacion, container, false);
        codVer=getArguments().getString("bString");

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event




}
