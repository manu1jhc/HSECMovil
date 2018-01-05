package com.pango.hsec.hsec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BOB on 29/12/2017.
 */

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    final Calendar calendar = Calendar.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, yy, mm, dd);
        if(GlobalVariables.CodRol!=1){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm, dd);
    }
    public void populateSetDate(int year, int month, int day) {
       // Toast.makeText(getActivity(),day+"/"+month+"/"+year,Toast.LENGTH_SHORT).show();
        Button botonFecha=(Button)getActivity().findViewById(R.id.btn_fecha);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date actual = calendar.getTime();
        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        botonFecha.setText(dt.format(actual));
    }

}