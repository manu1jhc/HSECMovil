package com.pango.hsec.hsec.Observaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;

public class FragmentObsDet extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    String jsonObsDet="";
    private View mView;
    String codObs;
    public static FragmentObsDet newInstance(String sampleText) {
        FragmentObsDet f = new FragmentObsDet();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_det, container, false);
        // Inflate the layout for this fragment


        return mView;
    }



}
