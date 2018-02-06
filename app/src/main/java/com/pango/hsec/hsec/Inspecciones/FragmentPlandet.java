package com.pango.hsec.hsec.Inspecciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pango.hsec.hsec.R;

public class FragmentPlandet extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View mView;
    String codInsp;
    String url;


    public FragmentPlandet() {
        // Required empty public constructor
    }

    public static FragmentPlandet newInstance(String sampleText) {
        FragmentPlandet fragment = new FragmentPlandet();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_fragment_plandet, container, false);
        codInsp=getArguments().getString("bString");


        return mView;

    }

}
