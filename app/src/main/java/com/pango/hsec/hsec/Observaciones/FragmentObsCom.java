package com.pango.hsec.hsec.Observaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;

public class FragmentObsCom extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    String jsonObsCom="";
    private View mView;
    String codObs;
    // TODO: Rename and change types and number of parameters
    public static FragmentObsCom newInstance(String sampleText) {
        FragmentObsCom f = new FragmentObsCom();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_obs_com, container, false);



        return mView;
    }



}
