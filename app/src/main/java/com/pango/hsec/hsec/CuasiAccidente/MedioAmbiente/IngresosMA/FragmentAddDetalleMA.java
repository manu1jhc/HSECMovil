package com.pango.hsec.hsec.CuasiAccidente.MedioAmbiente.IngresosMA;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pango.hsec.hsec.Busquedas.B_contrata;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddDetalleMA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddDetalleMA extends Fragment implements IActivity {
    String codIncMA;

    private View mView;
    Spinner spinner_titulo, spinner_turno;
    EditText edit_titulo_det, edit_des_suceso, edit_acciones;
    TextView tx_contrata;
    ImageButton btn_buscar_c;

    public static final int REQUEST_CODE = 1;

    public FragmentAddDetalleMA() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FragmentAddDetalleMA.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddDetalleMA newInstance(String sampleText) {
        FragmentAddDetalleMA fragment = new FragmentAddDetalleMA();
        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_detalle_ma, container, false);
        codIncMA=getArguments().getString("bString");

        spinner_titulo = (Spinner) mView.findViewById(R.id.spinner_titulo);
        spinner_turno = (Spinner) mView.findViewById(R.id.spinner_turno);

        edit_titulo_det = mView.findViewById(R.id.edit_titulo_det);
        edit_des_suceso = mView.findViewById(R.id.edit_des_suceso);
        edit_acciones = mView.findViewById(R.id.edit_acciones);
        btn_buscar_c = mView.findViewById(R.id.btn_buscar_c);
        tx_contrata = mView.findViewById(R.id.tx_contrata);

        ArrayAdapter adapterTitulo = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.TituIncidente);
        adapterTitulo.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_titulo.setAdapter(adapterTitulo);

        ArrayAdapter adapterTurno = new ArrayAdapter(getContext(),R.layout.custom_spinner_item, GlobalVariables.Turno);
        adapterTurno.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinner_turno.setAdapter(adapterTurno);

        btn_buscar_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), B_contrata.class);
                startActivityForResult(intent , REQUEST_CODE);

            }
        });

        return mView;
    }

    @Override
    public void success(String data, String Tipo){

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String tipo_dato=data.getStringExtra("tipo");

                String cod_contrata = data.getStringExtra("codContrata");
                String des_contrata = data.getStringExtra("desContrata");
                tx_contrata.setText(des_contrata);

                //GlobalVariables.AddIncidenteSeg.CodContrata=cod_contrata;
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}