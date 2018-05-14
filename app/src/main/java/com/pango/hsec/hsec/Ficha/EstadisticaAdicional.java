package com.pango.hsec.hsec.Ficha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.AdicionalAdapter;

public class EstadisticaAdicional extends AppCompatActivity {
    TextView tx_ref,tx_autor,tx_fecha,tx_descripcion,tx_titulo;
    AdicionalAdapter adicionalAdapter;
    int position;
    String descripcion="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica_adicional);
        Bundle datos = getIntent().getExtras();
        position=datos.getInt("position");
        descripcion=datos.getString("descripcion");

        tx_titulo=findViewById(R.id.tx_titulo);

        tx_ref=findViewById(R.id.tx_ref);
        tx_autor=findViewById(R.id.tx_autor);
        tx_fecha=findViewById(R.id.tx_fecha);
        tx_descripcion=findViewById(R.id.tx_descripcion);
//getEstadisticaDetModel
        final RecyclerView rec_adicional = (RecyclerView) findViewById(R.id.rec_adicional);
        rec_adicional.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rec_adicional.setLayoutManager(llm);
        rec_adicional.setFocusableInTouchMode(false);

        tx_ref.setText(GlobalVariables.dataAdicional.get(position).NroDocReferencia);
        tx_descripcion.setText(GlobalVariables.dataAdicional.get(position).Descripcion);

        if (GlobalVariables.dataAdicional.get(position).Responsable==null){
            tx_autor.setText(GlobalVariables.userLoaded.Nombres);
        }else {
            tx_autor.setText(GlobalVariables.dataAdicional.get(position).Responsable);
        }

        tx_fecha.setText(Utils.Obtenerfecha(GlobalVariables.dataAdicional.get(position).Fecha));
        tx_titulo.setText(descripcion);

        String[] DatosAd = GlobalVariables.dataAdicional.get(position).DatosAdicionales.split(";");

        adicionalAdapter = new AdicionalAdapter(DatosAd);
        rec_adicional.setAdapter(adicionalAdapter);

    }


    public void close(View view){
        finish();
    }


}
