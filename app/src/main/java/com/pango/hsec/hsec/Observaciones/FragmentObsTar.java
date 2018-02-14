package com.pango.hsec.hsec.Observaciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;


public class FragmentObsTar extends Fragment {
    String jsonObsTar="";
    private View mView;
    String codObs;


    // TODO: Rename and change types and number of parameters
    public static FragmentObsTar newInstance(String sampleText) {
        FragmentObsTar f = new FragmentObsTar();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_obs_tar, container, false);

        return mView;
    }


}
