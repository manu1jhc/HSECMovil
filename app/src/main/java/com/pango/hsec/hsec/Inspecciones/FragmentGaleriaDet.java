package com.pango.hsec.hsec.Inspecciones;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGaleriaDet extends Fragment implements IActivity {

    private View mView;
    String codInsp;
    String url;

    public FragmentGaleriaDet() {
        // Required empty public constructor
    }
    public static FragmentGaleriaDet newInstance(String sampleText) {
        FragmentGaleriaDet fragment = new FragmentGaleriaDet();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_fragment_galeria_det, container, false);
        codInsp=getArguments().getString("bString");

        url= GlobalVariables.Url_base+"media/GetMultimedia/"+codInsp;



        return mView;
    }




    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
