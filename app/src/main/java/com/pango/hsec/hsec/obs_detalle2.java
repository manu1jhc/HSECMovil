package com.pango.hsec.hsec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.R;

public class obs_detalle2 extends Fragment {

    private static View mView;

    public static final com.pango.hsec.hsec.obs_detalle2 newInstance(String sampleText) {
        obs_detalle2 f = new obs_detalle2();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_obs_detalle2,
                container, false);


        String sampleText = getArguments().getString("bString");



        TextView txtSampleText = (TextView) mView.findViewById(R.id.txtViewSample);
        txtSampleText.setText(sampleText);
        Button button=(Button) mView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Click en publicacion",Toast.LENGTH_SHORT).show();

            }
        });
        return mView;
    }
}