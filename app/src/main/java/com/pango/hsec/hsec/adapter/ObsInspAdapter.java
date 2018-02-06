package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.ObsInspModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;

/**
 * Created by Andre on 02/02/2018.
 */

public class ObsInspAdapter extends ArrayAdapter<ObsInspModel> {

    private Context context;
    ArrayList<ObsInspModel> obsInspModel=new ArrayList<ObsInspModel>();


    public ObsInspAdapter(Context context, ArrayList<ObsInspModel> obsInspModel) {
        super(context, R.layout.public_obsinsp, obsInspModel);
        this.context = context;
        this.obsInspModel=obsInspModel;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.public_obsinsp, parent, false);

        TextView tx_obs=convertView.findViewById(R.id.tx_obs);
        ImageView nivel=convertView.findViewById(R.id.tx_nivel);

        final String tempRiesgo = obsInspModel.get(position).CodNivelRiesgo;


        tx_obs.setText(obsInspModel.get(position).Observacion);



        if (tempRiesgo.equals("BA")) {
            //riesgo.setCardBackgroundColor(Color.GREEN);
            nivel.setImageResource(R.drawable.green_light);

        } else if (tempRiesgo.equals("ME")) {
            //riesgo.setCardBackgroundColor(Color.YELLOW);
            nivel.setImageResource(R.drawable.yellow_light);

        } else {
            //riesgo.setCardBackgroundColor(Color.RED);
            nivel.setImageResource(R.drawable.red_light);

        }


        return convertView;
    }
}
